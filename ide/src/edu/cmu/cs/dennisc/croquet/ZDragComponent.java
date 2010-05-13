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
package edu.cmu.cs.dennisc.croquet;

public abstract class ZDragComponent extends ZControl {
	private DragAndDropOperation dragAndDropOperation;

	private DragProxy dragProxy = null;
	private DropProxy dropProxy = null;

	private java.awt.event.ComponentListener componentAdapter = new java.awt.event.ComponentListener() {
		public void componentHidden( java.awt.event.ComponentEvent arg0 ) {
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentResized( java.awt.event.ComponentEvent e ) {
			ZDragComponent.this.updateProxySizes();
		}
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
	};

	public ZDragComponent() {
		this.updateProxySizes();
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.addComponentListener( this.componentAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeComponentListener( this.componentAdapter );
		super.removeNotify();
	}

	@Override
	protected boolean isMouseListeningDesired() {
		return super.isMouseListeningDesired() || this.dragAndDropOperation != null;
	}

	public Component<?> getSubject() {
		return this;
	}
	//	public void setSubject( java.awt.Component subject ) {
	//		this.subject = subject;
	//	}
	public DragAndDropOperation getDragAndDropOperation() {
		return this.dragAndDropOperation;
	}
	public void setDragAndDropOperation( DragAndDropOperation dragAndDropOperation ) {
		this.dragAndDropOperation = dragAndDropOperation;
	}

	protected boolean isAlphaDesiredWhenOverDropReceptor() {
		return false;
	}

	private boolean isActuallyPotentiallyDraggable() {
		boolean rv = this.dragAndDropOperation != null;
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
		javax.swing.JRootPane rootPane = this.getRoot().getRootPane();
		if( rootPane != null ) {
			return rootPane.getLayeredPane();
		} else {
			throw new RuntimeException( "cannot find rootPane: " + this );
			//return null;
		}
	}

	private void updateProxySizes() {
		if( isActuallyPotentiallyDraggable() ) {
			dragProxy.setSize( dragProxy.getProxyWidth(), dragProxy.getProxyHeight() );
			dropProxy.setSize( dropProxy.getProxyWidth(), dropProxy.getProxyHeight() );
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

	class DefaultDragAndDropContext extends DragAndDropContext {
		private DragAndDropOperation operation;
		private DropReceptorInfo[] potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
		private DropReceptor currentDropReceptor;
		private java.awt.event.MouseEvent originalMouseEvent;
		private java.awt.event.MouseEvent latestMouseEvent;

		public DefaultDragAndDropContext( DragAndDropOperation operation, java.awt.event.MouseEvent originalMouseEvent, java.awt.event.MouseEvent latestMouseEvent, java.util.List< ? extends DropReceptor > potentialDropReceptors ) {
			super( null );
			this.operation = operation;
			this.originalMouseEvent = originalMouseEvent;
			this.setLatestMouseEvent( latestMouseEvent );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
			int i = 0;
			for( DropReceptor dropReceptor : potentialDropReceptors ) {
				Component<?> dropComponent = dropReceptor.getComponent();
				java.awt.Rectangle bounds = dropComponent.getBounds();
				bounds = javax.swing.SwingUtilities.convertRectangle( dropComponent.getJComponent().getParent(), bounds, this.getDragSource().getJComponent() );
				this.potentialDropReceptorInfos[ i ] = new DropReceptorInfo( dropReceptor, bounds );
				i++;
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				//todo: pass original mouse pressed event?
				dropReceptorInfo.getDropReceptor().dragStarted( this );
			}
		}

		@Override
		public java.awt.event.MouseEvent getOriginalMouseEvent() {
			return this.originalMouseEvent;
		}
		@Override
		public java.awt.event.MouseEvent getLatestMouseEvent() {
			return this.latestMouseEvent;
		}
		public void setLatestMouseEvent( java.awt.event.MouseEvent latestMouseEvent ) {
			this.latestMouseEvent = latestMouseEvent;
		}

		@Override
		public ZDragComponent getDragSource() {
			java.util.EventObject e = this.originalMouseEvent;
			if( e != null ) {
				return (ZDragComponent)Component.lookup( (java.awt.Component)e.getSource() );
			} else {
				return null;
			}
		}
		@Override
		public DropReceptor getCurrentDropReceptor() {
			return this.currentDropReceptor;
		}
		public void setCurrentDropReceptor( DropReceptor currentDropReceptor ) {
			this.currentDropReceptor = currentDropReceptor;
		}
		private DropReceptor getDropReceptorUnder( int x, int y ) {
			DropReceptor rv = null;
			int prevHeight = Integer.MAX_VALUE;
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				assert dropReceptorInfo != null;
				if( dropReceptorInfo.contains( x, y ) ) {
					java.awt.Rectangle bounds = dropReceptorInfo.getBounds();
					assert bounds != null;
					int nextHeight = bounds.height;
					if( nextHeight < prevHeight ) {
						rv = dropReceptorInfo.getDropReceptor();
						prevHeight = nextHeight;
					}
				}
			}
			return rv;
		}
		private DropReceptor getDropReceptorUnder( java.awt.Rectangle bounds ) {
			DropReceptor rv = null;
			int prevHeight = Integer.MAX_VALUE;
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				if( dropReceptorInfo.intersects( bounds ) ) {
					int nextHeight = dropReceptorInfo.getBounds().height;
					if( nextHeight < prevHeight ) {
						rv = dropReceptorInfo.getDropReceptor();
						prevHeight = nextHeight;
					}
				}
			}
			return rv;
		}
		protected DropReceptor getDropReceptorUnder( java.awt.event.MouseEvent e ) {
			DropReceptor rv = getDropReceptorUnder( e.getX(), e.getY() );
			if( rv != null ) {
				//pass
			} else {
				if( ZDragComponent.this.dragProxy != null ) {
					java.awt.Rectangle dragBounds = ZDragComponent.this.dragProxy.getBounds();
					dragBounds = javax.swing.SwingUtilities.convertRectangle( ZDragComponent.this.dragProxy.getParent(), dragBounds, this.getDragSource().getJComponent() );
					int x = dragBounds.x;
					int y = dragBounds.y + dragBounds.height / 2;
					rv = getDropReceptorUnder( x, y );
					if( rv != null ) {
						//pass
					} else {
						rv = getDropReceptorUnder( dragBounds );
					}
				}
			}
			return rv;
		}
		public void handleMouseDragged( java.awt.event.MouseEvent e ) {
			this.setLatestMouseEvent( e );
			DropReceptor nextDropReceptor = getDropReceptorUnder( e );
			if( this.currentDropReceptor != nextDropReceptor ) {
				if( this.currentDropReceptor != null ) {
					ZDragComponent.this.dragAndDropOperation.handleDragExitedDropReceptor( this );
					this.currentDropReceptor.dragExited( this, false );
				}
				this.currentDropReceptor = nextDropReceptor;
				if( this.currentDropReceptor != null ) {
					this.currentDropReceptor.dragEntered( this );
					ZDragComponent.this.dragAndDropOperation.handleDragEnteredDropReceptor( this );
				}
			}
			if( ZDragComponent.this.dragProxy != null ) {
				ZDragComponent.this.dragProxy.setOverDropAcceptor( this.currentDropReceptor != null );
			}
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragUpdated( this );
			}
		}
		public void handleMouseReleased( java.awt.event.MouseEvent e ) {
			this.setLatestMouseEvent( e );
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragDropped( this );
				this.currentDropReceptor.dragExited( this, true );
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				dropReceptorInfo.getDropReceptor().dragStopped( this );
			}
			ZDragComponent.this.dragAndDropOperation.handleDragStopped( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
		}
		public void handleCancel( java.util.EventObject e ) {
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragExited( this, false );
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				dropReceptorInfo.getDropReceptor().dragStopped( this );
			}
			ZDragComponent.this.dragAndDropOperation.handleDragStopped( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
			ZDragComponent.this.hideDropProxyIfNecessary();
		}
	}

	private DefaultDragAndDropContext dragAndDropContext;

	private void handleLeftMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		Application.getSingleton().setDragInProgress( true );
		this.updateProxySizes();
		this.updateProxyPosition( e );

		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		layeredPane.add( this.dragProxy, new Integer( 1 ) );
		layeredPane.setLayer( this.dragProxy, javax.swing.JLayeredPane.DRAG_LAYER );

		//todo?
		this.dragAndDropContext = new DefaultDragAndDropContext( this.dragAndDropOperation, this.getLeftButtonPressedEvent(), e, this.dragAndDropOperation.createListOfPotentialDropReceptors( this ) );
		if( this.dragAndDropOperation != null ) {
			this.dragAndDropOperation.handleDragStarted( dragAndDropContext );
		}
	}
	private void handleLeftMouseDragged( java.awt.event.MouseEvent e ) {
		if( Application.getSingleton().isDragInProgress() ) {
			this.updateProxyPosition( e );
			if( this.dragAndDropContext != null) {
				this.dragAndDropContext.handleMouseDragged( e );
			}
		}
	}
	@Override
	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		super.handleMouseDraggedOutsideOfClickThreshold( e );
		if( isActuallyPotentiallyDraggable() ) {
			if(edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				this.handleLeftMouseDraggedOutsideOfClickThreshold( e );
			}
		}
	}
	@Override
	public void handleMouseDragged( java.awt.event.MouseEvent e ) {
		super.handleMouseDragged( e );
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
	@Override
	public void handleMouseMoved( java.awt.event.MouseEvent e ) {
		super.handleMouseMoved( e );
		if( isActuallyPotentiallyDraggable() ) {
			if( this.isFauxDrag ) {
				if( Application.getSingleton().isDragInProgress() ) {
					//pass
				} else {
					this.handleLeftMouseDraggedOutsideOfClickThreshold( e );
				}
				this.handleLeftMouseDragged( e );
			}
		}
	}
	
	protected boolean isClickReservedForSelection() {
		return false;
	}

	private static boolean isFauxDragDesired = false;
	public static boolean isFauxDragDesired() {
		return ZDragComponent.isFauxDragDesired;
	}
	public static void setFauxDragDesired( boolean isFauxDragDesired ) {
		ZDragComponent.isFauxDragDesired = isFauxDragDesired;
	}
	
//	protected boolean isFauxDragDesired() {
//		return false;
//	}
	private boolean isFauxDrag = false;
	
	@Override
	public void handleMousePressed( java.awt.event.MouseEvent e ) {
		super.handleMousePressed( e );
		if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
			if( Application.getSingleton().isDragInProgress() ) {
				this.handleCancel( e );
			}
		}
	}
	@Override
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				boolean isDrop;
				if( this.isWithinClickThreshold() ) {
					if( ZDragComponent.isFauxDragDesired && this.isClickReservedForSelection()==false ) {
						Component<?> focusedComponent;
						if( this.isFauxDrag ) {
							focusedComponent = null;
						} else {
							focusedComponent = this;
						}
						isDrop = this.isFauxDrag;
						this.isFauxDrag = !this.isFauxDrag;
						if( focusedComponent != null ) {
							edu.cmu.cs.dennisc.java.awt.MouseFocusEventQueue.getSingleton().pushComponentWithMouseFocus( focusedComponent.getJComponent() );
						} else {
							edu.cmu.cs.dennisc.java.awt.MouseFocusEventQueue.getSingleton().popComponentWithMouseFocus();
						}
					} else {
						isDrop = false;
					}
				} else {
					isDrop = true;
				}
				if( isDrop ) {
					Application.getSingleton().setDragInProgress( false );
					this.setActive( this.getJComponent().contains( e.getPoint() ) );
					javax.swing.JLayeredPane layeredPane = getLayeredPane();
					java.awt.Rectangle bounds = this.dragProxy.getBounds();
					layeredPane.remove( this.dragProxy );
					layeredPane.repaint( bounds );
					if( this.dragAndDropContext != null ) {
						this.dragAndDropContext.handleMouseReleased( e );
					}
				}
			}
		}
	}

	public void setDropProxyLocationAndShowIfNecessary( java.awt.Point p, Component<?> asSeenBy, Integer heightToAlignLeftCenterOn ) {
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		p = javax.swing.SwingUtilities.convertPoint( asSeenBy.getJComponent(), p, layeredPane );

		if( heightToAlignLeftCenterOn != null ) {
			java.awt.Rectangle dropBounds = this.dropProxy.getBounds();
			p.y += (heightToAlignLeftCenterOn - dropBounds.height) / 2;
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
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		if( this.dropProxy.getParent() != null ) {
			java.awt.Rectangle bounds = this.dropProxy.getBounds();
			if( layeredPane != null ) {
				layeredPane.remove( this.dropProxy );
				layeredPane.repaint( bounds );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: hideDropProxyIfNecessary, layeredPane is null" );
			}
		}
	}
	public void handleCancel( java.util.EventObject e ) {
		Application.getSingleton().setDragInProgress( false );
		this.setActive( false );
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		if( layeredPane != null ) {
			java.awt.Rectangle bounds = this.dragProxy.getBounds();
			layeredPane.remove( this.dragProxy );
			layeredPane.repaint( bounds );
		}
		if( this.dragAndDropContext != null ) {
			this.dragAndDropContext.handleCancel( e );
		}
		this.isFauxDrag = false;
		edu.cmu.cs.dennisc.java.awt.MouseFocusEventQueue.getSingleton().popComponentWithMouseFocus();
	}
}
