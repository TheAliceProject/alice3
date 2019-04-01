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
package org.alice.media.youtube.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.geo.impl.GeoRssWhere;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeMediaRating;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.data.youtube.YtStatistics;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * @author David Culyba
 */
public class YouTubeUploader {
	private static final String UPLOAD_SUCCESS_MESSAGE = "Successfully uploaded video to YouTube";

	private static final String APPLICATION_KEY = "ytapi-AliceTeamCarnegi-Alice3-4q4h8emc-0";
	private static final String DEVELOPER_ID = "AI39si59bgfz7SjW6wtREIkRBHmlKeFAmh_3_cV-qtenqZBdputuLmxUfX-euZBl32qcP2xLeztXlffaoQl3ROxVB4IRTwKJ_g";

	private List<YouTubeListener> listeners = new LinkedList<YouTubeListener>();

	private YouTubeService service = null;
	private URL uploadURL;

	public YouTubeUploader() {
		initializeService();
	}

	private void initializeService() {
		this.service = new YouTubeService( APPLICATION_KEY, DEVELOPER_ID );
	}

	public void logIn( String username, String password ) throws AuthenticationException {
		this.service.setUserCredentials( username, password );
	}

	public void removeYouTubeListener( YouTubeListener listener ) {
		this.listeners.remove( listener );
	}

	public void addYouTubeListener( YouTubeListener listener ) {
		if( !this.listeners.contains( listener ) ) {
			this.listeners.add( listener );
		}
	}

	public void cancelUpload() {
		if( this.uploadURL != null ) {
			try {
				this.service.delete( this.uploadURL );
			} catch( IOException e ) {
				e.printStackTrace();
			} catch( ServiceException e ) {
				e.printStackTrace();
			}
			this.uploadURL = null;
		}
	}

	public static void printVideoEntry( VideoEntry videoEntry, boolean detailed ) {
		System.out.println( "Title: " + videoEntry.getTitle().getPlainText() );
		System.out.println( "Creation time: " + videoEntry.getRecorded() );
		System.out.println( "Published time: " + videoEntry.getPublished() );
		if( videoEntry.isDraft() ) {
			System.out.println( "Video is not live" );
			YtPublicationState pubState = videoEntry.getPublicationState();
			if( pubState.getState() == YtPublicationState.State.PROCESSING ) {
				System.out.println( "Video is still being processed." );
			} else if( pubState.getState() == YtPublicationState.State.REJECTED ) {
				System.out.print( "Video has been rejected because: " );
				System.out.println( pubState.getDescription() );
				System.out.print( "For help visit: " );
				System.out.println( pubState.getHelpUrl() );
			} else if( pubState.getState() == YtPublicationState.State.FAILED ) {
				System.out.print( "Video failed uploading because: " );
				System.out.println( pubState.getDescription() );
				System.out.print( "For help visit: " );
				System.out.println( pubState.getHelpUrl() );
			}
		}

		if( videoEntry.getEditLink() != null ) {
			System.out.println( "Video is editable by current user." );
		}

		if( detailed ) {

			YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();

			System.out.println( "Uploaded by: " + mediaGroup.getUploader() );

			System.out.println( "Video ID: " + mediaGroup.getVideoId() );
			System.out.println( "Description: " +
					mediaGroup.getDescription().getPlainTextContent() );

			MediaPlayer mediaPlayer = mediaGroup.getPlayer();
			if( mediaPlayer != null ) {
				System.out.println( "Web Player URL: " + mediaPlayer.getUrl() );
			}
			MediaKeywords keywords = mediaGroup.getKeywords();
			System.out.print( "Keywords: " );
			for( String keyword : keywords.getKeywords() ) {
				System.out.print( keyword + "," );
			}

			GeoRssWhere location = videoEntry.getGeoCoordinates();
			if( location != null ) {
				System.out.println( "Latitude: " + location.getLatitude() );
				System.out.println( "Longitude: " + location.getLongitude() );
			}

			Rating rating = videoEntry.getRating();
			if( rating != null ) {
				System.out.println( "Average rating: " + rating.getAverage() );
			}

			YtStatistics stats = videoEntry.getStatistics();
			if( stats != null ) {
				System.out.println( "View count: " + stats.getViewCount() );
			}
			System.out.println();

			System.out.println( "\tThumbnails:" );
			for( MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails() ) {
				System.out.println( "\t\tThumbnail URL: " + mediaThumbnail.getUrl() );
				System.out.println( "\t\tThumbnail Time Index: " +
						mediaThumbnail.getTime() );
				System.out.println();
			}

			System.out.println( "\tMedia:" );
			for( YouTubeMediaContent mediaContent : mediaGroup.getYouTubeContents() ) {
				System.out.println( "\t\tMedia Location: " + mediaContent.getUrl() );
				System.out.println( "\t\tMedia Type: " + mediaContent.getType() );
				System.out.println( "\t\tDuration: " + mediaContent.getDuration() );
				System.out.println();
			}

			for( YouTubeMediaRating mediaRating : mediaGroup.getYouTubeRatings() ) {
				System.out.println( "Video restricted in the following countries: " +
						mediaRating.getCountries().toString() );
			}
		}
	}

