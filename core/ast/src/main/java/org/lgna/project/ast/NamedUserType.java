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
import edu.cmu.cs.dennisc.property.StringProperty;
import org.lgna.project.code.CodeGenerator;
import org.lgna.project.code.CodeOrganizer;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class NamedUserType extends UserType<NamedUserConstructor> implements CodeGenerator {
  public NamedUserType() {
  }

  public NamedUserType(String name, UserPackage _package, AbstractType<?, ?, ?> superType, NamedUserConstructor[] constructors, UserMethod[] methods, UserField[] fields) {
    super(superType, methods, fields);
    this.name.setValue(name);
    this._package.setValue(_package);
    this.constructors.add(constructors);
  }

  public NamedUserType(String name, UserPackage _package, Class<?> superCls, NamedUserConstructor[] constructors, UserMethod[] methods, UserField[] fields) {
    this(name, _package, JavaType.getInstance(superCls), constructors, methods, fields);
  }

  @Override
  public String getName() {
    return name.getValue();
  }

  @Override
  public StringProperty getNamePropertyIfItExists() {
    return this.name;
  }

  @Override
  public AbstractPackage getPackage() {
    return _package.getValue();
  }

  @Override
  public List<NamedUserConstructor> getDeclaredConstructors() {
    return constructors.getValue();
  }

  @Override
  public AccessLevel getAccessLevel() {
    return this.accessLevel.getValue();
  }

  @Override
  public boolean isStatic() {
    return false;
    //return this.isStatic.getValue();
  }

  @Override
  public boolean isAbstract() {
    return this.finalAbstractOrNeither.getValue() == TypeModifierFinalAbstractOrNeither.ABSTRACT;
  }

  @Override
  public boolean isFinal() {
    return this.finalAbstractOrNeither.getValue() == TypeModifierFinalAbstractOrNeither.FINAL;
  }

  @Override
  public boolean isStrictFloatingPoint() {
    return this.isStrictFloatingPoint.getValue();
  }

  @Override
  public void appendCode(SourceCodeGenerator generator) {
    generator.appendClass(getCodeOrganizer(generator), this);
  }

  private CodeOrganizer getCodeOrganizer(SourceCodeGenerator generator) {
    CodeOrganizer codeOrganizer = generator.getNewCodeOrganizerForTypeName(this.getName());
    for (NamedUserConstructor constructor : this.constructors) {
      codeOrganizer.addConstructor(constructor);
    }

    for (UserMethod method : this.methods) {
      if (method.isStatic()) {
        codeOrganizer.addStaticMethod(method);
      } else {
        codeOrganizer.addNonStaticMethod(method);
      }
    }

    for (UserField field : this.fields) {
      field.addToOrganizer(codeOrganizer, generator.isPublicStaticFinalFieldGetterDesired());
    }

    return codeOrganizer;
  }

  // This function is supposed to be a short term solution for exporting to tweedle.
  // Enums are replaced with strings and this is used in the conversion.
  public Class getResourceClass() {
    try {
      return Class.forName(getResourceName());
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  private String getResourceName() {
    String owningClass = getName();
    if ("SandDunes".equals(owningClass)) {
      owningClass = "Terrain";
    }
    return "org.lgna.story.resources." + superType.getValue().getName().toLowerCase() + "." + owningClass + "Resource";
  }

  public final StringProperty name = new StringProperty(this, null);
  public final DeclarationProperty<UserPackage> _package = DeclarationProperty.createReferenceInstance(this);
  public final NodeListProperty<NamedUserConstructor> constructors = new NodeListProperty<NamedUserConstructor>(this);
  public final EnumProperty<AccessLevel> accessLevel = new EnumProperty<AccessLevel>(this, AccessLevel.PUBLIC);
  public final EnumProperty<TypeModifierFinalAbstractOrNeither> finalAbstractOrNeither = new EnumProperty<TypeModifierFinalAbstractOrNeither>(this, TypeModifierFinalAbstractOrNeither.NEITHER);
  public final BooleanProperty isStrictFloatingPoint = new BooleanProperty(this, Boolean.FALSE);
}
