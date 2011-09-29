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

import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.Container;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.components.ViewController;

/**
 * @author Dennis Cosgrove
 */
public abstract class DialogOperation< S extends org.lgna.croquet.history.DialogOperationStep<?> > extends SingleThreadOperation<S> {
	protected static final Group DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( java.util.UUID.fromString( "35b47d9d-d17b-4862-ac22-5ece4e317242" ), "DIALOG_IMPLEMENTATION_GROUP" );
	protected static final Group ENCLOSING_DIALOG_GROUP = Group.getInstance( java.util.UUID.fromString( "8dc8d3e5-9153-423e-bf1b-caa94597f57c" ), "ENCLOSING_DIALOG_GROUP" );
	public DialogOperation(Group group, java.util.UUID id) {
		super(group, id);
	}

	protected void modifyPackedDialogSizeIfDesired( Dialog dialog ) {
	}
	protected java.awt.Point getDesiredDialogLocation( Dialog dialog ) {
		return null;
	}
	protected void tweakDialog( Dialog dialog, S context ) {
	}
	
	private Dialog activeDialog;
	public Dialog getActiveDialog() {
		return this.activeDialog;
	}
	
	protected abstract Container<?> createContentPane(S context, Dialog dialog);
	protected abstract void releaseContentPane(S context, Dialog dialog, Container<?> contentPane );
	protected void handleFinally( S context, Dialog dialog, Container<?> contentPane ) {
	}
	
	protected String getDialogTitle(S context) {
		String rv = this.getName();
		if( rv != null ) {
			rv = rv.replaceAll( "<[a-z]*>", "" );
			rv = rv.replaceAll( "</[a-z]*>", "" );
			if( rv.endsWith( "..." ) ) {
				rv = rv.substring( 0, rv.length()-3 );
			}
		}
		return rv;
	}
	
	protected boolean isWindowClosingEnabled( java.awt.event.WindowEvent e ) {
		return true;
	}
	
	protected void handleClosing() {
	}
	
	@Override
	protected final void perform( final S step ) {
		org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
		ViewController<?,?> viewController = trigger.getViewController();
		Component<?> owner;
		if( viewController != null ) {
			owner = viewController;
		} else {
			owner = Application.getActiveInstance().getFrame().getContentPanel();
		}
		final Dialog dialog = new Dialog( owner );
//		dialog.getAwtComponent().setUndecorated( true );
//		dialog.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.PLAIN_DIALOG);

		dialog.setDefaultCloseOperation( org.lgna.croquet.components.Dialog.DefaultCloseOperation.DO_NOTHING );
		java.awt.event.WindowListener windowListener = new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
				org.lgna.croquet.history.TransactionManager.fireDialogOpened( dialog );
				step.handleWindowOpened( e );
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				if( DialogOperation.this.isWindowClosingEnabled( e ) ) {
					dialog.setVisible( false );
					step.handleWindowClosing( e );
				}
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
		};
		dialog.addWindowListener( windowListener );

		
		Container<?> contentPane = this.createContentPane(step, dialog);
		
		try {
			if( contentPane != null ) {
				dialog.getAwtComponent().setContentPane( contentPane.getAwtComponent() );
				dialog.pack();
				this.modifyPackedDialogSizeIfDesired( dialog );
				java.awt.Point location = this.getDesiredDialogLocation( dialog );
				if( location != null ) {
					dialog.setLocation( location );
				} else {
					edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog.getAwtComponent(), Application.getActiveInstance().getFrame().getAwtComponent() ); 
				}
				this.tweakDialog( dialog, step );
				
				dialog.setTitle( this.getDialogTitle(step) );
				this.activeDialog = dialog;
				try {
					dialog.setVisible( true );
					this.handleClosing();
					this.releaseContentPane( step, dialog, contentPane );
					dialog.removeWindowListener( windowListener );
					dialog.getAwtComponent().dispose();
				} finally {
					this.activeDialog = null;
				}
			} else {
				this.releaseContentPane( step, dialog, contentPane );
			}
		} finally {
			this.handleFinally( step, dialog, contentPane );
		}
	}
}
