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

/*package-private*/ class IsShowingButtonModel extends javax.swing.JToggleButton.ToggleButtonModel {
	private final FrameComposite<?> frameComposite;
	private org.lgna.croquet.components.Frame frame;
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
			frameComposite.handlePreActivation();
		}
		public void windowClosing( java.awt.event.WindowEvent e ) {
			frameComposite.handlePostDeactivation();
			IsShowingButtonModel.this.setSelected( false );
		}
		public void windowClosed( java.awt.event.WindowEvent e ) {
		}
	};
	
	public IsShowingButtonModel( FrameComposite<?> frameComposite ) {
		this.frameComposite = frameComposite;
	}
	
	private org.lgna.croquet.components.Frame getFrame() {
		if( this.frame != null ) {
			//pass
		} else {
			this.frame = new org.lgna.croquet.components.Frame();
			this.frame.getContentPanel().addCenterComponent( this.frameComposite.getView() );
			this.frame.setTitle( this.frameComposite.getFrameTitle() );
			this.frame.pack();
			this.frameComposite.modifyPackedWindowSizeIfDesired( this.frame );
			this.frame.addWindowListener( this.windowListener );
		}
		return this.frame;
	}
	@Override
	public void setSelected( boolean b ) {
		super.setSelected( b );
		if( b ) {
			org.lgna.croquet.components.Frame frame = this.getFrame();
			frame.setVisible( true );
		} else {
			if( this.frame != null ) {
				this.frame.setVisible( false );
			} else {
				//pass
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class FrameComposite<V extends org.lgna.croquet.components.View<?,?>> extends AbstractWindowComposite<V> {
	public static final class InternalBooleanStateResolver extends IndirectResolver< InternalBooleanState, FrameComposite > {
		private InternalBooleanStateResolver( FrameComposite indirect ) {
			super( indirect );
		}
		public InternalBooleanStateResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			super( binaryDecoder );
		}
		@Override
		protected InternalBooleanState getDirect( FrameComposite indirect ) {
			return indirect.booleanState;
		}
	}

	private static final class InternalBooleanState extends BooleanState {
		private final FrameComposite frameComposite;
		public InternalBooleanState( Group group, FrameComposite frameComposite ) {
			super( group, java.util.UUID.fromString( "9afc0e33-5677-4e1f-a178-95d40f3e0b9c" ), false, new IsShowingButtonModel( frameComposite ) );
			this.frameComposite = frameComposite;
		}@Override
		protected Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return this.frameComposite.getClassUsedForLocalization();
		}
		public FrameComposite getFrameComposite() {
			return this.frameComposite;
		}
	}

	private final InternalBooleanState booleanState;
	private String title;
	public FrameComposite( java.util.UUID id, Group booleanStateGroup ) {
		super( id );
		this.booleanState = new InternalBooleanState( booleanStateGroup, this );
	}
	public BooleanState getBooleanState() {
		return this.booleanState;
	}
	@Override
	protected void localize() {
		super.localize();
		this.title = this.findLocalizedText( "title" );
	}
	protected String getFrameTitle() {
		this.initializeIfNecessary();
		String rv = this.title;
		if( rv != null ) {
			//pass
		} else {
			rv = this.booleanState.getTrueText();
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
