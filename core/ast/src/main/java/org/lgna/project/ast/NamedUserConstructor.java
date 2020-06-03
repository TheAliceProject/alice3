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

import edu.cmu.cs.dennisc.property.BooleanProperty;
import edu.cmu.cs.dennisc.property.EnumProperty;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.project.virtualmachine.VirtualMachine;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class NamedUserConstructor extends UserConstructor implements UserCode {
  public NamedUserConstructor() {
  }

  public NamedUserConstructor(UserParameter[] parameters, ConstructorBlockStatement body) {
    this.requiredParameters.add(parameters);
    this.body.setValue(body);
  }

  @Override
  public UserInstance evaluate(VirtualMachine vm, AbstractType fallbackType, Object[] arguments) {
    return UserInstance.createInstance(vm, this, (UserType<?>) fallbackType, arguments);
  }

  @Override
  public ManagementLevel getManagementLevel() {
    return this.managementLevel.getValue();
  }

  @Override
  public NodeProperty<ConstructorBlockStatement> getBodyProperty() {
    return this.body;
  }

  @Override
  public NodeListProperty<UserParameter> getRequiredParamtersProperty() {
    return this.requiredParameters;
  }

  @Override
  public Visibility getVisibility() {
    return this.visibility;
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }

  @Override
  public AbstractCode getNextLongerInChain() {
    return null;
  }

  @Override
  public AbstractCode getNextShorterInChain() {
    return null;
  }

  @Override
  public boolean isSignatureLocked() {
    return this.isSignatureLocked.getValue();
  }

  @Override
  public List<UserParameter> getRequiredParameters() {
    return this.requiredParameters.getValue();
  }

  @Override
  public AccessLevel getAccessLevel() {
    return this.accessLevel.getValue();
  }

  @Override
  public void process(AstProcessor processor) {
    processor.processConstructor(this);
  }

  @Override
  public Object instantiateFirstArgumentPassedToSuperConstructor() {
    ConstructorInvocationStatement superConstructorInvocationStatement = body.getValue().constructorInvocationStatement.getValue();
    SimpleArgumentListProperty arguments = superConstructorInvocationStatement.requiredArguments;
    if (arguments.size() > 0) {
      Expression expression = arguments.get(0).expression.getValue();
      if (expression instanceof FieldAccess) {
        return instantiateFieldAccess((FieldAccess) expression);
      } else if (expression instanceof InstanceCreation) {
        return ((InstanceCreation) expression).instantiateDynamicResource();
      }
    }
    return null;
  }

  private Object instantiateFieldAccess(FieldAccess fieldAccess) {
    JavaField javaField = (JavaField) fieldAccess.field.getValue();
    try {
      return javaField.getFieldReflectionProxy().getReification().get(null);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public final NodeListProperty<UserParameter> requiredParameters = new NodeListProperty<UserParameter>(this);
  public final EnumProperty<AccessLevel> accessLevel = new EnumProperty<AccessLevel>(this, AccessLevel.PUBLIC);
  public final NodeProperty<ConstructorBlockStatement> body = new NodeProperty<ConstructorBlockStatement>(this);
  public final EnumProperty<ManagementLevel> managementLevel = new EnumProperty<ManagementLevel>(this, ManagementLevel.NONE);
  public final BooleanProperty isSignatureLocked = new BooleanProperty(this, Boolean.FALSE);
  public final BooleanProperty isDeletionAllowed = new BooleanProperty(this, Boolean.FALSE);

  private Visibility visibility = Visibility.PRIME_TIME;
}
