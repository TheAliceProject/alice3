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

package org.alice.ide.swing;

public abstract class SnapshotListCellRenderer implements javax.swing.ListCellRenderer {
	private final int PANEL_INSET = 4;
	private final int LABEL_INSET = 8;
	private javax.swing.JPanel panel = new javax.swing.JPanel();
	private javax.swing.JLabel label = new javax.swing.JLabel();
	public SnapshotListCellRenderer() {
		this.panel.setOpaque( false );
		this.label.setOpaque( true );
		this.panel.setBorder( javax.swing.BorderFactory.createEmptyBorder( PANEL_INSET, PANEL_INSET, PANEL_INSET, PANEL_INSET ) );
		this.label.setBorder( javax.swing.BorderFactory.createEmptyBorder( LABEL_INSET, LABEL_INSET, LABEL_INSET, LABEL_INSET ) );
		this.label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
		this.label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );
		
		this.panel.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.panel.add( this.label );
	}
	
	protected abstract javax.swing.JLabel updateLabel( javax.swing.JLabel rv, Object value );
	public final java.awt.Component getListCellRendererComponent( javax.swing.JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
		this.updateLabel( this.label, value );
		java.awt.Color background;
		java.awt.Color foreground;
		if( isSelected ) {
			background = new java.awt.Color( 63, 63, 127 );
			foreground = java.awt.Color.YELLOW;
		} else {
			background = java.awt.Color.WHITE;
			foreground = java.awt.Color.BLACK;
		}
		this.label.setBackground( background );
		this.label.setForeground( foreground );
		return this.panel;
	}
}
