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

import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.Stacks;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.Composite;
import org.lgna.croquet.MenuBarComposite;
import org.lgna.croquet.ToolBarComposite;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
//todo: better name
public abstract class AbstractWindow<W extends java.awt.Window> extends ScreenElement {
  private static Map<Component, AbstractWindow<?>> map = Maps.newWeakHashMap();

  /*package-private*/
  static AbstractWindow<?> lookup(Component component) {
    if (component != null) {
      return AbstractWindow.map.get(component);
    } else {
      return null;
    }
  }

  private final W window;

  private final ContentPane contentPane;
  private final RootPane rootPane;

  public AbstractWindow(W window) {
    this.window = window;
    AbstractWindow.map.put(window, this);
    this.contentPane = new ContentPane(this);
    this.rootPane = new RootPane(this);
  }

  @Override
  public final W getAwtComponent() {
    return this.window;
  }

  public void release() {
    window.dispose();
    AbstractWindow.map.remove(window);
  }

  @Override
  public AbstractWindow<?> getRoot() {
    return this;
  }

  /* package-private */
  abstract Container getAwtContentPane();

  /* package-private */
  abstract JRootPane getJRootPane();

  public ContentPane getContentPane() {
    return this.contentPane;
  }

  public RootPane getRootPane() {
    return this.rootPane;
  }

  public void addWindowListener(WindowListener listener) {
    this.window.addWindowListener(listener);
  }

  public void removeWindowListener(WindowListener listener) {
    this.window.removeWindowListener(listener);
  }

  public void addWindowStateListener(WindowStateListener listener) {
    this.window.addWindowStateListener(listener);
  }

  public void removeWindowStateListener(WindowStateListener listener) {
    this.window.removeWindowStateListener(listener);
  }

  public boolean isVisible() {
    return this.window.isVisible();
  }

  public void setVisible(boolean isVisible) {
    this.window.setVisible(isVisible);
  }

  public int getX() {
    return this.window.getX();
  }

  public int getY() {
    return this.window.getY();
  }

  public Point getLocation(ScreenElement asSeenBy) {
    if (asSeenBy.getAwtComponent().isVisible() && this.isVisible()) {
      Point ptAsSeenBy = asSeenBy.getAwtComponent().getLocationOnScreen();
      Point ptThis = this.getAwtComponent().getLocationOnScreen();
      return new Point(ptThis.x - ptAsSeenBy.x, ptThis.y - ptAsSeenBy.y);
    } else {
      return null;
    }
  }

  public void setLocation(Point location) {
    this.window.setLocation(location);
  }

  public void setLocation(int x, int y) {
    this.window.setLocation(x, y);
  }

  public boolean isLocationByPlatform() {
    return this.window.isLocationByPlatform();
  }

  public void setLocationByPlatform(boolean isLocationByPlatform) {
    this.window.setLocationByPlatform(isLocationByPlatform);
  }

  public void setLocationRelativeTo(ScreenElement component) {
    this.window.setLocationRelativeTo(component.getAwtComponent());
  }

  public int getWidth() {
    return this.window.getWidth();
  }

  public int getHeight() {
    return this.window.getHeight();
  }

  public void setSize(Dimension size) {
    this.window.setSize(size);
  }

  public void setSize(int width, int height) {
    this.window.setSize(width, height);
  }

  //  public java.awt.Rectangle getBounds() {
  //  return this.window.getBounds();
  //  }
  //  public java.awt.Rectangle getLocalBounds() {
  //  return new java.awt.Rectangle( 0, 0, this.getWidth(), this.getHeight() );
  //  }
  public Rectangle getBounds(ScreenElement asSeenBy) {
    Point pt = this.getLocation(asSeenBy);
    if (pt != null) {
      return new Rectangle(pt.x, pt.y, this.getWidth(), this.getHeight());
    } else {
      return null;
    }
  }

  @Override
  public Shape getShape(ScreenElement asSeenBy, Insets insets) {
    return RectangleUtilities.inset(this.getBounds(asSeenBy), insets);
  }

  @Override
  public Shape getVisibleShape(ScreenElement asSeenBy, Insets insets) {
    return this.getShape(asSeenBy, insets);
  }

  @Override
  public ScrollPane getScrollPaneAncestor() {
    return null;
  }

  @Override
  public boolean isInView() {
    return this.isVisible();
  }

