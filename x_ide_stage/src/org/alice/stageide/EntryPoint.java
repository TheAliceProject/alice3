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
package org.alice.stageide;

/**
 * @author Dennis Cosgrove
 */
class SplashIcon extends javax.swing.ImageIcon {
	SplashIcon( java.net.URL url ) {
		super( url );
	}
	@Override
	public synchronized void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		super.paintIcon( c, g, x, y );
		int w = this.getIconWidth();
		int h = this.getIconHeight();
		g.setColor( new java.awt.Color( 0x379bd5 ) );
		g.drawString( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()/* + " BETA"*/, x + (int)(w*0.45), y + (int)(h*0.75) );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class EntryPoint {
	public static void main( final String[] args ) {
		System.out.println( "version: " + edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()/* + " BETA" */);
		javax.swing.Icon icon = new SplashIcon( EntryPoint.class.getResource( "images/SplashScreen.png" ) );
		java.awt.Window splashScreen = new edu.cmu.cs.dennisc.swing.SplashScreen( null, icon );
		org.alice.ide.LaunchUtilities.launch( StageIDE.class, splashScreen, args );
	}
}
