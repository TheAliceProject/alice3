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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.math.Vector4;
import edu.cmu.cs.dennisc.pattern.Visitable;
import edu.cmu.cs.dennisc.pattern.Visitor;
import edu.cmu.cs.dennisc.render.PicturePlaneUtils;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener;

import java.awt.Point;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class Component extends Element implements Visitable, ReferenceFrame {
  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public Composite getRoot() {
    if (this.vehicle != null) {
      return this.vehicle.getRoot();
    } else {
      return null;
    }
  }

  @Override
  public AffineMatrix4x4 getAbsoluteTransformation(AffineMatrix4x4 rv) {
    if (this.vehicle != null) {
      rv = this.vehicle.getAbsoluteTransformation(rv);
    } else {
      rv.setIdentity();
    }
    return rv;
  }

  @Override
  public final AffineMatrix4x4 getAbsoluteTransformation() {
    return getAbsoluteTransformation(AffineMatrix4x4.createNaN());
  }

  @Override
  public AffineMatrix4x4 getInverseAbsoluteTransformation(AffineMatrix4x4 rv) {
    if (this.vehicle != null) {
      rv = this.vehicle.getInverseAbsoluteTransformation(rv);
    } else {
      rv.setIdentity();
    }
    return rv;
  }

  @Override
  public final AffineMatrix4x4 getInverseAbsoluteTransformation() {
    return getInverseAbsoluteTransformation(AffineMatrix4x4.createNaN());
  }

  @Override
  public AffineMatrix4x4 getTransformation(AffineMatrix4x4 rv, ReferenceFrame asSeenBy) {
    if (this.vehicle != null) {
      return this.vehicle.getTransformation(rv, asSeenBy);
    } else {
      return asSeenBy.getInverseAbsoluteTransformation(rv);
    }
  }

  @Override
  public final AffineMatrix4x4 getTransformation(ReferenceFrame asSeenBy) {
    return getTransformation(AffineMatrix4x4.createNaN(), asSeenBy);
  }

  public Point3 getTranslation(Point3 rv, ReferenceFrame asSeenBy) {
    rv.set(getTransformation(asSeenBy).translation);
    return rv;
  }

  public final Point3 getTranslation(ReferenceFrame asSeenBy) {
    return getTranslation(new Point3(), asSeenBy);
  }

  public OrthogonalMatrix3x3 getAxes(OrthogonalMatrix3x3 rv, ReferenceFrame asSeenBy) {
    rv.setValue(getTransformation(asSeenBy).orientation);
    return rv;
  }

  public final OrthogonalMatrix3x3 getAxes(ReferenceFrame asSeenBy) {
    return getAxes(OrthogonalMatrix3x3.createNaN(), asSeenBy);
  }

  public Composite getParent() {
    return this.vehicle;
  }

  public void setParent(Composite parent) {
    if (vehicle == parent) {
      return;
    }
    setParentInHierarchy(parent);
    fireAbsoluteTransformationChange();
    fireHierarchyChanged();
  }

  public void setParentWithoutMoving(Composite parent) {
    if (vehicle == parent) {
      return;
    }
    setParentInHierarchy(parent);
    fireHierarchyChanged();
  }

  private void setParentInHierarchy(Composite parent) {
    if (vehicle != null) {
      vehicle.fireChildRemoved(this);
    }
    vehicle = parent;
    if (vehicle != null) {
      vehicle.fireChildAdded(this);
    }
  }

  @Override
  public boolean isLocalOf(Component other) {
    return this == other;
  }

  @Override
  public boolean isVehicleOf(Component other) {
    return this == other.getParent();
  }

  @Override
  public boolean isSceneOf(Component other) {
    return this == other.getRoot();
  }

  public boolean isDescendantOf(Composite possibleAncestor) {
    if (possibleAncestor == null) {
      return false;
    }
    if (this.vehicle == possibleAncestor) {
      return true;
    } else {
      if (this.vehicle == null) {
        return false;
      } else {
        return this.vehicle.isDescendantOf(possibleAncestor);
      }
    }
  }

  public void addAbsoluteTransformationListener(AbsoluteTransformationListener absoluteTransformationListener) {
    this.absoluteTransformationListeners.add(absoluteTransformationListener);
  }

  public void removeAbsoluteTransformationListener(AbsoluteTransformationListener absoluteTransformationListener) {
    this.absoluteTransformationListeners.remove(absoluteTransformationListener);
  }

  private void fireAbsoluteTransformationChange(AbsoluteTransformationEvent absoluteTransformationEvent) {
    for (AbsoluteTransformationListener atl : this.absoluteTransformationListeners) {
      atl.absoluteTransformationChanged(absoluteTransformationEvent);
    }
  }

  protected void fireAbsoluteTransformationChange() {
    fireAbsoluteTransformationChange(new AbsoluteTransformationEvent(this));
  }

  public void addHierarchyListener(HierarchyListener hierarchyListener) {
    this.hierarchyListeners.add(hierarchyListener);
  }

  public void removeHierarchyListener(HierarchyListener hierarchyListener) {
    this.hierarchyListeners.remove(hierarchyListener);
  }

  private void fireHierarchyChanged(HierarchyEvent hierarchyEvent) {
    for (HierarchyListener hl : this.hierarchyListeners) {
      hl.hierarchyChanged(hierarchyEvent);
    }
  }

  protected void fireHierarchyChanged() {
    fireHierarchyChanged(new HierarchyEvent(this));
  }

  // Vector4
  public Vector4 transformToAbsolute(Vector4 xyz) {
    Vector4 rv = new Vector4(xyz);
    if (!isAbsolute()) {
      AffineMatrix4x4 m = getAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public Vector4 transformFromAbsolute(Vector4 xyz) {
    Vector4 rv = new Vector4(xyz);
    if (!isAbsolute()) {
      AffineMatrix4x4 m = getInverseAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public Vector4 transformTo(Vector4 xyzw, Component to) {
    Vector4 p = transformToAbsolute(xyzw);
    return to.transformFromAbsolute(p);
  }

  public Vector4 transformFrom(Vector4 xyzw, Component from) {
    return from.transformTo(xyzw, this);
  }

  // Vector3
  public Vector3 transformToAbsolute(Vector3 xyz) {
    Vector3 rv = new Vector3(xyz);
    if (!isAbsolute()) {
      AffineMatrix4x4 m = getAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public Vector3 transformFromAbsolute(Vector3 xyz) {
    Vector3 rv = new Vector3(xyz);
    if (!isAbsolute()) {
      AffineMatrix4x4 m = getInverseAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public Vector3 transformTo(Vector3 xyz, Component to) {
    Vector3 p = transformToAbsolute(xyz);
    return to.transformFromAbsolute(p);
  }

  //Point3d
  public Point3 transformToAbsolute(Point3 xyz) {
    Point3 rv = new Point3(xyz);
    if (!isAbsolute()) {
      AffineMatrix4x4 m = getAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public Point3 transformFromAbsolute(Point3 xyz) {
    Point3 rv = new Point3(xyz);
    if (!isAbsolute()) {
      AffineMatrix4x4 m = getInverseAbsoluteTransformation();
      m.transform(rv);
    }
    return rv;
  }

  public Point3 transformTo(Point3 xyz, Component to) {
    Point3 p = transformToAbsolute(xyz);
    return to.transformFromAbsolute(p);
  }

  public Point3 transformFrom(Point3 xyz, Component from) {
    return from.transformTo(xyz, this);
  }


  public Vector3 transformFrom(Vector3 xyz, Component from) {
    return from.transformTo(xyz, this);
  }

  private boolean isAbsolute() {
    //todo: call isSceneOf()?
    if ((this instanceof Scene) || ((ReferenceFrame) this == AsSeenBy.SCENE)) {
      return true;
    } else {
      return false;
    }
  }

  // AWT (yes, java's ancient ui code) transformations are used by aabb collision, isInView, and speech/thought bubbles
  // where we need to know what's actually showing in the ui
  // TODO- this logic is balanced on a deprecated function- getActualViewportAsAwtRectangle. Is there a replacement?
  public Point transformToAWT(Vector4 xyzw, RenderTarget renderTarget, AbstractCamera camera) {
    Vector4 s_buffer = new Vector4(xyzw);
    if (this != camera) {
      s_buffer = transformTo(s_buffer, camera);
    }
    return PicturePlaneUtils.transformFromCameraToAWT(s_buffer, renderTarget, camera);
  }

  private final List<AbsoluteTransformationListener> absoluteTransformationListeners = Lists.newCopyOnWriteArrayList();
  private final List<HierarchyListener> hierarchyListeners = Lists.newCopyOnWriteArrayList();
  private Composite vehicle = null;
}
