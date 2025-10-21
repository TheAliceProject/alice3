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

import edu.cmu.cs.dennisc.java.awt.BevelState;
import edu.cmu.cs.dennisc.java.awt.BeveledShape;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingTypeFeedbackForExpressionsState;
import org.lgna.croquet.DragModel;
import org.lgna.project.ast.*;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionLikeSubstance extends NodeLikeSubstance {
  public static final int DOCKING_BAY_INSET_LEFT = 7;

  private final boolean isVoid;

  public ExpressionLikeSubstance(DragModel model, boolean isVoid) {
    super(model);
    this.isVoid = isVoid;
  }

  public ExpressionLikeSubstance(DragModel model) {
    this(model, false);
  }

  @Override
  protected Color getOutlineColor() {
    return UIManager.getColor("Alice.Block.foreground");
  }

  protected boolean isExpressionTypeFeedbackDesired() {
    return IsIncludingTypeFeedbackForExpressionsState.getInstance().getValue() || isKnurlDesired();
  }

  protected static boolean isExpressionTypeFeedbackSurpressedBasedOnParentClass(Expression e) {
    if (e != null) {
      Node parent = e.getParent();
      if (parent != null) {
        return ClassUtilities.isAssignableToAtLeastOne(parent.getClass(), ArrayAccess.class, ArrayLength.class);
      }
    }
    return false;
  }

  @Override
  protected int getInsetTop() {
    return this.isVoid ? 0 : super.getInsetTop();
  }

  @Override
  protected int getDockInsetLeft() {
    return (this.isVoid || !this.isExpressionTypeFeedbackDesired()) ? 0 : DOCKING_BAY_INSET_LEFT;
  }

  @Override
  protected int getInternalInsetLeft() {
    return this.isVoid ? 0 : super.getInternalInsetLeft();
  }

  @Override
  protected int getInsetBottom() {
    return this.isVoid ? 0 : super.getInsetBottom();
  }

  @Override
  protected int getInsetRight() {
    return this.isVoid ? 0 : super.getInsetRight();
  }

  protected BeveledShape createBoundsShape(int x, int y, int width, int height) {
    AbstractType<?, ?, ?> type = this.getExpressionType();
    if (type == null) {
      type = JavaType.OBJECT_TYPE;
    }
    int left = this.getDockInsetLeft();
    int top = this.getInsetTop();
    int right = this.getInsetRight();
    int bottom = this.getInsetBottom();
    RoundRectangle2D.Float shape = new RoundRectangle2D.Float(left, top, width - left - right, height - top - bottom, 8, 8);
    return BeveledShapeForType.createBeveledShapeFor(type, shape, left, Math.min(height * 0.5f, 12.0f));
  }

  @Override
  protected Shape createShape(int x, int y, int width, int height) {
    if (this.isVoid || (this.isExpressionTypeFeedbackDesired() == false)) {
      return null;
    } else {
      return this.createBoundsShape(x, y, width, height).getBaseShape();
    }
  }

  @Override
  protected void fillBounds(Graphics2D g2, int x, int y, int width, int height) {
    if (this.isExpressionTypeFeedbackDesired()) {
      BeveledShape beveledShape = createBoundsShape(x, y, width, height);
      beveledShape.fill(g2);
    }
  }

  @Override
  protected void paintPrologue(Graphics2D g2, int x, int y, int width, int height) {
    if (!this.isVoid && this.isExpressionTypeFeedbackDesired()) {
      BevelState bevelState = BevelState.FLUSH;
      BeveledShape beveledShape = createBoundsShape(x, y, width, height);
      g2.setPaint(this.getBackgroundColor());
      beveledShape.paint(g2, bevelState, 3.0f, 1.0f, 1.0f);
    }
  }

  public abstract AbstractType<?, ?, ?> getExpressionType();
}
