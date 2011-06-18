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
public final class CascadePopupPrepModel<B> extends PopupPrepModel {
	private final Cascade<B> cascade;
	/*package-private*/ CascadePopupPrepModel( Cascade< B > cascade ) {
		super( java.util.UUID.fromString( "56116a5f-a081-4ce8-9626-9c515c6c5887" ) );
		this.cascade = cascade;
	}
	@Override
	public Iterable< ? extends Model > getChildren() {
		return edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( this.cascade.getRoot() );
	}

	@Override
	protected Class< ? extends org.lgna.croquet.Model > getClassUsedForLocalization() {
		return this.cascade.getClassUsedForLocalization();
	}
	
	@Override
	protected void localize() {
		String name = this.getDefaultLocalizedText();
		if( name != null ) {
			this.setName( name );
			this.setMnemonicKey( this.getLocalizedMnemonicKey() );
			this.setAcceleratorKey( this.getLocalizedAcceleratorKeyStroke() );
		}
	}
	public Cascade< B > getCompletionModel() {
		return this.cascade;
	}
	protected void handleFinally( PerformObserver performObserver ) {
		performObserver.handleFinally();
	}

	public void handleCompletion( org.lgna.croquet.history.CascadePopupOperationStep< B > step, PerformObserver performObserver, B[] values ) {
		try {
			org.lgna.croquet.edits.Edit< ? extends Cascade< B > > edit = this.cascade.createEdit( step, values );
			step.commitAndInvokeDo( edit );
		} finally {
			this.handleFinally( performObserver );
		}
	}
	public void handleCancel( PerformObserver performObserver, org.lgna.croquet.history.CascadePopupOperationStep< B > completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		try {
			if( completionStep != null ) {
				completionStep.cancel();
			} else {
				org.lgna.croquet.history.TransactionManager.addCancelCompletionStep( this.cascade, trigger );
			}
		} finally {
			this.handleFinally( performObserver );
		}
	}
	
	@Override
	protected org.lgna.croquet.history.Step< ? > perform( org.lgna.croquet.triggers.Trigger trigger, org.lgna.croquet.PopupPrepModel.PerformObserver performObserver ) {
		org.lgna.croquet.cascade.RtCascadePopupPrepModel< B > rt = new org.lgna.croquet.cascade.RtCascadePopupPrepModel< B >( this, performObserver );
		return rt.perform( trigger );
	}
}
