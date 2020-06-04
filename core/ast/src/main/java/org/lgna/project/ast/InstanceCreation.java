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
package org.lgna.project.ast;

import org.lgna.project.code.PrecedentedOperation;
import org.lgna.project.virtualmachine.VirtualMachine;

/**
 * @author Dennis Cosgrove
 */
public final class InstanceCreation extends Expression implements ArgumentOwner, PrecedentedOperation {
  public InstanceCreation() {
  }

  public InstanceCreation(AbstractConstructor constructor, SimpleArgument... requiredArguments) {
    this(constructor, requiredArguments, null, null);
  }

  public InstanceCreation(AbstractConstructor constructor, SimpleArgument[] requiredArguments, SimpleArgument[] variableArguments, JavaKeyedArgument[] keyedArguments) {
    assert constructor != null;
    this.constructor.setValue(constructor);
    this.requiredArguments.add(requiredArguments);
    if (variableArguments != null) {
      this.variableArguments.add(variableArguments);
    }
    if (keyedArguments != null) {
      this.keyedArguments.add(keyedArguments);
    }
  }

  @Override
  public DeclarationProperty<? extends AbstractCode> getParameterOwnerProperty() {
    return this.constructor;
  }

  @Override
  public SimpleArgumentListProperty getRequiredArgumentsProperty() {
    return this.requiredArguments;
  }

  @Override
  public SimpleArgumentListProperty getVariableArgumentsProperty() {
    return this.variableArguments;
  }

  @Override
  public KeyedArgumentListProperty getKeyedArgumentsProperty() {
    return this.keyedArguments;
  }

  @Override
  public AbstractType<?, ?, ?> getType() {
    return constructor.getValue().getDeclaringType();
  }

  @Override
  public boolean isValid() {
    //todo
    return true;
  }

  @Override
  public void process(AstProcessor processor) {
    processor.processInstantiation(this);
  }

  @Override
  public int getLevelOfPrecedence() {
    return 13;
  }

  //todo: AbstractConstructor -> Expression<AbstractConstructor>
  public final DeclarationProperty<AbstractConstructor> constructor = new DeclarationProperty<AbstractConstructor>(this) {
    @Override
    public boolean isReference() {
      return (this.getValue() instanceof AnonymousUserConstructor) == false;
    }
  };
  public final SimpleArgumentListProperty requiredArguments = new SimpleArgumentListProperty(this);
  public final SimpleArgumentListProperty variableArguments = new SimpleArgumentListProperty(this);
  public final KeyedArgumentListProperty keyedArguments = new KeyedArgumentListProperty(this);

  public Object evaluate(VirtualMachine virtualMachine) {
    AbstractType<?, ?, ?> fallbackType = getParent() instanceof AbstractField ? ((AbstractField) getParent()).getValueType() : null;
    Object[] arguments = virtualMachine.evaluateArguments(constructor.getValue(), requiredArguments, variableArguments, keyedArguments);
    return constructor.getValue().evaluate(virtualMachine, fallbackType, arguments);
  }

  // DynamicResource subclasses use constructors that looks like this:
  //   public DynamicBipedResource(String modelName, String resourceName)
  // This InstanceCreation looks like this:
  //   AstUtilities.createInstanceCreation(
  //     DynamicBipedResource.class,
  //     new Class<?>[] {String.class,String.class},
  //     new StringLiteral(dynamicResource.getModelClassName()),
  //     new StringLiteral(dynamicResource.getModelVariantName()));
  public Object instantiateDynamicResource() {
    if (constructor.getValue() instanceof JavaConstructor) {
      JavaConstructor instanceConstructor = (JavaConstructor) constructor.getValue();
      Object[] constructorArguments = new Object[instanceConstructor.getRequiredParameters().size()];
      int index = 0;
      for (SimpleArgument argument : requiredArguments.getValue()) {
        if (argument.expression.getValue() instanceof StringLiteral) {
          StringLiteral literal = (StringLiteral) argument.expression.getValue();
          String argumentValue = literal.getValueProperty().getValue();
          constructorArguments[index++] = argumentValue;
        } else {
          return null;
        }
      }
      return instanceConstructor.evaluate(null, null, constructorArguments);
    }
    return null;
  }
}
