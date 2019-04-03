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
package org.alice.ide;

import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractParameter;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArrayInstanceCreation;
import org.lgna.project.ast.Code;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.EachInArrayTogether;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InfixExpression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.LogicalComplement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserConstructor;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.ResourceExpression;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StringConcatenation;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserMethod;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

/**
 * @author Dennis Cosgrove
 */
public class DefaultTheme implements Theme {
  private static final Color DEFAULT_NOUN_COLOR = new Color(0xFDF6C0);
  public static final Color DEFAULT_TYPE_COLOR = DEFAULT_NOUN_COLOR;
  public static final Color DEFAULT_CONSTRUCTOR_COLOR = new Color(0xE6D4A3);
  public static final Color DEFAULT_FIELD_COLOR = new Color(0xD6AC8B);

  public static final Color DEFAULT_PROCEDURE_COLOR = new Color(0xB2B7D9);
  public static final Color DEFAULT_FUNCTION_COLOR = new Color(0xADCF95);
  //  private static final java.awt.Color DEFAULT_EVENT_COLOR = new Color( 100, 200, 100 );
  //  private static final java.awt.Color DEFAULT_EVENT_BODY_COLOR = DEFAULT_EVENT_COLOR.brighter().brighter(); //new Color( 150, 225, 150 );
  private static final Color DEFAULT_EVENT_COLOR = new Color(0xd3d7f0);
  private static final Color DEFAULT_EVENT_BODY_COLOR = DEFAULT_PROCEDURE_COLOR;

  private static final Color DEFAULT_SELECTED_COLOR = new Color(255, 255, 179);
  private static final Color DEFAULT_UNSELECTED_COLOR = new Color(141, 137, 166);
  private static final Color DEFAULT_PRIMARY_BACKGROUND_COLOR = new Color(173, 167, 208);
  private static final Color DEFAULT_SECONDARY_BACKGROUND_COLOR = new Color(201, 201, 218);

  @Override
  public Color getTypeColor() {
    return DEFAULT_TYPE_COLOR;
  }

  @Override
  public Color getMutedTypeColor() {
    return ColorUtilities.scaleHSB(this.getTypeColor(), 1.0, 0.9, 0.9);
  }

  @Override
  public Color getProcedureColor() {
    return DEFAULT_PROCEDURE_COLOR;
  }

  @Override
  public Color getFunctionColor() {
    return DEFAULT_FUNCTION_COLOR;
  }

  @Override
  public Color getConstructorColor() {
    return DEFAULT_CONSTRUCTOR_COLOR;
  }

  @Override
  public Color getFieldColor() {
    return DEFAULT_FIELD_COLOR;
  }

  @Override
  public Color getLocalColor() {
    return getFieldColor();
  }

  @Override
  public Color getParameterColor() {
    return getFieldColor();
  }

  @Override
  public Color getEventColor() {
    return DEFAULT_EVENT_COLOR;
  }

  @Override
  public Color getEventBodyColor() {
    return DEFAULT_EVENT_BODY_COLOR;
  }

  @Override
  public Paint getPaintFor(Class<? extends Statement> cls, int x, int y, int width, int height) {
    Color color = this.getColorFor(cls);
    if (Comment.class.isAssignableFrom(cls)) {
      return color;
    } else {
      if (ClassUtilities.isAssignableToAtLeastOne(cls, DoTogether.class, EachInArrayTogether.class)) {
        Color colorA = ColorUtilities.scaleHSB(color, 1.0, 0.9, 0.85);
        Color colorB = ColorUtilities.scaleHSB(color, 1.0, 1.0, 1.15);
        return new GradientPaint(x, y, colorA, x + 200, y, colorB);
      } else {
        return color;
        //return new java.awt.GradientPaint( x, y, colorB, x, y + 64, color );
      }
    }
  }

