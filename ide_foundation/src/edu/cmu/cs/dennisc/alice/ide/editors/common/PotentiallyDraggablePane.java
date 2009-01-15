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
package edu.cmu.cs.dennisc.alice.ide.editors.common;

/**
 * @author Dennis Cosgrove
 */
abstract class Proxy extends javax.swing.JPanel {
	private static java.awt.image.BufferedImage image;
	private static java.awt.image.BufferedImage getOffscreenImage( int width, int height ) {
		if( image == null || image.getWidth() != width || image.getHeight() != height ) {
			image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
			//image = getGraphicsConfiguration().createCompatibleImage( width, height, java.awt.Transparency.TRANSLUCENT );
		}
		return image;
	}
	private PotentiallyDraggablePane potentiallyDraggableAffordance;
	boolean isOverDropAcceptor = false;
	boolean isCopyDesired = false;

	public Proxy( PotentiallyDraggablePane potentiallyDraggableAffordance ) {
		this.potentiallyDraggableAffordance = potentiallyDraggableAffordance;
		this.setOpaque( false );
	}
	
	protected PotentiallyDraggablePane getPotentiallyDraggablePane() {
		return this.potentiallyDraggableAffordance;
	}

	//todo: just use getWidth() and getHeight()?
	protected abstract int getProxyWidth();
	protected abstract int getProxyHeight();
	
	protected abstract void paintProxy( java.awt.Graphics2D g2 );
	protected abstract float getAlpha();
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		int width = this.getProxyWidth();
		int height = this.getProxyHeight();
		if( width > 0 && height > 0 ) {
			java.awt.image.BufferedImage image = Proxy.getOffscreenImage( width, height );
			//todo: synchronize
			//if( LayeredPaneProxy.image == null || LayeredPaneProxy.image.getWidth() < width || LayeredPaneProxy.image.getHeight() < height ) {
			java.awt.Graphics2D g2Image = (java.awt.Graphics2D)image.getGraphics();
			this.paintProxy( g2Image );
			g2Image.dispose();

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			
			java.awt.Composite prevComposite = g2.getComposite();

			//g2.setComposite( java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.CLEAR, 0.0f ) );
			//g2.clearRect( 0, 0, width, height );
			float alpha = this.getAlpha();
			if( alpha < 0.99f ) {
				g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, this.getAlpha() ) );
			}
			//java.awt.Color bgColor = new java.awt.Color( 0, 0, 0, 0 );
			//g2.drawImage( LayeredPaneimage, 0, 0, width, height, 0, 0, width, height, bgColor, this );
			g2.drawImage( image, 0, 0, width, height, this );
			g2.dispose();
			g2.setComposite( prevComposite );
		}
	}

	public boolean isOverDropAcceptor() {
		return this.isOverDropAcceptor;
	}
	public void setOverDropAcceptor( boolean isOverDropAcceptor ) {
		if( this.isOverDropAcceptor != isOverDropAcceptor ) {
			this.isOverDropAcceptor = isOverDropAcceptor;
			this.repaint();
		}
	}
	public boolean isCopyDesired() {
		return this.isCopyDesired;
	}
	public void setCopyDesired( boolean isCopyDesired ) {
		if( this.isCopyDesired != isCopyDesired ) {
			this.isCopyDesired = isCopyDesired;
			this.repaint();
		}
	}
}
/**
 * @author Dennis Cosgrove
 */
class DragProxy extends Proxy {
	public DragProxy( PotentiallyDraggablePane potentiallyDraggableAffordance ) {
		super( potentiallyDraggableAffordance );
	}
	@Override
	protected int getProxyWidth() {
		return this.getPotentiallyDraggablePane().getDragWidth();
	}
	@Override
	protected int getProxyHeight() {
		return this.getPotentiallyDraggablePane().getDragHeight();
	}
	@Override
	protected float getAlpha() {
		if( this.isOverDropAcceptor() ) {
			return 0.5f;
		} else {
			return 1.0f;
		}
	}
	@Override
	protected void paintProxy( java.awt.Graphics2D g2 ) {
		this.getPotentiallyDraggablePane().paintDrag( g2, this.isOverDropAcceptor(), this.isCopyDesired() );
	}
}
/**
 * @author Dennis Cosgrove
 */
