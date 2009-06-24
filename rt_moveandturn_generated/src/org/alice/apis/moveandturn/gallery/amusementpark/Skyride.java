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
package org.alice.apis.moveandturn.gallery.amusementpark;
	
public class Skyride extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Skyride() {
		super( "Amusement Park/Skyride" );
	}
	public enum Part {
		Tower2( "tower_2" ),
		Tower4( "tower_4" ),
		Tower1( "tower_1" ),
		Cables_C4( "cables", "c4" ),
		Cables_C3( "cables", "c3" ),
		Cables_C2( "cables", "c2" ),
		Cables_C1( "cables", "c1" ),
		Cables_C6( "cables", "c6" ),
		Cables_C5( "cables", "c5" ),
		Cables( "cables" ),
		Tower3( "tower_3" ),
		Car1( "Car1" ),
		Car2( "Car2" ),
		Car3( "Car3" ),
		Car4( "Car4" ),
		Car5( "Car5" ),
		Car6( "Car6" ),
		Lion1( "Lion1" ),
		Lion2( "Lion2" );
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
