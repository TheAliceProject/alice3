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
	public static void launch( final Class<? extends IDE> cls, final java.awt.Window splashScreen, final String[] args ) {
		if( splashScreen != null ) {
			try {
				javax.swing.SwingUtilities.invokeAndWait( new Runnable() {
					public void run() {
						splashScreen.setVisible( true );
					}
				} );
			} catch( InterruptedException ie ) {
				ie.printStackTrace();
			} catch( java.lang.reflect.InvocationTargetException ite ) {
				ite.printStackTrace();
			}
		}
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				for( String path : new String[] { "./application/classinfos.zip", "./application/classinfos" } ) {
					java.io.File file = new java.io.File( path );
					edu.cmu.cs.dennisc.print.PrintUtilities.println( file.getAbsolutePath() );
					if( file.exists() ) {
						long t0 = System.currentTimeMillis();
						edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.addClassInfosFrom( file );
						long tDelta = System.currentTimeMillis() - t0;
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "ClassInfoManager", tDelta );
						break;
					}
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
