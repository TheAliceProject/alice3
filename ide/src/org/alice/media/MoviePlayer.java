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
package org.alice.media;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.media.CachingControl;
import javax.media.CachingControlEvent;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.swing.JPanel;

/**
 * @author David Culyba
 */
public class MoviePlayer extends JPanel implements ControllerListener {

	private Player player;
	private Component visualComponent = null;
	// controls gain, position, start, stop
	private Component controlComponent = null;
	//    private boolean firstTime = true;
	//    private long CachingSize = 0L;    
	private int controlPanelHeight = 0;
	private int videoWidth = 0;
	private int videoHeight = 0;

	public MoviePlayer( File movieFile )
	{
		this();
		this.setMovie( movieFile );
	}

	public MoviePlayer()
	{
		this.setLayout( null );
	}

	public void setMovie( File movieFile )
	{
		if( this.player != null )
		{
			this.player.close();
			this.player.removeControllerListener( this );
			this.player.deallocate();
			this.visualComponent = null;
			this.controlComponent = null;
		}
		MediaLocator mediaLocator = createMediaLocator( "file://" + movieFile.getAbsolutePath() );
		try
		{
			this.player = Manager.createPlayer( mediaLocator );
		} catch( Exception e )
		{
			e.printStackTrace();
			this.player = null;
		}
		this.player.addControllerListener( this );
		this.init();
	}

	private MediaLocator createMediaLocator( String url ) {
		MediaLocator ml;
		if( url.indexOf( ":" ) > 0 ) {
			return new MediaLocator( url );
		}

		if( url.startsWith( File.separator ) ) {
			return new MediaLocator( "file:" + url );
		} else {
			String file = "file:" + System.getProperty( "user.dir" ) + File.separator + url;
			return new MediaLocator( file );
		}
	}

	public void init()
	{
		if( this.player != null )
		{
			this.player.realize();
		}
	}

	public void start()
	{
		if( this.player != null )
		{
			this.player.start();
		}
	}

	public void stop()
	{
		if( this.player != null )
		{
			this.player.stop();
		}
	}

	public void close()
	{
		if( this.player != null )
		{
			this.player.close();
			this.player.removeControllerListener( this );
			this.player.deallocate();
			this.visualComponent = null;
			this.controlComponent = null;
			this.player = null;
		}
	}

	public void controllerUpdate( ControllerEvent event ) {
		// If we're getting messages from a dead player, 
		// just leave
		if( player == null )
		{
			return;
		}

		// When the player is Realized, get the visual 
		// and control components and add them to the Applet
		if( event instanceof RealizeCompleteEvent )
		{
			int width = 320;
			int height = 0;

			if( controlComponent == null )
			{
				if( ( controlComponent = player.getControlPanelComponent() ) != null )
				{
					controlPanelHeight = controlComponent.getPreferredSize().height;
					this.add( controlComponent );
					height += controlPanelHeight;
				}
			}
			if( visualComponent == null )
			{
				if( ( visualComponent = player.getVisualComponent() ) != null )
				{
					this.add( visualComponent );
					Dimension videoSize = visualComponent.getPreferredSize();
					videoWidth = videoSize.width;
					videoHeight = videoSize.height;
					width = videoWidth;
					height += videoHeight;
					visualComponent.setBounds( 0, 0, videoWidth, videoHeight );
					visualComponent.invalidate();
				}
			}

			this.setBounds( 0, 0, width, height );
			if( controlComponent != null )
			{
				controlComponent.setBounds( 0, videoHeight, width, controlPanelHeight );
				controlComponent.invalidate();
			}
			this.player.prefetch();
			this.revalidate();

		}
		else if( event instanceof CachingControlEvent )
		{
			if( player.getState() > Controller.Realizing )
			{
				return;
			}
			// Put a progress bar up when downloading starts, 
			// take it down when downloading ends.
			CachingControlEvent e = (CachingControlEvent)event;
			CachingControl cc = e.getCachingControl();
		}
		else if( event instanceof EndOfMediaEvent )
		{
			// We've reached the end of the media; rewind and
			// start over
			player.setMediaTime( new Time( 0 ) );
		}
		else if( event instanceof ControllerErrorEvent )
		{
			// Tell TypicalPlayerApplet.start() to call it a day
			player = null;
		}
		else if( event instanceof ControllerClosedEvent )
		{
			this.removeAll();
		}
		//		else if (event instanceof PrefetchCompleteEvent)
		//		{
		//			if (this.visualComponent != null)
		//			{
		//				this.visualComponent.invalidate();
		//			}
		//			if (this.controlComponent != null)
		//			{
		//				this.controlComponent.invalidate();
		//			}
		//			this.revalidate();
		//		}

	}

	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.javax.swing.ApplicationFrame frame = new edu.cmu.cs.dennisc.javax.swing.ApplicationFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}

			@Override
			protected void handleAbout( java.util.EventObject e ) {
			}

			@Override
			protected void handlePreferences( java.util.EventObject e ) {
			}

			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		MoviePlayer myPlayer = new MoviePlayer( new File( "C:/Users/Administrator/Documents/testMovie.mov" ) );
		myPlayer.init();
		frame.getContentPane().add( myPlayer );
		frame.setPreferredSize( new Dimension( 800, 600 ) );
		frame.pack();
		frame.setVisible( true );

	}

}
