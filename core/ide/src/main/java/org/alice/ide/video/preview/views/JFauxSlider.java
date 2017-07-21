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
package org.alice.ide.video.preview.views;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class JFauxSlider extends javax.swing.JComponent {
	private static final int THUMB_HEIGHT = 12;
	private static final int HALF_THUMB_WIDTH = 8;
	private static final int THUMB_WIDTH = HALF_THUMB_WIDTH * 2;

	private static final int TRACK_HEIGHT = 8;
	private static final java.awt.Color TRACK_LEADING_COLOR = java.awt.Color.WHITE;
	private static final java.awt.Color TRACK_TRAILING_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.createGray( 221 );
	private static final java.awt.Color THUMB_FILL_COLOR = java.awt.Color.LIGHT_GRAY;
	private static final java.awt.Color THUMB_DRAW_COLOR = java.awt.Color.BLACK;

	private float portion = 0.0f;

	private static final java.awt.Shape THUMB_SHAPE = createThumbShape();

	private static java.awt.Shape createThumbShape() {
		java.awt.geom.RoundRectangle2D.Float roundRect = new java.awt.geom.RoundRectangle2D.Float( -HALF_THUMB_WIDTH, -THUMB_HEIGHT, THUMB_WIDTH, THUMB_HEIGHT, 4, 4 );
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( HALF_THUMB_WIDTH / 2, 0 );
		path.lineTo( 0, TRACK_HEIGHT / 2 );
		path.lineTo( -HALF_THUMB_WIDTH / 2, 0 );
		path.closePath();
		return edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createUnion( roundRect, path );
	}

	private final java.util.List<org.alice.ide.video.preview.views.events.ThumbListener> thumbListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			fireThumbPressed( e );
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
			fireThumbReleased( e );
		}
	};
	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		@Override
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseDragged( java.awt.event.MouseEvent e ) {
			fireThumbDragged( e );
		}
	};

	public JFauxSlider() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 0, 10, 0 ) );
	}

	public void addThumbListener( org.alice.ide.video.preview.views.events.ThumbListener listener ) {
		this.thumbListeners.add( listener );
	}

	public void removeThumbListener( org.alice.ide.video.preview.views.events.ThumbListener listener ) {
		this.thumbListeners.remove( listener );
	}

	private void fireThumbPressed( java.awt.event.MouseEvent e ) {
		this.updatePortion( e );
		for( org.alice.ide.video.preview.views.events.ThumbListener listener : thumbListeners ) {
			listener.thumbPressed( this.portion );
		}
	}

	private void fireThumbReleased( java.awt.event.MouseEvent e ) {
		this.updatePortion( e );
		for( org.alice.ide.video.preview.views.events.ThumbListener listener : thumbListeners ) {
			listener.thumbReleased( this.portion );
		}
	}

	private void fireThumbDragged( java.awt.event.MouseEvent e ) {
		this.updatePortion( e );
		for( org.alice.ide.video.preview.views.events.ThumbListener listener : thumbListeners ) {
			listener.thumbDragged( this.portion );
		}
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

	public float getPortion() {
		return this.portion;
	}

	public void setPortion( float portion ) {
		this.portion = portion;
		this.repaint();
	}

	private void updatePortion( java.awt.event.MouseEvent e ) {
		int w = this.getWidth();
		int trackWidth = w - ( HALF_THUMB_WIDTH * 2 );
		int x0 = HALF_THUMB_WIDTH;
		float p = ( e.getX() - x0 ) / (float)trackWidth;
		p = Math.max( p, 0.0f );
		p = Math.min( p, 1.0f );
		this.setPortion( p );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Insets insets = this.getInsets();
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), THUMB_HEIGHT + TRACK_HEIGHT + insets.top + insets.bottom );
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		Object prevAntialiasing = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		java.awt.Insets insets = this.getInsets();
		try {
			int trackWidth = this.getWidth() - ( HALF_THUMB_WIDTH * 2 );
			int x = HALF_THUMB_WIDTH;
			//int y = ( this.getHeight() - ( TRACK_HEIGHT + THUMB_HEIGHT ) ) / 2;
			int y = insets.top;

			y += THUMB_HEIGHT;

			int centerX = x + (int)Math.round( this.portion * trackWidth );
			java.awt.Shape trackShape = new java.awt.geom.RoundRectangle2D.Float( x, y, trackWidth, TRACK_HEIGHT, TRACK_HEIGHT, TRACK_HEIGHT );
			g2.setColor( TRACK_TRAILING_COLOR );
			g2.fill( trackShape );

			java.awt.Shape leadingRect = new java.awt.Rectangle( x, y, centerX - x, TRACK_HEIGHT );
			java.awt.Shape prevClip = g2.getClip();
			java.awt.geom.Area leadingClip = edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createIntersection( prevClip, leadingRect );
			try {
				g2.setClip( leadingClip );
				g2.setPaint( TRACK_LEADING_COLOR );
				g2.fill( trackShape );
			} finally {
				g2.setClip( prevClip );
			}
			//java.awt.Shape thumbShape = new java.awt.geom.Ellipse2D.Float( centerX - ( THUMB_SIZE / 2 ), 0, THUMB_SIZE, THUMB_SIZE );
			g2.translate( centerX, y );
			g2.setPaint( THUMB_FILL_COLOR );
			g2.fill( THUMB_SHAPE );
			g2.setPaint( THUMB_DRAW_COLOR );
			g2.draw( THUMB_SHAPE );
			g2.translate( -centerX, 10 );
		} finally {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, prevAntialiasing );
		}
	}
}
