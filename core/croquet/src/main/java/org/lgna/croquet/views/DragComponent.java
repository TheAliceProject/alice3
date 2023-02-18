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
package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.awt.ConsistentMouseDragEventQueue;
import edu.cmu.cs.dennisc.java.awt.event.InputEventUtilities;
import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import org.lgna.croquet.Application;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.DragTrigger;
import org.lgna.croquet.views.imp.JDragProxy;
import org.lgna.croquet.views.imp.JDragView;
import org.lgna.croquet.views.imp.JDropProxy;

import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;

public abstract class DragComponent<M extends DragModel> extends ViewController<JDragView, M> {
  private static final float DEFAULT_CLICK_THRESHOLD = 5.0f;

  private final MouseListener mouseListener;
  private final MouseMotionListener mouseMotionListener;
  private final ComponentListener componentListener;

  private final JDragProxy dragProxy;
  private final JDropProxy dropProxy;

  private float clickThreshold = DEFAULT_CLICK_THRESHOLD;
  private boolean isWithinClickThreshold = false;

  private MouseEvent mousePressedEvent = null;
  private MouseEvent leftButtonPressedEvent = null;

  private DragStep dragStep;

  private boolean isDueQuoteExitedUnquote;

  public DragComponent(M model, boolean isAlphaDesiredWhenOverDropReceptor) {
    super(model);
    if (model != null) {
      this.mouseListener = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent e) {
          DragComponent.this.handleMousePressed(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
          DragComponent.this.handleMouseReleased(e);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
          DragComponent.this.handleMouseClicked(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          DragComponent.this.handleMouseEntered();
        }

        @Override
        public void mouseExited(MouseEvent e) {
          DragComponent.this.handleMouseExited();
        }
      };
      this.mouseMotionListener = new MouseMotionListener() {
        @Override
        public void mouseMoved(MouseEvent e) {
          DragComponent.this.handleMouseMoved(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
          DragComponent.this.handleMouseDragged(e);
        }
      };
      this.componentListener = new ComponentListener() {
        @Override
        public void componentHidden(ComponentEvent arg0) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
          DragComponent.this.updateProxySizes();
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }
      };
      this.dragProxy = new JDragProxy(this, isAlphaDesiredWhenOverDropReceptor);
      this.dropProxy = new JDropProxy(this);
    } else {
      this.mouseListener = null;
      this.mouseMotionListener = null;
      this.componentListener = null;
      this.dragProxy = null;
      this.dropProxy = null;
    }
  }

  protected boolean isClickAndClackAppropriate() {
    return false;
  }

  private boolean isActuallyPotentiallyDraggable() {
    return this.dragProxy != null;
  }

  protected void handleMouseQuoteEnteredUnquote() {
    this.setActive(true);
  }

  protected void handleMouseQuoteExitedUnquote() {
    this.setActive(false);
    this.isDueQuoteExitedUnquote = false;
  }

  protected void handleLeftMouseButtonQuoteClickedUnquote(MouseEvent e) {
  }

  protected void handleBackButtonClicked(MouseEvent e) {
  }

  protected void handleForwardButtonClicked(MouseEvent e) {
  }

  private void handleMouseEntered() {
    if (!ConsistentMouseDragEventQueue.getInstance().isDragActive()) {
      this.handleMouseQuoteEnteredUnquote();
    }
  }

  private void handleMouseExited() {
    this.isDueQuoteExitedUnquote = ConsistentMouseDragEventQueue.getInstance().isDragActive();
    if (!this.isDueQuoteExitedUnquote) {
      this.handleMouseQuoteExitedUnquote();
    }
  }

  private void handleLeftMouseButtonDraggedOutsideOfClickThreshold(MouseEvent e) {
    DragModel dragModel = this.getModel();
    if (dragModel != null) {
      ConsistentMouseDragEventQueue.getInstance().setActiveDrag(this.mouseListener);
      this.updateProxySizes();
      this.updateProxyPosition(e);

      JLayeredPane layeredPane = getLayeredPane();
      layeredPane.add(this.dragProxy, 1);
      layeredPane.setLayer(this.dragProxy, JLayeredPane.DRAG_LAYER);

      final UserActivity activity = Application.getActiveInstance().acquireOpenActivity().getActivityWithoutTrigger();
      dragStep = activity.addDragStep(dragModel, DragTrigger.createUserInstance(this, leftButtonPressedEvent));
      this.dragStep.setLatestMouseEvent(e);
      this.dragStep.fireDragStarted();
      dragModel.handleDragStarted(this.dragStep);
      this.showDragProxy();
    }
  }

  private void handleMouseDraggedOutsideOfClickThreshold(MouseEvent e, boolean isClickAndClack) {
    this.isWithinClickThreshold = false;
    if (isActuallyPotentiallyDraggable()) {
      if (isClickAndClack || MouseEventUtilities.isQuoteLeftUnquoteMouseButton(e)) {
        this.handleLeftMouseButtonDraggedOutsideOfClickThreshold(e);
      }
    }
  }

