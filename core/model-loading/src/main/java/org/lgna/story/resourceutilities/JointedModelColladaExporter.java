/*******************************************************************************
 * Copyright (c) 2006, 2018, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.story.resourceutilities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import org.lgna.common.resources.ImageResource;
import org.lgna.story.implementation.ImageFactory;
import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.biped.AliceResource;
import org.lgna.story.resources.prop.SledResource;
import org.lgna.story.resourceutilities.exporterutils.collada.*;
import org.lgna.story.resourceutilities.exporterutils.collada.Asset.Unit;
import org.lgna.story.resourceutilities.exporterutils.collada.COLLADA.Scene;
import org.lgna.story.resourceutilities.exporterutils.collada.Geometry;
import org.lgna.story.resourceutilities.exporterutils.collada.Image;
import org.lgna.story.resourceutilities.exporterutils.collada.Mesh;
import org.lgna.story.resourceutilities.exporterutils.collada.Skin.VertexWeights;
import org.lgna.story.resourceutilities.exporterutils.collada.Source.TechniqueCommon;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dave Culyba
 */
public class JointedModelColladaExporter {

	private static final String COLLADA_EXTENSION = "dae";
	private static final String IMAGE_EXTENSION = "png";

	//Controls whether or not we flip the coordinate space during our conversion
	private static final boolean FLIP_COORDINATE_SPACE = true;
	private static final boolean SCALE_MODEL = true;
	private static final double MODEL_SCALE = 10.0;

	private final ObjectFactory factory;
	private final SkeletonVisual visual;
	private final ModelResourceInfo modelInfo;
	private final String modelName;
//	private final Map<Integer, File> idToTextureFileMap;

	private final HashMap<edu.cmu.cs.dennisc.scenegraph.Geometry, String> meshNameMap = new HashMap<>();
	private final HashMap<Integer, String> textureNameMap = new HashMap<>();

	private JointedModelColladaExporter( SkeletonVisual sv, ModelResourceInfo mi, String modelName ) {
		this.factory = new ObjectFactory();
		this.visual = sv;
		this.modelInfo = mi;
		this.modelName = modelName;
		//Go through all the meshes in the sgVisual and find or create names for all of them
		initializeMeshNameMap();
		//Go through all the textures and create names based on the IDs
		initializeTextureNameMap();
	}

	public JointedModelColladaExporter( SkeletonVisual sv, ModelResourceInfo mi ) {
		this( sv, mi, null );
	}

	public JointedModelColladaExporter( SkeletonVisual sv, String modelName ) {
		this( sv, null, modelName );
	}

	public String getImageExtension() {
		return IMAGE_EXTENSION;
	}

	public String getModelExtension() {
		return COLLADA_EXTENSION;
	}

	private Asset createAsset() {
		Asset asset = factory.createAsset();

		ZonedDateTime ct = ZonedDateTime.now();
		int zoneOffsetMinutes = ct.getOffset().getTotalSeconds() / 60;
		XMLGregorianCalendar createdDateTime;
		try {
			createdDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar( ct.getYear(), ct.getMonth().getValue(), ct.getDayOfMonth(), ct.getHour(), ct.getMinute(), ct.getSecond(), 0, zoneOffsetMinutes );
			asset.setCreated( createdDateTime );
			asset.setModified( createdDateTime );
		} catch( DatatypeConfigurationException e ) {
			e.printStackTrace();
		}

		Unit unit = factory.createAssetUnit();
		unit.setMeter( 1.0 );
		unit.setName( "meter" );
		asset.setUnit( unit );
		asset.setUpAxis( UpAxisType.Y_UP );

		return asset;
	}

	private Node createNodeForJoint( Joint joint ) {
		Node node = factory.createNode();
		node.setName( joint.jointID.getValue() );
		node.setId( joint.jointID.getValue() );
		node.setSid( joint.jointID.getValue() );
		node.setType( NodeType.JOINT );

		Matrix matrix = factory.createMatrix();
		matrix.setSid( "matrix" );
		AffineMatrix4x4 newTransform = new AffineMatrix4x4( joint.localTransformation.getValue() );
		if (SCALE_MODEL) {
			newTransform.translation.multiply(MODEL_SCALE);
		}
		double[] matrixValues = ColladaTransformUtilities.createColladaTransformFromAliceTransform(newTransform);
		//Flip the values from Alice space to Collada space
		if (FLIP_COORDINATE_SPACE) {
			matrixValues = ColladaTransformUtilities.createFlippedColladaTransform(matrixValues);
		}
		for( double matrixValue : matrixValues ) {
			matrix.getValue().add( matrixValue );
		}

		node.getLookatOrMatrixOrRotate().add( matrix );

		for( Component c : joint.getComponents() ) {
			if( c instanceof Joint ) {
				Node childNode = createNodeForJoint( (Joint)c );
				node.getNode().add( childNode );
			}
		}

		return node;
	}

	private Node createSkeletonNodes() {
		Joint rootJoint = visual.skeleton.getValue();
		if( rootJoint != null ) {
			return createNodeForJoint( visual.skeleton.getValue() );
		}
		return null;
	}

	private Param createParam( String name, String type ) {
		Param param = factory.createParam();
		if( name != null ) {
			param.setName( name );
		}
		param.setType( type );
		return param;
	}

