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

public enum OstrichBabyResource implements org.lgna.story.resources.FlyerResource {
	DEFAULT,
	TUTU;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOE = new org.lgna.story.resources.JointId( LEFT_FOOT, OstrichBabyResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOE = new org.lgna.story.resources.JointId( RIGHT_FOOT, OstrichBabyResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose SPREAD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.11530505334570243, -0.3111518378397419, -0.019558149772583727, 0.9431366588460915), new Position(0.053264498710632324, 0.004361819010227919, -0.052694011479616165) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.33712424894876997, -0.16282395327949592, -0.09647512742813068, 0.9222408312349276), new Position(-8.881783998477905E-18, 0.0, -0.044071439653635025) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.3371300096728568, 0.16282122205194674, 0.09647304407267146, 0.9222394255261713), new Position(-1.3322675997716858E-17, 3.552713599391162E-17, -0.04407140612602234) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.11530886110743206, 0.311152451687611, 0.019559458113965818, 0.9431359636644039), new Position(-0.053264494985342026, 0.0043619354255497456, -0.0526941679418087) )
	);

	public org.lgna.story.JointedModelPose getSpreadWingsPose(){
		return OstrichBabyResource.SPREAD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.JointedModelPose FOLD_WINGS_POSE = new org.lgna.story.JointedModelPose( 
		new JointIdTransformationPair( RIGHT_WING_SHOULDER, new Orientation(-0.5065088985082021, -0.5436209105646982, -0.00848788663461725, 0.6692181984295511), new Position(0.053264498710632324, 0.004361819010227919, -0.052694011479616165) ),
		new JointIdTransformationPair( LEFT_WING_ELBOW, new Orientation(-0.14729661209198372, -0.17095933866469354, -0.34120022211249906, 0.9125015183658247), new Position(-8.881783998477905E-18, 0.0, -0.044071439653635025) ),
		new JointIdTransformationPair( RIGHT_WING_ELBOW, new Orientation(-0.0445930182539574, 0.2424673070378421, 0.3251710235632905, 0.9129539271921782), new Position(-1.3322675997716858E-17, 3.552713599391162E-17, -0.04407140612602234) ),
		new JointIdTransformationPair( LEFT_WING_SHOULDER, new Orientation(-0.5361549331467905, 0.4489402224740307, -0.0016896836834987153, 0.7148340431711253), new Position(-0.053264494985342026, 0.0043619354255497456, -0.0526941679418087) )
	);

	public org.lgna.story.JointedModelPose getFoldWingsPose(){
		return OstrichBabyResource.FOLD_WINGS_POSE;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] NECK_ARRAY = { NECK_0, NECK_1 };
	public org.lgna.story.resources.JointId[] getNeckArray(){
		return OstrichBabyResource.NECK_ARRAY;
	}

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return OstrichBabyResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private OstrichBabyResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private OstrichBabyResource( ImplementationAndVisualType resourceType ) {
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
