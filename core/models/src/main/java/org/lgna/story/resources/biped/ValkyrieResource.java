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
import org.lgna.story.SBiped;
import org.lgna.story.implementation.BipedImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

public enum ValkyrieResource implements BipedResource {
	DEFAULT;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HEAD_0 = new JointId( HEAD, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME)
	public static final JointId HEAD_TOP = new JointId( HEAD_0, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId CAPE_TOP_0 = new JointId( NECK, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId CAPE_0 = new JointId( CAPE_TOP_0, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId CAPE_1 = new JointId( CAPE_0, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId CAPE_2 = new JointId( CAPE_1, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_TOP_0 = new JointId( NECK, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_0 = new JointId( HAIR_TOP_0, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_1 = new JointId( HAIR_0, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_2 = new JointId( HAIR_1, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId HAIR_BOTTOM_0 = new JointId( HAIR_2, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_THUMB_TIP = new JointId( RIGHT_THUMB_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_INDEX_FINGER_TIP = new JointId( RIGHT_INDEX_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_MIDDLE_FINGER_TIP = new JointId( RIGHT_MIDDLE_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER = new JointId( RIGHT_HAND, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_KNUCKLE = new JointId( RIGHT_RING_FINGER, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_RING_FINGER_TIP = new JointId( RIGHT_RING_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_PINKY_FINGER_TIP = new JointId( RIGHT_PINKY_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_THUMB_TIP = new JointId( LEFT_THUMB_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_INDEX_FINGER_TIP = new JointId( LEFT_INDEX_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_MIDDLE_FINGER_TIP = new JointId( LEFT_MIDDLE_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER = new JointId( LEFT_HAND, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_KNUCKLE = new JointId( LEFT_RING_FINGER, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_RING_FINGER_TIP = new JointId( LEFT_RING_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_PINKY_FINGER_TIP = new JointId( LEFT_PINKY_FINGER_KNUCKLE, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_TOES = new JointId( LEFT_FOOT, ValkyrieResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_TOES = new JointId( RIGHT_FOOT, ValkyrieResource.class );

	public static final JointId[] HAIR_BOTTOM_ARRAY = { HAIR_BOTTOM_0 };

	public static final JointId[] CAPE_ARRAY = { CAPE_0, CAPE_1, CAPE_2 };

	public static final JointId[] HAIR_TOP_ARRAY = { HAIR_TOP_0 };

	public static final JointId[] CAPE_TOP_ARRAY = { CAPE_TOP_0 };

	public static final JointId[] HEAD_ARRAY = { HEAD_0 };

	public static final JointId[] HAIR_ARRAY = { HAIR_0, HAIR_1, HAIR_2 };

	private final ImplementationAndVisualType resourceType;
	ValkyrieResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	ValkyrieResource( ImplementationAndVisualType resourceType ) {
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
