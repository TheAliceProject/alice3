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

import edu.cmu.cs.dennisc.croquet.Retargeter;

/**
 * @author Dennis Cosgrove
 */
public class TransactionHistory implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable, Iterable< Transaction > {
	private final java.util.List< Transaction > transactions;
	public TransactionHistory() {
		this.transactions = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	}
	public TransactionHistory( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.transactions = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList( binaryDecoder.decodeBinaryEncodableAndDecodableArray( Transaction.class ) );
		for( Transaction transaction : this.transactions ) {
			transaction.setParent( this );
		}
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		throw new AssertionError();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( this.transactions, Transaction.class ) );
	}
	
	@Deprecated
	public void EPIC_HACK_clear() {
		this.transactions.clear();
	}
	
	
	public void retarget( Retargeter retargeter ) {
		for( Transaction transaction : this.transactions ) {
			transaction.retarget( retargeter );
		}
	}

	public void addTransaction( Transaction transaction ) {
		this.transactions.add( transaction );
	}
	public void addTransaction( int index, Transaction transaction ) {
		this.transactions.add( index, transaction );
	}
	public void removeTransaction( Transaction transaction ) {
		this.transactions.add( transaction );
	}

	public java.util.Iterator< Transaction > iterator() {
		return this.transactions.iterator();
	}
	public java.util.ListIterator< Transaction > listIterator() {
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
	
	/*package-private*/ Transaction getLastTransaction() {
		final int N = this.transactions.size();
		if( N > 0 ) {
			return this.transactions.get( N-1 );
		} else {
			return null;
		}
	}
	public Transaction getActiveTransaction() {
		Transaction lastTransaction = this.getLastTransaction();
		Transaction rv;
		if( lastTransaction != null && lastTransaction.isActive() ) {
			rv = lastTransaction;
		} else {
			rv = new Transaction( this );
			this.transactions.add( rv );
		}
		return rv;
	}
}
