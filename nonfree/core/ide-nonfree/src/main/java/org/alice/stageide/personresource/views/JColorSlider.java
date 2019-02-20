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
package org.alice.stageide.personresource.views;

import edu.cmu.cs.dennisc.java.awt.GraphicsUtilities;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * @author Dennis Cosgrove
 */
public abstract class JColorSlider extends JComponent {
	private static int HALF_ARROW_WIDTH = 3;
	private static int ARROW_HEIGHT = 4;

	private final Color[] colors;
	private float portion = 0.5f;

	private final float[][] hsbBuffers = new float[ 6 ][ 3 ];
	private final float minHue;
	private final float maxHue;

	public JColorSlider( Color[] colors ) {
		this.colors = colors;
		int i = 0;
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for( Color color : colors ) {
			Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), hsbBuffers[ i ] );
			min = Math.min( min, hsbBuffers[ i ][ 0 ] );
			max = Math.max( max, hsbBuffers[ i ][ 0 ] );
			i++;
		}
		minHue = min;
		maxHue = max;
	}

	private final MouseListener mouseListener = new MouseListener() {
		@Override
		public void mousePressed( MouseEvent e ) {
			setPortion( e );
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			setPortion( e );
		}

		@Override
		public void mouseEntered( MouseEvent e ) {
		}

		@Override
		public void mouseExited( MouseEvent e ) {
		}

		@Override
		public void mouseClicked( MouseEvent e ) {
		}
	};

	private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved( MouseEvent e ) {
		}

		@Override
		public void mouseDragged( MouseEvent e ) {
			setPortion( e );
		}
	};

	private void setPortion( MouseEvent e ) {
		int xA = HALF_ARROW_WIDTH;
		int xF = this.getWidth() - HALF_ARROW_WIDTH;
		int w = xF - xA;
		this.portion = ( e.getX() - xA ) / (float)w;
		this.portion = Math.min( this.portion, 1.0f );
		this.portion = Math.max( this.portion, 0.0f );
		this.repaint();

		int index = (int)( this.portion / 0.2 );
		Color nextColor;
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
			nextColor = Color.getHSBColor( hsbBuffer[ 0 ], hsbBuffer[ 1 ], hsbBuffer[ 2 ] );
		}
		this.handleNextColor( nextColor );
	}

	protected abstract void handleNextColor( Color nextColor );

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
	public Dimension getMinimumSize() {
		Insets insets = this.getInsets();
		return new Dimension( insets.left + insets.right + HALF_ARROW_WIDTH + HALF_ARROW_WIDTH + 32, insets.top + insets.bottom + ARROW_HEIGHT + ARROW_HEIGHT + 16 );
	}

	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent( g );

		Insets insets = this.getInsets();
		g.translate( insets.left, insets.top );
		int width = this.getWidth() - insets.left - insets.right;

		int xA = HALF_ARROW_WIDTH;
		int xF = width - HALF_ARROW_WIDTH;

		int w = xF - xA;

		int xB = (int)( xA + ( w * 0.2 ) );
		int xC = (int)( xA + ( w * 0.4 ) );
		int xD = (int)( xA + ( w * 0.6 ) );
		int xE = (int)( xA + ( w * 0.8 ) );

		int height = this.getHeight() - insets.top - insets.bottom;
		int y0 = ARROW_HEIGHT;
		int y1 = height - ARROW_HEIGHT;

		int h = y1 - y0;

		GradientPaint abPaint = new GradientPaint( xA, y0, colors[ 0 ], xB, y0, colors[ 1 ] );
		GradientPaint bcPaint = new GradientPaint( xB, y0, colors[ 1 ], xC, y0, colors[ 2 ] );
		GradientPaint cdPaint = new GradientPaint( xC, y0, colors[ 2 ], xD, y0, colors[ 3 ] );
		GradientPaint dePaint = new GradientPaint( xD, y0, colors[ 3 ], xE, y0, colors[ 4 ] );
		GradientPaint efPaint = new GradientPaint( xE, y0, colors[ 4 ], xF, y0, colors[ 5 ] );

		Graphics2D g2 = (Graphics2D)g;

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

		g2.setColor( Color.BLACK );

		double x = portion * w;
		GraphicsUtilities.drawTriangle( g2, GraphicsUtilities.Heading.SOUTH, (int)x, 0, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );
		GraphicsUtilities.drawTriangle( g2, GraphicsUtilities.Heading.NORTH, (int)x, y1, HALF_ARROW_WIDTH * 2, ARROW_HEIGHT );

		g.translate( -insets.left, -insets.top );
	}
}
