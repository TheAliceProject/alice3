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
package org.alice.apis.moveandturn.gallery.hawaii.environments;
	
public class Volcano2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Volcano2() {
		super( "Hawaii/Environments/Volcano-2" );
	}
	public enum Part {
		Lava1( "lava1" ),
		Lava3( "lava3" ),
		Lava2( "lava2" ),
		Lava6( "lava6" ),
		Lava5( "lava5" ),
		Lava4( "lava4" ),
		Smoke1( "smoke1" ),
		Smoke2( "smoke2" ),
		Smoke5( "smoke5" ),
		Smoke7( "smoke7" ),
		Smoke4( "smoke4" ),
		Smoke8( "smoke8" ),
		Smoke6( "smoke6" ),
		Smoke17( "smoke17" ),
		Smoke12( "smoke12" ),
		Smoke11( "smoke11" ),
		Smoke10( "smoke10" ),
		Smoke13( "smoke13" ),
		Smoke9( "smoke9" ),
		Smoke14( "smoke14" ),
		Smoke15( "smoke15" ),
		Smoke16( "smoke16" );
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
