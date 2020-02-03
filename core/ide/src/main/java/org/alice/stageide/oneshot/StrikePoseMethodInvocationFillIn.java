/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.stageide.oneshot;

import javax.swing.JComponent;

import edu.cmu.cs.dennisc.map.MapToMapToMap;
import org.alice.ide.ast.SelectedInstanceFactoryExpression;
import org.alice.ide.croquet.models.cascade.ParameterBlank;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.x.PreviewAstI18nFactory;
import org.lgna.croquet.ImmutableCascadeFillIn;
import org.lgna.croquet.imp.cascade.ItemNode;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.SimpleArgument;

import java.util.List;
import java.util.UUID;

/**
 * @author Dave Culyba
 */
public class StrikePoseMethodInvocationFillIn extends ImmutableCascadeFillIn<MethodInvocationEditFactory, Expression> {
  private static MapToMapToMap<InstanceFactory, JavaMethod, List<SimpleArgument>, StrikePoseMethodInvocationFillIn> mapToMapToMap = MapToMapToMap.newInstance();

  public static StrikePoseMethodInvocationFillIn getInstance(InstanceFactory instanceFactory, JavaMethod method, List<SimpleArgument> arguments) {
    StrikePoseMethodInvocationFillIn rv = mapToMapToMap.get(instanceFactory, method, arguments);
    if (rv != null) {
      //pass
    } else {
      rv = new StrikePoseMethodInvocationFillIn(instanceFactory, method, arguments);
      mapToMapToMap.put(instanceFactory, method, arguments, rv);
    }
    return rv;
  }

  private final InstanceFactory instanceFactory;
  private final MethodInvocation transientValue;

  private StrikePoseMethodInvocationFillIn(InstanceFactory instanceFactory, JavaMethod method, List<SimpleArgument> arguments) {
    super(UUID.fromString("00505921-ef68-4c5a-bbf9-40e9a4dc56cd"), new ParameterBlank[0]);
    this.instanceFactory = instanceFactory;

    MethodInvocation mi = new MethodInvocation();
    mi.expression.setValue(new SelectedInstanceFactoryExpression(method.getDeclaringType()));
    mi.method.setValue(method);
    for (SimpleArgument argument : arguments) {
      mi.requiredArguments.add(argument);
    }
    this.transientValue = mi;
    this.transientValue.expression.setValue(instanceFactory.createExpression());
  }

  protected MethodInvocationEditFactory createMethodInvocationEditFactory(InstanceFactory instanceFactory, JavaMethod method, Expression[] argumentExpressions) {
    return new StrikePoseMethodInvocationEditFactory(instanceFactory, method, argumentExpressions);
  }

  private JavaMethod getMethod() {
    return (JavaMethod) this.transientValue.method.getValue();
  }

  @Override
  public MethodInvocationEditFactory createValue(ItemNode<? super MethodInvocationEditFactory, Expression> itemNode) {
    Expression[] argumentExpressions = new Expression[this.transientValue.requiredArguments.size()];
    for (int i = 0; i < this.transientValue.requiredArguments.size(); i++) {
      argumentExpressions[i] = this.transientValue.requiredArguments.get(i).expression.getValue();
    }
    return this.createMethodInvocationEditFactory(this.instanceFactory, this.getMethod(), argumentExpressions);
  }

  @Override
  public MethodInvocationEditFactory getTransientValue(ItemNode<? super MethodInvocationEditFactory, Expression> itemNode) {
    return null;
  }

  @Override
  protected JComponent createMenuItemIconProxy(ItemNode<? super MethodInvocationEditFactory, Expression> node) {
    return PreviewAstI18nFactory.getInstance().createStatementPane(new ExpressionStatement(this.transientValue)).getAwtComponent();
  }

}
