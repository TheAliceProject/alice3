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

import org.lgna.ik.poser.Pose;

import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */

public class KeyFrameData {

	private double eventTime;
	private Pose pose;
	private KeyFrameStyles style = KeyFrameStyles.ARRIVE_AND_EXIT_GENTLY;

	public KeyFrameData( double time, Pose pose ) {
		this.eventTime = time;
		this.pose = pose;
	}

	public static Pose interpolatePoses( KeyFrameData key1, KeyFrameData key2, double targetTime ) {

		//TODO "Easing is not implemented, yet.";

		//		if( true || ( !key1.getEventStyle().getIsSlowOutDesired() && !key2.getEventStyle().getIsSlowInDesired() ) ) {
		Pose pose1 = key1.getPose();
		Pose pose2 = key2.getPose();
		double k = ( targetTime - key1.getEventTime() ) / ( key2.getEventTime() - key1.getEventTime() );

		UnitQuaternion leftClavicleUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftClavicle().getUnitQuaternion(), pose2.getLeftClavicle().getUnitQuaternion(), k );
		UnitQuaternion leftShoulderUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftShoulder().getUnitQuaternion(), pose2.getLeftShoulder().getUnitQuaternion(), k );
		UnitQuaternion leftElbowUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftElbow().getUnitQuaternion(), pose2.getLeftElbow().getUnitQuaternion(), k );
		UnitQuaternion leftWristUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftWrist().getUnitQuaternion(), pose2.getLeftWrist().getUnitQuaternion(), k );
		UnitQuaternion rightClavicleUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightClavicle().getUnitQuaternion(), pose2.getRightClavicle().getUnitQuaternion(), k );
		UnitQuaternion rightShoulderUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightShoulder().getUnitQuaternion(), pose2.getRightShoulder().getUnitQuaternion(), k );
		UnitQuaternion rightElbowUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightElbow().getUnitQuaternion(), pose2.getRightElbow().getUnitQuaternion(), k );
		UnitQuaternion rightWristUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightWrist().getUnitQuaternion(), pose2.getRightWrist().getUnitQuaternion(), k );
		UnitQuaternion pelvisUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getPelvis().getUnitQuaternion(), pose2.getPelvis().getUnitQuaternion(), k );
		UnitQuaternion leftHipUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftHip().getUnitQuaternion(), pose2.getLeftHip().getUnitQuaternion(), k );
		UnitQuaternion leftKneeUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftKnee().getUnitQuaternion(), pose2.getLeftKnee().getUnitQuaternion(), k );
		UnitQuaternion leftAnkleUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getLeftAnkle().getUnitQuaternion(), pose2.getLeftAnkle().getUnitQuaternion(), k );
		UnitQuaternion rightHipUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightHip().getUnitQuaternion(), pose2.getRightHip().getUnitQuaternion(), k );
		UnitQuaternion rightKneeUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightKnee().getUnitQuaternion(), pose2.getRightKnee().getUnitQuaternion(), k );
		UnitQuaternion rightAnkleUnitQuaternion = UnitQuaternion.createInterpolation( pose1.getRightAnkle().getUnitQuaternion(), pose2.getRightAnkle().getUnitQuaternion(), k );
		Pose out = new Pose.BuilderWithQuaternions()
				.leftArm( leftClavicleUnitQuaternion, leftShoulderUnitQuaternion, leftElbowUnitQuaternion, leftWristUnitQuaternion )
				.rightArm( rightClavicleUnitQuaternion, rightShoulderUnitQuaternion, rightElbowUnitQuaternion, rightWristUnitQuaternion )
				.leftLeg( pelvisUnitQuaternion, leftHipUnitQuaternion, leftKneeUnitQuaternion, leftAnkleUnitQuaternion )
				.rightLeg( pelvisUnitQuaternion, rightHipUnitQuaternion, rightKneeUnitQuaternion, rightAnkleUnitQuaternion )
				.build();
		return out;

	}

	public void setStyle( KeyFrameStyles style ) {
		this.style = style;
	}

	public double getEventTime() {
		return this.eventTime;
	}

	public Pose getPose() {
		return pose;
	}

	public KeyFrameStyles getEventStyle() {
		return style;
	}

	public void setPose( Pose pose ) {
		this.pose = pose;
	}

	public void setTime( double newTime ) {
		this.eventTime = newTime;
	}
}
