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
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
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

	//Controls whether or not we flip the coordinate space during our conversion
	private static final boolean FLIP_COORDINATE_SPACE = true;
	private static final boolean SCALE_MODEL = true;
	private static final double MODEL_SCALE = 10.0;

	private final ObjectFactory factory;
	private final SkeletonVisual visual;
	private final ModelResourceInfo modelInfo;
	private final String modelName;
	private final Map<Integer, File> idToTextureFileMap;

	private final HashMap<edu.cmu.cs.dennisc.scenegraph.Geometry, String> meshNameMap = new HashMap<>();
	private final HashMap<Integer, String> textureNameMap = new HashMap<>();

	public JointedModelColladaExporter( SkeletonVisual sv, ModelResourceInfo mi, Map<Integer, File> idToTextureFileMap ) {
		this.factory = new ObjectFactory();
		this.visual = sv;
		this.modelInfo = mi;
		this.idToTextureFileMap = idToTextureFileMap;
		this.modelName = null;
	}

	public JointedModelColladaExporter( SkeletonVisual sv, String modelName, Map<Integer, File> idToTextureFileMap ) {
		this.factory = new ObjectFactory();
		this.visual = sv;
		this.modelInfo = null;
		this.idToTextureFileMap = idToTextureFileMap;
		this.modelName = modelName;
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
		Vertices vertices = factory.createVertices();
		String vertexName = meshName + "-VERTEX";
		vertices.setId( vertexName );
		//Vertices have two inputs: POSITION and NORMAL
		//These are linked via a string with the prefix "#" to the source names
		InputLocal positionInput = factory.createInputLocal();
		positionInput.setSemantic( "POSITION" );
		positionInput.setSource( "#" + positionName );
		vertices.getInput().add( positionInput );

		mesh.setVertices( vertices );
		//Build the triangle data
		Triangles triangles = factory.createTriangles();
		InputLocalOffset vertexInput = factory.createInputLocalOffset();
		vertexInput.setSemantic( "VERTEX" );
		vertexInput.setSource( "#" + vertexName );
		vertexInput.setOffset( BigInteger.valueOf( 0 ) );
		triangles.getInput().add( vertexInput );

		InputLocalOffset normalInput = factory.createInputLocalOffset();
		normalInput.setSemantic( "NORMAL" );
		normalInput.setSource( "#" + normalName );
		normalInput.setOffset( BigInteger.valueOf( 1 ) );
		triangles.getInput().add( normalInput );

		InputLocalOffset texCoordInput = factory.createInputLocalOffset();
		texCoordInput.setSemantic( "TEXCOORD" );
		texCoordInput.setSource( "#" + uvName );
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

	private Controller createControllerForMesh( WeightedMesh sgWeightedMesh ) {
		Controller controller = factory.createController();
		String meshName = meshNameMap.get( sgWeightedMesh );
		String controllerName = meshName + "Controller";
		controller.setId( controllerName );

		Skin skin = factory.createSkin();
		controller.setSkin( skin );
		skin.setSourceAttribute( "#" + getMeshIdForMeshName( meshName ) );

//		//Set the bind shape matrix
		double[] bindShapeMatrix = getBindShapeMatrix( sgWeightedMesh );
		for( double element : bindShapeMatrix ) {
			skin.getBindShapeMatrix().add( element );
		}

		WeightInfo wi = sgWeightedMesh.weightInfo.getValue();

		//Build the Joint Source
		Source jointSource = factory.createSource();
		String jointSourceName = controllerName + "-Joints";
		jointSource.setId( jointSourceName );
		NameArray jointNameArray = factory.createNameArray();
		jointNameArray.setId( controllerName + "-Joints-array" );
		for( Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet() ) {
			jointNameArray.getValue().add( entry.getKey() );
		}
		jointNameArray.setCount( BigInteger.valueOf( jointNameArray.getValue().size() ) );
		jointSource.setNameArray( jointNameArray );
		jointSource.setTechniqueCommon( createTechniqueCommonForArray( jointNameArray.getId(), "name", jointNameArray.getValue().size(), 1 ) );
		skin.getSourceAttribute2().add( jointSource );

		//Build the Matrix Source
		Source matricesSource = factory.createSource();
		String matricesSourceName = controllerName + "-Matrices";
		matricesSource.setId( matricesSourceName );
		FloatArray matricesArray = factory.createFloatArray();
		matricesArray.setId( controllerName + "-Matrices-array" );
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
		for( Integer i : idToTextureFileMap.keySet() ) {
			textureNameMap.put(i, "texture_"+i);
		}
	}

	private String getImageNameForID(Integer id) {
		return textureNameMap.get(id) + "_diffuseMap";
	}

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
			return modelInfo.getModelName();
		}
		else {
			return modelName;
		}
	}

	public void writeColladaFile( File outputFile ) {
		COLLADA collada = factory.createCOLLADA();

		//Required part of a collada file
		collada.setVersion("1.4.1");

		Asset asset = createAsset();
		collada.setAsset( asset );

		//Go through all the meshes in the sgVisual and find or create names for all of them
		initializeMeshNameMap();
		//Go through all the textures and create names based on the IDs
		initializeTextureNameMap();


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
		for ( Entry<Integer, File> entry : idToTextureFileMap.entrySet()) {
			Image image = factory.createImage();
			image.setName(getImageNameForID(entry.getKey()));
			image.setId(getImageIDForID(entry.getKey()));
			image.setInitFrom(entry.getValue().getName());
			libraryImages.getImage().add(image);

			Material material = factory.createMaterial();
			material.setName(getMaterialNameForID(entry.getKey()));
			material.setId(getMaterialIDForID(entry.getKey()));
			InstanceEffect instanceEffect = factory.createInstanceEffect();
			instanceEffect.setUrl("#"+ getEffectIDForID(entry.getKey()));
			material.setInstanceEffect(instanceEffect);
			libraryMaterials.getMaterial().add(material);

			Effect effect = createEffect(entry.getKey());
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
			FileOutputStream fos = new FileOutputStream( outputFile );
			marshaller.marshal( collada, fos );
			fos.close();
		} catch( JAXBException | IOException e ) {
			e.printStackTrace();
		}
	}

	private static SkeletonVisual loadAliceModel( JointedModelResource resource ) {
		VisualData<JointedModelResource> v = ImplementationAndVisualType.ALICE.getFactory( resource ).createVisualData();
		SkeletonVisual sv = (SkeletonVisual)v.getSgVisuals()[ 0 ];
		return sv;
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

	private static File saveTexture(TexturedAppearance texture, File outputFile) {
		BufferedImageTexture bufferedTexture = (BufferedImageTexture)texture.diffuseColorTexture.getValue();
		try {
			//Flip the textures because Alice uses flipped textures and things outside of Alice don't
			BufferedImage flippedImage = createFlippedImage(bufferedTexture.getBufferedImage());
			ImageIO.write(flippedImage, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return outputFile;
	}

	private static Map<Integer, File> saveTexturesToFile( File directory, String baseName, TexturedAppearance[] textures ) {
		Map<Integer, File> idToTextureFileMap = new HashMap();
		for ( TexturedAppearance texture : textures ) {
			File textureFile = new File(directory, baseName + "_" + texture.textureId.getValue() + ".png");
			try {
				FileUtilities.createParentDirectoriesIfNecessary(textureFile);
				textureFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			textureFile = saveTexture(texture, textureFile);
			if (textureFile != null) {
				idToTextureFileMap.put(texture.textureId.getValue(), textureFile);
			}
		}
		return idToTextureFileMap;
	}

	private static List<File> exportAliceModel(SkeletonVisual sgSkeletonVisual, String modelName, File rootDir) {
		Map<Integer, File> idToTextureFileMap  = saveTexturesToFile(rootDir, modelName, sgSkeletonVisual.textures.getValue());

		JointedModelColladaExporter exporter = new JointedModelColladaExporter( sgSkeletonVisual, modelName, idToTextureFileMap );
		File colladaOutputFile = new File( rootDir, modelName + ".dae" );
		exporter.writeColladaFile( colladaOutputFile );

		List<File> outputFiles = new ArrayList<>(idToTextureFileMap.size() + 1);
		for (Entry<Integer, File> entry : idToTextureFileMap.entrySet()) {
			outputFiles.add(entry.getValue());
		}
		outputFiles.add(colladaOutputFile);

		return outputFiles;
	}

	private static List<File> exportAliceModel(JointedModelResource modelResource, File rootDir) {
		ModelResourceInfo modelInfo = AliceResourceUtilties.getModelResourceInfo( modelResource.getClass(), modelResource.toString() );
		SkeletonVisual sgSkeletonVisual = loadAliceModel( modelResource );
		Map<Integer, File> idToTextureFileMap  = saveTexturesToFile(rootDir, modelInfo.getModelName()+"_"+modelInfo.getResourceName(), sgSkeletonVisual.textures.getValue());

		JointedModelColladaExporter exporter = new JointedModelColladaExporter( sgSkeletonVisual, modelInfo, idToTextureFileMap );
		File colladaOutputFile = new File( rootDir, modelInfo.getModelName() + ".dae" );
		exporter.writeColladaFile( colladaOutputFile );

		List<File> outputFiles = new ArrayList<>(idToTextureFileMap.size() + 1);
		for (Entry<Integer, File> entry : idToTextureFileMap.entrySet()) {
			outputFiles.add(entry.getValue());
		}
		outputFiles.add(colladaOutputFile);

		return outputFiles;
	}

	private static void testModelExport(JointedModelResource modelResource, File rootDir) {
		List<File> modelFiles = exportAliceModel(modelResource, rootDir);
		Logger modelLogger = Logger.getLogger( "org.lgna.story.resourceutilities.AliceColladaModelLoader" );
		SkeletonVisual sv = null;
		try {
			String modelTestName = modelResource.getClass().getSimpleName()+"_TEST";
			sv = AliceColladaModelLoader.loadAliceModelFromCollada(modelFiles.get(modelFiles.size() - 1), modelTestName, modelLogger);
			List<File> modelTestFiles = exportAliceModel(sv, modelTestName, rootDir);
		}
		catch (ModelLoadingException e) {
			e.printStackTrace();
		}
	}

	public static void main( String[] args ) {

//		File outputRootDir = Paths.get(".").toFile();
//		File outputRootDir = new File("C:\\Users\\dculyba\\Documents\\Projects\\Alice\\Unity\\trilibtest\\Win64");
		File outputRootDir = new File("C:\\Users\\dculyba\\Documents\\Projects\\Alice\\Code\\alice-unity-player\\Assets\\Models");

		List<File> aliceFiles = exportAliceModel(AliceResource.WONDERLAND, outputRootDir);
//		List<File> pirateShipFiles = exportAliceModel(PirateShipResource.DEFAULT, outputRootDir);
//		List<File> colaBottleFiles = exportAliceModel(ColaBottleResource.DEFAULT, outputRootDir);
		List<File> sledFiles = exportAliceModel(SledResource.DEFAULT, outputRootDir);
//		testModelExport(YetiBabyResource.TUTU, new File(outputRootDir, "YetiBaby"));

	}

}
