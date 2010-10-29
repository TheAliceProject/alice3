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
public class BooleanFillerInner extends ExpressionFillerInner {
	public BooleanFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.BooleanLiteral.class );
	}
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		final edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator[] CONDITIONAL_OPERATORS = {
				edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.AND, 	
				edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.OR, 	
		};
		//todo: relational
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = ide.getCascadeManager().createCopyOfPreviousExpression();
		final boolean isTop = blank.getParentFillIn() == null;
		if( isTop && previousExpression != null ) {
			if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression ) {
				edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression previousConditionalInfixExpression = (edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression)previousExpression;
				edu.cmu.cs.dennisc.alice.ast.Expression leftOperand = previousConditionalInfixExpression.leftOperand.getValue();
				edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator prevOperator = previousConditionalInfixExpression.operator.getValue();
				edu.cmu.cs.dennisc.alice.ast.Expression rightOperand = previousConditionalInfixExpression.rightOperand.getValue();
				for( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator operator : CONDITIONAL_OPERATORS ) {
					if( operator != prevOperator ) {
						blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression( leftOperand, operator, rightOperand ), "(replace operator)" ) );
					}
				}
				blank.addSeparator();
				blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( leftOperand, "(reduce to left operand only)" ) );
				blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( rightOperand, "(reduce to right operand only)" ) );
				blank.addSeparator();
			} else if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.LogicalComplement ) {
				edu.cmu.cs.dennisc.alice.ast.LogicalComplement previousLogicalComplement = (edu.cmu.cs.dennisc.alice.ast.LogicalComplement)previousExpression;
				blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( previousLogicalComplement.operand.getValue(), "(reduce to inner operand only)" ) );
				blank.addSeparator();
			}
		}
		blank.addFillIn( new BooleanLiteralFillIn( true ) );
		blank.addFillIn( new BooleanLiteralFillIn( false ) );
		if( isTop && previousExpression != null ) {
			blank.addSeparator();
	//			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Random" ) {
	//			@Override
	//			protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
	//				addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextBoolean" );
	//			}
	//		} );
			addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextBoolean" );
			
			blank.addSeparator();
			if( previousExpression != null ) {
				blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.LogicalComplement >( new edu.cmu.cs.dennisc.alice.ast.LogicalComplement( previousExpression ) ) );
			}
			blank.addFillIn( new org.alice.ide.cascade.IncompleteLogicalComplementFillIn() );
			blank.addSeparator();
			if( blank.getParentFillIn() == null ) {
				if( previousExpression != null ) {
					for( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator operator : CONDITIONAL_OPERATORS ) {
						blank.addFillIn( new org.alice.ide.cascade.MostlyDeterminedConditionalInfixExpressionFillIn( previousExpression, operator ) );
					}
				}
			}
			for( edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator operator : edu.cmu.cs.dennisc.alice.ast.ConditionalInfixExpression.Operator.values() ) {
				blank.addFillIn( new org.alice.ide.cascade.IncompleteConditionalExpressionFillIn( operator ) );
			}
			blank.addSeparator();
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Relational (Real Number) { ==, !=, <, <=, >=, > }" ) {
				@Override
				protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
					for( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator : edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.values() ) {
						blank.addFillIn( new org.alice.ide.cascade.IncompleteRelationalExpressionFillIn( Number.class, operator ) );
					}
				}
			} );
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Relational (Integer) { ==, !=, <, <=, >=, > }" ) {
				@Override
				protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
					for( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator : edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.values() ) {
						blank.addFillIn( new org.alice.ide.cascade.IncompleteRelationalExpressionFillIn( Integer.class, operator ) );
					}
				}
			} );
			
			final edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator[] TRIMMED_RELATIONAL_OPERATORS = {
					edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.EQUALS,
					edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.NOT_EQUALS,
			};
			for( final edu.cmu.cs.dennisc.pattern.Tuple2< String, Class<?> > tuple : ide.getNameClsPairsForRelationalFillIns() ) {
				blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Relational (" + tuple.getA() + ") { ==, != }" ) {
					@Override
					protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
						for( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator : TRIMMED_RELATIONAL_OPERATORS ) {
							blank.addFillIn( new org.alice.ide.cascade.IncompleteRelationalExpressionFillIn( tuple.getB(), operator ) );
						}
					}
				} );
			}
			blank.addSeparator();
		}
	}
}
