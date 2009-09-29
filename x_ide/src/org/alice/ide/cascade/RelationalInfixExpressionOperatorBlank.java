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
public class RelationalInfixExpressionOperatorBlank extends edu.cmu.cs.dennisc.cascade.Blank {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType operandType;
	public RelationalInfixExpressionOperatorBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType operandType ) {
		this.operandType = operandType;
	}
	@Override
	protected void addChildren() {
		this.addChild( new RelationalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.EQUALS, this.operandType ) );
		this.addChild( new RelationalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.NOT_EQUALS, this.operandType ) );
		if( this.operandType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE ) {
			//pass
		} else {
			this.addChild( new RelationalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS, this.operandType ) );
			this.addChild( new RelationalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.LESS_EQUALS, this.operandType ) );
			this.addChild( new RelationalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER, this.operandType ) );
			this.addChild( new RelationalInfixExpressionOperatorFillIn( edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator.GREATER_EQUALS, this.operandType ) );
		}
	}
}
