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

import org.lgna.croquet.components.Container;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.components.ViewController;

/**
 * @author Dennis Cosgrove
 */
public abstract class DialogOperation extends SingleThreadOperation {
	protected static final Group DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( java.util.UUID.fromString( "35b47d9d-d17b-4862-ac22-5ece4e317242" ), "DIALOG_IMPLEMENTATION_GROUP" );
	protected static final Group ENCLOSING_DIALOG_GROUP = Group.getInstance( java.util.UUID.fromString( "8dc8d3e5-9153-423e-bf1b-caa94597f57c" ), "ENCLOSING_DIALOG_GROUP" );

	public static final org.lgna.croquet.history.Step.Key< Dialog > DIALOG_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DialogOperation.DIALOG_KEY" );
	public static final org.lgna.croquet.history.Step.Key< Container< ? > > CONTENT_PANE_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DialogOperation.CONTENT_PANEL_KEY" );
	
	public DialogOperation( Group group, java.util.UUID id ) {
		super( group, id );
	}

	protected void modifyPackedDialogSizeIfDesired( Dialog dialog ) {
	}
	protected java.awt.Point getDesiredDialogLocation() {
		return null;
	}

	@Override
	protected org.lgna.croquet.history.TransactionHistory createTransactionHistoryIfNecessary() {
		return new org.lgna.croquet.history.TransactionHistory();
	}

	protected abstract Container< ? > createContentPane( org.lgna.croquet.history.CompletionStep<?> step, Dialog dialog );
	protected abstract void releaseContentPane( org.lgna.croquet.history.CompletionStep<?> step, Dialog dialog, Container< ? > contentPane );
	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, Dialog dialog, Container< ? > contentPane ) {
	}

	protected String getDialogTitle( org.lgna.croquet.history.CompletionStep<?> step ) {
		String rv = this.getName();
		if( rv != null ) {
			rv = rv.replaceAll( "<[a-z]*>", "" );
			rv = rv.replaceAll( "</[a-z]*>", "" );
			if( rv.endsWith( "..." ) ) {
				rv = rv.substring( 0, rv.length() - 3 );
			}
		}
		return rv;
	}

	protected boolean isClearedToClose( Dialog dialog ) {
		return true;
	}

	protected void handleClosing() {
	}

	@Override
	protected final void perform( final org.lgna.croquet.history.CompletionStep<?> step ) {
		org.lgna.croquet.history.CompletionStep<?> ancestor = step.getFirstAncestorStepOfModelAssignableTo( DialogOperation.class, org.lgna.croquet.history.CompletionStep.class );
		Dialog ownerDialog;
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
			ViewController< ?, ? > viewController = trigger.getViewController();
			if( viewController != null ) {
				owner = viewController;
			} else {
				owner = Application.getActiveInstance().getFrame().getContentPanel();
			}
		}
		final Dialog dialog = new Dialog( owner ) {
			@Override
			protected boolean isClearedToClose() {
				return DialogOperation.this.isClearedToClose( this );
			}
		};
		step.putEphemeralDataFor( DIALOG_KEY, dialog );
		//		dialog.getAwtComponent().setUndecorated( true );
		//		dialog.getRootPane().setWindowDecorationStyle(javax.swing.JRootPane.PLAIN_DIALOG);
		Container< ? > contentPane = this.createContentPane( step, dialog );
		step.putEphemeralDataFor( CONTENT_PANE_KEY, contentPane );
		
		try {
			if( contentPane != null ) {
				dialog.getAwtComponent().setContentPane( contentPane.getAwtComponent() );
				dialog.pack();
				this.modifyPackedDialogSizeIfDesired( dialog );
				if( ownerDialog != null ) {
					final int OFFSET = 32;
					java.awt.Point p = ownerDialog.getLocation();
					dialog.setLocation( p.x + OFFSET, p.y + OFFSET );
					//dialog.getAwtComponent().setLocationRelativeTo( ownerDialog.getAwtComponent() );
				} else {
					java.awt.Point location = this.getDesiredDialogLocation();
					if( location != null ) {
						dialog.setLocation( location );
					} else {
						edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog.getAwtComponent(), Application.getActiveInstance().getFrame().getAwtComponent() );
					}
				}

				dialog.setTitle( this.getDialogTitle( step ) );
				dialog.setVisible( true );
				this.handleClosing();
				this.releaseContentPane( step, dialog, contentPane );
				dialog.dispose();
			} else {
				this.releaseContentPane( step, dialog, contentPane );
			}
		} finally {
			this.handleFinally( step, dialog, contentPane );
		}
	}
}
