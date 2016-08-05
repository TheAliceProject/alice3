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
package org.lgna.story.resourceutilities;

import javax.swing.event.DocumentEvent;

/**
 * @author dculyba
 * 
 */
public class FindResourcesPanel extends javax.swing.JPanel {

	private static interface FileDialog {
		public String getFile();

		public void setFile( String filename );

		public String getDirectory();

		public void setDirectory( String path );

		public void show();
	}

	private static class AwtFileDialog implements FileDialog {
		private final java.awt.FileDialog awtFileDialog;

		public AwtFileDialog( java.awt.Component root, String title, int mode ) {
			if( root instanceof java.awt.Frame ) {
				awtFileDialog = new java.awt.FileDialog( (java.awt.Frame)root, title, mode );
			} else if( root instanceof java.awt.Dialog ) {
				awtFileDialog = new java.awt.FileDialog( (java.awt.Dialog)root, title, mode );
			} else {
				awtFileDialog = new java.awt.FileDialog( (java.awt.Dialog)null, title, mode );
			}
		}

		@Override
		public String getFile() {
			return this.awtFileDialog.getFile();
		}

		@Override
		public void setFile( String filename ) {
			this.awtFileDialog.setFile( filename );
		}

		@Override
		public String getDirectory() {
			return this.awtFileDialog.getDirectory();
		}

		@Override
		public void setDirectory( String path ) {
			this.awtFileDialog.setDirectory( path );
		}

		@Override
		public void show() {
			this.awtFileDialog.setVisible( true );
		}
	}

	private static class SwingFileDialog implements FileDialog {
		private final javax.swing.JFileChooser jFileChooser = new javax.swing.JFileChooser();
		private final java.awt.Component root;
		private final String title;
		private final int mode;
		private int result = javax.swing.JFileChooser.CANCEL_OPTION;

		public SwingFileDialog( java.awt.Component root, String title, int mode ) {
			this.root = root;
			this.title = title;
			this.mode = mode;
			this.jFileChooser.setApproveButtonText( "Select" );
			this.jFileChooser.setFileSelectionMode( javax.swing.JFileChooser.DIRECTORIES_ONLY );
		}

