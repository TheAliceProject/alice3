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
package org.alice.apis.moveandturn.gallery.people;
	
public class Bob extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Bob() {
		super( "People/Bob" );
	}
	public enum Part {
		UpperBody_LeftUpperArm_LeftLowerarm_LeftHand( "upper_body", "left_upper_arm", "left_lowerarm", "left_hand" ),
		UpperBody_LeftUpperArm_LeftLowerarm( "upper_body", "left_upper_arm", "left_lowerarm" ),
		UpperBody_LeftUpperArm( "upper_body", "left_upper_arm" ),
		UpperBody_RightUpperarm_RightLowerarm_RightHand_FingerPress( "upper_body", "right_upperarm", "right_lowerarm", "right_hand", "Finger_press" ),
		UpperBody_RightUpperarm_RightLowerarm_RightHand( "upper_body", "right_upperarm", "right_lowerarm", "right_hand" ),
		UpperBody_RightUpperarm_RightLowerarm( "upper_body", "right_upperarm", "right_lowerarm" ),
		UpperBody_RightUpperarm( "upper_body", "right_upperarm" ),
		UpperBody_Head_LeftEar( "upper_body", "head", "left_ear" ),
		UpperBody_Head_RightEar( "upper_body", "head", "right_ear" ),
		UpperBody_Head_HairLeft( "upper_body", "head", "hair_left" ),
		UpperBody_Head_HairRight( "upper_body", "head", "hair_right" ),
		UpperBody_Head( "upper_body", "head" ),
		UpperBody( "upper_body" ),
		LeftThigh_LeftCalf_LeftFoot( "left_thigh", "left_calf", "left_foot" ),
		LeftThigh_LeftCalf( "left_thigh", "left_calf" ),
		LeftThigh( "left_thigh" ),
		RightThigh_RightCalf_RightFoot( "right_thigh", "right_calf", "right_foot" ),
		RightThigh_RightCalf( "right_thigh", "right_calf" ),
		RightThigh( "right_thigh" );
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
