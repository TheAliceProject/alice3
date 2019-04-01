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
package org.alice.media.youtube.croquet.views;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.alice.media.youtube.core.YouTubeEvent;
import org.alice.media.youtube.core.YouTubeListener;
import org.alice.media.youtube.core.YouTubeUploader;

/**
 * @author David Culyba
 */
public class UploadToYouTubeStatusPane extends JDialog implements YouTubeListener, ActionListener {

	public enum UploadStatus {
		Uploading( "Uploading..." ),
		Cancelling( "Cancelling..." ),
		Waiting( "Waiting..." ),
		Succeeded( "Upload Succeeded" ),
		Failed( "Upload Failed" ),
		Cancelled( "Cancelled" ),
		FailedCancelled( "Cancel Failed" );

		private String status;

		private UploadStatus( String status ) {
			this.status = status;
		}

		@Override
		public String toString() {
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

	public UploadToYouTubeStatusPane( Frame owner, YouTubeUploader uploader ) {
		super( owner );
		this.uploader = uploader;
		this.status = UploadStatus.Waiting;

		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload00.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload01.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload02.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload03.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload04.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload05.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload06.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload07.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload08.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload09.png" ) ) );
		images.add( new ImageIcon( UploadToYouTubeStatusPane.class.getResource( "icons/upload10.png" ) ) );

		this.imageLabel = new JLabel( images.get( 0 ) );

		this.statusLabel = new JLabel();
		this.statusLabel.setOpaque( false );
		this.statusLabel.setFont( this.statusLabel.getFont().deriveFont( 18f ) );
		this.statusLabel.setText( this.status.toString() );
		this.statusLabel.setHorizontalAlignment( JLabel.CENTER );

		this.doneButton = new JButton( "Done" );
		this.doneButton.addActionListener( this );
		this.doneButtonPanel = new JPanel();
		this.doneButtonPanel.setOpaque( false );
		this.doneButtonPanel.add( this.doneButton );

		this.cancelButton = new JButton( "Cancel Upload" );
		this.cancelButton.addActionListener( this );
		this.notDoneButtonPanel = new JPanel();
		this.notDoneButtonPanel.setOpaque( false );
		//this.notDoneButtonPanel.add( this.cancelButton );

		this.cardLayout = new CardLayout();

		this.buttonPanel = new JPanel();
		this.buttonPanel.setOpaque( false );
		this.buttonPanel.setLayout( this.cardLayout );
		this.buttonPanel.add( this.doneButtonPanel, DONE_UPLOADING_KEY );
		this.buttonPanel.add( this.notDoneButtonPanel, UPLOADING_KEY );
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

	private void animate() {
		SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
			@Override
			public Boolean doInBackground() {
				int count = 0;
				while( UploadToYouTubeStatusPane.this.isUploading ) {

					UploadToYouTubeStatusPane.this.imageLabel.setIcon( UploadToYouTubeStatusPane.this.images.get( count ) );
					try {
						int sleepTime = 100;
						if( ( count == 0 ) || ( count == ( UploadToYouTubeStatusPane.this.images.size() - 1 ) ) ) {
							sleepTime = 300;
						}
						Thread.sleep( sleepTime );
					} catch( Exception e ) {

					}
					count = ( count + 1 ) % UploadToYouTubeStatusPane.this.images.size();
				}
				UploadToYouTubeStatusPane.this.imageLabel.setIcon( UploadToYouTubeStatusPane.this.images.get( UploadToYouTubeStatusPane.this.images.size() - 1 ) );
				return Boolean.TRUE;
			}
		};
		worker.execute();
	}

	private void stopUpload( YouTubeEvent event ) {
		if( event.getType() == YouTubeEvent.EventType.UPLOAD_FAILED ) {
			this.status = UploadStatus.Failed;
		}
		if( event.getType() == YouTubeEvent.EventType.UPLOAD_SUCCESS ) {
			this.status = UploadStatus.Succeeded;
		}
		if( event.getType() == YouTubeEvent.EventType.UPLOAD_CANCELLED_SUCCESS ) {
			this.status = UploadStatus.Cancelled;
		}
		if( event.getType() == YouTubeEvent.EventType.UPLOAD_CANCELLED_FAILED ) {
			this.status = UploadStatus.FailedCancelled;
		}
		this.isUploading = false;
		this.cardLayout.show( this.buttonPanel, DONE_UPLOADING_KEY );
		this.statusLabel.setText( event.getType().toString() );
		this.uploadDetails = (String)event.getMoreInfo();
	}

	public UploadStatus getStatus() {
		return this.status;
	}

	public String getDetails() {
		return this.uploadDetails;
	}

	private void startUpload() {
		if( !this.isUploading ) {
			this.isUploading = true;
			this.status = UploadStatus.Uploading;
			this.statusLabel.setText( this.status.toString() );
			this.cardLayout.show( this.buttonPanel, UPLOADING_KEY );
			animate();
		}
	}

	@Override
	public void youTubeEventTriggered( YouTubeEvent event ) {
		if( ( event.getType() == YouTubeEvent.EventType.UPLOAD_FAILED ) ||
				( event.getType() == YouTubeEvent.EventType.UPLOAD_SUCCESS ) ||
				( event.getType() == YouTubeEvent.EventType.UPLOAD_CANCELLED_SUCCESS ) ||
				( event.getType() == YouTubeEvent.EventType.UPLOAD_CANCELLED_FAILED ) ) {

			stopUpload( event );
		} else if( event.getType() == YouTubeEvent.EventType.UPLOAD_STARTED ) {
			startUpload();
		}

	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		if( e.getSource() == this.cancelButton ) {
			this.uploader.cancelUpload();
			this.status = UploadStatus.Cancelling;
			this.statusLabel.setText( this.status.toString() );
			this.cancelButton.setEnabled( false );
		} else if( e.getSource() == this.doneButton ) {
			this.setVisible( false );
		}

	}

}
