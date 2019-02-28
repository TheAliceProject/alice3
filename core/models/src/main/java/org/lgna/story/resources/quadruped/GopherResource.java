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

import org.lgna.project.annotations.*;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum GopherResource implements org.lgna.story.resources.QuadrupedResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, GopherResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_EAR_TIP = new org.lgna.story.resources.JointId( LEFT_EAR, GopherResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_EAR_TIP = new org.lgna.story.resources.JointId( RIGHT_EAR, GopherResource.class );

	public static final org.lgna.story.JointedModelPose STANDING_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( BACK_RIGHT_ANKLE, new Orientation(0.10769756659306254, 0.0, -0.0, 0.9941837024161746), new Position(3.543831903073829E-15, -5.127441138341737E-9, -0.04050196334719658) ),
		new JointIdTransformationPair( BACK_RIGHT_KNEE, new Orientation(-0.2427685443265368, 0.0, 0.0, 0.9700842406129347), new Position(2.7444712406404217E-15, -9.6486743927926E-8, -0.06502123922109604) ),
		new JointIdTransformationPair( FRONT_RIGHT_KNEE, new Orientation(0.21759046043616914, 0.0, -0.0, 0.9760401587676482), new Position(-8.48055048408014E-9, 1.8329589579479943E-7, -0.08825784176588058) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.08554054829095022, 1.090263587339282E-8, -9.360488802242653E-10, 0.9963346900505289), new Position(-1.2660871157900433E-13, 1.2434497928741312E-16, -0.06683173775672913) ),
		new JointIdTransformationPair( FRONT_LEFT_ANKLE, new Orientation(-0.17829078521772038, 1.7840144457238992E-8, 3.23252539698328E-9, 0.9839778431989454), new Position(6.686470266359379E-10, 3.291749164913149E-11, -0.062381576746702194) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.7087709015845068, 0.7054387351620872, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.2588190601165086, 0.0, 0.0, 0.9659258222660823), new Position(-8.412741059338202E-10, 2.6645351995433716E-17, -0.05229594185948372) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.6650381550383573, 0.0, -0.0, 0.74680938153131), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(-0.13349715639147094, -1.2713697852898733E-8, 9.438325263127164E-8, 0.9910491961731174), new Position(4.008268994493136E-10, 7.549516646860403E-17, -0.04840134456753731) ),
		new JointIdTransformationPair( BACK_RIGHT_HIP, new Orientation(0.0, 0.03858850914587394, 0.9992551861070819, 6.123233995736766E-17), new Position(-0.05583290010690689, -0.018729202449321747, -0.03732302784919739) ),
		new JointIdTransformationPair( FRONT_RIGHT_CLAVICLE, new Orientation(-0.9486891441818007, -0.03538818278473128, 0.054993726167844384, 0.3093740039385549), new Position(0.05362340062856674, 0.037455394864082336, -0.02667793445289135) ),
		new JointIdTransformationPair( FRONT_LEFT_KNEE, new Orientation(0.21759046043616914, 0.0, -0.0, 0.9760401587676482), new Position(2.6645351995433716E-17, -8.16072556752978E-12, -0.08825785666704178) ),
		new JointIdTransformationPair( BACK_LEFT_ANKLE, new Orientation(0.11261196392963636, 0.0, -0.0, 0.9936390418959544), new Position(9.272244083646797E-11, -1.395589893649074E-10, -0.04050195589661598) ),
		new JointIdTransformationPair( FRONT_RIGHT_ANKLE, new Orientation(-0.17829078521772132, 0.0, 0.0, 0.9839778431989454), new Position(-5.527579283182149E-9, 2.4571642143200734E-7, -0.06238207221031189) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(1.4210854397564648E-16, 0.19541487097740173, 0.013087395578622818) ),
		new JointIdTransformationPair( BACK_RIGHT_HOCK, new Orientation(0.7281596909698698, 0.0, -0.0, 0.6854075170631438), new Position(-3.934630246805624E-15, 3.3880851901813E-8, -0.06496506184339523) ),
		new JointIdTransformationPair( BACK_LEFT_HIP, new Orientation(0.0, 0.03858850914587394, 0.9992551861070819, 6.123233995736766E-17), new Position(0.055832937359809875, -0.018729204311966896, -0.03732334449887276) ),
		new JointIdTransformationPair( FRONT_LEFT_CLAVICLE, new Orientation(-0.9393539643774075, 0.0520968235470615, -0.10174440586898617, 0.3233390271203302), new Position(-0.05362338945269585, 0.037455394864082336, -0.02667783945798874) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.26162246010429807, -0.018461742658399748, -0.012528523193009712, 0.9649123735000978), new Position(-1.776356799695581E-17, 2.4868995857482625E-16, -0.056210730224847794) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.15853128277480436, 0.01967869260855184, 0.010514223228024257, 0.9871018349435404), new Position(3.509025958692291E-8, -5.17588716775208E-7, -0.056210894137620926) ),
		new JointIdTransformationPair( BACK_LEFT_KNEE, new Orientation(-0.2427685443265368, 0.0, 0.0, 0.9700842406129347), new Position(-1.2896350213588686E-14, 3.552713599391162E-17, -0.06502098590135574) ),
		new JointIdTransformationPair( BACK_LEFT_HOCK, new Orientation(0.7196561061995315, 0.0, -0.0, 0.6943306768462191), new Position(5.312284032804593E-12, -3.108624482185328E-17, -0.06496499478816986) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.023285052097844677, 0.0, -0.0, 0.9997288664176907), new Position(1.0367098292263439E-16, -1.3322675997716858E-17, -0.05428590252995491) )
	);


	public static final org.lgna.story.JointedModelPose CROUCH_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( BACK_RIGHT_ANKLE, new Orientation(0.32906592654394085, 0.0, -0.0, 0.9443069500897352), new Position(-0.0, -5.127441138341737E-9, -0.04050196334719658) ),
		new JointIdTransformationPair( BACK_RIGHT_KNEE, new Orientation(-0.5877016847467218, 0.0, 0.0, 0.809077703157036), new Position(-0.0, -9.6486743927926E-8, -0.06502123922109604) ),
		new JointIdTransformationPair( FRONT_RIGHT_KNEE, new Orientation(0.3413932815288471, 0.0, -0.0, 0.9399205430923219), new Position(-8.48055048408014E-9, 1.8329589579479943E-7, -0.08825784176588058) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.36376101080625445, 1.0193087932345006E-8, -3.980545924610872E-9, 0.9314923118400988), new Position(-0.0, 0.0, -0.06683173775672913) ),
		new JointIdTransformationPair( FRONT_LEFT_ANKLE, new Orientation(0.5572246365625874, 1.5054987929179427E-8, -1.0102837767147605E-8, 0.8303617912739555), new Position(6.686470266359379E-10, 3.291749164913149E-11, -0.062381576746702194) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.5559056164292243, 0.8312454184068891, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(0.07915090270115042, 0.0, -0.0, 0.9968626458051244), new Position(-8.412741059338202E-10, 0.0, -0.05229594185948372) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.5240632771785935, 0.0, -0.0, 0.8516793302134511), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(0.06335343376547307, 6.0335094833133405E-9, 9.504454123914256E-8, 0.9979911534828927), new Position(4.008268994493136E-10, 0.0, -0.04840134456753731) ),
		new JointIdTransformationPair( BACK_RIGHT_HIP, new Orientation(0.0, 0.6217857081079987, 0.7831874189443001, 6.123233995736766E-17), new Position(-0.05583290010690689, -0.018729202449321747, -0.03732302784919739) ),
		new JointIdTransformationPair( FRONT_RIGHT_CLAVICLE, new Orientation(-0.8462465682304361, -0.017669803045799966, 0.01509481007082632, 0.5322843887692539), new Position(0.05362340062856674, 0.037455394864082336, -0.02667793445289135) ),
		new JointIdTransformationPair( FRONT_LEFT_KNEE, new Orientation(0.3413932815288471, 0.0, -0.0, 0.9399205430923219), new Position(-0.0, -8.16072556752978E-12, -0.08825785666704178) ),
		new JointIdTransformationPair( BACK_LEFT_ANKLE, new Orientation(0.3291966784754639, 0.0, -0.0, 0.9442613763575857), new Position(9.272244083646797E-11, -1.395589893649074E-10, -0.04050195589661598) ),
		new JointIdTransformationPair( FRONT_RIGHT_ANKLE, new Orientation(0.5588335442863557, 0.0, -0.0, 0.8292798500990782), new Position(-5.527579283182149E-9, 2.4571642143200734E-7, -0.06238207221031189) ),
		new JointIdTransformationPair( BACK_RIGHT_HOCK, new Orientation(0.650811119584774, 0.0, -0.0, 0.7592396766666064), new Position(-0.0, 3.3880851901813E-8, -0.06496506184339523) ),
		new JointIdTransformationPair( ROOT, new Orientation(-0.5365084529043711, 0.0, 0.0, 0.8438949460461048), new Position(-0.0, 0.19541487097740173, -0.007463501300662756) ),
		new JointIdTransformationPair( BACK_LEFT_HIP, new Orientation(0.0, 0.6217857081079987, 0.7831874189443001, 6.123233995736766E-17), new Position(0.055832937359809875, -0.018729204311966896, -0.03732334449887276) ),
		new JointIdTransformationPair( FRONT_LEFT_CLAVICLE, new Orientation(-0.8460252535779381, 0.01784326682291877, -0.01648152046654116, 0.5325891921740269), new Position(-0.05362338945269585, 0.037455394864082336, -0.02667783945798874) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.09135030200217556, 0.0203452561015438, 0.00915804325439565, 0.9955688439892163), new Position(3.509025958692291E-8, -5.17588716775208E-7, -0.056210894137620926) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.09429164830722851, -0.020318125545905598, -0.009218126847118433, 0.9952945719590945), new Position(-0.0, 0.0, -0.056210730224847794) ),
		new JointIdTransformationPair( BACK_LEFT_KNEE, new Orientation(-0.5945213101492242, 0.0, 0.0, 0.8040798541055795), new Position(-0.0, 0.0, -0.06502098590135574) ),
		new JointIdTransformationPair( BACK_LEFT_HOCK, new Orientation(0.650549041654649, 0.0, -0.0, 0.7594642482712519), new Position(5.312284032804593E-12, 0.0, -0.06496499478816986) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.14137049270311963, 0.0, 0.0, 0.9899567585469969), new Position(-0.0, 0.0, -0.05428590252995491) )
	);


	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return GopherResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private GopherResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private GopherResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public org.lgna.story.resources.JointId[] getRootJointIds(){
		return org.lgna.story.resources.QuadrupedResource.JOINT_ID_ROOTS;
	}

	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.QuadrupedImp createImplementation( org.lgna.story.SQuadruped abstraction ) {
		return new org.lgna.story.implementation.QuadrupedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