	//	public void getUploadedVideo()
	//	{
	//		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
	//			@Override
	//			public Boolean doInBackground()
	//			{
	//				String feedUrl = "http://gdata.youtube.com/feeds/api/users/default/uploads";
	//				try
	//				{
	//					VideoFeed videoFeed = service.getFeed(new URL(feedUrl), VideoFeed.class);
	//					VideoEntry uploadedVideo = null;
	//					DateTime dt = null;
	//					for (VideoEntry v : videoFeed.getEntries())
	//					{
	//						printVideoEntry(v, true);
	//						if (uploadedVideo == null)
	//						{
	//							uploadedVideo = v;
	//						}
	//						else if (v.getPublished().compareTo( uploadedVideo.getPublished() ) > 0)
	//						{
	//							uploadedVideo = v;
	//						}
	//					}
	//					System.out.println("\n\nSelected:");
	//					printVideoEntry(uploadedVideo, true);
	//					for (YouTubeListener l : YouTubeUploader.this.listeners)
	//					{
	//						l.youTubeEventTriggered( new YouTubeEvent(YouTubeEvent.EventType.LinkRetrieved, uploadedVideo ) );
	//					}
	//				}
	//				catch (Exception e)
	//				{
	//					e.printStackTrace();
	//				}
	//				return Boolean.TRUE;
	//			}
	//
	//		};
	//		worker.execute();
	//	}

	/**
	 * Uploads a new video to YouTube.
	 *
	 * @param videoEntry A video entry for the youtube feeds.
	 * @throws IOException Problems reading user input.
	 */
	public void uploadVideo( final VideoEntry videoEntry ) throws IOException {
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
			@Override
			public Boolean doInBackground() {
				for( YouTubeListener l : YouTubeUploader.this.listeners ) {
					l.youTubeEventTriggered( new YouTubeEvent( YouTubeEvent.EventType.UPLOAD_STARTED ) );
				}
				String message = "";
				boolean success = false;
				VideoEntry uploadedVideo = null;
				try {
					uploadURL = new URL( "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads" );
					uploadedVideo = YouTubeUploader.this.service.insert( uploadURL, videoEntry );
					message = UPLOAD_SUCCESS_MESSAGE;
					success = true;
				} catch( ServiceException se ) {
					System.out.println( "ServiceException" );
					System.out.println( se.getMessage() );
					message = se.getResponseBody();
				} catch( MalformedURLException e ) {
					System.out.println( "malformed" );
					message = e.getMessage();
				} catch( IOException e ) {
					System.out.println( "ioException" );
					message = e.getMessage();
				}
				for( YouTubeListener l : YouTubeUploader.this.listeners ) {
					if( success ) {
						l.youTubeEventTriggered( new YouTubeEvent( YouTubeEvent.EventType.UPLOAD_SUCCESS, uploadedVideo ) );
					} else {
						l.youTubeEventTriggered( new YouTubeEvent( YouTubeEvent.EventType.UPLOAD_FAILED, message ) );
					}
				}
				return new Boolean( success );
			}

			@Override
			public void done() {
			}
		};
		worker.execute();
	}
}
