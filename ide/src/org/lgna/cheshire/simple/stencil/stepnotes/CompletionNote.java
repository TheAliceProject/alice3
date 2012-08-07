/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.lgna.cheshire.simple.stencil.stepnotes;

/**
 * @author Dennis Cosgrove
 */
public abstract class CompletionNote<M extends org.lgna.croquet.CompletionModel> extends StepNote<org.lgna.croquet.history.CompletionStep<M> > {
	public CompletionNote( org.lgna.croquet.history.CompletionStep<M> step ) {
		super( step );
	}
	@Override
	public void setActive( boolean isActive ) {
		super.setActive( isActive );
		org.lgna.croquet.history.CompletionStep step = this.getStep();
		org.lgna.croquet.Model model = step.getModel();
		if( model instanceof org.lgna.croquet.OwnedByCompositeOperation ) {
			org.lgna.croquet.OwnedByCompositeOperation ownedByCompositeOperation = (org.lgna.croquet.OwnedByCompositeOperation)model;
			org.lgna.croquet.OperationOwningComposite operationOwningComposite = ownedByCompositeOperation.getComposite();
			if( operationOwningComposite instanceof org.alice.ide.ast.declaration.InsertForEachInArrayLoopComposite ) {
				final org.alice.ide.ast.declaration.InsertForEachInArrayLoopComposite forEachInArrayLoopComposite = (org.alice.ide.ast.declaration.InsertForEachInArrayLoopComposite)operationOwningComposite;
				
				org.lgna.croquet.edits.Edit edit = step.getEdit();
				if( edit instanceof org.alice.ide.croquet.edits.ast.InsertStatementEdit ) {
					org.alice.ide.croquet.edits.ast.InsertStatementEdit insertStatementEdit = (org.alice.ide.croquet.edits.ast.InsertStatementEdit)edit;
					org.lgna.project.ast.Statement statement = insertStatementEdit.getStatement();
					if( statement instanceof org.lgna.project.ast.ForEachInArrayLoop ) {
						final org.lgna.project.ast.ForEachInArrayLoop forEachInArrayLoop = (org.lgna.project.ast.ForEachInArrayLoop)statement;
						forEachInArrayLoopComposite.clearCommitRejectors();
						forEachInArrayLoopComposite.addCommitRejector( new org.lgna.croquet.CommitRejector() {
							public org.lgna.croquet.AbstractSeverityStatusComposite.Status getRejectionStatus(org.lgna.croquet.history.CompletionStep<?> step) {
								StringBuilder sb = new StringBuilder();
								org.lgna.project.ast.AbstractType<?,?,?> requiredType = forEachInArrayLoop.item.getValue().getValueType();
								org.lgna.project.ast.AbstractType<?,?,?> candidateType = forEachInArrayLoopComposite.getValueComponentType();
								//todo: should be able to compare types directly if retargeting correctly 
								if( candidateType != null && candidateType.getName().contentEquals( requiredType.getName() ) ) {
									//pass
								} else {
									sb.append( "item type must be " );
									sb.append( requiredType.getName() );
								}
								if( sb.length() > 0 ) {
									forEachInArrayLoopComposite.EPIC_HACK_externalErrorStatus.setText( sb.toString() );
									return forEachInArrayLoopComposite.EPIC_HACK_externalErrorStatus;
								} else {
									return org.lgna.croquet.AbstractSeverityStatusComposite.IS_GOOD_TO_GO_STATUS;
								}
							}
						} );
					}
				}
			}
		}
	}
	@Override
	public boolean isWhatWeveBeenWaitingFor( org.lgna.croquet.history.event.Event<?> event ) {
		org.lgna.croquet.history.CompletionStep<?> completionStep = this.getStep();
		org.lgna.croquet.history.TransactionHistory subTransactionHistory = completionStep.getTransactionHistory();
		if( subTransactionHistory != null ) {
			return super.isWhatWeveBeenWaitingFor( event );
		} else {
			if( event instanceof org.lgna.croquet.history.event.EditCommittedEvent ) {
				org.lgna.croquet.history.event.EditCommittedEvent editCommittedEvent = (org.lgna.croquet.history.event.EditCommittedEvent)event;
				org.lgna.croquet.Model candidateModel = editCommittedEvent.getNode().getModel();
				return this.isCorrectModel( candidateModel ) || this.isCorrectModelClass( candidateModel );
			} else if( event instanceof org.lgna.croquet.history.event.FinishedEvent ) {
				org.lgna.croquet.history.event.FinishedEvent finishedEvent = (org.lgna.croquet.history.event.FinishedEvent)event;
				org.lgna.croquet.Model candidateModel = finishedEvent.getNode().getModel();
				return this.isCorrectModel( candidateModel ) || this.isCorrectModelClass( candidateModel );
			} else {
				return false;
			}
		}
	}
}
