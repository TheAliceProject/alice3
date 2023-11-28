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
package org.alice.stageide.type.croquet.views.renderers;

import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;
import org.alice.ide.Theme;
import org.alice.ide.icons.CheckIcon;
import org.alice.ide.icons.CheckIconFactory;
import org.alice.stageide.type.croquet.TypeNode;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.icon.EmptyIconFactory;

import org.lgna.project.ast.UserField;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Paint;

/**
 * @author Dennis Cosgrove
 */
public class FieldCellRenderer extends ListCellRenderer<UserField> {
  private static final Dimension ICON_SIZE = Theme.EXTRA_SMALL_SQUARE_ICON_SIZE;
  private static final Icon EMPTY_ICON = EmptyIconFactory.getInstance().getIconToFit(ICON_SIZE);
  private static final Icon UNSELECTED_CHECK_ICON = CheckIconFactory.getInstance().getIconToFit(ICON_SIZE);
  private static final Icon SELECTED_CHECK_ICON = new CheckIcon(ICON_SIZE) {
    @Override
    protected Paint getInnerPaint(Component c) {
      return Color.WHITE;
    }

    @Override
    protected Paint getOuterPaint(Component c) {
      return Color.BLACK;
    }
  };

  private final SingleSelectTreeState<TypeNode> typeState;

  public FieldCellRenderer(SingleSelectTreeState<TypeNode> typeState) {
    this.typeState = typeState;
  }

  @Override
  protected JLabel getListCellRendererComponent(JLabel rv, JList list, UserField value, int index, boolean isSelected, boolean cellHasFocus) {
    if (value != null) {
      rv.setText(value.getName());
    }
    Icon icon = EMPTY_ICON;
    if (value != null) {
      TypeNode typeNode = this.typeState.getValue();
      if (typeNode != null) {
        if (typeNode.getType().isAssignableFrom(value.getValueType())) {
          icon = isSelected ? SELECTED_CHECK_ICON : UNSELECTED_CHECK_ICON;
        }
      }
    }
    rv.setIcon(icon);
    return rv;
  }
}
