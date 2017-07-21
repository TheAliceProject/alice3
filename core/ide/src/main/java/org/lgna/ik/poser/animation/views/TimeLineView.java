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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import org.lgna.croquet.views.Panel;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.TimeLineListener;
import org.lgna.ik.poser.animation.composites.TimeLineComposite;
import org.lgna.story.Pose;

/**
 * @author Matt May
 */

public class TimeLineView extends Panel {

	private JTimeLineView jView;
	private final Map<KeyFrameData, JTimeLinePoseMarker> map = edu.cmu.cs.dennisc.java.util.Maps.newConcurrentHashMap();

	public TimeLineView( TimeLineComposite composite ) {
		super( composite );
		composite.getTimeLine().addListener( listener );
	}

	private TimeLineListener listener = new TimeLineListener() {

		private ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				select( ( (JTimeLinePoseMarker)e.getSource() ).getKeyFrameData() );
				JTimeLinePoseMarker source = (JTimeLinePoseMarker)e.getSource();
				KeyFrameData data = source.getKeyFrameData();
				if( ( (TimeLineComposite)getComposite() ).getSelectedKeyFrame() == data ) {
					// do nothing
				} else {
					( (TimeLineComposite)getComposite() ).selectKeyFrame( data );
				}
			}
		};

		@Override
		public void selectedKeyFrameChanged( KeyFrameData event ) {
			select( event );
			revalidateAndRepaint();
		}

		@Override
		public void keyFrameModified( KeyFrameData event ) {
			revalidateAndRepaint();
		}

		@Override
		public void keyFrameDeleted( KeyFrameData event ) {
			jView.remove( map.get( event ) );
			revalidateAndRepaint();
		}

		@Override
		public void keyFrameAdded( KeyFrameData event ) {
			JTimeLinePoseMarker comp = new JTimeLinePoseMarker( event, jView );
			comp.addActionListener( actionListener );
			map.put( event, comp );
			jView.add( comp );
			revalidateAndRepaint();
		}

		@Override
		public void endTimeChanged( double endTime ) {
			revalidateAndRepaint();
		}

		@Override
		public void currentTimeChanged( double currentTime, Pose pose ) {
			revalidateAndRepaint();
		}
	};

	private void select( KeyFrameData selected ) {

		List<KeyFrameData> keyFrames = ( (TimeLineComposite)getComposite() ).getTimeLine().getKeyFrames();
		for( KeyFrameData data : keyFrames ) {
			JTimeLinePoseMarker button = map.get( data );
			if( button != null ) {
				if( button.getKeyFrameData() != selected ) {
					button.setSelected( false );
				} else {
					button.setSelected( true );
				}
			}
		}
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jPanel ) {
		return new TimeLineLayout( (TimeLineComposite)getComposite() );
	}

	@Override
	protected JPanel createJPanel() {
		jView = new JTimeLineView( this );
		return jView;
	}

	public void deselect( KeyFrameData selected ) {
		( (AbstractButton)map.get( selected ) ).setSelected( false );
	}

	@Override
	public void revalidateAndRepaint() {
		super.revalidateAndRepaint();
		jView.revalidate();
		jView.repaint();
	}
}

class TimeLineLayout implements LayoutManager {
	private final TimeLineComposite composite;

	public TimeLineLayout( TimeLineComposite masterComposite ) {
		this.composite = masterComposite;
	}

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
		double rv = ( ( xActual - xMin ) / ( xMax - xMin ) ) * composite.getTimeLine().getEndTime();
		return rv;
	}

	public static int calculateLeftXForJTimeLinePoseMarker( Container parent, double portion ) {
		return calculateCenterXForJTimeLinePoseMarker( parent, portion ) - ( JTimeLinePoseMarker.SIZE.width / 2 );
	}

	@Override
	public void layoutContainer( Container parent ) {
		assert parent instanceof JTimeLineView;
		for( Component child : parent.getComponents() ) {
			if( child instanceof JTimeLinePoseMarker ) {
				JTimeLinePoseMarker jMarker = (JTimeLinePoseMarker)child;
				double time = jMarker.getKeyFrameData().getEventTime();
				int x = calculateLeftXForJTimeLinePoseMarker( parent, time / composite.getTimeLine().getEndTime() );
				child.setLocation( x, 0 );
				child.setSize( child.getPreferredSize() );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( child );
			}
		}
	}

	@Override
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

	@Override
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

	@Override
	public void removeLayoutComponent( Component comp ) {
	}

	@Override
	public void addLayoutComponent( String name, Component comp ) {
	}

}
