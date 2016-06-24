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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class IteratingOperation extends Operation {
	public IteratingOperation( Group group, java.util.UUID id ) {
		super( group, id );
	}

	protected org.lgna.croquet.history.TransactionHistory createTransactionHistoryIfNecessary() {
		return new org.lgna.croquet.history.TransactionHistory();
	}

	protected Object createIteratingData() {
		return null;
	}

	protected abstract boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData );

	protected abstract Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData );

	protected abstract void handleSuccessfulCompletionOfSubModels( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps );

	protected void iterateOverSubModels( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger, this.createTransactionHistoryIfNecessary() );
		try {
			java.util.List<org.lgna.croquet.history.Step<?>> subSteps = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			Object iteratingData = this.createIteratingData();
			while( this.hasNext( step, subSteps, iteratingData ) ) {
				Model model = this.getNext( step, subSteps, iteratingData );
				if( model != null ) {
					org.lgna.croquet.history.Step<?> subStep = model.fire( org.lgna.croquet.triggers.IterationTrigger.createUserInstance() );
					if( ( subStep != null ) && subStep.getOwnerTransaction().isSuccessfullyCompleted() ) {
						subSteps.add( subStep );
					} else {
						if( subStep != null ) {
							if( subStep.getOwnerTransaction().isPending() ) {
								edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "subStep is pending", this );
							} else {
								//pass
							}
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "subStep is null", this );
						}
						step.cancel();
						return;
					}
				} else {
					step.cancel();
					return;
				}
			}
			this.handleSuccessfulCompletionOfSubModels( step, subSteps );
		} catch( CancelException ce ) {
			step.cancel();
		}
	}
}
