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

import edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.alice.ide.ast.declaration.views.AddParameterView;
import org.alice.ide.croquet.edits.ast.AddParameterEdit;
import org.alice.ide.name.validators.ParameterNameValidator;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.SimpleArgumentListProperty;
import org.lgna.project.ast.UserCode;
import org.lgna.project.ast.UserParameter;
import org.lgna.project.ast.UserType;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class AddParameterComposite extends DeclarationLikeSubstanceComposite<UserParameter> {
	private static InitializingIfAbsentMap<UserCode, AddParameterComposite> map = Maps.newInitializingIfAbsentHashMap();

	public static AddParameterComposite getInstance( UserCode code ) {
		return map.getInitializingIfAbsent( code, new InitializingIfAbsentMap.Initializer<UserCode, AddParameterComposite>() {
			@Override
			public AddParameterComposite initialize( UserCode key ) {
				return new AddParameterComposite( key );
			}
		} );
	}

	private final BooleanState isRequirementToUpdateInvocationsUnderstoodState = this.createBooleanState( "isRequirementToUpdateInvocationsUnderstoodState", false );
	private final ErrorStatus hasNotAgreedToUpdateInvocationsStatus = this.createErrorStatus( "hasNotAgreedToUpdateInvocationsStatus" );
	private final UserCode code;
	//todo: remove
	private final ParameterNameValidator parameterNameValidator;

	private AddParameterComposite( UserCode code ) {
		super( UUID.fromString( "628f8e97-84b5-480c-8f05-d69749a4203e" ), new Details()
				.valueComponentType( ApplicabilityStatus.EDITABLE, null )
				.valueIsArrayType( ApplicabilityStatus.EDITABLE, false )
				.name( ApplicabilityStatus.EDITABLE ) );
		this.code = code;
		this.parameterNameValidator = new ParameterNameValidator( code );
	}

	@Override
	public UserType<?> getDeclaringType() {
		return null;
	}

	@Override
	protected void localize() {
		super.localize();
		//todo
		String codeText;
		if( code instanceof AbstractMethod ) {
			AbstractMethod method = (AbstractMethod)code;
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

	public BooleanState getIsRequirementToUpdateInvocationsUnderstoodState() {
		return this.isRequirementToUpdateInvocationsUnderstoodState;
	}

	public UserCode getCode() {
		return this.code;
	}

	@Override
	protected AddParameterView createView() {
		return new AddParameterView( this );
	}

	private UserParameter createParameter() {
		return new UserParameter( this.getDeclarationLikeSubstanceName(), this.getValueType() );
	}

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
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
	public UserParameter getPreviewValue() {
		return this.createParameter();
	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		return new AddParameterEdit( completionStep, this.code, this.createParameter() );
	}

	@Override
	public void handlePreActivation() {
		List<SimpleArgumentListProperty> argumentLists = IDE.getActiveInstance().getArgumentLists( code );
		this.isRequirementToUpdateInvocationsUnderstoodState.setValueTransactionlessly( argumentLists.size() == 0 );
		super.handlePreActivation();
	}

	@Override
	protected boolean isNameAvailable( String name ) {
		return this.parameterNameValidator.isNameAvailable( name );
	}
}
