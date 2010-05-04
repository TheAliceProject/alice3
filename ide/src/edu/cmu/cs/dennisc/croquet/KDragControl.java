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

/**
 * @author Dennis Cosgrove
 */
public abstract class KDragControl extends KControl {
	/**
	 * @author Dennis Cosgrove
	 */
	private static abstract class Proxy extends javax.swing.JPanel {
		private static java.awt.image.BufferedImage image;
		private static java.awt.image.BufferedImage getOffscreenImage( int width, int height ) {
			if( image == null || image.getWidth() != width || image.getHeight() != height ) {
				image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
				//image = getGraphicsConfiguration().createCompatibleImage( width, height, java.awt.Transparency.TRANSLUCENT );
			}
			return image;
		}
		private KDragControl dragComponent;
		private boolean isOverDropAcceptor = false;
		private boolean isCopyDesired = false;
		

		public Proxy( KDragControl dragComponent ) {
			this.dragComponent = dragComponent;
			this.setOpaque( false );
		}
		
		protected KDragControl getDragComponent() {
			return this.dragComponent;
		}
		protected KDragControl getSubject() {
			return this.dragComponent;
		}

		public int getProxyWidth() {
			return this.getSubject().getWidth();
		}
		public int getProxyHeight() {
			return this.getSubject().getHeight();
		}
		
		protected void fillBounds( java.awt.Graphics2D g2 ) {
			int x = 0;
			int y = 0;
			int width = this.getDragComponent().getWidth();
			int height = this.getDragComponent().getHeight();
			this.getDragComponent().fillBounds( g2, x, y, width, height );
		}
		
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

		public int getDropWidth() {
			return this.getWidth();
		}
		public int getDropHeight() {
			return this.getHeight();
		}
	}
	/**
	 * @author Dennis Cosgrove
	 */
	private static class DragProxy extends Proxy {
		private java.awt.event.KeyListener keyAdapter = new java.awt.event.KeyListener() {
			public void keyPressed( java.awt.event.KeyEvent e ) {
			}
			public void keyReleased( java.awt.event.KeyEvent e ) {
				if( e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE ) {
					DragProxy.this.getDragComponent().handleCancel( e );
				}
			}
			public void keyTyped( java.awt.event.KeyEvent e ) {
			}
		};
		private boolean isAlphaDesiredWhenOverDropReceptor;
		public DragProxy( KDragControl dragComponent, boolean isAlphaDesiredWhenOverDropReceptor ) {
			super( dragComponent );
			this.isAlphaDesiredWhenOverDropReceptor = isAlphaDesiredWhenOverDropReceptor;
		}
		private final int DROP_SHADOW_SIZE = 6;
		@Override
		public int getProxyWidth() {
			return super.getProxyWidth() + DROP_SHADOW_SIZE;
		}
		@Override
		public int getProxyHeight() {
			return super.getProxyHeight() + DROP_SHADOW_SIZE;
		}
		@Override
		protected float getAlpha() {
			if( this.isAlphaDesiredWhenOverDropReceptor && this.isOverDropAcceptor() ) {
				return 0.5f;
			} else {
				return 1.0f;
			}
		}
		
		
		@Override
		public void addNotify() {
			super.addNotify();
			this.addKeyListener( this.keyAdapter );
			this.requestFocus();
		}
		@Override
		public void removeNotify() {
			this.transferFocus();
			this.removeKeyListener( this.keyAdapter );
			super.removeNotify();
		}
		
		@Override
		protected void paintProxy( java.awt.Graphics2D g2 ) {
			java.awt.Paint prevPaint = g2.getPaint();
			g2.setPaint( new java.awt.Color( 0,0,0,64 ) );
			//todo?
			g2.translate( DROP_SHADOW_SIZE, DROP_SHADOW_SIZE );
			fillBounds( g2 );
			g2.translate( -DROP_SHADOW_SIZE, -DROP_SHADOW_SIZE );
			g2.setPaint( prevPaint );
			this.getSubject().getJComponent().print( g2 );

			
//			if( isOverDragAccepter ) {
//				//pass
//			} else {
//				g2.setPaint( new java.awt.Color( 127, 127, 127, 127 ) );
//				this.createBoundsShape().fill( g2 );
//			}
			if( this.isCopyDesired() ) {
				
				//todo:
				g2.setPaint( edu.cmu.cs.dennisc.zoot.PaintUtilities.getCopyTexturePaint() );
				fillBounds( g2 );
			}
		}
	}
	/**
	 * @author Dennis Cosgrove
	 */
	private static class DropProxy extends Proxy {
		public DropProxy( KDragControl dragComponent ) {
			super( dragComponent );
		}
		@Override
		protected float getAlpha() {
			return 0.75f;
		}
		@Override
		protected void paintProxy( java.awt.Graphics2D g2 ) {
			this.getSubject().getJComponent().print( g2 );
			g2.setColor( new java.awt.Color( 0, 0, 0, 127 ) );
			fillBounds( g2 );
		}
	}

	private edu.cmu.cs.dennisc.zoot.DragAndDropOperation dragAndDropOperation;

	private boolean isAlphaDesiredWhenOverDropReceptor() {
		return false;
	}
	private DragProxy dragProxy = new DragProxy( this, this.isAlphaDesiredWhenOverDropReceptor() );
	private DropProxy dropProxy = new DropProxy( this );

