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
public abstract class DeclarationLikeSubstanceComposite<N extends org.lgna.project.ast.Node> extends org.alice.ide.preview.PreviewContainingOperationInputDialogCoreComposite<org.alice.ide.ast.declaration.views.DeclarationLikeSubstanceView,N> implements org.alice.ide.croquet.models.declaration.InitializerStateOwner {
	protected static enum Status {
		APPLICABLE_AND_EDITBLE( true, true ),
		APPLICABLE_BUT_NOT_EDITABLE( true, false ),
		NOT_APPLICABLE( false, false );
		private final boolean isApplicable;
		private final boolean isEditable;
		private Status( boolean isApplicable, boolean isEditable ) {
			this.isApplicable = isApplicable;
			this.isEditable = isEditable;
		}
		public boolean isApplicable() {
			return this.isApplicable;
		}
		public boolean isEditable() {
			return this.isEditable;
		}
	}
	protected static class Details {
		private Status declarationTypeStatus = Status.NOT_APPLICABLE;
		private org.lgna.project.ast.UserType<?> declarationTypeInitialValue;
		private Status valueComponentTypeStatus = Status.NOT_APPLICABLE;
		private org.lgna.project.ast.AbstractType<?,?,?> valueComponentTypeInitialValue;
		private Status valueIsArrayTypeStatus = Status.NOT_APPLICABLE;
		private boolean valueIsArrayTypeInitialValue;
		private Status nameStatus = Status.NOT_APPLICABLE;
		private String nameInitialValue;
		private Status initializerStatus = Status.NOT_APPLICABLE;
		private org.lgna.project.ast.Expression initializerInitialValue;

		public Details declarationType( Status status, org.lgna.project.ast.UserType<?> initialValue ) {
			this.declarationTypeStatus = status;
			this.declarationTypeInitialValue = initialValue;
			return this;
		}
		public Details valueComponentType( Status status, org.lgna.project.ast.AbstractType<?,?,?> initialValue ) {
			this.valueComponentTypeStatus = status;
			this.valueComponentTypeInitialValue = initialValue;
			return this;
		}
		public Details valueIsArrayType( Status status, boolean initialValue ) {
			this.valueIsArrayTypeStatus = status;
			this.valueIsArrayTypeInitialValue = initialValue;
			return this;
		}
		public Details name( Status status, String initialValue ) {
			this.nameStatus = status;
			this.nameInitialValue = initialValue;
			return this;
		}
		public Details name( Status status ) {
			return this.name( status, "" );
		}
		public Details initializer( Status status, org.lgna.project.ast.Expression initialValue ) {
			this.initializerStatus = status;
			this.initializerInitialValue = initialValue;
			return this;
		}
	}
	private final org.alice.ide.croquet.models.declaration.DeclaringTypeState declaringTypeState;
	private final org.alice.ide.croquet.models.declaration.ValueComponentTypeState valueComponentTypeState;
	private final org.lgna.croquet.BooleanState valueIsArrayTypeState;
	private final org.lgna.croquet.StringState nameState;
	private final org.alice.ide.croquet.models.ExpressionState initializerState;
	public DeclarationLikeSubstanceComposite( java.util.UUID migrationId, Details details ) {
		super( migrationId, org.alice.ide.IDE.PROJECT_GROUP );
		if( details.declarationTypeStatus.isApplicable() ) {
			this.declaringTypeState = new org.alice.ide.croquet.models.declaration.DeclaringTypeState( details.declarationTypeInitialValue );
			this.declaringTypeState.setEnabled( details.declarationTypeStatus.isEditable() );
		} else {
			this.declaringTypeState = null;
		}

		if( details.valueComponentTypeStatus.isApplicable() ) {
			this.valueComponentTypeState = new org.alice.ide.croquet.models.declaration.ValueComponentTypeState( details.valueComponentTypeInitialValue );
			this.valueComponentTypeState.setEnabled( details.valueComponentTypeStatus.isEditable() );
		} else {
			this.valueComponentTypeState = null;
		}

		if( details.valueIsArrayTypeStatus.isApplicable() ) {
			this.valueIsArrayTypeState = this.createBooleanState( this.createKey( "valueIsArrayTypeState" ), details.valueIsArrayTypeInitialValue );
			this.valueIsArrayTypeState.setEnabled( details.valueIsArrayTypeStatus.isEditable() );
		} else {
			this.valueIsArrayTypeState = null;
		}
		
		if( details.nameStatus.isApplicable() ) {
			this.nameState = this.createStringState( this.createKey( "nameState" ), details.nameInitialValue );
			this.nameState.setEnabled( details.nameStatus.isEditable() );
		} else {
			this.nameState = null;
		}

		if( details.initializerStatus.isApplicable() ) {
			this.initializerState = this.createInitializerState( details.initializerInitialValue );
			this.initializerState.setEnabled( details.initializerStatus.isEditable() );
		} else {
			this.initializerState = null;
		}
	}
	protected org.alice.ide.croquet.models.ExpressionState createInitializerState( org.lgna.project.ast.Expression initialValue ) {
		return new org.alice.ide.croquet.models.declaration.InitializerState( this, initialValue );
	}
	
	@Override
	protected void localize() {
		super.localize();
		if( this.valueComponentTypeState != null ) {
			String text = this.findLocalizedText( "valueComponentTypeState.sidekickLabel", org.lgna.croquet.AbstractComposite.class );
			if( text != null ) {
				org.lgna.croquet.StringValue sidekickLabel = this.valueComponentTypeState.getSidekickLabel();
				sidekickLabel.setText( text );
			}
		}
		if( this.initializerState != null ) {
			String text = this.findLocalizedText( "initializerState.sidekickLabel", org.lgna.croquet.AbstractComposite.class );
			if( text != null ) {
				org.lgna.croquet.StringValue sidekickLabel = this.initializerState.getSidekickLabel();
				sidekickLabel.setText( text );
			}
		}
	}

	public org.alice.ide.croquet.models.declaration.DeclaringTypeState getDeclaringTypeState() {
		return this.declaringTypeState;
	}
	public org.alice.ide.croquet.models.declaration.ValueComponentTypeState getValueComponentTypeState() {
		return this.valueComponentTypeState;
	}
	public org.lgna.croquet.BooleanState getValueIsArrayTypeState() {
		return this.valueIsArrayTypeState;
	}
	public org.lgna.croquet.StringState getNameState() {
		return this.nameState;
	}
	public org.alice.ide.croquet.models.ExpressionState getInitializerState() {
		return this.initializerState;
	}
	
	public org.lgna.project.ast.UserType< ? > getDeclaringType() {
		if( this.declaringTypeState != null ) {
			return this.declaringTypeState.getValue();
		} else {
			return null;
		}
	}
	public org.lgna.project.ast.AbstractType<?,?,?> getValueComponentType() {
		if( this.valueComponentTypeState != null ) {
			return this.valueComponentTypeState.getValue();
		} else {
			return null;
		}
	}
	public org.lgna.project.ast.AbstractType<?,?,?> getValueType() {
		org.lgna.project.ast.AbstractType< ?,?,? > componentType = this.getValueComponentType();
		if( componentType != null ) {
			if( this.valueIsArrayTypeState.getValue() ) {
				return componentType.getArrayType();
			} else {
				return componentType;
			}
		} else {
			return null;
		}
	}
	public String getDeclarationLikeSubstanceName() {
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
	
	@Override
	protected org.lgna.croquet.PotentiallyGatedComposite.Status getStatus( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}
}
