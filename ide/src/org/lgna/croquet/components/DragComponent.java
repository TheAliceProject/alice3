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
package org.lgna.croquet.components;

public abstract class DragComponent<M extends org.lgna.croquet.DragModel> extends ViewController<javax.swing.JPanel, M> {
	private DragProxy dragProxy = null;
	private DropProxy dropProxy = null;

	private class ControlAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		public void mousePressed( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMousePressed( e );
		}

		public void mouseReleased( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMouseReleased( e );
		}

		public void mouseClicked( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMouseClicked( e );
		}

		public void mouseEntered( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMouseEntered( e );
		}

		public void mouseExited( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMouseExited( e );
		}

		public void mouseMoved( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMouseMoved( e );
		}

		public void mouseDragged( java.awt.event.MouseEvent e ) {
			DragComponent.this.handleMouseDragged( e );
		}
	}

	private ControlAdapter controlAdapter = null;

	private float clickThreshold = 5.0f;

	public float getClickThreshold() {
		return this.clickThreshold;
	}

	public void setClickThreshold( float clickThreshold ) {
		this.clickThreshold = clickThreshold;
	}

	private boolean isWithinClickThreshold = false;

	protected boolean isWithinClickThreshold() {
		return this.isWithinClickThreshold;
	}

	private java.awt.event.MouseEvent mousePressedEvent = null;
	private java.awt.event.MouseEvent leftButtonPressedEvent = null;

	private boolean isActive;

	public boolean isPressed() {
		return false;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public java.awt.event.MouseEvent getLeftButtonPressedEvent() {
		return this.leftButtonPressedEvent;
	}

	private final java.awt.event.ComponentListener componentAdapter = new java.awt.event.ComponentListener() {
		public void componentHidden( java.awt.event.ComponentEvent arg0 ) {
		}

		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}

		public void componentResized( java.awt.event.ComponentEvent e ) {
			DragComponent.this.updateProxySizes();
		}

		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
	};

	public DragComponent( M model ) {
		super( model );
	}

	public DragProxy getDragProxy() {
		return this.dragProxy;
	}

	public DropProxy getDropProxy() {
		return this.dropProxy;
	}

	public java.awt.Dimension getDropProxySize() {
		return this.dropProxy.getSize();
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component<?> parent ) {
		super.handleAddedTo( parent );
		if( isMouseListeningDesired() ) {
			if( this.controlAdapter != null ) {
				//pass
			} else {
				this.controlAdapter = new ControlAdapter();
				this.addMouseListener( this.controlAdapter );
				this.addMouseMotionListener( this.controlAdapter );
			}
		}
		this.addComponentListener( this.componentAdapter );
	}

	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component<?> parent ) {
		if( this.controlAdapter != null ) {
			this.removeMouseListener( this.controlAdapter );
			this.removeMouseMotionListener( this.controlAdapter );
			this.controlAdapter = null;
			//			
			//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "REMOVE NOTIFY: ", this.getClass() );
		}
		this.removeComponentListener( this.componentAdapter );
		super.handleRemovedFrom( parent );
	}

	protected boolean isMouseListeningDesired() {
		org.lgna.croquet.DragModel dragModel = this.getModel();
		return dragModel != null;
	}

	public JComponent<?> getSubject() {
		return this;
	}

	protected boolean isAlphaDesiredWhenOverDropReceptor() {
		return true;
	}

	private boolean isActuallyPotentiallyDraggable() {
		org.lgna.croquet.DragModel dragModel = this.getModel();
		boolean rv = dragModel != null;
		if( rv ) {
			if( this.dragProxy != null ) {
				//pass
			} else {
				this.dragProxy = new DragProxy( this, this.isAlphaDesiredWhenOverDropReceptor() );
			}
			if( this.dropProxy != null ) {
				//pass
			} else {
				this.dropProxy = new DropProxy( this );
			}
		}
		return rv;
	}

	protected javax.swing.JLayeredPane getLayeredPane() {
		AbstractWindow<?> root = this.getRoot();
		if( root != null ) {
			javax.swing.JRootPane rootPane = root.getJRootPane();
			if( rootPane != null ) {
				return rootPane.getLayeredPane();
			} else {
				//throw new RuntimeException( "cannot find rootPane: " + this );
				return null;
			}
		} else {
			return null;
		}
	}

	private void updateProxySizes() {
		if( isActuallyPotentiallyDraggable() ) {
			dragProxy.setSize( dragProxy.getProxySize() );
			dropProxy.setSize( dropProxy.getProxySize() );
		}
	}

	private synchronized void updateProxyPosition( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			java.awt.event.MouseEvent mousePressedEvent = this.getLeftButtonPressedEvent();
			if( mousePressedEvent != null ) {
				javax.swing.JLayeredPane layeredPane = getLayeredPane();
				java.awt.Point locationOnScreenLayeredPane = layeredPane.getLocationOnScreen();
				java.awt.Point locationOnScreen = this.getLocationOnScreen();
				int dx = locationOnScreen.x - locationOnScreenLayeredPane.x;
				int dy = locationOnScreen.y - locationOnScreenLayeredPane.y;

				dx -= mousePressedEvent.getX();
				dy -= mousePressedEvent.getY();

				boolean isCopyDesired = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( e );
				int x = e.getX() + dx;
				int y = e.getY() + dy;
				dragProxy.setCopyDesired( isCopyDesired );
				dragProxy.setLocation( x, y );
				//layeredPane.setPosition( dragProxy, dy );
				dropProxy.setCopyDesired( isCopyDesired );
			}
		}
	}

	private org.lgna.croquet.history.DragStep step;

	private void handleLeftMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		org.lgna.croquet.DragModel dragModel = this.getModel();
		if( dragModel != null ) {
			org.lgna.croquet.Application application = org.lgna.croquet.Application.getActiveInstance();
			application.setDragInProgress( true );
			this.updateProxySizes();
			this.updateProxyPosition( e );

			javax.swing.JLayeredPane layeredPane = getLayeredPane();
			layeredPane.add( this.dragProxy, new Integer( 1 ) );
			layeredPane.setLayer( this.dragProxy, javax.swing.JLayeredPane.DRAG_LAYER );

			this.step = org.lgna.croquet.history.TransactionManager.addDragStep( dragModel, org.lgna.croquet.triggers.DragTrigger.createUserInstance( this, this.getLeftButtonPressedEvent() ) );
			this.step.setLatestMouseEvent( e );
			this.step.fireDragStarted();
			dragModel.handleDragStarted( this.step );
			this.showDragProxy();
		}
	}

	private void handleLeftMouseDragged( java.awt.event.MouseEvent e ) {
		if( org.lgna.croquet.Application.getActiveInstance().isDragInProgress() ) {
			this.updateProxyPosition( e );
			if( this.step != null ) {
				this.step.handleMouseDragged( e );
			}
		}
	}

	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		this.isWithinClickThreshold = false;
		if( isActuallyPotentiallyDraggable() ) {
			if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				this.handleLeftMouseDraggedOutsideOfClickThreshold( e );
			}
		}
	}

	protected void handleMouseDragged( java.awt.event.MouseEvent e ) {
		if( this.isWithinClickThreshold ) {
			int dx = e.getX() - this.mousePressedEvent.getX();
			int dy = e.getY() - this.mousePressedEvent.getY();
			if( ( ( dx * dx ) + ( dy * dy ) ) > ( this.clickThreshold * this.clickThreshold ) ) {
				handleMouseDraggedOutsideOfClickThreshold( e );
			}
		}
		if( isActuallyPotentiallyDraggable() ) {
			if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "isActuallyPotentiallyDraggable == true" );
				if( this.isWithinClickThreshold() ) {
					//pass
				} else {
					this.handleLeftMouseDragged( e );
				}
			}
		}
	}

	protected void handleMouseClicked( java.awt.event.MouseEvent e ) {
	}

	private void setActive( boolean isActive ) {
		if( this.isActive != isActive ) {
			this.isActive = isActive;
			this.repaint();
		}
	}

	protected void handleMouseQuoteEnteredUnquote() {
		this.setActive( true );
	}

	protected void handleMouseQuoteExitedUnquote() {
		this.setActive( false );
	}

	private boolean wasQuoteExitedUnquoteSkipped;

	protected final void handleMouseEntered( java.awt.event.MouseEvent e ) {
		if( org.lgna.croquet.Application.getActiveInstance().isDragInProgress() ) {
			//pass
		} else {
			this.handleMouseQuoteEnteredUnquote();
			this.wasQuoteExitedUnquoteSkipped = false;
		}
	}

	protected final void handleMouseExited( java.awt.event.MouseEvent e ) {
		this.wasQuoteExitedUnquoteSkipped = org.lgna.croquet.Application.getActiveInstance().isDragInProgress();
		if( this.wasQuoteExitedUnquoteSkipped ) {
			//pass
		} else {
			this.handleMouseQuoteExitedUnquote();
		}
	}

	protected void handleMouseMoved( java.awt.event.MouseEvent e ) {
	}

	protected void handleMousePressed( java.awt.event.MouseEvent e ) {
		//		java.awt.event.MouseEvent prevMousePressedEvent = this.mousePressedEvent;
		this.isWithinClickThreshold = true;
		this.mousePressedEvent = e;
		this.leftButtonPressedEvent = null;
		if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
			this.leftButtonPressedEvent = e;
		}
		if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
			if( org.lgna.croquet.Application.getActiveInstance().isDragInProgress() ) {
				this.handleCancel( e );
			}
		}
	}

	protected void handleMouseReleased( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				if( this.isWithinClickThreshold() ) {
					//pass
				} else {
					org.lgna.croquet.Application.getActiveInstance().setDragInProgress( false );
					if( this.wasQuoteExitedUnquoteSkipped ) {
						this.handleMouseQuoteExitedUnquote();
						this.wasQuoteExitedUnquoteSkipped = false;
					}
					//					this.setActive( this.getAwtComponent().contains( e.getPoint() ) );
					javax.swing.JLayeredPane layeredPane = getLayeredPane();
					java.awt.Rectangle bounds = this.dragProxy.getBounds();
					layeredPane.remove( this.dragProxy );
					layeredPane.repaint( bounds );
					if( this.step != null ) {
						this.step.handleMouseReleased( e );
					}
				}
			}
		}
	}

	public void showDragProxy() {
		this.dragProxy.setVisible( true );
	}

	public void hideDragProxy() {
		this.dragProxy.setVisible( false );
	}

	public void setDropProxyLocationAndShowIfNecessary( java.awt.Point p, Component<?> asSeenBy, Integer heightToAlignLeftCenterOn, int availableHeight ) {
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		p = javax.swing.SwingUtilities.convertPoint( asSeenBy.getAwtComponent(), p, layeredPane );

		this.dropProxy.setAvailableHeight( availableHeight );
		if( heightToAlignLeftCenterOn != null ) {
			//			java.awt.Rectangle dropBounds = this.dropProxy.getBounds();
			//			p.y += (heightToAlignLeftCenterOn - dropBounds.height) / 2;
			p.y += ( heightToAlignLeftCenterOn - this.dropProxy.getAvailableHeight() ) / 2;
			//p.x += 8;
		}

		//java.awt.event.MouseEvent mousePressedEvent = this.getMousePressedEvent();
		//p.x -= mousePressedEvent.getX();
		//p.y -= mousePressedEvent.getY();

		this.dropProxy.setLocation( p );
		if( this.dropProxy.getParent() != null ) {
			//pass
		} else {
			layeredPane.add( this.dropProxy, new Integer( 1 ) );
			layeredPane.setLayer( this.dropProxy, javax.swing.JLayeredPane.DEFAULT_LAYER );
		}
	}

	public void hideDropProxyIfNecessary() {
		java.awt.Container parent = this.dropProxy.getParent();
		if( parent != null ) {
			java.awt.Rectangle bounds = this.dropProxy.getBounds();
			parent.remove( this.dropProxy );
			parent.repaint( bounds.x, bounds.y, bounds.width, bounds.height );
			//			javax.swing.JLayeredPane layeredPane = getLayeredPane();
			//			if( layeredPane != null ) {
			//				layeredPane.repaint( bounds );
			//			} else {
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: hideDropProxyIfNecessary, layeredPane is null" );
			//			}
		}
	}

	public void handleCancel( java.util.EventObject e ) {
		org.lgna.croquet.Application.getActiveInstance().setDragInProgress( false );
		if( this.wasQuoteExitedUnquoteSkipped ) {
			this.handleMouseQuoteExitedUnquote();
			this.wasQuoteExitedUnquoteSkipped = false;
		}
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		if( layeredPane != null ) {
			java.awt.Rectangle bounds = this.dragProxy.getBounds();
			layeredPane.remove( this.dragProxy );
			layeredPane.repaint( bounds );
		}
		if( this.step != null ) {
			this.step.handleCancel( e );
		}
	}

	protected abstract void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height );

	protected abstract void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height );

	protected abstract void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height );
}
