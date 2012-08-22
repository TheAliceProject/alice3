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
public abstract class ValueCreator<T> extends AbstractCompletionModel {
	//todo: edits are never created by value creators.  allow group specification anyways?
	public static final org.lgna.croquet.Group VALUE_CREATOR_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "4bef663b-1474-40ec-9731-4e2a2cb49333" ), "VALUE_CREATOR_GROUP" );

	public static final class InternalFillInResolver<F> extends IndirectResolver<InternalFillIn<F>, ValueCreator<F>> {
		private InternalFillInResolver( ValueCreator<F> internal ) {
			super( internal );
		}

		public InternalFillInResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected InternalFillIn<F> getDirect( ValueCreator<F> indirect ) {
			return indirect.fillIn;
		}
	}

	private static final class InternalFillIn<F> extends CascadeFillIn<F, Void> {
		private final ValueCreator<F> valueCreator;
		private String text;

		private InternalFillIn( ValueCreator<F> valueCreator ) {
			super( java.util.UUID.fromString( "258797f2-c1b6-4887-b6fc-42702493d573" ) );
			this.valueCreator = valueCreator;
		}

		@Override
		protected void localize() {
			super.localize();
			this.text = this.findDefaultLocalizedText();
		}

		public ValueCreator<F> getValueCreator() {
			return this.valueCreator;
		}

		@Override
		protected Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.valueCreator.getClassUsedForLocalization();
		}

		@Override
		protected InternalFillInResolver<F> createResolver() {
			return new InternalFillInResolver<F>( this.valueCreator );
		}

		@Override
		protected String getTutorialItemText() {
			this.initializeIfNecessary();
			return this.text;
		}

		@Override
		protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode<? super F, Void> step ) {
			return new javax.swing.JLabel( this.text );
		}

		@Override
		public final F createValue( org.lgna.croquet.cascade.ItemNode<? super F, Void> node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
			org.lgna.croquet.triggers.Trigger trigger = new org.lgna.croquet.triggers.NullTrigger( org.lgna.croquet.triggers.Trigger.Origin.USER );
			org.lgna.croquet.history.Step<?> step = this.valueCreator.fire( trigger );
			if( step != null ) {
				return (F)step.getEphemeralDataFor( VALUE_KEY );
			} else {
				throw new CancelException();
			}
		}

		@Override
		public F getTransientValue( org.lgna.croquet.cascade.ItemNode<? super F, Void> node ) {
			return null;
		}
	}

	private InternalFillIn<T> fillIn = new InternalFillIn<T>( this );
	public static final org.lgna.croquet.history.Step.Key<Object> VALUE_KEY = org.lgna.croquet.history.Step.Key.createInstance( "ValueCreator.VALUE_KEY" );

	public ValueCreator( java.util.UUID migrationId ) {
		super( VALUE_CREATOR_GROUP, migrationId );
	}

	@Override
	protected void localize() {
	}

	@Override
	public java.lang.Iterable<? extends org.lgna.croquet.PrepModel> getPotentialRootPrepModels() {
		return java.util.Collections.emptyList();
	}

	@Override
	public boolean isAlreadyInState( org.lgna.croquet.edits.Edit<?> edit ) {
		return false;
	}

	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.Edit<?> edit ) {
		return rv;
	}

	public CascadeFillIn<T, Void> getFillIn() {
		return this.fillIn;
	}

	protected abstract T createValue( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger );

	@Override
	public org.lgna.croquet.history.Step<?> fire( org.lgna.croquet.triggers.Trigger trigger ) {
		this.initializeIfNecessary();
		org.lgna.croquet.history.Transaction transaction = org.alice.ide.IDE.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory().acquireActiveTransaction();
		T value = this.createValue( transaction, trigger );
		org.lgna.croquet.history.CompletionStep<?> rv = transaction.getCompletionStep();
		if( rv != null ) {
			rv.putEphemeralDataFor( VALUE_KEY, value );
		}
		return rv;
	}
}
