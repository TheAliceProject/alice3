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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class MethodInvocation extends Expression {
	public ExpressionProperty expression = new ExpressionProperty( this ) {
		@Override
		public AbstractType getExpressionType() {
			return method.getValue().getDeclaringType();
		}
	};
	public DeclarationProperty< AbstractMethod > method = new DeclarationProperty< AbstractMethod >( this );
	public ArgumentListProperty arguments = new ArgumentListProperty( this );

	public MethodInvocation() {
	}
	public MethodInvocation( Expression expression, AbstractMethod method, Argument... arguments ){
		if( expression instanceof NullLiteral ) {
			//pass
		} else {
			AbstractType expressionType = expression.getType();
			if( expressionType != null ) {
				AbstractType declaringType = method.getDeclaringType();
				if( declaringType != null ) {
					//todo
					//assert declaringType.isAssignableFrom( expressionType );
				}
			}
		}
		this.expression.setValue( expression );
		this.method.setValue( method );
		this.arguments.add( arguments );
	}
	@Override
	public AbstractType getType() {
		return method.getValue().getReturnType();
	}
	
	public boolean isValid() {
		boolean rv = false;
		Expression e = expression.getValue();
		AbstractMethod m = method.getValue();
		if( e != null && m != null ) {
			if( m.isStatic() ) {
				//todo
				rv = true;
			} else {
				AbstractType declaringType = m.getDeclaringType();
				AbstractType expressionType = e.getType();
				if( expressionType instanceof AnonymousInnerTypeDeclaredInAlice ) {
					//todo
					rv = true;
				} else {
					if( declaringType != null && expressionType != null ) {
						rv = declaringType.isAssignableFrom( expressionType );
					} else {
						rv = false;
					}
				}
			}
		} else {
			rv = false;
		}
		return rv;
	}
}
