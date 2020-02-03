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
 */
package test.ik;

import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.render.PickObserver;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities;
import edu.cmu.cs.dennisc.ui.DragStyle;
import edu.cmu.cs.dennisc.ui.lookingglass.OnscreenLookingGlassDragAdapter;
import edu.cmu.cs.dennisc.ui.scenegraph.SetPointOfViewAction;

import java.awt.Point;
import java.awt.event.MouseEvent;

//TODO(gazi) should move this to an appropriate package other than test.
//todo: allow specification of a reference frame other than absolute

/**
 * @author Dennis Cosgrove
 */
public class NiceDragAdapter extends OnscreenLookingGlassDragAdapter {
  private AbstractCamera m_sgCamera = null;
  private Transformable m_sgDragAcceptor = null;
  private Plane m_planeInAbsolute = Plane.createInstance(0, 1, 0, 0); //todo: edu.cmu.cs.dennisc.math.Plane.NaN?
  private Point3 m_xyzInAbsoluteAtPress = null;
  private Point3 m_xyzInDragAcceptorAtPress = null;
  private Vector3 m_offset = null;

  private AffineMatrix4x4 m_undoPOV;
  private Runnable onClickRunnable;

  public void setOnClickRunnable(Runnable runnable) {
    onClickRunnable = runnable;
  }

  public Point3 getXyzInAbsoluteAtPress() {
    return m_xyzInAbsoluteAtPress;
  }

  @Override
  protected boolean isAcceptable(MouseEvent e) {
    return MouseEventUtilities.isQuoteLeftUnquoteMouseButton(e);
  }

  protected void updateTranslation(Transformable sgDragAcceptor, Tuple3 xyz, ReferenceFrame asSeenBy) {
    if (sgDragAcceptor != null) {
      sgDragAcceptor.setTranslationOnly(xyz, asSeenBy);
    }
  }

  protected Transformable lookupDragAcceptor(Visual sgVisual) {
    Composite sgParent = sgVisual.getParent();
    if (sgParent instanceof Transformable) {
      return (Transformable) sgParent;
    } else {
      return null;
    }
  }

  protected Transformable getDragAcceptor() {
    return m_sgDragAcceptor;
  }

  private Point3 getPointInPlane(Plane plane, int xPixel, int yPixel) {
    Ray ray = getOnscreenRenderTarget().getRayAtPixel(xPixel, yPixel, m_sgCamera);
    AffineMatrix4x4 m = m_sgCamera.getAbsoluteTransformation();
    ray.transform(m);
    double t = plane.intersect(ray);
    return ray.getPointAlong(t);
  }

  private double yDelta = 0.0;

  @Override
  protected void handleMousePress(Point current, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange) {
    if (isOriginalAsOpposedToStyleChange) {
      PickObserver pickObserver = null;
      PickResult pickResult = getOnscreenRenderTarget().getSynchronousPicker().pickFrontMost(current.x, current.y, PickSubElementPolicy.NOT_REQUIRED, pickObserver);
      m_sgCamera = (AbstractCamera) pickResult.getSource();
      Visual sgVisual = pickResult.getVisual();
      if (sgVisual != null) {
        m_sgDragAcceptor = lookupDragAcceptor(sgVisual);
        if (m_sgDragAcceptor != null) {
          m_undoPOV = m_sgDragAcceptor.getTransformation(AsSeenBy.SCENE);
          m_xyzInAbsoluteAtPress = TransformationUtilities.transformToAbsolute_New(pickResult.getPositionInSource(), m_sgCamera);
        }
      }
      this.yDelta = 0.0;
    } else {
      if (m_sgDragAcceptor != null) {
        Ray ray = getOnscreenRenderTarget().getRayAtPixel(current.x, current.y, m_sgCamera);
        ray.transform(m_sgCamera.getAbsoluteTransformation());
        double t = m_planeInAbsolute.intersect(ray);
        m_xyzInAbsoluteAtPress = ray.getPointAlong(t);
        //m_xyzInAbsoluteAtPress.y += this.yDelta;
      }
    }

    if (m_sgDragAcceptor != null) {
      AffineMatrix4x4 m = m_sgDragAcceptor.getAbsoluteTransformation();
      m_offset = Vector3.createSubtraction(m_xyzInAbsoluteAtPress, m.translation);
      m_xyzInDragAcceptorAtPress = m_sgDragAcceptor.transformTo_New(m_xyzInAbsoluteAtPress, m_sgDragAcceptor.getRoot()/* todo: edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE */);
      if (!dragStyle.isShiftDown()) {
        AffineMatrix4x4 cameraAbsolute = m_sgCamera.getAbsoluteTransformation();
        Vector3 axis = Vector3.createSubtraction(cameraAbsolute.translation, m_xyzInAbsoluteAtPress);
        axis.normalize();
        m_planeInAbsolute = Plane.createInstance(m_xyzInAbsoluteAtPress, axis);
      } else {
        m_planeInAbsolute = Plane.createInstance(m_xyzInAbsoluteAtPress, Vector3.accessPositiveYAxis());
      }
    } else {
      m_planeInAbsolute = Plane.NaN;
      m_xyzInAbsoluteAtPress = Point3.createNaN();
      m_xyzInDragAcceptorAtPress = null;
    }

    if (onClickRunnable != null) {
      onClickRunnable.run();
    }
  }

