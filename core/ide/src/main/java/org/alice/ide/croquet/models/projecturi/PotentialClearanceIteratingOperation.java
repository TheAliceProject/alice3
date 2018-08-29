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
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelDialog;
import edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult;
import org.alice.ide.ProjectApplication;
import org.alice.ide.ProjectDocumentFrame;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Group;
import org.lgna.croquet.Model;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.history.UserActivity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class PotentialClearanceIteratingOperation extends SingleThreadIteratingOperation {
	public PotentialClearanceIteratingOperation( Group group, UUID migrationId, ProjectDocumentFrame projectDocumentFrame, Model postClearanceModel ) {
		super( group, migrationId );
		this.projectDocumentFrame = projectDocumentFrame;
		this.postClearanceModel = postClearanceModel;
	}

	protected Model getPostClearanceModel() {
		return this.postClearanceModel;
	}

	@Override
	protected Iterator<Model> createIteratingData() {
		List<Model> models = Lists.newLinkedList();
		ProjectApplication application = ProjectApplication.getActiveInstance();
		boolean isPostClearanceModelDesired = this.postClearanceModel != null;
		if (!application.isProjectUpToDateWithFile()) {
			YesNoCancelResult result = new YesNoCancelDialog.Builder( findLocalizedText("message") )
					.title( findLocalizedText("title") )
					.buildAndShow();
			if( result == YesNoCancelResult.CANCEL ) {
				throw new CancelException();
			}
			if( result == YesNoCancelResult.YES ) {
				models.add( SaveProjectOperation.getInstance() );
			}
		}
		if( isPostClearanceModelDesired ) {
			models.add( this.postClearanceModel );
		}
		return models.iterator();
	}

	@Override
	protected boolean hasNext( List<UserActivity> subSteps, Iterator<Model> iterator ) {
		return iterator.hasNext();
	}

	@Override
	protected Model getNext( List<UserActivity> subSteps, Iterator<Model> iterator ) {
		return iterator.next();
	}

	private final ProjectDocumentFrame projectDocumentFrame;
	private final Model postClearanceModel;
}
