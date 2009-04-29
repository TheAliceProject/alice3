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

import org.alice.ide.cascade.RelationalInfixExpressionOperatorFillIn;

/**
 * @author Dennis Cosgrove
 */
public class BooleanFillerInner extends ExpressionFillerInner {
	public BooleanFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.BooleanLiteral.class );
	}
	@Override
	public void addFillIns( cascade.Blank blank ) {
		this.addExpressionFillIn( blank, true );
		this.addExpressionFillIn( blank, false );
		blank.addSeparator();
		blank.addFillIn( new cascade.MenuFillIn( "Random" ) {
			@Override
			protected void addChildrenToBlank(cascade.Blank blank) {
				addNodeChildForMethod( blank, RANDOM_UTILITIES_TYPE_EXPRESSION, "nextBoolean" );
			}
		} );
		blank.addSeparator();
		blank.addFillIn( new org.alice.ide.cascade.LogicalComplementFillIn() );
		blank.addFillIn( new org.alice.ide.cascade.ConditionalExpressionFillIn() );
		blank.addSeparator();
		blank.addFillIn( new cascade.MenuFillIn( "relational { ==, !=, <, <=, >=, > }" ) {
			@Override
			protected void addChildrenToBlank(cascade.Blank blank) {
				blank.addFillIn( new org.alice.ide.cascade.RelationalExpressionFillIn( "Boolean", Boolean.class ) );
				blank.addFillIn( new org.alice.ide.cascade.RelationalExpressionFillIn( "Real Number", Number.class ) );
				blank.addFillIn( new org.alice.ide.cascade.RelationalExpressionFillIn( "Integer", Integer.class ) );
			}
		} );
		blank.addSeparator();
	}
}
