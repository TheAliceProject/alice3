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

package org.lgna.croquet.imp.cascade;

import org.lgna.croquet.Application;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.CascadeRoot;
import org.lgna.croquet.CompletionModel;

/**
 * @author Dennis Cosgrove
 */
public class RtRoot<T, CM extends CompletionModel> extends RtBlankOwner<T[], T, CascadeRoot<T, CM>, RootNode<T, CM>> {
	public RtRoot( CascadeRoot<T, CM> element ) {
		super( element, RootNode.createInstance( element ), null, -1 );
	}

	@Override
	public RtRoot<T, CM> getRtRoot() {
		return this;
	}

	@Override
	public RtBlank<?> getNearestBlank() {
		return null;
	}

	@Override
	public void select() {
	}

	public final T[] createValues( org.lgna.croquet.history.TransactionHistory transactionHistory, Class<T> componentType ) {
		RtBlank<T>[] rtBlanks = this.getBlankChildren();
		T[] rv = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newTypedArrayInstance( componentType, rtBlanks.length );
		for( int i = 0; i < rtBlanks.length; i++ ) {
			T value = rtBlanks[ i ].createValue( transactionHistory );
			try {
				rv[ i ] = value;
			} catch( ArrayStoreException ase ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "value:", value, "componentType:", componentType );
				throw ase;
			}
		}
		return rv;
	}

	public org.lgna.croquet.history.CompletionStep<CM> cancel( org.lgna.croquet.history.TransactionHistory transactionHistory, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		org.lgna.croquet.history.Transaction transaction = transactionHistory.acquireActiveTransaction();
		org.lgna.croquet.history.CompletionStep<CM> completionStep = this.getElement().createCompletionStep( transaction, trigger );
		this.getElement().handleCancel( completionStep, trigger, ce );
		return completionStep;
	}

	public org.lgna.croquet.history.CompletionStep<CM> complete( org.lgna.croquet.triggers.Trigger trigger ) {
		CascadeRoot<T, CM> root = this.getElement();
		org.lgna.croquet.history.TransactionHistory transactionHistory = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
		org.lgna.croquet.history.CompletionStep<CM> completionStep;
		try {
			//T[] values = this.createValues( transactionHistory, root.getComponentType() );
			completionStep = root.handleCompletion( transactionHistory, trigger, this );
		} catch( CancelException ce ) {
			completionStep = this.cancel( transactionHistory, trigger, ce );
		}
		//
		//		org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
		//		org.lgna.croquet.history.CompletionStep<CM> completionStep = root.createCompletionStep( transaction, trigger );
		//		try {
		//			T[] values = this.createValues( completionStep.getTransactionHistory(), root.getComponentType() );
		//			root.handleCompletion( completionStep, values );
		//		} catch( CancelException ce ) {
		//			this.cancel( completionStep, trigger, ce );
		//		}
		return completionStep;
	}

	protected void handleActionPerformed( java.awt.event.ActionEvent e ) {
		this.complete( org.lgna.croquet.triggers.ActionEventTrigger.createUserInstance( e ) );
	}

	public javax.swing.event.PopupMenuListener createPopupMenuListener( final org.lgna.croquet.views.MenuItemContainer menuItemContainer ) {
		return new javax.swing.event.PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible( javax.swing.event.PopupMenuEvent e ) {
				RtRoot.this.addNextNodeMenuItems( menuItemContainer );
			}

			@Override
			public void popupMenuWillBecomeInvisible( javax.swing.event.PopupMenuEvent e ) {
				RtRoot.this.removeAll( menuItemContainer );
			}

			@Override
			public void popupMenuCanceled( javax.swing.event.PopupMenuEvent e ) {
				//todo
				org.lgna.croquet.history.TransactionHistory transactionHistory = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
				RtRoot.this.cancel( transactionHistory, org.lgna.croquet.triggers.PopupMenuEventTrigger.createUserInstance( e ), null );
			}
		};
	}
}
