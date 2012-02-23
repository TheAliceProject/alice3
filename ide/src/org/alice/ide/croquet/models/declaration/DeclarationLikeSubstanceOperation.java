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
public abstract class DeclarationLikeSubstanceOperation< T extends org.lgna.project.ast.Node > extends org.alice.ide.croquet.models.InputDialogOperationWithPreview< Void > {
	private final org.lgna.project.ast.UserType<?> initialDeclaringType;
	private final org.lgna.project.ast.AbstractType<?,?,?> initialValueComponentType;
	private final boolean initialIsArrayValueType;
	private final String initialName;
	private final org.lgna.project.ast.Expression initialExpression;
	
	private final DeclaringTypeState declaringTypeState;
	private final ValueComponentTypeState valueComponentTypeState;
	private final IsArrayValueTypeState isArrayValueTypeState;
	private final NameState nameState;
	private final org.alice.ide.croquet.models.ExpressionState initializerState;
	private final boolean isDeclaringTypeEditable;
	private final boolean isValueComponentTypeEditable;
	private final boolean isIsArrayValueTypeEditable;
	private final boolean isNameEditable;
	private final boolean isInitializerEditable;
	private final org.alice.ide.name.NameValidator nameValidator;
	
	private String declaringTypeLabelText; 
	private String valueTypeLabelText; 
	private String nameLabelText; 
	private String initializerLabelText; 

	public DeclarationLikeSubstanceOperation( 
			java.util.UUID id, 
			org.lgna.project.ast.UserType<?> initialDeclaringType,
			boolean isDeclaringTypeEditable,
			org.lgna.project.ast.AbstractType<?,?,?> initialValueComponentType,
			boolean isValueComponentTypeEditable,
			boolean initialIsArrayValueType,
			boolean isIsArrayValueTypeEditable,
			String initialName,
			boolean isNameEditable,
			org.lgna.project.ast.Expression initialExpression,
			boolean isInitializerEditable,
			org.alice.ide.name.NameValidator nameValidator
	) {
		super( org.alice.ide.IDE.PROJECT_GROUP, id );
		this.initialDeclaringType = initialDeclaringType;
		this.initialValueComponentType = initialValueComponentType;
		this.initialIsArrayValueType = initialIsArrayValueType;
		this.initialName = initialName;
		this.initialExpression = initialExpression;
		
		if( initialDeclaringType != null || isDeclaringTypeEditable ) {
			this.declaringTypeState = new DeclaringTypeState( this, initialDeclaringType );
		} else {
			this.declaringTypeState = null;
		}
		
		this.valueComponentTypeState = new ValueComponentTypeState( this, initialValueComponentType );
		this.isArrayValueTypeState = new IsArrayValueTypeState( this, initialIsArrayValueType );
		
		if( initialName != null || isNameEditable ) {
			this.nameState = new NameState( this, initialName );
		} else {
			this.nameState = null;
		}
		if( initialExpression != null || isInitializerEditable ) {
			this.initializerState = this.createInitializerState( initialExpression );
		} else {
			this.initializerState = null;
		}
		
		this.isDeclaringTypeEditable = isDeclaringTypeEditable;
		this.isValueComponentTypeEditable = isValueComponentTypeEditable;
		this.isIsArrayValueTypeEditable = isIsArrayValueTypeEditable;
		this.isNameEditable = isNameEditable;
		this.isInitializerEditable = isInitializerEditable;
		
		this.isArrayValueTypeState.setEnabled( this.isIsArrayValueTypeEditable );
		
		this.nameValidator = nameValidator;
	}
	