	private Accessor createAccessorForArray( String arrayId, String arrayType, int arraySize, int stride ) {
		Accessor accessor = factory.createAccessor();
		accessor.setSource( "#" + arrayId );
		if( stride > 1 ) {
			accessor.setStride( BigInteger.valueOf( stride ) );
		}
		accessor.setCount( BigInteger.valueOf( arraySize / stride ) );

		if( stride == 3 ) {
			accessor.getParam().add( createParam( "X", arrayType ) );
			accessor.getParam().add( createParam( "Y", arrayType ) );
			accessor.getParam().add( createParam( "Z", arrayType ) );
		} else if( stride == 2 ) {
			accessor.getParam().add( createParam( "S", arrayType ) );
			accessor.getParam().add( createParam( "T", arrayType ) );
		} else { //Stride of 1 or below is used to create a parameter that just specifies type
			accessor.getParam().add( createParam( null, arrayType ) );
		}

		return accessor;
	}

	private TechniqueCommon createTechniqueCommonForArray( String arrayId, String arrayType, int arraySize, int stride ) {
		TechniqueCommon accessorTechnique = factory.createSourceTechniqueCommon();
		accessorTechnique.setAccessor( createAccessorForArray( arrayId, arrayType, arraySize, stride ) );
		return accessorTechnique;
	}


	//Helper classes to initalize Collada lists of numbers from Alice data
	//These are used to convert the lists of doubles and floats that represent normals, vertices, etc.
	private interface ListInitializer {
		public void initializeList(List<Double> toInitialize);
	}

	private class DoubleArrayInitializeList implements ListInitializer {
		private final double[] da;

		public DoubleArrayInitializeList( double[] da ) {
			this.da = da;
		}

		@Override
		public void initializeList( List<Double> toInitialize ) {
			for( double element : da ) {
				toInitialize.add( element );
			}
		}
	}

	private class DoubleListInitializeList implements ListInitializer {
		private final List<Double> dl;

		public DoubleListInitializeList( List<Double> dl ) {
			this.dl = dl;
		}

		@Override
		public void initializeList( List<Double> toInitialize ) {
			for( int i = 0; i < dl.size(); i++ ) {
				toInitialize.add( dl.get( i ) );
			}
		}
	}

	private class FloatListInitializeList implements ListInitializer {
		private final List<Float> fl;

		public FloatListInitializeList( List<Float> fl ) {
			this.fl = fl;
		}

		@Override
		public void initializeList( List<Double> toInitialize ) {
			for( int i = 0; i < fl.size(); i++ ) {
				toInitialize.add( fl.get( i ).doubleValue() );
			}
		}
	}

	private class DoubleBufferInitializeList implements ListInitializer {
		private final DoubleBuffer db;

		public DoubleBufferInitializeList( DoubleBuffer db ) {
			this.db = db;
		}

		@Override
		public void initializeList( List<Double> toInitialize ) {
			db.rewind();
			final int N = db.limit();
			for( int i = 0; i < N; i++ ) {
				toInitialize.add( db.get( i ) );
			}
		}
	}

	private class FloatBufferInitializeList implements ListInitializer {
		private final FloatBuffer fb;

		public FloatBufferInitializeList( FloatBuffer fb ) {
			this.fb = fb;
		}

		@Override
		public void initializeList( List<Double> toInitialize ) {
			fb.rewind();
			final int N = fb.limit();
			for( int i = 0; i < N; i++ ) {
				toInitialize.add( Double.valueOf( fb.get( i ) ) );
			}
		}
	}

	private Source createFloatArraySourceFromInitializer( ListInitializer initializer, String name, int stride, double scale, boolean flipXandZ ) {

		Source source = factory.createSource();
		source.setId( name );

		//Build the array of vertices (called "position" in the collada file)
		FloatArray floatArray = factory.createFloatArray();
		floatArray.setId( name + "-array" );
		List<Double> values = floatArray.getValue();
		initializer.initializeList( values );

		//Flip X and Z as part of flipping the normals and positions from Alice space to Collada space
		if (flipXandZ || scale != 1.0) {
			//Combine whether or not we're flipping with the scale value so we can do it all in one multiply
			double flipScale = flipXandZ ? scale * -1.0 : scale;
			for (int i=0; i<values.size(); i+=3) {
				values.set(i, values.get(i) * flipScale); //Only flip X and Z
				values.set(i+1, values.get(i+1) * scale);
				values.set(i+2, values.get(i+2) * flipScale); //Only flip X and Z
			}
		}

		int valueCount = values.size();
		floatArray.setCount( BigInteger.valueOf( valueCount ) );

		//Build the accessor
		TechniqueCommon accessorTechnique = createTechniqueCommonForArray( floatArray.getId(), "float", floatArray.getValue().size(), stride );
		source.setFloatArray( floatArray );
		source.setTechniqueCommon( accessorTechnique );

		return source;
	}

	//Mesh name is used both in library_geometries and in library_controllers
	private String getMeshIdForMeshName( String meshName ) {
		return meshName + "-id";
	}

