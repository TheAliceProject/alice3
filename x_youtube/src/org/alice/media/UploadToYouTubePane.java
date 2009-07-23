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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingworker.SwingWorker;

import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

import edu.cmu.cs.dennisc.image.ImageUtilities;

import swing.Pane;
import zoot.ZLabel;

/**
 * @author David Culyba
 */
public class UploadToYouTubePane extends JDialog implements ActionListener, DocumentListener, YouTubeListener{
	
	private static final String ALICE_DEVELOPER_TAG = "Alice3";
	private static final String TITLE = "Upload to YouTube";
	
	private static final String READY_STATUS = "Ready to upload.";
	private static final String NOT_LOGGED_IN_STATUS = "Not logged into YouTube.";
	private static final String NO_TITLE_STATUS = "You must enter a title before uploading.";
	private static final String NO_DESCRIPTION_STATUS = "You must enter a desciption before uploading.";
	private static final String NO_TITLE_NO_DESCRIPTION_STATUS = "You must enter a title and a desciption before uploading.";
	
	private static final Color ERROR_COLOR = Color.RED;
	private static final Color HAPPY_COLOR = new Color(0.0f, .6f, 0.0f);
	private static final Color NEUTRAL_COLOR = Color.GRAY;
	
	private File videoFile;
	private VideoEntry videoEntry;
	private YouTubeUploader uploader;
	private YouTubeLoginPanel loginPanel;
	private YouTubeMediaGroupEditorPanel infoPanel;
	private UploadToYouTubeStatusPane statusPane;
	
	//private JLabel videoFileLabel;
	private JButton uploadButton;
	private ImageIcon uploadIcon;
	private ImageIcon thumbnailImage;
	private JButton exitButton;
	
	private JLabel statusLabel;
	private VideoEntry uploadedVideo;
	private YouTubeEvent youTubeResults;

