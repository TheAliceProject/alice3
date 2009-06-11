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
class InfixExpressionOperatorFillIn< E extends edu.cmu.cs.dennisc.alice.ast.InfixExpression, F > extends SimpleExpressionFillIn< E > {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava operandType;
	public InfixExpressionOperatorFillIn( E infixExpression, Class<?> leftOperandCls, F operator, Class<?> rightOperandCls ) {
		super( infixExpression );
		infixExpression.leftOperand.setValue( new org.alice.ide.ast.EmptyExpression( leftOperandCls ) );
		infixExpression.operator.setValue( operator );
		infixExpression.rightOperand.setValue( new org.alice.ide.ast.EmptyExpression( rightOperandCls ) );
	}
	@Override
	public E getModel() {
		E rv = super.getModel();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getParentBlank() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getParentBlank().getParentFillIn() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getParentBlank().getParentFillIn().getBlankAt( 0 ) );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getParentBlank().getParentFillIn().getBlankAt( 0 ).getSelectedFillIn() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.getParentBlank().getParentFillIn().getBlankAt( 0 ).getSelectedFillIn().getTransientValue() );
		edu.cmu.cs.dennisc.alice.ast.Expression leftValue = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getParentBlank().getParentFillIn().getBlankAt( 0 ).getSelectedFillIn().getTransientValue();
		if( leftValue != null ) {
			//pass
		} else {
			leftValue = new org.alice.ide.ast.EmptyExpression( this.operandType );
		}
		rv.leftOperand.setValue( leftValue );
		return rv;
	}
	@Override
	protected boolean isMenuItemIconUpToDate() {
		return false;
	}
	@Override
	protected javax.swing.JComponent getMenuProxy() {
		return this.createMenuProxy();
	}
}