  private boolean isInTheMidstOfClickAndClack() {
    if (isActuallyPotentiallyDraggable()) {
      if (this.isClickAndClackAppropriate()) {
        ConsistentMouseDragEventQueue eventQueue = ConsistentMouseDragEventQueue.getInstance();
        if (eventQueue.isClickAndClackSupported()) {
          Component peekComponent = eventQueue.peekClickAndClackComponent();
          return this.getAwtComponent() == peekComponent;
        }
      }
    }
    return false;
  }

  private void handleQuoteMouseDraggedUnquote(MouseEvent e, boolean isClickAndClack) {
    if (this.isWithinClickThreshold) {
      int dx = e.getX() - this.mousePressedEvent.getX();
      int dy = e.getY() - this.mousePressedEvent.getY();
      if (((dx * dx) + (dy * dy)) > (this.clickThreshold * this.clickThreshold)) {
        this.handleMouseDraggedOutsideOfClickThreshold(e, isClickAndClack);
      }
    }
    if (isActuallyPotentiallyDraggable()
        && (isClickAndClack || MouseEventUtilities.isQuoteLeftUnquoteMouseButton(e))
        && !this.isWithinClickThreshold) {
      this.updateProxyPosition(e);
      if (this.dragStep != null) {
        this.dragStep.handleMouseDragged(e);
      }
    }
  }

  private void handleMouseDragged(MouseEvent e) {
    this.handleQuoteMouseDraggedUnquote(e, false);
  }

  private void handleMouseMoved(MouseEvent e) {
    if (this.isInTheMidstOfClickAndClack()) {
      this.handleQuoteMouseDraggedUnquote(e, true);
    }
  }

  private void handleMousePressed(MouseEvent e) {
    if (MouseEventUtilities.isQuoteRightUnquoteMouseButton(e)) {
      if (ConsistentMouseDragEventQueue.getInstance().isDragActive()) {
        this.handleCancel(e);
      }
    } else {
      if (!this.isInTheMidstOfClickAndClack()) {
        this.isWithinClickThreshold = true;
        this.mousePressedEvent = e;
        if (MouseEventUtilities.isQuoteLeftUnquoteMouseButton(e)) {
          this.leftButtonPressedEvent = e;
        } else {
          this.leftButtonPressedEvent = null;
        }
      }
    }
  }

  private void handleMouseReleased(MouseEvent e) {
    boolean isClack = this.isInTheMidstOfClickAndClack();
    if (isClack) {
      ConsistentMouseDragEventQueue eventQueue = ConsistentMouseDragEventQueue.getInstance();
      eventQueue.popClickAndClackMouseFocusComponentButAllowForPotentialFollowUpClickEvent();
    }
    if (!isActuallyPotentiallyDraggable() || (!isClack && !MouseEventUtilities.isQuoteLeftUnquoteMouseButton(e))) {
      return;
    }
    if (this.isWithinClickThreshold) {
      if (this.isClickAndClackAppropriate()) {
        ConsistentMouseDragEventQueue eventQueue = ConsistentMouseDragEventQueue.getInstance();
        if (eventQueue.isClickAndClackSupported()) {
          Component peekComponent = eventQueue.peekClickAndClackComponent();
          Component awtComponent = this.getAwtComponent();
          if (awtComponent != peekComponent) {
            eventQueue.pushClickAndClackMouseFocusComponent(awtComponent);
          }
        }
      } else {
        this.handleLeftMouseButtonQuoteClickedUnquote(e);
      }
    } else {
      if (this.isDueQuoteExitedUnquote) {
        this.handleMouseQuoteExitedUnquote();
      }
      JLayeredPane layeredPane = getLayeredPane();
      Rectangle bounds = this.dragProxy.getBounds();
      layeredPane.remove(this.dragProxy);
      layeredPane.repaint(bounds);
      if (this.dragStep != null) {
        this.dragStep.handleMouseReleased(e);
        this.dragStep = null;
      }
    }
  }

  private void handleMouseClicked(MouseEvent e) {
    int button = e.getButton();
    switch (button) {
      case 4 -> this.handleBackButtonClicked(e);
      case 5 -> this.handleForwardButtonClicked(e);
    }
  }

  public void handleCancel(EventObject e) {
    ConsistentMouseDragEventQueue.getInstance().endActiveDrag();
    if (this.isDueQuoteExitedUnquote) {
      this.handleMouseQuoteExitedUnquote();
    }
    if (this.isInTheMidstOfClickAndClack()) {
      ConsistentMouseDragEventQueue eventQueue = ConsistentMouseDragEventQueue.getInstance();
      eventQueue.popClickAndClackMouseFocusComponentButAllowForPotentialFollowUpClickEvent();
    }
    JLayeredPane layeredPane = this.getLayeredPane();
    if (layeredPane != null) {
      Rectangle bounds = this.dragProxy.getBounds();
      layeredPane.remove(this.dragProxy);
      layeredPane.repaint(bounds);
    }
    if (this.dragStep != null) {
      this.dragStep.handleCancel(e);
      this.dragStep = null;
    }
  }

