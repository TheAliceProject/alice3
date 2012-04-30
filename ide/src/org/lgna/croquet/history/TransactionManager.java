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
@Deprecated
public class TransactionManager {
	private TransactionManager() {
		throw new AssertionError();
	}

	@Deprecated
	public static void TODO_REMOVE_fireEvent( org.lgna.croquet.triggers.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		transaction.addPrepStep( new TODO_REMOVE_BogusStep( transaction, trigger ) );
	}

	@Deprecated
	private static TransactionHistory getActiveTransactionHistory() {
		return org.alice.ide.IDE.getActiveInstance().getProjectTransactionHistory().getActiveTransactionHistory();
	}

	@Deprecated
	private static void handleMenuSelectionStateChanged( javax.swing.event.ChangeEvent e ) {
		org.lgna.croquet.triggers.ChangeEventTrigger trigger = new org.lgna.croquet.triggers.ChangeEventTrigger( e );
		MenuSelection menuSelection = new MenuSelection( trigger );
		if( menuSelection.isValid() ) {
			getActiveTransaction().addMenuSelection( menuSelection );
		}
	}
	@Deprecated
	private static javax.swing.event.ChangeListener menuSelectionChangeListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			handleMenuSelectionStateChanged( e );
		}
	};
	@Deprecated
	public static void startListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().addChangeListener( menuSelectionChangeListener );
	}
	@Deprecated
	public static void stopListeningToMenuSelection() {
		javax.swing.MenuSelectionManager.defaultManager().removeChangeListener( menuSelectionChangeListener );
	}

	@Deprecated
	private static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().acquireActiveTransaction();
	}

	@Deprecated
	public static void firePopupMenuResized( PopupPrepStep step ) {
		step.fireChanged( new org.lgna.croquet.history.event.PopupMenuResizedEvent( step ) );
	}

	@Deprecated
	public static DragStep addDragStep( org.lgna.croquet.DragModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		return DragStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	@Deprecated
	public static CompletionStep<?> addOperationStep( org.lgna.croquet.Operation model, org.lgna.croquet.triggers.Trigger trigger, TransactionHistory transactionHistory ) {
		Transaction transaction = getActiveTransaction();
		return CompletionStep.createAndAddToTransaction( transaction, model, trigger, transactionHistory ); 
	}
	@Deprecated
	public static CompletionStep<?> addOperationStep( org.lgna.croquet.Operation model, org.lgna.croquet.triggers.Trigger trigger ) {
		return addOperationStep( model, trigger, null ); 
	}
	@Deprecated
	public static PopupPrepStep addPopupPrepStep( org.lgna.croquet.PopupPrepModel popupPrepModel, org.lgna.croquet.triggers.Trigger trigger ) {
		return PopupPrepStep.createAndAddToTransaction( getActiveTransaction(), popupPrepModel, trigger );
	}

	@Deprecated
	public static <T> StateChangeStep<T> addStateChangeStep( org.lgna.croquet.State< T > model, org.lgna.croquet.triggers.Trigger trigger ) {
		return StateChangeStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	@Deprecated
	public static <T> ListSelectionStatePrepStep<T> addListSelectionPrepStep( org.lgna.croquet.ListSelectionState.InternalPrepModel< T > model, org.lgna.croquet.triggers.Trigger trigger ) {
		return ListSelectionStatePrepStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}
	@Deprecated
	public static CancelCompletionStep addCancelCompletionStep( org.lgna.croquet.CompletionModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		return CancelCompletionStep.createAndAddToTransaction( getActiveTransaction(), model, trigger ); 
	}

	@Deprecated
	public static void simulatedMenuTransaction( Transaction transaction, java.util.List< MenuItemPrepModel > menuItemPrepModels ) {
		for( org.lgna.croquet.MenuItemPrepModel menuItemPrepModel : menuItemPrepModels ) {
			System.err.println( "todo: add step for: " + menuItemPrepModel );
			//org.lgna.croquet.steps.MenuItemPrepStep.createAndAddToTransaction( transaction, menuItemPrepModel, org.lgna.croquet.triggers.SimulatedTrigger.SINGLETON );
		}
	}

	@Deprecated
	public static <T> Transaction createSimulatedTransactionForState( org.lgna.croquet.State< T > state, T value ) {
		Transaction rv = new Transaction( (TransactionHistory)null );
		org.lgna.croquet.triggers.Trigger trigger = new org.lgna.croquet.triggers.SimulatedTrigger();
		StateChangeStep< T > step = StateChangeStep.createAndAddToTransaction( rv, state, trigger );
		step.setEdit( new org.lgna.croquet.edits.StateEdit< T >( step, state.getValue(), value ) );
		return rv;
	}

	@Deprecated
	public static <E> Transaction createSimulatedTransaction( TransactionHistory transactionHistory, State< E > state, E prevValue, E nextValue ) {
		org.lgna.croquet.history.Transaction rv = new org.lgna.croquet.history.Transaction( transactionHistory );
		org.lgna.croquet.history.StateChangeStep< E > completionStep = org.lgna.croquet.history.StateChangeStep.createAndAddToTransaction( rv, state, new org.lgna.croquet.triggers.SimulatedTrigger() );
		org.lgna.croquet.edits.StateEdit< E > edit = new org.lgna.croquet.edits.StateEdit<E>( completionStep, prevValue, nextValue );
		completionStep.setEdit( edit );
		return rv;
	}

	@Deprecated
	public static <T> Transaction createSimulatedTransactionForCascade( TransactionHistory transactionHistory, Cascade<T> cascade ) {
		org.lgna.croquet.history.Transaction rv = new org.lgna.croquet.history.Transaction( transactionHistory );
		org.lgna.croquet.history.CompletionStep< Cascade<T> > completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( rv, cascade, new org.lgna.croquet.triggers.SimulatedTrigger(), transactionHistory );
		org.lgna.croquet.edits.Edit edit = null; //todo
		completionStep.setEdit( edit );
		return rv;
	}
}
