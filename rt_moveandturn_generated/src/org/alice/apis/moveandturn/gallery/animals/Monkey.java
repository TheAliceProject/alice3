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
	
public class Monkey extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Monkey() {
		super( "Animals/monkey" );
	}
	public enum Part {
		LeftArm_LeftForearm_LeftHand( "left arm", "left forearm", "left hand" ),
		LeftArm_LeftForearm( "left arm", "left forearm" ),
		LeftArm( "left arm" ),
		LeftLeg_LeftCalf_LeftFoot( "left leg", "left calf", "left foot" ),
		LeftLeg_LeftCalf( "left leg", "left calf" ),
		LeftLeg( "left leg" ),
		RightArm_RightForearm_RightHand( "right arm", "right forearm", "right hand" ),
		RightArm_RightForearm( "right arm", "right forearm" ),
		RightArm( "right arm" ),
		RightLeg_RightCalf_RightFoot( "right leg", "right calf", "right foot" ),
		RightLeg_RightCalf( "right leg", "right calf" ),
		RightLeg( "right leg" ),
		Tail( "tail" ),
		Head_UpperTeeth( "head", "upper teeth" ),
		Head_LeftEar( "head", "left ear" ),
		Head_RightEye( "head", "right eye" ),
		Head_LeftEyebrow( "head", "left eyebrow" ),
		Head_RightEar( "head", "right ear" ),
		Head_LeftEye( "head", "left eye" ),
		Head_RightEyebrow( "head", "right eyebrow" ),
		Head_Jaw_LowerTeeth( "head", "jaw", "lower teeth" ),
		Head_Jaw( "head", "jaw" ),
		Head( "head" );
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
