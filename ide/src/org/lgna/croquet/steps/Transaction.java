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
	public Transaction( TransactionHistory parent ) {
		this.setParent( parent );
		this.prepSteps = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.completionStep = null;
	}
	public Transaction( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.prepSteps = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( (PrepStep<?>[])binaryDecoder.decodeBinaryEncodableAndDecodableArray( PrepStep.class ) );
		for( PrepStep< ? > prepStep : this.prepSteps ) {
			prepStep.setParent( this );
		}
		this.completionStep = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.completionStep.setParent( this );
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( (java.util.List)this.prepSteps, PrepStep.class ) );
		binaryEncoder.encode( this.completionStep );
	}
	
	public boolean isValid() {
		if( this.completionStep != null ) {
			return this.completionStep.isValid();
		} else {
			return false;
		}
	}
	
	public java.util.ListIterator< PrepStep< ? > > prepStepListIterator() {
		return this.prepSteps.listIterator();
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

	public String getTitle( edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		if( this.completionStep != null ) {
			return this.completionStep.getTutorialTransactionTitle( userInformation );
		} else {
			return null;
		}
	}
	
	private class PendingSteps {
		private edu.cmu.cs.dennisc.croquet.CompletionModel dropCompletionModel;
		private edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor;
		private edu.cmu.cs.dennisc.croquet.DropSite dropSite;
		private java.util.List< edu.cmu.cs.dennisc.croquet.MenuItemPrepModel > lastMenuSelection;
		private javax.swing.event.ChangeEvent lastChangeEvent;
		public void pendDrop( edu.cmu.cs.dennisc.croquet.CompletionModel dropCompletionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
			this.dropCompletionModel = dropCompletionModel;
			this.dropReceptor = dropReceptor;
			this.dropSite = dropSite;
		}
		public void pendMenuSelection( javax.swing.event.ChangeEvent changeEvent, java.util.List< edu.cmu.cs.dennisc.croquet.MenuItemPrepModel > models ) {
			this.lastChangeEvent = changeEvent;
			this.lastMenuSelection = models;
		}
//		private boolean isDropPrepStepAlreadyAdded() {
//			for( PrepStep< ? > prepStep : Transaction.this.getPrepSteps() ) {
//				if( prepStep instanceof DropPrepStep ) {
//					return true;
//				}
//			}
//			return false;
//		}
		private boolean isReifying;
		public Step<?> reify( Step<?> step, boolean isLastPrep ) {
			Step<?> rv = step;
			if( this.isReifying ) {
				//pass
			} else {
				this.isReifying = true;
				org.lgna.croquet.Trigger trigger = new org.lgna.croquet.triggers.ChangeEventTrigger( this.lastChangeEvent );
				try {
					boolean isDropPrep;
					if( this.lastMenuSelection != null && this.lastMenuSelection.size() > 0 ) {
						isDropPrep = true;
					} else {
						isDropPrep = isLastPrep;
					}
					if( this.dropCompletionModel != null ) {
						if( isDropPrep ) {
							DropPrepStep.createAndAddToTransaction( Transaction.this, this.dropCompletionModel, trigger, this.dropReceptor, this.dropSite );
						} else {
							DropCompletionStep.createAndAddToTransaction( Transaction.this, this.dropCompletionModel, trigger, this.dropReceptor, this.dropSite );
							rv = null;
						}
					}
					if( this.lastMenuSelection != null && this.lastMenuSelection.size() > 0 ) {
						final int N = this.lastMenuSelection.size();
						for( int i=0; i<N; i++ ) {
							edu.cmu.cs.dennisc.croquet.Model model = this.lastMenuSelection.get( i );
							
//							if( model instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
//								MenuModelStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.MenuModel)model );
							if( model instanceof edu.cmu.cs.dennisc.croquet.OperationMenuItemPrepModel ) {
								OperationMenuItemPrepStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.OperationMenuItemPrepModel)model, trigger );
							} else if( model instanceof edu.cmu.cs.dennisc.croquet.BooleanStateMenuItemPrepModel ) {
								BooleanStateMenuItemPrepStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.BooleanStateMenuItemPrepModel)model, trigger );
							} else if( model instanceof edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? > ) {
								edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? > fillIn = (edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? >)model;
//								if( fillIn instanceof edu.cmu.cs.dennisc.croquet.CascadeInputDialogOperationFillIn ) {
//									isLastPrep = false;
//								}
//								if( i < N-1 || isLastPrep ) {
//									CascadeFillInPrepStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? >)model );
//								} else {
//									CascadeFillInCompletionStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.CascadePopupOperation)this.dropCompletionModel, fillIn );
//									rv = null;
//								}

//								if( fillIn instanceof edu.cmu.cs.dennisc.croquet.CascadeInputDialogOperationFillIn ) {
//									edu.cmu.cs.dennisc.croquet.CascadeInputDialogOperationFillIn cascadeInputDialogOperationFillIn = (edu.cmu.cs.dennisc.croquet.CascadeInputDialogOperationFillIn)fillIn;
//									rv = InputDialogOperationStep.createAndAddToTransaction( Transaction.this, cascadeInputDialogOperationFillIn.getInputDialogOperation() );
//								} else {
									if( i < N-1 || isLastPrep ) {
										CascadeFillInPrepStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? >)model, trigger );
									} else {
										CascadeFillInCompletionStep.createAndAddToTransaction( Transaction.this, (edu.cmu.cs.dennisc.croquet.CascadePopupOperation)this.dropCompletionModel, trigger, fillIn );
										rv = null;
									}
//								}
							} else if( model instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
								//pass
							} else if( model instanceof edu.cmu.cs.dennisc.croquet.MenuBarComposite ) {
								//pass
							} else if( model instanceof edu.cmu.cs.dennisc.croquet.CompletionModel ) {
								//pass
							} else {
								assert false : model;
							}
						}
					}
					this.dropCompletionModel = null;
					this.dropReceptor = null;
					this.dropSite = null;
					this.lastChangeEvent = null;
					this.lastMenuSelection = null;
				} finally {
					this.isReifying = false;
				}
			}
			return rv;
		}
	}
