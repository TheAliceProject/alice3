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
package org.alice.apis.moveandturn.gallery.scifi;
	
public class ScientistWoman extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public ScientistWoman() {
		super( "SciFi/Scientist_woman" );
	}
	public enum Part {
		RightLeg_RightShin_RightFoot( "rightLeg", "rightShin", "rightFoot" ),
		RightLeg_RightShin( "rightLeg", "rightShin" ),
		RightLeg( "rightLeg" ),
		LeftLeg_LeftShin_LeftFoot( "leftLeg", "leftShin", "leftFoot" ),
		LeftLeg_LeftShin( "leftLeg", "leftShin" ),
		LeftLeg( "leftLeg" ),
		Torso_RightArm_RightForeArm_RightHand_RightThumb_RightThumbTip( "torso", "rightArm", "rightForeArm", "rightHand", "rightThumb", "rightThumbTip" ),
		Torso_RightArm_RightForeArm_RightHand_RightThumb( "torso", "rightArm", "rightForeArm", "rightHand", "rightThumb" ),
		Torso_RightArm_RightForeArm_RightHand_RightIndex_RightIndexMiddle_LeftIndexTip( "torso", "rightArm", "rightForeArm", "rightHand", "rightIndex", "rightIndexMiddle", "leftIndexTip" ),
		Torso_RightArm_RightForeArm_RightHand_RightIndex_RightIndexMiddle( "torso", "rightArm", "rightForeArm", "rightHand", "rightIndex", "rightIndexMiddle" ),
		Torso_RightArm_RightForeArm_RightHand_RightIndex( "torso", "rightArm", "rightForeArm", "rightHand", "rightIndex" ),
		Torso_RightArm_RightForeArm_RightHand_RightRing_RightRingMiddle_RightRingTip( "torso", "rightArm", "rightForeArm", "rightHand", "rightRing", "rightRingMiddle", "rightRingTip" ),
		Torso_RightArm_RightForeArm_RightHand_RightRing_RightRingMiddle( "torso", "rightArm", "rightForeArm", "rightHand", "rightRing", "rightRingMiddle" ),
		Torso_RightArm_RightForeArm_RightHand_RightRing( "torso", "rightArm", "rightForeArm", "rightHand", "rightRing" ),
		Torso_RightArm_RightForeArm_RightHand_RightPinky_RightPinkyMiddle_RightPinkyTip( "torso", "rightArm", "rightForeArm", "rightHand", "rightPinky", "rightPinkyMiddle", "rightPinkyTip" ),
		Torso_RightArm_RightForeArm_RightHand_RightPinky_RightPinkyMiddle( "torso", "rightArm", "rightForeArm", "rightHand", "rightPinky", "rightPinkyMiddle" ),
		Torso_RightArm_RightForeArm_RightHand_RightPinky( "torso", "rightArm", "rightForeArm", "rightHand", "rightPinky" ),
		Torso_RightArm_RightForeArm_RightHand_RightMiddle_RightMiddleMiddle_RightMiddleTip( "torso", "rightArm", "rightForeArm", "rightHand", "rightMiddle", "rightMiddleMiddle", "rightMiddleTip" ),
		Torso_RightArm_RightForeArm_RightHand_RightMiddle_RightMiddleMiddle( "torso", "rightArm", "rightForeArm", "rightHand", "rightMiddle", "rightMiddleMiddle" ),
		Torso_RightArm_RightForeArm_RightHand_RightMiddle( "torso", "rightArm", "rightForeArm", "rightHand", "rightMiddle" ),
		Torso_RightArm_RightForeArm_RightHand( "torso", "rightArm", "rightForeArm", "rightHand" ),
		Torso_RightArm_RightForeArm( "torso", "rightArm", "rightForeArm" ),
		Torso_RightArm( "torso", "rightArm" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftIndex_LeftIndexMiddle_RightIndexTip( "torso", "leftArm", "leftForeArm", "leftHand", "leftIndex", "leftIndexMiddle", "rightIndexTip" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftIndex_LeftIndexMiddle( "torso", "leftArm", "leftForeArm", "leftHand", "leftIndex", "leftIndexMiddle" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftIndex( "torso", "leftArm", "leftForeArm", "leftHand", "leftIndex" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftThumb_LeftThumbTip( "torso", "leftArm", "leftForeArm", "leftHand", "leftThumb", "leftThumbTip" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftThumb( "torso", "leftArm", "leftForeArm", "leftHand", "leftThumb" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftMiddle_LeftMiddleMiddle_LeftMiddleTip( "torso", "leftArm", "leftForeArm", "leftHand", "leftMiddle", "leftMiddleMiddle", "leftMiddleTip" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftMiddle_LeftMiddleMiddle( "torso", "leftArm", "leftForeArm", "leftHand", "leftMiddle", "leftMiddleMiddle" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftMiddle( "torso", "leftArm", "leftForeArm", "leftHand", "leftMiddle" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftRing_LeftRingMiddle_LeftRingTip( "torso", "leftArm", "leftForeArm", "leftHand", "leftRing", "leftRingMiddle", "leftRingTip" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftRing_LeftRingMiddle( "torso", "leftArm", "leftForeArm", "leftHand", "leftRing", "leftRingMiddle" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftRing( "torso", "leftArm", "leftForeArm", "leftHand", "leftRing" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftPinky_LeftPinkyMiddle_LeftPinkyTip( "torso", "leftArm", "leftForeArm", "leftHand", "leftPinky", "leftPinkyMiddle", "leftPinkyTip" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftPinky_LeftPinkyMiddle( "torso", "leftArm", "leftForeArm", "leftHand", "leftPinky", "leftPinkyMiddle" ),
		Torso_LeftArm_LeftForeArm_LeftHand_LeftPinky( "torso", "leftArm", "leftForeArm", "leftHand", "leftPinky" ),
		Torso_LeftArm_LeftForeArm_LeftHand( "torso", "leftArm", "leftForeArm", "leftHand" ),
		Torso_LeftArm_LeftForeArm( "torso", "leftArm", "leftForeArm" ),
		Torso_LeftArm( "torso", "leftArm" ),
		Torso_HelmetRight( "torso", "HelmetRight" ),
		Torso_HelmetLeft( "torso", "helmetLeft" ),
		Torso_Head_Hair( "torso", "head", "hair" ),
		Torso_Head( "torso", "head" ),
		Torso( "torso" );
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
