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
public abstract class AbstractNumberFillerInner extends ExpressionFillerInner {
	public AbstractNumberFillerInner( edu.cmu.cs.dennisc.alice.ast.AbstractType type, Class< ? extends edu.cmu.cs.dennisc.alice.ast.Expression > cls ) {
		super( type, cls );
	}
	protected static final edu.cmu.cs.dennisc.alice.ast.TypeExpression MATH_TYPE_EXPRESSION = org.alice.ide.ast.NodeUtilities.createTypeExpression( java.lang.Math.class );
	protected static final edu.cmu.cs.dennisc.alice.ast.TypeExpression RANDOM_UTILITIES_TYPE_EXPRESSION = org.alice.ide.ast.NodeUtilities.createTypeExpression( org.alice.random.RandomUtilities.class );
	protected static void addNodeChildForMethod( cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression, String methodName, Class... parameterClses ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = typeExpression.value.getValue();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = type.getDeclaredMethod( methodName, parameterClses );
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( type );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractMethod m : type.getDeclaredMethods() ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( m );
		}
		for( Class cls : parameterClses ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( cls );
		}
		assert method != null : methodName;
		org.alice.ide.cascade.MethodInvocationFillIn methodInvocationFillIn = new org.alice.ide.cascade.MethodInvocationFillIn( typeExpression, method );
		blank.addFillIn( methodInvocationFillIn );
	}
	
	
//	protected void addArithmetic( cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType valueType, operandType ) {
//		blank.addChild( ecc.dennisc.alice.ide.cascade.ArithmeticInfixExpressionFillIn( valueType, operandType ) )
//	}
}
