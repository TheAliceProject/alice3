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
public class MouseEventUtilities {
	public static boolean isQuoteLeftUnquoteMouseButton( java.awt.event.MouseEvent e ) {
		if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
			if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
				return e.isControlDown() == false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	public static boolean isQuoteRightUnquoteMouseButton( java.awt.event.MouseEvent e ) {
		if( javax.swing.SwingUtilities.isRightMouseButton( e ) ) {
			return true;
		} else {
			if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
				if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
					return e.isControlDown();
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
}
