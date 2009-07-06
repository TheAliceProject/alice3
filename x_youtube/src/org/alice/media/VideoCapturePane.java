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
import java.awt.Component;
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
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

import org.alice.media.encoder.EncoderListener;
import org.alice.media.encoder.ImagesToMOVEncoder;
import org.alice.stageide.MoveAndTurnRuntimeProgram;
import org.jdesktop.swingworker.SwingWorker;

import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine;
import edu.cmu.cs.dennisc.swing.FileSelectionPane;

import swing.LineAxisPane;
import zoot.ZFrame;

/**
 * @author David Culyba
 */

public class VideoCapturePane extends LineAxisPane implements ActionListener, DocumentListener, EncoderListener{
	
	private static final float FRAME_RATE = 24;
	private static final Color ERROR_COLOR = Color.RED;
	private static final Color NEUTRAL_TEXT_COLOR = Color.BLACK;
	private static final Color NEUTRAL_LABEL_COLOR = Color.GRAY;
	private static final Color ACTIVE_LABEL_COLOR = Color.BLACK;
	private static final Color HAPPY_COLOR = new Color(0f, .6f, 0f);
	
	private class OwnerPane extends javax.swing.JPanel {
		public OwnerPane() {
			setBackground( java.awt.Color.DARK_GRAY );
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return new java.awt.Dimension( 640, 480 );
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		} 
	}
	
	private class RecordableRuntimeProgram extends MoveAndTurnRuntimeProgram
	{
		
		public RecordableRuntimeProgram(TypeDeclaredInAlice sceneType, VirtualMachine vm)
		{
			super(sceneType, vm);
		}
		
		@Override
		protected java.awt.Component createSpeedMultiplierControlPanel() {
			return null;
		}
		@Override
		protected edu.cmu.cs.dennisc.animation.Animator createAnimator() {
			return new edu.cmu.cs.dennisc.animation.FrameBasedAnimator( FRAME_RATE );
		}
		@Override
		protected void run() {
			VideoCapturePane.this.onWorldStart();
			super.run();
			this.setMovieEncoder( null );
		}
		
		@Override
		public void showInAWTContainer( java.awt.Container awtContainer, String[] args ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "showInAWTContainer", awtContainer, args );
			setArgs( args );
			init();
			awtContainer.setLayout( new java.awt.GridLayout( 1, 1 ) );
			
			awtContainer.add( this );
			if( awtContainer instanceof javax.swing.JComponent ) {
				((javax.swing.JComponent)awtContainer).revalidate();
			}
			awtContainer.repaint();
		}
		
