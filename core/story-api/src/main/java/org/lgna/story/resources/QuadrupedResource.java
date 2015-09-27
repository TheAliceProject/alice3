/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.lgna.story.resources;

import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;

/**
 * @author dculyba
 * 
 */
@org.lgna.project.annotations.ResourceTemplate( modelClass = org.lgna.story.SQuadruped.class )
public interface QuadrupedResource extends JointedModelResource {
	public static final org.lgna.story.resources.JointId ROOT = new JointId( null, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId SPINE_BASE = new org.lgna.story.resources.JointId( ROOT, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId SPINE_MIDDLE = new org.lgna.story.resources.JointId( SPINE_BASE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId SPINE_UPPER = new org.lgna.story.resources.JointId( SPINE_MIDDLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId NECK = new org.lgna.story.resources.JointId( SPINE_UPPER, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId HEAD = new org.lgna.story.resources.JointId( NECK, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId LEFT_EYE = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId LEFT_EYELID = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId LEFT_EAR = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId MOUTH = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	//	public static final org.lgna.story.resources.JointId JAW_TIP = new org.lgna.story.resources.JointId( MOUTH, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_EAR = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_EYE = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId RIGHT_EYELID = new org.lgna.story.resources.JointId( HEAD, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_LEFT_CLAVICLE = new org.lgna.story.resources.JointId( SPINE_UPPER, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_LEFT_SHOULDER = new org.lgna.story.resources.JointId( FRONT_LEFT_CLAVICLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_LEFT_KNEE = new org.lgna.story.resources.JointId( FRONT_LEFT_SHOULDER, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_LEFT_ANKLE = new org.lgna.story.resources.JointId( FRONT_LEFT_KNEE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_LEFT_FOOT = new org.lgna.story.resources.JointId( FRONT_LEFT_ANKLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_LEFT_TOE = new org.lgna.story.resources.JointId( FRONT_LEFT_FOOT, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_RIGHT_CLAVICLE = new org.lgna.story.resources.JointId( SPINE_UPPER, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_RIGHT_SHOULDER = new org.lgna.story.resources.JointId( FRONT_RIGHT_CLAVICLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_RIGHT_KNEE = new org.lgna.story.resources.JointId( FRONT_RIGHT_SHOULDER, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_RIGHT_ANKLE = new org.lgna.story.resources.JointId( FRONT_RIGHT_KNEE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_RIGHT_FOOT = new org.lgna.story.resources.JointId( FRONT_RIGHT_ANKLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId FRONT_RIGHT_TOE = new org.lgna.story.resources.JointId( FRONT_RIGHT_FOOT, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId PELVIS_LOWER_BODY = new org.lgna.story.resources.JointId( ROOT, QuadrupedResource.class );

	//Tails are now included as arrays access
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId TAIL_0 = new org.lgna.story.resources.JointId( PELVIS_LOWER_BODY, QuadrupedResource.class );
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId TAIL_1 = new org.lgna.story.resources.JointId( TAIL_0, QuadrupedResource.class );
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId TAIL_2 = new org.lgna.story.resources.JointId( TAIL_1, QuadrupedResource.class );
	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final org.lgna.story.resources.JointId TAIL_3 = new org.lgna.story.resources.JointId( TAIL_2, QuadrupedResource.class );

	public static final org.lgna.story.resources.JointId BACK_LEFT_HIP = new org.lgna.story.resources.JointId( PELVIS_LOWER_BODY, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_LEFT_KNEE = new org.lgna.story.resources.JointId( BACK_LEFT_HIP, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_LEFT_HOCK = new org.lgna.story.resources.JointId( BACK_LEFT_KNEE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_LEFT_ANKLE = new org.lgna.story.resources.JointId( BACK_LEFT_HOCK, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_LEFT_FOOT = new org.lgna.story.resources.JointId( BACK_LEFT_ANKLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_LEFT_TOE = new org.lgna.story.resources.JointId( BACK_LEFT_FOOT, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_RIGHT_HIP = new org.lgna.story.resources.JointId( PELVIS_LOWER_BODY, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_RIGHT_KNEE = new org.lgna.story.resources.JointId( BACK_RIGHT_HIP, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_RIGHT_HOCK = new org.lgna.story.resources.JointId( BACK_RIGHT_KNEE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_RIGHT_ANKLE = new org.lgna.story.resources.JointId( BACK_RIGHT_HOCK, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_RIGHT_FOOT = new org.lgna.story.resources.JointId( BACK_RIGHT_ANKLE, QuadrupedResource.class );
	public static final org.lgna.story.resources.JointId BACK_RIGHT_TOE = new org.lgna.story.resources.JointId( BACK_RIGHT_FOOT, QuadrupedResource.class );

	@FieldTemplate( visibility = org.lgna.project.annotations.Visibility.COMPLETELY_HIDDEN )
	public static org.lgna.story.resources.JointId[] JOINT_ID_ROOTS = { ROOT };

	public org.lgna.story.resources.JointId[] getTailArray();

	public org.lgna.story.implementation.QuadrupedImp createImplementation( org.lgna.story.SQuadruped abstraction );
}
