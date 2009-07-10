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
package org.alice.ide.operations.help;

/**
 * @author Dennis Cosgrove
 */
public class HelpOperation extends HTMLMessageOperation {
	public HelpOperation() {
		this.putValue( javax.swing.Action.NAME, "Help..." );
		this.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_F1, 0 ) );
	}
	@Override
	protected StringBuffer getMessage( StringBuffer rv ) {
		String url = "http://kenai.com/projects/alice/pages/Help";
		rv.append( "Help is available " );
		rv.append( "<a href=\"" );
		rv.append( url );
		rv.append( "\">" );
		rv.append( "on the web" );
		rv.append( "</a>" );
		rv.append( "." );
		return rv;
	}
	@Override
	protected String getTitle() {
		return "Help";
	}
	@Override
	protected int getMessageType() {
		return javax.swing.JOptionPane.PLAIN_MESSAGE;
	}
}
