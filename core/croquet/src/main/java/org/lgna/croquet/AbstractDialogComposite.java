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
public abstract class AbstractDialogComposite<V extends org.lgna.croquet.views.CompositeView<?, ?>> extends AbstractWindowComposite<V> {
	protected static final Group DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( java.util.UUID.fromString( "4e436a8e-cfbc-447c-8c80-bc488d318f5b" ), "DIALOG_IMPLEMENTATION_GROUP" );
	protected static final org.lgna.croquet.history.Step.Key<org.lgna.croquet.views.Dialog> DIALOG_KEY = org.lgna.croquet.history.Step.Key.createInstance( "AbstractDialogComposite.DIALOG_KEY" );

	private final boolean isModal;
	private String title;

	public AbstractDialogComposite( java.util.UUID migrationId, boolean isModal ) {
		super( migrationId );
		this.isModal = isModal;
	}

	protected void showDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
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
		final org.lgna.croquet.views.Dialog dialog = new org.lgna.croquet.views.Dialog( owner, this.isModal );
		step.putEphemeralDataFor( DIALOG_KEY, dialog );
		class DialogWindowListener implements java.awt.event.WindowListener {
			@Override
			public void windowOpened( java.awt.event.WindowEvent e ) {
				handleDialogOpened( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) );
			}

			@Override
			public void windowClosing( java.awt.event.WindowEvent e ) {
				if( isWindowClosingEnabled( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) ) ) {
					dialog.setVisible( false );
				}
			}

			@Override
			public void windowClosed( java.awt.event.WindowEvent e ) {
				handleDialogClosed( org.lgna.croquet.triggers.WindowEventTrigger.createUserInstance( e ) );
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
		org.lgna.croquet.views.CompositeView<?, ?> view = this.allocateView( step );
		try {
			dialog.getAwtComponent().setContentPane( view.getAwtComponent() );
			this.updateWindowSize( dialog );
			if( window != null ) {
				final int OFFSET = 32;
				java.awt.Point p = window.getLocation();
				dialog.setLocation( p.x + OFFSET, p.y + OFFSET );
				//dialog.getAwtComponent().setLocationRelativeTo( ownerDialog.getAwtComponent() );
			} else {
				java.awt.Point location = this.getDesiredWindowLocation();
				if( location != null ) {
					dialog.setLocation( location );
				} else {
					edu.cmu.cs.dennisc.java.awt.WindowUtilities.setLocationOnScreenToCenteredWithin( dialog.getAwtComponent(), owner.getAwtComponent() );
				}
			}
			dialog.setTitle( this.getDialogTitle( step ) );
			this.handlePreShowDialog( step );
			//application.pushWindow( dialog );
			dialog.setVisible( true );

			if( isModal ) {
				this.handlePostHideDialog( step );
				dialog.removeWindowListener( dialogWindowListener );
				this.releaseView( step, view );
				dialog.getAwtComponent().dispose();
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle non-modal dialogs" );
			}
		} finally {
			if( isModal ) {
				//application.popWindow();
				this.handleFinally( step, dialog );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle non-modal dialogs" );
			}
		}
	}

	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}

	//todo
	protected abstract org.lgna.croquet.views.CompositeView<?, ?> allocateView( org.lgna.croquet.history.CompletionStep<?> step );

	protected abstract void releaseView( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.views.CompositeView<?, ?> view );

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

	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.views.Dialog dialog ) {
	}

	public boolean isSubTransactionHistoryRequired() {
		return true;
	}
}
