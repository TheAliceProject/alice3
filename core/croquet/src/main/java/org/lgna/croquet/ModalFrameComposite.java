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

import edu.cmu.cs.dennisc.java.util.Lists;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.dialog.LaunchOperationOwningCompositeImp;
import org.lgna.croquet.views.CompositeView;
import org.lgna.croquet.views.Frame;

import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class ModalFrameComposite<V extends CompositeView<?, ?>> extends AbstractWindowComposite<V> implements OperationOwningComposite<V> {
	public ModalFrameComposite( UUID id, Group launchOperationGroup ) {
		super( id );
		this.imp = new LaunchOperationOwningCompositeImp<ModalFrameComposite<V>>( this, launchOperationGroup );
	}

	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}

	protected LaunchOperationOwningCompositeImp<ModalFrameComposite<V>> getImp() {
		return this.imp;
	}

	public Operation getLaunchOperation( String subKeyText ) {
		return this.imp.getLaunchOperation( subKeyText );
	}

	@Override
	public String modifyNameIfNecessary( String text ) {
		return text;
	}

	protected void handlePreShowWindow( Frame frame ) {
	}

	protected void handlePostHideWindow( Frame frame ) {
	}

	protected void handleFinally() {
	}

	@Override
	public void perform( UserActivity userActivity ) {
		final List<Frame> framesToDisable = Lists.newLinkedList();

		Application application = Application.getActiveInstance();
		DocumentFrame documentFrame = application.getDocumentFrame();
		framesToDisable.add( documentFrame.getFrame() );

		final Frame frame = new Frame();
		class ModalFrameWindowListener implements WindowListener {
			@Override
			public void windowOpened( WindowEvent e ) {
				for( Frame frame : framesToDisable ) {
					frame.getAwtComponent().setEnabled( false );
				}
			}

			@Override
			public void windowClosing( WindowEvent e ) {
				if( isWindowClosingEnabled() ) {
					for( Frame frame : framesToDisable ) {
						frame.getAwtComponent().setEnabled( true );
					}
					//e.getComponent().setVisible( false );
					frame.release();
				}
			}

			@Override
			public void windowClosed( WindowEvent e ) {
				frame.removeWindowListener( this );
				try {
					userActivity.finish();
					handlePostHideWindow( frame );
				} finally {
					handleFinally();
				}
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

		ModalFrameWindowListener windowListener = new ModalFrameWindowListener();
		frame.setDefaultCloseOperation( Frame.DefaultCloseOperation.DO_NOTHING );
		frame.addWindowListener( windowListener );
		V view = this.getView();
		frame.getAwtComponent().setContentPane( view.getAwtComponent() );

		this.updateWindowSize( frame );
		final int OFFSET = 32;
		Point p = documentFrame.getFrame().getLocation();
		frame.setLocation( p.x + OFFSET, p.y + OFFSET );
		frame.setTitle( this.getModalFrameTitle() );
		this.handlePreShowWindow( frame );
		frame.setVisible( true );

		//			dialogOwner.handlePreShowDialog( userActivity );
		//			//application.pushWindow( dialog );
		//			dialog.setVisible( true );
		//
		//			if( isModal ) {
		//				dialogOwner.handlePostHideDialog( userActivity );
		//				dialog.removeWindowListener( dialogWindowListener );
		//				dialogOwner.releaseView( userActivity, view );
		//				dialog.getAwtComponent().dispose();
		//			} else {
		//				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle non-modal dialogs" );
		//			}
		//		} finally {
		//			if( isModal ) {
		//				//application.popWindow();
		//				dialogOwner.handleFinally( userActivity, dialog );
		//			} else {
		//				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "todo: handle non-modal dialogs" );
		//			}
		//		}
	}

	protected abstract String getName();

	protected String getModalFrameTitle() {
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

	protected boolean isWindowClosingEnabled() {
		return true;
	}

	private final LaunchOperationOwningCompositeImp<ModalFrameComposite<V>> imp;
	private String title;
}