  @Override
  protected void handleMouseDrag(Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, DragStyle dragStyle) {
    if ((m_sgDragAcceptor == null) || m_planeInAbsolute.isNaN() || m_xyzInDragAcceptorAtPress.isNaN()) {
      //pass
    } else {
      if (dragStyle.isShiftDown()) {
        //angular drag
        AffineMatrix4x4 cameraMatrixWrtDragged = m_sgCamera.getTransformation(m_sgDragAcceptor);
        if (dragStyle.isControlDown()) {
          m_sgDragAcceptor.applyRotationAboutArbitraryAxis(cameraMatrixWrtDragged.orientation.up, new AngleInRadians(xDeltaSincePrevious * 0.01));
        } else {
          m_sgDragAcceptor.applyRotationAboutArbitraryAxis(cameraMatrixWrtDragged.orientation.backward, new AngleInRadians(xDeltaSincePrevious * 0.01));
          m_sgDragAcceptor.applyRotationAboutArbitraryAxis(cameraMatrixWrtDragged.orientation.right, new AngleInRadians(yDeltaSincePrevious * 0.01));
        }
      } else {
        //linear drag
        final Point3 xyzInAbsolutePlane = getPointInPlane(m_planeInAbsolute, current.x, current.y);
        xyzInAbsolutePlane.subtract(m_offset);
        getOnscreenRenderTarget().getRenderFactory().invokeLater(new Runnable() {
          @Override
          public void run() {
            updateTranslation(m_sgDragAcceptor, xyzInAbsolutePlane, AsSeenBy.SCENE);
          }
        });
      }
    }
  }

  @Override
  protected Point handleMouseRelease(Point rv, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange) {
    if ((m_sgCamera != null) && (m_sgDragAcceptor != null)) {
      //      if( dragStyle.isControlDown() ) {
      //        java.awt.Point p = m_sgDragAcceptor.transformToAWT_New( m_xyzInDragAcceptorAtPress, getOnscreenRenderTarget(), m_sgCamera );
      //        warpCursor( p );
      //        showCursor();
      //        rv.setLocation( p );
      //      }
    }
    if (isOriginalAsOpposedToStyleChange) {
      if (m_sgDragAcceptor != null) {
        AffineMatrix4x4 redoPOV = m_sgDragAcceptor.getTransformation(AsSeenBy.SCENE);
        if (getUndoRedoManager() != null) {
          getUndoRedoManager().pushAlreadyRunActionOntoUndoStack(new SetPointOfViewAction(getAnimator(), m_sgDragAcceptor, AsSeenBy.SCENE, m_undoPOV, redoPOV));
        }
      }
      m_sgDragAcceptor = null;
      m_planeInAbsolute = Plane.NaN;
    }
    return rv;
  }

}
