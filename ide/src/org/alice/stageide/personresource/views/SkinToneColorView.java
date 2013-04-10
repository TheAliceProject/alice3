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
public class SkinToneColorView extends org.lgna.croquet.components.ViewController<javax.swing.JComponent, org.alice.stageide.personresource.ColorState> {
	private static int HALF_ARROW_WIDTH = 4;
	private static int ARROW_HEIGHT = 6;
	private static java.awt.Color B_COLOR = org.lgna.story.resources.sims2.BaseSkinTone.DARKER.getColor();
	private static java.awt.Color C_COLOR = org.lgna.story.resources.sims2.BaseSkinTone.DARK.getColor();
	private static java.awt.Color D_COLOR = org.lgna.story.resources.sims2.BaseSkinTone.LIGHT.getColor();
	private static java.awt.Color E_COLOR = org.lgna.story.resources.sims2.BaseSkinTone.LIGHTER.getColor();

	private static java.awt.Color A_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( B_COLOR, 1.0, 1.0, 0.9 );
	private static java.awt.Color F_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( E_COLOR, 1.0, 1.0, 1.1 );

	private static java.awt.Color[] colors = {
			A_COLOR,
			B_COLOR,
			C_COLOR,
			D_COLOR,
			E_COLOR,
			F_COLOR
	};

	private static final float[][] hsbBuffers = new float[ 6 ][ 3 ];
	private static final float minHue;
	private static final float maxHue;
	static {
		int i = 0;
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for( java.awt.Color color : colors ) {
			java.awt.Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), hsbBuffers[ i ] );
			min = Math.min( min, hsbBuffers[ i ][ 0 ] );
			max = Math.max( max, hsbBuffers[ i ][ 0 ] );
			i++;
		}
		minHue = min;
		maxHue = max;
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( minHue, maxHue );
	}

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
			int xF = this.getWidth() - HALF_ARROW_WIDTH;
			int w = xF - xA;
			this.portion = ( e.getX() - xA ) / (float)w;
			this.portion = Math.min( this.portion, 1.0f );
			this.portion = Math.max( this.portion, 0.0f );
			this.repaint();

			int index = (int)( this.portion / 0.2 );
			java.awt.Color nextColor;
			if( index < 0 ) {
				nextColor = colors[ 0 ];
			} else if( index >= ( colors.length - 1 ) ) {
				nextColor = colors[ colors.length - 1 ];
			} else {
				float interp = this.portion % 0.2f;
				interp *= 5.0;

				float[] hsbBuffer0 = hsbBuffers[ index ];
				float[] hsbBuffer1 = hsbBuffers[ index + 1 ];
				float[] hsbBuffer = new float[ 3 ];
				for( int i = 0; i < 3; i++ ) {
					hsbBuffer[ i ] = ( hsbBuffer0[ i ] * ( 1 - interp ) ) + ( hsbBuffer1[ i ] * interp );
				}
				nextColor = java.awt.Color.getHSBColor( hsbBuffer[ 0 ], hsbBuffer[ 1 ], hsbBuffer[ 2 ] );
			}

			SkinToneColorView.this.getModel().getSwingModel().setValue( nextColor, e );
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
			rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( rv, ARROW_HEIGHT + ARROW_HEIGHT + 32 );
			rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( rv, HALF_ARROW_WIDTH + HALF_ARROW_WIDTH + 200 );
			return rv;
		}

		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );

			int xA = HALF_ARROW_WIDTH;
			int xF = this.getWidth() - HALF_ARROW_WIDTH;

			int w = xF - xA;

			int xB = (int)( xA + ( w * 0.2 ) );
			int xC = (int)( xA + ( w * 0.4 ) );
			int xD = (int)( xA + ( w * 0.6 ) );
			int xE = (int)( xA + ( w * 0.8 ) );

			int y0 = ARROW_HEIGHT;
			int y1 = this.getHeight() - ARROW_HEIGHT;

			int h = y1 - y0;

			java.awt.GradientPaint abPaint = new java.awt.GradientPaint( xA, y0, colors[ 0 ], xB, y0, colors[ 1 ] );
			java.awt.GradientPaint bcPaint = new java.awt.GradientPaint( xB, y0, colors[ 1 ], xC, y0, colors[ 2 ] );
			java.awt.GradientPaint cdPaint = new java.awt.GradientPaint( xC, y0, colors[ 2 ], xD, y0, colors[ 3 ] );
			java.awt.GradientPaint dePaint = new java.awt.GradientPaint( xD, y0, colors[ 3 ], xE, y0, colors[ 4 ] );
			java.awt.GradientPaint efPaint = new java.awt.GradientPaint( xE, y0, colors[ 4 ], xF, y0, colors[ 5 ] );

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			g2.setPaint( abPaint );
			g2.fillRect( xA, y0, xB - xA, h );
			g2.setPaint( bcPaint );
			g2.fillRect( xB, y0, xC - xB, h );
			g2.setPaint( cdPaint );
			g2.fillRect( xC, y0, xD - xC, h );
			g2.setPaint( dePaint );
			g2.fillRect( xD, y0, xE - xD, h );
			g2.setPaint( efPaint );
			g2.fillRect( xE, y0, xF - xE, h );

			g2.setColor( java.awt.Color.BLACK );

			double x = portion * w;
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.SOUTH, (int)x, 0, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawTriangle( g2, edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.Heading.NORTH, (int)x, y1, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );
		}
	}

	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
		}
	};

	public SkinToneColorView( org.alice.stageide.personresource.ColorState model ) {
		super( model );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().getSwingModel().addChangeListener( this.changeListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.getModel().getSwingModel().removeChangeListener( this.changeListener );
		super.handleUndisplayable();
	}

	@Override
	protected javax.swing.JComponent createAwtComponent() {
		return new JColorView();
	}
}
