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

package org.alice.apis.moveandturn.inputpanes;
/**
 * @author Dennis Cosgrove
 */
public abstract class InputPane< E > extends zoot.ZInputPane< E > {
	private zoot.ZLabel messageLabel;
	public InputPane( String message ) {
		this.messageLabel = zoot.ZLabel.acquire( message );
		this.messageLabel.setFontToScaledFont( 1.5f );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.messageLabel, java.awt.BorderLayout.NORTH );
		
	}
	protected zoot.ZLabel getMessageLabel() {
		return this.messageLabel;
	}
	@Override
	protected boolean isCancelDesired() {
		return false;
	}
}
