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
package org.lgna.croquet.history;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.pattern.Criterion;
import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.event.AddStepEvent;
import org.lgna.croquet.history.event.TransactionEvent;
import org.lgna.croquet.triggers.Trigger;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Dennis Cosgrove
 */
public class Transaction extends TransactionNode<TransactionHistory> {

	private final List<PrepStep<?>> prepSteps;
	private CompletionStep<?> completionStep;

	public Transaction( TransactionHistory parent ) {
		super( parent );
		this.prepSteps = Lists.newLinkedList();
		this.completionStep = null;
	}

	public boolean isValid() {
		if( this.completionStep != null ) {
			return this.completionStep.isValid();
		} else {
			return false;
		}
	}

	Step<?> findAcceptableStep( Criterion<Step<?>> criterion ) {
		final int N = getPrepStepCount();
		for( int i = 0; i < N; i++ ) {
			PrepStep<?> prepStep = getPrepStepAt( N - 1 - i );
			if( criterion.accept( prepStep ) ) {
				return prepStep;
			}
		}
		return getOwner().findAcceptableStep(criterion);
	}

	public void addMenuSelection( MenuSelection menuSelection ) {
		ListIterator<PrepStep<?>> iterator = this.prepSteps.listIterator( this.prepSteps.size() );
		while( iterator.hasPrevious() ) {
			PrepStep<?> prepStep = iterator.previous();
			if( prepStep instanceof MenuItemSelectStep ) {
				MenuItemSelectStep prevMenuItemSelectStep = (MenuItemSelectStep)prepStep;
				if( menuSelection.isPrevious( prevMenuItemSelectStep.getMenuBarComposite(), prevMenuItemSelectStep.getMenuItemPrepModels() ) ) {
					break;
				} else {
					iterator.remove();
				}
			} else {
				break;
			}
		}
		MenuItemSelectStep.createAndAddToTransaction( this, menuSelection.getMenuBarComposite(), menuSelection.getMenuItemPrepModels(), menuSelection.getTrigger() );
	}

	public Edit getEdit() {
		if( this.completionStep != null ) {
			return this.completionStep.getEdit();
		} else {
			return null;
		}
	}

	public TransactionHistory getOwnerTransactionHistory() {
		return this.getOwner();
	}

	public int getChildStepCount() {
		return this.getPrepStepCount() + ( this.completionStep != null ? 1 : 0 );
	}

	public Step<?> getChildStepAt( int index ) {
		if( index == this.getPrepStepCount() ) {
			return this.getCompletionStep();
		} else {
			return this.getPrepStepAt( index );
		}
	}

	public int getIndexOfChildStep( Step<?> step ) {
		if( step instanceof PrepStep<?> ) {
			PrepStep<?> prepStep = (PrepStep<?>)step;
			return this.getIndexOfPrepStep( prepStep );
		} else {
			if( step == this.completionStep ) {
				return this.prepSteps.size();
			} else {
				return -1;
			}
		}
	}

	public int getIndexOfPrepStep( PrepStep<?> prepStep ) {
		return this.prepSteps.indexOf( prepStep );
	}

	public PrepStep<?> getPrepStepAt( int i ) {
		return this.prepSteps.get( i );
	}

	public int getPrepStepCount() {
		return this.prepSteps.size();
	}

	private void addStep( Step<?> step ) {
		assert step != null;
		TransactionEvent e = new AddStepEvent( step );
		if( step instanceof PrepStep<?> ) {
			this.prepSteps.add( (PrepStep<?>)step );
		} else if( step instanceof CompletionStep<?> ) {
			this.completionStep = (CompletionStep<?>)step;
		} else {
			assert false : step;
		}
		step.fireChanged( e );
	}

	/* package-private */void addPrepStep( PrepStep<?> step ) {
		this.addStep( step );
	}

	public CompletionStep<?> getCompletionStep() {
		return this.completionStep;
	}

	public <M extends CompletionModel> CompletionStep<M> createAndSetCompletionStep( M model, Trigger trigger, TransactionHistory subTransactionHistory ) {
		return CompletionStep.createAndAddToTransaction( this, model, trigger, subTransactionHistory );
	}

	public <M extends CompletionModel> CompletionStep<M> createAndSetCompletionStep( M model, Trigger trigger ) {
		return this.createAndSetCompletionStep( model, trigger, null );
	}

	/* package-private */void setCompletionStep( CompletionStep<?> step ) {
		//assert this.completionStep == null : this.completionStep + " " + step;
		this.addStep( step );
	}

	public boolean isPending() {
		return completionStep == null || completionStep.isPending();
	}

	public boolean isSuccessfullyCompleted() {
		return completionStep != null && completionStep.isSuccessfullyCompleted();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		for( PrepStep prepStep : this.prepSteps ) {
			sb.append( prepStep );
			sb.append( "," );
		}
		sb.append( this.completionStep );
		sb.append( "]" );
		return sb.toString();
	}
}
