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
package org.alice.ide.choosers;


/**
 * @author Dennis Cosgrove
 */
public class DoubleChooser extends AbstractNumberChooser< Double > {
	public DoubleChooser() {
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		//todo: handle other numbers
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.DoubleLiteral ) {
			edu.cmu.cs.dennisc.alice.ast.DoubleLiteral doubleLiteral = (edu.cmu.cs.dennisc.alice.ast.DoubleLiteral)previousExpression;
			this.setAndSelectText( Double.toString( doubleLiteral.value.getValue() ) );
		}
	}
	public String getTitleDefault() {
		return "Enter Custom Double";
	}
	@Override
	protected Double valueOf( String text ) {
		return Double.valueOf( text );
	}
}
