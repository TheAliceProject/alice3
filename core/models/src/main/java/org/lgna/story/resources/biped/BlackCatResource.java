/*
* Alice 3 End User License Agreement
 * 
 * Copyright (c) 2006-2016, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.resources.biped;

import org.lgna.project.annotations.*;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum BlackCatResource implements BipedResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId LEFT_EAR = new JointId( HEAD, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_EAR_TIP = new JointId( LEFT_EAR, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId RIGHT_EAR = new JointId( HEAD, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_EAR_TIP = new JointId( RIGHT_EAR, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId LEFT_MIDDLE_10 = new JointId( LEFT_HAND, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_MIDDLE_FINGER_TIP = new JointId( LEFT_MIDDLE_FINGER_KNUCKLE, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId LEFT_PINKY_10 = new JointId( LEFT_HAND, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_PINKY_FINGER_TIP = new JointId( LEFT_PINKY_FINGER_KNUCKLE, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_INDEX_FINGER_TIP = new JointId( LEFT_INDEX_FINGER_KNUCKLE, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_MIDDLE_FINGER_TIP = new JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId( RIGHT_INDEX_FINGER_KNUCKLE, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId RIGHT_PINKY_10 = new JointId( RIGHT_HAND, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_PINKY_FINGER_TIP = new JointId( RIGHT_PINKY_FINGER_KNUCKLE, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOES = new JointId( LEFT_FOOT, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOES = new JointId( RIGHT_FOOT, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME, methodNameHint="getTail")
	public static final JointId TAIL_0 = new JointId( PELVIS_LOWER_BODY, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_1 = new JointId( TAIL_0, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_2 = new JointId( TAIL_1, BlackCatResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_3 = new JointId( TAIL_2, BlackCatResource.class );

	public static final JointedModelPose STANDING_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_HAND, new Orientation(0.2727423655177994, -0.03029732835356901, 0.02650303819642, 0.9612446425924829), new Position(2.220445916901415E-17, 4.44089183380283E-17, -0.027636880055069923) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.18209673314977978, -0.025798338270240025, -0.013040863240693791, 0.9828556157465905), new Position(-1.3107292757674055E-13, 1.846913681019524E-12, -0.042175695300102234) ),
		new JointIdTransformationPair( LEFT_FOOT, new Orientation(0.32027099669152587, 0.012374577008681066, 0.004183981785786851, 0.9472359013564092), new Position(-0.0, 0.0, -0.042510129511356354) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.5670396063027775, -0.11502931856180391, -0.13954227396860694, 0.8035933639166444), new Position(-0.0, 0.0, -0.04684589058160782) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.054652896560809426, 0.07695580346570202, 0.08873278211173899, 0.9915731735929388), new Position(-0.0, 0.0, -0.04377799853682518) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.11862006356647481, 0.0, -0.0, 0.9929397164578952), new Position(-0.0, 0.0, -0.07420297712087631) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.2318142555517553, -0.7042874828115042, -0.5675616537500389, 0.3579316438519256), new Position(8.881783998477905E-18, 5.329070399086743E-17, -0.03565483167767525) ),
		new JointIdTransformationPair( RIGHT_HAND, new Orientation(0.27195736738820653, 0.02782749231214745, -0.03208848355537363, 0.9613714943860203), new Position(1.776356799695581E-17, 5.329070399086743E-17, -0.027637111023068428) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.21772136144144147, 0.7087523297237922, 0.5745943406699783, 0.34656729152246185), new Position(-0.0, 1.776356799695581E-17, -0.035654857754707336) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(-0.8333994670295659, 0.0, 0.0, 0.5526710851445329), new Position(-0.0, 0.0, -5.373144813347608E-5) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.8731141544506754, 0.0, 0.0, 0.48751581851041725), new Position(-0.0, 0.0, -0.032331813126802444) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.05465115593022331, -0.07695547221093887, -0.08873237818702032, 0.9915733313848035), new Position(-0.0, 0.0, -0.04377803951501846) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.5541517598673634, 0.0, -0.0, 0.8324156576109701), new Position(-0.0, 0.0, -5.373144813347608E-5) ),
		new JointIdTransformationPair( RIGHT_FOOT, new Orientation(0.32027134612818564, -0.012375104249728179, -0.004184165883215375, 0.947235775506769), new Position(-0.0, 0.0, -0.04251011461019516) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.06484305831082728, 0.011712743349193392, -0.1773692572768001, 0.9819360142113186), new Position(0.03988019749522209, 0.020929928869009018, -0.022689780220389366) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.5583114372875643, 0.1561055062397037, 0.19660998957086384, 0.7907363163005003), new Position(-0.0, 0.0, -0.04684584587812424) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(-0.18214176467557872, 0.04118645882338976, 0.023430270924179653, 0.9821298669600038), new Position(-3.996802716596996E-17, -8.88178366760566E-17, -0.042175937443971634) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.15768892794289655, 0.1398823869480903, 0.02187678713640136, 0.9772863070822764), new Position(5.329070399086743E-17, -1.776356799695581E-17, -0.03969644010066986) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.23171935777848135, 0.0, -0.0, 0.972782678315526), new Position(-0.0, 0.12584878504276276, -0.006195439957082272) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.15746600075466416, -0.1363091104896017, -0.053394849809015564, 0.9766131654947819), new Position(-2.6645351995433716E-17, -1.776356799695581E-17, -0.03969629108905792) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.21615466018639753, 0.6880029077948974, -0.6175803766892475, 0.3138847560392761), new Position(-0.015280931256711483, -0.02564496174454689, 0.005185632035136223) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.06484224957484394, -0.011712552696116712, 0.17736859938415256, 0.9819361887271079), new Position(-0.03988022729754448, 0.02092992328107357, -0.022689811885356903) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.17935298797908994, 0.0, 0.0, 0.9837847862733863), new Position(-0.0, 0.0, -0.07805275171995163) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.21615724530486874, -0.6880086836036923, 0.6175742377883233, 0.3138823942311786), new Position(0.015280899591743946, -0.025645095854997635, 0.005185140762478113) )
	);


	public static final JointedModelPose RESTING_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_HAND, new Orientation(-0.03139075779155093, -0.03690439438533802, 0.016075053804207762, 0.9986962894922137), new Position(4.44089183380283E-17, 8.88178366760566E-17, -0.027636880055069923) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.3574109358804209, -0.013353488579581957, -0.01382696527981797, 0.9337493894439703), new Position(-2.621458551534811E-13, 1.846913681019524E-12, -0.042175695300102234) ),
		new JointIdTransformationPair( LEFT_FOOT, new Orientation(0.32027099669152587, 0.012374577008681066, 0.004183981785786851, 0.9472359013564092), new Position(1.776356799695581E-17, 4.44089183380283E-17, -0.042510129511356354) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.3416784266050899, -0.12725820549012015, -0.2034121200691222, 0.908671949240808), new Position(3.7747583234302016E-17, 1.776356799695581E-17, -0.04684589058160782) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.27926900354443107, 0.054700342296242495, 0.1039403070708114, 0.9530021557048021), new Position(-1.2079226634976645E-15, -3.552713599391162E-17, -0.04377799853682518) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.37663184196266253, 0.0, -0.0, 0.9263630258272466), new Position(3.423927533717066E-17, 1.9539925127523637E-16, -0.07420297712087631) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.23162412504396723, -0.7045888091929701, -0.5671875356161512, 0.3580547082490693), new Position(1.776356799695581E-17, 1.0658140798173486E-16, -0.03565483167767525) ),
		new JointIdTransformationPair( RIGHT_HAND, new Orientation(-0.032177249083468876, 0.036243364410938575, -0.02214631895110298, 0.9985792826483595), new Position(3.552713599391162E-17, 1.0658140798173486E-16, -0.027637111023068428) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.21753727518030108, 0.7090573836810528, 0.5742178556369226, 0.3466828735634984), new Position(-0.0, 3.552713599391162E-17, -0.035654857754707336) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.710962391053417, 0.0, 0.0, 0.7032300324272337), new Position(5.589089992431357E-17, 8.88178366760566E-17, -0.032331813126802444) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(-0.2792673321491248, -0.054700080517217135, -0.10393977450482804, 0.953002718601372), new Position(1.3322675997716858E-17, 4.916778247564257E-13, -0.04377803951501846) ),
		new JointIdTransformationPair( NECK, new Orientation(0.1232644231841398, 0.0, -0.0, 0.9923738619981288), new Position(2.5887007699096126E-17, -1.776356799695581E-17, -0.019585033878684044) ),
		new JointIdTransformationPair( RIGHT_FOOT, new Orientation(0.32027134612818564, -0.012375104249728179, -0.004184165883215375, 0.947235775506769), new Position(-3.552713599391162E-17, -1.2434497928741312E-16, -0.04251011461019516) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(0.36934954113291213, -0.06671640959747561, -0.1647603045237668, 0.912131503243126), new Position(0.03988019749522209, 0.020929928869009018, -0.022689780220389366) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.341677122082717, 0.1272588193476159, 0.20341282843419092, 0.9086721952232969), new Position(-2.220445916901415E-17, -3.552713599391162E-17, -0.04684584587812424) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(-0.3576161743627588, 0.006053064809027349, 0.018991107282095514, 0.9336559163229046), new Position(-7.993605433193992E-17, -1.776356733521132E-16, -0.042175937443971634) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.2470925550971181, 0.1639021923162816, 0.0059158669140124505, 0.955011174326327), new Position(1.0658140798173486E-16, -3.552713599391162E-17, -0.03969644010066986) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.12584878504276276, -0.006195439957082272) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.24594395630078034, -0.165616824659585, -0.03732432172177673, 0.9543005463462784), new Position(-5.329070399086743E-17, -3.552713599391162E-17, -0.03969629108905792) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.20885953006218916, 0.6735796281878613, -0.6332803575222463, 0.3187854607239797), new Position(-0.015280931256711483, -0.02564496174454689, 0.005185632035136223) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(0.36935034179857734, 0.06671630185805061, 0.16475963856164375, 0.9121313072034618), new Position(-0.03988022729754448, 0.02092992328107357, -0.022689811885356903) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.14424114495639132, 0.0, 0.0, 0.9895425670993994), new Position(5.5179361642937295E-18, -1.2434497928741312E-16, -0.07805275171995163) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.2088621672954571, -0.6735855437097309, 0.6332743531129641, 0.31878316151777436), new Position(0.015280899591743946, -0.025645095854997635, 0.005185140762478113) )
	);


	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3 };

	private final ImplementationAndVisualType resourceType;
	BlackCatResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	BlackCatResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}


	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public BipedImp createImplementation( SBiped abstraction ) {
		return new BipedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
