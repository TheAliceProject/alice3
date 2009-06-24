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
public abstract class AbstractChooser<E> implements ValueChooser< E >, zoot.InputValidator {
	private static final String[] LABEL_TEXTS = { "value:" };
	private zoot.ZInputPane< ? > inputPane;
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
		org.alice.ide.IDE ide = this.getIDE();
		if( ide != null ) {
			return ide.getPreviousExpression();
		} else {
			return null;
		}
	}
	public String[] getLabelTexts() {
		return LABEL_TEXTS;
	}
	public zoot.ZInputPane< ? > getInputPane() {
		return this.inputPane;
	}
	public void setInputPane( zoot.ZInputPane< ? > inputPane ) {
		this.inputPane = inputPane;
		this.inputPane.addOKButtonValidator( this );
	}
}
