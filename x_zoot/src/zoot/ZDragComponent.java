/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package zoot;

public abstract class ZDragComponent extends ZControl {
//	private java.awt.Component subject;
	private DragAndDropOperation dragAndDropOperation;
	
	private DragProxy dragProxy = null;
	private DropProxy dropProxy = null;
	private DropReceptorInfo[] potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
	private DropReceptor currentDropReceptor = null;
	
	public ZDragComponent() {
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentHidden( java.awt.event.ComponentEvent arg0 ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				ZDragComponent.this.updateProxySizes();
			}
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
		} );
		this.updateProxySizes();
	}
	
	

	@Override
	protected boolean isMouseListeningDesired() { 
		return super.isMouseListeningDesired() || this.dragAndDropOperation != null;
	}
	
	public java.awt.Component getSubject() {
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

//	@Override
//	protected boolean isSelectionMouseListeningDesired() {
//		return super.isSelectionMouseListeningDesired() || isActuallyPotentiallyDraggable();
//	}
	protected final boolean isActuallyPotentiallyDraggable() {
		boolean rv = this.dragAndDropOperation != null;
		if( rv ) {
			java.awt.Component subject = this.getSubject();
			if( this.dragProxy != null ) {
				//pass
			} else {
				this.dragProxy = new DragProxy( subject );
			}
			if( this.dropProxy != null ) {
				//pass
			} else {
				this.dropProxy = new DropProxy( subject );
			}
		}
		return rv;
	}

	protected javax.swing.JLayeredPane getLayeredPane() {
		javax.swing.JRootPane rootPane = javax.swing.SwingUtilities.getRootPane( this );
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
			java.awt.event.MouseEvent mousePressedEvent = this.getMousePressedEvent();
			if( mousePressedEvent != null ) {
				javax.swing.JLayeredPane layeredPane = getLayeredPane();
				java.awt.Point locationOnScreenLayeredPane = layeredPane.getLocationOnScreen();
				java.awt.Point locationOnScreen = this.getLocationOnScreen();
				int dx = locationOnScreen.x - locationOnScreenLayeredPane.x;
				int dy = locationOnScreen.y - locationOnScreenLayeredPane.y;
		
				dx -= mousePressedEvent.getX();
				dy -= mousePressedEvent.getY();
		
				boolean isCopyDesired = edu.cmu.cs.dennisc.swing.SwingUtilities.isQuoteControlUnquoteDown( e );
				int x = e.getX() + dx;
				int y = e.getY() + dy;
				dragProxy.setCopyDesired( isCopyDesired );
				dragProxy.setLocation( x, y );
				//layeredPane.setPosition( dragProxy, dy );
				dropProxy.setCopyDesired( isCopyDesired );
			}
		}
	}
		
	@Override
	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		super.handleMouseDraggedOutsideOfClickThreshold( e );
		if( isActuallyPotentiallyDraggable() ) {
			ZManager.setDragInProgress( true );
			this.updateProxySizes();
			this.updateProxyPosition( e );
			
			
			javax.swing.JLayeredPane layeredPane = getLayeredPane();
			layeredPane.add( this.dragProxy, new Integer( 1 ) );
			layeredPane.setLayer( this.dragProxy, javax.swing.JLayeredPane.DRAG_LAYER );
			java.util.List< ? extends DropReceptor > potentialDropReceptors = this.dragAndDropOperation.getPotentialDropReceptors( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
			int i=0;
			for( DropReceptor dropReceptor : potentialDropReceptors ) {
				java.awt.Component dropComponent = dropReceptor.getAWTComponent();
				java.awt.Rectangle bounds = dropComponent.getBounds();
				bounds = javax.swing.SwingUtilities.convertRectangle( dropComponent.getParent(), bounds, this );
				this.potentialDropReceptorInfos[ i ] = new DropReceptorInfo( dropReceptor, bounds );
				i++;
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				//todo: pass original mouse pressed event?
				dropReceptorInfo.getDropReceptor().dragStarted( this, e );
			}
			this.dragAndDropOperation.handleDragStarted( this.currentDropReceptor );
			this.currentDropReceptor = null;
		}
	}
	private DropReceptor getDropReceptorUnder( int x, int y ) {
		DropReceptor rv = null;
		int prevHeight = Integer.MAX_VALUE;
		for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
			if( dropReceptorInfo.contains( x, y ) ) {
				 int nextHeight = dropReceptorInfo.getBounds().height;
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
			if( this.dragProxy != null ) {
				java.awt.Rectangle dragBounds = this.dragProxy.getBounds();
				dragBounds = javax.swing.SwingUtilities.convertRectangle( this.dragProxy.getParent(), dragBounds, this );
				int x = dragBounds.x;
				int y = dragBounds.y + dragBounds.height/2;
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
	@Override
	public void handleMouseDragged( java.awt.event.MouseEvent e ) {
		super.handleMouseDragged( e );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleMouseDragged", e );
		if( isActuallyPotentiallyDraggable() ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( "isActuallyPotentiallyDraggable == true" );
			if( this.isWithinClickThreshold() ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "isWithinClickThreshold == false" );
				this.updateProxyPosition( e );
				DropReceptor nextDropReceptor = getDropReceptorUnder( e );
				if( this.currentDropReceptor != nextDropReceptor ) {
					if( this.currentDropReceptor != null ) {
						this.dragAndDropOperation.handleDragExited( this.currentDropReceptor );
						this.currentDropReceptor.dragExited( this, e, false );
					}
					this.currentDropReceptor = nextDropReceptor;
					if( this.currentDropReceptor != null ) {
						this.currentDropReceptor.dragEntered( this, e );
						this.dragAndDropOperation.handleDragEntered( this.currentDropReceptor );
					}
				}
				if( this.dragProxy != null ) {
					this.dragProxy.setOverDropAcceptor( this.currentDropReceptor != null );
				}
				if( this.currentDropReceptor != null ) {
					this.currentDropReceptor.dragUpdated( this, e );
				}
			}
		}
	}
	@Override
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			if( ZManager.isDragInProgress() ) {
				ZManager.setDragInProgress( false );
				this.setActive( this.contains( e.getPoint() ) );
				javax.swing.JLayeredPane layeredPane = getLayeredPane();
				java.awt.Rectangle bounds = this.dragProxy.getBounds();
				layeredPane.remove( this.dragProxy );
				layeredPane.repaint( bounds );
				
				if( this.currentDropReceptor != null ) {
					this.currentDropReceptor.dragDropped( this, e );
					this.currentDropReceptor.dragExited( this, e, true );
				}
				for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
					dropReceptorInfo.getDropReceptor().dragStopped( this, e );
				}
				this.dragAndDropOperation.handleDragStopped( this.currentDropReceptor );
			}
		}
		this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
	}

	
	public void setDropProxyLocationAndShowIfNecessary( java.awt.Point p, java.awt.Component asSeenBy, Integer heightToAlignLeftCenterOn ) {
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		p = javax.swing.SwingUtilities.convertPoint( asSeenBy, p, layeredPane );
		
		if( heightToAlignLeftCenterOn != null ) {
			java.awt.Rectangle dropBounds = this.dropProxy.getBounds();
			p.y += ( heightToAlignLeftCenterOn - dropBounds.height ) / 2;
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
}