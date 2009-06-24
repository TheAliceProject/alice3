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
	
public class PinkBallerina extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public PinkBallerina() {
		super( "Textbook Demos/Wanda Temp/PinkBallerina" );
	}
	public enum Part {
		Torso_Neck_Head( "Torso", "neck", "Head" ),
		Torso_Neck( "Torso", "neck" ),
		Torso_LeftArm_LeftForarm( "Torso", "Left_arm", "Left_forarm" ),
		Torso_LeftArm( "Torso", "Left_arm" ),
		Torso_RightArm_RightForarm( "Torso", "Right_arm", "Right_forarm" ),
		Torso_RightArm( "Torso", "Right_arm" ),
		Torso( "Torso" ),
		LeftThigh_LeftKnee_LeftCalf_LeftFoot( "Left_thigh", "Left_knee", "Left_Calf", "Left_foot" ),
		LeftThigh_LeftKnee_LeftCalf( "Left_thigh", "Left_knee", "Left_Calf" ),
		LeftThigh_LeftKnee( "Left_thigh", "Left_knee" ),
		LeftThigh( "Left_thigh" ),
		RightThigh_Knee01_RightKnee_RightFoot( "Right_thigh", "knee01", "Right_Knee", "Right_Foot" ),
		RightThigh_Knee01_RightKnee( "Right_thigh", "knee01", "Right_Knee" ),
		RightThigh_Knee01( "Right_thigh", "knee01" ),
		RightThigh( "Right_thigh" );
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
