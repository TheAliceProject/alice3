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
package org.alice.ide.ast.export;

/**
 * @author Dennis Cosgrove
 */
public class ExportTypeToFileDialogOperation extends org.lgna.croquet.FileDialogOperation {
	private final org.lgna.project.ast.NamedUserType type;

	public ExportTypeToFileDialogOperation( org.lgna.project.ast.NamedUserType type ) {
		super( org.alice.ide.IDE.EXPORT_GROUP, java.util.UUID.fromString( "000e1da5-0494-4afc-bb05-2fd0c0a46163" ) );
		this.type = type;
		this.setButtonIcon( org.alice.ide.icons.Icons.FOLDER_ICON_SMALL );
	}

	private java.io.File getDefaultDirectory() {
		return org.alice.ide.croquet.models.ui.preferences.UserTypesDirectoryState.getInstance().getDirectoryEnsuringExistance();
	}

	private String getExtension() {
		return org.lgna.project.io.IoUtilities.TYPE_EXTENSION;
	}

	private String getInitialFilename() {
		return this.type.name.getValue() + "." + this.getExtension();
	}

	@Override
	protected java.io.File showFileDialog( java.awt.Component awtComponent ) {
		return edu.cmu.cs.dennisc.java.awt.FileDialogUtilities.showSaveFileDialog( awtComponent, this.getDefaultDirectory(), this.getInitialFilename(), this.getExtension(), true );
	}

	@Override
	protected void handleFile( java.io.File file ) throws org.lgna.croquet.CancelException, java.io.IOException {
		org.lgna.project.io.IoUtilities.writeType( file, type, new org.alice.ide.ast.export.type.TypeSummaryDataSource( new org.alice.ide.ast.export.type.TypeSummary( this.type ) ) );
	}
}
