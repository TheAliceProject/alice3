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

package edu.cmu.cs.dennisc.javax.swing;

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.util.DStack;
import edu.cmu.cs.dennisc.java.util.Stacks;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Rectangle;

/**
 * @author Dennis Cosgrove
 */
public class RepaintManagerUtilities {
  private static RepaintManager originalRepaintManager;
  private static DStack<JComponent> stencils = Stacks.newStack();

  private static class StencilRepaintManager extends RepaintManager {
    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
      super.addDirtyRegion(c, x, y, w, h);
      final JComponent jStencil = stencils.peek();
      if ((jStencil == c) || jStencil.isAncestorOf(c)) {
        //pass
      } else {
        Component srcRoot = SwingUtilities.getRoot(c);
        Component dstRoot = SwingUtilities.getRoot(jStencil);

        if ((srcRoot != null) && (srcRoot == dstRoot)) {
          Rectangle rect = new Rectangle(x, y, w, h);
          Rectangle visibleRect = rect.intersection(c.getVisibleRect());
          if ((visibleRect.width != 0) && (visibleRect.height != 0)) {
            final Rectangle rectAsSeenByStencil = ComponentUtilities.convertRectangle(c, visibleRect, jStencil);
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                StencilRepaintManager.super.addDirtyRegion(jStencil, rectAsSeenByStencil.x, rectAsSeenByStencil.y, rectAsSeenByStencil.width, rectAsSeenByStencil.height);
                //jStencil.repaint( rectAsSeenByStencil.x, rectAsSeenByStencil.y, rectAsSeenByStencil.width, rectAsSeenByStencil.height );
              }
            });
          }
        }
      }
    }
  }

  private RepaintManagerUtilities() {
    throw new AssertionError();
  }

  public static void pushStencil(JComponent jStencil) {
    if (stencils.size() > 0) {
      //pass
    } else {
      originalRepaintManager = RepaintManager.currentManager(jStencil);
      RepaintManager.setCurrentManager(new StencilRepaintManager());
    }
    stencils.push(jStencil);
  }

  public static JComponent popStencil() {
    JComponent rv = stencils.pop();
    if (stencils.size() > 0) {
      //pass
    } else {
      RepaintManager.setCurrentManager(originalRepaintManager);
    }
    return rv;
  }
}
