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

import org.lgna.croquet.*;

/**
 * @author Dennis Cosgrove
 */
public class TransactionManager {
	private static final java.util.Stack< TransactionHistory > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	static {
		stack.push( new TransactionHistory() );
	}
	private TransactionManager() {
		throw new AssertionError();
	}

	public static void TODO_REMOVE_fireEvent( org.lgna.croquet.triggers.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		transaction.addPrepStep( new TODO_REMOVE_BogusStep( transaction, trigger ) );
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
	
	private static boolean isCroquetMenuSelection( javax.swing.MenuElement[] menuElements ) {
		for( javax.swing.MenuElement menuElement : menuElements ) {
			org.lgna.croquet.components.Component< ? > component = org.lgna.croquet.components.Component.lookup( menuElement.getComponent() );
			if( component instanceof org.lgna.croquet.components.MenuBar || component instanceof org.lgna.croquet.components.MenuItem || component instanceof org.lgna.croquet.components.Menu || component instanceof org.lgna.croquet.components.PopupMenu || component instanceof org.lgna.croquet.components.MenuTextSeparator ) {
				return true;
			}
		}
		return menuElements.length == 0;
	}

	private static javax.swing.JMenuBar getJMenuBarOrigin( javax.swing.MenuElement[] menuElements ) { 
		if( menuElements.length > 0 ) {
			javax.swing.MenuElement menuElement0 = menuElements[ 0 ];
			if( menuElement0 instanceof javax.swing.JMenuBar ) {
				return (javax.swing.JMenuBar)menuElement0;
			}
		}
		return null;
	}
	private static org.lgna.croquet.components.MenuBar getMenuBarOrigin( javax.swing.MenuElement[] menuElements ) {
		javax.swing.JMenuBar jMenuBar = getJMenuBarOrigin( menuElements );
		if( jMenuBar != null ) {
			return (org.lgna.croquet.components.MenuBar)org.lgna.croquet.components.Component.lookup( jMenuBar );
		} else {
			return null;
		}
	}
	private static MenuBarComposite getMenuBarModelOrigin( javax.swing.MenuElement[] menuElements ) {
		org.lgna.croquet.components.MenuBar menuBar = getMenuBarOrigin( menuElements );
		if( menuBar != null ) {
			return menuBar.getComposite();
		} else {
			return null;
		}
	}

	private static void handleMenuSelectionStateChanged( javax.swing.event.ChangeEvent e ) {
		org.lgna.croquet.triggers.MenuSelectionTrigger trigger = new org.lgna.croquet.triggers.MenuSelectionTrigger( e );
		if( trigger.isValid() ) {
			getActiveTransaction().addMenuSelection( trigger );
//		} else {
//			System.err.println( "warning: not croquet menu selection." );
		}
	}
	private static javax.swing.event.ChangeListener menuSelectionChangeListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			handleMenuSelectionStateChanged( e );
		}
	};
	public static void startListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().addChangeListener( menuSelectionChangeListener );
	}
	public static void stopListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().removeChangeListener( menuSelectionChangeListener );
	}

	public static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().getActiveTransaction();
	}
	private static Transaction getLastTransaction() {
		return getActiveTransactionHistory().getLastTransaction();
	}

//	/*package-private*/ static void fireAddingStep( Step<?> step ) {
//		for( Observer observer : observers ) {
//			observer.addingStep( step );
//		}
//	}
//	/*package-private*/ static void fireAddedStep( Step<?> step ) {
//		for( Observer observer : observers ) {
//			observer.addedStep( step );
//		}
//	}
//	
//	//todo: reduce accessibility
	@Deprecated
	public static void fireTransactionCanceled( Transaction transaction ) {
//		for( Observer observer : observers ) {
//			observer.transactionCanceled( transaction );
//		}
	}
