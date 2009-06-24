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
package org.alice.apis.moveandturn.gallery.people;
	
public class StreetGirl extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public StreetGirl() {
		super( "People/street_girl" );
	}
	public enum Part {
		Neck_Head_Ear( "neck", "head", "ear" ),
		Neck_Head_HairMain( "neck", "head", "hair main" ),
		Neck_Head_Hair01( "neck", "head", "hair01" ),
		Neck_Head_Hair02( "neck", "head", "hair02" ),
		Neck_Head_Hair03( "neck", "head", "hair03" ),
		Neck_Head_LeftEye( "neck", "head", "left eye" ),
		Neck_Head_LeftEyebrow( "neck", "head", "left eyebrow" ),
		Neck_Head_RightEye( "neck", "head", "right eye" ),
		Neck_Head_RightEyebrow( "neck", "head", "right eyebrow" ),
		Neck_Head_HairMain2( "neck", "head", "hair main2" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" ),
		LeftArm_LeftForearm_LeftHand( "left arm", "left forearm", "left hand" ),
		LeftArm_LeftForearm( "left arm", "left forearm" ),
		LeftArm( "left arm" ),
		LeftLeg( "left leg" ),
		RightArm_RightForearm_Righthand( "right arm", "right forearm", "righthand" ),
		RightArm_RightForearm( "right arm", "right forearm" ),
		RightArm( "right arm" ),
		RightLeg( "right leg" );
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
