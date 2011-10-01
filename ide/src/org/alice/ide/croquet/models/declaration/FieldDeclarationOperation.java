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
public abstract class FieldDeclarationOperation extends DeclarationOperation< org.lgna.project.ast.UserField > {
	public FieldDeclarationOperation( 
			java.util.UUID id, 
			org.lgna.project.ast.UserType<?> initialDeclaringType, boolean isDeclaringTypeEditable,
			org.lgna.project.ast.AbstractType<?,?,?> initialValueComponentType, boolean isValueComponentTypeEditable,
			boolean initialIsArrayValueType, boolean isIsArrayValueTypeEditable,
			String initialName, boolean isNameEditable,
			org.lgna.project.ast.Expression initialExpression, boolean isInitializerEditable
		) {
		super( 
				id, 
				initialDeclaringType, isDeclaringTypeEditable, 
				initialValueComponentType, isValueComponentTypeEditable, 
				initialIsArrayValueType, isIsArrayValueTypeEditable, 
				initialName, isNameEditable, 
				initialExpression, isInitializerEditable
		);
	}

	@Override
	public org.lgna.project.ast.UserField createPreviewDeclaration() {
		org.lgna.project.ast.UserField rv = new org.lgna.project.ast.UserField();
		rv.name.setValue( this.getDeclarationName() );
		rv.valueType.setValue( this.getValueType() );
		rv.initializer.setValue( this.getInitializer() );
		return rv;
	}

	protected abstract org.lgna.project.ast.ManagementLevel getManagementLevel();
	protected abstract boolean isFieldFinal();
	protected abstract org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.UserField field );
	@Override
	protected final org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.InputDialogOperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.AbstractType< ?, ?, ? > valueType, String declarationName, org.lgna.project.ast.Expression initializer ) {
		org.lgna.project.ast.UserField field = new org.lgna.project.ast.UserField();
		if( this.isFieldFinal() ) {
			field.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		}
		field.managementLevel.setValue( this.getManagementLevel() );
		field.valueType.setValue( valueType );
		field.name.setValue( declarationName );
		field.initializer.setValue( initializer );

		return this.createEdit( step, declaringType, field ); 
	}
}
