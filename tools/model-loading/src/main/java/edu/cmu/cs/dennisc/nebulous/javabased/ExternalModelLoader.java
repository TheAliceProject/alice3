/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 * 3. Products derived from the software may not be called "alice", "wonderland", nor may 
 *    "alice", "wonderland" appear in their name, without prior written permission of 
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
 */

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.implementation.alice.ModelResourceIoUtilities;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resourceutilities.JavaCodeUtilities;
import org.lgna.story.resourceutilities.exporterutils.AliceModelExporter;
import org.lgna.story.resourceutilities.exporterutils.AssetReference;
import org.lgna.story.resourceutilities.exporterutils.ModelResourceExporter;
import org.lgna.story.resourceutilities.exporterutils.PipelineException;
import org.lgna.story.resourceutilities.exporterutils.PipelineNamingUtilities;
import org.lgna.story.resourceutilities.exporterutils.TextureReference;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.controller.Controller;
import com.dddviewr.collada.controller.Skin;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.Matrix;
import com.dddviewr.collada.visualscene.VisualScene;

import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.qa.Problem;
import edu.cmu.cs.dennisc.scenegraph.qa.QualityAssuranceUtilities;

public class ExternalModelLoader extends AbstractModelLoader {

	
	public ExternalModelLoader( Object o ) throws IOException {
		super(o);
	}
	
	
	@Override
	protected void initializeFromObject( Object o ) throws IOException {
		if (o instanceof Collada) {
			
		}
		
	}
	
