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
import org.lgna.story.SJointedModel;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.PropResource;

public enum CircusWagonResource implements PropResource {
	DEFAULT,
	CIRCUS_WAGON_COVER,
	CIRCUS_WAGON_COVER_OPEN;

@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ROOT = new JointId( null, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId DOOR = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId AXEL_FRONT_0 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId WHEEL_FRONT_LEFT = new JointId( AXEL_FRONT_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId WHEEL_FRONT_RIGHT = new JointId( AXEL_FRONT_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId TONGUE = new JointId( AXEL_FRONT_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId YOKE = new JointId( TONGUE, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId AXEL_BACK_0 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId WHEEL_BACK_LEFT = new JointId( AXEL_BACK_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId WHEEL_BACK_RIGHT = new JointId( AXEL_BACK_0, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_00 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_01 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_02 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_03 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_04 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_05 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_06 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_07 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_08 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_09 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_10 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_11 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_12 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_13 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_14 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_15 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_16 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_17 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.COMPLETELY_HIDDEN)
	public static final JointId ORGAN_18 = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId WAGON_COVER_BASE = new JointId( ROOT, CircusWagonResource.class );
@FieldTemplate(visibility=Visibility.PRIME_TIME)
	public static final JointId WAGON_COVER = new JointId( WAGON_COVER_BASE, CircusWagonResource.class );

@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] JOINT_ID_ROOTS = { ROOT };

	public static final JointId[] AXEL_FRONT_ARRAY = { AXEL_FRONT_0 };

	public static final JointId[] ORGAN_ARRAY = { ORGAN_00, ORGAN_01, ORGAN_02, ORGAN_03, ORGAN_04, ORGAN_05, ORGAN_06, ORGAN_07, ORGAN_08, ORGAN_09, ORGAN_10, ORGAN_11, ORGAN_12, ORGAN_13, ORGAN_14, ORGAN_15, ORGAN_16, ORGAN_17, ORGAN_18 };

	public static final JointId[] AXEL_BACK_ARRAY = { AXEL_BACK_0 };

	private final ImplementationAndVisualType resourceType;
	CircusWagonResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	CircusWagonResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	@Override
	public JointId[] getRootJointIds(){
		return CircusWagonResource.JOINT_ID_ROOTS;
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public BasicJointedModelImp createImplementation( SJointedModel abstraction ) {
		return new BasicJointedModelImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
