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
public class TransactionHistory extends TransactionNode<CompletionStep<?>> implements Iterable<Transaction> {
	private final java.util.List<Transaction> transactions;

	public TransactionHistory() {
		super( (CompletionStep<?>)null );
		this.transactions = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	}

	public TransactionHistory getActiveTransactionHistory() {
		Transaction transaction = getLastTransaction();
		if( ( transaction != null ) && transaction.isPending() ) {
			CompletionStep<?> completionStep = transaction.getCompletionStep();
			if( completionStep == null ) {
				return this;
			}
			TransactionHistory history = completionStep.getTransactionHistory();
			if( history == null ) {
				return this;
			}
			return history.getActiveTransactionHistory();
		} else {
			return this;
		}
	}

	@Override
	protected void appendContexts( java.util.List<org.lgna.croquet.Context> out ) {
		for( Transaction transaction : this.transactions ) {
			transaction.appendContexts( out );
		}
	}

	public void addTransaction( Transaction transaction ) {
		assert transaction != null;
		org.lgna.croquet.history.event.Event<?> e = new org.lgna.croquet.history.event.AddTransactionEvent( transaction, this.transactions.size() );
		this.fireChanging( e );
		transaction.setOwner( this );
		this.transactions.add( transaction );
		this.fireChanged( e );
	}

	public void addTransaction( int index, Transaction transaction ) {
		assert transaction != null;
		assert index >= 0;
		org.lgna.croquet.history.event.Event<?> e = new org.lgna.croquet.history.event.AddTransactionEvent( transaction, index );
		this.fireChanging( e );
		transaction.setOwner( this );
		this.transactions.add( index, transaction );
		this.fireChanged( e );
	}

	@Override
	public java.util.Iterator<Transaction> iterator() {
		return this.transactions.iterator();
	}

	public java.util.ListIterator<Transaction> listIterator() {
		return this.transactions.listIterator();
	}

	public int getIndexOfTransaction( Transaction transaction ) {
		return this.transactions.indexOf( transaction );
	}

	public Transaction getTransactionAt( int i ) {
		return this.transactions.get( i );
	}

	public int getTransactionCount() {
		return this.transactions.size();
	}

	/* package-private */Transaction getLastTransaction() {
		final int n = this.transactions.size();
		if( n > 0 ) {
			return this.transactions.get( n - 1 );
		} else {
			return null;
		}
	}

	public Transaction acquireActiveTransaction() {
		Transaction lastTransaction = this.getLastTransaction();
		Transaction transaction = null;
		if( lastTransaction != null ) {
			if( lastTransaction.isPending() ) {
				transaction = lastTransaction;
			}
		}
		if( transaction == null ) {
			transaction = new Transaction( this );
			this.transactions.add( transaction );
		}
		return transaction;
	}
}
