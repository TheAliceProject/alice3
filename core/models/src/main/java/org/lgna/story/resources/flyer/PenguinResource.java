/*
* Alice 3 End User License Agreement
 * 
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 * 
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package org.lgna.story.resources.flyer;

import org.lgna.project.annotations.*;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SFlyer;
import org.lgna.story.implementation.FlyerImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum PenguinResource implements FlyerResource {
	ADULT,
	BABY;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOE = new JointId( LEFT_FOOT, PenguinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOE = new JointId( RIGHT_FOOT, PenguinResource.class );

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.5009863483147118, -0.49892562375388366, 0.5066331685837185, 0.4933647061301709), new Position(0.07028825581073761, 0.029651585966348648, -0.03582237288355827) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.0, 0.0018593196059238058, 0.0, 0.9999982714638076), new Position(1.5543122080054396E-16, 5.400124724014126E-15, -0.2587554156780243) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.0, -0.0018593196059238058, -0.0, 0.9999982714638076), new Position(3.108624482185328E-17, -2.9160674526115815E-13, -0.25875526666641235) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.5120860897574203, 0.5100722054400342, -0.49540343279166416, 0.4818398288775326), new Position(-0.07060802727937698, 0.029686404392123222, -0.03355240076780319) )
	);

	@Override
	public JointedModelPose getSpreadWingsPose(){
		return PenguinResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.5993580664773056, -0.7500686227386157, 0.27188604168462965, 0.06515327826476462), new Position(0.07028825581073761, 0.029651585966348648, -0.03582237288355827) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.39161872076183823, 0.024396455466252895, 0.017147215589257803, 0.9196442592149858), new Position(1.5543122080054396E-16, 5.400124724014126E-15, -0.2587554156780243) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.3364316900896974, 0.23436937846933056, -0.020505641297892296, 0.9118466049803836), new Position(3.108624482185328E-17, -2.9160674526115815E-13, -0.25875526666641235) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.6157128848765895, 0.746420748069846, -0.16170600069233873, 0.19391977616501285), new Position(-0.07060802727937698, 0.029686404392123222, -0.03355240076780319) )
	);

	@Override
	public JointedModelPose getFoldWingsPose(){
		return PenguinResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	@Override
	public JointId[] getNeckArray(){
		return PenguinResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	@Override
	public JointId[] getTailArray(){
		return PenguinResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	PenguinResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	PenguinResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public JointId[] getRootJointIds(){
		return FlyerResource.JOINT_ID_ROOTS;
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public FlyerImp createImplementation( SFlyer abstraction ) {
		return new FlyerImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
