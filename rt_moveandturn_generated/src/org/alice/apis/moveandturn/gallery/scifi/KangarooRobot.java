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
	
public class KangarooRobot extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public KangarooRobot() {
		super( "SciFi/kangaroo_robot" );
	}
	public enum Part {
		Elevator_Chest_Neck_Head_LeftEar( "Elevator", "Chest", "Neck", "Head", "LeftEar" ),
		Elevator_Chest_Neck_Head_RightEar( "Elevator", "Chest", "Neck", "Head", "RightEar" ),
		Elevator_Chest_Neck_Head_Cigarette( "Elevator", "Chest", "Neck", "Head", "Cigarette" ),
		Elevator_Chest_Neck_Head( "Elevator", "Chest", "Neck", "Head" ),
		Elevator_Chest_Neck( "Elevator", "Chest", "Neck" ),
		Elevator_Chest_LeftArm_LeftForeArm_LeftHand_Cup( "Elevator", "Chest", "LeftArm", "LeftForeArm", "LeftHand", "Cup" ),
		Elevator_Chest_LeftArm_LeftForeArm_LeftHand( "Elevator", "Chest", "LeftArm", "LeftForeArm", "LeftHand" ),
		Elevator_Chest_LeftArm_LeftForeArm( "Elevator", "Chest", "LeftArm", "LeftForeArm" ),
		Elevator_Chest_LeftArm( "Elevator", "Chest", "LeftArm" ),
		Elevator_Chest_RightArm_RightForeArm_RightHand( "Elevator", "Chest", "RightArm", "RightForeArm", "RightHand" ),
		Elevator_Chest_RightArm_RightForeArm( "Elevator", "Chest", "RightArm", "RightForeArm" ),
		Elevator_Chest_RightArm( "Elevator", "Chest", "RightArm" ),
		Elevator_Chest( "Elevator", "Chest" ),
		Elevator_RightPiston( "Elevator", "RightPiston" ),
		Elevator_LeftPiston( "Elevator", "LeftPiston" ),
		Elevator( "Elevator" ),
		LeftStrut_LeftTread( "LeftStrut", "LeftTread" ),
		LeftStrut( "LeftStrut" ),
		RightStrut_RightTread( "RightStrut", "RightTread" ),
		RightStrut( "RightStrut" );
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
