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
package org.alice.stageide.personresource.views;

/**
 * @author Dennis Cosgrove
 */
public class ColorView extends org.lgna.croquet.components.ViewController<javax.swing.JComponent, org.alice.stageide.personresource.ColorState> {
	private static int HALF_ARROW_WIDTH = 4;
	private static int ARROW_HEIGHT = 6;
	private static java.awt.Color A_COLOR = new java.awt.Color( 0xFF0000 );
	private static java.awt.Color B_COLOR = new java.awt.Color( 0x00FF00 );
	private static java.awt.Color C_COLOR = new java.awt.Color( 0x0000FF );
	private static java.awt.Color D_COLOR = new java.awt.Color( 0xFFFFFF );

	private class JColorView extends javax.swing.JComponent {
		private float portion = 0.5f;

		private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
			public void mousePressed( java.awt.event.MouseEvent e ) {
				setPortion( e );
			}

			public void mouseReleased( java.awt.event.MouseEvent e ) {
				setPortion( e );
			}

			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}

			public void mouseExited( java.awt.event.MouseEvent e ) {
			}

			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
		};

		private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
			public void mouseMoved( java.awt.event.MouseEvent e ) {
			}

			public void mouseDragged( java.awt.event.MouseEvent e ) {
				setPortion( e );
			}
		};

		private void setPortion( java.awt.event.MouseEvent e ) {
			int xA = HALF_ARROW_WIDTH;
			int xD = this.getWidth() - HALF_ARROW_WIDTH;
			int w = xD - xA;
			this.portion = ( e.getX() - xA ) / (float)w;
			this.portion = Math.min( this.portion, 1.0f );
			this.portion = Math.max( this.portion, 0.0f );
			this.repaint();

			java.awt.Color color0;
			java.awt.Color color1;
			if( this.portion < 0.333f ) {
				color0 = A_COLOR;
				color1 = B_COLOR;
			} else if( this.portion < 0.667f ) {
				color0 = B_COLOR;
				color1 = C_COLOR;
			} else {
				color0 = C_COLOR;
				color1 = D_COLOR;
			}
			java.awt.Color nextColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.interpolate( color0, color1, portion );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( nextColor );
			ColorView.this.getModel().getSwingModel().setValue( nextColor, e );
		}

		@Override
		public void addNotify() {
			super.addNotify();
			this.addMouseListener( this.mouseListener );
			this.addMouseMotionListener( this.mouseMotionListener );
		}

		@Override
		public void removeNotify() {
			this.removeMouseMotionListener( this.mouseMotionListener );
			this.removeMouseListener( this.mouseListener );
			super.removeNotify();
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			java.awt.Dimension rv = super.getPreferredSize();
			rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( rv, 100 );
			return rv;
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );

			int xA = HALF_ARROW_WIDTH;
			int xD = this.getWidth() - HALF_ARROW_WIDTH;

			int w = xD - xA;

			int xB = xA + ( w / 3 );
			int xC = xA + ( ( 2 * w ) / 3 );

			int y0 = ARROW_HEIGHT;
			int y1 = this.getHeight() - ARROW_HEIGHT;

			int h = y1 - y0;

			java.awt.GradientPaint abPaint = new java.awt.GradientPaint( xA, y0, A_COLOR, xB, y0, B_COLOR );
			java.awt.GradientPaint bcPaint = new java.awt.GradientPaint( xB, y0, B_COLOR, xC, y0, C_COLOR );
			java.awt.GradientPaint cdPaint = new java.awt.GradientPaint( xC, y0, C_COLOR, xD, y0, D_COLOR );

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			g2.setPaint( abPaint );
			g2.fillRect( xA, y0, xB - xA, h );
			g2.setPaint( bcPaint );
			g2.fillRect( xB, y0, xC - xB, h );
			g2.setPaint( cdPaint );
			g2.fillRect( xC, y0, xD - xC, h );

			g2.setColor( java.awt.Color.BLACK );

			double x = xA + ( portion * w );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.SOUTH, (int)x, 0, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.NORTH, (int)x, y1, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );
		}
	}

	public ColorView( org.alice.stageide.personresource.ColorState model ) {
		super( model );
	}

	@Override
	protected javax.swing.JComponent createAwtComponent() {
		return new JColorView();
	}
}