  private JLayeredPane getLayeredPane() {
    AbstractWindow<?> root = this.getRoot();
    if (root != null) {
      JRootPane rootPane = root.getJRootPane();
      if (rootPane != null) {
        return rootPane.getLayeredPane();
      } else {
        //throw new RuntimeException( "cannot find rootPane: " + this );
        return null;
      }
    } else {
      return null;
    }
  }

  public SwingComponentView<?> getSubject() {
    return this;
  }

  public JDragProxy getDragProxy() {
    return this.dragProxy;
  }

  public JDropProxy getDropProxy() {
    return this.dropProxy;
  }

  public void showDragProxy() {
    this.dragProxy.setVisible(true);
  }

  public void hideDragProxy() {
    this.dragProxy.setVisible(false);
  }

  public Dimension getDropProxySize() {
    return this.dropProxy.getSize();
  }

  private void updateProxySizes() {
    if (this.isActuallyPotentiallyDraggable()) {
      this.dragProxy.setSize(this.dragProxy.getProxySize());
      this.dropProxy.setSize(this.dropProxy.getProxySize());
    }
  }

  private synchronized void updateProxyPosition(MouseEvent e) {
    if (this.isActuallyPotentiallyDraggable()) {
      if (this.leftButtonPressedEvent != null) {
        JLayeredPane layeredPane = getLayeredPane();
        Point locationOnScreenLayeredPane = layeredPane.getLocationOnScreen();
        Point locationOnScreen = this.getLocationOnScreen();
        int dx = locationOnScreen.x - locationOnScreenLayeredPane.x;
        int dy = locationOnScreen.y - locationOnScreenLayeredPane.y;

        dx -= mousePressedEvent.getX();
        dy -= mousePressedEvent.getY();

        boolean isCopyDesired = InputEventUtilities.isQuoteControlUnquoteDown(e);
        int x = e.getX() + dx;
        int y = e.getY() + dy;
        this.dragProxy.setCopyDesired(isCopyDesired);
        this.dragProxy.setLocation(x, y);
        //layeredPane.setPosition( dragProxy, dy );
        this.dropProxy.setCopyDesired(isCopyDesired);
      }
    }
  }

  public void setDropProxyLocationAndShowIfNecessary(Point p, AwtComponentView<?> asSeenBy, Integer heightToAlignLeftCenterOn, int availableHeight) {
    JLayeredPane layeredPane = getLayeredPane();
    p = SwingUtilities.convertPoint(asSeenBy.getAwtComponent(), p, layeredPane);

    this.dropProxy.setAvailableHeight(availableHeight);
    if (heightToAlignLeftCenterOn != null) {
      p.y += (heightToAlignLeftCenterOn - this.dropProxy.getAvailableHeight()) / 2;
    }

    this.dropProxy.setLocation(p);
    if (this.dropProxy.getParent() == null) {
      layeredPane.add(this.dropProxy, 1);
      layeredPane.setLayer(this.dropProxy, JLayeredPane.DEFAULT_LAYER);
    }
  }

  public void hideDropProxyIfNecessary() {
    Container parent = this.dropProxy.getParent();
    if (parent != null) {
      Rectangle bounds = this.dropProxy.getBounds();
      parent.remove(this.dropProxy);
      parent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
    }
  }

  private boolean isActive;

  public boolean isActive() {
    return this.isActive;
  }

  private void setActive(boolean isActive) {
    if (this.isActive != isActive) {
      this.isActive = isActive;
      this.repaint();
    }
  }

  protected abstract void fillBounds(Graphics2D g2, int x, int y, int width, int height);

  protected abstract void paintPrologue(Graphics2D g2, int x, int y, int width, int height);

  protected abstract void paintEpilogue(Graphics2D g2, int x, int y, int width, int height);

  public float getClickThreshold() {
    return this.clickThreshold;
  }

  public void setClickThreshold(float clickThreshold) {
    this.clickThreshold = clickThreshold;
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    if (this.mouseListener != null) {
      this.addMouseListener(this.mouseListener);
    }
    if (this.mouseMotionListener != null) {
      this.addMouseMotionListener(this.mouseMotionListener);
    }
    if (this.componentListener != null) {
      this.addComponentListener(this.componentListener);
    }
  }

  @Override
  protected void handleUndisplayable() {
    if (this.componentListener != null) {
      this.removeComponentListener(this.componentListener);
    }
    if (this.mouseMotionListener != null) {
      this.removeMouseMotionListener(this.mouseMotionListener);
    }
    if (this.mouseListener != null) {
      this.removeMouseListener(this.mouseListener);
    }
    super.handleUndisplayable();
  }
}