	public UploadToYouTubePane(Frame owner)
	{
		super(owner);
		this.videoEntry = new VideoEntry();
		
		this.uploader = new YouTubeUploader();
		this.uploader.addYouTubeListener( this );
		
		this.loginPanel = new YouTubeLoginPanel(this.uploader);
		this.loginPanel.addYouTubeListener( this );
		
		this.infoPanel = new YouTubeMediaGroupEditorPanel();
		this.infoPanel.addDocumentListener( this );

		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		Frame frame = null;
		if( root instanceof java.awt.Frame ) 
		{
			frame  = (java.awt.Frame)root;
		} 
		else if( root instanceof java.awt.Dialog ) 
		{
			if ( ((java.awt.Dialog )root).getOwner() instanceof Frame)
			{
				frame = (java.awt.Frame)((java.awt.Dialog )root).getOwner();
			}
		}
		
		this.statusPane = new UploadToYouTubeStatusPane(frame, this.uploader);
		this.uploader.addYouTubeListener( this.statusPane );
		
		this.thumbnailImage = new ImageIcon();
		JLabel thumbnailLabel = new JLabel(this.thumbnailImage);
		
		this.exitButton = new JButton("Exit");
		this.exitButton.addActionListener( this );
		
		JLabel fileTitle = new JLabel("File to upload:");
		fileTitle.setForeground( Color.GRAY );
		
		
//		this.videoFileLabel = new JLabel();
//		this.videoFileLabel.setFont( this.videoFileLabel.getFont().deriveFont( 14f ));
//		this.videoFileLabel.setHorizontalAlignment( JLabel.CENTER );
		
		this.statusLabel = new JLabel(NOT_LOGGED_IN_STATUS);
		this.statusLabel.setForeground( ERROR_COLOR );
		this.statusLabel.setHorizontalAlignment( JLabel.CENTER );
		this.statusLabel.setVerticalAlignment( JLabel.CENTER );
		
		JPanel movieInfoPanel = new JPanel();
		movieInfoPanel.setOpaque( true );
		movieInfoPanel.setBackground( new Color(.85f, .85f, .85f) );
		movieInfoPanel.setLayout( new GridBagLayout() );
		movieInfoPanel.add( thumbnailLabel, 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		movieInfoPanel.add( this.infoPanel, 
				new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				3, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		this.uploadIcon = new ImageIcon( UploadToYouTubePane.class.getResource( "images/upload_to_youtube.png" ));
		this.uploadButton = new JButton(this.uploadIcon);
		this.uploadButton.addActionListener( this );
		this.uploadButton.setEnabled( false );
		
		
		this.setLocationRelativeTo( owner );
		this.setTitle( TITLE );
		this.setModal( true );
		
		this.getContentPane().setLayout( new GridBagLayout() );
		this.getContentPane().add( this.loginPanel, 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		this.getContentPane().add( movieInfoPanel, 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( this.statusLabel, 
				new GridBagConstraints( 
				0, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( this.uploadButton, 
				new GridBagConstraints( 
				0, //gridX
				3, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 4, 2, 8, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( this.exitButton, 
				new GridBagConstraints( 
				0, //gridX
				4, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 4, 0, 8, 8 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
	}
	
	public UploadToYouTubePane(Frame owner, File videoFile, Image thumbnail)
	{
		this(owner);
		this.setVideo( videoFile, thumbnail );
	}
	
	public boolean init()
	{
		boolean success = this.infoPanel.initialize();
		if (success)
		{
			YouTubeMediaGroup mg = this.videoEntry.getOrCreateMediaGroup();
			mg.getCategories().add(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME, ALICE_DEVELOPER_TAG)); 
			mg.setPrivate( true );
			this.infoPanel.setMediaGroup( mg );
		}
		return success;
		
	}
	
	public void setVideo(File videoFile, Image thumbnail)
	{
		this.thumbnailImage.setImage( thumbnail );
		this.videoFile = videoFile;
		//this.videoFileLabel.setText( videoFile.getName() );
	}
	
	public void enableUI(boolean enable)
	{
		this.uploadButton.setEnabled( enable );
		this.loginPanel.enableUI( enable );
		this.infoPanel.enableUI( enable );
	}
	
	public void actionPerformed( ActionEvent e ) {
		if (e.getSource() == this.uploadButton)
		{
			MediaFileSource ms = new MediaFileSource(this.videoFile, "video/quicktime");
			this.infoPanel.updateMediaGroup();
			this.videoEntry.setMediaSource(ms);
			try {
				this.uploader.uploadVideo( this.videoEntry );
			} catch( IOException e1 ) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == this.exitButton)
		{
			this.close();
		}
		
	}
	
	public UploadToYouTubeStatusPane.UploadStatus getUploadStatus()
	{
		return this.statusPane.getStatus();
	}
	
	public YouTubeEvent getYouTubeResults()
	{
		return this.youTubeResults;
	}
	
	public String getUploadDetails()
	{
		return this.statusPane.getDetails();
	}
	
	public VideoEntry getUploadedVideo()
	{
		return this.uploadedVideo;
	}
	
	public static void main( String[] args ) {
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		BufferedImage thumbnail = ImageUtilities.read( "C:/movie_dump/thumbnail.jpg");
		frame.getContentPane().add( new UploadToYouTubePane(frame, new File("C:/Users/Administrator/Documents/testMovie.mov"), thumbnail) );
		frame.pack();
		frame.setVisible( true );
	}

	public void youTubeEventTriggered( YouTubeEvent event ) {
		if (event.getType() == YouTubeEvent.EventType.UPLOAD_FAILED ||
			event.getType() == YouTubeEvent.EventType.UPLOAD_SUCCESS ||
			event.getType() == YouTubeEvent.EventType.UPLOAD_CANCELLED_SUCCESS ||
			event.getType() == YouTubeEvent.EventType.UPLOAD_CANCELLED_FAILED)
		{
			this.youTubeResults = event;
			if (event.getType() == YouTubeEvent.EventType.UPLOAD_SUCCESS)
			{
				this.uploadedVideo = (VideoEntry)event.getMoreInfo();
			}
			else
			{
				this.uploadedVideo = null;
			}
			enableUI(true);
		}
		else if (event.getType() == YouTubeEvent.EventType.UPLOAD_STARTED )
		{
			this.uploadedVideo = null;
			enableUI(false);
			this.showStatusPane();
		}
		else if (event.getType() == YouTubeEvent.EventType.LOGIN_STARTED )
		{
			this.uploadButton.setEnabled( false );
		}
		else if (event.getType() == YouTubeEvent.EventType.LOGIN_FAILED ||
				 event.getType() == YouTubeEvent.EventType.LOGIN_SUCCESS)
		{
			updateStatus();
		}
//		else if (event.getType() == YouTubeEvent.EventType.LINK_RETRIEVED )
//		{
//			this.uploadedVideo = (VideoEntry)event.getMoreInfo();
//		}
	}
	
	private void close()
	{
		this.setVisible( false );
	}
	
	private void onUploadFinished()
	{
		this.close();
	}
	
	private void showStatusPane()
	{
		this.statusPane.pack();
		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
			@Override
			public Boolean doInBackground()
			{
				int parentWidth = UploadToYouTubePane.this.getWidth();
				int parentHeight = UploadToYouTubePane.this.getHeight();
				int parentX = UploadToYouTubePane.this.getLocationOnScreen().x;
				int parentY = UploadToYouTubePane.this.getLocationOnScreen().y;
				Dimension dialogSize = UploadToYouTubePane.this.statusPane.getSize();
				int positionX = (parentWidth / 2) - dialogSize.width/2 + parentX;
				int positionY = (parentHeight / 2) - dialogSize.height/2 + parentY;
				UploadToYouTubePane.this.statusPane.setLocation( positionX, positionY );
				UploadToYouTubePane.this.statusPane.setVisible( true );
				return Boolean.TRUE;
			}
			
			@Override
			public void done()
			{
				UploadToYouTubePane.this.onUploadFinished();
			}
		};
		worker.execute();
	}
	
	private void updateStatus()
	{
		boolean canUpload = false;
		if (!this.loginPanel.isLoggedIn())
		{
			this.statusLabel.setForeground( ERROR_COLOR );
			this.statusLabel.setText( NOT_LOGGED_IN_STATUS );
		}
		else if (!this.infoPanel.isTitleValid() && !this.infoPanel.isDescriptionValid())
		{
			this.statusLabel.setForeground( ERROR_COLOR );
			this.statusLabel.setText( NO_TITLE_NO_DESCRIPTION_STATUS );
		}
		else if (!this.infoPanel.isDescriptionValid())
		{
			this.statusLabel.setForeground( ERROR_COLOR );
			this.statusLabel.setText( NO_DESCRIPTION_STATUS );
		}
		else
		{
			this.statusLabel.setForeground( HAPPY_COLOR );
			this.statusLabel.setText( READY_STATUS );
			canUpload = true;
		}
		this.uploadButton.setEnabled( canUpload );
	}
	
	public void changedUpdate( DocumentEvent arg0 ) {
		updateStatus();
	}

	public void insertUpdate( DocumentEvent arg0 ) {
		updateStatus();
	}

	public void removeUpdate( DocumentEvent arg0 ) {
		updateStatus();
	}

	
	
}
