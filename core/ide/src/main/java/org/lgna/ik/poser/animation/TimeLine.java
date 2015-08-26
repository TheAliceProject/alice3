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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.lgna.story.AnimationStyle;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.Pose;
import org.lgna.story.PoseBuilder;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.PoseUtilities;
import org.lgna.story.resources.JointId;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */
public class TimeLine {

	private double currentTime = 0;
	private double endTime = 10;
	//I used datas here, get over it
	private List<KeyFrameData> datas = Lists.newArrayList();
	private List<TimeLineListener> listeners = Lists.newArrayList();
	private Pose<?> initialPose;
	private final List<JointId> usedIds = Lists.newCopyOnWriteArrayList();
	private boolean listenersEnabled = true;
	private KeyFrameData selectedKeyFrame;

	public void addKeyFrameData( KeyFrameData keyFrameData ) {
		if( datas.size() == 0 ) {
			datas.add( keyFrameData );
			fireKeyFrameAdded( keyFrameData );
			checkAddingJoints( keyFrameData );
		} else if( datas.get( 0 ).getEventTime() > keyFrameData.getEventTime() ) {
			datas.add( 0, keyFrameData );
			fireKeyFrameAdded( keyFrameData );
			checkAddingJoints( keyFrameData );
		} else {
			for( int i = 0; i != ( datas.size() - 1 ); ++i ) {
				if( datas.get( i ).getEventTime() < keyFrameData.getEventTime() ) {
					if( datas.get( i + 1 ).getEventTime() > keyFrameData.getEventTime() ) {
						datas.add( i + 1, keyFrameData );
						fireKeyFrameAdded( keyFrameData );
						checkAddingJoints( keyFrameData );
						return;
					}
				}
			}
			datas.add( keyFrameData );
			fireKeyFrameAdded( keyFrameData );
			checkAddingJoints( keyFrameData );
		}
	}

	public void addKeyFrameData( Pose<?> pose, double time ) {
		addKeyFrameData( new KeyFrameData( time, pose ) );
	}

