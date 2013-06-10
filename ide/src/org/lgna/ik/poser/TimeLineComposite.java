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
package org.lgna.ik.poser;

import java.util.List;

import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.MutableDataListSelectionState;
import org.lgna.croquet.SimpleComposite;
import org.lgna.ik.poser.events.TimeLineListener;
import org.lgna.ik.poser.view.OuterTimeLineView;
import org.lgna.ik.poser.view.TimeLineView;
import org.lgna.story.AnimationStyle;
import org.lgna.story.Duration;
import org.lgna.story.SetPose.Detail;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.concurrent.Collections;

/**
 * @author Matt May
 */
public class TimeLineComposite extends SimpleComposite<OuterTimeLineView> {
	public TimeLineComposite() {
		super( java.util.UUID.fromString( "45b24458-c06e-4480-873a-f1698bf03edb" ) );
	}

	private double currTime = 0;
	private double endTime = 10;

	private final MutableDataListSelectionState<PoseEvent> posesInTimeline = createListSelectionState( createKey( "asdf" ), PoseEvent.class, new ItemCodec<PoseEvent>() {

		public Class<PoseEvent> getValueClass() {
			return PoseEvent.class;
		}

		public PoseEvent decodeValue( BinaryDecoder binaryDecoder ) {
			throw new RuntimeException( "todo" );
		}

		public void encodeValue( BinaryEncoder binaryEncoder, PoseEvent value ) {
			throw new RuntimeException( "todo" );
		}

		public void appendRepresentation( StringBuilder sb, PoseEvent value ) {
			sb.append( value );
		}
	}, -1 );

	private final List<TimeLineListener> listeners = Collections.newCopyOnWriteArrayList();
	private TimeLineView jTimeLineView;

	public class PoseEvent {

		private double eventTime;
		private Pose pose;
		private AnimationStyle style = AnimationStyle.BEGIN_ABRUPTLY_AND_END_GENTLY;

		PoseEvent( double time, Pose pose ) {
			this.eventTime = time;
			this.pose = pose;
		}

		public void setStyle( AnimationStyle style ) {
			this.style = style;
		}

		public double getEventTime() {
			return this.eventTime;
		}

	}

	public void addTimeLineListener( TimeLineListener listener ) {
		this.listeners.add( listener );
	}

	public void removeTimeLineListener( TimeLineListener listener ) {
		this.listeners.remove( listener );
	}

	private void fireChanged() {
		for( TimeLineListener listener : this.listeners ) {
			listener.changed();
		}
	}

	public void addEventAtCurrentTime( Pose pose ) {
		if( getPoseAtGivenTime( this.currTime ) == null ) {
			PoseEvent poseEvent = new PoseEvent( currTime, pose );
			insertPoseEvent( poseEvent );
		}
	}

	private PoseEvent getPoseAtGivenTime( double givenTime ) {
		for( int i = 0; i != posesInTimeline.getItemCount(); ++i ) {
			PoseEvent event = posesInTimeline.getItemAt( i );
			if( event.getEventTime() > givenTime ) {
				return null;
			}
			if( event.getEventTime() == givenTime ) {
				return event;
			}
		}
		return null;
	}

	private void insertPoseEvent( PoseEvent poseEvent ) {
		posesInTimeline.addItem( poseEvent );
		this.fireChanged();
	}

	public Detail[] getParametersForPose( Pose pose ) {
		Detail[] rv = new Detail[ 2 ];//duration, style
		for( int i = 0; i != posesInTimeline.getItemCount(); ++i ) {
			PoseEvent itr = posesInTimeline.getItemAt( i );
			if( itr.pose.equals( pose ) ) {
				rv[ 0 ] = itr.style;
				if( i == 0 ) {
					rv[ 1 ] = new Duration( 0 );
				} else {
					rv[ 1 ] = new Duration( itr.eventTime - itr.eventTime );
				}
			}
		}
		return rv;
	}

	public List<PoseEvent> getPosesInTimeline() {
		return Collections.newCopyOnWriteArrayList( this.posesInTimeline.getData().toArray() );
	}

	public double getEndTime() {
		return endTime;
	}

	public void addMoreTime( double timeToAdd ) {
		endTime += timeToAdd;
	}

	public double getCurrentTime() {
		return currTime;
	}

	public void setCurrentTime( double time ) {
		if( time > endTime ) {
			this.currTime = endTime;
			return;
		}
		if( time < 0 ) {
			this.currTime = 0;
			return;
		}
		this.currTime = time;
		fireChanged();
	}

	@Override
	public OuterTimeLineView createView() {
		return new OuterTimeLineView( this );
	}

	public ListSelectionState<PoseEvent> getPoseEventListSelectionState() {
		return posesInTimeline;
	}

	public int getViewWidth() {
		return jTimeLineView.getWidth();
	}

	public int getViewHeight() {
		return jTimeLineView.getHeight();
	}

	public void setJComponent( TimeLineView jTimeLineView ) {
		this.jTimeLineView = jTimeLineView;
	}
}
