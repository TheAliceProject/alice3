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

public enum DragonBabyResource implements QuadrupedResource {
	DEFAULT_PINK,
	DEFAULT_AQUA,
	DEFAULT_BLUE,
	DEFAULT_GREEN,
	DEFAULT_RED,
	TUTU_PINK,
	TUTU_AQUA,
	TUTU_BLUE,
	TUTU_GREEN,
	TUTU_RED;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_EAR_TIP = new JointId( LEFT_EAR, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_EAR_TIP = new JointId( RIGHT_EAR, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId LEFT_WING_SHOULDER = new JointId( FRONT_LEFT_CLAVICLE, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId LEFT_WING_ELBOW = new JointId( LEFT_WING_SHOULDER, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId LEFT_WING_WRIST = new JointId( LEFT_WING_ELBOW, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_WING_TIP = new JointId( LEFT_WING_WRIST, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId RIGHT_WING_SHOULDER = new JointId( FRONT_RIGHT_CLAVICLE, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId RIGHT_WING_ELBOW = new JointId( RIGHT_WING_SHOULDER, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId RIGHT_WING_WRIST = new JointId( RIGHT_WING_ELBOW, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_WING_TIP = new JointId( RIGHT_WING_WRIST, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_4 = new JointId( TAIL_3, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_5 = new JointId( TAIL_4, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_6 = new JointId( TAIL_5, DragonBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId TAIL_7 = new JointId( TAIL_6, DragonBabyResource.class );

	public static final JointedModelPose LEAP_POSE = new JointedModelPose(
		new JointIdTransformationPair( TAIL_2, new Orientation(1.3050571535089177E-4, -0.31563858039726966, 0.11130551463057665, 0.9423286857280092), new Position(-0.0, 0.0, -0.29213982820510864) ),
		new JointIdTransformationPair( TAIL_1, new Orientation(0.13148393274826833, 0.03748822795851807, -0.1551271486777222, 0.9783875387272156), new Position(-0.0, 0.0, -0.3043278753757477) ),
		new JointIdTransformationPair( TAIL_0, new Orientation(0.21686737104728582, -0.01698670849253014, -0.11132151449104151, 0.9696842349554052), new Position(-0.0, -0.020908674225211143, -0.31986454129219055) ),
		new JointIdTransformationPair( TAIL_6, new Orientation(-0.3744663900096793, -0.18587812009638213, 0.06346421064195354, 0.9061989523223625), new Position(-0.0, 0.0, -0.28549838066101074) ),
		new JointIdTransformationPair( BACK_RIGHT_ANKLE, new Orientation(0.10927754556400321, 0.0, -0.0, 0.9940112766138557), new Position(-0.0, 0.0, -0.049395859241485596) ),
		new JointIdTransformationPair( TAIL_5, new Orientation(-0.2834304355744445, -0.19493663288036006, 0.015485093149955826, 0.9388434956058893), new Position(-0.0, 0.0, -0.3085555136203766) ),
		new JointIdTransformationPair( BACK_RIGHT_KNEE, new Orientation(-0.21640391466931982, -0.3860637715512945, -0.00834691221608177, 0.8966908269118407), new Position(-0.0, 0.0, -0.24710328876972198) ),
		new JointIdTransformationPair( TAIL_4, new Orientation(-0.11658549601445635, -0.401731267884755, -0.21272913071701055, 0.8830436724574586), new Position(-0.0, 0.0, -0.27890968322753906) ),
		new JointIdTransformationPair( FRONT_RIGHT_KNEE, new Orientation(0.01930936357768857, 0.0023782277267260996, -0.014194731707665265, 0.9997099589895321), new Position(5.058140538360956E-12, 0.0, -0.20535147190093994) ),
		new JointIdTransformationPair( TAIL_3, new Orientation(-0.28022922176448267, -0.3029135647837943, -0.21469149344453684, 0.8852245580539442), new Position(-0.0, 0.0, -0.3004724383354187) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(0.11325667607991555, -0.0901313866923052, 0.10524270134771403, 0.9838563067182561), new Position(-0.0, 0.0, -0.18812794983386993) ),
		new JointIdTransformationPair( TAIL_7, new Orientation(-0.1688584781757473, -0.04244402882077808, 0.06021558879788814, 0.9828832085407478), new Position(-0.0, 0.0, -0.30005818605422974) ),
		new JointIdTransformationPair( FRONT_LEFT_ANKLE, new Orientation(-0.15961042580462909, -0.0033385258601073145, -0.002145660720442207, 0.9871721037183026), new Position(1.815404271354737E-9, 0.0, -0.1694529503583908) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.44639959750366653, 0.2695646831866616, -0.18046080395360997, 0.8339641354179782), new Position(-0.12549792230129242, 0.0927409827709198, -0.037354715168476105) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(-0.35552922103271767, -0.6369639194135416, -0.5000243790626656, 0.46674571096069434), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.5615461126963716, -0.05013247769363011, 0.23795323032107046, 0.7909051511879659), new Position(-0.0, 0.0, -0.1785990446805954) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.6125131776765171, 0.4374186201206259, -0.18884625979111316, 0.6307373844218291), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( FRONT_LEFT_KNEE, new Orientation(-0.0032663789453758916, 0.0, 0.0, 0.9999946653700634), new Position(-0.0, 0.0, -0.20535191893577576) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(0.2201269036440802, 0.005629154702627807, 0.024936487839637797, 0.9751362112462173), new Position(-1.5809398061505942E-12, -3.374083130947003E-12, -0.16245037317276) ),
		new JointIdTransformationPair( BACK_LEFT_ANKLE, new Orientation(-0.13995871054229525, 0.0, 0.0, 0.9901573407006272), new Position(-2.8115819134671982E-12, 0.0, -0.04939586669206619) ),
		new JointIdTransformationPair( FRONT_LEFT_FOOT, new Orientation(0.23990056055230585, 0.0034341926915057845, 0.0019889536114639467, 0.9707893548194575), new Position(-0.0, 0.0, -0.07602706551551819) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 1.409812331199646, 0.1542566567659378) ),
		new JointIdTransformationPair( BACK_LEFT_HIP, new Orientation(0.09303866464733608, -0.7050851295744915, -0.6730052281681312, 0.20313229627984908), new Position(0.28344565629959106, 0.13859230279922485, -0.019438786432147026) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.08228293679592259, 0.18887316242866534, 0.524302996339767, 0.826234116250095), new Position(-0.0, 0.0, -0.07331021130084991) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(0.11547136163011383, -0.1307968034404543, 0.25907314040332674, 0.9499682461927897), new Position(-0.0, 0.0, -0.308514267206192) ),
		new JointIdTransformationPair( FRONT_RIGHT_FOOT, new Orientation(0.1716936211077613, -0.0032920076968481265, -0.002226512066258518, 0.9851423784409273), new Position(-0.0, 0.0, -0.07602719962596893) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(0.16654788110915555, -0.004259861449439045, -0.02521168504212649, 0.9857017945686306), new Position(-0.0, 0.0, -0.16245037317276) ),
		new JointIdTransformationPair( BACK_LEFT_FOOT, new Orientation(-0.12596260407468027, 0.0, 0.0, 0.9920349904991886), new Position(-0.0, 0.0, -0.06562508642673492) ),
		new JointIdTransformationPair( NECK, new Orientation(0.08822574013037536, -0.09249073462468159, -0.008227839855474707, 0.9917630692047102), new Position(-0.0, 0.0, -0.17540343105793) ),
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.11170085883744657, 0.3549384321446811, 0.27863459759101844, 0.8853837521362946), new Position(-0.0, 0.0, -0.18973159790039062) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.3575637234987325, -0.4040060639835102, 0.3553067179959919, 0.7633376841537372), new Position(0.12549744546413422, 0.09274234622716904, -0.037355080246925354) ),
		new JointIdTransformationPair( FRONT_RIGHT_CLAVICLE, new Orientation(-0.5359275291877931, -0.2006378975547779, 0.07736472773291804, 0.8164195100720445), new Position(0.11903341114521027, 0.11363326013088226, 0.03545680642127991) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.06605759202274322, -0.20990360736861724, -0.292831789794765, 0.9304979382219999), new Position(-8.33224114704656E-12, 0.0, -0.18973098695278168) ),
		new JointIdTransformationPair( FRONT_RIGHT_ANKLE, new Orientation(-0.13141554901619537, 0.07204826450455828, -0.12377489532776899, 0.9809275082009133), new Position(-1.337880869457797E-12, 4.962907307337616E-10, -0.16945290565490723) ),
		new JointIdTransformationPair( BACK_RIGHT_HOCK, new Orientation(0.07664188240100821, 0.0, -0.0, 0.9970586852648293), new Position(-0.0, 0.0, -0.21168743073940277) ),
		new JointIdTransformationPair( FRONT_LEFT_CLAVICLE, new Orientation(-0.19870515502836406, 0.5527213108066109, -0.5817147051153397, 0.5626930031535392), new Position(-0.11903329193592072, 0.11363358795642853, 0.03545711934566498) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.20701637894619945, -0.13589565600358527, -0.3542751475209962, 0.9017570123799067), new Position(-0.0, 0.0, -0.07331031560897827) ),
		new JointIdTransformationPair( BACK_LEFT_KNEE, new Orientation(-0.2921860178516849, 0.2163111750632989, -0.39963175286032854, 0.8415053586403898), new Position(-0.0, 0.0, -0.2471032738685608) ),
		new JointIdTransformationPair( BACK_LEFT_HOCK, new Orientation(0.23655935277823212, 0.0, -0.0, 0.9716170401002362), new Position(-0.0, 0.0, -0.2116871476173401) )
	);


	public static final JointedModelPose SITTING_POSE = new JointedModelPose(
		new JointIdTransformationPair( TAIL_2, new Orientation(-0.016010302147572587, 0.0, 0.0, 0.9998718268983997), new Position(-0.0, 0.0, -0.29213982820510864) ),
		new JointIdTransformationPair( TAIL_1, new Orientation(0.03408407735284543, 0.0, -0.0, 0.9994189690370127), new Position(-0.0, 0.0, -0.3043278753757477) ),
		new JointIdTransformationPair( TAIL_0, new Orientation(0.0774837883021087, 0.0, -0.0, 0.9969936120910474), new Position(-0.0, -0.020908674225211143, -0.31986454129219055) ),
		new JointIdTransformationPair( TAIL_6, new Orientation(-0.002810269637016928, 0.0, 0.0, 0.9999960511844871), new Position(-0.0, 0.0, -0.28549838066101074) ),
		new JointIdTransformationPair( TAIL_5, new Orientation(0.004478500080040073, 0.0, -0.0, 0.9999899714682308), new Position(-0.0, 0.0, -0.3085555136203766) ),
		new JointIdTransformationPair( TAIL_4, new Orientation(0.0030444247835302554, 0.0, -0.0, 0.9999953657281305), new Position(-0.0, 0.0, -0.27890968322753906) ),
		new JointIdTransformationPair( FRONT_RIGHT_KNEE, new Orientation(-0.00241069551490863, 0.0, 0.0, 0.9999970942693456), new Position(5.058140538360956E-12, 0.0, -0.20535147190093994) ),
		new JointIdTransformationPair( TAIL_3, new Orientation(0.004699310682353554, 0.0, -0.0, 0.9999889581785945), new Position(-0.0, 0.0, -0.3004724383354187) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.24558041029520475, 0.0, 0.0, 0.9693762231864566), new Position(-0.0, 0.0, -0.18812794983386993) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.28032638057432735, 0.3772743315860891, -0.40020989286823544, 0.7867103919046465), new Position(-0.12549792230129242, 0.0927409827709198, -0.037354715168476105) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(0.0, 0.9922708791706761, 0.12409070210879426, 6.123233995736766E-17), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.7470663731290387, 0.0, 0.0, 0.6647494521545873), new Position(-0.0, 0.0, -0.1785990446805954) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.7471390736170068, 0.0, -0.0, 0.6646677400586858), new Position(-0.0, 0.0, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(0.4045048422977428, 0.0, -0.0, 0.914535856354292), new Position(-0.0, 0.0, -0.17540343105793) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.26562207277360816, -0.30924080864592124, 0.41065875380318384, 0.8155822611167343), new Position(0.12549744546413422, 0.09274234622716904, -0.037355080246925354) ),
		new JointIdTransformationPair( BACK_RIGHT_HIP, new Orientation(0.0, 0.9603783135744859, 0.2786996498308285, 6.123233995736766E-17), new Position(-0.28344598412513733, 0.1385926902294159, -0.01943856105208397) ),
		new JointIdTransformationPair( FRONT_RIGHT_CLAVICLE, new Orientation(-0.011030705474807804, -0.5597098974796698, -0.38698970467705873, 0.7326951089464078), new Position(0.11903341114521027, 0.11363326013088226, 0.03545680642127991) ),
		new JointIdTransformationPair( FRONT_LEFT_KNEE, new Orientation(0.0024168688810087074, 0.0, -0.0, 0.999997079368141), new Position(-0.0, 0.0, -0.20535191893577576) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(-0.0, 0.2272057980298996, 0.1542566567659378) ),
		new JointIdTransformationPair( BACK_LEFT_HIP, new Orientation(0.0, 0.9605313039590322, 0.27817191467644853, 6.123233995736766E-17), new Position(0.28344565629959106, 0.13859230279922485, -0.019438786432147026) ),
		new JointIdTransformationPair( FRONT_LEFT_CLAVICLE, new Orientation(-0.05799503304569289, 0.5828705087620147, 0.3898322490892328, 0.7105838189317074), new Position(-0.11903329193592072, 0.11363358795642853, 0.03545711934566498) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.24071114765194407, -0.2997453491198517, -0.6140875352873514, 0.6892803261966848), new Position(-0.0, 0.0, -0.07331031560897827) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.3109462636533445, 0.2601510524474346, 0.5661837727905219, 0.7176836256033765), new Position(-0.0, 0.0, -0.07331021130084991) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.2496935038507202, 0.0, 0.0, 0.9683249217771638), new Position(-0.0, 0.0, -0.308514267206192) )
	);


	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3, TAIL_4, TAIL_5, TAIL_6, TAIL_7 };
	@Override
	public JointId[] getTailArray(){
		return DragonBabyResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	DragonBabyResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	DragonBabyResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public JointId[] getRootJointIds(){
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
