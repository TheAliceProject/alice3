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
package org.alice.ide.ast.type.merge.croquet.edits;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.IDE;
import org.alice.ide.ProjectStack;
import org.alice.ide.ast.type.merge.core.MergeUtilities;
import org.alice.ide.declarationseditor.DeclarationTabState;
import org.alice.ide.project.ProjectChangeOfInterestManager;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.Member;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

import java.net.URI;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class ImportTypeEdit extends AbstractEdit {
  private final URI uriForDescriptionPurposesOnly;
  private final NamedUserType existingType;
  private final List<UserMethod> methodsToAdd;
  private final List<UserMethod> methodsToRemove;
  private final List<UserField> fieldsToAdd;
  private final List<UserField> fieldsToRemove;
  private final List<RenameMemberData> renames;

  public ImportTypeEdit(UserActivity userActivity, URI uriForDescriptionPurposesOnly, NamedUserType existingType, List<UserMethod> methodsToAdd, List<UserMethod> methodsToRemove, List<UserField> fieldsToAdd, List<UserField> fieldsToRemove, List<RenameMemberData> renames) {
    super(userActivity);
    this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
    this.existingType = existingType;
    this.methodsToAdd = methodsToAdd;
    this.methodsToRemove = methodsToRemove;
    this.fieldsToAdd = fieldsToAdd;
    this.fieldsToRemove = fieldsToRemove;
    this.renames = renames;
  }

  public ImportTypeEdit(BinaryDecoder binaryDecoder, Object step) {
    super(binaryDecoder, step);
    Logger.todo("decode", this);
    this.uriForDescriptionPurposesOnly = null;
    this.existingType = null;
    this.methodsToAdd = null;
    this.methodsToRemove = null;
    this.fieldsToAdd = null;
    this.fieldsToRemove = null;
    this.renames = null;
  }

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    super.encode(binaryEncoder);
    Logger.todo("encode", this);
  }

  private static <M extends Member> void add(NodeListProperty<M> property, M member) {
    property.add(member);
  }

  private static <M extends Member> void remove(NodeListProperty<M> property, M member) {
    property.remove(property.indexOf(member));
  }

  @Override
  protected final void doOrRedoInternal(boolean isDo) {
    for (UserMethod method : this.methodsToAdd) {
      add(this.existingType.methods, method);
    }
    for (UserField field : this.fieldsToAdd) {
      add(this.existingType.fields, field);
    }
    for (UserMethod method : this.methodsToRemove) {
      remove(this.existingType.methods, method);
    }
    for (UserField field : this.fieldsToRemove) {
      remove(this.existingType.fields, field);
    }
    for (RenameMemberData renameData : this.renames) {
      renameData.setPrevName(renameData.getMember().getName());
      renameData.getMember().setName(renameData.getNextName());
    }

    NamedUserType programType = ProjectStack.peekProject().getProgramType();
    if (programType != null) {
      MergeUtilities.mendMethodInvocationsAndFieldAccesses(programType);
    }
    //todo: remove
    ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
    DeclarationTabState declarationTabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
    declarationTabState.removeAllOrphans();
  }

  @Override
  protected final void undoInternal() {
    for (UserMethod method : this.methodsToAdd) {
      remove(this.existingType.methods, method);
    }
    for (UserField field : this.fieldsToAdd) {
      remove(this.existingType.fields, field);
    }
    for (UserMethod method : this.methodsToRemove) {
      add(this.existingType.methods, method);
    }
    for (UserField field : this.fieldsToRemove) {
      add(this.existingType.fields, field);
    }

    for (RenameMemberData renameData : this.renames) {
      renameData.getMember().setName(renameData.getPrevName());
    }

    NamedUserType programType = ProjectStack.peekProject().getProgramType();
    if (programType != null) {
      MergeUtilities.mendMethodInvocationsAndFieldAccesses(programType);
    }

    //todo: remove
    ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
    DeclarationTabState declarationTabState = IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
    declarationTabState.removeAllOrphans();
  }

  @Override
  protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
    rv.append("import ");
    rv.append(this.uriForDescriptionPurposesOnly);
  }
}
