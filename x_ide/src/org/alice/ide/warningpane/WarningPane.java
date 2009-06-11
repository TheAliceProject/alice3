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
package org.alice.ide.warningpane;

public class WarningPane extends swing.PageAxisPane {
	public WarningPane( boolean isSolicited ) {
		zoot.ZLabel label = zoot.ZLabel.acquire(  new javax.swing.ImageIcon( this.getClass().getResource( "images/toxic.png" ) ) );

		StringBuffer sb = new StringBuffer();
		sb.append( "<html><body>" );
		sb.append( "<h1>WARNING: Alice3 is not for the faint of heart.</h1>" );
		sb.append( "<font size=\"+1\">" );
		sb.append( "Alice3 is currently under development.  We are working very hard to make this dialog box obsolete.<br>" );
		sb.append( "Thank you for your patience.<br>" );
		sb.append( "We welcome your feedback.<br>" );
		sb.append( "</font>" );
		//sb.append( "bug reports: <a href=http://bugs.alice.org:8080/>http://bugs.alice.org:8080/</a>" );
		sb.append( "<li>resources: <u>http://alice.kenai.com/</u><br>" );
		sb.append( "<li>bug reports: <u>http://bugs.alice.org:8080/</u><br>" );
		sb.append( "<li>blog: <u>http://blog.alice.org/</u><br>" );
		sb.append( "<li>community: <u>http://www.alice.org/community/</u><br>" );
		sb.append( "</body></html>" );
		
		javax.swing.JEditorPane editorPane = new javax.swing.JEditorPane( "text/html", sb.toString() );
		editorPane.setEditable( false );
		editorPane.setOpaque( false );
		
		label.setAlignmentX( 0.0f );
		editorPane.setAlignmentX( 0.0f );
		this.add( label );
		this.add( editorPane );
		
		if( isSolicited ) {
			//pass
		} else {
			this.add( new javax.swing.JCheckBox( "show this warning at start up ") );
		}
	}
	
	public static void main( String[] args ) {
		WarningPane warningPane = new WarningPane( false );
		javax.swing.JOptionPane.showMessageDialog( null, warningPane, "Alice3 is currently under development", javax.swing.JOptionPane.WARNING_MESSAGE );
	}
}
