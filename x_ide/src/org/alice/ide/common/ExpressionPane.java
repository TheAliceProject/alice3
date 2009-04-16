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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPane extends org.alice.ide.common.ExpressionLikeSubstance  {
	private edu.cmu.cs.dennisc.alice.ast.Expression expression;
	public ExpressionPane( edu.cmu.cs.dennisc.alice.ast.Expression expression, java.awt.Component component ) {
		this.expression = expression;
		this.add( component );
		this.setBackground( getIDE().getColorFor( expression ) );
		//this.setBackground( java.awt.Color.GREEN );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		if( this.expression != null ) {
			return this.expression.getType();
		} else {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class );
		}
	}
	@Override
	protected int getInsetTop() {
		if( this.expression instanceof edu.cmu.cs.dennisc.alice.ast.InfixExpression ) {
			return 0;
		} else {
			return super.getInsetTop();
		}
	}
	@Override
	protected int getInsetBottom() {
		if( this.expression instanceof edu.cmu.cs.dennisc.alice.ast.InfixExpression || this.expression instanceof edu.cmu.cs.dennisc.alice.ast.LogicalComplementExpression ) {
			return 0;
		} else {
			return super.getInsetTop();
		}
	}
}
