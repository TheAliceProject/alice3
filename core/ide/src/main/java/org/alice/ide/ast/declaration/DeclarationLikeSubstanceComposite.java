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

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.tree.DefaultNode;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.views.DeclarationLikeSubstanceView;
import org.alice.ide.croquet.codecs.NodeCodec;
import org.alice.ide.croquet.models.ast.declaration.OtherTypesMenuModel;
import org.alice.ide.croquet.models.ast.declaration.TypeFillIn;
import org.alice.ide.croquet.models.declaration.InitializerStateOwner;
import org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType;
import org.alice.ide.custom.ArrayCustomExpressionCreatorComposite;
import org.alice.ide.preferences.recursion.IsIdentifierNameValidityStrictState;
import org.alice.ide.preview.PreviewContainingOperationInputDialogCoreComposite;
import org.alice.stageide.type.croquet.OtherTypeDialog;
import org.lgna.croquet.AbstractSeverityStatusComposite;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeFillIn;
import org.lgna.croquet.CascadeLineSeparator;
import org.lgna.croquet.CustomItemState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.State;
import org.lgna.croquet.StringState;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.croquet.views.Dialog;
import org.lgna.project.Project;
import org.lgna.project.annotations.ValueDetails;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArrayInstanceCreation;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.StaticAnalysisUtilities;
import org.lgna.project.ast.UserType;
import org.lgna.story.SThing;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class DeclarationLikeSubstanceComposite<N extends Node> extends PreviewContainingOperationInputDialogCoreComposite<DeclarationLikeSubstanceView, N> implements InitializerStateOwner {
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
		private AbstractType<?, ?, ?> valueComponentTypeInitialValue;
		private ApplicabilityStatus valueIsArrayTypeStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private boolean valueIsArrayTypeInitialValue;
		private ApplicabilityStatus nameStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private String nameInitialValue;
		private ApplicabilityStatus initializerStatus = ApplicabilityStatus.NOT_APPLICABLE;
		private Expression initializerInitialValue;

		public Details isFinal( ApplicabilityStatus status, boolean initialValue ) {
			this.isFinalStatus = status;
			this.inFinalInitialValue = initialValue;
			return this;
		}

		public Details valueComponentType( ApplicabilityStatus status, AbstractType<?, ?, ?> initialValue ) {
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

		public Details initializer( ApplicabilityStatus status, Expression initialValue ) {
			this.initializerStatus = status;
			this.initializerInitialValue = initialValue;
			return this;
		}
	}

	private final BooleanState isFinalState;
	private final CustomItemState<AbstractType> valueComponentTypeState;
	private final BooleanState valueIsArrayTypeState;
	private final StringState nameState;
	private final CustomItemState<Expression> initializerState;

	private final ErrorStatus errorStatus = this.createErrorStatus( "errorStatus" );

	private final Details details;

	private static class ValueComponentTypeCustomizer implements ItemStateCustomizer<AbstractType> {
		@Override
		public CascadeFillIn getFillInFor( AbstractType type ) {
			return TypeFillIn.getInstance( type );
		}

		@Override
		public void prologue() {
		}

		@Override
		public void epilogue() {
		}

		private void appendBlankChildren( List<CascadeBlankChild> blankChildren, NamedUserType programType, DefaultNode<NamedUserType> node ) {
			NamedUserType type = node.getValue();
			if( type != null ) {
				final boolean IS_PROGRAM_TYPE_EVER_A_GOOD_IDEA_TO_INCLUDE = false;
				boolean isProgramTypeIncluded = IS_PROGRAM_TYPE_EVER_A_GOOD_IDEA_TO_INCLUDE && IsIncludingProgramType.getInstance().getValue();
				if( isProgramTypeIncluded || ( type != programType ) ) {
					blankChildren.add( this.getFillInFor( type ) );
				}
			}
			for( DefaultNode<NamedUserType> child : node.getChildren() ) {
				appendBlankChildren( blankChildren, programType, child );
			}
		}

		@Override
		public void appendBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<AbstractType> blankNode ) {
			for( JavaType type : IDE.getActiveInstance().getApiConfigurationManager().getPrimeTimeSelectableJavaTypes() ) {
				blankChildren.add( this.getFillInFor( type ) );
			}
			blankChildren.add( CascadeLineSeparator.getInstance() );
			Project project = IDE.getActiveInstance().getProject();

			//org.lgna.project.ast.NamedUserType programType = project.getProgramType();
			//edu.cmu.cs.dennisc.tree.DefaultNode<org.lgna.project.ast.NamedUserType> root = org.lgna.project.ProgramTypeUtilities.getNamedUserTypesAsTree( project );
			//appendBlankChildren( blankChildren, programType, root );

			blankChildren.add( CascadeLineSeparator.getInstance() );
			blankChildren.add( OtherTypeDialog.getInstance().getValueCreator( SThing.class ).getFillIn() );
			OtherTypesMenuModel otherTypesMenuModel = OtherTypesMenuModel.getInstance();
			if( otherTypesMenuModel.isEmpty() ) {
				//pass
			} else {
				blankChildren.add( CascadeLineSeparator.getInstance() );
				blankChildren.add( otherTypesMenuModel );
			}
		}
	}

	public DeclarationLikeSubstanceComposite( UUID migrationId, Details details ) {
		super( migrationId, Application.PROJECT_GROUP );

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
			this.valueComponentTypeState = this.createCustomItemState( "valueComponentTypeState", NodeCodec.getInstance( AbstractType.class ), details.valueComponentTypeInitialValue, new ValueComponentTypeCustomizer() );
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

	private class InitializerCustomizer implements ItemStateCustomizer<Expression> {
		@Override
		public CascadeFillIn getFillInFor( Expression value ) {
			//todo
			if( value instanceof ArrayInstanceCreation ) {
				ArrayInstanceCreation arrayInstanceCreation = (ArrayInstanceCreation)value;
				return ArrayCustomExpressionCreatorComposite.getInstance( arrayInstanceCreation.getType() ).getValueCreator().getFillIn();
			} else {
				return null;
			}
		}

		@Override
		public void prologue() {
		}

		@Override
		public void epilogue() {
		}

		@Override
		public void appendBlankChildren( List<CascadeBlankChild> blankChildren, BlankNode<Expression> blankNode ) {
			ValueDetails valueDetails = null;
			AbstractType<?, ?, ?> type = DeclarationLikeSubstanceComposite.this.getValueType();
			IDE.getActiveInstance().getExpressionCascadeManager().appendItems( blankChildren, blankNode, type, valueDetails );
		}
	}

	protected ItemStateCustomizer<Expression> createInitializerCustomizer() {
		return new InitializerCustomizer();
	}

	protected final CustomItemState<Expression> createInitializerState( Expression initialValue ) {
		return this.createCustomItemState( "initializerState", NodeCodec.getInstance( Expression.class ), initialValue, this.createInitializerCustomizer() );
	}

	public BooleanState getIsFinalState() {
		return this.isFinalState;
	}

	public CustomItemState<AbstractType> getValueComponentTypeState() {
		return this.valueComponentTypeState;
	}

	public BooleanState getValueIsArrayTypeState() {
		return this.valueIsArrayTypeState;
	}

	public StringState getNameState() {
		return this.nameState;
	}

	public CustomItemState<Expression> getInitializerState() {
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

	public abstract UserType<?> getDeclaringType();

	public AbstractType<?, ?, ?> getValueComponentType() {
		if( this.valueComponentTypeState != null ) {
			return this.valueComponentTypeState.getValue();
		} else {
			return null;
		}
	}

	@Override
	public AbstractType<?, ?, ?> getValueType() {
		AbstractType<?, ?, ?> componentType = this.getValueComponentType();
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

	public Expression getInitializer() {
		if( this.initializerState != null ) {
			Expression rv = this.initializerState.getValue();
			if( rv != null ) {
				//pass
			} else {
				rv = new NullLiteral();
			}
			return rv;
		} else {
			return null;
		}
	}

	protected String getValueTypeExplanation( AbstractType<?, ?, ?> valueType ) {
		if( valueType != null ) {
			return null;
		} else {
			String mustBeSetTest = this.findLocalizedText( "mustBeSet" );
			return mustBeSetTest.replaceAll( "</type/>", this.valueComponentTypeState.getSidekickLabel().getText().replaceAll( ":", "" ) );
		}
	}

	protected final boolean isNameValid( String name ) {
		if( IsIdentifierNameValidityStrictState.getInstance().getValue() ) {
			return StaticAnalysisUtilities.isValidIdentifier( name );
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
					String notAvalableText = this.findLocalizedText( "isNotAvailable" );
					return notAvalableText.replaceAll( "</name/>", "\"" + declarationName + "\"" );
				}
			} else {
				String notValidNameText = this.findLocalizedText( "isNotAValidName" );
				return notValidNameText.replaceAll( "</name/>", "\"" + declarationName + "\"" );
			}
		} else {
			String notValidText = this.findLocalizedText( "isNotValid" );
			notValidText = notValidText.replaceAll( "</name/>", "\"" + declarationName + "\"" );
			return notValidText.replaceAll( "</type/>", this.nameState.getSidekickLabel().getText().replaceAll( ":", "" ) );
		}
	}

	private boolean isNullAllowedForInitializerUnderAnyCircumstances() {
		AbstractType<?, ?, ?> type = this.getValueType();
		if( type != null ) {
			if( type.isArray() ) {
				return false;
			} else {
				if( type.isPrimitive() || JavaType.isWrapperType( type ) ) {
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

	protected String getInitializerExplanation( Expression initializer ) {
		if( ( initializer != null ) || ( this.isNullAllowedForInitializerUnderAnyCircumstances() && this.isNullAllowedForInitializer() ) ) {
			return null;
		} else {
			String mustBeSetTest = this.findLocalizedText( "mustBeSet" );
			return mustBeSetTest.replaceAll( "</type/>", this.initializerState.getSidekickLabel().getText().replaceAll( ":", "" ) );
		}
	}

	@Override
	protected AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck() {
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

	private final Map<AbstractType<?, ?, ?>, Expression> mapTypeToInitializer = Maps.newHashMap();
	private final State.ValueListener<Boolean> isArrayValueTypeListener = new State.ValueListener<Boolean>() {
		@Override
		public void changing( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanging();
		}

		@Override
		public void changed( State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanged();
		}
	};
	private final State.ValueListener<AbstractType> valueComponentTypeListener = new State.ValueListener<AbstractType>() {
		@Override
		public void changing( State<AbstractType> state, AbstractType prevValue, AbstractType nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanging();
		}

		@Override
		public void changed( State<AbstractType> state, AbstractType prevValue, AbstractType nextValue, boolean isAdjusting ) {
			DeclarationLikeSubstanceComposite.this.handleValueTypeChanged();
		}
	};
	private final ValueListener<Expression> initializerListener = new ValueListener<Expression>() {
		@Override
		public void valueChanged( ValueEvent<Expression> e ) {
			DeclarationLikeSubstanceComposite.this.getView().handleInitializerChanged( e.getNextValue() );
		}
	};

	private void handleValueTypeChanging() {
		AbstractType<?, ?, ?> prevType = this.getValueType();
		Logger.info( "preserve:", prevType );
		if( prevType != null ) {
			Expression prevInitializer = this.getInitializer();
			this.mapTypeToInitializer.put( prevType, prevInitializer );
		}
	}

	private void handleValueTypeChanged() {
		if( this.initializerState != null ) {
			AbstractType<?, ?, ?> nextType = this.getValueType();
			Logger.info( "restore:", nextType );
			Expression nextInitializer = this.mapTypeToInitializer.get( nextType );
			this.initializerState.setValueTransactionlessly( nextInitializer );
		}
	}

	protected boolean getIsFinalInitialValue() {
		return this.details.inFinalInitialValue;
	}

	//perhaps for InitializerManagedFieldDeclaration
	protected AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
		return this.details.valueComponentTypeInitialValue;
	}

	protected String getNameInitialValue() {
		return this.details.nameInitialValue;
	}

	protected boolean getValueIsArrayTypeInitialValue() {
		return this.details.valueIsArrayTypeInitialValue;
	}

	protected Expression getInitializerInitialValue() {
		return this.details.initializerInitialValue;
	}

	@Override
	protected void handlePreShowDialog( Dialog dialog ) {
		if( this.isFinalState != null ) {
			boolean isFinal = this.getIsFinalInitialValue();
			this.isFinalState.setValueTransactionlessly( isFinal );
			if( details.isFinalStatus == ApplicabilityStatus.DISPLAYED ) {
				Operation trueOperation = this.isFinalState.getSetToTrueOperation();
				Operation falseOperation = this.isFinalState.getSetToFalseOperation();
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
		super.handlePreShowDialog( dialog );
	}

	@Override
	protected void handlePostHideDialog() {
		super.handlePostHideDialog();
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