	private Triangles createTriangles(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh, String verticesName, String normalsName, String UVsName) {
		//Build the triangle data
		Triangles triangles = factory.createTriangles();
		InputLocalOffset vertexInput = factory.createInputLocalOffset();
		vertexInput.setSemantic( "VERTEX" );
		vertexInput.setSource( "#" + verticesName );
		vertexInput.setOffset( BigInteger.valueOf( 0 ) );
		triangles.getInput().add( vertexInput );

		InputLocalOffset normalInput = factory.createInputLocalOffset();
		normalInput.setSemantic( "NORMAL" );
		normalInput.setSource( "#" + normalsName );
		normalInput.setOffset( BigInteger.valueOf( 1 ) );
		triangles.getInput().add( normalInput );

		InputLocalOffset texCoordInput = factory.createInputLocalOffset();
		texCoordInput.setSemantic( "TEXCOORD" );
		texCoordInput.setSource( "#" + UVsName );
		texCoordInput.setOffset( BigInteger.valueOf( 2 ) );
		texCoordInput.setSet( BigInteger.valueOf( 0 ) );
		triangles.getInput().add( texCoordInput );
		//Copy over the triangle indices
		List<BigInteger> triangleList = triangles.getP();
		IntBuffer ib = sgMesh.indexBuffer.getValue();
		final int N = sgMesh.indexBuffer.getValue().limit();
		for( int i = 0; i < N; i+=3 ) {
			//Reverse the order of the triangle indices because collada needs the indices in counter clockwise order
			triangleList.add( BigInteger.valueOf( ib.get( i+0 ) ) ); //Position 0
			triangleList.add( BigInteger.valueOf( ib.get( i+0 ) ) ); //Normal 0
			triangleList.add( BigInteger.valueOf( ib.get( i+0 ) ) ); //UV 0

			triangleList.add( BigInteger.valueOf( ib.get( i+1 ) ) ); //Position 1
			triangleList.add( BigInteger.valueOf( ib.get( i+1 ) ) ); //Normal 1
			triangleList.add( BigInteger.valueOf( ib.get( i+1 ) ) ); //UV 1

			triangleList.add( BigInteger.valueOf( ib.get( i+2 ) ) ); //Position 2
			triangleList.add( BigInteger.valueOf( ib.get( i+2 ) ) ); //Normal 2
			triangleList.add( BigInteger.valueOf( ib.get( i+2 ) ) ); //UV 2
		}
		triangles.setCount( BigInteger.valueOf( N / 3 ) );

		return triangles;
	}

	private Vertices createVertices(String verticesName, String positionsName) {
		Vertices vertices = factory.createVertices();
		vertices.setId( verticesName );
		//Vertices have two inputs: POSITION and NORMAL
		//These are linked via a string with the prefix "#" to the source names
		InputLocal positionInput = factory.createInputLocal();
		positionInput.setSemantic( "POSITION" );
		positionInput.setSource( "#" + positionsName );
		vertices.getInput().add( positionInput );
		return vertices;
	}

	private Geometry createGeometryForMesh( edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh ) {
		//Geometry consists of a Geometry node with a single Mesh node inside
		Geometry geometry = factory.createGeometry();

		//Top level Geometry setup
		String meshName = meshNameMap.get( sgMesh );
		String meshIdName = getMeshIdForMeshName( meshName );
		geometry.setId( meshIdName );
		geometry.setName( meshName+"mesh" );

		//Set up the Mesh node
		Mesh mesh = factory.createMesh();
		//Build the Position source. This will hold the array of vertices and a accessor that tells the file the array is of stride 3
		String positionName = meshName + "-POSITION";
		double scale = SCALE_MODEL ? MODEL_SCALE : 1.0;
		//Pass flipXandZ = TRUE to flip Alice coordinate space to Collada coordinate space
		//Only pass scale to position. Normals and UVs should not be scaled
		Source positionSource = createFloatArraySourceFromInitializer( new DoubleBufferInitializeList( sgMesh.vertexBuffer.getValue() ), positionName, 3, scale, FLIP_COORDINATE_SPACE );
		mesh.getSource().add( positionSource );

		String normalName = meshName + "-NORMAL";
		//Pass flipXandZ = TRUE to flip Alice coordinate space to Collada coordinate space
		Source normalSource = createFloatArraySourceFromInitializer( new FloatBufferInitializeList( sgMesh.normalBuffer.getValue() ), normalName, 3, 1.0, FLIP_COORDINATE_SPACE );
		mesh.getSource().add( normalSource );

		String uvName = meshName + "-UV";
		Source uvSource = createFloatArraySourceFromInitializer( new FloatBufferInitializeList( sgMesh.textCoordBuffer.getValue() ), uvName, 2, 1.0, false );
		mesh.getSource().add( uvSource );
		//Create and add vertices
		String vertexName = meshName + "-VERTEX";
		Vertices vertices = createVertices(vertexName, positionName);
		mesh.setVertices( vertices );
		//Create and add the triangles
		Triangles triangles = createTriangles(sgMesh, vertexName, normalName, uvName);
		//Grab the texture ID number from the mesh and use that to make the material reference
		Integer textureID = sgMesh.textureId.getValue();
		triangles.setMaterial(getMaterialIDForID(textureID));
		mesh.getLinesOrLinestripsOrPolygons().add( triangles );

		geometry.setMesh( mesh );

		return geometry;
	}

	private double[] getBindShapeMatrix( WeightedMesh sgWeightedMesh ) {
		//All Alice models have a bind shape of the identity
		AffineMatrix4x4 bindShapeMatrix = AffineMatrix4x4.createIdentity();
		return ColladaTransformUtilities.createColladaTransformFromAliceTransform( bindShapeMatrix );
	}

