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
package org.alice.ide.croquet.models.projecturi;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult;
import org.alice.ide.ProjectApplication;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.Operation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.UserActivity;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class PotentialClearanceIteratingOperation extends Operation {
	public PotentialClearanceIteratingOperation( Group group, UUID migrationId, Triggerable postClearanceModel ) {
		super( group, migrationId );
		this.postClearanceModel = postClearanceModel;
	}

	protected List<Triggerable> createIteratingData() {
		List<Triggerable> steps = Lists.newLinkedList();
		ProjectApplication application = ProjectApplication.getActiveInstance();
		boolean isPostClearanceModelDesired = this.postClearanceModel != null;
		if (!application.isProjectUpToDateWithFile()) {
			YesNoCancelResult result =
				Dialogs.confirmOrCancel( findLocalizedText( "title"), findLocalizedText( "message") );
			if( result == YesNoCancelResult.CANCEL ) {
				throw new CancelException();
			}
			if( result == YesNoCancelResult.YES ) {
				steps.add( SaveProjectOperation.getInstance() );
			}
		}
		if( isPostClearanceModelDesired ) {
			steps.add( this.postClearanceModel );
		}
		return steps;
	}

	@Override
	protected final void performInActivity( final UserActivity userActivity ) {
		userActivity.setCompletionModel( this );
		try {
			List<UserActivity> finishedActivities = Lists.newLinkedList();
			for ( Triggerable step : createIteratingData() ) {
				UserActivity child = userActivity.newChildActivity();
				step.fire( child );
				if ( child.isSuccessfullyCompleted() ) {
					finishedActivities.add( child );
				} else {
					if (child.isPending()) {
						Logger.severe( "Canceling while a subStep is pending. The substep should either finish or throw CancelException.", this );
					}
					throw new CancelException();
				}
			}
			handleSuccessfulCompletionOfSubModels( userActivity, finishedActivities );
		} catch( CancelException ce ) {
			userActivity.cancel(ce);
		}
	}

	protected void handleSuccessfulCompletionOfSubModels( UserActivity activity, List<UserActivity> subSteps ){
		activity.finish();
	}

	private final Triggerable postClearanceModel;
}
