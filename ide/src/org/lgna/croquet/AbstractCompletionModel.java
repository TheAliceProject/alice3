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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCompletionModel extends AbstractModel implements CompletionModel {
	private final Group group;
	private int ignoreCount = 0;

	private static final class SidekickLabel extends PlainStringValue {
		private final AbstractCompletionModel completionModel;

		public SidekickLabel( AbstractCompletionModel completionModel ) {
			super( java.util.UUID.fromString( "9ca020c1-1a00-44f1-8541-84b31b787e49" ) );
			this.completionModel = completionModel;
		}

		@Override
		protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.completionModel.getClassUsedForLocalization();
		}

		@Override
		protected String getSubKeyForLocalization() {
			return "sidekickLabel";
		}
	}

	private SidekickLabel sidekickLabel;

	public AbstractCompletionModel( Group group, java.util.UUID id ) {
		super( id );
		this.group = group;
	}

	public Group getGroup() {
		return this.group;
	}

	protected abstract void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger );

	@Deprecated
	protected Model getSurrogateModel() {
		return null;
	}

	@Override
	public final org.lgna.croquet.history.CompletionStep<?> fire( org.lgna.croquet.triggers.Trigger trigger ) {
		Model surrogateModel = this.getSurrogateModel();
		if( surrogateModel != null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: end use of surrogate", this );
			org.lgna.croquet.history.Step<?> step = surrogateModel.fire( trigger );
			return step.getOwnerTransaction().getCompletionStep();
		} else {
			if( this.isEnabled() ) {
				this.initializeIfNecessary();
				org.lgna.croquet.history.TransactionHistory history = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
				org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
				this.perform( transaction, trigger );
				return transaction.getCompletionStep();
			} else {
				return null;
			}
		}
	}

	@Deprecated
	public final org.lgna.croquet.history.CompletionStep<?> fire() {
		return fire( org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
	}

	public synchronized PlainStringValue getSidekickLabel() {
		if( this.sidekickLabel != null ) {
			//pass
		} else {
			this.sidekickLabel = new SidekickLabel( this );
			this.sidekickLabel.initializeIfNecessary();
		}
		return this.sidekickLabel;
	}

	public StringValue peekSidekickLabel() {
		return this.sidekickLabel;
	}

	protected void pushIgnore() {
		this.ignoreCount++;
	}

	protected void popIgnore() {
		this.ignoreCount--;
		assert this.ignoreCount >= 0;
	}

	protected boolean isAppropriateToComplete() {
		return ( Manager.isInTheMidstOfUndoOrRedo() == false ) && ( this.ignoreCount == 0 );
	}

	public final String getTutorialTransactionTitle( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.initializeIfNecessary();
		org.lgna.croquet.edits.Edit<?> edit = step.getEdit();
		if( edit != null ) {
			return edit.getTutorialTransactionTitle();
		} else {
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			return this.getTutorialNoteText( step, trigger != null ? "" : "", edit );
		}
	}

	public abstract boolean isAlreadyInState( org.lgna.croquet.edits.Edit<?> edit );

	public org.lgna.croquet.edits.Edit<?> commitTutorialCompletionEdit( org.lgna.croquet.history.CompletionStep<?> completionStep, org.lgna.croquet.edits.Edit<?> originalEdit, org.lgna.croquet.Retargeter retargeter ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( originalEdit );
		return null;
	}

	public abstract Iterable<? extends PrepModel> getPotentialRootPrepModels();

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( "group=" );
		sb.append( this.getGroup() );
	}

	protected boolean isSubTransactionHistoryRequired() {
		return false;
	}

	protected void addGeneratedPrepSteps( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.edits.Edit<?> edit ) {
	}

	protected void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.Edit<?> ownerEdit ) {
	}

	protected void addGeneratedPostTransactions( org.lgna.croquet.history.TransactionHistory ownerTransactionHistory, org.lgna.croquet.edits.Edit<?> edit ) {
	}

	protected void pushGeneratedContexts( org.lgna.croquet.edits.Edit<?> edit ) {
	}

	protected void popGeneratedContexts( org.lgna.croquet.edits.Edit<?> edit ) {
	}

	public final org.lgna.croquet.history.Transaction addGeneratedTransaction( org.lgna.croquet.history.TransactionHistory ownerTransactionHistory, org.lgna.croquet.triggers.Trigger trigger, org.lgna.croquet.edits.Edit<?> edit ) {
		this.pushGeneratedContexts( edit );
		try {
			org.lgna.croquet.history.Transaction transaction = org.lgna.croquet.history.Transaction.createAndAddToHistory( ownerTransactionHistory );
			this.addGeneratedPrepSteps( transaction, edit );
			org.lgna.croquet.history.TransactionHistory subTransactionHistory;
			if( this.isSubTransactionHistoryRequired() ) {
				subTransactionHistory = new org.lgna.croquet.history.TransactionHistory();
			} else {
				subTransactionHistory = null;
			}
			org.lgna.croquet.history.CompletionStep completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, trigger, subTransactionHistory );
			if( subTransactionHistory != null ) {
				this.addGeneratedSubTransactions( subTransactionHistory, edit );
			}
			if( edit != null ) {
				edit.setCompletionStep( completionStep );
			}
			completionStep.setEdit( edit );
			this.addGeneratedPostTransactions( ownerTransactionHistory, edit );
			return transaction;
		} finally {
			this.popGeneratedContexts( edit );
		}
	}
}
