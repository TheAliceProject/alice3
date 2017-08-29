import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lgna.story.resources.biped.AliceResource;
import org.lgna.story.resources.biped.AlienResource;
import org.lgna.story.resources.fish.ClownFishResource;
import org.lgna.story.resources.flyer.ChickenResource;
import org.lgna.story.resources.prop.ColaBottleResource;
import org.lgna.story.resources.prop.SledResource;
import org.lgna.story.resourceutilities.AdaptiveRecenteringThumbnailMaker;

import com.jme3.animation.Bone;
import com.jogamp.common.nio.Buffers;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Joints;
import com.dddviewr.collada.controller.LibraryControllers;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.images.Image;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.scene.InstanceVisualScene;
import com.dddviewr.collada.visualscene.LibraryVisualScenes;
import com.dddviewr.collada.visualscene.VisualScene;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.Mesh;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.WeightInfo;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;

public class AliceModelLoader {
	
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
	
	private static void printMesh( Mesh mesh ){
		System.out.println( "Mesh "+mesh.getName());
		System.out.println( "  texture id: "+mesh.textureId.getValue());
		double[] vertices = BufferUtilities.convertDoubleBufferToArray( mesh.vertexBuffer.getValue() );
		System.out.println( "  vertex count "+vertices.length/3);
		System.out.println( "  vertices:");
		for (int i=0; i<vertices.length; i+=3) {
			System.out.println( "   ("+vertices[i]+", "+vertices[i+1]+", "+vertices[i+2]+")");
		}
		
		System.out.println( "  normals:" );
		float[] normals = BufferUtilities.convertFloatBufferToArray( mesh.normalBuffer.getValue() );
		for (int i=0; i<normals.length; i+=3) {
			System.out.println( "   ("+normals[i]+", "+normals[i+1]+", "+normals[i+2]+")");
		}
	}
	
	private static void printWeightInfo( WeightInfo weightInfo) {
		System.out.println( "Weight Info:" );
		Map<String, InverseAbsoluteTransformationWeightsPair> mapReferencesToInverseAbsoluteTransformationWeightsPairs = weightInfo.getMap();
		for (Entry<String, InverseAbsoluteTransformationWeightsPair> pair : mapReferencesToInverseAbsoluteTransformationWeightsPairs.entrySet()) {
			InverseAbsoluteTransformationWeightsPair iatwp = pair.getValue();
			AffineMatrix4x4 inverseTransform = iatwp.getInverseAbsoluteTransformation();
			AffineMatrix4x4 originalTransform = AffineMatrix4x4.createInverse( inverseTransform );
			System.out.println( "  joint: "+pair.getKey() );
			System.out.println( "  inverse absolute transform:");
			System.out.println( "    t: "+inverseTransform.translation);
			System.out.println( "    x: "+inverseTransform.orientation.right);
			System.out.println( "    y: "+inverseTransform.orientation.up);
			System.out.println( "    z: "+inverseTransform.orientation.backward);
			System.out.println( "  original absolute transform:");
			System.out.println( "    t: "+originalTransform.translation);
			System.out.println( "    x: "+originalTransform.orientation.right);
			System.out.println( "    y: "+originalTransform.orientation.up);
			System.out.println( "    z: "+originalTransform.orientation.backward);
//			System.out.println( "  weights:" );
//			System.out.print(   "   ");
//			
//			iatwp.reset();
//			while (!iatwp.isDone()) {
//				System.out.print( "("+iatwp.getIndex()+", "+iatwp.getWeight()+"),");
//				iatwp.advance();
//			}
//			iatwp.reset();
			System.out.println();
		}
	}
	
	private static void printSkeletonVisual( SkeletonVisual sv ) {
		System.out.println( "Skeleton: ");
		printJoints(sv.skeleton.getValue(), "");
		System.out.println();
		System.out.println( "BoundingBox:" );
		System.out.println(sv.baseBoundingBox.getValue());
//		for (edu.cmu.cs.dennisc.scenegraph.Geometry g : sv.geometries.getValue()) {
//			if (g instanceof Mesh) {
//				Mesh m = (Mesh)g;
//				printMesh(m);
//				System.out.println();
//			}
//		}
//		for (WeightedMesh wm : sv.weightedMeshes.getValue()) {
////			printMesh(wm);
//			printWeightInfo(wm.weightInfo.getValue());
//			System.out.println();
//		}
	}
	