//	
//	/*package-private*/ <F> edu.cmu.cs.dennisc.croquet.CascadePopupOperation< F > getPendingCascadePopupOperation() {
//		return (edu.cmu.cs.dennisc.croquet.CascadePopupOperation< F >)this.pendingDrop.dropCompletionModel;
//	}
	private PendingSteps pendingSteps = new PendingSteps();
	/*package-private*/ void pendDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		this.pendingSteps.pendDrop( completionModel, dropReceptor, dropSite );
	}

	
	/*package-private*/ void pendMenuSelection( javax.swing.event.ChangeEvent changeEvent, java.util.List< edu.cmu.cs.dennisc.croquet.MenuItemPrepModel > models ) {
		this.pendingSteps.pendMenuSelection( changeEvent, models );
	}
	
	public edu.cmu.cs.dennisc.croquet.Edit< ? > getEdit() {
		if( this.completionStep != null ) {
			return this.completionStep.getEdit();
		} else {
			return null;
		}
	}

	/*package-private*/ void reify() {
		this.pendingSteps.reify( null, false );
	}
//	public void commit( edu.cmu.cs.dennisc.croquet.Edit edit ) {
//		this.pendingSteps.reify( null, false );
//		assert this.completionStep != null;
//		this.completionStep.commit( edit );
//	}
//	public void finish() {
//		this.pendingSteps.reify( null, false );
//		assert this.completionStep != null;
//		this.completionStep.finish();
//	}
//	public void cancel( org.lgna.croquet.Trigger trigger ) {
//		this.pendingSteps.reify( null, false );
//		if( this.completionStep != null ) {
//			this.completionStep.cancel();
//			TransactionManager.fireTransactionCanceled( this );
//		} else {
//			CancelCompletionStep.createAndAddToTransaction( this, null, trigger );
//		}
//	}
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
		if( step instanceof PrepStep< ? > ) {
			PrepStep< ? > prepStep = (PrepStep< ? >)step;
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
	public org.lgna.croquet.steps.PrepStep< ? >[] getPrepStepsAsArray() {
		org.lgna.croquet.steps.PrepStep<?>[] rv = new org.lgna.croquet.steps.PrepStep[ this.prepSteps.size() ];
		return this.prepSteps.toArray( rv );
	}
	public void setPrepSteps( org.lgna.croquet.steps.PrepStep< ? >... prepSteps ) {
		edu.cmu.cs.dennisc.java.util.CollectionUtilities.set( this.prepSteps, prepSteps );
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
		if( step instanceof CascadePopupOperationStep< ? > ) {
			step = null;
		} else {
			step = this.pendingSteps.reify( step, true );
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
}
