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

package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddFieldComposite extends AddDeclarationComposite< org.lgna.project.ast.UserField > {
	protected static class FieldDetailsBuilder {
		private ApplicabilityStatus valueComponentTypeStatus;
		private org.lgna.project.ast.AbstractType<?,?,?> valueComponentTypeInitialValue;
		private ApplicabilityStatus valueIsArrayTypeStatus;
		private boolean valueIsArrayTypeInitialValue;
		private ApplicabilityStatus initializerStatus;
		private org.lgna.project.ast.Expression initializerInitialValue;

		public FieldDetailsBuilder valueComponentType( ApplicabilityStatus status, org.lgna.project.ast.AbstractType<?,?,?> initialValue ) {
			this.valueComponentTypeStatus = status;
			this.valueComponentTypeInitialValue = initialValue;
			return this;
		}
		public FieldDetailsBuilder valueIsArrayType( ApplicabilityStatus status, boolean initialValue ) {
			this.valueIsArrayTypeStatus = status;
			this.valueIsArrayTypeInitialValue = initialValue;
			return this;
		}
		public FieldDetailsBuilder initializer( ApplicabilityStatus status, org.lgna.project.ast.Expression initialValue ) {
			this.initializerStatus = status;
			this.initializerInitialValue = initialValue;
			return this;
		}
		public Details build() {
			assert this.valueComponentTypeStatus != null : this;
			assert this.valueIsArrayTypeStatus != null : this;
			assert this.initializerStatus != null : this;
			return new Details()
				.valueComponentType( this.valueComponentTypeStatus, this.valueComponentTypeInitialValue )
				.valueIsArrayType( this.valueIsArrayTypeStatus, this.valueIsArrayTypeInitialValue )
				.name( ApplicabilityStatus.EDITABLE )
				.initializer( this.initializerStatus, this.initializerInitialValue );
		}
	}

	public AddFieldComposite( java.util.UUID migrationId, Details details ) {
		super( migrationId, details );
	}
	protected abstract org.lgna.project.ast.ManagementLevel getManagementLevel();
	protected abstract boolean isFieldFinal();
	private org.lgna.project.ast.UserField createField() {
		org.lgna.project.ast.UserField field = new org.lgna.project.ast.UserField();
		if( this.isFieldFinal() ) {
			field.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		}
		field.accessLevel.setValue( org.lgna.project.ast.AccessLevel.PRIVATE );
		field.managementLevel.setValue( this.getManagementLevel() );
		field.valueType.setValue( this.getValueType() );
		field.name.setValue( this.getDeclarationLikeSubstanceName() );
		field.initializer.setValue( this.getInitializer() );
		return field;
	}
	@Override
	public org.lgna.project.ast.UserField getPreviewValue() {
		return this.createField();
	}

	protected abstract org.alice.ide.croquet.edits.ast.DeclareFieldEdit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field );
	@Override
	protected final org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return this.createEdit( completionStep, this.getDeclaringType(), this.createField() );
	}
	
	@Override
	protected boolean isNameAvailable( String name ) {
		return org.lgna.project.ast.StaticAnalysisUtilities.isAvailableFieldName( name, this.getDeclaringType() );
	}
	
	protected org.lgna.project.ast.InstanceCreation getInstanceCreationFromInitializer() {
		org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> initializerState = this.getInitializerState();
		if( initializerState != null ) {
			org.lgna.project.ast.Expression expression = initializerState.getValue();
			if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
				return (org.lgna.project.ast.InstanceCreation)expression;
			}
		}
		return null;
	}
	protected java.lang.reflect.Field getFldFromInstanceCreationInitializer( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		if( instanceCreation != null ) {
			org.lgna.project.ast.JavaField argumentField = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( instanceCreation );
			if( argumentField != null ) {
				java.lang.reflect.Field fld = argumentField.getFieldReflectionProxy().getReification();
				return fld;
			}
		}
		return null;
	}
	
	private String generateNameFromInitializer() {
		org.lgna.project.ast.InstanceCreation instanceCreation = this.getInstanceCreationFromInitializer();
		if( instanceCreation != null ) {
			java.lang.reflect.Field fld = this.getFldFromInstanceCreationInitializer( instanceCreation );
			if( fld != null ) {
				return org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( fld.getName() );
			} else {
				org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
				org.lgna.project.ast.AbstractType< ?,?,? > abstractType = constructor.getDeclaringType();
				String typeName = abstractType.getName();
				if( typeName != null ) {
					//todo: move to api configuration
					if( typeName.length() > 1 ) {
						if( typeName.charAt( 0 ) == 'S' && Character.isUpperCase( typeName.charAt( 1 ) ) ) {
							typeName = typeName.substring( 1 );
						}
					}
					return org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertFirstCharacterToLowerCase( typeName );
				} else {
					return "";
				}
			}
		} else {
			return "";
		}
	}
//	protected void updateNameTextField() {
//		if( org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptProvidingInitialFieldNamesState.getInstance().getValue() ) {
//			String name = generateNameFromInitializer();
//			this.getNameState().setValueTransactionlessly( name );
//			this.getNameState().selectAll();
////			javax.swing.JTextField jTextField = this.nameTextField.getAwtComponent();
////			if( jTextField.getSelectionStart() == 0 && jTextField.getSelectionEnd() == jTextField.getDocument().getLength() ) {
////				String name = generateNameFromInitializer();
////				model.getNameState().setValue( name );
////				this.nameTextField.requestFocus();
////				this.nameTextField.selectAll();
////				this.nameTextField.repaint();
////			}
//		}
//	}
	
	@Override
	protected java.lang.String getInitialNameValue() {
		if( org.alice.stageide.croquet.models.gallerybrowser.preferences.IsPromptProvidingInitialFieldNamesState.getInstance().getValue() ) {
			return generateNameFromInitializer();
		} else {
			return super.getInitialNameValue();
		}
	}
}
