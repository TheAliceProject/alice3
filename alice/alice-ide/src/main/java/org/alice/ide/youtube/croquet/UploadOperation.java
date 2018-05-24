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
package org.alice.ide.youtube.croquet;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.ProjectStack;
import org.alice.media.youtube.croquet.ExportToYouTubeWizardDialogComposite;
import org.lgna.croquet.Model;
import org.lgna.croquet.OperationOwningComposite;
import org.lgna.croquet.OwnedByCompositeOperation;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Step;

import edu.wustl.lookingglass.media.FFmpegProcess;

/**
 * @author Matt May
 */
public class UploadOperation extends SingleThreadIteratingOperation {
	public UploadOperation( ProjectDocumentFrame projectDocumentFrame ) {
		super( IDE.EXPORT_GROUP, UUID.fromString( "9a855203-b1ce-4ba3-983d-b941a36b2c10" ) );
		this.projectDocumentFrame = projectDocumentFrame;
	}

	private synchronized ExportToYouTubeWizardDialogComposite getWizard() {
		if( this.exportToYouTubeWizardDialogComposite != null ) {
			//pass
		} else {
			this.exportToYouTubeWizardDialogComposite = new ExportToYouTubeWizardDialogComposite();
		}
		return this.exportToYouTubeWizardDialogComposite;
	}

	private File getFFmpegFileIfNotExecutable() {
		File fileKnownToBeNotExecuable;
		if( FFmpegProcess.isArchitectureSpecificCommandAbsolute() ) {
			String command = FFmpegProcess.getArchitectureSpecificCommand();
			File file = new File( command );
			if( file.exists() ) {
				fileKnownToBeNotExecuable = file.canExecute() ? null : file;
			} else {
				fileKnownToBeNotExecuable = null;
			}
		} else {
			fileKnownToBeNotExecuable = null;
		}
		return fileKnownToBeNotExecuable;
	}

	private Model getExecutionPermissionModel( File file ) {
		ExecutionPermissionFailedDialogComposite composite = new ExecutionPermissionFailedDialogComposite( file );
		return composite.getLaunchOperation();
	}

	private Model getWizardModel() {
		ExportToYouTubeWizardDialogComposite wizard = this.getWizard();
		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			ide.getUpToDateProgramType();
		}
		wizard.setProject( ProjectStack.peekProject() );
		return wizard.getLaunchOperation();
	}

	@Override
	protected boolean hasNext( CompletionStep<?> step, List<Step<?>> subSteps, Iterator<Model> iteratingData ) {
		int size = subSteps.size();
		if( size == 0 ) {
			return true;
		} else if( size == 1 ) {
			Step<?> prevStep = subSteps.get( 0 );
			Model prevModel = prevStep.getModel();
			if( prevModel instanceof OwnedByCompositeOperation ) {
				OwnedByCompositeOperation ownedByCompositeOperation = (OwnedByCompositeOperation)prevModel;
				OperationOwningComposite<?> composite = ownedByCompositeOperation.getComposite();
				if( composite instanceof ExecutionPermissionFailedDialogComposite ) {
					ExecutionPermissionFailedDialogComposite executionComposite = (ExecutionPermissionFailedDialogComposite)composite;
					return executionComposite.getIsFixed();
				}
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	protected Model getNext( CompletionStep<?> step, List<Step<?>> subSteps, Iterator<Model> iteratingData ) {
		File fileKnownToBeNotExecuable = getFFmpegFileIfNotExecutable();
		if( subSteps.size() == 0 ) {
			if( fileKnownToBeNotExecuable != null ) {
				return getExecutionPermissionModel( fileKnownToBeNotExecuable );
			} else {
				return getWizardModel();
			}
		} else {
			assert fileKnownToBeNotExecuable == null : "this should be null check logic in getNext()";
			return getWizardModel();
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( CompletionStep<?> step, List<Step<?>> subSteps ) {
	}

	private final ProjectDocumentFrame projectDocumentFrame;
	private ExportToYouTubeWizardDialogComposite exportToYouTubeWizardDialogComposite;
}
