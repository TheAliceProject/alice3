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
package org.alice.ide.testing.framesize.croquet;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.UserActivity;

import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class CycleFrameSizeOperation extends ActionOperation {
  private static Dimension[] sizes = new Dimension[] {new Dimension(1024, 768), new Dimension(1366, 768), new Dimension(1280, 720)};

  private int index;

  public CycleFrameSizeOperation() {
    super(Application.APPLICATION_UI_GROUP, UUID.fromString("fce834d4-4665-405e-9fae-2461613e3412"));
    this.getImp().setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
  }

  @Override
  protected void localize() {
    super.localize();
    this.setName("Cycle Frame Size");
  }

  @Override
  protected void perform(UserActivity activity) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension maximumWindowSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
    int heightDelta = screenSize.height - maximumWindowSize.height;

    int i = index % sizes.length;
    int w = sizes[i].width;
    int h = sizes[i].height - heightDelta;

    Application.getActiveInstance().getDocumentFrame().getFrame().setSize(w, h);
    index++;

    Logger.outln(w + "x" + h, "(original:", sizes[i].width + "x" + sizes[i].height, "-" + heightDelta, "accounting for height of taskbar)");
    activity.finish();
  }
}
