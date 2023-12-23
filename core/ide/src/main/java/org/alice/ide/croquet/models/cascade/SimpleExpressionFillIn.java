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

package org.alice.ide.croquet.models.cascade;

import org.alice.ide.Theme;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.UserField;
import org.lgna.story.SThing;

import javax.swing.Icon;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
@Deprecated public class SimpleExpressionFillIn<E extends Expression> extends ExpressionFillInWithoutBlanks<E> {
  private final E transientValue;
  private final boolean isLeadingIconDesired;

  public SimpleExpressionFillIn(E value, boolean isLeadingIconDesired) {
    super(UUID.fromString("7479f074-b5f1-4c72-96da-5ebc3c547db5"));
    this.transientValue = value;
    this.isLeadingIconDesired = true;
  }

  @Override
  public E createValue(ItemNode<? super E, Void> node) {
    return this.transientValue;
  }

  @Override
  public E getTransientValue(ItemNode<? super E, Void> node) {
    return this.transientValue;
  }

  @Override
  protected Icon getLeadingIcon(ItemNode<? super E, Void> step) {
    if (this.isLeadingIconDesired) {
      if (this.transientValue instanceof FieldAccess) {
        FieldAccess fieldAccess = (FieldAccess) this.transientValue;
        AbstractField field = fieldAccess.field.getValue();
        if (field instanceof UserField) {
          UserField userField = (UserField) field;
          AbstractType<?, ?, ?> type = userField.getValueType();
          if (type != null) {
            if (type.isAssignableTo(SThing.class)) {
              IconFactory iconFactory = IconFactoryManager.getIconFactoryForField(userField);
              if (iconFactory != null) {
                return iconFactory.getIconToFit(Theme.EXTRA_SMALL_RECT_ICON_SIZE);
              } else {
                return EmptyIconFactory.getInstance().getIconToFit(Theme.EXTRA_SMALL_RECT_ICON_SIZE);
              }
            }
          }
        }
      }
    }
    return super.getLeadingIcon(step);
  }

  @Override
  protected void appendRepr(StringBuilder sb) {
    super.appendRepr(sb);
    sb.append(this.transientValue);
  }
}
