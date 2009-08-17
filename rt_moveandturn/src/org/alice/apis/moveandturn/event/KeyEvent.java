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
package org.alice.apis.moveandturn.event;

/**
 * @author Dennis Cosgrove
 */
public class KeyEvent extends edu.cmu.cs.dennisc.pattern.event.Event< java.awt.Component > {
	private java.awt.event.KeyEvent e;
	public KeyEvent( java.awt.event.KeyEvent e ) {
		super( e.getComponent() );
		this.e = e;
	}
	
	public org.alice.apis.moveandturn.Key getKey() {
		return org.alice.apis.moveandturn.Key.get( e );
	}
	
	public Boolean isKey( org.alice.apis.moveandturn.Key key ) {
		return this.getKey() == key;
	}
//	public Boolean isKey( org.alice.apis.moveandturn.Key... keys ) {
//		for( org.alice.apis.moveandturn.Key key : keys ) {
//			if( this.isKey( key ) ) {
//				return true;
//			}
//		}
//		return false;
//	}
	public Boolean isLetter() {
		char ch = this.e.getKeyChar();
		return Character.isLetter( ch );
	}
	public Boolean isDigit() {
		char ch = this.e.getKeyChar();
		return Character.isDigit( ch );
	}
}
