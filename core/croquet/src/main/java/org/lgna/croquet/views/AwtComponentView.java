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

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.awt.FontUtilities;
import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import edu.cmu.cs.dennisc.java.awt.font.TextAttribute;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.print.PrintUtilities;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.Locale;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class AwtComponentView<J extends Component> extends ScreenElement {
  private static final Map<Component, AwtComponentView<?>> map = Maps.newWeakHashMap();

  private static class InternalAwtContainerAdapter extends AwtContainerView<Container> {
    private final Container awtContainer;

    public InternalAwtContainerAdapter(Container awtContainer) {
      this.awtContainer = awtContainer;
    }

    @Override
    protected Container createAwtComponent() {
      return this.awtContainer;
    }
  }

  private static class InternalAwtComponentAdapter extends AwtComponentView<Component> {
    private final Component awtComponent;

    public InternalAwtComponentAdapter(Component awtComponent) {
      this.awtComponent = awtComponent;
    }

    @Override
    protected Component createAwtComponent() {
      return this.awtComponent;
    }
  }

  //todo reduce visibility to /* package-private */
  public static AwtComponentView<?> lookup(Component awtComponent) {
    if (awtComponent != null) {
      AwtComponentView<?> rv = AwtComponentView.map.get(awtComponent);
      if (rv == null) {
        if (awtComponent instanceof Container) {
          Container awtContainer = (Container) awtComponent;
          rv = new InternalAwtContainerAdapter(awtContainer);
        } else {
          rv = new InternalAwtComponentAdapter(awtComponent);
        }

        //note: trigger desired side effect of updating map
        rv.getAwtComponent();
        //

      }
      return rv;
    } else {
      return null;
    }
  }

  private final HierarchyListener hierarchyListener = AwtComponentView.this::handleHierarchyChanged;

  public final Object getTreeLock() {
    return this.getAwtComponent().getTreeLock();
  }

  protected void handleDisplayable() {
  }

  protected void handleUndisplayable() {
  }

  private boolean isDisplayableState = false;

  private void trackDisplayability() {
    if (!isDisplayableState && awtComponent.isDisplayable()) {
      this.handleDisplayable();
      this.isDisplayableState = true;
    }
    if (isDisplayableState && !awtComponent.isDisplayable()) {
      this.handleUndisplayable();
      this.isDisplayableState = false;
    }
  }

  protected void handleAddedTo(AwtComponentView<?> parent) {
  }

  protected void handleRemovedFrom(AwtComponentView<?> parent) {
  }

  private Container awtParent;

  private void handleParentChange(Container newParent) {
    if (this.awtParent != null) {
      this.handleRemovedFrom(AwtComponentView.lookup(this.awtParent));
    }
    this.awtParent = newParent;
    if (this.awtParent != null) {
      this.handleAddedTo(AwtComponentView.lookup(this.awtParent));
    }
  }

  private static boolean isWarningAlreadyPrinted = false;

  protected void handleHierarchyChanged(HierarchyEvent e) {
    long flags = e.getChangeFlags();
    if ((flags & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0) {
      if (e.getComponent() == this.awtComponent) {
        this.trackDisplayability();
      } else {
        PrintUtilities.println("handleDisplayabilityChanged:", this.awtComponent.hashCode(), this.awtComponent.isDisplayable());
      }
    }
    if ((flags & HierarchyEvent.PARENT_CHANGED) != 0 && e.getComponent() == e.getChanged()) {
      Container eventAwtParent = e.getChangedParent();
      if (eventAwtParent != AwtComponentView.this.awtParent) {
        handleParentChange(eventAwtParent);
      } else {
        if (!isWarningAlreadyPrinted) {
          //Thread.dumpStack();
          PrintUtilities.println("investigate: hierarchyChanged seems to not be actually changing the parent");
          //            edu.cmu.cs.dennisc.print.PrintUtilities.println( "    flags:", flags );
          //            edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
          //            edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtChanged:", awtChanged.getClass().getName(), awtChanged );
          //            edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.hashCode(), awtParent.getClass().getName(), awtParent.getLayout() );
          isWarningAlreadyPrinted = true;
        }
      }
    }

  }

  private J awtComponent;

  protected abstract J createAwtComponent();

  // todo: reduce visibility to /*package-private*/
  @Override
  public final J getAwtComponent() {
    if (this.awtComponent == null) {
      this.checkEventDispatchThread();
      this.awtComponent = this.createAwtComponent();
      this.trackDisplayability();
      this.awtComponent.addHierarchyListener(this.hierarchyListener);
      this.awtComponent.setName(this.getClass().getName());
      ComponentOrientation componentOrientation = ComponentOrientation.getOrientation(JComponent.getDefaultLocale());
      if (!componentOrientation.isLeftToRight()) {
        awtComponent.setComponentOrientation(componentOrientation);
      }
      AwtComponentView.map.put(this.awtComponent, this);
    }
    return this.awtComponent;
  }

  protected void release() {
    if (this.awtComponent != null) {
      //System.err.println( "release: " + this.hashCode() );
      this.awtComponent.removeHierarchyListener(this.hierarchyListener);
      this.trackDisplayability();
      AwtComponentView.map.remove(this.awtComponent);
      this.awtComponent = null;
    }
  }

  private boolean isTreeLockRequired() {
    //todo
    return this.getAwtComponent().isDisplayable();
  }

  protected void checkEventDispatchThread() {
    if (!SwingUtilities.isEventDispatchThread()) {
      Logger.severe(Thread.currentThread(), this);
    }
  }

  protected void checkTreeLock() {
    if (this.isTreeLockRequired() && !Thread.holdsLock(this.getTreeLock())) {
      Logger.severe("tree lock required", this);
    }
  }

  public Locale getLocale() {
    return this.getAwtComponent().getLocale();
  }

  public Font getFont() {
    return this.getAwtComponent().getFont();
  }

  public void setFont(Font font) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setFont(font);
  }

  public final void scaleFont(float scaleFactor) {
    if (scaleFactor != 1.0f) {
      this.setFont(FontUtilities.scaleFont(this.getAwtComponent(), scaleFactor));
    }
  }

  public final void setFontSize(float fontSize) {
    this.setFont(this.getFont().deriveFont(fontSize));
  }

  public final void changeFont(TextAttribute<?>... textAttributes) {
    if (textAttributes.length > 0) {
      this.setFont(FontUtilities.deriveFont(this.getAwtComponent(), textAttributes));
    }
  }

  protected Dimension constrainPreferredSizeIfNecessary(Dimension rv) {
    if (minimumPreferredWidth != null) {
      rv.width = Math.max(rv.width, minimumPreferredWidth);
    }
    if (maximumPreferredWidth != null) {
      rv.width = Math.min(rv.width, maximumPreferredWidth);
    }
    if (minimumPreferredHeight != null) {
      rv.height = Math.max(rv.height, minimumPreferredHeight);
    }
    if (maximumPreferredHeight != null) {
      rv.height = Math.min(rv.height, maximumPreferredHeight);
    }
    return rv;
  }

  private Integer minimumPreferredWidth = null;
  private Integer maximumPreferredWidth = null;
  private Integer minimumPreferredHeight = null;
  private Integer maximumPreferredHeight = null;

  public final Integer getMaximumPreferredWidth() {
    return this.maximumPreferredWidth;
  }

  public final void setMaximumPreferredWidth(Integer maximumPreferredWidth) {
    this.maximumPreferredWidth = maximumPreferredWidth;
  }

  public final Integer getMinimumPreferredWidth() {
    return this.minimumPreferredWidth;
  }

  public final void setMinimumPreferredWidth(Integer minimumPreferredWidth) {
    this.minimumPreferredWidth = minimumPreferredWidth;
  }

  public final Integer getMaximumPreferredHeight() {
    return this.maximumPreferredHeight;
  }

  public final void setMaximumPreferredHeight(Integer maximumPreferredHeight) {
    this.maximumPreferredHeight = maximumPreferredHeight;
  }

  public final Integer getMinimumPreferredHeight() {
    return this.minimumPreferredHeight;
  }

  public final void setMinimumPreferredHeight(Integer minimumPreferredHeight) {
    this.minimumPreferredHeight = minimumPreferredHeight;
  }

  private boolean isMaximumSizeClampedToPreferredSize = false;

  public boolean isMaximumSizeClampedToPreferredSize() {
    return this.isMaximumSizeClampedToPreferredSize;
  }

  public void setMaximumSizeClampedToPreferredSize(boolean isMaximumSizeClampedToPreferredSize) {
    this.isMaximumSizeClampedToPreferredSize = isMaximumSizeClampedToPreferredSize;
  }

  public ComponentOrientation getComponentOrientation() {
    return this.getAwtComponent().getComponentOrientation();
  }

  public void setComponentOrientation(ComponentOrientation componentOrientation) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setComponentOrientation(componentOrientation);
  }

  public Color getForegroundColor() {
    return this.getAwtComponent().getForeground();
  }

  public void setForegroundColor(Color color) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setForeground(color);
  }

  public Color getBackgroundColor() {
    return this.getAwtComponent().getBackground();
  }

  public void setBackgroundColor(Color color) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setBackground(color);
  }

  public boolean isShowing() {
    return this.getAwtComponent().isShowing();
  }

  public boolean isVisible() {
    return this.getAwtComponent().isVisible();
  }

  public void setVisible(boolean isVisible) {
    this.checkEventDispatchThread();
    this.checkTreeLock();
    this.getAwtComponent().setVisible(isVisible);
  }

  public boolean isOpaque() {
    return this.getAwtComponent().isOpaque();
  }

  public int getX() {
    return this.getAwtComponent().getX();
  }

  public int getY() {
    return this.getAwtComponent().getY();
  }

  public void setLocation(int x, int y) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setLocation(x, y);
  }

  public final void setLocation(Point pt) {
    this.setLocation(pt.x, pt.y);
  }

  public void setLocation(Point pt, ScreenElement asSeenBy) {
    this.checkEventDispatchThread();
    this.getAwtComponent().setLocation(asSeenBy.convertPoint(pt, this.getParent()));
  }

  public final void setLocation(int x, int y, ScreenElement asSeenBy) {
    this.setLocation(new Point(x, y), asSeenBy);
  }

  public Point getLocationOnScreen() {
    return this.getAwtComponent().getLocationOnScreen();
  }

  public Rectangle getVisibleRectangle() {
    return this.getBounds();
  }

  public final Rectangle getVisibleRectangle(ScreenElement asSeenBy) {
    return this.convertRectangle(this.getVisibleRectangle(), asSeenBy);
  }

  public Rectangle getBounds(ScreenElement asSeenBy) {
    AwtContainerView<?> parent = this.getParent();
    if (parent != null) {
      return parent.convertRectangle(this.getBounds(), asSeenBy);
    } else {
      return null;
    }
  }

  @Override
  public Shape getShape(ScreenElement asSeenBy, Insets insets) {
    Rectangle rv = this.getBounds(asSeenBy);
    return RectangleUtilities.inset(rv, insets);
  }

  @Override
  public Shape getVisibleShape(ScreenElement asSeenBy, Insets insets) {
    Rectangle rv = this.getVisibleRectangle(asSeenBy);
    return RectangleUtilities.inset(rv, insets);
  }

  @Override
  public boolean isInView() {
    if (this.isVisible()) { //&& this.getAwtComponent().isShowing() && this.getAwtComponent().isDisplayable() && this.getAwtComponent().isValid() ) {
      Rectangle visibleRect = this.getVisibleRectangle();
      Dimension size = this.getAwtComponent().getSize();
      return (visibleRect.width == size.width) || (visibleRect.height == size.height);
    } else {
      return false;
    }
  }

  public <E extends AwtContainerView<?>> E getFirstAncestorAssignableTo(Class<E> cls) {
    AwtContainerView<?> parent = this.getParent();
    if (parent != null) {
      if (cls.isAssignableFrom(parent.getClass())) {
        return cls.cast(parent);
      } else {
        return parent.getFirstAncestorAssignableTo(cls);
      }
    } else {
      return null;
    }
  }

  @Override
  public ScrollPane getScrollPaneAncestor() {
    return this.getFirstAncestorAssignableTo(ScrollPane.class);
  }

  public Rectangle convertRectangle(Rectangle rectangle, ScreenElement destination) {
    return SwingUtilities.convertRectangle(this.getAwtComponent(), rectangle, destination.getAwtComponent());
  }

  public MouseEvent convertMouseEvent(MouseEvent e, ScreenElement destination) {
    return MouseEventUtilities.convertMouseEvent(this.getAwtComponent(), e, destination.getAwtComponent());
  }

  @Deprecated
  public AwtContainerView<?> getParent() {
    return (AwtContainerView<?>) AwtComponentView.lookup(this.getAwtComponent().getParent());
  }

  @Deprecated
  @Override
  public AbstractWindow<?> getRoot() {
    return AbstractWindow.lookup(SwingUtilities.getRoot(this.getAwtComponent()));
  }

  public int getWidth() {
    return this.getAwtComponent().getWidth();
  }

  public int getHeight() {
    return this.getAwtComponent().getHeight();
  }

  public void repaint() {
    this.getAwtComponent().repaint();
  }

  public void requestFocus() {
    this.getAwtComponent().requestFocus();
  }

  public void requestFocusLater() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        requestFocus();
      }
    });
  }

  @Deprecated
  public void addHierarchyListener(HierarchyListener listener) {
    this.getAwtComponent().addHierarchyListener(listener);
  }

  @Deprecated
  public void removeHierarchyListener(HierarchyListener listener) {
    this.getAwtComponent().removeHierarchyListener(listener);
  }

  @Deprecated
  public void addKeyListener(KeyListener listener) {
    this.getAwtComponent().addKeyListener(listener);
  }

  @Deprecated
  public void removeKeyListener(KeyListener listener) {
    this.getAwtComponent().removeKeyListener(listener);
  }

  @Deprecated
  public void addMouseListener(MouseListener listener) {
    this.getAwtComponent().addMouseListener(listener);
  }

  @Deprecated
  public void removeMouseListener(MouseListener listener) {
    this.getAwtComponent().removeMouseListener(listener);
  }

  @Deprecated
  public void addMouseMotionListener(MouseMotionListener listener) {
    this.getAwtComponent().addMouseMotionListener(listener);
  }

  @Deprecated
  public void removeMouseMotionListener(MouseMotionListener listener) {
    this.getAwtComponent().removeMouseMotionListener(listener);
  }

  @Deprecated
  public void addMouseWheelListener(MouseWheelListener listener) {
    this.getAwtComponent().addMouseWheelListener(listener);
  }

  @Deprecated
  public void removeMouseWheelListener(MouseWheelListener listener) {
    this.getAwtComponent().removeMouseWheelListener(listener);
  }

  @Deprecated
  public void setPreferredSize(Dimension preferredSize) {
    this.getAwtComponent().setPreferredSize(preferredSize);
  }

  @Deprecated
  public void makeStandOut() {
    ComponentUtilities.makeStandOut(this.getAwtComponent());
  }

  protected StringBuilder appendRepr(StringBuilder rv) {
    return rv;
  }

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getName());
    sb.append("[");
    this.appendRepr(sb);
    sb.append("]");
    return sb.toString();
  }
}
