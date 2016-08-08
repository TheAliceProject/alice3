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

/**
 * @author Dennis Cosgrove
 */
public class FileSystemTab extends SelectUriTab {
	private final org.lgna.croquet.StringState pathState = this.createStringState( "pathState" );
	private final org.lgna.croquet.Operation browseOperation = this.createActionOperation( "browseOperation", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			java.io.File file = org.lgna.croquet.Application.getActiveInstance().getDocumentFrame().showOpenFileDialog( org.alice.ide.ProjectApplication.getActiveInstance().getMyProjectsDirectory(), null, org.lgna.project.io.IoUtilities.PROJECT_EXTENSION, true );
			if( file != null ) {
				FileSystemTab.this.pathState.setValueTransactionlessly( edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible( file ) );
			}
			return null;
		}
	} );

	public FileSystemTab() {
		super( java.util.UUID.fromString( "b1698424-1f0e-4499-852a-da627fa9e789" ) );
	}

	@Override
	protected org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	public org.lgna.croquet.StringState getPathState() {
		return this.pathState;
	}

	public org.lgna.croquet.Operation getBrowseOperation() {
		return this.browseOperation;
	}

	@Override
	public org.alice.ide.uricontent.UriProjectLoader getSelectedUri() {
		String path = this.pathState.getValue();
		java.io.File file = new java.io.File( path );
		if( file.exists() ) {
			return new org.alice.ide.uricontent.FileProjectLoader( file );
		} else {
			return null;
		}
	}

	@Override
	protected void refresh() {
	}

	@Override
	protected org.alice.ide.projecturi.views.FileSystemPane createView() {
		return new org.alice.ide.projecturi.views.FileSystemPane( this );
	}
}
