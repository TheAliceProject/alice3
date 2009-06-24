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
