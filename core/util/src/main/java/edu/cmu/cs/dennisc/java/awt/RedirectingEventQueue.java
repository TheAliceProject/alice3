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

import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public class RedirectingEventQueue extends EventQueue {
  private Component src;
  private Component dst;
  private Component last;

  public RedirectingEventQueue(Component src, Component dst) {
    this.src = src;
    this.dst = dst;
    this.last = null;
  }

  private static Component getDeepestMouseListener(Component dst, Component descendant) {
    Component rv = descendant;
    while (rv != null) {
      if ((rv.getMouseListeners().length > 0) || (rv.getMouseMotionListeners().length > 0)) {
        break;
      }
      if (rv == dst) {
        rv = null;
        break;
      }
      rv = rv.getParent();
    }
    return rv;
  }

  @Override
  protected void dispatchEvent(AWTEvent e) {
    if (e instanceof MouseEvent) {
      MouseEvent me = (MouseEvent) e;
      Component curr = me.getComponent();
      int id = me.getID();
      if (curr == this.src) {
        if ((id == MouseEvent.MOUSE_ENTERED) || (id == MouseEvent.MOUSE_EXITED)) {
          e = MouseEventUtilities.convertMouseEvent(this.src, me, this.dst);
        } else if (id == MouseEvent.MOUSE_MOVED) {
          me = MouseEventUtilities.convertMouseEvent(this.src, me, dst);
          Component descendant = SwingUtilities.getDeepestComponentAt(this.dst, me.getX(), me.getY());
          descendant = RedirectingEventQueue.getDeepestMouseListener(dst, descendant);
          if (this.last != descendant) {
            Component exitComponent;
            if (this.last != null) {
              exitComponent = this.last;
            } else {
              exitComponent = this.src;
            }
            MouseEvent exitEvent = new MouseEvent(exitComponent, MouseEvent.MOUSE_EXITED, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), me.getButton());
            exitEvent = MouseEventUtilities.convertMouseEvent(this.src, exitEvent, exitComponent);
            //edu.cmu.cs.dennisc.print.PrintUtilities.println( exitEvent );
            super.dispatchEvent(exitEvent);
          }
          if (descendant != null) {
            e = MouseEventUtilities.convertMouseEvent(dst, me, descendant);
          }
          if (this.last != descendant) {
            Component enterComponent;
            if (descendant != null) {
              enterComponent = descendant;
            } else {
              enterComponent = this.src;
            }
            MouseEvent enterEvent = new MouseEvent(enterComponent, MouseEvent.MOUSE_ENTERED, me.getWhen(), me.getModifiers(), me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), me.getButton());
            enterEvent = MouseEventUtilities.convertMouseEvent(this.src, enterEvent, enterComponent);
            //edu.cmu.cs.dennisc.print.PrintUtilities.println( enterEvent );
            super.dispatchEvent(enterEvent);
            this.last = descendant;
          }
        } else if ((id == MouseEvent.MOUSE_PRESSED) || (id == MouseEvent.MOUSE_CLICKED) || (id == MouseEvent.MOUSE_RELEASED) || (id == MouseEvent.MOUSE_DRAGGED)) {
          if (this.last != null) {
            e = MouseEventUtilities.convertMouseEvent(this.src, me, this.last);
          }
        }
      }
    }
    super.dispatchEvent(e);
  }
}
