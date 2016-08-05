/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.issue.swing;

/**
 * @author Dennis Cosgrove
 */
public class JStandardHeaderPane extends javax.swing.JPanel {
	private static final javax.swing.ImageIcon LOGO_ICON = new javax.swing.ImageIcon( JStandardHeaderPane.class.getResource( "/org/alice/ide/issue/swing/views/images/meanQueen.png" ) );

	public JStandardHeaderPane( org.lgna.issue.ApplicationIssueConfiguration config ) {
		StringBuilder sbHeader = new StringBuilder();
		sbHeader.append( "<html>" );
		sbHeader.append( "<h1>" );
		if( org.alice.ide.issue.UserProgramRunningStateUtilities.isUserProgramRunning() ) {
			sbHeader.append( "An exception has been caught during the running of your program.<p>" );
			sbHeader.append( "<p>While this <em>could</em> be the result of a problem in your code,<br>it is likely a bug in " );
			sbHeader.append( config.getApplicationName() );
		} else {
			sbHeader.append( "A bug has been found" );
		}
		sbHeader.append( "</h1>" );
		sbHeader.append( "<p>Please accept our apologies and press the <em>\"" );
		sbHeader.append( config.getSubmitActionName() );
		sbHeader.append( "\"</em> button.<p>" );
		sbHeader.append( "<p>We will do our best to fix the problem and make a new release.<p>" );
		//sbHeader.append( "<p><p><p>Note:" );
		sbHeader.append( "</html>" );

		java.awt.Color backgroundColor = java.awt.Color.DARK_GRAY;
		java.awt.Color foregroundColor = java.awt.Color.WHITE;
		StringBuilder sbBottom = new StringBuilder();
		sbBottom.append( "<html>" );
		sbBottom.append( "<body bgcolor=\"" );
		sbBottom.append( edu.cmu.cs.dennisc.java.awt.ColorUtilities.toHashText( backgroundColor ) );
		sbBottom.append( "\" text=\"" );
		sbBottom.append( edu.cmu.cs.dennisc.java.awt.ColorUtilities.toHashText( foregroundColor ) );
		sbBottom.append( "\">" );
		sbBottom.append( "Note: it is possible that this bug has already been fixed.<p>" );
		sbBottom.append( "To download the latest release go to: <a href=\"" );
		sbBottom.append( config.getDownloadUrlSpec() );
		sbBottom.append( "\">" );
		sbBottom.append( config.getDownloadUrlText() );
		sbBottom.append( "</a> [web]." );
		sbBottom.append( "</body>" );
		sbBottom.append( "</html>" );

		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( sbBottom.toString() );

		javax.swing.JLabel logoLabel = new javax.swing.JLabel( LOGO_ICON );

		javax.swing.JLabel headerLabel = new javax.swing.JLabel( sbHeader.toString() );
		headerLabel.setForeground( foregroundColor );
		headerLabel.setVerticalAlignment( javax.swing.SwingConstants.TOP );

		java.awt.Color linkColor = new java.awt.Color( 191, 191, 255 );
		edu.cmu.cs.dennisc.javax.swing.components.JBrowserHtmlView browserView = new edu.cmu.cs.dennisc.javax.swing.components.JBrowserHtmlView();
		browserView.setText( sbBottom.toString() );
		browserView.getHtmlDocument().getStyleSheet().addRule( "A {color:" + edu.cmu.cs.dennisc.java.awt.ColorUtilities.toHashText( linkColor ) + "}" );
		browserView.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.setLayout( new net.miginfocom.swing.MigLayout( "fill, insets 16 8 0 8" ) );
		this.add( new javax.swing.JLabel( edu.cmu.cs.dennisc.javax.swing.IconUtilities.getErrorIcon() ), "aligny top, spany 2" );
		this.add( headerLabel );
		this.add( logoLabel, "spany 2, wrap" );
		this.add( browserView, "aligny bottom, gap bottom 8" );
		this.setBackground( java.awt.Color.DARK_GRAY );
		//this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 16, 8, 0, 8 ) );
	}
}
