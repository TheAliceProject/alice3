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
package org.alice.ide.createdeclarationpanes;

/**
 * @author Dennis Cosgrove
 */
public abstract class RowsInputPane< E > extends zoot.ZInputPane< E > { 
	public RowsInputPane() {
		java.util.List< java.awt.Component[] > componentRows = this.createComponentRows();
		javax.swing.JPanel panel = new javax.swing.JPanel();
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( panel, componentRows, this.getSpringXPad(), this.getSpringYPad() );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( panel, java.awt.BorderLayout.NORTH );
		this.add( javax.swing.Box.createGlue(), java.awt.BorderLayout.CENTER );

		int inset = 8;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( inset, inset, inset, inset ) );
	}
	protected int getSpringXPad() {
		return 12;
	}
	protected int getSpringYPad() {
		return 12;
	}
	protected java.util.List< java.awt.Component[] > createComponentRows() {
		return new java.util.LinkedList< java.awt.Component[] >();
	}
}
