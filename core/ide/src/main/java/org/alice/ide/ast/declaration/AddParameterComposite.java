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
public final class AddParameterComposite extends DeclarationLikeSubstanceComposite<org.lgna.project.ast.UserParameter> {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.UserCode, AddParameterComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static AddParameterComposite getInstance( org.lgna.project.ast.UserCode code ) {
		return map.getInitializingIfAbsent( code, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.UserCode, AddParameterComposite>() {
			@Override
			public org.alice.ide.ast.declaration.AddParameterComposite initialize( org.lgna.project.ast.UserCode key ) {
				return new AddParameterComposite( key );
			}
		} );
	}

	private final org.lgna.croquet.BooleanState isRequirementToUpdateInvocationsUnderstoodState = this.createBooleanState( "isRequirementToUpdateInvocationsUnderstoodState", false );
	private final ErrorStatus hasNotAgreedToUpdateInvocationsStatus = this.createErrorStatus( "hasNotAgreedToUpdateInvocationsStatus" );
	private final org.lgna.project.ast.UserCode code;
	//todo: remove
	private final org.alice.ide.name.validators.ParameterNameValidator parameterNameValidator;

	private AddParameterComposite( org.lgna.project.ast.UserCode code ) {
		super( java.util.UUID.fromString( "628f8e97-84b5-480c-8f05-d69749a4203e" ), new Details()
				.valueComponentType( ApplicabilityStatus.EDITABLE, null )
				.valueIsArrayType( ApplicabilityStatus.EDITABLE, false )
				.name( ApplicabilityStatus.EDITABLE ) );
		this.code = code;
		this.parameterNameValidator = new org.alice.ide.name.validators.ParameterNameValidator( code );
	}

	@Override
	public org.lgna.project.ast.UserType<?> getDeclaringType() {
		return null;
	}

	@Override
	protected void localize() {
		super.localize();
		//todo
		String codeText;
		if( code instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)code;
			if( method.isProcedure() ) {
				codeText = "procedure";
			} else {
				codeText = "function";
			}
		} else {
			codeText = "constructor";
		}
		String text = "I understand that I need to update the invocations to this " + codeText + ".";
		this.isRequirementToUpdateInvocationsUnderstoodState.setTextForBothTrueAndFalse( text );

		this.hasNotAgreedToUpdateInvocationsStatus.setText( "You must agree to update the invocations." );
	}

	public org.lgna.croquet.BooleanState getIsRequirementToUpdateInvocationsUnderstoodState() {
		return this.isRequirementToUpdateInvocationsUnderstoodState;
	}

	public org.lgna.project.ast.UserCode getCode() {
		return this.code;
	}

	@Override
	protected org.alice.ide.ast.declaration.views.AddParameterView createView() {
		return new org.alice.ide.ast.declaration.views.AddParameterView( this );
	}

	private org.lgna.project.ast.UserParameter createParameter() {
		return new org.lgna.project.ast.UserParameter( this.getDeclarationLikeSubstanceName(), this.getValueType() );
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		Status rv = super.getStatusPreRejectorCheck( step );
		if( rv == IS_GOOD_TO_GO_STATUS ) {
			if( this.isRequirementToUpdateInvocationsUnderstoodState.getValue() ) {
				//pass
			} else {
				return this.hasNotAgreedToUpdateInvocationsStatus;
			}
		}
		return rv;
	}

	@Override
	public org.lgna.project.ast.UserParameter getPreviewValue() {
		return this.createParameter();
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		return new org.alice.ide.croquet.edits.ast.AddParameterEdit( completionStep, this.code, this.createParameter() );
	}

	@Override
	public void handlePreActivation() {
		java.util.List<org.lgna.project.ast.SimpleArgumentListProperty> argumentLists = org.alice.ide.IDE.getActiveInstance().getArgumentLists( code );
		this.isRequirementToUpdateInvocationsUnderstoodState.setValueTransactionlessly( argumentLists.size() == 0 );
		super.handlePreActivation();
	}

	@Override
	protected boolean isNameAvailable( String name ) {
		return this.parameterNameValidator.isNameAvailable( name );
	}
}
