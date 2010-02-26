/**
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
package org.alice.media;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;

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
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.swing.JPanel;

/**
 * @author David Culyba
 */
public class MoviePlayer extends JPanel implements ControllerListener{
	
	private Player player;
	private Component visualComponent = null;
    // controls gain, position, start, stop
    private Component controlComponent = null;
//    private boolean firstTime = true;
//    private long CachingSize = 0L;    
    private int controlPanelHeight = 0;
    private int videoWidth = 0;
    private int videoHeight = 0;
	
	public MoviePlayer(File movieFile)
	{
		this();
		this.setMovie( movieFile );
	}
	
	public MoviePlayer()
	{
		this.setLayout( null );
	}
	
	public void setMovie(File movieFile)
	{
		if (this.player != null)
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.player = null;
		}
		this.player.addControllerListener( this );
		this.init();
	}

	private MediaLocator createMediaLocator( String url ) {
		MediaLocator ml;
		if( url.indexOf( ":" ) > 0 && (ml = new MediaLocator( url )) != null ) {
			return ml;
		}

		if( url.startsWith( File.separator ) ) {
			if( (ml = new MediaLocator( "file:" + url )) != null ) {
				return ml;
			}
		} else {
			String file = "file:" + System.getProperty( "user.dir" ) + File.separator + url;
			if( (ml = new MediaLocator( file )) != null ) {
				return ml;
			}
		}

		return null;
	}
	
	public void init()
	{
		if (this.player != null)
		{
			this.player.realize();
		}
	}
	
	public void start()
	{
		if (this.player != null)
		{
			this.player.start();
		}
	}

	public void stop() 
	{
		if (this.player != null)
		{
		    this.player.stop();
		}
	}

	public void destroy() 
	{
		if (this.player != null)
		{
			this.player.close();
		}
	}
	
	public void controllerUpdate( ControllerEvent event ) {
		// If we're getting messages from a dead player, 
		// just leave
		if (player == null)
		{
			return;
		}
		
		// When the player is Realized, get the visual 
		// and control components and add them to the Applet
		if (event instanceof RealizeCompleteEvent) 
		{
			int width = 320;
			int height = 0;
			
		    if (controlComponent == null)
		    {
				if (( controlComponent = player.getControlPanelComponent()) != null)
				{
				    controlPanelHeight = controlComponent.getPreferredSize().height;
				    this.add(controlComponent);
				    height += controlPanelHeight;
				}
		    }
		    if (visualComponent == null)
		    {
				if (( visualComponent = player.getVisualComponent()) != null)
				{
				    this.add(visualComponent);
				    Dimension videoSize = visualComponent.getPreferredSize();
				    videoWidth = videoSize.width;
				    videoHeight = videoSize.height;
				    width = videoWidth;
				    height += videoHeight;
				    visualComponent.setBounds(0, 0, videoWidth, videoHeight);
				    visualComponent.invalidate();
				}
		    }
		
		    this.setBounds(0, 0, width, height);
		    if (controlComponent != null)
		    {
				controlComponent.setBounds(0, videoHeight, width, controlPanelHeight);
				controlComponent.invalidate();
		    }
		    this.player.prefetch();
		    this.revalidate();
		    
		} 
		else if (event instanceof CachingControlEvent) 
		{
		    if (player.getState() > Controller.Realizing)
		    {
		    	return;
		    }
		    // Put a progress bar up when downloading starts, 
		    // take it down when downloading ends.
		    CachingControlEvent e = (CachingControlEvent) event;
		    CachingControl cc = e.getCachingControl();
		} 
		else if (event instanceof EndOfMediaEvent) 
		{
		    // We've reached the end of the media; rewind and
		    // start over
		    player.setMediaTime(new Time(0));
		} 
		else if (event instanceof ControllerErrorEvent) 
		{
		    // Tell TypicalPlayerApplet.start() to call it a day
		    player = null;
		} 
		else if (event instanceof ControllerClosedEvent) 
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
		edu.cmu.cs.dennisc.croquet.KFrame frame = new edu.cmu.cs.dennisc.croquet.KFrame() {
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
		MoviePlayer myPlayer = new MoviePlayer(new File( "C:/Users/Administrator/Documents/testMovie.mov" ));
		myPlayer.init();
		frame.getContentPane().add( myPlayer );
		frame.setPreferredSize( new Dimension(800, 600) );
		frame.pack();
		frame.setVisible( true );
		
	}

}
