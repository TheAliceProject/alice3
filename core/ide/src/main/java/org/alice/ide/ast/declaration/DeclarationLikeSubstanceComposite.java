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
public abstract class DeclarationLikeSubstanceComposite<N extends org.lgna.project.ast.Node> extends org.alice.ide.preview.PreviewContainingOperationInputDialogCoreComposite<org.alice.ide.ast.declaration.views.DeclarationLikeSubstanceView, N> implements org.alice.ide.croquet.models.declaration.InitializerStateOwner {
	protected static enum ApplicabilityStatus {
		EDITABLE( 3 ),
		DISPLAYED( 2 ),
		APPLICABLE_BUT_NOT_DISPLAYED( 1 ),
		NOT_APPLICABLE( 0 );
		private final int value;

		private ApplicabilityStatus( int value ) {
			this.value = value;
		}

		public boolean isEditable() {
			return this.value >= EDITABLE.value;
		}

		public boolean isDisplayed() {
			return this.value >= DISPLAYED.value;
		}

		public boolean isApplicable() {
			return this.value >= APPLICABLE_BUT_NOT_DISPLAYED.value;
		}
	}

	protected static class Details {
		private ApplicabilityStatus isFinalStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private boolean inFinalInitialValue;
		private ApplicabilityStatus valueComponentTypeStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private org.lgna.project.ast.AbstractType<?, ?, ?> valueComponentTypeInitialValue;
		private ApplicabilityStatus valueIsArrayTypeStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private boolean valueIsArrayTypeInitialValue;
		private ApplicabilityStatus nameStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private String nameInitialValue;
		private ApplicabilityStatus initializerStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private org.lgna.project.ast.Expression initializerInitialValue;

		public Details isFinal( ApplicabilityStatus status, boolean initialValue ) {
			this.isFinalStatus = status;
			this.inFinalInitialValue = initialValue;
			return this;
		}

		public Details valueComponentType( ApplicabilityStatus status, org.lgna.project.ast.AbstractType<?, ?, ?> initialValue ) {
			this.valueComponentTypeStatus = status;
			this.valueComponentTypeInitialValue = initialValue;
			return this;
		}

		public Details valueIsArrayType( ApplicabilityStatus status, boolean initialValue ) {
			this.valueIsArrayTypeStatus = status;
			this.valueIsArrayTypeInitialValue = initialValue;
			return this;
		}

		public Details name( ApplicabilityStatus status, String initialValue ) {
			this.nameStatus = status;
			this.nameInitialValue = initialValue;
			return this;
		}

		public Details name( ApplicabilityStatus status ) {
			return this.name( status, "" );
		}

		public Details initializer( ApplicabilityStatus status, org.lgna.project.ast.Expression initialValue ) {
			this.initializerStatus = status;
			this.initializerInitialValue = initialValue;
			return this;
		}
	}

	private final org.lgna.croquet.BooleanState isFinalState;
	private final org.lgna.croquet.CustomItemState<org.lgna.project.ast.AbstractType> valueComponentTypeState;
	private final org.lgna.croquet.BooleanState valueIsArrayTypeState;
	private final org.lgna.croquet.StringState nameState;
	private final org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> initializerState;

	private final ErrorStatus errorStatus = this.createErrorStatus( "errorStatus" );

	private final Details details;

	private static class ValueComponentTypeCustomizer implements ItemStateCustomizer<org.lgna.project.ast.AbstractType> {
		@Override
		public org.lgna.croquet.CascadeFillIn getFillInFor( org.lgna.project.ast.AbstractType type ) {
			return org.alice.ide.croquet.models.ast.declaration.TypeFillIn.getInstance( type );
		}

		@Override
		public void prologue( org.lgna.croquet.triggers.Trigger trigger ) {
		}

		@Override
		public void epilogue() {
		}

