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
package org.lgna.croquet.views.imp;

/**
 * @author Dennis Cosgrove
 */
public class DropDownButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
	private static enum DropDownButtonUIDispatchState {
		DISPATCH_TO_BUTTON,
		DISPATCH_TO_ANCESTOR
	}

	public DropDownButtonUI( javax.swing.AbstractButton button ) {
		this.button = button;
	}

	@Override
	protected void installListeners( javax.swing.AbstractButton b ) {
		//note: do not call super
		//super.installListeners( b );
		b.addMouseListener( this.mouseListener );
		b.addMouseMotionListener( this.mouseMotionListener );
	}

	@Override
	protected void uninstallListeners( javax.swing.AbstractButton b ) {
		b.removeMouseMotionListener( this.mouseMotionListener );
		b.removeMouseListener( this.mouseListener );
		//note: do not call super
		//super.uninstallListeners( b );
	}

	private void dispatchToAncestor( java.awt.event.MouseEvent e ) {
		java.awt.EventQueue eventQueue = java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue();
		eventQueue.postEvent( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.convertMouseEvent( this.button, e, this.ancestor ) );
	}

	private void handleMouseEntered( java.awt.event.MouseEvent e ) {
		javax.swing.ButtonModel buttonModel = this.button.getModel();
		if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ) {
			//pass
		} else {
			if( this.button.isEnabled() ) {
				if( this.dispatchState != null ) {
					//pass
				} else {
					buttonModel.setRollover( true );
				}
			}
		}
	}

	private void handleMouseExited( java.awt.event.MouseEvent e ) {
		javax.swing.ButtonModel buttonModel = this.button.getModel();
		if( this.dispatchState != DropDownButtonUIDispatchState.DISPATCH_TO_ANCESTOR ) {
			buttonModel.setRollover( false );
		}
	}

	private void handleMousePressed( java.awt.event.MouseEvent e ) {
		javax.swing.ButtonModel buttonModel = this.button.getModel();
		//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( this.dispatchState, this );
		if( this.button.isEnabled() ) {
			buttonModel.setPressed( true );
			this.mousePressedEvent = e;
			this.dispatchState = DropDownButtonUIDispatchState.DISPATCH_TO_BUTTON;
		} else {
			this.mousePressedEvent = null;
			this.dispatchState = null;
		}
	}

	private void handleMouseReleased( java.awt.event.MouseEvent e ) {
		javax.swing.ButtonModel buttonModel = this.button.getModel();
		if( this.dispatchState == DropDownButtonUIDispatchState.DISPATCH_TO_BUTTON ) {
			buttonModel.setArmed( true );
			buttonModel.setPressed( false );
			buttonModel.setArmed( false );
		} else {
			if( this.dispatchState == DropDownButtonUIDispatchState.DISPATCH_TO_ANCESTOR ) {
				this.dispatchToAncestor( e );
			}
			buttonModel.setPressed( false );
		}
		buttonModel.setRollover( false );
		this.mousePressedEvent = null;
		this.dispatchState = null;
	}

	private static JDragView findDraggableJDragView( java.awt.Component awtComponent ) {
		JDragView jDragView = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.findFirstAncestor( awtComponent, false, JDragView.class );
		if( jDragView != null ) {
			if( jDragView.isActuallyDraggable() ) {
				return jDragView;
			} else {
				return findDraggableJDragView( jDragView );
			}
		} else {
			return null;
		}
	}

	private void handleMouseDragged( java.awt.event.MouseEvent e ) {
		javax.swing.ButtonModel buttonModel = this.button.getModel();
		if( this.dispatchState == DropDownButtonUIDispatchState.DISPATCH_TO_BUTTON ) {
			if( e.getComponent().contains( e.getPoint() ) ) {
				//pass
			} else {
				buttonModel.setPressed( false );
				buttonModel.setRollover( false );
				this.ancestor = findDraggableJDragView( this.button );
				if( this.ancestor != null ) {
					this.dispatchToAncestor( this.mousePressedEvent );
					this.dispatchToAncestor( e );
					this.dispatchState = DropDownButtonUIDispatchState.DISPATCH_TO_ANCESTOR;
				} else {
					this.dispatchState = null;
				}
			}
		} else if( this.dispatchState == DropDownButtonUIDispatchState.DISPATCH_TO_ANCESTOR ) {
			this.dispatchToAncestor( e );
		}
	}

	private DropDownButtonUIDispatchState dispatchState;
	private java.awt.event.MouseEvent mousePressedEvent;
	private JDragView ancestor;

	private final javax.swing.AbstractButton button;

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
			handleMouseEntered( e );
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
			handleMouseExited( e );
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			handleMousePressed( e );
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			handleMouseReleased( e );
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			handleMouseDragged( e );
		}
	};
}
