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
package org.lgna.croquet.imp.frame;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractIsFrameShowingState extends org.lgna.croquet.BooleanState {
	public AbstractIsFrameShowingState( org.lgna.croquet.Group group, java.util.UUID migrationId ) {
		super( group, migrationId, false );
	}

	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}

	@Override
	protected abstract Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization();

	public abstract org.lgna.croquet.FrameComposite<?> getFrameComposite();

	private String getFrameTitle() {
		this.initializeIfNecessary();
		String rv = this.title;
		if( rv != null ) {
			//pass
		} else {
			rv = this.getTrueText();
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

	@Override
	protected void fireChanged( Boolean prevValue, Boolean nextValue, IsAdjusting isAdjusting ) {
		super.fireChanged( prevValue, nextValue, isAdjusting );
		if( nextValue ) {
			org.lgna.croquet.views.Frame frameView = this.getOwnerFrameView_createIfNecessary();
			frameView.setTitle( this.getFrameTitle() );
			this.getFrameComposite().handlePreActivation();
			frameView.setVisible( true );
		} else {
			if( this.ownerFrameView != null ) {
				if( this.ownerFrameView.isVisible() ) {
					this.getFrameComposite().handlePostDeactivation();
					this.ownerFrameView.setVisible( false );
				}
			} else {
				//pass
			}
		}
	}

	private org.lgna.croquet.views.Frame getOwnerFrameView_createIfNecessary() {
		if( this.ownerFrameView != null ) {
			//pass
		} else {
			org.lgna.croquet.FrameComposite<?> frameComposite = this.getFrameComposite();
			this.ownerFrameView = new org.lgna.croquet.views.Frame();
			this.ownerFrameView.getContentPane().addCenterComponent( frameComposite.getRootComponent() );
			frameComposite.updateWindowSize( this.ownerFrameView );
			this.ownerFrameView.addWindowListener( this.windowListener );
		}
		return this.ownerFrameView;
	}

	private void handleWindowClosing( java.awt.event.WindowEvent e ) {
		this.setValueTransactionlessly( false );
	}

	private final java.awt.event.WindowListener windowListener = new java.awt.event.WindowListener() {
		@Override
		public void windowActivated( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowDeactivated( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowIconified( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowDeiconified( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowOpened( java.awt.event.WindowEvent e ) {
		}

		@Override
		public void windowClosing( java.awt.event.WindowEvent e ) {
			handleWindowClosing( e );
		}

		@Override
		public void windowClosed( java.awt.event.WindowEvent e ) {
		}
	};

	private String title;
	private org.lgna.croquet.views.Frame ownerFrameView;

}
