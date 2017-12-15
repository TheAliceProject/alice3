import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.fish.ClownFishResource;
import org.lgna.story.resources.flyer.ChickenResource;
import org.xml.sax.SAXException;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.controller.VertexWeights;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Primitives;
import com.dddviewr.collada.geometry.Triangles;
import com.dddviewr.collada.images.Image;
import com.dddviewr.collada.materials.InstanceEffect;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.BaseXform;
import com.dddviewr.collada.visualscene.Matrix;
import com.dddviewr.collada.visualscene.Rotate;
import com.dddviewr.collada.visualscene.Scale;
import com.dddviewr.collada.visualscene.Translate;
import com.dddviewr.collada.visualscene.VisualScene;
import com.jogamp.common.nio.Buffers;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.nebulous.javabased.AffineMatrix;
import edu.cmu.cs.dennisc.nebulous.javabased.TextureGroup;
import edu.cmu.cs.dennisc.nebulous.javabased.Utilities;
import edu.cmu.cs.dennisc.nebulous.javabased.UtilitySkeletonVisualAdapter;
import edu.cmu.cs.dennisc.nebulous.javabased.VertexMapData;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.nebulous.javabased.Mesh.MeshType;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Indices;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.PlentifulInverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.SparseInverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.WeightInfo;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;
import edu.cmu.cs.dennisc.texture.Texture;

public class AliceColladaModelLoader {
	
	private static Logger LOGGER;
	
	private static boolean nodeIsJoint( Node n ) {
		return (n.getType() != null && n.getType().equals( "JOINT" ));
	}
	
	private static Node findRootNode( List<Node> nodes ) {
		for (Node n : nodes) {
			if (nodeIsJoint(n)) {
				if (n.getName().equalsIgnoreCase( "root" )) {
					return n;
				}
			}
		}
		for (Node n : nodes) {
			Node childRoot = findRootNode(n.getChildNodes());
			if (childRoot != null) {
				return childRoot;
			}
		}
		return null;
	}
	
	public static AffineMatrix4x4 createAliceMatrixFromArray12( double[] matrixArray ) {
		assert matrixArray.length == 12;
		AffineMatrix4x4 rv = AffineMatrix4x4.createNaN();
		rv.orientation.right.x = matrixArray[ 0 ];
		rv.orientation.up.x = matrixArray[ 1 ];
		rv.orientation.backward.x = matrixArray[ 2 ];
		rv.translation.x = matrixArray[ 3 ];
		rv.orientation.right.y = matrixArray[ 4 ];
		rv.orientation.up.y = matrixArray[ 5 ];
		rv.orientation.backward.y = matrixArray[ 6 ];
		rv.translation.y = matrixArray[ 7 ];
		rv.orientation.right.z = matrixArray[ 8 ];
		rv.orientation.up.z = matrixArray[ 9 ];
		rv.orientation.backward.z = matrixArray[ 10 ];
		rv.translation.z = matrixArray[ 11 ];
		return rv;
	}

	public static AffineMatrix4x4 createAliceMatrixFromArray16( double[] matrixArray ) {
		assert matrixArray.length == 16;
		AffineMatrix4x4 rv = AffineMatrix4x4.createNaN();
		rv.orientation.right.x = matrixArray[ 0 ];
		rv.orientation.up.x = matrixArray[ 1 ];
		rv.orientation.backward.x = matrixArray[ 2 ];
		rv.translation.x = matrixArray[ 3 ];
		rv.orientation.right.y = matrixArray[ 4 ];
		rv.orientation.up.y = matrixArray[ 5 ];
		rv.orientation.backward.y = matrixArray[ 6 ];
		rv.translation.y = matrixArray[ 7 ];
		rv.orientation.right.z = matrixArray[ 8 ];
		rv.orientation.up.z = matrixArray[ 9 ];
		rv.orientation.backward.z = matrixArray[ 10 ];
		rv.translation.z = matrixArray[ 11 ];
		return rv;
	}
	
	private static AffineMatrix4x4 floatArrayToAliceMatrix( float[] floatData ) throws ModelLoadingException {
		double[] doubleData = new double[floatData.length];
		for (int i=0; i<floatData.length; i++) {
			doubleData[i] = floatData[i];
		}
		if (doubleData.length == 12) {
			return createAliceMatrixFromArray12( doubleData );
		}
		else if (doubleData.length == 16) {
			return createAliceMatrixFromArray16( doubleData );
		}
		else {
			throw new ModelLoadingException("Error converting collada matrix to Alice matrix. Expected array of size 12 or 16, instead got "+floatData.length);
		}
	}
	
	private static AffineMatrix4x4 colladaMatrixToAliceMatrix( Matrix m ) throws ModelLoadingException {
		return floatArrayToAliceMatrix(m.getData());
	}
	