		private void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.project.ast.NamedUserType programType, edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> node ) {
			org.lgna.project.ast.NamedUserType type = node.getValue();
			if( type != null ) {
				final boolean IS_PROGRAM_TYPE_EVER_A_GOOD_IDEA_TO_INCLUDE = false;
				boolean isProgramTypeIncluded = IS_PROGRAM_TYPE_EVER_A_GOOD_IDEA_TO_INCLUDE && org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType.getInstance().getValue();
				if( isProgramTypeIncluded || ( type != programType ) ) {
					blankChildren.add( this.getFillInFor( type ) );
				}
			}
			for( edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> child : node.getChildren() ) {
				appendBlankChildren( blankChildren, programType, child );
			}
		}

		@Override
		public void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.AbstractType> blankNode ) {
			for( org.lgna.project.ast.JavaType type : org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getPrimeTimeSelectableJavaTypes() ) {
				blankChildren.add( this.getFillInFor( type ) );
			}
			blankChildren.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			org.lgna.project.Project project = org.alice.ide.IDE.getActiveInstance().getProject();

			//org.lgna.project.ast.NamedUserType programType = project.getProgramType();
			//edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> root = org.lgna.project.ProgramTypeUtilities.getNamedUserTypesAsTree( project );
			//appendBlankChildren( blankChildren, programType, root );

			blankChildren.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			blankChildren.add( org.alice.stageide.type.croquet.OtherTypeDialog.getInstance().getValueCreator( org.lgna.story.SThing.class ).getFillIn() );
			org.alice.ide.croquet.models.ast.declaration.OtherTypesMenuModel otherTypesMenuModel = org.alice.ide.croquet.models.ast.declaration.OtherTypesMenuModel.getInstance();
			if( otherTypesMenuModel.isEmpty() ) {
				//pass
			} else {
				blankChildren.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
				blankChildren.add( otherTypesMenuModel );
			}
		}
	}

	public DeclarationLikeSubstanceComposite( java.util.UUID migrationId, Details details ) {
		super( migrationId, org.lgna.croquet.Application.PROJECT_GROUP );

		this.details = details;

		if( details.isFinalStatus.isApplicable() ) {
			this.isFinalState = this.createBooleanState( "isFinalState", details.inFinalInitialValue );
			if( details.isFinalStatus.isDisplayed() ) {
				this.isFinalState.setEnabled( details.valueComponentTypeStatus.isEditable() );
			}
		} else {
			this.isFinalState = null;
		}

		if( details.valueComponentTypeStatus.isApplicable() ) {
			this.valueComponentTypeState = this.createCustomItemState( "valueComponentTypeState", org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.AbstractType.class ), details.valueComponentTypeInitialValue, new ValueComponentTypeCustomizer() );
			if( details.valueComponentTypeStatus.isDisplayed() ) {
				this.valueComponentTypeState.setEnabled( details.valueComponentTypeStatus.isEditable() );
			}
		} else {
			this.valueComponentTypeState = null;
		}

		if( details.valueIsArrayTypeStatus.isApplicable() ) {
			this.valueIsArrayTypeState = this.createBooleanState( "valueIsArrayTypeState", details.valueIsArrayTypeInitialValue );
			if( details.valueIsArrayTypeStatus.isDisplayed() ) {
				this.valueIsArrayTypeState.setEnabled( details.valueIsArrayTypeStatus.isEditable() );
			}
		} else {
			this.valueIsArrayTypeState = null;
		}

		if( details.nameStatus.isApplicable() ) {
			this.nameState = this.createStringState( "nameState", details.nameInitialValue );
			if( details.nameStatus.isDisplayed() ) {
				this.nameState.setEnabled( details.nameStatus.isEditable() );
			}
		} else {
			this.nameState = null;
		}

		if( details.initializerStatus.isApplicable() ) {
			this.initializerState = this.createInitializerState( details.initializerInitialValue );
			if( details.initializerStatus.isDisplayed() ) {
				this.initializerState.setEnabled( details.initializerStatus.isEditable() );
			}
		} else {
			this.initializerState = null;
		}
	}

	private class InitializerCustomizer implements ItemStateCustomizer<org.lgna.project.ast.Expression> {
		@Override
		public org.lgna.croquet.CascadeFillIn getFillInFor( org.lgna.project.ast.Expression value ) {
			//todo
			if( value instanceof org.lgna.project.ast.ArrayInstanceCreation ) {
				org.lgna.project.ast.ArrayInstanceCreation arrayInstanceCreation = (org.lgna.project.ast.ArrayInstanceCreation)value;
				return org.alice.ide.custom.ArrayCustomExpressionCreatorComposite.getInstance( arrayInstanceCreation.getType() ).getValueCreator().getFillIn();
			} else {
				return null;
			}
		}

		@Override
		public void prologue( org.lgna.croquet.triggers.Trigger trigger ) {
		}

		@Override
		public void epilogue() {
		}

		@Override
		public void appendBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
			org.lgna.project.annotations.ValueDetails valueDetails = null;
			org.lgna.project.ast.AbstractType<?, ?, ?> type = DeclarationLikeSubstanceComposite.this.getValueType();
			org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().appendItems( blankChildren, blankNode, type, valueDetails );
		}
	}

	protected ItemStateCustomizer<org.lgna.project.ast.Expression> createInitializerCustomizer() {
		return new InitializerCustomizer();
	}

	protected final org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> createInitializerState( org.lgna.project.ast.Expression initialValue ) {
		return this.createCustomItemState( "initializerState", org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Expression.class ), initialValue, this.createInitializerCustomizer() );
	}

	public org.lgna.croquet.BooleanState getIsFinalState() {
		return this.isFinalState;
	}

	public org.lgna.croquet.CustomItemState<org.lgna.project.ast.AbstractType> getValueComponentTypeState() {
		return this.valueComponentTypeState;
	}

	public org.lgna.croquet.BooleanState getValueIsArrayTypeState() {
		return this.valueIsArrayTypeState;
	}

	public org.lgna.croquet.StringState getNameState() {
		return this.nameState;
	}

	public org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> getInitializerState() {
		return this.initializerState;
	}

	public boolean isValueComponentTypeDisplayed() {
		return details.valueComponentTypeStatus.isDisplayed();
	}

	public boolean isValueIsArrayTypeStateDisplayed() {
		return details.valueIsArrayTypeStatus.isDisplayed();
	}

	public boolean isInitializerDisplayed() {
		return details.initializerStatus.isDisplayed();
	}

	public abstract org.lgna.project.ast.UserType<?> getDeclaringType();

	public org.lgna.project.ast.AbstractType<?, ?, ?> getValueComponentType() {
		if( this.valueComponentTypeState != null ) {
			return this.valueComponentTypeState.getValue();
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.project.ast.AbstractType<?, ?, ?> getValueType() {
		org.lgna.project.ast.AbstractType<?, ?, ?> componentType = this.getValueComponentType();
		if( componentType != null ) {
			if( ( this.valueIsArrayTypeState != null ) && this.valueIsArrayTypeState.getValue() ) {
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
			org.lgna.project.ast.Expression rv = this.initializerState.getValue();
			if( rv != null ) {
				//pass
			} else {
				rv = new org.lgna.project.ast.NullLiteral();
			}
			return rv;
		} else {
			return null;
		}
	}

	protected String getValueTypeExplanation( org.lgna.project.ast.AbstractType<?, ?, ?> valueType ) {
		if( valueType != null ) {
			return null;
		} else {
			return this.valueComponentTypeState.getSidekickLabel().getText().replaceAll( ":", "" ) + " must be set";
		}
	}

	protected final boolean isNameValid( String name ) {
		if( org.alice.ide.preferences.recursion.IsIdentifierNameValidityStrictState.getInstance().getValue() ) {
			return org.lgna.project.ast.StaticAnalysisUtilities.isValidIdentifier( name );
		} else {
			return ( name != null ) && ( name.length() > 0 );
		}
	}

	protected abstract boolean isNameAvailable( String name );

	protected String getNameExplanation( String declarationName ) {
		if( declarationName.length() > 0 ) {
			if( this.isNameValid( declarationName ) ) {
				if( this.isNameAvailable( declarationName ) ) {
					return null;
				} else {
					return "\"" + declarationName + "\"" + " is not available.";
				}
			} else {
				return "\"" + declarationName + "\"" + " is not a valid name.";
			}
		} else {
			return "\"" + declarationName + "\" is not a valid " + this.nameState.getSidekickLabel().getText().replaceAll( ":", "" );
		}
	}

	private boolean isNullAllowedForInitializerUnderAnyCircumstances() {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = this.getValueType();
		if( type != null ) {
			if( type.isArray() ) {
				return false;
			} else {
				if( type.isPrimitive() || org.lgna.project.ast.JavaType.isWrapperType( type ) ) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			return false;
		}
	}

	protected boolean isNullAllowedForInitializer() {
		return false;
	}

	protected String getInitializerExplanation( org.lgna.project.ast.Expression initializer ) {
		if( ( initializer != null ) || ( this.isNullAllowedForInitializerUnderAnyCircumstances() && this.isNullAllowedForInitializer() ) ) {
			return null;
		} else {
			return this.initializerState.getSidekickLabel().getText().replaceAll( ":", "" ) + " must be set";
		}
	}

	@Override
	protected org.lgna.croquet.AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		final String valueTypeText;
		if( this.valueComponentTypeState != null ) {
			valueTypeText = this.getValueTypeExplanation( this.getValueType() );
		} else {
			valueTypeText = null;
		}
		final String nameText;
		if( this.isNameEditable() ) {
			nameText = this.getNameExplanation( this.nameState.getValue() );
		} else {
			nameText = null;
		}
		final String initializerText;
		if( this.initializerState != null ) {
			initializerText = this.getInitializerExplanation( initializerState.getValue() );
		} else {
			initializerText = null;
		}
		if( ( valueTypeText != null ) || ( nameText != null ) || ( initializerText != null ) ) {
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
			this.errorStatus.setText( sb.toString() );
			return this.errorStatus;
		} else {
			return IS_GOOD_TO_GO_STATUS;
		}
	}

	private boolean isValueComponentTypeEditable() {
		return this.valueComponentTypeState != null ? this.valueComponentTypeState.isEnabled() : false;
	}

	private boolean isValueIsArrayTypeEditable() {
		return this.valueIsArrayTypeState != null ? this.valueIsArrayTypeState.isEnabled() : false;
	}

	private boolean isNameEditable() {
		return this.nameState != null ? this.nameState.isEnabled() : false;
	}

	private boolean isInitializerEditable() {
		return this.initializerState != null ? this.initializerState.isEnabled() : false;
	}

	private final java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, org.lgna.project.ast.Expression> mapTypeToInitializer = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final org.lgna.croquet.State.ValueListener<Boolean> isArrayValueTypeListener = new org.lgna.croquet.State.ValueListener<Boolean>() {
		@Override
		public void changing( org.lgna.croquet.State<java.lang.Boolean> state, java.lang.Boolean prevValue, java.lang.Boolean nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanging();
		}

		@Override
		public void changed( org.lgna.croquet.State<java.lang.Boolean> state, java.lang.Boolean prevValue, java.lang.Boolean nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanged();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.project.ast.AbstractType> valueComponentTypeListener = new org.lgna.croquet.State.ValueListener<org.lgna.project.ast.AbstractType>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.project.ast.AbstractType> state, org.lgna.project.ast.AbstractType prevValue, org.lgna.project.ast.AbstractType nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanging();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.project.ast.AbstractType> state, org.lgna.project.ast.AbstractType prevValue, org.lgna.project.ast.AbstractType nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanged();
		}
	};
	private final org.lgna.croquet.event.ValueListener<org.lgna.project.ast.Expression> initializerListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.Expression>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.Expression> e ) {
			DeclarationLikeSubstanceComposite.this.getView().handleInitializerChanged( e.getNextValue() );
		}
	};

	private void handleValueTypeChanging() {
		org.lgna.project.ast.AbstractType<?, ?, ?> prevType = this.getValueType();
		edu.cmu.cs.dennisc.java.util.logging.Logger.info( "preserve:", prevType );
		if( prevType != null ) {
			org.lgna.project.ast.Expression prevInitializer = this.getInitializer();
			this.mapTypeToInitializer.put( prevType, prevInitializer );
		}
	}

	private void handleValueTypeChanged() {
		if( this.initializerState != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> nextType = this.getValueType();
			edu.cmu.cs.dennisc.java.util.logging.Logger.info( "restore:", nextType );
			org.lgna.project.ast.Expression nextInitializer = this.mapTypeToInitializer.get( nextType );
			this.initializerState.setValueTransactionlessly( nextInitializer );
		}
	}

	protected boolean getIsFinalInitialValue() {
		return this.details.inFinalInitialValue;
	}

	//perhaps for InitializerManagedFieldDeclaration
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
		return this.details.valueComponentTypeInitialValue;
	}

	protected String getNameInitialValue() {
		return this.details.nameInitialValue;
	}

	protected boolean getValueIsArrayTypeInitialValue() {
		return this.details.valueIsArrayTypeInitialValue;
	}

	protected org.lgna.project.ast.Expression getInitializerInitialValue() {
		return this.details.initializerInitialValue;
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.isFinalState != null ) {
			boolean isFinal = this.getIsFinalInitialValue();
			this.isFinalState.setValueTransactionlessly( isFinal );
			if( details.isFinalStatus == ApplicabilityStatus.DISPLAYED ) {
				org.lgna.croquet.Operation trueOperation = this.isFinalState.getSetToTrueOperation();
				org.lgna.croquet.Operation falseOperation = this.isFinalState.getSetToFalseOperation();
				trueOperation.setEnabled( isFinal );
				falseOperation.setEnabled( isFinal == false );
			}
		}
		if( this.initializerState != null ) {
			//todo
			this.initializerState.setValueTransactionlessly( this.getInitializerInitialValue() );
		}

		if( this.valueComponentTypeState != null ) {
			this.valueComponentTypeState.setValueTransactionlessly( this.getValueComponentTypeInitialValue() );
		}
		if( this.valueIsArrayTypeState != null ) {
			this.valueIsArrayTypeState.setValueTransactionlessly( this.getValueIsArrayTypeInitialValue() );
		}
		if( this.nameState != null ) {
			this.nameState.setValueTransactionlessly( this.getNameInitialValue() );
		}

		if( this.isValueComponentTypeEditable() || this.isInitializerEditable() ) {
			if( this.isValueIsArrayTypeEditable() ) {
				this.valueIsArrayTypeState.addValueListener( this.isArrayValueTypeListener );
			}
			this.valueComponentTypeState.addValueListener( this.valueComponentTypeListener );
		}

		this.mapTypeToInitializer.clear();

		if( this.isInitializerEditable() ) {
			this.initializerState.addNewSchoolValueListener( this.initializerListener );
		}
		this.getView().handleInitializerChanged( this.getInitializer() );
		super.handlePreShowDialog( step );
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		super.handlePostHideDialog( completionStep );
		if( this.isInitializerEditable() ) {
			this.initializerState.removeNewSchoolValueListener( this.initializerListener );
		}
		if( this.isValueComponentTypeEditable() || this.isInitializerEditable() ) {
			if( this.isValueIsArrayTypeEditable() ) {
				this.valueIsArrayTypeState.removeValueListener( this.isArrayValueTypeListener );
			}
			this.valueComponentTypeState.removeValueListener( this.valueComponentTypeListener );
		}
	}
}
