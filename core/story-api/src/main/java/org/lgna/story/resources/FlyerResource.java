/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.story.resources;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;

/**
 * @author dculyba
 *
 */
@org.lgna.project.annotations.ResourceTemplate( modelClass = org.lgna.story.SFlyer.class )
public interface FlyerResource extends JointedModelResource {
	public static final org.lgna.story.resources.JointId ROOT = new JointId( null, FlyerResource.class );
	public static final org.lgna.story.resources.JointId SPINE_BASE = new org.lgna.story.resources.JointId( ROOT, FlyerResource.class );
	public static final org.lgna.story.resources.JointId SPINE_MIDDLE = new org.lgna.story.resources.JointId( SPINE_BASE, FlyerResource.class );
	public static final org.lgna.story.resources.JointId SPINE_UPPER = new org.lgna.story.resources.JointId( SPINE_MIDDLE, FlyerResource.class );

	//Necks are now included as arrays access
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN ) public static final org.lgna.story.resources.JointId NECK_0 = new org.lgna.story.resources.JointId( SPINE_UPPER, FlyerResource.class );
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN ) public static final org.lgna.story.resources.JointId NECK_1 = new org.lgna.story.resources.JointId( NECK_0, FlyerResource.class );

	public static final org.lgna.story.resources.JointId HEAD = new org.lgna.story.resources.JointId( NECK_1, FlyerResource.class );
	public static final org.lgna.story.resources.JointId MOUTH = new org.lgna.story.resources.JointId( HEAD, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_EYE = new org.lgna.story.resources.JointId( HEAD, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_EYE = new org.lgna.story.resources.JointId( HEAD, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_EYELID = new org.lgna.story.resources.JointId( HEAD, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_EYELID = new org.lgna.story.resources.JointId( HEAD, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_WING_SHOULDER = new org.lgna.story.resources.JointId( SPINE_UPPER, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_WING_ELBOW = new org.lgna.story.resources.JointId( LEFT_WING_SHOULDER, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_WING_WRIST = new org.lgna.story.resources.JointId( LEFT_WING_ELBOW, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_WING_TIP = new org.lgna.story.resources.JointId( LEFT_WING_WRIST, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_WING_SHOULDER = new org.lgna.story.resources.JointId( SPINE_UPPER, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_WING_ELBOW = new org.lgna.story.resources.JointId( RIGHT_WING_SHOULDER, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_WING_WRIST = new org.lgna.story.resources.JointId( RIGHT_WING_ELBOW, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_WING_TIP = new org.lgna.story.resources.JointId( RIGHT_WING_WRIST, FlyerResource.class );
	public static final org.lgna.story.resources.JointId PELVIS_LOWER_BODY = new org.lgna.story.resources.JointId( ROOT, FlyerResource.class );

	//Tails are now included as arrays access
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN ) public static final org.lgna.story.resources.JointId TAIL_0 = new org.lgna.story.resources.JointId( PELVIS_LOWER_BODY, FlyerResource.class );
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN ) public static final org.lgna.story.resources.JointId TAIL_1 = new org.lgna.story.resources.JointId( TAIL_0, FlyerResource.class );
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN ) public static final org.lgna.story.resources.JointId TAIL_2 = new org.lgna.story.resources.JointId( TAIL_1, FlyerResource.class );

	public static final org.lgna.story.resources.JointId LEFT_HIP = new org.lgna.story.resources.JointId( PELVIS_LOWER_BODY, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_KNEE = new org.lgna.story.resources.JointId( LEFT_HIP, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_ANKLE = new org.lgna.story.resources.JointId( LEFT_KNEE, FlyerResource.class );
	public static final org.lgna.story.resources.JointId LEFT_FOOT = new org.lgna.story.resources.JointId( LEFT_ANKLE, FlyerResource.class );
	//	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_BALL, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_HIP = new org.lgna.story.resources.JointId( PELVIS_LOWER_BODY, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_KNEE = new org.lgna.story.resources.JointId( RIGHT_HIP, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_ANKLE = new org.lgna.story.resources.JointId( RIGHT_KNEE, FlyerResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_FOOT = new org.lgna.story.resources.JointId( RIGHT_ANKLE, FlyerResource.class );
	//	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_BALL, FlyerResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN ) public static final JointId[] JOINT_ID_ROOTS = { ROOT };

	public org.lgna.story.resources.JointId[] getTailArray();

	public org.lgna.story.resources.JointId[] getNeckArray();

	public org.lgna.story.JointedModelPose getSpreadWingsPose();

	public org.lgna.story.JointedModelPose getFoldWingsPose();

	public org.lgna.story.implementation.FlyerImp createImplementation( org.lgna.story.SFlyer abstraction );
}