class DropProxy extends Proxy {
	public DropProxy( PotentiallyDraggablePane potentiallyDraggableAffordance ) {
		super( potentiallyDraggableAffordance );
	}
	@Override
	protected int getProxyWidth() {
		return this.getPotentiallyDraggablePane().getDropWidth();
	}
	@Override
	protected int getProxyHeight() {
		return this.getPotentiallyDraggablePane().getDropHeight();
	}
	@Override
	protected float getAlpha() {
		return 0.75f;
	}
	@Override
	protected void paintProxy( java.awt.Graphics2D g2 ) {
		this.getPotentiallyDraggablePane().paintDrop( g2, this.isOverDropAcceptor(), this.isCopyDesired() );
	}
}

/**
 * @author Dennis Cosgrove
 */
class DropReceptorInfo {
	private DropReceptor dropReceptor;
	private java.awt.Rectangle bounds;
	public DropReceptorInfo( DropReceptor dropReceptor, java.awt.Rectangle bounds ) {
		this.dropReceptor = dropReceptor;
		this.bounds = bounds;
	}
	public boolean contains( int x, int y ) {
		return this.bounds.contains( x, y );
	}
	public boolean intersects( java.awt.Rectangle rectangle ) {
		return this.bounds.intersects( rectangle );
	}
	public DropReceptor getDropReceptor() {
		return this.dropReceptor;
	}
	public void setDropReceptor( DropReceptor dropReceptor ) {
		this.dropReceptor = dropReceptor;
	}
	public java.awt.Rectangle getBounds() {
		return this.bounds;
	}
	public void setBounds( java.awt.Rectangle bounds ) {
		this.bounds = bounds;
	}
}
/**
 * @author Dennis Cosgrove
 */
public abstract class PotentiallyDraggablePane extends PotentiallySelectablePane {
	private DragProxy dragProxy = null;
	private DropProxy dropProxy = null;
	private DropReceptorInfo[] potentialDropReceptorInfos = null;
	private DropReceptor currentDropReceptor = null;
	
