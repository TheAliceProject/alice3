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

package org.lgna.croquet.dialog;

/**
 * @author Dennis Cosgrove
 */
public class DialogUtilities {
	public static final org.lgna.croquet.history.Step.Key<org.lgna.croquet.views.Dialog> DIALOG_KEY = org.lgna.croquet.history.Step.Key.createInstance( "DialogUtilities.DIALOG_KEY" );

	public static <V extends org.lgna.croquet.views.SwingComponentView<?>> void showDialog( final DialogOwner<V> dialogOwner, org.lgna.croquet.history.CompletionStep<?> step ) {
		org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
		org.lgna.croquet.DocumentFrame documentFrame = application.getDocumentFrame();

		org.lgna.croquet.views.AbstractWindow<?> window = application.peekWindow();

		org.lgna.croquet.views.ScreenElement owner;
		if( window != null ) {
			owner = window;
		} else {
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			org.lgna.croquet.views.ViewController<?, ?> viewController = trigger.getViewController();
			if( viewController != null ) {
				owner = viewController;
			} else {
				owner = documentFrame.getFrame().getContentPane();
			}
		}
		boolean isModal = dialogOwner.isModal();
		final org.lgna.croquet.views.Dialog dialog = new org.lgna.croquet.views.Dialog( owner, dialogOwner.isModal() );
		step.putEphemeralDataFor( DIALOG_KEY, dialog );
		class DialogWindowListener implements java.awt.event.WindowListener {
			@Override
			public void windowOpened( java.awt.event.WindowEvent e ) {
				dialogOwner.handleDialogOpened( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) );
			}

			@Override
			public void windowClosing( java.awt.event.WindowEvent e ) {
				if( dialogOwner.isWindowClosingEnabled( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) ) ) {
					dialog.setVisible( false );
				}
			}

			@Override
			public void windowClosed( java.awt.event.WindowEvent e ) {
				dialogOwner.handleDialogClosed( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) );
			}

			@Override
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}

			@Override
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}

			@Override
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}

			@Override
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
		}
		DialogWindowListener dialogWindowListener = new DialogWindowListener();
		dialog.addWindowListener( dialogWindowListener );
		V view = dialogOwner.allocateView( step );
		try {
			dialog.getAwtComponent().setContentPane( view.getAwtComponent() );
			dialogOwner.updateDialogSize( dialog );
			if( window != null ) {
				final int OFFSET = 32;
				java.awt.Point p = window.getLocation();
				dialog.setLocation( p.x + OFFSET, p.y + OFFSET );
				//dialog.getAwtComponent().setLocationRelativeTo( ownerDialog.getAwtComponent() );
			} else {
				java.awt.Point location = dialogOwner.getDesiredDialogLocation();
				if( location != null ) {
					dialog.setLocation( location );
				} else {
					edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog.getAwtComponent(), owner.getAwtComponent() );
				}
			}
			dialog.setTitle( dialogOwner.getDialogTitle( step ) );
			dialogOwner.handlePreShowDialog( step );
			//application.pushWindow( dialog );
			dialog.setVisible( true );

			if( isModal ) {
				dialogOwner.handlePostHideDialog( step );
				dialog.removeWindowListener( dialogWindowListener );
				dialogOwner.releaseView( step, view );
				dialog.getAwtComponent().dispose();
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle non-modal dialogs" );
			}
		} finally {
			if( isModal ) {
				//application.popWindow();
				dialogOwner.handleFinally( step, dialog );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle non-modal dialogs" );
			}
		}
	}
}
