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
package org.lgna.ik.poser.animation;

import java.util.Comparator;
import java.util.List;

import org.lgna.ik.poser.Pose;
import org.lgna.story.AnimationStyle;

import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class TimeLine {

	private double currentTime = 0;
	private double endTime = 10;
	//I used datas here, get over it
	private List<KeyFrameData> datas = Collections.newArrayList();
	private List<TimeLineListener> listeners = Collections.newArrayList();
	private int index;

	public void addKeyFrameData( KeyFrameData keyFrameData ) {
		if( datas.size() == 0 ) {
			datas.add( keyFrameData );
			fireKeyFrameAdded( keyFrameData );
			return;
		}
		for( int i = 0; i != datas.size(); ++i ) {
			if( datas.get( i ).getEventTime() > keyFrameData.getEventTime() ) {
				datas.add( i, keyFrameData );
				fireKeyFrameAdded( keyFrameData );
				return;
			}
		}
		datas.add( keyFrameData );
		fireKeyFrameAdded( keyFrameData );
	}

	public void addKeyFrameData( Pose pose, double time ) {
		addKeyFrameData( new KeyFrameData( time, pose ) );
	}

	public void addKeyFrameData( Pose pose ) {
		addKeyFrameData( pose, currentTime );
	}

	public void addListener( TimeLineListener listener ) {
		this.listeners.add( listener );
	}

	public void removeListener( TimeLineListener listener ) {
		this.listeners.remove( listener );
	}

	public void moveExistingKeyFrameData( KeyFrameData data, double newTime ) {
		if( ( newTime > 0 ) && ( newTime < endTime ) ) {
			data.setTime( newTime );
			java.util.Collections.sort( datas, new Comparator<KeyFrameData>() {
				public int compare( KeyFrameData o1, KeyFrameData o2 ) {
					return new Double( o1.getEventTime() ).compareTo( o2.getEventTime() );
				}
			} );
		}
	}

	public void modifyExistingPose( KeyFrameData data, Pose newPose ) {
		data.setPose( newPose );
		fireKeyFrameModified( data );
	}

	public void removeKeyFrameData( KeyFrameData item ) {
		datas.remove( item );
		fireKeyFrameDeleted( item );
	}

	public void setCurrentTime( double currentTime ) {
		if( currentTime > endTime ) {
			currentTime = endTime;
		}
		if( currentTime < 0 ) {
			currentTime = 0;
		}
		this.currentTime = currentTime;
		fireCurrentTimeChanged( currentTime );
		fireSelectedKeyFrameChanged( getFrameForCurrentTime() );
	}

	public void setEndTime( double endTime ) {
		if( ( endTime > 0 ) && ( endTime > datas.get( datas.size() - 1 ).getEventTime() ) ) {
			this.endTime = endTime;
			fireEndTimeChanged( endTime );
			if( this.endTime < currentTime ) {
				setCurrentTime( this.endTime );
			}
		}
	}

	private void fireKeyFrameAdded( KeyFrameData item ) {
		for( TimeLineListener listener : listeners ) {
			listener.keyFrameAdded( item );
		}
	}

	private void fireKeyFrameModified( KeyFrameData data ) {
		for( TimeLineListener listener : listeners ) {
			listener.keyFrameModified( data );
		}
	}

	private void fireKeyFrameDeleted( KeyFrameData item ) {
		for( TimeLineListener listener : listeners ) {
			listener.keyFrameDeleted( item );
		}
	}

	private void fireCurrentTimeChanged( double time ) {
		for( TimeLineListener listener : listeners ) {
			listener.currentTimeChanged( time );
		}
	}

	private void fireEndTimeChanged( double endTime ) {
		for( TimeLineListener listener : listeners ) {
			listener.endTimeChanged( endTime );
		}
	}

	private void fireSelectedKeyFrameChanged( KeyFrameData item ) {
		for( TimeLineListener listener : listeners ) {
			listener.selectedKeyFrameChanged( item );
		}
	}

	public double getDurationForKeyFrame( KeyFrameData data ) {
		for( int i = 0; i != datas.size(); ++i ) {
			if( datas.get( index ).equals( data ) ) {
				if( i == 0 ) {
					return data.getEventTime();
				} else {
					return data.getEventTime() - datas.get( i - 1 ).getEventTime();
				}
			}
		}
		assert false : data;
		return 0;
	}

	public AnimationStyle getStyleForKeyFramePose( KeyFrameData data ) {
		for( int i = 0; i != datas.size(); ++i ) {
			KeyFrameData prev = null;
			if( i != 0 ) {
				prev = datas.get( i - 1 );
			}
			KeyFrameData itr = datas.get( i );
			KeyFrameStyles.getAnimationStyleFromTwoKeyFramStyles( prev.getEventStyle(), itr.getEventStyle() );
		}
		throw new RuntimeException( "Pose Not Found:" + data );
	}

	public double getEndTime() {
		return endTime;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public List<KeyFrameData> getKeyFrames() {
		return datas;
	}

	public KeyFrameData getFrameForCurrentTime() {
		for( KeyFrameData frame : datas ) {
			if( frame.getEventTime() == currentTime ) {
				return frame;
			} else if( frame.getEventTime() > currentTime ) {
				return null;
			}
		}
		return null;
	}

	public void setSelectedKeyFrame( KeyFrameData keyFrameData ) {
		this.setCurrentTime( keyFrameData.getEventTime() );
		fireSelectedKeyFrameChanged( keyFrameData );
	}
}
