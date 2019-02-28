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

public enum EagleResource implements org.lgna.story.resources.FlyerResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, EagleResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_HINDFOOT = new org.lgna.story.resources.JointId( LEFT_ANKLE, EagleResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_HINDTOE = new org.lgna.story.resources.JointId( LEFT_HINDFOOT, EagleResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, EagleResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_HINDFOOT = new org.lgna.story.resources.JointId( RIGHT_ANKLE, EagleResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_HINDTOE = new org.lgna.story.resources.JointId( RIGHT_HINDFOOT, EagleResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( TAIL_1, new Orientation(-0.0685296254659093, 0.0, 0.0, 0.9976490818085797), new Position(-0.0, 0.0, -0.09041246026754379) ),
		new JointIdTransformationPair( TAIL_0, new Orientation(-0.4229670272245295, 0.0, 0.0, 0.9061450733082668), new Position(-0.0, 0.0, -0.12702907621860504) ),
		new JointIdTransformationPair( LEFT_FOOT, new Orientation(-0.5956637171144652, -0.05580200799850926, 0.05924754372906666, 0.7990998689643147), new Position(-0.0, 0.0, -0.08203809708356857) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.2612865340404986, 0.04092694418042898, 0.019761333818658015, 0.964190760199901), new Position(-0.0, 0.0, -0.07284130156040192) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.008028511486054865, -0.11780604850355016, 0.04974462761435421, 0.9917574047935346), new Position(-0.0, 0.0, -0.17551539838314056) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(0.21769647882438434, -0.26434248507639985, -0.37963281043058056, 0.8594243555634647), new Position(-0.0, 0.0, -0.061022594571113586) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.08220889520106722, 0.0, 0.0, 0.9966151200688358), new Position(-0.0, 0.0, -0.09101147949695587) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.16016010939940337, 0.646181479156264, -0.6305366883870885, 0.39902596400539847), new Position(-0.15430203080177307, 0.10322856903076172, 0.010111533105373383) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.8779341344328248, 0.47878142779110255, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(0.2177041745781506, 0.2643321551820354, 0.379621439641084, 0.8594306060834099), new Position(-0.0, 0.0, -0.06102294474840164) ),
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(0.006691134015722402, 0.050093123771085386, -0.022031160454080465, 0.9984791112714801), new Position(-0.0, 0.0, -0.14633992314338684) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.1601589828165881, -0.6461810399298427, 0.6305381292115818, 0.39902485069065724), new Position(0.15430200099945068, 0.10322847217321396, 0.010111864656209946) ),
		new JointIdTransformationPair( RIGHT_FOOT, new Orientation(-0.5976827395353399, 0.026573085119195608, -0.09830246257515525, 0.7952394858534373), new Position(-0.0, 0.0, -0.08203822374343872) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.2612853914845166, -0.04092520710577699, -0.019760432811229075, 0.9641911620188421), new Position(-0.0, 0.0, -0.07284101098775864) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.0406860649155565, 0.41497486986792687, 0.8246684186537786, 0.3821812407416936), new Position(-0.12353299558162689, -0.08568504452705383, -0.02527046948671341) ),
		new JointIdTransformationPair( RIGHT_HINDFOOT, new Orientation(0.021935967599363292, 0.4942337726631946, 0.8634725181613753, 0.09832091163449684), new Position(-0.018376827239990234, -0.04763345420360565, 0.003825669875368476) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.008027222275734557, 0.11780942031040127, -0.049746286283617935, 0.9917569315057251), new Position(-0.0, 0.0, -0.1755155324935913) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(0.00669009134225375, -0.050095622695370594, 0.02203222052297796, 0.9984789694948123), new Position(-0.0, 0.0, -0.14634038507938385) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.12865006392262748, 0.0, -0.0, 0.9916900529160831), new Position(-0.0, 0.23318789899349213, 0.058836210519075394) ),
		new JointIdTransformationPair( LEFT_HINDFOOT, new Orientation(0.020225163235615573, -0.49430624056843625, -0.8631290868382704, 0.10129394237033777), new Position(0.018377147614955902, -0.04763346537947655, 0.0038255846593528986) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.04068523609396077, -0.41497714236588346, -0.8246616693391402, 0.3821934248526609), new Position(0.12353269755840302, -0.08568517863750458, -0.025270670652389526) )
	);

	public org.lgna.story.JointedModelPose getSpreadWingsPose(){
		return EagleResource.SPREAD_WINGS_POSE;
	}

	public static final org.lgna.story.JointedModelPose FLYING_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( TAIL_1, new Orientation(-0.0685296254659093, 0.0, 0.0, 0.9976490818085797), new Position(-3.0429468047697607E-16, -1.776356799695581E-17, -0.09041246026754379) ),
		new JointIdTransformationPair( TAIL_0, new Orientation(-0.4229670272245295, 0.0, 0.0, 0.9061450733082668), new Position(-3.7602505681027566E-17, -3.552713599391162E-17, -0.12702907621860504) ),
		new JointIdTransformationPair( LEFT_FOOT, new Orientation(-0.5956637171144652, -0.05580200799850926, 0.05924754372906666, 0.7990998689643147), new Position(-5.329070399086743E-17, -2.6645351995433716E-17, -0.08203809708356857) ),
		new JointIdTransformationPair( NECK_1, new Orientation(0.02136474595425701, 0.0, -0.0, 0.9997717477656137), new Position(-3.6990506282060434E-19, -1.0266927224256434E-34, -1.6427084706753996E-34) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.2612865340404986, 0.04092694418042898, 0.019761333818658015, 0.964190760199901), new Position(-5.329070399086743E-17, 8.881783998477905E-18, -0.07284130156040192) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.008028511486054865, -0.11780604850355016, 0.04974462761435421, 0.9917574047935346), new Position(-2.6645351995433716E-17, -8.589626122229891E-17, -0.17551539838314056) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.08298444002866745, 0.05216618627309799, -0.08257777243852615) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(0.21769647882438434, -0.26434248507639985, -0.37963281043058056, 0.8594243555634647), new Position(-1.776356799695581E-17, -1.776356799695581E-17, -0.061022594571113586) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.08220889520106722, 0.0, 0.0, 0.9966151200688358), new Position(-5.823918627401281E-17, 5.4513223996661603E-17, -0.09101147949695587) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.16016010939940337, 0.646181479156264, -0.6305366883870885, 0.39902596400539847), new Position(-0.15430203080177307, 0.10322856903076172, 0.010111533105373383) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.8779341344328248, 0.47878142779110255, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(0.2177041745781506, 0.2643321551820354, 0.379621439641084, 0.8594306060834099), new Position(7.105427198782324E-17, -5.329070399086743E-17, -0.06102294474840164) ),
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(0.006691134015722402, 0.050093123771085386, -0.022031160454080465, 0.9984791112714801), new Position(1.0484718743157049E-16, -2.1739141950312246E-17, -0.14633992314338684) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.1601589828165881, -0.6461810399298427, 0.6305381292115818, 0.39902485069065724), new Position(0.15430200099945068, 0.10322847217321396, 0.010111864656209946) ),
		new JointIdTransformationPair( RIGHT_FOOT, new Orientation(-0.5976827395353399, 0.026573085119195608, -0.09830246257515525, 0.7952394858534373), new Position(5.329070399086743E-17, 5.329070399086743E-17, -0.08203822374343872) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.2612853914845166, -0.04092520710577699, -0.019760432811229075, 0.9641911620188421), new Position(-7.105427198782324E-17, 4.44089183380283E-17, -0.07284101098775864) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.0406860649155565, 0.41497486986792687, 0.8246684186537786, 0.3821812407416936), new Position(-0.12353299558162689, -0.08568504452705383, -0.02527046948671341) ),
		new JointIdTransformationPair( RIGHT_HINDFOOT, new Orientation(0.021935967599363292, 0.4942337726631946, 0.8634725181613753, 0.09832091163449684), new Position(-0.018376827239990234, -0.04763345420360565, 0.003825669875368476) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.008027222275734557, 0.11780942031040127, -0.049746286283617935, 0.9917569315057251), new Position(-1.3188211171701463E-17, 9.3896555237926E-18, -0.1755155324935913) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(0.00669009134225375, -0.050095622695370594, 0.02203222052297796, 0.9984789694948123), new Position(-3.573961222349689E-18, 9.118441364346062E-17, -0.14634038507938385) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(0.08298439532518387, 0.05216614529490471, -0.08257797360420227) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.23318789899349213, 0.058836210519075394) ),
		new JointIdTransformationPair( LEFT_HINDFOOT, new Orientation(0.020225163235615573, -0.49430624056843625, -0.8631290868382704, 0.10129394237033777), new Position(0.018377147614955902, -0.04763346537947655, 0.0038255846593528986) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.04068523609396077, -0.41497714236588346, -0.8246616693391402, 0.3821934248526609), new Position(0.12353269755840302, -0.08568517863750458, -0.025270670652389526) )
	);


	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose FOLD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( TAIL_1, new Orientation(0.3362022066716763, 0.0, -0.0, 0.9417898259320364), new Position(-3.0429468047697607E-16, -1.776356799695581E-17, -0.09041246026754379) ),
		new JointIdTransformationPair( TAIL_0, new Orientation(-0.18651920333769886, 0.0, 0.0, 0.9824513152244594), new Position(-3.7602505681027566E-17, -3.552713599391162E-17, -0.12702907621860504) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.581910460005932, 0.045253128343971126, 0.004204627254871233, 0.8119819530139917), new Position(-5.329070399086743E-17, 8.881783998477905E-18, -0.07284130156040192) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.8868820541769561, -0.202038297758841, -0.050448326329099756, 0.41240236976545763), new Position(-2.6645351995433716E-17, -8.589626122229891E-17, -0.17551539838314056) ),
		new JointIdTransformationPair( LEFT_FOOT, new Orientation(0.25780549375377176, 0.04733072787666803, -0.052620419091809985, 0.9636011732473353), new Position(-5.329070399086743E-17, -2.6645351995433716E-17, -0.08203809708356857) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.07510873959191172, -0.12576820320135376, -0.44517453588765454, 0.883380251589104), new Position(-1.776356799695581E-17, -1.776356799695581E-17, -0.061022594571113586) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.02754356401595792, 0.0, 0.0, 0.9996206040700135), new Position(-5.823918627401281E-17, 5.4513223996661603E-17, -0.09101147949695587) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.8766450369899628, 0.32523724922421715, -0.0665961386958443, 0.34825158312452076), new Position(-0.15430203080177307, 0.10322856903076172, 0.010111533105373383) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.9666079095681014, 0.2562599249987893, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.04356039898548174, 0.14155345153170096, 0.4403936296802046, 0.8855046939100791), new Position(7.105427198782324E-17, -5.329070399086743E-17, -0.06102294474840164) ),
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.9661549370043928, 0.056342140501210454, 0.04886826875172551, 0.24694552681712773), new Position(1.0484718743157049E-16, -2.1739141950312246E-17, -0.14633992314338684) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.8845137472473356, -0.2738708485334666, 0.07955032705746722, 0.3691909190636813), new Position(0.15430200099945068, 0.10322847217321396, 0.010111864656209946) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.0660644081141461, 0.35909596261867677, 0.8504894036379193, 0.3786203347828929), new Position(-0.12353299558162689, -0.08568504452705383, -0.02527046948671341) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.5625102974838493, -0.028268298789316186, 0.013629895758334499, 0.8261944652742514), new Position(-7.105427198782324E-17, 4.44089183380283E-17, -0.07284101098775864) ),
		new JointIdTransformationPair( RIGHT_FOOT, new Orientation(0.2745306363668914, -0.09014967743004967, 0.03706642158879158, 0.9566253424125087), new Position(5.329070399086743E-17, 5.329070399086743E-17, -0.08203822374343872) ),
		new JointIdTransformationPair( RIGHT_HINDFOOT, new Orientation(0.08779541802859445, 0.9726075171323045, 0.20949084165494505, 0.04939807135527577), new Position(-0.018376827239990234, -0.04763345420360565, 0.003825669875368476) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.7918874044244203, 0.3265439104804073, -0.017359052866738688, 0.5157345019733391), new Position(-1.3188211171701463E-17, 9.3896555237926E-18, -0.1755155324935913) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.9215933652928915, -0.2614134403932988, -0.015587924392301047, 0.28650601885999233), new Position(-3.573961222349689E-18, 9.118441364346062E-17, -0.14634038507938385) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.12865006392262748, 0.0, -0.0, 0.9916900529160831), new Position(-0.0, 0.2576700448989868, 0.06529698520898819) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.0581847551766666, -0.3766883605810904, -0.8428397078290586, 0.37992320299553806), new Position(0.12353269755840302, -0.08568517863750458, -0.025270670652389526) ),
		new JointIdTransformationPair( LEFT_HINDFOOT, new Orientation(0.08735958944235667, -0.9661844713797868, -0.23625839512931573, 0.055116604829624216), new Position(0.018377147614955902, -0.04763346537947655, 0.0038255846593528986) )
	);

	public org.lgna.story.JointedModelPose getFoldWingsPose(){
		return EagleResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	public org.lgna.story.resources.JointId[] getNeckArray(){
		return EagleResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return EagleResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private EagleResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private EagleResource( ImplementationAndVisualType resourceType ) {
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
