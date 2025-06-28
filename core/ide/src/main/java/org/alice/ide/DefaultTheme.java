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
import org.lgna.project.ast.*;

import javax.swing.*;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;

/**
 * @author Dennis Cosgrove
 */
public class DefaultTheme implements Theme {
  // this big fancy function returns... some shade of purple or yellow. that's all.  occasionally we go wild and return an orange, but that's pretty rare.
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
        return UIManager.getColor("Alice.Comment.Color");
      } else {
        //        if( org.lgna.project.ast.ExpressionStatement.class.isAssignableFrom( cls ) ) {
        //          return new java.awt.Color( 255, 230, 180 );
        ////        } else if( org.lgna.project.ast.LocalDeclarationStatement.class.isAssignableFrom( cls ) ) {
        ////          return new java.awt.Color( 255, 230, 180 );
        //        } else {
        return UIManager.getColor("Alice.Procedure.Block.Color");
        //return new java.awt.Color( 255, 255, 210 );
        //        }
      }
    } else if (Expression.class.isAssignableFrom(cls)) {
      if (ClassUtilities.isAssignableToAtLeastOne(cls, MethodInvocation.class)) {
        return UIManager.getColor("Alice.Function.Block.Color");
      } else if (ClassUtilities.isAssignableToAtLeastOne(cls, InfixExpression.class, LogicalComplement.class, StringConcatenation.class)) {
        return UIManager.getColor("Alice.Function.Block.Color").brighter();
      } else if (ClassUtilities.isAssignableToAtLeastOne(cls, InstanceCreation.class, ArrayInstanceCreation.class)) {
        return UIManager.getColor("Alice.Constructor.Block.Color");
      } else if (ResourceExpression.class.isAssignableFrom(cls)) {
        return UIManager.getColor("Alice.Resource.Color");
      } else {
        if (NullLiteral.class.isAssignableFrom(cls)) {
          return Color.RED;
        } else {
          return UIManager.getColor("Alice.Noun.Color");
        }
      }
    } else if (AbstractField.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Field.Color");
    } else if (AbstractParameter.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Field.Color");
    } else if (AbstractType.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Type.Color");
    } else if (UserLocal.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Field.Color");
    } else {
      return Color.BLUE;
    }
  }

  @Override
  public Color getColorFor(Node node) {
    if (node != null) {
      if (node instanceof AbstractMethod method) {
        if (method.isProcedure()) {
          return UIManager.getColor("Alice.Procedure.Color");
        } else {
          return UIManager.getColor("Alice.Function.Color");
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

  // cute little bit of color on tab/the very outermost edge of the coding area that shows what we're writing

  @Override
  public Color getCodeColor(Code code) {
    if (code instanceof UserMethod userMethod) {
      if (userMethod.isProcedure()) {
        return UIManager.getColor("Alice.Procedure.Color");
      } else {
        return UIManager.getColor("Alice.Function.Color");
      }
    } else if (code instanceof NamedUserConstructor) {
      return UIManager.getColor("Alice.Constructor.Color");
    } else {
      return Color.GRAY;
    }
  }
}
