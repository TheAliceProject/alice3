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
	public static interface Observer {
		public void addingStep( Step<?> step );
		public void addedStep( Step<?> step );
		public void editCommitting( edu.cmu.cs.dennisc.croquet.Edit<?> edit );
		public void editCommitted( edu.cmu.cs.dennisc.croquet.Edit<?> edit );
		public void dropPending( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite );
		public void dropPended( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite );
		public void menuItemsSelectionChanged( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models );
		public void popupMenuResized( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu );
		public void dialogOpened( edu.cmu.cs.dennisc.croquet.Dialog dialog );
	}
	private static final java.util.List<Observer> observers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private static final java.util.Stack< TransactionHistory > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
//	private static final java.util.Set< CompletionStep<?> > stepsAwaitingFinish = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
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
	
	public static void addObserver( Observer observer ) {
		observers.add( observer );
	}
	public static void removeObserver( Observer observer ) {
		observers.remove( observer );
	}
	
	private static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().getActiveTransaction();
	}
	private static Transaction getLastTransaction() {
		return getActiveTransactionHistory().getLastTransaction();
	}

	/*package-private*/ static void fireAddingStep( Step<?> step ) {
		for( Observer observer : observers ) {
			observer.addingStep( step );
		}
	}
	/*package-private*/ static void fireAddedStep( Step<?> step ) {
		for( Observer observer : observers ) {
			observer.addedStep( step );
		}
	}
	
	//todo: reduce accessibility
	public static void firePopupMenuResized( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu ) {
		for( Observer observer : observers ) {
			observer.popupMenuResized( popupMenu );
		}
	}
	public static void fireDialogOpened( edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
		for( Observer observer : observers ) {
			observer.dialogOpened( dialog );
		}
	}
	private static void fireEditCommitting( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		for( Observer observer : observers ) {
			observer.editCommitting( edit );
		}
	}
	private static void fireEditCommitted( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		for( Observer observer : observers ) {
			observer.editCommitted( edit );
		}
	}
	private static void fireDropPending( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		for( Observer observer : observers ) {
			observer.dropPending( completionModel, dropReceptor, dropSite );
		}
	}
	private static void fireDropPended( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		for( Observer observer : observers ) {
			observer.dropPended( completionModel, dropReceptor, dropSite );
		}
	}
	private static void fireMenuItemsSelectionChanged( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models ) {
		for( Observer observer : observers ) {
			observer.menuItemsSelectionChanged( models );
		}
	}
	
	
	public static DragStep addDragStep( edu.cmu.cs.dennisc.croquet.DragAndDropModel model ) {
		return DragStep.createAndAddToTransaction( getActiveTransaction(), model ); 
	}
	public static ActionOperationStep addActionOperationStep( edu.cmu.cs.dennisc.croquet.ActionOperation model ) {
		Transaction transaction = getActiveTransaction();
		return ActionOperationStep.createAndAddToTransaction( transaction, model ); 
	}
	public static PlainDialogOperationStep addDialogOperationStep( edu.cmu.cs.dennisc.croquet.PlainDialogOperation model ) {
		Transaction transaction = getActiveTransaction();
		return PlainDialogOperationStep.createAndAddToTransaction( transaction, model );
	}
	public static <J extends edu.cmu.cs.dennisc.croquet.JComponent< ? >> InputDialogOperationStep<J> addInputDialogOperationStep( edu.cmu.cs.dennisc.croquet.InputDialogOperation< J > model ) {
		Transaction transaction = getActiveTransaction();
		return InputDialogOperationStep.createAndAddToTransaction( transaction, model );
	}
	public static StandardPopupOperationPrepStep addStandardPopupOperationPrepStep( edu.cmu.cs.dennisc.croquet.StandardPopupOperation standardPopupOperation ) {
		return StandardPopupOperationPrepStep.createAndAddToTransaction( getActiveTransaction(), standardPopupOperation );
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
	public static <E> CancelCompletionStep addCancelCompletionStep( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
		return CancelCompletionStep.createAndAddToTransaction( getActiveTransaction(), model ); 
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

//	private static void finishPendingTransactionIfNecessary() {
//		TransactionHistory activeTransactionHistory = getActiveTransactionHistory();
//		CompletionStep< ? > completionStep = activeTransactionHistory.getParent();
//		if( completionStep != null && completionStep.isActive() ) {
//			if( stepsAwaitingFinish.contains( completionStep ) ) {
//				finish( completionStep.getModel() );
//				stepsAwaitingFinish.remove( completionStep );
//			}
//		}
//	}

	public static void pendDrop( edu.cmu.cs.dennisc.croquet.CompletionModel completionModel, edu.cmu.cs.dennisc.croquet.DropReceptor dropReceptor, edu.cmu.cs.dennisc.croquet.DropSite dropSite ) {
		fireDropPending( completionModel, dropReceptor, dropSite );
		getLastTransaction().pendDrop( completionModel, dropReceptor, dropSite );
		fireDropPended( completionModel, dropReceptor, dropSite );
	}
	
	public static void handleMenuSelectionChanged( java.util.List< edu.cmu.cs.dennisc.croquet.Model > models ) {
		getActiveTransaction().pendMenuSelection( models );
		fireMenuItemsSelectionChanged( models );
	}

	public static void commit( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		popCompletionStepTransactionHistoryIfNecessary( edit.getModel() );
		fireEditCommitting( edit );
		getLastTransaction().commit( edit );
		fireEditCommitted( edit );
//		finishPendingTransactionIfNecessary();
	}
	public static void finish( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
		popCompletionStepTransactionHistoryIfNecessary( model );
		getLastTransaction().finish();
//		finishPendingTransactionIfNecessary();
	}
	public static void cancel( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
		popCompletionStepTransactionHistoryIfNecessary( model );
		getLastTransaction().cancel();
//		finishPendingTransactionIfNecessary();
	}
}
