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
public class BooleanFillerInner extends AbstractNumberFillerInner {
	public BooleanFillerInner() {
		super( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE, edu.cmu.cs.dennisc.alice.ast.BooleanLiteral.class );
	}
	@Override
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank ) {
		this.addExpressionFillIn( blank, true );
		this.addExpressionFillIn( blank, false );
//		blank.addSeparator()
//		blank.addChild( ecc.dennisc.alice.ide.cascade.ConditionalInfixExpressionFillIn() )
//		blank.addChild( ecc.dennisc.alice.ide.cascade.RelationalInfixExpressionFillIn( "relational expressions { ==, !=, <, <=, >=, > } (Real Number)", java.lang.Double ) )
//		blank.addChild( ecc.dennisc.alice.ide.cascade.RelationalInfixExpressionFillIn( "relational expressions { ==, !=, <, <=, >=, > } (Integer)", java.lang.Integer ) )
	}
}
