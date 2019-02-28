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

public enum ToucanResource implements org.lgna.story.resources.FlyerResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, ToucanResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, ToucanResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.09691421792553971, 0.3553246179879511, -0.2446392974181501, 0.8969412825677897), new Position(-5.329070399086743E-17, -1.0302869372059921E-15, -0.10181731730699539) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.3256089350511457, -0.7890678977060188, 0.44892117127382664, 0.2642356073787152), new Position(0.034449100494384766, 0.03586234152317047, -0.03468025475740433) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.12399960212650232, 0.6065952090023534, -0.1572740039465371, 0.7693706770929634), new Position(2.220445916901415E-17, -2.1316281596346973E-16, -0.07598204910755157) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.1239976735452864, -0.6065945448691266, 0.1572719866835324, 0.7693719239061096), new Position(3.996802716596996E-17, -2.1316281596346973E-16, -0.0759819746017456) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-1.4827205236789014E-6, -0.3683055737233625, 0.0, 0.9297047942019587), new Position(-8.881783998477905E-18, -1.172395454711859E-15, -0.10181751102209091) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.3256084119806618, 0.7890674826058739, -0.4489221641070238, 0.26423580475576536), new Position(-0.03444909676909447, 0.03586234897375107, -0.03468045964837074) )
	);

	public org.lgna.story.JointedModelPose getSpreadWingsPose(){
		return ToucanResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose FOLD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.22330181442399732, 0.8607634098940861, -0.14064340945419676, 0.43524944944199007), new Position(-0.0, 0.0, -0.10181731730699539) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.18922996933352004, 0.8886177647498341, -0.4097739096998681, 0.08146060278642564), new Position(0.034449100494384766, 0.03586234152317047, -0.03468025475740433) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.29855720077309306, 0.9060560546748658, -0.011099871321366417, 0.29967118064672243), new Position(-0.0, 0.0, -0.07598204910755157) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.3792707220097134, -0.8684481811005177, -0.052183844851468625, 0.3150052737748226), new Position(-0.0, 0.0, -0.0759819746017456) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.06382346525251933, -0.8977009929382459, -0.008651193042778588, 0.43587228567527037), new Position(-0.0, 0.0, -0.10181751102209091) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.1316780576093391, -0.9061701775238433, 0.3923625208297738, 0.08699511916621112), new Position(-0.03444909676909447, 0.03586234897375107, -0.03468045964837074) )
	);

	public org.lgna.story.JointedModelPose getFoldWingsPose(){
		return ToucanResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	public org.lgna.story.resources.JointId[] getNeckArray(){
		return ToucanResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return ToucanResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private ToucanResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private ToucanResource( ImplementationAndVisualType resourceType ) {
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
