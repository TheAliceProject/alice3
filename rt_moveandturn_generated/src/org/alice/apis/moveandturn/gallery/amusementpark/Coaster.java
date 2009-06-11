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
	
public class Coaster extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Coaster() {
		super( "Amusement Park/Coaster" );
	}
	public enum Part {
		Track( "track" ),
		Base( "base" ),
		Supports( "supports" ),
		Car9_Locks( "Car9", "locks" ),
		Car9( "Car9" ),
		Car8_Locks( "Car8", "locks" ),
		Car8( "Car8" ),
		Car7_Locks( "Car7", "locks" ),
		Car7( "Car7" ),
		Car6_Locks( "Car6", "locks" ),
		Car6( "Car6" ),
		Car5_Locks( "Car5", "locks" ),
		Car5( "Car5" ),
		Car4_Locks( "Car4", "locks" ),
		Car4( "Car4" ),
		Car3_Locks( "car3", "locks" ),
		Car3( "car3" ),
		Car2_Locks( "car2", "locks" ),
		Car2( "car2" ),
		Car1_Locks( "car1", "locks" ),
		Car1( "car1" ),
		Booth( "booth" );
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
