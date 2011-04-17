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

import edu.cmu.cs.dennisc.croquet.Model;


/**
 * @author Dennis Cosgrove
 */
public class TransactionManager {
	public static interface StepObserver {
		public void addingStep(Step<?> step);
		public void addedStep(Step<?> step);
	}
	private static final java.util.List<StepObserver> stepObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private static final java.util.Stack< TransactionHistory > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private static final java.util.Set< CompletionStep<?> > stepsAwaitngFinish = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
	static {
		stack.push( new TransactionHistory() );
	}
	private TransactionManager() {
		throw new AssertionError();
	}
	public static TransactionHistory getRootTransactionHistory() {
		return stack.firstElement();
	}
	private static TransactionHistory getActiveTransactionHistory() {
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
	
	private static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().getActiveTransaction();
	}
	private static Transaction getLastTransaction() {
		return getActiveTransactionHistory().getLastTransaction();
	}

	/*package-private*/ static void fireAddingStep( Step<?> step ) {
//		reifyDropIfNecessary( step );
		for( StepObserver stepObserver : stepObservers ) {
			stepObserver.addingStep( step );
		}
	}
	/*package-private*/ static void fireAddedStep( Step<?> step ) {
		for( StepObserver stepObserver : stepObservers ) {
			stepObserver.addedStep( step );
		}
	}
	
	public static DragStep addDragStep( edu.cmu.cs.dennisc.croquet.DragAndDropModel model ) {
		return DragStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static ActionOperationStep addActionOperationStep( edu.cmu.cs.dennisc.croquet.ActionOperation model ) {
		Transaction transaction = getActiveTransaction();
		addMenuPrepStepsIfNecessary( transaction );
		return ActionOperationStep.createAndAddToTransaction( transaction, model ); 
	}
	public static DialogOperationStep addDialogOperationStep( edu.cmu.cs.dennisc.croquet.DialogOperation model ) {
		Transaction transaction = getActiveTransaction();
		addMenuPrepStepsIfNecessary( transaction );
		return DialogOperationStep.createAndAddToTransaction( transaction, model );
	}
	public static <J extends edu.cmu.cs.dennisc.croquet.JComponent< ? >> InputDialogOperationStep<J> addInputDialogOperationStep( edu.cmu.cs.dennisc.croquet.InputDialogOperation< J > model ) {
		Transaction transaction = getActiveTransaction();
		addMenuPrepStepsIfNecessary( transaction );
		return InputDialogOperationStep.createAndAddToTransaction( transaction, model );
	}
	public static StandardPopupOperationStep addStandardPopupOperationStep( edu.cmu.cs.dennisc.croquet.StandardPopupOperation model ) {
		StandardPopupOperationStep rv = StandardPopupOperationStep.createAndAddToTransaction( getActiveTransaction(), model ); 
		stepsAwaitngFinish.add( rv );
		return rv;
	}
	public static <T> CascadePopupOperationStep<T> addCascadePopupOperationStep( edu.cmu.cs.dennisc.croquet.CascadePopupOperation<T> model ) {
		return CascadePopupOperationStep.createAndAddToTransaction( getActiveTransaction(), model );
	}
	public static BooleanStateChangeStep addBooleanStateChangeStep( edu.cmu.cs.dennisc.croquet.BooleanState model ) {
		return BooleanStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model );
	}
	public static StringStateChangeStep addStringStateChangeStep( edu.cmu.cs.dennisc.croquet.StringState model ) {
		return StringStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static <E> ListSelectionStateChangeStep<E> addListSelectionStateChangeStep( edu.cmu.cs.dennisc.croquet.ListSelectionState< E > model ) {
		return ListSelectionStateChangeStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static <E> ListSelectionStatePrepStep<E> addListSelectionPrepStep( edu.cmu.cs.dennisc.croquet.ListSelectionStatePrepModel< E > model ) {
		return ListSelectionStatePrepStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static <E> CancelPrepStep addCancelPrepStep() {
		return CancelPrepStep.createAndAddToTransaction( getActiveTransaction() ); 
	}

	public static void pendDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor ) {
		getLastTransaction().pendDrop( completionModel, dropReceptor );
	}

	private static void popCompletionStepTransactionHistoryIfNecessary( edu.cmu.cs.dennisc.croquet.Model model ) {
		TransactionHistory transactionHistory = getActiveTransactionHistory();
		CompletionStep< ? > completionStep = transactionHistory.getParent();
		if( completionStep != null ) {
			if( completionStep.getModel() == model ) {
				completionStep.popTransactionHistoryIfNecessary();
			}
		}
	}

	private static void finishPendingTransactionIfNecessary() {
		TransactionHistory activeTransactionHistory = getActiveTransactionHistory();
		CompletionStep< ? > completionStep = activeTransactionHistory.getParent();
		if( completionStep != null && completionStep.isActive() ) {
			if( stepsAwaitngFinish.contains( completionStep ) ) {
				finish( completionStep.getModel() );
				stepsAwaitngFinish.remove( completionStep );
			}
		}
	}
	private static java.util.List< Model > lastMenuSelection; 
	public static void handleMenuSelectionChanged( java.util.List< Model > models ) {
		lastMenuSelection = models;
	}

	private static void addMenuPrepStepsIfNecessary( Transaction transaction ) {
		if( lastMenuSelection != null && lastMenuSelection.size() > 0 ) {
			for( Model model : lastMenuSelection ) {
				if( model instanceof edu.cmu.cs.dennisc.croquet.MenuModel ) {
					MenuModelStep.createAndAddToTransaction( transaction, (edu.cmu.cs.dennisc.croquet.MenuModel)model );
				} else if( model instanceof edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? > ) {
					CascadeFillInStep.createAndAddToTransaction( transaction, (edu.cmu.cs.dennisc.croquet.CascadeFillIn< ?, ? >)model );
				} else if( model instanceof edu.cmu.cs.dennisc.croquet.MenuBarModel ) {
					//pass
				} else if( model instanceof edu.cmu.cs.dennisc.croquet.CompletionModel ) {
					//pass
				} else {
					assert false : model;
				}
			}
			lastMenuSelection = null;
		}
	}

	public static void commit( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		addMenuPrepStepsIfNecessary( getLastTransaction() );
		popCompletionStepTransactionHistoryIfNecessary( edit.getModel() );
		getLastTransaction().commit( edit );
		finishPendingTransactionIfNecessary();
	}
	public static void finish( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
		addMenuPrepStepsIfNecessary( getLastTransaction() );
		popCompletionStepTransactionHistoryIfNecessary( model );
		getLastTransaction().finish();
		finishPendingTransactionIfNecessary();
	}
	public static void cancel( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
		addMenuPrepStepsIfNecessary( getLastTransaction() );
		popCompletionStepTransactionHistoryIfNecessary( model );
		getLastTransaction().cancel();
		finishPendingTransactionIfNecessary();
	}
}
