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
						splashScreen.toBack();
						splashScreen.repaint();
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
				String installDir = System.getProperty( "org.alice.ide.IDE.install.dir" );
				if( installDir != null ) {
					java.io.File applicationRootDirectory = new java.io.File( installDir, "application" );
					if( applicationRootDirectory != null && applicationRootDirectory.exists() ) {
						for( String path : new String[] { "classinfos.zip", "classinfos" } ) {
							java.io.File file = new java.io.File( applicationRootDirectory, path );
							if( file.exists() ) {
								edu.cmu.cs.dennisc.print.PrintUtilities.println( "reading class information:", file.getAbsolutePath() );
								//long t0 = System.currentTimeMillis();
								edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.addClassInfosFrom( file );
								//long tDelta = System.currentTimeMillis() - t0;
								break;
							}
						}
					}
				} else {
					StringBuffer sb = new StringBuffer();
					sb.append( "-Dorg.alice.ide.IDE.install.dir=\"" );
					if( edu.cmu.cs.dennisc.lang.SystemUtilities.isWindows() ) {
						sb.append( "/Program Files" );
					} else if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
						sb.append( "/Applications" );
					} else {
						sb.append( System.getProperty( "user.home" ) );
					}
					sb.append( "/Alice3Beta\"" );
					edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( sb.toString() );
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "The text below has been copied to the clipboard for your convenience." );
					edu.cmu.cs.dennisc.print.PrintUtilities.println( sb );
				}
				IDE ide = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cls );
//				java.io.File applicationRootDirectory = ide.getApplicationRootDirectory();
//				if( applicationRootDirectory != null && applicationRootDirectory.exists() ) {
//					for( String path : new String[] { "classinfos.zip", "classinfos" } ) {
//						java.io.File file = new java.io.File( applicationRootDirectory, path );
//						if( file.exists() ) {
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( file.getAbsolutePath() );
//							//long t0 = System.currentTimeMillis();
//							edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.addClassInfosFrom( file );
//							//long tDelta = System.currentTimeMillis() - t0;
//							break;
//						}
//					}
//				}
				final int DEFAULT_WIDTH = 1000;
				final int DEFAULT_HEIGHT = 740;
				int xLocation = 0;
				int yLocation = 0;
				int width = DEFAULT_WIDTH;
				int height = DEFAULT_HEIGHT;
				boolean isMaximizationDesired = true;
				if( args.length > 0 ) {
					java.io.File file = new java.io.File( args[ 0 ] );
					if( file.exists() ) {
						ide.loadProjectFrom( file );
					} else {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "file does not exist:", file );
					}
					if( args.length > 2 ) {
						try {
							xLocation = Integer.parseInt( args[ 1 ] );
							yLocation = Integer.parseInt( args[ 2 ] );
							if( args.length > 4 ) {
								width = Integer.parseInt( args[ 3 ] );
								height = Integer.parseInt( args[ 4 ] );
							}
							isMaximizationDesired = false;
						} catch( NumberFormatException nfe ) {
							xLocation = 0;
							yLocation = 0;
							width = DEFAULT_WIDTH;
							height = DEFAULT_HEIGHT;
						}
					}
				}
				ide.setLocation( xLocation, yLocation );
				ide.setSize( width, height );
				
				if( isMaximizationDesired ) {
					ide.maximize();
				}
				ide.setSplashScreen( splashScreen );
				ide.setVisible( true );
			}
		} );
		
	}
	
}
