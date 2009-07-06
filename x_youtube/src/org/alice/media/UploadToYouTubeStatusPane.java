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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingworker.SwingWorker;

import com.google.gdata.util.ServiceException;

import swing.Pane;

/**
 * @author David Culyba
 */
public class UploadToYouTubeStatusPane extends JDialog implements YouTubeListener, ActionListener
{
	
	public enum UploadStatus
	{
		Uploading("Uploading..."),
		Cancelling("Cancelling..."),
		Waiting("Waiting..."),
		Succeeded("Upload Succeeded"),
		Failed("Upload Failed"),
		Cancelled("Cancelled"),
		FailedCancelled("Cancel Failed");
		
		private String status;
		private UploadStatus(String status)
		{
			this.status = status;
		}
		
		@Override
		public String toString()
		{
			return this.status;
		}
	}
	
	private JLabel statusLabel;
	private JButton doneButton;
	private JButton cancelButton;
	private JPanel doneButtonPanel;
	private JPanel notDoneButtonPanel;
	private JPanel buttonPanel;
	private CardLayout cardLayout;
	private JLabel imageLabel;
	private List<ImageIcon> images = new LinkedList<ImageIcon>();
	private boolean isUploading = false;
	private UploadStatus status;
	private String uploadDetails;
	
	private static final String UPLOADING_KEY = "UPLOADING";
	private static final String DONE_UPLOADING_KEY = "UPLOADED";
	
	private static final String TITLE = "Uploading Status";
	
	private YouTubeUploader uploader;
	
	public UploadToYouTubeStatusPane(Frame owner,  YouTubeUploader uploader)
	{
		super(owner);
		this.uploader = uploader;
		this.status = UploadStatus.Waiting;
		
		images.add( new ImageIcon( this.getClass().getResource( "images/upload00.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload01.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload02.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload03.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload04.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload05.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload06.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload07.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload08.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload09.png" )) );
		images.add( new ImageIcon( this.getClass().getResource( "images/upload10.png" )) );
		
		this.imageLabel = new JLabel(images.get(0));
		
		this.statusLabel = new JLabel();
		this.statusLabel.setOpaque( false );
		this.statusLabel.setFont( this.statusLabel.getFont().deriveFont( 18f ) );
		this.statusLabel.setText( this.status.toString() );
		this.statusLabel.setHorizontalAlignment( JLabel.CENTER );
		
		this.doneButton = new JButton("Done");
		this.doneButton.addActionListener( this );
		this.doneButtonPanel = new JPanel();
		this.doneButtonPanel.setOpaque( false );
		this.doneButtonPanel.add( this.doneButton );
		
		this.cancelButton = new JButton("Cancel Upload");
		this.cancelButton.addActionListener( this );
		this.notDoneButtonPanel = new JPanel();
		this.notDoneButtonPanel.setOpaque( false );
		//this.notDoneButtonPanel.add( this.cancelButton );
		
		this.cardLayout = new CardLayout();
		
		this.buttonPanel = new JPanel();
		this.buttonPanel.setOpaque( false );
		this.buttonPanel.setLayout( this.cardLayout );
		this.buttonPanel.add( this.doneButtonPanel, DONE_UPLOADING_KEY);
		this.buttonPanel.add( this.notDoneButtonPanel, UPLOADING_KEY);
		this.cardLayout.show( this.buttonPanel, UPLOADING_KEY );
		
		this.setLocationRelativeTo( owner );
		this.setTitle( TITLE );
		this.setModal( true );
		this.getContentPane().setBackground( Color.WHITE );
		this.getContentPane().setLayout( new GridBagLayout() );
		this.getContentPane().add( this.statusLabel, 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( this.imageLabel, 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.getContentPane().add( this.buttonPanel, 
				new GridBagConstraints( 
				0, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
	}
	
	private void animate()
	{
		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
			@Override
			public Boolean doInBackground()
			{
				int count = 0;
				while (UploadToYouTubeStatusPane.this.isUploading)
				{
					
					UploadToYouTubeStatusPane.this.imageLabel.setIcon( UploadToYouTubeStatusPane.this.images.get( count ) );
					try
					{
						int sleepTime = 100;
						if (count == 0 || count == UploadToYouTubeStatusPane.this.images.size() - 1)
						{
							sleepTime = 300;
						}
						Thread.sleep( sleepTime );
					}
					catch (Exception e)
					{
						
					}
					count = (count + 1) % UploadToYouTubeStatusPane.this.images.size();
				}
				UploadToYouTubeStatusPane.this.imageLabel.setIcon( UploadToYouTubeStatusPane.this.images.get( UploadToYouTubeStatusPane.this.images.size() - 1 ) );
				return Boolean.TRUE;
			}
		};
		worker.execute();
	}
	
	private void stopUpload(YouTubeEvent event)
	{
		if (event.getType() == YouTubeEvent.EventType.UploadFailed)
		{
			this.status = UploadStatus.Failed;
		}
		if (event.getType() == YouTubeEvent.EventType.UploadSucces)
		{
			this.status = UploadStatus.Succeeded;
		}
		if (event.getType() == YouTubeEvent.EventType.UploadCancelledSuccess)
		{
			this.status = UploadStatus.Cancelled;
		}
		if (event.getType() == YouTubeEvent.EventType.UploadCancelledFailed)
		{
			this.status = UploadStatus.FailedCancelled;
		}
		this.isUploading = false;
		this.cardLayout.show( this.buttonPanel, DONE_UPLOADING_KEY );
		this.statusLabel.setText( event.getType().toString() );
		this.uploadDetails = event.getMoreInfo();
	}
	
	public UploadStatus getStatus()
	{
		return this.status;
	}
	
	public String getDetails()
	{
		return this.uploadDetails;
	}
	
	private void startUpload()
	{
		if (!this.isUploading)
		{
			this.isUploading = true;
			this.status = UploadStatus.Uploading;
			this.statusLabel.setText( this.status.toString() );
			this.cardLayout.show( this.buttonPanel, UPLOADING_KEY );
			animate();
		}
	}

	public void youTubeEventTriggered( YouTubeEvent event ) {
		if (event.getType() == YouTubeEvent.EventType.UploadFailed ||
			event.getType() == YouTubeEvent.EventType.UploadSucces ||
			event.getType() == YouTubeEvent.EventType.UploadCancelledSuccess ||
			event.getType() == YouTubeEvent.EventType.UploadCancelledFailed)
		{
			
			stopUpload(event);
		}
		else if (event.getType() == YouTubeEvent.EventType.UploadStarted )
		{
			startUpload();
		}
		
	}

	public void actionPerformed( ActionEvent e ) {
		if (e.getSource() == this.cancelButton)
		{
			this.uploader.cancelUpload();
			this.status = UploadStatus.Cancelling;
			this.statusLabel.setText( this.status.toString() );
			this.cancelButton.setEnabled( false );
		}
		else if (e.getSource() == this.doneButton)
		{
			this.setVisible( false );
		}
		
	}

}
