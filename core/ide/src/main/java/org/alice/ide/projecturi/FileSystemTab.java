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

package org.alice.ide.projecturi;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.alice.ide.ProjectApplication;
import org.alice.ide.projecturi.views.FileSystemPane;
import org.alice.ide.uricontent.FileProjectLoader;
import org.alice.ide.uricontent.UriProjectLoader;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.AbstractComposite;
import org.lgna.croquet.Application;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringState;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.io.IoUtilities;

import java.io.File;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class FileSystemTab extends SelectUriTab {
	private final StringState pathState = this.createStringState( "pathState" );
	private final Operation browseOperation = this.createActionOperation( "browseOperation", new Action() {
		@Override
		public Edit perform( CompletionStep<?> step, AbstractComposite.InternalActionOperation source ) throws CancelException {
			final StageIDE ide = StageIDE.getActiveInstance();
			File file = ide.getDocumentFrame().showOpenFileDialog( ide.getProjectsDirectory(), null, IoUtilities.PROJECT_EXTENSION, true );
			if( file != null ) {
				FileSystemTab.this.pathState.setValueTransactionlessly( FileUtilities.getCanonicalPathIfPossible( file ) );
			}
			return null;
		}
	} );

	public FileSystemTab() {
		super( UUID.fromString( "b1698424-1f0e-4499-852a-da627fa9e789" ) );
	}

	@Override
	protected ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	public StringState getPathState() {
		return this.pathState;
	}

	public Operation getBrowseOperation() {
		return this.browseOperation;
	}

	@Override
	public UriProjectLoader getSelectedUri() {
		String path = this.pathState.getValue();
		File file = new File( path );
		if( file.exists() ) {
			return new FileProjectLoader( file );
		} else {
			return null;
		}
	}

	@Override
	protected void refresh() {
	}

	@Override
	protected FileSystemPane createView() {
		return new FileSystemPane( this );
	}
}
