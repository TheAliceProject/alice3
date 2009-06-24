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
package org.alice.apis.moveandturn.gallery.holidays.christmas;
	
public class Mistletoe extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Mistletoe() {
		super( "Holidays/Christmas/mistletoe" );
	}
	public enum Part {
		Berry( "berry" ),
		Berry1( "berry1" ),
		Berry2( "berry2" ),
		Berry3( "berry3" ),
		Berry4( "berry4" ),
		Berry5( "berry5" ),
		Berry6( "berry6" ),
		Leaf1( "leaf1" ),
		Leaf2( "leaf2" ),
		Leaf3( "leaf3" ),
		Leaf4( "leaf4" ),
		Leaf5( "leaf5" ),
		Leaf6( "leaf6" ),
		Leaf8( "leaf8" ),
		Leaf9( "leaf9" );
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
