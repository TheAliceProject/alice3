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
	
public class EvilStepSister2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public EvilStepSister2() {
		super( "People/EvilStepSister2" );
	}
	public enum Part {
		Stomach_Torso_Face_Hair( "Stomach", "Torso", "Face", "Hair" ),
		Stomach_Torso_Face_RightEar( "Stomach", "Torso", "Face", "RightEar" ),
		Stomach_Torso_Face_LeftEar( "Stomach", "Torso", "Face", "LeftEar" ),
		Stomach_Torso_Face_Nose( "Stomach", "Torso", "Face", "Nose" ),
		Stomach_Torso_Face( "Stomach", "Torso", "Face" ),
		Stomach_Torso_RightUpperArm_RightLowerArm_RightHand( "Stomach", "Torso", "RightUpperArm", "RightLowerArm", "RightHand" ),
		Stomach_Torso_RightUpperArm_RightLowerArm( "Stomach", "Torso", "RightUpperArm", "RightLowerArm" ),
		Stomach_Torso_RightUpperArm( "Stomach", "Torso", "RightUpperArm" ),
		Stomach_Torso_LeftUpperArm_LeftLowerArm_LeftHand( "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm", "LeftHand" ),
		Stomach_Torso_LeftUpperArm_LeftLowerArm( "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm" ),
		Stomach_Torso_LeftUpperArm( "Stomach", "Torso", "LeftUpperArm" ),
		Stomach_Torso( "Stomach", "Torso" ),
		Stomach_Skirt( "Stomach", "Skirt" ),
		Stomach_RightThigh_RightCalf_RightFoot( "Stomach", "RightThigh", "RightCalf", "RightFoot" ),
		Stomach_RightThigh_RightCalf( "Stomach", "RightThigh", "RightCalf" ),
		Stomach_RightThigh( "Stomach", "RightThigh" ),
		Stomach_LeftThigh_LeftCalf_LeftFoot( "Stomach", "LeftThigh", "LeftCalf", "LeftFoot" ),
		Stomach_LeftThigh_LeftCalf( "Stomach", "LeftThigh", "LeftCalf" ),
		Stomach_LeftThigh( "Stomach", "LeftThigh" ),
		Stomach( "Stomach" );
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
