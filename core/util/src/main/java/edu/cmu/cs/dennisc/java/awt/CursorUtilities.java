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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Dennis Cosgrove
 */
public class CursorUtilities {
  private static final Map<Component, Stack<Cursor>> mapComponentToStack = new HashMap<Component, Stack<Cursor>>();
  public static final Cursor NULL_CURSOR;

  static {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    MemoryImageSource source = new MemoryImageSource(1, 1, new int[] {0}, 0, 1);
    Image nullImage = toolkit.createImage(source);
    NULL_CURSOR = toolkit.createCustomCursor(nullImage, new Point(0, 0), "NULL_CURSOR");
  }

  private static Stack<Cursor> getStack(Component component) {
    Stack<Cursor> rv = CursorUtilities.mapComponentToStack.get(component);
    if (rv != null) {
      //pass
    } else {
      rv = new Stack<Cursor>();
      CursorUtilities.mapComponentToStack.put(component, rv);
    }
    return rv;
  }

  public static void pushAndSet(Component component, Cursor nextCursor) {
    if (nextCursor != null) {
      //pass
    } else {
      nextCursor = NULL_CURSOR;
    }
    Stack<Cursor> stack = CursorUtilities.getStack(component);
    Cursor prevCursor = component.getCursor();
    stack.push(prevCursor);
    component.setCursor(nextCursor);
  }

  public static void pushAndSetPredefinedCursor(Component component, int nextCursorType) {
    pushAndSet(component, Cursor.getPredefinedCursor(nextCursorType));

  }

  public static Cursor popAndSet(Component component) {
    Stack<Cursor> stack = CursorUtilities.getStack(component);
    Cursor prevCursor = stack.pop();
    component.setCursor(prevCursor);
    return prevCursor;
  }
}