		@Override
		public String getFile() {
			if( this.result != javax.swing.JFileChooser.CANCEL_OPTION ) {
				java.io.File file = this.jFileChooser.getSelectedFile();
				if( file != null ) {
					return file.getName();
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		@Override
		public void setFile( String filename ) {
			this.jFileChooser.setSelectedFile( new java.io.File( filename ) );
		}

		@Override
		public String getDirectory() {
			if( this.result != javax.swing.JFileChooser.CANCEL_OPTION ) {
				java.io.File file = this.jFileChooser.getCurrentDirectory();
				if( file != null ) {
					return file.getAbsolutePath();
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		@Override
		public void setDirectory( String path ) {
			if( path != null ) {
				this.jFileChooser.setCurrentDirectory( new java.io.File( path ) );
			}
		}

		@Override
		public void show() {
			//todo: use this.title
			this.result = javax.swing.JFileChooser.CANCEL_OPTION;
			if( mode == java.awt.FileDialog.LOAD ) {
				this.result = this.jFileChooser.showOpenDialog( this.root );
			} else {
				this.result = this.jFileChooser.showSaveDialog( this.root );
			}

		}
	}

	class BrowseAction extends javax.swing.AbstractAction {
		private final FileDialog jFileChooser;

		public BrowseAction() {
			super( "browse" );
			String title = "Select gallery directory";
			if( true || edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
				jFileChooser = new SwingFileDialog( FindResourcesPanel.this, title, java.awt.FileDialog.LOAD );
			} else {
				jFileChooser = new AwtFileDialog( FindResourcesPanel.this, title, java.awt.FileDialog.LOAD );
			}

		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			jFileChooser.show();
			if( ( jFileChooser.getDirectory() != null ) && ( jFileChooser.getFile() != null ) ) {
				setGalleryDir( new java.io.File( jFileChooser.getDirectory(), jFileChooser.getFile() ) );
			}
		}
	}

	class OkayAction extends javax.swing.AbstractAction {
		public OkayAction() {
			super( "OK" );
		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			javax.swing.SwingUtilities.getRoot( FindResourcesPanel.this ).setVisible( false );
		}
	}

	class CancelAction extends javax.swing.AbstractAction {
		public CancelAction() {
			super( "Cancel" );
		}

		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			javax.swing.SwingUtilities.getRoot( FindResourcesPanel.this ).setVisible( false );
		}
	}

	private static class SingletonHolder {
		private static FindResourcesPanel instance = new FindResourcesPanel();
	}

	public static FindResourcesPanel getInstance() {
		return SingletonHolder.instance;
	}

	private BrowseAction browseAction = new BrowseAction();
	private javax.swing.JButton browseButton = new javax.swing.JButton( browseAction );
	private javax.swing.JButton okayButton = new javax.swing.JButton( new OkayAction() );
	private javax.swing.JButton cancelButton = new javax.swing.JButton( new CancelAction() );
	private javax.swing.JTextArea textDescription = new javax.swing.JTextArea(
			"Alice can't find the gallery resources. If you have Alice installed, please use the 'browse' button to navigate to the Alice install directory." );

	private javax.swing.JTextField installDirectoryField = new javax.swing.JTextField();
	private javax.swing.JLabel statusLabel = new javax.swing.JLabel();
	private java.io.File galleryDir = null;

	private FindResourcesPanel() {
		java.awt.Font font = this.browseButton.getFont();
		this.browseButton.setFont( font.deriveFont( font.getSize2D() * 1.2f ) );
		this.browseButton.setAlignmentX( java.awt.Component.CENTER_ALIGNMENT );
		java.io.File startingDir = new java.io.File( "" );
		this.browseAction.jFileChooser.setDirectory( startingDir.getAbsolutePath() );
		this.textDescription.setEditable( false );
		this.textDescription.setLineWrap( true );
		this.textDescription.setWrapStyleWord( true );
		this.textDescription.setBorder( null );
		this.textDescription.setOpaque( false );
		this.textDescription.setBackground( new java.awt.Color( 1, 1, 1, 0 ) );
		this.installDirectoryField.setText( startingDir.getAbsolutePath() );
		this.installDirectoryField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {

			@Override
			public void insertUpdate( DocumentEvent e ) {
				doUpdate();
			}

			@Override
			public void removeUpdate( DocumentEvent e ) {
				doUpdate();
			}

			@Override
			public void changedUpdate( DocumentEvent e ) {
				doUpdate();
			}

		} );
		this.doUpdate();
		this.setLayout( new java.awt.GridBagLayout() );

		this.add( textDescription, new java.awt.GridBagConstraints(
				0, //gridX
				0, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				java.awt.GridBagConstraints.CENTER, //anchor 
				java.awt.GridBagConstraints.HORIZONTAL, //fill
				new java.awt.Insets( 8, 8, 10, 8 ), //insets: top, left, bottom, right
				0, //ipadX
				0 ) //ipadY
		);
		this.add( installDirectoryField, new java.awt.GridBagConstraints(
				0, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				java.awt.GridBagConstraints.EAST, //anchor 
				java.awt.GridBagConstraints.HORIZONTAL, //fill
				new java.awt.Insets( 2, 4, 2, 2 ), //insets: top, left, bottom, right
				0, //ipadX
				0 ) //ipadY
		);
		this.add( browseButton, new java.awt.GridBagConstraints(
				1, //gridX
				1, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				java.awt.GridBagConstraints.WEST, //anchor 
				java.awt.GridBagConstraints.NONE, //fill
				new java.awt.Insets( 2, 4, 2, 4 ), //insets: top, left, bottom, right
				0, //ipadX
				0 ) //ipadY
		);
		this.add( statusLabel, new java.awt.GridBagConstraints(
				0, //gridX
				2, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				java.awt.GridBagConstraints.CENTER, //anchor 
				java.awt.GridBagConstraints.NONE, //fill
				new java.awt.Insets( 12, 4, 2, 4 ), //insets: top, left, bottom, right
				0, //ipadX
				0 ) //ipadY
		);
		this.add( javax.swing.Box.createVerticalGlue(), new java.awt.GridBagConstraints(
				0, //gridX
				3, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				1.0, //weightY
				java.awt.GridBagConstraints.CENTER, //anchor 
				java.awt.GridBagConstraints.BOTH, //fill
				new java.awt.Insets( 0, 0, 0, 0 ), //insets: top, left, bottom, right
				0, //ipadX
				0 ) //ipadY
		);
		javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
		buttonPanel.setLayout( new java.awt.FlowLayout( java.awt.FlowLayout.RIGHT, 8, 4 ) );
		buttonPanel.add( okayButton );
		buttonPanel.add( cancelButton );
		this.add( buttonPanel, new java.awt.GridBagConstraints(
				0, //gridX
				4, //gridY
				2, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				java.awt.GridBagConstraints.EAST, //anchor 
				java.awt.GridBagConstraints.NONE, //fill
				new java.awt.Insets( 2, 2, 2, 2 ), //insets: top, left, bottom, right
				0, //ipadX
				0 ) //ipadY
		);
		this.setPreferredSize( new java.awt.Dimension( 500, 250 ) );
	}

	private void setGalleryDir( final java.io.File dir ) {
		setGalleryDir( dir.getAbsolutePath() );
	}

	private void setGalleryDir( final String dir ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				FindResourcesPanel.this.installDirectoryField.setText( dir );
			}
		} );
	}

	private void doUpdate() {
		String dirText = this.installDirectoryField.getText();
		this.galleryDir = StorytellingResources.getGalleryDirectory( new java.io.File( dirText ) );
		if( this.galleryDir != null ) {
			this.statusLabel.setForeground( java.awt.Color.BLACK );
			this.statusLabel.setText( "Found gallery at '" + this.galleryDir.getAbsolutePath() + "'" );
			this.okayButton.setEnabled( true );
		}
		else
		{
			this.statusLabel.setForeground( java.awt.Color.RED );
			this.statusLabel.setText( "Cannot find gallery at '" + this.installDirectoryField.getText() + "'" );
			this.okayButton.setEnabled( false );
		}
	}

	public java.io.File getGalleryDir() {
		return this.galleryDir;
	}

	public void show( javax.swing.JRootPane root ) {
		javax.swing.JDialog dialog = edu.cmu.cs.dennisc.javax.swing.JDialogUtilities.createPackedJDialog( this, root, "Locate Resources", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog, root );
		dialog.setVisible( true );
	}

}
