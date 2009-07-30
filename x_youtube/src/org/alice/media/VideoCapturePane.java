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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.alice.media.encoder.EncoderListener;
import org.alice.media.encoder.ImagesToMOVEncoder;
import org.jdesktop.swingworker.SwingWorker;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.animation.Program;
import edu.cmu.cs.dennisc.io.FileUtilities;
import edu.cmu.cs.dennisc.swing.FileSelectionPane;

import swing.LineAxisPane;

/**
 * @author David Culyba
 */

public abstract class VideoCapturePane extends LineAxisPane implements ActionListener, DocumentListener, EncoderListener{
	
	private enum CaptureState
	{
		RECORD,
		PREVIEW,
		SAVE,
	}
	
	private class OwnerPane extends javax.swing.JPanel {
		private Dimension customPreferredSize;
		public OwnerPane(Dimension preferredSize) {
			this.customPreferredSize = new Dimension(preferredSize);
			setBackground( java.awt.Color.DARK_GRAY );
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return new java.awt.Dimension(this.customPreferredSize);
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		} 
	}
	
	private class MovieFileSelectionPane extends FileSelectionPane
	{
		public MovieFileSelectionPane()
		{
			super();
			this.getFileChooser().setFileFilter( new FileFilter()
			{

				@Override
				public boolean accept( File f ) {
					if (f.isDirectory())
					{
						return false;
					}
					if (f.getName().endsWith( "mov" ))
					{
						return true;
					}
					return false;
				}

				@Override
				public String getDescription() {
					return "*.mov";
				}
				
			});
		}
		
		@Override
		protected String getFeedbackText() {
			// TODO Auto-generated method stub
			return "Temp Text";
		}
		
		@Override
		protected int getDesiredFileSelectionMode() {
			return javax.swing.JFileChooser.FILES_AND_DIRECTORIES;
		}
		
	};
	
	private static final Color ERROR_COLOR = Color.RED;
	private static final Color NEUTRAL_TEXT_COLOR = Color.BLACK;
	private static final Color NEUTRAL_LABEL_COLOR = Color.GRAY;
	private static final Color ACTIVE_LABEL_COLOR = Color.BLACK;
	private static final Color HAPPY_COLOR = new Color(0f, .6f, 0f);
	private static final Dimension worldSize = new Dimension(512, 384);
	
	private MovieFileSelectionPane fileSelectionPane = new MovieFileSelectionPane();
	private OwnerPane worldPane = new OwnerPane(worldSize);
	private Project project;
	private Program rtProgram;
	private boolean isRestart = false;
	private UploadToYouTubePane youTubeUploaderPane;
	private File recordedMovieFile;
	private File savedMovieFile;
	private MoviePlayer moviePlayer;
	private int frameRate = 24;
	
	private JButton recordButton;
	private ImageIcon recordIcon;
	private ImageIcon stopIcon;
	private JTextField fileNameField;
	private JLabel pathLabel;
	private JButton browseButton;
	private ImagesToMOVEncoder encoder;
	private JLabel statusLabel;
	private JLabel statusLabel2;
	private JPanel youTubeControlPanel;
	private JButton exportToYouTubeButton;
	private CardLayout youTubeCardLayout;
	private JButton cancelButton;
	private JButton nextButton;
	private JButton backButton;
	private JButton finishButton;
	private JButton saveButton;
	private JLabel saveStatus;
	private YouTubeResultsPane youTubeResultPane;
	
	
	private JPanel controlPanel;
	private JPanel recordPanel;
	private JPanel savePanel;
	private CardLayout controlPanelCardLayout;
	
	private JPanel worldPanel;
	private JPanel playPanel;
	private JPanel previewPanel;
	private JPanel previewControlPanel;
	private CardLayout worldPanelCardLayout;
	private boolean youTubeInitialized = false;
	
	private CaptureState state = CaptureState.RECORD;
	
