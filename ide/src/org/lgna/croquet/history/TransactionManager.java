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
@Deprecated
public class TransactionManager {
	private TransactionManager() {
		throw new AssertionError();
	}

	@Deprecated
	private static TransactionHistory getActiveTransactionHistory() {
		return org.lgna.croquet.Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
	}

	@Deprecated
	private static Transaction getActiveTransaction() {
		return getActiveTransactionHistory().acquireActiveTransaction();
	}

	@Deprecated
	public static void TODO_REMOVE_fireEvent( org.lgna.croquet.triggers.Trigger trigger ) {
		Transaction transaction = getActiveTransaction();
		transaction.addPrepStep( new TODO_REMOVE_BogusStep( transaction, trigger ) );
	}

	@Deprecated
	public static void firePopupMenuResized( PopupPrepStep step ) {
		step.fireChanged( new org.lgna.croquet.history.event.PopupMenuResizedEvent( step ) );
	}

	@Deprecated
	public static DragStep addDragStep( org.lgna.croquet.DragModel model, org.lgna.croquet.triggers.DragTrigger trigger ) {
		return DragStep.createAndAddToTransaction( getActiveTransaction(), model, trigger );
	}

	@Deprecated
	public static PopupPrepStep addPopupPrepStep( org.lgna.croquet.PopupPrepModel popupPrepModel, org.lgna.croquet.triggers.Trigger trigger ) {
		return PopupPrepStep.createAndAddToTransaction( getActiveTransaction(), popupPrepModel, trigger );
	}

	@Deprecated
	public static <T> ListSelectionStatePrepStep<T> addListSelectionPrepStep( org.lgna.croquet.ListSelectionState.InternalPrepModel<T> model, org.lgna.croquet.triggers.Trigger trigger ) {
		return ListSelectionStatePrepStep.createAndAddToTransaction( getActiveTransaction(), model, trigger );
	}

	@Deprecated
	public static CompletionStep addCancelCompletionStep( org.lgna.croquet.CompletionModel model, org.lgna.croquet.triggers.Trigger trigger ) {
		CompletionStep rv = CompletionStep.createAndAddToTransaction( getActiveTransaction(), model, trigger, null );
		rv.cancel();
		return rv;
	}
}
