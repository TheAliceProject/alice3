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

public enum FreyaResource implements BipedResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.PRIME_TIME, methodNameHint="getHair")
	public static final JointId HAIR_0 = new JointId( HEAD, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_1 = new JointId( HAIR_0, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_2 = new JointId( HAIR_1, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_3 = new JointId( HAIR_2, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_4 = new JointId( HAIR_3, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_5 = new JointId( HAIR_4, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_6 = new JointId( HAIR_5, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_THUMB_TIP = new JointId( LEFT_THUMB_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_INDEX_FINGER_TIP = new JointId( LEFT_INDEX_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_MIDDLE_FINGER_TIP = new JointId( LEFT_MIDDLE_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER = new JointId( LEFT_HAND, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_KNUCKLE = new JointId( LEFT_RING_FINGER, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_TIP = new JointId( LEFT_RING_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_PINKY_FINGER_TIP = new JointId( LEFT_PINKY_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_THUMB_TIP = new JointId( RIGHT_THUMB_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId( RIGHT_INDEX_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_MIDDLE_FINGER_TIP = new JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER = new JointId( RIGHT_HAND, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_KNUCKLE = new JointId( RIGHT_RING_FINGER, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_TIP = new JointId( RIGHT_RING_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_PINKY_FINGER_TIP = new JointId( RIGHT_PINKY_FINGER_KNUCKLE, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOES = new JointId( LEFT_FOOT, FreyaResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOES = new JointId( RIGHT_FOOT, FreyaResource.class );

	public static final JointedModelPose STRAIGHT_HAIR_POSE = new JointedModelPose(
		new JointIdTransformationPair( HAIR_5, new Orientation(-0.12799943958844273, 4.941312245704286E-8, 6.3773121724839114E-9, 0.9917742401701317), new Position(-6.93864757317364E-11, 7.105427198782324E-17, -0.11929963529109955) ),
		new JointIdTransformationPair( HAIR_0, new Orientation(0.0, 0.9169989149925444, 0.39888969641054445, 6.123233995736766E-17), new Position(-6.395538321157801E-9, 0.058704935014247894, 0.06564968824386597) ),
		new JointIdTransformationPair( HAIR_4, new Orientation(0.018896074315177255, 4.7698425088848027E-8, -9.014648639728618E-10, 0.9998214532482652), new Position(-3.892767705404587E-11, -1.4210854397564648E-16, -0.12737232446670532) ),
		new JointIdTransformationPair( HAIR_3, new Orientation(0.011264914057608955, 3.860835619795791E-8, -4.349641411302779E-10, 0.999936548842612), new Position(-1.0761477819976051E-10, 7.105427198782324E-17, -0.1260433793067932) ),
		new JointIdTransformationPair( HAIR_2, new Orientation(0.023618003051221147, 5.477263839468759E-8, -1.2939790614679257E-9, 0.9997210560610742), new Position(-4.1378866999508546E-10, 4.2632563192693945E-16, -0.12310846149921417) ),
		new JointIdTransformationPair( HAIR_1, new Orientation(-0.16246462583651486, 9.792227922266127E-8, 1.6123108728423562E-8, 0.9867143686760579), new Position(-1.5664688182469884E-17, 0.0, -0.12453165650367737) )
	);


	public static final JointId[] HAIR_ARRAY = { HAIR_0, HAIR_1, HAIR_2, HAIR_3, HAIR_4, HAIR_5, HAIR_6 };

	private final ImplementationAndVisualType resourceType;
	FreyaResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	FreyaResource( ImplementationAndVisualType resourceType ) {
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
