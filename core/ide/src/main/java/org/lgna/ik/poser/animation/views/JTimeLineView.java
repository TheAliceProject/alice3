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
package org.lgna.ik.poser.animation.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.lgna.croquet.Application;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.ik.poser.animation.composites.TimeLineComposite;
import org.lgna.ik.poser.animation.edits.CurrentTimeLineTimeChangeEdit;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;

/**
 * @author Matt May
 */
public class JTimeLineView extends JPanel {
	private static final java.text.NumberFormat CENTI_FORMAT = new java.text.DecimalFormat( "0.00" );
	private static final java.awt.Shape ARROW = createArrow();
	//	private static final java.awt.Shape D_ARROW = createEndOfTimeLineArrows();

	private static java.awt.Paint PRESSED_PAINT = new Color( 191, 191, 255 );

	public JTimeLineView( TimeLineView timeLineView ) {
		this.addMouseListener( mlAdapter );
		this.addMouseMotionListener( mmlAdapter );
		this.composite = (TimeLineComposite)timeLineView.getComposite();
		this.component = timeLineView;
		this.setBackground( new Color( 221, 221, 221 ) );
		this.setForeground( Color.DARK_GRAY );
	}

	@Override
	public Dimension getPreferredSize() {
		return DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), ( JTimeLinePoseMarker.SIZE.height - 0 ) * 2 );
	}

	private int calculateTimeMarkerOffset() {
		return TimeLineLayout.calculateCenterXForJTimeLinePoseMarker( this, getComposite().getTimeLine().getCurrentTime() / getComposite().getTimeLine().getEndTime() );
	}

	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );

		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = new edu.cmu.cs.dennisc.java.awt.GraphicsContext();
		gc.pushAll( g2 );
		try {
			gc.pushAndSetAntialiasing( true );
			gc.pushAndSetTextAntialiasing( true );
			gc.pushPaint();

			int height = this.getHeight();
			int minY = ( height * 2 ) / 5;
			int maxY = height - minY;
			int centerY = ( minY + maxY ) / 2;

			int minX = TimeLineLayout.calculateMinX( this );
			int maxX = TimeLineLayout.calculateMaxX( this );

			g.setColor( Color.DARK_GRAY );
			g.drawLine( minX, minY, minX, maxY );
			g.drawLine( maxX, minY, maxX, maxY );
			g.drawLine( minX, centerY, maxX, centerY );

			java.awt.geom.AffineTransform prevTransform = g2.getTransform();

			java.awt.Paint timePaint;
			if( this.isTimeSliding || ( this.isWithinTimeMarker && this.isMousePressed ) ) {
				timePaint = PRESSED_PAINT;
			} else {
				if( this.isWithinTimeMarker ) {
					timePaint = Color.LIGHT_GRAY;
				} else {
					timePaint = Color.WHITE;
				}
			}
			int xTimeMarker = this.calculateTimeMarkerOffset();
			g2.translate( xTimeMarker, centerY );
			g2.setPaint( timePaint );
			g2.fill( ARROW );
			g2.setPaint( Color.BLACK );
			g2.draw( ARROW );
			g2.setTransform( prevTransform );

			int yText = centerY + ARROW.getBounds().height;

			double currentTime = this.composite.getTimeLine().getCurrentTime();
			String currentTimeText = edu.cmu.cs.dennisc.java.lang.DoubleUtilities.format( currentTime, CENTI_FORMAT );

			java.awt.FontMetrics fm = g.getFontMetrics();
			int messageWidth = fm.stringWidth( currentTimeText );
			int ascent = fm.getMaxAscent();
			g.drawString( currentTimeText, xTimeMarker - ( messageWidth / 2 ), yText + ascent );
			//			g.setColor( Color.WHITE );
			//			g2.translate( TimeLineLayout.calculateCenterXForJTimeLinePoseMarker( this, 1 ), centerY );
			//			g2.fill( D_ARROW );
			//			g2.setTransform( prevTransform );
		} finally {
			gc.popAll();
		}

	}

	//	private static java.awt.Shape createEndOfTimeLineArrows() {
	//		final int HEIGHT = 10;
	//		final int WIDTH = 16;
	//		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
	//		rv.moveTo( 1, 1 );
	//		rv.lineTo( 1, HEIGHT );
	//		rv.lineTo( WIDTH, 0 );
	//		rv.lineTo( 1, -HEIGHT );
	//		rv.lineTo( 1, -1 );
	//		rv.lineTo( -1, -1 );
	//		rv.lineTo( -1, -HEIGHT );
	//		rv.lineTo( -WIDTH, 0 );
	//		rv.lineTo( -1, HEIGHT );
	//		rv.lineTo( -1, 1 );
	//		rv.closePath();
	//		//		rv.lineTo( 1, 1 );
	//		return rv;
	//	}

	private static java.awt.Shape createArrow() {
		final int HALF_ARROW = 8;
		final int ARROW_HEIGHT = 24;
		java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
		rv.moveTo( 0, 0 );
		rv.lineTo( HALF_ARROW, ARROW_HEIGHT );
		rv.lineTo( -HALF_ARROW, ARROW_HEIGHT );
		rv.closePath();
		return rv;
	}

	public TimeLineComposite getComposite() {
		return composite;
	}

	public double getTime( MouseEvent e ) {
		int x = e.getLocationOnScreen().x - this.getLocationOnScreen().x;
		return ( (TimeLineLayout)getLayout() ).calculateTimeForX( x, component.getAwtComponent() );
	}

	private void setWithinTimeMarker( boolean isWithinTimeMarker ) {
		if( this.isWithinTimeMarker != isWithinTimeMarker ) {
			this.isWithinTimeMarker = isWithinTimeMarker;
			this.repaint();
		}
	}

	private void setMousePressed( boolean isMousePressed ) {
		if( this.isMousePressed != isMousePressed ) {
			this.isMousePressed = isMousePressed;
			this.repaint();
		}
	}

	private final TimeLineComposite composite;
	private final TimeLineView component;
	private boolean isTimeSliding = false;
	//private boolean isEndSliding = false;
	private boolean isWithinTimeMarker = false;
	private boolean isMousePressed = false;
	private final MouseMotionListener mmlAdapter = new MouseMotionListener() {

		private void handleTimeMarker( MouseEvent e ) {
			int height = e.getComponent().getHeight();
			int minY = ( height * 2 ) / 5;
			int maxY = height - minY;
			int centerY = ( minY + maxY ) / 2;

			int xTimeMarker = calculateTimeMarkerOffset();

			int x = e.getX() - xTimeMarker;
			int y = e.getY() - centerY;

			setWithinTimeMarker( ARROW.contains( x, y ) );
		}

		@Override
		public void mouseMoved( MouseEvent e ) {
			this.handleTimeMarker( e );
		}

		@Override
		public void mouseDragged( MouseEvent e ) {
			this.handleTimeMarker( e );
			if( isTimeSliding ) { //|| isEndSliding ) {
				double calculateTimeForX = ( (TimeLineLayout)getLayout() ).calculateTimeForX( e.getPoint().x, component.getAwtComponent() );
				if( isTimeSliding ) {
					if( isTimeSliding ) {
						getComposite().getTimeLine().setCurrentTime( calculateTimeForX );
					}
					//				} else if( isEndSliding ) {
					//					getComposite().getTimeLine().setEndTime( calculateTimeForX );
				}
			}
		}
	};

	private final MouseListener mlAdapter = new MouseListener() {

		private double prevCurrTime = 0;
		private double prevEndTime = 10;//getComposite().getTimeLine().getEndTime();

		@Override
		public void mousePressed( MouseEvent e ) {
			prevCurrTime = getComposite().getTimeLine().getCurrentTime();
			prevEndTime = getComposite().getTimeLine().getEndTime();
			Point locationOnScreen = e.getPoint();
			double deltax = ( (TimeLineLayout)getLayout() ).calculateTimeForX( locationOnScreen.x, JTimeLineView.this ) / getComposite().getTimeLine().getEndTime();
			locationOnScreen.x = (int)( deltax );
			locationOnScreen.y = locationOnScreen.y - ( getHeight() / 2 );

			isTimeSliding = ( ARROW.contains( locationOnScreen ) && getComposite().getIsTimeMutable() );
			//			isEndSliding = ( D_ARROW.contains( locationOnScreen ) );
			//			if( isEndSliding ) {
			//				System.out.println( "the end is nigh" );
			//			}
			setMousePressed( true );
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			setMousePressed( false );
			if( isTimeSliding ) {
				org.lgna.croquet.history.TransactionHistory history = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
				org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( null, NullTrigger.createUserInstance() );
				if( isTimeSliding ) {
					step.commitAndInvokeDo( new CurrentTimeLineTimeChangeEdit( step, getComposite().getTimeLine(), getComposite().getTimeLine().getCurrentTime(), prevCurrTime ) );
					isTimeSliding = false;
				}
				if( isTimeSliding ) {
					composite.selectKeyFrame( null );
				}
				//				if( isEndSliding ) {
				//					step.commitAndInvokeDo( new EndTimeLineTimeChangeEdit( step, getComposite().getTimeLine(), getComposite().getTimeLine().getEndTime(), prevEndTime ) );
				//				}
			}
		}

		@Override
		public void mouseEntered( MouseEvent e ) {
		}

		@Override
		public void mouseExited( MouseEvent e ) {
			setWithinTimeMarker( false );
		}

		@Override
		public void mouseClicked( MouseEvent e ) {
		}
	};
}
