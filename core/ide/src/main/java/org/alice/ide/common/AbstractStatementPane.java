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
package org.alice.ide.common;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import org.alice.ide.x.AstI18nFactory;
import org.alice.ide.x.MutableAstI18nFactory;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.views.PaintUtilities;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;

import javax.swing.BoxLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractStatementPane extends StatementLikeSubstance {
  private static final Color PASSIVE_OUTLINE_PAINT_FOR_NON_DRAGGABLE = ColorUtilities.createGray(160);

  public AbstractStatementPane(DragModel model, AstI18nFactory factory, Statement statement, StatementListProperty owner) {
    super(model, StatementLikeSubstance.getClassFor(statement), BoxLayout.LINE_AXIS);
    this.factory = factory;
    this.statement = statement;
    this.owner = owner;
    if (this.factory instanceof MutableAstI18nFactory) {
      this.isEnabledListener = e -> AbstractStatementPane.this.repaint();
    } else {
      this.isEnabledListener = null;
    }
  }

  public AstI18nFactory getFactory() {
    return this.factory;
  }

  @Override
  protected Paint getPassiveOutlinePaint() {
    if (this.getModel() != null) {
      return super.getPassiveOutlinePaint();
    } else {
      return PASSIVE_OUTLINE_PAINT_FOR_NON_DRAGGABLE;
    }
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    if (this.isEnabledListener != null) {
      this.statement.isEnabled.addPropertyListener(this.isEnabledListener);
    }
  }

  @Override
  protected void handleUndisplayable() {
    if (this.isEnabledListener != null) {
      this.statement.isEnabled.removePropertyListener(this.isEnabledListener);
    }
    super.handleUndisplayable();
  }

  public Statement getStatement() {
    return this.statement;
  }

  public StatementListProperty getOwner() {
    return this.owner;
  }

  @Override
  protected void paintEpilogue(Graphics2D g2, int x, int y, int width, int height) {
    super.paintEpilogue(g2, x, y, width, height);
    if (this.statement.isEnabled.getValue()) {
      //pass
    } else {
      g2.setPaint(PaintUtilities.getDisabledTexturePaint());
      this.fillBounds(g2);
    }
  }

  private final AstI18nFactory factory;
  private final Statement statement;
  private final StatementListProperty owner;
  private final PropertyListener isEnabledListener;
}
