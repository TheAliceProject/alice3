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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YtPublicationState;

import edu.cmu.cs.dennisc.swing.Hyperlink;

/**
 * @author David Culyba
 */
public class YouTubeResultsPane extends JPanel {

	private JLabel videoStatus;
	private JTextPane additionalStatusInfo;
	private JTextField videoLinkCopyableField;
	private Hyperlink youTubeLink;
	private JLabel videoStatusTitle;
	
	public YouTubeResultsPane()
	{
		super();
		this.setOpaque(false);
		this.setPreferredSize( new Dimension( 280, 200));
		
		this.setLayout( new GridBagLayout() );
		
		JLabel titleLabel = new JLabel("Upload to YouTube Results:");
		titleLabel.setFont( titleLabel.getFont().deriveFont(Font.BOLD, 16f));
	
		this.videoStatusTitle = new JLabel(" Video status:");
		this.videoStatus = new JLabel("status");
		
		this.additionalStatusInfo = new JTextPane();
		this.additionalStatusInfo.setEditable( false );
		this.additionalStatusInfo.setEditable( false );
		this.additionalStatusInfo.setBorder( BorderFactory.createLineBorder( Color.GRAY ) );
		this.additionalStatusInfo.setOpaque( false );
		this.additionalStatusInfo.setDragEnabled( false );
		this.additionalStatusInfo.setFocusable( false );
		this.additionalStatusInfo.setText( "Video not uploaded yet." );
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setAlignment( attributes, StyleConstants.ALIGN_CENTER );
		this.additionalStatusInfo.getStyledDocument().setParagraphAttributes( 0, 0, attributes, true );
		
		JLabel videoLinkLabel = new JLabel(" Video link:");
		this.youTubeLink = new Hyperlink("");
		this.youTubeLink.setContentText( "Your uploaded video");
		this.youTubeLink.setFont( this.youTubeLink.getFont().deriveFont( 16f ));
		this.youTubeLink.setDefaultColor(new Color(0f, 0f, .6f));
		//this.youTubeLink.setHorizontalAlignment( JLabel.CENTER );
		this.videoLinkCopyableField = new JTextField(26);
		//this.videoLinkCopyableField.setFocusable( false );
		this.videoLinkCopyableField.setEditable( false );
		this.videoLinkCopyableField.setHorizontalAlignment( JTextField.CENTER );
		
		this.add( titleLabel, 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( this.additionalStatusInfo, 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( videoLinkLabel, 
				new GridBagConstraints( 
				0, //gridX
				3, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( this.youTubeLink, 
				new GridBagConstraints( 
				1, //gridX
				3, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 8, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( this.videoLinkCopyableField, 
				new GridBagConstraints( 
				0, //gridX
				4, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( Box.createVerticalGlue(), 
				new GridBagConstraints( 
				0, //gridX
				5, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
	}
	
	private void removeStatusComponents()
	{
//		this.remove( this.videoStatusTitle );
//		this.remove( this.videoStatus );
		this.remove( this.additionalStatusInfo );
	}
	
//	
//	@Override
//	public Dimension getPreferredSize()
//	{
//		return super.getPreferredSize();
//	}
	
	public void setResults(YouTubeEvent event)
	{
		if (event == null)
		{
			return;
		}
		if (event.getType() == YouTubeEvent.EventType.UPLOAD_SUCCESS )
		{
			VideoEntry videoEntry = (VideoEntry)event.getMoreInfo();
			if(videoEntry.isDraft())
			{
				//this.videoStatus.setText(" Video is not live");
				YtPublicationState pubState = videoEntry.getPublicationState();
				//this.additionalStatusInfo.setRows( 0 );
				if(pubState.getState() == YtPublicationState.State.PROCESSING)
				{
					this.additionalStatusInfo.setText("Successfully uploaded! (But not processed yet...give it a little bit of time.)");
				//	this.addStatusComponents();
				//	this.additionalStatusInfo.setRows( 1 );
				}
				else if(pubState.getState() == YtPublicationState.State.REJECTED)
				{
					this.additionalStatusInfo.setText("Video has been rejected because: \n" +pubState.getDescription());
				//	this.addStatusComponents();
					//this.additionalStatusInfo.setRows( 2 );
				}
				else if(pubState.getState() == YtPublicationState.State.FAILED)
				{
					this.additionalStatusInfo.setText("Video failed to upload because: \n" +pubState.getDescription());
				//	this.addStatusComponents();
					//	this.additionalStatusInfo.setRows( 2 );
				}
			}
			else
			{
				this.additionalStatusInfo.setText("Successfully uploaded!");
				//this.additionalStatusInfo.setRows( 0 );
			//	this.addStatusComponents();
			}
			YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();
			MediaPlayer mediaPlayer = mediaGroup.getPlayer();
		    if (mediaPlayer != null)
		    {
		    	this.youTubeLink.setURI( mediaPlayer.getUrl() );
		    	this.videoLinkCopyableField.setText( mediaPlayer.getUrl() );
		    }
		}
	}
	
}
