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
package org.alice.apis.moveandturn.gallery.fantasy.faeries;
	
public class FaerieTerrain extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public FaerieTerrain() {
		super( "Fantasy/Faeries/FaerieTerrain" );
	}
	public enum Part {
		Pond( "pond" ),
		River01( "river01" ),
		Lip_Waterfall( "lip", "waterfall" ),
		Lip( "lip" ),
		Rock14( "rock14" ),
		Rock13( "rock13" ),
		Rock12( "rock12" ),
		Rock11( "rock11" ),
		Rock10( "rock10" ),
		Rock09( "rock09" ),
		Rock08( "rock08" ),
		Rock07( "rock07" ),
		Rock06( "rock06" ),
		Rock05( "rock05" ),
		Rock04( "rock04" ),
		Rock03( "rock03" ),
		Rock02( "rock02" ),
		Rock01( "rock01" ),
		Rock15( "rock15" );
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
