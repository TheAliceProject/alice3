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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class RelationalExpressionFillIn extends InfixExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	public RelationalExpressionFillIn( String menuText, Class<?> cls ) {
		super( menuText );
		type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getLeftOperandType() {
		return this.type;
	}
	@Override
	protected edu.cmu.cs.dennisc.cascade.Blank createOperatorBlank() {
		return new RelationalInfixExpressionOperatorBlank( this.type );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getRightOperandType() {
		return this.type;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression createValue( edu.cmu.cs.dennisc.alice.ast.Expression left, Object operatorContainingExpression, edu.cmu.cs.dennisc.alice.ast.Expression right ) {
		edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression aie = (edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression)operatorContainingExpression;
		return new edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression( left, aie.operator.getValue(), right, this.type, this.type );
	}
}
