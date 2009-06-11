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
package org.alice.apis.moveandturn.gallery.highschool.studentsandteachers;
	
public class Cheerleader extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Cheerleader() {
		super( "High School/Students and Teachers/Cheerleader" );
	}
	public enum Part {
		Torso_LeftArm_LeftForearm_LeftHand_LeftPomPom( "Torso", "LeftArm", "LeftForearm", "LeftHand", "LeftPomPom" ),
		Torso_LeftArm_LeftForearm_LeftHand( "Torso", "LeftArm", "LeftForearm", "LeftHand" ),
		Torso_LeftArm_LeftForearm( "Torso", "LeftArm", "LeftForearm" ),
		Torso_LeftArm( "Torso", "LeftArm" ),
		Torso_RightArm_RightForearm_RightHand_RightPomPom( "Torso", "RightArm", "RightForearm", "RightHand", "RightPomPom" ),
		Torso_RightArm_RightForearm_RightHand( "Torso", "RightArm", "RightForearm", "RightHand" ),
		Torso_RightArm_RightForearm( "Torso", "RightArm", "RightForearm" ),
		Torso_RightArm( "Torso", "RightArm" ),
		Torso_Neck_Head_LeftPonyTail( "Torso", "Neck", "Head", "LeftPonyTail" ),
		Torso_Neck_Head_RightPonyTail( "Torso", "Neck", "Head", "RightPonyTail" ),
		Torso_Neck_Head( "Torso", "Neck", "Head" ),
		Torso_Neck( "Torso", "Neck" ),
		Torso( "Torso" ),
		LeftLeg_LeftLowerLeg_LeftFoot( "LeftLeg", "LeftLowerLeg", "LeftFoot" ),
		LeftLeg_LeftLowerLeg( "LeftLeg", "LeftLowerLeg" ),
		LeftLeg( "LeftLeg" ),
		RightLeg_RightLowerLeg_RightFoot( "RightLeg", "RightLowerLeg", "RightFoot" ),
		RightLeg_RightLowerLeg( "RightLeg", "RightLowerLeg" ),
		RightLeg( "RightLeg" );
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