	//	Here is an example of a more complete <vertex_weights> element. Note that the <vcount> element
	//	says that the first vertex has 3 bones, the second has 2, etc. Also, the <v> element says that the first
	//	vertex is weighted with weights[0] towards the bind shape, weights[1] towards bone 0, and
	//	weights[2] towards bone 1:
	//	<skin>
	//	 <source id="joints"/>
	//	 <source id="weights"/>
	//	 <vertex_weights count="4">
	//	 	<input semantic="JOINT" source="#joints"/>
	//	 	<input semantic="WEIGHT" source="#weights"/>
	//	 	<vcount>3 2 2 3</vcount>
	//	 	<v>
	//	 		-1 0 0 1 1 2
	//	 		-1 3 1 4
	//	 		-1 3 2 4
	//	 		-1 0 3 1 2 2
	//	 	</v>
	//	 </vertex_weights>
	//	</skin>
	private class ColladaSkinWeights {
		public final List<Integer> weightIndices = new ArrayList<Integer>();
		public final List<Integer> jointIndices = new ArrayList<Integer>();

	}

	private Source createJointSource(WeightInfo wi, String jointSourceName) {
		Source jointSource = factory.createSource();
		jointSource.setId( jointSourceName );
		NameArray jointNameArray = factory.createNameArray();
		jointNameArray.setId( jointSourceName+"-array" );
		for( Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet() ) {
			jointNameArray.getValue().add( entry.getKey() );
		}
		jointNameArray.setCount( BigInteger.valueOf( jointNameArray.getValue().size() ) );
		jointSource.setNameArray( jointNameArray );
		jointSource.setTechniqueCommon( createTechniqueCommonForArray( jointNameArray.getId(), "name", jointNameArray.getValue().size(), 1 ) );
		return jointSource;
	}

	private Source createMatrixSource(WeightInfo wi, String matricesSourceName) {
		//Build the Matrix Source
		Source matricesSource = factory.createSource();
		matricesSource.setId( matricesSourceName );
		FloatArray matricesArray = factory.createFloatArray();
		matricesArray.setId( matricesSourceName+"-array" );
		for( Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet() ) {
			InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
			AffineMatrix4x4 inverseBindMatrix = iatwp.getInverseAbsoluteTransformation();
			if (SCALE_MODEL) {
				inverseBindMatrix.translation.multiply(MODEL_SCALE);
			}
			//Invert matrix, flip its values from Alice space to Collada space and then invert it back
			double[] matrix = ColladaTransformUtilities.createColladaTransformFromAliceTransform(inverseBindMatrix);
			if (FLIP_COORDINATE_SPACE) {
				matrix = ColladaTransformUtilities.createFlippedColladaTransform(matrix);
			}
			for( double element : matrix ) {
				matricesArray.getValue().add( element );
			}
		}
		int matrixCount = matricesArray.getValue().size();
		matricesArray.setCount( BigInteger.valueOf( matrixCount ) );
		matricesSource.setFloatArray( matricesArray );
		matricesSource.setTechniqueCommon( createTechniqueCommonForArray( matricesArray.getId(), "float4x4", matrixCount, 16 ) );

		return matricesSource;
	}

	private Controller createControllerForMesh( WeightedMesh sgWeightedMesh ) {
		Controller controller = factory.createController();
		String meshName = meshNameMap.get( sgWeightedMesh );
		String controllerName = meshName + "Controller";
		controller.setId( controllerName );

		Skin skin = factory.createSkin();
		controller.setSkin( skin );
		skin.setSourceAttribute( "#" + getMeshIdForMeshName( meshName ) );

		//Set the bind shape matrix
		double[] bindShapeMatrix = getBindShapeMatrix( sgWeightedMesh );
		for( double element : bindShapeMatrix ) {
			skin.getBindShapeMatrix().add( element );
		}

		WeightInfo wi = sgWeightedMesh.weightInfo.getValue();

		//Build the Joint Source
		String jointSourceName = controllerName + "-Joints";
		Source jointSource = createJointSource(wi, jointSourceName);
		skin.getSourceAttribute2().add( jointSource );

		String matricesSourceName = controllerName + "-Matrices";
		Source matricesSource = createMatrixSource(wi, matricesSourceName);
		skin.getSourceAttribute2().add( matricesSource );

		//Build the VertexWeights
		int vertexCount = sgWeightedMesh.vertexBuffer.getValue().limit() / 3;
		VertexWeights vw = factory.createSkinVertexWeights();
		vw.setCount( BigInteger.valueOf( vertexCount ) );
		ColladaSkinWeights[] skinWeights = new ColladaSkinWeights[ vertexCount ];
		List<Float> weightArray = new ArrayList<Float>();
		int jointIndex = 0;
		//Iterate through the WeightInfo and get the joint indices and weight indices
		//ColladaSkinWeights is an array that uses a vertex index to index to a ColladaSkinWeights
		//The ColladaSkinWeights object contains the joint indices the vertex is mapped to and the weight indices for those joints
		for( Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet() ) {
			InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
			iatwp.reset();
			while( !iatwp.isDone() ) {
				int vertexIndex = iatwp.getIndex();
				float weight = iatwp.getWeight();
				weightArray.add( weight );
				if( skinWeights[ vertexIndex ] == null ) {
					skinWeights[ vertexIndex ] = new ColladaSkinWeights();
				}
				ColladaSkinWeights vertexWeights = skinWeights[ vertexIndex ];
				vertexWeights.jointIndices.add( jointIndex );
				vertexWeights.weightIndices.add( weightArray.size() - 1 );

				iatwp.advance();
			}
			jointIndex++;
		}
		//Add the ColladaSkinWeights to the VertexWeights structure
		for( ColladaSkinWeights vertexWeights : skinWeights ) {
			vw.getVcount().add( BigInteger.valueOf( vertexWeights.jointIndices.size() ) );
			for( int j = 0; j < vertexWeights.jointIndices.size(); j++ ) {
				vw.getV().add( vertexWeights.jointIndices.get( j ).longValue() );
				vw.getV().add( vertexWeights.weightIndices.get( j ).longValue() );
			}
		}

		//Build the weights source.
		String weightsSourceName = controllerName + "-Weights";
		Source weightsSource = createFloatArraySourceFromInitializer( new FloatListInitializeList( weightArray ), weightsSourceName, 1, 1.0,false );
		skin.getSourceAttribute2().add( weightsSource );

		InputLocalOffset jointInput = factory.createInputLocalOffset();
		jointInput.setSemantic( "JOINT" );
		jointInput.setSource( "#" + jointSourceName );
		jointInput.setOffset( BigInteger.valueOf( 0 ) );
		vw.getInput().add( jointInput );

		InputLocalOffset weightInput = factory.createInputLocalOffset();
		weightInput.setSemantic( "WEIGHT" );
		weightInput.setSource( "#" + weightsSourceName );
		weightInput.setOffset( BigInteger.valueOf( 1 ) );
		vw.getInput().add( weightInput );

		//Create and add Joints object
		Skin.Joints joints = factory.createSkinJoints();
		InputLocal localJointInput = factory.createInputLocal();
		localJointInput.setSemantic( "JOINT" );
		localJointInput.setSource( "#" + jointSourceName );
		joints.getInput().add(localJointInput);

		InputLocal localInvBindMatrixInput = factory.createInputLocal();
		localInvBindMatrixInput.setSemantic( "INV_BIND_MATRIX" );
		localInvBindMatrixInput.setSource( "#" + matricesSourceName );
		joints.getInput().add(localInvBindMatrixInput);

		skin.setJoints(joints);
		skin.setVertexWeights( vw );

		return controller;
	}

