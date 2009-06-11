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
	
public class PJ extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public PJ() {
		super( "People/PJ" );
	}
	public enum Part {
		LowerBody_RightLeg_RightFoot( "Lower Body", "Right Leg", "Right Foot" ),
		LowerBody_RightLeg( "Lower Body", "Right Leg" ),
		LowerBody_LeftLeg_LeftFoot( "Lower Body", "Left Leg", "Left Foot" ),
		LowerBody_LeftLeg( "Lower Body", "Left Leg" ),
		LowerBody( "Lower Body" ),
		Head_LeftEar( "Head", "Left Ear" ),
		Head_RightEar( "Head", "Right Ear" ),
		Head( "Head" ),
		LeftArm_LeftForearm_LeftHand( "Left Arm", "Left Forearm", "Left Hand" ),
		LeftArm_LeftForearm( "Left Arm", "Left Forearm" ),
		LeftArm( "Left Arm" ),
		RightArm_RightForearm_RightHand( "Right Arm", "Right Forearm", "Right Hand" ),
		RightArm_RightForearm( "Right Arm", "Right Forearm" ),
		RightArm( "Right Arm" );
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
