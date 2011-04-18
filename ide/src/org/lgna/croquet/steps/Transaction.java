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
package org.lgna.croquet.steps;

/**
 * @author Dennis Cosgrove
 */
public class Transaction implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	private static class DescendantStepIterator implements java.util.Iterator< Step<?> > {
		private final java.util.List< Transaction > transactions = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
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
				CompletionStep< ? > completionStep = transaction.getCompletionStep();
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
		public boolean hasNext() {
			return this.transactionIndex < this.transactions.size();
		}
		public Step< ? > next() {
			if( this.transactionIndex < this.transactions.size() ) {
				Step< ? > rv;
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
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	private TransactionHistory parent;
	private final java.util.List< PrepStep<?> > prepSteps;
	private CompletionStep<?> completionStep;
	/*package-private*/ Transaction( TransactionHistory parent ) {
		this.setParent( parent );
		this.prepSteps = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
		this.completionStep = null;
	}
	public Transaction( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.prepSteps = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList( (PrepStep<?>[])binaryDecoder.decodeBinaryEncodableAndDecodableArray( PrepStep.class ) );
		for( PrepStep< ? > prepStep : this.prepSteps ) {
			prepStep.setParent( this );
		}
		this.completionStep = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.completionStep.setParent( this );
	}

	public java.util.Iterator<Step<?>> childStepIterator() {
		return new DescendantStepIterator( this, false );
	}
	public java.util.Iterator<Step<?>> descendantStepIterator() {
		return new DescendantStepIterator( this, true );
	}
	public Iterable<Step<?>> getChildSteps() {
		return new Iterable< Step<?> >() {
			public java.util.Iterator< org.lgna.croquet.steps.Step< ? >> iterator() {
				return Transaction.this.childStepIterator();
			}
		};
	}
	public Iterable<Step<?>> getDescendantSteps() {
		return new Iterable< Step<?> >() {
			public java.util.Iterator< org.lgna.croquet.steps.Step< ? >> iterator() {
				return Transaction.this.descendantStepIterator();
			}
		};
	}
	
	
	public String getTitle() {
		edu.cmu.cs.dennisc.croquet.Edit< ? > edit = this.getEdit();
		if( edit != null ) {
			return edit.getPresentation( java.util.Locale.getDefault() );
		} else {
			return null;
		}
	}
	
	private class PendingDrop {
		private final edu.cmu.cs.dennisc.croquet.CompletionModel completionModel;
		private final edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor;
		private final edu.cmu.cs.dennisc.croquet.DropSite dropSite;
		public PendingDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
			this.completionModel = completionModel;
			this.dropReceptor = dropReceptor;
			this.dropSite = dropSite;
		}
		public void reifyPrepStep() {
			DropPrepStep.createAndAddToTransaction( Transaction.this, this.completionModel, this.dropReceptor, this.dropSite );
		}
		public void reifyCompletionStep() {
			DropCompletionStep.createAndAddToTransaction( Transaction.this, this.completionModel, this.dropReceptor, this.dropSite );
		}
	}
	private PendingDrop pendingDrop;
	public void pendDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		this.pendingDrop = new PendingDrop( completionModel, dropReceptor, dropSite );
	}
	
	public edu.cmu.cs.dennisc.croquet.Edit< ? > getEdit() {
		if( this.completionStep != null ) {
			return this.completionStep.getEdit();
		} else {
			return null;
		}
	}
	private void reifyDropCompletionStepIfNecessary() {
		if( this.completionStep != null ) {
			//pass
		} else {
			assert pendingDrop != null;
			pendingDrop.reifyCompletionStep();
			
		}
	}
	public void commit( edu.cmu.cs.dennisc.croquet.Edit edit ) {
		this.reifyDropCompletionStepIfNecessary();
		assert this.completionStep != null;
		this.completionStep.commit( edit );
	}
	public void finish() {
		this.reifyDropCompletionStepIfNecessary();
		assert this.completionStep != null;
		this.completionStep.finish();
	}
	public void cancel() {
		this.reifyDropCompletionStepIfNecessary();
		assert this.completionStep != null;
		this.completionStep.cancel();
	}
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		for( PrepStep< ? > prepStep : this.prepSteps ) {
			prepStep.retarget( retargeter );
		}
		this.completionStep.retarget( retargeter );
	}

	public TransactionHistory getParent() {
		return this.parent;
	}
	/*package-private*/ void setParent( TransactionHistory parent ) {
		this.parent = parent;
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		throw new AssertionError();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)this.prepSteps, PrepStep.class ) );
		binaryEncoder.encode( this.completionStep );
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
	
	public Iterable< PrepStep<?> > getPrepSteps() {
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
		if( pendingDrop != null ) {
			if( step instanceof PrepStep< ? > ) {
				if( step instanceof DropPrepStep ) {
					//pass
				} else {
					pendingDrop.reifyPrepStep();
				}
			} else if( step instanceof CompletionStep< ? > ) {
				if( step instanceof DropCompletionStep ) {
					//pass
				} else {
					if( step.getModel() == pendingDrop.completionModel ) {
						step = null;
					}
				}
			} else {
				assert false : step;
			}
		}
		if( step != null ) {
			TransactionManager.fireAddingStep( step );
			if( step instanceof PrepStep< ? > ) {
				this.prepSteps.add( (PrepStep< ? >)step );
			} else if( step instanceof CompletionStep< ? > ) {
				this.completionStep = (CompletionStep< ? >)step;
			} else {
				assert false : step;
			}
			TransactionManager.fireAddedStep( step );
		}
	}
	
	/*package-private*/ void addPrepStep( PrepStep< ? > step ) {
		this.addStep( step );
	}
//	public void removePrepStep( PrepStep< ? > step ) {
//		this.prepSteps.remove( step );
//	}
	public CompletionStep< ? > getCompletionStep() {
		return this.completionStep;
	}
	/*package-private*/ void setCompletionStep( CompletionStep<?> step ) {
		assert this.completionStep == null : this.completionStep + " " + step;
		this.addStep( step );
	}
	public boolean isActive() {
		if( this.completionStep != null ) {
			return this.completionStep.isActive();
		} else {
			return true;
		}
	}
}