	private static Joint createAliceSkeletonFromNode( Node node ) throws ModelLoadingException {
		AffineMatrix4x4 aliceMatrix = AffineMatrix4x4.createIdentity();
		for (int i=0; i<node.getXforms().size(); i++) {
			BaseXform xform = node.getXforms().get( i );
			if (xform instanceof Matrix) {
				aliceMatrix = colladaMatrixToAliceMatrix( (Matrix)xform );
			}
			else if (xform instanceof Translate){
				Translate translate = (Translate)xform;
				aliceMatrix.translation.set( translate.getX(), translate.getY(), translate.getZ() );
			}
			else if (xform instanceof Scale) {
				Scale scale = (Scale)xform;
				OrthogonalMatrix3x3 scaleMatrix = new OrthogonalMatrix3x3( new Vector3(scale.getX(),0,0), new Vector3(0,scale.getY(),0), new Vector3(0,0,scale.getZ()) );
				aliceMatrix.orientation.applyMultiplication( scaleMatrix );
			}
			else if (xform instanceof Rotate) {
				Rotate rotate = (Rotate)xform;
				Vector3 axis = new Vector3( rotate.getX(), rotate.getY(), rotate.getZ() );
				aliceMatrix.orientation.applyRotationAboutArbitraryAxis( axis, new AngleInDegrees( rotate.getAngle() ) );
			}
		}
		
		Joint j = new Joint();
		j.jointID.setValue( node.getName() );
		j.setName( node.getName() );
//		aliceMatrix.orientation.normalizeColumns();
		
		j.localTransformation.setValue( aliceMatrix );
		for (Node child : node.getChildNodes()) {
			if (nodeIsJoint( child )) {
				Joint childJoint = createAliceSkeletonFromNode( child );
				childJoint.setParent( j );
			}
		}
		return j;
	}
	
	private static void getGeometryInfo( Geometry geometry ) {
		float[] normalData = geometry.getMesh().getNormalData();
		int normalCount = normalData.length / 3;
		float[] vertexData = geometry.getMesh().getPositionData();
		int vertexCount = vertexData.length / 3;
		float[] uvData = geometry.getMesh().getTexCoordData();
		int uvCount = uvData.length / 2;
		Triangles tris = (Triangles)geometry.getMesh().getPrimitives().get( 0 );
		int triCount = tris.getCount();
		
		System.out.println( "Tris:  "+triCount+", normals: "+normalCount+", vertices: "+vertexCount+", uvs: "+uvCount);
	}
	
	private static int getMaterialIdForMaterialName(String materialName, Collada colladaModel) {
		int id = 0;
		for (Material material : colladaModel.getLibraryMaterials().getMaterials()) {
			if (material.getName().equals( materialName )) {
				return id;
			}
			id++;
		}
		return -1;
	}
	
	private static Controller getControllerForGeometry( Geometry geometry, Collada colladaModel ) {
		for (Controller controller : colladaModel.getLibraryControllers().getControllers()) {
			Geometry foundGeometry = colladaModel.findGeometry( controller.getSkin().getSource() );
			if ( foundGeometry == geometry ) {
				return controller;
			}
		}
		return null;
	}
	
