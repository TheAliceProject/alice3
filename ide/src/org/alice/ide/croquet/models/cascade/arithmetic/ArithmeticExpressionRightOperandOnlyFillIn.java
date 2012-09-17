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

package org.alice.ide.croquet.models.cascade.arithmetic;

/**
 * @author Dennis Cosgrove
 */
public abstract class ArithmeticExpressionRightOperandOnlyFillIn extends org.alice.ide.croquet.models.cascade.PreviousExpressionBasedFillInWithExpressionBlanks<org.lgna.project.ast.ArithmeticInfixExpression> {
	private final org.lgna.project.ast.ArithmeticInfixExpression transientValue;

	public ArithmeticExpressionRightOperandOnlyFillIn( java.util.UUID id, org.lgna.project.ast.AbstractType<?, ?, ?> resultType, org.lgna.project.ast.AbstractType<?, ?, ?> leftOperandType, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, org.lgna.project.ast.AbstractType<?, ?, ?> rightOperandType ) {
		super( id );
		this.addBlank( org.alice.ide.croquet.models.cascade.CascadeManager.getBlankForType( rightOperandType ) );
		this.transientValue = new org.lgna.project.ast.ArithmeticInfixExpression(
				new org.alice.ide.ast.PreviousValueExpression( leftOperandType ),
				operator,
				new org.alice.ide.ast.EmptyExpression( rightOperandType ),
				resultType
				);
	}

	public ArithmeticExpressionRightOperandOnlyFillIn( java.util.UUID id, Class<?> resultCls, Class<?> leftOperandCls, org.lgna.project.ast.ArithmeticInfixExpression.Operator operator, Class<?> rightOperandCls ) {
		this( id, org.lgna.project.ast.JavaType.getInstance( resultCls ), org.lgna.project.ast.JavaType.getInstance( leftOperandCls ), operator, org.lgna.project.ast.JavaType.getInstance( rightOperandCls ) );
	}

	//	@Override
	//	protected boolean isInclusionDesired( org.lgna.croquet.steps.CascadeFillInPrepStep< org.lgna.project.ast.ArithmeticInfixExpression, org.lgna.project.ast.Expression > context, org.lgna.project.ast.Expression previousExpression ) {
	//		return org.alice.ide.croquet.models.cascade.CascadeManager.isInclusionDesired( context, previousExpression, this.transientValue.leftOperand.getValue().getType() );
	//	}
	@Override
	protected org.lgna.project.ast.ArithmeticInfixExpression createValue( org.lgna.project.ast.Expression previousExpression, org.lgna.project.ast.Expression[] expressions ) {
		assert expressions.length == 1;
		return new org.lgna.project.ast.ArithmeticInfixExpression( previousExpression, this.transientValue.operator.getValue(), expressions[ 0 ], this.transientValue.getType() );
	}

	@Override
	public org.lgna.project.ast.ArithmeticInfixExpression getTransientValue( org.lgna.croquet.cascade.ItemNode<? super org.lgna.project.ast.ArithmeticInfixExpression, org.lgna.project.ast.Expression> step ) {
		return this.transientValue;
	}
}
