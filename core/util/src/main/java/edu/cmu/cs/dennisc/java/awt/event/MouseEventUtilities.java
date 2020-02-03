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
package edu.cmu.cs.dennisc.java.awt.event;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;

import javax.swing.SwingUtilities;
import javax.swing.event.MenuDragMouseEvent;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author Dennis Cosgrove
 */
public class MouseEventUtilities {
  public static boolean isQuoteLeftUnquoteMouseButton(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e)) {
      if (SystemUtilities.isMac()) {
        return e.isControlDown() == false;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public static boolean isQuoteRightUnquoteMouseButton(MouseEvent e) {
    if (SwingUtilities.isRightMouseButton(e)) {
      return true;
    } else {
      if (SystemUtilities.isMac()) {
        if (SwingUtilities.isLeftMouseButton(e)) {
          return e.isControlDown();
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
  }

  public static MouseEvent performPlatformFilter(MouseEvent original) {
    MouseEvent rv;
    if (SystemUtilities.isMac()) {
      int completeModifiers = InputEventUtilities.getCompleteModifiers(original);
      int filteredModifiers = InputEventUtilities.performPlatformModifiersFilter(completeModifiers);
      if (completeModifiers == filteredModifiers) {
        rv = original;
      } else {
        //todo:
        boolean isPopupTrigger = original.isPopupTrigger();

        rv = new MouseEvent(original.getComponent(), original.getID(), original.getWhen(), filteredModifiers, original.getX(), original.getY(), original.getClickCount(), isPopupTrigger);
      }
    } else {
      rv = original;
    }
    return rv;
  }

  public static MouseEvent convertMouseEvent(Component source, MouseEvent sourceEvent, Component destination) {
    int modifiers = sourceEvent.getModifiers();
    int modifiersEx = sourceEvent.getModifiersEx();
    int modifiersComplete = modifiers | modifiersEx;
    MouseEvent me = SwingUtilities.convertMouseEvent(source, sourceEvent, destination);
    if (me instanceof MouseWheelEvent) {
      MouseWheelEvent mwe = (MouseWheelEvent) me;
      return new MouseWheelEvent(me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation());
    } else if (me instanceof MenuDragMouseEvent) {
      MenuDragMouseEvent mdme = (MenuDragMouseEvent) me;
      return new MenuDragMouseEvent(me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger(), mdme.getPath(), mdme.getMenuSelectionManager());
    } else {
      return new MouseEvent(me.getComponent(), me.getID(), me.getWhen(), modifiersComplete, me.getX(), me.getY(), me.getClickCount(), me.isPopupTrigger());
    }
  }
}
