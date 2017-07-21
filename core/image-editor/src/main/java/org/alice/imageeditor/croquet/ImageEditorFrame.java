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
package org.alice.imageeditor.croquet;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorFrame extends org.lgna.croquet.FrameCompositeWithInternalIsShowingState<org.alice.imageeditor.croquet.views.ImageEditorPane> {
	public static String INVALID_PATH_NOT_A_DIRECTORY = "INVALID_PATH_NOT_A_DIRECTORY";
	public static String INVALID_PATH_EMPTY_SUB_PATH = "INVALID_PATH_EMPTY_SUB_PATH";
	private static final String DEFAULT_ROOT_DIRECTORY_PATH = edu.cmu.cs.dennisc.java.io.UserDirectoryUtilities.getBestGuessPicturesDirectory().getAbsolutePath();

	private final org.lgna.croquet.StringState rootDirectoryState = this.createPreferenceStringState( "rootDirectoryState", DEFAULT_ROOT_DIRECTORY_PATH, null );

	private final org.lgna.croquet.Operation browseOperation = this.createActionOperation( "browseOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			String rootDirectoryPath = rootDirectoryState.getValue();
			javax.swing.JFileChooser jFileChooser = new javax.swing.JFileChooser();
			if( ( rootDirectoryPath != null ) && ( rootDirectoryPath.length() > 0 ) ) {
				java.io.File rootDirectory = new java.io.File( rootDirectoryPath );
				if( rootDirectory.exists() ) {
					jFileChooser.setCurrentDirectory( rootDirectory );
				}
			}
			jFileChooser.setDialogType( javax.swing.JFileChooser.SAVE_DIALOG );
			jFileChooser.setApproveButtonText( "Set Root Directory" );
			jFileChooser.setFileSelectionMode( javax.swing.JFileChooser.DIRECTORIES_ONLY );
			java.awt.Component awtParent = ImageEditorFrame.this.getView().getAwtComponent();
			int result = jFileChooser.showSaveDialog( awtParent );
			if( result == javax.swing.JFileChooser.APPROVE_OPTION ) {
				java.io.File file = jFileChooser.getSelectedFile();
				if( file != null ) {
					if( file.exists() ) {
						if( file.isDirectory() ) {
							rootDirectoryState.setValueTransactionlessly( file.getAbsolutePath() );
						} else {
							new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "file is not a directory" )
									.buildAndShow();
						}
					} else {
						new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "file does not exist" )
								.buildAndShow();
					}
				} else {
					new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "file is null" )
							.buildAndShow();
				}
				return null;
			} else {
				throw new org.lgna.croquet.CancelException();
			}
		}
	} );

	private final SaveOperation saveOperation = new SaveOperation( this );

	private final org.lgna.croquet.Operation clearOperation = this.createActionOperation( "clearOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			clearShapes();
			//todo
			getView().repaint();
			return null;
		}
	} );

	private final org.lgna.croquet.Operation cropOperation = this.createActionOperation( "cropOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			crop();
			return null;
		}
	} );

	private final org.lgna.croquet.Operation uncropOperation = this.createActionOperation( "uncropOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			uncrop();
			return null;
		}
	} );

	private final org.lgna.croquet.Operation copyOperation = this.createActionOperation( "copyOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			if( isGoodToGoCroppingIfNecessary() ) {
				copyImageToClipboard( getView().render() );
				return null;
			} else {
				throw new org.lgna.croquet.CancelException();
			}
		}
	} );

	private final org.lgna.croquet.BooleanState showDashedBorderState = this.createBooleanState( "showDashedBorderState", true );
	private final org.lgna.croquet.BooleanState showInScreenResolutionState = this.createBooleanState( "showInScreenResolutionState", true );
	private final org.lgna.croquet.BooleanState dropShadowState = this.createBooleanState( "dropShadowState", true );

	private final org.lgna.croquet.ImmutableDataSingleSelectListState<Tool> toolState = this.createImmutableListStateForEnum( "toolState", Tool.class, new org.lgna.croquet.codecs.EnumCodec.LocalizationCustomizer<Tool>() {
		@Override
		public String customize( String localization, Tool value ) {
			if( value == Tool.ADD_RECTANGLE ) {
				return localization + " (F11)";
			} else {
				return localization + " (F12)";
			}
		}
	}, Tool.ADD_RECTANGLE );

	private final org.lgna.croquet.ValueHolder<java.awt.Image> imageHolder = org.lgna.croquet.ValueHolder.createInstance( null );

	private final org.lgna.croquet.ValueHolder<String> pathHolder = org.lgna.croquet.ValueHolder.createInstance( INVALID_PATH_EMPTY_SUB_PATH );

	private final java.util.List<java.awt.Shape> shapes = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final org.lgna.croquet.ValueHolder<java.awt.Rectangle> cropSelectHolder = org.lgna.croquet.ValueHolder.createInstance( null );

	private final org.lgna.croquet.ValueHolder<java.awt.Rectangle> cropCommitHolder = org.lgna.croquet.ValueHolder.createInstance( null );

	private final FilenameComboBoxModel filenameComboBoxModel = new FilenameComboBoxModel();

	private FilenameListWorker worker;

	private final org.lgna.croquet.event.ValueListener<java.awt.Rectangle> cropSelectListener = new org.lgna.croquet.event.ValueListener<java.awt.Rectangle>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.awt.Rectangle> e ) {
			handleCropSelectChanged( e );
		}
	};

	private final org.lgna.croquet.event.ValueListener<java.awt.Rectangle> cropCommitListener = new org.lgna.croquet.event.ValueListener<java.awt.Rectangle>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.awt.Rectangle> e ) {
			handleCropCommitChanged( e );
		}
	};

	private final org.lgna.croquet.event.ValueListener<String> rootDirectoryListener = new org.lgna.croquet.event.ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			java.io.File file = new java.io.File( e.getNextValue() );
			synchronized( filenameComboBoxModel ) {
				if( worker != null ) {
					if( worker.getRootDirectory().equals( file ) ) {
						//pass
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "equal", worker.getRootDirectory(), file );
					} else {
						if( worker.isDone() ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "cancel" );
							worker.cancel( true );
						}
						worker = null;
					}
				}
				if( worker != null ) {
					//pass
				} else {
					if( file.isDirectory() ) {
						worker = new FilenameListWorker( filenameComboBoxModel, file );
						filenameComboBoxModel.prologue();
						worker.execute();
					} else {
						filenameComboBoxModel.setSelectedItem( "" );
						filenameComboBoxModel.prologue();
						filenameComboBoxModel.done( new java.io.File[ 0 ] );
					}
				}
			}
			updatePath();
		}
	};

	private final javax.swing.event.DocumentListener editorListener = new javax.swing.event.DocumentListener() {
		@Override
		public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			handleEditorChanged( e );
		}

		@Override
		public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			handleEditorChanged( e );
		}

		@Override
		public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			handleEditorChanged( e );
		}
	};

	//todo
	private final javax.swing.JComboBox jComboBox = new javax.swing.JComboBox( this.filenameComboBoxModel );

	public ImageEditorFrame() {
		super( java.util.UUID.fromString( "19b37463-3d9a-44eb-9682-6d5ddf73f5b3" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP ); //todo?
		this.jComboBox.setEditable( true );
		this.saveOperation.setEnabled( false );
		this.cropOperation.setEnabled( false );
		this.uncropOperation.setEnabled( false );
	}

	/* package-private */boolean isGoodToGoCroppingIfNecessary() {
		java.awt.Rectangle cropSelection = this.getCropSelectHolder().getValue();
		if( cropSelection != null ) {
			int result = javax.swing.JOptionPane.showConfirmDialog( this.getView().getAwtComponent(), "You have set a crop rectangle.  Commit this crop and continue?", "Crop?", javax.swing.JOptionPane.OK_CANCEL_OPTION );
			if( result == javax.swing.JOptionPane.OK_OPTION ) {
				this.crop();
			} else {
				//cancel
				return false;
			}
		}
		return true;
	}

	public org.lgna.croquet.ValueHolder<java.awt.Image> getImageHolder() {
		return this.imageHolder;
	}

	public org.lgna.croquet.ValueHolder<String> getPathHolder() {
		return this.pathHolder;
	}

	public org.lgna.croquet.ValueHolder<java.awt.Rectangle> getCropSelectHolder() {
		return this.cropSelectHolder;
	}

	public org.lgna.croquet.ValueHolder<java.awt.Rectangle> getCropCommitHolder() {
		return this.cropCommitHolder;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<Tool> getToolState() {
		return this.toolState;
	}

	public org.lgna.croquet.Operation getCropOperation() {
		return this.cropOperation;
	}

	public org.lgna.croquet.Operation getUncropOperation() {
		return this.uncropOperation;
	}

	public org.lgna.croquet.Operation getClearOperation() {
		return this.clearOperation;
	}

	public org.lgna.croquet.Operation getCopyOperation() {
		return this.copyOperation;
	}

	public org.lgna.croquet.BooleanState getShowDashedBorderState() {
		return this.showDashedBorderState;
	}

	public org.lgna.croquet.BooleanState getShowInScreenResolutionState() {
		return this.showInScreenResolutionState;
	}

	public org.lgna.croquet.BooleanState getDropShadowState() {
		return this.dropShadowState;
	}

	public org.lgna.croquet.StringState getRootDirectoryState() {
		return this.rootDirectoryState;
	}

	public javax.swing.JComboBox getJComboBox() {
		return this.jComboBox;
	}

	public org.lgna.croquet.Operation getBrowseOperation() {
		return this.browseOperation;
	}

	public org.lgna.croquet.Operation getSaveOperation() {
		return this.saveOperation;
	}

	private void copyImageToClipboard( java.awt.Image image ) {
		if( image != null ) {
			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image, 300 );
		}
	}

	/* package-private */void crop() {
		this.cropCommitHolder.setValue( this.cropSelectHolder.getValue() );
		this.cropSelectHolder.setValue( null );
		this.getView().setDefaultButtonToSave();
		this.getView().revalidateAndRepaint();
	}

	private void uncrop() {
		this.cropSelectHolder.setValue( this.cropCommitHolder.getValue() );
		this.cropCommitHolder.setValue( null );
		this.getView().revalidateAndRepaint();
	}

	private void handleCropSelectChanged( org.lgna.croquet.event.ValueEvent<java.awt.Rectangle> e ) {
		this.cropOperation.setEnabled( e.getNextValue() != null );
		if( this.cropOperation.isEnabled() ) {
			this.getView().setDefaultButtonToCrop();
		} else {
			this.getView().setDefaultButtonToSave();
		}
	}

	private void handleCropCommitChanged( org.lgna.croquet.event.ValueEvent<java.awt.Rectangle> e ) {
		this.uncropOperation.setEnabled( e.getNextValue() != null );
	}

	public void addShape( java.awt.Shape shape ) {
		this.shapes.add( shape );
	}

	public void removeShape( java.awt.Shape shape ) {
		this.shapes.remove( shape );
	}

	public void clearShapes() {
		this.shapes.clear();
	}

	public java.util.List<java.awt.Shape> getShapes() {
		return java.util.Collections.unmodifiableList( this.shapes );
	}

	@Override
	protected org.alice.imageeditor.croquet.views.ImageEditorPane createView() {
		return new org.alice.imageeditor.croquet.views.ImageEditorPane( this );
	}

	public void setImageClearShapesAndShowFrame( java.awt.Image image ) {
		this.clearShapes();
		this.imageHolder.setValue( image );
		this.getIsFrameShowingState().setValueTransactionlessly( true );
		org.lgna.croquet.views.AbstractWindow<?> window = this.getView().getRoot();
		if( window != null ) {
			this.getView().revalidateAndRepaint();
			window.pack();
		}
	}

	public java.io.File getFile() {
		String path = this.pathHolder.getValue();
		return new java.io.File( path );
	}

	private void updatePath() {
		String rootDirectoryPath = this.rootDirectoryState.getValue();
		java.awt.Component awtComponent = this.jComboBox.getEditor().getEditorComponent();
		if( awtComponent instanceof javax.swing.JTextField ) {
			javax.swing.JTextField jTextField = (javax.swing.JTextField)awtComponent;
			javax.swing.text.Document document = jTextField.getDocument();
			String text = edu.cmu.cs.dennisc.javax.swing.DocumentUtilities.getText( document );
			if( text.length() > 0 ) {
				if( text.endsWith( ".png" ) ) {
					//pass
				} else if( text.endsWith( ".pn" ) ) {
					text = text + "g";
				} else if( text.endsWith( ".p" ) ) {
					text = text + "ng";
				} else if( text.endsWith( "." ) ) {
					text = text + "png";
				} else {
					text = text + ".png";
				}
				java.io.File rootDirectory = new java.io.File( rootDirectoryPath );
				java.io.File file;
				String path;
				if( rootDirectory.isDirectory() ) {
					if( text.startsWith( rootDirectoryPath ) ) {
						file = new java.io.File( text );
					} else {
						file = new java.io.File( new java.io.File( rootDirectoryPath ), text );
					}
					path = file.getAbsolutePath();
				} else {
					file = null;
					path = INVALID_PATH_NOT_A_DIRECTORY;
				}
				this.pathHolder.setValue( path );
				if( ( file != null ) && file.isFile() ) {
					this.saveOperation.setName( "save over..." );
				} else {
					this.saveOperation.setName( "save" );
				}
				this.saveOperation.setEnabled( edu.cmu.cs.dennisc.java.io.FileUtilities.isValidFile( file ) );
			} else {
				this.pathHolder.setValue( INVALID_PATH_EMPTY_SUB_PATH );
				this.saveOperation.setName( "save" );
				this.saveOperation.setEnabled( false );
			}
		} else {
			throw new RuntimeException( String.valueOf( awtComponent ) );
		}
	}

	private void handleEditorChanged( javax.swing.event.DocumentEvent e ) {
		String text = edu.cmu.cs.dennisc.javax.swing.DocumentUtilities.getText( e.getDocument() );
		this.filenameComboBoxModel.setSelectedItem( text );
		this.updatePath();
	}

	@Override
	public void handlePreActivation() {
		this.rootDirectoryState.addAndInvokeNewSchoolValueListener( this.rootDirectoryListener );
		java.awt.Component awtEditorComponent = this.getJComboBox().getEditor().getEditorComponent();
		if( awtEditorComponent instanceof javax.swing.JTextField ) {
			javax.swing.JTextField jTextField = (javax.swing.JTextField)awtEditorComponent;
			jTextField.getDocument().addDocumentListener( this.editorListener );
		}
		this.updatePath();
		this.cropSelectHolder.addValueListener( this.cropSelectListener );
		this.cropCommitHolder.addValueListener( this.cropCommitListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.cropCommitHolder.removeValueListener( this.cropCommitListener );
		this.cropSelectHolder.removeValueListener( this.cropSelectListener );
		java.awt.Component awtEditorComponent = this.getJComboBox().getEditor().getEditorComponent();
		if( awtEditorComponent instanceof javax.swing.JTextField ) {
			javax.swing.JTextField jTextField = (javax.swing.JTextField)awtEditorComponent;
			jTextField.getDocument().removeDocumentListener( this.editorListener );
		}
		this.rootDirectoryState.removeNewSchoolValueListener( this.rootDirectoryListener );
		if( this.worker != null ) {
			if( this.worker.isCancelled() || this.worker.isDone() ) {
				//pass
			} else {
				this.worker.cancel( true );
			}
		}
	}

	//	public static void main( String[] args ) throws Exception {
	//		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
	//
	//		//final javax.swing.ImageIcon icon = new javax.swing.ImageIcon( org.alice.ide.warning.components.WarningView.class.getResource( "images/toxic.png" ) );
	//		final java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read( org.alice.ide.warning.components.WarningView.class.getResource( "images/toxic.png" ) );
	//		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
	//		final ImageEditorFrame imageComposite = new ImageEditorFrame();
	//		imageComposite.getShowInScreenResolutionState().setValueTransactionlessly( false );
	//		imageComposite.getToolState().setValueTransactionlessly( Tool.CROP_SELECT );
	//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
	//			public void run() {
	//				imageComposite.setImageClearShapesAndShowFrame( image );
	//				( (org.lgna.croquet.components.Frame)imageComposite.getView().getRoot() ).setDefaultCloseOperation( org.lgna.croquet.components.Frame.DefaultCloseOperation.EXIT );
	//			}
	//		} );
	//	}
}
