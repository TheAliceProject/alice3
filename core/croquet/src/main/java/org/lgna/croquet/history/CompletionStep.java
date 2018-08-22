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
package org.lgna.croquet.history;

import org.lgna.croquet.CompletionModel;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.event.CancelEvent;
import org.lgna.croquet.history.event.EditCommittedEvent;
import org.lgna.croquet.history.event.FinishedEvent;
import org.lgna.croquet.triggers.DropTrigger;
import org.lgna.croquet.triggers.Trigger;

/**
 * @author Dennis Cosgrove
 */
public final class CompletionStep<M extends CompletionModel> extends Step<M> {

	private final TransactionHistory transactionHistory;
	private AbstractEdit<M> edit;
	private boolean isSuccessfullyCompleted = false;
	private boolean isPending = true;

	public static <M extends CompletionModel> CompletionStep<M> createAndAddToTransaction( Transaction parent, M model, Trigger trigger, TransactionHistory transactionHistory ) {
		return new CompletionStep<M>( parent, model, trigger, transactionHistory );
	}

	public CompletionStep( Transaction parent, M model, Trigger trigger, TransactionHistory transactionHistory ) {
		super( parent, model, trigger );
		parent.setCompletionStep( this );
		this.transactionHistory = transactionHistory;
		if( this.transactionHistory != null ) {
			this.transactionHistory.setOwner( this );
		}
	}

	public DropSite findDropSite() {
		Step<?> step = findAcceptableStep( step1 -> step1.getTrigger() instanceof DropTrigger );
		return step != null ? ((DropTrigger) step.getTrigger()).getDropSite() : null;
	}

	public boolean isValid() {
		return ( this.getModel() != null ) && ( ( this.edit == null ) || this.edit.isValid() );
	}

	public TransactionHistory getTransactionHistory() {
		return this.transactionHistory;
	}

	public boolean isPending() {
		return this.isPending;
	}

	public boolean isSuccessfullyCompleted() {
		return this.isSuccessfullyCompleted;
	}

	public boolean isCanceled() {
		return !isPending() && !isSuccessfullyCompleted();
	}


	public AbstractEdit<?> getEdit() {
		return this.edit;
	}

	private void setEdit( Edit edit ) {
		this.isSuccessfullyCompleted = true;
		this.edit = (AbstractEdit<M>)edit;
		this.isPending = false;
	}

	public void commitAndInvokeDo( Edit edit ) {
		EditCommittedEvent e = new EditCommittedEvent( this, edit );
		this.fireChanging( e );
		this.setEdit( edit );
		edit.doOrRedo( true );
		this.fireChanged( e );
	}

	public void finish() {
		this.isSuccessfullyCompleted = true;
		FinishedEvent e = new FinishedEvent( this );
		this.fireChanging( e );
		this.edit = null;
		this.isPending = false;
		this.fireChanged( e );
	}

	public void cancel() {
		this.isSuccessfullyCompleted = false;
		CancelEvent e = new CancelEvent( this );
		this.fireChanging( e );
		this.edit = null;
		this.isPending = false;
		this.fireChanged( e );
	}

	@Override
	protected StringBuilder updateRepr( StringBuilder rv ) {
		rv = super.updateRepr( rv );
		rv.append( ";edit=" );
		rv.append( this.edit );
		return rv;
	}
}
