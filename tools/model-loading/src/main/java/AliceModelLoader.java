import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jme3.animation.Bone;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Joints;
import com.dddviewr.collada.controller.LibraryControllers;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.scene.InstanceVisualScene;
import com.dddviewr.collada.visualscene.LibraryVisualScenes;
import com.dddviewr.collada.visualscene.VisualScene;

import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;

public class AliceModelLoader {

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
	
	public static SkeletonVisual loadAliceModelFromCollada(Collada colladaModel, String modelName) {
		
		List<edu.cmu.cs.dennisc.scenegraph.Mesh> meshes = new ArrayList<>();
		List<edu.cmu.cs.dennisc.texture.Texture> textures = new ArrayList<>();
		
		
		VisualScene scene = colladaModel.getLibraryVisualScenes().getScene( colladaModel.getScene().getInstanceVisualScene().getUrl() );
		
		//Find and build the skeleton first
		Joint scenegraphSkeleton = null;
		Node rootNode = findRootNode(scene.getNodes());
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
