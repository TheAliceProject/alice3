/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.nebulous;

import com.jogamp.opengl.GL;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.math.Sphere;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.NebulousStorytellingResources;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends Geometry {

  static {
    if (SystemUtilities.getBooleanProperty("org.alice.ide.disableDefaultNebulousLoading", false)) {
      //Don't load nebulous resources if the default loading is disabled
      //Disabling should only happen under controlled circumstances like running the model batch process
    } else {
      NebulousStorytellingResources.INSTANCE.loadSimsBundles();
    }
    //    Manager.setDebugDraw( true );
  }

  private enum Material {
    NONE(0, null),
    SIMPLE_TEXTURE(1, new Color4f(1f, 1f, 1f, 1f)),
    GLASS(2, new Color4f(179f / 255f, 223f / 255f, 242f / 255f, 128f / 255f)),
    METAL(3, new Color4f(150f / 255f, 150f / 255f, 160f / 255f, 1f)),
    STONE(4, new Color4f(135f / 255f, 130f / 255f, 130f / 255f, 1f));

    private int value;
    private Color4f color;
    Material(int value, Color4f color) {
      this.value = value;
      this.color = color;
    }

    static Material getMaterialForValue(int value) {
      for (Material mat : Material.values()) {
        if (mat.value == value) {
          return mat;
        }
      }
      return null;
    }
  }

  public Model() throws LicenseRejectedException {
    Manager.initializeIfNecessary();
  }

  private native void render(GL gl, float globalBrightness, boolean renderAlpha, boolean renderOpaque);

  public void synchronizedRender(GL gl, float globalBrightness, boolean renderAlpha, boolean renderOpaque) {
    synchronized (renderLock) {
      try {
        this.render(gl, globalBrightness, renderAlpha, renderOpaque);
      } catch (RuntimeException re) {
        System.err.println(this);
        throw re;
      }
    }
  }

  private native void pick();

  public void synchronizedPick() {
    synchronized (renderLock) {
      pick();
    }
  }

  private native boolean isAlphaBlended();

  public boolean synchronizedIsAlphaBlended() {
    synchronized (renderLock) {
      return isAlphaBlended();
    }
  }

  private native boolean hasOpaque();

  public boolean synchronizedHasOpaque() {
    synchronized (renderLock) {
      return hasOpaque();
    }
  }

  private native void getAxisAlignedBoundingBoxForJoint(String name, String parent, double[] bboxData);

  private native void updateAxisAlignedBoundingBox(double[] bboxData);

  private native void getOriginalTransformationForPartNamed(double[] transformOut, String name, String parent);

  private native void getLocalTransformationForPartNamed(double[] transformOut, String name, String parent);

  private native void setLocalTransformationForPartNamed(String name, String parent, double[] transformIn);

  private native void getAbsoluteTransformationForPartNamed(double[] transformOut, String name);

  private native String[] getUnweightedMeshIds();

  private native String[] getWeightedMeshIds();

  private native String[] getTextureIds();

  private native void prepareModelForExporting();

  protected native int[] getIndicesForMesh(String meshId, String textureId);

  private native float[] getVerticesForMesh(String meshId);

  private native float[] getUnweightedVerticesForMesh(String meshId);

  private native float[] getUnweightedNormalsForMesh(String meshId);

  private native float[] getNormalsForMesh(String meshId);

  private native float[] getUvsForMesh(String meshId);

  protected native String[] getTextureIdsForMesh(String meshId);

  private native boolean isMeshWeightedToJoint(String meshId, String jointId);

  private native double[] getInvAbsTransForWeightedMeshAndJoint(String meshId, String jointId);

  private native float[] getVertexWeightsForWeightedMeshAndJoint(String meshId, String jointId, String parentId);

  private native int getMaterialTypeForTexture(String textureId);

  private native int getImageWidthForTexture(String textureId);

  private native int getImageHeightForTexture(String textureId);

  private native int getBytesPerPixelForTexture(String textureId);

  private native void getImageDataForTexture(String textureId, byte[] imageData);


//  Returning more structured data will be:
//  private native void getSkinWeightsForMesh(SomethingOrOther[] skinWeightsOut, String meshId);

  public void setVisual(Visual visual) {
    this.sgAssociatedVisual = visual;
  }

  public void setSGParent(Composite parent) {
    this.sgParent = parent;
  }

  public Composite getSGParent() {
    return this.sgParent;
  }

  public AffineMatrix4x4 getOriginalTransformationForJoint(JointId joint) {
    double[] buffer = new double[12];
    try {
      getOriginalTransformationForPartNamed(buffer, joint.toString(), joint.getParent() == null ? "" : joint.getParent().toString());
    } catch (RuntimeException re) {
      Logger.severe(joint);
      throw re;
    }
    return AffineMatrix4x4.createFromColumnMajorArray12(buffer);
  }

  public AffineMatrix4x4 getLocalTransformationForJoint(JointId joint) {
    double[] buffer = new double[12];
    try {
      getLocalTransformationForPartNamed(buffer, joint.toString(), joint.getParent() == null ? "" : joint.getParent().toString());
    } catch (RuntimeException re) {
      Logger.severe(joint);
      throw re;
    }
    return AffineMatrix4x4.createFromColumnMajorArray12(buffer);
  }

  public void setLocalTransformationForJoint(JointId joint, AffineMatrix4x4 localTrans) {
    synchronized (renderLock) {
      setLocalTransformationForPartNamed(joint.toString(), joint.getParent() == null ? "" : joint.getParent().toString(), localTrans.getAsColumnMajorArray12());
    }
  }

  public boolean hasJoint(JointId joint) {
    //There's no specific "hasJoint" native call, so this uses the getLocalTransformationForPartNamed and catches the error if the joint isn't found
    //TODO: implement a simpler "hasJoint" in the native code
    double[] buffer = new double[12];
    try {
      getLocalTransformationForPartNamed(buffer, joint.toString(), joint.getParent() == null ? "" : joint.getParent().toString());
    } catch (RuntimeException re) {
      return false;
    }
    return true;
  }

  public AffineMatrix4x4 getAbsoluteTransformationForJoint(JointId joint) {
    double[] buffer = new double[12];
    getAbsoluteTransformationForPartNamed(buffer, joint.toString());
    return AffineMatrix4x4.createFromColumnMajorArray12(buffer);
  }

  @Override
  public void transform(AbstractMatrix4x4 trans) {
    throw new RuntimeException("todo");
  }

  public AxisAlignedBox getAxisAlignedBoundingBoxForJoint(JointId joint) {
    double[] bboxData = new double[6];
    getAxisAlignedBoundingBoxForJoint(joint.toString(), joint.getParent() == null ? "" : joint.getParent().toString(), bboxData);
    AxisAlignedBox bbox = new AxisAlignedBox(bboxData[0], bboxData[1], bboxData[2], bboxData[3], bboxData[4], bboxData[5]);
    bbox.scale(this.sgAssociatedVisual.scale.getValue());
    return bbox;
  }

  @Override
  protected void updateBoundingBox(AxisAlignedBox boundingBox) {
    //the bounding boxes come in the form (double[6])
    double[] bboxData = new double[6];
    updateAxisAlignedBoundingBox(bboxData);
    boundingBox.setMinimum(bboxData[0], bboxData[1], bboxData[2]);
    boundingBox.setMaximum(bboxData[3], bboxData[4], bboxData[5]);
  }

  private WeightInfo createWeightInfo(String meshId, List<JointId> resourceJointIds, Map<Integer, Integer> newIndexToOldVertex, Map<Integer, Integer> oldVertexIndexToNewIndex) {
    WeightInfo weightInfo = new WeightInfo();
    for (JointId joint : resourceJointIds) {
      if (isMeshWeightedToJoint(meshId, joint.toString())) {
        double[] inverseAbsTransform = getInvAbsTransForWeightedMeshAndJoint(meshId, joint.toString());
        float[] vertexWeights = getVertexWeightsForWeightedMeshAndJoint(meshId, joint.toString(), joint.getParent() == null ? "" : joint.getParent().toString());
        //Sims weights reference the old vertex indices so we need to remap them to the new vertices
        //First we find the new highest index
        int maxVertexIndex = 0;
        for (int i = 0; i < vertexWeights.length; i++) {
          if (oldVertexIndexToNewIndex.containsKey(i)) {
            int newVertexIndex = oldVertexIndexToNewIndex.get(i);
            if (newVertexIndex > maxVertexIndex) {
              maxVertexIndex = newVertexIndex;
            }
          } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Model data will be missing some points\n\t");
            this.appendRepr(sb);
            sb.append("\n\tMesh ").append(meshId)
              .append(" has no mapping from old vertex ")
              .append(i)
              .append(" to a new one in the export");
            Logger.warning(sb);
          }
        }
        //Since we stored weights in an array where the index in the array is also the vertex index,
        // this becomes the size of the new weight array
        float[] remappedVertexWeights = new float[maxVertexIndex + 1];
        //Now we need to transfer the weight values from the old array to the new array
        for (int i = 0; i < remappedVertexWeights.length; i++) {
          int oldVertexIndex = newIndexToOldVertex.get(i);
          //If a new index maps outside the range of weights, then set it to be unweighted. Not all vertices have weights.
          if (oldVertexIndex >= vertexWeights.length) {
            remappedVertexWeights[i] = 0;
          } else {
            remappedVertexWeights[i] = vertexWeights[oldVertexIndex];
          }
        }
        AffineMatrix4x4 aliceInverseBindMatrix = AffineMatrix4x4.createFromColumnMajorArray12(inverseAbsTransform);
        InverseAbsoluteTransformationWeightsPair iawp = InverseAbsoluteTransformationWeightsPair.createInverseAbsoluteTransformationWeightsPair(remappedVertexWeights, aliceInverseBindMatrix);
        if (iawp != null) {
          weightInfo.addReference(joint.toString(), iawp);
        }
      }
    }
    return weightInfo;
  }

  private void initializeMesh(String meshId, Mesh mesh, List<JointId> resourceJointIds, Map<String, Integer> textureNamesToIds) {
    String[] meshTextureIds = getTextureIdsForMesh(meshId);
    //Get the appropriate vertices and normals for the mesh type passed in
    //If we're building a weighted mesh, just get the vertices and normals directly
    //If we're building an unweighted mesh, use the specific getForUnweightedMesh call to get values appropriate for an unweighted mesh
    float[] vertices = (mesh instanceof WeightedMesh) ? getVerticesForMesh(meshId) : getUnweightedVerticesForMesh(meshId);
    float[] normals = (mesh instanceof WeightedMesh) ? getNormalsForMesh(meshId) : getUnweightedNormalsForMesh(meshId);
    float[] uvs = getUvsForMesh(meshId);
    /*
    Indices have to be unified. Alice uses a single set of indices for vertices, normals, and UVs
    The Sims use a single array of indices, but separate values for indices into the vertices, normals, and UVS:
    From the Sims rendering code:
    for( size_t i=0; i<nIndexCount;  ) {
      nIndex = pnIndices[ i++ ];
      glTexCoord2fv( vfTextureCoordinates + nIndex );
      nIndex = pnIndices[ i++ ];
      glNormal3fv( vfNormals + nIndex );
      nIndex = pnIndices[ i++ ];
      glVertex3fv( vfVertices + nIndex );
     }
     */
    Map<String, int[]> textureIdToIndices = new HashMap<>();
    int originalIndexCount = 0;
    for (String textureId : meshTextureIds) {
      int[] indices = getIndicesForMesh(meshId, textureId);
      originalIndexCount += indices.length;
      textureIdToIndices.put(textureId, indices);
    }

    int newIndexCount = originalIndexCount / 3;
    double[] newVertices = new double[newIndexCount * 3];
    float[] newNormals = new float[newIndexCount * 3];
    float[] newUVs = new float[newIndexCount * 2];
    String[] newTextureIds = new String[newIndexCount];
    mesh.textureIdArray.ensureCapacity(newIndexCount);
    int[] newIndices = new int[newIndexCount];
    Map<Integer, Integer> oldVertexIndexToNewIndex = new HashMap<>();
    Map<Integer, Integer> newIndexToOldVertex = new HashMap<>();
    int currentIndex = 0;
    for (Map.Entry<String, int[]> indicesEntry: textureIdToIndices.entrySet()) {
        int[] currentIndices = indicesEntry.getValue();
        String currentTextureId = indicesEntry.getKey();
        for (int i = 0; i < currentIndices.length;) {
          //int currentIndex = originalIndex / 3; //sims indices are stored as triplets (uv, normal, vertex), so divide by 3 to get actual index
          int uvIndex = currentIndices[i];
          newUVs[currentIndex * 2] = uvs[uvIndex];
          newUVs[currentIndex * 2 + 1] = uvs[uvIndex + 1];
          i++;
          int normalIndex = currentIndices[i];
          newNormals[currentIndex * 3] = normals[normalIndex];
          newNormals[currentIndex * 3 + 1] = normals[normalIndex + 1];
          newNormals[currentIndex * 3 + 2] = normals[normalIndex + 2];
          i++;
          int vertexIndex = currentIndices[i];
          newVertices[currentIndex * 3] = vertices[vertexIndex];
          newVertices[currentIndex * 3 + 1] = vertices[vertexIndex + 1];
          newVertices[currentIndex * 3 + 2] = vertices[vertexIndex + 2];
          //Add a mapping to the textureId
          newTextureIds[currentIndex] = currentTextureId;
          mesh.textureIdArray.add(currentIndex, textureNamesToIds.get(currentTextureId));
          //Since we're remapping the indices, we'll need to remap the skin weights as well
          //To do this we'll need to store the remapping data
          oldVertexIndexToNewIndex.put(vertexIndex / 3, currentIndex);
          newIndexToOldVertex.put(currentIndex, vertexIndex / 3);
          newIndices[currentIndex] = currentIndex;
          i++;
          currentIndex++;
        }
    }
    mesh.normalBuffer.setValue(BufferUtilities.createDirectFloatBuffer(newNormals));
    mesh.vertexBuffer.setValue(BufferUtilities.createDirectDoubleBuffer(newVertices));
    mesh.textCoordBuffer.setValue(BufferUtilities.createDirectFloatBuffer(newUVs));
    mesh.indexBuffer.setValue(BufferUtilities.createDirectIntBuffer(newIndices));
    mesh.textureId.setValue(textureNamesToIds.get(meshTextureIds[0]));
    if (mesh instanceof  WeightedMesh) {
      WeightInfo weightInfo = createWeightInfo(meshId, resourceJointIds, newIndexToOldVertex, oldVertexIndexToNewIndex);
      ((WeightedMesh) mesh).weightInfo.setValue(weightInfo);
    }
  }

  private Joint createSkeleton(List<JointId> resourceJointIds) {
    Joint rootJoint = null;
    resourceJointIds.sort(Comparator.comparingInt(JointId::hierarchyDepth));
    List<Joint> processedJoints = new ArrayList<>();
    for (JointId currentJointId : resourceJointIds) {
      Joint j = new Joint();
      j.jointID.setValue(currentJointId.toString());
      j.setName(currentJointId.toString());
      j.localTransformation.setValue(getOriginalTransformationForJoint(currentJointId));
      processedJoints.add(j);
      if (currentJointId.getParent() == null) {
        rootJoint = j;
      } else {
        for (Joint p : processedJoints) {
          if (p.jointID.getValue().equals(currentJointId.getParent().toString())) {
            j.setParent(p);
            break;
          }
        }
      }
    }
    return rootJoint;
  }

  private boolean isActuallyWeightedToJoints(String weightedMeshId, List<JointId> resourceJointIds) {
    for (JointId jointId : resourceJointIds) {
      if (isMeshWeightedToJoint(weightedMeshId, jointId.toString())) {
        return true;
      }
    }
    return false;
  }

  public SkeletonVisual createSkeletonVisualForExporting(JointedModelResource resource) {

    prepareModelForExporting();
    //Create skeleton from nebulous data
    List<JointId> resourceJointIds = AliceResourceClassUtilities.getJoints(resource.getClass());
    Joint skeleton = createSkeleton(resourceJointIds);
    List<TexturedAppearance> textures = new ArrayList<>();
    Map<String, Integer> textureNameToIdMap = new HashMap<>();
    String[] textureIds = getTextureIds();
    int idCount = 0;
    for (String textureId : textureIds) {
      int materialType = getMaterialTypeForTexture(textureId);
      Material material =  Material.getMaterialForValue(materialType);
      if (material != null) {
        TexturedAppearance texturedAppearance = new TexturedAppearance();
        texturedAppearance.diffuseColor.setValue(material.color);
        texturedAppearance.textureId.setValue(idCount);
        if (material == Material.SIMPLE_TEXTURE) {
          int width = this.getImageWidthForTexture(textureId);
          int height = this.getImageHeightForTexture(textureId);
          int bytesPerPixel = this.getBytesPerPixelForTexture(textureId);
          byte[] imageData = new byte[width * height * bytesPerPixel];
          this.getImageDataForTexture(textureId, imageData);
          BufferedImage bufferedImage = NebulousTexture.createBufferedImageFromNebulousData(imageData, width, height, bytesPerPixel);
          BufferedImageTexture bufferedImageTexture = new BufferedImageTexture();
          bufferedImageTexture.setBufferedImage(bufferedImage);
          texturedAppearance.diffuseColorTexture.setValue(bufferedImageTexture);
        }
        textures.add(texturedAppearance);
      }
      textureNameToIdMap.put(textureId, idCount);
      idCount++;
    }
    List<Mesh> unWeightedMeshes = new ArrayList<>();
    List<WeightedMesh> weightedMeshes = new ArrayList<>();
    //Build meshes and weighted meshes
    List<String> unweightedMeshIds = new ArrayList<>(Arrays.asList(getUnweightedMeshIds())); //Need this as a list so we can add to it.
    String[] weightedMeshIds = getWeightedMeshIds();
    for (String meshId : weightedMeshIds) {
      //Check to see if the weighted mesh is really weighted to joints, and therefore actually a weighted mesh.
      // Some meshes are listed on the sims side as weighted meshes but are not weighted to any joints
      if (isActuallyWeightedToJoints(meshId, resourceJointIds)) {
        WeightedMesh mesh = new WeightedMesh();
        mesh.setName(meshId);
        initializeMesh(meshId, mesh, resourceJointIds, textureNameToIdMap);
        weightedMeshes.add(mesh);
      } else {
        //Add meshes that are weighted to no joints to the unweighted mesh list
        unweightedMeshIds.add(meshId);
      }
    }
    for (String meshId : unweightedMeshIds) {
      Mesh mesh = new Mesh();
      initializeMesh(meshId, mesh, resourceJointIds, textureNameToIdMap);
      unWeightedMeshes.add(mesh);
    }

    SkeletonVisual skeletonVisual = new SkeletonVisual();
    skeletonVisual.setName(getName());
    skeletonVisual.frontFacingAppearance.setValue(new SimpleAppearance());
    skeletonVisual.skeleton.setValue(skeleton);
    skeletonVisual.geometries.setValue(unWeightedMeshes.toArray(new Mesh[unWeightedMeshes.size()]));
    skeletonVisual.weightedMeshes.setValue(weightedMeshes.toArray(new WeightedMesh[weightedMeshes.size()]));
    skeletonVisual.textures.setValue(textures.toArray(new TexturedAppearance[textures.size()]));

    return skeletonVisual;
  }

  @Override
  protected void updateBoundingSphere(Sphere boundingSphere) {
    boundingSphere.setNaN();
  }

  @Override
  protected void updatePlane(Vector3 forward, Vector3 upGuide, Point3 translation) {
    throw new RuntimeException("todo");
  }

  protected Composite sgParent;
  protected Visual sgAssociatedVisual;

  protected final Object renderLock = new Object();
}
