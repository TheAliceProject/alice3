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

package org.lgna.story.resources.quadruped;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.JointedModelPose;
import org.lgna.story.SQuadruped;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.QuadrupedImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.QuadrupedResource;

public enum HippoResource implements QuadrupedResource {
	DEFAULT,
	TUTU;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, HippoResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_EAR_TIP = new JointId( LEFT_EAR, HippoResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_EAR_TIP = new JointId( RIGHT_EAR, HippoResource.class );

	public static final JointedModelPose STANDING_POSE = new JointedModelPose(
		new JointIdTransformationPair( NECK, new Orientation(0.0022508653460187713, 0.0, -0.0, 0.9999974667993885), new Position(-8.925206679297754E-17, -1.2434497928741312E-16, -0.289608895778656) ),
		new JointIdTransformationPair( FRONT_LEFT_KNEE, new Orientation(-0.05426715218312285, 0.003955025736003041, -0.010615048389096788, 0.9984621949343192), new Position(-5.684341759025859E-16, 7.105427198782324E-17, -0.26132869720458984) ),
		new JointIdTransformationPair( FRONT_RIGHT_KNEE, new Orientation(-0.05426584566892725, -0.00396666352640805, 0.010615279448153062, 0.9984622173204863), new Position(8.526512638538789E-16, -7.105427198782324E-17, -0.261328786611557) ),
		new JointIdTransformationPair( RIGHT_EAR, new Orientation(-0.033137539511456524, -0.8636337656459212, -0.05726496367460149, 0.49975928830459904), new Position(0.17778100073337555, 0.19248543679714203, 0.13140587508678436) ),
		new JointIdTransformationPair( FRONT_RIGHT_ANKLE, new Orientation(-0.0679310351886025, 0.03155173230666149, -0.42006335080385426, 0.9043984984275968), new Position(8.526512638538789E-16, -2.1316281596346973E-16, -0.24924065172672272) ),
		new JointIdTransformationPair( FRONT_LEFT_ANKLE, new Orientation(-0.06791386984462064, -0.0315363915219121, 0.4199833401929695, 0.9044374805661589), new Position(-0.0, 1.4210853868169056E-15, -0.24924002587795258) ),
		new JointIdTransformationPair( LEFT_EAR, new Orientation(-0.033138869263115225, 0.8636336856161202, 0.05726728763610603, 0.4997590721336452), new Position(-0.1777813881635666, 0.1924838274717331, 0.13140566647052765) ),
		new JointIdTransformationPair( MOUTH, new Orientation(-0.17029810503399848, 0.0, 0.0, 0.985392589490011), new Position(-5.609500045388838E-16, 0.11095729470252991, 0.06674396246671677) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.4876865165978015, 0.014419197074527764, 0.10182499998994379, 0.8669403772241066), new Position(-2.8421708795129297E-16, 1.776356799695581E-17, -0.303034245967865) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.4876852121686379, -0.014427443063590022, -0.10183110366414622, 0.8669402569065642), new Position(-1.7053025277077578E-15, 3.9079850255047273E-16, -0.3030356168746948) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.7196902042040786, 0.0, 0.0, 0.6942953333940043), new Position(9.702503006957914E-16, 5.542233135640874E-15, -0.31114962697029114) )
	);


	public static final JointedModelPose SWIMMING_POSE = new JointedModelPose(
		new JointIdTransformationPair( BACK_RIGHT_KNEE, new Orientation(-0.5608510909828361, 0.0, 0.0, 0.8279166949297269), new Position(-0.0, 0.0, -0.21854940056800842) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(0.4401825001844438, 0.0, -0.0, 0.8979083285789101), new Position(-0.14629380404949188, 0.2564859092235565, 9.687237325124443E-4) ),
		new JointIdTransformationPair( FRONT_RIGHT_KNEE, new Orientation(0.12967483877842592, -0.24337506629399683, 0.3849773330245208, 0.880764137752843), new Position(-0.0, 0.0, -0.261328786611557) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.0826067040828587, 0.0, 0.0, 0.9965822256294595), new Position(-0.0, 0.0, -0.3496585488319397) ),
		new JointIdTransformationPair( FRONT_LEFT_ANKLE, new Orientation(0.04109039549297724, -0.023438317823855546, 0.2763811367751101, 0.9598831657502472), new Position(-0.0, 0.0, -0.24924002587795258) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(-0.1607779554617116, 0.0, 0.0, 0.9869906023045771), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.3105579110349868, 0.0, 0.0, 0.9505544612980286), new Position(-0.0, 0.0, -0.31114962697029114) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.7567949690036236, 0.0, -0.0, 0.6536523348774976), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( BACK_RIGHT_HIP, new Orientation(0.32996935434807223, 0.0, -0.0, 0.9439916446617079), new Position(0.21336700022220612, 0.002060934668406844, -0.349062979221344) ),
		new JointIdTransformationPair( FRONT_LEFT_KNEE, new Orientation(0.11063458404121446, 0.31822006217434695, -0.07971660746166574, 0.9381584318965454), new Position(-0.0, 0.0, -0.26132869720458984) ),
		new JointIdTransformationPair( ROOT, new Orientation(-0.7742733629136523, 0.0, 0.0, 0.6328512933402156), new Position(-0.0, 0.8704078197479248, -0.2904745638370514) ),
		new JointIdTransformationPair( RIGHT_EYELID, new Orientation(0.4595419903216173, 0.42565431571354095, 0.23970674772606126, 0.7417413550155325), new Position(0.1462939977645874, 0.2564854323863983, 9.688819409348071E-4) ),
		new JointIdTransformationPair( BACK_LEFT_HIP, new Orientation(0.32996948982572744, 0.0, -0.0, 0.9439915973059025), new Position(-0.21336665749549866, 0.0020608932245522738, -0.34906303882598877) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.8679275390639664, 0.20221330575491123, 0.04596824795803549, 0.45132968669222995), new Position(-0.0, 0.0, -0.303034245967865) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.113304793443481, 0.0, 0.0, 0.9935602768743979), new Position(-0.0, 0.0, -0.3391798436641693) ),
		new JointIdTransformationPair( LEFT_EYELID, new Orientation(0.39702916142384165, -0.4043992926366909, -0.30645975918317264, 0.7647950530014018), new Position(-0.14629380404949188, 0.2564859092235565, 9.687237325124443E-4) ),
		new JointIdTransformationPair( NECK, new Orientation(0.09131544770816613, 0.0, -0.0, 0.9958220167328382), new Position(-0.0, 0.0, -0.289608895778656) ),
		new JointIdTransformationPair( FRONT_RIGHT_CLAVICLE, new Orientation(0.2126093467619942, -0.2967203708644296, -0.09486957098132259, 0.926150123730129), new Position(0.16560199856758118, -0.16726715862751007, -0.058004461228847504) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(0.4401820939574431, 0.0, -0.0, 0.8979085277238661), new Position(0.1462939977645874, 0.2564854323863983, 9.688819409348071E-4) ),
		new JointIdTransformationPair( BACK_RIGHT_HOCK, new Orientation(0.49277878621177507, 0.0, -0.0, 0.8701546229605688), new Position(-0.0, 0.0, -0.1791146695613861) ),
		new JointIdTransformationPair( FRONT_RIGHT_ANKLE, new Orientation(-0.00458398777898531, -0.0076191635401672, -0.31623383674649425, 0.948639602746787), new Position(-0.0, 0.0, -0.24924065172672272) ),
		new JointIdTransformationPair( FRONT_LEFT_CLAVICLE, new Orientation(0.26082912851063655, 0.27224303591310206, 0.3256171794829575, 0.8670786282355036), new Position(-0.16560208797454834, -0.1672673225402832, -0.05800887197256088) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.8804919598088404, -0.24086412625103057, -0.2828761925683918, 0.29444768818132194), new Position(-0.0, 0.0, -0.3030356168746948) ),
		new JointIdTransformationPair( BACK_LEFT_HOCK, new Orientation(0.48727722702171145, 0.0, -0.0, 0.873247332676162), new Position(-0.0, 0.0, -0.17911483347415924) ),
		new JointIdTransformationPair( BACK_LEFT_KNEE, new Orientation(-0.5579719919654297, 9.838420601253766E-4, -0.02008966532737354, 0.8296159916394845), new Position(-0.0, 0.0, -0.21854928135871887) )
	);


	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3 };
	@Override
	public JointId[] getTailArray() {
		return HippoResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	HippoResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	HippoResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public JointId[] getRootJointIds() {
		return QuadrupedResource.JOINT_ID_ROOTS;
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public QuadrupedImp createImplementation( SQuadruped abstraction ) {
		return new QuadrupedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
