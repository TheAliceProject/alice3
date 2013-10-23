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
public abstract class FrameComposite<V extends org.lgna.croquet.components.Panel> extends AbstractWindowComposite<V> {
	public static final class IsFrameShowingStateResolver extends IndirectResolver<BooleanState, FrameComposite> {
		private IsFrameShowingStateResolver( FrameComposite indirect ) {
			super( indirect );
		}

		public IsFrameShowingStateResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}

		@Override
		protected BooleanState getDirect( FrameComposite indirect ) {
			return indirect.isFrameShowingState;
		}
	}

	protected static final class IsFrameShowingState extends BooleanState {
		private final FrameComposite frameComposite;

		public IsFrameShowingState( Group group, FrameComposite frameComposite ) {
			super( group, java.util.UUID.fromString( "9afc0e33-5677-4e1f-a178-95d40f3e0b9c" ), false );
			this.frameComposite = frameComposite;
		}

		@Override
		protected Class<? extends AbstractElement> getClassUsedForLocalization() {
			return this.frameComposite.getClassUsedForLocalization();
		}

		public FrameComposite getFrameComposite() {
			return this.frameComposite;
		}

		@Override
		protected void fireChanged( Boolean prevValue, Boolean nextValue, IsAdjusting isAdjusting ) {
			super.fireChanged( prevValue, nextValue, isAdjusting );
			if( nextValue ) {
				org.lgna.croquet.components.Frame frameView = this.frameComposite.getOwnerFrameView_createIfNecessary();
				frameView.setTitle( this.frameComposite.getFrameTitle() );
				this.frameComposite.handlePreActivation();
				frameView.setVisible( true );
			} else {
				if( this.frameComposite.ownerFrameView != null ) {
					if( this.frameComposite.ownerFrameView.isVisible() ) {
						this.frameComposite.handlePostDeactivation();
						this.frameComposite.ownerFrameView.setVisible( false );
					}
				} else {
					//pass
				}
			}
		}
	}

	private org.lgna.croquet.components.Frame ownerFrameView;

	private final java.awt.event.WindowListener windowListener = new java.awt.event.WindowListener() {
		public void windowActivated( java.awt.event.WindowEvent e ) {
		}

		public void windowDeactivated( java.awt.event.WindowEvent e ) {
		}

		public void windowIconified( java.awt.event.WindowEvent e ) {
		}

		public void windowDeiconified( java.awt.event.WindowEvent e ) {
		}

		public void windowOpened( java.awt.event.WindowEvent e ) {
		}

		public void windowClosing( java.awt.event.WindowEvent e ) {
			handleWindowClosing( e );
		}

		public void windowClosed( java.awt.event.WindowEvent e ) {
		}
	};

	private org.lgna.croquet.components.Frame getOwnerFrameView_createIfNecessary() {
		if( this.ownerFrameView != null ) {
			//pass
		} else {
			this.ownerFrameView = new org.lgna.croquet.components.Frame();
			this.ownerFrameView.getContentPane().addCenterComponent( this.getView() );
			this.updateWindowSize( this.ownerFrameView );
			this.ownerFrameView.addWindowListener( this.windowListener );
		}
		return this.ownerFrameView;
	}

	private String title;
	private final IsFrameShowingState isFrameShowingState;

	public FrameComposite( java.util.UUID id, Group booleanStateGroup ) {
		super( id );
		this.isFrameShowingState = new IsFrameShowingState( booleanStateGroup, this );
	}

	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}

	public BooleanState getIsFrameShowingState() {
		return this.isFrameShowingState;
	}

	protected void handleWindowClosing( java.awt.event.WindowEvent e ) {
		this.isFrameShowingState.setValueTransactionlessly( false );
	}

	protected String getFrameTitle() {
		this.initializeIfNecessary();
		String rv = this.title;
		if( rv != null ) {
			//pass
		} else {
			rv = this.getIsFrameShowingState().getTrueText();
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
}