	private static WeightInfo createWeightInfoForController( Controller meshController ) throws ModelLoadingException {
		Skin skin = meshController.getSkin();        	
    	VertexWeights vertexWeights = skin.getVertexWeights();
    	float[] weightData = skin.getWeightData();
    	int[] vertexWeightData = vertexWeights.getData();
    	/*
    	 *  From the Collada 1.5 Spec:
    	 *  Here is an example of a more complete <vertex_weights> element. Note that the <vcount> element
			says that the first vertex has 3 bones, the second has 2, etc. Also, the <v> element says that the first
			vertex is weighted with weights[0] towards the bind shape, weights[1] towards bone 0, and
			weights[2] towards bone 1:
			<skin>
			 <source id="joints"/>
			 <source id="weights"/>
			 <vertex_weights count="4">
			 <input semantic="JOINT" source="#joints"/>
			 <input semantic="WEIGHT" source="#weights"/>
			 <vcount>3 2 2 3</vcount>
			 <v>
			 -1 0 0 1 1 2
			 -1 3 1 4
			 -1 3 2 4
			 -1 0 3 1 2 2
			 </v>
			 </vertex_weights>
			</skin> 
    	 */
    	int entryCount = 0;
    	Map<Integer, float[]> jointWeightMap = new HashMap<Integer, float[]>();
    	//Loop through the vertex weights and build a map of joints (stored as their indices to the joint data) to arrays of weight info
    	//The weight info is stored as arrays of weights where the index of the entry is the index of the joint and the weight values is the weighting of that vertex to the joint
    	for (int vertex=0; vertex<vertexWeights.getCount(); vertex++) {
    		int boneCount = vertexWeights.getVcount().getData()[vertex];
    		for (int bone=0; bone<boneCount; bone++) {
    			int jointIndex = vertexWeightData[entryCount*2];
    			int weightIndex = vertexWeightData[entryCount*2 + 1];
  
    			float weight = weightData[weightIndex];
    			
    			float[] weightArray;
    			if (jointWeightMap.containsKey( jointIndex )) {
    				weightArray = jointWeightMap.get( jointIndex );
    			}
    			else {
    				weightArray = new float[vertexWeights.getCount()];
    				jointWeightMap.put( jointIndex, weightArray );
    			}
    			weightArray[vertex] = weight;
    			entryCount++;
    		}
    		
    	}
    	//Take the weight data and convert it to WeightInfo
    	String[] jointData = skin.getJointData();
    	if (jointData == null) {
    		throw new ModelLoadingException( "Error conversing mesh "+meshController.getName()+", no joint data found on mesh skin." );
    	}
    	
    	//Array of inverse bind matrices for all the referenced joints. Is indexed into by the joint index.
    	float[] inverseBindMatrixData = skin.getInvBindMatrixData();
    	if (inverseBindMatrixData == null) {
    		throw new ModelLoadingException( "Error conversing mesh "+meshController.getName()+", no inverse bind matrix data found on mesh skin." );
    	}
    	WeightInfo weightInfo = new WeightInfo();
    	for (Entry<Integer, float[]> jointAndWeights : jointWeightMap.entrySet()) {
			int nonZeroWeights = 0;
			for (float weight : jointAndWeights.getValue()) {
				if (weight != 0) {
					nonZeroWeights++;
				}
			}
			if (nonZeroWeights > 0) {
				InverseAbsoluteTransformationWeightsPair iawp;
				double portion = ((double) nonZeroWeights)
						/ jointAndWeights.getValue().length;
				if (portion > .9) {
					iawp = new PlentifulInverseAbsoluteTransformationWeightsPair();
				} else {
					iawp = new SparseInverseAbsoluteTransformationWeightsPair();
				}
				int jointIndex = jointAndWeights.getKey();
				String jointId = jointData[jointIndex];
				float[] inverseBindMatrix = Arrays.copyOfRange( inverseBindMatrixData, 16*jointIndex, 16*jointIndex+16 );
				AffineMatrix4x4 aliceInverseBindMatrix = floatArrayToAliceMatrix( inverseBindMatrix );

				//Create the new transform by inverting the existing one so that we're working in untransformed space
				AffineMatrix4x4 newTransform = AffineMatrix4x4.createInverse( aliceInverseBindMatrix );
				//Since these are absolute transforms, they need to have the scale removed both from the translation and the orientaion
				//The scale is derived from the scale applied to the orientation
				double xScale = newTransform.orientation.right.calculateMagnitude();
				double yScale = newTransform.orientation.up.calculateMagnitude();
				double zScale = newTransform.orientation.backward.calculateMagnitude();
				//Create a scale that will remove the implicit scale
				Vector3 transformScale = new Vector3( xScale, yScale, zScale );
				//Apply the scale
				newTransform.translation.multiply( transformScale );
				//Normalize the orientation to remove the implicit scale
				newTransform.orientation.normalizeColumns();
				//Re-invert the transform and set the new value
				newTransform.invert();
				
				
				
				iawp.setWeights(jointAndWeights.getValue());
				iawp.setInverseAbsoluteTransformation(newTransform);
				weightInfo.addReference( jointId, iawp );
			}
		}
    	return weightInfo;
	}
	
	private static void printWeightInfo( WeightInfo wi ) {
		for (Entry<String, InverseAbsoluteTransformationWeightsPair> entry : wi.getMap().entrySet()) {
			InverseAbsoluteTransformationWeightsPair iatwp = entry.getValue();
			Point3 t = iatwp.getInverseAbsoluteTransformation().translation;
			OrthogonalMatrix3x3 o = iatwp.getInverseAbsoluteTransformation().orientation;
			
			System.out.println( entry.getKey()+":");
			System.out.println( " inverse transform = ("+t.x+", "+t.y+", "+t.z+"), [["+o.right.x+", "+o.right.y+", "+o.right.z+"], ["+o.up.x+", "+o.up.y+", "+o.up.z+"], ["+o.backward.x+", "+o.backward.y+", "+o.backward.z+"]]");
			List<Float> weights = new ArrayList<Float>();
			List<Integer> indices = new ArrayList<Integer>();
			while (!iatwp.isDone()) {
				weights.add( iatwp.getWeight() );
				indices.add(  iatwp.getIndex() );
				iatwp.advance();
			}
			System.out.println("  weight count = "+weights.size());
			System.out.println("  weights: "+weights);
			System.out.println( " indices: "+indices);
		}
	}
	
