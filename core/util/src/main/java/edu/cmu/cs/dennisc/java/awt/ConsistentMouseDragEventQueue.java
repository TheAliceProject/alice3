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
package edu.cmu.cs.dennisc.java.awt;

import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Stacks;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;

/**
 * @author Dennis Cosgrove
 */
public class ConsistentMouseDragEventQueue extends EventQueue {
  private static final boolean IS_CLICK_AND_CLACK_DESIRED_DEFAULT = false;
  private static final boolean IS_CLICK_AND_CLACK_DESIRED = IS_CLICK_AND_CLACK_DESIRED_DEFAULT || SystemUtilities.isPropertyTrue("edu.cmu.cs.dennisc.java.awt.ConsistentMouseDragEventQueue.isClickAndClackDesired");

  private MouseListener activeDrag;

  public void setActiveDrag(MouseListener dragComponent) {
    activeDrag = dragComponent;
  }

  public void endActiveDrag() {
    activeDrag = null;
  }

  public boolean isDragActive() {
    return activeDrag != null;
  }

  private static class SingletonHolder {
    private static ConsistentMouseDragEventQueue instance = new ConsistentMouseDragEventQueue();
  }

  public static ConsistentMouseDragEventQueue getInstance() {
    return SingletonHolder.instance;
  }

  public static void pushIfAppropriate() {
    if ((IS_CLICK_AND_CLACK_DESIRED == false) && SystemUtilities.isWindows()) {
      //pass
    } else {
      Toolkit.getDefaultToolkit().getSystemEventQueue().push(SingletonHolder.instance);
    }
  }

  private final DStack<Component> stack;
  private int lastPressOrDragModifiers;
  private Component componentForPotentialFollowUpClickEvent;

  private ConsistentMouseDragEventQueue() {
    if (IS_CLICK_AND_CLACK_DESIRED) {
      this.stack = Stacks.newStack();
    } else {
      this.stack = null;
    }
  }

  public boolean isClickAndClackSupported() {
    return this.stack != null;
  }

  public Component peekClickAndClackComponent() {
    if (this.stack != null) {
      if (this.stack.size() > 0) {
        return this.stack.peek();
      } else {
        return null;
      }
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public void pushClickAndClackMouseFocusComponent(Component awtComponent) {
    if (this.stack != null) {
      this.stack.push(awtComponent);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public Component popClickAndClackMouseFocusComponent() {
    if (this.stack != null) {
      return this.stack.pop();
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public Component popClickAndClackMouseFocusComponentButAllowForPotentialFollowUpClickEvent() {
    Component rv = this.popClickAndClackMouseFocusComponent();
    this.componentForPotentialFollowUpClickEvent = rv;
    return rv;
  }

  protected MouseEvent convertClickAndClackIfNecessary(MouseEvent e) {
    Component component;
    if (this.componentForPotentialFollowUpClickEvent != null) {
      if (e.getID() == MouseEvent.MOUSE_CLICKED) {
        component = this.componentForPotentialFollowUpClickEvent;
      } else {
        component = null;
      }
      this.componentForPotentialFollowUpClickEvent = null;
    } else {
      component = null;
    }
    if (component != null) {
      //pass
    } else {
      if (this.stack.size() > 0) {
        component = this.stack.peek();
      } else {
        component = null;
      }
    }
    if (component != null) {
      Component curr = e.getComponent();
      if (curr == component) {
        //pass
      } else {
        e = MouseEventUtilities.convertMouseEvent(curr, e, component);
      }
    }
    return e;
  }

  @Override
  protected void dispatchEvent(AWTEvent e) {
    if (e instanceof MouseWheelEvent) {
      MouseWheelEvent mouseWheelEvent = (MouseWheelEvent) e;

      Component source = mouseWheelEvent.getComponent();
      int id = mouseWheelEvent.getID();
      long when = mouseWheelEvent.getWhen();
      int modifiers = mouseWheelEvent.getModifiers();
      int x = mouseWheelEvent.getX();
      int y = mouseWheelEvent.getY();
      //1.7
      //int xAbs = mouseWheelEvent.getXOnScreen();
      //int yAbs = mouseWheelEvent.getYOnScreen();
      int clickCount = mouseWheelEvent.getClickCount();
      boolean popupTrigger = mouseWheelEvent.isPopupTrigger();
      int scrollType = mouseWheelEvent.getScrollType();
      int scrollAmount = mouseWheelEvent.getScrollAmount();
      int wheelRotation = mouseWheelEvent.getWheelRotation();
      //1.7
      //double preciseWheelRotation = mouseWheelEvent.getPreciseWheelRotation();

      //note:
      modifiers = this.lastPressOrDragModifiers;

      // 1.7
      //= new java.awt.event.MouseWheelEvent( source, id, when, modifiers, x, y, xAbs, yAbs, clickCount, popupTrigger, scrollType, scrollAmount, wheelRotation, preciseWheelRotation );

      // 1.5
      e = new MouseWheelEvent(source, id, when, modifiers, x, y, clickCount, popupTrigger, scrollType, scrollAmount, wheelRotation);

    } else if (e instanceof MouseEvent) {
      MouseEvent mouseEvent = (MouseEvent) e;
      int id = mouseEvent.getID();
      switch (id) {
      case MouseEvent.MOUSE_PRESSED:
      case MouseEvent.MOUSE_DRAGGED:
        this.lastPressOrDragModifiers = mouseEvent.getModifiers();
        break;
      case MouseEvent.MOUSE_RELEASED:
        if (activeDrag != null) {
          MouseListener dropped = activeDrag;
          activeDrag = null;
          dropped.mouseReleased(mouseEvent);
          return;
        }
        break;
      }

      if (this.stack != null) {
        e = this.convertClickAndClackIfNecessary(mouseEvent);
      }
    }
    super.dispatchEvent(e);
  }
}
