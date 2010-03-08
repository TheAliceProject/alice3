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
	
public class EvilNinja extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public EvilNinja() {
		super( "People/evilNinja" );
	}
	public enum Part {
		Torso_LeftArm_LeftForeArm_LeftHand_LeftHandFgers_LeftHndMids_LeftHndTips( "torso", "leftArm", "leftForeArm", "leftHand", "leftHand_fgers", "leftHnd_mids", "leftHnd_tips" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftHandFgers_LeftHndMids( "torso", "leftArm", "leftForeArm", "leftHand", "leftHand_fgers", "leftHnd_mids" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftHandFgers( "torso", "leftArm", "leftForeArm", "leftHand", "leftHand_fgers" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftHndTmb_LeftHndTbTip( "torso", "leftArm", "leftForeArm", "leftHand", "leftHnd_tmb", "leftHnd_tbTip" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftHndTmb( "torso", "leftArm", "leftForeArm", "leftHand", "leftHnd_tmb" ),
		Torso_LeftArm_LeftForeArm_LeftHand( "torso", "leftArm", "leftForeArm", "leftHand" ),
		Torso_LeftArm_LeftForeArm( "torso", "leftArm", "leftForeArm" ),
		Torso_LeftArm( "torso", "leftArm" ),
		Torso_Head_RightEye( "torso", "head", "rightEye" ),
		Torso_Head_LeftEye( "torso", "head", "leftEye" ),
		Torso_Head( "torso", "head" ),
		Torso_RightArm_RightForeArm_RightHand_RightHandFgers_RightHndMids_RightHndTips( "torso", "rightArm", "rightForeArm", "rightHand", "rightHand_fgers", "rightHnd_mids", "rightHnd_tips" ),
		Torso_RightArm_RightForeArm_RightHand_RightHandFgers_RightHndMids( "torso", "rightArm", "rightForeArm", "rightHand", "rightHand_fgers", "rightHnd_mids" ),
		Torso_RightArm_RightForeArm_RightHand_RightHandFgers( "torso", "rightArm", "rightForeArm", "rightHand", "rightHand_fgers" ),
		Torso_RightArm_RightForeArm_RightHand_RightHndTmb_RightHndTbTip( "torso", "rightArm", "rightForeArm", "rightHand", "rightHnd_tmb", "rightHnd_tbTip" ),
		Torso_RightArm_RightForeArm_RightHand_RightHndTmb( "torso", "rightArm", "rightForeArm", "rightHand", "rightHnd_tmb" ),
		Torso_RightArm_RightForeArm_RightHand( "torso", "rightArm", "rightForeArm", "rightHand" ),
		Torso_RightArm_RightForeArm( "torso", "rightArm", "rightForeArm" ),
		Torso_RightArm( "torso", "rightArm" ),
		Torso( "torso" ),
		LeftLeg_LeftShin_LeftFoot( "leftLeg", "leftShin", "leftFoot" ),
		LeftLeg_LeftShin( "leftLeg", "leftShin" ),
		LeftLeg( "leftLeg" ),
		RightLeg_RightShin_RightFoot( "rightLeg", "rightShin", "rightFoot" ),
		RightLeg_RightShin( "rightLeg", "rightShin" ),
		RightLeg( "rightLeg" );
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
