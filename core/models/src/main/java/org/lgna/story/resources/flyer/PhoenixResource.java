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
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
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

public enum PhoenixResource implements FlyerResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOE = new JointId( LEFT_FOOT, PhoenixResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOE = new JointId( RIGHT_FOOT, PhoenixResource.class );

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.10756514695679051, 0.08416097973752806, 0.012540087358762657, 0.9905500567153677), new Position(-1.776356799695581E-17, -7.105427198782324E-17, -0.19206586480140686) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.34911977282524415, -0.5797043594429134, 0.3331980276489753, 0.6565343206841239), new Position(0.06489770114421844, 0.03269122913479805, 0.024828080087900162) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.036220903958123205, 0.07711411006008148, 2.6283713882083745E-4, 0.9963640855945866), new Position(1.3322675997716858E-17, 2.1316281596346973E-16, -0.16212941706180573) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.036222399487633526, -0.07711418055960541, -2.628279511359903E-4, 0.9963640257724317), new Position(1.776356799695581E-17, 7.105427198782324E-17, -0.16212941706180573) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.1075644589393419, -0.08416096552299857, -0.01254014293751436, 0.9905501319319634), new Position(-0.0, -2.8421708795129297E-16, -0.19206587970256805) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.3491203169204957, 0.5797045939495106, -0.33319756664830913, 0.6565340582535011), new Position(-0.06489770114421844, 0.03268999978899956, 0.024828080087900162) )
	);

	@Override
	public JointedModelPose getSpreadWingsPose() {
return PhoenixResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.31047152376899495, 0.9318084483260477, -0.11743458168929075, 0.14679770972403852), new Position(-0.0, 0.0, -0.19206586480140686) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.49337480427386626, 0.8535870060771169, -0.14450558594802004, 0.08419418741129137), new Position(0.06489770114421844, 0.03269122913479805, 0.024828080087900162) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.02675302666781874, -0.9955314295170998, 0.018931482438077542, 0.08856098113981903), new Position(-0.0, 0.0, -0.16212941706180573) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.022928219966361896, 0.9954341920695408, -0.01572909291981446, 0.09131079686408806), new Position(-0.0, 0.0, -0.16212941706180573) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.31236097096767707, -0.929264099415577, 0.11230814574451994, 0.16212876904767537), new Position(-0.0, 0.0, -0.19206587970256805) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.4933735167627242, -0.8535879183854677, 0.14450474751087622, 0.08419392193242974), new Position(-0.06489770114421844, 0.03268999978899956, 0.024828080087900162) )
	);

	@Override
	public JointedModelPose getFoldWingsPose() {
return PhoenixResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	@Override
	public JointId[] getNeckArray() {
return PhoenixResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	@Override
	public JointId[] getTailArray() {
		return PhoenixResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	PhoenixResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	PhoenixResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public JointId[] getRootJointIds() {
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
