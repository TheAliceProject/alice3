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
package org.alice.apis.moveandturn.gallery.animals;
	
public class SleepingCat extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SleepingCat() {
		super( "Animals/sleeping_cat" );
	}
	public enum Part {
		Body_Head_LeftEar( "Body", "Head", "left Ear" ),
		Body_Head_RightEye( "Body", "Head", "right Eye" ),
		Body_Head_Mouth( "Body", "Head", "Mouth" ),
		Body_Head_RightEye01( "Body", "Head", "right Eye01" ),
		Body_Head_RightEar( "Body", "Head", "right Ear" ),
		Body_Head( "Body", "Head" ),
		Body_LeftArm_LeftElbow_LeftHand( "Body", "left Arm", "left Elbow", "left Hand" ),
		Body_LeftArm_LeftElbow( "Body", "left Arm", "left Elbow" ),
		Body_LeftArm( "Body", "left Arm" ),
		Body_RightArm_RightElbow_RightHand( "Body", "right Arm", "right Elbow", "right Hand" ),
		Body_RightArm_RightElbow( "Body", "right Arm", "right Elbow" ),
		Body_RightArm( "Body", "right Arm" ),
		Body_LeftThigh_LeftLeg_LeftFoot( "Body", "left Thigh", "left Leg", "left Foot" ),
		Body_LeftThigh_LeftLeg( "Body", "left Thigh", "left Leg" ),
		Body_LeftThigh( "Body", "left Thigh" ),
		Body_Tail1_Tail2_Tail3_Tail4( "Body", "Tail1", "Tail2", "Tail3", "Tail4" ),
		Body_Tail1_Tail2_Tail3( "Body", "Tail1", "Tail2", "Tail3" ),
		Body_Tail1_Tail2( "Body", "Tail1", "Tail2" ),
		Body_Tail1( "Body", "Tail1" ),
		Body_RightThigh_RightLeg_RightFoot( "Body", "right Thigh", "right Leg", "right foot" ),
		Body_RightThigh_RightLeg( "Body", "right Thigh", "right Leg" ),
		Body_RightThigh( "Body", "right Thigh" ),
		Body( "Body" ),
		Bed( "bed" ),
		BedPillow( "BedPillow" );
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
