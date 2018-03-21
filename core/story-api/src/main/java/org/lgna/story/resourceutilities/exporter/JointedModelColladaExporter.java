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
package org.lgna.story.resourceutilities.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.biped.AliceResource;
import org.lgna.story.resources.prop.ColaBottleResource;
import org.lgna.story.resourceutilities.ModelResourceInfo;
import org.lgna.story.resourceutilities.exporterutils.collada.Accessor;
import org.lgna.story.resourceutilities.exporterutils.collada.Asset;
import org.lgna.story.resourceutilities.exporterutils.collada.Asset.Unit;
import org.lgna.story.resourceutilities.exporterutils.collada.COLLADA;
import org.lgna.story.resourceutilities.exporterutils.collada.COLLADA.Scene;
import org.lgna.story.resourceutilities.exporterutils.collada.Controller;
import org.lgna.story.resourceutilities.exporterutils.collada.FloatArray;
import org.lgna.story.resourceutilities.exporterutils.collada.Geometry;
import org.lgna.story.resourceutilities.exporterutils.collada.InputLocal;
import org.lgna.story.resourceutilities.exporterutils.collada.InputLocalOffset;
import org.lgna.story.resourceutilities.exporterutils.collada.InstanceWithExtra;
import org.lgna.story.resourceutilities.exporterutils.collada.LibraryControllers;
import org.lgna.story.resourceutilities.exporterutils.collada.LibraryGeometries;
import org.lgna.story.resourceutilities.exporterutils.collada.LibraryVisualScenes;
import org.lgna.story.resourceutilities.exporterutils.collada.Matrix;
import org.lgna.story.resourceutilities.exporterutils.collada.Mesh;
import org.lgna.story.resourceutilities.exporterutils.collada.NameArray;
import org.lgna.story.resourceutilities.exporterutils.collada.Node;
import org.lgna.story.resourceutilities.exporterutils.collada.NodeType;
import org.lgna.story.resourceutilities.exporterutils.collada.ObjectFactory;
import org.lgna.story.resourceutilities.exporterutils.collada.Param;
import org.lgna.story.resourceutilities.exporterutils.collada.Skin;
import org.lgna.story.resourceutilities.exporterutils.collada.Skin.VertexWeights;
import org.lgna.story.resourceutilities.exporterutils.collada.Source;
import org.lgna.story.resourceutilities.exporterutils.collada.Source.TechniqueCommon;
import org.lgna.story.resourceutilities.exporterutils.collada.Triangles;
import org.lgna.story.resourceutilities.exporterutils.collada.UpAxisType;
import org.lgna.story.resourceutilities.exporterutils.collada.Vertices;
import org.lgna.story.resourceutilities.exporterutils.collada.VisualScene;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.WeightInfo;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;

/**
 * @author Dave Culyba
 */
public class JointedModelColladaExporter {

	private final ObjectFactory factory;
	private final SkeletonVisual visual;
	private final ModelResourceInfo modelInfo;

	private final HashMap<edu.cmu.cs.dennisc.scenegraph.Geometry, String> meshNameMap = new HashMap<>();

