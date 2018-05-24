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

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.history.Step;
import org.lgna.croquet.history.Transaction;
import org.lgna.croquet.history.TransactionHistory;
import org.lgna.croquet.triggers.IterationTrigger;
import org.lgna.croquet.triggers.Trigger;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class IteratingOperation extends Operation {
	public IteratingOperation( Group group, UUID id ) {
		super( group, id );
	}

	protected TransactionHistory createTransactionHistoryIfNecessary() {
		return new TransactionHistory();
	}

	protected Iterator<Model> createIteratingData() {
		return null;
	}

	protected abstract boolean hasNext( CompletionStep<?> step, List<Step<?>> subSteps, Iterator<Model> iteratingData );

	protected abstract Model getNext( CompletionStep<?> step, List<Step<?>> subSteps, Iterator<Model> iteratingData );

	protected abstract void handleSuccessfulCompletionOfSubModels( CompletionStep<?> step, List<Step<?>> subSteps );

	protected void iterateOverSubModels( Transaction transaction, Trigger trigger ) {
		CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger, this.createTransactionHistoryIfNecessary() );
		try {
			List<Step<?>> subSteps = Lists.newLinkedList();
			Iterator<Model> iteratingData = this.createIteratingData();
			while( this.hasNext( step, subSteps, iteratingData ) ) {
				Model model = this.getNext( step, subSteps, iteratingData );
				if( model != null ) {
					Step<?> subStep = model.fire( IterationTrigger.createUserInstance() );
					if( ( subStep != null ) && subStep.getOwnerTransaction().isSuccessfullyCompleted() ) {
						subSteps.add( subStep );
					} else {
						if( subStep != null ) {
							if( subStep.getOwnerTransaction().isPending() ) {
								Logger.severe( "subStep is pending", this );
							} else {
								//pass
							}
						} else {
							Logger.severe( "subStep is null", this );
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
