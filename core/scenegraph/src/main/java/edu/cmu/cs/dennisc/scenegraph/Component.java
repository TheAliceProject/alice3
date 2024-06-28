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
import edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities;

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

  public Vector4 transformTo(Vector4 rv, Vector4 xyzw, Component to) {
    return TransformationUtilities.transformTo(rv, xyzw, this, to);
  }

  public Vector4 transformFrom(Vector4 rv, Vector4 xyzw, Component from) {
    return from.transformTo(rv, xyzw, this);
  }

  public Point3 transformTo(Point3 rv, Point3 xyz, Component to) {
    return TransformationUtilities.transformTo(rv, xyz, this, to);
  }

  public Point3 transformFrom(Point3 rv, Point3 xyz, Component from) {
    return from.transformTo(rv, xyz, this);
  }

  public Vector3 transformTo(Vector3 rv, Vector3 xyz, Component to) {
    return TransformationUtilities.transformTo(rv, xyz, this, to);
  }

  public Vector3 transformFrom(Vector3 rv, Vector3 xyz, Component from) {
    return from.transformTo(rv, xyz, this);
  }

  public Vector4 transformTo_New(Vector4 xyzw, Component to) {
    return TransformationUtilities.transformTo_New(xyzw, this, to);
  }

  public Vector4 transformFrom_New(Vector4 xyzw, Component from) {
    return from.transformTo_New(xyzw, this);
  }

  public Point3 transformTo_New(Point3 xyz, Component to) {
    return TransformationUtilities.transformTo_New(xyz, this, to);
  }

  public Point3 transformFrom_New(Point3 xyz, Component from) {
    return from.transformTo_New(xyz, this);
  }

  public Vector3 transformTo_New(Vector3 xyz, Component to) {
    return TransformationUtilities.transformTo_New(xyz, this, to);
  }

  public Vector3 transformFrom_New(Vector3 xyz, Component from) {
    return from.transformTo_New(xyz, this);
  }

  public Vector4 transformTo_AffectReturnValuePassedIn(Vector4 xyzw, Component to) {
    return TransformationUtilities.transformTo_AffectReturnValuePassedIn(xyzw, this, to);
  }

  public Vector4 transformFrom_AffectReturnValuePassedIn(Vector4 xyzw, Component from) {
    return from.transformTo_AffectReturnValuePassedIn(xyzw, this);
  }

  public Point3 transformTo_AffectReturnValuePassedIn(Point3 xyz, Component to) {
    return TransformationUtilities.transformTo_AffectReturnValuePassedIn(xyz, this, to);
  }

  public Point3 transformFrom_AffectReturnValuePassedIn(Point3 xyz, Component from) {
    return from.transformTo_AffectReturnValuePassedIn(xyz, this);
  }

  public Vector3 transformTo_AffectReturnValuePassedIn(Vector3 xyz, Component to) {
    return TransformationUtilities.transformTo_AffectReturnValuePassedIn(xyz, this, to);
  }

  public Vector3 transformFrom_AffectReturnValuePassedIn(Vector3 xyz, Component from) {
    return from.transformTo_AffectReturnValuePassedIn(xyz, this);
  }

  private static final Vector4 s_buffer = new Vector4();

  public Point transformToAWT(Point rv, Vector4 xyzw, RenderTarget renderTarget, AbstractCamera camera) {
    synchronized (s_buffer) {
      if (this != camera) {
        transformTo(s_buffer, xyzw, camera);
      } else {
        s_buffer.set(xyzw);
      }
      PicturePlaneUtils.transformFromCameraToAWT(rv, s_buffer, renderTarget, camera);
    }
    return rv;
  }

  public Point transformToAWT_New(Vector4 xyzw, RenderTarget renderTarget, AbstractCamera camera) {
    return transformToAWT(new Point(), xyzw, renderTarget, camera);
  }

  public Point transformToAWT_New(Point3 xyz, RenderTarget renderTarget, AbstractCamera camera) {
    return transformToAWT_New(new Vector4(xyz.x, xyz.y, xyz.z, 1.0), renderTarget, camera);
  }

  private final List<AbsoluteTransformationListener> absoluteTransformationListeners = Lists.newCopyOnWriteArrayList();
  private final List<HierarchyListener> hierarchyListeners = Lists.newCopyOnWriteArrayList();
  private Composite vehicle = null;
}
