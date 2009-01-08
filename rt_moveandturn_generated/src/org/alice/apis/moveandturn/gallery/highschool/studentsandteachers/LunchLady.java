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
	
public class LunchLady extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public LunchLady() {
		super( "High School/Students and Teachers/LunchLady" );
	}
	public enum Part {
		Body_LeftArm_LeftForearm_LeftHand( "Body", "LeftArm", "LeftForearm", "LeftHand" ),
		Body_LeftArm_LeftForearm( "Body", "LeftArm", "LeftForearm" ),
		Body_LeftArm( "Body", "LeftArm" ),
		Body_RightArm_RightForearm_RightHand( "Body", "RightArm", "RightForearm", "RightHand" ),
		Body_RightArm_RightForearm( "Body", "RightArm", "RightForearm" ),
		Body_RightArm( "Body", "RightArm" ),
		Body_Neck_Head_Wig( "Body", "Neck", "Head", "wig" ),
		Body_Neck_Head_Glasses( "Body", "Neck", "Head", "glasses" ),
		Body_Neck_Head( "Body", "Neck", "Head" ),
		Body_Neck( "Body", "Neck" ),
		Body( "Body" ),
		LeftLeg_LeftFoot( "LeftLeg", "LeftFoot" ),
		LeftLeg( "LeftLeg" ),
		RightLeg_RightFoot( "RightLeg", "RightFoot" ),
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
