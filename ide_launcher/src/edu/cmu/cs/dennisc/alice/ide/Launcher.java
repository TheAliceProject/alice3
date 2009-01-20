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
package edu.cmu.cs.dennisc.alice.ide;

/**
 * @author Dennis Cosgrove
 */
class Icon extends javax.swing.ImageIcon {
	Icon( java.net.URL url ) {
		super( url );
	}
	@Override
	public synchronized void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		super.paintIcon( c, g, x, y );
		int w = this.getIconWidth();
		int h = this.getIconHeight();
		g.setColor( new java.awt.Color( 0x379bd5 ) );
		g.drawString( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText(), x + (int)(w*0.45), y + (int)(h*0.75) );
	}
}

/**
 * @author Dennis Cosgrove
 */
class Launcher implements Runnable {
	private static edu.cmu.cs.dennisc.swing.SplashScreen s_splashScreen;

	public static void hideSplashScreenIfNecessary() {
		if( s_splashScreen != null ) {
			s_splashScreen.setVisible( false );
			s_splashScreen = null;
		}
	}


	protected javax.swing.Icon getIcon() {
		return new Icon( this.getClass().getResource( "images/SplashScreen.png" ) );
	}
	public void run() {
		s_splashScreen = new edu.cmu.cs.dennisc.swing.SplashScreen( null, getIcon() );
		s_splashScreen.setVisible( true );
	}

	public static void main( String[] args ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		javax.swing.SwingUtilities.invokeAndWait( new Launcher() );
		org.python.util.jython.main( args );
	}
}
