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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;

import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.JointId;
import org.lgna.story.resourceutilities.AdaptiveRecenteringThumbnailMaker;
import org.lgna.story.resourceutilities.exporterutils.PipelineException;
import org.lgna.story.resourceutilities.exporterutils.PipelineNamingUtilities;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.WeightInfo;

public abstract class AbstractModelLoader extends edu.cmu.cs.dennisc.scenegraph.Geometry {

	public static final int GL_TRIANGLES = 0x4;
	public static long MIN_THUMBNAIL_FILE_SIZE = 500; // size in bytes

	public static final int MODEL_THUMBNAIL_WIDTH = 160;
	public static final int MODEL_THUMBNAIL_HEIGHT = 120;

	protected List<Mesh> meshes;
	protected List<TextureGroup> textureGroups;
	protected List<Texture> textures;
	protected Map<String, Short> jointToReferenceMap;
	protected List<String> namesToPreserve;
	protected Map<Short, String> referenceToJointNameMap;
	protected Texture activeTexture;
	protected Joint skeleton;
	protected boolean hasDifferentBindPose = false;
	protected String name;

	private AbstractModelLoader() {
		this.meshes = new LinkedList<Mesh>();
		this.textures = new LinkedList<Texture>();
		this.textureGroups = new LinkedList<TextureGroup>();
		this.namesToPreserve = new LinkedList<String>();
		this.activeTexture = null;
		this.skeleton = null;
	}
	
	public AbstractModelLoader(Object o) throws IOException {
		this();
		if (o instanceof String) {
			initializeFromPath( (String)o );
		}
		else if (o instanceof File) {
			initializeFromFile( (File)o );
		}
		else {
			this.initializeFromObject(o);
		}
	}

	protected abstract void initializeFromObject(Object o) throws IOException;
	
	protected abstract void initializeFromPath( String path ) throws IOException;
	
	protected abstract void initializeFromFile( File file ) throws IOException;
	
	public Joint getJoint(String jointReference) {
		return this.skeleton.getJoint(jointReference);
	}

	public int getJointCount() {
		return this.jointToReferenceMap.size();
	}

	public Short getJointReferenceForIndex(int index) {
		int count = 0;
		for (Entry<String, Short> entry : this.jointToReferenceMap.entrySet()) {
			if (count == index) {
				return entry.getValue();
			}
			count++;
		}
		return null;
	}

	public String getJointNameForIndex(int index) {
		int count = 0;
		for (Entry<String, Short> entry : this.jointToReferenceMap.entrySet()) {
			if (count == index) {
				return entry.getKey();
			}
			count++;
		}
		return "NOT FOUND";
	}

	public Short getJointReference(String jointName) {
		return this.jointToReferenceMap.get(jointName);
	}

	public boolean hasJointNamed(String jointName) {
		return this.jointToReferenceMap.containsKey(jointName);
	}

	public void processWeightedMesh() {
		throw new AssertionError(
				"Method no longer supported. Use edu.cmu.cs.dennisc.resource.SkeketonModelResource");
	}

	protected Texture getTexture(String name) {
		for (Texture t : this.textures) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}

	
	//Takes the existing ID to MayaName map and creates an ID to AliceName map
	// This is where the primary Alice joint naming happens. 
	// The method PipelineNamingUtilities.getAliceJointNameForMayaJointName(mayaName) does most of the heavy lifting
	// Additionally, we find and rename joints that are part of arrays
	// We make sure all joints in basic arrays are named <array name>_<array index>
	protected Map<Short, String> createJointNameToReferenceMap(
			Map<String, Short> referenceMap, Map<String, String> customArrayNameMap) throws DataFormatException{
		Map<Short, String> map = new HashMap<Short, String>();
		List<String> jointNames = new LinkedList<String>();
		for (Entry<String, Short> entry : referenceMap.entrySet()) {
			String mayaName = entry.getKey();
			
			String aliceName = PipelineNamingUtilities
					.getAliceJointNameForMayaJointName(mayaName, PipelineNamingUtilities.shouldPreserveName(mayaName, this.namesToPreserve));
			jointNames.add(aliceName);
			map.put(entry.getValue(), aliceName);
		}
		
		//Now find and rename joints in arrays
//		Map<String, List<String>> arrayEntries = ModelResourceExporter.getArrayEntries(jointNames, customArrayNameMap, getCustomJointsToSuppressList());
//		if (!arrayEntries.isEmpty()) {
//			for (Entry<Short, String> entry : map.entrySet()) {
//				String arrayName = ModelResourceExporter.getArrayNameForJoint(entry.getValue(), customArrayNameMap);
//				if (arrayName != null) {
//					List<String> jointArray = arrayEntries.get(arrayName);
//					String newName = PipelineNamingUtilities.getAliceArrayJointName(entry.getValue(), jointArray, arrayName);
//					entry.setValue(newName);					
//				}
//			}
//		}
		return map;
	}

