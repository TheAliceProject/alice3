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
	
public class Camel extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Camel() {
		super( "Animals/Camel" );
	}
	public enum Part {
		RightFrontThigh_RightFrontleg( "RightFrontThigh", "RightFrontleg" ),
		RightFrontThigh( "RightFrontThigh" ),
		LeftFrontThigh_LeftFrontleg( "LeftFrontThigh", "LeftFrontleg" ),
		LeftFrontThigh( "LeftFrontThigh" ),
		Neck_Head_LeftEar( "neck", "head", "leftEar" ),
		Neck_Head_RightEar( "neck", "head", "rightEar" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" ),
		LeftRearThigh_LeftRearLeg( "LeftRearThigh", "LeftRearLeg" ),
		LeftRearThigh( "LeftRearThigh" ),
		RightRearThigh_RightRearleg( "RightRearThigh", "RightRearleg" ),
		RightRearThigh( "RightRearThigh" ),
		Tail( "tail" );
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
