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
	
public class Magician extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Magician() {
		super( "People/Magician" );
	}
	public enum Part {
		LeftArm_LeftForearm_LeftHand_LeftPinky_LeftPinkyMiddleJoint_LeftPinkyTopJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Pinky", "Left_Pinky_middle_joint", "Left_Pinky_top_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftPinky_LeftPinkyMiddleJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Pinky", "Left_Pinky_middle_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftPinky( "LeftArm", "LeftForearm", "LeftHand", "Left_Pinky" ),
		LeftArm_LeftForearm_LeftHand_LeftRingFinger_LeftRingMiddleJoint_LeftRingTopJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Ring_Finger", "Left_Ring_middle_joint", "Left_Ring_top_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftRingFinger_LeftRingMiddleJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Ring_Finger", "Left_Ring_middle_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftRingFinger( "LeftArm", "LeftForearm", "LeftHand", "Left_Ring_Finger" ),
		LeftArm_LeftForearm_LeftHand_LeftMiddleFinger_LeftMiddleMiddleJoint_LeftMiddleTopJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Middle_Finger", "Left_Middle_middle_joint", "Left_Middle_top_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftMiddleFinger_LeftMiddleMiddleJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Middle_Finger", "Left_Middle_middle_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftMiddleFinger( "LeftArm", "LeftForearm", "LeftHand", "Left_Middle_Finger" ),
		LeftArm_LeftForearm_LeftHand_LeftPointerFinger_LeftPointerMiddleJoint_LeftPointerTopjoint( "LeftArm", "LeftForearm", "LeftHand", "Left_pointer_finger", "Left_Pointer_middle_joint", "Left_Pointer_topjoint" ),
		LeftArm_LeftForearm_LeftHand_LeftPointerFinger_LeftPointerMiddleJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_pointer_finger", "Left_Pointer_middle_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftPointerFinger( "LeftArm", "LeftForearm", "LeftHand", "Left_pointer_finger" ),
		LeftArm_LeftForearm_LeftHand_LeftThumb_LeftThumbTopJoint( "LeftArm", "LeftForearm", "LeftHand", "Left_Thumb", "Left_Thumb_top_joint" ),
		LeftArm_LeftForearm_LeftHand_LeftThumb( "LeftArm", "LeftForearm", "LeftHand", "Left_Thumb" ),
		LeftArm_LeftForearm_LeftHand( "LeftArm", "LeftForearm", "LeftHand" ),
		LeftArm_LeftForearm( "LeftArm", "LeftForearm" ),
		LeftArm( "LeftArm" ),
		Pelvis_LeftThigh_LeftCalf_LeftShoe( "Pelvis", "LeftThigh", "LeftCalf", "LeftShoe" ),
		Pelvis_LeftThigh_LeftCalf( "Pelvis", "LeftThigh", "LeftCalf" ),
		Pelvis_LeftThigh( "Pelvis", "LeftThigh" ),
		Pelvis_RightThigh_RightCalf_RightShoe( "Pelvis", "RightThigh", "RightCalf", "RightShoe" ),
		Pelvis_RightThigh_RightCalf( "Pelvis", "RightThigh", "RightCalf" ),
		Pelvis_RightThigh( "Pelvis", "RightThigh" ),
		Pelvis( "Pelvis" ),
		Head_Cap( "head", "Cap" ),
		Head_RighttopEyeLid( "head", "Righttop_eye_lid" ),
		Head_RightEyeBall( "head", "RightEyeBall" ),
		Head_RightbottomEyeLid( "head", "Rightbottom_eye_lid" ),
		Head_LefttopEyeLid( "head", "Lefttop_eye_lid" ),
		Head_LeftEyeBall( "head", "LeftEyeBall" ),
		Head_LeftbottomEyeLid( "head", "Leftbottom_eye_lid" ),
		Head( "head" ),
		BowTie( "bow_tie" ),
		RightArm_LeftForearm_Righthand_RightPinky_RightPinkyMiddleJoint_TopJoint( "RightArm", "LeftForearm", "Righthand", "Right_Pinky", "Right_pinky_middle_joint", "top_joint" ),
		RightArm_LeftForearm_Righthand_RightPinky_RightPinkyMiddleJoint( "RightArm", "LeftForearm", "Righthand", "Right_Pinky", "Right_pinky_middle_joint" ),
		RightArm_LeftForearm_Righthand_RightPinky( "RightArm", "LeftForearm", "Righthand", "Right_Pinky" ),
		RightArm_LeftForearm_Righthand_RightRingFinger_RightRingMiddleJoint_RightRingTopJoint( "RightArm", "LeftForearm", "Righthand", "Right_Ring_Finger", "Right_Ring_middle_joint", "Right_Ring_top_joint" ),
		RightArm_LeftForearm_Righthand_RightRingFinger_RightRingMiddleJoint( "RightArm", "LeftForearm", "Righthand", "Right_Ring_Finger", "Right_Ring_middle_joint" ),
		RightArm_LeftForearm_Righthand_RightRingFinger( "RightArm", "LeftForearm", "Righthand", "Right_Ring_Finger" ),
		RightArm_LeftForearm_Righthand_RightMiddleFinger_RightMiddleMiddleJoint_RightMiddleTopJoint( "RightArm", "LeftForearm", "Righthand", "Right_Middle_Finger", "Right_Middle_middle_joint", "Right_Middle_top_joint" ),
		RightArm_LeftForearm_Righthand_RightMiddleFinger_RightMiddleMiddleJoint( "RightArm", "LeftForearm", "Righthand", "Right_Middle_Finger", "Right_Middle_middle_joint" ),
		RightArm_LeftForearm_Righthand_RightMiddleFinger( "RightArm", "LeftForearm", "Righthand", "Right_Middle_Finger" ),
		RightArm_LeftForearm_Righthand_RightPointerFinger_RightPointerMiddleJoint_RightPointerTopjoint( "RightArm", "LeftForearm", "Righthand", "Right_pointer_finger", "Right_Pointer_middle_joint", "Right_Pointer_topjoint" ),
		RightArm_LeftForearm_Righthand_RightPointerFinger_RightPointerMiddleJoint( "RightArm", "LeftForearm", "Righthand", "Right_pointer_finger", "Right_Pointer_middle_joint" ),
		RightArm_LeftForearm_Righthand_RightPointerFinger( "RightArm", "LeftForearm", "Righthand", "Right_pointer_finger" ),
		RightArm_LeftForearm_Righthand_RightThumb_RightThumbTopJoint( "RightArm", "LeftForearm", "Righthand", "Right_Thumb", "Right_Thumb_top_joint" ),
		RightArm_LeftForearm_Righthand_RightThumb( "RightArm", "LeftForearm", "Righthand", "Right_Thumb" ),
		RightArm_LeftForearm_Righthand( "RightArm", "LeftForearm", "Righthand" ),
		RightArm_LeftForearm( "RightArm", "LeftForearm" ),
		RightArm( "RightArm" );
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
