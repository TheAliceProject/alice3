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
public class IntegerFillerInner extends AbstractNumberFillerInner {
	public IntegerFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.IntegerLiteral.class );
	}
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		this.addExpressionFillIn( blank, 0 );
		this.addExpressionFillIn( blank, 1 );
		this.addExpressionFillIn( blank, 2 );
		this.addExpressionFillIn( blank, 3 );
//		this.addExpressionFillIn( blank, 4 );
//		this.addExpressionFillIn( blank, 5 );
//		this.addExpressionFillIn( blank, 6 );
//		this.addExpressionFillIn( blank, 7 );
//		this.addExpressionFillIn( blank, 8 );
//		this.addExpressionFillIn( blank, 9 );
		blank.addSeparator();
		blank.addFillIn( new org.alice.ide.cascade.customfillin.CustomIntegerFillIn() );
		blank.addSeparator();
		blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Random" ) {
			@Override
			protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
				//addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextIntegerFrom0ToNExclusive", java.lang.Integer.class );
				addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextIntegerFromAToBExclusive", java.lang.Integer.class, java.lang.Integer.class );
				addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextIntegerFromAToBInclusive", java.lang.Integer.class, java.lang.Integer.class );
			}
		} );
		blank.addSeparator();
		blank.addFillIn( new edu.cmu.cs.dennisc.cascade.MenuFillIn( "Math" ) {
			@Override
			protected void addChildrenToBlank(edu.cmu.cs.dennisc.cascade.Blank blank) {
				for( edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator operator : edu.cmu.cs.dennisc.alice.ast.ArithmeticInfixExpression.Operator.values() ) {
					blank.addFillIn( new org.alice.ide.cascade.IncompleteArithmeticExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, operator, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) );
				}
				blank.addSeparator();
				addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "abs", java.lang.Integer.TYPE );
				blank.addSeparator();
				addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "min", java.lang.Integer.TYPE, java.lang.Integer.TYPE );
				addNodeChildForMethod( blank, MATH_TYPE_EXPRESSION, "max", java.lang.Integer.TYPE, java.lang.Integer.TYPE );
			}
		} );
	}
}
