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

package org.alice.ide.x.components;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import org.alice.ide.x.AstI18nFactory;
import org.lgna.croquet.views.imp.JDragView;
import org.lgna.project.ast.JavaKeyedArgument;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.Container;

/**
 * @author Dennis Cosgrove
 */
public class KeyedArgumentView extends ArgumentView<JavaKeyedArgument> {
  public KeyedArgumentView(AstI18nFactory factory, JavaKeyedArgument keyedArgument) {
    super(factory, keyedArgument);
  }

  @Override
  protected String getName() {
    //return this.getArgument().keyMethod.getValue().getName();
    return null;
  }

  @Override
  protected void internalRefresh() {
    super.internalRefresh();
    setBorder(new FlatRoundBorder());

    // the code that sets the names of arguments is the same code that sets the names on our draggable
    // procedures and functions (their color is set to Alice.Block.foreground). This won't work for our events
    // (which afaict is the only thing that creates this particular view) so this code digs out the appropriate
    // JLabel so that we can make it a readable color.
    if (getFirstChildOf(getAwtComponent()) instanceof JPanel panel
        && getFirstChildOf(panel) instanceof JDragView dragComponent
        && getFirstChildOf(dragComponent) instanceof  JPanel panel2) {
      for (Component x : panel2.getComponents()) {
        if (x instanceof JPanel panel3 && getFirstChildOf(panel3) instanceof JLabel thatLabel) {
          thatLabel.setForeground(UIManager.getColor("Alice.differentForeground"));
        }
      }
    }
  }

  private Component getFirstChildOf(Container container) {
    if (container == null || container.getComponentCount() <= 0) {
      return null;
    }
    return container.getComponent(0);
  }
}
