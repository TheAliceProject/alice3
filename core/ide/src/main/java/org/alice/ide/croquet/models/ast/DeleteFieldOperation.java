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
package org.alice.ide.croquet.models.ast;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.delete.references.croquet.ReferencesToFieldPreventingDeletionDialog;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class DeleteFieldOperation extends DeleteMemberOperation<UserField> {
  private static Map<UserField, DeleteFieldOperation> map = Maps.newHashMap();

  public static synchronized DeleteFieldOperation getInstance(UserField field) {
    return getInstance(field, field.getDeclaringType());
  }

  public static synchronized DeleteFieldOperation getInstance(UserField field, UserType<?> declaringType) {
    DeleteFieldOperation rv = map.get(field);
    if (rv == null) {
      rv = new DeleteFieldOperation(field, declaringType);
      map.put(field, rv);
    }
    return rv;
  }

  private Statement[] doStatements;
  private Statement[] undoStatements;
  private transient int index;

  private DeleteFieldOperation(UserField field, UserType<?> declaringType) {
    super(UUID.fromString("29e5416c-c0c4-4b6d-9146-5461d5c73c42"), field, declaringType);
    this.setEnabled(field.isDeletionAllowed.getValue());
  }

  @Override
  public Class<UserField> getNodeParameterType() {
    return UserField.class;
  }

  @Override
  public NodeListProperty<UserField> getNodeListProperty(UserType<?> declaringType) {
    return declaringType.fields;
  }

  @Override
  protected boolean isClearToDelete(UserField field, UserActivity activity) {
    List<FieldAccess> references = IDE.getActiveInstance().getFieldAccesses(field);
    final int N = references.size();
    if (N > 0) {
      ReferencesToFieldPreventingDeletionDialog referencesToFieldPreventingDeletionDialog = new ReferencesToFieldPreventingDeletionDialog(field, references);
      UserActivity referenceCheckActivity = activity.newChildActivity();
      referencesToFieldPreventingDeletionDialog.getLaunchOperation().fire(referenceCheckActivity);
      if (referenceCheckActivity.isSuccessfullyCompleted()) {
        ProjectDocumentFrame projectDocumentFrame = IDE.getActiveInstance().getDocumentFrame();
        projectDocumentFrame.getFindComposite().getMemberReferencesOperationInstance(field).fire(referenceCheckActivity);
      }
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void doOrRedoInternal(boolean isDo) {
    UserField field = this.getMember();
    if (field.managementLevel.getValue() == ManagementLevel.MANAGED) {
      //Save the index position of the field so we can insert it correctly on undo
      this.index = this.getDeclaringType().fields.indexOf(field);
      //Save the state of field by precomputing the undo and redo statements
      StorytellingSceneEditor editor = StageIDE.getActiveInstance().getSceneEditor();
      Map<AbstractField, Statement> riders = editor.getRiders(field);
      this.doStatements = editor.getDoStatementsForRemoveField(field, riders);
      this.undoStatements = editor.getUndoStatementsForRemoveField(field, riders);
      editor.removeField(this.getDeclaringType(), field, this.doStatements);
    } else {
      super.doOrRedoInternal(isDo);
    }
  }

  @Override
  public void undoInternal() {
    UserField field = this.getMember();
    if (field.managementLevel.getValue() == ManagementLevel.MANAGED) {
      IDE.getActiveInstance().getSceneEditor().addField(this.getDeclaringType(), field, this.index, this.undoStatements);
    } else {
      super.undoInternal();
    }
  }
}
