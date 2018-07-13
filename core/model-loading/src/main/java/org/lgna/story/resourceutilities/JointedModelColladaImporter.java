package org.lgna.story.resourceutilities;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.controller.VertexWeights;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.NewParam;
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
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
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
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;
import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.xml.sax.SAXException;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JointedModelColladaImporter {

//	private static Logger LOGGER;

	private boolean FLIP_MODEL = true;
	private final File colladaModelFile;
	private final String modelName;
	private final Logger modelLoadingLogger;
	private final File rootPath;

	public JointedModelColladaImporter(File colladaModelFile, String modelName, Logger modelLoadingLogger) {
		this.colladaModelFile = colladaModelFile;
		this.modelName = modelName;
		this.modelLoadingLogger = modelLoadingLogger;
		this.rootPath = colladaModelFile.getParentFile();
	}

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

	public void setFlipModel(boolean flipModel) {
		this.FLIP_MODEL = flipModel;
	}

	private static AffineMatrix4x4 floatArrayToAliceMatrix( float[] floatData ) throws ModelLoadingException {
		double[] doubleData = new double[floatData.length];
		for (int i=0; i<floatData.length; i++) {
			doubleData[i] = floatData[i];
		}
		if (doubleData.length == 12) {
			return AffineMatrix4x4.createFromRowMajorArray12(doubleData );
		}
		else if (doubleData.length == 16) {
			return AffineMatrix4x4.createFromRowMajorArray16(doubleData );
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
    		throw new ModelLoadingException( "Error converting mesh "+meshController.getName()+", no joint data found on mesh skin." );
    	}
    
    	//Array of inverse bind matrices for all the referenced joints. Is indexed into by the joint index.
    	float[] inverseBindMatrixData = skin.getInvBindMatrixData();
    	if (inverseBindMatrixData == null) {
    		throw new ModelLoadingException( "Error converting mesh "+meshController.getName()+", no inverse bind matrix data found on mesh skin." );
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

	private Mesh createAliceSGMeshFromGeometry( Geometry geometry, Collada colladaModel ) throws ModelLoadingException {

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
        			modelLoadingLogger.log( Level.WARNING, "Converting mesh '"+geometry.getName()+"', Unsupported primitive count: Found extra triangle primitives, only processing the first.");
        		}
        	}
        	else {
				modelLoadingLogger.log( Level.WARNING, "Converting mesh '"+geometry.getName()+"', Unsupported primitive type: Skipping primitive of type "+p.getClass()+".");
        	}
        }
        if (tris == null) {
        	throw new ModelLoadingException( "Error converting mesh "+geometry.getName()+", no triangle primitive data found." );
        }
		int[] triangleIndexData = tris.getData();
        sgMesh.indexBuffer.setValue( Buffers.newDirectIntBuffer(triangleIndexData) );
        sgMesh.textureId.setValue( getMaterialIdForMaterialName( tris.getMaterial(), colladaModel ) );
        
        if (meshController != null && sgMesh instanceof WeightedMesh) {
        	WeightInfo weightInfo = createWeightInfoForController( meshController );
        
        	//Since this is weighted mesh, we need to transform the mesh data into the bind space
			float[] bindMatrixData = meshController.getSkin().getBindShapeMatrix();
        	AffineMatrix4x4 bindMatrix;
        	if (bindMatrixData != null ) {
        		bindMatrix = floatArrayToAliceMatrix( bindMatrixData );
			}
			else {
        		bindMatrix = AffineMatrix4x4.createIdentity();
			}
        	double[] bindSpaceVertexData = new double[vertexData.length];
            for (int i=0; i<vertexData.length; i+=3) {
            	bindMatrix.transformVertex( bindSpaceVertexData, i, doubleVertexData, i );
            }
            sgMesh.vertexBuffer.setValue( Buffers.newDirectDoubleBuffer(bindSpaceVertexData) );

        	((WeightedMesh)sgMesh).weightInfo.setValue( weightInfo );
        }
        
        return sgMesh;
	}

	private List<Mesh> createAliceMeshesFromCollada( Collada colladaModel ) throws ModelLoadingException {
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
		BufferedImageTexture aliceTexture = new BufferedImageTexture();
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

	private List<TexturedAppearance> createAliceMaterialsFromCollada( Collada colladaModel, File rootPath, List<Mesh> aliceMeshes ) throws ModelLoadingException {
		List<Material> materials = colladaModel.getLibraryMaterials().getMaterials();
		List<TexturedAppearance> textureAppearances = new LinkedList<TexturedAppearance>();
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
				TexturedAppearance m_sgAppearance = new TexturedAppearance();
				m_sgAppearance.diffuseColorTexture.setValue( getAliceTexture(bufferedImage) );
				m_sgAppearance.textureId.setValue(id);
				textureAppearances.add(m_sgAppearance);
			}
			else {
				modelLoadingLogger.log( Level.WARNING, "Loading materials: Skipping unreferenced material "+material.getName());
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

		NewParam textureParam = effect.findNewParam(textureName);
		while (textureParam != null) {
			if (textureParam.getSurface() != null) {
				textureName = textureParam.getSurface().getInitFrom();
				textureParam = null;
				break;
			}
			else if (textureParam.getSampler2D() != null) {
				textureParam = effect.findNewParam(textureParam.getSampler2D().getSource());
			}
			else {
				textureParam = null;
			}
		}

		return colladaModel.findImage( textureName );
	}


	private static void flipJoints( Joint j ) {
		AffineMatrix4x4 newTransform = new AffineMatrix4x4( j.localTransformation.getValue() );
		newTransform = ColladaTransformUtilities.createFlippedAffineTransform( newTransform );
		j.localTransformation.setValue( newTransform );
		for( int i = 0; i < j.getComponentCount(); i++ )
		{
			Component comp = j.getComponentAt( i );
			if (comp instanceof Joint) {
				flipJoints((Joint)comp);
			}
		}
	}

	private static Mesh flipMesh( Mesh mesh ) {
		double[] vertices = BufferUtilities.convertDoubleBufferToArray( mesh.vertexBuffer.getValue() );
		double[] newVertices = ColladaTransformUtilities.createFlippedPoint3DoubleArray(vertices);
		mesh.vertexBuffer.setValue( Buffers.newDirectDoubleBuffer(newVertices) );

		float[] normals = BufferUtilities.convertFloatBufferToArray( mesh.normalBuffer.getValue() );
		float[] newNormals = ColladaTransformUtilities.createFlippedPoint3FloatArray(normals);
		mesh.normalBuffer.setValue( Buffers.newDirectFloatBuffer(newNormals) );

		return mesh;
	}

	private static WeightInfo flipWeightInfo( WeightInfo weightInfo) {
		Map<String, InverseAbsoluteTransformationWeightsPair> mapReferencesToInverseAbsoluteTransformationWeightsPairs = weightInfo.getMap();
		for (Entry<String, InverseAbsoluteTransformationWeightsPair> pair : mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet()) {
			InverseAbsoluteTransformationWeightsPair iatwp = pair.getValue();
			AffineMatrix4x4 originalTransform = AffineMatrix4x4.createInverse( iatwp.getInverseAbsoluteTransformation() );
			AffineMatrix4x4 newTransform = ColladaTransformUtilities.createFlippedAffineTransform( originalTransform );
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


	/**
	 * 	Alice models are in a different geometric space than maya models
	 *  Models must have their space transformed so that the look correct in Alice
	 */
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

	//Looks at the transforms in the skeleton and removes any scaling found in the orientation matrices
	//Scaling is removed by applying that scale to the children of that joint and normalizing the matrix
	private static void removeImplicitScale(SkeletonVisual sv) {
		//Implicit scale is set on the orientation of the root joint of the skeleton
		Vector3 rootScale = getImplicitScale( sv.skeleton.getValue() );
		removeImplicitScaleFromSkeleton(sv.skeleton.getValue(), new Vector3(1,1,1));
		for (edu.cmu.cs.dennisc.scenegraph.Geometry g : sv.geometries.getValue()) {
			//The collada import pipeline only supports meshes, so we only need to worry about transforming meshes
			//If we start to support things like cylinders and boxes, then this would need to be updated
			if (g instanceof Mesh) {
				((Mesh)g).scale(rootScale);
			}
		}

	}

	private static Vector3 getImplicitScale( Joint skeleton ) {
		OrthogonalMatrix3x3 orientation = skeleton.localTransformation.getValue().orientation;
		double scaleX = orientation.right.calculateMagnitude();
		double scaleY = orientation.up.calculateMagnitude();
		double scaleZ = orientation.backward.calculateMagnitude();

		return new Vector3( scaleX, scaleY, scaleZ );
	}

	private Collada readColladaModel() throws ModelLoadingException {
		Collada colladaModel;
		try {
			colladaModel = Collada.readFile( colladaModelFile.getAbsolutePath() );
		} catch( SAXException | IOException e ) {
			throw new ModelLoadingException("Failed to load collada file "+colladaModelFile, e);
		}
		colladaModel.deindexMeshes();
		return colladaModel;
	}

	public SkeletonVisual loadSkeletonVisual() throws ModelLoadingException {
		assert modelLoadingLogger != null;

		Collada colladaModel = readColladaModel();

		VisualScene scene = colladaModel.getLibraryVisualScenes().getScene( colladaModel.getScene().getInstanceVisualScene().getUrl() );
		//Find and build the skeleton first
		Node rootNode = findRootNode(scene.getNodes());
		Joint aliceSkeleton = createAliceSkeletonFromNode( rootNode );

		//Find and build meshes (both static and weighted) from the collada model
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

		//Remove any scale from the model
		removeImplicitScale( skeletonVisual );
		float extraScale = colladaModel.getUnit().getMeter();
		if (extraScale != 1.0f) {
			skeletonVisual.scale(new Vector3(extraScale, extraScale, extraScale));
		}
		//Convert the model from maya/collada space to Alice space
		if (FLIP_MODEL) {
			flipAliceModel(skeletonVisual);
		}


		List<TexturedAppearance> sgTextureAppearances = createAliceMaterialsFromCollada( colladaModel, rootPath, aliceMeshes );
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
				AxisAlignedBox b = g
						.getAxisAlignedMinimumBoundingBox();
				absoluteBBox.union(b);
			}
		}
		skeletonVisual.baseBoundingBox.setValue(absoluteBBox);
		skeletonVisualAdapter.handleReleased();
		skeletonVisual.setTracker( null );

		return skeletonVisual;
	}


/*
 *  Convenience methods for debugging
 */


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
}
