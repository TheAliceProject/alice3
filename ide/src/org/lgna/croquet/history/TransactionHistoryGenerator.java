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

package org.lgna.croquet.history;

/**
 * @author Dennis Cosgrove
 */
public class TransactionHistoryGenerator {
	private final java.util.Stack<TransactionHistory> stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	public TransactionHistoryGenerator() {
		this.stack.push( new TransactionHistory() );
	}
	
	public TransactionHistory peekTransactionHistory() {
		return this.stack.peek();
	}
	public void pushTransactionHistory( TransactionHistory transactionHistory ) {
		this.stack.push( transactionHistory );
	}
	public TransactionHistory popTransactionHistory( TransactionHistory expectedTransactionHistory ) {
		TransactionHistory rv = this.stack.pop();
		assert rv == expectedTransactionHistory : rv + " " + expectedTransactionHistory;
		return rv;
	}
//
//	private <T> void createAndAddOperationTransaction( org.lgna.croquet.Operation operation, org.lgna.croquet.edits.Edit<org.lgna.croquet.Operation> edit, TransactionHistory subTransactionHistory ) {
//		TransactionHistory transactionHistory = this.stack.peek();
//		Transaction transaction = Transaction.createAndAddToHistory( transactionHistory );
//		org.lgna.croquet.triggers.Trigger trigger = org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance();
//		CompletionStep<org.lgna.croquet.Operation> step = CompletionStep.createAndAddToTransaction( transaction, operation, trigger, subTransactionHistory );
//		step.setEdit( edit );
//
//		if( subTransactionHistory != null ) {
//			this.stack.push( subTransactionHistory );
//			try {
//				
//			} finally {
//				TransactionHistory poppedTransactionHistory = this.stack.pop();
//				assert poppedTransactionHistory == subTransactionHistory;
//			}
//		}
//	}
//
//	public <T> void createAndAddOperationTransaction( org.lgna.croquet.Operation operation, org.lgna.croquet.edits.Edit<org.lgna.croquet.Operation> edit ) {
//		this.createAndAddOperationTransaction( operation, edit, null );
//	}
//	
//	public <T> void createAndAddOperationTransactionWithSubTransactionHistory( org.lgna.croquet.Operation operation, org.lgna.croquet.edits.Edit<org.lgna.croquet.Operation> edit ) {
//		this.createAndAddOperationTransaction( operation, edit, new TransactionHistory() );
//	}
//
//	public <T> void createAndAddStateTransaction( org.lgna.croquet.State<T> state, T prevValue, T nextValue ) {
//		TransactionHistory transactionHistory = this.stack.peek();
//		Transaction transaction = Transaction.createAndAddToHistory( transactionHistory );
//		org.lgna.croquet.triggers.Trigger trigger = org.lgna.croquet.triggers.ChangeEventTrigger.createGeneratorInstance();
//		StateChangeStep<T> step = StateChangeStep.createAndAddToTransaction( transaction, state, trigger );
//		org.lgna.croquet.edits.StateEdit<T> edit = new org.lgna.croquet.edits.StateEdit<T>( step, prevValue, nextValue );
//		step.setEdit( edit );
//	}
//	public void createAndAddOwnedByCompositeOperationTransaction( org.lgna.croquet.OwnedByCompositeOperation operation, org.lgna.croquet.edits.Edit edit ) {
//		TransactionHistory transactionHistory = this.stack.peek();
//		Transaction transaction = Transaction.createAndAddToHistory( transactionHistory );
//		org.lgna.croquet.triggers.Trigger trigger = org.lgna.croquet.triggers.ActionEventTrigger.createGeneratorInstance();
//		
//		TransactionHistory subTransactionHistory = new TransactionHistory();
//		CompletionStep step = CompletionStep.createAndAddToTransaction( transaction, operation, trigger, subTransactionHistory );
//		
//		this.stack.push( subTransactionHistory );
//		try {
//			operation.getComposite().addGeneratedTransactions( this, edit );
//		} finally {
//			TransactionHistory poppedTransactionHistory = this.stack.pop();
//			assert poppedTransactionHistory == subTransactionHistory;
//		}
//	}
}
