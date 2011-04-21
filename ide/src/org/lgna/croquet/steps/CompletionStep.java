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
public abstract class CompletionStep< M extends edu.cmu.cs.dennisc.croquet.CompletionModel > extends Step< M > {
	private final TransactionHistory transactionHistory;
	private edu.cmu.cs.dennisc.croquet.Edit<M> edit;
	private boolean isSuccessfullyCompleted;
	private boolean isPending = true;
	public CompletionStep( Transaction parent, M model, TransactionHistory transactionHistory ) {
		super( parent, model );
		parent.setCompletionStep( this );
		this.transactionHistory = transactionHistory;
		if( this.transactionHistory != null ) {
			this.transactionHistory.setParent( this );
			TransactionManager.pushTransactionHistory( this.transactionHistory );
		}
	}
	public CompletionStep( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
		this.isPending = binaryDecoder.decodeBoolean();
		this.isSuccessfullyCompleted = binaryDecoder.decodeBoolean();
		edu.cmu.cs.dennisc.croquet.Edit.Memento< M > memento = binaryDecoder.decodeBinaryEncodableAndDecodable();
		if( memento != null ) {
			this.edit = memento.createEdit();
			this.edit.setCompletionStep( this );
		} else {
			this.edit = null;
		}
		this.transactionHistory = binaryDecoder.decodeBinaryEncodableAndDecodable();
		if( this.transactionHistory != null ) {
			this.transactionHistory.setParent( this );
		}
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.isPending );
		binaryEncoder.encode( this.isSuccessfullyCompleted );
		edu.cmu.cs.dennisc.croquet.Edit.Memento<M> memento;
		if( this.edit != null ) {
			memento = this.edit.createMemento();
		} else {
			memento = null;
		}
		binaryEncoder.encode( memento );
		binaryEncoder.encode( this.transactionHistory );
	}
	
	@Override
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		super.retarget( retargeter );
		if( this.transactionHistory != null ) {
			this.transactionHistory.retarget( retargeter );
		}
		if( this.edit != null ) {
			this.edit.retarget( retargeter );
		}
	}

	public TransactionHistory getTransactionHistory() {
		return this.transactionHistory;
	}
	/*package-private*/ void popTransactionHistoryIfNecessary() {
		if( this.transactionHistory != null ) {
			TransactionHistory pop = TransactionManager.popTransactionHistory();
			assert pop == this.transactionHistory;
		}
	}
	public boolean isPending() {
		return this.isPending;
	}
	public boolean isSuccessfullyCompleted() {
		return this.isSuccessfullyCompleted;
	}
	public edu.cmu.cs.dennisc.croquet.Edit< ? > getEdit() {
		return this.edit;
	}

	public void commit( edu.cmu.cs.dennisc.croquet.Edit<M> edit ) {
		this.isSuccessfullyCompleted = true;
		this.edit = edit;
		this.edit.setCompletionStep( this );
		this.isPending = false;
	}
	public void finish() {
		this.isSuccessfullyCompleted = true;
		this.edit = null;
		this.isPending = false;
	}
	public void cancel() {
		this.isSuccessfullyCompleted = false;
		this.edit = null;
		this.isPending = false;
	}

	public String getTutorialTransactionTitle( edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		return this.getModel().getTutorialTransactionTitle( this.edit, userInformation );
	}
	@Override
	protected StringBuilder updateRepr( StringBuilder rv ) {
		rv = super.updateRepr( rv );
		rv.append( ";edit=" );
		rv.append( this.edit );
//		rv.append( ";isActive=" );
//		rv.append( this.isActive );
		return rv;
	}
}
