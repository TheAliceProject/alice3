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

package org.alice.ide.type;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.project.Project;
import org.lgna.project.ast.*;

import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class TypeCache {
  private final Project project;
  private final Map<TypeKey, NamedUserType> map = Maps.newHashMap();

  public TypeCache(Project project) {
    this.project = project;
    this.seed();
  }

  private static TypeKey createKeyForType(NamedUserType type) {
    AbstractType<?, ?, ?> superType = type.getSuperType();
    List<NamedUserConstructor> constructors = type.getDeclaredConstructors();
    final int CONSTRUCTOR_COUNT = constructors.size();
    switch (CONSTRUCTOR_COUNT) {
    case 1:
      NamedUserConstructor constructor0 = constructors.get(0);
      List<? extends AbstractParameter> requiredParameters = constructor0.getRequiredParameters();
      final int REQUIRED_PARAMETER_COUNT = requiredParameters.size();
      switch (REQUIRED_PARAMETER_COUNT) {
      case 0:
        ConstructorInvocationStatement constructorInvocationStatement = constructor0.body.getValue().constructorInvocationStatement.getValue();
        final int SUPER_CONSTRUCTOR_INVOCATION_ARGUMENT_COUNT = constructorInvocationStatement.requiredArguments.size();
        switch (SUPER_CONSTRUCTOR_INVOCATION_ARGUMENT_COUNT) {
        case 0:
          return new ExtendsTypeKey(superType);
        case 1:
          Expression expression = constructorInvocationStatement.requiredArguments.get(0).expression.getValue();
          if (expression instanceof FieldAccess) {
            //Cases like Alien() { super(AlienResource.DEFAULT); }
            FieldAccess fieldAccess = (FieldAccess) expression;
            return new ExtendsTypeWithSuperArgumentFieldKey(superType, fieldAccess.field.getValue());
          } else if (expression instanceof InstanceCreation) {
            //Cases like Alien2() { super(new DynamicBipedResource("Alien2", "Alien2")); }
            return new ExtendsTypeWithNamedType(superType, type.name.getName());
          } else {
            throw new AssertionError(type + " " + expression);
          }
        default:
          throw new AssertionError(type);
        }
      case 1:
        //Cases like Alice(AliceResource resource) { super(resource); }
        //Cases like AdultPerson(AdultPersonResource resource) { super(resource); }
        AbstractParameter parameter0 = requiredParameters.get(0);
        return new ExtendsTypeWithConstructorParameterTypeKey(superType, parameter0.getValueType());
      default:
        throw new AssertionError(type);
      }
    default:
      throw new AssertionError(type);
    }
  }

  private void seed() {
    for (NamedUserType type : this.project.getNamedUserTypes()) {
      TypeKey key = createKeyForType(type);
      if (key != null) {
        this.map.put(key, type);
      } else {
        Logger.severe(type);
      }
    }
  }

  public NamedUserType getTypeFor(TypeKey key) {
    synchronized (this.map) {
      NamedUserType rv = this.map.get(key);
      if (rv != null) {
        rv = key.createType();
        this.map.put(key, rv);
      }
      return rv;
    }
  }
}
