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
package org.alice.apis.moveandturn.gallery.fantasy;
	
public class Dragon2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Dragon2() {
		super( "Fantasy/dragon2" );
	}
	public enum Part {
		Neck_Cranium_Jaw( "Neck", "Cranium", "Jaw" ),
		Neck_Cranium( "Neck", "Cranium" ),
		Neck( "Neck" ),
		Rightupperarm_Rightforearm( "rightupperarm", "rightforearm" ),
		Rightupperarm( "rightupperarm" ),
		Leftupperarm_Leftforearm( "leftupperarm", "leftforearm" ),
		Leftupperarm( "leftupperarm" ),
		LeftThigh_LeftLeg_LeftFoot( "leftThigh", "leftLeg", "leftFoot" ),
		LeftThigh_LeftLeg( "leftThigh", "leftLeg" ),
		LeftThigh( "leftThigh" ),
		RightThigh_RightLeg_RightFoot( "rightThigh", "rightLeg", "rightFoot" ),
		RightThigh_RightLeg( "rightThigh", "rightLeg" ),
		RightThigh( "rightThigh" ),
		Tail01_Tail02_Tail03_Tail04_Tail05( "tail01", "tail02", "tail03", "tail04", "Tail05" ),
		Tail01_Tail02_Tail03_Tail04( "tail01", "tail02", "tail03", "tail04" ),
		Tail01_Tail02_Tail03( "tail01", "tail02", "tail03" ),
		Tail01_Tail02( "tail01", "tail02" ),
		Tail01( "tail01" ),
		LeftWing( "leftWing" ),
		RightWing( "rightWing" );
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
