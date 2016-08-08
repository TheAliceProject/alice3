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

package org.alice.ide.croquet.models.ui.debug.components;

/**
 * @author Dennis Cosgrove
 */
public class TransactionHistoryTreeModel extends edu.cmu.cs.dennisc.javax.swing.models.AbstractMutableTreeModel<Object> {

	private org.lgna.croquet.history.TransactionHistory root;

	public TransactionHistoryTreeModel( org.lgna.croquet.history.TransactionHistory root ) {
		this.root = root;
	}

	@Override
	public org.lgna.croquet.history.TransactionHistory getRoot() {
		return this.root;
	}

	@Override
	public boolean isLeaf( Object node ) {
		return node instanceof org.lgna.croquet.history.PrepStep<?>;
	}

	@Override
	public int getChildCount( Object parent ) {
		if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
			org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
			return transactionHistory.getTransactionCount();
		} else if( parent instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)parent;
			return transaction.getChildStepCount();
		} else if( parent instanceof org.lgna.croquet.history.CompletionStep<?> ) {
			org.lgna.croquet.history.CompletionStep<?> completionStep = (org.lgna.croquet.history.CompletionStep<?>)parent;
			//return completionStep.getTransactionHistory() != null ? 1 : 0;
			org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
			if( transactionHistory != null ) {
				return transactionHistory.getTransactionCount();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	@Override
	public Object getChild( Object parent, int index ) {
		if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
			org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
			return transactionHistory.getTransactionAt( index );
		} else if( parent instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)parent;
			return transaction.getChildStepAt( index );
		} else if( parent instanceof org.lgna.croquet.history.CompletionStep<?> ) {
			org.lgna.croquet.history.CompletionStep<?> completionStep = (org.lgna.croquet.history.CompletionStep<?>)parent;
			org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
			if( transactionHistory != null ) {
				return transactionHistory.getTransactionAt( index );
			} else {
				return null;
			}
			//			assert index == 0;
			//			return completionStep.getTransactionHistory();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	public int getIndexOfChild( Object parent, Object child ) {
		if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
			org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
			if( child instanceof org.lgna.croquet.history.Transaction ) {
				return transactionHistory.getIndexOfTransaction( (org.lgna.croquet.history.Transaction)child );
			} else {
				return -1;
			}
		} else if( parent instanceof org.lgna.croquet.history.Transaction ) {
			org.lgna.croquet.history.Transaction transaction = (org.lgna.croquet.history.Transaction)parent;
			if( child instanceof org.lgna.croquet.history.PrepStep<?> ) {
				return transaction.getIndexOfPrepStep( (org.lgna.croquet.history.PrepStep<?>)child );
			} else {
				return -1;
			}
		} else if( parent instanceof org.lgna.croquet.history.CompletionStep<?> ) {
			org.lgna.croquet.history.CompletionStep<?> completionStep = (org.lgna.croquet.history.CompletionStep<?>)parent;
			org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
			return transactionHistory.getIndexOfTransaction( (org.lgna.croquet.history.Transaction)child );
			//			assert child == completionStep.getTransactionHistory();
			//			return 0;
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	private java.util.List<Object> updatePath( java.util.List<Object> rv, Object node ) {
		Object parent;
		if( node instanceof org.lgna.croquet.history.Transaction ) {
			parent = ( (org.lgna.croquet.history.Transaction)node ).getOwner();
			if( parent instanceof org.lgna.croquet.history.TransactionHistory ) {
				org.lgna.croquet.history.TransactionHistory transactionHistory = (org.lgna.croquet.history.TransactionHistory)parent;
				if( transactionHistory.getOwner() != null ) {
					parent = transactionHistory;
				}
			}
		} else if( node instanceof org.lgna.croquet.history.Step ) {
			parent = ( (org.lgna.croquet.history.Step)node ).getOwner();
		} else {
			parent = null;
		}
		if( parent != null ) {
			updatePath( rv, parent );
		}
		rv.add( node );
		return rv;
	}

	@Override
	public javax.swing.tree.TreePath getTreePath( Object node ) {
		java.util.List<Object> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		updatePath( list, node );
		return new javax.swing.tree.TreePath( list.toArray() );
	}
}
