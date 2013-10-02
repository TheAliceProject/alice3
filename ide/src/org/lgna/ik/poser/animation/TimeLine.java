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
import java.util.Map;

import org.lgna.ik.poser.pose.JointKey;
import org.lgna.ik.poser.pose.Pose;
import org.lgna.ik.poser.pose.builder.PoseBuilder;
import org.lgna.story.AnimationStyle;
import org.lgna.story.SBiped;
import org.lgna.story.resources.JointId;

import com.sun.tools.javac.util.Pair;

import edu.cmu.cs.dennisc.java.util.Collections;
import edu.cmu.cs.dennisc.math.Orientation;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */
public class TimeLine {

	private double currentTime = 0;
	private double endTime = 10;
	//I used datas here, get over it
	private List<KeyFrameData> datas = Collections.newArrayList();
	private List<TimeLineListener> listeners = Collections.newArrayList();
	private Pose<SBiped> initialPose;

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

	public void addKeyFrameData( Pose<?> pose, double time ) {
		addKeyFrameData( new KeyFrameData( time, pose ) );
	}

	public void addKeyFrameData( Pose<?> pose ) {
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

	public void modifyExistingPose( KeyFrameData data, Pose<?> newPose ) {
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
		if( endTime > 0 ) {
			if( ( datas.size() == 0 ) || ( endTime > datas.get( datas.size() - 1 ).getEventTime() ) ) {
				this.endTime = endTime;
				fireEndTimeChanged( endTime );
				if( this.endTime < currentTime ) {
					setCurrentTime( this.endTime );
				}
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
		Pose<?> pose = calculatePoseForTime( time );
		for( TimeLineListener listener : listeners ) {
			listener.currentTimeChanged( time, pose );
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
			if( datas.get( i ) == data ) {
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
		KeyFrameData prev = null;
		for( int i = 0; i != datas.size(); ++i ) {
			if( datas.get( i ) == data ) {
				if( i != 0 ) {
					prev = datas.get( i - 1 );
				}
				KeyFrameData itr = datas.get( i );
				return KeyFrameStyles.getAnimationStyleFromTwoKeyFramStyles( prev != null ? prev.getEventStyle() : KeyFrameStyles.ARRIVE_AND_EXIT_GENTLY, itr.getEventStyle() );
			}
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

	private Pose<?> calculatePoseForTime( double desiredTime ) {
		KeyFrameData before = null;
		KeyFrameData after = null;

		for( KeyFrameData data : datas ) {
			if( desiredTime >= data.getEventTime() ) {
				before = data;
			} else {
				after = data;
				break;
			}
		}

		if( ( before == null ) && ( after == null ) ) {
			return null;
		} else if( before == null ) {
			return after.getPose();
		} else if( after == null ) {
			return before.getPose();
		} else {
			return interpolatePoses( before, after, desiredTime );
		}
	}

	public void refresh() {
		while( !datas.isEmpty() ) {
			removeKeyFrameData( datas.get( 0 ) );
		}
		setCurrentTime( 0 );
		setEndTime( 10 );
	}

	private Pose<?> interpolatePoses( KeyFrameData key1, KeyFrameData key2, double targetTime ) {

		//TODO "Easing is not implemented, yet.";

		//		if( true || ( !key1.getEventStyle().getIsSlowOutDesired() && !key2.getEventStyle().getIsSlowInDesired() ) ) {
		Pose<?> init = getInitPoseIfNecessary( key1, key2 );
		//		Pose<?> pose1 = key1.getPose();
		Pose<?> pose2 = key2.getPose();
		Map<JointId, Pair<Orientation, Orientation>> map = Collections.newInitializingIfAbsentHashMap();
		for( JointKey key : init.getJointKeys() ) {
			map.put( key.getJointId(), new Pair<Orientation, Orientation>( key.getOrientation(), null ) );
		}
		for( JointKey key : pose2.getJointKeys() ) {
			Pair<Orientation, Orientation> pair = map.get( key.getJointId() );
			if( pair != null ) {
				assert pair.fst != null;
				assert pair.snd == null;
				map.put( key.getJointId(), new Pair<Orientation, Orientation>( pair.fst, key.getOrientation() ) );
			} else {
				map.put( key.getJointId(), new Pair<Orientation, Orientation>( null, key.getOrientation() ) );
				throw new RuntimeException( "UNHANDLED: no policy yet set for how to handle animation with no initial orientation" );
			}
		}
		init.getJointKeys();
		double k = ( targetTime - key1.getEventTime() ) / ( key2.getEventTime() - key1.getEventTime() );
		List<JointKey> builderList = Collections.newArrayList();
		for( JointId joint : map.keySet() ) {
			UnitQuaternion rightAnkleUnitQuaternion = UnitQuaternion.createInterpolation( new UnitQuaternion( map.get( joint ).fst.createOrthogonalMatrix3x3() ), new UnitQuaternion( map.get( joint ).snd.createOrthogonalMatrix3x3() ), k );
			builderList.add( new JointKey( rightAnkleUnitQuaternion.createOrthogonalMatrix3x3(), joint ) );
		}
		PoseBuilder<?> builder = init.getBuilder();
		for( JointKey key : builderList ) {
			builder.addCustom( key.getLGNAOrientation(), key.getJointId() );
		}
		return builder.build();
	}

	private Pose<?> getInitPoseIfNecessary( KeyFrameData key1, KeyFrameData key2 ) {
		List<JointKey> rvKeys = Collections.newArrayList();
		List<JointId> unhandledIds = Collections.newArrayList();
		PoseBuilder<?> builder = key1.getPose().getBuilder();
		for( JointKey jointKey : key2.getPose().getJointKeys() ) {
			rvKeys.add( jointKey );
			unhandledIds.add( jointKey.getJointId() );
		}
		for( JointKey jointKey : key1.getPose().getJointKeys() ) {
			unhandledIds.remove( jointKey.getJointId() );
		}
		if( unhandledIds.isEmpty() ) {
			return key1.getPose();
		} else {
			return getInitPose( getPriorKeyFrame( key1 ), builder, unhandledIds, rvKeys );
		}
	}

	private Pose<?> getInitPose( KeyFrameData priorKeyFrame, PoseBuilder<?> builder, List<JointId> unhandledIds, List<JointKey> rvKeys ) {
		Pose<?> prevPose = initialPose;
		if( priorKeyFrame != null ) {
			prevPose = priorKeyFrame.getPose();
		}
		List<JointId> handledIds = Collections.newArrayList();
		for( JointId id : unhandledIds ) {
			for( JointKey key : prevPose.getJointKeys() ) {
				if( key.getJointId().equals( id ) ) {
					handledIds.add( id );
					rvKeys.add( key );
				}
			}
		}
		for( JointId id : handledIds ) {
			unhandledIds.remove( id );
		}
		if( unhandledIds.isEmpty() ) {
			for( JointKey key : rvKeys ) {
				builder.addCustom( key.getLGNAOrientation(), key.getJointId() );
			}
			Pose<?> rv = builder.build();
			return rv;
		} else {
			return getInitPose( getPriorKeyFrame( priorKeyFrame ), builder, unhandledIds, rvKeys );
		}
	}

	private KeyFrameData getPriorKeyFrame( KeyFrameData key1 ) {
		KeyFrameData rv = null;
		for( KeyFrameData frame : datas ) {
			if( frame.getEventTime() < key1.getEventTime() ) {
				rv = frame;
			} else {
				break;
			}
		}
		return rv;
	}

	public void setInitialPose( Pose<SBiped> pose ) {
		this.initialPose = pose;
	}
}
