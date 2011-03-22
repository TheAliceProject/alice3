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

package org.alice.ide.croquet.models.cascade.number;

/**
 * @author Dennis Cosgrove
 */
public class MathCascadeMenu extends org.alice.ide.croquet.models.cascade.ExpressionCascadeMenu< edu.cmu.cs.dennisc.alice.ast.Expression > {
	private static class SingletonHolder {
		private static MathCascadeMenu instance = new MathCascadeMenu();
	}
	public static MathCascadeMenu getInstance() {
		return SingletonHolder.instance;
	}
	private MathCascadeMenu() {
		super( java.util.UUID.fromString( "d6d9501b-2a63-4b72-9ceb-3046ff079ba1" ) );
	}
	@Override
	protected void addChildrenToBlank( edu.cmu.cs.dennisc.croquet.CascadeBlank blank ) {
//		for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : PRIME_TIME_DOUBLE_ARITHMETIC_OPERATORS ) {
//			blank.addFillIn( new org.alice.ide.cascade.MostlyDeterminedArithmeticInfixExpressionFillIn( previousExpression, operator, Double.class, Number.class ) );
//		}
//		blank.addSeparator();
//		for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : PRIME_TIME_DOUBLE_ARITHMETIC_OPERATORS ) {
//			blank.addFillIn( new org.alice.ide.cascade.IncompleteArithmeticExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE, operator, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) );
//		}
//		blank.addSeparator();
//
//		blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "remainder, integer divide" ) {
//			@Override
//			protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
//				if( previousExpression != null ) {
//					for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : TUCKED_AWAY_DOUBLE_ARITHMETIC_OPERATORS ) {
//						blank.addFillIn( new org.alice.ide.cascade.MostlyDeterminedArithmeticInfixExpressionFillIn( previousExpression, operator, Double.class, Number.class ) );
//					}
//					blank.addSeparator();
//				}
//				for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : TUCKED_AWAY_DOUBLE_ARITHMETIC_OPERATORS ) {
//					blank.addFillIn( new org.alice.ide.cascade.IncompleteArithmeticExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE, operator, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) );
//				}
//			}
//		} );
		for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : org.alice.ide.croquet.models.cascade.arithmetic.ArithmeticUtilities.PRIME_TIME_DOUBLE_ARITHMETIC_OPERATORS ) {
			blank.addFillIn( org.alice.ide.croquet.models.cascade.number.MostlyPredeterminedArithmeticExpressionFillIn.getInstance( operator ) );
		}
		for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : org.alice.ide.croquet.models.cascade.arithmetic.ArithmeticUtilities.PRIME_TIME_DOUBLE_ARITHMETIC_OPERATORS ) {
			blank.addFillIn( org.alice.ide.croquet.models.cascade.number.ArithmeticExpressionLeftAndRightOperandsFillIn.getInstance( operator ) );
		}
		
		blank.addSeparator();
		blank.addMenu( org.alice.ide.croquet.models.cascade.number.MinMaxCascadeMenu.getInstance() );
		blank.addMenu( FloorCeilingCascadeMenu.getInstance() );
		blank.addMenu( PowerCascadeMenu.getInstance() );
		blank.addMenu( TrigonometryCascadeMenu.getInstance() );
		blank.addMenu( ExponentCascadeMenu.getInstance() );
	}
}
