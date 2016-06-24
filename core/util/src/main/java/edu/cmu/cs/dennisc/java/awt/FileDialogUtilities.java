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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class FileDialogUtilities {

	private static interface FileDialog {
		public String getFile();

		public void setFile( String filename );

		public String getDirectory();

		public void setDirectory( String path );

		public java.io.FilenameFilter getFilenameFilter();

		public void setFilenameFilter( java.io.FilenameFilter filenameFilter );

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
		public java.io.FilenameFilter getFilenameFilter() {
			return this.awtFileDialog.getFilenameFilter();
		}

		@Override
		public void setFilenameFilter( java.io.FilenameFilter filenameFilter ) {
			this.awtFileDialog.setFilenameFilter( filenameFilter );
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
		public java.io.FilenameFilter getFilenameFilter() {
			//todo
			return null;
		}

		@Override
		public void setFilenameFilter( java.io.FilenameFilter filenameFilter ) {
			//todo
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

	private static edu.cmu.cs.dennisc.map.MapToMap<java.awt.Component, String, FileDialog> mapPathToLoadFileDialog = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static edu.cmu.cs.dennisc.map.MapToMap<java.awt.Component, String, FileDialog> mapPathToSaveFileDialog = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static java.util.Map<String, String> mapSecondaryKeyToPath = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static FileDialog createFileDialog( java.awt.Component root, String title, int mode ) {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			return new SwingFileDialog( root, title, mode );
		} else {
			return new AwtFileDialog( root, title, mode );
		}
	}

	private static final String NULL_KEY = "null";

	private static java.io.File showFileDialog( java.awt.Component component, String title, int mode, String directoryKey, String filename, String extension, boolean isSharingDesired ) {
		FileDialog fileDialog;
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( component );
		String secondaryKey;
		if( directoryKey != null ) {
			secondaryKey = directoryKey;
		} else {
			secondaryKey = NULL_KEY;
		}
		edu.cmu.cs.dennisc.map.MapToMap<java.awt.Component, String, FileDialog> mapPathToFileDialog;
		if( mode == java.awt.FileDialog.LOAD ) {
			mapPathToFileDialog = FileDialogUtilities.mapPathToLoadFileDialog;
		} else {
			mapPathToFileDialog = FileDialogUtilities.mapPathToSaveFileDialog;
		}
		if( isSharingDesired ) {
			fileDialog = mapPathToFileDialog.get( component, secondaryKey );
		} else {
			fileDialog = null;
		}
		if( fileDialog != null ) {
			//pass
		} else {
			fileDialog = createFileDialog( root, title, mode );
			if( isSharingDesired ) {
				mapPathToFileDialog.put( component, secondaryKey, fileDialog );
			}
		}
		if( filename != null ) {
			fileDialog.setFile( filename );
		}

		String path;
		if( isSharingDesired ) {
			path = FileDialogUtilities.mapSecondaryKeyToPath.get( secondaryKey );
		} else {
			path = null;
		}
		if( path != null ) {
			//pass
		} else {
			path = directoryKey;
		}
		if( path != null ) {
			fileDialog.setDirectory( path );
		}

		fileDialog.show();
		String fileName = fileDialog.getFile();
		java.io.File rv;
		if( fileName != null ) {
			String requestedDirectoryPath = fileDialog.getDirectory();
			if( isSharingDesired ) {
				FileDialogUtilities.mapSecondaryKeyToPath.put( secondaryKey, requestedDirectoryPath );
			}
			java.io.File directory = new java.io.File( requestedDirectoryPath );
			if( mode == java.awt.FileDialog.SAVE ) {
				if( fileName.endsWith( "." + extension ) ) {
					//pass
				} else {
					fileName += "." + extension;
				}
			}
			rv = new java.io.File( directory, fileName );
		} else {
			rv = null;
		}
		return rv;
	}

	public static java.io.File showOpenFileDialog( java.awt.Component component, java.io.File directory, String filename, String extension, boolean isSharingDesired ) {
		return showOpenFileDialog( component, directory != null ? directory.getAbsolutePath() : null, filename, extension, isSharingDesired );
	}

	public static java.io.File showSaveFileDialog( java.awt.Component component, java.io.File directory, String filename, String extension, boolean isSharingDesired ) {
		return showSaveFileDialog( component, directory != null ? directory.getAbsolutePath() : null, filename, extension, isSharingDesired );
	}

	public static java.io.File showOpenFileDialog( java.awt.Component component, String directoryPath, String filename, String extension, boolean isSharingDesired ) {
		return showFileDialog( component, "Open...", java.awt.FileDialog.LOAD, directoryPath, filename, extension, isSharingDesired );
	}

	public static java.io.File showSaveFileDialog( java.awt.Component component, String directoryPath, String filename, String extension, boolean isSharingDesired ) {
		return showFileDialog( component, "Save...", java.awt.FileDialog.SAVE, directoryPath, filename, extension, isSharingDesired );
	}

	private static java.io.File showFileDialog( int mode, java.util.UUID sharingId, java.awt.Component component, String title, java.io.File directory, String filename, java.io.FilenameFilter filenameFilter, String extensionToAddIfMissing ) {
		if( ( extensionToAddIfMissing != null ) && ( extensionToAddIfMissing.charAt( 0 ) == '.' ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "removing leading . from", extensionToAddIfMissing );
			extensionToAddIfMissing = extensionToAddIfMissing.substring( 1 );
		}
		java.awt.Component root = javax.swing.SwingUtilities.getRoot( component );
		FileDialog fileDialog = createFileDialog( root, title, mode );
		if( filename != null ) {
			fileDialog.setFile( filename );
		}
		if( ( directory != null ) && directory.exists() ) {
			fileDialog.setDirectory( directory.toString() );
		}
		if( filenameFilter != null ) {
			fileDialog.setFilenameFilter( filenameFilter );
		}
		fileDialog.show();
		String fileName = fileDialog.getFile();
		java.io.File rv;
		if( fileName != null ) {
			String requestedDirectoryPath = fileDialog.getDirectory();
			//			if( isSharingDesired ) {
			//				FileDialogUtilities.mapSecondaryKeyToPath.put( secondaryKey, requestedDirectoryPath );
			//			}
			java.io.File rvDirectory = new java.io.File( requestedDirectoryPath );
			if( mode == java.awt.FileDialog.SAVE ) {
				if( fileName.endsWith( "." + extensionToAddIfMissing ) ) {
					//pass
				} else {
					fileName += "." + extensionToAddIfMissing;
				}
			}
			rv = new java.io.File( rvDirectory, fileName );
		} else {
			rv = null;
		}
		return rv;
	}

	public static java.io.File showOpenFileDialog( java.util.UUID sharingId, java.awt.Component component, String title, java.io.File initialDirectory, String initialFilename, java.io.FilenameFilter filenameFilter ) {
		return showFileDialog( java.awt.FileDialog.LOAD, sharingId, component, title, initialDirectory, initialFilename, filenameFilter, null );
	}

	public static java.io.File showSaveFileDialog( java.util.UUID sharingId, java.awt.Component component, String title, java.io.File initialDirectory, String initialFilename, java.io.FilenameFilter filenameFilter, String extensionToAddIfMissing ) {
		return showFileDialog( java.awt.FileDialog.SAVE, sharingId, component, title, initialDirectory, initialFilename, filenameFilter, extensionToAddIfMissing );
	}
}