	protected TextureGroup getTextureGroupForID(int id) {
		for (TextureGroup tg : this.textureGroups) {
			if (tg.getID() == id) {
				return tg;
			}
		}
		return null;
	}


	protected void processWeightedMesh(Composite currentNode,
			AffineMatrix4x4 oTransformationPre,
			List<WeightedMeshControl> controls) {
		if (currentNode == null) {
			return;
		}
		AffineMatrix4x4 oTransformationPost = oTransformationPre;
		if (currentNode instanceof Transformable) {
			oTransformationPost = AffineMatrix4x4.createMultiplication(
					oTransformationPre,
					((Transformable) currentNode).localTransformation
							.getValue());
			if (currentNode instanceof Joint) {
				for (WeightedMeshControl wmControl : controls) {
					wmControl.process((Joint) currentNode, oTransformationPost);
				}
			}
		}
		for (int i = 0; i < currentNode.getComponentCount(); i++) {
			Component comp = currentNode.getComponentAt(i);
			if (comp instanceof Composite) {
				Composite jointChild = (Composite) comp;
				processWeightedMesh(jointChild, oTransformationPost, controls);
			}
		}
	}

	protected boolean meshUsesTextureGroup(Mesh mesh, TextureGroup tg) {
		if (mesh.getTextureGroup().getID() == tg.getID()) {
			return true;
		}
		return false;
	}

	private static float[] getWeightsAsArray(
			edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair iatwp) {
		List<Integer> indices = new ArrayList<Integer>();
		List<Float> weights = new ArrayList<Float>();
		iatwp.reset();
		int largestIndex = 0;
		while (!iatwp.isDone()) {
			int index = iatwp.getIndex();
			if (index > largestIndex) {
				largestIndex = index;
			}
			indices.add(index);
			weights.add(iatwp.getWeight());
			iatwp.advance();
		}

		float[] rv = new float[largestIndex + 1];
		for (int i = 0; i < indices.size(); i++) {
			rv[indices.get(i)] = weights.get(i);
		}
		return rv;
	}

	private static void remapWeightedMesh(
			edu.cmu.cs.dennisc.scenegraph.WeightedMesh sgWeightedMesh,
			Map<String, String> remapJointsMap) {
		WeightInfo weightInfo = sgWeightedMesh.weightInfo.getValue();
		for (Entry<String, String> replacement : remapJointsMap.entrySet()) {
			edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair weightsToMap = weightInfo
					.getMap().get(replacement.getKey());
			edu.cmu.cs.dennisc.scenegraph.InverseAbsoluteTransformationWeightsPair weightsToAddTo = weightInfo
					.getMap().get(replacement.getValue());
			if (weightsToMap == null) {
				return;
			}
			if (weightsToAddTo != null) {
				float[] toReplace = getWeightsAsArray(weightsToMap);
				float[] toAddTo = getWeightsAsArray(weightsToAddTo);

				int maxSize = Math.max(toReplace.length, toAddTo.length);
				float[] newWeights = new float[maxSize];
				for (int i = 0; i < maxSize; i++) {
					float weight = 0;
					if (i < toReplace.length && i < toAddTo.length) {
						weight = toReplace[i] + toAddTo[i];
					} else if (i < toReplace.length) {
						weight = toReplace[i];
					} else if (i < toAddTo.length) {
						weight = toAddTo[i];
					}
					newWeights[i] = weight;
				}

				weightsToAddTo.setWeights(newWeights);
			} else {
				// What do we do here?
				// We have existing weights that don't map to an existing joint
				Logger.severe("Trying to map " + replacement.getKey() + " to "
						+ replacement.getValue()
						+ ". No existing joint to map weights to.");
			}
			weightInfo.getMap().remove(replacement.getKey());
		}
	}

