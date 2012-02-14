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
		protected org.lgna.croquet.history.TransactionHistory createTransactionHistoryIfNecessary() {
			return new org.lgna.croquet.history.TransactionHistory();
		}
		@Override
		protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
			org.lgna.croquet.history.CompletionStep<?> ancestor = step.getFirstAncestorStepOfModelAssignableTo( DialogOperation.class, org.lgna.croquet.history.CompletionStep.class );
			org.lgna.croquet.components.Dialog ownerDialog;
			if( ancestor != null ) {
				ownerDialog = ancestor.getEphemeralDataFor( DIALOG_KEY );
			} else {
				ownerDialog = null;
			}
			org.lgna.croquet.components.ScreenElement owner;
			if( ownerDialog != null ) {
				owner = ownerDialog;
			} else {
				org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
				org.lgna.croquet.components.ViewController< ?, ? > viewController = trigger.getViewController();
				if( viewController != null ) {
					owner = viewController;
				} else {
					owner = Application.getActiveInstance().getFrame().getContentPanel();
				}
			}
			final org.lgna.croquet.components.Dialog dialog = new org.lgna.croquet.components.Dialog( owner );
			step.putEphemeralDataFor( DIALOG_KEY, dialog );

			class DialogWindowListener implements java.awt.event.WindowListener {
				public void windowOpened( java.awt.event.WindowEvent e ) {
					composite.handleDialogOpened( new org.lgna.croquet.triggers.WindowEventTrigger( e ) );
				}
				public void windowClosing( java.awt.event.WindowEvent e ) {
					if( composite.isWindowClosingEnabled( e ) ) {
						dialog.setVisible( false );
					}
				}
				public void windowClosed( java.awt.event.WindowEvent e ) {
					composite.handleDialogClosed( new org.lgna.croquet.triggers.WindowEventTrigger( e ) );
				}
				public void windowActivated( java.awt.event.WindowEvent e ) {
				}
				public void windowDeactivated( java.awt.event.WindowEvent e ) {
				}
				public void windowDeiconified( java.awt.event.WindowEvent e ) {
				}
				public void windowIconified( java.awt.event.WindowEvent e ) {
				}
			}
			DialogWindowListener dialogWindowListener = new DialogWindowListener();
			dialog.addWindowListener( dialogWindowListener );
			V view = composite.getView();
			try {
				dialog.getAwtComponent().setContentPane( view.getAwtComponent() );
				dialog.pack();
				composite.modifyPackedDialogSizeIfDesired( dialog );
				if( ownerDialog != null ) {
					final int OFFSET = 32;
					java.awt.Point p = ownerDialog.getLocation();
					dialog.setLocation( p.x + OFFSET, p.y + OFFSET );
					//dialog.getAwtComponent().setLocationRelativeTo( ownerDialog.getAwtComponent() );
				} else {
					java.awt.Point location = composite.getDesiredDialogLocation();
					if( location != null ) {
						dialog.setLocation( location );
					} else {
						edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog.getAwtComponent(), Application.getActiveInstance().getFrame().getAwtComponent() );
					}
				}
				dialog.setTitle( composite.getDialogTitle( step ) );
				composite.handlePreShowDialog( step );
				dialog.setVisible( true );
				composite.handlePostHideDialog( step );
				//this.releaseContentPane( step, dialog, contentPane );
				dialog.removeWindowListener( dialogWindowListener );
				dialog.getAwtComponent().dispose();
			} finally {
				composite.handleFinally( step, dialog );
			}
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
	
	//todo: remove?
	protected boolean isWindowClosingEnabled( java.awt.event.WindowEvent e ) {
		return true;
	}
	protected String getDialogTitle( org.lgna.croquet.history.CompletionStep<?> step ) {
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
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
	}
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
	}
	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog ) {
	}
}