	private static final String SAVE_KEY = "SAVE";
	private static final String RECORD_KEY = "RECORD";
	private static final String PLAY_KEY = "PLAY";
	private static final String PREVIEW_KEY = "PREVIEW";
	private static final String YOUTUBE_BUTTON_KEY = "YOUTUBE_BUTTON";
	private static final String YOUTUBE_STATUS_KEY = "YOUTUBE_STATUS";
	private static final String YOUTUBE_LINK_KEY = "YOUTUBE_LINK";
	
	private static final String NO_RECORDING_STATUS = "Ready to record.";
	private static final String YOUTUBE_NOT_UPLOADED_STATUS = "Not uploaded to YouTube.";
	
	
	
	public VideoCapturePane(Project project, int frameRate) {
		this.project = project;
		this.frameRate = frameRate;
		this.rtProgram = this.createProgram( this.project );
		
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( this );
		Frame frame = null;
		if( root instanceof java.awt.Frame ) 
		{
			frame  = (java.awt.Frame)root;
		} 
		else if( root instanceof java.awt.Dialog ) 
		{
			frame  = (java.awt.Frame)root;
		}
		
		this.youTubeUploaderPane = new UploadToYouTubePane(frame);
		youTubeInitialized = this.youTubeUploaderPane.init();
		
		this.encoder = new ImagesToMOVEncoder(this.frameRate);
		this.encoder.addListener( this );
		//this.encoder = new SeriesOfImagesMovieEncoder("C:/movie_dump", "test", "0000", "png");
		
		
		showWorldInContainer(VideoCapturePane.this.worldPane);
		
		this.recordButton = new JButton();
		this.recordButton.setOpaque(false);
		this.recordIcon = new ImageIcon( VideoCapturePane.class.getResource( "images/rec_button.png" ));
		this.stopIcon = new ImageIcon( VideoCapturePane.class.getResource( "images/stop_button.png" ));
		this.recordButton.setIcon( this.recordIcon );
		
		this.pathLabel = new JLabel(getDefaultDirectory());
		this.pathLabel.setFont(  this.getFont().deriveFont(Font.ITALIC) );
		this.fileNameField = new JTextField(16);
		this.fileNameField.setForeground( NEUTRAL_TEXT_COLOR );
		this.fileNameField.setText(getDefaultFilename());
		this.browseButton = new JButton("Browse...");
		this.browseButton.addActionListener( this );
		
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener( this );
		this.nextButton = new JButton("Next");
		this.nextButton.addActionListener( this );
		this.backButton = new JButton("Back");
		this.backButton.addActionListener( this );
		this.finishButton = new JButton("Finish");
		this.finishButton.addActionListener( this );
		
		
		JLabel statusTitle = new JLabel("Recording status:");
		statusTitle.setForeground( Color.GRAY );
		statusTitle.setFont(  this.getFont().deriveFont(Font.ITALIC)  );
		
		this.statusLabel = new JLabel();
		this.statusLabel.setFont( this.statusLabel.getFont().deriveFont( 12f ));
		this.statusLabel.setForeground( NEUTRAL_LABEL_COLOR );
		this.statusLabel.setText(NO_RECORDING_STATUS);
		this.statusLabel.setHorizontalAlignment( JLabel.CENTER );
		this.statusLabel.setVerticalAlignment( JLabel.CENTER );
		Dimension labelSize =  new Dimension(60, 40);
		this.statusLabel.setPreferredSize( labelSize );
		this.statusLabel.setMinimumSize( labelSize );
		
		this.statusLabel2 = new JLabel();
		this.statusLabel2.setFont( this.statusLabel2.getFont().deriveFont( 14f ));
		this.statusLabel2.setForeground( NEUTRAL_LABEL_COLOR );
		this.statusLabel2.setText(NO_RECORDING_STATUS);
		this.statusLabel2.setHorizontalAlignment( JLabel.CENTER );
		this.statusLabel2.setVerticalAlignment( JLabel.CENTER );
		this.statusLabel2.setPreferredSize( labelSize );
		this.statusLabel2.setMinimumSize( labelSize );
		
		this.exportToYouTubeButton =  new JButton(new ImageIcon( VideoCapturePane.class.getResource( "images/export_to_youtube.png" )));
		this.exportToYouTubeButton.addActionListener( this );
		this.exportToYouTubeButton.setEnabled( this.youTubeInitialized );

		JPanel recordButtonHolder = new JPanel();
		recordButtonHolder.setOpaque(false);
		recordButtonHolder.add( this.exportToYouTubeButton );
		
		this.youTubeResultPane = new YouTubeResultsPane();
		
		
		this.youTubeControlPanel = new JPanel();
		this.youTubeControlPanel.setOpaque(false);
		this.youTubeCardLayout = new CardLayout();
		this.youTubeControlPanel.setLayout( this.youTubeCardLayout );
		this.youTubeControlPanel.add(recordButtonHolder, YOUTUBE_BUTTON_KEY);
		this.youTubeControlPanel.add(this.youTubeResultPane, YOUTUBE_STATUS_KEY);
		this.youTubeCardLayout.show( this.youTubeControlPanel, YOUTUBE_BUTTON_KEY );
		
		
		this.saveButton = new JButton("Save Movie to File");
		this.saveButton.setFont( this.saveButton.getFont().deriveFont( 24f ) );
		this.saveButton.addActionListener( this );
		this.saveStatus = new JLabel();
		this.saveStatus.setHorizontalAlignment( JLabel.CENTER );
		Dimension saveDimension = new Dimension(256, 40);
		this.saveStatus.setMaximumSize( saveDimension );
		this.saveStatus.setMinimumSize( saveDimension );
		this.saveStatus.setPreferredSize( saveDimension );
		
		this.moviePlayer = new MoviePlayer();
		Dimension playerSize = this.worldPane.getPreferredSize();
		Dimension previewSize = new Dimension();
		previewSize.setSize( playerSize.width, playerSize.height + 30 );
		this.moviePlayer.setPreferredSize( previewSize );
		this.moviePlayer.setMinimumSize( previewSize );
		this.moviePlayer.setMaximumSize( previewSize );
		this.previewPanel = new JPanel();
		this.previewPanel.setLayout( new BorderLayout() );
		JPanel movieHolder = new JPanel();
		movieHolder.setLayout( new GridBagLayout() );
		movieHolder.setBorder( BorderFactory.createEmptyBorder(0,6,0,0) );
		movieHolder.add( this.moviePlayer , 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 8, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		JLabel previewLabel = new JLabel("Preview Movie");
		previewLabel.setFont( previewLabel.getFont().deriveFont( 18f ));
		previewLabel.setBorder( BorderFactory.createEmptyBorder( 6,12,6,6 ) );
		this.previewPanel.add( previewLabel, BorderLayout.NORTH );
		this.previewPanel.add( movieHolder, BorderLayout.CENTER );
		
		
		this.playPanel = new JPanel();
		this.playPanel.setLayout( new BorderLayout());
		JPanel worldHolder = new JPanel();
		worldHolder.setLayout( new GridBagLayout() );
		worldHolder.setPreferredSize( previewSize );
		worldHolder.setMinimumSize( previewSize );
		worldHolder.setMaximumSize( previewSize );
		worldHolder.setBorder( BorderFactory.createEmptyBorder(0,6,0,0) );
		worldHolder.add( this.worldPane , 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 8, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		JLabel recordLabel = new JLabel("Record World");
		recordLabel.setFont( recordLabel.getFont().deriveFont( 18f ));
		recordLabel.setBorder( BorderFactory.createEmptyBorder( 6,12,6,6 ) );
		this.playPanel.add( recordLabel, BorderLayout.NORTH );
		this.playPanel.add( worldHolder, BorderLayout.CENTER );
		
		this.worldPanel = new JPanel();
		this.worldPanelCardLayout = new CardLayout();
		this.worldPanel.setLayout( this.worldPanelCardLayout );
		this.worldPanel.add( this.previewPanel, PREVIEW_KEY );
		this.worldPanel.add(  this.playPanel, PLAY_KEY );
		this.worldPanelCardLayout.show( this.worldPanel, PLAY_KEY );
		
		
		this.recordPanel = new JPanel();
		this.recordPanel.setOpaque( false );
		this.recordPanel.setLayout( new GridBagLayout() );
		
		this.savePanel = new JPanel();
		this.savePanel.setOpaque(false);
		this.savePanel.setLayout( new GridBagLayout() );
		
//		this.previewControlPanel = new JPanel();
//		this.previewControlPanel.setOpaque( false );
//		this.previewControlPanel.setLayout( new GridBagLayout() );
//		JTextArea previewInstructions = new JTextArea();
//		previewInstructions.setEditable( false );
//		previewInstructions.setOpaque( false );
//		previewInstructions.setBorder(null);
//		previewInstructions.setText( "Preview your video )
		
		
		
		this.controlPanelCardLayout = new CardLayout();
		this.controlPanel = new JPanel();
		this.controlPanel.setOpaque( false );
		this.controlPanel.setLayout( this.controlPanelCardLayout );
		this.controlPanel.add( this.recordPanel, RECORD_KEY );
		this.controlPanel.add(  this.savePanel, SAVE_KEY );
		this.controlPanelCardLayout.show( this.controlPanel, RECORD_KEY );
		this.recordPanel.add( Box.createVerticalGlue() , 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.recordPanel.add( this.recordButton , 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
//		this.recordPanel.add( statusTitle, 
//				new GridBagConstraints( 
//				0, //gridX
//				2, //gridY
//				1, //gridWidth
//				1, //gridHeight
//				1.0, //weightX
//				0.0, //weightY
//				GridBagConstraints.NORTH, //anchor 
//				GridBagConstraints.HORIZONTAL, //fill
//				new Insets( 16, 8, 8, 2 ), //insets
//				0, //ipadX
//				0 ) //ipadY
//				);
		this.recordPanel.add( this.statusLabel , 
				new GridBagConstraints( 
				0, //gridX
				2, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 4, 8, 8, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.recordPanel.add( Box.createVerticalGlue() , 
				new GridBagConstraints( 
				0, //gridX
				3, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		int gridY = 0;
		this.savePanel.add( this.statusLabel2, 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 80, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.savePanel.add( this.saveButton , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 30, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.savePanel.add( this.saveStatus , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.savePanel.add( this.youTubeControlPanel , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 16, 8, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.savePanel.add( Box.createVerticalGlue() , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new GridBagLayout() );
		buttonPanel.add( Box.createHorizontalGlue() , 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		buttonPanel.add( this.backButton , 
				new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		buttonPanel.add( this.nextButton , 
				new GridBagConstraints( 
				2, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 8, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		buttonPanel.add( this.finishButton , 
				new GridBagConstraints( 
				3, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 8, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		buttonPanel.add( this.cancelButton , 
				new GridBagConstraints( 
				4, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 16, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
//		JPanel sidePanel = new JPanel();
//		sidePanel.setLayout(new GridBagLayout() );
//		gridY = 0;
//		sidePanel.add( this.controlPanel , 
//				new GridBagConstraints( 
//				0, //gridX
//				gridY++, //gridY
//				1, //gridWidth
//				1, //gridHeight
//				0.0, //weightX
//				1.0, //weightY
//				GridBagConstraints.CENTER, //anchor 
//				GridBagConstraints.BOTH, //fill
//				new Insets( 0, 0, 0, 0 ), //insets
//				0, //ipadX
//				0 ) //ipadY
//				);
//		sidePanel.add( buttonPanel, 
//				new GridBagConstraints( 
//				0, //gridX
//				gridY++, //gridY
//				1, //gridWidth
//				1, //gridHeight
//				0.0, //weightX
//				0.0, //weightY
//				GridBagConstraints.SOUTHEAST, //anchor 
//				GridBagConstraints.HORIZONTAL, //fill
//				new Insets( 0, 0, 8, 8 ), //insets
//				0, //ipadX
//				0 ) //ipadY
//				);
	
		
		this.setLayout( new GridBagLayout() );
		this.add( this.worldPanel , 
				new GridBagConstraints( 
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.BOTH, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		this.add( this.controlPanel , 
				new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.VERTICAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( buttonPanel , 
				new GridBagConstraints( 
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.SOUTHEAST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 8, 8 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		this.cancelButton.setEnabled( true );		
		this.nextButton.setEnabled( false );
		this.finishButton.setEnabled( false );
		this.backButton.setEnabled( false );
		
		this.cancelButton.addActionListener( this );
		this.nextButton.addActionListener( this );
		this.finishButton.addActionListener( this );
		this.backButton.addActionListener( this );
		this.fileNameField.addActionListener( this );
		this.recordButton.addActionListener( this );
		this.fileNameField.getDocument().addDocumentListener( this );
		this.revalidate();
		//this.recordButton.getAction().setEnabled( false );
	}

	abstract protected Program createProgram(Project project);
	
	abstract protected void onClose();
	
	private void record() {
		//
		this.fileNameField.setEnabled( false );
		this.fileNameField.setEditable( false );
		this.exportToYouTubeButton.setEnabled( false );
		this.revalidate();
		
		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
			@Override
			public Boolean doInBackground()
			{
				java.io.File videoFile = VideoCapturePane.this.getMovieFile();
				if (videoFile == null)
				{
					return Boolean.TRUE;
				}
				if (videoFile.exists())
				{
					videoFile.delete();
				}
				
				VideoCapturePane.this.encoder.setOutputFile( videoFile );
				
				if (VideoCapturePane.this.isRestart)
				{
					VideoCapturePane.this.restartWorld();
				}
				else
				{
					VideoCapturePane.this.isRestart = true;
				}
				VideoCapturePane.this.rtProgram.setMovieEncoder( VideoCapturePane.this.encoder );
				VideoCapturePane.this.runWorld();
				return Boolean.TRUE;
			}
			
			@Override
			public void done()
			{
			}
		};
		worker.execute();
	}
	
	private String getDefaultDirectory()
	{
		return edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory().getAbsolutePath();
	}
	
	private String getDefaultFilename()
	{
		return "testMovie";
	}
	
	protected void setState(CaptureState state)
	{
		if (this.state != state)
		{
			this.state = state;
			
			switch (this.state)
			{
				case RECORD : 
				{
					this.cancelButton.setEnabled( true );
					boolean hasNext = this.recordedMovieFile != null && FileUtilities.existsAndHasLengthGreaterThanZero( this.recordedMovieFile );
					this.nextButton.setEnabled(hasNext);
					this.finishButton.setEnabled( false );
					this.backButton.setEnabled( false );
					
					this.setRecordMode();
				}
				break;
				case PREVIEW : 
				{
					this.cancelButton.setEnabled( true );
					boolean hasNext = this.recordedMovieFile != null && FileUtilities.existsAndHasLengthGreaterThanZero( this.recordedMovieFile );
					this.nextButton.setEnabled(hasNext);
					this.finishButton.setEnabled( false );
					this.backButton.setEnabled( true );
				}
				break;
				case SAVE :
				{
					this.cancelButton.setEnabled( true );
					this.nextButton.setEnabled(false);
					this.finishButton.setEnabled( true );
					this.backButton.setEnabled( true );
					
					this.setSaveMode();
					
					
				}
				break;
			}
		}
	}
	
	protected void showWorldInContainer(java.awt.Container awtContainer)
	{
		this.rtProgram.setArgs(  new String[] {} );
		this.rtProgram.init();
		awtContainer.setLayout( new java.awt.GridLayout( 1, 1 ) );
		awtContainer.add( this.rtProgram );
		if( awtContainer instanceof javax.swing.JComponent ) {
			((javax.swing.JComponent)awtContainer).revalidate();
		}
		awtContainer.repaint();
	}
	
	protected void runWorld()
	{
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				java.awt.Container contentPane = VideoCapturePane.this.rtProgram.getContentPane();
				contentPane.repaint();
				VideoCapturePane.this.onWorldStart();
				VideoCapturePane.this.rtProgram.start();
			}
		} );
	}
	
	private void restartWorld()
	{
		this.worldPane.removeAll();
		this.rtProgram = this.createProgram( this.project );
		showWorldInContainer(VideoCapturePane.this.worldPane);
	}
	
	private void onWorldStart()
	{
		this.fileNameField.setEnabled( false );
		this.fileNameField.setEditable( false );
		this.browseButton.setEnabled( false );
		this.recordButton.setEnabled( true );
		this.recordButton.setIcon( this.stopIcon );
		this.revalidate();
	}
	
	private void onWorldEnd()
	{
		this.fileNameField.setEnabled( true );
		this.fileNameField.setEditable( true );
		this.browseButton.setEnabled( true );
		this.recordButton.setEnabled( true );
		this.recordButton.setIcon( this.recordIcon );
		this.revalidate();
	}
	
	private void stop() {
		this.rtProgram.setClosed( true );
	}

	private void uploadToYouTube()
	{
		File videoFile = this.savedMovieFile;
		if (videoFile ==  null)
		{
			videoFile = this.recordedMovieFile;
		}
		if (videoFile != null)
		{
			this.youTubeUploaderPane.setVideo(videoFile, this.encoder.getFirstFrame().getScaledInstance( 400, 300, Image.SCALE_FAST ));
			this.youTubeUploaderPane.pack();
			SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
				@Override
				public Boolean doInBackground()
				{
					int parentWidth = VideoCapturePane.this.getWidth();
					int parentHeight = VideoCapturePane.this.getHeight();
					int parentX = VideoCapturePane.this.getLocationOnScreen().x;
					int parentY = VideoCapturePane.this.getLocationOnScreen().y;
					Dimension dialogSize = VideoCapturePane.this.youTubeUploaderPane.getSize();
					int positionX = (parentWidth / 2) - dialogSize.width/2 + parentX;
					int positionY = (parentHeight / 2) - dialogSize.height/2 + parentY;
					if (positionY < 0)
					{
						positionY = 0;
					}
					if (positionX < 0)
					{
						positionX = 0;
					}
					VideoCapturePane.this.youTubeUploaderPane.setLocation( positionX, positionY );
					VideoCapturePane.this.youTubeUploaderPane.setVisible( true );
					return Boolean.TRUE;
				}
				
				@Override
				public void done()
				{
					VideoCapturePane.this.onUploadFinished();
				}
			};
			worker.execute();
		}
	}
	
	private void onUploadFinished()
	{
		this.youTubeResultPane.setResults( this.youTubeUploaderPane.getYouTubeResults() );
		this.youTubeCardLayout.show( this.youTubeControlPanel, YOUTUBE_STATUS_KEY );
//		if (this.youTubeUploaderPane.getUploadStatus() == UploadStatus.Succeeded)
//		{
//			boolean isSuccessful = false;
//			VideoEntry uploadedVideo = this.youTubeUploaderPane.getUploadedVideo();
//			if (uploadedVideo != null)
//			{
//				this.exportToYouTubeButton.setEnabled( false );
//				this.youTubeResultPane.setResults( this.youTube )
//				this.youTubeStatus.setForeground( HAPPY_COLOR );
//				this.youTubeStatus.setText( "Uploaded movie to YouTube" );
//				YouTubeMediaGroup mediaGroup = uploadedVideo.getMediaGroup();
//				MediaPlayer mediaPlayer = mediaGroup.getPlayer();
//			    if (mediaPlayer != null)
//			    {
//			    	this.youTubeLink.setURI( mediaPlayer.getUrl() );
//			    	this.youTubeLink.setBackground( Color.RED );
//			    	this.youTubeCardLayout.show( this.youTubeControlPanel, YOUTUBE_LINK_KEY );
//			    	isSuccessful = true;
//			    }
//			}
//			if (!isSuccessful)
//			{
//				this.youTubeStatus.setForeground( ERROR_COLOR );
//				this.youTubeStatus.setText( "Upload wasn't good for some reason" );
//				
//			}
//			
//		}
//		else if (this.youTubeUploaderPane.getUploadStatus() == UploadStatus.Cancelled ||
//				 this.youTubeUploaderPane.getUploadStatus() == UploadStatus.Waiting)
//		{
//			this.exportToYouTubeButton.setEnabled( true );
//			this.youTubeStatus.setForeground( NEUTRAL_TEXT_COLOR );
//			this.youTubeStatus.setText( YOUTUBE_NOT_UPLOADED_STATUS );
//			this.youTubeCardLayout.show( this.youTubeControlPanel, YOUTUBE_STATUS_KEY );
//		}
//		else
//		{
//			this.youTubeStatus.setForeground( ERROR_COLOR );
//			this.youTubeStatus.setText( this.youTubeUploaderPane.getUploadDetails() );
//			this.youTubeCardLayout.show( this.youTubeControlPanel, YOUTUBE_STATUS_KEY );
//		}
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run() {
				VideoCapturePane.this.repaint();
			}
		});
		
	}
	
	private boolean copyFile(File source, File dest)
	{
		try
		{
			InputStream in = new FileInputStream(source);
			OutputStream out = new FileOutputStream(dest);
			
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
			    out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	private void copyRecordedFile(final File recordedFile, final File destFile)
	{
		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
			@Override
			public Boolean doInBackground()
			{
				boolean copySuccess = copyFile(recordedFile, destFile);
				VideoCapturePane.this.savedMovieFile = destFile;
				return new Boolean(copySuccess);
			}
			
			@Override
			public void done()
			{
				try
				{
					if (get().booleanValue())
					{
						VideoCapturePane.this.saveStatus.setForeground( VideoCapturePane.HAPPY_COLOR );
						VideoCapturePane.this.saveStatus.setText( "<html>Saved movie to:<br>"+destFile.getAbsolutePath()+"</html>" );
					}
					else
					{
						VideoCapturePane.this.saveStatus.setForeground( VideoCapturePane.ERROR_COLOR );
						VideoCapturePane.this.saveStatus.setText( "Failed to save the movie" );
					}
				}
				catch (Exception e)
				{
					
				}
			}
		};
		worker.execute();
	}
	
	public void actionPerformed( ActionEvent e )
	{
		if (e.getSource() == this.recordButton)
		{
			this.recordButton.setEnabled( false );
			if (this.encoder.isRunning())
			{
				this.stop();
			}
			else
			{
				this.record();
			}
		}
		else if (e.getSource() == this.saveButton)
		{
			File selectedFile = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showSaveFileDialog(this, getDefaultDirectory(), getDefaultFilename(), "mov", true);
			if (selectedFile != null)
			{
				copyRecordedFile( this.recordedMovieFile, selectedFile );
			}
		}
		else if (e.getSource() == this.exportToYouTubeButton)
		{
			this.uploadToYouTube();
		}
		else if (e.getSource() == this.cancelButton)
		{
			this.close();
		}
		else if (e.getSource() == this.finishButton)
		{
			this.close();
		}
		else if (e.getSource() == this.nextButton)
		{
			if (this.state == CaptureState.RECORD)
			{
				this.setState( CaptureState.SAVE );
			}
		}
		else if (e.getSource() == this.backButton)
		{
			if (this.state == CaptureState.SAVE)
			{
				this.setState( CaptureState.RECORD );
			}
		}
	}

	private void close()
	{
		if (this.recordedMovieFile != null)
		{
			this.recordedMovieFile.delete();
		}
		this.onClose();
	}
	
	private File getMovieFile()
	{
		if (this.recordedMovieFile != null)
		{
			return this.recordedMovieFile;
		}
		String filename = "myAliceMovie.mov";
		String path = this.getDefaultDirectory();
		this.recordedMovieFile = new File(path, filename);
		if (this.recordedMovieFile.exists())
		{
			return this.recordedMovieFile;
		}
		boolean goodFile = false;
		try {
			goodFile = this.recordedMovieFile.createNewFile();
			this.recordedMovieFile.delete();
		} catch( IOException e ) {
			return null;
		}
		if (goodFile)
		{
			return this.recordedMovieFile;
		}
		return null;
	}
	
	private boolean validateSaveFile()
	{
		return (getMovieFile() != null);
	}
	
	private void updateUIForSaveFile()
	{
		if (validateSaveFile())
		{
			this.recordButton.setEnabled( true );
			this.fileNameField.setForeground( NEUTRAL_TEXT_COLOR );
		}
		else
		{
			this.recordButton.setEnabled( false );
			this.fileNameField.setForeground( ERROR_COLOR );
		}
	}

	public void changedUpdate( DocumentEvent e ) {
		updateUIForSaveFile();
	}

	public void insertUpdate( DocumentEvent e ) {
		updateUIForSaveFile();
	}

	public void removeUpdate( DocumentEvent e ) {
		updateUIForSaveFile();
	}
	
	private void setSaveMode()
	{
		this.statusLabel.setForeground( HAPPY_COLOR );
		this.statusLabel.setText( "Successfully recorded movie!");
		this.statusLabel2.setForeground( this.statusLabel.getForeground() );
		this.statusLabel2.setText( this.statusLabel.getText() );
		this.exportToYouTubeButton.setEnabled( this.youTubeInitialized );
		this.controlPanelCardLayout.show( this.controlPanel, SAVE_KEY );
		this.worldPanelCardLayout.show( this.worldPanel, PREVIEW_KEY );
		
	}
	
	private void setRecordMode()
	{
		this.moviePlayer.destroy();
		this.restartWorld();
		this.isRestart = false;
		this.recordButton.setIcon( this.recordIcon );
		this.statusLabel.setForeground( NEUTRAL_LABEL_COLOR );
		this.statusLabel.setText(NO_RECORDING_STATUS);
		this.controlPanelCardLayout.show( this.controlPanel, RECORD_KEY );
		this.worldPanelCardLayout.show( this.worldPanel, PLAY_KEY );
		this.youTubeCardLayout.show( this.youTubeControlPanel, YOUTUBE_BUTTON_KEY );
	}

	public void encodingFinished(boolean success) {
		if (success)
		{
			javax.swing.SwingUtilities.invokeLater( new Runnable(){
				public void run() {
					VideoCapturePane.this.moviePlayer.setMovie( VideoCapturePane.this.recordedMovieFile );
					VideoCapturePane.this.setState( CaptureState.SAVE );
				}
			});
			
		}
		else
		{
			javax.swing.SwingUtilities.invokeLater( new Runnable(){
				public void run() {
					VideoCapturePane.this.statusLabel.setForeground( ERROR_COLOR );
					VideoCapturePane.this.statusLabel.setText( "Failed to record movie" );
					VideoCapturePane.this.statusLabel2.setForeground( VideoCapturePane.this.statusLabel.getForeground() );
					VideoCapturePane.this.statusLabel.setText( VideoCapturePane.this.statusLabel.getText() );
				}
			});
		}
		this.onWorldEnd();
		
	}

	public void encodingStarted() {
		this.statusLabel.setForeground( ACTIVE_LABEL_COLOR );
		this.statusLabel.setText( "Recording movie..." );
	}

	public void frameUpdate( int frameCount ) {
		this.statusLabel.setText( "Recording frame "+frameCount );
		
	}
}