	protected org.lgna.project.ast.UserType< ? > getInitialDeclaringType() {
		return this.initialDeclaringType;
	}
	protected org.lgna.project.ast.AbstractType< ?, ?, ? > getInitialValueComponentType() {
		return this.initialValueComponentType;
	}
	protected org.alice.ide.croquet.models.ExpressionState createInitializerState( org.lgna.project.ast.Expression initialValue ) {
		return new InitializerState( this, initialValue );
	}
	@Override
	protected void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
		final int WIDTH = Math.min( dialog.getWidth(), 480 );
		final int HEIGHT = Math.max( edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( WIDTH ), dialog.getHeight() );
		dialog.setSize( edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumSize( dialog.getSize(), WIDTH, HEIGHT ) );
	}
	@Override
	protected void localize() {
		super.localize();
		if( this.declaringTypeState != null ) {
			this.declaringTypeLabelText = this.findLocalizedText( "declaringTypeLabel", DeclarationLikeSubstanceOperation.class );
		}
		if( this.valueComponentTypeState != null ) {
			this.valueTypeLabelText = this.findLocalizedText( "valueTypeLabel", DeclarationLikeSubstanceOperation.class );
		}
		if( this.nameState != null ) {
			this.nameLabelText = this.findLocalizedText( "nameLabel", DeclarationLikeSubstanceOperation.class );
		}
		if( this.initializerState != null ) {
			this.initializerLabelText = this.findLocalizedText( "initializerLabel", DeclarationLikeSubstanceOperation.class );
		}
	}

	public String getDeclaringTypeLabelText() {
		return this.declaringTypeLabelText;
	}
	public String getValueTypeLabelText() {
		return this.valueTypeLabelText;
	}
	public String getNameLabelText() {
		return this.nameLabelText;
	}
	public String getInitializerLabelText() {
		return this.initializerLabelText;
	}
	
	public DeclaringTypeState getDeclaringTypeState() {
		return this.declaringTypeState;
	}
	public ValueComponentTypeState getComponentValueTypeState() {
		return this.valueComponentTypeState;
	}
	public IsArrayValueTypeState getIsArrayState() {
		return this.isArrayValueTypeState;
	}
	public NameState getNameState() {
		return this.nameState;
	}
	public org.alice.ide.croquet.models.ExpressionState getInitializerState() {
		return this.initializerState;
	}
	
	public boolean isDeclaringTypeEditable() {
		return this.isDeclaringTypeEditable;
	}
	public boolean isValueComponentTypeEditable() {
		return this.isValueComponentTypeEditable;
	}
	public boolean isIsArrayEditable() {
		return this.isIsArrayValueTypeEditable;
	}
	public boolean isNameEditable() {
		return this.isNameEditable;
	}
	public boolean isInitializerEditable() {
		return this.isInitializerEditable;
	}
	
	public org.lgna.project.ast.UserType< ? > getDeclaringType() {
		if( this.declaringTypeState != null ) {
			return this.declaringTypeState.getValue();
		} else {
			return null;
		}
	}
	public org.lgna.project.ast.AbstractType< ?,?,? > getValueType() {
		org.lgna.project.ast.AbstractType< ?,?,? > componentType = this.valueComponentTypeState.getValue();
		if( componentType != null ) {
			if( this.isArrayValueTypeState.getValue() ) {
				return componentType.getArrayType();
			} else {
				return componentType;
			}
		} else {
			return null;
		}
	}
	public String getDeclarationName() {
		if( this.nameState != null ) {
			return this.nameState.getValue();
		} else {
			return null;
		}
	}
	public org.lgna.project.ast.Expression getInitializer() {
		if( this.initializerState != null ) {
			return this.initializerState.getValue();
		} else {
			return null;
		}
	}
	
	protected String getValueTypeExplanation( org.lgna.project.ast.AbstractType< ?,?,? > valueType ) {
		if( valueType != null ) {
			return null;
		} else {
			return "enter a valid " + this.valueTypeLabelText;
		}
	}
	protected String getNameExplanation( String declarationName ) {
		if( declarationName.length() > 0 ) {
			if( this.nameValidator != null ) {
				//todo
				if( this.nameValidator instanceof org.alice.ide.name.validators.MemberNameValidator ) {
					org.alice.ide.name.validators.MemberNameValidator memberNameValidator = (org.alice.ide.name.validators.MemberNameValidator)this.nameValidator;
					memberNameValidator.setType( this.getDeclaringType() );
				}
				return this.nameValidator.getExplanationIfOkButtonShouldBeDisabled( declarationName );
			} else {
				return null;
			}
		} else {
			return "\"" + declarationName + "\" is not a valid " + this.nameLabelText;
		}
	}
	protected boolean isNullAllowedForInitializer() {
		return false;
	}
	protected String getInitializerExplanation( org.lgna.project.ast.Expression initializer ) {
		if( initializer != null || this.isNullAllowedForInitializer() ) {
			return null;
		} else {
			return this.initializerLabelText + " must be set";
		}
	}
	@Override
	protected String getInternalExplanation( org.lgna.croquet.history.OperationStep step ) {
		final String valueTypeText;
		if( this.valueComponentTypeState != null ) {
			valueTypeText = this.getValueTypeExplanation( this.getValueType() );
		} else {
			valueTypeText = null;
		}
		final String nameText = this.getNameExplanation( this.nameState.getValue() );
		final String initializerText;
		if( this.initializerState != null ) {
			initializerText = this.getInitializerExplanation( initializerState.getValue() );
		} else {
			initializerText = null;
		}
		if( valueTypeText != null || nameText != null || initializerText != null ) {
			String preText = "";
			StringBuilder sb = new StringBuilder();
			//sb.append( "You must " );
			if( valueTypeText != null ) {
				sb.append( valueTypeText );
				preText = " AND ";
			}
			if( nameText != null ) {
				sb.append( preText );
				sb.append( nameText );
				preText = " AND ";
			}
			if( initializerText != null ) {
				sb.append( preText );
				sb.append( initializerText );
			}
			sb.append( "." );
			return sb.toString();
		} else {
			return null;
		}
	}

	public abstract T createPreviewDeclaration();

	
	private final java.util.Map< org.lgna.project.ast.AbstractType< ?,?,? >, org.lgna.project.ast.Expression > mapTypeToInitializer = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private final org.lgna.croquet.State.ValueListener< Boolean > isArrayValueTypeListener = new org.lgna.croquet.State.ValueListener< Boolean >() {
		public void changing( org.lgna.croquet.State< java.lang.Boolean > state, java.lang.Boolean prevValue, java.lang.Boolean nextValue, boolean isAdjusting ) {
//			assert state.getValue() == prevValue;
//			assert prevValue != nextValue;
			DeclarationLikeSubstanceOperation.this.handleValueTypeChanging();
		}
		public void changed( org.lgna.croquet.State< java.lang.Boolean > state, java.lang.Boolean prevValue, java.lang.Boolean nextValue, boolean isAdjusting ) {
//			assert state.getValue() == nextValue;
//			assert prevValue != nextValue;
			DeclarationLikeSubstanceOperation.this.handleValueTypeChanged();
		}
	};
	private final org.lgna.croquet.State.ValueListener< org.lgna.project.ast.AbstractType > valueComponentTypeListener = new org.lgna.croquet.State.ValueListener< org.lgna.project.ast.AbstractType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.AbstractType> state, org.lgna.project.ast.AbstractType prevValue, org.lgna.project.ast.AbstractType nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceOperation.this.handleValueTypeChanging();
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.AbstractType> state, org.lgna.project.ast.AbstractType prevValue, org.lgna.project.ast.AbstractType nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceOperation.this.handleValueTypeChanged();
		}
	};
	
	private void handleValueTypeChanging() {
		org.lgna.project.ast.AbstractType< ?,?,? > prevType = this.getValueType();
		edu.cmu.cs.dennisc.java.util.logging.Logger.info( "preserve:", prevType );
		if( prevType != null ) {
			org.lgna.project.ast.Expression prevInitializer = this.getInitializer();
			this.mapTypeToInitializer.put( prevType, prevInitializer );
		}
	}
	private void handleValueTypeChanged() {
		org.lgna.project.ast.AbstractType< ?,?,? > nextType = this.getValueType();
		edu.cmu.cs.dennisc.java.util.logging.Logger.info( "restore:", nextType );
		org.lgna.project.ast.Expression nextInitializer = this.mapTypeToInitializer.get( nextType );
		this.initializerState.setValue( nextInitializer );
	}
	
	protected abstract org.lgna.croquet.edits.Edit< ? > createEdit( org.lgna.croquet.history.OperationStep step, org.lgna.project.ast.UserType< ? > declaringType, org.lgna.project.ast.AbstractType<?,?,?> valueType, String declarationName, org.lgna.project.ast.Expression initializer );
	protected abstract org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > createMainComponent( org.lgna.croquet.history.OperationStep step );
	@Override
	protected org.alice.ide.croquet.components.declaration.DeclarationPanel< ? > prologue( org.lgna.croquet.history.OperationStep step ) {
		if( this.declaringTypeState != null ) {
			this.declaringTypeState.setValueTransactionlessly( this.initialDeclaringType );
		}
		if( this.valueComponentTypeState != null ) {
			this.valueComponentTypeState.setValueTransactionlessly( this.getInitialValueComponentType() );
		}
		if( this.isArrayValueTypeState != null ) {
			this.isArrayValueTypeState.setValueTransactionlessly( this.initialIsArrayValueType );
		}
		if( this.nameState != null ) {
			this.nameState.setValueTransactionlessly( this.initialName );
		}
		if( this.initializerState != null ) {
			//todo
			this.initializerState.setValueTransactionlessly( this.initialExpression );
		}
		
		if( this.isValueComponentTypeEditable() && this.isInitializerEditable() ) {
			if( this.isIsArrayValueTypeEditable ) {
				this.isArrayValueTypeState.addValueListener( this.isArrayValueTypeListener );
			}
			this.valueComponentTypeState.addValueListener( this.valueComponentTypeListener );
		}
		
		this.mapTypeToInitializer.clear();
		return this.createMainComponent( step );
	}
	@Override
	protected final void epilogue( org.lgna.croquet.history.OperationStep step, boolean isCommit ) {
		if( this.isValueComponentTypeEditable() && this.isInitializerEditable() ) {
			if( this.isIsArrayValueTypeEditable ) {
				this.isArrayValueTypeState.removeValueListener( this.isArrayValueTypeListener );
			}
			this.valueComponentTypeState.removeValueListener( this.valueComponentTypeListener );
		}
		if( isCommit ) {
			org.lgna.croquet.edits.Edit< ? > edit = this.createEdit( step, this.getDeclaringType(), this.getValueType(), this.getDeclarationName(), this.getInitializer() );
			if( edit != null ) {
				step.commitAndInvokeDo( edit );
			} else {
				step.cancel();
			}
		} else {
			step.cancel();
		}
	}
}
