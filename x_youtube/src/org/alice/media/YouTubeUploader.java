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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingworker.SwingWorker;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * @author David Culyba
 */
public class YouTubeUploader {
	private static final String UPLOAD_SUCCESS_MESSAGE = "Successfully uploaded video to YouTube";
	
	private static final String APPLICATION_KEY =  "ytapi-AliceTeamCarnegi-Alice3-4q4h8emc-0";
	private static final String DEVELOPER_ID = "AI39si59bgfz7SjW6wtREIkRBHmlKeFAmh_3_cV-qtenqZBdputuLmxUfX-euZBl32qcP2xLeztXlffaoQl3ROxVB4IRTwKJ_g";

	private List<YouTubeListener> listeners = new LinkedList<YouTubeListener>();
	
	private YouTubeService service = null;
	private URL uploadURL;
	
	
	public YouTubeUploader()
	{
		initializeService();
	}
	
	private void initializeService()
	{
		this.service = new YouTubeService(APPLICATION_KEY, DEVELOPER_ID);
	}
	
	public void logIn(String username, String password) throws AuthenticationException
	{
		this.service.setUserCredentials(username, password);
	}
	
	public void removeYouTubeListener(YouTubeListener listener)
	{
		this.listeners.remove( listener );
	}
	
	public void addYouTubeListener(YouTubeListener listener)
	{
		if (!this.listeners.contains( listener ))
		{
			this.listeners.add(listener);
		}
	}
	
	public void cancelUpload()
	{
		if (this.uploadURL != null)
		{
			try
			{
				this.service.delete( this.uploadURL );
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (ServiceException e)
			{
				e.printStackTrace();
			}
			this.uploadURL = null;
		}
	}
	
	/**
	   * Uploads a new video to YouTube.
	   * 
	   * @param service An authenticated YouTubeService object.
	   * @throws IOException Problems reading user input.
	   */
	public void uploadVideo(final VideoEntry videoEntry) throws IOException 
	{
		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
			@Override
			public Boolean doInBackground()
			{
				for (YouTubeListener l : YouTubeUploader.this.listeners)
				{
					l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.UploadStarted) );
				}
				String message = "";
				boolean success = false;
				try 
				{
					uploadURL = new URL("http://uploads.gdata.youtube.com/feeds/api/users/default/uploads");
					YouTubeUploader.this.service.insert(uploadURL, videoEntry);
					message = UPLOAD_SUCCESS_MESSAGE;
					success = true;
				} 
				catch (ServiceException se)
				{
				  message = se.getResponseBody();
				} 
				catch( MalformedURLException e ) 
				{
					message = e.getMessage();
				} 
				catch( IOException e ) 
				{
					message = e.getMessage();
				}
				for (YouTubeListener l : YouTubeUploader.this.listeners)
				{
					if (success)
					{
						l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.UploadSucces, message) );
					}
					else
					{
						l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.UploadFailed, message) );
					}
				}
				return new Boolean(success);
			}
			
			@Override
			public void done()
			{
			}
		};
		worker.execute();
	}
}
