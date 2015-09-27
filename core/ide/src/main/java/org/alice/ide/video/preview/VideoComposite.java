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
package org.alice.ide.video.preview;

/**
 * @author Dennis Cosgrove
 */
public final class VideoComposite extends org.lgna.croquet.SimpleComposite<org.alice.ide.video.preview.views.VideoView> {
	private final org.lgna.croquet.Operation togglePlayPauseOperation = this.createActionOperation( "togglePlayPauseOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			if( getView().isErrorFreeSinceLastPrepareMedia() ) {
				edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer = getView().getVideoPlayer();
				if( videoPlayer.isPlaying() ) {
					videoPlayer.pause();
				} else {
					videoPlayer.playResume();
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
			}
			return null;
		}
	} );

	public VideoComposite() {
		super( java.util.UUID.fromString( "ffa047e2-9bce-4a46-8a16-70c19ced4d00" ) );
	}

	public org.lgna.croquet.Operation getTogglePlayPauseOperation() {
		return this.togglePlayPauseOperation;
	}

	@Override
	protected org.alice.ide.video.preview.views.VideoView createView() {
		return new org.alice.ide.video.preview.views.VideoView( this );
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		final java.net.URL SECURE_URL = new java.net.URL( "https://lookingglass.wustl.edu/videos/projects/483.webm?1382676625" );
		final boolean IS_SIMPLE_TEST_DESIRED = false;
		if( IS_SIMPLE_TEST_DESIRED ) {
			final boolean IS_APPLICATION_ROOT_DESIRED = true;
			edu.cmu.cs.dennisc.video.VideoPlayer videoPlayer;
			if( IS_APPLICATION_ROOT_DESIRED ) {
				videoPlayer = edu.cmu.cs.dennisc.video.vlcj.VlcjUtilities.createVideoPlayer();
			} else {
				final boolean IS_NATIVE_DISCOVERY_DESIRED = false;
				if( IS_NATIVE_DISCOVERY_DESIRED ) {
					uk.co.caprica.vlcj.discovery.NativeDiscovery nativeDiscovery = new uk.co.caprica.vlcj.discovery.NativeDiscovery();
					if( nativeDiscovery.discover() ) {
						//pass
					} else {
						System.err.println( "vlcj native discovery failed" );
					}
				} else {
					if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
						if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.is64Bit() ) {
							com.sun.jna.NativeLibrary.addSearchPath( uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files/VideoLAN/VLC" );
						} else {
							throw new RuntimeException();
						}
					} else {
						throw new RuntimeException();
					}
				}
				com.sun.jna.Native.loadLibrary( uk.co.caprica.vlcj.runtime.RuntimeUtil.getLibVlcLibraryName(), uk.co.caprica.vlcj.binding.LibVlc.class );
				videoPlayer = new edu.cmu.cs.dennisc.video.vlcj.VlcjVideoPlayer();
			}

			javax.swing.JFrame frame = new javax.swing.JFrame( "vlcj test" );
			frame.setLocation( 100, 100 );
			frame.setSize( 1050, 600 );
			frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
			frame.getContentPane().add( videoPlayer.getVideoSurface(), java.awt.BorderLayout.CENTER );
			frame.setVisible( true );

			videoPlayer.prepareMedia( SECURE_URL.toURI() );
			videoPlayer.playResume();
		} else {
			final java.net.URI uriA;
			final java.net.URI uriB;
			if( args.length > 0 ) {
				uriA = new java.net.URI( args[ 0 ] );
				uriB = new java.net.URI( args[ 1 ] );
			} else {
				java.io.File directory = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
				if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
					directory = directory.getParentFile();
				}
				java.io.File fileB = new java.io.File( directory, "Videos/b.webm" );
				final boolean IS_SECURE_URL_TEST_DESIRED = true;
				if( IS_SECURE_URL_TEST_DESIRED ) {
					uriA = SECURE_URL.toURI();
				} else {
					java.io.File fileA = new java.io.File( directory, "Videos/c.webm" );
					uriA = fileA.toURI();
				}
				uriB = fileB.toURI();
			}
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
					org.lgna.croquet.DocumentFrame documentFrame = app.getDocumentFrame();
					org.lgna.croquet.views.Frame frame = documentFrame.getFrame();

					final VideoComposite videoComposite = new VideoComposite();
					videoComposite.getView().setUri( uriA );
					frame.setMainComposite( videoComposite );

					javax.swing.Action action = new javax.swing.AbstractAction() {
						@Override
						public void actionPerformed( java.awt.event.ActionEvent e ) {
							videoComposite.getView().setUri( uriB );
						}
					};
					action.putValue( javax.swing.Action.NAME, "set second video" );
					frame.getMainComposite().getView().getAwtComponent().add( new javax.swing.JButton( action ), java.awt.BorderLayout.PAGE_START );

					frame.pack();
					frame.setVisible( true );

					final boolean IS_SNAPSHOT_TEST = false;
					if( IS_SNAPSHOT_TEST ) {
						new Thread() {
							@Override
							public void run() {
								float tPrev = Float.NaN;
								while( true ) {
									float tCurr = videoComposite.getView().getVideoPlayer().getPosition();
									if( tCurr != tPrev ) {
										edu.cmu.cs.dennisc.java.util.logging.Logger.outln( tCurr );
										tPrev = tCurr;
									}
									edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( 1 );
								}
							}
						}.start();
					}
				}
			} );
		}
	}
}
