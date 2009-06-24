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
package org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp;
	
public class BlueBallerina extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public BlueBallerina() {
		super( "Textbook Demos/Wanda Temp/BlueBallerina" );
	}
	public enum Part {
		Torso_Neck_Head( "Torso", "neck", "Head" ),
		Torso_Neck( "Torso", "neck" ),
		Torso_LeftArm_LeftForarm( "Torso", "Left_arm", "Left_forarm" ),
		Torso_LeftArm( "Torso", "Left_arm" ),
		Torso_RightArm_RightForarm( "Torso", "Right_Arm", "Right_forarm" ),
		Torso_RightArm( "Torso", "Right_Arm" ),
		Torso( "Torso" ),
		RightThigh_RightKnee_RightLeg_RightFoot( "Right_thigh", "Right_knee", "Right_leg", "Right_Foot" ),
		RightThigh_RightKnee_RightLeg( "Right_thigh", "Right_knee", "Right_leg" ),
		RightThigh_RightKnee( "Right_thigh", "Right_knee" ),
		RightThigh( "Right_thigh" ),
		LeftThigh_LeftKnee_LeftLeg_LeftFoot( "Left_thigh", "Left_knee", "Left_leg", "Left_Foot" ),
		LeftThigh_LeftKnee_LeftLeg( "Left_thigh", "Left_knee", "Left_leg" ),
		LeftThigh_LeftKnee( "Left_thigh", "Left_knee" ),
		LeftThigh( "Left_thigh" );
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