	//	public void addKeyFrameData( Pose<?> pose ) {
	//		addKeyFrameData( pose, currentTime );
	//	}

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
				@Override
				public int compare( KeyFrameData o1, KeyFrameData o2 ) {
					return new Double( o1.getEventTime() ).compareTo( o2.getEventTime() );
				}
			} );
			fireKeyFrameModified( data );
		}
	}

	public void modifyExistingPose( KeyFrameData data, Pose<?> newPose ) {
		data.setPose( newPose );
		checkAddingJoints( data );
		fireKeyFrameModified( data );
	}

	public void removeKeyFrameData( KeyFrameData item ) {
		datas.remove( item );
		checkRemovingJoints();
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
		if( listenersEnabled ) {
			Pose<?> pose = calculatePoseForTime( time );
			for( TimeLineListener listener : listeners ) {
				listener.currentTimeChanged( time, pose );
			}
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
		selectedKeyFrame = keyFrameData;
		if( keyFrameData != null ) {
			this.setCurrentTime( keyFrameData.getEventTime() );
		}
		fireSelectedKeyFrameChanged( keyFrameData );
	}

	public KeyFrameData getSelectedKeyFrame() {
		return this.selectedKeyFrame;
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
			return interpolatePoses( before, after, desiredTime );
			//			return after.getPose();
		} else if( after == null ) {
			return before.getPoseActual();
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
		Pose<?> pose2 = key2.getPoseActual();
		Map<JointId, BeginAndEndQuaternionPair> map = Maps.newInitializingIfAbsentHashMap();
		for( JointId key : usedIds ) {
			map.put( key, new BeginAndEndQuaternionPair( findQuaternionForJointId( key, init ), null ) );
		}
		for( JointId key : usedIds ) {
			BeginAndEndQuaternionPair pair = map.get( key );
			if( pair != null ) {
				assert pair.getStartQuaternion() != null;
				assert pair.getEndQuaternion() == null;
				map.put( key, new BeginAndEndQuaternionPair( pair.getStartQuaternion(), findQuaternionForJointId( key, pose2 ) ) );
			} else {
				map.put( key, new BeginAndEndQuaternionPair( null, findQuaternionForJointId( key, pose2 ) ) );
				throw new RuntimeException( "UNHANDLED: " + key );
			}
		}
		double prevTime = key1 != null ? key1.getEventTime() : 0;
		double k = ( targetTime - prevTime ) / ( key2.getEventTime() - prevTime );
		List<JointIdTransformationPair> builderList = Lists.newArrayList();
		for( JointId joint : map.keySet() ) {
			UnitQuaternion interpolatedQuaternion = UnitQuaternion.createInterpolation( map.get( joint ).getStartQuaternion(), map.get( joint ).getEndQuaternion(), k );
			builderList.add( new JointIdTransformationPair( joint, interpolatedQuaternion ) );
		}
		PoseBuilder<?, ?> builder = PoseUtilities.createBuilderForPoseClass( init.getClass() );
		for( JointIdTransformationPair key : builderList ) {
			EmployeesOnly.addJointIdTransformationPair( builder, key );
		}
		return builder.build();
	}

	private Pose<?> getInitPoseIfNecessary( KeyFrameData key1, KeyFrameData key2 ) {
		if( key1 != null ) {
			//pass
		} else {
			return initialPose;
		}
		List<JointIdTransformationPair> jtPairs = Lists.newArrayList();
		List<JointId> unhandledIds = Lists.newArrayList();
		PoseBuilder<?, ?> builder = PoseUtilities.createBuilderForPoseClass( key1.getPose().getClass() );
		for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( key2.getPoseActual() ) ) {
			jtPairs.add( jtPair );
			unhandledIds.add( jtPair.getJointId() );
		}
		for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( key1.getPoseActual() ) ) {
			unhandledIds.remove( jtPair.getJointId() );
		}
		if( unhandledIds.isEmpty() ) {
			return key1.getPoseActual();
		} else {
			return getInitPose( getPriorKeyFrame( key1 ), builder, unhandledIds, jtPairs );
		}
	}

	private Pose<?> getInitPose( KeyFrameData priorKeyFrame, PoseBuilder<?, ?> builder, List<JointId> unhandledIds, List<JointIdTransformationPair> jtPairs ) {
		Pose<?> prevPose = initialPose;
		if( priorKeyFrame != null ) {
			prevPose = priorKeyFrame.getPose();
		}
		List<JointId> handledIds = Lists.newArrayList();
		for( JointId id : unhandledIds ) {
			for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( prevPose ) ) {
				if( jtPair.getJointId().equals( id ) ) {
					handledIds.add( id );
					jtPairs.add( jtPair );
				}
			}
		}
		for( JointId id : handledIds ) {
			unhandledIds.remove( id );
		}
		if( unhandledIds.isEmpty() ) {
			for( JointIdTransformationPair jtPair : jtPairs ) {
				EmployeesOnly.addJointIdTransformationPair( builder, jtPair );
			}
			Pose<?> rv = builder.build();
			return rv;
		} else {
			return getInitPose( getPriorKeyFrame( priorKeyFrame ), builder, unhandledIds, jtPairs );
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

	private void checkRemovingJoints() {
		if( getNoLongerUsedJoints().isEmpty() ) {
			return;
		} else {
			correctPoseActuals();
		}
	}

	private List<JointId> getNoLongerUsedJoints() {
		ArrayList<JointId> unused = Lists.newArrayList( usedIds );
		for( JointId id : usedIds ) {
			boolean removed = false;
			for( KeyFrameData data : datas ) {
				for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( data.getPose() ) ) {
					if( jtPair.getJointId().equals( id ) ) {
						removed = true;
						unused.remove( id );
					}
				}
				if( removed ) {
					break;
				}
			}
		}
		return unused;
	}

	private void checkAddingJoints( KeyFrameData keyFrameData ) {
		//		List<JointId> idsForUpdate = Collections.newArrayList();
		for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( keyFrameData.getPose() ) ) {
			if( !usedIds.contains( jtPair.getJointId() ) ) {
				//				idsForUpdate.add( key.getJointId() );
				usedIds.add( jtPair.getJointId() );
			}
		}
		correctPoseActuals();
	}

	private void correctPoseActuals() {
		Pose<?> prev = initialPose;
		for( KeyFrameData data : datas ) {
			PoseBuilder<?, ?> builder = PoseUtilities.createBuilderForPoseClass( data.getPoseActual().getClass() );
			for( JointId id : usedIds ) {
				if( contains( EmployeesOnly.getJointIdTransformationPairs( data.getPose() ), id ) ) {
					EmployeesOnly.addJointIdTransformationPair( builder, new JointIdTransformationPair( id, findQuaternionForJointId( id, data.getPose() ) ) );
				} else {
					EmployeesOnly.addJointIdTransformationPair( builder, new JointIdTransformationPair( id, findQuaternionForJointId( id, initialPose ) ) );
					//					builder.addCustom( orientationForId( id, prev ), id );
					// thought this would be correct changed to other open to either
				}
			}
			data.setPoseActual( builder.build() );
			prev = data.getPoseActual();
		}
	}

	private static UnitQuaternion findQuaternionForJointId( JointId id, Pose<?> pose ) {
		for( JointIdTransformationPair jqPair : EmployeesOnly.getJointIdTransformationPairs( pose ) ) {
			if( jqPair.getJointId().equals( id ) ) {
				return jqPair.getTransformation().orientation.createUnitQuaternion();
			}
		}
		return null;
	}

	private static boolean contains( JointIdTransformationPair[] jtPairs, JointId id ) {
		for( JointIdTransformationPair key : jtPairs ) {
			if( key.getJointId().equals( id ) ) {
				return true;
			}
		}
		return false;
	}

	public void setInitialPose( Pose<?> pose ) {
		this.initialPose = pose;
	}

	public void disableListeners() {
		this.listenersEnabled = false;
	}

	public void enableListeners() {
		this.listenersEnabled = true;
	}

	private static class BeginAndEndQuaternionPair {
		private final UnitQuaternion startQuaternion;
		private final UnitQuaternion endQuaternion;

		public BeginAndEndQuaternionPair( UnitQuaternion startQuaternion, UnitQuaternion endQuaternion ) {
			this.startQuaternion = startQuaternion;
			this.endQuaternion = endQuaternion;
		}

		public UnitQuaternion getStartQuaternion() {
			return this.startQuaternion;
		}

		public UnitQuaternion getEndQuaternion() {
			return this.endQuaternion;
		}
	}
}
