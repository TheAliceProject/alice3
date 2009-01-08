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
	
public class Nerd extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Nerd() {
		super( "High School/Students and Teachers/Nerd" );
	}
	public enum Part {
		LeftLeg_LeftShin_LeftFoot( "leftLeg", "leftShin", "leftFoot" ),
		LeftLeg_LeftShin( "leftLeg", "leftShin" ),
		LeftLeg( "leftLeg" ),
		RightLeg_RightShin_RightFoot( "rightLeg", "rightShin", "rightFoot" ),
		RightLeg_RightShin( "rightLeg", "rightShin" ),
		RightLeg( "rightLeg" ),
		LeftArm_LeftForearm_LeftHand( "leftArm", "leftForearm", "leftHand" ),
		LeftArm_LeftForearm( "leftArm", "leftForearm" ),
		LeftArm( "leftArm" ),
		RightArm_RightForearm_RightHand( "rightArm", "rightForearm", "rightHand" ),
		RightArm_RightForearm( "rightArm", "rightForearm" ),
		RightArm( "rightArm" ),
		Tie( "tie" ),
		Neck_Head_Glasses( "neck", "head", "glasses" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" );
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