	public JointedModelColladaExporter( SkeletonVisual sv, ModelResourceInfo mi ) {
		this.factory = new ObjectFactory();
		this.visual = sv;
		this.modelInfo = mi;
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
		double[] matrixValues = joint.getLocalTransformation().getAsColumnMajorArray12();
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
		public void initializeList( List<Double> toInitialize );
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

	private Source createFloatArraySourceFromInitializer( ListInitializer initializer, String name, int stride ) {

		Source source = factory.createSource();
		source.setId( name );

		//Build the array of vertices (called "position" in the collada file)
		FloatArray floatArray = factory.createFloatArray();
		floatArray.setId( name + "-array" );
		List<Double> values = floatArray.getValue();
		initializer.initializeList( values );
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
		geometry.setName( meshName );

		//Set up the Mesh node
		Mesh mesh = factory.createMesh();

		//Build the Position source. This will hold the array of vertices and a accessor that tells the file the array is of stride 3
		String positionName = meshName + "-POSITION";
		Source positionSource = createFloatArraySourceFromInitializer( new DoubleBufferInitializeList( sgMesh.vertexBuffer.getValue() ), positionName, 3 );
		mesh.getSource().add( positionSource );

		String normalName = meshName + "-NORMAL";
		Source normalSource = createFloatArraySourceFromInitializer( new FloatBufferInitializeList( sgMesh.normalBuffer.getValue() ), normalName, 3 );
		mesh.getSource().add( normalSource );

		String uvName = meshName + "-UV";
		Source uvSource = createFloatArraySourceFromInitializer( new FloatBufferInitializeList( sgMesh.textCoordBuffer.getValue() ), uvName, 2 );
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

		InputLocal normalInput = factory.createInputLocal();
		normalInput.setSemantic( "NORMAL" );
		normalInput.setSource( "#" + normalName );
		vertices.getInput().add( normalInput );

		mesh.setVertices( vertices );
		//Build the triangle data
		Triangles triangles = factory.createTriangles();
		InputLocalOffset vertexInput = factory.createInputLocalOffset();
		vertexInput.setSemantic( "VERTEX" );
		vertexInput.setSource( "#" + vertexName );
		vertexInput.setOffset( BigInteger.valueOf( 0 ) );
		triangles.getInput().add( vertexInput );
		InputLocalOffset texCoordInput = factory.createInputLocalOffset();
		texCoordInput.setSemantic( "TEXCOORD" );
		texCoordInput.setSource( "#" + uvName );
		texCoordInput.setOffset( BigInteger.valueOf( 1 ) );
		texCoordInput.setSet( BigInteger.valueOf( 0 ) );
		triangles.getInput().add( texCoordInput );
		//Copy over the triangle indices
		List<BigInteger> triangleList = triangles.getP();
		IntBuffer ib = sgMesh.indexBuffer.getValue();
		final int N = sgMesh.indexBuffer.getValue().limit();
		for( int i = 0; i < N; i++ ) {
			triangleList.add( BigInteger.valueOf( ib.get( i ) ) );
		}
		mesh.getLinesOrLinestripsOrPolygons().add( triangles );

		geometry.setMesh( mesh );

		return geometry;
	}

	private double[] getBindShapeMatrix( edu.cmu.cs.dennisc.scenegraph.WeightedMesh sgWeightedMesh ) {
		//TODO: have this return the correct bind shape matrix
		AffineMatrix4x4 bindShapeMatrix = AffineMatrix4x4.createIdentity();
		return bindShapeMatrix.getAsColumnMajorArray16();
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

	private VertexWeights createVertexWeights( int vertexCount, WeightInfo wi, String jointsSource, String weightsSource ) {
		VertexWeights vw = factory.createSkinVertexWeights();
		vw.setCount( BigInteger.valueOf( vertexCount ) );

		InputLocalOffset jointInput = factory.createInputLocalOffset();
		jointInput.setSemantic( "JOINT" );
		jointInput.setSource( "#" + jointsSource );
		jointInput.setOffset( BigInteger.valueOf( 0 ) );
		vw.getInput().add( jointInput );

		InputLocalOffset weightInput = factory.createInputLocalOffset();
		weightInput.setSemantic( "WEIGHT" );
		weightInput.setSource( "#" + weightsSource );
		weightInput.setOffset( BigInteger.valueOf( 1 ) );
		vw.getInput().add( weightInput );

		ArrayList<ColladaSkinWeights> skinWeights = new ArrayList<ColladaSkinWeights>( vertexCount );
		List<Float> weightArray = new ArrayList<Float>();
		int jointIndex = 0;
		for( Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet() ) {
			InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
			iatwp.reset();
			while( !iatwp.isDone() ) {
				int vertexIndex = iatwp.getIndex();
				float weight = iatwp.getWeight();
				weightArray.add( weight );
				if( skinWeights.get( vertexIndex ) == null ) {
					skinWeights.set( vertexIndex, new ColladaSkinWeights() );
				}
				ColladaSkinWeights vertexWeights = skinWeights.get( vertexIndex );
				vertexWeights.jointIndices.add( jointIndex );
				vertexWeights.weightIndices.add( weightArray.size() - 1 );

				iatwp.advance();
			}
			jointIndex++;
		}
		for( int i = 0; i < skinWeights.size(); i++ ) {
			ColladaSkinWeights vertexWeights = skinWeights.get( i );
			vw.getVcount().add( BigInteger.valueOf( vertexWeights.jointIndices.size() ) );
			for( int j = 0; j < vertexWeights.jointIndices.size(); j++ ) {
				vw.getV().add( vertexWeights.jointIndices.get( j ).longValue() );
				vw.getV().add( vertexWeights.weightIndices.get( j ).longValue() );
			}
		}

		return vw;
	}

	private Controller createControllerForMesh( edu.cmu.cs.dennisc.scenegraph.WeightedMesh sgWeightedMesh ) {
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

		Source matricesSource = factory.createSource();
		String matricesSourceName = controllerName + "-Matrices";
		matricesSource.setId( matricesSourceName );
		FloatArray matricesArray = factory.createFloatArray();
		matricesArray.setId( controllerName + "-Matrices-array" );
		for( Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet() ) {
			InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
			double[] matrix = iatwp.getInverseAbsoluteTransformation().getAsColumnMajorArray16();
			for( double element : matrix ) {
				matricesArray.getValue().add( element );
			}
		}
		int matrixCount = jointNameArray.getValue().size() / 16;
		matricesArray.setCount( BigInteger.valueOf( matrixCount ) );
		matricesSource.setFloatArray( matricesArray );
		matricesSource.setTechniqueCommon( createTechniqueCommonForArray( matricesArray.getId(), "float4x4", matrixCount, 16 ) );
		skin.getSourceAttribute2().add( matricesSource );

		//Add weights
		int vertexCount = sgWeightedMesh.vertexBuffer.getValue().limit() / 3;
		VertexWeights vw = factory.createSkinVertexWeights();
		vw.setCount( BigInteger.valueOf( vertexCount ) );

		InputLocalOffset jointInput = factory.createInputLocalOffset();
		jointInput.setSemantic( "JOINT" );
		jointInput.setSource( "#" + jointSourceName );
		jointInput.setOffset( BigInteger.valueOf( 0 ) );
		vw.getInput().add( jointInput );

		InputLocalOffset weightInput = factory.createInputLocalOffset();
		weightInput.setSemantic( "WEIGHT" );
		weightInput.setSource( "#" + matricesSourceName );
		weightInput.setOffset( BigInteger.valueOf( 1 ) );
		vw.getInput().add( weightInput );

		ColladaSkinWeights[] skinWeights = new ColladaSkinWeights[ vertexCount ];
		List<Float> weightArray = new ArrayList<Float>();
		int jointIndex = 0;
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
		for( ColladaSkinWeights vertexWeights : skinWeights ) {
			vw.getVcount().add( BigInteger.valueOf( vertexWeights.jointIndices.size() ) );
			for( int j = 0; j < vertexWeights.jointIndices.size(); j++ ) {
				vw.getV().add( vertexWeights.jointIndices.get( j ).longValue() );
				vw.getV().add( vertexWeights.weightIndices.get( j ).longValue() );
			}
		}

		skin.setVertexWeights( vw );

		//Build the Position source. This will hold the array of vertices and a accessor that tells the file the array is of stride 3
		String weightsSourceName = controllerName + "-Weights";
		Source weightsSource = createFloatArraySourceFromInitializer( new FloatListInitializeList( weightArray ), weightsSourceName, 1 );
		skin.getSourceAttribute2().add( weightsSource );

		return controller;
	}

	private VisualScene createVisualScene( String sceneName ) {
		VisualScene visualScene = factory.createVisualScene();

		visualScene.setId( sceneName );
		visualScene.setName( sceneName );

		Node skeletonNodes = createSkeletonNodes();
		visualScene.getNode().add( skeletonNodes );

		return visualScene;
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

	public void writeColladaFile( File outputFile ) {
		COLLADA collada = factory.createCOLLADA();

		Asset asset = createAsset();
		collada.setAsset( asset );

		//Go through all the meshes in the sgVisual and find or create names for all of them
		initializeMeshNameMap();

		//Visual Scene
		VisualScene visualScene = createVisualScene( this.modelInfo.getModelName() );
		LibraryVisualScenes lvs = factory.createLibraryVisualScenes();
		lvs.getVisualScene().add( visualScene );
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add( lvs );

		Scene scene = factory.createCOLLADAScene();
		InstanceWithExtra sceneInstance = factory.createInstanceWithExtra();
		sceneInstance.setUrl( "#" + visualScene.getId() );
		scene.setInstanceVisualScene( sceneInstance );
		collada.setScene( scene );

		//Geometry
		LibraryGeometries lg = factory.createLibraryGeometries();

		for( edu.cmu.cs.dennisc.scenegraph.Geometry g : visual.geometries.getValue() ) {
			if( g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh ) {
				edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = (edu.cmu.cs.dennisc.scenegraph.Mesh)g;
				Geometry geometry = createGeometryForMesh( sgMesh );
				lg.getGeometry().add( geometry );
			}
		}
		for( WeightedMesh sgWM : visual.weightedMeshes.getValue() ) {
			Geometry geometry = createGeometryForMesh( sgWM );
			lg.getGeometry().add( geometry );
		}
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add( lg );

		//Controllers
		LibraryControllers lc = factory.createLibraryControllers();
		for( WeightedMesh sgWM : visual.weightedMeshes.getValue() ) {
			Controller controller = createControllerForMesh( sgWM );
			lc.getController().add( controller );
		}
		collada.getLibraryAnimationsOrLibraryAnimationClipsOrLibraryCameras().add( lc );

		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance( "org.lgna.story.resourceutilities.exporterutils.collada" );
			final Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
			FileOutputStream fos = new FileOutputStream( outputFile );
			marshaller.marshal( collada, fos );
			marshaller.marshal( collada, System.out );
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

	public static void main( String[] args ) {

		ModelResourceInfo modelInfoAlice = AliceResourceUtilties.getModelResourceInfo( AliceResource.class, "WONDERLAND" );

		ModelResourceInfo modelInfo = AliceResourceUtilties.getModelResourceInfo( ColaBottleResource.class, "DEFAULT" );
		SkeletonVisual sgSkeletonVisual = loadAliceModel( ColaBottleResource.DEFAULT );

		JointedModelColladaExporter exporter = new JointedModelColladaExporter( sgSkeletonVisual, modelInfo );
		exporter.writeColladaFile( new File( modelInfo.getModelName() + ".dae" ) );

	}

}
