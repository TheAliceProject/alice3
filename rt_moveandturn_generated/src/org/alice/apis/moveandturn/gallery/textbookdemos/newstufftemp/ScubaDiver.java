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
package org.alice.apis.moveandturn.gallery.textbookdemos.newstufftemp;
	
public class ScubaDiver extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public ScubaDiver() {
		super( "Textbook Demos/New Stuff (Temp)/ScubaDiver" );
	}
	public enum Part {
		Pelvis_RightUpperLeg_RightKnee_RightLowerLeg_RightFoot( "Pelvis", "RightUpperLeg", "RightKnee", "RightLowerLeg", "RightFoot" ),
		Pelvis_RightUpperLeg_RightKnee_RightLowerLeg( "Pelvis", "RightUpperLeg", "RightKnee", "RightLowerLeg" ),
		Pelvis_RightUpperLeg_RightKnee( "Pelvis", "RightUpperLeg", "RightKnee" ),
		Pelvis_RightUpperLeg( "Pelvis", "RightUpperLeg" ),
		Pelvis_LeftUpperLeg_LeftKnee_LeftLowerLeg_LeftFoot( "Pelvis", "LeftUpperLeg", "LeftKnee", "LeftLowerLeg", "LeftFoot" ),
		Pelvis_LeftUpperLeg_LeftKnee_LeftLowerLeg( "Pelvis", "LeftUpperLeg", "LeftKnee", "LeftLowerLeg" ),
		Pelvis_LeftUpperLeg_LeftKnee( "Pelvis", "LeftUpperLeg", "LeftKnee" ),
		Pelvis_LeftUpperLeg( "Pelvis", "LeftUpperLeg" ),
		Pelvis( "Pelvis" ),
		LeftUpperArm_LeftForearm_LeftHand( "LeftUpperArm", "LeftForearm", "LeftHand" ),
		LeftUpperArm_LeftForearm( "LeftUpperArm", "LeftForearm" ),
		LeftUpperArm( "LeftUpperArm" ),
		RightUpperArm_RightForearm_RightHand( "RightUpperArm", "RightForearm", "RightHand" ),
		RightUpperArm_RightForearm( "RightUpperArm", "RightForearm" ),
		RightUpperArm( "RightUpperArm" ),
		Neck_Head_Regulator( "Neck", "Head", "Regulator" ),
		Neck_Head( "Neck", "Head" ),
		Neck( "Neck" ),
		OxygenTank_Tube( "OxygenTank", "Tube" ),
		OxygenTank_Valve1( "OxygenTank", "Valve1" ),
		OxygenTank_Valve3( "OxygenTank", "Valve3" ),
		OxygenTank_Valve2( "OxygenTank", "Valve2" ),
		OxygenTank( "OxygenTank" );
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
