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
package org.alice.ide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public abstract class CustomFillIn<E extends edu.cmu.cs.dennisc.alice.ast.Expression, F> extends cascade.FillIn< E > {
	protected abstract org.alice.ide.choosers.ValueChooser< ? > createCustomPane();
	protected abstract E createExpression( F value );

	@Override
	public E getTransientValue() {
		return null;
	}
	@Override
	public E getValue() {
		java.awt.Component owner = org.alice.ide.IDE.getSingleton();
		org.alice.ide.choosers.ValueChooser< ? > customPane = this.createCustomPane();
		zoot.ZInputPane< E > inputPane = new CustomInputPane( this, customPane );
		E value = inputPane.showInJDialog( owner );
		if( value != null ) {
			return value;
		} else {
			throw new cascade.CancelException( "" );
		}
	}

	@Override
	protected void addChildren() {
	}
	protected abstract String getMenuProxyText();
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return new javax.swing.JLabel( this.getMenuProxyText() );
	}
}