	private java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener() {
		public void componentHidden( java.awt.event.ComponentEvent arg0 ) {
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentResized( java.awt.event.ComponentEvent e ) {
			KDragControl.this.updateProxySizes();
		}
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
	};
	@Override
	protected void adding() {
		super.adding();
		this.getJComponent().addComponentListener( this.componentListener );
	}
	@Override
	protected void removed() {
		this.getJComponent().removeComponentListener( this.componentListener );
		super.removed();
	}
	private boolean isActuallyPotentiallyDraggable() {
		return true;
	}
	private void updateProxySizes() {
		if( this.isActuallyPotentiallyDraggable() ) {
			this.dragProxy.setSize( this.dragProxy.getProxyWidth(), this.dragProxy.getProxyHeight() );
			this.dropProxy.setSize( this.dropProxy.getProxyWidth(), this.dropProxy.getProxyHeight() );
		}
	}
	
	//todo: reduce visibility?
	public KComponent< ? > getSubject() {
		return this;
	}
	private void handleCancel( java.awt.event.KeyEvent e ) {
	}
	protected javax.swing.JLayeredPane getLayeredPane() {
		javax.swing.JRootPane rootPane = javax.swing.SwingUtilities.getRootPane( this.getJComponent() );
		if( rootPane != null ) {
			return rootPane.getLayeredPane();
		} else {
			throw new RuntimeException( "cannot find rootPane: " + this );
			//return null;
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
	
	/**
	 * @author Dennis Cosgrove
	 */
	private static class DropReceptorInfo {
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


	class DefaultDragAndDropContext extends AbstractContext< DragAndDropOperation > implements DragAndDropContext {
		private DropReceptorInfo[] potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
		private DropReceptor currentDropReceptor;
		private java.awt.event.MouseEvent latestMouseEvent;

		public DefaultDragAndDropContext( DragAndDropOperation operation, java.awt.event.MouseEvent originalMouseEvent, java.awt.event.MouseEvent latestMouseEvent, java.util.List< ? extends DropReceptor > potentialDropReceptors ) {
			super( operation, originalMouseEvent, ZManager.CANCEL_IS_WORTHWHILE );
			this.setLatestMouseEvent( latestMouseEvent );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ potentialDropReceptors.size() ];
			int i = 0;
			for( DropReceptor dropReceptor : potentialDropReceptors ) {
				KComponent< ? > dropComponent = dropReceptor.getComponent();
				java.awt.Rectangle bounds = dropComponent.getBounds();
				bounds = dropComponent.getParent().convertRectangle( bounds, this.getDragSource() );
				this.potentialDropReceptorInfos[ i ] = new DropReceptorInfo( dropReceptor, bounds );
				i++;
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				//todo: pass original mouse pressed event?
				dropReceptorInfo.getDropReceptor().dragStarted( this );
			}
		}

		public java.awt.event.MouseEvent getOriginalMouseEvent() {
			return (java.awt.event.MouseEvent)this.getEvent();
		}
		public java.awt.event.MouseEvent getLatestMouseEvent() {
			return this.latestMouseEvent;
		}
		public void setLatestMouseEvent( java.awt.event.MouseEvent latestMouseEvent ) {
			this.latestMouseEvent = latestMouseEvent;
		}

		public KDragControl getDragSource() {
			java.util.EventObject e = getEvent();
			if( e != null ) {
				return (KDragControl)e.getSource();
			} else {
				return null;
			}
		}
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
				if( KDragControl.this.dragProxy != null ) {
					java.awt.Rectangle dragBounds = KDragControl.this.dragProxy.getBounds();
					dragBounds = KDragControl.this.dragProxy.getParent().convertRectangle( dragBounds, this.getDragSource() );
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
					KDragControl.this.dragAndDropOperation.handleDragExitedDropReceptor( this );
					this.currentDropReceptor.dragExited( this, false );
				}
				this.currentDropReceptor = nextDropReceptor;
				if( this.currentDropReceptor != null ) {
					this.currentDropReceptor.dragEntered( this );
					KDragControl.this.dragAndDropOperation.handleDragEnteredDropReceptor( this );
				}
			}
			if( KDragControl.this.dragProxy != null ) {
				KDragControl.this.dragProxy.setOverDropAcceptor( this.currentDropReceptor != null );
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
			KDragControl.this.dragAndDropOperation.handleDragStopped( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
		}
		public void handleCancel( java.util.EventObject e ) {
			if( this.currentDropReceptor != null ) {
				this.currentDropReceptor.dragExited( this, false );
			}
			for( DropReceptorInfo dropReceptorInfo : this.potentialDropReceptorInfos ) {
				dropReceptorInfo.getDropReceptor().dragStopped( this );
			}
			KDragControl.this.dragAndDropOperation.handleDragStopped( this );
			this.potentialDropReceptorInfos = new DropReceptorInfo[ 0 ];
			KDragControl.this.hideDropProxyIfNecessary();
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
			//todo: investidate why this would be null
			if( this.dragAndDropContext != null ) {
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

	private boolean isFauxDragDesired() {
		return false;
	}
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
					if( this.isFauxDragDesired() && this.isClickReservedForSelection()==false ) {
						java.awt.Component focusedComponent;
						if( this.isFauxDrag ) {
							focusedComponent = null;
						} else {
							focusedComponent = this.getJComponent();
						}
						isDrop = this.isFauxDrag;
						this.isFauxDrag = !this.isFauxDrag;
						if( focusedComponent != null ) {
							edu.cmu.cs.dennisc.java.awt.MouseFocusEventQueue.getSingleton().pushComponentWithMouseFocus( focusedComponent );
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

	public void setDropProxyLocationAndShowIfNecessary( java.awt.Point p, KComponent< ? > asSeenBy, Integer heightToAlignLeftCenterOn ) {
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		p = asSeenBy.convertPoint( p, layeredPane );

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
