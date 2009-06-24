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
	
public class CheshireCat extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public CheshireCat() {
		super( "Textbook Demos/New Stuff (Temp)/cheshireCat" );
	}
	public enum Part {
		Tail( "tail" ),
		FrontRightUpperLeg_FrontRightLowerLeg_FrontRightFoot( "frontRightUpperLeg", "frontRightLowerLeg", "frontRightFoot" ),
		FrontRightUpperLeg_FrontRightLowerLeg( "frontRightUpperLeg", "frontRightLowerLeg" ),
		FrontRightUpperLeg( "frontRightUpperLeg" ),
		RearRightUpperLeg_RearRightLowerLeg_RearRightFoot( "rearRightUpperLeg", "rearRightLowerLeg", "rearRightFoot" ),
		RearRightUpperLeg_RearRightLowerLeg( "rearRightUpperLeg", "rearRightLowerLeg" ),
		RearRightUpperLeg( "rearRightUpperLeg" ),
		RearLeftUpperLeg_RearLeftLowerLeg_RearLeftFoot( "rearLeftUpperLeg", "rearLeftLowerLeg", "rearLeftFoot" ),
		RearLeftUpperLeg_RearLeftLowerLeg( "rearLeftUpperLeg", "rearLeftLowerLeg" ),
		RearLeftUpperLeg( "rearLeftUpperLeg" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg_FrontLeftFoot( "frontLeftUpperLeg", "frontLeftLowerLeg", "frontLeftFoot" ),
		FrontLeftUpperLeg_FrontLeftLowerLeg( "frontLeftUpperLeg", "frontLeftLowerLeg" ),
		FrontLeftUpperLeg( "frontLeftUpperLeg" ),
		Head_LeftEye_LeftEyeLid( "head", "leftEye", "leftEyeLid" ),
		Head_LeftEye( "head", "leftEye" ),
		Head_RightEye_RightEyeLid( "head", "rightEye", "rightEyeLid" ),
		Head_RightEye( "head", "rightEye" ),
		Head_Smile( "head", "smile" ),
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
