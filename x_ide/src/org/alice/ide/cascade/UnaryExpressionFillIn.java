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
public abstract class UnaryExpressionFillIn< E extends edu.cmu.cs.dennisc.alice.ast.Expression > extends cascade.FillIn< E > {
	private String menuText;
	public UnaryExpressionFillIn( String menuText ) {
		this.menuText = menuText;
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType getOperandType();
	@Override
	protected void addChildren() {
		this.addChild( new ExpressionBlank( getOperandType() ) );
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return zoot.ZLabel.acquire( menuText );
	}
	
	protected abstract E createValue( edu.cmu.cs.dennisc.alice.ast.Expression operand );
	@Override
	public final E getValue() {
		edu.cmu.cs.dennisc.alice.ast.Expression operand = (edu.cmu.cs.dennisc.alice.ast.Expression)this.getBlankAt( 0 ).getSelectedFillIn().getValue();
		return this.createValue( operand );
	}
	
}
