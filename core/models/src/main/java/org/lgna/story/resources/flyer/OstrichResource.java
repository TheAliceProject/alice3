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
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum OstrichResource implements org.lgna.story.resources.FlyerResource {
	DEFAULT,
	TUTU;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, OstrichResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, OstrichResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.15138695773246694, 0.2352941434904374, 0.09906296320749249, 0.9549372672527556), new Position(-2.1316281596346973E-16, -4.2632563192693945E-16, -0.16651597619056702) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.10634903986093108, -0.808604165000871, 0.2687632493166735, 0.5124602441952408), new Position(0.18703699111938477, -0.011172603815793991, -0.16388170421123505) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.368501809203979, 0.17011943529316265, -0.024341504978446993, 0.9136045564051846), new Position(-3.552713599391162E-17, 2.8421708795129297E-16, -0.18572375178337097) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.3684854839038744, -0.17011529146740673, 0.024344577239868635, 0.913611830768461), new Position(1.4210854397564648E-16, 1.4210854397564648E-16, -0.1857243925333023) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.1514136452021329, -0.23529835997564977, -0.0990616495687256, 0.9549321334126813), new Position(-7.105427198782324E-17, -2.8421708795129297E-16, -0.16651445627212524) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.10634856562801578, 0.8086036768143707, -0.2687617454514732, 0.5124619016210101), new Position(-0.18703727424144745, -0.011169017292559147, -0.1638818085193634) )
	);

	public org.lgna.story.JointedModelPose getSpreadWingsPose(){
		return OstrichResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose FOLD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(0.1625743518485947, -0.026897084353826834, 0.09069096598281771, 0.982151350690603), new Position(-2.1316281596346973E-16, -4.2632563192693945E-16, -0.16651597619056702) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.10258049531764607, -0.9309423347798994, 0.1392023940053109, 0.32163069629332053), new Position(0.18703699111938477, -0.011172603815793991, -0.16388170421123505) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.19232628806828156, -0.22968022562690005, 0.25214198538265387, 0.9201532546707348), new Position(-3.552713599391162E-17, 2.8421708795129297E-16, -0.18572375178337097) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.16692269662423703, 0.14169516564125328, -0.1848519669811944, 0.9580652606627453), new Position(1.4210854397564648E-16, 1.4210854397564648E-16, -0.1857243925333023) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(0.1467479518599, 0.024258242059896178, -0.24052279050559155, 0.9591795262433885), new Position(-7.105427198782324E-17, -2.8421708795129297E-16, -0.16651445627212524) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.0938041036636642, 0.9393829603112407, -0.14273098868162176, 0.29730171355495627), new Position(-0.18703727424144745, -0.011169017292559147, -0.1638818085193634) )
	);

	public org.lgna.story.JointedModelPose getFoldWingsPose(){
		return OstrichResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	public org.lgna.story.resources.JointId[] getNeckArray(){
		return OstrichResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return OstrichResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private OstrichResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private OstrichResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public org.lgna.story.resources.JointId[] getRootJointIds(){
		return org.lgna.story.resources.FlyerResource.JOINT_ID_ROOTS;
	}

	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.FlyerImp createImplementation( org.lgna.story.SFlyer abstraction ) {
		return new org.lgna.story.implementation.FlyerImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
