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

package org.lgna.story.resources.biped;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
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

public enum PlayingCardResource implements BipedResource {
	BLANK,
	ONE1,
	TWO2,
	THREE3,
	FOUR4,
	FIVE5,
	SIX6,
	SEVEN7,
	EIGHT8,
	NINE9,
	TEN10;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_THUMB_TIP = new JointId( LEFT_THUMB_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_INDEX_FINGER_TIP = new JointId( LEFT_INDEX_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_MIDDLE_FINGER_TIP = new JointId( LEFT_MIDDLE_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER = new JointId( LEFT_HAND, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_KNUCKLE = new JointId( LEFT_RING_FINGER, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_TIP = new JointId( LEFT_RING_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_PINKY_FINGER_TIP = new JointId( LEFT_PINKY_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_THUMB_TIP = new JointId( RIGHT_THUMB_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId( RIGHT_INDEX_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_MIDDLE_FINGER_TIP = new JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER = new JointId( RIGHT_HAND, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_KNUCKLE = new JointId( RIGHT_RING_FINGER, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_TIP = new JointId( RIGHT_RING_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_PINKY_FINGER_TIP = new JointId( RIGHT_PINKY_FINGER_KNUCKLE, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOES = new JointId( LEFT_FOOT, PlayingCardResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOES = new JointId( RIGHT_FOOT, PlayingCardResource.class );

	public static final JointedModelPose STANDING_POSE = new JointedModelPose(
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.05976186503631742, 0.0, 0.0, 0.9982126624559423), new Position(3.355605754861909E-19, 5.329070399086743E-17, -0.26492035388946533) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(0.0, 0.6488543744550531, 0.0, 0.7609126104557222), new Position(-0.07335834950208664, 0.0019372437382116914, -0.041472021490335464) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER, new Orientation(-0.07036511573050964, -0.6817773545879214, -0.06615585790211664, 0.7251563912181721), new Position(0.07867980003356934, -5.684341759025859E-16, 0.022841691970825195) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.5477809679516521, 0.11582447209219361, 0.1431940689159744, 0.8160981322376635), new Position(6.88338280561553E-17, 2.8421708795129297E-16, -0.11329611390829086) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(-0.7071067811865475, 0.0, 0.0, 0.7071067811865476), new Position(-0.0, -4.973799171496525E-16, -5.373144813347608E-5) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(0.005062607752277275, 0.0, -0.0, 0.9999871849192602), new Position(1.776356799695581E-17, 1.2967404207643823E-15, -0.20496971905231476) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.6827957700124511, 0.0, 0.0, 0.7306092912447144), new Position(3.881022113909051E-16, 0.0, -0.05756701901555061) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.6544917826934445, 0.0, -0.0, 0.7560691148213614), new Position(-0.0, 6.252776040807564E-15, -5.373144813347608E-5) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(-0.11175401642973513, -0.5787352374138824, -0.004720564732345108, 0.8078083195015556), new Position(1.4210854397564648E-16, 5.684341759025859E-16, -0.04292537271976471) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(-0.0020499091337581377, 0.0, 0.0, 0.9999978989340644), new Position(0.12753799557685852, 0.029098233208060265, -0.0978902280330658) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.4469920825299733, 0.0, -0.0, 0.8945379132018484), new Position(1.776356799695581E-17, 5.861977273559295E-16, -0.231216698884964) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.07348907640314703, -0.6458265959909139, -0.06266739840749468, 0.7573507514600072), new Position(0.07335832715034485, 0.0019399999873712659, -0.04147103428840637) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-5.062616928763243E-16, 0.0, -0.24058400094509125) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.6318022012710571, 0.09399875998497009) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.05040750147004427, 0.0, -0.0, 0.9987287338389477), new Position(-0.0, 0.0, -0.1990116387605667) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.42576081841946245, -0.7016428825136216, 0.43441935836988743, 0.3710859900215328), new Position(0.10586899518966675, -0.027492839843034744, -0.007895448245108128) ),
		new JointIdTransformationPair( LEFT_RING_FINGER, new Orientation(0.0, 0.6730564121183904, 0.0, 0.7395911479366958), new Position(-0.08537881076335907, 0.001574391731992364, 8.079967810772359E-4) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.05684336191037548, 0.0, -0.0, 0.998383108935005), new Position(-3.552713467042264E-16, 1.4210854397564648E-16, -0.22732900083065033) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.44699211586649357, 0.0, -0.0, 0.8945378965439056), new Position(7.105427198782324E-17, -2.220446049250313E-16, -0.23121656477451324) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(0.005062607752277275, 0.0, -0.0, 0.9999871849192602), new Position(3.552713599391162E-17, 4.529709855767344E-16, -0.20496949553489685) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.5766023672582241, -0.11063245279037312, -0.14724191884937124, 0.7959962234805847), new Position(-5.995203909459372E-17, 2.8421708795129297E-16, -0.11329599469900131) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER, new Orientation(0.0, 0.6849746761461597, 0.0, 0.7285668761606333), new Position(-0.07868067175149918, -1.4210853868169056E-15, 0.022840620949864388) ),
		new JointIdTransformationPair( NECK, new Orientation(0.047423020275181374, 0.0, -0.0, 0.998874895644084), new Position(-4.6629365578418697E-17, 0.0, -0.11043738573789597) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.05687612050388447, 0.0, -0.0, 0.9983812432715409), new Position(2.9309886367796475E-16, 1.2903455972983202E-13, -0.22732849419116974) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(2.8421708795129297E-16, 1.3557155344092603E-13, -0.2405843734741211) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.42576020540715426, 0.7016416820478187, -0.4344208861760609, 0.3710871746083007), new Position(-0.10586892813444138, -0.027492910623550415, -0.007894791662693024) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(-0.0020499091337581377, 0.0, 0.0, 0.9999978989340644), new Position(-0.12753841280937195, 0.029098238795995712, -0.09789053350687027) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(0.0, 0.609322814663151, 0.0, 0.7929222581886419), new Position(-2.8421708795129297E-16, -4.2632563192693945E-16, -0.04292543604969978) )
	);


	public static final JointedModelPose TUNNEL_POSE = new JointedModelPose(
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.37068145334872094, 0.0, 0.0, 0.9287600659714434), new Position(3.355605754861909E-19, 5.329070399086743E-17, -0.26492035388946533) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(0.0, 0.5533916891901902, 0.0, 0.8329211477296202), new Position(-0.07335834950208664, 0.0019372437382116914, -0.041472021490335464) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER, new Orientation(-0.08245690478758833, -0.8162295575699238, -0.05028623516285384, 0.5695976323293516), new Position(0.07867980003356934, -5.684341759025859E-16, 0.022841691970825195) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(0.11345970396569453, 0.335998172748281, -0.6281082112163628, 0.6926125890340242), new Position(6.88338280561553E-17, 2.8421708795129297E-16, -0.11329611390829086) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(-0.913865810001099, 0.0, 0.0, 0.40601635596492325), new Position(-0.0, -4.973799171496525E-16, -5.373144813347608E-5) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.905269388233005, 0.0, 0.0, 0.42483800998526605), new Position(3.881022113909051E-16, 0.0, -0.05756701901555061) ),
		new JointIdTransformationPair( RIGHT_KNEE, new Orientation(0.06317772580098173, 0.0, -0.0, 0.9980022920628068), new Position(1.776356799695581E-17, 1.2967404207643823E-15, -0.20496971905231476) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.18729071062782476, 0.0, -0.0, 0.9823045300274882), new Position(-0.0, 6.252776040807564E-15, -5.373144813347608E-5) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(-0.11437109928174843, 0.06882535453933647, -0.0466579953751268, 0.9899521976788545), new Position(1.4210854397564648E-16, 5.684341759025859E-16, -0.04292537271976471) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(0.2594162735789509, 0.0, -0.0, 0.9657656014801992), new Position(0.12753799557685852, 0.029098233208060265, -0.0978902280330658) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(0.39525261354405494, -0.2233348784271988, -0.550575816027703, 0.7005449124573893), new Position(1.776356799695581E-17, 5.861977273559295E-16, -0.231216698884964) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.06717697648987322, -0.5706479611429566, -0.06939092837703519, 0.815495590013851), new Position(0.07335832715034485, 0.0019399999873712659, -0.04147103428840637) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(-0.012530596812503405, 0.001234680602980604, -0.1643457937338733, 0.9863224218222529), new Position(-5.062616928763243E-16, 0.0, -0.24058400094509125) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.6318022012710571, 0.022204283624887466) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(-0.16815083525111651, -0.21079181141843864, 0.773277295220981, 0.5738765838915273), new Position(0.10586899518966675, -0.027492839843034744, -0.007895448245108128) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.3962896460786887, 0.0, 0.0, 0.9181255450159459), new Position(-0.0, 0.0, -0.1990116387605667) ),
		new JointIdTransformationPair( LEFT_RING_FINGER, new Orientation(0.0, 0.7099287312403136, 0.0, 0.7042735239660217), new Position(-0.08537881076335907, 0.001574391731992364, 8.079967810772359E-4) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(0.6704989656556924, -0.2528101237401988, -0.2622662247855362, 0.6463239170306594), new Position(-3.552713467042264E-16, 1.4210854397564648E-16, -0.22732900083065033) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(0.35039591328221625, 0.24558330344081022, 0.5519225257873493, 0.7157465127786047), new Position(7.105427198782324E-17, -2.220446049250313E-16, -0.23121656477451324) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(0.06317772580098173, 0.0, -0.0, 0.9980022920628068), new Position(3.552713599391162E-17, 4.529709855767344E-16, -0.20496949553489685) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(0.10926361389387514, -0.3389317785628188, 0.4606627083709201, 0.8130046625176655), new Position(-5.995203909459372E-17, 2.8421708795129297E-16, -0.11329599469900131) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER, new Orientation(0.0, 0.799079678201254, 0.0, 0.6012251390999717), new Position(-0.07868067175149918, -1.4210853868169056E-15, 0.022840620949864388) ),
		new JointIdTransformationPair( NECK, new Orientation(0.4761391619866794, 0.0, -0.0, 0.879369944006857), new Position(-4.6629365578418697E-17, 0.0, -0.11043738573789597) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(0.635446505117782, 0.26584268961729324, 0.24798576800404384, 0.6812036864096411), new Position(2.9309886367796475E-16, 1.2903455972983202E-13, -0.22732849419116974) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.012293851519732316, -0.0064907556103608105, 0.21268743766754133, 0.9770213841901482), new Position(2.8421708795129297E-16, 1.3557155344092603E-13, -0.2405843734741211) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(-0.17358330723439722, 0.24762193036567867, -0.6742878683327876, 0.673712168266812), new Position(-0.10586892813444138, -0.027492910623550415, -0.007894791662693024) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(0.25941621613782495, 0.0, -0.0, 0.9657656169095756), new Position(-0.12753841280937195, 0.029098238795995712, -0.09789053350687027) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(-0.07081404800964389, 0.0107632835595668, -0.01719739450967635, 0.9972831954633462), new Position(-2.8421708795129297E-16, -4.2632563192693945E-16, -0.04292543604969978) )
	);


	private final ImplementationAndVisualType resourceType;
	PlayingCardResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	PlayingCardResource( ImplementationAndVisualType resourceType ) {
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
