/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide;

import edu.wustl.lookingglass.utilities.memory.HeapWatchDog;

/**
 * @author Dennis Cosgrove
 */
public class EntryPoint {
	private static final String NIMBUS_LOOK_AND_FEEL_NAME = "Nimbus";
	private static final String MENU_BAR_UI_NAME = "MenuBarUI";

	private static HeapWatchDog heapMonitor;

	public static void main( final String[] args ) {
		final edu.cmu.cs.dennisc.crash.CrashDetector crashDetector = new edu.cmu.cs.dennisc.crash.CrashDetector( EntryPoint.class );
		if( crashDetector.isPreviouslyOpenedButNotSucessfullyClosed() ) {
			String propertyName = "org.alice.stageide.isCrashDetectionDesired";
			String isCrashDetectionDesiredText = System.getProperty( propertyName, "true" );
			if( "true".equals( isCrashDetectionDesiredText.toLowerCase( java.util.Locale.ENGLISH ) ) ) {
				javax.swing.JOptionPane.showMessageDialog( null, "Alice did not successfully close last time." );
			}
		}
		crashDetector.open();

		String text = org.lgna.project.ProjectVersion.getCurrentVersionText()/* + " BETA" */;
		System.out.println( "version: " + text );

		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {

				if( edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.isInstalledLookAndFeelNamed( NIMBUS_LOOK_AND_FEEL_NAME ) ) {
					final Object macMenuBarUI;
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
						if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "apple.laf.useScreenMenuBar" ) ) {
							macMenuBarUI = javax.swing.UIManager.get( MENU_BAR_UI_NAME );
						} else {
							macMenuBarUI = null;
						}
					} else {
						macMenuBarUI = null;
					}
					edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( NIMBUS_LOOK_AND_FEEL_NAME );
					if( macMenuBarUI != null ) {
						javax.swing.UIManager.put( MENU_BAR_UI_NAME, macMenuBarUI );
					}
				}

				edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.scaleFontIAppropriate();

				javax.swing.UIManager.put( "ScrollBar.width", 13 );
				javax.swing.UIManager.put( "ScrollBar.incrementButtonGap", 0 );
				javax.swing.UIManager.put( "ScrollBar.decrementButtonGap", 0 );
				javax.swing.UIManager.put( "ScrollBar.thumb", edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 140 ) );

				//java.awt.Font defaultFont = new java.awt.Font( null, java.awt.Font.BOLD, 14 );
				//javax.swing.UIManager.getLookAndFeelDefaults().put( "defaultFont", defaultFont );

				edu.cmu.cs.dennisc.java.awt.ConsistentMouseDragEventQueue.pushIfAppropriate();

				final int DEFAULT_WIDTH = 1000;
				final int DEFAULT_HEIGHT = 740;
				int xLocation = 0;
				int yLocation = 0;
				int width = DEFAULT_WIDTH;
				int height = DEFAULT_HEIGHT;
				boolean isMaximizationDesired = true;
				java.io.File file = null;
				String localeString = null;
				int index = 0;
				if( args.length > 0 ) {
					if( "null".equalsIgnoreCase( args[ 0 ] ) ) {
						//pass
					} else {
						if( "-l".equalsIgnoreCase( args[ 0 ] ) ) {
							index = 1;
							if( args.length > 1 ) {
								localeString = args[ 1 ];
								index = 2;
							}
						}
					}
					if( args.length > index ) {
						file = new java.io.File( args[ index ] );
					}
					if( args.length > ( index + 2 ) ) {
						try {
							xLocation = Integer.parseInt( args[ index + 1 ] );
							yLocation = Integer.parseInt( args[ index + 2 ] );
							if( args.length > ( index + 4 ) ) {
								width = Integer.parseInt( args[ index + 3 ] );
								height = Integer.parseInt( args[ index + 4 ] );
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

				javax.swing.JFrame rootFrame = edu.cmu.cs.dennisc.javax.swing.WindowStack.getRootFrame();
				rootFrame.setLocation( xLocation, yLocation );
				rootFrame.setSize( width, height );

				if( isMaximizationDesired ) {
					rootFrame.setExtendedState( rootFrame.getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH );
				}
				if( localeString != null ) {
					System.setProperty( "org.alice.ide.locale", localeString );
					String localeTest = System.getProperty( "org.alice.ide.locale" );
					System.out.println( localeTest );
				}

				org.alice.ide.story.AliceIde ide = new org.alice.ide.story.AliceIde( crashDetector );
				if( file != null ) {
					if( file.exists() ) {
						ide.setProjectFileToLoadOnWindowOpened( file );
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "file does not exist:", file );
					}
				}
				ide.initialize( args );
				ide.getDocumentFrame().getFrame().setVisible( true );
				heapMonitor = new HeapWatchDog();
			}
		} );
	}
}
