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
	public static final class Resolver extends IndirectResolver<OwnedByCompositeOperation, OperationOwningComposite<?>> {
		private final String subKey;

		private Resolver( OperationOwningComposite<?> indirect, String subKey ) {
			super( indirect );
			this.subKey = subKey;
		}

		public Resolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
			this.subKey = binaryDecoder.decodeString();
		}

		@Override
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			super.encode( binaryEncoder );
			binaryEncoder.encode( this.subKey );
		}

		@Override
		protected OwnedByCompositeOperation getDirect( OperationOwningComposite<?> indirect ) {
			return indirect.getLaunchOperation( this.subKey );
		}
	}

	private final OperationOwningComposite composite;
	private final String subKey;

	public OwnedByCompositeOperation( Group group, OperationOwningComposite composite, String subKey ) {
		super( group, java.util.UUID.fromString( "c5afd59b-dd75-4ad5-b2ad-59bc9bd5c8ce" ) );
		this.composite = composite;
		this.subKey = subKey;
	}

	public OwnedByCompositeOperation( Group group, OperationOwningComposite composite ) {
		this( group, composite, null );
	}

	@Override
	protected void initialize() {
		this.composite.initializeIfNecessary();
		super.initialize();
	}

	@Override
	public boolean isToolBarTextClobbered() {
		return this.composite.isToolBarTextClobbered( super.isToolBarTextClobbered() );
	}

	public OperationOwningComposite getComposite() {
		return this.composite;
	}

	@Override
	protected Class<? extends AbstractElement> getClassUsedForLocalization() {
		//todo
		return ( (AbstractComposite<?>)this.composite ).getClassUsedForLocalization();
	}

	@Override
	protected String getSubKeyForLocalization() {
		return this.subKey;
	}

	//todo: pass subKey into composite methods

	@Override
	protected String modifyNameIfNecessary( String text ) {
		return this.composite.modifyNameIfNecessary( super.modifyNameIfNecessary( text ) );
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
	protected void pushGeneratedContexts( org.lgna.croquet.edits.AbstractEdit<?> edit ) {
		super.pushGeneratedContexts( edit );
		this.composite.pushGeneratedContexts( edit );
	}

	@Override
	protected void popGeneratedContexts( org.lgna.croquet.edits.AbstractEdit<?> edit ) {
		this.composite.popGeneratedContexts( edit );
		super.popGeneratedContexts( edit );
	}

	@Override
	protected void addGeneratedSubTransactions( org.lgna.croquet.history.TransactionHistory subTransactionHistory, org.lgna.croquet.edits.AbstractEdit<?> ownerEdit ) throws UnsupportedGenerationException {
		super.addGeneratedSubTransactions( subTransactionHistory, ownerEdit );
		this.composite.addGeneratedSubTransactions( subTransactionHistory, ownerEdit );
	}

	@Override
	protected void appendTutorialStepText( StringBuilder text, org.lgna.croquet.history.Step<?> step, org.lgna.croquet.edits.AbstractEdit<?> edit ) {
		this.composite.appendTutorialStepText( text, step, edit );
	}

	@Override
	protected Resolver createResolver() {
		return new Resolver( this.composite, this.subKey );
	}
}
