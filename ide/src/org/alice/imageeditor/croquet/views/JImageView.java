/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.imageeditor.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class JImageView extends javax.swing.JComponent {
	private static java.awt.Shape createShape( java.awt.Point a, java.awt.Point b ) {
		int x = Math.min( a.x, b.x );
		int y = Math.min( a.y, b.y );
		int width = Math.abs( b.x - a.x );
		int height = Math.abs( b.y - a.y );
		return new java.awt.Rectangle( x, y, width, height );
	}

	private static final java.awt.Stroke SHAPE_STROKE = new java.awt.BasicStroke( 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
	private static final java.awt.Stroke OUTLINE_STROKE = new java.awt.BasicStroke( 0.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND, 8.0f, new float[] { 8.0f }, 0.0f );
	private static final javax.swing.KeyStroke ESCAPE_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_ESCAPE, 0 );
	private static final javax.swing.KeyStroke CLEAR_KEY_STROKE = javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK );

	private static final java.awt.Stroke[] DROP_SHADOW_STROKES;
	static {
		final int N = 5;
		DROP_SHADOW_STROKES = new java.awt.Stroke[ N ];
		for( int i = 0; i < N; i++ ) {
			DROP_SHADOW_STROKES[ i ] = new java.awt.BasicStroke( ( i + 1 ) * 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND );
		}
	};

	private static final java.awt.Paint DROP_SHADOW_PAINT = new java.awt.Color( 127, 127, 127, 15 );

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mousePressed( java.awt.event.MouseEvent e ) {
			if( e.getButton() == java.awt.event.MouseEvent.BUTTON1 ) {
				ptPressed = e.getPoint();
			} else {
				ptPressed = null;
				repaint();
			}
		}

		public void mouseReleased( java.awt.event.MouseEvent e ) {
			if( ptPressed != null ) {
				composite.addShape( createShape( ptPressed, e.getPoint() ) );
				ptPressed = null;
				ptDragged = null;
				repaint();
			}
		}

		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		public void mouseExited( java.awt.event.MouseEvent e ) {
		}
	};

	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved( java.awt.event.MouseEvent e ) {

		}

		public void mouseDragged( java.awt.event.MouseEvent e ) {
			if( ptPressed != null ) {
				ptDragged = e.getPoint();
				repaint();
			}
		}
	};

	private final java.awt.event.ActionListener escapeListener = new java.awt.event.ActionListener() {
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			ptPressed = null;
			ptDragged = null;
			repaint();
		}
	};

	private java.awt.Point ptPressed;
	private java.awt.Point ptDragged;

	private final org.alice.imageeditor.croquet.ImageEditorFrame composite;

	public JImageView( org.alice.imageeditor.croquet.ImageEditorFrame composite ) {
		this.composite = composite;
	}

	/* package-private */void render( java.awt.Graphics2D g2 ) {
		java.awt.Image image = composite.getImageHolder().getValue();
		if( image != null ) {
			g2.drawImage( image, 0, 0, this );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
		}

		java.awt.Stroke prevStroke = g2.getStroke();

		if( composite.getDropShadowState().getValue() ) {
			final int DROP_SHADOW_OFFSET = 4;
			g2.translate( DROP_SHADOW_OFFSET, DROP_SHADOW_OFFSET );
			g2.setPaint( DROP_SHADOW_PAINT );
			for( java.awt.Stroke stroke : DROP_SHADOW_STROKES ) {
				g2.setStroke( stroke );
				for( java.awt.Shape shape : composite.getShapes() ) {
					g2.draw( shape );
				}
			}
			g2.translate( -DROP_SHADOW_OFFSET, -DROP_SHADOW_OFFSET );
		}

		g2.setPaint( java.awt.Color.RED );
		g2.setStroke( SHAPE_STROKE );
		for( java.awt.Shape shape : composite.getShapes() ) {
			g2.draw( shape );
		}

		if( ( ptPressed != null ) && ( ptDragged != null ) ) {
			g2.draw( createShape( ptPressed, ptDragged ) );
		}

		g2.setStroke( prevStroke );
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.translate( 1, 1 );
		this.render( g2 );
		g2.translate( -1, -1 );
		if( composite.getShowDashedBorderState().getValue() ) {
			java.awt.Stroke prevStroke = g2.getStroke();
			g2.setColor( java.awt.Color.DARK_GRAY );
			g2.setStroke( OUTLINE_STROKE );
			g2.drawRect( 0, 0, this.getWidth() - 1, this.getHeight() - 1 );
			g2.setStroke( prevStroke );
		}
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Image image = composite.getImageHolder().getValue();
		if( image != null ) {
			return new java.awt.Dimension( image.getWidth( this ) + 2, image.getHeight( this ) + 2 );
		} else {
			return super.getPreferredSize();
		}
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.addMouseListener( this.mouseListener );
		this.addMouseMotionListener( this.mouseMotionListener );
		this.registerKeyboardAction( this.escapeListener, ESCAPE_KEY_STROKE, javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW );
	}

	@Override
	public void removeNotify() {
		this.unregisterKeyboardAction( CLEAR_KEY_STROKE );
		this.unregisterKeyboardAction( ESCAPE_KEY_STROKE );
		this.removeMouseMotionListener( this.mouseMotionListener );
		this.removeMouseListener( this.mouseListener );
		super.removeNotify();
	}
};
