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
package edu.cmu.cs.dennisc.ui.html;

/**
 * @author Dennis Cosgrove
 */
public class HTMLPane extends javax.swing.JEditorPane {
	private javax.swing.event.HyperlinkListener hyperlinkAdapter = new javax.swing.event.HyperlinkListener() {
		public void hyperlinkUpdate( javax.swing.event.HyperlinkEvent e ) {
			if( e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED ) {
				try {
					edu.cmu.cs.dennisc.browser.BrowserUtilities.browse( e.getURL() );
				} catch( Exception exc ) {
					exc.printStackTrace();
				}
			}
		}
	};
	public HTMLPane() {
		this.setEditable( false );
		this.setContentType( "text/html" );
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.addHyperlinkListener( this.hyperlinkAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeHyperlinkListener( this.hyperlinkAdapter );
		super.removeNotify();
	}
	
	public static void main( String[] args ) throws Exception {
		String text = "<html><a href=http://www.alice.org/3>alice3</a></html>";
		HTMLPane htmlPane = new HTMLPane();
		htmlPane.setText( text );
		//htmlPane.setPage( "http://www.alice.org" );
		javax.swing.JOptionPane.showMessageDialog( null, htmlPane );
	}
}
