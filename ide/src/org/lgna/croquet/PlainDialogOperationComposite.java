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
public abstract class PlainDialogOperationComposite<V extends org.lgna.croquet.components.View<?, ?>> extends AbstractDialogComposite<V> implements OperationOwningComposite<V> {
	public static final class InternalCloseOperationResolver extends IndirectResolver<InternalCloseOperation, PlainDialogOperationComposite> {
		private InternalCloseOperationResolver( PlainDialogOperationComposite indirect ) {
			super( indirect );
		}

		public InternalCloseOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected InternalCloseOperation getDirect( PlainDialogOperationComposite indirect ) {
			return indirect.getCloseOperation();
		}
	}

	public static class InternalCloseOperation extends Operation {
		private final PlainDialogOperationComposite plainDialogOperationComposite;

		private InternalCloseOperation( PlainDialogOperationComposite plainDialogOperationComposite ) {
			super( DIALOG_IMPLEMENTATION_GROUP, java.util.UUID.fromString( "2a116435-9536-4590-8294-c4050ea65a4e" ) );
			assert plainDialogOperationComposite != null;
			this.plainDialogOperationComposite = plainDialogOperationComposite;
		}

		@Override
		protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit ) {
			rv.append( "Press the <strong>Close</strong> button when you are ready." );
			return rv;
		}

		public PlainDialogOperationComposite getPlainDialogOperationComposite() {
			return this.plainDialogOperationComposite;
		}

		@Override
		protected InternalCloseOperationResolver createResolver() {
			return new InternalCloseOperationResolver( this.plainDialogOperationComposite );
		}

		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger );
			step.finish();
		}
	}

	private InternalCloseOperation closeOperation = new InternalCloseOperation( this );

	private final OwnedByCompositeOperation operation;

	public PlainDialogOperationComposite( java.util.UUID migrationId, Group operationGroup, boolean isModal ) {
		super( migrationId, isModal );
		this.operation = new OwnedByCompositeOperation( operationGroup, this );
	}

	public PlainDialogOperationComposite( java.util.UUID migrationId, Group operationGroup ) {
		this( migrationId, operationGroup, true );
	}

	public boolean isToolBarTextClobbered( boolean defaultValue ) {
		return defaultValue;
	}

	public OwnedByCompositeOperation getOperation() {
		return this.operation;
	}

	public InternalCloseOperation getCloseOperation() {
		return this.closeOperation;
	}

	@Override
	protected String getName() {
		return this.getOperation().getName();
	}

	public void clobberLocalizationIfDesired( OwnedByCompositeOperation operation ) {
	}

	@Override
	protected org.lgna.croquet.components.View<?, ?> allocateView( org.lgna.croquet.history.CompletionStep<?> step ) {
		//todo
		return this.getView();
	}

	@Override
	protected void releaseView( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.View<?, ?> view ) {
		super.releaseView();
		//todo
	}

	@Override
	protected GoldenRatioPolicy getGoldenRatioPolicy() {
		//todo
		return null;
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.handlePreActivation();
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.handlePostDeactivation();
	}

	public void perform( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		org.lgna.croquet.dialog.DialogUtilities.showDialog( new DialogOwner( this ) {
			@Override
			public void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
				super.handlePostHideDialog( completionStep );
				completionStep.finish();
			}
		}, completionStep );
	}

	public void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.Edit<?> ownerEdit ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "todo: generate close transaction" );
	}

	public void addGeneratedPostTransactions( org.lgna.croquet.history.TransactionHistory ownerTransactionHistory, org.lgna.croquet.edits.Edit<?> edit ) {
	}
}
