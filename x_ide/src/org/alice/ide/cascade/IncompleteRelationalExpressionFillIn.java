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
public class IncompleteRelationalExpressionFillIn extends IncompleteInfixExpressionFillIn< edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression, edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator > {
	public IncompleteRelationalExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractType operandType, edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator ) {
		super( operandType, operator, operandType );
	}
	public IncompleteRelationalExpressionFillIn( Class<?> operandCls, edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator ) {
		this( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( operandCls ), operator );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression createIncomplete( edu.cmu.cs.dennisc.alice.ast.AbstractType leftOperandType, edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType ) {
		return org.alice.ide.ast.NodeUtilities.createIncompleteRelationalInfixExpression( leftOperandType, operator, rightOperandType );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression createValue( edu.cmu.cs.dennisc.alice.ast.Expression left, edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression.Operator operator, edu.cmu.cs.dennisc.alice.ast.Expression right, edu.cmu.cs.dennisc.alice.ast.AbstractType leftOperandType, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType ) {
		return new edu.cmu.cs.dennisc.alice.ast.RelationalInfixExpression( left, operator, right, leftOperandType, rightOperandType );
	}
	
}
