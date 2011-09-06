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
	public static class BipedJointId extends JointId {
		public static final BipedJointId PELVIS_LOWER_BODY = new BipedJointId( null );
		
		public static final BipedJointId LEFT_HIP = new BipedJointId( PELVIS_LOWER_BODY );
		public static final BipedJointId LEFT_KNEE = new BipedJointId( LEFT_HIP );
		public static final BipedJointId LEFT_ANKLE = new BipedJointId( LEFT_KNEE );
		
		public static final BipedJointId RIGHT_HIP = new BipedJointId( PELVIS_LOWER_BODY );
		public static final BipedJointId RIGHT_KNEE = new BipedJointId( RIGHT_HIP );
		public static final BipedJointId RIGHT_ANKLE = new BipedJointId( RIGHT_KNEE );
		
		public static final BipedJointId PELVIS_UPPER_BODY = new BipedJointId( null );
		
		public static final BipedJointId SPINE_MIDDLE = new BipedJointId( PELVIS_UPPER_BODY );
		public static final BipedJointId SPINE_UPPER = new BipedJointId( SPINE_MIDDLE );
		
		public static final BipedJointId NECK = new BipedJointId( SPINE_UPPER );
		public static final BipedJointId HEAD = new BipedJointId( NECK );
		public static final BipedJointId MOUTH = new BipedJointId(HEAD);
		public static final BipedJointId LEFT_EYE = new BipedJointId(HEAD);
		public static final BipedJointId RIGHT_EYE = new BipedJointId(HEAD);
		
		public static final BipedJointId RIGHT_CLAVICLE = new BipedJointId( SPINE_UPPER );
		public static final BipedJointId RIGHT_SHOULDER = new BipedJointId( RIGHT_CLAVICLE );
		public static final BipedJointId RIGHT_ELBOW = new BipedJointId( RIGHT_SHOULDER );
		public static final BipedJointId RIGHT_WRIST = new BipedJointId( RIGHT_ELBOW );
		
		public static final BipedJointId LEFT_CLAVICLE = new BipedJointId( SPINE_UPPER );
		public static final BipedJointId LEFT_SHOULDER = new BipedJointId( LEFT_CLAVICLE );
		public static final BipedJointId LEFT_ELBOW = new BipedJointId( LEFT_SHOULDER );
		public static final BipedJointId LEFT_WRIST = new BipedJointId( LEFT_ELBOW );
		
		private static BipedJointId[] roots = { PELVIS_LOWER_BODY, PELVIS_UPPER_BODY };

		protected BipedJointId( JointId parent ) {
			super(parent);
		}
		public static BipedJointId[] getRoots() {
			return roots;
		}
	};
	
	public static class OgreJointId extends BipedJointId {
		public static final OgreJointId LEFT_HORN = new OgreJointId( BipedJointId.HEAD );
		public static final OgreJointId RIGHT_HORN = new OgreJointId( BipedJointId.HEAD );
		
		public static final OgreJointId TAIL_A = new OgreJointId( BipedJointId.PELVIS_LOWER_BODY );
		public static final OgreJointId AIL_B = new OgreJointId( TAIL_A );
		
		protected OgreJointId( JointId parent ) {
			super(parent);
		}
	};

	public org.lgna.story.implementation.BipedImplementation createImplementation( org.lgna.story.Biped abstraction );
}