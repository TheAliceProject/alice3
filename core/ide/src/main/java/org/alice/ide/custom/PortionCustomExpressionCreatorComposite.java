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

package org.alice.ide.custom;

/**
 * @author Dennis Cosgrove
 */
public class PortionCustomExpressionCreatorComposite extends CustomExpressionCreatorComposite<org.alice.ide.custom.components.PortionCustomExpressionCreatorView> {
	private static class SingletonHolder {
		private static PortionCustomExpressionCreatorComposite instance = new PortionCustomExpressionCreatorComposite();
	}

	public static PortionCustomExpressionCreatorComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.BoundedIntegerState valueState = this.createBoundedIntegerState( "valueState", new BoundedIntegerDetails().minimum( 0 ).maximum( 100 ) );

	private PortionCustomExpressionCreatorComposite() {
		super( java.util.UUID.fromString( "f1d64eb4-38fd-4c43-856f-e8aa2b1708d1" ) );
	}

	@Override
	protected org.alice.ide.custom.components.PortionCustomExpressionCreatorView createView() {
		return new org.alice.ide.custom.components.PortionCustomExpressionCreatorView( this );
	}

	public org.lgna.croquet.BoundedIntegerState getValueState() {
		return this.valueState;
	}

	@Override
	protected org.lgna.project.ast.Expression createValue() {
		java.math.BigDecimal decimal = new java.math.BigDecimal( this.valueState.getValue() );
		decimal = decimal.movePointLeft( 2 );
		return new org.lgna.project.ast.DoubleLiteral( decimal.doubleValue() );
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	@Override
	protected void initializeToPreviousExpression( org.lgna.project.ast.Expression expression ) {
		double value;
		if( expression instanceof org.lgna.project.ast.DoubleLiteral ) {
			org.lgna.project.ast.DoubleLiteral doubleLiteral = (org.lgna.project.ast.DoubleLiteral)expression;
			value = doubleLiteral.value.getValue();
		} else {
			value = Double.NaN;
		}
		if( Double.isNaN( value ) ) {
			//pass
		} else {
			java.math.BigDecimal decimal = new java.math.BigDecimal( value, new java.math.MathContext( java.math.BigDecimal.ROUND_HALF_DOWN ) );
			decimal = decimal.movePointRight( 2 );
			this.valueState.setValueTransactionlessly( decimal.intValue() );
		}
	}
}