	public SkeletonVisual initializeSkeletonVisual(SkeletonVisual smr) {
		if (smr == null) {
			smr = new SkeletonVisual();
		}

		smr.setName(this.name);
		smr.frontFacingAppearance.setValue(new SimpleAppearance());
		smr.skeleton.setValue(this.skeleton);
		if (this.skeleton != null) {
			System.out.println("Skeleton present.");
			PrintWriter pw = new java.io.PrintWriter( System.out );
			printJoints(this.skeleton, "", pw);
			pw.flush();
			
		} else {
			System.out.println("No skeleton.");
		}
		// this.skeleton.setLocalTransformation(AffineMatrix4x4.createIdentity());
		// smr.skeleton.getValue().applyRotationAboutYAxis(new
		// AngleInDegrees(180));
		List<edu.cmu.cs.dennisc.scenegraph.Mesh> sgMeshes = new LinkedList<edu.cmu.cs.dennisc.scenegraph.Mesh>();
		List<edu.cmu.cs.dennisc.scenegraph.WeightedMesh> sgWeightedMeshes = new LinkedList<edu.cmu.cs.dennisc.scenegraph.WeightedMesh>();
		for (Mesh mesh : this.meshes) {
			if (mesh instanceof WeightedMesh) {
				edu.cmu.cs.dennisc.scenegraph.WeightedMesh sgWeightedMesh = ((WeightedMesh) mesh)
						.createSceneGraphWeightedMesh(this.referenceToJointNameMap);

				sgWeightedMesh.skeleton.setValue(this.skeleton);

				remapWeightedMesh(sgWeightedMesh,
						PipelineNamingUtilities.sRemapJointsMap);

				sgWeightedMeshes.add(sgWeightedMesh);
			} else {
				edu.cmu.cs.dennisc.scenegraph.Mesh sgMesh = mesh
						.createSceneGraphMesh();
				sgMeshes.add(sgMesh);
			}
		}
		smr.geometries
				.setValue(sgMeshes
						.toArray(new edu.cmu.cs.dennisc.scenegraph.Mesh[sgMeshes
								.size()]));
		smr.weightedMeshes
				.setValue(sgWeightedMeshes
						.toArray(new edu.cmu.cs.dennisc.scenegraph.WeightedMesh[sgWeightedMeshes
								.size()]));
		
		List<edu.cmu.cs.dennisc.scenegraph.TexturedAppearance> sgTextureAppearances = new LinkedList<edu.cmu.cs.dennisc.scenegraph.TexturedAppearance>();
		if (this.textureGroups.size() > 0) {
			for (int i = 0; i < this.textureGroups.size(); i++) {
				TextureGroup tg = this.textureGroups.get(i);
				if (tg.getActiveTexture() == null) {
					System.err.println("NULL");
				}
				edu.cmu.cs.dennisc.scenegraph.TexturedAppearance m_sgAppearance = new edu.cmu.cs.dennisc.scenegraph.TexturedAppearance();
				m_sgAppearance.diffuseColorTexture.setValue(getAliceTexture(tg
						.getActiveTexture()));
				m_sgAppearance.textureId.setValue(tg.getID());
				sgTextureAppearances.add(m_sgAppearance);
			}
		} else {
			edu.cmu.cs.dennisc.scenegraph.TexturedAppearance m_sgAppearance = new edu.cmu.cs.dennisc.scenegraph.TexturedAppearance();
			List<edu.cmu.cs.dennisc.texture.Texture> textures = this
					.getAliceTextures();
			if (textures.size() > 0) {
				m_sgAppearance.diffuseColorTexture.setValue(textures.get(0));
				m_sgAppearance.textureId.setValue(-1);
				sgTextureAppearances.add(m_sgAppearance);
			}
		}

		smr.textures
				.setValue(sgTextureAppearances
						.toArray(new edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[sgTextureAppearances
								.size()]));

		UtilitySkeletonVisualAdapter skeletonVisualAdapter = new UtilitySkeletonVisualAdapter();
		skeletonVisualAdapter.initialize(smr);
		skeletonVisualAdapter.processWeightedMesh();
		skeletonVisualAdapter.initializeJointBoundingBoxes(smr.skeleton
				.getValue());
		AxisAlignedBox absoluteBBox = skeletonVisualAdapter
				.getAbsoluteBoundingBox();
		if (smr.geometries.getValue() != null) {
			for (edu.cmu.cs.dennisc.scenegraph.Geometry g : smr.geometries
					.getValue()) {
				edu.cmu.cs.dennisc.math.AxisAlignedBox b = g
						.getAxisAlignedMinimumBoundingBox();
				absoluteBBox.union(b);
			}
		}
		smr.baseBoundingBox.setValue(absoluteBBox);
		
		
		if (this.hasDifferentBindPose && this.skeleton != null) {
			List<UtilityWeightedMeshControl> weightedMeshControls = skeletonVisualAdapter.getUtilityWeightedMeshControls();
			edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] defaultPoseWeightedMeshes = new edu.cmu.cs.dennisc.scenegraph.WeightedMesh[weightedMeshControls.size()];
			//Go through the weighted mesh controls from the skeletonVisualAdapter and grab the weighted meshes
			//Since we used this adapter to transform the mesh based on the skeleton, then these meshes are in the "default pose" state
			for (int i=0; i<weightedMeshControls.size(); i++)
			{
				defaultPoseWeightedMeshes[i] = Utilities.createSgWeightedMeshFromControl(weightedMeshControls.get(i));
			}
			//Set the defaultPoseWeightedMesh to be the value of these transformed meshes
			smr.defaultPoseWeightedMeshes.setValue(defaultPoseWeightedMeshes);
			smr.hasDefaultPoseWeightedMeshes.setValue(true);
		}
		
