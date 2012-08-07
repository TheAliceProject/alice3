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
public abstract class AbstractDialogComposite<V extends org.lgna.croquet.components.View<?,?>> extends AbstractWindowComposite<V>{
	protected static class DialogOwner implements org.lgna.croquet.dialog.DialogOwner<org.lgna.croquet.components.View<?,?>> {
		private final AbstractDialogComposite<?> composite;
		public DialogOwner( AbstractDialogComposite<?> composite ) {
			this.composite = composite;
		}
		public org.lgna.croquet.components.View<?,?> allocateView( org.lgna.croquet.history.CompletionStep<?> step ) {
			return this.composite.allocateView( step );
		}

		public void releaseView( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.View<?,?> view ) {
			this.composite.releaseView( step, view );
		}

		public String getDialogTitle( org.lgna.croquet.history.CompletionStep<?> step ) {
			return this.composite.getDialogTitle( step );
		}

		public java.awt.Point getDesiredDialogLocation() {
			return this.composite.getDesiredWindowLocation();
		}
		public void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
			this.composite.modifyPackedWindowSizeIfDesired( dialog );
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

		public void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
			this.composite.handlePreShowDialog( step );
		}

		public void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
			this.composite.handlePostHideDialog( step );
		}

		public void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog ) {
			this.composite.handleFinally( step, dialog );
		}
	}
	private String title;
	public AbstractDialogComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}
	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}
	//todo
	protected abstract org.lgna.croquet.components.View<?,?> allocateView( org.lgna.croquet.history.CompletionStep<?> step );
	protected abstract void releaseView( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.View<?,?> view );

	//todo: remove?
	protected final boolean isWindowClosingEnabled( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
		return true;
	}
	protected abstract String getName();
	protected String getDialogTitle( org.lgna.croquet.history.CompletionStep<?> step ) {
		this.initializeIfNecessary();
		String rv = this.title;
		if( rv != null ) {
			//pass
		} else {
			rv = this.getName();
			if( rv != null ) {
				rv = rv.replaceAll( "<[a-z]*>", "" );
				rv = rv.replaceAll( "</[a-z]*>", "" );
				if( rv.endsWith( "..." ) ) {
					rv = rv.substring( 0, rv.length() - 3 );
				}
			}
		}
		return rv;
	}
	
	private void handleDialogOpened( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
		//org.lgna.croquet.history.TransactionManager.fireDialogOpened( dialog );
	}
	private void handleDialogClosed( org.lgna.croquet.triggers.WindowEventTrigger trigger ) {
	}
	protected boolean isDefaultButtonDesired() {
		return true;
	}
	protected abstract void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step );
	protected abstract void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> step );
	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog ) {
	}
	
	public boolean isSubTransactionHistoryRequired() {
		return true;
	}
	public void pushGeneratedContexts( org.lgna.croquet.edits.Edit<?> ownerEdit ) {
	}
	public void popGeneratedContexts( org.lgna.croquet.edits.Edit<?> ownerEdit ) {
	}
}
