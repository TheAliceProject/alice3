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
package org.alice.ide.video.preview.views;

/**
 * @author Dennis Cosgrove
 */
/* package-private */class JFauxSlider extends javax.swing.JComponent {
	private static final int THUMB_SIZE = 24;
	private static final int TRACK_HEIGHT = 16;
	private static final int INDENT = THUMB_SIZE / 2;
	private static final java.awt.Color TRACK_LEADING_COLOR = java.awt.Color.BLUE.darker();
	private static final java.awt.Color TRACK_TRAILING_COLOR = java.awt.Color.LIGHT_GRAY;
	private static final java.awt.Color THUMB_FILL_COLOR = new java.awt.Color( 191, 191, 255 );
	private static final java.awt.Color THUMB_DRAW_COLOR = java.awt.Color.BLACK;

	private float portion = 0.0f;

	private final java.util.List<org.alice.ide.video.preview.views.events.ThumbListener> thumbListeners = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}

		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		public void mousePressed( java.awt.event.MouseEvent e ) {
			fireThumbPressed( e );
		}

		public void mouseReleased( java.awt.event.MouseEvent e ) {
			fireThumbReleased( e );
		}
	};
	private final java.awt.event.MouseMotionListener mouseMotionListener = new java.awt.event.MouseMotionListener() {
		public void mouseMoved( java.awt.event.MouseEvent e ) {
		}

		public void mouseDragged( java.awt.event.MouseEvent e ) {
			fireThumbDragged( e );
		}
	};

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
		int trackWidth = w - ( INDENT * 2 );
		int x0 = INDENT;
		float p = ( e.getX() - x0 ) / (float)trackWidth;
		p = Math.max( p, 0.0f );
		p = Math.min( p, 1.0f );
		this.setPortion( p );
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), THUMB_SIZE );
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		Object prevAntialiasing = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		try {
			int w = this.getWidth();
			int h = this.getHeight();

			int trackWidth = w - ( INDENT * 2 );
			int x = INDENT;
			int y = ( h - TRACK_HEIGHT ) / 2;

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
			java.awt.Shape thumbShape = new java.awt.geom.Ellipse2D.Float( centerX - ( THUMB_SIZE / 2 ), 0, THUMB_SIZE, THUMB_SIZE );
			g2.setPaint( THUMB_FILL_COLOR );
			g2.fill( thumbShape );
			g2.setPaint( THUMB_DRAW_COLOR );
			g2.draw( thumbShape );
		} finally {
			edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.setAntialiasing( g2, prevAntialiasing );
		}
	}
}