		if (this.hasDifferentBindPose && this.skeleton != null) {
			List<UtilityWeightedMeshControl> weightedMeshControls = skeletonVisualAdapter.getUtilityWeightedMeshControls();
			edu.cmu.cs.dennisc.scenegraph.WeightedMesh[] defaultPoseWeightedMeshes = new edu.cmu.cs.dennisc.scenegraph.WeightedMesh[weightedMeshControls.size()];
			//Go through the weighted mesh controls from the skeletonVisualAdapter and grab the weighted meshes
			//Since we used this adapter to transform the mesh based on the skeleton, then these meshes are in the "default pose" state
			for (int i=0; i<weightedMeshControls.size(); i++)
			{
				defaultPoseWeightedMeshes[i] = (edu.cmu.cs.dennisc.scenegraph.WeightedMesh)weightedMeshControls.get(i).getSgWeightedMesh().newCopy();
				//The weighted meshes themselves aren't enough. The weighted mesh controller actually stores the transformed data in its own buffers
				//GRab the buffers and set them on the weighted meshes to make the meshes correctly represent the transformed meshes
				defaultPoseWeightedMeshes[i].normalBuffer.setValue(weightedMeshControls.get(i).getTransformedNormals().duplicate());
				defaultPoseWeightedMeshes[i].vertexBuffer.setValue(weightedMeshControls.get(i).getTransformedVertices().duplicate());
			}
			//Set the defaultPoseWeightedMesh to be the value of these transformed meshes
			smr.defaultPoseWeightedMeshes.setValue(defaultPoseWeightedMeshes);
			smr.hasDefaultPoseWeightedMeshes.setValue(true);
		}
		
