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
	
public class Bikekid2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Bikekid2() {
		super( "People/bikekid2" );
	}
	public enum Part {
		Frame_Handlebars_HandR( "frame", "handlebars", "handR" ),
		Frame_Handlebars_HandL( "frame", "handlebars", "handL" ),
		Frame_Handlebars_Frontwheel( "frame", "handlebars", "frontwheel" ),
		Frame_Handlebars( "frame", "handlebars" ),
		Frame_Backwheel( "frame", "backwheel" ),
		Frame_Gears_PetalR( "frame", "gears", "petalR" ),
		Frame_Gears_PetalL( "frame", "gears", "petalL" ),
		Frame_Gears( "frame", "gears" ),
		Frame_ThighL_CalfL_FootL( "frame", "thighL", "calfL", "FootL" ),
		Frame_ThighL_CalfL( "frame", "thighL", "calfL" ),
		Frame_ThighL( "frame", "thighL" ),
		Frame_ThighR_CalfR_FootR( "frame", "thighR", "calfR", "FootR" ),
		Frame_ThighR_CalfR( "frame", "thighR", "calfR" ),
		Frame_ThighR( "frame", "thighR" ),
		Frame_Kid1( "frame", "kid1" ),
		Frame( "frame" ),
		BottomBracket( "bottomBracket" );
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
