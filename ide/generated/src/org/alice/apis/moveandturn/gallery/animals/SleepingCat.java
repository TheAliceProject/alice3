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
package org.alice.apis.moveandturn.gallery.animals;
	
public class SleepingCat extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SleepingCat() {
		super( "Animals/sleeping_cat" );
	}
	public enum Part {
		Body_Head_LeftEar( "Body", "Head", "left Ear" ),
		Body_Head_RightEye( "Body", "Head", "right Eye" ),
		Body_Head_Mouth( "Body", "Head", "Mouth" ),
		Body_Head_RightEye01( "Body", "Head", "right Eye01" ),
		Body_Head_RightEar( "Body", "Head", "right Ear" ),
		Body_Head( "Body", "Head" ),
		Body_LeftArm_LeftElbow_LeftHand( "Body", "left Arm", "left Elbow", "left Hand" ),
		Body_LeftArm_LeftElbow( "Body", "left Arm", "left Elbow" ),
		Body_LeftArm( "Body", "left Arm" ),
		Body_RightArm_RightElbow_RightHand( "Body", "right Arm", "right Elbow", "right Hand" ),
		Body_RightArm_RightElbow( "Body", "right Arm", "right Elbow" ),
		Body_RightArm( "Body", "right Arm" ),
		Body_LeftThigh_LeftLeg_LeftFoot( "Body", "left Thigh", "left Leg", "left Foot" ),
		Body_LeftThigh_LeftLeg( "Body", "left Thigh", "left Leg" ),
		Body_LeftThigh( "Body", "left Thigh" ),
		Body_Tail1_Tail2_Tail3_Tail4( "Body", "Tail1", "Tail2", "Tail3", "Tail4" ),
		Body_Tail1_Tail2_Tail3( "Body", "Tail1", "Tail2", "Tail3" ),
		Body_Tail1_Tail2( "Body", "Tail1", "Tail2" ),
		Body_Tail1( "Body", "Tail1" ),
		Body_RightThigh_RightLeg_RightFoot( "Body", "right Thigh", "right Leg", "right foot" ),
		Body_RightThigh_RightLeg( "Body", "right Thigh", "right Leg" ),
		Body_RightThigh( "Body", "right Thigh" ),
		Body( "Body" ),
		Bed( "bed" ),
		BedPillow( "BedPillow" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public org.alice.apis.moveandturn.Model getPart( Part part ) {
		return getDescendant( org.alice.apis.moveandturn.Model.class, part.getPath() );
	}

}
