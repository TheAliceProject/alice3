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
package org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp;
	
public class Rockette extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Rockette() {
		super( "Textbook Demos/Wanda Temp/rockette" );
	}
	public enum Part {
		Hips_Stomach_Torso_Collar_Head_HatRim_ElfHat_HatBall( "Hips", "Stomach", "Torso", "Collar", "Head", "HatRim", "ElfHat", "HatBall" ),
		Hips_Stomach_Torso_Collar_Head_HatRim_ElfHat( "Hips", "Stomach", "Torso", "Collar", "Head", "HatRim", "ElfHat" ),
		Hips_Stomach_Torso_Collar_Head_HatRim( "Hips", "Stomach", "Torso", "Collar", "Head", "HatRim" ),
		Hips_Stomach_Torso_Collar_Head_RightEar( "Hips", "Stomach", "Torso", "Collar", "Head", "RightEar" ),
		Hips_Stomach_Torso_Collar_Head_LeftEar( "Hips", "Stomach", "Torso", "Collar", "Head", "LeftEar" ),
		Hips_Stomach_Torso_Collar_Head( "Hips", "Stomach", "Torso", "Collar", "Head" ),
		Hips_Stomach_Torso_Collar( "Hips", "Stomach", "Torso", "Collar" ),
		Hips_Stomach_Torso_LeftUpperArm_LeftLowerArm_LeftCuff_LeftHand_LeftThumb( "Hips", "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm", "LeftCuff", "LeftHand", "LeftThumb" ),
		Hips_Stomach_Torso_LeftUpperArm_LeftLowerArm_LeftCuff_LeftHand( "Hips", "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm", "LeftCuff", "LeftHand" ),
		Hips_Stomach_Torso_LeftUpperArm_LeftLowerArm_LeftCuff( "Hips", "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm", "LeftCuff" ),
		Hips_Stomach_Torso_LeftUpperArm_LeftLowerArm( "Hips", "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm" ),
		Hips_Stomach_Torso_LeftUpperArm( "Hips", "Stomach", "Torso", "LeftUpperArm" ),
		Hips_Stomach_Torso_RightUpperArm_RightLowerArm_RightCuff_RightHand_RightThumb( "Hips", "Stomach", "Torso", "RightUpperArm", "RightLowerArm", "RightCuff", "RightHand", "RightThumb" ),
		Hips_Stomach_Torso_RightUpperArm_RightLowerArm_RightCuff_RightHand( "Hips", "Stomach", "Torso", "RightUpperArm", "RightLowerArm", "RightCuff", "RightHand" ),
		Hips_Stomach_Torso_RightUpperArm_RightLowerArm_RightCuff( "Hips", "Stomach", "Torso", "RightUpperArm", "RightLowerArm", "RightCuff" ),
		Hips_Stomach_Torso_RightUpperArm_RightLowerArm( "Hips", "Stomach", "Torso", "RightUpperArm", "RightLowerArm" ),
		Hips_Stomach_Torso_RightUpperArm( "Hips", "Stomach", "Torso", "RightUpperArm" ),
		Hips_Stomach_Torso( "Hips", "Stomach", "Torso" ),
		Hips_Stomach( "Hips", "Stomach" ),
		Hips_Skirt( "Hips", "Skirt" ),
		Hips_RightThigh_RightCalf_RightFoot_RightShoe_RightHeel( "Hips", "RightThigh", "RightCalf", "RightFoot", "RightShoe", "RightHeel" ),
		Hips_RightThigh_RightCalf_RightFoot_RightShoe( "Hips", "RightThigh", "RightCalf", "RightFoot", "RightShoe" ),
		Hips_RightThigh_RightCalf_RightFoot( "Hips", "RightThigh", "RightCalf", "RightFoot" ),
		Hips_RightThigh_RightCalf( "Hips", "RightThigh", "RightCalf" ),
		Hips_RightThigh( "Hips", "RightThigh" ),
		Hips_LeftThigh_LeftCalf_LeftFoot_LeftShoe_LeftHeel( "Hips", "LeftThigh", "LeftCalf", "LeftFoot", "LeftShoe", "LeftHeel" ),
		Hips_LeftThigh_LeftCalf_LeftFoot_LeftShoe( "Hips", "LeftThigh", "LeftCalf", "LeftFoot", "LeftShoe" ),
		Hips_LeftThigh_LeftCalf_LeftFoot( "Hips", "LeftThigh", "LeftCalf", "LeftFoot" ),
		Hips_LeftThigh_LeftCalf( "Hips", "LeftThigh", "LeftCalf" ),
		Hips_LeftThigh( "Hips", "LeftThigh" ),
		Hips( "Hips" );
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
