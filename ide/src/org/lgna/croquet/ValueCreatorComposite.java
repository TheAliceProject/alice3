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
public abstract class ValueCreatorComposite<V extends org.lgna.croquet.components.View<?,?>,T> extends Composite<V> {
	private static class DialogOwner<V extends org.lgna.croquet.components.View<?,?>,T> implements org.lgna.croquet.dialog.DialogOwner<V> {

		private final ValueCreatorComposite<V,T> composite;
		public DialogOwner( ValueCreatorComposite<V,T> composite ) {
			this.composite = composite;
		}
		public V allocateView( org.lgna.croquet.history.TransactionNode<?> step ) {
			return this.composite.allocateView( step );
		}

		public void releaseView( org.lgna.croquet.history.TransactionNode<?> step, V view ) {
			this.composite.releaseView( step, view );
		}

		public String getDialogTitle( org.lgna.croquet.history.TransactionNode<?> step ) {
			return this.composite.getDialogTitle( step );
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

		public void handlePreShowDialog( org.lgna.croquet.history.TransactionNode<?> step ) {
			this.composite.handlePreShowDialog( step );
		}

		public void handlePostHideDialog( org.lgna.croquet.history.TransactionNode<?> step ) {
			this.composite.handlePostHideDialog( step );
		}

		public void handleFinally( org.lgna.croquet.history.TransactionNode<?> step, org.lgna.croquet.components.Dialog dialog ) {
			this.composite.handleFinally( step, dialog );
		}
		
	}

	public static final class InternalFillInResolver<F> extends IndirectResolver< InternalFillIn<F>, ValueCreatorComposite<?,F> > {
		private InternalFillInResolver( ValueCreatorComposite<?,F> internal ) {
			super( internal );
		}
		public InternalFillInResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalFillIn<F> getDirect( ValueCreatorComposite<?,F> indirect ) {
			return indirect.getFillIn();
		}
	}
	private static final class InternalFillIn<F> extends CascadeFillIn< F, Void > {
		private final ValueCreatorComposite<?,F> composite;
		private InternalFillIn( ValueCreatorComposite<?,F> composite ) {
			super( java.util.UUID.fromString( "258797f2-c1b6-4887-b6fc-42702493d573" ) );
			this.composite = composite;
		}
		public ValueCreatorComposite<?,F> getComposite() {
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
		public final F createValue( org.lgna.croquet.cascade.ItemNode< ? super F,Void > node ) {
			return this.composite.createValue( node );
		}
		@Override
		public F getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super F,Void > node ) {
			return null;
		}
	}

	private InternalFillIn<T> fillIn;
	public ValueCreatorComposite( java.util.UUID id ) {
		super( id );
	}
	public synchronized InternalFillIn<T> getFillIn() {
		if( this.fillIn != null ) {
			//pass
		} else {
			this.fillIn = new InternalFillIn<T>( this );
		}
		return this.fillIn;
	}
	public T getPreviewValue() {
		return this.createValue();
	}
	protected abstract T createValue();
	private T createValue( org.lgna.croquet.cascade.ItemNode< ? super T,Void > node ) {
		org.lgna.croquet.dialog.DialogUtilities.showDialog( new DialogOwner<V,T>( this ), /*todo: remove cast*/(org.lgna.croquet.cascade.AbstractItemNode)node );
		return this.createValue();
	}

	//todo
	private V allocateView( org.lgna.croquet.history.TransactionNode<?> node ) {
		return this.getView();
	}
	//todo
	private void releaseView( org.lgna.croquet.history.TransactionNode<?> node, V view ) {
	}
	
	
	//todo: remove?
	protected boolean isWindowClosingEnabled( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
		return true;
	}
	protected String getDialogTitle( org.lgna.croquet.history.TransactionNode<?> node ) {
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
	protected void handlePreShowDialog( org.lgna.croquet.history.TransactionNode<?> node ) {
	}
	protected void handlePostHideDialog( org.lgna.croquet.history.TransactionNode<?> node ) {
	}
	protected void handleFinally( org.lgna.croquet.history.TransactionNode<?> node, org.lgna.croquet.components.Dialog dialog ) {
	}
}
