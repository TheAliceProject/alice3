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
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum OdinResource implements org.lgna.story.resources.BipedResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LOWER_LIP = new org.lgna.story.resources.JointId( MOUTH, OdinResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME, methodNameHint="getBeard")
	public static final org.lgna.story.resources.JointId BEARD_0 = new org.lgna.story.resources.JointId( HEAD, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId BEARD_1 = new org.lgna.story.resources.JointId( BEARD_0, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId BEARD_2 = new org.lgna.story.resources.JointId( BEARD_1, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId BEARD_3 = new org.lgna.story.resources.JointId( BEARD_2, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_THUMB_TIP = new org.lgna.story.resources.JointId( RIGHT_THUMB_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_INDEX_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_INDEX_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_MIDDLE_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER = new org.lgna.story.resources.JointId( RIGHT_HAND, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( RIGHT_RING_FINGER, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_RING_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_RING_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_PINKY_FINGER_TIP = new org.lgna.story.resources.JointId( RIGHT_PINKY_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_THUMB_TIP = new org.lgna.story.resources.JointId( LEFT_THUMB_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_INDEX_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_INDEX_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_MIDDLE_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_MIDDLE_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER = new org.lgna.story.resources.JointId( LEFT_HAND, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER_KNUCKLE = new org.lgna.story.resources.JointId( LEFT_RING_FINGER, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_RING_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_RING_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_PINKY_FINGER_TIP = new org.lgna.story.resources.JointId( LEFT_PINKY_FINGER_KNUCKLE, OdinResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME, methodNameHint="getCloak")
	public static final org.lgna.story.resources.JointId CLOAK_0 = new org.lgna.story.resources.JointId( SPINE_UPPER, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId CLOAK_1 = new org.lgna.story.resources.JointId( CLOAK_0, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId CLOAK_2 = new org.lgna.story.resources.JointId( CLOAK_1, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId CLOAK_3 = new org.lgna.story.resources.JointId( CLOAK_2, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId CLOAK_4 = new org.lgna.story.resources.JointId( CLOAK_3, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_TOES = new org.lgna.story.resources.JointId( RIGHT_FOOT, OdinResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_TOES = new org.lgna.story.resources.JointId( LEFT_FOOT, OdinResource.class );

	public static final org.lgna.story.resources.JointId[] BEARD_ARRAY = { BEARD_0, BEARD_1, BEARD_2, BEARD_3 };

	public static final org.lgna.story.resources.JointId[] CLOAK_ARRAY = { CLOAK_0, CLOAK_1, CLOAK_2, CLOAK_3, CLOAK_4 };

	private final ImplementationAndVisualType resourceType;
	private OdinResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private OdinResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}


	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.BipedImp createImplementation( org.lgna.story.SBiped abstraction ) {
		return new org.lgna.story.implementation.BipedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
