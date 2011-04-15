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
public class TransactionManager {
	public static interface StepObserver {
		public void addingStep(Step<?> step);
		public void addedStep(Step<?> step);
	}
	private static java.util.List<StepObserver> stepObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private static java.util.Stack< TransactionHistory > stack;
	static {
		stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
		stack.push( new TransactionHistory() );
	}
	private TransactionManager() {
		throw new AssertionError();
	}
	public static TransactionHistory getRootTransactionHistory() {
		return stack.firstElement();
	}
	public static TransactionHistory getActiveTransactionHistory() {
		return stack.peek();
	}
	
	public static void pushTransactionHistory( TransactionHistory transactionHistory ) {
		stack.push( transactionHistory );
	}
	public static TransactionHistory popTransactionHistory() {
		return stack.pop();
	}
	
	public static void addStepObserver( StepObserver stepObserver ) {
		stepObservers.add( stepObserver );
	}
	public static void removeStepObserver( StepObserver stepObserver ) {
		stepObservers.remove( stepObserver );
	}
	
	public static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().getActiveTransaction();
	}
	/*package-private*/ static void fireAddingStep( Step<?> step ) {
		for( StepObserver stepObserver : stepObservers ) {
			stepObserver.addingStep( step );
		}
	}
	/*package-private*/ static void fireAddedStep( Step<?> step ) {
		for( StepObserver stepObserver : stepObservers ) {
			stepObserver.addedStep( step );
		}
	}
		
	public static void addDragStep( edu.cmu.cs.dennisc.croquet.DragAndDropModel model ) {
		DragStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static void addDropStep( edu.cmu.cs.dennisc.croquet.CompletionModel model, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor ) {
		DropStep.createAndAddToTransaction( getActiveTransaction(), model, dropReceptor ); 
	}
	public static void addActionOperationStep( edu.cmu.cs.dennisc.croquet.ActionOperation model ) {
		ActionOperationStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static <J extends edu.cmu.cs.dennisc.croquet.JComponent< ? >> void addInputDialogOperationStep( edu.cmu.cs.dennisc.croquet.InputDialogOperation< J > model ) {
		InputDialogOperationStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static void addBooleanStateChangeStep( edu.cmu.cs.dennisc.croquet.BooleanState model ) {
		BooleanStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static <E> void addListSelectionStateChangeStep( edu.cmu.cs.dennisc.croquet.ListSelectionState< E > model ) {
		ListSelectionStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static <E> void addListSelectionPrepStep( edu.cmu.cs.dennisc.croquet.ListSelectionStatePrepModel< E > model ) {
		ListSelectionStatePrepStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}

//	public static <E> void addCancelStep( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
//		CancelStep.createAndAddToTransaction( getActiveTransaction(), model ); 
//		this.setCompletionStep( new CancelStep( this, model ) );
//	}
	
	public static void commit( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		TransactionHistory transactionHistory = getActiveTransactionHistory();
		CompletionStep< ? > completionStep = transactionHistory.getParent();
		if( completionStep != null ) {
			if( completionStep.getModel() == edit.getModel() ) {
				completionStep.popTransactionHistoryIfNecessary();
				transactionHistory = getActiveTransactionHistory();
			}
		}
		transactionHistory.getLastTransaction().commit( edit );
	}
	public static void finish( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
	}
	public static void cancel( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
	}
}
