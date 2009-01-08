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
	
public class BadGuyRobot extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public BadGuyRobot() {
		super( "SciFi/badGuyRobot" );
	}
	public enum Part {
		RightArm_Cannon( "rightArm", "Cannon" ),
		RightArm( "rightArm" ),
		LeftArm_Connection1( "leftArm", "connection1" ),
		LeftArm_Connection2( "leftArm", "connection2" ),
		LeftArm_Hinge1( "leftArm", "hinge1" ),
		LeftArm_Hinge2( "leftArm", "hinge2" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger5_Finger5b( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger5", "finger5b" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger5( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger5" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger4_Finger4b( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger4", "finger4b" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger4( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger4" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger3_Finger3b( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger3", "finger3b" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger3( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger3" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger2_Finger2b( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger2", "finger2b" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Finger2( "leftArm", "armGear", "armSection", "leftForearm", "hand", "finger2" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Thumb_Thumb1b( "leftArm", "armGear", "armSection", "leftForearm", "hand", "thumb", "thumb1b" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand_Thumb( "leftArm", "armGear", "armSection", "leftForearm", "hand", "thumb" ),
		LeftArm_ArmGear_ArmSection_LeftForearm_Hand( "leftArm", "armGear", "armSection", "leftForearm", "hand" ),
		LeftArm_ArmGear_ArmSection_LeftForearm( "leftArm", "armGear", "armSection", "leftForearm" ),
		LeftArm_ArmGear_ArmSection( "leftArm", "armGear", "armSection" ),
		LeftArm_ArmGear( "leftArm", "armGear" ),
		LeftArm( "leftArm" ),
		MidSectionRod1( "midSectionRod1" ),
		MidSectionRod2( "midSectionRod2" ),
		MidSectionRod3( "midSectionRod3" ),
		MidSectionRod4( "midSectionRod4" ),
		MidSectionRod5( "midSectionRod5" ),
		EngergyCore( "engergyCore" ),
		MidSectionRod6( "midSectionRod6" ),
		MidSectionRod7( "midSectionRod7" ),
		MidSectionRod8( "midSectionRod8" ),
		Legs_LeftLeg_Boot_Spike4( "legs", "leftLeg", "Boot", "spike4" ),
		Legs_LeftLeg_Boot_Spike5( "legs", "leftLeg", "Boot", "spike5" ),
		Legs_LeftLeg_Boot( "legs", "leftLeg", "Boot" ),
		Legs_LeftLeg_Spike1( "legs", "leftLeg", "spike1" ),
		Legs_LeftLeg_Spike2( "legs", "leftLeg", "spike2" ),
		Legs_LeftLeg_Spike3( "legs", "leftLeg", "spike3" ),
		Legs_LeftLeg( "legs", "leftLeg" ),
		Legs_RightLeg_WhiteHelix( "legs", "rightLeg", "whiteHelix" ),
		Legs_RightLeg_GreyHelix( "legs", "rightLeg", "greyHelix" ),
		Legs_RightLeg_RightFoot_Extrusion1( "legs", "rightLeg", "rightFoot", "extrusion1" ),
		Legs_RightLeg_RightFoot_Extrusion2( "legs", "rightLeg", "rightFoot", "extrusion2" ),
		Legs_RightLeg_RightFoot_Extrusion3( "legs", "rightLeg", "rightFoot", "extrusion3" ),
		Legs_RightLeg_RightFoot_Extrusion4( "legs", "rightLeg", "rightFoot", "extrusion4" ),
		Legs_RightLeg_RightFoot( "legs", "rightLeg", "rightFoot" ),
		Legs_RightLeg( "legs", "rightLeg" ),
		Legs( "legs" ),
		Head( "Head" ),
		PadR_Spike( "padR", "spike" ),
		PadR( "padR" ),
		PadL_Spike( "padL", "spike" ),
		PadL( "padL" );
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
