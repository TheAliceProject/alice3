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

/**
 * @author Dennis Cosgrove
 */
public class Transaction extends TransactionNode<TransactionHistory> {

	private static class DescendantStepIterator implements java.util.Iterator<Step<?>> {
		private final java.util.List<Transaction> transactions = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private int transactionIndex;
		private int stepIndex;

		public DescendantStepIterator( Transaction transaction, boolean isRecusionDesired ) {
			this.addTransactionAndTransactionDescendants( transaction, isRecusionDesired );
		}

		private void addTransactionAndTransactionDescendants( Transaction transaction, boolean isRecusionDesired ) {
			if( transaction.getChildStepCount() > 0 ) {
				this.transactions.add( transaction );
			}
			if( isRecusionDesired ) {
				CompletionStep<?> completionStep = transaction.getCompletionStep();
				if( completionStep != null ) {
					TransactionHistory transactionHistory = completionStep.getTransactionHistory();
					if( transactionHistory != null ) {
						for( Transaction child : transactionHistory ) {
							this.addTransactionAndTransactionDescendants( child, isRecusionDesired );
						}
					}
				}
			}
		}

		@Override
		public boolean hasNext() {
			return this.transactionIndex < this.transactions.size();
		}

		@Override
		public Step<?> next() {
			if( this.transactionIndex < this.transactions.size() ) {
				Step<?> rv;
				Transaction transaction = this.transactions.get( transactionIndex );
				rv = transaction.getChildStepAt( stepIndex );
				stepIndex++;
				if( stepIndex < transaction.getChildStepCount() ) {
					//pass
				} else {
					stepIndex = 0;
					transactionIndex++;
				}
				return rv;
			} else {
				throw new java.util.NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException( "remove" );
		}
	}

	private final java.util.List<PrepStep<?>> prepSteps;
	private CompletionStep<?> completionStep;

	public static Transaction createAndAddToHistory( TransactionHistory owner ) {
		Transaction rv = new Transaction( owner );
		owner.addTransaction( rv );
		return rv;
	}

	public Transaction( TransactionHistory parent ) {
		super( parent );
		this.prepSteps = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.completionStep = null;
	}

	@Override
	protected void appendContexts( java.util.List<org.lgna.croquet.Context> out ) {
		for( PrepStep<?> prepStep : this.prepSteps ) {
			prepStep.appendContexts( out );
		}
		if( this.completionStep != null ) {
			this.completionStep.appendContexts( out );
		}
	}

	public Iterable<org.lgna.croquet.Context> getAllContexts() {
		java.util.List<org.lgna.croquet.Context> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.appendContexts( rv );
		return rv;
	}

	public <C extends org.lgna.croquet.Context> C findFirstContext( Step<?> step, Class<C> cls ) {
		while( step != null ) {
			for( org.lgna.croquet.Context context : step.getContexts() ) {
				if( cls.isAssignableFrom( context.getClass() ) ) {
					return cls.cast( context );
				}
			}
			C context = step.findFirstContext( cls );
			if( context != null ) {
				return context;
			} else {
				step = step.getPreviousStep();
			}
		}
		CompletionStep<?> grandparent = this.getFirstAncestorAssignableTo( CompletionStep.class );
		if( grandparent != null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.info( "note: searching outside transaction", cls );
			return grandparent.findFirstContext( cls );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( cls );
			return null;
		}
	}

	public boolean containsPrepStep( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.PrepModel prepModel, Class<? extends org.lgna.croquet.history.PrepStep> cls ) {
		Iterable<org.lgna.croquet.history.PrepStep<?>> prepSteps = transaction.getPrepSteps();
		for( org.lgna.croquet.history.PrepStep<?> prepStep : prepSteps ) {
			if( ( cls == null ) || cls.isAssignableFrom( prepStep.getClass() ) ) {
				if( prepStep.getModel() == prepModel ) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isValid() {
		if( this.completionStep != null ) {
			return this.completionStep.isValid();
		} else {
			return false;
		}
	}

	public java.util.ListIterator<PrepStep<?>> prepStepListIterator() {
		return this.prepSteps.listIterator();
	}

	public java.util.Iterator<Step<?>> childStepIterator() {
		return new DescendantStepIterator( this, false );
	}

	public java.util.Iterator<Step<?>> descendantStepIterator() {
		return new DescendantStepIterator( this, true );
	}

	public Iterable<Step<?>> getChildSteps() {
		return new Iterable<Step<?>>() {
			@Override
			public java.util.Iterator<org.lgna.croquet.history.Step<?>> iterator() {
				return Transaction.this.childStepIterator();
			}
		};
	}

	public Iterable<Step<?>> getDescendantSteps() {
		return new Iterable<Step<?>>() {
			@Override
			public java.util.Iterator<org.lgna.croquet.history.Step<?>> iterator() {
				return Transaction.this.descendantStepIterator();
			}
		};
	}

	public void addMenuSelection( MenuSelection menuSelection ) {
		java.util.ListIterator<PrepStep<?>> iterator = this.prepSteps.listIterator( this.prepSteps.size() );
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

	public org.lgna.croquet.edits.Edit getEdit() {
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

	public void removeAllPrepSteps() {
		this.prepSteps.clear();
	}

	public org.lgna.croquet.history.PrepStep<?>[] getPrepStepsAsArray() {
		org.lgna.croquet.history.PrepStep<?>[] rv = new org.lgna.croquet.history.PrepStep[ this.prepSteps.size() ];
		return this.prepSteps.toArray( rv );
	}

	public void setPrepSteps( org.lgna.croquet.history.PrepStep<?>... prepSteps ) {
		edu.cmu.cs.dennisc.java.lang.ArrayUtilities.set( this.prepSteps, prepSteps );
	}

	public Iterable<PrepStep<?>> getPrepSteps() {
		return this.prepSteps;
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
		org.lgna.croquet.history.event.Event<?> e = new org.lgna.croquet.history.event.AddStepEvent( this, step );
		step.fireChanging( e );
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

	public <M extends org.lgna.croquet.CompletionModel> CompletionStep<M> createAndSetCompletionStep( M model, org.lgna.croquet.triggers.Trigger trigger, TransactionHistory subTransactionHistory ) {
		return CompletionStep.createAndAddToTransaction( this, model, trigger, subTransactionHistory );
	}

	public <M extends org.lgna.croquet.CompletionModel> CompletionStep<M> createAndSetCompletionStep( M model, org.lgna.croquet.triggers.Trigger trigger ) {
		return this.createAndSetCompletionStep( model, trigger, null );
	}

	/* package-private */void setCompletionStep( CompletionStep<?> step ) {
		//assert this.completionStep == null : this.completionStep + " " + step;
		this.addStep( step );
	}

	public boolean isPending() {
		if( this.completionStep != null ) {
			return this.completionStep.isPending();
		} else {
			return true;
		}
	}

	public boolean isSuccessfullyCompleted() {
		if( this.completionStep != null ) {
			return this.completionStep.isSuccessfullyCompleted();
		} else {
			return false;
		}
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
