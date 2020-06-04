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
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.icons.PersonResourceIconFactory;
import org.alice.stageide.personresource.PersonResourceComposite;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.ValueConverter;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.icon.TrimmedImageIconFactory;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.implementation.alice.AliceResourceUtilities;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.ChildPersonResource;
import org.lgna.story.resources.sims2.ElderPersonResource;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.PersonResource;
import org.lgna.story.resources.sims2.TeenPersonResource;
import org.lgna.story.resources.sims2.ToddlerPersonResource;

import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class PersonResourceKey extends InstanceCreatorKey {
  private static IconFactory createIconFactory(String subPath) {
    return new TrimmedImageIconFactory(PersonResourceKey.class.getResource("images/" + subPath + ".png"), 160, 120);
  }

  private static final IconFactory ELDER_ICON_FACTORY = createIconFactory("elder");
  private static final IconFactory ADULT_ICON_FACTORY = createIconFactory("adult");
  private static final IconFactory TEEN_ICON_FACTORY = createIconFactory("teen");
  private static final IconFactory CHILD_ICON_FACTORY = createIconFactory("child");
  private static final IconFactory TODDLER_ICON_FACTORY = createIconFactory("toddler");
  private static final IconFactory PERSON_ICON_FACTORY = new PersonResourceIconFactory();

  private static class SingletonHolder {
    private static PersonResourceKey elderInstance = new PersonResourceKey(LifeStage.ELDER);
    private static PersonResourceKey adultInstance = new PersonResourceKey(LifeStage.ADULT);
    private static PersonResourceKey teenInstance = new PersonResourceKey(LifeStage.TEEN);
    private static PersonResourceKey childInstance = new PersonResourceKey(LifeStage.CHILD);
    private static PersonResourceKey toddlerInstance = new PersonResourceKey(LifeStage.TODDLER);
    private static PersonResourceKey personInstance = new PersonResourceKey(null);
  }

  public static PersonResourceKey getElderInstance() {
    return SingletonHolder.elderInstance;
  }

  public static PersonResourceKey getAdultInstance() {
    return SingletonHolder.adultInstance;
  }

  public static PersonResourceKey getTeenInstance() {
    return SingletonHolder.teenInstance;
  }

  public static PersonResourceKey getChildInstance() {
    return SingletonHolder.childInstance;
  }

  public static PersonResourceKey getToddlerInstance() {
    return SingletonHolder.toddlerInstance;
  }

  private static PersonResourceKey getPersonInstance() {
    return SingletonHolder.personInstance;
  }

  public static PersonResourceKey getInstanceForResourceClass(Class<?> cls) {
    if (cls == ElderPersonResource.class) {
      return getElderInstance();
    } else if (cls == AdultPersonResource.class) {
      return getAdultInstance();
    } else if (cls == TeenPersonResource.class) {
      return getTeenInstance();
    } else if (cls == ChildPersonResource.class) {
      return getChildInstance();
    } else if (cls == ToddlerPersonResource.class) {
      return getToddlerInstance();
    } else {
      return getPersonInstance();
    }
  }

  private final LifeStage lifeStage;

  private PersonResourceKey(LifeStage lifeStage) {
    this.lifeStage = lifeStage;
  }

  public LifeStage getLifeStage() {
    return this.lifeStage;
  }

  @Override
  public Class<? extends ModelResource> getModelResourceCls() {
    if (this.lifeStage == LifeStage.ELDER) {
      return ElderPersonResource.class;
    } else if (this.lifeStage == LifeStage.ADULT) {
      return AdultPersonResource.class;
    } else if (this.lifeStage == LifeStage.TEEN) {
      return TeenPersonResource.class;
    } else if (this.lifeStage == LifeStage.CHILD) {
      return ChildPersonResource.class;
    } else if (this.lifeStage == LifeStage.TODDLER) {
      return ToddlerPersonResource.class;
    } else {
      return PersonResource.class;
    }
  }

  private ValueConverter<PersonResource, InstanceCreation> getPersonResourceValueCreator() {
    return PersonResourceComposite.getInstance().getRandomPersonExpressionValueConverter(this.lifeStage);
  }

  @Override
  public String getInternalText() {
    StringBuilder sb = new StringBuilder();
    if (this.lifeStage != null) {
      sb.append(this.lifeStage.getDisplayText());
    } else {
      sb.append("Person");
    }

    return sb.toString();
  }

  @Override
  public String getSearchText() {
    return getInternalText();
  }

  @Override
  public String getLocalizedDisplayText() {
    Formatter formatter = FormatterState.getInstance().getValue();
    String className = (lifeStage == null) ? "Person" : lifeStage.getLocalizedDisplayText();

    return String.format(formatter.getNewFormat(), className, "…");
  }

  @Override
  public IconFactory getIconFactory() {
    if (this.lifeStage == LifeStage.ELDER) {
      return ELDER_ICON_FACTORY;
    } else if (this.lifeStage == LifeStage.ADULT) {
      return ADULT_ICON_FACTORY;
    } else if (this.lifeStage == LifeStage.TEEN) {
      return TEEN_ICON_FACTORY;
    } else if (this.lifeStage == LifeStage.CHILD) {
      return CHILD_ICON_FACTORY;
    } else if (this.lifeStage == LifeStage.TODDLER) {
      return TODDLER_ICON_FACTORY;
    } else {
      return PERSON_ICON_FACTORY;
    }
  }

  @Override
  public InstanceCreation createInstanceCreation(Set<NamedUserType> typeCache) {
    return this.getPersonResourceValueCreator().fireAndGetValue();
  }

  @Override
  public String[] getTags() {
    return null;
  }

  @Override
  public String[] getGroupTags() {
    return null;
  }

  @Override
  public String[] getThemeTags() {
    return null;
  }

  @Override
  public Triggerable getLeftClickOperation(ResourceNode node, SingleSelectTreeState<ResourceNode> controller) {
    if (NebulousIde.nonfree.isNonFreeEnabled()) {
      return node.getDropOperation(null, null);
    } else {
      return null;
    }
  }

  @Override
  public Triggerable getDropOperation(ResourceNode node, DragStep step, DropSite dropSite) {
    if (NebulousIde.nonfree.isNonFreeEnabled()) {
      return NebulousIde.nonfree.getPersonResourceDropOperation(this);
    } else {
      return null;
    }
  }

  @Override
  public AxisAlignedBox getBoundingBox() {
    return AliceResourceUtilities.getBoundingBox(getModelResourceCls());
  }

  @Override
  public boolean getPlaceOnGround() {
    return true;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  protected void appendRep(StringBuilder sb) {
    sb.append(this.getLocalizedDisplayText());
  }
}