	private void addMeshToNameMap( edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh ) {
		String meshName = sgMesh.getName();
		if( ( meshName == null ) || ( meshName.length() == 0 ) ) {
			meshName = "mesh" + meshNameMap.size();
		}
		meshNameMap.put( sgMesh, meshName );
	}

	private void initializeMeshNameMap() {
		meshNameMap.clear();
		for( edu.cmu.cs.dennisc.scenegraph.Geometry g : visual.geometries.getValue() ) {
			if( g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
				addMeshToNameMap( (edu.cmu.cs.dennisc.scenegraph.Mesh)g );
			}
		}
		for( WeightedMesh wm : visual.weightedMeshes.getValue() ) {
			addMeshToNameMap( wm );
		}
	}

	private void initializeTextureNameMap() {
		textureNameMap.clear();
		//Make names for all the texture IDs
		//There's no naming convention. Naming is geared toward human readability--texture_0 is fine
		for( TexturedAppearance texture : visual.textures.getValue()) {
			textureNameMap.put(texture.textureId.getValue(), "texture_"+texture.textureId.getValue());
		}
	}

	private String getImageNameForID(Integer id) {
		return textureNameMap.get(id) + "_diffuseMap";
	}

	//Image file name must be unique to the model.
	//We don't know if models share textures, so we must construct a unique file name to avoid collisions
	//To support sharing textures, we will need to have a way to connect the texture id (a number) to a unique filename
	private String getExternallyUniqueImageNameForID(Integer id) { return getFullResourceName() + "_" + getImageNameForID(id); }

	private String getImageFileNameForID(Integer id) { return getExternallyUniqueImageNameForID(id) + "."+IMAGE_EXTENSION; }

	private String getImageIDForID(Integer id) {
		return getImageNameForID(id) + "-image";
	}

	private String getMaterialNameForID(Integer id) {
		return textureNameMap.get(id) + "_shader";
	}

	private String getMaterialIDForID(Integer id) {
		return getMaterialNameForID(id);
	}

	private String getEffectNameForID(Integer id) { return textureNameMap.get(id) + "_fx"; }

	private String getEffectIDForID(Integer id) { return getEffectNameForID(id); }

	private BindMaterial createBindMaterialForTextureID( Integer textureID ) {
		BindMaterial bindMaterial = factory.createBindMaterial();

		BindMaterial.TechniqueCommon techniqueCommon = factory.createBindMaterialTechniqueCommon();
		InstanceMaterial instanceMaterial = factory.createInstanceMaterial();
		techniqueCommon.getInstanceMaterial().add(instanceMaterial);
		instanceMaterial.setSymbol(getMaterialNameForID(textureID));
		instanceMaterial.setTarget("#"+ getMaterialIDForID(textureID));
		InstanceMaterial.BindVertexInput bindVertexInput = factory.createInstanceMaterialBindVertexInput();
		instanceMaterial.getBindVertexInput().add(bindVertexInput);
		bindVertexInput.setSemantic("UVMap");
		bindVertexInput.setInputSet(BigInteger.ZERO);
		bindVertexInput.setInputSemantic("TEXCOORD");

		bindMaterial.setTechniqueCommon(techniqueCommon);
		return bindMaterial;
	}

	private Node createVisualSceneNode( String name ) {
		Node visualSceneNode = factory.createNode();
		visualSceneNode.setName(name);
		visualSceneNode.setId(name);
		visualSceneNode.setSid(name);
		return visualSceneNode;
	}

