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
	
public class SpaceColonist extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SpaceColonist() {
		super( "SciFi/space_colonist" );
	}
	public enum Part {
		RightLeg_RightShin_RightFoot( "rightLeg", "rightShin", "rightFoot" ),
		RightLeg_RightShin( "rightLeg", "rightShin" ),
		RightLeg( "rightLeg" ),
		LeftLeg_LeftShin_LeftFoot( "leftLeg", "leftShin", "leftFoot" ),
		LeftLeg_LeftShin( "leftLeg", "leftShin" ),
		LeftLeg( "leftLeg" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightThumb_RightThumbTip( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightThumb", "rightThumbTip" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightThumb( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightThumb" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightIndex_RightIndexMiddle_LeftIndexTip( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightIndex", "rightIndexMiddle", "leftIndexTip" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightIndex_RightIndexMiddle( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightIndex", "rightIndexMiddle" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightIndex( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightIndex" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightRing_RightRingMiddle_RightRingTip( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightRing", "rightRingMiddle", "rightRingTip" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightRing_RightRingMiddle( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightRing", "rightRingMiddle" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightRing( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightRing" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightPinky_RightPinkyMiddle_RightPinkyTip( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightPinky", "rightPinkyMiddle", "rightPinkyTip" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightPinky_RightPinkyMiddle( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightPinky", "rightPinkyMiddle" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightPinky( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightPinky" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightMiddle_RightMiddleMiddle_RightMiddleTip( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightMiddle", "rightMiddleMiddle", "rightMiddleTip" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightMiddle_RightMiddleMiddle( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightMiddle", "rightMiddleMiddle" ),
		SpaceMan_RightArm_RightForeArm_RightHand_RightMiddle( "spaceMan", "rightArm", "rightForeArm", "rightHand", "rightMiddle" ),
		SpaceMan_RightArm_RightForeArm_RightHand( "spaceMan", "rightArm", "rightForeArm", "rightHand" ),
		SpaceMan_RightArm_RightForeArm( "spaceMan", "rightArm", "rightForeArm" ),
		SpaceMan_RightArm( "spaceMan", "rightArm" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftIndex_LeftIndexMiddle_RightIndexTip( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftIndex", "leftIndexMiddle", "rightIndexTip" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftIndex_LeftIndexMiddle( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftIndex", "leftIndexMiddle" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftIndex( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftIndex" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftThumb_LeftThumbTip( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftThumb", "leftThumbTip" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftThumb( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftThumb" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftMiddle_LeftMiddleMiddle_LeftMiddleTip( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftMiddle", "leftMiddleMiddle", "leftMiddleTip" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftMiddle_LeftMiddleMiddle( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftMiddle", "leftMiddleMiddle" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftMiddle( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftMiddle" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftRing_LeftRingMiddle_LeftRingTip( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftRing", "leftRingMiddle", "leftRingTip" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftRing_LeftRingMiddle( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftRing", "leftRingMiddle" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftRing( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftRing" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftPinky_LeftPinkyMiddle_LeftPinkyTip( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftPinky", "leftPinkyMiddle", "leftPinkyTip" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftPinky_LeftPinkyMiddle( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftPinky", "leftPinkyMiddle" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand_LeftPinky( "spaceMan", "leftArm", "leftForeArm", "leftHand", "leftPinky" ),
		SpaceMan_LeftArm_LeftForeArm_LeftHand( "spaceMan", "leftArm", "leftForeArm", "leftHand" ),
		SpaceMan_LeftArm_LeftForeArm( "spaceMan", "leftArm", "leftForeArm" ),
		SpaceMan_LeftArm( "spaceMan", "leftArm" ),
		SpaceMan_HelmetRight( "spaceMan", "HelmetRight" ),
		SpaceMan_HelmetLeft( "spaceMan", "helmetLeft" ),
		SpaceMan( "spaceMan" );
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
