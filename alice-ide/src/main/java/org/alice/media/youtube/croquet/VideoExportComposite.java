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
package org.alice.media.youtube.croquet;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.ide.video.preview.VideoComposite;
import org.alice.media.youtube.croquet.views.ExportVideoView;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Operation;
import org.lgna.croquet.StringState;
import org.lgna.croquet.WizardPageComposite;

import edu.cmu.cs.dennisc.java.awt.FileDialogUtilities;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;

/**
 * @author Matt May
 */
public class VideoExportComposite extends WizardPageComposite<ExportVideoView, ExportToYouTubeWizardDialogComposite> {

	private final VideoComposite videoComposite = new VideoComposite();
	private final StringState previewState = this.createStringState( "preview", "Preview" );

	public VideoExportComposite( ExportToYouTubeWizardDialogComposite owner ) {
		super( UUID.fromString( "fbe4a55d-5076-42f7-9bcf-87560b74b204" ), owner );
		this.registerSubComposite( this.videoComposite );
	}

	private final ActionOperation exportToFileOperation = this.createActionOperation( "exportToFileOperation", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {

			File videosDirectory = StageIDE.getActiveInstance().getVideosDirectory();

			String filename = "*.webm";

			File exportFile = FileDialogUtilities.showSaveFileDialog(
					VideoExportComposite.this.getView().getAwtComponent(), "Save Video", videosDirectory, filename, FileUtilities.createFilenameFilter( "webm" ), "webm" );
			if( exportFile != null ) {
				try {
					FileUtilities.copyFile( getOwner().getTempRecordedVideoFile(), exportFile );
				} catch( IOException ioe ) {
					Dialogs.showWarning( "cannot export file: " + exportFile );
				}
			}
			return null;
		}
	} );

	public VideoComposite getVideoComposite() {
		return this.videoComposite;
	}

	public StringState getPreviewState() {
		return this.previewState;
	}

	public Operation getExportToFileOperation() {
		return this.exportToFileOperation;
	}

	@Override
	protected ExportVideoView createView() {
		return new ExportVideoView( this );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.videoComposite.getView().setUri( this.getOwner().getTempRecordedVideoFile().toURI() );
	}

	@Override
	public void handlePostDeactivation() {
		this.videoComposite.getView().setUri( null );
		super.handlePostDeactivation();
	}

	@Override
	public Status getPageStatus() {
		Status rv = IS_GOOD_TO_GO_STATUS;
		return rv;
	}

	@Override
	public void resetData() {
	}
}
