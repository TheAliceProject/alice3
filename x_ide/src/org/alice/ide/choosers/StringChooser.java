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
public class StringChooser extends AbstractChooserWithTextField< String > {
	public StringChooser() {
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.StringLiteral ) {
			edu.cmu.cs.dennisc.alice.ast.StringLiteral stringLiteral = (edu.cmu.cs.dennisc.alice.ast.StringLiteral)previousExpression;
			this.setAndSelectText( stringLiteral.value.getValue() );
		}
	}
	@Override
	protected String valueOf( String text ) {
		return text;
	}
}
