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

public enum DragonResource implements org.lgna.story.resources.QuadrupedResource {
	DEFAULT_PURPLE,
	DEFAULT_GREEN,
	DEFAULT_BLUE,
	DEFAULT_ORANGE,
	DEFAULT_RED,
	TUTU_PURPLE,
	TUTU_GREEN,
	TUTU_BLUE,
	TUTU_ORANGE,
	TUTU_RED;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_EAR_TIP = new org.lgna.story.resources.JointId( LEFT_EAR, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_EAR_TIP = new org.lgna.story.resources.JointId( RIGHT_EAR, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_WING_BASE = new org.lgna.story.resources.JointId( FRONT_LEFT_CLAVICLE, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_WING_CLAVICLE = new org.lgna.story.resources.JointId( LEFT_WING_BASE, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_WING_SHOULDER = new org.lgna.story.resources.JointId( LEFT_WING_CLAVICLE, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_WING_ELBOW = new org.lgna.story.resources.JointId( LEFT_WING_SHOULDER, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_WING_WRIST = new org.lgna.story.resources.JointId( LEFT_WING_ELBOW, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_WING_TIP = new org.lgna.story.resources.JointId( LEFT_WING_WRIST, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_WING_BASE = new org.lgna.story.resources.JointId( FRONT_RIGHT_CLAVICLE, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_WING_CLAVICLE = new org.lgna.story.resources.JointId( RIGHT_WING_BASE, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_WING_SHOULDER = new org.lgna.story.resources.JointId( RIGHT_WING_CLAVICLE, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_WING_ELBOW = new org.lgna.story.resources.JointId( RIGHT_WING_SHOULDER, DragonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_WING_WRIST = new org.lgna.story.resources.JointId( RIGHT_WING_ELBOW, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_WING_TIP = new org.lgna.story.resources.JointId( RIGHT_WING_WRIST, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_4 = new org.lgna.story.resources.JointId( TAIL_3, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_5 = new org.lgna.story.resources.JointId( TAIL_4, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_6 = new org.lgna.story.resources.JointId( TAIL_5, DragonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_7 = new org.lgna.story.resources.JointId( TAIL_6, DragonResource.class );

	public static final org.lgna.story.JointedModelPose DANCING_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( TAIL_0, new Orientation(0.5926301915099443, 2.987555342030624E-4, -1.1483609126700141E-4, 0.8054746139194516), new Position(9.934968869991252E-15, -1.4921396773335746E-14, -0.4251580536365509) ),
		new JointIdTransformationPair( FRONT_RIGHT_FOOT, new Orientation(-0.1281214412389045, -0.001284325786318459, -0.0016164170944888668, 0.9917563380175178), new Position(-1.4210854397564648E-16, -2.8421708795129297E-16, -0.1468764692544937) ),
		new JointIdTransformationPair( LEFT_EYE, new Orientation(0.1969319088129337, 0.29729956482073827, 0.06280644138591618, 0.9321406240311231), new Position(-0.10730500519275665, 0.07058603316545486, -0.2106112241744995) ),
		new JointIdTransformationPair( SPINE_UPPER, new Orientation(-0.45470129077178917, -2.419165635534466E-4, -2.2537280720606392E-4, 0.8906439394359246), new Position(-1.71390681411742E-17, 5.11590756194745E-14, -0.4183505177497864) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.0730233687738638, -0.12470467473962578, -0.02937883863640779, 0.9890668407905384), new Position(-0.0, 0.0, -0.8766546845436096) ),
		new JointIdTransformationPair( FRONT_LEFT_ANKLE, new Orientation(-0.4309084758609762, -0.0013261935327489869, -0.001578420672632007, 0.9023932819065446), new Position(1.8161345760603353E-9, 7.423722192534399E-10, -0.3803081512451172) ),
		new JointIdTransformationPair( PELVIS_LOWER_BODY, new Orientation(3.860201876756302E-4, 0.7781378531832399, 0.6280934105941515, 6.123233995736766E-17), new Position(-0.0, 1.4210854397564648E-16, -0.0) ),
		new JointIdTransformationPair( HEAD, new Orientation(-0.6462948305186887, 1.9686146771354286E-4, 5.4477094288847116E-5, 0.7630877736686835), new Position(-3.437781960144437E-12, 9.94759834299305E-16, -0.596635639667511) ),
		new JointIdTransformationPair( SPINE_BASE, new Orientation(0.718261375834034, 2.726910295763387E-4, -2.815046887740759E-4, 0.6957732693771105), new Position(-0.0, 1.4210854397564648E-16, -0.0) ),
		new JointIdTransformationPair( NECK, new Orientation(0.43094868472580555, -1.5120981701557384E-4, 3.4781933722822806E-5, 0.9023764220428782), new Position(-8.50708342326445E-17, -1.337383545921278E-12, -0.367740660905838) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.11357552301056843, 0.11353418114770815, 0.016806741845329987, 0.9868779679942904), new Position(7.105427198782324E-17, -2.1316281596346973E-16, -0.8766559362411499) ),
		new JointIdTransformationPair( BACK_RIGHT_HIP, new Orientation(3.860201991799267E-4, 0.41707666704297297, 0.9088712256402064, 6.123233995736766E-17), new Position(-0.6370894908905029, 0.10917611420154572, 0.03491037338972092) ),
		new JointIdTransformationPair( FRONT_LEFT_FOOT, new Orientation(-0.06472464203532409, 0.0013826518194829315, 0.0015292069844945063, 0.9979010324242298), new Position(2.8421708795129297E-16, 5.783817435406347E-14, -0.14687533676624298) ),
		new JointIdTransformationPair( RIGHT_EYE, new Orientation(0.19626211742746374, -0.30703218662158466, -0.06486910985913212, 0.9289803099244572), new Position(0.10730580240488052, 0.07058494538068771, -0.21061353385448456) ),
		new JointIdTransformationPair( FRONT_RIGHT_ANKLE, new Orientation(-0.36848160710944433, 0.0014326038866430933, 0.0014865884337764937, 0.9296327462621858), new Position(3.552713467042264E-16, -1.4210854397564648E-16, -0.3803105652332306) ),
		new JointIdTransformationPair( ROOT, new Orientation(0.0, 0.0, 0.0, 1.0), new Position(1.4210854397564648E-16, 0.7196337580680847, 0.08737537264823914) ),
		new JointIdTransformationPair( BACK_LEFT_HIP, new Orientation(3.860201991799267E-4, 0.41707759596022975, 0.9088707993639367, 6.123233995736766E-17), new Position(0.6370964050292969, 0.10943447053432465, 0.03396546095609665) ),
		new JointIdTransformationPair( FRONT_RIGHT_SHOULDER, new Orientation(-0.13501023746322305, 0.2770416697566224, 0.01019860488953046, 0.9512708013267799), new Position(-2.8421708795129297E-16, -2.355689865396471E-9, -0.22962525486946106) ),
		new JointIdTransformationPair( FRONT_LEFT_SHOULDER, new Orientation(-0.11110501455133663, -0.2888147561145715, 0.07895525364746792, 0.9476327243787499), new Position(-0.0, -9.94759834299305E-16, -0.22962559759616852) ),
		new JointIdTransformationPair( SPINE_MIDDLE, new Orientation(-0.16222318786967693, -1.0550627427931851E-4, -2.694725513254918E-5, 0.9867540856057658), new Position(-7.497171286563464E-13, -4.2632563192693945E-16, -0.5874068140983582) )
	);


	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3, TAIL_4, TAIL_5, TAIL_6, TAIL_7 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return DragonResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private DragonResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private DragonResource( ImplementationAndVisualType resourceType ) {
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
