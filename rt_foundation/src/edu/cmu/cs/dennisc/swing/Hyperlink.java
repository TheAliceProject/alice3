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
public class Hyperlink extends AbstractHyperlink {
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent event) {
			String uri = Hyperlink.this.getURI();
			try {
				edu.cmu.cs.dennisc.browser.BrowserUtilities.browse( uri );
			} catch( Exception e ) {
				e.printStackTrace();
				edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( uri );
				javax.swing.JOptionPane.showMessageDialog( Hyperlink.this, "Alice was unable to launch your default browser.\n\nThe text\n\n    " + uri + "\n\nhas been copied to your clipboard so that you may paste it into the address line of your favorite web browser." );
			}
		}
	};
	public Hyperlink( String uri ) {
		this.setURI( uri );
		this.setAction( action );
	}
	public Hyperlink( java.net.URI uri ) {
		this( uri.getRawPath() );
	}
	public String getURI() {
		return (String)this.action.getValue( javax.swing.Action.NAME );
	}
	public void setURI( String uri ) {
		this.action.putValue( javax.swing.Action.NAME, uri );
	}
	public void setURI( java.net.URI uri ) {
		setURI( uri.getRawPath() );
	}
}
