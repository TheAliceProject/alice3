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

public enum PeacockResource implements org.lgna.story.resources.FlyerResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId PLUME = new org.lgna.story.resources.JointId( HEAD, PeacockResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId PLUME_TIP = new org.lgna.story.resources.JointId( PLUME, PeacockResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId PLUMAGE_BASE = new org.lgna.story.resources.JointId( SPINE_MIDDLE, PeacockResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId PLUMAGE_CENTER = new org.lgna.story.resources.JointId( PLUMAGE_BASE, PeacockResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId PLUMAGE_CENTER_TIP = new org.lgna.story.resources.JointId( PLUMAGE_CENTER, PeacockResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_PLUMAGE = new org.lgna.story.resources.JointId( PLUMAGE_BASE, PeacockResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId PLUMAGE_LEFT_TIP = new org.lgna.story.resources.JointId( LEFT_PLUMAGE, PeacockResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_PLUMAGE = new org.lgna.story.resources.JointId( PLUMAGE_BASE, PeacockResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId PLUMAGE_RIGHT_TIP = new org.lgna.story.resources.JointId( RIGHT_PLUMAGE, PeacockResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, PeacockResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, PeacockResource.class );

	public static final org.lgna.story.JointedModelPose SPREAD_FAN_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.12231281943138078, 0.04626133475909722, -0.10775195615651657, 0.9855399429010969), new Position(-0.0, 0.0, -0.12098211795091629) ),
		new JointIdTransformationPair( NECK_1, new Orientation(0.22462874619252948, 0.0, -0.0, 0.9744444193405657), new Position(-0.0, 0.0, -0.19225969910621643) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.012160854414105415, 0.05413903668522835, -0.02552882395684719) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(0.28064856282760264, 0.04918190821786835, 0.16545842920924778, 0.9441615498898791), new Position(-0.0, 0.0, -0.18522407114505768) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.12265655128782164, 0.0, -0.0, 0.9924491777547997), new Position(-0.0, 0.0, -0.14220421016216278) ),
		new JointIdTransformationPair( RIGHT_PLUMAGE, new Orientation(0.6666426303257622, -0.07863486941525115, -0.7333308680673861, 0.10784247161452422), new Position(-0.0, -1.8166466020375083E-7, 1.7463702306486084E-7) ),
		new JointIdTransformationPair( MOUTH, new Orientation(-0.39821783256050886, 0.0, 0.0, 0.9172908796182433), new Position(-0.0, 0.038151565939188004, 0.01582215540111065) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, -0.9826289389113649, 0.18558116395239357, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( LEFT_PLUMAGE, new Orientation(-0.10575900916407786, -0.7549232130887196, 0.08285299871456558, 0.6419044749216509), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.4708006191985305, 0.0, 0.0, 0.8822396369254106), new Position(-0.0, 0.0, -0.12479551136493683) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(0.2806489204820336, -0.049182014219316256, -0.16545855655988845, 0.944161415739236), new Position(-0.0, 0.0, -0.18522484600543976) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(-0.1803790344241791, 0.0, 0.0, 0.9835971756467181), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.12231225438803245, -0.04626092818244141, 0.1077524955259698, 0.985539973140685), new Position(-0.0, 0.0, -0.1209818497300148) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.14683987566428403, -0.7759540371198312, -0.5702145058733521, 0.22624942095824688), new Position(-0.14612199366092682, -0.12353052198886871, 0.13302047550678253) ),
		new JointIdTransformationPair( PLUMAGE_BASE, new Orientation(0.0, -0.9273150168822646, 0.374281791521636, 6.123233995736766E-17), new Position(-0.0, 0.2214999794960022, 0.035736970603466034) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(0.012160900048911572, 0.05413946881890297, -0.025528723374009132) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.4940221905708313, 0.15073300898075104) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.14684017071743674, 0.7759538538421277, 0.570214617233517, 0.2262495773796222), new Position(0.14612188935279846, -0.12353096902370453, 0.13302063941955566) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.07996378853937311, 0.0, 0.0, 0.9967977691199105), new Position(-0.0, 0.0, -0.13431204855442047) )
	);


	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_FAN_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( NECK_1, new Orientation(0.22462874619252948, 0.0, -0.0, 0.9744444193405657), new Position(-0.0, 0.0, -0.19225969910621643) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.012160854414105415, 0.05413903668522835, -0.02552882395684719) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.12265655128782164, 0.0, -0.0, 0.9924491777547997), new Position(-0.0, 0.0, -0.14220421016216278) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.13445123625387137, 0.4745333326646994, 0.05876941981355122, 0.867921158029183), new Position(-0.22840334475040436, 0.12150435149669647, 0.20569664239883423) ),
		new JointIdTransformationPair( LEFT_PLUMAGE, new Orientation(-0.05495130158404337, -0.5820349542251, 0.024514456298844037, 0.81093446587673), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, -0.9826289389113649, 0.18558116395239357, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(0.2806489204820336, -0.049182014219316256, -0.16545855655988845, 0.944161415739236), new Position(-0.0, 0.0, -0.18522484600543976) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.4708006191985305, 0.0, 0.0, 0.8822396369254106), new Position(-0.0, 0.0, -0.12479551136493683) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(-0.1803790344241791, 0.0, 0.0, 0.9835971756467181), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.14683987566428403, -0.7759540371198312, -0.5702145058733521, 0.22624942095824688), new Position(-0.14612199366092682, -0.12353052198886871, 0.13302047550678253) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.12231225438803245, -0.04626092818244141, 0.1077524955259698, 0.985539973140685), new Position(-0.0, 0.0, -0.1209818497300148) ),
		new JointIdTransformationPair( PLUMAGE_BASE, new Orientation(0.0, -0.9273150168822646, 0.374281791521636, 6.123233995736766E-17), new Position(-0.0, 0.2214999794960022, 0.035736970603466034) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.01959932860629276, -0.20536482151221122, 0.010379101868183768, 0.9784341728741132), new Position(-0.0, 0.0, -0.2660560607910156) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.4940221905708313, 0.15073300898075104) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.07996378853937311, 0.0, 0.0, 0.9967977691199105), new Position(-0.0, 0.0, -0.13431204855442047) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.01959722371209145, 0.20536472749926188, -0.01037919384116979, 0.9784342337925118), new Position(-0.0, 0.0, -0.2660559415817261) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.12231281943138078, 0.04626133475909722, -0.10775195615651657, 0.9855399429010969), new Position(-0.0, 0.0, -0.12098211795091629) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(0.28064856282760264, 0.04918190821786835, 0.16545842920924778, 0.9441615498898791), new Position(-0.0, 0.0, -0.18522407114505768) ),
		new JointIdTransformationPair( RIGHT_PLUMAGE, new Orientation(0.863548997526973, 0.09347349312073304, -0.47115332958471573, 0.15349389230586494), new Position(-0.0, -1.8166466020375083E-7, 1.7463702306486084E-7) ),
		new JointIdTransformationPair( MOUTH, new Orientation(-0.39821783256050886, 0.0, 0.0, 0.9172908796182433), new Position(-0.0, 0.038151565939188004, 0.01582215540111065) ),
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.14409094213136991, -0.08718968408063645, -0.006519096604387683, 0.9856943039122604), new Position(-0.0, 0.0, -0.2324630320072174) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.13445008935924985, -0.4745334366197718, -0.058770063263110135, 0.867921235288761), new Position(0.2284030020236969, 0.12150456011295319, 0.205696702003479) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.14409111015706724, 0.08718975773209754, 0.006518969235326895, 0.985694273677407), new Position(-0.0, 0.0, -0.23246325552463531) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(0.012160900048911572, 0.05413946881890297, -0.025528723374009132) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.14684017071743674, 0.7759538538421277, 0.570214617233517, 0.2262495773796222), new Position(0.14612188935279846, -0.12353096902370453, 0.13302063941955566) )
	);


	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.14409094213136991, -0.08718968408063645, -0.006519096604387683, 0.9856943039122604), new Position(-0.0, 0.0, -0.2324630320072174) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.13445008935924985, -0.4745334366197718, -0.058770063263110135, 0.867921235288761), new Position(0.2284030020236969, 0.12150456011295319, 0.205696702003479) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.01959932860629276, -0.20536482151221122, 0.010379101868183768, 0.9784341728741132), new Position(-0.0, 0.0, -0.2660560607910156) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.01959722371209145, 0.20536472749926188, -0.01037919384116979, 0.9784342337925118), new Position(-0.0, 0.0, -0.2660559415817261) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.14409111015706724, 0.08718975773209754, 0.006518969235326895, 0.985694273677407), new Position(-0.0, 0.0, -0.23246325552463531) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.13445123625387137, 0.4745333326646994, 0.05876941981355122, 0.867921158029183), new Position(-0.22840334475040436, 0.12150435149669647, 0.20569664239883423) )
	);

	public org.lgna.story.JointedModelPose getSpreadWingsPose(){
		return PeacockResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose FOLD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.15764208596910806, 0.16691471373844116, -0.1359485090969004, 0.9637460526213597), new Position(-0.0, 0.0, -0.2324630320072174) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.07419631484554134, -0.47123412575514395, -0.8442528529034685, 0.24427530769908146), new Position(0.2284030020236969, 0.12150456011295319, 0.205696702003479) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.45043781545574624, -0.7479805428629389, 0.2560746993596334, 0.41479709528128544), new Position(-0.0, 0.0, -0.2660560607910156) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.29551908958046774, 0.8105977215805534, -0.2280126665684781, 0.45123167591191904), new Position(-0.0, 0.0, -0.2660559415817261) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.19027679525978325, -0.08831035327363035, 0.38026718799230164, 0.9007734945183393), new Position(-0.0, 0.0, -0.23246325552463531) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.15286925453951872, 0.42767377992512773, 0.8125747576859002, 0.36530588847264184), new Position(-0.22840334475040436, 0.12150435149669647, 0.20569664239883423) )
	);

	public org.lgna.story.JointedModelPose getFoldWingsPose(){
		return PeacockResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	public org.lgna.story.resources.JointId[] getNeckArray(){
		return PeacockResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return PeacockResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private PeacockResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private PeacockResource( ImplementationAndVisualType resourceType ) {
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
