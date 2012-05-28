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
	private final OperationOwningComposite composite;
	public OwnedByCompositeOperation( Group group, OperationOwningComposite composite ) {
		super( group, java.util.UUID.fromString( "c5afd59b-dd75-4ad5-b2ad-59bc9bd5c8ce" ) );
		this.composite = composite;
	}
	@Override
	protected java.lang.Class< ? extends org.lgna.croquet.Element > getClassUsedForLocalization() {
		return this.composite.getClass();
	}
	@Override
	protected void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.CompletionStep<OwnedByCompositeOperation> completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, trigger );
		try {
			org.lgna.croquet.edits.Edit edit = this.composite.createEdit( completionStep );
			if( edit != null ) {
				completionStep.commitAndInvokeDo( edit );
			} else {
				completionStep.finish();
			}
		} catch( CancelException ce ) {
			completionStep.cancel();
		}
	}
}

//public static final class InternalOperationResolver extends IndirectResolver<InternalOperation,DialogCoreComposite> {
//	private InternalOperationResolver( DialogCoreComposite indirect ) {
//		super( indirect );
//	}
//	public InternalOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
//		super( binaryDecoder );
//	}
//	@Override
//	protected InternalOperation getDirect( DialogCoreComposite indirect ) {
//		return (InternalOperation)indirect.getModel();
//	}
//}
//protected static final class InternalOperation extends ActionOperation {
//	private final DialogCoreComposite composite;
//
//	public InternalOperation( Group group, DialogCoreComposite composite ) {
//		super( group, java.util.UUID.fromString( "996e6478-a443-4f81-8976-61074d5c63b4" ) );
//		this.composite = composite;
//	}
//	@Override
//	protected void localize() {
//		//todo
//		//note: do not call super
//		this.setName( this.findLocalizedText( null, Composite.class ) );
//	}
//	@Override
//	protected Class< ? extends org.lgna.croquet.Element > getClassUsedForLocalization() {
//		return this.composite.getClassUsedForLocalization();
//	}
//	@Override
//	protected void initialize() {
//		super.initialize();
//		this.composite.initializeIfNecessary();
//	}
//	public DialogCoreComposite getComposite() {
//		return this.composite;
//	}
//	@Override
//	protected InternalOperationResolver createResolver() {
//		return new InternalOperationResolver( this.composite );
//	}
//	
//	@Override
//	protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
//		org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger, new org.lgna.croquet.history.TransactionHistory() );
//		org.lgna.croquet.dialog.DialogUtilities.showDialog( new DialogOwner( this.composite ), step );
//	}
//}
