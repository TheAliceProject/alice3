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
package org.lgna.ik.poser.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.Transient;

import org.lgna.croquet.components.JComponent;
import org.lgna.ik.poser.TimeLine;
import org.lgna.ik.poser.TimeLine.PoseEvent;
import org.lgna.ik.poser.events.TimeLineListener;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;

class JTimeLineView extends javax.swing.JComponent {
	private final TimeLine timeLine;
	private final TimeLineListener timeLineListener = new TimeLineListener() {
		public void changed() {
			repaint();
		}
	};

	public int getTimeFromLocation( int x ) {
		int time = (int)( ( (double)( x - getXMin() ) / ( getXMax() - getXMin() ) ) * ( timeLine.getEndTime() - timeLine.getStartTime() ) );
		if( time < timeLine.getStartTime() ) {
			return timeLine.getStartTime();
		}
		if( time > timeLine.getEndTime() ) {
			return timeLine.getEndTime();
		}
		return time;
	}

	public int getXforTime( int time ) {
		return (int)( ( (double)( time - timeLine.getStartTime() ) / ( (double)timeLine.getEndTime() - timeLine.getStartTime() ) )
				* ( getXMax() - getXMin() ) ) + getXMin();
	}

	private int getXMin() {
		return ( getWidth() * 1 ) / 32;
	}

	private int getXMax() {
		return getWidth() - ( ( getWidth() * 1 ) / 24 );
	}

	public JTimeLineView( TimeLine timeLine ) {
		this.timeLine = timeLine;
		this.timeLine.addTimeLineListener( this.timeLineListener );
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		return DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), 100 );
	}

	@Override
	public void paint( Graphics g ) {
		super.paint( g );
		g.setColor( Color.RED );
		g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), this.getHeight() / 4, this.getHeight() / 4 );
		g.setColor( Color.BLACK );
		g.drawLine( getXMin(), (int)( getHeight() * .5 ), getXMax(), (int)( getHeight() * .5 ) );
		g.drawLine( getXMin(), (int)( ( getHeight() * .5 ) + ( getHeight() / 16 ) ), getXMin(), (int)( ( getHeight() * .5 ) - ( getHeight() / 16 ) ) );
		g.drawLine( getXMax(), (int)( ( getHeight() * .5 ) + ( getHeight() / 16 ) ), getXMax(), (int)( ( getHeight() * .5 ) - ( getHeight() / 16 ) ) );
		Point[] pArr = this.getPointsForSlider();
		int[] xArr = new int[ pArr.length ];
		int[] yArr = new int[ pArr.length ];
		for( int i = 0; i != pArr.length; ++i ) {
			xArr[ i ] = pArr[ i ].x;
			yArr[ i ] = pArr[ i ].y;
		}
		g.fillPolygon( xArr, yArr, pArr.length );
		for( PoseEvent o : timeLine.getPosesInTimeline() ) {
			int x = getXforTime( o.getEventTime() );
			g.drawLine( x, 0, x, getHeight() );
		}
	}

	private Point[] getPointsForSlider() {
		Point arrow = new Point( getXforTime( timeLine.getCurrentTime() ), ( this.getHeight() ) / 2 );
		Point leftBase = new Point( arrow.x + ( this.getWidth() / 32 ), ( this.getHeight() * 3 ) / 4 );
		Point rightBase = new Point( arrow.x - ( this.getWidth() / 32 ), ( this.getHeight() * 3 ) / 4 );
		int[] xPoints = { arrow.x, leftBase.x, rightBase.x };
		int[] yPoints = { arrow.y, leftBase.y, rightBase.y };
		Point[] rv = { arrow, leftBase, rightBase };
		return rv;
	}

	public void updateSlider( MouseEvent e ) {
		this.timeLine.setCurrentTime( getTimeFromLocation( e.getLocationOnScreen().x ) );
	}

	public boolean isClickingOnSlider( Point locationOnScreen ) {

		return false;
	}
}

/**
 * @author Matt May
 */
public class TimeLineView extends JComponent<JTimeLineView> implements MouseListener, MouseMotionListener {
	private final TimeLine timeLine;
	private boolean isSliding = false;

	public TimeLineView( TimeLine timeLine ) {
		this.timeLine = timeLine;
		addMouseMotionListener( this );
		addMouseListener( this );
	}

	@Override
	protected JTimeLineView createAwtComponent() {
		return new JTimeLineView( this.timeLine );
	}

	public void mouseClicked( MouseEvent e ) {
	}

	public void mousePressed( MouseEvent e ) {
		if( getAwtComponent().isClickingOnSlider( e.getLocationOnScreen() ) ) {
			isSliding = true;
		}
	}

	public void mouseReleased( MouseEvent e ) {
		isSliding = false;
	}

	public void mouseEntered( MouseEvent e ) {
	}

	public void mouseExited( MouseEvent e ) {
	}

	public void mouseDragged( MouseEvent e ) {
		if( isSliding ) {
			this.getAwtComponent().updateSlider( e );
		}
	}

	public void mouseMoved( MouseEvent e ) {
	}
}
