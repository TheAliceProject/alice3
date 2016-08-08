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

/**
 * @author Dennis Cosgrove
 */
public class ImportTypeEdit extends org.lgna.croquet.edits.AbstractEdit {
	private final java.net.URI uriForDescriptionPurposesOnly;
	private final org.lgna.project.ast.NamedUserType existingType;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToAdd;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToRemove;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToAdd;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToRemove;
	private final java.util.List<RenameMemberData> renames;

	public ImportTypeEdit( org.lgna.croquet.history.CompletionStep completionStep, java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType existingType, java.util.List<org.lgna.project.ast.UserMethod> methodsToAdd, java.util.List<org.lgna.project.ast.UserMethod> methodsToRemove, java.util.List<org.lgna.project.ast.UserField> fieldsToAdd, java.util.List<org.lgna.project.ast.UserField> fieldsToRemove, java.util.List<RenameMemberData> renames ) {
		super( completionStep );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.existingType = existingType;
		this.methodsToAdd = methodsToAdd;
		this.methodsToRemove = methodsToRemove;
		this.fieldsToAdd = fieldsToAdd;
		this.fieldsToRemove = fieldsToRemove;
		this.renames = renames;
	}

	public ImportTypeEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "decode", this );
		this.uriForDescriptionPurposesOnly = null;
		this.existingType = null;
		this.methodsToAdd = null;
		this.methodsToRemove = null;
		this.fieldsToAdd = null;
		this.fieldsToRemove = null;
		this.renames = null;
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "encode", this );
	}

	private static <M extends org.lgna.project.ast.Member> void add( org.lgna.project.ast.NodeListProperty<M> property, M member ) {
		property.add( member );
	}

	private static <M extends org.lgna.project.ast.Member> void remove( org.lgna.project.ast.NodeListProperty<M> property, M member ) {
		property.remove( property.indexOf( member ) );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		for( org.lgna.project.ast.UserMethod method : this.methodsToAdd ) {
			add( this.existingType.methods, method );
		}
		for( org.lgna.project.ast.UserField field : this.fieldsToAdd ) {
			add( this.existingType.fields, field );
		}
		for( org.lgna.project.ast.UserMethod method : this.methodsToRemove ) {
			remove( this.existingType.methods, method );
		}
		for( org.lgna.project.ast.UserField field : this.fieldsToRemove ) {
			remove( this.existingType.fields, field );
		}
		for( RenameMemberData renameData : this.renames ) {
			renameData.setPrevName( renameData.getMember().getName() );
			renameData.getMember().setName( renameData.getNextName() );
		}

		org.lgna.project.ast.NamedUserType programType = org.alice.ide.ProjectStack.peekProject().getProgramType();
		if( programType != null ) {
			org.alice.ide.ast.type.merge.core.MergeUtilities.mendMethodInvocationsAndFieldAccesses( programType );
		}
		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
		org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		declarationTabState.removeAllOrphans();
	}

	@Override
	protected final void undoInternal() {
		for( org.lgna.project.ast.UserMethod method : this.methodsToAdd ) {
			remove( this.existingType.methods, method );
		}
		for( org.lgna.project.ast.UserField field : this.fieldsToAdd ) {
			remove( this.existingType.fields, field );
		}
		for( org.lgna.project.ast.UserMethod method : this.methodsToRemove ) {
			add( this.existingType.methods, method );
		}
		for( org.lgna.project.ast.UserField field : this.fieldsToRemove ) {
			add( this.existingType.fields, field );
		}

		for( RenameMemberData renameData : this.renames ) {
			renameData.getMember().setName( renameData.getPrevName() );
		}

		org.lgna.project.ast.NamedUserType programType = org.alice.ide.ProjectStack.peekProject().getProgramType();
		if( programType != null ) {
			org.alice.ide.ast.type.merge.core.MergeUtilities.mendMethodInvocationsAndFieldAccesses( programType );
		}

		//todo: remove
		org.alice.ide.project.ProjectChangeOfInterestManager.SINGLETON.fireProjectChangeOfInterestListeners();
		org.alice.ide.declarationseditor.DeclarationTabState declarationTabState = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		declarationTabState.removeAllOrphans();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "import " );
		rv.append( this.uriForDescriptionPurposesOnly );
	}
}
