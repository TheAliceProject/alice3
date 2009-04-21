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
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class LaunchUtilities {
	public static void launch( final Class<? extends IDE> cls, final java.awt.Window splashScreen, final String[] args ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		if( splashScreen != null ) {
			javax.swing.SwingUtilities.invokeAndWait( new Runnable() {
				public void run() {
					splashScreen.setVisible( true );
				}
			} );
		}
		javax.swing.SwingUtilities.invokeAndWait( new Runnable() {
			public void run() {
				java.io.File classInfoDirectory = new java.io.File( "./application/classinfos" );
				if( classInfoDirectory.exists() ) {
					edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.setDirectory( classInfoDirectory );
				}
				IDE ide = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cls );
				if( args.length > 0 ) {
					ide.loadProjectFrom( new java.io.File( args[ 0 ] ) );
//				} else {
//					ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
				}
				ide.setSize( 1000, 740 );
				
				if( "false".equals( System.getProperty( IDE.class.getName() + ".isMaximized" ) ) ) {
					//pass
				} else {
					ide.maximize();
				}
				ide.setSplashScreen( splashScreen );
				ide.setVisible( true );
			}
		} );
		
	}
	
}
