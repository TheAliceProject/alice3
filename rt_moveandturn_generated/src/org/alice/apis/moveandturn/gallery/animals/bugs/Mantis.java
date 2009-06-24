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
package org.alice.apis.moveandturn.gallery.animals.bugs;
	
public class Mantis extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Mantis() {
		super( "Animals/Bugs/mantis" );
	}
	public enum Part {
		RightBackLeg_RightBackLeg2_RightBackLeg3( "RightBackLeg", "RightBackLeg2", "RightBackLeg3" ),
		RightBackLeg_RightBackLeg2( "RightBackLeg", "RightBackLeg2" ),
		RightBackLeg( "RightBackLeg" ),
		RightMiddleLeg( "RightMiddleLeg" ),
		MidBody_RightArm_RightForearm( "mid_body", "RightArm", "RightForearm" ),
		MidBody_RightArm( "mid_body", "RightArm" ),
		MidBody_Head_REye( "mid_body", "head", "r_eye" ),
		MidBody_Head_LEye( "mid_body", "head", "l_eye" ),
		MidBody_Head_RAntenna( "mid_body", "head", "r_antenna" ),
		MidBody_Head_LAntenna( "mid_body", "head", "l_antenna" ),
		MidBody_Head( "mid_body", "head" ),
		MidBody_LeftArm_LeftForeArm( "mid_body", "LeftArm", "LeftForeArm" ),
		MidBody_LeftArm( "mid_body", "LeftArm" ),
		MidBody( "mid_body" ),
		LeftMiddleLeg( "LeftMiddleLeg" ),
		LeftBackLeg_LeftBackLeg2_LeftBackLeg3( "LeftBackLeg", "LeftBackLeg2", "LeftBackLeg3" ),
		LeftBackLeg_LeftBackLeg2( "LeftBackLeg", "LeftBackLeg2" ),
		LeftBackLeg( "LeftBackLeg" );
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
