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

import org.alice.ide.delete.references.croquet.ReferencesToFieldPreventingDeletionDialog;
import org.lgna.croquet.history.CompletionStep;

/**
 * @author Dennis Cosgrove
 */
public class DeleteFieldOperation extends DeleteMemberOperation<org.lgna.project.ast.UserField> {
	private static java.util.Map<org.lgna.project.ast.UserField, DeleteFieldOperation> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized DeleteFieldOperation getInstance( org.lgna.project.ast.UserField field ) {
		return getInstance( field, field.getDeclaringType() );
	}

	public static synchronized DeleteFieldOperation getInstance( org.lgna.project.ast.UserField field, org.lgna.project.ast.UserType<?> declaringType ) {
		DeleteFieldOperation rv = map.get( field );
		if( rv != null ) {
			//pass
		} else {
			rv = new DeleteFieldOperation( field, declaringType );
			map.put( field, rv );
		}
		return rv;
	}

	private org.lgna.project.ast.Statement[] doStatements;
	private org.lgna.project.ast.Statement[] undoStatements;
	private transient int index;

	private DeleteFieldOperation( org.lgna.project.ast.UserField field, org.lgna.project.ast.UserType<?> declaringType ) {
		super( java.util.UUID.fromString( "29e5416c-c0c4-4b6d-9146-5461d5c73c42" ), field, declaringType );
		this.setEnabled( field.isDeletionAllowed.getValue() );
	}

	@Override
	public Class<org.lgna.project.ast.UserField> getNodeParameterType() {
		return org.lgna.project.ast.UserField.class;
	}

	@Override
	public org.lgna.project.ast.NodeListProperty<org.lgna.project.ast.UserField> getNodeListProperty( org.lgna.project.ast.UserType<?> declaringType ) {
		return declaringType.fields;
	}

	@Override
	protected boolean isClearToDelete( org.lgna.project.ast.UserField field ) {
		java.util.List<org.lgna.project.ast.FieldAccess> references = org.alice.ide.IDE.getActiveInstance().getFieldAccesses( field );
		final int N = references.size();
		if( N > 0 ) {
			ReferencesToFieldPreventingDeletionDialog referencesToFieldPreventingDeletionDialog = new ReferencesToFieldPreventingDeletionDialog( field, references );
			CompletionStep<?> step = referencesToFieldPreventingDeletionDialog.getLaunchOperation().fire();
			if( step.isSuccessfullyCompleted() ) {
				org.alice.ide.ProjectDocumentFrame projectDocumentFrame = org.alice.ide.IDE.getActiveInstance().getDocumentFrame();
				projectDocumentFrame.getFindComposite().getMemberReferencesOperationInstance( field ).fire();
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doOrRedoInternal( boolean isDo ) {
		org.lgna.project.ast.UserField field = this.getMember();
		if( field.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.MANAGED ) {
			//Save the index position of the field so we can insert it correctly on undo
			this.index = this.getDeclaringType().fields.indexOf( field );
			//Save the state of field by precomputing the undo and redo statements
			this.doStatements = org.alice.ide.IDE.getActiveInstance().getSceneEditor().getDoStatementsForRemoveField( field );
			this.undoStatements = org.alice.ide.IDE.getActiveInstance().getSceneEditor().getUndoStatementsForRemoveField( field );
			org.alice.ide.IDE.getActiveInstance().getSceneEditor().removeField(
					this.getDeclaringType(),
					field,
					this.doStatements
					);
		} else {
			super.doOrRedoInternal( isDo );
		}
	}

	@Override
	public void undoInternal() {
		org.lgna.project.ast.UserField field = this.getMember();
		if( field.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.MANAGED ) {
			org.alice.ide.IDE.getActiveInstance().getSceneEditor().addField(
					this.getDeclaringType(),
					field,
					this.index,
					this.undoStatements
					);
		} else {
			super.undoInternal();
		}
	}
}
