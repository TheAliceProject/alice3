/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.imageeditor.croquet;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorFrame extends org.lgna.croquet.FrameComposite<org.alice.imageeditor.croquet.views.ImageEditorPane> {
	private static java.io.File getBestGuessPicturesDirectory() {
		java.io.File defaultDirectory = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
		java.io.File userDirectory;
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			userDirectory = defaultDirectory.getParentFile();
		} else {
			userDirectory = defaultDirectory;
		}
		java.io.File file = new java.io.File( userDirectory, "Pictures" );
		if( file.isDirectory() ) {
			return file;
		} else {
			return defaultDirectory;
		}
	}

	//private static final java.io.File DEFAULT_DIRECTORY = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory();
	private static final String DEFAULT_ROOT_DIRECTORY_PATH = getBestGuessPicturesDirectory().getAbsolutePath();

	private final org.lgna.croquet.ValueHolder<java.awt.Image> imageHolder = new org.lgna.croquet.ValueHolder<java.awt.Image>();
	private final java.util.List<java.awt.Shape> shapes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private final org.lgna.croquet.StringState rootDirectoryState = this.createPreferenceStringState( this.createKey( "rootDirectoryState" ), DEFAULT_ROOT_DIRECTORY_PATH, null );

	private final org.lgna.croquet.Operation browseOperation = this.createActionOperation( this.createKey( "browseOperation" ), new Action() {
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
							org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "file is not a directory" );
						}
					} else {
						org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "file is does not exist" );
					}
				} else {
					org.lgna.croquet.Application.getActiveInstance().showMessageDialog( "file is null" );
				}
				return null;
			} else {
				throw new org.lgna.croquet.CancelException();
			}
		}
	} );

	private class FilenameComboBoxModel implements javax.swing.ComboBoxModel {
		private java.io.File[] data;

		private Object selectedItem;

		private final java.util.List<javax.swing.event.ListDataListener> listDataListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

		public int getSize() {
			return this.data != null ? this.data.length : 0;
		}

		public Object getElementAt( int index ) {
			return this.data[ index ];
		}

		public Object getSelectedItem() {
			return this.selectedItem;
		}

		public void setSelectedItem( Object selectedItem ) {
			if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( this.selectedItem, selectedItem ) ) {
				//pass
			} else {
				if( ( this.selectedItem != null ) && ( selectedItem != null ) && this.selectedItem.toString().contentEquals( selectedItem.toString() ) ) {
					//pass
				} else {
					this.selectedItem = selectedItem;
					this.fireContentsChanged();
				}
			}
		}

		public void addListDataListener( javax.swing.event.ListDataListener listener ) {
			this.listDataListeners.add( listener );
		}

		public void removeListDataListener( javax.swing.event.ListDataListener listener ) {
			this.listDataListeners.remove( listener );
		}

		public void setData( java.io.File[] data ) {
			this.data = data;
			this.fireContentsChanged();
		}

		private void fireContentsChanged() {
			javax.swing.event.ListDataEvent e = new javax.swing.event.ListDataEvent( this, javax.swing.event.ListDataEvent.CONTENTS_CHANGED, -1, -1 );
			for( javax.swing.event.ListDataListener listDataListener : listDataListeners ) {
				listDataListener.contentsChanged( e );
			}
		}
	}

	private final FilenameComboBoxModel filenameComboBoxModel = new FilenameComboBoxModel();

	private class FilenameListWorker extends org.lgna.croquet.worker.WorkerWithProgress<java.io.File[], java.io.File[]> {
		private final java.io.File rootDirectory;

		public FilenameListWorker( java.io.File rootDirectory ) {
			this.rootDirectory = rootDirectory;
		}

		@Override
		protected java.io.File[] do_onBackgroundThread() throws Exception {
			if( this.rootDirectory.isDirectory() ) {
				return edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( this.rootDirectory, "png" );
			} else {
				return new java.io.File[ 0 ];
			}
		}

		@Override
		protected void handleDone_onEventDispatchThread( java.io.File[] value ) {
			filenameComboBoxModel.setData( value );
		}

		@Override
		protected void handleProcess_onEventDispatchThread( java.util.List<java.io.File[]> chunks ) {
		}
	}

	private FilenameListWorker worker;

	private final SaveOperation saveOperation = new SaveOperation( this );

	private static final java.io.File[] IN_THE_MIDST_OF_WORKING_DATA = new java.io.File[] { null };//, null, null, null, null, null, null, null };

	private final org.lgna.croquet.State.ValueListener<String> rootDirectoryListener = new org.lgna.croquet.State.ValueListener<String>() {
		public void changing( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			java.io.File file = new java.io.File( nextValue );
			if( file.isDirectory() ) {
				if( worker != null ) {
					if( worker.rootDirectory.equals( file ) ) {
						//pass
						edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "equal", worker.rootDirectory, file );
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
					filenameComboBoxModel.setData( IN_THE_MIDST_OF_WORKING_DATA );
					worker = new FilenameListWorker( file );
					worker.execute();
				}
			}
		}
	};

	public ImageEditorFrame() {
		super( java.util.UUID.fromString( "19b37463-3d9a-44eb-9682-6d5ddf73f5b3" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP ); //todo?
	}

	public org.lgna.croquet.ValueHolder<java.awt.Image> getImageHolder() {
		return this.imageHolder;
	}

	public org.lgna.croquet.StringState getRootDirectoryState() {
		return this.rootDirectoryState;
	}

	public FilenameComboBoxModel getFilenameComboBoxModel() {
		return this.filenameComboBoxModel;
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

	public void addShape( java.awt.Shape shape ) {
		this.shapes.add( shape );
		this.copyImageToClipboard( this.getView().render() );
	}

	public void removeShape( java.awt.Shape shape ) {
		this.shapes.remove( shape );
		this.copyImageToClipboard( this.getView().render() );
	}

	public void clearShapes() {
		this.shapes.clear();
		this.copyImageToClipboard( this.imageHolder.getValue() );
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
		this.getBooleanState().setValueTransactionlessly( true );
		org.lgna.croquet.components.AbstractWindow<?> window = this.getView().getRoot();
		if( window != null ) {
			window.pack();
		}
	}

	/* package-private */java.io.File getFile() {
		Object selectedItem = this.filenameComboBoxModel.getSelectedItem();
		if( selectedItem instanceof java.io.File ) {
			java.io.File file = (java.io.File)selectedItem;
			return file;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( selectedItem );
			return null;
		}
	}

	@Override
	public void handlePreActivation() {
		this.rootDirectoryState.addAndInvokeValueListener( this.rootDirectoryListener );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		super.handlePostDeactivation();
		this.rootDirectoryState.removeValueListener( this.rootDirectoryListener );
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		final javax.swing.ImageIcon icon = new javax.swing.ImageIcon( org.alice.stageide.StageIDE.class.getResource( "images/SplashScreen.png" ) );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		final ImageEditorFrame imageComposite = new ImageEditorFrame();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				imageComposite.setImageClearShapesAndShowFrame( icon.getImage() );
			}
		} );
	}
}
