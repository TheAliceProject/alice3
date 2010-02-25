/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.cascade.fillerinners;


/**
 * @author Dennis Cosgrove
 */
public class NumberFillerInner extends AbstractNumberFillerInner {
	public NumberFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.DoubleLiteral.class );
	}
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		final edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = org.alice.ide.IDE.getSingleton().getPreviousExpression();
		final boolean isTop = blank.getParentFillIn() == null;
		if( isTop ) {
			if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression ) {
				edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression previousArithmeticInfixExpression = (edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression)previousExpression;
				edu.cmu.cs.dennisc.alice.ast.Expression leftOperand = previousArithmeticInfixExpression.leftOperand.getValue();
				edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator prevOperator = previousArithmeticInfixExpression.operator.getValue();
				edu.cmu.cs.dennisc.alice.ast.Expression rightOperand = previousArithmeticInfixExpression.rightOperand.getValue();
				edu.cmu.cs.dennisc.alice.ast.AbstractType expressionType = previousArithmeticInfixExpression.expressionType.getValue();
				for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : TRIMMED_ARITHMETIC_OPERATORS ) {
					if( operator != prevOperator ) {
						blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( new edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression( leftOperand, operator, rightOperand, expressionType ), "(replace operator)" ) );
					}
				}
				blank.addSeparator();
				blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( leftOperand, "(reduce to left operand only)" ) );
				blank.addFillIn( new org.alice.ide.cascade.LabeledExpressionFillIn( rightOperand, "(reduce to right operand only)" ) );
				blank.addSeparator();
			}
		}
		this.addExpressionFillIn( blank, 0.0 );
		this.addExpressionFillIn( blank, 0.25 );
		this.addExpressionFillIn( blank, 0.5 );
		this.addExpressionFillIn( blank, 1.0 );
		this.addExpressionFillIn( blank, 2.0 );
		//this.addExpressionFillIn( blank, 5.0 );
		this.addExpressionFillIn( blank, 10.0 );
		//this.addExpressionFillIn( blank, 100.0 );
		blank.addSeparator();
		if( isTop ) {
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Math" ) {
				@Override
				protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
					if( previousExpression != null ) {
						for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : TRIMMED_ARITHMETIC_OPERATORS ) {
							blank.addFillIn( new org.alice.ide.cascade.MostlyDeterminedArithmeticInfixExpressionFillIn( previousExpression, operator, Double.class, Number.class ) );
						}
						blank.addSeparator();
					}
					for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.values() ) {
						blank.addFillIn( new org.alice.ide.cascade.IncompleteArithmeticExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE, operator, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE ) );
					}
					blank.addSeparator();
					blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "min, max" ) {
						@Override
						protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "min", java.lang.Double.TYPE, java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "max", java.lang.Double.TYPE, java.lang.Double.TYPE );
						}
					} );
					blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "absolute value, round, ceiling, floor" ) {
						@Override
						protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "abs", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "rint", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "ceil", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "floor", java.lang.Double.TYPE );
						}
					} );
					//blank.addSeparator();
					blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "sqrt, pow" ) {
						@Override
						protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "sqrt", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "pow", java.lang.Double.TYPE, java.lang.Double.TYPE );
						}
					} );
					//blank.addSeparator();
					blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "sin, cos, tan, asin, acos, atan, atan2, PI" ) {
						@Override
						protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "sin", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "cos", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "tan", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "asin", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "acos", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "atan", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "atan2", java.lang.Double.TYPE, java.lang.Double.TYPE );
							addNodeChildForField( blank, MATH_TYPE_EXPRESSION, java.lang.Double.TYPE, "PI" );
						}
					} );
					//blank.addSeparator();
					blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "exp, log, E" ) {
						@Override
						protected void addChildrenToBlank( edu.cmu.cs.dennisc.cascade.Blank blank ) {
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "exp", java.lang.Double.TYPE );
							addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "log", java.lang.Double.TYPE );
							addNodeChildForField( blank, MATH_TYPE_EXPRESSION, java.lang.Double.TYPE, "E" );
						}
					} );
				}
			} );
			blank.addSeparator();
			blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Random" ) {
				@Override
				protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
					addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextDoubleInRange", java.lang.Number.class, java.lang.Number.class );
					addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "random" );
				}
			} );
		}
		//		self._addArithmeticFillIns( blank, ecc.dennisc.alice.ast.getType( java.lang.Double ), ecc.dennisc.alice.ast.getType( java.lang.Number ) )
		blank.addSeparator();
		blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomDoubleFillIn() );
	}
}
