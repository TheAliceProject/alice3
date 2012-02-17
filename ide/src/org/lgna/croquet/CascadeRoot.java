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
public abstract class CascadeRoot<T, CS extends org.lgna.croquet.history.CompletionStep< ? >> extends CascadeBlankOwner< T[], T > {
	public static final class InternalPopupPrepModelResolver<T> extends IndirectResolver< InternalPopupPrepModel<T>, CascadeRoot<T,?> > {
		private InternalPopupPrepModelResolver( CascadeRoot<T,?> indirect ) {
			super( indirect );
		}
		public InternalPopupPrepModelResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalPopupPrepModel<T> getDirect( CascadeRoot<T,?> indirect ) {
			return indirect.getPopupPrepModel();
		}
	}

	public static final class InternalPopupPrepModel<T> extends PopupPrepModel {
		private final CascadeRoot< T,? > root;
		private InternalPopupPrepModel( CascadeRoot< T,? > root ) {
			super( java.util.UUID.fromString( "56116a5f-a081-4ce8-9626-9c515c6c5887" ) );
			this.root = root;
		}
		public CascadeRoot< T, ? > getCascadeRoot() {
			return this.root;
		}
		@Override
		protected InternalPopupPrepModelResolver<T> createResolver() {
			return new InternalPopupPrepModelResolver<T>( this.root );
		}
		@Override
		public Iterable< ? extends Model > getChildren() {
			return edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( this.root );
		}

		@Override
		protected Class< ? extends org.lgna.croquet.Model > getClassUsedForLocalization() {
			return this.root.getClassUsedForLocalization();
		}
		
		protected void handleFinally() {
			this.root.epilogue();
		}
		
		@Override
		protected org.lgna.croquet.history.Step< ? > perform( org.lgna.croquet.triggers.Trigger trigger ) {
			this.root.prologue();
			final org.lgna.croquet.cascade.RtRoot< T,? > rtRoot = new org.lgna.croquet.cascade.RtRoot( this.root );
			org.lgna.croquet.history.Step< ? > rv;
			if( rtRoot.isGoodToGo() ) {
				rv = rtRoot.complete( new org.lgna.croquet.triggers.AutomaticCompletionTrigger( trigger ) );
				this.handleFinally();
			} else {
				final org.lgna.croquet.history.PopupPrepStep prepStep = org.lgna.croquet.history.TransactionManager.addPopupPrepStep( this, trigger );			
				final org.lgna.croquet.components.PopupMenu popupMenu = new org.lgna.croquet.components.PopupMenu( this );
				popupMenu.addComponentListener( new java.awt.event.ComponentListener() {
					public void componentShown( java.awt.event.ComponentEvent e ) {
					}
					public void componentMoved( java.awt.event.ComponentEvent e ) {
					}
					public void componentResized( java.awt.event.ComponentEvent e ) {
						org.lgna.croquet.history.TransactionManager.firePopupMenuResized( prepStep );
					}
					public void componentHidden( java.awt.event.ComponentEvent e ) {
					}
				} );
				popupMenu.addPopupMenuListener( rtRoot.createPopupMenuListener( popupMenu ) );
				trigger.showPopupMenu( popupMenu );
				rv = prepStep;
			}
			return rv;
		}
	}
	private final InternalPopupPrepModel< T > popupPrepModel = new InternalPopupPrepModel< T >( this );

	public CascadeRoot( java.util.UUID id, CascadeBlank< T >[] blanks ) {
		super( id );
		if( blanks != null ) {
			for( int i = 0; i < blanks.length; i++ ) {
				assert blanks[ i ] != null : this;
				this.addBlank( blanks[ i ] );
			}
		}
	}
	public InternalPopupPrepModel< T > getPopupPrepModel() {
		return this.popupPrepModel;
	}
	@Override
	protected Class< ? extends org.lgna.croquet.Model > getClassUsedForLocalization() {
		return this.getCompletionModel().getClass();
	}
	@Override
	protected final javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super T[], T > step ) {
		return null;
	}
	@Override
	public final T[] createValue( org.lgna.croquet.cascade.ItemNode< ? super T[], T > step ) {
		//todo
		//this.cascade.getComponentType();
		//handled elsewhere for now
		throw new AssertionError();
	}
	@Override
	public final T[] getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super T[], T > step ) {
		//todo
		//this.cascade.getComponentType();
		//handled elsewhere for now
		throw new AssertionError();
	}
	@Override
	public final String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super T[], T > step ) {
		return this.getDefaultLocalizedText();
	}
	@Override
	public final javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super T[], T > step ) {
		return null;
	}

	public abstract CompletionModel getCompletionModel();
	public abstract Class< T > getComponentType();
	public abstract CS createCompletionStep( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger );
	protected abstract org.lgna.croquet.edits.Edit createEdit( CS completionStep, T[] values );

	public abstract void prologue();
	public abstract void epilogue();
	public final void handleCompletion( CS completionStep, T[] values ) {
		try {
			org.lgna.croquet.edits.Edit edit = this.createEdit( completionStep, values );
			if( edit != null ) {
				completionStep.commitAndInvokeDo( edit );
			} else {
				completionStep.cancel();
			}
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}
	public final void handleCancel( CS completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce ) {
		try {
			if( completionStep != null ) {
				completionStep.cancel();
			} else {
				org.lgna.croquet.history.TransactionManager.addCancelCompletionStep( this.getCompletionModel(), trigger );
			}
		} finally {
			this.getPopupPrepModel().handleFinally();
		}
	}
	//
	//	public abstract void handleCompletion( CS completionStep, T[] values );
	//	public abstract void handleCancel( CS completionStep, org.lgna.croquet.triggers.Trigger trigger, CancelException ce );
}