	private static Mesh createAliceSGMeshFromGeometry( Geometry geometry, Collada colladaModel ) throws ModelLoadingException {
		
		Controller meshController = getControllerForGeometry(geometry, colladaModel);
		Mesh sgMesh;
		if (meshController != null) {
			sgMesh = new WeightedMesh();
		}
		else {
			sgMesh = new Mesh();
		}
        sgMesh.setName(geometry.getName());
        sgMesh.normalBuffer.setValue(Buffers.newDirectFloatBuffer(geometry.getMesh().getNormalData()));
        float[] vertexData = geometry.getMesh().getPositionData();
        double[] doubleVertexData = new double[vertexData.length];
        for (int i=0; i<vertexData.length; i++) {
        	doubleVertexData[i] = vertexData[i];
        }
        sgMesh.vertexBuffer.setValue( Buffers.newDirectDoubleBuffer(doubleVertexData) );
        sgMesh.textCoordBuffer.setValue( Buffers.newDirectFloatBuffer(geometry.getMesh().getTexCoordData()) );
        
        //Find the triangle data and use it to set the index data
        Triangles tris = null;
        for (Primitives p : geometry.getMesh().getPrimitives()) {
        	if (p instanceof Triangles) {
        		if (tris == null) {
        			tris = (Triangles)p;
        		}
        		else {
        			LOGGER.log( Level.WARNING, "Converting mesh '"+geometry.getName()+"', Unsupported primitive count: Found extra triangle primitives, only processing the first.");
        		}
        	}
        	else {
        		LOGGER.log( Level.WARNING, "Converting mesh '"+geometry.getName()+"', Unsupported primitive type: Skipping primitive of type "+p.getClass()+".");
        	}
        }
        if (tris == null) {
        	throw new ModelLoadingException( "Error conversing mesh "+geometry.getName()+", no triangle primitive data found." );
        }
		int[] triangleIndexData = tris.getData();
        sgMesh.indexBuffer.setValue( Buffers.newDirectIntBuffer(triangleIndexData) );
        sgMesh.textureId.setValue( getMaterialIdForMaterialName( tris.getMaterial(), colladaModel ) );
        
        if (meshController != null && sgMesh instanceof WeightedMesh) {
        	WeightInfo weightInfo = createWeightInfoForController( meshController );
        	
        	//Since this is weighted mesh, we need to transform the mesh data into the bind space
        	AffineMatrix4x4 bindMatrix = floatArrayToAliceMatrix( meshController.getSkin().getBindShapeMatrix() );
        	double[] bindSpaceVertexData = new double[vertexData.length];
            for (int i=0; i<vertexData.length; i+=3) {
            	bindMatrix.transformVertex( bindSpaceVertexData, i, doubleVertexData, i );
            }
            sgMesh.vertexBuffer.setValue( Buffers.newDirectDoubleBuffer(bindSpaceVertexData) );
        	
        	
        	((WeightedMesh)sgMesh).weightInfo.setValue( weightInfo );
        	System.out.println("\nCollada weight info for "+geometry.getName());
        	printWeightInfo( weightInfo );
        	
        	
//        	SkeletonVisual fish = loadAliceModel( ClownFishResource.DEFAULT );
//        	int count = 0;
//        	for (WeightedMesh wm : fish.weightedMeshes.getValue()) {
//        		System.out.println("\nAlice weight info for "+count++);
//        		WeightInfo wi = wm.weightInfo.getValue();
//        		printWeightInfo( wi );
//        	}
        }
        
        return sgMesh;
	}
	
	private static List<Mesh> createAliceMeshesFromCollada( Collada colladaModel ) throws ModelLoadingException {
		List<Geometry> geometries = colladaModel.getLibraryGeometries().getGeometries();
		List<Mesh> meshes = new ArrayList<Mesh>();
		for (Geometry geometry : geometries) {
			Mesh mesh = createAliceSGMeshFromGeometry( geometry, colladaModel );
			meshes.add( mesh );
		}
		return meshes;
	}
	
	public static SkeletonVisual loadAliceModel( JointedModelResource resource ) {
		VisualData< JointedModelResource > v = ImplementationAndVisualType.ALICE.getFactory( resource ).createVisualData();
		SkeletonVisual sv = (SkeletonVisual)v.getSgVisuals()[0];
		return sv;
	}
	