  @Override
  public Color getColorFor(Class<? extends Node> cls) {
    if (Statement.class.isAssignableFrom(cls)) {
      if (Comment.class.isAssignableFrom(cls)) {
        return ColorUtilities.createGray(245);
      } else {
        //        if( org.lgna.project.ast.ExpressionStatement.class.isAssignableFrom( cls ) ) {
        //          return new java.awt.Color( 255, 230, 180 );
        ////        } else if( org.lgna.project.ast.LocalDeclarationStatement.class.isAssignableFrom( cls ) ) {
        ////          return new java.awt.Color( 255, 230, 180 );
        //        } else {
        return new Color(0xd3d7f0);
        //return new java.awt.Color( 255, 255, 210 );
        //        }
      }
    } else if (Expression.class.isAssignableFrom(cls)) {
      if (ClassUtilities.isAssignableToAtLeastOne(cls, MethodInvocation.class)) {
        return new Color(0xd3e7c7);
      } else if (ClassUtilities.isAssignableToAtLeastOne(cls, InfixExpression.class, LogicalComplement.class, StringConcatenation.class)) {
        return new Color(0xDEEBD3);
      } else if (ClassUtilities.isAssignableToAtLeastOne(cls, InstanceCreation.class, ArrayInstanceCreation.class)) {
        //return new java.awt.Color( 0xbdcfb3 );
        return DEFAULT_CONSTRUCTOR_COLOR;
      } else if (ResourceExpression.class.isAssignableFrom(cls)) {
        return new Color(0xffffff);
      } else {
        if (NullLiteral.class.isAssignableFrom(cls)) {
          return Color.RED;
        } else {
          return DEFAULT_NOUN_COLOR;
        }
      }
    } else if (AbstractField.class.isAssignableFrom(cls)) {
      return this.getFieldColor();
    } else if (AbstractParameter.class.isAssignableFrom(cls)) {
      return this.getParameterColor();
    } else if (AbstractType.class.isAssignableFrom(cls)) {
      return this.getTypeColor();
    } else if (UserLocal.class.isAssignableFrom(cls)) {
      return this.getLocalColor();
    } else {
      return Color.BLUE;
    }
  }

  @Override
  public Color getColorFor(Node node) {
    if (node != null) {
      if (node instanceof AbstractMethod) {
        AbstractMethod method = (AbstractMethod) node;
        if (method.isProcedure()) {
          return this.getProcedureColor();
        } else {
          return this.getFunctionColor();
        }
      } else {
        Class<? extends Node> cls = node.getClass();
        //        if( node instanceof org.lgna.project.ast.FieldAccess ) {
        //          org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)node;
        //          if( fieldAccess.expression.getValue() instanceof org.lgna.project.ast.TypeExpression ) {
        //            //pass
        //          } else {
        //            cls = org.lgna.project.ast.MethodInvocation.class;
        //          }
        //        }
        return this.getColorFor(cls);
      }
    } else {
      return Color.RED;
    }
  }

  @Override
  public Color getCommentForegroundColor() {
    return new Color(0, 100, 0);
  }

  @Override
  public Color getCodeColor(Code code) {
    if (code instanceof UserMethod) {
      UserMethod userMethod = (UserMethod) code;
      if (userMethod.isProcedure()) {
        return getProcedureColor();
      } else {
        return getFunctionColor();
      }
    } else if (code instanceof NamedUserConstructor) {
      return getConstructorColor();
    } else {
      return Color.GRAY;
    }
  }

  @Override
  public Color getSelectedColor() {
    return DEFAULT_SELECTED_COLOR;
  }

  @Override
  public Color getUnselectedColor() {
    return DEFAULT_UNSELECTED_COLOR;
  }

  @Override
  public Color getPrimaryBackgroundColor() {
    return DEFAULT_PRIMARY_BACKGROUND_COLOR;
  }

  @Override
  public Color getSecondaryBackgroundColor() {
    return DEFAULT_SECONDARY_BACKGROUND_COLOR;
  }
}
