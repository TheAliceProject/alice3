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

public abstract class ExpandPane extends javax.swing.JPanel {
	class ToggleButton extends javax.swing.JToggleButton {
		@Override
		public java.awt.Dimension getPreferredSize() {
			java.awt.Dimension rv = super.getPreferredSize();
			java.awt.Font font = this.getFont();
			if( font != null ) {
				java.awt.Graphics g = SwingUtilities.getGraphics();
				java.awt.FontMetrics fm = g.getFontMetrics( font );
				for( String s : new String[] { ExpandPane.this.getExpandedButtonText(), ExpandPane.this.getCollapsedButtonText() } ) {
					java.awt.geom.Rectangle2D bounds = fm.getStringBounds( s, g );
					rv.width = Math.max( rv.width, (int)bounds.getWidth()+16 );
					rv.height = Math.max( rv.height, (int)bounds.getHeight()+4 );
				}
			}
			return rv;
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
			String text;
			if( this.isSelected() ) {
				text = ExpandPane.this.getExpandedButtonText();
			} else {
				text = ExpandPane.this.getCollapsedButtonText();
			}
			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, this.getSize() );
		}
	}
	private javax.swing.JLabel label = this.createLabel();
	private ToggleButton toggle = new ToggleButton();
	private javax.swing.JComponent center;

	public ExpandPane() {
		this.toggle.addActionListener( new java.awt.event.ActionListener() {
			public void actionPerformed( java.awt.event.ActionEvent e ) {
				ExpandPane.this.handleToggled( e );
			}
		} );
		this.label.setText( this.getCollapsedLabelText() );
		//this.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.GRAY ) );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.createTopPane(), java.awt.BorderLayout.NORTH );
		this.center = this.createCenterPane();
	}
	
	//todo: rename
	public javax.swing.JComponent getCenterComponent() {
		return this.center;
	}
	
	protected javax.swing.JLabel createLabel() {
		return new javax.swing.JLabel();
	}
	
	protected abstract String getExpandedLabelText();
	protected abstract String getCollapsedLabelText();
	protected String getExpandedButtonText() {
		return "V";
	}
	protected String getCollapsedButtonText() {
		return ">>>";
	}
	private void handleToggled( java.awt.event.ActionEvent e ) {
		if( this.toggle.isSelected() ) {
			this.add( this.center, java.awt.BorderLayout.CENTER );
			this.label.setText( this.getExpandedLabelText() );
		} else {
			this.remove( this.center );
			this.label.setText( this.getCollapsedLabelText() );
		}
		this.revalidate();
		this.repaint();
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		if( root instanceof java.awt.Window ) {
			java.awt.Window window = (java.awt.Window)root;
			window.pack();
		}
	}
	protected javax.swing.JComponent createTopPane() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		rv.setLayout( new java.awt.BorderLayout() );
		rv.add( this.label, java.awt.BorderLayout.CENTER );
		rv.add( this.toggle, java.awt.BorderLayout.EAST );
		return rv;
	}
	protected abstract javax.swing.JComponent createCenterPane();
}