	private static Texture getAliceTexture(BufferedImage image) {
		boolean flipImage = true;
		edu.cmu.cs.dennisc.texture.BufferedImageTexture aliceTexture = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
		BufferedImage tex;
		if (flipImage) {
			try {
				int type;
				if (image.getTransparency() == BufferedImage.OPAQUE) {
					type = BufferedImage.TYPE_3BYTE_BGR;
				}
				else {
					type = BufferedImage.TYPE_4BYTE_ABGR;
				}
				tex = new BufferedImage(image.getWidth(null),
						image.getHeight(null), type);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
			image.getWidth(null);
			image.getHeight(null);

			if (image instanceof BufferedImage) {
				int imageWidth = image.getWidth(null);
				int[] tmpData = new int[imageWidth];
				int row = 0;
				BufferedImage bufferedImage = ((BufferedImage) image);
				for (int y = image.getHeight(null) - 1; y >= 0; y--) {
					bufferedImage.getRGB(0, (flipImage ? row++ : y),
							imageWidth, 1, tmpData, 0, imageWidth);
					tex.setRGB(0, y, imageWidth, 1, tmpData, 0, imageWidth);
				}
			} else {
				AffineTransform tx = null;
				if (flipImage) {
					tx = AffineTransform.getScaleInstance(1, -1);
					tx.translate(0, -image.getHeight(null));
				}
				Graphics2D g = (Graphics2D) tex.getGraphics();
				g.drawImage(image, tx, null);
				g.dispose();
			}

		} else {
			tex = (BufferedImage) image;
		}
		aliceTexture.setBufferedImage(tex);
		return aliceTexture;
	}
	
	private static List<edu.cmu.cs.dennisc.scenegraph.TexturedAppearance> createAliceMaterialsFromCollada( Collada colladaModel, File rootPath, List<Mesh> aliceMeshes ) throws ModelLoadingException {
		List<Material> materials = colladaModel.getLibraryMaterials().getMaterials();
		List<edu.cmu.cs.dennisc.scenegraph.TexturedAppearance> textureAppearances = new LinkedList<edu.cmu.cs.dennisc.scenegraph.TexturedAppearance>();
		for (Material material : materials) {
			int id = getMaterialIdForMaterialName( material.getName(), colladaModel );
			boolean isUsed = false;
			for (Mesh aliceMesh : aliceMeshes) {
				if (aliceMesh.textureId.getValue() == id) {
					isUsed = true;
					break;
				}
			}
			if (isUsed) {
				Image image = getColladaImageForMaterial( material, colladaModel );
				BufferedImage bufferedImage;
				try {
					File imageFile = resolveTextureFileName( image.getInitFrom(), rootPath );
					bufferedImage = ImageUtilities.read( imageFile );
				} catch( IOException e) {
					throw new ModelLoadingException("Error loading texture: "+image.getInitFrom()+" not found.", e);
				}
				edu.cmu.cs.dennisc.scenegraph.TexturedAppearance m_sgAppearance = new edu.cmu.cs.dennisc.scenegraph.TexturedAppearance();
				m_sgAppearance.diffuseColorTexture.setValue( getAliceTexture(bufferedImage) );
				m_sgAppearance.textureId.setValue(id);
				textureAppearances.add(m_sgAppearance);
			}
			else {
				LOGGER.log( Level.WARNING, "Loading materials: Skipping unreferenced material "+material.getName());
			}
		}
		if (textureAppearances.size() == 0) {
			throw new ModelLoadingException("No supported materials found. Alice models must have a texture.");
		}
		return textureAppearances;
	}
	
	private static File resolveTextureFileName(String textureFileName, File rootPath) {
		//Strip URI info. This seems to be inconsistent with java.net.URI and therefore it's easier to discard it
		if (textureFileName.startsWith( "file://" )) {
			textureFileName = textureFileName.substring( 7 );
		}
		else if (textureFileName.startsWith( "file:///" )) {
			textureFileName = textureFileName.substring( 8 );
		}
		
		//Check for texture file
		File textureFile = new File( textureFileName );
		if (textureFile.exists()) {
			return textureFile;
		}
		//If texture file isn't found, check in the root directory (the directory where the collada file is loaded from)
		File localFile = new File( rootPath, textureFile.getName() );
		if (localFile.exists()) {
			return localFile;
		}
		
		return null;
	}
	
	private static Image getColladaImageForMaterial( Material material, Collada colladaModel ) {
		InstanceEffect ie = material.getInstanceEffect();
		Effect effect = colladaModel.findEffect( ie.getUrl() );
		String textureName = effect.getEffectMaterial().getDiffuse().getTexture().getTexture();
		return colladaModel.findImage( textureName );
	}
	
	private static AffineMatrix4x4 flipTransform( AffineMatrix4x4 transform ) {		
		transform.orientation.right.y *= -1;
		
		transform.orientation.up.x *= -1;
		transform.orientation.up.z *= -1;
		
		transform.orientation.backward.y *= -1;
		
		transform.translation.x *= -1;
		transform.translation.z *= -1;

		return transform;
	}
	
	private static AffineMatrix4x4 scaleTransform( AffineMatrix4x4 transform, Vector3 scale ) {
		transform.translation.multiply( scale );
		return transform;
	}
	
	private static void flipJoints( Joint j ) {
		AffineMatrix4x4 newTransform = new AffineMatrix4x4( j.localTransformation.getValue() );
		flipTransform( newTransform );
		j.localTransformation.setValue( newTransform );
		for( int i = 0; i < j.getComponentCount(); i++ )
		{
			Component comp = j.getComponentAt( i );
			if (comp instanceof Joint) {
				flipJoints((Joint)comp);
			}
		}
	}
	
	private static void scaleJoints( Joint j, Vector3 scale) {
		AffineMatrix4x4 newTransform = new AffineMatrix4x4( j.localTransformation.getValue() );
		scaleTransform( newTransform, scale );
		j.localTransformation.setValue( newTransform );
		for( int i = 0; i < j.getComponentCount(); i++ )
		{
			Component comp = j.getComponentAt( i );
			if (comp instanceof Joint) {
				scaleJoints((Joint)comp, scale);
			}
		}
	}
	
	private static Mesh flipMesh( Mesh mesh ) {
		double[] vertices = BufferUtilities.convertDoubleBufferToArray( mesh.vertexBuffer.getValue() );
		double[] newVertices = new double[vertices.length];
		for (int i=0; i<vertices.length; i+=3) {
			newVertices[i] = vertices[i] * -1;
			newVertices[i+1] = vertices[i+1];
			newVertices[i+2] = vertices[i+2] * -1;
		}
		mesh.vertexBuffer.setValue( Buffers.newDirectDoubleBuffer(newVertices) );
		
		float[] normals = BufferUtilities.convertFloatBufferToArray( mesh.normalBuffer.getValue() );
		float[] newNormals = new float[normals.length];
		for (int i=0; i<normals.length; i+=3) {
			newNormals[i] = normals[i] * -1;
			newNormals[i+1] = normals[i+1];
			newNormals[i+2] = normals[i+2] * -1;
		}
		mesh.normalBuffer.setValue( Buffers.newDirectFloatBuffer(newNormals) );
		
		return mesh;
	}
	
	private static Mesh scaleMesh( Mesh mesh, Vector3 scale) {
		double[] vertices = BufferUtilities.convertDoubleBufferToArray( mesh.vertexBuffer.getValue() );
		double[] newVertices = new double[vertices.length];
		for (int i=0; i<vertices.length; i+=3) {
			newVertices[i] = vertices[i] * scale.x;
			newVertices[i+1] = vertices[i+1] * scale.y;
			newVertices[i+2] = vertices[i+2] * scale.z;
		}
		mesh.vertexBuffer.setValue( Buffers.newDirectDoubleBuffer(newVertices) );
		return mesh;
	}
	
	private static WeightInfo flipWeightInfo( WeightInfo weightInfo) {
		Map<String, InverseAbsoluteTransformationWeightsPair> mapReferencesToInverseAbsoluteTransformationWeightsPairs = weightInfo.getMap();
		for (Entry<String, InverseAbsoluteTransformationWeightsPair> pair : mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet()) {
			InverseAbsoluteTransformationWeightsPair iatwp = pair.getValue();
			AffineMatrix4x4 originalTransform = AffineMatrix4x4.createInverse( iatwp.getInverseAbsoluteTransformation() );
			AffineMatrix4x4 newTransform = flipTransform( originalTransform );
			newTransform.invert();
			iatwp.setInverseAbsoluteTransformation( newTransform );
		}
		return weightInfo;
	}
	
	private static WeightInfo scaleWeightInfo( WeightInfo weightInfo, Vector3 scale) {
		Map<String, InverseAbsoluteTransformationWeightsPair> mapReferencesToInverseAbsoluteTransformationWeightsPairs = weightInfo.getMap();
		for (Entry<String, InverseAbsoluteTransformationWeightsPair> pair : mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet()) {
			InverseAbsoluteTransformationWeightsPair iatwp = pair.getValue();
			AffineMatrix4x4 originalInverseTransform = iatwp.getInverseAbsoluteTransformation();
			AffineMatrix4x4 newTransform = AffineMatrix4x4.createInverse( originalInverseTransform );
			//These need to have the scale removed just from the translation
			newTransform.translation.multiply( scale );
			newTransform.invert();
			iatwp.setInverseAbsoluteTransformation( newTransform );
		}
		return weightInfo;
	}
	
	private static WeightInfo removeImplicitScaleFromWeightInfo( WeightInfo weightInfo ) {
		Map<String, InverseAbsoluteTransformationWeightsPair> mapReferencesToInverseAbsoluteTransformationWeightsPairs = weightInfo.getMap();
		for (Entry<String, InverseAbsoluteTransformationWeightsPair> pair : mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet()) {
			InverseAbsoluteTransformationWeightsPair iatwp = pair.getValue();
			AffineMatrix4x4 originalInverseTransform = iatwp.getInverseAbsoluteTransformation();
			//Create the new transform by inverting the existing one so that we're working in untransformed space
			AffineMatrix4x4 newTransform = AffineMatrix4x4.createInverse( originalInverseTransform );
			//Since these are absolute transforms, they need to have the scale removed both from the translation and the orientaion
			//The scale is derived from the scale applied to the orientation
			double xScale = newTransform.orientation.right.calculateMagnitude();
			double yScale = newTransform.orientation.up.calculateMagnitude();
			double zScale = newTransform.orientation.backward.calculateMagnitude();
			//Create a scale that will remove the implicit scale
			Vector3 transformScale = new Vector3( xScale, yScale, zScale );
			//Apply the scale
			newTransform.translation.multiply( transformScale );
			//Normalize the orientation to remove the implicit scale
			newTransform.orientation.normalizeColumns();
			//Re-invert the transform and set the new value
			newTransform.invert();
			iatwp.setInverseAbsoluteTransformation( newTransform );
		}
		return weightInfo;
	}
	
	
	private static void flipAliceModel(SkeletonVisual sv) {
		flipJoints(sv.skeleton.getValue());
		for (edu.cmu.cs.dennisc.scenegraph.Geometry g : sv.geometries.getValue()) {
			//The collada import pipeline only supports meshes, so we only need to worry about transforming meshes
			//If we start to support things like cylinders and boxes, then this would need to be updated
			if (g instanceof Mesh) {
				Mesh m = (Mesh)g;
				flipMesh(m);
			}
		}
		for (WeightedMesh wm : sv.weightedMeshes.getValue()) {
			flipMesh(wm);
			flipWeightInfo(wm.weightInfo.getValue());
		}
	}
	
	private static void scaleAliceModel(SkeletonVisual sv, Vector3 scale) {
		scaleJoints(sv.skeleton.getValue(), scale);
		for (edu.cmu.cs.dennisc.scenegraph.Geometry g : sv.geometries.getValue()) {
			//The collada import pipeline only supports meshes, so we only need to worry about transforming meshes
			//If we start to support things like cylinders and boxes, then this would need to be updated
			if (g instanceof Mesh) {
				Mesh m = (Mesh)g;
				scaleMesh(m, scale);
			}
		}
		for (WeightedMesh wm : sv.weightedMeshes.getValue()) {
			scaleMesh(wm, scale);
			scaleWeightInfo(wm.weightInfo.getValue(), scale);
		}
	}
	
	private static void removeImplicitScaleFromSkeleton( Joint joint, Vector3 scale) {
		Vector3 localScale = getImplicitScale(joint);
		AffineMatrix4x4 scaledTransform = joint.localTransformation.getValue();
		scaledTransform.translation.multiply( scale );
		scaledTransform.orientation.normalizeColumns();
		joint.localTransformation.setValue( scaledTransform );
		Vector3 newScale = Vector3.createMultiplication( scale, localScale );
		for( int i = 0; i < joint.getComponentCount(); i++ )
		{
			Component comp = joint.getComponentAt( i );
			if (comp instanceof Joint) {
				removeImplicitScaleFromSkeleton((Joint)comp, newScale);
			}
		}
		
	}
	
	private static void removeImplicitScale(SkeletonVisual sv) {
		//Implicit scale is set on the orientation of the root joint of the skeleton
		Vector3 rootScale = getImplicitScale( sv.skeleton.getValue() );
		removeImplicitScaleFromSkeleton(sv.skeleton.getValue(), new Vector3(1,1,1));
		
//		
//		//First step is to normalize the orientation
//		AffineMatrix4x4 rootTransform = sv.skeleton.getValue().getLocalTransformation();
//		rootTransform.orientation.normalizeColumns();
//		sv.skeleton.getValue().localTransformation.setValue( rootTransform );
//		//Scale all the child joints. This skips the translation of the root joint which should not be scaled
//		for( int i = 0; i < sv.skeleton.getValue().getComponentCount(); i++ )
//		{
//			Component comp = sv.skeleton.getValue().getComponentAt( i );
//			if (comp instanceof Joint) {
//				scaleJoints((Joint)comp, scale);
//			}
//		}
		for (edu.cmu.cs.dennisc.scenegraph.Geometry g : sv.geometries.getValue()) {
			//The collada import pipeline only supports meshes, so we only need to worry about transforming meshes
			//If we start to support things like cylinders and boxes, then this would need to be updated
			if (g instanceof Mesh) {
				Mesh m = (Mesh)g;
				scaleMesh(m, rootScale);
			}
		}
		for (WeightedMesh wm : sv.weightedMeshes.getValue()) {
//			scaleMesh(wm, scale);
//			removeImplicitScaleFromWeightInfo(wm.weightInfo.getValue());
		}
	}
	
	private static Vector3 getImplicitScale( Joint skeleton ) {
		OrthogonalMatrix3x3 orientation = skeleton.localTransformation.getValue().orientation;
		double scaleX = orientation.right.calculateMagnitude();
		double scaleY = orientation.up.calculateMagnitude();
		double scaleZ = orientation.backward.calculateMagnitude();
		
		return new Vector3( scaleX, scaleY, scaleZ );
	}
	
	private static void printJoints( Joint j, String indent) {
		System.out.println( indent+"Joint "+j.jointID.getValue() );
		PrintUtilities.print( indent+"    local transform: ", j.localTransformation.getValue().translation, j.localTransformation.getValue().orientation );
		System.out.println();
		AffineMatrix4x4 absoluteTransform = j.getAbsoluteTransformation();
		PrintUtilities.print( indent+" absolute transform: ", absoluteTransform.translation, absoluteTransform.orientation );
		System.out.println();
		for( int i = 0; i < j.getComponentCount(); i++ )
		{
			Component comp = j.getComponentAt( i );
			if (comp instanceof Joint) {
				printJoints((Joint)comp, indent+"  ");
			}
		}
	}
	
	public static SkeletonVisual loadAliceModelFromCollada(File colladaModelFile, String modelName, Logger modelLoadingLogger) throws ModelLoadingException {
		
		assert modelLoadingLogger != null;
		AliceColladaModelLoader.LOGGER = modelLoadingLogger;
		Collada colladaModel;
		try {
			colladaModel = Collada.readFile( colladaModelFile.getAbsolutePath() );
		} catch( SAXException | IOException e ) {
			throw new ModelLoadingException("Failed to load collada file "+colladaModelFile, e);
		}
		colladaModel.deindexMeshes();
		colladaModel.dump( System.out, 0 );
		
		
		File rootPath = colladaModelFile.getParentFile();
		
		VisualScene scene = colladaModel.getLibraryVisualScenes().getScene( colladaModel.getScene().getInstanceVisualScene().getUrl() );
		//Find and build the skeleton first
		Node rootNode = findRootNode(scene.getNodes());
		Joint aliceSkeleton = createAliceSkeletonFromNode( rootNode );
		printJoints(aliceSkeleton, "");
		
		
		List<Mesh> aliceMeshes = createAliceMeshesFromCollada( colladaModel );
		
		SkeletonVisual skeletonVisual = new SkeletonVisual();
		skeletonVisual.setName( modelName );
		skeletonVisual.frontFacingAppearance.setValue( new SimpleAppearance() );
		skeletonVisual.skeleton.setValue( aliceSkeleton );
		List<Mesh> aliceGeometry = new ArrayList<Mesh>();
		List<WeightedMesh> aliceWeightedMeshes = new ArrayList<WeightedMesh>();
		//Loop through the meshes and divide them into lists of regular meshes and weighted meshes
		for (Mesh mesh : aliceMeshes) {
			if (mesh instanceof WeightedMesh) {
				WeightedMesh weightedMesh = (WeightedMesh)mesh;
				//Link the weighted mesh to the skeleton
				weightedMesh.skeleton.setValue( aliceSkeleton );
				aliceWeightedMeshes.add( weightedMesh );
			}
			else {
				aliceGeometry.add( mesh );
			}
		}
		
		skeletonVisual.geometries.setValue( aliceGeometry.toArray( new Mesh[aliceGeometry.size()] ) );
		skeletonVisual.weightedMeshes.setValue( aliceWeightedMeshes.toArray( new WeightedMesh[aliceWeightedMeshes.size()] ) );
		
		
		removeImplicitScale( skeletonVisual );
		
		printJoints(aliceSkeleton, "");
//		Vector3 implicitScale = getImplicitScale(aliceSkeleton);
//		if (implicitScale.x != 1.0 || implicitScale.y != 1.0 || implicitScale.z != 1.0) {
//			
//		}
		float extraScale = colladaModel.getUnit().getMeter();
		if (extraScale != 1.0f) {
			scaleAliceModel( skeletonVisual, new Vector3(extraScale, extraScale, extraScale));
		}
		flipAliceModel( skeletonVisual );
		
		
		List<edu.cmu.cs.dennisc.scenegraph.TexturedAppearance> sgTextureAppearances = createAliceMaterialsFromCollada( colladaModel, rootPath, aliceMeshes );
		skeletonVisual.textures.setValue(sgTextureAppearances.toArray(new TexturedAppearance[sgTextureAppearances.size()]));
		
		UtilitySkeletonVisualAdapter skeletonVisualAdapter = new UtilitySkeletonVisualAdapter();
		skeletonVisualAdapter.initialize(skeletonVisual);
		skeletonVisualAdapter.processWeightedMesh();
		skeletonVisualAdapter.initializeJointBoundingBoxes(skeletonVisual.skeleton
				.getValue());
		AxisAlignedBox absoluteBBox = skeletonVisualAdapter
				.getAbsoluteBoundingBox();
		if (skeletonVisual.geometries.getValue() != null) {
			for (edu.cmu.cs.dennisc.scenegraph.Geometry g : skeletonVisual.geometries
					.getValue()) {
				edu.cmu.cs.dennisc.math.AxisAlignedBox b = g
						.getAxisAlignedMinimumBoundingBox();
				absoluteBBox.union(b);
			}
		}
		skeletonVisual.baseBoundingBox.setValue(absoluteBBox);
		skeletonVisualAdapter.handleReleased();
		skeletonVisual.setTracker( null );
		
		return skeletonVisual;
	}
}
