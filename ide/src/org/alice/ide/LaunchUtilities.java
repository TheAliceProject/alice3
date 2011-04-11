/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class LaunchUtilities {
	private static java.io.File getInstallDirectory() {
		String installDir = System.getProperty( "org.alice.ide.IDE.install.dir" );
		if( installDir != null ) {
			java.io.File rv = new java.io.File( installDir ); 
			if( rv.exists() && rv.isDirectory() ) {
				return rv;
			}
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( "-Dorg.alice.ide.IDE.install.dir=\"" );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				sb.append( "/Program Files" );
			} else if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
				sb.append( "/Applications" );
			} else {
				sb.append( System.getProperty( "user.home" ) );
			}
			sb.append( "/Alice3Beta\"" );
			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( sb.toString() );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "The text below has been copied to the clipboard for your convenience." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( sb );
		}
		String userDir = System.getProperty( "user.dir" );
		if( userDir != null ) {
			java.io.File rv = new java.io.File( userDir ); 
			for( String childName : new String[] { "application", "ext", "gallery", "lib" } ) {
				java.io.File childFile = new java.io.File( rv, childName );
				if( childFile.exists() && childFile.isDirectory() ) {
					//pass
				} else {
					return null;
				}
			}
			System.setProperty( "org.alice.ide.IDE.install.dir", userDir );
			return rv;
		}
		return null;
	}
	private static void preLaunch( final java.awt.Window splashScreen ) {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
			//pass
		} else {
			javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
			if( lookAndFeelInfo != null ) {
//				javax.swing.LookAndFeel laf = javax.swing.UIManager.getLookAndFeel();
				try {
					edu.cmu.cs.dennisc.javax.swing.plaf.nimbus.NimbusUtilities.installModifiedNimbus( lookAndFeelInfo );
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			}
		}
		
		//java.awt.Font defaultFont = new java.awt.Font( null, java.awt.Font.BOLD, 14 );
		//javax.swing.UIManager.getLookAndFeelDefaults().put( "defaultFont", defaultFont );

		if( splashScreen != null ) {
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.LaunchUtilities.isSupressionOfSplashScreenDesired" ) ) {
				//pass
			} else {
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
		}
		java.io.File installDir = getInstallDirectory();
		if( installDir != null ) {
			java.io.File applicationRootDirectory = new java.io.File( installDir, "application" );
			if( applicationRootDirectory != null && applicationRootDirectory.exists() ) {
				for( String path : new String[] { "classinfos.zip", "classinfos" } ) {
					java.io.File file = new java.io.File( applicationRootDirectory, path );
					if( file.exists() ) {
						edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.addClassInfosFrom( file );
						break;
					}
				}
			}
		}
	}
	private static Runnable createRunnable( final java.awt.Window splashScreen, final String[] args, final boolean isVisible ) {
		return new Runnable() {
			public void run() {
				IDE ide = IDE.getSingleton();
				final int DEFAULT_WIDTH = 1000;
				final int DEFAULT_HEIGHT = 740;
				int xLocation = 0;
				int yLocation = 0;
				int width = DEFAULT_WIDTH;
				int height = DEFAULT_HEIGHT;
				boolean isMaximizationDesired = true;
				if( args.length > 0 ) {
					if( "null".equalsIgnoreCase( args[ 0 ] ) ) {
						//pass
					} else {
						java.io.File file = new java.io.File( args[ 0 ] );
						if( file.exists() ) {
							ide.loadProjectFrom( file );
						} else {
							edu.cmu.cs.dennisc.print.PrintUtilities.println( "file does not exist:", file );
						}
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
				ide.getFrame().setLocation( xLocation, yLocation );
				ide.getFrame().setSize( width, height );
				
				if( isMaximizationDesired ) {
					ide.getFrame().maximize();
				}
				ide.setSplashScreen( splashScreen );
				ide.initialize( args );
				ide.getFrame().setVisible( isVisible );
				
				if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "org.alice.ide.IDE.isSceneEditorExpanded" ) ) {
					org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().setValue( true );
				}
			}
		};
	}
	
	public static void launch( final Class<? extends IDE> cls, final java.awt.Window splashScreen, final String[] args ) {
		preLaunch( splashScreen );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
//				CreateIdeOperation createIdeOperation = CreateIdeOperation.getInstance( cls );
//				createIdeOperation.fire();
				IDE ide = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cls );
				Runnable runnable = createRunnable( splashScreen, args, true );
				runnable.run();
			}
		} );
	}
	public static <I extends IDE> I launchAndWait( final Class<I> cls, final java.awt.Window splashScreen, final String[] args, boolean isVisible ) throws InterruptedException, java.lang.reflect.InvocationTargetException {
		preLaunch( splashScreen );
		IDE ide = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( cls );
		Runnable runnable = createRunnable( splashScreen, args, isVisible );
		javax.swing.SwingUtilities.invokeAndWait( runnable );
		return cls.cast( IDE.getSingleton() );
	}
	
}