	public PotentiallyDraggablePane( int axis ) {
		super( axis );
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentHidden( java.awt.event.ComponentEvent arg0 ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				PotentiallyDraggablePane.this.updateProxySizes();
			}
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
		} );
		this.updateProxySizes();
	}

	@Override
	protected boolean isSelectionMouseListeningDesired() {
		return super.isSelectionMouseListeningDesired() || isActuallyPotentiallyDraggable();
	}
	protected boolean isActuallyPotentiallyDraggable() {
		return false;
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
			if( this.dragProxy != null ) {
				dragProxy.setSize( this.getDragWidth(), this.getDragHeight() );
			}
			if( this.dropProxy != null ) {
				dropProxy.setSize( this.getDropWidth(), this.getDropHeight() );
			}
		}
	}
	private synchronized void updateProxyPosition( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			java.awt.event.MouseEvent mousePressedEvent = this.getMousePressedEvent();
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
		
	@Override
	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		super.handleMouseDraggedOutsideOfClickThreshold( e );
		if( isActuallyPotentiallyDraggable() ) {
			getIDE().setDragInProgress( true );
			if( this.dragProxy != null ) {
				//pass
			} else {
				this.dragProxy = new DragProxy( this );
			}
			if( this.dropProxy != null ) {
				//pass
			} else {
				this.dropProxy = new DropProxy( this );
			}
			this.updateProxySizes();
			this.updateProxyPosition( e );
			
			
			javax.swing.JLayeredPane layeredPane = getLayeredPane();
			layeredPane.add( this.dragProxy, new Integer( 1 ) );
			layeredPane.setLayer( this.dragProxy, javax.swing.JLayeredPane.DRAG_LAYER );
			java.util.List< ? extends DropReceptor > potentialDropReceptors = getIDE().getPotentialDropReceptors( this );
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
			getIDE().handleDragStarted( this.currentDropReceptor );
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
		return rv;
	}
	@Override
	protected void handleMouseDragged( java.awt.event.MouseEvent e ) {
		super.handleMouseDragged( e );
		if( isActuallyPotentiallyDraggable() ) {
			if( this.isWithinClickThreshold() ) {
				//pass
			} else {
				this.updateProxyPosition( e );
				DropReceptor nextDropReceptor = getDropReceptorUnder( e );
				if( this.currentDropReceptor != nextDropReceptor ) {
					if( this.currentDropReceptor != null ) {
						getIDE().handleDragExited( this.currentDropReceptor );
						this.currentDropReceptor.dragExited( this, e, false );
					}
					this.currentDropReceptor = nextDropReceptor;
					if( this.currentDropReceptor != null ) {
						this.currentDropReceptor.dragEntered( this, e );
						getIDE().handleDragEntered( this.currentDropReceptor );
					}
				}
				this.dragProxy.setOverDropAcceptor( this.currentDropReceptor != null );
				if( this.currentDropReceptor != null ) {
					this.currentDropReceptor.dragUpdated( this, e );
				}
			}
		}
	}
	@Override
	protected void handleMouseReleased( java.awt.event.MouseEvent e ) {
		super.handleMouseReleased( e );
		if( isActuallyPotentiallyDraggable() ) {
			if( getIDE().isDragInProgress() ) {
				getIDE().setDragInProgress( false );
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
				getIDE().handleDragStopped( this.currentDropReceptor );
			}
		}
		this.potentialDropReceptorInfos = null;
	}

	private static java.awt.TexturePaint copyTexturePaint = null;

	private static java.awt.TexturePaint getCopyTexturePaint() {
		if( PotentiallyDraggablePane.copyTexturePaint != null ) {
			//pass
		} else {
			int width = 8;
			int height = 8;
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
			java.awt.Graphics g = image.getGraphics();
			g.setColor( new java.awt.Color( 0, 0, 255, 96 ) );
			g.drawLine( 2, 4, 6, 4 );
			g.drawLine( 4, 2, 4, 6 );
			g.dispose();
			PotentiallyDraggablePane.copyTexturePaint = new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
		}
		return PotentiallyDraggablePane.copyTexturePaint;
	}
	
	private final int DROP_SHADOW_SIZE = 6;
	public int getDragWidth() {
		return getDropWidth() + DROP_SHADOW_SIZE;
	}
	public int getDragHeight() {
		return getDropHeight() + DROP_SHADOW_SIZE;
	}
	public int getDropWidth() {
		return this.getWidth();
	}
	public int getDropHeight() {
		return this.getHeight();
	}
	public void paintDrag( java.awt.Graphics2D g2, boolean isOverDragAccepter, boolean isCopyDesired ) {
		java.awt.Paint prevPaint = g2.getPaint();
		
		g2.setPaint( new java.awt.Color( 0,0,0,64 ) );
		g2.translate( DROP_SHADOW_SIZE, DROP_SHADOW_SIZE );
		this.createBoundsShape().fill( g2 );
		g2.translate( -DROP_SHADOW_SIZE, -DROP_SHADOW_SIZE );
		g2.setPaint( prevPaint );
		print( g2 );
//		if( isOverDragAccepter ) {
//			//pass
//		} else {
//			g2.setPaint( new java.awt.Color( 127, 127, 127, 127 ) );
//			this.createBoundsShape().fill( g2 );
//		}
		if( isCopyDesired ) {
			g2.setPaint( PotentiallyDraggablePane.getCopyTexturePaint() );
			this.createBoundsShape().fill( g2 );
		}
	}
	public void paintDrop( java.awt.Graphics2D g2, boolean isOverDragAccepter, boolean isCopyDesired ) {
		print( g2 );
		g2.setColor( new java.awt.Color( 0, 0, 0, 127 ) );
		this.createBoundsShape().fill( g2 );
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
