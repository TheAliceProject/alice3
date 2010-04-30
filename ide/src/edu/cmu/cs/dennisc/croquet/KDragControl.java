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
	public interface Paintable {
		public void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height );
		public void paint( java.awt.Graphics2D g2, int x, int y, int width, int height );
	}
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
			Paintable paintable = this.getDragComponent().getPaintable();
			paintable.fillBounds( g2, x, y, width, height );
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
	private Paintable paintable;
	public KDragControl( Paintable paintable ) {
		this.paintable = paintable;
	}
	
	public Paintable getPaintable() {
		return this.paintable;
	}
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
	
	protected KComponent< ? > getSubject() {
		return this;
	}
	private void handleCancel( java.awt.event.KeyEvent e ) {
	}
	@Override
	protected void paintComponent( java.awt.Graphics2D g2 ) {
		int width = this.getWidth();
		int height = this.getHeight();
		this.paintable.paint( g2, 0, 0, width, height );
	}
}
