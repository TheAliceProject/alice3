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

import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.util.BufferUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.scenegraph.BlendShape;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.WeightedMesh;
import org.lgna.story.resources.JointedModelResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class Person extends Model {
  static {
    AdapterFactory.register(Person.class, PersonAdapter.class);
  }

  private final Object o;

  public Person(Object o) throws LicenseRejectedException {
    this.o = o;
    if (o == null) {
      Logger.severe("NOT INITIALIZING PERSON: lifeStage=null");
    } else {
      synchronized (renderLock) {
        initialize(o);
      }
    }
  }

  private native void initialize(Object o);

  private native String[] getBlendedMeshIds();
  private native float[] getVerticesForBlend(String meshId, int blendId);
  private native float[] getNormalsForBlend(String meshId, int blendId);

  private native void unload();

  public void synchronizedUnload() {
    synchronized (renderLock) {
      unload();
    }
  }

  private native void setGender(Object o);

  public void synchronizedSetGender(Object o) {
    synchronized (renderLock) {
      setGender(o);
    }
  }

  private native void setOutfit(Object o);

  public void synchronizedSetOutfit(Object o) {
    synchronized (renderLock) {
      setOutfit(o);
    }
  }

  private native void setSkinTone(Object o);

  public void synchronizedSetSkinTone(Object o) {
    synchronized (renderLock) {
      setSkinTone(o);
    }
  }

  private native void setObesityLevel(Object o);

  public void synchronizedSetObesityLevel(Object o) {
    synchronized (renderLock) {
      setObesityLevel(o);
    }
  }

  private native void setEyeColor(Object o);

  public void synchronizedSetEyeColor(Object o) {
    synchronized (renderLock) {
      setEyeColor(o);
    }
  }

  private native void setHair(Object o);

  public void synchronizedSetHair(Object o) {
    synchronized (renderLock) {
      setHair(o);
    }
  }

  private native void setFace(Object o);

  public void synchronizedSetFace(Object o) {
    synchronized (renderLock) {
      setFace(o);
    }
  }

  private native void setAll(Object gender, Object outfit, Object skinTone, Object obesityLevel, Object eyeColor, Object hair, Object face);

  public void synchronizedSetAll(Object gender, Object outfit, Object skinTone, Object obesityLevel, Object eyeColor, Object hair, Object face) {
    if (gender == null || outfit == null || skinTone == null || obesityLevel == null || eyeColor == null || hair == null || face == null) {
      Logger.severe("NOT SYNCHRONIZING ATTRIBUTES ON PERSON: gender=" + gender + ", outfit=" + outfit + ", skinTone=" + skinTone + ", obesityLevel=" + obesityLevel + ", eyeColor=" + eyeColor + ", obesityLevel=" + obesityLevel + ", eyeColor=" + eyeColor + ", hair=" + hair + ", face=" + face);
    } else {
      synchronized (renderLock) {
        setAll(gender, outfit, skinTone, obesityLevel, eyeColor, hair, face);
      }
    }
  }

  public void synchronizedSetAll(Object gender, Object outfit, Object skinTone, Object obesityLevel, Object eyeColor, Object hair) {
    synchronizedSetAll(gender, outfit, skinTone, obesityLevel, eyeColor, hair, "");
  }

  @Override
  public SkeletonVisual createSkeletonVisualForExporting(JointedModelResource resource) {
    SkeletonVisual sv = super.createSkeletonVisualForExporting(resource);
    String[] blendedMeshIds = getBlendedMeshIds();
    for (WeightedMesh mesh : sv.weightedMeshes.getValue()) {
      String meshId = mesh.getName();
      if (Arrays.asList(blendedMeshIds).contains(meshId)) {
        ArrayList<BlendShape> shapes = new ArrayList<>();
        String[] meshTextureIds = getTextureIdsForMesh(meshId);
        List<int[]> textureIndices = new ArrayList<>();
        int originalIndexCount = 0;
        for (String textureId : meshTextureIds) {
          int[] indices = getIndicesForMesh(meshId, textureId);
          originalIndexCount += indices.length;
          textureIndices.add(indices);
        }
        for (int i = 0; i < 3; i++) {
          BlendShape blendShape = initializeBlendShape(mesh, i, textureIndices, originalIndexCount);
          if (blendShape != null) {
            shapes.add(blendShape);
          }
        }
        sv.blendShapes.put(mesh, shapes);
      }
    }
    return sv;
  }

  private BlendShape initializeBlendShape(WeightedMesh mesh, int blendShapeId, List<int[]> textureIndices, int originalIndexCount) {
    String meshId = mesh.getName();
    BlendShape blendShape = new BlendShape(blendShapeId);
    float[] vertices = getVerticesForBlend(meshId, blendShapeId);
    if (vertices.length == 0) {
      return null;
    }
    float[] normals = getNormalsForBlend(meshId, blendShapeId);
    double[] newVertices = new double[originalIndexCount];
    float[] newNormals = new float[originalIndexCount];
    int currentIndex = 0;
    for (int[] currentIndices: textureIndices) {
      for (int i = 0; i < currentIndices.length;) {
        // First entry in texture triples is for uv. Skip it.
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
        i++;
        currentIndex++;
      }
    }
    blendShape.normalBuffer = BufferUtilities.createDirectFloatBuffer(newNormals);
    blendShape.vertexBuffer = BufferUtilities.createDirectDoubleBuffer(newVertices);
    return blendShape;
  }

  @Override
  protected void appendRepr(StringBuilder sb) {
    super.appendRepr(sb);
    sb.append(";");
    sb.append(this.o != null ? this.o.getClass().getName() : null);
    sb.append(".");
    sb.append(this.o);
  }
}
