/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class ManagedFieldDeclarationOperation extends FieldDeclarationOperation {
	protected static class EditCustomization {
		private final java.util.List< org.lgna.project.ast.Statement > doStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private final java.util.List< org.lgna.project.ast.Statement > undoStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private final java.util.List< org.alice.virtualmachine.Resource > resources = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		public void addDoStatement( org.lgna.project.ast.Statement statement ) {
			this.doStatements.add( statement );
		}
		public org.lgna.project.ast.Statement[] getDoStatements() {
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.doStatements, org.lgna.project.ast.Statement.class );
		}
		public void addUndoStatement( org.lgna.project.ast.Statement statement ) {
			this.undoStatements.add( statement );
		}
		public org.lgna.project.ast.Statement[] getUndoStatements() {
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.undoStatements, org.lgna.project.ast.Statement.class );
		}
		public void addResource( org.alice.virtualmachine.Resource resource ) {
			this.resources.add( resource );
		}
		public org.alice.virtualmachine.Resource[] getResources() {
			return edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.resources, org.alice.virtualmachine.Resource.class );
		}
	}

	public ManagedFieldDeclarationOperation( 
			java.util.UUID id, 
			org.lgna.project.ast.AbstractType<?,?,?> initialValueComponentType, boolean isValueComponentTypeEditable,
			boolean initialIsArrayValueType, boolean isIsArrayValueTypeEditable,
			String initialName, boolean isNameEditable,
			org.lgna.project.ast.Expression initialExpression, boolean isInitializerEditable
		) {
		super( 
				id, 
				null, false, 
				initialValueComponentType, isValueComponentTypeEditable, 
				initialIsArrayValueType, isIsArrayValueTypeEditable, 
				initialName, isNameEditable, 
				initialExpression, isInitializerEditable
		);
	}
	
	@Override
	protected InstanceCreationInitializerState createInitializerState( org.lgna.project.ast.Expression initialValue ) {
		return new InstanceCreationInitializerState( this, (org.lgna.project.ast.InstanceCreation)initialValue );
	}
	@Override
	public InstanceCreationInitializerState getInitializerState() {
		return (InstanceCreationInitializerState)super.getInitializerState();
	}
	protected abstract EditCustomization customize( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field, EditCustomization rv );
	@Override
	protected boolean isFieldFinal() {
		return true;
	}
	@Override
	protected boolean isFieldManaged() {
		return true;
	}
	@Override
	public org.lgna.project.ast.UserType< ? > getDeclaringType() {
		return org.alice.ide.IDE.getActiveInstance().getSceneType();
	}

	@Override
	public org.lgna.project.ast.UserField createPreviewDeclaration() {
		org.lgna.project.ast.UserField rv = new org.lgna.project.ast.UserField();
		rv.name.setValue( this.getDeclarationName() );
		rv.valueType.setValue( this.getValueType() );
		rv.initializer.setValue( this.getInitializer() );
		return rv;
	}
	
	@Override
	protected org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field ) {
		EditCustomization customization = new EditCustomization();
		this.customize( step, declaringType, field, customization );
		return new org.alice.ide.croquet.edits.ast.DeclareGalleryFieldEdit( step, this.getDeclaringType(), field, customization.getDoStatements(), customization.getUndoStatements() );
	}

}