	@Override
	protected void initializeFromPath( String path ) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			initializeFromFile(file);
		}
		else {
			throw new IOException("Can't find file: "+path);
		}
		
	}

	@Override
	protected void initializeFromFile( File file ) throws IOException {
		if (file.exists()) {
			String fileName = file.getName();
			if (fileName.endsWith("dae")) {
//				this.loadFromCollada(file);
			} else {
				throw new IllegalArgumentException("Cannot load file " + file);
			}

			// printSkeleton(this.skeleton, "");
		}
		
	}

	
	//////////////////////////////COLLADA

	private static boolean nodeIsJoint( Node n ) {
		return n.getType().equals( "JOINT" );
	}
	
	private static AffineMatrix4x4 colladaMatrixToAffineMatrix4x4( Matrix matrix ) {
		float[] floatData = matrix.getData();
		double[] doubleData = new double[floatData.length];
		for (int i=0; i<floatData.length; i++) {
			doubleData[i] = floatData[i];
		}
		return AffineMatrix4x4.createFromColumnMajorArray16( doubleData );
	}
	
	private Joint createSceneGraphSkeletonFromNode( Node node ) {
		Joint j = new Joint();
		j.isFreeInX.setValue(true);
		j.isFreeInY.setValue(true);
		j.isFreeInZ.setValue(true);

		AffineMatrix4x4 lt = colladaMatrixToAffineMatrix4x4(node.getMatrix());
		// ls.scale.set(scale.x, scale.y, scale.z);
		j.localTransformation.setValue(lt);
		j.setName(node.getName());
		j.jointID.setValue(node.getName());
		if (node.getChildNodes() != null) {
			for (Node child : node.getChildNodes()) {
				if (nodeIsJoint(child)) {
					Joint childJoint = createSceneGraphSkeletonFromNode(child);
					j.addComponent(childJoint);
				}
			}
		}
		return j;
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
	
	private List<Joint> getSkeletonAsList(Composite n, List<Joint> joints) {
		if (joints == null) {
			joints = new LinkedList<Joint>();
		}
		if (n != null) {
			if (n instanceof Joint) {

				Joint j = (Joint) n;
				joints.add(j);
			}
			for (int i = 0; i < n.getComponentCount(); i++) {
				if (n.getComponentAt(i) instanceof Composite) {
					getSkeletonAsList((Composite) n.getComponentAt(i), joints);
				}
			}
		}
		return joints;
	}

	/*
	
	private void loadFromCollada(Collada colladaModel, String modelName) {
		if (modelName == null) {
			String modelId = colladaModel.getScene().getInstanceVisualScene().getUrl();
			if (modelId.startsWith("#")) {
				modelId = modelId.substring(1);
			}
			modelName = modelId;
		} else {
			this.name = modelName;
		}
		this.meshes.clear();
		this.textures.clear();
		
		
		VisualScene scene = colladaModel.getLibraryVisualScenes().getScene( colladaModel.getScene().getInstanceVisualScene().getUrl() );
		
		//Find and build the skeleton first
		Joint scenegraphSkeleton = null;
		Node rootNode = findRootNode(scene.getNodes());
		
		this.skeleton = null;
		if (rootNode != null) {
			this.skeleton = createSceneGraphSkeletonFromNode(rootNode);
		}
		
		
		int meshCount = 0;
		
		for (Controller controller : colladaModel.getLibraryControllers().getControllers()) {
			Skin skin = controller.getSkin();
//			Joints joints = skin.getJoints();
//			
//		}

		for (SkinNode sn : skinNodes) {
			ArrayList<BoneInfluence>[][] boneInfluences = sn.getCache();
			List<Spatial> skinSpatials = sn.getSkins().getChildren();
			for (int skinIndex = 0; skinIndex < skinSpatials.size(); skinIndex++) {
				if (skinSpatials.get(skinIndex) instanceof TriMesh) {
					WeightedMesh wm = new WeightedMesh();
					TriMesh tm = (TriMesh) skinSpatials.get(skinIndex);
					// tm.rotatePoints(new Quaternion(new float[] {(float)
					// (-Math.PI / 2.0), (float) (-Math.PI), 0 }));
					RenderState rs = tm.getRenderState(StateType.Texture);
					if (rs instanceof TextureState) {
						TextureState ts = (TextureState) rs;
						com.jme.image.Texture texture = ts.getTexture();
						com.jme.image.Image image = texture.getImage();
						if (image != null) {
							String textureName = Utilities
									.getNameFromFile(texture.getImageLocation());
							Texture meshTexture = this.getTexture(textureName);
							if (meshTexture == null) {
								meshTexture = new Texture(texture, name, true);
								this.textures.add(meshTexture);
							}
							wm.setTexture(meshTexture);
						}
					}
					if (tm.getName() != null) {
						wm.setName(tm.getName());
					} else {
						wm.setName("mesh_" + meshCount);
					}
					wm.setTriangleMode(GL_TRIANGLES);
					wm.setMeshType(Mesh.MeshType.COLLADA_BASED);
					FloatBuffer normals = tm.getNormalBuffer();
					FloatBuffer verts = tm.getVertexBuffer();

					wm.setVertexBuffer(verts);
					wm.setNormalBuffer(normals);
					IntBuffer indexBuffer = tm.getIndexBuffer();
					wm.setIndexBuffer(indexBuffer);
					List<TexCoords> texCoords = tm.getTextureCoords();
					assert texCoords.size() == 1;
					TexCoords texCoord = texCoords.get(0);
					wm.setTexCoordsBuffer(texCoord.coords);

					wm.setSkinNode(sn);

					ArrayList<BoneInfluence>[] meshBoneInfluences = boneInfluences[skinIndex];

					Map<Bone, float[]> boneWeightMap = new HashMap<Bone, float[]>();
					for (int vertexIndex = 0; vertexIndex < meshBoneInfluences.length; vertexIndex++) {
						for (BoneInfluence bi : meshBoneInfluences[vertexIndex]) {
							float[] weights;
							if (boneWeightMap.containsKey(bi.bone)) {
								weights = boneWeightMap.get(bi.bone);
							} else {
								weights = new float[tm.getVertexCount()];
								boneWeightMap.put(bi.bone, weights);
							}
							weights[vertexIndex] = bi.weight;
						}
					}

					for (Entry<Bone, float[]> boneAndWeights : boneWeightMap
							.entrySet()) {
						int nonZeroWeights = 0;
						for (float weight : boneAndWeights.getValue()) {
							if (weight != 0) {
								nonZeroWeights++;
							}
						}
						if (nonZeroWeights > 0) {
							InverseAbsoluteTransformationWeightsPair iawp;
							double portion = ((double) nonZeroWeights)
									/ boneAndWeights.getValue().length;
							if (portion > .9) {
								iawp = new PlentifulInverseAbsoluteTransformationWeightsPair();
							} else {
								iawp = new SparseInverseAbsoluteTransformationWeightsPair();
							}
							Bone bone = boneAndWeights.getKey();
							// short reference =
							// jointToReferenceMap.get(bone.getName());
							Matrix4f boneAbsoluteTransform = new Matrix4f();
							bone.getLocalToWorldMatrix(boneAbsoluteTransform);
							Matrix4f bindMatrix = bone.getBindMatrix();
							// temp
							// bone.setBindMatrix(new Matrix4f());
							// boneAbsoluteTransform =
							// boneAbsoluteTransform.mult(bindMatrix);
							Matrix4f inverseAbsoluteMatrix = boneAbsoluteTransform
									.invert();
							Matrix4f inverseBindMatrix = bindMatrix.invert();
							iawp.setWeights(boneAndWeights.getValue());
							AffineMatrix iam = Utilities
									.convertJMEMatrix4f(inverseAbsoluteMatrix);
							iawp.setInverseAbsoluteTransformation(AffineMatrix
									.createAliceMatrix(iam));

							// wm.addReference(reference, iawp);
						}
					}
					this.meshes.add(wm);

					meshCount++;
				}
			}
		}
		if (this.textures.size() > 0) {
			this.activeTexture = this.textures.get(0);
		}
	}

	private void loadFromCollada(File colladaFile) {
		MultiFormatResourceLocator locator = new MultiFormatResourceLocator(
				colladaFile.getParentFile().toURI(), ".png", ".tga", ".jpg");
		ResourceLocatorTool.addResourceLocator(
				ResourceLocatorTool.TYPE_TEXTURE, locator);

		FileInputStream input = null;
		File inputFile = colladaFile;
		try {
			input = new FileInputStream(inputFile);
		} catch (Exception e) {
		}
		if (input != null) {
			Spatial root;
			ColladaImporter.load(input, null);
			root = ColladaImporter.getModel();
			String modelName = Utilities.getNameFromFile(inputFile);
			this.loadFromCollada(root, modelName);
		}
	}

	//Makes sure the ModelResourceExporter is properly initialized with data
	//Saves out: Alice Visual (a3r), Alice Textures (a3t), and Thumbnails
	//Sets on ModelResourceExporter: resources (textures), thumbnails, joint map, and bounding box
	public static void prepareForExport(ModelResourceExporter mre,
			AliceModelExporter ame, String bundlePath, String resourcePath,
			boolean cullBackfaces, boolean useAlphaTest, boolean useAlphaBlend, boolean makeResources, boolean makeThumbnails)
			throws PipelineException {
		
		String modelResourceName = ame.getName();
		String directoryString = JavaCodeUtilities.getDirectoryStringForPackage(mre.getPackageString());
		String fullResourcePath = resourcePath
				+ "/"
				+ directoryString
				+ "/"
				+ ModelResourceIoUtilities.getResourceSubDirWithSeparator(mre
						.getClassName());

		SkeletonVisual sv = null;
		File visualFile = new File(
				fullResourcePath
						+ AliceResourceUtilties
								.getVisualResourceFileNameFromModelName(modelResourceName));
		if (!makeResources && visualFile.exists()) {
			try {
				sv = AliceResourceUtilties.decodeVisual(visualFile.toURI()
						.toURL());
			} catch (Exception e) {
				System.err
						.println("FAILED TO INITALIZE ALICE VISUAL FROM FILE: "
								+ visualFile + ". REBUILDING.");
				sv = null;
			}
			
			java.util.List<Problem> problems = QualityAssuranceUtilities.inspect(sv);
			if (problems.size() != 0) {
				StringBuilder sb = new StringBuilder();
				for (Problem p : problems) {
					sb.append(p.toString() +"\n");
				}
				throw new PipelineException("Problems on resource loaded from "+visualFile+": \n"+sb.toString());
			}
		}

		List<AssetReference> textureResourceFiles = new ArrayList<AssetReference>();
		if (!makeResources) {
			TextureReference tr = ame.getMutableTextureReference();
			for (AssetReference ar : tr.getAssetReferences()) {
				String textureName;
				if (tr.getAssetReferences().size() == 1) {
					textureName = AliceResourceUtilties.getDefaultTextureEnumName(modelResourceName);
				} else {
					textureName = ar.getName();
				}
				String aliceTextureName = getAliceTextureName(textureName);
				String aliceTextureResourceName = getAliceResourceNameForImageFile(modelResourceName, textureName);
				File textureFile = new File(fullResourcePath
						+ aliceTextureResourceName);
				if (textureFile.exists()) {
					textureResourceFiles.add(new AssetReference(
							aliceTextureName, textureFile.getAbsolutePath()));
				} else {
					System.err
							.println("FAILED TO INITALIZE ALICE TEXTURES FROM FILE: "
									+ ar.getAssetFile() + ". REBUILDING.");
					textureResourceFiles.clear();
					break;
				}
			}
		}
		// Initialize and save Alice data if need be.
		if (textureResourceFiles.isEmpty() || sv == null) {
			PipelineModelLoader jam = null;
			try {
				// Load the nebulous resource
				jam = new PipelineModelLoader(bundlePath);
			} catch (Exception e) {
				e.printStackTrace();
				throw new PipelineException("Error loading "
						+ modelResourceName + ".nebundle", e);
			}
			try {
				// Grab the textures
				List<TexturedAppearance[]> textureAppearances = jam
						.getAliceTextureGroups();
				if (useAlphaBlend) {
					for (TexturedAppearance[] tas : textureAppearances) {
						for (TexturedAppearance ta : tas) {
							ta.isDiffuseColorTextureAlphaBlended.setValue(true);
						}
					}
				}
				List<String> textureNames = jam.getMainAliceTextureGroupNames();
				// Convert it to a Alice Scenegraph Visual
				sv = jam.initializeSkeletonVisual(null);
				sv.setTracker(null);
				if (mre.shouldMoveCenterToBottom() || mre.shouldRecenterXZ()) {
					AxisAlignedBox bbox = sv.baseBoundingBox.getValue();
					double heightToCenter = mre.shouldMoveCenterToBottom() ? -bbox.getYMinimum() : 0;
					double xDistToCenter = mre.shouldRecenterXZ() ? -(bbox.getXMaximum() + bbox.getXMinimum())*.5 : 0;
					double zDistToCenter = mre.shouldRecenterXZ() ? -(bbox.getZMaximum() + bbox.getZMinimum())*.5 : 0;
					
					translateSkeletonVisual(sv, new Vector3(xDistToCenter, heightToCenter,
							zDistToCenter));
					
				}

				sv = addMissingJoints(sv, mre.getClassData().superClass);
				
				java.util.List<Problem> problems = QualityAssuranceUtilities.inspect(sv);
				if (problems.size() != 0) {
					StringBuilder sb = new StringBuilder();
					for (Problem p : problems) {
						sb.append(p.toString() +"\n");
					}
					throw new PipelineException("Problems on resource "+modelResourceName+": \n"+sb.toString());
				}

				for (Geometry g : sv.geometries.getValue()) {
					if (g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh) {
						((edu.cmu.cs.dennisc.scenegraph.Mesh) g).cullBackfaces
								.setValue(cullBackfaces);
						((edu.cmu.cs.dennisc.scenegraph.Mesh) g).useAlphaTest
						.setValue(useAlphaTest);
					}
				}
				for (edu.cmu.cs.dennisc.scenegraph.WeightedMesh wm : sv.weightedMeshes
						.getValue()) {
					wm.cullBackfaces.setValue(cullBackfaces);
					wm.useAlphaTest.setValue(useAlphaTest);
				}
				
				// Null out the appearance since we save the textures separately
				sv.textures.setValue(new TexturedAppearance[0]);
				// Save out the visual
				if (makeResources || !visualFile.exists()) {
					AliceResourceUtilties.encodeVisual(sv, visualFile);
				}
				// Save out the textures and add their names to resource
				// exporter
				textureResourceFiles.clear();
				for (int i = 0; i < textureAppearances.size(); i++) {
					TexturedAppearance[] t = textureAppearances.get(i);
					String textureName;
					if (textureAppearances.size() == 1) {
						textureName = getAliceTextureName(AliceResourceUtilties.getDefaultTextureEnumName(modelResourceName));
					} else {
						textureName = getAliceTextureName(textureNames.get(i));
					}
					String aliceTextureName = getAliceResourceNameForImageFile(
							modelResourceName, textureName);
					File textureFile = new File(fullResourcePath
							+ aliceTextureName);
					if (makeResources || !textureFile.exists() || !ThumbnailUtilities.isNonWhiteImage(textureFile)) {
						try {
							AliceResourceUtilties.encodeTexture(t, textureFile);
						}
						catch (Exception n)
						{
							n.printStackTrace();
							throw new PipelineException(
									"Failed to convert bundle to Alice data.", n);
						}
					}
					textureResourceFiles.add(new AssetReference(textureName,
							textureFile.getAbsolutePath()));
				}
				textureAppearances.clear();
			} catch (Exception e) {
				e.printStackTrace();
				throw new PipelineException(
						"Failed to convert bundle to Alice data.", e);
			}
		}
		if (sv == null || textureResourceFiles.isEmpty()) {
			throw new PipelineException("Failed to build Alice resources.",
					ame.getName());
		}

		List<Tuple2<String, String>> jointList = AbstractModelLoader.getSkeletonJointList(sv);
		if (!mre.hasJointMap() && jointList != null) {
			mre.setJointMap(jointList);
		} else if (mre.hasJointMap()) {
			List<Tuple2<String, String>> baseMap = mre.getJointMap();
			List<Tuple2<String, String>> thisMap = jointList;
			List<String> missingJoints = new ArrayList<String>();
			boolean hasAll = true;
			for (Tuple2<String, String> base : baseMap) {
				if (!hasTuple(thisMap, base)) {
					String arrayName = ModelResourceExporter.getArrayNameForJoint(base.getA(), null, PipelineNamingUtilities.getDefaultArrayNamesToSkip());
					if (arrayName == null || !ModelResourceExporter.hasArray(arrayName, thisMap) || (ModelResourceExporter.hasArray(arrayName, thisMap) && !mre.shouldHideJointsOfArray(arrayName)))
					{
						ModelResourceExporter.hasArray(arrayName, thisMap);
						missingJoints.add(base.getA());
						hasAll = false;
					}
				}
			}
			if (!hasAll) {
				StringBuilder sb = new StringBuilder();
				String prefix = null;
				for (String m : missingJoints) {
					if (prefix == null) {
						prefix = ", ";
					} else {
						sb.append(prefix);
					}
					sb.append(m);
				}
				throw new PipelineException(
						"Trying to add a resource ("
								+ modelResourceName
								+ ") that does not have all the joints of its base resource ("
								+ mre.getClassName() + "). Missing: "
								+ sb.toString(), modelResourceName);
			}
			mre.setJointMap(jointList);
		}

		for (AssetReference ar : textureResourceFiles) {
			TexturedAppearance[] texture = null;
			try {
				texture = AliceResourceUtilties.decodeTexture(ar.getAssetFile()
						.toURI().toURL());
			} catch (Exception e) {
				throw new PipelineException(
						"Failed to decode texture from file: "
								+ ar.getAssetFile(), e);
			}
			if (texture == null) {
				throw new PipelineException(
						"Failed to decode texture from file.", ar
								.getAssetFile().getAbsolutePath());
			}
			String textureName = ar.getName();
			mre.addResource(modelResourceName, textureName, ImplementationAndVisualType.ALICE.toString(), ame.getAttributionName(), ame.getAttributionYear());
			String thumbName = AliceResourceUtilties
					.getThumbnailResourceFileName(modelResourceName,
							textureName);
			File thumbnailFile = new File(mre.getThumbnailPath(resourcePath,
					thumbName));
			boolean badFile = false;
			long size = thumbnailFile.length();
			if (size < MIN_THUMBNAIL_FILE_SIZE) {
				badFile = true;
			}
			if (makeResources || makeThumbnails || !thumbnailFile.exists()
					|| badFile) {
				sv.textures.setValue(texture);
				try {
					BufferedImage thumbnail = createThumbnail(sv, MODEL_THUMBNAIL_WIDTH, MODEL_THUMBNAIL_HEIGHT);
					try {
						ImageUtilities.write(thumbnailFile, thumbnail);
					} catch (IOException e) {
						System.err.println("Error writing thumbnail for "+modelResourceName);
						throw new PipelineException(
								"Error writing thumbnail for "+modelResourceName
										+ ar.getAssetFile(), e);
					}
				}
				catch (Exception glException) {
					System.err.println("Error generating thumbnail for "+modelResourceName);
					glException.printStackTrace();
					
					BufferedImage thumbnail = PipelineUtilities.getNoThumbnailImage();
					try {
						ImageUtilities.write(thumbnailFile, thumbnail);
					} catch (IOException e) {
						System.err.println("Error writing thumbnail for "+modelResourceName);
						throw new PipelineException(
								"Error writing thumbnail for "+modelResourceName
										+ ar.getAssetFile(), e);
					}
				}
				mre.addExistingThumbnail(textureName, thumbnailFile);
			} else {
				mre.addExistingThumbnail(textureName, thumbnailFile);
			}
		}
		mre.setBoundingBox(modelResourceName, sv.baseBoundingBox.getValue());
		sv.setTracker(null);
		sv.release();
		sv = null;
	}

*/

}
