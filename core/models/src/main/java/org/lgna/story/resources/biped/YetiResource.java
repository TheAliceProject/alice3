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

public enum YetiResource implements BipedResource {
	DEFAULT,
	TUTU;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, YetiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOES = new JointId( LEFT_FOOT, YetiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOES = new JointId( RIGHT_FOOT, YetiResource.class );

	public static final JointedModelPose ROAR_POSE = new JointedModelPose(
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.31559790155431544, -0.02659484737617487, -0.2910022193351527, 0.9027781493642645), new Position(-6.750155905017657E-16, 0.0, -0.40061911940574646) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(-0.3146514737305478, 0.07418748807729088, 0.32356037635276624, 0.8892689972929634), new Position(3.9079850255047273E-16, -5.684341759025859E-16, -0.4006199240684509) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.02864537854932153, 0.4992243350015387, 0.016516264182847115, 0.8658416244599701), new Position(-2.4868995857482625E-16, -2.8421708795129297E-16, -0.5225135684013367) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.41600249836760383, -0.18420839986157034, -0.06463835321891499, 0.88816162384191), new Position(3.197442173277597E-16, -5.684341759025859E-16, -0.5213111042976379) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.02854931552409733, -0.5044612719339203, -0.016692178744633378, 0.8628007492293189), new Position(1.4210854397564648E-16, 5.684341759025859E-16, -0.5225085020065308) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.47324050721541644, 0.17125768511744668, 0.0380234911865106, 0.8632893151989651), new Position(-2.309263806517031E-16, -5.684341759025859E-16, -0.5213114023208618) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(0.140731867745832, 0.43924457580666293, -0.16340022090444686, 0.8721004023812696), new Position(-0.21006222069263458, -0.16559594869613647, -0.21146538853645325) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(0.1563760610316423, -0.44123640149728405, 0.1445797969220168, 0.8717532035226162), new Position(0.21006199717521667, -0.1655956506729126, -0.21146629750728607) )
	);


	public static final JointedModelPose TWIRL_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_HAND, new Orientation(-0.17383781549158972, 0.052801120109634975, 0.024806775958052012, 0.983044800345766), new Position(-0.0, 0.0, -0.19329427182674408) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.23694982801206957, 0.0, 0.0, 0.9715218880730637), new Position(-0.0, 0.0, -0.4716648757457733) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER, new Orientation(-0.07631152784628686, 0.03828326266545354, -0.11576685533576367, 0.989600413158322), new Position(0.15566129982471466, 0.005552301649004221, -0.15427255630493164) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER, new Orientation(-0.13084061205045922, 0.03114721101924546, -0.11290718283249723, 0.9844605393558509), new Position(0.16311794519424438, -0.020072348415851593, -0.20185531675815582) ),
		new JointIdTransformationPair( RIGHT_SHOULDER, new Orientation(-0.07773105952077483, 0.3477414656225317, 0.08532641176978874, 0.9304585745356769), new Position(-0.0, 0.0, -0.5213114023208618) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.6904393671590131, 0.0, -0.0, 0.7233902683040887), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( RIGHT_THUMB, new Orientation(-0.12664126105537116, 0.42465534158983786, 0.8512395201861865, 0.2811069389586717), new Position(-0.17975492775440216, -0.027096478268504143, 0.06650525331497192) ),
		new JointIdTransformationPair( RIGHT_ANKLE, new Orientation(-0.032096708832173124, 0.22437727642642583, -0.4271843372775354, 0.8752931972147132), new Position(-0.0, 0.0, -0.17402160167694092) ),
		new JointIdTransformationPair( RIGHT_HIP, new Orientation(0.03574491045090958, 0.14586608681072896, -0.21396834825991007, 0.9652268811208623), new Position(0.16449199616909027, 0.009030896238982677, -0.19471383094787598) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER_KNUCKLE, new Orientation(-0.2587704057986061, 0.0, 0.0, 0.9659388578387479), new Position(-0.0, 0.0, -0.09965150058269501) ),
		new JointIdTransformationPair( RIGHT_INDEX_FINGER, new Orientation(-0.28220373429719015, 0.058414208954472614, 0.1227599233063957, 0.9496730141321021), new Position(-0.16311801970005035, -0.020071599632501602, -0.20184610784053802) ),
		new JointIdTransformationPair( RIGHT_ELBOW, new Orientation(0.18158780930494217, 0.5491607948036837, -0.5173230420442702, 0.6307338258984376), new Position(-0.0, 0.0, -0.5225135684013367) ),
		new JointIdTransformationPair( LEFT_INDEX_FINGER_KNUCKLE, new Orientation(-0.28552107792291437, 0.0, 0.0, 0.9583724297274714), new Position(-0.0, 0.0, -0.09965123236179352) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.8384686708450317, 0.05296479910612106) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER, new Orientation(-0.2630794684324067, 0.13435869771887343, -0.03551968114852006, 0.9547121481827484), new Position(-0.0, 0.0, -0.24256038665771484) ),
		new JointIdTransformationPair( RIGHT_CLAVICLE, new Orientation(0.028214719532613373, -0.47724882652582445, 0.5867394352472908, 0.6535857421234874), new Position(0.21006199717521667, -0.1655956506729126, -0.21146629750728607) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.12154819982993155, 0.0, -0.0, 0.9925855303791724), new Position(-0.0, 0.0, -0.25851795077323914) ),
		new JointIdTransformationPair( RIGHT_WRIST, new Orientation(-0.2518748937709932, 0.27606159866672203, -0.5449731406998326, 0.7505686561169371), new Position(-0.0, 0.0, -0.40061911940574646) ),
		new JointIdTransformationPair( LEFT_FOOT, new Orientation(-0.06286931797706756, 0.032693507210176664, 0.10068132926810833, 0.9923919857496851), new Position(-0.0, 0.0, -0.16748571395874023) ),
		new JointIdTransformationPair( LEFT_ANKLE, new Orientation(-0.2709151218449633, -0.15733524610753763, 0.08223276459011028, 0.9460911105786148), new Position(-0.0, 0.0, -0.1740211695432663) ),
		new JointIdTransformationPair( LEFT_THUMB_KNUCKLE, new Orientation(-0.31054086352280164, 0.3508210084227616, 0.07182005839691726, 0.8805287453249966), new Position(-0.0, 0.0, -0.10778175294399261) ),
		new JointIdTransformationPair( LEFT_KNEE, new Orientation(-0.7009417852350677, 0.0, 0.0, 0.7132184894627146), new Position(-0.0, 0.0, -0.202312171459198) ),
		new JointIdTransformationPair( LEFT_SHOULDER, new Orientation(-0.22826362658953916, -0.4022047153741073, 0.12409890032494364, 0.8779103295013475), new Position(-0.0, 0.0, -0.5213111042976379) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER, new Orientation(-0.1147321772746764, 0.20568939459422672, 0.07319944735704456, 0.9691079616615973), new Position(-0.15566106140613556, 0.005557908210903406, -0.1542782336473465) ),
		new JointIdTransformationPair( RIGHT_HAND, new Orientation(-0.15241839489549486, -0.05333045363178744, -0.02365252794948147, 0.9865926482264642), new Position(-0.0, 0.0, -0.19329524040222168) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.28552107792291437, 0.0, 0.0, 0.9583724297274714), new Position(-0.0, 0.0, -0.09735599905252457) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.15518502651398858, 0.0, 0.0, 0.9878854222762135), new Position(-0.0, 0.0, -0.3137291669845581) ),
		new JointIdTransformationPair( RIGHT_THUMB_KNUCKLE, new Orientation(-0.36470357209309046, 0.21232315640445093, 0.18456028533603736, 0.8876078429315908), new Position(-0.0, 0.0, -0.10778135061264038) ),
		new JointIdTransformationPair( LEFT_MIDDLE_FINGER, new Orientation(-0.2554704705814877, 0.12247442024715016, -0.013448416767465155, 0.9589337803688252), new Position(-0.0, 0.0, -0.2425697296857834) ),
		new JointIdTransformationPair( LEFT_WRIST, new Orientation(-0.29329969446884546, -0.22778292697522007, 0.44694246380622005, 0.8138382280588135), new Position(-0.0, 0.0, -0.4006199240684509) ),
		new JointIdTransformationPair( RIGHT_PINKY_FINGER_KNUCKLE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.0, -0.09122301638126373) ),
		new JointIdTransformationPair( RIGHT_MIDDLE_FINGER_KNUCKLE, new Orientation(-0.2587704057986061, 0.0, 0.0, 0.9659388578387479), new Position(-0.0, 0.0, -0.09736298769712448) ),
		new JointIdTransformationPair( LEFT_ELBOW, new Orientation(0.07959684337412891, -0.5432785609272243, 0.508816844479058, 0.6630370777988911), new Position(-0.0, 0.0, -0.5225085020065308) ),
		new JointIdTransformationPair( LEFT_CLAVICLE, new Orientation(0.05171795734112225, 0.45703524280065777, -0.5698621203950512, 0.6809560951085332), new Position(-0.21006222069263458, -0.16559594869613647, -0.21146538853645325) ),
		new JointIdTransformationPair( LEFT_PINKY_FINGER_KNUCKLE, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.0, -0.09122179448604584) ),
		new JointIdTransformationPair( LEFT_HIP, new Orientation(0.5343452682394443, 0.18772174256947288, 0.23481769033920014, 0.7899976797311244), new Position(-0.1644924134016037, 0.009030669927597046, -0.19471397995948792) ),
		new JointIdTransformationPair( LEFT_THUMB, new Orientation(-0.37957360112446537, -0.22029786102417914, -0.4501215225007903, 0.7776781781297114), new Position(0.17975540459156036, -0.027099402621388435, 0.06650011986494064) )
	);


	private final ImplementationAndVisualType resourceType;
	YetiResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	YetiResource( ImplementationAndVisualType resourceType ) {
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
