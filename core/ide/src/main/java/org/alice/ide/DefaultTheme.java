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

import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import org.lgna.project.ast.*;

import javax.swing.UIManager;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class DefaultTheme implements Theme {

  // Closer to zero the darker it is. This threshold is arbitrary and may need adjustment.
  private static final int DARK_THRESHOLD = 600;

  @Override
  public Color getKnurlColorFor(Color backgroundColor) {
    return isDark(backgroundColor)
        ? UIManager.getColor("Alice.Block.lightKnurlForeground")
        : UIManager.getColor("Alice.Block.darkKnurlForeground");
  }

  private static boolean isDark(Color color) {
    int brightness = color.getRed() + color.getGreen() + color.getBlue();
    return brightness < DARK_THRESHOLD;
  }

  @Override
  public Color getColorFor(Class<? extends Node> cls) {
    if (Statement.class.isAssignableFrom(cls)) {
      return getStatementColor(cls);
    } else if (Expression.class.isAssignableFrom(cls)) {
      return getExpressionColor(cls);
    } else  {
      // we don't expect anything to actually hit this
      return UIManager.getColor("Alice.differentBackground");
    }
  }

  private static Color getStatementColor(Class<? extends Node> cls) {
    if (Comment.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Comment.background");
    } else if (LocalDeclarationStatement.class.isAssignableFrom(cls)) {
      // aka variable creation
      return UIManager.getColor("Alice.Block.background");
    } else if (ClassUtilities.isAssignableToAtLeastOne(cls, AbstractStatementWithBody.class, ConditionalStatement.class)) {
      // e.g. If/Else, DoInOrder, Loops, etc
      return UIManager.getColor("Alice.Block.background");
    } else if (ExpressionStatement.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Procedure.blockColor");
    } else if (ReturnStatement.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Function.color");
    } else {
      // we don't expect anything to actually hit this
      return UIManager.getColor("Alice.differentBackground");
    }
  }

  private static Color getExpressionColor(Class<? extends Node> cls) {
    if (MethodInvocation.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Function.blockColor");
    } else if (ClassUtilities.isAssignableToAtLeastOne(cls, InfixExpression.class, LogicalComplement.class, StringConcatenation.class)) {
      return UIManager.getColor("Alice.Function.blockColor");
    } else if (ClassUtilities.isAssignableToAtLeastOne(cls, InstanceCreation.class, ArrayInstanceCreation.class)) {
      return UIManager.getColor("Alice.Constructor.blockColor");
    } else if (ResourceExpression.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Resource.color");
    } else if (TypeExpression.class.isAssignableFrom(cls)) {
      return UIManager.getColor("Alice.Type.blockColor");
    } else if (NullLiteral.class.isAssignableFrom(cls)) {
      return Color.RED;
    } else {
      return UIManager.getColor("Alice.Instance.color");
    }
  }

  @Override
  public Color getColorFor(Node node) {
    if (node == null) {
      return UIManager.getColor("Alice.Alert.color");
    } else if (node instanceof AbstractMethod m) {
      return m.isProcedure() ? UIManager.getColor("Alice.Procedure.color") : UIManager.getColor("Alice.Function.color");
    } else {
      Class<? extends Node> cls = node.getClass();
      return this.getColorFor(cls);
    }
  }

  // cute little bit of color on tab/the very outermost edge of the coding area that shows what we're writing
  @Override
  public Color getCodeColor(Code code) {
    if (code instanceof AbstractMethod m) {
      return m.isProcedure() ? UIManager.getColor("Alice.Procedure.color") : UIManager.getColor("Alice.Function.color");
    } else if (code instanceof NamedUserConstructor) {
      return UIManager.getColor("Alice.Constructor.color");
    } else {
      return UIManager.getColor("Alice.differentBackground");
    }
  }
}
