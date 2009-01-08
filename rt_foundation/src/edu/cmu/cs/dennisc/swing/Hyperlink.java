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
public class Hyperlink extends javax.swing.JLabel {
	private javax.swing.Action action;
	public Hyperlink( javax.swing.Action action ) {
		this.action = action;
		this.setText( (String)action.getValue( javax.swing.Action.NAME ) );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseClicked( java.awt.event.MouseEvent e ) {
				Hyperlink.this.action.actionPerformed( null );
			}
			public void mouseEntered( java.awt.event.MouseEvent e ) {
				Hyperlink.this.setForeground( java.awt.Color.BLUE );
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
				Hyperlink.this.setForeground( java.awt.Color.BLACK );
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			} 
		} );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		java.awt.Font font = g.getFont();
		java.util.Map< java.awt.font.TextAttribute, Object > attributes = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		attributes.put( java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON );
		g.setFont( font.deriveFont( attributes ) );
		super.paintComponent( g );
	}
}