	private Node createVisualSceneNodeForMesh(edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh) {
		String meshName = meshNameMap.get( sgMesh );
		String geometryURL = "#"+ getMeshIdForMeshName( meshName );

		Node visualSceneNode = createVisualSceneNode(meshName);
		InstanceGeometry instanceGeometry = factory.createInstanceGeometry();
		instanceGeometry.setUrl(geometryURL);
		instanceGeometry.setBindMaterial(createBindMaterialForTextureID(sgMesh.textureId.getValue()));
		visualSceneNode.getInstanceGeometry().add(instanceGeometry);

		return visualSceneNode;
	}

	private Node createVisualSceneNodeForWeightedMesh(WeightedMesh sgWeightedMesh) {
		String meshName = meshNameMap.get( sgWeightedMesh );
		String controllerURL = "#"+ meshName + "Controller";

		Node visualSceneNode = createVisualSceneNode(meshName);
		InstanceController instanceController = factory.createInstanceController();
		instanceController.setUrl(controllerURL);
		instanceController.setBindMaterial(createBindMaterialForTextureID(sgWeightedMesh.textureId.getValue()));
		visualSceneNode.getInstanceController().add(instanceController);

		return visualSceneNode;
	}

	private CommonColorOrTextureType.Color createCommonColor( String sid, double r, double g, double b, double a) {
		CommonColorOrTextureType.Color color = factory.createCommonColorOrTextureTypeColor();
		color.setSid(sid);
		color.getValue().add(r);
		color.getValue().add(g);
		color.getValue().add(b);
		color.getValue().add(a);
		return color;
	}

	private CommonColorOrTextureType createCommonColorType( String sid, double r, double g, double b, double a ) {
		CommonColorOrTextureType colorType = factory.createCommonColorOrTextureType();
		colorType.setColor( createCommonColor("emission", 0, 0, 0, 1) );
		return colorType;
	}

	private Effect createEffect(Integer textureID) {
		Effect effect = factory.createEffect();
		effect.setId( getEffectIDForID(textureID) );
		effect.setName( getEffectNameForID(textureID)) ;

		ProfileCOMMON profile = factory.createProfileCOMMON();

		//Create the surface param
		CommonNewparamType surfaceParam = factory.createCommonNewparamType();
		surfaceParam.setSid(getImageNameForID(textureID)+"-surface");
		FxSurfaceCommon surface = factory.createFxSurfaceCommon();
		surface.setType("2D");
		FxSurfaceInitFromCommon surfaceInit = factory.createFxSurfaceInitFromCommon();
		//This "setValue" needs something that has an ID.
		//We're using the same pattern we used for making the Image entries for the library_images section
		Image image = factory.createImage();
		image.setName(getImageNameForID(textureID));
		image.setId(getImageIDForID(textureID));
		surfaceInit.setValue(image);

		surface.getInitFrom().add(surfaceInit);
		surfaceParam.setSurface(surface);
		profile.getImageOrNewparam().add(surfaceParam);

		//Create the sampler param
		CommonNewparamType samplerParam = factory.createCommonNewparamType();
		samplerParam.setSid(getImageNameForID(textureID)+"-sampler");
		FxSampler2DCommon sampler = factory.createFxSampler2DCommon();
		sampler.setSource(getImageNameForID(textureID)+"-surface");
		samplerParam.setSampler2D(sampler);
		profile.getImageOrNewparam().add(samplerParam);

		ProfileCOMMON.Technique technique = factory.createProfileCOMMONTechnique();
		technique.setSid("standard");

		ProfileCOMMON.Technique.Lambert lambert = factory.createProfileCOMMONTechniqueLambert();
		lambert.setEmission( createCommonColorType("emission", 0, 0, 0, 1) );
		lambert.setAmbient( createCommonColorType("ambient", 0, 0, 0, 1) );
		CommonColorOrTextureType diffuse = factory.createCommonColorOrTextureType();
		CommonColorOrTextureType.Texture texture = factory.createCommonColorOrTextureTypeTexture();
		texture.setTexture( getImageNameForID(textureID)+"-sampler" );
		texture.setTexcoord( "UVMap" ); //Based on generated example collada file
		diffuse.setTexture(texture);
		lambert.setDiffuse(diffuse);
		technique.setLambert(lambert);
		profile.setTechnique(technique);
		JAXBElement<ProfileCOMMON> profileCOMMONJAXBElement = new JAXBElement<ProfileCOMMON>(new QName("http://www.collada.org/2005/11/COLLADASchema", "profile_COMMON"), ProfileCOMMON.class, profile);
		effect.getFxProfileAbstract().add(profileCOMMONJAXBElement);

		return effect;
	}

	private String getModelName() {
		if (modelInfo != null) {
			String modelName = "";
			if (modelInfo.getParent() != null) {
				modelName = modelInfo.getParent().getModelName()+ "_";
			}
			return modelName + modelInfo.getModelName();
		}
		else {
			return modelName;
		}
	}

	private String getFullResourceName() {
		if (modelInfo != null) {
			String resourceName = "";
			if (modelInfo.getParent() != null) {
				resourceName = modelInfo.getParent().getModelName()+ "_";
			}
			return resourceName + modelInfo.getResourceName();
		}
		else {
			return modelName;
		}
	}

