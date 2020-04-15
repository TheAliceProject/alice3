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
package org.alice.stageide.modelresource;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import org.alice.ide.croquet.models.ui.formatter.FormatterState;
import org.alice.ide.formatter.Formatter;
import org.alice.stageide.icons.IconFactoryManager;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.implementation.alice.AliceResourceUtilties;
import org.lgna.story.resources.ModelResource;

import javax.swing.JComponent;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public final class ClassResourceKey extends InstanceCreatorKey {
  private final Class<? extends ModelResource> cls;

  public ClassResourceKey(Class<? extends ModelResource> cls) {
    this.cls = cls;
  }

  @Override
  public Class<? extends ModelResource> getModelResourceCls() {
    return this.cls;
  }

  public JavaType getType() {
    return JavaType.getInstance(this.cls);
  }

  @Override
  public String getInternalText() {
    return AliceResourceUtilties.getModelClassName(getModelResourceCls(), null, null);
  }

  @Override
  public String getSearchText() {
    return AliceResourceUtilties.getModelClassName(getModelResourceCls(), null, JComponent.getDefaultLocale());
  }

  @Override
  public String getLocalizedDisplayText() {
    Formatter formatter = FormatterState.getInstance().getValue();
    return formatter.galleryLabelFor(this);
  }

  @Override
  public IconFactory getIconFactory() {
    if (this.isLeaf()) {
      ModelResource modelResource = cls.getEnumConstants()[0];
      return IconFactoryManager.getIconFactoryForResourceInstance(modelResource);
    } else {
      return IconFactoryManager.getIconFactoryForResourceCls(cls);
    }
  }

  @Override
  public InstanceCreation createInstanceCreation(Set<NamedUserType> typeCache) {
    throw new Error();
  }

  @Override
  public boolean isInterface() {
    return cls.isInterface();
  }

  @Override
  public boolean isLeaf() {
    return this.cls.isEnum() && (this.cls.getEnumConstants().length == 1);
  }

  @Override
  public String[] getTags() {
    return AliceResourceUtilties.getTags(getModelResourceCls(), null, JComponent.getDefaultLocale());
  }

  @Override
  public String[] getGroupTags() {
    return AliceResourceUtilties.getGroupTags(getModelResourceCls(), null, JComponent.getDefaultLocale());
  }

  @Override
  public String[] getThemeTags() {
    return AliceResourceUtilties.getThemeTags(getModelResourceCls(), null, JComponent.getDefaultLocale());
  }

  @Override
  public Triggerable getLeftClickOperation(ResourceNode node, SingleSelectTreeState<ResourceNode> controller) {
    if (isLeaf()) {
      ResourceNode child = node.getFirstChild();
      if (child != null) {
        return child.getLeftButtonClickOperation(controller);
      } else {
        return null;
      }
    } else {
      return controller.getItemSelectionOperation(node);
    }
  }

  @Override
  public Triggerable getDropOperation(ResourceNode node, DragStep step, DropSite dropSite) {
    if (isLeaf()) {
      ResourceNode child = node.getFirstChild();
      if (child != null) {
        return child.getDropOperation(step, dropSite);
      } else {
        return null;
      }
    } else {
      return new AddFieldCascade(node, dropSite);
    }
  }

  @Override
  public AxisAlignedBox getBoundingBox() {
    return AliceResourceUtilties.getBoundingBox(getModelResourceCls());
  }

  @Override
  public boolean getPlaceOnGround() {
    return AliceResourceUtilties.getPlaceOnGround(getModelResourceCls());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ClassResourceKey) {
      ClassResourceKey other = (ClassResourceKey) o;
      return this.cls.equals(other.cls);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.cls.hashCode();
  }

  @Override
  protected void appendRep(StringBuilder sb) {
    sb.append(this.cls.getSimpleName());
  }
}
