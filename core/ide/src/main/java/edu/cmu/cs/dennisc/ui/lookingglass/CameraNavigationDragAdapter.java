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
package edu.cmu.cs.dennisc.ui.lookingglass;

import edu.cmu.cs.dennisc.clock.Clock;
import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.rungekutta.RungeKuttaUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import edu.cmu.cs.dennisc.render.event.RenderTargetDisplayChangeEvent;
import edu.cmu.cs.dennisc.render.event.RenderTargetInitializeEvent;
import edu.cmu.cs.dennisc.render.event.RenderTargetListener;
import edu.cmu.cs.dennisc.render.event.RenderTargetRenderEvent;
import edu.cmu.cs.dennisc.render.event.RenderTargetResizeEvent;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.ui.DragStyle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

enum CameraNavigationMode {
  TRANSLATE_XZ, TRANSLATE_Y, ORBIT
}

/**
 * @author Dennis Cosgrove
 */
public class CameraNavigationDragAdapter extends OnscreenLookingGlassDragAdapter implements MouseWheelListener, RenderTargetListener {
  private CameraNavigationFunction m_function = new CameraNavigationFunction();
  private double m_tPrev = Double.NaN;

  private Cursor m_awtCursorPrev = null;
  private double m_metersPerMouseWheelTick;

  public CameraNavigationDragAdapter() {
    m_metersPerMouseWheelTick = 1.0;
  }

  @Override
  protected boolean isAcceptable(MouseEvent e) {
    return MouseEventUtilities.isQuoteRightUnquoteMouseButton(e);
  }

  @Override
  public void setOnscreenRenderTarget(OnscreenRenderTarget onscreenLookingGlass) {
    super.setOnscreenRenderTarget(onscreenLookingGlass);
    onscreenLookingGlass.getRenderFactory().addAutomaticDisplayListener(new AutomaticDisplayListener() {
      @Override
      public void automaticDisplayCompleted(AutomaticDisplayEvent e) {
        CameraNavigationDragAdapter.this.handleDisplayed();
      }
    });
  }

  private void handleDisplayed() {
    AbstractCamera sgCamera = getSGCamera();
    if (sgCamera != null) {
      double tCurr = Clock.getCurrentTime();
      if (Double.isNaN(m_tPrev)) {
        m_tPrev = Clock.getCurrentTime();
      }
      double tDelta = tCurr - m_tPrev;
      update(tDelta);
      m_tPrev = tCurr;
    }
  }

  private int m_sgCameraIndex = 0;
  private boolean isMultipleCameraWarningAlreadyDelivered = false;

