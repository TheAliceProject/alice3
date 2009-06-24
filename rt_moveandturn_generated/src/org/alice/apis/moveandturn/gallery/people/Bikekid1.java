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
	
public class Bikekid1 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Bikekid1() {
		super( "People/bikekid1" );
	}
	public enum Part {
		Frame_Handlebars_RightHand( "Frame", "Handlebars", "RightHand" ),
		Frame_Handlebars_LeftHand( "Frame", "Handlebars", "LeftHand" ),
		Frame_Handlebars_FrontWheel( "Frame", "Handlebars", "FrontWheel" ),
		Frame_Handlebars( "Frame", "Handlebars" ),
		Frame_BackWheel( "Frame", "BackWheel" ),
		Frame_Gears_RightPetal( "Frame", "Gears", "RightPetal" ),
		Frame_Gears_LeftPetal( "Frame", "Gears", "LeftPetal" ),
		Frame_Gears( "Frame", "Gears" ),
		Frame_LeftThigh_LeftCalf_LeftFoot( "Frame", "LeftThigh", "LeftCalf", "LeftFoot" ),
		Frame_LeftThigh_LeftCalf( "Frame", "LeftThigh", "LeftCalf" ),
		Frame_LeftThigh( "Frame", "LeftThigh" ),
		Frame_RightThigh_RighCalf_RightFoot( "Frame", "RightThigh", "RighCalf", "RightFoot" ),
		Frame_RightThigh_RighCalf( "Frame", "RightThigh", "RighCalf" ),
		Frame_RightThigh( "Frame", "RightThigh" ),
		Frame_Kid( "Frame", "Kid" ),
		Frame( "Frame" ),
		BottomBracket( "BottomBracket" );
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
