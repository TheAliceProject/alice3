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
package org.alice.apis.moveandturn.gallery.space;
	
public class Astronaut extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Astronaut() {
		super( "Space/astronaut" );
	}
	public enum Part {
		LeftUpperArm_LeftLowerArm_LeftHand_LeftThumb( "LeftUpperArm", "LeftLowerArm", "LeftHand", "LeftThumb" ),
		LeftUpperArm_LeftLowerArm_LeftHand_LeftIndexFinger( "LeftUpperArm", "LeftLowerArm", "LeftHand", "LeftIndexFinger" ),
		LeftUpperArm_LeftLowerArm_LeftHand_LeftMiddleFinger( "LeftUpperArm", "LeftLowerArm", "LeftHand", "LeftMiddleFinger" ),
		LeftUpperArm_LeftLowerArm_LeftHand_LeftRingFinger( "LeftUpperArm", "LeftLowerArm", "LeftHand", "LeftRingFinger" ),
		LeftUpperArm_LeftLowerArm_LeftHand_LeftPinky( "LeftUpperArm", "LeftLowerArm", "LeftHand", "LeftPinky" ),
		LeftUpperArm_LeftLowerArm_LeftHand( "LeftUpperArm", "LeftLowerArm", "LeftHand" ),
		LeftUpperArm_LeftLowerArm( "LeftUpperArm", "LeftLowerArm" ),
		LeftUpperArm( "LeftUpperArm" ),
		RightUpperArm_RightLowerArm_RighHand_RightThumb( "RightUpperArm", "RightLowerArm", "RighHand", "RightThumb" ),
		RightUpperArm_RightLowerArm_RighHand_RightIndexFinger( "RightUpperArm", "RightLowerArm", "RighHand", "RightIndexFinger" ),
		RightUpperArm_RightLowerArm_RighHand_RightMiddleFinger( "RightUpperArm", "RightLowerArm", "RighHand", "RightMiddleFinger" ),
		RightUpperArm_RightLowerArm_RighHand_RightRingFinger( "RightUpperArm", "RightLowerArm", "RighHand", "RightRingFinger" ),
		RightUpperArm_RightLowerArm_RighHand_RightPinky( "RightUpperArm", "RightLowerArm", "RighHand", "RightPinky" ),
		RightUpperArm_RightLowerArm_RighHand( "RightUpperArm", "RightLowerArm", "RighHand" ),
		RightUpperArm_RightLowerArm( "RightUpperArm", "RightLowerArm" ),
		RightUpperArm( "RightUpperArm" ),
		RightUpperLeg_RightLowerLeg_RightFoot( "RightUpperLeg", "RightLowerLeg", "RightFoot" ),
		RightUpperLeg_RightLowerLeg( "RightUpperLeg", "RightLowerLeg" ),
		RightUpperLeg( "RightUpperLeg" ),
		LeftUpperLeg_LeftLowerLeg_LeftFoot( "LeftUpperLeg", "LeftLowerLeg", "LeftFoot" ),
		LeftUpperLeg_LeftLowerLeg( "LeftUpperLeg", "LeftLowerLeg" ),
		LeftUpperLeg( "LeftUpperLeg" ),
		BackPack( "BackPack" ),
		Helmet_Visor( "Helmet", "Visor" ),
		Helmet_Helmetview( "Helmet", "Helmetview" ),
		Helmet( "Helmet" );
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
