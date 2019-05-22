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
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AbstractMatrix4x4;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Sphere;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.story.resources.JointId;
import org.lgna.story.resourceutilities.NebulousStorytellingResources;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;

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

  private native float[] getVerticesForMesh(String meshId);

  private native float[] getNormalsForMesh(String meshId);

  private native float[] getUvsForMesh(String meshId);

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
