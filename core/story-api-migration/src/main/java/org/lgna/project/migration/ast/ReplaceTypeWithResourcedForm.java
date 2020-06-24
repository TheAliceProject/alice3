/**
 * Copyright (c) 2020 Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.migration.ast;

import edu.cmu.cs.dennisc.pattern.Crawlable;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.DeclarationProperty;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLocal;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.ResourceTypeHelper;

import java.util.List;
import java.util.Set;

/*
 * Used to change the use of a gallery class from one that does not take a resource argument
 * to one that does take a resource.
 * Used in a CompoundMigration.
 */
public class ReplaceTypeWithResourcedForm<T extends ModelResource> implements NodeMigration {
  private final String oldClass;
  private final T newResource;
  private final String newClass;

  public ReplaceTypeWithResourcedForm(String oldClassName, T resource) {
    this.oldClass = oldClassName;
    this.newResource = resource;
    newClass = newResource.getClass().getSimpleName().replace("Resource", "");
  }

  @Override
  public void migrateNode(Crawlable node, ResourceTypeHelper typeHelper, Set<NamedUserType> typeCache) {
    if (node instanceof UserField) {
      migrateField((UserField) node, typeHelper, typeCache);
    }
    if (node instanceof UserLocal) {
      migrateType(((UserLocal) node).valueType, typeCache);
    }
    if (node instanceof UserParameter) {
      migrateType(((UserParameter) node).valueType, typeCache);
    }
  }

  protected void migrateField(UserField field, ResourceTypeHelper typeHelper, Set<NamedUserType> typeCache) {
    final AbstractType<?, ?, ?> oldFieldType = field.valueType.getValue();
    if (oldClass.equals(oldFieldType.getName()) && constructorsTakeNoArguments(oldFieldType.getDeclaredConstructors())) {
      AbstractType<?, ?, ?> superType = oldFieldType.getSuperType();
      if (superType instanceof NamedUserType) {
        typeCache.add((NamedUserType) superType);
      }
      if (typeHelper == null) {
        throw new MigrationException("Unable to migrate project without ResourceTypeHelper");
      }
      InstanceCreation instantiation = typeHelper.createInstanceCreation(newResource, typeCache);
      field.valueType.setValue(instantiation.getType());
      field.initializer.setValue(instantiation);
    }
  }

  private boolean constructorsTakeNoArguments(List<? extends AbstractConstructor> constructors) {
    for (AbstractConstructor constructor : constructors) {
      if (constructor.getRequiredParameters().size() > 0) {
        return false;
      }
    }
    return true;
  }

  private void migrateType(DeclarationProperty<AbstractType<?, ?, ?>> property, Set<NamedUserType> typeCache) {
    final AbstractType<?, ?, ?> oldLocalType = property.getValue();
    if (oldClass.equals(oldLocalType.getName())) {
      // This relies on the typeCache being filled in when the UserField is migrated. The Project structure enforces this so far.
      for (NamedUserType existingType : typeCache) {
        if (newClass.equals(existingType.getName())) {
          property.setValue(existingType);
          return;
        }
      }
    }
  }
}
