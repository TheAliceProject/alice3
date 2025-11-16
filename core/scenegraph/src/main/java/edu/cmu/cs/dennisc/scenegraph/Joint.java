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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.property.BooleanProperty;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.StringProperty;
import org.alice.math.immutable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Joint extends Transformable implements ModelJoint {
  //  private static final java.util.Comparator<Joint> JOINT_ARRAY_COMPARATOR = new java.util.Comparator<Joint>() {
  //    @Override
  //    public int compare( Joint o1, Joint o2 ) {
  //      return o1.jointID.getValue().compareTo( o2.jointID.getValue() );
  //    }
  //  };

  private Joint getJoint(Composite c, String jointID) {
    if (c == null) {
      return null;
    }
    if (c instanceof Joint) {
      Joint j = (Joint) c;
      if (j.jointID.getValue().equals(jointID)) {
        return j;
      }
    }
    for (int i = 0; i < c.getComponentCount(); i++) {
      Component comp = c.getComponentAt(i);
      if (comp instanceof Composite) {
        Joint foundJoint = getJoint((Composite) comp, jointID);
        if (foundJoint != null) {
          return foundJoint;
        }
      }
    }
    return null;
  }

  public void scale(double scale) {
    AffineMatrix4x4 newTransform = localTransformation.getValue();
    Point3 scaled = newTransform.translation().times(scale);
    localTransformation.setValue(new AffineMatrix4x4(newTransform.orientation(), scaled));
    AxisAlignedBox bb = boundingBox.getValue();
    if (bb != null) {
      bb = bb.scale(scale);
    }
    for (int i = 0; i < getComponentCount(); i++) {
      Component comp = getComponentAt(i);
      if (comp instanceof Joint) {
        ((Joint) comp).scale(scale);
      }
    }
  }

  public Joint getJoint(String jointID) {
    return getJoint(this, jointID);
  }

  private void getJoints(Composite c, String nameKey, List<Joint> joints) {
    if (c == null) {
      return;
    }
    if (c instanceof Joint) {
      Joint j = (Joint) c;
      if (j.jointID.getValue().startsWith(nameKey)) {
        joints.add(j);
      }
    }
    for (int i = 0; i < c.getComponentCount(); i++) {
      Component comp = c.getComponentAt(i);
      if (comp instanceof Composite) {
        getJoints((Composite) comp, nameKey, joints);
      }
    }
  }

  public Joint[] getJoints(String nameKey) {
    List<Joint> joints = new ArrayList<Joint>();
    getJoints(this, nameKey, joints);
    return joints.toArray(new Joint[joints.size()]);
  }

  public void setParentVisual(SkeletonVisual parentVisual) {
    this.parentVisual = parentVisual;
  }

  private SkeletonVisual getParentVisual() {
    if ((this.parentVisual == null) && (this.getParent() instanceof Joint)) {
      this.parentVisual = ((Joint) this.getParent()).getParentVisual();
    }
    return this.parentVisual;
  }

  private AxisAlignedBox getBoundingBox(Composite c, AffineMatrix4x4 transform, boolean cumulative) {
    if (c == null) {
      return null;
    }
    AxisAlignedBox bounds = AxisAlignedBox.NaN;
    if (c instanceof Joint j) {
      //We scale the local bounding box based on the scale of the SkeletonVisual base object
      //We can do this here (in the local space of the joint) because we restrict the scale to be a uniform scale
      AxisAlignedBox scaledBBox = boundingBox.getValue();
      SkeletonVisual sv = this.getParentVisual();
      if (sv != null) {
        scaledBBox = scaledBBox.scale(sv.scale.getValue());
      }

      Point3 localMin = scaledBBox.minimum();
      Point3 localMax = scaledBBox.maximum();

      Point3 transformedMin = transform.transform(localMin);
      Point3 transformedMax = transform.transform(localMax);
      if (!transformedMin.isNaN()) {
        bounds = bounds.union(transformedMin);
      }
      if (!transformedMax.isNaN()) {
        bounds = bounds.union(transformedMax);
      }
    }
    if (cumulative) {
      for (int i = 0; i < c.getComponentCount(); i++) {
        Component comp = c.getComponentAt(i);
        if (comp instanceof Composite) {
          AffineMatrix4x4 childTransform = transform.times(((AbstractTransformable) comp).getLocalTransformation());
          AxisAlignedBox childAabb = getBoundingBox((Composite) comp, childTransform, cumulative);
          if (childAabb != null && !childAabb.isNaN()) {
            bounds = bounds.union(childAabb);
          }
        }
      }
    }
    return bounds;
  }

  public AxisAlignedBox getBoundingBox(boolean cumulative) {
    return getBoundingBox(this, AffineMatrix4x4.IDENTITY, cumulative);
  }

  @Override
  protected void appendRepr(StringBuilder sb) {
    super.appendRepr(sb);
    sb.append(" jointId=" + this.jointID.getValue());
  }

  public final StringProperty jointID = new StringProperty(this, null);
  public final BooleanProperty isFreeInX = new BooleanProperty(this, false);
  public final BooleanProperty isFreeInY = new BooleanProperty(this, false);
  public final BooleanProperty isFreeInZ = new BooleanProperty(this, false);

  //    public final edu.cmu.cs.dennisc.property.DoubleProperty boundingRadius = new edu.cmu.cs.dennisc.property.DoubleProperty( this, Double.NaN, true);

  public final InstanceProperty<AxisAlignedBox> boundingBox = new InstanceProperty<AxisAlignedBox>(this, AxisAlignedBox.NaN);

  public final InstanceProperty<Vector3f> oStiffness = new InstanceProperty<>(this, Vector3f.ZERO);
  public final InstanceProperty<EulerAngles> oBoneOrientation = new InstanceProperty<>(this, EulerAngles.IDENTITY);
  public final InstanceProperty<EulerAngles> oPreferedAngles = new InstanceProperty<>(this, EulerAngles.IDENTITY);
  public final InstanceProperty<EulerAngles> oLocalRotationAxis = new InstanceProperty<>(this, EulerAngles.IDENTITY);
  public final InstanceProperty<Vector3f> oMinimumDampRange = new InstanceProperty<>(this, Vector3f.ZERO);
  public final InstanceProperty<Vector3f> oMaximumDampRange = new InstanceProperty<>(this, Vector3f.ZERO);
  public final InstanceProperty<Vector3f> oMinimumDampStrength = new InstanceProperty<>(this, Vector3f.ZERO);
  public final InstanceProperty<Vector3f> oMaximumDampStrength = new InstanceProperty<>(this, Vector3f.ZERO);

  private SkeletonVisual parentVisual = null;

  public void visitJoints(Consumer<Joint> op) {
    op.accept(this);
    for (Component c : getComponents()) {
      if (c instanceof Joint) {
        ((Joint) c).visitJoints(op);
      }
    }
  }
}
