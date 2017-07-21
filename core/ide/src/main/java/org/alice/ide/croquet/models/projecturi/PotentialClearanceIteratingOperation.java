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

/**
 * @author Dennis Cosgrove
 */
public abstract class PotentialClearanceIteratingOperation extends org.lgna.croquet.SingleThreadIteratingOperation {
	public PotentialClearanceIteratingOperation( org.lgna.croquet.Group group, java.util.UUID migrationId, org.alice.ide.ProjectDocumentFrame projectDocumentFrame, org.lgna.croquet.Model postClearanceModel ) {
		super( group, migrationId );
		this.projectDocumentFrame = projectDocumentFrame;
		this.postClearanceModel = postClearanceModel;
	}

	protected org.lgna.croquet.Model getPostClearanceModel() {
		return this.postClearanceModel;
	}

	@Override
	protected java.util.Iterator<org.lgna.croquet.Model> createIteratingData() {
		java.util.List<org.lgna.croquet.Model> models = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		org.alice.ide.ProjectApplication application = org.alice.ide.ProjectApplication.getActiveInstance();
		boolean isPostClearanceModelDesired = this.postClearanceModel != null;
		if( application.isProjectUpToDateWithFile() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult result = new edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelDialog.Builder( "Your program has been modified.  Would you like to save it?" )
					.title( "Save changed project?" )
					.buildAndShow();
			if( result == edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult.YES ) {
				models.add( SaveProjectOperation.getInstance() );
			} else if( result == edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult.NO ) {
				//pass
			} else {
				isPostClearanceModelDesired = false;
			}
		}
		if( isPostClearanceModelDesired ) {
			models.add( this.postClearanceModel );
		}
		return models.iterator();
	}

	@Override
	protected boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		java.util.Iterator<org.lgna.croquet.Model> iterator = (java.util.Iterator<org.lgna.croquet.Model>)iteratingData;
		return iterator.hasNext();
	}

	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		java.util.Iterator<org.lgna.croquet.Model> iterator = (java.util.Iterator<org.lgna.croquet.Model>)iteratingData;
		return iterator.next();
	}

	private final org.alice.ide.ProjectDocumentFrame projectDocumentFrame;
	private final org.lgna.croquet.Model postClearanceModel;
}
