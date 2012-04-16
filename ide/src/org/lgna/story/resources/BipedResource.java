/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.lgna.story.resources;


/**
 * @author Dennis Cosgrove
 */
@org.lgna.project.annotations.ResourceTemplate( modelClass=org.lgna.story.Biped.class )
public interface BipedResource extends JointedModelResource {
	public static final JointId ROOT = new JointId( null, BipedResource.class );

	public static final JointId PELVIS_LOWER_BODY = new JointId( ROOT, BipedResource.class );
	
	public static final JointId LEFT_HIP = new JointId( PELVIS_LOWER_BODY, BipedResource.class );
	public static final JointId LEFT_KNEE = new JointId( LEFT_HIP, BipedResource.class );
	public static final JointId LEFT_ANKLE = new JointId( LEFT_KNEE, BipedResource.class );
	
	public static final JointId RIGHT_HIP = new JointId( PELVIS_LOWER_BODY, BipedResource.class );
	public static final JointId RIGHT_KNEE = new JointId( RIGHT_HIP, BipedResource.class );
	public static final JointId RIGHT_ANKLE = new JointId( RIGHT_KNEE, BipedResource.class );
	
	public static final JointId SPINE_BASE = new JointId( ROOT, BipedResource.class );
	
	public static final JointId SPINE_MIDDLE = new JointId( SPINE_BASE, BipedResource.class );
	public static final JointId SPINE_UPPER = new JointId( SPINE_MIDDLE, BipedResource.class );
	
	public static final JointId NECK = new JointId( SPINE_UPPER, BipedResource.class );
	public static final JointId HEAD = new JointId( NECK, BipedResource.class );
	public static final JointId MOUTH = new JointId(HEAD, BipedResource.class);
	public static final JointId LEFT_EYE = new JointId(HEAD, BipedResource.class);
	public static final JointId RIGHT_EYE = new JointId(HEAD, BipedResource.class);
	
	public static final JointId RIGHT_CLAVICLE = new JointId( SPINE_UPPER, BipedResource.class );
	public static final JointId RIGHT_SHOULDER = new JointId( RIGHT_CLAVICLE, BipedResource.class );
	public static final JointId RIGHT_ELBOW = new JointId( RIGHT_SHOULDER, BipedResource.class );
	public static final JointId RIGHT_WRIST = new JointId( RIGHT_ELBOW, BipedResource.class );
	
	public static final JointId LEFT_CLAVICLE = new JointId( SPINE_UPPER, BipedResource.class );
	public static final JointId LEFT_SHOULDER = new JointId( LEFT_CLAVICLE, BipedResource.class );
	public static final JointId LEFT_ELBOW = new JointId( LEFT_SHOULDER, BipedResource.class );
	public static final JointId LEFT_WRIST = new JointId( LEFT_ELBOW, BipedResource.class );
	
	public static final JointId[] JOINT_ID_ROOTS = { ROOT };

	public org.lgna.story.implementation.BipedImp createImplementation( org.lgna.story.Biped abstraction );
}