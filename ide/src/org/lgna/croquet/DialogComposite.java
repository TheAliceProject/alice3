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
public abstract class DialogComposite<V extends org.lgna.croquet.components.View<?,?>> extends Composite<V> {
	protected static final Group DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( java.util.UUID.fromString( "d4dfb949-2761-432e-9ad0-932ba0d6b4f6" ), "DIALOG_IMPLEMENTATION_GROUP" );

	protected static final org.lgna.croquet.history.Step.Key< org.lgna.croquet.components.Dialog > DIALOG_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DialogComposite.DIALOG_KEY" );

	
	private static class DialogOwner<V extends org.lgna.croquet.components.View<?,?>> implements org.lgna.croquet.dialog.DialogOwner<V> {

		private final DialogComposite<V> composite;
		public DialogOwner( DialogComposite<V> composite ) {
			this.composite = composite;
		}
		public V allocateView( org.lgna.croquet.history.Node<?> node ) {
			return this.composite.allocateView( node );
		}

		public void releaseView( org.lgna.croquet.history.Node<?> node, V view ) {
			this.composite.releaseView( node, view );
		}

		public String getDialogTitle( org.lgna.croquet.history.Node<?> node ) {
			return this.composite.getDialogTitle( node );
		}

		public java.awt.Point getDesiredDialogLocation() {
			return this.composite.getDesiredDialogLocation();
		}
		public void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
			this.composite.modifyPackedDialogSizeIfDesired( dialog );
		}

		public boolean isWindowClosingEnabled( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
			return this.composite.isWindowClosingEnabled( trigger );
		}

		public void handleDialogOpened( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
			this.composite.handleDialogOpened( trigger );
		}

		public void handleDialogClosed( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
			this.composite.handleDialogClosed( trigger );
		}

		public void handlePreShowDialog( org.lgna.croquet.history.Node<?> node ) {
			this.composite.handlePreShowDialog( node );
		}

		public void handlePostHideDialog( org.lgna.croquet.history.Node<?> node ) {
			this.composite.handlePostHideDialog( node );
		}

		public void handleFinally( org.lgna.croquet.history.Node<?> node, org.lgna.croquet.components.Dialog dialog ) {
			this.composite.handleFinally( node, dialog );
		}
		
	}
	
	public static final class InternalOperationResolver<V extends org.lgna.croquet.components.View<?,?>> extends IndirectResolver<InternalOperation<V>,DialogComposite<V>> {
		private InternalOperationResolver( DialogComposite<V> indirect ) {
			super( indirect );
		}
		public InternalOperationResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalOperation<V> getDirect( DialogComposite<V> indirect ) {
			return indirect.operation;
		}
	}

	
	
	private static final class InternalOperation<V extends org.lgna.croquet.components.View<?,?>> extends ActionOperation {
		private final DialogComposite<V> composite;

		private InternalOperation( Group group, DialogComposite<V> composite ) {
			super( group, java.util.UUID.fromString( "57aa20b4-0d4b-4cbf-82ae-191ee681aa6f" ) );
			this.composite = composite;
		}
		@Override
		protected Class< ? extends org.lgna.croquet.Element > getClassUsedForLocalization() {
			return this.composite.getClass();
		}
		@Override
		protected void initialize() {
			super.initialize();
			this.composite.initializeIfNecessary();
		}
		public DialogComposite<V> getComposite() {
			return this.composite;
		}
		@Override
		protected InternalOperationResolver<V> createResolver() {
			return new InternalOperationResolver<V>( this.composite );
		}
		
		@Override
		protected final void perform( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( this, trigger, new org.lgna.croquet.history.TransactionHistory() );
			org.lgna.croquet.dialog.DialogUtilities.showDialog( new DialogOwner<V>( this.composite ), step );
		}
	}

	private final InternalOperation<V> operation;

	public DialogComposite( java.util.UUID id, Group operationGroup ) {
		super( id );
		this.operation = new InternalOperation<V>( operationGroup, this );
	}
	public Operation getOperation() {
		return this.operation;
	}
	
	
	//todo
	private V allocateView( org.lgna.croquet.history.Node<?> node ) {
		return this.getView();
	}
	//todo
	private void releaseView( org.lgna.croquet.history.Node<?> node, V view ) {
	}
	
	
	//todo: remove?
	protected boolean isWindowClosingEnabled( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
		return true;
	}
	protected String getDialogTitle( org.lgna.croquet.history.Node<?> node ) {
//		String rv = this.getName();
//		if( rv != null ) {
//			rv = rv.replaceAll( "<[a-z]*>", "" );
//			rv = rv.replaceAll( "</[a-z]*>", "" );
//			if( rv.endsWith( "..." ) ) {
//				rv = rv.substring( 0, rv.length() - 3 );
//			}
//		}
//		return rv;
		return "";
	}
	protected void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
	}
	protected java.awt.Point getDesiredDialogLocation() {
		return null;
	}
	private void handleDialogOpened( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
		//org.lgna.croquet.history.TransactionManager.fireDialogOpened( dialog );
	}
	private void handleDialogClosed( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
	}
	protected void handlePreShowDialog( org.lgna.croquet.history.Node<?> node ) {
	}
	protected void handlePostHideDialog( org.lgna.croquet.history.Node<?> node ) {
	}
	protected void handleFinally( org.lgna.croquet.history.Node<?> node, org.lgna.croquet.components.Dialog dialog ) {
	}
}
