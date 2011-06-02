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
package org.alice.ide.cascade.fillerinners;

/**
 * @author Dennis Cosgrove
 */
public class IntegerFillerInner extends AbstractNumberFillerInner {
	public IntegerFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.IntegerLiteral.class );
	}
	@Override
	public java.util.List< org.lgna.croquet.CascadeItem > addItems( java.util.List< org.lgna.croquet.CascadeItem > rv, boolean isTop, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression ) {
		if( isTop && prevExpression != null ) {
			if( prevExpression instanceof edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression ) {
				edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression prevArithmeticInfixExpression = (edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression)prevExpression;
				final edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator prevOperator = prevArithmeticInfixExpression.operator.getValue();
				for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : org.alice.ide.croquet.models.cascade.arithmetic.ArithmeticUtilities.PRIME_TIME_INTEGER_ARITHMETIC_OPERATORS ) {
					if( operator != prevOperator ) {
						rv.add( org.alice.ide.croquet.models.cascade.arithmetic.ReplaceOperatorInPreviousArithmeticExpressionFillIn.getInstance( operator ) );
					}
				}
				rv.add( org.alice.ide.croquet.models.cascade.arithmetic.ReplaceOperatorDivideRemainderCascadeMenu.getInstance() );
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
				rv.add( org.alice.ide.croquet.models.cascade.arithmetic.ReduceToLeftOperandInPreviousArithmeticExpressionFillIn.getInstance() );
				rv.add( org.alice.ide.croquet.models.cascade.arithmetic.ReduceToRightOperandInPreviousArithmeticExpressionFillIn.getInstance() );
				rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			}
		}
		
		for( int i=0; i<4; i++ ) {
			rv.add( org.alice.ide.croquet.models.cascade.literals.IntegerLiteralFillIn.getInstance( i ) );
		}
		if( isTop && prevExpression != null ) {
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.integer.RandomCascadeMenu.getInstance() );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.integer.RealToIntegerCascadeMenu.getInstance() );
			rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
			rv.add( org.alice.ide.croquet.models.cascade.integer.MathCascadeMenu.getInstance() );
		}
		rv.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		//rv.add( org.alice.ide.croquet.models.cascade.custom.CustomIntegerLiteralFillIn.getInstance() );
		rv.add( org.alice.ide.croquet.models.custom.CustomIntegerInputDialogOperation.getInstance().getFillIn() );
 		return rv;
	}
}