  public TrackableShape getCloseButtonTrackableShape() {
    return new TrackableShape() {
      @Override
      public Shape getShape(ScreenElement asSeenBy, Insets insets) {
        Rectangle bounds = AbstractWindow.this.getBounds(asSeenBy);
        if (bounds != null) {
          bounds.height = bounds.height - AbstractWindow.this.getRootPane().getHeight();
          bounds.height -= 8;
          return RectangleUtilities.inset(bounds, insets);
        } else {
          return null;
        }
      }

      @Override
      public Shape getVisibleShape(ScreenElement asSeenBy, Insets insets) {
        return this.getShape(asSeenBy, insets);
      }

      @Override
      public ScrollPane getScrollPaneAncestor() {
        return null;
      }

      @Override
      public boolean isInView() {
        return AbstractWindow.this.isInView();
      }

      @Override
      public void addComponentListener(ComponentListener listener) {
        AbstractWindow.this.addComponentListener(listener);
      }

      @Override
      public void removeComponentListener(ComponentListener listener) {
        AbstractWindow.this.removeComponentListener(listener);
      }

      @Override
      public void addHierarchyBoundsListener(HierarchyBoundsListener listener) {
        AbstractWindow.this.addHierarchyBoundsListener(listener);
      }

      @Override
      public void removeHierarchyBoundsListener(HierarchyBoundsListener listener) {
        AbstractWindow.this.removeHierarchyBoundsListener(listener);
      }
    };
  }

  public void pack() {
    this.getAwtComponent().pack();
  }

  private static Button lookupButton(JButton jButton) {
    AwtComponentView<?> component = AwtComponentView.lookup(jButton);
    if (component instanceof Button) {
      Button button = (Button) component;
      return button;
    } else {
      return null;
    }
  }

  public Button getDefaultButton() {
    return lookupButton(this.getJRootPane().getDefaultButton());
  }

  public void setDefaultButton(Button button) {
    this.getJRootPane().setDefaultButton(button.getAwtComponent());
  }

  private DStack<JButton> defaultJButtonStack;

  public void pushDefaultButton(Button button) {
    if (this.defaultJButtonStack == null) {
      this.defaultJButtonStack = Stacks.newStack();
    }
    this.defaultJButtonStack.push(this.getJRootPane().getDefaultButton());
    this.setDefaultButton(button);
  }

  public Button popDefaultButton() {
    Button rv;
    if (this.defaultJButtonStack != null) {
      if (this.defaultJButtonStack.isEmpty()) {
        Logger.warning(this.defaultJButtonStack);
        rv = null;
      } else {
        JButton jButton = this.defaultJButtonStack.pop();
        this.getJRootPane().setDefaultButton(jButton);
        rv = lookupButton(jButton);
      }
    } else {
      Logger.warning(this.defaultJButtonStack);
      rv = null;
    }
    return rv;
  }

  private MenuBarComposite menuBarComposite;
  private ToolBarComposite toolBarComposite;
  private Composite<?> mainComposite;

  public Composite<?> getMainComposite() {
    return this.mainComposite;
  }

  public void setToolBarComposite(ToolBarComposite toolBarComposite) {
    if (this.toolBarComposite != toolBarComposite) {
      synchronized (this.getAwtComponent().getTreeLock()) {
        if (this.toolBarComposite != null) {
          this.getContentPane().removeComponent(this.toolBarComposite.getView());
          this.toolBarComposite.handlePostDeactivation();
        }
        this.toolBarComposite = toolBarComposite;
        if (this.toolBarComposite != null) {
          this.toolBarComposite.handlePreActivation();
          this.getContentPane().addPageStartComponent(this.toolBarComposite.getView());
        }
      }
      this.getContentPane().revalidateAndRepaint();
    }
  }

  public void setMainComposite(Composite<?> mainComposite) {
    if (this.mainComposite != mainComposite) {
      synchronized (this.getAwtComponent().getTreeLock()) {
        if (this.mainComposite != null) {
          this.getContentPane().removeComponent(this.mainComposite.getView());
          this.mainComposite.handlePostDeactivation();
        }
        this.mainComposite = mainComposite;
        if (this.mainComposite != null) {
          this.mainComposite.handlePreActivation();
          this.getContentPane().addCenterComponent(this.mainComposite.getView());
        }
      }
      this.getContentPane().revalidateAndRepaint();
    }
  }

  public MenuBarComposite getMenuBarComposite() {
    return this.menuBarComposite;
  }

  protected abstract void setJMenuBar(JMenuBar jMenuBar);

  public void setMenuBarComposite(MenuBarComposite menuBarComposite) {
    if (this.menuBarComposite != menuBarComposite) {
      synchronized (this.getAwtComponent().getTreeLock()) {
        if (this.menuBarComposite != null) {
          this.menuBarComposite.handlePostDeactivation();
        }
        this.menuBarComposite = menuBarComposite;
        JMenuBar jMenuBar;
        if (this.menuBarComposite != null) {
          this.menuBarComposite.handlePreActivation();
          jMenuBar = menuBarComposite.getView().getAwtComponent();
        } else {
          jMenuBar = null;
        }
        this.setJMenuBar(jMenuBar);
      }

    }
  }
}
