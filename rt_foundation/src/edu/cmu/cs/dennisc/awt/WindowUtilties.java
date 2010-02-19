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
package edu.cmu.cs.dennisc.awt;

/**
 * @author Dennis Cosgrove
 */
public class WindowUtilties {
	public static void setLocationOnScreenToCenteredWithin( java.awt.Window window, java.awt.Component root ) {
		java.awt.Dimension sizeDialog = window.getSize();
		java.awt.Rectangle boundsRoot;
		if( root != null && root.isShowing() ) {
			java.awt.Point locationRoot = root.getLocationOnScreen();
			java.awt.Dimension sizeRoot = root.getSize();
			boundsRoot = new java.awt.Rectangle( locationRoot, sizeRoot );
		} else {
			//todo: handle multiple screens better?
			boundsRoot = window.getGraphicsConfiguration().getBounds();
		}
		int x = boundsRoot.x + (boundsRoot.width - sizeDialog.width) / 2;
		int y = boundsRoot.y + (boundsRoot.height - sizeDialog.height) / 2;
		window.setLocation( x, y );
		//ensureTopLeftCornerIsOnScreen( window );
	}

	public static void ensureTopLeftCornerIsOnScreen( final java.awt.Window window ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "ensureTopLeftCornerIsOnScreen", window.isValid(), window.isVisible() );
		assert window != null;
		if( /*window.isValid() &&*/ window.isVisible() ) {
			java.awt.Point ptScreen = window.getLocationOnScreen();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( ptScreen );
			if( ptScreen.x < 0 || ptScreen.y < 0 ) {
				java.awt.Point ptParent = window.getLocation();
				ptParent.x -= Math.min( ptScreen.x, 0 );
				ptParent.y -= Math.min( ptScreen.y, 0 );
				window.setLocation( ptParent );
			}
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					WindowUtilties.ensureTopLeftCornerIsOnScreen( window );
				}
			} );
		}
	}
}
