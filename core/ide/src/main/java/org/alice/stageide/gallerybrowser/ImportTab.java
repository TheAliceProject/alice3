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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public final class ImportTab extends GalleryTab {
	private final org.lgna.croquet.PlainStringValue notDirectoryText = this.createStringValue( "notDirectoryText" );
	private final org.lgna.croquet.PlainStringValue noFilesText = this.createStringValue( "noFilesText" );
	private final org.lgna.croquet.StringState directoryState = this.createStringState( "directoryState" );
	private final org.lgna.croquet.Operation browseOperation = this.createActionOperation( "browseOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();

			java.io.File directory = new java.io.File( directoryState.getValue() );
			if( directory.isDirectory() ) {
				fileChooser.setCurrentDirectory( directory );
			}
			fileChooser.setFileSelectionMode( javax.swing.JFileChooser.FILES_AND_DIRECTORIES );
			fileChooser.setFileFilter( new javax.swing.filechooser.FileNameExtensionFilter( "*." + org.lgna.project.io.IoUtilities.TYPE_EXTENSION, org.lgna.project.io.IoUtilities.TYPE_EXTENSION ) );
			int option = fileChooser.showOpenDialog( null );
			switch( option ) {
			case javax.swing.JFileChooser.APPROVE_OPTION:
				java.io.File file = fileChooser.getSelectedFile();
				if( file.isFile() ) {
					file = file.getParentFile();
				}
				directoryState.setValueTransactionlessly( file.getAbsolutePath() );
				break;
			default:
				throw new org.lgna.croquet.CancelException();
			}
			return null;
		}
	} );
	private final org.lgna.croquet.Operation restoreToDefaultOperation = this.createActionOperation( "restoreToDefaultOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			restoreToDefault();
			return null;
		}
	} );

	public ImportTab() {
		super( java.util.UUID.fromString( "89ae8138-80a3-40e8-a8e6-e2f9b47ac452" ) );
		this.restoreToDefault();
		this.browseOperation.setButtonIcon( org.alice.ide.icons.Icons.FOLDER_ICON_SMALL );
	}

	public boolean isDirectoryStateSetToDefault() {
		return org.alice.ide.croquet.models.ui.preferences.UserTypesDirectoryState.getInstance().getDirectoryEnsuringExistance().getAbsolutePath().contentEquals( this.directoryState.getValue() );
	}

	private void restoreToDefault() {
		this.directoryState.setValueTransactionlessly( org.alice.ide.croquet.models.ui.preferences.UserTypesDirectoryState.getInstance().getDirectoryEnsuringExistance().getAbsolutePath() );
	}

	public org.lgna.croquet.PlainStringValue getNotDirectoryText() {
		return this.notDirectoryText;
	}

	public org.lgna.croquet.PlainStringValue getNoFilesText() {
		return this.noFilesText;
	}

	public org.lgna.croquet.StringState getDirectoryState() {
		return this.directoryState;
	}

	public org.lgna.croquet.Operation getBrowseOperation() {
		return this.browseOperation;
	}

	public org.lgna.croquet.Operation getRestoreToDefaultOperation() {
		return this.restoreToDefaultOperation;
	}

	@Override
	protected org.alice.stageide.gallerybrowser.views.ImportTabView createView() {
		return new org.alice.stageide.gallerybrowser.views.ImportTabView( this );
	}
}
