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
package org.alice.ide.member;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.IDE;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.member.views.MemberTabView;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.annotations.Visibility;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMember;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;

import javax.swing.JComponent;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberTabComposite<V extends MemberTabView> extends MemberOrControlFlowTabComposite<V> {
  public static boolean ARE_TOOL_PALETTES_INERT = true;

  static boolean getExpandedAccountingForInert(boolean isExpanded) {
    if (ARE_TOOL_PALETTES_INERT) {
      return true;
    } else {
      return isExpanded;
    }
  }

  static final String GROUP_BY_CATEGORY_KEY = "groupByCategory";
  static final String SORT_ALPHABETICALLY_KEY = "sortAlphabetically";

  public static MethodsSubComposite SEPARATOR = null;

  protected static boolean isInclusionDesired(AbstractMember member) {
    if (member instanceof AbstractMethod) {
      AbstractMethod method = (AbstractMethod) member;
      if (method.isStatic()) {
        return false;
      }
    } else if (member instanceof AbstractField) {
      AbstractField field = (AbstractField) member;
      if (field.isStatic()) {
        return false;
      }
    }
    if (member.isPublicAccess() || member.isUserAuthored()) {
      Visibility visibility = member.getVisibility();
      return (visibility == null) || visibility.equals(Visibility.PRIME_TIME);
    } else {
      return false;
    }
  }

  private class InstanceFactoryListener implements ValueListener<InstanceFactory> {
    private boolean isActive;

    public boolean isActive() {
      return this.isActive;
    }

    public void setActive(boolean isActive) {
      this.isActive = isActive;
    }

    @Override
    public void valueChanged(ValueEvent<InstanceFactory> e) {
      if (!e.isAdjusting() && this.isActive) {
        MemberTabComposite.this.refreshContentsLater();
      }
    }
  }

  private final InstanceFactoryListener instanceFactoryListener = new InstanceFactoryListener();

  private final ValueListener<String> sortListener = new ValueListener<String>() {
    @Override
    public void valueChanged(ValueEvent<String> e) {
      MemberTabComposite.this.getView().refreshLater();
    }
  };

  private final ValueListener<DeclarationComposite<?, ?>> declarationCompositeListener = new ValueListener<DeclarationComposite<?, ?>>() {
    @Override
    public void valueChanged(ValueEvent<DeclarationComposite<?, ?>> e) {
      MemberTabComposite.this.refreshContentsLater();
    }
  };

  private final List<JComponent> jTitlesInNeedOfRepaintWhenInstanceFactoryChanges = Lists.newCopyOnWriteArrayList();
  private final AddMethodMenuModel addMethodMenuModel;

  public MemberTabComposite(UUID migrationId, AddMethodMenuModel addMethodMenuModel) {
    super(migrationId);
    this.addMethodMenuModel = addMethodMenuModel;
  }

  public AddMethodMenuModel getAddMethodMenuModel() {
    return this.addMethodMenuModel;
  }

  @Override
  protected void initialize() {
    super.initialize();
    IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().addNewSchoolValueListener(this.instanceFactoryListener);
  }

  public abstract ImmutableDataSingleSelectListState<String> getSortState();

  @Override
  protected final ScrollPane createScrollPaneIfDesired() {
    return null;
  }

  private void refreshContentsLater() {
    for (MethodsSubComposite subComposite : this.getSubComposites()) {
      if (subComposite != null) {
        subComposite.getView().refreshLater();
      }
    }
    this.getView().refreshLater();
  }

  protected abstract boolean isAcceptable(AbstractMethod method);

  protected abstract List<FilteredMethodsSubComposite> getPotentialCategorySubComposites();

  protected abstract List<FilteredMethodsSubComposite> getPotentialCategoryOrAlphabeticalSubComposites();

  protected abstract UserMethodsSubComposite getUserMethodsSubComposite(NamedUserType type);

  protected abstract UnclaimedMethodsComposite getUnclaimedMethodsComposite();

  public List<MethodsSubComposite> getSubComposites() {
    if (findLocalizedText(SORT_ALPHABETICALLY_KEY).equals(getSortState().getValue())) {
      return getSubCompositesAlphabetically();
    } else {
      return getSubCompositesByCategory();
    }
  }

  private List<MethodsSubComposite> getSubCompositesAlphabetically() {
    List<AbstractMethod> methods = Lists.newLinkedList();
    InstanceFactory instanceFactory = IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().getValue();
    if (instanceFactory != null) {
      AbstractType<?, ?, ?> type = instanceFactory.getValueType();
      while (type != null) {
        type.getDeclaredMethods();
        for (AbstractMethod method : type.getDeclaredMethods()) {
          if (this.isAcceptable(method)) {
            if (isInclusionDesired(method)) {
              methods.add(method);
            }
          }
        }
        type = type.isFollowToSuperClassDesired() ? type.getSuperType() : null;
      }
    }
    removeOverrides(methods);
    List<MethodsSubComposite> subComposites = Lists.newLinkedList();
    if (methods.size() > 0) {
      UnclaimedMethodsComposite methodsComposite = this.getUnclaimedMethodsComposite();
      methodsComposite.sortAndSetMethods(methods);
      subComposites.add(methodsComposite);
    }
    return subComposites;
  }

  private List<MethodsSubComposite> getSubCompositesByCategory() {
    List<MethodsSubComposite> subComposites = Lists.newLinkedList();
    List<JavaMethod> javaMethods = Lists.newLinkedList();

    InstanceFactory instanceFactory = IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().getValue();
    if (instanceFactory != null) {
      AbstractType<?, ?, ?> type = instanceFactory.getValueType();
      while (type != null) {
        if (type instanceof NamedUserType) {
          NamedUserType namedUserType = (NamedUserType) type;
          UserMethodsSubComposite userMethodsSubComposite = this.getUserMethodsSubComposite(namedUserType);
          subComposites.add(userMethodsSubComposite);
        } else if (type instanceof JavaType) {
          JavaType javaType = (JavaType) type;
          for (JavaMethod javaMethod : javaType.getDeclaredMethods()) {
            if (this.isAcceptable(javaMethod)) {
              if (isInclusionDesired(javaMethod)) {
                javaMethods.add(javaMethod);
              }
            }
          }
        }
        type = type.isFollowToSuperClassDesired() ? type.getSuperType() : null;
      }
    }
    removeOverrides(javaMethods);

    if (subComposites.size() > 0) {
      subComposites.add(SEPARATOR);
    }
    addSubComposites(subComposites, javaMethods, getPotentialCategorySubComposites());

    List<FilteredMethodsSubComposite> postSubComposites = Lists.newLinkedList();
    addSubComposites(postSubComposites, javaMethods, getPotentialCategoryOrAlphabeticalSubComposites());

    if (javaMethods.size() > 0) {
      UnclaimedMethodsComposite unclaimedMethodsComposite = this.getUnclaimedMethodsComposite();
      unclaimedMethodsComposite.sortAndSetMethods(javaMethods);
      subComposites.add(unclaimedMethodsComposite);
    }

    subComposites.addAll(postSubComposites);
    return subComposites;
  }

  private <T extends FilteredMethodsSubComposite> void addSubComposites(List<? super FilteredMethodsSubComposite> subComposites, List<JavaMethod> javaMethods, List<T> potentialSubComposites) {
    for (T potentialSubComposite : potentialSubComposites) {
      List<JavaMethod> acceptedMethods = Lists.newLinkedList();
      ListIterator<JavaMethod> methodIterator = javaMethods.listIterator();
      while (methodIterator.hasNext()) {
        JavaMethod method = methodIterator.next();
        if (potentialSubComposite.isAcceptingOf(method)) {
          acceptedMethods.add(method);
          methodIterator.remove();
        }
      }

      if (acceptedMethods.size() > 0) {
        potentialSubComposite.sortAndSetMethods(acceptedMethods);
        subComposites.add(potentialSubComposite);
      }
    }
  }

  private static <T extends AbstractMethod> void removeOverrides(List<T> javaMethods) {
    ListIterator<T> iterator = javaMethods.listIterator();
    while (iterator.hasNext()) {
      AbstractMethod method = iterator.next();
      AbstractMethod overridden = method.getOverriddenMethod();
      if (overridden != null && javaMethods.contains(overridden)) {
        iterator.remove();
      }
    }
  }

  @Override
  public void handlePreActivation() {
    super.handlePreActivation();
    this.instanceFactoryListener.setActive(true);
    this.getSortState().addNewSchoolValueListener(this.sortListener);
    IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().addNewSchoolValueListener(this.declarationCompositeListener);
    this.refreshContentsLater();
  }

  @Override
  public void handlePostDeactivation() {
    IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().removeNewSchoolValueListener(this.declarationCompositeListener);
    this.getSortState().removeNewSchoolValueListener(this.sortListener);
    this.instanceFactoryListener.setActive(false);
    super.handlePostDeactivation();
  }
}
