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
package edu.cmu.cs.dennisc.alice.ide.about;

/**
 * @author Dennis Cosgrove
 */
class ViewEULAAction extends javax.swing.AbstractAction {
	private String text;
	private String title;
	public ViewEULAAction( String text, String title ) {
		this.putValue( javax.swing.Action.NAME, "View EULA" );
		this.text = text;
		this.title = title;
	}
	public void actionPerformed( java.awt.event.ActionEvent e ) {
		javax.swing.JTextArea textArea = new javax.swing.JTextArea();
		textArea.setText( text );
		textArea.setEditable( false );
		textArea.setLineWrap( true );
		textArea.setWrapStyleWord( true );
		final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( textArea );
		scrollPane.setPreferredSize( new java.awt.Dimension( 480, 320 ) );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				scrollPane.getVerticalScrollBar().setValue( 0 );
			}
		} );
		javax.swing.JOptionPane.showMessageDialog( null, scrollPane, this.title, javax.swing.JOptionPane.PLAIN_MESSAGE );
	}
}

/**
 * @author Dennis Cosgrove
 */
class ViewEULAsPane extends javax.swing.JPanel {
	public ViewEULAsPane() {
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( this, createEULARows(), 8, 4 );
		this.setAlignmentX( 0.0f );
	}
	private java.util.ArrayList< java.awt.Component[] > createEULARows() {
		return addEULARows( new java.util.ArrayList< java.awt.Component[] >() );
	}
	protected java.util.ArrayList< java.awt.Component[] > addEULARows( java.util.ArrayList< java.awt.Component[] > rv ) {
		javax.swing.JLabel aliceLabel = new javax.swing.JLabel( "<html>Alice 3:</html>", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel lookingGlassLabel = new javax.swing.JLabel( "<html>Looking Glass Walk & Touch API:</html>", javax.swing.SwingConstants.TRAILING );
		javax.swing.JLabel simsLabel = new javax.swing.JLabel( "<html>The Sims <sup>TM</sup> 2 Art Assets:</html>", javax.swing.SwingConstants.TRAILING );

		rv.add( new java.awt.Component[] { aliceLabel, new javax.swing.JButton( new ViewEULAAction( edu.cmu.cs.dennisc.alice.License.TEXT, "License Agreement: Alice 3" ) ), javax.swing.Box.createHorizontalGlue() } );
		rv.add( new java.awt.Component[] { lookingGlassLabel, new javax.swing.JButton( new ViewEULAAction( edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "License Agreement: Looking Glass Walk & Touch API" ) ), javax.swing.Box.createHorizontalGlue() } );
		rv.add( new java.awt.Component[] { simsLabel, new javax.swing.JButton( new ViewEULAAction( edu.cmu.cs.dennisc.nebulous.License.TEXT, "License Agreement: The Sims (TM) 2 Art Assets" ) ), javax.swing.Box.createHorizontalGlue() } );
		
		return rv;
	}

}

/**
 * @author Dennis Cosgrove
 */
public class AboutPane extends edu.cmu.cs.dennisc.zoot.ZPageAxisPane {
	public AboutPane() {
		this.add( this.createLabel( "current version: " + edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() ) );
		this.add( javax.swing.Box.createVerticalStrut( 8 ) );
		this.add( this.createLabel( "Alice 3, the Move and Turn API, and the Stage API: designed and implemented by Dennis Cosgrove" ) );
		this.add( this.createLabel( "The Looking Glass Walk and Touch API: designed and implemented by Caitlin Kelleher" ) );
		this.add( this.createLabel( "Scene Editor: designed and implemented by David Culyba and Dennis Cosgrove" ) );
		this.add( javax.swing.Box.createVerticalStrut( 24 ) );
		this.add( this.createViewEULAsPane() );
		this.add( javax.swing.Box.createVerticalStrut( 24 ) );
	}
	
	private java.awt.Component createLabel( String text ) {
		javax.swing.JTextField rv = new javax.swing.JTextField( text );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		rv.setOpaque( false );
		rv.setEditable( false );
		return rv;
	}
	private java.awt.Component createViewEULAsPane() {
		return new ViewEULAsPane();
	}
	
	public static void main( String[] args ) {
		AboutPane aboutPane = new AboutPane();
		javax.swing.JOptionPane.showMessageDialog( null, aboutPane, "About Alice 3", javax.swing.JOptionPane.PLAIN_MESSAGE );
	}
}
