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

public enum OwlResource implements FlyerResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOE = new JointId( LEFT_FOOT, OwlResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOE = new JointId( RIGHT_FOOT, OwlResource.class );

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.037609908058621334, -0.033108710691774734, -0.0012467808411919313, 0.9987430868995713), new Position(1.1297628888721871E-14, 9.73443551379469E-15, -0.035948291420936584) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-3.552713599391162E-17, -1.4210854397564648E-16, -0.029219498857855797) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(9.325873115683739E-17, 1.4210854397564648E-16, -0.029219545423984528) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.037609464144413796, 0.03310831584827563, 0.0012467534365903884, 0.9987431167393992), new Position(1.332267550140849E-16, 0.0, -0.0359485000371933) )
	);

	@Override
	public JointedModelPose getSpreadWingsPose() {
return OwlResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.36627163476349334, 0.5328099043388761, 0.11016451375981098, 0.7548658657762585), new Position(-0.0, 0.0, -0.035948291420936584) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.5112281687526656, 0.3578631053138355, -0.19837700513956072, 0.7557951582014755), new Position(-0.0, 0.0, -0.029219498857855797) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.3382063757159662, -0.5758932799520348, -0.04251370740460413, 0.7430719764692205), new Position(-0.0, 0.0, -0.029219545423984528) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.3258969100641382, -0.5383231985208026, 0.28258473662091177, 0.7239787321281403), new Position(-0.0, 0.0, -0.0359485000371933) )
	);

	@Override
	public JointedModelPose getFoldWingsPose() {
return OwlResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	@Override
	public JointId[] getNeckArray() {
return OwlResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	@Override
	public JointId[] getTailArray() {
		return OwlResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	OwlResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	OwlResource( ImplementationAndVisualType resourceType ) {
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
