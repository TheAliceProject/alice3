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
package org.alice.apis.moveandturn.gallery.spooky;
	
public class Skeleton extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Skeleton() {
		super( "Spooky/Skeleton" );
	}
	public enum Part {
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LRingFinger1_LRingFinger2( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LRingFinger1", "LRingFinger2" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LRingFinger1( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LRingFinger1" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LMiddleFinger1_LMiddleFinger2( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LMiddleFinger1", "LMiddleFinger2" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LMiddleFinger1( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LMiddleFinger1" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LPointerFinger1_LPointerFinger2( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LPointerFinger1", "LPointerFinger2" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LPointerFinger1( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LPointerFinger1" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LeftThumb1_LeftThumb2( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LeftThumb1", "LeftThumb2" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LeftThumb1( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LeftThumb1" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LPinky1_LPinky2( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LPinky1", "LPinky2" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand_LPinky1( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand", "LPinky1" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm_LeftHand( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm", "LeftHand" ),
		Spine2_Ribcage_LeftHumerus_LeftUpperArm( "Spine2", "Ribcage", "LeftHumerus", "LeftUpperArm" ),
		Spine2_Ribcage_LeftHumerus( "Spine2", "Ribcage", "LeftHumerus" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RPinky1_RPinky2( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RPinky1", "RPinky2" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RPinky1( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RPinky1" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RRingFinger1_RRingFinger2( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RRingFinger1", "RRingFinger2" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RRingFinger1( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RRingFinger1" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RMiddleFinger1_RMiddleFInger2( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RMiddleFinger1", "RMiddleFInger2" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RMiddleFinger1( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RMiddleFinger1" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RPointerFinger1_RPointerFinger2( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RPointerFinger1", "RPointerFinger2" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RPointerFinger1( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RPointerFinger1" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RightThumb1_RightThumb2( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RightThumb1", "RightThumb2" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand_RightThumb1( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand", "RightThumb1" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm_RightHand( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm", "RightHand" ),
		Spine2_Ribcage_RightHumerus_RightUpperArm( "Spine2", "Ribcage", "RightHumerus", "RightUpperArm" ),
		Spine2_Ribcage_RightHumerus( "Spine2", "Ribcage", "RightHumerus" ),
		Spine2_Ribcage( "Spine2", "Ribcage" ),
		Spine2_Spine3_Head_Jaw_LowerTeeth( "Spine2", "Spine3", "Head", "Jaw", "LowerTeeth" ),
		Spine2_Spine3_Head_Jaw( "Spine2", "Spine3", "Head", "Jaw" ),
		Spine2_Spine3_Head_UpperTeeth( "Spine2", "Spine3", "Head", "UpperTeeth" ),
		Spine2_Spine3_Head( "Spine2", "Spine3", "Head" ),
		Spine2_Spine3( "Spine2", "Spine3" ),
		Spine2( "Spine2" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LBigToe1_LBigToe2( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LBigToe1", "LBigToe2" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LBigToe1( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LBigToe1" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeB1_LToeB2( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeB1", "LToeB2" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeB1( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeB1" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeC1_LToeC2( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeC1", "LToeC2" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeC1( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeC1" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeD1_LToeD2( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeD1", "LToeD2" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeD1( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeD1" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeE1_LToeE2( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeE1", "LToeE2" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot_LToeE1( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot", "LToeE1" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg_LeftFoot( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg", "LeftFoot" ),
		LeftPelvicGirdle_LeftFemur_LeftLowerLeg( "LeftPelvicGirdle", "LeftFemur", "LeftLowerLeg" ),
		LeftPelvicGirdle_LeftFemur( "LeftPelvicGirdle", "LeftFemur" ),
		LeftPelvicGirdle( "LeftPelvicGirdle" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeE1_RToeE2( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeE1", "RToeE2" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeE1( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeE1" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeD1_RToeD2( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeD1", "RToeD2" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeD1( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeD1" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeC1_RToeC2( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeC1", "RToeC2" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeC1( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeC1" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeB1_RToeB2( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeB1", "RToeB2" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RToeB1( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RToeB1" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RBigToe1_RBigToe2( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RBigToe1", "RBigToe2" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot_RBigToe1( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot", "RBigToe1" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg_RightFoot( "RightPelvicGirdle", "RightFemur", "RightLowerLeg", "RightFoot" ),
		RightPelvicGirdle_RightFemur_RightLowerLeg( "RightPelvicGirdle", "RightFemur", "RightLowerLeg" ),
		RightPelvicGirdle_RightFemur( "RightPelvicGirdle", "RightFemur" ),
		RightPelvicGirdle( "RightPelvicGirdle" );
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
