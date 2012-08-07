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
public final class OwnedByCompositeOperation extends ActionOperation {
	public static final class Resolver extends IndirectResolver< OwnedByCompositeOperation, OperationOwningComposite<?> > {
		private Resolver( OperationOwningComposite<?> indirect ) {
			super( indirect );
		}
		public Resolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected OwnedByCompositeOperation getDirect( OperationOwningComposite<?> indirect ) {
			return indirect.getOperation();
		}
	}

	private final OperationOwningComposite composite;

	public OwnedByCompositeOperation( Group group, OperationOwningComposite composite ) {
		super( group, java.util.UUID.fromString( "c5afd59b-dd75-4ad5-b2ad-59bc9bd5c8ce" ) );
		this.composite = composite;
	}
	public OperationOwningComposite getComposite() {
		return this.composite;
	}
	@Override
	protected java.lang.Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
		return this.composite.getClass();
	}
	@Override
	protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<OwnedByCompositeOperation> completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, trigger, new org.lgna.croquet.history.TransactionHistory() );
		this.composite.perform( completionStep );
	}

	@Override
	protected boolean isSubTransactionHistoryRequired() {
		return this.composite.isSubTransactionHistoryRequired();
	}
	@Override
	protected void pushGeneratedContexts( org.lgna.croquet.edits.Edit<?> edit ) {
		super.pushGeneratedContexts( edit );
		this.composite.pushGeneratedContexts( edit );
	}
	@Override
	protected void popGeneratedContexts( org.lgna.croquet.edits.Edit<?> edit ) {
		this.composite.popGeneratedContexts( edit );
		super.popGeneratedContexts( edit );
	}
	@Override
	protected void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.Edit<?> ownerEdit ) {
		super.addGeneratedSubTransactions( subTransactionHistory, ownerEdit );
		this.composite.addGeneratedSubTransactions( subTransactionHistory, ownerEdit );
	}
	@Override
	protected void addGeneratedPostTransactions( org.lgna.croquet.history.TransactionHistory ownerTransactionHistory, org.lgna.croquet.edits.Edit<?> edit ) {
		super.addGeneratedPostTransactions( ownerTransactionHistory, edit );
		this.composite.addGeneratedPostTransactions( ownerTransactionHistory, edit );
	}
	@Override
	protected Resolver createResolver() {
		return new Resolver( this.composite );
	}
}
