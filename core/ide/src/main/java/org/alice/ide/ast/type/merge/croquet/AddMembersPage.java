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
package org.alice.ide.ast.type.merge.croquet;

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.ast.type.croquet.ImportTypeWizard;
import org.alice.ide.ast.type.merge.core.MergeUtilities;
import org.alice.ide.ast.type.merge.croquet.edits.ImportTypeEdit;
import org.alice.ide.ast.type.merge.croquet.edits.RenameMemberData;
import org.alice.ide.ast.type.merge.croquet.views.AddMembersPane;
import org.lgna.common.Resource;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.WizardPageComposite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.ast.AbstractNode;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.Member;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.code.CodeAppender;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class AddMembersPage extends WizardPageComposite<Panel, ImportTypeWizard> {

  public static String modifyFilenameLocalizedText(String s, URI uri) {
    File file = new File(uri);
    return s.replaceAll("</filename/>", file.getName());
  }

  private final URI uriForDescriptionPurposesOnly;

  private final NamedUserType importedRootType;
  private final Set<Resource> importedResources;

  private final NamedUserType srcType;
  private final NamedUserType dstType;

  private final ProceduresToolPalette addProceduresComposite;
  private final FunctionsToolPalette addFunctionsComposite;
  private final FieldsToolPalette addFieldsComposite;

  private final ErrorStatus actionItemsRemainingError = this.createErrorStatus("actionItemsRemainingError");

  private final Operation acceptAllDifferentImplementationsOperation = this.createActionOperation("acceptAllDifferentImplementationsOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      acceptAllDifferentImplementations();
      return null;
    }
  });
  private final Operation rejectAllDifferentImplementationsOperation = this.createActionOperation("rejectAllDifferentImplementationsOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      rejectAllDifferentImplementations();
      return null;
    }
  });

  private final PlainStringValue differentImplementationsHeader = this.createStringValue("differentImplementationsHeader");
  private final PlainStringValue differentImplementationsSubHeader = this.createStringValue("differentImplementationsSubHeader");

  private boolean isManagementLevelAppropriate(UserMethod method) {
    ManagementLevel managementLevel = method.getManagementLevel();
    return (managementLevel == null) || (managementLevel == ManagementLevel.NONE);
  }

  public AddMembersPage(ImportTypeWizard wizard, URI uriForDescriptionPurposesOnly, NamedUserType importedRootType, Set<Resource> importedResources, NamedUserType srcType, NamedUserType dstType) {
    super(UUID.fromString("d00d925e-0a2c-46c7-b6c8-0d3d1189bc5c"), wizard);
    this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
    this.importedRootType = importedRootType;
    this.importedResources = importedResources;
    this.srcType = srcType;
    this.dstType = dstType;

    if (this.dstType != null) {
      List<UserMethod> projectProcedures = Lists.newLinkedList();
      List<UserMethod> projectFunctions = Lists.newLinkedList();
      for (UserMethod projectMethod : this.dstType.methods) {
        if (isManagementLevelAppropriate(projectMethod)) {
          if (projectMethod.isProcedure()) {
            projectProcedures.add(projectMethod);
          } else {
            projectFunctions.add(projectMethod);
          }
        }
      }
      this.addProceduresComposite = this.registerSubComposite(new ProceduresToolPalette(this.uriForDescriptionPurposesOnly, projectProcedures));
      this.addFunctionsComposite = this.registerSubComposite(new FunctionsToolPalette(this.uriForDescriptionPurposesOnly, projectFunctions));

      List<UserField> dstFields = this.dstType.getDeclaredFields();
      List<UserField> projectFields = Lists.newArrayListWithInitialCapacity(dstFields.size());
      projectFields.addAll(dstFields);
      this.addFieldsComposite = this.registerSubComposite(new FieldsToolPalette(this.uriForDescriptionPurposesOnly, projectFields));

      for (UserMethod importMethod : this.srcType.methods) {
        if (isManagementLevelAppropriate(importMethod)) {
          MethodsToolPalette<?> addMethodsComposite = importMethod.isProcedure() ? this.addProceduresComposite : this.addFunctionsComposite;
          UserMethod projectMethod = MergeUtilities.findMethodWithMatchingName(importMethod, dstType);
          if (projectMethod != null) {
            if (MergeUtilities.isEquivalent(importMethod, projectMethod)) {
              addMethodsComposite.addIdenticalMembers(importMethod, projectMethod);
            } else {
              if (MergeUtilities.isHeaderEquivalent(importMethod, projectMethod)) {
                addMethodsComposite.addDifferentImplementationMembers(importMethod, projectMethod);
              } else {
                addMethodsComposite.addDifferentSignatureMembers(importMethod, projectMethod);
              }
            }
          } else {
            addMethodsComposite.addImportOnlyMember(importMethod);
          }
        }
      }

      this.addProceduresComposite.reifyProjectOnly();
      this.addFunctionsComposite.reifyProjectOnly();

      for (UserField importField : this.srcType.fields) {
        UserField projectField = dstType.getDeclaredField(importField.getName());
        if (projectField != null) {
          if (MergeUtilities.isEquivalent(importField, projectField)) {
            this.addFieldsComposite.addIdenticalMembers(importField, projectField);
          } else {
            if (MergeUtilities.isValueTypeEquivalent(importField, projectField)) {
              this.addFieldsComposite.addDifferentImplementationMembers(importField, projectField);
            } else {
              this.addFieldsComposite.addDifferentSignatureMembers(importField, projectField);
            }
          }
        } else {
          this.addFieldsComposite.addImportOnlyMember(importField);
        }
      }

      this.addFieldsComposite.reifyProjectOnly();

    } else {
      this.addProceduresComposite = null;
      this.addFunctionsComposite = null;
      this.addFieldsComposite = null;
    }
  }

  @Override
  protected ScrollPane createScrollPaneIfDesired() {
    return null;
  }

  public ProceduresToolPalette getAddProceduresComposite() {
    return this.addProceduresComposite;
  }

  public FunctionsToolPalette getAddFunctionsComposite() {
    return this.addFunctionsComposite;
  }

  public FieldsToolPalette getAddFieldsComposite() {
    return this.addFieldsComposite;
  }

  @Override
  protected AddMembersPane createView() {
    return new AddMembersPane(this);
  }

  public NamedUserType getImportedType() {
    return this.importedRootType;
  }

  public Set<Resource> getImportedResources() {
    return this.importedResources;
  }

  public NamedUserType getDstType() {
    return this.dstType;
  }

  private void addRenameIfNecessary(List<RenameMemberData> renames, MemberNameState<? extends Member> nameState, Member member) {
    String nextName = nameState.getValue();
    if (nextName.contentEquals(member.getName())) {
      //pass
    } else {
      renames.add(new RenameMemberData(member, nextName));
    }
  }

  private void addRenameIfNecessary(List<RenameMemberData> renames, MemberNameState<? extends Member> nameState) {
    addRenameIfNecessary(renames, nameState, nameState.getMember());
  }

  private <M extends AbstractNode & CodeAppender> M createImportCopy(M original) {
    return AstUtilities.createCopy(original, this.importedRootType);
  }

  private <M extends AbstractNode & Member & CodeAppender> void addMembersAndRenames(List<M> membersToAdd, List<M> membersToRemove, List<RenameMemberData> renames, MembersToolPalette<?, M> addMembersComposite) {
    for (ImportOnly<M> importOnly : addMembersComposite.getImportOnlys()) {
      if (importOnly.getImportHub().getIsDesiredState().getValue()) {
        membersToAdd.add(this.createImportCopy(importOnly.getImportHub().getMember()));
      }
    }

    for (DifferentSignature<M> differentSignature : addMembersComposite.getDifferentSignatures()) {
      if (differentSignature.getImportHub().getIsDesiredState().getValue()) {
        M member = this.createImportCopy(differentSignature.getImportHub().getMember());
        membersToAdd.add(member);
        addRenameIfNecessary(renames, differentSignature.getImportHub().getNameState(), member);
        addRenameIfNecessary(renames, differentSignature.getProjectHub().getNameState());
      }
    }

    for (DifferentImplementation<M> differentImplementation : addMembersComposite.getDifferentImplementations()) {
      if (differentImplementation.getImportHub().getIsDesiredState().getValue()) {
        M member = this.createImportCopy(differentImplementation.getImportHub().getMember());
        membersToAdd.add(member);
        if (differentImplementation.getProjectHub().getIsDesiredState().getValue()) {
          addRenameIfNecessary(renames, differentImplementation.getImportHub().getNameState(), member);
          addRenameIfNecessary(renames, differentImplementation.getProjectHub().getNameState());
        } else {
          membersToRemove.add(differentImplementation.getProjectHub().getMember());
        }
      } else {
        if (differentImplementation.getProjectHub().getIsDesiredState().getValue()) {
          //pass
        } else {
          //should not happen
        }
      }
    }
  }

  public Edit createEdit(UserActivity userActivity) {
    if (this.dstType != null) {
      List<RenameMemberData> renames = Lists.newLinkedList();

      List<UserMethod> methodsToAdd = Lists.newLinkedList();
      List<UserMethod> methodsToRemove = Lists.newLinkedList();
      for (MethodsToolPalette<?> addMethodsComposite : new MethodsToolPalette[] {this.addProceduresComposite, this.addFunctionsComposite}) {
        addMembersAndRenames(methodsToAdd, methodsToRemove, renames, addMethodsComposite);
      }

      List<UserField> fieldsToAdd = Lists.newLinkedList();
      List<UserField> fieldsToRemove = Lists.newLinkedList();
      addMembersAndRenames(fieldsToAdd, fieldsToRemove, renames, this.addFieldsComposite);
      return new ImportTypeEdit(userActivity, this.uriForDescriptionPurposesOnly, this.dstType, methodsToAdd, methodsToRemove, fieldsToAdd, fieldsToRemove, renames);
    } else {
      return null;
    }
  }

  private <M extends Member> List<MemberHub<M>> getPreviewMemberHubs(MembersToolPalette<?, M> addMembersComposite) {
    if (this.dstType != null) {
      boolean isIncludingAll = getOwner().getPreviewPage().getIsIncludingAllState().getValue();
      List<MemberHub<M>> hubs = Lists.newLinkedList();
      for (ImportOnly<M> importOnly : addMembersComposite.getImportOnlys()) {
        if (isIncludingAll || importOnly.getImportHub().getIsDesiredState().getValue()) {
          hubs.add(importOnly.getImportHub());
        }
      }

      for (DifferentSignature<M> differentSignature : addMembersComposite.getDifferentSignatures()) {
        if (isIncludingAll || differentSignature.getImportHub().getIsDesiredState().getValue()) {
          hubs.add(differentSignature.getImportHub());
        }
        hubs.add(differentSignature.getProjectHub());
      }

      for (DifferentImplementation<M> differentImplementation : addMembersComposite.getDifferentImplementations()) {
        if (isIncludingAll || differentImplementation.getImportHub().getIsDesiredState().getValue()) {
          hubs.add(differentImplementation.getImportHub());
        }
        if (isIncludingAll || differentImplementation.getProjectHub().getIsDesiredState().getValue()) {
          hubs.add(differentImplementation.getProjectHub());
        }
      }

      for (Identical<M> identical : addMembersComposite.getIdenticals()) {
        hubs.add(identical.getProjectHub());
      }

      for (ProjectOnly<M> projectOnly : addMembersComposite.getProjectOnlys()) {
        hubs.add(projectOnly.getProjectHub());
      }

      return hubs;
    } else {
      return Collections.emptyList();
    }
  }

  public List<MemberHub<UserMethod>> getPreviewProcedureHubs() {
    return getPreviewMemberHubs(this.addProceduresComposite);
  }

  public List<MemberHub<UserMethod>> getPreviewFunctionHubs() {
    return getPreviewMemberHubs(this.addFunctionsComposite);
  }

  public List<MemberHub<UserField>> getPreviewFieldHubs() {
    return getPreviewMemberHubs(this.addFieldsComposite);
  }

  @Override
  public Status getPageStatus() {
    //todo: remove; icons and components rely on repaint being called
    this.getView().repaint();
    //

    StringBuffer sb = new StringBuffer();
    for (MembersToolPalette<?, ?> addMembersComposite : new MembersToolPalette[] {this.addProceduresComposite, this.addFunctionsComposite, this.addFieldsComposite}) {
      addMembersComposite.appendStatusPreRejectorCheck(sb);
    }
    if (sb.length() > 0) {
      this.actionItemsRemainingError.setText(sb.toString());
      return this.actionItemsRemainingError;
    } else {
      return IS_GOOD_TO_GO_STATUS;
    }
  }

  public PlainStringValue getDifferentImplementationsHeader() {
    return this.differentImplementationsHeader;
  }

  public PlainStringValue getDifferentImplementationsSubHeader() {
    return this.differentImplementationsSubHeader;
  }

  public Operation getAcceptAllDifferentImplementationsOperation() {
    return this.acceptAllDifferentImplementationsOperation;
  }

  public Operation getRejectAllDifferentImplementationsOperation() {
    return this.rejectAllDifferentImplementationsOperation;
  }

  private void applyAllDifferentImplementations(boolean isAccept) {
    for (MembersToolPalette<?, ?> addMembersComposite : new MembersToolPalette[] {this.getAddProceduresComposite(), this.getAddFunctionsComposite(), this.getAddFieldsComposite()}) {
      for (DifferentImplementation<?> differentImplementation : addMembersComposite.getDifferentImplementations()) {
        differentImplementation.getImportHub().getIsDesiredState().setValueTransactionlessly(isAccept);
        differentImplementation.getProjectHub().getIsDesiredState().setValueTransactionlessly(isAccept == false);
      }
    }
  }

  private void acceptAllDifferentImplementations() {
    this.applyAllDifferentImplementations(true);
  }

  private void rejectAllDifferentImplementations() {
    this.applyAllDifferentImplementations(false);
  }

  public boolean isContainingDifferentImplementations() {
    for (MembersToolPalette<?, ?> addMembersComposite : new MembersToolPalette[] {this.getAddProceduresComposite(), this.getAddFunctionsComposite(), this.getAddFieldsComposite()}) {
      if (addMembersComposite.getDifferentImplementations().size() > 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void resetData() {
  }
}
