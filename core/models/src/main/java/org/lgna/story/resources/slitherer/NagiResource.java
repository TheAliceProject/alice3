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

package org.lgna.story.resources.slitherer;

import org.lgna.project.annotations.*;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum NagiResource implements org.lgna.story.resources.SlithererResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_EAR = new org.lgna.story.resources.JointId( HEAD, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_EAR_TIP = new org.lgna.story.resources.JointId( LEFT_EAR, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_EAR = new org.lgna.story.resources.JointId( HEAD, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_EAR_TIP = new org.lgna.story.resources.JointId( RIGHT_EAR, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_CLAVICLE = new org.lgna.story.resources.JointId( SPINE_UPPER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_SHOULDER = new org.lgna.story.resources.JointId( LEFT_CLAVICLE, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_ELBOW = new org.lgna.story.resources.JointId( LEFT_SHOULDER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_WRIST = new org.lgna.story.resources.JointId( LEFT_ELBOW, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_HAND = new org.lgna.story.resources.JointId( LEFT_WRIST, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_PINKY_FINGER = new org.lgna.story.resources.JointId( LEFT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_PINKY_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_PINKY_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_MIDDLE_FINGER = new org.lgna.story.resources.JointId( LEFT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_MIDDLE_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_MIDDLE_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER = new org.lgna.story.resources.JointId( LEFT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_RING_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_INDEX_FINGER = new org.lgna.story.resources.JointId( LEFT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_INDEX_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_INDEX_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_THUMB = new org.lgna.story.resources.JointId( LEFT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_THUMB_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_THUMB, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_THUMB_TIP = new org.lgna.story.resources.JointId( LEFT_THUMB_KNUCKLE, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_CLAVICLE = new org.lgna.story.resources.JointId( SPINE_UPPER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_SHOULDER = new org.lgna.story.resources.JointId( RIGHT_CLAVICLE, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_ELBOW = new org.lgna.story.resources.JointId( RIGHT_SHOULDER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_WRIST = new org.lgna.story.resources.JointId( RIGHT_ELBOW, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_HAND = new org.lgna.story.resources.JointId( RIGHT_WRIST, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_PINKY_FINGER = new org.lgna.story.resources.JointId( RIGHT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_PINKY_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_PINKY_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_MIDDLE_FINGER = new org.lgna.story.resources.JointId( RIGHT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_MIDDLE_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_MIDDLE_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER = new org.lgna.story.resources.JointId( RIGHT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_RING_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_INDEX_FINGER = new org.lgna.story.resources.JointId( RIGHT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_INDEX_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_INDEX_FINGER, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_THUMB = new org.lgna.story.resources.JointId( RIGHT_HAND, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_THUMB_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_THUMB, NagiResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_THUMB_TIP = new org.lgna.story.resources.JointId( RIGHT_THUMB_KNUCKLE, NagiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_1 = new org.lgna.story.resources.JointId( TAIL_0, NagiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_2 = new org.lgna.story.resources.JointId( TAIL_1, NagiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_3 = new org.lgna.story.resources.JointId( TAIL_2, NagiResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId TAIL_4 = new org.lgna.story.resources.JointId( TAIL_3, NagiResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3, TAIL_4 };
	public org.lgna.story.resources.JointId[] getTailArray(){
		return NagiResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	private NagiResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private NagiResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public org.lgna.story.resources.JointId[] getRootJointIds(){
		return org.lgna.story.resources.SlithererResource.JOINT_ID_ROOTS;
	}

	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.SlithererImp createImplementation( org.lgna.story.SSlitherer abstraction ) {
		return new org.lgna.story.implementation.SlithererImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
