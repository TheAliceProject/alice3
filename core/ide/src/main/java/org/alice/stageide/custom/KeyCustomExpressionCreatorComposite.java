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
package org.alice.stageide.custom;

/**
 * @author Dennis Cosgrove
 */
public class KeyCustomExpressionCreatorComposite extends org.alice.ide.custom.CustomExpressionCreatorComposite<org.alice.stageide.custom.components.KeyCustomExpressionCreatorView> {
	private static class SingletonHolder {
		private static KeyCustomExpressionCreatorComposite instance = new KeyCustomExpressionCreatorComposite();
	}

	public static KeyCustomExpressionCreatorComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.PlainStringValue pressAnyKeyLabel = this.createStringValue( "pressAnyKeyLabel" );
	private final ErrorStatus keyRequiredError = this.createErrorStatus( "keyRequiredError" );

	private KeyCustomExpressionCreatorComposite() {
		super( java.util.UUID.fromString( "908ee2c1-97a9-4fb4-9716-7846cb206549" ) );
	}

	@Override
	protected org.alice.stageide.custom.components.KeyCustomExpressionCreatorView createView() {
		return new org.alice.stageide.custom.components.KeyCustomExpressionCreatorView( this );
	}

	public org.lgna.croquet.PlainStringValue getPressAnyKeyLabel() {
		return this.pressAnyKeyLabel;
	}

	public KeyState getValueState() {
		return KeyState.getInstance();
	}

	@Override
	protected org.lgna.project.ast.Expression createValue() {
		org.lgna.story.Key key = this.getValueState().getValue();
		if( key != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Key.class );
			org.lgna.project.ast.AbstractField field = type.getDeclaredField( type, key.name() );
			assert field.isPublicAccess() && field.isStatic() && field.isFinal();
			return new org.lgna.project.ast.FieldAccess( new org.lgna.project.ast.TypeExpression( type ), field );
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.getValueState().getValue() != null ) {
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.keyRequiredError;
		}
	}

	@Override
	protected boolean isDefaultButtonDesired() {
		return false;
	}

	@Override
	protected void initializeToPreviousExpression( org.lgna.project.ast.Expression expression ) {
		org.lgna.story.Key key = null;
		if( expression instanceof org.lgna.project.ast.FieldAccess ) {
			org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expression;
			org.lgna.project.ast.AbstractType<?, ?, ?> type = fieldAccess.getType();
			if( type == org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Key.class ) ) {
				org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
				if( field != null ) {
					key = Enum.valueOf( org.lgna.story.Key.class, field.getName() );
				}
			}
		}
		this.getValueState().setValueTransactionlessly( key );
	}
}
