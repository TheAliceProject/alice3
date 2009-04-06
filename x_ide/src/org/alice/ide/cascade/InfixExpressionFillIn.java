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
public abstract class InfixExpressionFillIn< E extends edu.cmu.cs.dennisc.alice.ast.Expression > extends cascade.FillIn< E > {
	private String menuText;
	public InfixExpressionFillIn( String menuText ) {
		this.menuText = menuText;
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getLeftOperandType();
	protected abstract cascade.Blank createOperatorBlank();
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getRightOperandType();
	@Override
	protected void addChildren() {
		this.addChild( new ExpressionBlank( getLeftOperandType() ) );
		this.addChild( this.createOperatorBlank() );
		this.addChild( new ExpressionBlank( getRightOperandType() ) );
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return new zoot.ZLabel( menuText );
	}
	
	protected abstract E createValue( edu.cmu.cs.dennisc.alice.ast.Expression left, Object operator, edu.cmu.cs.dennisc.alice.ast.Expression right );
	@Override
	public final E getValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression left = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getValue();
		Object operator = this.getBlankAt( 1 ).getSelectedFillIn().getValue();
		edu.cmu.cs.dennisc.alice.ast.Expression right = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 2 ).getSelectedFillIn().getValue();
		return this.createValue( left, operator, right );
	}
	
}
