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
public abstract class ValueCreatorInputDialogCoreComposite<V extends org.lgna.croquet.components.View<?,?>,T> extends InputDialogCoreComposite<V> {
	public static final class InternalFillInResolver<F> extends IndirectResolver< InternalFillIn<F>, ValueCreatorInputDialogCoreComposite<?,F> > {
		private InternalFillInResolver( ValueCreatorInputDialogCoreComposite<?,F> internal ) {
			super( internal );
		}
		public InternalFillInResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalFillIn<F> getDirect( ValueCreatorInputDialogCoreComposite<?,F> indirect ) {
			return indirect.fillIn;
		}
	}
	private static final class InternalFillIn<F> extends CascadeFillIn< F, Void > {
		private final ValueCreatorInputDialogCoreComposite<?,F> composite;
		private InternalFillIn( ValueCreatorInputDialogCoreComposite<?,F> composite ) {
			super( java.util.UUID.fromString( "258797f2-c1b6-4887-b6fc-42702493d573" ) );
			this.composite = composite;
		}
		public ValueCreatorInputDialogCoreComposite<?,F> getComposite() {
			return this.composite;
		}
		@Override
		protected Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.composite.getClass();
		}
		@Override
		protected InternalFillInResolver<F> createResolver() {
			return new InternalFillInResolver<F>( this.composite );
		}
		@Override
		protected String getTutorialItemText() {
			return this.composite.getDefaultLocalizedText();
		}
		@Override
		protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super F,Void > step ) {
			return new javax.swing.JLabel( this.getTutorialItemText() );
		}
		@Override
		public final F createValue( org.lgna.croquet.cascade.ItemNode< ? super F,Void > node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
			return this.composite.createValue( node, transactionHistory );
		}
		@Override
		public F getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super F,Void > node ) {
			return null;
		}
	}

	private InternalFillIn<T> fillIn = new InternalFillIn<T>( this );
	public ValueCreatorInputDialogCoreComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}
	
	public org.lgna.croquet.CascadeFillIn<T,Void> getFillIn() {
		return this.fillIn;
	}
	public T getPreviewValue() {
		return this.createValue();
	}
	protected abstract T createValue();
	private T createValue( org.lgna.croquet.cascade.ItemNode< ? super T,Void > node, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		throw new CancelException();
//		org.lgna.croquet.history.TransactionHistory transactionHistory = completionStep.getTransactionHistory();
//		assert transactionHistory != null : completionStep;
//		org.lgna.croquet.history.Transaction transaction = new org.lgna.croquet.history.Transaction( transactionHistory );
//		org.lgna.croquet.history.CompletionStep subCompletionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, null, new org.lgna.croquet.triggers.SimulatedTrigger(), new org.lgna.croquet.history.TransactionHistory() );
//		org.lgna.croquet.dialog.DialogUtilities.showDialog( new DialogOwner( this ), subCompletionStep );
//		return this.createValue();
	}
}
