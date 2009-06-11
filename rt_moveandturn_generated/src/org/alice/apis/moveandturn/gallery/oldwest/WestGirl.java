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
package org.alice.apis.moveandturn.gallery.oldwest;
	
public class WestGirl extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public WestGirl() {
		super( "Old West/WestGirl" );
	}
	public enum Part {
		Body_LeftArm_LeftForearm_LeftHand( "body", "LeftArm", "LeftForearm", "LeftHand" ),
		Body_LeftArm_LeftForearm( "body", "LeftArm", "LeftForearm" ),
		Body_LeftArm( "body", "LeftArm" ),
		Body_RightArm_RightForearm_RightHand( "body", "RightArm", "RightForearm", "RightHand" ),
		Body_RightArm_RightForearm( "body", "RightArm", "RightForearm" ),
		Body_RightArm( "body", "RightArm" ),
		Body_Neck_Head_Lpony( "body", "neck", "Head", "Lpony" ),
		Body_Neck_Head_Rpony( "body", "neck", "Head", "Rpony" ),
		Body_Neck_Head( "body", "neck", "Head" ),
		Body_Neck( "body", "neck" ),
		Body_Belt( "body", "belt" ),
		Body( "body" ),
		LeftLeg_LeftFoot( "LeftLeg", "LeftFoot" ),
		LeftLeg( "LeftLeg" ),
		RightLeg_Rightfoot( "RightLeg", "Rightfoot" ),
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
