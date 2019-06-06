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
import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.math.Sphere;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.NebulousStorytellingResources;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  private native void getAxisAlignedBoundingBoxForJoint(JointId name, JointId parent, double[] bboxData);

  private native void updateAxisAlignedBoundingBox(double[] bboxData);

  private native void getOriginalTransformationForPartNamed(double[] transformOut, JointId name, JointId parent);

  private native void getLocalTransformationForPartNamed(double[] transformOut, JointId name, JointId parent);

  private native void setLocalTransformationForPartNamed(JointId name, JointId parent, double[] transformIn);

  private native void getAbsoluteTransformationForPartNamed(double[] transformOut, JointId name);

  private native String[] getUnweightedMeshIds();

  private native String[] getWeightedMeshIds();

  private native String[] getTextureIds();

  private native int[] getIndicesForMesh(String meshId);

  private native float[] getVerticesForMesh(String meshId);

  private native float[] getNormalsForMesh(String meshId);

  private native float[] getUvsForMesh(String meshId);

  private native boolean isMeshWeightedToJoint(String meshId, JointId jointId);

  private native double[] getInvAbsTransForWeightedMeshAndJoint(String meshId, JointId jointId);

  private native float[] getVertexWeightsForWeightedMeshAndJoint(String meshId, JointId jointId);

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
      getOriginalTransformationForPartNamed(buffer, joint, joint.getParent());
    } catch (RuntimeException re) {
      Logger.severe(joint);
      throw re;
    }
    AffineMatrix4x4 affineMatrix = AffineMatrix4x4.createFromColumnMajorArray12(buffer);
    return affineMatrix;
  }

  public AffineMatrix4x4 getLocalTransformationForJoint(JointId joint) {
    double[] buffer = new double[12];
    try {
      getLocalTransformationForPartNamed(buffer, joint, joint.getParent());
    } catch (RuntimeException re) {
      Logger.severe(joint);
      throw re;
    }
    AffineMatrix4x4 affineMatrix = AffineMatrix4x4.createFromColumnMajorArray12(buffer);
    return affineMatrix;
  }

  public void setLocalTransformationForJoint(JointId joint, AffineMatrix4x4 localTrans) {
    synchronized (renderLock) {
      setLocalTransformationForPartNamed(joint, joint.getParent(), localTrans.getAsColumnMajorArray12());
    }
  }

  public boolean hasJoint(JointId joint) {
    //There's no specific "hasJoint" native call, so this uses the getLocalTransformationForPartNamed and catches the error if the joint isn't found
    //TODO: implement a simpler "hasJoint" in the native code
    double[] buffer = new double[12];
    try {
      getLocalTransformationForPartNamed(buffer, joint, joint.getParent());
    } catch (RuntimeException re) {
      return false;
    }
    return true;
  }

  public AffineMatrix4x4 getAbsoluteTransformationForJoint(JointId joint) {
    double[] buffer = new double[12];
    getAbsoluteTransformationForPartNamed(buffer, joint);
    return AffineMatrix4x4.createFromColumnMajorArray12(buffer);
  }

  @Override
  public void transform(AbstractMatrix4x4 trans) {
    throw new RuntimeException("todo");
  }

  public AxisAlignedBox getAxisAlignedBoundingBoxForJoint(JointId joint) {
    double[] bboxData = new double[6];
    getAxisAlignedBoundingBoxForJoint(joint, joint.getParent(), bboxData);
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

  private void initializeMesh(String meshId, Mesh mesh, List<JointId> resourceJointIds) {

    float[] vertices = getVerticesForMesh(meshId);
    float[] normals = getNormalsForMesh(meshId);
    float[] uvs = getUvsForMesh(meshId);
    int[] indices = getIndicesForMesh(meshId);

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

    int indexCount = indices.length / 3;
    double[] newVertices = new double[indexCount * 3];
    float[] newNormals = new float[indexCount * 3];
    float[] newUVs = new float[indexCount * 2];
    int[] newIndices = new int[indexCount];
    Map<Integer, Integer> oldVertexIndexToNewIndex = new HashMap<>();
    Map<Integer, Integer> newIndexToOldVertex = new HashMap<>();
    for (int i = 0; i < indices.length; ) {
      int currentIndex = i / 3;

      int uvIndex = indices[i];
      newUVs[currentIndex * 2] = uvs[uvIndex];
      newUVs[currentIndex * 2 + 1] = uvs[uvIndex + 1];
      i++;

      int normalIndex = indices[i];
      newNormals[currentIndex * 3] = normals[normalIndex];
      newNormals[currentIndex * 3 + 1] = normals[normalIndex + 1];
      newNormals[currentIndex * 3 + 2] = normals[normalIndex + 2];
      i++;

      int vertexIndex = indices[i];
      newVertices[currentIndex * 3] = vertices[vertexIndex];
      newVertices[currentIndex * 3 + 1] = vertices[vertexIndex + 1];
      newVertices[currentIndex * 3 + 2] = vertices[vertexIndex + 2];
      oldVertexIndexToNewIndex.put(vertexIndex / 3, currentIndex);
      newIndexToOldVertex.put(currentIndex, vertexIndex / 3);

      newIndices[currentIndex] = currentIndex;
      i++;
    }

    mesh.normalBuffer.setValue(BufferUtilities.createDirectFloatBuffer(newNormals));
    mesh.vertexBuffer.setValue(BufferUtilities.createDirectDoubleBuffer(newVertices));
    mesh.textCoordBuffer.setValue(BufferUtilities.createDirectFloatBuffer(newUVs));
    mesh.indexBuffer.setValue(BufferUtilities.createDirectIntBuffer(newIndices));

    if (mesh instanceof  WeightedMesh) {
      WeightedMesh weightedMesh = (WeightedMesh) mesh;
      WeightInfo weightInfo = new WeightInfo();
      for (JointId joint : resourceJointIds) {
        if (isMeshWeightedToJoint(meshId, joint)) {
          double[] inverseAbsTransform = getInvAbsTransForWeightedMeshAndJoint(meshId, joint);
          float[] vertexWeights = getVertexWeightsForWeightedMeshAndJoint(meshId, joint);

          int maxVertexIndex = 0;
          for (int i = 0; i < vertexWeights.length; i++) {
            if (!oldVertexIndexToNewIndex.containsKey(i)) {
              System.out.println("NO " + i);
            } else {
              int newVertexIndex = oldVertexIndexToNewIndex.get(i);
              if (newVertexIndex > maxVertexIndex) {
                maxVertexIndex = newVertexIndex;
              }
            }
          }
          float[] remappedVertexWeights = new float[maxVertexIndex + 1];
          for (int i = 0; i < remappedVertexWeights.length; i++) {
            remappedVertexWeights[i] = vertexWeights[newIndexToOldVertex.get(i)];
          }

          int nonZeroWeights = 0;
          for (float weight : remappedVertexWeights) {
            if (weight != 0) {
              nonZeroWeights++;
            }
          }
          if (nonZeroWeights > 0) {
            InverseAbsoluteTransformationWeightsPair iawp;
            double portion = ((double) nonZeroWeights) / remappedVertexWeights.length;
            if (portion > .9) {
              iawp = new PlentifulInverseAbsoluteTransformationWeightsPair();
            } else {
              iawp = new SparseInverseAbsoluteTransformationWeightsPair();
            }
            AffineMatrix4x4 aliceInverseBindMatrix = AffineMatrix4x4.createFromColumnMajorArray12(inverseAbsTransform);
            iawp.setWeights(remappedVertexWeights);
            iawp.setInverseAbsoluteTransformation(aliceInverseBindMatrix);
            weightInfo.addReference(joint.toString(), iawp);
          }
        }
      }
      weightedMesh.weightInfo.setValue(weightInfo);
    }

  }

  private Joint createSkeleton(List<JointId> resourceJointIds) {
    Joint rootJoint = null;
    List<JointId> processedIds = new ArrayList<>();
    List<Joint> processedJoints = new ArrayList<>();
    while (resourceJointIds.size() != processedIds.size()) {
      for (JointId currentJointId : resourceJointIds) {
        if (currentJointId.getParent() == null || processedIds.contains(currentJointId.getParent())) {
          Joint j = new Joint();
          j.jointID.setValue(currentJointId.toString());
          j.setName(currentJointId.toString());
          j.localTransformation.setValue(getOriginalTransformationForJoint(currentJointId));

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
          processedIds.add(currentJointId);
          processedJoints.add(j);
        }
      }
    }
    return rootJoint;
  }

  public SkeletonVisual createSkeletonVisual(JointedModelResource resource) {
    //Create skeleton from nebulous data
    List<JointId> resourceJointIds = AliceResourceClassUtilities.getJoints(resource.getClass());
    Joint skeleton = createSkeleton(resourceJointIds);

    List<Mesh> unWeightedMeshes = new ArrayList<>();
    List<WeightedMesh> weightedMeshes = new ArrayList<>();
    //Build meshes and weighted meshes
    String[] unweightedMeshIds = getUnweightedMeshIds();
    String[] weightedMeshIds = getWeightedMeshIds();


    for (String meshId : weightedMeshIds) {
      Map<Integer, Integer> uvIndexToNewIndex = new HashMap<>();
      Map<Integer, Integer> normalIndexToNewIndex = new HashMap<>();
      Map<Integer, Integer> vertexIndexToNewIndex = new HashMap<>();
      WeightedMesh mesh = new WeightedMesh();
      initializeMesh(meshId, mesh, resourceJointIds);
      mesh.textureId.setValue(0);
      weightedMeshes.add(mesh);
    }
    for (String meshId : unweightedMeshIds) {
      Mesh mesh = new Mesh();
      initializeMesh(meshId, mesh, resourceJointIds);
      mesh.textureId.setValue(0);
      unWeightedMeshes.add(mesh);
    }

    List<TexturedAppearance> textures = new ArrayList<>();
    String[] textureIds = getTextureIds();
    int idCount = 0;
    for (String textureId : textureIds) {
      int width = this.getImageWidthForTexture(textureId);
      int height = this.getImageHeightForTexture(textureId);
      int bytesPerPixel = this.getBytesPerPixelForTexture(textureId);
      byte[] imageData = new byte[width * height * bytesPerPixel];
      this.getImageDataForTexture(textureId, imageData);

      BufferedImage bufferedImage = NebulousTexture.createBufferedImageFromNebulousData(imageData, width, height, bytesPerPixel);

      BufferedImageTexture bufferedImageTexture = new BufferedImageTexture();
      bufferedImageTexture.setBufferedImage(bufferedImage);
      TexturedAppearance texturedAppearance = new TexturedAppearance();
      texturedAppearance.diffuseColorTexture.setValue(bufferedImageTexture);
      texturedAppearance.textureId.setValue(idCount);
      textures.add(texturedAppearance);
      idCount++;
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

  public Object TESTING_getResourceKey() {
    return this;
  }

  protected Composite sgParent;
  protected Visual sgAssociatedVisual;

  protected final Object renderLock = new Object();
}
