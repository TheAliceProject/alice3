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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public class KSuggestiveTextField extends javax.swing.JTextField {
	private String textForBlankCondition;

	public KSuggestiveTextField( String text, String textForBlankCondition ) {
		super( text );
		this.setBorder( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );
		this.textForBlankCondition = textForBlankCondition;
		this.addFocusListener( new SuggestiveTextFocusAdapter( this ) );
		//setToolTipText( this.textForBlankCondition );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		java.awt.Dimension rv = super.getMaximumSize();
		java.awt.Dimension preferred = getPreferredSize();
		rv.height = preferred.height;
		return rv;
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		SuggestiveTextUtilties.drawBlankTextIfNecessary( this, g, this.textForBlankCondition );
	}
}
