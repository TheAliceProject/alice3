/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

import edu.cmu.cs.dennisc.java.awt.WindowUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.CompositeView;
import org.lgna.croquet.views.Dialog;
import org.lgna.croquet.views.ScreenElement;
import org.lgna.croquet.views.ViewController;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDialogComposite<V extends CompositeView<?, ?>> extends AbstractWindowComposite<V> {
	protected static final Group DIALOG_IMPLEMENTATION_GROUP = Group.getInstance( UUID.fromString( "4e436a8e-cfbc-447c-8c80-bc488d318f5b" ), "DIALOG_IMPLEMENTATION_GROUP" );

	protected static enum IsModal {
		TRUE( true ),
		FALSE( true );
		private IsModal( boolean value ) {
			this.value = value;
		}

		private final boolean value;
	}

	private final boolean isModal;
	private String title;
	protected Dialog dialog;
	protected UserActivity openingActivity;

	public AbstractDialogComposite( UUID migrationId, IsModal isModal ) {
		super( migrationId );
		this.isModal = isModal.value;
	}

	public UserActivity getOpeningActivity() {
		return openingActivity;
	}

	protected void showDialog( UserActivity userActivity ) {
		openingActivity = userActivity;
		Application<?> application = Application.getActiveInstance();
		DocumentFrame documentFrame = application.getDocumentFrame();

		AbstractWindow<?> window = documentFrame.peekWindow();

		ScreenElement owner;
		if( window != null ) {
			owner = window;
		} else {
//			Trigger trigger = completionStep.getTrigger();
			ViewController<?, ?> viewController = null; //trigger.getViewController();
			if( viewController != null ) {
				owner = viewController;
			} else {
				owner = documentFrame.getFrame().getContentPane();
			}
		}
		dialog = new Dialog( owner, this.isModal );
		class DialogWindowListener implements WindowListener {
			@Override
			public void windowOpened( WindowEvent e ) {
				handleDialogOpened();
			}

			@Override
			public void windowClosing( WindowEvent e ) {
				dialog.setVisible( false );
			}

			@Override
			public void windowClosed( WindowEvent e ) {
				handleDialogClosed();
			}

			@Override
			public void windowActivated( WindowEvent e ) {
			}

			@Override
			public void windowDeactivated( WindowEvent e ) {
			}

			@Override
			public void windowDeiconified( WindowEvent e ) {
			}

			@Override
			public void windowIconified( WindowEvent e ) {
			}
		}
		DialogWindowListener dialogWindowListener = new DialogWindowListener();
		dialog.addWindowListener( dialogWindowListener );
		CompositeView<?, ?> view = this.allocateView();
		try {
			dialog.getAwtComponent().setContentPane( view.getAwtComponent() );
			this.updateWindowSize( dialog );
			if( window != null ) {
				final int OFFSET = 32;
				Point p = window.getLocation();
				dialog.setLocation( p.x + OFFSET, p.y + OFFSET );
				//dialog.getAwtComponent().setLocationRelativeTo( ownerDialog.getAwtComponent() );
			} else {
				Point location = this.getDesiredWindowLocation();
				if( location != null ) {
					dialog.setLocation( location );
				} else {
					WindowUtilities.setLocationOnScreenToCenteredWithin( dialog.getAwtComponent(), owner.getAwtComponent() );
				}
			}
			dialog.setTitle( this.getDialogTitle() );
			handlePreShowDialog( dialog );
			//application.pushWindow( dialog );
			dialog.setVisible( true );

			if( isModal ) {
				this.handlePostHideDialog();
				dialog.removeWindowListener( dialogWindowListener );
				this.releaseView( view );
				dialog.getAwtComponent().dispose();
			} else {
				Logger.outln( "todo: handle non-modal dialogs" );
			}
		} finally {
			if( isModal ) {
				//application.popWindow();
				this.handleFinally( dialog );
			} else {
				Logger.outln( "todo: handle non-modal dialogs" );
			}
		}
	}

	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}

	//todo
	protected abstract CompositeView<?, ?> allocateView();

	protected abstract void releaseView( CompositeView<?, ?> view );

	protected abstract String getDefaultTitleText();

	protected String getDialogTitle() {
		this.initializeIfNecessary();
		String rv = this.title;
		if( rv != null ) {
			//pass
		} else {
			rv = this.getDefaultTitleText();
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

	private void handleDialogOpened() {
	}

	private void handleDialogClosed() {
	}

	protected boolean isDefaultButtonDesired() {
		return true;
	}

	protected abstract void handlePreShowDialog( Dialog dialog );

	protected abstract void handlePostHideDialog();

	protected void handleFinally( Dialog dialog ) {
	}
}