		public void runWorld()
		{
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					java.awt.Container contentPane = RecordableRuntimeProgram.this.getContentPane();
					contentPane.repaint();
					RecordableRuntimeProgram.this.start();
				}
			} );
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
						return true;
					}
					if (f.getName().endsWith( "mov" ))
					{
						return true;
					}
					return false;
				}

				@Override
				public String getDescription() {
					return "*.mov or Directories";
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
	
	private MovieFileSelectionPane fileSelectionPane = new MovieFileSelectionPane();
	private OwnerPane pane = new OwnerPane();
	private edu.cmu.cs.dennisc.alice.Project project;
	private RecordableRuntimeProgram rtProgram;
	private boolean isRestart = false;
	private UploadToYouTubePane youTubeUploaderPane;
	
	private JButton recordButton;
	private ImageIcon recordIcon;
	private ImageIcon stopIcon;
	private JTextField fileNameField;
	private JLabel pathLabel;
	private JButton browseButton;
	private ImagesToMOVEncoder encoder;
	private JLabel statusLabel;
	private JButton exportToYouTubeButton;
	private JButton doneButton;
	
	private static final String NO_RECORDING_STATUS = "No recording.";
	
	private String getDefaultDirectory()
	{
		return edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory().getAbsolutePath();
	}
	
	private String getDefaultFilename()
	{
		if (this.project != null)
		{
			
		}
		return "testMovie";
	}
	
	public VideoCapturePane(edu.cmu.cs.dennisc.alice.Project project) {
		this.project = project;
		
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
		
		this.encoder = new ImagesToMOVEncoder((int)FRAME_RATE);
		this.encoder.addListener( this );
		//this.encoder = new SeriesOfImagesMovieEncoder("C:/movie_dump", "test", "0000", "png");
		
		edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
		this.rtProgram = new RecordableRuntimeProgram( sceneType, vm );
		rtProgram.showInAWTContainer( this.pane, new String[] {} );
		
		JPanel controlPanel = new JPanel();
		controlPanel.setOpaque( false );
		controlPanel.setLayout( new GridBagLayout() );
		
		this.recordButton = new JButton();
		this.recordButton.setOpaque(false);
		this.recordIcon = new ImageIcon( this.getClass().getResource( "images/rec_button.png" ));
		this.stopIcon = new ImageIcon( this.getClass().getResource( "images/stop_button.png" ));
		this.recordButton.setIcon( this.recordIcon );
		
		this.pathLabel = new JLabel(getDefaultDirectory());
		this.pathLabel.setFont(  this.getFont().deriveFont(Font.ITALIC) );
		this.fileNameField = new JTextField(16);
		this.fileNameField.setForeground( NEUTRAL_TEXT_COLOR );
		this.fileNameField.setText(getDefaultFilename());
		this.browseButton = new JButton("Browse...");
		this.browseButton.addActionListener( this );
		
		this.doneButton = new JButton("Done");
		this.doneButton.addActionListener( this );
		
		JLabel statusTitle = new JLabel("Recorded File:");
		statusTitle.setForeground( Color.GRAY );
		statusTitle.setFont(  this.getFont().deriveFont(Font.ITALIC)  );
		
		this.statusLabel = new JLabel();
//		TitledBorder titleBorder = BorderFactory.createTitledBorder( "Recording Status" );
//		titleBorder.setTitleColor( Color.GRAY );
//		titleBorder.setTitleFont(  this.getFont().deriveFont(Font.ITALIC)  );
//		this.statusLabel.setBorder( titleBorder );
		this.statusLabel.setForeground( NEUTRAL_LABEL_COLOR );
		this.statusLabel.setText(NO_RECORDING_STATUS);
		this.statusLabel.setHorizontalAlignment( JLabel.CENTER );
		this.statusLabel.setVerticalAlignment( JLabel.CENTER );
		Dimension labelSize =  new Dimension(60, 40);
		this.statusLabel.setPreferredSize( labelSize );
		this.statusLabel.setMinimumSize( labelSize );
		
		this.exportToYouTubeButton =  new JButton(new ImageIcon( this.getClass().getResource( "images/export_to_youtube.png" )));
		this.exportToYouTubeButton.addActionListener( this );
		this.exportToYouTubeButton.setEnabled( false );
		
		int gridY = 0;
		controlPanel.add( new JLabel("Save movie to: "), 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 16, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.pathLabel , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.fileNameField , 
				new GridBagConstraints( 
				0, //gridX
				gridY, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 8, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.browseButton , 
				new GridBagConstraints( 
				1, //gridX
				gridY++, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 8, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.recordButton , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 32, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( statusTitle, 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 16, 8, 8, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.statusLabel , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 4, 8, 8, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.exportToYouTubeButton , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 16, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		
		controlPanel.add( Box.createVerticalGlue() , 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.VERTICAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		controlPanel.add( this.doneButton, 
				new GridBagConstraints( 
				0, //gridX
				gridY++, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.SOUTHEAST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 8, 8 ), //insets
				0, //ipadX
				0 ) //ipadY
				);
		this.add( this.pane );
		this.add( controlPanel );
		this.add( javax.swing.Box.createGlue() );
		
		this.fileNameField.addActionListener( this );
		this.recordButton.addActionListener( this );
		this.fileNameField.getDocument().addDocumentListener( this );
		//this.recordButton.getAction().setEnabled( false );
	}
	
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
					VideoCapturePane.this.pane.removeAll();
					edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
					edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)VideoCapturePane.this.project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
					VideoCapturePane.this.rtProgram = new RecordableRuntimeProgram( sceneType, vm );
					VideoCapturePane.this.rtProgram.showInAWTContainer( VideoCapturePane.this.pane, new String[] {} );
				}
				else
				{
					VideoCapturePane.this.isRestart = true;
				}
				VideoCapturePane.this.rtProgram.setMovieEncoder( VideoCapturePane.this.encoder );
				VideoCapturePane.this.rtProgram.runWorld();
				return Boolean.TRUE;
			}
			
			@Override
			public void done()
			{
			}
		};
		worker.execute();
		
		
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
	
	public static void main( String[] args ) {
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			public void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		File projectFile = new File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "movieTest.a3p" );
		edu.cmu.cs.dennisc.alice.Project project = edu.cmu.cs.dennisc.alice.io.FileUtilities.readProject(projectFile);
		frame.getContentPane().add( new VideoCapturePane(project) );
		frame.pack();
		frame.setVisible( true );
	}

	private void uploadToYouTube()
	{
		this.youTubeUploaderPane.setVideo(this.getMovieFile(), this.encoder.getFirstFrame().getScaledInstance( 400, 300, Image.SCALE_FAST ));
		this.youTubeUploaderPane.pack();
		SwingWorker< Boolean, Void > worker = new SwingWorker< Boolean, Void >(){
			@Override
			public Boolean doInBackground()
			{
				int parentWidth = VideoCapturePane.this.getWidth();
				int parentHeight = VideoCapturePane.this.getHeight();
				Dimension dialogSize = VideoCapturePane.this.youTubeUploaderPane.getSize();
				int positionX = (parentWidth / 2) - dialogSize.width/2;
				int positionY = (parentHeight / 2) - dialogSize.height/2;
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
	
	private void onUploadFinished()
	{
		
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
		else if (e.getSource() == this.browseButton)
		{
			File selectedFile = this.fileSelectionPane.showInJDialog( this, "Choose movie save location" );
			if (selectedFile != null)
			{
				if (selectedFile.isDirectory())
				{
					this.pathLabel.setText( selectedFile.getAbsolutePath() );
				}
				else
				{
					this.pathLabel.setText( selectedFile.getParentFile().getAbsolutePath() );
					this.fileNameField.setText( selectedFile.getName() );
				}
			}
		}
		else if (e.getSource() == this.exportToYouTubeButton)
		{
			this.uploadToYouTube();
		}
		else if (e.getSource() == this.doneButton)
		{
			this.close();
		}
	}

	private void close()
	{
		this.setVisible( false );
		
		//VERY VERY TEMPORARY
		System.exit( 0 );
	}
	
	private File getMovieFile()
	{
		if (this.fileNameField.getText() == null || this.pathLabel.getText() == null)
		{
			return null;
		}
		String filename = this.fileNameField.getText().trim();
		String path = this.pathLabel.getText().trim();
		if (filename.equals( "" ) || path.equals( "" ))
		{
			return null;
		}
		if (!filename.endsWith( ".mov" ))
		{
			filename = filename + ".mov";
		}
		File movieFile = new File(path, filename);
		if (movieFile.exists())
		{
			return movieFile;
		}
		boolean goodFile = false;
		try {
			goodFile = movieFile.createNewFile();
			movieFile.delete();
		} catch( IOException e ) {
			return null;
		}
		if (goodFile)
		{
			return movieFile;
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

	public void encodingFinished(boolean success) {
		if (success)
		{
			javax.swing.SwingUtilities.invokeLater( new Runnable(){
				public void run() {
					VideoCapturePane.this.statusLabel.setForeground( HAPPY_COLOR );
					VideoCapturePane.this.statusLabel.setText( "<html>Successfully recorded movie to:<br><center><b>"+VideoCapturePane.this.encoder.getOutputFile().getName()+"</b></center></html>");
					VideoCapturePane.this.exportToYouTubeButton.setEnabled( true );
				}
			});
			
		}
		else
		{
			javax.swing.SwingUtilities.invokeLater( new Runnable(){
				public void run() {
					VideoCapturePane.this.statusLabel.setForeground( ERROR_COLOR );
					VideoCapturePane.this.statusLabel.setText( "Failed to record movie" );
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

