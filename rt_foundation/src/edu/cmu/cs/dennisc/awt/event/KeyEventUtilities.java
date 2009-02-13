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
package edu.cmu.cs.dennisc.awt.event;

/**
 * @author Dennis Cosgrove
 */
public class KeyEventUtilities {
	public static int getQuoteControlUnquoteKey() {
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
			return java.awt.event.KeyEvent.VK_ALT;
		} else {
			return java.awt.event.KeyEvent.VK_CONTROL;
		}
	}
	public static java.awt.event.KeyEvent performPlatformFilter( java.awt.event.KeyEvent original ) {
		java.awt.event.KeyEvent rv;
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
			int keyCode = original.getKeyCode();
			switch( keyCode ) {
			case java.awt.event.KeyEvent.VK_CONTROL:
			case java.awt.event.KeyEvent.VK_ALT:
			case java.awt.event.KeyEvent.VK_META:
				rv = null;
				break;
			default:
				rv = original;
			}
			if( rv != null ) {
				//pass
			} else {
				switch( keyCode ) {
				case java.awt.event.KeyEvent.VK_CONTROL:
					keyCode = java.awt.event.KeyEvent.VK_ALT;
					break;
				case java.awt.event.KeyEvent.VK_ALT:
				case java.awt.event.KeyEvent.VK_META:
					keyCode = java.awt.event.KeyEvent.VK_CONTROL;
					break;
				}
				int completeModifiers = InputEventUtilities.getCompleteModifiers( original );
				int filteredModifiers = InputEventUtilities.performPlatformModifiersFilter( completeModifiers );
				rv = new java.awt.event.KeyEvent(original.getComponent(), original.getID(), original.getWhen(), filteredModifiers, original.getKeyCode(), original.getKeyChar() );
			}
		} else {
			rv = original;
		}
		return rv;
	}
}
