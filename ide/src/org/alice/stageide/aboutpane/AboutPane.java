/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.stageide.aboutpane;

/**
 * @author Dennis Cosgrove
 */
class ViewEULAAction extends javax.swing.AbstractAction {
	private String text;
	private String title;
	public ViewEULAAction( String text, String title ) {
		this.putValue( javax.swing.Action.NAME, "View EULA..." );
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
		org.alice.ide.IDE.getActiveInstance().showMessageDialog( scrollPane, this.title, org.lgna.croquet.MessageType.PLAIN );
	}
}

/**
 * @author Dennis Cosgrove
 */
class ViewEULAsPane extends edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane {
	public ViewEULAsPane() {
		super( 8, 4 );
		this.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
	private static java.awt.Component createLabel( String text ) {
		return new javax.swing.JLabel( text, javax.swing.SwingConstants.TRAILING );
	}
	@Override
	protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
		java.awt.Component aliceLabel = createLabel( "<html>Alice 3:</html>" );
//		java.awt.Component lookingGlassLabel = createLabel( "<html>Looking Glass Walk & Touch API:</html>" );
		java.awt.Component simsLabel = createLabel( "<html>The Sims <sup>TM</sup> 2 Art Assets:</html>" );
		rv.add( new java.awt.Component[] { aliceLabel, new javax.swing.JButton( new ViewEULAAction( org.lgna.project.License.TEXT, "License Agreement: Alice 3" ) ) } );
//		rv.add( new java.awt.Component[] { lookingGlassLabel, new javax.swing.JButton( new ViewEULAAction( edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "License Agreement: Looking Glass Walk & Touch API" ) ) } );
		rv.add( new java.awt.Component[] { simsLabel, new javax.swing.JButton( new ViewEULAAction( edu.cmu.cs.dennisc.nebulous.License.TEXT, "License Agreement: The Sims (TM) 2 Art Assets" ) ) } );
		return rv;
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AboutPane extends edu.cmu.cs.dennisc.javax.swing.components.JPageAxisPane {
	public AboutPane() {
		this.add( this.createLabel( "current version: " + org.lgna.project.Version.getCurrentVersionText() ) );
		this.add( javax.swing.Box.createVerticalStrut( 24 ) );
		this.add( this.createViewEULAsPane() );
		this.add( javax.swing.Box.createVerticalStrut( 24 ) );
		
		String[] sponsors = { "Sun Foundation", "Electonic Arts Foundation", "National Science Foundation", "Hearst Foundations", "Heinz Endowments", "Google", "Defense Advanced Research Projects Agency", "Disney and Hyperion" };
		StringBuffer sb = new StringBuffer();
		sb.append( "Alice 3 is supported by:<br><ul>" );
		for( String sponsor : sponsors ) {
			sb.append( "<li><b>" );
			sb.append( sponsor );
			sb.append( "</b></li>" );
		}
		sb.append( "</ul>" );
		this.add( this.createLabel( sb.toString() ) );
		this.add( this.createLabel( "<b>The Sims <sup>TM</sup> 2</b> Art Assets donated by <b>Electronic Arts</b>.</body>" ) );
		this.add( this.createLabel( "Technical support provided by <b>Sun Microsystems</b>." ) );
		this.add( javax.swing.Box.createVerticalStrut( 24 ) );
		this.add( this.createLabel( "Alice 3, the Move&Turn and Stage APIs: designed and implemented by Dennis Cosgrove" ) );
		this.add( this.createLabel( "The Looking Glass Walk&Touch API: designed and implemented by Caitlin Kelleher" ) );
		this.add( this.createLabel( "Scene Editor: designed and implemented by David Culyba and Dennis Cosgrove" ) );
		this.add( javax.swing.Box.createVerticalStrut( 8 ) );
		this.add( this.createLabel( "A special thank you to Steve Seabolt." ) );
		this.add( javax.swing.Box.createVerticalStrut( 8 ) );
		this.add( this.createLabel( "Alice 3 is dedicated to Randy." ) );
	}
	
	private java.awt.Component createLabel( String text ) {
		javax.swing.JEditorPane rv = new javax.swing.JEditorPane( "text/html", "<html><body>" + text + "</body></html>" );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		rv.setOpaque( false );
		rv.setEditable( false );
		rv.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
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
