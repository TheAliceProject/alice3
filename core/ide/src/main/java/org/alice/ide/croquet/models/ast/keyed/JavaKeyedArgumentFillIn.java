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

package org.alice.ide.croquet.models.ast.keyed;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.IncompleteAstUtilities;
import org.alice.ide.croquet.models.ast.cascade.MethodUtilities;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.lgna.croquet.CascadeBlank;
import org.lgna.croquet.ImmutableCascadeFillIn;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.JavaMethod;

import javax.swing.Icon;
import javax.swing.JComponent;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class JavaKeyedArgumentFillIn extends ImmutableCascadeFillIn<JavaKeyedArgument, Expression> {
  private static Map<JavaMethod, JavaKeyedArgumentFillIn> map = Maps.newHashMap();

  public static JavaKeyedArgumentFillIn getInstance(JavaMethod value) {
    synchronized (map) {
      JavaKeyedArgumentFillIn rv = map.get(value);
      if (rv == null) {
        rv = new JavaKeyedArgumentFillIn(value, MethodUtilities.createParameterBlanks(value));
        map.put(value, rv);
      }
      return rv;
    }
  }

  private final JavaKeyedArgument transientValue;

  private JavaKeyedArgumentFillIn(JavaMethod keyMethod, CascadeBlank<Expression>[] blanks) {
    super(UUID.fromString("484ff351-b7a9-4c7a-b2de-a6479b97ade7"), blanks);
    this.transientValue = new JavaKeyedArgument();
    this.transientValue.expression.setValue(IncompleteAstUtilities.createIncompleteStaticMethodInvocation(keyMethod));
  }

  @Override
  public String getMenuItemText() {
    Formatter formatter = FormatterState.getInstance().getValue();
    JavaMethod method = this.transientValue.getKeyMethod();
    return formatter.getNameForDeclaration(method);
  }

  @Override
  public Icon getMenuItemIcon(ItemNode<? super JavaKeyedArgument, Expression> node) {
    return null;
  }

  @Override
  protected JComponent createMenuItemIconProxy(ItemNode<? super JavaKeyedArgument, Expression> node) {
    throw new AssertionError();
  }

  @Override
  public JavaKeyedArgument createValue(ItemNode<? super JavaKeyedArgument, Expression> node) {
    Expression[] argumentExpressions = this.createFromBlanks(node, Expression.class);
    JavaMethod keyMethod = this.transientValue.getKeyMethod();
    return new JavaKeyedArgument(this.transientValue.parameter.getValue(), keyMethod, argumentExpressions);
  }

  @Override
  public JavaKeyedArgument getTransientValue(ItemNode<? super JavaKeyedArgument, Expression> node) {
    return this.transientValue;
  }
}
