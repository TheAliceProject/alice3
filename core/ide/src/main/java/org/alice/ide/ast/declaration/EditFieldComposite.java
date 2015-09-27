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
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class EditFieldComposite extends FieldComposite {
	private final org.lgna.project.ast.UserField field;
	private final org.lgna.project.ast.UserField previewField = new org.lgna.project.ast.UserField();

	public EditFieldComposite( java.util.UUID migrationId, org.lgna.project.ast.UserField field, ApplicabilityStatus initializerApplicabilityStatus, org.lgna.project.ast.Expression initializerInitialValue ) {
		super( migrationId, new Details()
				.isFinal( ApplicabilityStatus.DISPLAYED, false )
				.valueComponentType( ApplicabilityStatus.DISPLAYED, null )
				.valueIsArrayType( ApplicabilityStatus.DISPLAYED, false )
				.name( ApplicabilityStatus.EDITABLE )
				.initializer( initializerApplicabilityStatus, initializerInitialValue ) );
		this.field = field;
	}

	@Override
	protected org.alice.ide.ast.declaration.views.DeclarationLikeSubstanceView createView() {
		return new org.alice.ide.ast.declaration.views.EditFieldView( this );
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return null;
	}

	@Override
	protected boolean isNameAvailable( String name ) {
		return org.lgna.project.ast.StaticAnalysisUtilities.isAvailableFieldName( name, this.field );
	}

	@Override
	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		return this.field.getDeclaringType();
	}

	@Override
	public org.lgna.project.ast.UserField getPreviewValue() {
		if( this.getIsFinalState().getValue() ) {
			this.previewField.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		} else {
			this.previewField.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.NEITHER );
		}
		this.previewField.valueType.setValue( this.getValueType() );
		this.previewField.name.setValue( this.getDeclarationLikeSubstanceName() );
		this.previewField.initializer.setValue( this.getInitializer() );
		return this.previewField;
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
		org.lgna.project.ast.AbstractType<?, ?, ?> valueType = this.field.getValueType();
		if( valueType.isArray() ) {
			return valueType.getComponentType();
		} else {
			return valueType;
		}
	}

	@Override
	protected boolean getIsFinalInitialValue() {
		return this.field.isFinal();
	}

	@Override
	protected boolean getValueIsArrayTypeInitialValue() {
		return this.field.getValueType().isArray();
	}

	@Override
	protected String getNameInitialValue() {
		return this.field.getName();
	}

	@Override
	protected org.lgna.project.ast.Expression getInitializerInitialValue() {
		return this.field.initializer.getValue();
	}
}