	public static void main( String[] args ) {
			
			File colladaFile  = new File( "C:/Users/dculyba/Documents/Alice/Collada/alice.dae" );
			AliceModelLoader.loadAliceModelFromCollada( colladaFile, "chicken");
	}
	
	public static SkeletonVisual loadAliceModelFromCollada(File colladaModelFile, String modelName) {
		
		try {
			
			SkeletonVisual alice_sv = AliceColladaModelLoader.loadAliceModel( AliceResource.CARNEGIE_MELLON );
			
			Logger modelLogger = Logger.getLogger( "AliceColladaModelLoader" );
			SkeletonVisual sv = AliceColladaModelLoader.loadAliceModelFromCollada( colladaModelFile, modelName, modelLogger );
			System.out.println( "Collada Model" );
//			printJoints(sv.skeleton.getValue(), "");
			printSkeletonVisual(sv);
			
			System.out.println( "Alice Model" );
			printSkeletonVisual(alice_sv);
//			printJoints(alice_sv.skeleton.getValue(), "");
			
			
//			BufferedImage chicken_image = AdaptiveRecenteringThumbnailMaker.getInstance(800, 600).createThumbnail(chicken_sv, false);
			
			BufferedImage image = AdaptiveRecenteringThumbnailMaker.getInstance(800, 600).createThumbnail(alice_sv, false);
			JPanel imagePanel = new JPanel();
			imagePanel.setSize( image.getWidth(), image.getHeight() );
			imagePanel.setBackground( Color.MAGENTA );
			ImageIcon icon = new ImageIcon( image );
			imagePanel.add( new JLabel(icon) );
			JFrame frame = new JFrame();
			frame.getContentPane().add( imagePanel );
			frame.pack();
	        frame.setVisible(true);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible( true );
//			ImageUtilities.write(new File("C:\\Users\\dculyba\\Documents\\Alice\\colladaTest.png"), image);
		}
		catch (ModelLoadingException e) {
			e.printStackTrace();
		}
//		catch( IOException e ) {
//			e.printStackTrace();
//		}
		
		
//		for (Controller controller : libraryControllers.getControllers()) {
//			Skin skin = controller.getSkin();
//			Joints joints = skin.getJoints();
//			
//		}

//		List<SkinNode> skinNodes = getSkinNodes(root);
//		Bone jmeSkeleton = null;
//		for (SkinNode sn : skinNodes) {
//			if (jmeSkeleton == null) {
//				jmeSkeleton = sn.getSkeleton();
//			} else {
//				assert jmeSkeleton == sn.getSkeleton();
//			}
//		}
//
//		// jmeSkeleton.rotateUpTo(new Vector3f(0, 1, 0));
//		// jmeSkeleton.setLocalRotation(new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0,
//		// 1));
//		List<Bone> bones = getBonesAsList(jmeSkeleton, null);
//		// jointToReferenceMap.clear();
//		// short boneCount = 1;
//		// for (Bone b : bones)
//		// {
//		// jointToReferenceMap.put(b.getName(), new Short(boneCount++));
//		// }
//		this.skeleton = createSkeleton(jmeSkeleton);
//		// printCollada(root, "");
//		int meshCount = 0;
//
//		for (SkinNode sn : skinNodes) {
//			ArrayList<BoneInfluence>[][] boneInfluences = sn.getCache();
//			List<Spatial> skinSpatials = sn.getSkins().getChildren();
//			for (int skinIndex = 0; skinIndex < skinSpatials.size(); skinIndex++) {
//				if (skinSpatials.get(skinIndex) instanceof TriMesh) {
//					WeightedMesh wm = new WeightedMesh();
//					TriMesh tm = (TriMesh) skinSpatials.get(skinIndex);
//					// tm.rotatePoints(new Quaternion(new float[] {(float)
//					// (-Math.PI / 2.0), (float) (-Math.PI), 0 }));
//					RenderState rs = tm.getRenderState(StateType.Texture);
//					if (rs instanceof TextureState) {
//						TextureState ts = (TextureState) rs;
//						com.jme.image.Texture texture = ts.getTexture();
//						com.jme.image.Image image = texture.getImage();
//						if (image != null) {
//							String textureName = Utilities
//									.getNameFromFile(texture.getImageLocation());
//							Texture meshTexture = this.getTexture(textureName);
//							if (meshTexture == null) {
//								meshTexture = new Texture(texture, name, true);
//								this.textures.add(meshTexture);
//							}
//							wm.setTexture(meshTexture);
//						}
//					}
//					if (tm.getName() != null) {
//						wm.setName(tm.getName());
//					} else {
//						wm.setName("mesh_" + meshCount);
//					}
//					wm.setTriangleMode(GL_TRIANGLES);
//					wm.setMeshType(Mesh.MeshType.COLLADA_BASED);
//					FloatBuffer normals = tm.getNormalBuffer();
//					FloatBuffer verts = tm.getVertexBuffer();
//
//					wm.setVertexBuffer(verts);
//					wm.setNormalBuffer(normals);
//					IntBuffer indexBuffer = tm.getIndexBuffer();
//					wm.setIndexBuffer(indexBuffer);
//					List<TexCoords> texCoords = tm.getTextureCoords();
//					assert texCoords.size() == 1;
//					TexCoords texCoord = texCoords.get(0);
//					wm.setTexCoordsBuffer(texCoord.coords);
//
//					wm.setSkinNode(sn);
//
//					ArrayList<BoneInfluence>[] meshBoneInfluences = boneInfluences[skinIndex];
//
//					Map<Bone, float[]> boneWeightMap = new HashMap<Bone, float[]>();
//					for (int vertexIndex = 0; vertexIndex < meshBoneInfluences.length; vertexIndex++) {
//						for (BoneInfluence bi : meshBoneInfluences[vertexIndex]) {
//							float[] weights;
//							if (boneWeightMap.containsKey(bi.bone)) {
//								weights = boneWeightMap.get(bi.bone);
//							} else {
//								weights = new float[tm.getVertexCount()];
//								boneWeightMap.put(bi.bone, weights);
//							}
//							weights[vertexIndex] = bi.weight;
//						}
//					}
//
//					for (Entry<Bone, float[]> boneAndWeights : boneWeightMap
//							.entrySet()) {
//						int nonZeroWeights = 0;
//						for (float weight : boneAndWeights.getValue()) {
//							if (weight != 0) {
//								nonZeroWeights++;
//							}
//						}
//						if (nonZeroWeights > 0) {
//							InverseAbsoluteTransformationWeightsPair iawp;
//							double portion = ((double) nonZeroWeights)
//									/ boneAndWeights.getValue().length;
//							if (portion > .9) {
//								iawp = new PlentifulInverseAbsoluteTransformationWeightsPair();
//							} else {
//								iawp = new SparseInverseAbsoluteTransformationWeightsPair();
//							}
//							Bone bone = boneAndWeights.getKey();
//							// short reference =
//							// jointToReferenceMap.get(bone.getName());
//							Matrix4f boneAbsoluteTransform = new Matrix4f();
//							bone.getLocalToWorldMatrix(boneAbsoluteTransform);
//							Matrix4f bindMatrix = bone.getBindMatrix();
//							// temp
//							// bone.setBindMatrix(new Matrix4f());
//							// boneAbsoluteTransform =
//							// boneAbsoluteTransform.mult(bindMatrix);
//							Matrix4f inverseAbsoluteMatrix = boneAbsoluteTransform
//									.invert();
//							Matrix4f inverseBindMatrix = bindMatrix.invert();
//							iawp.setWeights(boneAndWeights.getValue());
//							AffineMatrix iam = Utilities
//									.convertJMEMatrix4f(inverseAbsoluteMatrix);
//							iawp.setInverseAbsoluteTransformation(AffineMatrix
//									.createAliceMatrix(iam));
//
//							// wm.addReference(reference, iawp);
//						}
//					}
//					this.meshes.add(wm);
//
//					meshCount++;
//				}
//			}
//		}
//		if (this.textures.size() > 0) {
//			this.activeTexture = this.textures.get(0);
//		}
		
		return null;
	}
	
//	private static void collectSkinNodes(Spatial s, List<SkinNode> skinNodes) {
//		if (s instanceof SkinNode) {
//			skinNodes.add((SkinNode) s);
//		}
//		if (s instanceof com.jme.scene.Node) {
//			com.jme.scene.Node node = (com.jme.scene.Node) s;
//			List<Spatial> children = node.getChildren();
//			if (children != null) {
//				for (Spatial child : children) {
//					collectSkinNodes(child, skinNodes);
//				}
//			}
//		}
//	}
//
//	private static List<TriMesh> getTriMeshNodes(Spatial s) {
//		List<TriMesh> triMeshNodes = new LinkedList<TriMesh>();
//		collectTriMeshNodes(s, triMeshNodes);
//		return triMeshNodes;
//	}
//
//	private static void collectTriMeshNodes(Spatial s, List<TriMesh> triMeshNodes) {
//		if (s instanceof TriMesh) {
//			triMeshNodes.add((TriMesh) s);
//		}
//		if (s instanceof com.jme.scene.Node) {
//			com.jme.scene.Node node = (com.jme.scene.Node) s;
//			List<Spatial> children = node.getChildren();
//			if (children != null) {
//				for (Spatial child : children) {
//					collectTriMeshNodes(child, triMeshNodes);
//				}
//			}
//		}
//	}
//
//	private List<SkinNode> getSkinNodes(Spatial s) {
//		List<SkinNode> skinNodes = new LinkedList<SkinNode>();
//		collectSkinNodes(s, skinNodes);
//		return skinNodes;
//	}
//	
//	public static SkeletonVisual loadAliceModelFromSpatial(Spatial root, String modelName) {
//		String name;
//		List<edu.cmu.cs.dennisc.scenegraph.Mesh> meshes = new ArrayList<>();
//		List<edu.cmu.cs.dennisc.texture.Texture> textures = new ArrayList<>();
//		
//		if (modelName == null) {
//			if (root.getName() != null) {
//				name = root.getName();
//			} else {
//				name = "NO_NAME";
//			}
//		} else {
//			name = modelName;
//		}
//		List<SkinNode> skinNodes = getSkinNodes(root);
//		Bone jmeSkeleton = null;
//		for (SkinNode sn : skinNodes) {
//			if (jmeSkeleton == null) {
//				jmeSkeleton = sn.getSkeleton();
//			} else {
//				assert jmeSkeleton == sn.getSkeleton();
//			}
//		}
//
//		// jmeSkeleton.rotateUpTo(new Vector3f(0, 1, 0));
//		// jmeSkeleton.setLocalRotation(new Matrix3f(1, 0, 0, 0, 1, 0, 0, 0,
//		// 1));
//		List<Bone> bones = getBonesAsList(jmeSkeleton, null);
//		// jointToReferenceMap.clear();
//		// short boneCount = 1;
//		// for (Bone b : bones)
//		// {
//		// jointToReferenceMap.put(b.getName(), new Short(boneCount++));
//		// }
//		this.skeleton = createSkeleton(jmeSkeleton);
//		// printCollada(root, "");
//		int meshCount = 0;
//
//		for (SkinNode sn : skinNodes) {
//			ArrayList<BoneInfluence>[][] boneInfluences = sn.getCache();
//			List<Spatial> skinSpatials = sn.getSkins().getChildren();
//			for (int skinIndex = 0; skinIndex < skinSpatials.size(); skinIndex++) {
//				if (skinSpatials.get(skinIndex) instanceof TriMesh) {
//					WeightedMesh wm = new WeightedMesh();
//					TriMesh tm = (TriMesh) skinSpatials.get(skinIndex);
//					// tm.rotatePoints(new Quaternion(new float[] {(float)
//					// (-Math.PI / 2.0), (float) (-Math.PI), 0 }));
//					RenderState rs = tm.getRenderState(StateType.Texture);
//					if (rs instanceof TextureState) {
//						TextureState ts = (TextureState) rs;
//						com.jme.image.Texture texture = ts.getTexture();
//						com.jme.image.Image image = texture.getImage();
//						if (image != null) {
//							String textureName = Utilities
//									.getNameFromFile(texture.getImageLocation());
//							Texture meshTexture = this.getTexture(textureName);
//							if (meshTexture == null) {
//								meshTexture = new Texture(texture, name, true);
//								this.textures.add(meshTexture);
//							}
//							wm.setTexture(meshTexture);
//						}
//					}
//					if (tm.getName() != null) {
//						wm.setName(tm.getName());
//					} else {
//						wm.setName("mesh_" + meshCount);
//					}
//					wm.setTriangleMode(GL_TRIANGLES);
//					wm.setMeshType(Mesh.MeshType.COLLADA_BASED);
//					FloatBuffer normals = tm.getNormalBuffer();
//					FloatBuffer verts = tm.getVertexBuffer();
//
//					wm.setVertexBuffer(verts);
//					wm.setNormalBuffer(normals);
//					IntBuffer indexBuffer = tm.getIndexBuffer();
//					wm.setIndexBuffer(indexBuffer);
//					List<TexCoords> texCoords = tm.getTextureCoords();
//					assert texCoords.size() == 1;
//					TexCoords texCoord = texCoords.get(0);
//					wm.setTexCoordsBuffer(texCoord.coords);
//
//					wm.setSkinNode(sn);
//
//					ArrayList<BoneInfluence>[] meshBoneInfluences = boneInfluences[skinIndex];
//
//					Map<Bone, float[]> boneWeightMap = new HashMap<Bone, float[]>();
//					for (int vertexIndex = 0; vertexIndex < meshBoneInfluences.length; vertexIndex++) {
//						for (BoneInfluence bi : meshBoneInfluences[vertexIndex]) {
//							float[] weights;
//							if (boneWeightMap.containsKey(bi.bone)) {
//								weights = boneWeightMap.get(bi.bone);
//							} else {
//								weights = new float[tm.getVertexCount()];
//								boneWeightMap.put(bi.bone, weights);
//							}
//							weights[vertexIndex] = bi.weight;
//						}
//					}
//
//					for (Entry<Bone, float[]> boneAndWeights : boneWeightMap
//							.entrySet()) {
//						int nonZeroWeights = 0;
//						for (float weight : boneAndWeights.getValue()) {
//							if (weight != 0) {
//								nonZeroWeights++;
//							}
//						}
//						if (nonZeroWeights > 0) {
//							InverseAbsoluteTransformationWeightsPair iawp;
//							double portion = ((double) nonZeroWeights)
//									/ boneAndWeights.getValue().length;
//							if (portion > .9) {
//								iawp = new PlentifulInverseAbsoluteTransformationWeightsPair();
//							} else {
//								iawp = new SparseInverseAbsoluteTransformationWeightsPair();
//							}
//							Bone bone = boneAndWeights.getKey();
//							// short reference =
//							// jointToReferenceMap.get(bone.getName());
//							Matrix4f boneAbsoluteTransform = new Matrix4f();
//							bone.getLocalToWorldMatrix(boneAbsoluteTransform);
//							Matrix4f bindMatrix = bone.getBindMatrix();
//							// temp
//							// bone.setBindMatrix(new Matrix4f());
//							// boneAbsoluteTransform =
//							// boneAbsoluteTransform.mult(bindMatrix);
//							Matrix4f inverseAbsoluteMatrix = boneAbsoluteTransform
//									.invert();
//							Matrix4f inverseBindMatrix = bindMatrix.invert();
//							iawp.setWeights(boneAndWeights.getValue());
//							AffineMatrix iam = Utilities
//									.convertJMEMatrix4f(inverseAbsoluteMatrix);
//							iawp.setInverseAbsoluteTransformation(AffineMatrix
//									.createAliceMatrix(iam));
//
//							// wm.addReference(reference, iawp);
//						}
//					}
//					this.meshes.add(wm);
//
//					meshCount++;
//				}
//			}
//		}
//		if (this.textures.size() > 0) {
//			this.activeTexture = this.textures.get(0);
//		}
//	}
	
	
}
