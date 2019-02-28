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

public enum RoyalHotelResource implements org.lgna.story.resources.PropResource {
	DEFAULT;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId ROOT = new org.lgna.story.resources.JointId( null, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_TOP = new org.lgna.story.resources.JointId( ROOT, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_BOTTOM = new org.lgna.story.resources.JointId( ROOT, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_TOP = new org.lgna.story.resources.JointId( ROOT, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_BOTTOM = new org.lgna.story.resources.JointId( ROOT, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_00 = new org.lgna.story.resources.JointId( ROOT, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_01 = new org.lgna.story.resources.JointId( LEFT_STAIRS_00, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_02 = new org.lgna.story.resources.JointId( LEFT_STAIRS_01, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_03 = new org.lgna.story.resources.JointId( LEFT_STAIRS_02, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_04 = new org.lgna.story.resources.JointId( LEFT_STAIRS_03, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_05 = new org.lgna.story.resources.JointId( LEFT_STAIRS_04, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_06 = new org.lgna.story.resources.JointId( LEFT_STAIRS_05, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_07 = new org.lgna.story.resources.JointId( LEFT_STAIRS_06, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_08 = new org.lgna.story.resources.JointId( LEFT_STAIRS_07, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_09 = new org.lgna.story.resources.JointId( LEFT_STAIRS_08, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_10 = new org.lgna.story.resources.JointId( LEFT_STAIRS_09, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_11 = new org.lgna.story.resources.JointId( LEFT_STAIRS_10, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_12 = new org.lgna.story.resources.JointId( LEFT_STAIRS_11, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_13 = new org.lgna.story.resources.JointId( LEFT_STAIRS_12, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_14 = new org.lgna.story.resources.JointId( LEFT_STAIRS_13, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_15 = new org.lgna.story.resources.JointId( LEFT_STAIRS_14, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_16 = new org.lgna.story.resources.JointId( LEFT_STAIRS_15, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_17 = new org.lgna.story.resources.JointId( LEFT_STAIRS_16, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_18 = new org.lgna.story.resources.JointId( LEFT_STAIRS_17, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_19 = new org.lgna.story.resources.JointId( LEFT_STAIRS_18, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId LEFT_STAIRS_20 = new org.lgna.story.resources.JointId( LEFT_STAIRS_19, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_00 = new org.lgna.story.resources.JointId( ROOT, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_01 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_00, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_02 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_01, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_03 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_02, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_04 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_03, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_05 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_04, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_06 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_05, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_07 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_06, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_08 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_07, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_09 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_08, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_10 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_09, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_11 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_10, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_12 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_11, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_13 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_12, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_14 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_13, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_15 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_14, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_16 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_15, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_17 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_16, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_18 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_17, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_19 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_18, RoyalHotelResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final org.lgna.story.resources.JointId RIGHT_STAIRS_20 = new org.lgna.story.resources.JointId( RIGHT_STAIRS_19, RoyalHotelResource.class );

@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId[] JOINT_ID_ROOTS = { ROOT };

	public static final org.lgna.story.resources.JointId[] RIGHT_STAIRS_ARRAY = { RIGHT_STAIRS_00, RIGHT_STAIRS_01, RIGHT_STAIRS_02, RIGHT_STAIRS_03, RIGHT_STAIRS_04, RIGHT_STAIRS_05, RIGHT_STAIRS_06, RIGHT_STAIRS_07, RIGHT_STAIRS_08, RIGHT_STAIRS_09, RIGHT_STAIRS_10, RIGHT_STAIRS_11, RIGHT_STAIRS_12, RIGHT_STAIRS_13, RIGHT_STAIRS_14, RIGHT_STAIRS_15, RIGHT_STAIRS_16, RIGHT_STAIRS_17, RIGHT_STAIRS_18, RIGHT_STAIRS_19, RIGHT_STAIRS_20 };

	public static final org.lgna.story.resources.JointId[] LEFT_STAIRS_ARRAY = { LEFT_STAIRS_00, LEFT_STAIRS_01, LEFT_STAIRS_02, LEFT_STAIRS_03, LEFT_STAIRS_04, LEFT_STAIRS_05, LEFT_STAIRS_06, LEFT_STAIRS_07, LEFT_STAIRS_08, LEFT_STAIRS_09, LEFT_STAIRS_10, LEFT_STAIRS_11, LEFT_STAIRS_12, LEFT_STAIRS_13, LEFT_STAIRS_14, LEFT_STAIRS_15, LEFT_STAIRS_16, LEFT_STAIRS_17, LEFT_STAIRS_18, LEFT_STAIRS_19, LEFT_STAIRS_20 };

	private final ImplementationAndVisualType resourceType;
	private RoyalHotelResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	private RoyalHotelResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public org.lgna.story.resources.JointId[] getRootJointIds(){
		return RoyalHotelResource.JOINT_ID_ROOTS;
	}

	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory<org.lgna.story.resources.JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	public org.lgna.story.implementation.BasicJointedModelImp createImplementation( org.lgna.story.SJointedModel abstraction ) {
		return new org.lgna.story.implementation.BasicJointedModelImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
