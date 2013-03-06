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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import org.lgna.croquet.BooleanState;
import org.lgna.croquet.components.BooleanStateButton;
import org.lgna.croquet.components.CustomRadioButtons;
import org.lgna.ik.poser.TimeLineComposite;
import org.lgna.ik.poser.TimeLineComposite.PoseEvent;
import org.lgna.ik.poser.events.TimeLineListener;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;

class TimeLineLayout implements LayoutManager {
	public static int calculateMinX( Container parent ) {
		java.awt.Insets insets = parent.getInsets();
		return insets.left + ( JTimeLinePoseMarker.SIZE.width / 2 );
	}

	public static int calculateMaxX( Container parent ) {
		java.awt.Insets insets = parent.getInsets();
		return parent.getWidth() - insets.right - ( JTimeLinePoseMarker.SIZE.width / 2 );
	}

	public static int calculateCenterXForJTimeLinePoseMarker( Container parent, double portion ) {
		int minX = calculateMinX( parent );
		int maxX = calculateMaxX( parent );

		double x = ( ( maxX - minX ) * portion ) + minX;

		return (int)Math.round( x );
	}

	public double calculateTimeForX( int x, Container parent ) {
		double xActual = x;
		double xMin = calculateMinX( parent );
		double xMax = calculateMaxX( parent );
		double rv = ( ( xActual - xMin ) / ( xMax - xMin ) ) * timeLine.getEndTime();
		return rv;
	}

	public static int calculateLeftXForJTimeLinePoseMarker( Container parent, double portion ) {
		return calculateCenterXForJTimeLinePoseMarker( parent, portion ) - ( JTimeLinePoseMarker.SIZE.width / 2 );
	}

	private final TimeLineComposite timeLine;

	public TimeLineLayout( TimeLineComposite masterComposite ) {
		super();
		this.timeLine = masterComposite;
	}

	public void layoutContainer( Container parent ) {
		for( Component child : parent.getComponents() ) {
			if( child instanceof JTimeLinePoseMarker ) {
				JTimeLinePoseMarker jMarker = (JTimeLinePoseMarker)child;
				double time = jMarker.getTimeLinePoseMarker().getItem().getEventTime();
				int x = calculateLeftXForJTimeLinePoseMarker( parent, time / timeLine.getEndTime() );
				child.setLocation( x, 0 );
				child.setSize( child.getPreferredSize() );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( child );
			}
		}
	}

	public Dimension minimumLayoutSize( Container parent ) {
		Component[] children = parent.getComponents();
		int width = 0;
		int height = 0;
		for( Component component : children ) {
			if( component.getMinimumSize().width > width ) {
				width = component.getMinimumSize().width;
			}
			if( component.getMinimumSize().height > height ) {
				height = component.getMinimumSize().height;
			}
		}
		return new Dimension( width, height );
	}

	public Dimension preferredLayoutSize( Container parent ) {
		Component[] children = parent.getComponents();
		int width = 0;
		int height = 0;
		for( Component component : children ) {
			if( component.getPreferredSize().width > width ) {
				width = component.getPreferredSize().width;
			}
			if( component.getPreferredSize().height > height ) {
				height = component.getPreferredSize().height;
			}
		}
		return new Dimension( width, height );
	}

	public void removeLayoutComponent( Component comp ) {
	}

	public void addLayoutComponent( String name, Component comp ) {
	}

}

/**
 * @author Matt May
 */

public class TimeLineView extends CustomRadioButtons<PoseEvent> {

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

	private static final java.awt.Shape ARROW = createArrow();

	private class JTimeLineView extends JItemSelectablePanel {
		private final TimeLineComposite timeLine;
		private final TimeLineListener timeLineListener = new TimeLineListener() {
			public void changed() {
				repaint();
			}
		};

		public JTimeLineView( TimeLineComposite timeLine ) {
			this.timeLine = timeLine;
			this.timeLine.addTimeLineListener( this.timeLineListener );
			this.addMouseListener( mlAdapter );
			this.addMouseMotionListener( mmlAdapter );
		}

		@Override
		public Dimension getPreferredSize() {
			return DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), 100 );
		}

		@Override
		public void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.setColor( Color.RED );
			g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), this.getHeight() / 4, this.getHeight() / 4 );

			int minY = ( this.getHeight() * 2 ) / 5;
			int maxY = this.getHeight() - minY;
			int centerY = ( minY + maxY ) / 2;

			int minX = TimeLineLayout.calculateMinX( this );
			int maxX = TimeLineLayout.calculateMaxX( this );

			g.setColor( Color.BLACK );
			g.drawLine( minX, minY, minX, maxY );
			g.drawLine( maxX, minY, maxX, maxY );
			g.drawLine( minX, centerY, maxX, centerY );

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;

			int currentTimeX = TimeLineLayout.calculateCenterXForJTimeLinePoseMarker( this, timeLine.getCurrentTime() / timeLine.getEndTime() );

			java.awt.geom.AffineTransform prevTransform = g2.getTransform();

			g2.translate( currentTimeX, centerY );
			g2.fill( ARROW );
			g2.setTransform( prevTransform );

			//			for( PoseEvent poseEvent : timeLine.getPosesInTimeline() ) {
			//				int poseX = TimeLineLayout.calculateCenterXForJTimeLinePoseMarker( this, poseEvent.getEventTime() / timeLine.getEndTime() );
			//				g.drawLine( poseX, 0, poseX, getHeight() );
			//			}
		}

		private boolean isSliding = false;

		MouseMotionListener mmlAdapter = new MouseMotionListener() {

			public void mouseMoved( MouseEvent e ) {
			}

			public void mouseDragged( MouseEvent e ) {
				if( isSliding ) {
					timeLine.setCurrentTime( ( (TimeLineLayout)getLayout() ).calculateTimeForX( e.getPoint().x, TimeLineView.this.getAwtComponent() ) );
				}
			}
		};

		MouseListener mlAdapter = new MouseListener() {

			public void mouseReleased( MouseEvent e ) {
				isSliding = false;
			}

			public void mousePressed( MouseEvent e ) {
				Point locationOnScreen = e.getPoint();
				double deltax = ( (TimeLineLayout)getLayout() ).calculateTimeForX( locationOnScreen.x, JTimeLineView.this ) / timeLine.getEndTime();
				locationOnScreen.x = (int)( deltax );
				locationOnScreen.y = locationOnScreen.y - ( getHeight() / 2 );
				isSliding = ARROW.contains( locationOnScreen );
			}

			public void mouseExited( MouseEvent e ) {
			}

			public void mouseEntered( MouseEvent e ) {
			}

			public void mouseClicked( MouseEvent e ) {
			}
		};
	}

	private TimeLineComposite composite;

	public TimeLineView( TimeLineComposite composite ) {
		super( composite.getPoseEventListSelectionState() );
		this.composite = composite;
		composite.setJComponent( this );
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jPanel ) {
		return new TimeLineLayout( composite );
	}

	@Override
	protected void addPrologue( int count ) {
	}

	@Override
	protected void addItem( PoseEvent item, BooleanStateButton<?> button ) {
		this.internalAddComponent( button );
	}

	@Override
	protected void addEpilogue() {
	}

	@Override
	protected BooleanStateButton<?> createButtonForItemSelectedState( PoseEvent item, BooleanState itemSelectedState ) {
		return new TimeLinePoseMarker( itemSelectedState, item );
	}

	@Override
	protected void removeAllDetails() {
		this.internalRemoveAllComponents();
	}

	@Override
	protected JPanel createJPanel() {
		return new JTimeLineView( composite );
	}

}
