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
package org.alice.ide.capture.views;

/**
 * @author Dennis Cosgrove
 */
public class ImageCaptureRectangleStencilView extends org.lgna.croquet.views.LayerStencil {
	private static final java.awt.Color STENCIL_BASE_COLOR = new java.awt.Color( 63, 63, 63, 63 );
	private static final java.awt.Color STENCIL_LINE_COLOR = new java.awt.Color( 31, 31, 31, 63 );

	private static java.awt.Paint createStencilPaint() {
		int width = 8;
		int height = 8;
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
		g2.setColor( STENCIL_BASE_COLOR );
		g2.fillRect( 0, 0, width, height );
		g2.setColor( STENCIL_LINE_COLOR );
		g2.drawLine( 0, height, width, 0 );
		g2.fillRect( 0, 0, 1, 1 );
		g2.dispose();
		return new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
	}

	private static final java.awt.Paint STENCIL_PAINT = createStencilPaint();

	private final JZoomView jZoomView = new JZoomView();
	private final org.lgna.croquet.views.Window window = new org.lgna.croquet.views.Window();

	private final class MouseAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
		private int xPressed = -1;
		private int yPressed = -1;

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			synchronized( hole ) {
				this.xPressed = e.getX();
				this.yPressed = e.getY();
				hole.setBounds( this.xPressed, this.yPressed, 0, 0 );
				repaint();
			}
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			synchronized( hole ) {
				if( isHoleValid() ) {
					setStencilShowing( false );
					captureImageAndShowFrame();
				}
				jZoomView.handleMouseMovedOrDragged( null );
				invalidateHole();
				repaint();
			}
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		private void handleMouseMovedOrDragged( java.awt.event.MouseEvent e ) {
			jZoomView.handleMouseMovedOrDragged( e );
			updateWindowLocation( e.getXOnScreen(), e.getYOnScreen() );
		}

		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
			this.handleMouseMovedOrDragged( e );
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			synchronized( hole ) {
				if( isHoleValid() ) {
					edu.cmu.cs.dennisc.java.awt.RectangleUtilities.setBounds( hole, this.xPressed, this.yPressed, e.getX(), e.getY() );
					this.handleMouseMovedOrDragged( e );
				}
				repaint();
			}
		}
	}

	private final MouseAdapter mouseAdapter = new MouseAdapter();
	private final org.alice.imageeditor.croquet.ImageEditorFrame imageComposite = new org.alice.imageeditor.croquet.ImageEditorFrame();

	private static final javax.swing.KeyStroke ESCAPE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 );
	private final java.awt.event.ActionListener escapeKeyListener = new java.awt.event.ActionListener() {
		@Override
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			if( isHoleValid() ) {
				invalidateHole();
				repaint();
			} else {
				setStencilShowing( false );
			}
		}
	};

	private final java.awt.Rectangle hole = new java.awt.Rectangle();
	private final org.alice.ide.capture.ImageCaptureComposite imageCaptureComposite;

	public ImageCaptureRectangleStencilView( org.lgna.croquet.views.AbstractWindow<?> window, Integer layerId, org.alice.ide.capture.ImageCaptureComposite imageCaptureComposite ) {
		super( window, layerId );
		this.imageCaptureComposite = imageCaptureComposite;
		this.window.getAwtComponent().setContentPane( this.jZoomView );
	}

	private void captureImageAndShowFrame() {
		synchronized( this.hole ) {
			if( this.isHoleValid() ) {
				if( ( this.hole.width > 0 ) && ( this.hole.height > 0 ) ) {
					java.awt.Image image = edu.cmu.cs.dennisc.capture.ImageCaptureUtilities.captureRectangle( this.getWindow().getRootPane().getAwtComponent(), this.hole, imageCaptureComposite.getDpi() );
					image = imageCaptureComposite.convertToRgbaIfNecessary( image );
					imageComposite.setImageClearShapesAndShowFrame( image );
				}
			}
		}
	}

	public org.alice.imageeditor.croquet.ImageEditorFrame getImageComposite() {
		return this.imageComposite;
	}

	private void updateWindowLocation( int xScreen, int yScreen ) {
		final int OFFSET = 32;
		window.setLocation( xScreen + OFFSET, yScreen + OFFSET );
	}

	private void invalidateHole() {
		this.hole.setBounds( -1, -1, -1, -1 );
		//this.window.setVisible( false );
	}

	private boolean isHoleValid() {
		return this.hole.width > -1;
	}

	@Override
	protected boolean contains( int x, int y, boolean superContains ) {
		return superContains;
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return null;
	}

	@Override
	public void setStencilShowing( boolean isShowing ) {
		boolean prevIsStencilShowing = this.isStencilShowing();
		if( prevIsStencilShowing != isShowing ) {
			if( prevIsStencilShowing ) {
				this.unregisterKeyboardAction( ESCAPE_KEY_STROKE );
				this.removeMouseMotionListener( this.mouseAdapter );
				this.removeMouseListener( this.mouseAdapter );
			}
			super.setStencilShowing( isShowing );
			if( isShowing ) {
				java.awt.PointerInfo pointerInfo = java.awt.MouseInfo.getPointerInfo();
				java.awt.Point p = pointerInfo.getLocation();
				this.updateWindowLocation( p.x, p.y );
				this.window.pack();
				this.window.getAwtComponent().toFront();
			}
			this.window.setVisible( isShowing );
			if( isShowing ) {
				this.addMouseListener( this.mouseAdapter );
				this.addMouseMotionListener( this.mouseAdapter );
				this.registerKeyboardAction( this.escapeKeyListener, ESCAPE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
				this.requestFocusLater();
			}
		}
	}

	//
	@Override
	protected void paintComponentPrologue( java.awt.Graphics2D g2 ) {
	}

	@Override
	protected void paintComponentEpilogue( java.awt.Graphics2D g2 ) {
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2 ) {
		java.awt.Shape prevClip = g2.getClip();
		java.awt.Paint prevPaint = g2.getPaint();
		java.awt.Stroke prevStroke = g2.getStroke();

		java.awt.Shape shape = prevClip;

		synchronized( this.hole ) {
			if( this.hole.width > 0 ) {
				java.awt.geom.Area area = new java.awt.geom.Area( shape );
				area.subtract( new java.awt.geom.Area( this.hole ) );
				shape = area;
			}
		}
		g2.setPaint( STENCIL_PAINT );
		g2.fill( shape );

		synchronized( this.hole ) {
			g2.setPaint( java.awt.Color.WHITE );
			g2.drawRect( this.hole.x - 1, this.hole.y - 1, this.hole.width + 1, this.hole.height + 1 );
		}

		java.awt.event.MouseEvent e = this.jZoomView.getMouseEvent();
		if( e != null ) {

		}
		g2.setStroke( prevStroke );
		g2.setPaint( prevPaint );
	}
}
