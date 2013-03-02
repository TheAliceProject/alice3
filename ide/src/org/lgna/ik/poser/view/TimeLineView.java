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
		g.fillOval( 0, 0, this.getWidth(), this.getHeight() );
		int start = timeLine.getStartTime();
		int end = timeLine.getEndTime();
		for( PoseEvent o : timeLine.getPosesInTimeline() ) {
			int x = calculateX( start, o.getEventTime(), end );
			g.drawLine( x, 0, x, getHeight() );
		}
	}

	private int calculateX( int start, int eventTime, int end ) {
		return 0;//(( double ) end - start ) / 
	}
}

/**
 * @author Matt May
 */
public class TimeLineView extends JComponent<JTimeLineView> {
	private final TimeLine timeLine;

	public TimeLineView( TimeLine timeLine ) {
		this.timeLine = timeLine;
	}

	@Override
	protected JTimeLineView createAwtComponent() {
		return new JTimeLineView( this.timeLine );
	}
}
