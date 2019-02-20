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

import edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.io.UserDirectoryUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.DocumentUtilities;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.imageeditor.croquet.views.ImageEditorPane;
import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.FrameCompositeWithInternalIsShowingState;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringState;
import org.lgna.croquet.ValueHolder;
import org.lgna.croquet.codecs.EnumCodec;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.AbstractWindow;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorFrame extends FrameCompositeWithInternalIsShowingState<ImageEditorPane> {
	public static String INVALID_PATH_NOT_A_DIRECTORY = "INVALID_PATH_NOT_A_DIRECTORY";
	public static String INVALID_PATH_EMPTY_SUB_PATH = "INVALID_PATH_EMPTY_SUB_PATH";
	private static final String DEFAULT_ROOT_DIRECTORY_PATH = UserDirectoryUtilities.getBestGuessPicturesDirectory().getAbsolutePath();

	private final StringState rootDirectoryState = this.createPreferenceStringState( "rootDirectoryState", DEFAULT_ROOT_DIRECTORY_PATH, null );

	private final Operation browseOperation = this.createActionOperation( "browseOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			String rootDirectoryPath = rootDirectoryState.getValue();
			JFileChooser jFileChooser = new JFileChooser();
			if( ( rootDirectoryPath != null ) && ( rootDirectoryPath.length() > 0 ) ) {
				File rootDirectory = new File( rootDirectoryPath );
				if( rootDirectory.exists() ) {
					jFileChooser.setCurrentDirectory( rootDirectory );
				}
			}
			jFileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
			jFileChooser.setApproveButtonText( "Set Root Directory" );
			jFileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
			Component awtParent = ImageEditorFrame.this.getView().getAwtComponent();
			int result = jFileChooser.showSaveDialog( awtParent );
			if( result == JFileChooser.APPROVE_OPTION ) {
				File file = jFileChooser.getSelectedFile();
				if( file != null ) {
					if( file.exists() ) {
						if( file.isDirectory() ) {
							rootDirectoryState.setValueTransactionlessly( file.getAbsolutePath() );
						} else {
							Dialogs.showInfo( "file is not a directory" );
						}
					} else {
						Dialogs.showInfo( "file does not exist" );
					}
				} else {
					Dialogs.showInfo( "file is null" );
				}
				return null;
			} else {
				throw new CancelException();
			}
		}
	} );

	private final SaveOperation saveOperation = new SaveOperation( this );

	private final Operation clearOperation = this.createActionOperation( "clearOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			clearShapes();
			//todo
			getView().repaint();
			return null;
		}
	} );

	private final Operation cropOperation = this.createActionOperation( "cropOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			crop();
			return null;
		}
	} );

	private final Operation uncropOperation = this.createActionOperation( "uncropOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			uncrop();
			return null;
		}
	} );

	private final Operation copyOperation = this.createActionOperation( "copyOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			if( isGoodToGoCroppingIfNecessary() ) {
				copyImageToClipboard( getView().render() );
				return null;
			} else {
				throw new CancelException();
			}
		}
	} );

	private final BooleanState showDashedBorderState = this.createBooleanState( "showDashedBorderState", true );
	private final BooleanState showInScreenResolutionState = this.createBooleanState( "showInScreenResolutionState", true );
	private final BooleanState dropShadowState = this.createBooleanState( "dropShadowState", true );

	private final ImmutableDataSingleSelectListState<Tool> toolState = this.createImmutableListStateForEnum( "toolState", Tool.class, new EnumCodec.LocalizationCustomizer<Tool>() {
		@Override
		public String customize( String localization, Tool value ) {
			if( value == Tool.ADD_RECTANGLE ) {
				return localization + " (F11)";
			} else {
				return localization + " (F12)";
			}
		}
	}, Tool.ADD_RECTANGLE );

	private final ValueHolder<Image> imageHolder = ValueHolder.createInstance( null );

	private final ValueHolder<String> pathHolder = ValueHolder.createInstance( INVALID_PATH_EMPTY_SUB_PATH );

	private final List<Shape> shapes = Lists.newCopyOnWriteArrayList();

	private final ValueHolder<Rectangle> cropSelectHolder = ValueHolder.createInstance( null );

	private final ValueHolder<Rectangle> cropCommitHolder = ValueHolder.createInstance( null );

	private final FilenameComboBoxModel filenameComboBoxModel = new FilenameComboBoxModel();

	private FilenameListWorker worker;

	private final ValueListener<Rectangle> cropSelectListener = new ValueListener<Rectangle>() {
		@Override
		public void valueChanged( ValueEvent<Rectangle> e ) {
			handleCropSelectChanged( e );
		}
	};

	private final ValueListener<Rectangle> cropCommitListener = new ValueListener<Rectangle>() {
		@Override
		public void valueChanged( ValueEvent<Rectangle> e ) {
			handleCropCommitChanged( e );
		}
	};

	private final ValueListener<String> rootDirectoryListener = new ValueListener<String>() {
		@Override
		public void valueChanged( ValueEvent<String> e ) {
			File file = new File( e.getNextValue() );
			synchronized( filenameComboBoxModel ) {
				if( worker != null ) {
					if( worker.getRootDirectory().equals( file ) ) {
						//pass
						Logger.outln( "equal", worker.getRootDirectory(), file );
					} else {
						if( worker.isDone() ) {
							//pass
						} else {
							Logger.outln( "cancel" );
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
						filenameComboBoxModel.done( new File[ 0 ] );
					}
				}
			}
			updatePath();
		}
	};

	private final DocumentListener editorListener = new DocumentListener() {
		@Override
		public void changedUpdate( DocumentEvent e ) {
			handleEditorChanged( e );
		}

		@Override
		public void insertUpdate( DocumentEvent e ) {
			handleEditorChanged( e );
		}

		@Override
		public void removeUpdate( DocumentEvent e ) {
			handleEditorChanged( e );
		}
	};

	//todo
	private final JComboBox jComboBox = new JComboBox( this.filenameComboBoxModel );

	public ImageEditorFrame() {
		super( UUID.fromString( "19b37463-3d9a-44eb-9682-6d5ddf73f5b3" ), Application.DOCUMENT_UI_GROUP ); //todo?
		this.jComboBox.setEditable( true );
		this.saveOperation.setEnabled( false );
		this.cropOperation.setEnabled( false );
		this.uncropOperation.setEnabled( false );
	}

	/* package-private */boolean isGoodToGoCroppingIfNecessary() {
		Rectangle cropSelection = this.getCropSelectHolder().getValue();
		if( cropSelection != null ) {
			int result = JOptionPane.showConfirmDialog( this.getView().getAwtComponent(), "You have set a crop rectangle.  Commit this crop and continue?", "Crop?", JOptionPane.OK_CANCEL_OPTION );
			if( result == JOptionPane.OK_OPTION ) {
				this.crop();
			} else {
				//cancel
				return false;
			}
		}
		return true;
	}

	public ValueHolder<Image> getImageHolder() {
		return this.imageHolder;
	}

	public ValueHolder<String> getPathHolder() {
		return this.pathHolder;
	}

	public ValueHolder<Rectangle> getCropSelectHolder() {
		return this.cropSelectHolder;
	}

	public ValueHolder<Rectangle> getCropCommitHolder() {
		return this.cropCommitHolder;
	}

	public ImmutableDataSingleSelectListState<Tool> getToolState() {
		return this.toolState;
	}

	public Operation getCropOperation() {
		return this.cropOperation;
	}

	public Operation getUncropOperation() {
		return this.uncropOperation;
	}

	public Operation getClearOperation() {
		return this.clearOperation;
	}

	public Operation getCopyOperation() {
		return this.copyOperation;
	}

	public BooleanState getShowDashedBorderState() {
		return this.showDashedBorderState;
	}

	public BooleanState getShowInScreenResolutionState() {
		return this.showInScreenResolutionState;
	}

	public BooleanState getDropShadowState() {
		return this.dropShadowState;
	}

	public StringState getRootDirectoryState() {
		return this.rootDirectoryState;
	}

	public JComboBox getJComboBox() {
		return this.jComboBox;
	}

	public Operation getBrowseOperation() {
		return this.browseOperation;
	}

	public Operation getSaveOperation() {
		return this.saveOperation;
	}

	private void copyImageToClipboard( Image image ) {
		if( image != null ) {
			ClipboardUtilities.setClipboardContents( image, 300 );
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

	private void handleCropSelectChanged( ValueEvent<Rectangle> e ) {
		this.cropOperation.setEnabled( e.getNextValue() != null );
		if( this.cropOperation.isEnabled() ) {
			this.getView().setDefaultButtonToCrop();
		} else {
			this.getView().setDefaultButtonToSave();
		}
	}

	private void handleCropCommitChanged( ValueEvent<Rectangle> e ) {
		this.uncropOperation.setEnabled( e.getNextValue() != null );
	}

	public void addShape( Shape shape ) {
		this.shapes.add( shape );
	}

	public void removeShape( Shape shape ) {
		this.shapes.remove( shape );
	}

	public void clearShapes() {
		this.shapes.clear();
	}

	public List<Shape> getShapes() {
		return Collections.unmodifiableList( this.shapes );
	}

	@Override
	protected ImageEditorPane createView() {
		return new ImageEditorPane( this );
	}

	public void setImageClearShapesAndShowFrame( Image image ) {
		this.clearShapes();
		this.imageHolder.setValue( image );
		this.getIsFrameShowingState().setValueTransactionlessly( true );
		AbstractWindow<?> window = this.getView().getRoot();
		if( window != null ) {
			this.getView().revalidateAndRepaint();
			window.pack();
		}
	}

	public File getFile() {
		String path = this.pathHolder.getValue();
		return new File( path );
	}

	private void updatePath() {
		String rootDirectoryPath = this.rootDirectoryState.getValue();
		Component awtComponent = this.jComboBox.getEditor().getEditorComponent();
		if( awtComponent instanceof JTextField ) {
			JTextField jTextField = (JTextField)awtComponent;
			Document document = jTextField.getDocument();
			String text = DocumentUtilities.getText( document );
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
				File rootDirectory = new File( rootDirectoryPath );
				File file;
				String path;
				if( rootDirectory.isDirectory() ) {
					if( text.startsWith( rootDirectoryPath ) ) {
						file = new File( text );
					} else {
						file = new File( new File( rootDirectoryPath ), text );
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
				this.saveOperation.setEnabled( FileUtilities.isValidFile( file ) );
			} else {
				this.pathHolder.setValue( INVALID_PATH_EMPTY_SUB_PATH );
				this.saveOperation.setName( "save" );
				this.saveOperation.setEnabled( false );
			}
		} else {
			throw new RuntimeException( String.valueOf( awtComponent ) );
		}
	}

	private void handleEditorChanged( DocumentEvent e ) {
		String text = DocumentUtilities.getText( e.getDocument() );
		this.filenameComboBoxModel.setSelectedItem( text );
		this.updatePath();
	}

	@Override
	public void handlePreActivation() {
		this.rootDirectoryState.addAndInvokeNewSchoolValueListener( this.rootDirectoryListener );
		Component awtEditorComponent = this.getJComboBox().getEditor().getEditorComponent();
		if( awtEditorComponent instanceof JTextField ) {
			JTextField jTextField = (JTextField)awtEditorComponent;
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
		Component awtEditorComponent = this.getJComboBox().getEditor().getEditorComponent();
		if( awtEditorComponent instanceof JTextField ) {
			JTextField jTextField = (JTextField)awtEditorComponent;
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
