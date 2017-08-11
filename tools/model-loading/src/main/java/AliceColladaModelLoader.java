import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.lgna.story.implementation.JointedModelImp.VisualData;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.flyer.ChickenResource;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Primitives;
import com.dddviewr.collada.geometry.Triangles;
import com.dddviewr.collada.images.Image;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.BaseXform;
import com.dddviewr.collada.visualscene.Matrix;
import com.dddviewr.collada.visualscene.VisualScene;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.nebulous.javabased.Utilities;
import edu.cmu.cs.dennisc.nebulous.javabased.VertexMapData;
import edu.cmu.cs.dennisc.nebulous.javabased.Mesh.MeshType;
import edu.cmu.cs.dennisc.scenegraph.Indices;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;

public class AliceColladaModelLoader {
	
	private static boolean nodeIsJoint( Node n ) {
		return n.getType().equals( "JOINT" );
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
	
	private static AffineMatrix4x4 colladaMatrixToAliceMatrix( Matrix m ) throws ModelLoadingException {
		float[] floatData = m.getData();
		double[] doubleData = new double[floatData.length];
		for (int i=0; i<floatData.length; i++) {
			doubleData[i] = floatData[i];
		}
		if (doubleData.length == 12) {
			return AffineMatrix4x4.createFromColumnMajorArray12( doubleData );
		}
		else if (doubleData.length == 16) {
			return AffineMatrix4x4.createFromColumnMajorArray16( doubleData );
		}
		else {
			throw new ModelLoadingException("Error converting collada matrix to Alice matrix. Expected array of size 12 or 16, instead got "+floatData.length);
		}
	}
	
	private static Joint createAliceSkeletonFromNode( Node node ) throws ModelLoadingException {
		Matrix m = null;
		BaseXform xform = node.getXforms().get( 0 );
		if (xform instanceof Matrix) {
			m = (Matrix)xform;
		}
		Joint j = new Joint();
		j.jointID.setValue( node.getName() );
		j.setName( node.getName() );
		j.localTransformation.setValue( colladaMatrixToAliceMatrix( m ) );
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
	
	private static Mesh createAliceSGMeshFromGeometry( Geometry geometry, Collada collada ) {
		VisualData< ChickenResource > chicken = loadAliceChicken();
		SkeletonVisual sv = (SkeletonVisual)chicken.getSgVisuals()[0];
		for (int i=0; i<sv.weightedMeshes.getValue().length; i++) {
			int[] indexBuffer = Utilities.convertIntBufferToArray( sv.weightedMeshes.getValue()[i].indexBuffer.getValue() );
			int indexCount = indexBuffer.length;
			int triangleCount = indexCount / 3;
			float[] normalBuffer = Utilities.convertFloatBufferToArray( sv.weightedMeshes.getValue()[i].normalBuffer.getValue() );
			int normalCount = normalBuffer.length / 3;
			double[] vertexBuffer = Utilities.convertDoubleBufferToArray( sv.weightedMeshes.getValue()[i].vertexBuffer.getValue() );
			int vertexCount = vertexBuffer.length / 3;
			float[] uvBuffer = Utilities.convertFloatBufferToArray(sv.weightedMeshes.getValue()[i].textCoordBuffer.getValue() );
			int uvCount = uvBuffer.length / 2;
			System.out.println( "here" );
		}
		
		getGeometryInfo(geometry);
		Triangles tris = (Triangles)geometry.getMesh().getPrimitives().get( 0 );
		int triData = tris.getData().length;
		geometry.getMesh().deindex( collada );
		getGeometryInfo(geometry);
		
		Triangles tris2 = (Triangles)geometry.getMesh().getPrimitives().get( 0 );
		int triData2 = tris2.getData().length;
		
        Mesh sgMesh = new Mesh();
        sgMesh.setName(geometry.getName());
        sgMesh.normalBuffer.setValue(geometry.getMesh().getNormalData());
        float[] vertexData = geometry.getMesh().getPositionData();
        double[] doubleVertexData = new double[vertexData.length];
        for (int i=0; i<vertexData.length; i++) {
        	doubleVertexData[i] = vertexData[i];
        }
        DoubleBuffer vertexBufferd = Utilities.createDoubleBuffer(doubleVertexData);
        sgMesh.vertexBuffer.setValue(vertexBufferd);
        sgMesh.textCoordBuffer.setValue(geometry.getMesh().getTexCoordData());
        
        
        int totalIndices = 0;
        for (Primitives p : geometry.getMesh().getPrimitives()) {
        	totalIndices += p.getCount() * p.getStride();
        }
        
        int[] indices = new int[totalIndices];
        int baseIndex = 0;
        for (Primitives p : geometry.getMesh().getPrimitives()) {
        	int stride = p.getStride();
        	int[] data = p.getData();
        	for (int i=0; i < p.getCount(); i++) {
        		for (int s=0; s<stride; s++) {
        			indices[baseIndex + s] = data[i*stride + s];
        		}
        		baseIndex += stride;
        	}
        }
        
        
        return sgMesh;
	}
	
	private static List<Mesh> createAliceMeshesFromGeometry( List<Geometry> geometries, Collada collada ) {
		List<Mesh> meshes = new ArrayList<Mesh>();
		for (Geometry geometry : geometries) {
			Mesh mesh = createAliceSGMeshFromGeometry( geometry, collada );
			meshes.add( mesh );
		}
		return meshes;
	}
	
	public static VisualData< ChickenResource > loadAliceChicken() {
		VisualData< ChickenResource > v = ImplementationAndVisualType.ALICE.getFactory( ChickenResource.DEFAULT ).createVisualData();
		return v;
	}
	
	public static SkeletonVisual loadAliceModelFromCollada(Collada colladaModel, String modelName) throws ModelLoadingException {
		
		List<edu.cmu.cs.dennisc.scenegraph.Mesh> meshes = new ArrayList<>();
		List<edu.cmu.cs.dennisc.texture.Texture> textures = new ArrayList<>();
		
		
		VisualScene scene = colladaModel.getLibraryVisualScenes().getScene( colladaModel.getScene().getInstanceVisualScene().getUrl() );
		//Find and build the skeleton first
		Joint scenegraphSkeleton = null;
		Node rootNode = findRootNode(scene.getNodes());
		Joint skeletonRoot = createAliceSkeletonFromNode( rootNode );
		
		List<Geometry> geometries = colladaModel.getLibraryGeometries().getGeometries();
//		for (Geometry g : geometries) {
//			g.getMesh().deindex( colladaModel );
//		}
		
		List<Mesh> aliceMeshes = createAliceMeshesFromGeometry( geometries, colladaModel );
		
		List<Material> materials = colladaModel.getLibraryMaterials().getMaterials();
		List<Image> images = colladaModel.getLibraryImages().getImages();
		
		return null;
	}
}
