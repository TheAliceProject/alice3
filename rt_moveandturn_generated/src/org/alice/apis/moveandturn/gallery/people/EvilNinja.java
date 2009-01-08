/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
