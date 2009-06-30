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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractHyperlink extends javax.swing.JLabel {
	private javax.swing.Action action;
	private java.awt.Color defaultColor = java.awt.Color.BLACK;
	private java.awt.Color armedColor = java.awt.Color.BLUE;
	public AbstractHyperlink() {
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				assert AbstractHyperlink.this.action != null;
				AbstractHyperlink.this.action.actionPerformed( null );
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				AbstractHyperlink.this.setForeground( armedColor );
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
				AbstractHyperlink.this.setForeground( defaultColor );
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			} 
		} );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
	public java.awt.Color getDefaultColor() {
		return this.defaultColor;
	}
	public void setDefaultColor( java.awt.Color defaultColor ) {
		this.defaultColor = defaultColor;
	}
	public java.awt.Color getArmedColor() {
		return this.armedColor;
	}
	public void setArmedColor( java.awt.Color armedColor ) {
		this.armedColor = armedColor;
	}
	public javax.swing.Action getAction() {
		return this.action;
	}
	public void setAction( javax.swing.Action action ) {
		assert action != null;
		this.setText( "<html><u>" + (String)action.getValue( javax.swing.Action.NAME ) + "</u></html>" );
		this.action = action;
	}
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		java.awt.Font font = g.getFont();
//		java.util.Map< java.awt.font.TextAttribute, Object > attributes = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
//		attributes.put( java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON );
//		g.setFont( font.deriveFont( attributes ) );
//		super.paintComponent( g );
//	}
}
