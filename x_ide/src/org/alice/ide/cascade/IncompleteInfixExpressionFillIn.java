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
public abstract class IncompleteInfixExpressionFillIn< E extends edu.cmu.cs.dennisc.alice.ast.Expression, O > extends IncompleteExpressionFillIn< E > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType leftOperandType;
	private O operator;
	private edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType;
	public IncompleteInfixExpressionFillIn( edu.cmu.cs.dennisc.alice.ast.AbstractType leftOperandType, O operator, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType ) {
		this.leftOperandType = leftOperandType;
		this.operator = operator;
		this.rightOperandType = rightOperandType;
	}
	@Override
	protected void addChildren() {
		this.addChild( new ExpressionBlank( this.leftOperandType ) );
		this.addChild( new ExpressionBlank( this.rightOperandType ) );
	}
	protected abstract E createIncomplete( edu.cmu.cs.dennisc.alice.ast.AbstractType leftOperandType, O operator, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType );
	@Override
	protected final E createIncomplete() {
		return this.createIncomplete( this.leftOperandType, this.operator, this.rightOperandType );
	}
	protected abstract E createValue( edu.cmu.cs.dennisc.alice.ast.Expression left, O operator, edu.cmu.cs.dennisc.alice.ast.Expression right, edu.cmu.cs.dennisc.alice.ast.AbstractType leftOperandType, edu.cmu.cs.dennisc.alice.ast.AbstractType rightOperandType );
	@Override
	public final E getValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression left = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getValue();
		edu.cmu.cs.dennisc.alice.ast.Expression right = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 1 ).getSelectedFillIn().getValue();
		return this.createValue( left, this.operator, right, this.leftOperandType, this.rightOperandType );
	}
	@Override
	public E getTransientValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression left = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getTransientValue();
		edu.cmu.cs.dennisc.alice.ast.Expression right = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 1 ).getSelectedFillIn().getTransientValue();
		return this.createValue( left, this.operator, right, this.leftOperandType, this.rightOperandType );
	}
}
