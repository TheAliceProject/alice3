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

public enum SeagullResource implements org.lgna.story.resources.FlyerResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, SeagullResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, SeagullResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.043078986180409444, 0.3470781617466532, -0.013856216397796607, 0.9367438048129113), new Position(1.776356799695581E-17, -1.4210854397564648E-16, -0.19179965555667877) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.27217397057918336, -0.6237593403675638, 0.3927123185618436, 0.6185649924568676), new Position(0.10425299406051636, 0.08015521615743637, -0.03774765506386757) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.029445599502421992, -0.28672116990627855, 0.03358889302160315, 0.956972159293624), new Position(-7.993605433193992E-17, 2.1316281596346973E-16, -0.12953056395053864) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.029446825430198255, 0.28672163477974194, -0.033589173643946056, 0.9569719724397536), new Position(7.549516646860403E-17, 1.4210854397564648E-16, -0.12953050434589386) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.04307794692468836, -0.3470790110846328, 0.0138564547189428, 0.9367435343870215), new Position(-7.105427198782324E-17, -2.1316281596346973E-16, -0.191799595952034) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.2721746487073109, 0.62375967612432, -0.39271165915047307, 0.6185647741422219), new Position(-0.10425280034542084, 0.08015549927949905, -0.03774803504347801) )
	);

	public org.lgna.story.JointedModelPose getSpreadWingsPose(){
		return SeagullResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose FOLD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.24672107664485718, 0.9442045874457553, -0.061082247308342, 0.20946447538748927), new Position(1.776356799695581E-17, -1.4210854397564648E-16, -0.19179965555667877) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(0.39066462037612104, 0.8267271177604794, -0.31661776081664444, 0.25230263708745865), new Position(0.10425299406051636, 0.08015521615743637, -0.03774765506386757) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.007039663722833027, -0.9848391688451514, -0.023316254476617483, 0.17175158491333442), new Position(-7.993605433193992E-17, 2.1316281596346973E-16, -0.12953056395053864) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.007041075114891526, 0.9848399275677799, 0.023312859432998333, 0.17174763728963977), new Position(7.549516646860403E-17, 1.4210854397564648E-16, -0.12953050434589386) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.24672048129721577, -0.9442045873297488, 0.06107892083196142, 0.2094661471549333), new Position(-7.105427198782324E-17, -2.1316281596346973E-16, -0.191799595952034) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(0.3906647421516097, -0.8267267274639684, 0.31661900617876193, 0.2523021646017069), new Position(-0.10425280034542084, 0.08015549927949905, -0.03774803504347801) )
	);

	public org.lgna.story.JointedModelPose getFoldWingsPose(){
		return SeagullResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	public org.lgna.story.resources.JointId[] getNeckArray(){
		return SeagullResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return SeagullResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private SeagullResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private SeagullResource( ImplementationAndVisualType resourceType ) {
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