		skeletonVisualAdapter.handleReleased();
		return smr;
	}

	public edu.cmu.cs.dennisc.texture.Texture getAliceTexture(Texture t) {
		if (t == null) {
			System.out.println("NULLL!!!");
			return null;
		}
		boolean flipImage = true;
		edu.cmu.cs.dennisc.texture.BufferedImageTexture aliceTexture = new edu.cmu.cs.dennisc.texture.BufferedImageTexture();
		BufferedImage image = t.getImage();
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
		aliceTexture.setName(t.getName());
//		aliceTexture.setMipMappingDesired(false); //this doesn't actually get saved
		return aliceTexture;
	}

	public List<edu.cmu.cs.dennisc.texture.Texture> getAliceTextures() {
		List<edu.cmu.cs.dennisc.texture.Texture> aliceTextures = new LinkedList<edu.cmu.cs.dennisc.texture.Texture>();
		for (Texture t : this.textures) {
			edu.cmu.cs.dennisc.texture.Texture aliceTexture = getAliceTexture(t);
			if (aliceTexture != null) {
				aliceTextures.add(aliceTexture);
			}
		}
		return aliceTextures;
	}

	public static String makeJavaReadyName(String name) {
		if (name == null) {
			return null;
		}
		List<Character> badChars = new LinkedList<Character>();
		final int N = name.length();
		if (N > 0) {
			char c0 = name.charAt(0);
			if (Character.isLetter(c0) || c0 == '_') {
			} else {
				badChars.add(c0);
			}
			for (int i = 1; i < N; i++) {
				char cI = name.charAt(i);
				if (Character.isLetterOrDigit(cI) || cI == '_') {
					// pass
				} else {
					badChars.add(cI);
				}
			}
		}
		for (Character badChar : badChars) {
			name = name.replace(badChar, '_');
		}
		name = name.toUpperCase();
		return name;
	}

	public TextureGroup getMainTextureGroup() {
		TextureGroup mainTextureGroup = null;
		//Search for the textureGroup marked "isMain"
		for (TextureGroup t : this.textureGroups) {
			if (t.isMain()) {
				mainTextureGroup = t;
				break;
			}
		}
		if (mainTextureGroup != null) {
			return mainTextureGroup;
		}
		//If no group is marked "isMain", find the texture group with the most textures
		int maxCount = 0;
		for (TextureGroup t : this.textureGroups) {
			if (t.getTextureCount() > maxCount) {
				mainTextureGroup = t;
				maxCount = t.getTextureCount();
			}
		}
		return mainTextureGroup;
	}
	
	public List<String> getMainAliceTextureGroupNames() {
		List<String> names = new LinkedList<String>();
		TextureGroup mainTextureGroup = getMainTextureGroup();
		for (int i=0; i<mainTextureGroup.getTextureCount(); i++) {
			String name = null;
			Texture texture = mainTextureGroup.getTexture(i);
			name = makeJavaReadyName(texture.getName());
			assert name != null;
			names.add(name);
		}
		return names;
	}

	public List<TexturedAppearance[]> getAliceTextureGroups() {
		Map<Texture, TexturedAppearance> textureMap = new HashMap<Texture, TexturedAppearance>();
		List<TexturedAppearance[]> aliceTextures = new LinkedList<TexturedAppearance[]>();
		int maxCount = 0;
		for (TextureGroup t : this.textureGroups) {
			if (t.getTextureCount() > maxCount) {
				maxCount = t.getTextureCount();
			}
		}
		for (int i = 0; i < maxCount; i++) {
			TexturedAppearance[] appearances = new TexturedAppearance[this.textureGroups
					.size()];
			for (int t = 0; t < this.textureGroups.size(); t++) {
				TextureGroup tg = this.textureGroups.get(t);
				Texture texture = null;
				if (i < tg.getTextureCount()) {
					texture = tg.getTexture(i);
				} else {
					texture = tg.getActiveTexture();
				}
				if (textureMap.containsKey(texture)) {
					appearances[t] = textureMap.get(texture);
				} else {
					appearances[t] = new TexturedAppearance();
					appearances[t].diffuseColorTexture
							.setValue(getAliceTexture(texture));
					appearances[t].textureId.setValue(tg.getID());
					appearances[t].isDiffuseColorTextureAlphaBlended.setValue(tg.usesAlphaBlend());
					textureMap.put(texture, appearances[t]);
				}
			}
			aliceTextures.add(appearances);
		}
		return aliceTextures;
	}

	public Joint getSkeleton() {
		return this.skeleton;
	}

	@Override
	public void transform(edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans) {
		throw new RuntimeException("todo");
	}

	@Override
	protected void updateBoundingBox(
			edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox) {
		boundingBox.setNaN();
	}

	@Override
	protected void updateBoundingSphere(
			edu.cmu.cs.dennisc.math.Sphere boundingSphere) {
		boundingSphere.setNaN();
		// final double HALF_HEIGHT = 1.5;
		// boundingSphere.center.set( 0.0, HALF_HEIGHT, 0.0 );
		// boundingSphere.radius = HALF_HEIGHT;
	}

	@Override
	protected void updatePlane(edu.cmu.cs.dennisc.math.Vector3 forward,
			edu.cmu.cs.dennisc.math.Vector3 upGuide,
			edu.cmu.cs.dennisc.math.Point3 translation) {
		throw new RuntimeException("todo");
	}

	public static void buildJointMap(Joint s, String parentId,
			List<Tuple2<String, String>> jointList) {
		Tuple2<String, String> jointEntry = Tuple2.createInstance(
				s.jointID.getValue(), parentId);
		jointList.add(jointEntry);
		for (int i = 0; i < s.getComponentCount(); i++) {
			if (s.getComponentAt(i) instanceof Joint) {
				buildJointMap((Joint) s.getComponentAt(i),
						s.jointID.getValue(), jointList);
			}
		}
	}
	
	protected static List<Tuple2<String, String>> getSkeletonJointList(
			SkeletonVisual sv) {
		List<Tuple2<String, String>> jointMap = new ArrayList<Tuple2<String, String>>();
		if (sv.skeleton.getValue() != null) {
			Joint rootJoint = sv.skeleton.getValue();
			buildJointMap(rootJoint, null, jointMap);
			// for (int i = 0; i < rootJoint.getComponentCount(); i++) {
			// if (rootJoint.getComponentAt(i) instanceof Joint) {
			// buildJointMap((Joint) rootJoint.getComponentAt(i), null,
			// jointMap);
			// }
			// }
		}
		return jointMap;
	}

	public static BufferedImage createThumbnail(SkeletonVisual sv, int width, int height) {
		try {
			return AdaptiveRecenteringThumbnailMaker.getInstance(width, height).createThumbnail(sv);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage createThumbnail(SkeletonVisual sv) {
		try {
			return AdaptiveRecenteringThumbnailMaker.getInstance().createThumbnail(sv);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void printJoints(Component joint, String space,
			java.io.PrintWriter pw) {
		if (joint instanceof Joint) {
			pw.println(space + ((Joint) joint).jointID.getValue() +": "+((Joint)joint).getAbsoluteTransformation().translation);
		}
		if (joint instanceof Composite) {
			for (Component c : ((Composite) joint).getComponentsAsArray()) {
				printJoints(c, space + "  ", pw);
			}
		}
	}

	public static boolean shouldMoveOriginToBottomCenter(SkeletonVisual sv) {
		return true;
	}

	
	public static List<Field> getJointIdFields(Class<?> resourceClass) {
		Field[] fields = resourceClass.getDeclaredFields();
		List<Field> jointFields = new ArrayList<Field>();
		for (Field f : fields) {
			if (org.lgna.story.resources.JointId.class.isAssignableFrom(f.getType())) { 
				jointFields.add(f);
			}
		}
		return jointFields;
	}
	
	public static SkeletonVisual addMissingJoints(SkeletonVisual sv, Class<?> resourceClass) throws PipelineException {
		
		List<Field> requiredJointFields = getJointIdFields(resourceClass);
		
		if (sv.skeleton.getValue() == null) {
			System.err.println("NULL SKELETON!!");
			
			if (!requiredJointFields.isEmpty()) {
				System.err.println("No skeleton found on "+sv.getName()+" where its base class requires a skeleton. Base class is "+resourceClass);
				throw new PipelineException("No skeleton found on "+sv.getName()+" where its base class requires a skeleton. Base class is "+resourceClass);
			}
			return sv;
		}
		
		System.out.println("\nCHECKING FOR MISSING JOINTS ON "+sv.getName());
		
		List<org.lgna.story.resources.JointId> missingJoints = new LinkedList<org.lgna.story.resources.JointId>();
		for (Field f : requiredJointFields) {
			String fieldName = f.getName();
			if (sv.skeleton.getValue().getJoint(fieldName) == null) {
				try {
					org.lgna.story.resources.JointId id = (org.lgna.story.resources.JointId) f.get(null);
					missingJoints.add(id);
				} catch (IllegalAccessException iae) {
					iae.printStackTrace();
				}
			}
		}
		int index = 0;
		if (missingJoints.isEmpty()) {
			System.out.println("   NO MISSING JOINTS");
		}
		while (!missingJoints.isEmpty()) {
			System.out.println("   MISSING JOINTS:");
			for (JointId joint : missingJoints) {
				System.out.println("     "+joint);
			}
			JointId currentJoint = missingJoints.get(index);
			System.out.println("   TRYING TO AUTOMATICALLY ADD JOINT "+currentJoint);
			JointId parentJointID = currentJoint.getParent();
			if (parentJointID != null) {
				Joint parentJoint = sv.skeleton.getValue().getJoint(parentJointID.toString());
				if (parentJoint != null) {
					Joint newJoint = new Joint();
					newJoint.jointID.setValue(currentJoint.toString());
					newJoint.setParent(parentJoint);
					missingJoints.remove(index);
					
					System.out.println("   ADDED JOINT "+currentJoint.toString());
				} else {
					System.out.println("   NO JOINT FOUND ON SKELETON FOR "+parentJointID.toString()+", SKIPPING IT");
					index = (index + 1) % missingJoints.size();
				}
			}
			else {
				System.out.println("   NO PARENT JOINT FOR "+currentJoint.toString()+", CAN'T ADD IT");
			}
		}
		return sv;
	}

	public static void translateSkeletonVisual(SkeletonVisual sv,
			Vector3 translation) {
		if (sv.skeleton.getValue() != null) {
			AffineMatrix4x4 rootTransform = sv.skeleton.getValue().localTransformation
					.getValue();
			rootTransform.translation.x += translation.x;
			rootTransform.translation.y += translation.y;
			rootTransform.translation.z += translation.z;
			sv.skeleton.getValue().localTransformation.setValue(rootTransform);
		}
		for (Geometry g : sv.geometries.getValue()) {
			if (g instanceof edu.cmu.cs.dennisc.scenegraph.Mesh) {
				edu.cmu.cs.dennisc.scenegraph.Mesh mesh = (edu.cmu.cs.dennisc.scenegraph.Mesh) g;
				java.nio.DoubleBuffer xyzs = mesh.vertexBuffer.getValue();
				double[] new_xyzs = new double[xyzs.capacity()];
				final int N = xyzs.limit();
				for (int i = 0; i < N; i += 3) {
					new_xyzs[i + 0] = xyzs.get(i + 0) + translation.x;
					new_xyzs[i + 1] = xyzs.get(i + 1) + translation.y;
					new_xyzs[i + 2] = xyzs.get(i + 2) + translation.z;
				}
				mesh.vertexBuffer.setValue(Utilities
						.createDoubleBuffer(new_xyzs));
			}
		}
		AxisAlignedBox bbox = sv.baseBoundingBox.getValue();
		bbox.setXMaximum(bbox.getXMaximum() + translation.x);
		bbox.setXMinimum(bbox.getXMinimum() + translation.x);
		bbox.setYMaximum(bbox.getYMaximum() + translation.y);
		bbox.setYMinimum(bbox.getYMinimum() + translation.y);
		bbox.setZMaximum(bbox.getZMaximum() + translation.z);
		bbox.setZMinimum(bbox.getZMinimum() + translation.z);
		sv.baseBoundingBox.setValue(bbox);
		AxisAlignedBox b = sv.getAxisAlignedMinimumBoundingBox();
//		System.out.println("bbox: "+b);
	}

	protected static boolean hasTuple(List<Tuple2<String, String>> baseMap,
			Tuple2<String, String> tuple) {
		for (Tuple2<String, String> base : baseMap) {
			if (base.equals(tuple)) {
				return true;
			}
		}
		return false;
	}

	public static String getAliceTextureName(String textureName) {
		textureName = FileUtilities.getBaseName(textureName);
		if (textureName.toLowerCase().endsWith("_diffuse")) {
			textureName = textureName.substring(0, textureName.toLowerCase()
					.lastIndexOf("_diffuse"));
		}
		textureName = AliceResourceUtilties.trimName(textureName);
		textureName = textureName.replace(' ', '_');
		while (textureName.contains("__")) {
			textureName = textureName.replace("__", "_");
		}
		return textureName;
	}

	public static String getAliceResourceNameForImageFile(String modelResourceName, String textureName) {
		textureName = getAliceTextureName(textureName);
		return AliceResourceUtilties.getTextureResourceFileName(
				modelResourceName, textureName);
	}


}