	public void writeCollada( OutputStream os ) throws IOException {
		COLLADA collada = factory.createCOLLADA();

		//Required part of a collada file
		collada.setVersion("1.4.1");

		Asset asset = createAsset();
		collada.setAsset( asset );


		//Create the Visual Scene, but don't add it yet because it needs to be at the end
		String sceneName = getModelName();
		VisualScene visualScene = factory.createVisualScene();
		visualScene.setId( sceneName );
		visualScene.setName( sceneName );

		//Add the skeleton to the visual scene
		Node skeletonNodes = createSkeletonNodes();
		visualScene.getNode().add( skeletonNodes );

		//Create the scene and add it
		Scene scene = factory.createCOLLADAScene();
		InstanceWithExtra sceneInstance = factory.createInstanceWithExtra();
		sceneInstance.setUrl( "#" + visualScene.getId() );
		scene.setInstanceVisualScene( sceneInstance );
		collada.setScene( scene );

		//Images and Materials
		LibraryImages libraryImages = factory.createLibraryImages();
		LibraryMaterials libraryMaterials = factory.createLibraryMaterials();
		LibraryEffects libraryEffects = factory.createLibraryEffects();
		for ( TexturedAppearance texture : visual.textures.getValue()) {
			Image image = factory.createImage();
			Integer textureID = texture.textureId.getValue();
			image.setName(getImageNameForID(textureID));
			image.setId(getImageIDForID(textureID));
			image.setInitFrom(getImageFileNameForID(textureID));
			libraryImages.getImage().add(image);

			Material material = factory.createMaterial();
			material.setName(getMaterialNameForID(textureID));
			material.setId(getMaterialIDForID(textureID));
			InstanceEffect instanceEffect = factory.createInstanceEffect();
			instanceEffect.setUrl("#"+ getEffectIDForID(textureID));
			material.setInstanceEffect(instanceEffect);
			libraryMaterials.getMaterial().add(material);

			Effect effect = createEffect(textureID);
			libraryEffects.getEffect().add(effect);
		}
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(libraryImages);
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(libraryMaterials);
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add(libraryEffects);

		//Geometry
		LibraryGeometries lg = factory.createLibraryGeometries();
		//Grab the static geometry and add them to the scene
		for( edu.cmu.cs.dennisc.scenegraph.Geometry g : visual.geometries.getValue() ) {
			if( g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
				edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = (edu.cmu.cs.dennisc.scenegraph.Mesh)g;
				Geometry geometry = createGeometryForMesh( sgMesh );
				lg.getGeometry().add( geometry );

				Node vsNode = createVisualSceneNodeForMesh(sgMesh);
				visualScene.getNode().add(vsNode);
			}
		}
		//Grab the weighted meshes and add their geometry to the scene
		for( WeightedMesh sgWM : visual.weightedMeshes.getValue() ) {
			Geometry geometry = createGeometryForMesh( sgWM );
			lg.getGeometry().add( geometry );
		}
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add( lg );

		//Weighted Mesh Controllers
		LibraryControllers lc = factory.createLibraryControllers();
		//Create controllers for all the weighted meshes
		for( WeightedMesh sgWM : visual.weightedMeshes.getValue() ) {
			Controller controller = createControllerForMesh( sgWM );
			lc.getController().add( controller );

			Node vsNode = createVisualSceneNodeForWeightedMesh(sgWM);
			visualScene.getNode().add(vsNode);
		}
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add( lc );

		//Finally add visual scene last so it's add the end
		LibraryVisualScenes lvs = factory.createLibraryVisualScenes();
		lvs.getVisualScene().add( visualScene );
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add( lvs );

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance( "org.lgna.story.resourceutilities.exporterutils.collada" );
			final Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
			marshaller.marshal( collada, os );
		} catch( JAXBException e ) {
			throw new IOException(e);
		}
	}

	private static BufferedImage createFlippedImage(BufferedImage image)
	{
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

		BufferedImage flippedImage = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = flippedImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return flippedImage;
	}

	private static void writeTexture(TexturedAppearance texture, OutputStream os) throws IOException {
		BufferedImageTexture bufferedTexture = (BufferedImageTexture)texture.diffuseColorTexture.getValue();
		//Flip the textures because Alice uses flipped textures and things outside of Alice don't
		BufferedImage flippedImage = createFlippedImage(bufferedTexture.getBufferedImage());
		ImageIO.write(flippedImage, IMAGE_EXTENSION, os);
	}

	public String getColladaFileName() {
		return getModelName() + "." + COLLADA_EXTENSION;
	}

	public List<String> getTextureFileNames() {
		List<String> textureFileNames = new ArrayList<>();
		for (TexturedAppearance texture : visual.textures.getValue()) {
			Integer textureID = texture.textureId.getValue();
			textureFileNames.add(getImageFileNameForID(textureID));
		}
		return textureFileNames;
	}

	public DataSource createColladaDataSource(String pathName) {
		final String name = pathName + "/" + getColladaFileName();
		return new DataSource() {
			@Override public String getName() { return name; }

			@Override public void write( OutputStream os ) throws IOException {
				writeCollada(os);
			}
		};
	}

	public Map<Integer, String> createTextureIdToImageMap() {
		Map<Integer, String> textureIdToFileMap = new HashMap<>();
		for (TexturedAppearance texture : visual.textures.getValue()) {
			Integer textureID = texture.textureId.getValue();
			textureIdToFileMap.put(textureID, getExternallyUniqueImageNameForID(textureID));
		}
		return textureIdToFileMap;
	}

	private TexturedAppearance getTextureAppearance(Integer textureId) {
		for (TexturedAppearance texture : visual.textures.getValue()) {
			if (textureId == texture.textureId.getValue()) {
				return texture;
			}
		}
		return null;
	}

	public ImageResource createImageResourceForTexture(Integer textureId) throws IOException {
		TexturedAppearance texturedAppearance = getTextureAppearance(textureId);
		BufferedImageTexture bufferedTexture = (BufferedImageTexture)texturedAppearance.diffuseColorTexture.getValue();
		return ImageFactory.createImageResource(bufferedTexture.getBufferedImage(), getImageFileNameForID(textureId));
	}

	public Integer getTextureIdForName(String textureName) {
		//Strip off any extension or path from the text name
		String bsaeTextureName = FileUtilities.getBaseName(textureName);
		for (TexturedAppearance texture : visual.textures.getValue()) {
			Integer textureID = texture.textureId.getValue();
			String toCheck = getExternallyUniqueImageNameForID(textureID);
			if (textureName.equals(toCheck)) {
				return textureID;
			}
		}
		return -1;
	}

	public List<DataSource> createImageDataSources(String pathName) {
		List<DataSource> dataSources = new ArrayList<>();
		for (TexturedAppearance texture : visual.textures.getValue()) {
			Integer textureID = texture.textureId.getValue();
			final String textureName = pathName +"/"+getImageFileNameForID(textureID);
			DataSource dataSource = new DataSource() {
				@Override public String getName() { return textureName; }

				@Override public void write( OutputStream os ) throws IOException {
					writeTexture(texture, os);
				}
			};
			dataSources.add(dataSource);
		}
		return dataSources;
	}

	public List<DataSource> createDataSources(String pathName) {
		List<DataSource> dataSources = new ArrayList<>();
		dataSources.add( createColladaDataSource(pathName));
		dataSources.addAll(createImageDataSources(pathName));
		return dataSources;

	}

	public List<File> saveTexturesToDirectory( File directory ) throws IOException {
		List<File> textureFiles = new ArrayList<>();
		for ( TexturedAppearance texture : visual.textures.getValue() ) {
			File textureFile = new File(directory, getImageFileNameForID(texture.textureId.getValue()));
			try {
				FileUtilities.createParentDirectoriesIfNecessary(textureFile);
				textureFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(textureFile);
				writeTexture(texture, fos);
				textureFiles.add(textureFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return textureFiles;
	}

	public File saveColladaToDirectory( File directory ) throws IOException {
		File colladaOutputFile = new File( directory, getModelName() + ".dae" );
		writeCollada(new FileOutputStream(colladaOutputFile));
		return colladaOutputFile;
	}


	//Local testing code

	private static List<File> exportAliceModelToDir(JointedModelColladaExporter exporter, File rootDir) throws IOException {
		List<File> outputFiles = new ArrayList<>();

		outputFiles.add(exporter.saveColladaToDirectory(rootDir));
		outputFiles.addAll(exporter.saveTexturesToDirectory(rootDir));

		return outputFiles;
	}

	private static SkeletonVisual loadAliceModel( JointedModelResource resource ) {
		VisualData<JointedModelResource> v = ImplementationAndVisualType.ALICE.getFactory( resource ).createVisualData();
		SkeletonVisual sv = (SkeletonVisual)v.getSgVisuals()[ 0 ];
		return sv;
	}

	private static List<File> exportAliceModelResourceToDir(JointedModelResource modelResource, File rootDir) throws IOException {
		ModelResourceInfo modelInfo = AliceResourceUtilties.getModelResourceInfo( modelResource.getClass(), modelResource.toString() );
		SkeletonVisual sgSkeletonVisual = loadAliceModel( modelResource );
		JointedModelColladaExporter exporter = new JointedModelColladaExporter(sgSkeletonVisual, modelInfo);
		return exportAliceModelToDir(exporter, rootDir);
	}

	private static void testModelExport(JointedModelResource modelResource, File rootDir) throws IOException{

//		List<File> modelFiles = exportAliceModelResourceToDir(modelResource, rootDir);
//		Logger modelLogger = Logger.getLogger( "org.lgna.story.resourceutilities.AliceColladaModelLoader" );
//		SkeletonVisual sv = null;
//		try {
//			String modelTestName = modelResource.getClass().getSimpleName()+"_TEST";
//			sv = AliceColladaModelLoader.loadAliceModelFromCollada(modelFiles.get(modelFiles.size() - 1), modelTestName, modelLogger);
//			JointedModelColladaExporter exporter = new JointedModelColladaExporter(sv, modelTestName);
//			List<File> modelTestFiles = exportAliceModelToDir(exporter, rootDir);
//		}
//		catch (ModelLoadingException e) {
//			e.printStackTrace();
//		}
	}

	public static void main( String[] args ) {

//		File outputRootDir = Paths.get(".").toFile();
//		File outputRootDir = new File("C:\\Users\\dculyba\\Documents\\Projects\\Alice\\Unity\\trilibtest\\Win64");
		File outputRootDir = new File("C:\\Users\\dculyba\\Documents\\Projects\\Alice\\Code\\alice-unity-player\\Assets\\Models");
		try {
			List<File> aliceFiles = exportAliceModelResourceToDir(AliceResource.WONDERLAND, outputRootDir);
//		List<File> pirateShipFiles = exportAliceModelResourceToDir(PirateShipResource.DEFAULT, outputRootDir);
//		List<File> colaBottleFiles = exportAliceModelResourceToDir(ColaBottleResource.DEFAULT, outputRootDir);
			List<File> sledFiles = exportAliceModelResourceToDir(SledResource.DEFAULT, outputRootDir);
//		testModelExport(YetiBabyResource.TUTU, new File(outputRootDir, "YetiBaby"));
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}

}
