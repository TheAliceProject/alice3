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
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = org.alice.ide.IDE.getSingleton().getPreviousExpression();
		if( blank.getParentFillIn() == null ) {
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
		this.addExpressionFillIn( blank, true );
		this.addExpressionFillIn( blank, false );
		blank.addSeparator();
		if( blank.getParentFillIn() == null ) {
			if( previousExpression != null ) {
				blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.LogicalComplement >( new edu.cmu.cs.dennisc.alice.ast.LogicalComplement( previousExpression ) ) );
			}
		}
		blank.addFillIn( new org.alice.ide.cascade.LogicalComplementFillIn() );
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
		blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "relational { ==, !=, <, <=, >=, > }" ) {
			@Override
			protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
				blank.addFillIn( new org.alice.ide.cascade.RelationalExpressionFillIn( "Boolean", Boolean.class ) );
				blank.addFillIn( new org.alice.ide.cascade.RelationalExpressionFillIn( "Real Number", Number.class ) );
				blank.addFillIn( new org.alice.ide.cascade.RelationalExpressionFillIn( "Integer", Integer.class ) );
			}
		} );
		blank.addSeparator();
		blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Random" ) {
			@Override
			protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
				addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextBoolean" );
			}
		} );
		blank.addSeparator();
	}
}