//	//todo: reduce accessibility
	@Deprecated
	public static void firePopupMenuResized( PopupPrepStep step ) {
		step.fireChanged( new org.lgna.croquet.history.event.PopupMenuResizedEvent( step ) );
//		for( Observer observer : observers ) {
//			observer.popupMenuResized( popupMenu );
//		}
	}
	@Deprecated
	public static void fireDialogOpened( org.lgna.croquet.components.Dialog dialog ) {
//		for( Observer observer : observers ) {
//			observer.dialogOpened( dialog );
//		}
	}
	@Deprecated
	public static void fireDialogClosing( org.lgna.croquet.components.Dialog dialog ) {
//		for( Observer observer : observers ) {
//			observer.dialogClosing( dialog );
//		}
	}
	@Deprecated
	public static void fireDialogClosed( org.lgna.croquet.components.Dialog dialog ) {
//		for( Observer observer : observers ) {
//			observer.dialogClosed( dialog );
//		}
	}
//	private static void fireEditCommitting( org.lgna.croquet.edits.Edit< ? > edit ) {
//		for( Observer observer : observers ) {
//			observer.editCommitting( edit );
//		}
//	}
//	private static void fireEditCommitted( org.lgna.croquet.edits.Edit< ? > edit ) {
//		for( Observer observer : observers ) {
//			observer.editCommitted( edit );
//		}
//	}
//	private static void fireFinishing( Transaction transaction ) {
//		for( Observer observer : observers ) {
//			observer.finishing( transaction );
//		}
//	}
//	private static void fireFinished( Transaction transaction ) {
//		for( Observer observer : observers ) {
//			observer.finished( transaction );
//		}
//	}
//	private static void fireDropPending( org.lgna.croquet.Model model, org.lgna.croquet.DropReceptor dropReceptor, org.lgna.croquet.DropSite dropSite ) {
//		for( Observer observer : observers ) {
//			observer.dropPending( model, dropReceptor, dropSite );
//		}
//	}
//	private static void fireDropPended( org.lgna.croquet.Model model, org.lgna.croquet.DropReceptor dropReceptor, org.lgna.croquet.DropSite dropSite ) {
//		for( Observer observer : observers ) {
//			observer.dropPended( model, dropReceptor, dropSite );
//		}
//	}
//	private static void fireMenuItemsSelectionChanged( java.util.List< org.lgna.croquet.Model > models ) {
//		for( Observer observer : observers ) {
//			observer.menuItemsSelectionChanged( models );
//		}
//	}
		
	public static DragStep addDragStep( org.lgna.croquet.DragModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		return DragStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static CompletionStep<?> addOperationStep( org.lgna.croquet.Operation model, org.lgna.croquet.triggers.Trigger trigger, TransactionHistory transactionHistory ) {
		Transaction transaction = getActiveTransaction();
		return CompletionStep.createAndAddToTransaction( transaction, model, trigger, transactionHistory ); 
	}
	public static CompletionStep<?> addOperationStep( org.lgna.croquet.Operation model, org.lgna.croquet.triggers.Trigger trigger ) {
		return addOperationStep( model, trigger, null ); 
	}
	public static PopupPrepStep addPopupPrepStep( org.lgna.croquet.PopupPrepModel popupPrepModel, org.lgna.croquet.triggers.Trigger trigger ) {
		return PopupPrepStep.createAndAddToTransaction( getActiveTransaction(), popupPrepModel, trigger );
	}
	public static <T> CascadeCompletionStep<T> addCascadeCompletionStep( org.lgna.croquet.Cascade<T> model, org.lgna.croquet.triggers.Trigger trigger ) {
		return CascadeCompletionStep.createAndAddToTransaction( getActiveTransaction(), model, trigger );
	}

	public static <T> StateChangeStep<T> addStateChangeStep( org.lgna.croquet.State< T > model, org.lgna.croquet.triggers.Trigger trigger ) {
		return StateChangeStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static <T> ListSelectionStatePrepStep<T> addListSelectionPrepStep( org.lgna.croquet.ListSelectionState.InternalPrepModel< T > model, org.lgna.croquet.triggers.Trigger trigger ) {
		return ListSelectionStatePrepStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	public static CancelCompletionStep addCancelCompletionStep( org.lgna.croquet.CompletionModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		return CancelCompletionStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}


//	private static void popCompletionStepTransactionHistoryIfNecessary( org.lgna.croquet.Model model ) {
//		TransactionHistory transactionHistory = getActiveTransactionHistory();
//		CompletionStep< ? > completionStep = transactionHistory.getParent();
//		if( completionStep != null ) {
//			if( completionStep.getModel() == model ) {
//				completionStep.popTransactionHistoryIfNecessary();
//			}
//		}
//	}

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

//	public static void commit( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
//		popCompletionStepTransactionHistoryIfNecessary( edit.getModel() );
//		fireEditCommitting( edit );
//		getLastTransaction().commit( edit );
//		fireEditCommitted( edit );
////		finishPendingTransactionIfNecessary();
//	}
//	public static void finish( edu.cmu.cs.dennisc.croquet.CompletionModel model ) {
//		popCompletionStepTransactionHistoryIfNecessary( model );
//		Transaction transaction = getLastTransaction();
//		fireFinishing( transaction );
//		transaction.finish();
//		fireFinished( transaction );
////		finishPendingTransactionIfNecessary();
//	}
//	public static void cancel( edu.cmu.cs.dennisc.croquet.CompletionModel model, org.lgna.croquet.Trigger trigger ) {
//		popCompletionStepTransactionHistoryIfNecessary( model );
//		getLastTransaction().cancel( trigger );
////		finishPendingTransactionIfNecessary();
//	}

	public static void simulatedMenuTransaction( Transaction transaction, java.util.List< MenuItemPrepModel > menuItemPrepModels ) {
		for( org.lgna.croquet.MenuItemPrepModel menuItemPrepModel : menuItemPrepModels ) {
			System.err.println( "todo: add step for: " + menuItemPrepModel );
			//org.lgna.croquet.steps.MenuItemPrepStep.createAndAddToTransaction( transaction, menuItemPrepModel, org.lgna.croquet.triggers.SimulatedTrigger.SINGLETON );
		}
	}

//	public static void handleBoundedIntegerStateChanged( BoundedIntegerState boundedIntegerState, int value, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
//		TransactionHistory transactionHistory = getActiveTransactionHistory();
//		Transaction transaction = transactionHistory.getActiveTransaction();
//		if( isAdjusting ) {
//			org.lgna.croquet.history.event.AdjustValueStateEvent adjustEvent = new org.lgna.croquet.history.event.AdjustValueStateEvent( transaction );
//			transaction.fireChanging( adjustEvent );
//			transaction.fireChanged( adjustEvent );
//		} else {
//			StateChangeStep.createAndAddToTransaction( transaction, boundedIntegerState, trigger );
//		}
////		org.lgna.croquet.steps.TransactionManager.handleStateChanged( BoundedRangeIntegerState.this, e );
////		org.lgna.croquet.steps.BoundedRangeIntegerStateChangeStep step;
////		if( this.previousValueIsAdjusting ) {
////			step = (org.lgna.croquet.steps.BoundedRangeIntegerStateChangeStep)org.lgna.croquet.steps.TransactionManager.getActiveTransaction().getCompletionStep();
////		} else {
////			step = org.lgna.croquet.steps.TransactionManager.addBoundedRangeIntegerStateChangeStep( BoundedRangeIntegerState.this );
////		}
////		this.previousValueIsAdjusting = boundedModel.getValueIsAdjusting();
////		step.handleStateChanged( e );
////		BoundedRangeIntegerState.this.fireValueChanged( e );
////
////		if( this.previousValueIsAdjusting ) {
////			//pass
////		} else {
////			int nextValue = boundedModel.getValue();
////			step.commitAndInvokeDo( new org.lgna.croquet.edits.BoundedRangeIntegerStateEdit( e, BoundedRangeIntegerState.this.previousValue, nextValue, false ) );
////			BoundedRangeIntegerState.this.previousValue = nextValue;
//////				ModelContext< ? > popContext = ContextManager.popContext();
//////				assert popContext == boundedIntegerStateContext;
////		}
//	}
//	public static void handleBoundedDoubleStateChanged( BoundedDoubleState boundedDoubleState, double value, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
//		TransactionHistory transactionHistory = getActiveTransactionHistory();
//		Transaction transaction = transactionHistory.getActiveTransaction();
//		if( isAdjusting ) {
//			org.lgna.croquet.history.event.AdjustValueStateEvent adjustEvent = new org.lgna.croquet.history.event.AdjustValueStateEvent( transaction );
//			transaction.fireChanging( adjustEvent );
//			transaction.fireChanged( adjustEvent );
//		} else {
//			StateChangeStep.createAndAddToTransaction( transaction, boundedDoubleState, trigger );
//		}
//	}

//	public static org.lgna.croquet.edits.StateEdit commitEdit( BooleanState booleanState, boolean value, org.lgna.croquet.triggers.Trigger trigger ) {
//		org.lgna.croquet.history.StateChangeStep< Boolean > step = org.lgna.croquet.history.TransactionManager.addStateChangeStep( booleanState, trigger );
//		org.lgna.croquet.edits.StateEdit rv = new org.lgna.croquet.edits.StateEdit( step, value );
//		step.commitAndInvokeDo( rv );
//		return rv;
//	}

	public static <T> Transaction createSimulatedTransactionForState( org.lgna.croquet.State< T > state, T value ) {
		Transaction rv = new Transaction( (TransactionHistory)null );
		org.lgna.croquet.triggers.Trigger trigger = new org.lgna.croquet.triggers.SimulatedTrigger();
		StateChangeStep< T > step = StateChangeStep.createAndAddToTransaction( rv, state, trigger );
		step.setEdit( new org.lgna.croquet.edits.StateEdit< T >( step, state.getValue(), value ) );
		return rv;
	}

	public static <T> void handleStateChanged( State<T> state, T prevValue, T nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		TransactionHistory transactionHistory = getActiveTransactionHistory();
		Transaction transaction = transactionHistory.getActiveTransaction();
		if( isAdjusting ) {
			org.lgna.croquet.history.event.AdjustValueStateEvent adjustEvent = new org.lgna.croquet.history.event.AdjustValueStateEvent( transaction );
			transaction.fireChanging( adjustEvent );
			transaction.fireChanged( adjustEvent );
		} else {
			StateChangeStep.createAndAddToTransaction( transaction, state, trigger );
		}
	}

	public static <E> Transaction createSimulatedTransaction( TransactionHistory transactionHistory, State< E > state, E prevValue, E nextValue ) {
		org.lgna.croquet.history.Transaction rv = new org.lgna.croquet.history.Transaction( transactionHistory );
		org.lgna.croquet.history.StateChangeStep< E > completionStep = org.lgna.croquet.history.StateChangeStep.createAndAddToTransaction( rv, state, new org.lgna.croquet.triggers.SimulatedTrigger() );
		org.lgna.croquet.edits.StateEdit< E > edit = new org.lgna.croquet.edits.StateEdit( completionStep, prevValue, nextValue );
		completionStep.setEdit( edit );
		return rv;
	}
	public static void handleDocumentEvent( StringState stringState, org.lgna.croquet.triggers.Trigger trigger, String previousValue, String nextValue ) {
		Transaction transaction = getLastTransaction();
		StateChangeStep< String > stateChangeStep = null;
		if( transaction != null ) {
			CompletionStep< ? > step = transaction.getCompletionStep();
			if( step instanceof StateChangeStep ) {
				if( step.getModel() == stringState ) {
					stateChangeStep = (StateChangeStep< String >)step;
				}
			}
		} else {
			transaction = getActiveTransaction();
		}
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "pendDocumentEvent" );
//		if( stateChangeStep != null ) {
//			stateChangeStep.pendDocumentEvent( trigger, nextValue );
//		} else {
//			stateChangeStep = StateChangeStep.createAndAddToTransaction( transaction, stringState, trigger, previousValue, nextValue );
//		}
	}
}
