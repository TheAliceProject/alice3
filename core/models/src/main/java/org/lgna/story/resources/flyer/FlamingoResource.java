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
import org.lgna.story.JointedModelPose;
import org.lgna.story.SFlyer;
import org.lgna.story.implementation.FlyerImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum FlamingoResource implements FlyerResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOE = new JointId( RIGHT_FOOT, FlamingoResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOE = new JointId( LEFT_FOOT, FlamingoResource.class );

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose SPREAD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.022583610652889437, -0.009622528163541583, 0.004804918329192585, 0.9996871011678956), new Position(1.4210854397564648E-16, -3.552713599391162E-17, -0.16872522234916687) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.38008125531629433, -0.5895224135299628, 0.36409546462892517, 0.6127283704380306), new Position(0.1615239977836609, 0.027831947430968285, -0.008098756894469261) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.022585566798122535, 0.00963552452732833, -0.0048053433517201245, 0.999686929750891), new Position(-1.4210854397564648E-16, -2.625810539055168E-13, -0.16872461140155792) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.3800748792258831, 0.5895281556943126, -0.364102111523191, 0.6127228510586548), new Position(-0.1615235060453415, 0.027827320620417595, -0.008100642822682858) )
	);

	@Override
	public JointedModelPose getSpreadWingsPose(){
		return FlamingoResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointedModelPose FOLD_WINGS_POSE = new JointedModelPose(
		new JointIdTransformationPair( LEFT_WING_WRIST, new Orientation(-0.1720469551054684, 0.08162321099631419, 0.09514984223846192, 0.9770793233855601), new Position(1.4210854397564648E-16, -3.552713599391162E-17, -0.16872522234916687) ),
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.4029969752928731, -0.8423191971062056, 0.039661718822472776, 0.35569475136864104), new Position(0.1615239977836609, 0.027831947430968285, -0.008098756894469261) ),
		new JointIdTransformationPair( RIGHT_WING_WRIST, new Orientation(-0.12909166895060495, -0.13182831553818858, -0.002541674665810646, 0.9828276431398357), new Position(-1.4210854397564648E-16, -2.625810539055168E-13, -0.16872461140155792) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.38446269109786196, 0.8681704191627212, -0.12348191182854473, 0.28848012045162236), new Position(-0.1615235060453415, 0.027827320620417595, -0.008100642822682858) )
	);

	@Override
	public JointedModelPose getFoldWingsPose(){
		return FlamingoResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	@Override
	public JointId[] getNeckArray(){
		return FlamingoResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	@Override
	public JointId[] getTailArray(){
		return FlamingoResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	FlamingoResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	FlamingoResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public JointId[] getRootJointIds(){
		return FlyerResource.JOINT_ID_ROOTS;
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public FlyerImp createImplementation( SFlyer abstraction ) {
		return new FlyerImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
