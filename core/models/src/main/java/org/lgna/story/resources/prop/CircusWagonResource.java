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

package org.lgna.story.resources.prop;

import org.lgna.project.annotations.*;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.Orientation;
import org.lgna.story.Position;
import org.lgna.story.resources.ImplementationAndVisualType;

public enum CircusWagonResource implements org.lgna.story.resources.PropResource {
	DEFAULT,
	CIRCUS_WAGON_COVER,
	CIRCUS_WAGON_COVER_OPEN;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ROOT = new org.lgna.story.resources.JointId( null, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId DOOR = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId AXEL_FRONT_0 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId WHEEL_FRONT_LEFT = new org.lgna.story.resources.JointId( AXEL_FRONT_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId WHEEL_FRONT_RIGHT = new org.lgna.story.resources.JointId( AXEL_FRONT_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId TONGUE = new org.lgna.story.resources.JointId( AXEL_FRONT_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId YOKE = new org.lgna.story.resources.JointId( TONGUE, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId AXEL_BACK_0 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId WHEEL_BACK_LEFT = new org.lgna.story.resources.JointId( AXEL_BACK_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId WHEEL_BACK_RIGHT = new org.lgna.story.resources.JointId( AXEL_BACK_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_00 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_01 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_02 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_03 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_04 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_05 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_06 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_07 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_08 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_09 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_10 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_11 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_12 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_13 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_14 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_15 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_16 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_17 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ORGAN_18 = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId WAGON_COVER_BASE = new org.lgna.story.resources.JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId WAGON_COVER = new org.lgna.story.resources.JointId( WAGON_COVER_BASE, CircusWagonResource.class );

@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] JOINT_ID_ROOTS = { ROOT };

	public static final org.lgna.story.resources.JointId[] AXEL_FRONT_ARRAY = { AXEL_FRONT_0 };

	public static final org.lgna.story.resources.JointId[] ORGAN_ARRAY = { ORGAN_00, ORGAN_01, ORGAN_02, ORGAN_03, ORGAN_04, ORGAN_05, ORGAN_06, ORGAN_07, ORGAN_08, ORGAN_09, ORGAN_10, ORGAN_11, ORGAN_12, ORGAN_13, ORGAN_14, ORGAN_15, ORGAN_16, ORGAN_17, ORGAN_18 };

	public static final org.lgna.story.resources.JointId[] AXEL_BACK_ARRAY = { AXEL_BACK_0 };

	private final ImplementationAndVisualType resourceType;
	private CircusWagonResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private CircusWagonResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public org.lgna.story.resources.JointId[] getRootJointIds(){
		return CircusWagonResource.JOINT_ID_ROOTS;
	}

	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.BasicJointedModelImp createImplementation( org.lgna.story.SJointedModel abstraction ) {
		return new org.lgna.story.implementation.BasicJointedModelImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
