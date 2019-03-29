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

public enum RavenResource implements FlyerResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOE = new JointId( LEFT_FOOT, RavenResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOE = new JointId( RIGHT_FOOT, RavenResource.class );

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.06510098062188452, 0.34403078489574335, -0.03790837192023564, 0.9359314273516222), new Position(-0.0, 0.0, -0.31250783801078796) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.026277642511426905, -0.6618229044522504, 0.11353832374608268, 0.740546404816983), new Position(0.13491375744342804, 0.020335394889116287, 0.07297966629266739) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.022135513714453492, 0.14518936770443566, -0.022955798182980134, 0.9888898310064502), new Position(1.7060781942745962E-7, -5.780006517852598E-7, -0.2656984329223633) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.022135513714453492, -0.14518936770443566, 0.022955798182980134, 0.9888898310064502), new Position(-0.0, 0.0, -0.2656974494457245) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.06510098062188452, -0.34403078489574335, 0.03790837192023564, 0.9359314273516222), new Position(-2.3401878479489824E-7, 3.8147859982018417E-7, -0.31250786781311035) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.026277642511426905, 0.6618229044522504, -0.11353832374608268, 0.740546404816983), new Position(-0.1033005490899086, 0.020335467532277107, 0.07297960668802261) )
	);

	@Override
	public JointedModelPose getSpreadWingsPose() {
return RavenResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.2141309900268654, 0.9614851202627342, -0.10740651621909011, 0.13475207937968212), new Position(-0.0, 0.0, -0.31250783801078796) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.4544562254261921, -0.8835260548165733, -0.11316390462908088, 0.006721630809539123), new Position(0.13491375744342804, 0.020335394889116287, 0.07297966629266739) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.0751529079463998, 0.9900472266406705, -0.06499918350145885, 0.09966762559858953), new Position(1.7060781942745962E-7, -5.780006517852598E-7, -0.2656984329223633) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.07515042790796075, -0.990047419470112, 0.0649961334478519, 0.09966956919065947), new Position(-0.0, 0.0, -0.2656974494457245) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.21413284713522085, -0.961484579531595, 0.10740626695097563, 0.1347531851956458), new Position(-2.3401878479489824E-7, 3.8147859982018417E-7, -0.31250786781311035) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.45445935517753583, 0.8835244530006562, 0.11316397360950285, 0.006719413547130323), new Position(-0.1033005490899086, 0.020335467532277107, 0.07297960668802261) )
	);

	@Override
	public JointedModelPose getFoldWingsPose() {
return RavenResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	@Override
	public JointId[] getNeckArray() {
return RavenResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	@Override
	public JointId[] getTailArray() {
		return RavenResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	RavenResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	RavenResource( ImplementationAndVisualType resourceType ) {
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
