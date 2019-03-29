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

public enum FalconResource implements FlyerResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOE = new JointId( LEFT_FOOT, FalconResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOE = new JointId( RIGHT_FOOT, FalconResource.class );

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(0.00543370154583049, 0.3975249786868126, 0.062645381568644, 0.9154342807518621), new Position(-1.498800977364843E-17, -2.1316281596346973E-16, -0.10720979422330856) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.30766538268834626, -0.7817136119864743, 0.3927353655500894, 0.3741988425643927), new Position(0.06113654002547264, -4.1078979847952724E-4, 0.005746513605117798) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.02648678367622417, 0.4202450579602583, 0.08223770000251708, 0.9032881612468769), new Position(-5.329070399086743E-17, 1.4210854397564648E-16, -0.10817328840494156) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.027212189112170295, -0.4232997037887957, -0.08246482542997977, 0.9018183908652807), new Position(1.0658140798173486E-16, 0.0, -0.1081729531288147) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(0.00548861485111523, -0.3972139218596212, -0.06326229957708794, 0.915526546223611), new Position(-6.106226767787259E-17, -2.1316281596346973E-16, -0.1066131666302681) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.30766675402547755, 0.7817152746368591, -0.39273228135697846, 0.37419747867535064), new Position(-0.05910273268818855, -4.104471590835601E-4, 0.005746267735958099) )
	);

	@Override
	public JointedModelPose getSpreadWingsPose() {
return FalconResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.05876085841816866, 0.9467619962557903, -0.05630580838743837, 0.31147799265044085), new Position(-1.498800977364843E-17, -2.1316281596346973E-16, -0.10720979422330856) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.5994733585431814, 0.7247523607079441, -0.32825376303561177, 0.0872649706249922), new Position(0.06113654002547264, -4.1078979847952724E-4, 0.005746513605117798) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.014205316654529437, 0.9846026687061756, 0.03104880289060068, 0.17144026829914374), new Position(-5.329070399086743E-17, 1.4210854397564648E-16, -0.10817328840494156) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.06621820246726075, -0.9813911626129387, -0.020600065959114535, 0.17905913238287927), new Position(1.0658140798173486E-16, 0.0, -0.1081729531288147) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.11357044678593456, -0.9329260696610783, 0.010615902399726604, 0.3415230662485562), new Position(-6.106226767787259E-17, -2.1316281596346973E-16, -0.1066131666302681) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.6885578248728461, -0.6478113024171651, 0.2988074306630727, 0.13016434860392673), new Position(-0.05910273268818855, -4.104471590835601E-4, 0.005746267735958099) )
	);

	@Override
	public JointedModelPose getFoldWingsPose() {
return FalconResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	@Override
	public JointId[] getNeckArray() {
return FalconResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	@Override
	public JointId[] getTailArray() {
		return FalconResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	FalconResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	FalconResource( ImplementationAndVisualType resourceType ) {
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