  public AbstractCamera getSGCamera() {
    OnscreenRenderTarget onscreenLookingGlass = getOnscreenRenderTarget();
    if (onscreenLookingGlass != null) {
      int cameraCount = onscreenLookingGlass.getSgCameraCount();
      if ((cameraCount > 1) && (this.isMultipleCameraWarningAlreadyDelivered == false)) {
        Logger.warning("onscreenLookingGlass camera count:", onscreenLookingGlass.getSgCameraCount());
        this.isMultipleCameraWarningAlreadyDelivered = true;
      }
      if (m_sgCameraIndex < cameraCount) {
        return onscreenLookingGlass.getSgCameraAt(m_sgCameraIndex);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public Transformable getSGCameraTransformable() {
    AbstractCamera sgCamera = getSGCamera();
    if (sgCamera != null) {
      return (Transformable) sgCamera.getParent();
    } else {
      return null;
    }
  }

  private CameraNavigationMode m_cameraNavigationMode = null;
  private int m_xPixel0 = -1;
  private int m_yPixel0 = -1;
  private int m_xPixelPrev = -1;
  private int m_yPixelPrev = -1;

  public CameraNavigationMode getCameraNavigationMode() {
    return m_cameraNavigationMode;
  }

  public void startCameraNavigationMode(CameraNavigationMode cameraNavigationMode, int x, int y) {
    m_cameraNavigationMode = cameraNavigationMode;
    m_xPixel0 = x;
    m_yPixel0 = y;
    m_xPixelPrev = m_xPixel0;
    m_yPixelPrev = m_yPixel0;
  }

  public void stopCameraNavigationMode() {
    m_function.stopImmediately();
    m_cameraNavigationMode = null;
  }

  public void handleMouseDragged(int xPixel, int yPixel) {
    final double TRANSLATION_XZ_FACTOR = 0.05;
    final double TRANSLATION_Y_FACTOR = 0.05;
    final double ORBIT_YAW_FACTOR = 0.02;
    final double ORBIT_PITCH_FACTOR = 0.002;
    if (m_cameraNavigationMode != null) {
      if (m_cameraNavigationMode == CameraNavigationMode.ORBIT) {
        int xPixelDelta = xPixel - m_xPixelPrev;
        int yPixelDelta = yPixel - m_yPixelPrev;
        m_function.requestOrbit(xPixelDelta * ORBIT_YAW_FACTOR, -yPixelDelta * ORBIT_PITCH_FACTOR);
      } else if (m_cameraNavigationMode == CameraNavigationMode.TRANSLATE_XZ) {
        m_function.requestVelocity((xPixel - m_xPixel0) * TRANSLATION_XZ_FACTOR, 0, (yPixel - m_yPixel0) * TRANSLATION_XZ_FACTOR);
      } else if (m_cameraNavigationMode == CameraNavigationMode.TRANSLATE_Y) {
        m_function.requestVelocity(0, -((yPixel - m_yPixel0) * TRANSLATION_Y_FACTOR), 0);
      }
      m_xPixelPrev = xPixel;
      m_yPixelPrev = yPixel;
    }
  }

  public void requestTarget(double x, double y, double z) {
    m_function.requestTarget(x, y, z);
  }

  public void requestTarget(Point3 target) {
    m_function.requestTarget(target.x, target.y, target.z);
  }

  public void requestYaw(Angle yaw) {
    m_function.requestYaw(yaw);
  }

  public void requestDistance(double distance) {
    m_function.requestDistance(distance);
  }

  public Angle getYawRequested() {
    return m_function.getYawRequested();
  }

  public Angle getPitchRequested() {
    return m_function.getPitchRequested();
  }

  public double getDistanceRequested() {
    return m_function.getDistanceRequested();
  }

  public Point3 accessTargetRequested() {
    return m_function.accessTargetRequested();
  }

  public Point3 getTargetRequested(Point3 rv) {
    return m_function.getTargetRequested(rv);
  }

  public Point3 getTargetRequested() {
    return m_function.getTargetRequested();
  }

  private boolean m_isEnabled = true;

  public boolean isEnabled() {
    return m_isEnabled;
  }

  public void setEnabled(boolean isEnabled) {
    m_isEnabled = isEnabled;
  }

  public void update(double tDelta) {
    if (m_isEnabled) {
      RungeKuttaUtilities.rk4(m_function, 0, tDelta);
      //edu.cmu.cs.dennisc.print.PrintUtilities.printlns( m_function.getTransformation() );
      getSGCameraTransformable().setLocalTransformation(m_function.getTransformation());
    }
  }

  final int BOX_HALF_SIZE = 6;
  final int DASH_SIZE = 6;

  private void paintBox(Graphics2D g) {
    g.setColor(Color.BLUE);
    g.drawRect(m_xPixel0 - BOX_HALF_SIZE, m_yPixel0 - BOX_HALF_SIZE, BOX_HALF_SIZE + BOX_HALF_SIZE + 1, BOX_HALF_SIZE + BOX_HALF_SIZE + 1);
    g.drawLine(m_xPixel0 - BOX_HALF_SIZE, m_yPixel0, m_xPixel0 - BOX_HALF_SIZE - DASH_SIZE, m_yPixel0);
    g.drawLine(m_xPixel0 + BOX_HALF_SIZE + 1, m_yPixel0, m_xPixel0 + BOX_HALF_SIZE + DASH_SIZE + 1, m_yPixel0);
    g.drawLine(m_xPixel0, m_yPixel0 - BOX_HALF_SIZE, m_xPixel0, m_yPixel0 - BOX_HALF_SIZE - DASH_SIZE);
    g.drawLine(m_xPixel0, m_yPixel0 + BOX_HALF_SIZE + 1, m_xPixel0, m_yPixel0 + BOX_HALF_SIZE + DASH_SIZE + 1);
  }

  private void paintDash(Graphics2D g) {
    g.setColor(Color.BLUE);
    g.drawLine(m_xPixel0 - BOX_HALF_SIZE - DASH_SIZE, m_yPixel0, m_xPixel0 + BOX_HALF_SIZE + DASH_SIZE, m_yPixel0);
  }

  private void paintArrow(Graphics2D g, int xPixelDelta, int yPixelDelta) {
    int[] xPoints = {0, 8, 8, 20, 20, 8, 8};
    int[] yPoints = {0, 10, 5, 5, -5, -5, -10};

    double theta = Math.atan2(yPixelDelta, xPixelDelta);
    g.translate(m_xPixelPrev, m_yPixelPrev);
    g.rotate(theta);

    g.setColor(Color.DARK_GRAY);
    g.fillPolygon(xPoints, yPoints, xPoints.length);
    g.setColor(Color.LIGHT_GRAY);
    g.drawPolygon(xPoints, yPoints, xPoints.length);
    g.rotate(-theta);
    g.translate(-m_xPixelPrev, -m_yPixelPrev);
  }

  public void paint(Graphics2D g) {
    if (m_cameraNavigationMode == CameraNavigationMode.TRANSLATE_XZ) {
      paintBox(g);
      paintArrow(g, m_xPixel0 - m_xPixelPrev, m_yPixel0 - m_yPixelPrev);
    } else if (m_cameraNavigationMode == CameraNavigationMode.TRANSLATE_Y) {
      paintDash(g);
      paintArrow(g, 0, m_yPixel0 - m_yPixelPrev);
    } else if (m_cameraNavigationMode == CameraNavigationMode.ORBIT) {
      g.setColor(Color.BLACK);
      final int RADIUS = 10;
      g.drawOval(m_xPixel0 - RADIUS, m_yPixel0 - RADIUS, RADIUS + RADIUS + 1, RADIUS + RADIUS + 1);
    }
  }

  @Override
  public void initialized(RenderTargetInitializeEvent e) {
  }

  @Override
  public void resized(RenderTargetResizeEvent e) {
  }

  @Override
  public void displayChanged(RenderTargetDisplayChangeEvent e) {
  }

  @Override
  public void cleared(RenderTargetRenderEvent e) {
  }

  @Override
  public void rendered(RenderTargetRenderEvent e) {
    paint(e.getGraphics2D());
  }

  //todo: add get/set m_wheelFactor

  @Override
  protected void addListeners(Component component) {
    super.addListeners(component);

    //todo

    this.getOnscreenRenderTarget().getAwtComponent().addMouseWheelListener(this);
    //component.addMouseWheelListener( this );
  }

  @Override
  protected void removeListeners(Component component) {
    super.removeListeners(component);

    //todo

    this.getOnscreenRenderTarget().getAwtComponent().addMouseWheelListener(this);
    //component.removeMouseWheelListener( this );
  }

  @Override
  protected void handleMousePress(Point current, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange) {
    CameraNavigationMode cameraNavigationMode;
    if (dragStyle.isControlDown()) {
      if (dragStyle.isShiftDown()) {
        //todo?
        cameraNavigationMode = null;
      } else {
        cameraNavigationMode = CameraNavigationMode.ORBIT;
      }
    } else {
      if (dragStyle.isShiftDown()) {
        cameraNavigationMode = CameraNavigationMode.TRANSLATE_Y;
      } else {
        cameraNavigationMode = CameraNavigationMode.TRANSLATE_XZ;
      }
    }

    startCameraNavigationMode(cameraNavigationMode, current.x, current.y);
    if (isOriginalAsOpposedToStyleChange) {
      getOnscreenRenderTarget().addRenderTargetListener(this);
      m_awtCursorPrev = getAWTComponent().getCursor();
      getAWTComponent().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }
  }

  @Override
  protected void handleMouseDrag(Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, DragStyle dragStyle) {
    handleMouseDragged(current.x, current.y);
  }

  @Override
  protected Point handleMouseRelease(Point rvCurrent, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange) {
    stopCameraNavigationMode();
    if (isOriginalAsOpposedToStyleChange) {
      getAWTComponent().setCursor(m_awtCursorPrev);
      getOnscreenRenderTarget().removeRenderTargetListener(this);
    }
    return rvCurrent;
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    m_function.requestDistanceChange(e.getWheelRotation() * m_metersPerMouseWheelTick);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    super.keyPressed(e);
    m_function.setKeyPressed(e.getKeyCode(), true);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    super.keyReleased(e);
    m_function.setKeyPressed(e.getKeyCode(), false);
  }
}
