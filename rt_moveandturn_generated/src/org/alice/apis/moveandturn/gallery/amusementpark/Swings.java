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
	
public class Swings extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Swings() {
		super( "Amusement Park/Swings" );
	}
	public enum Part {
		Roof_Chair1( "roof", "Chair1" ),
		Roof_Chair2( "roof", "Chair2" ),
		Roof_Chair3( "roof", "Chair3" ),
		Roof_Chair4( "roof", "Chair4" ),
		Roof_Chair5( "roof", "Chair5" ),
		Roof_Chair6( "roof", "Chair6" ),
		Roof_Chair7( "roof", "Chair7" ),
		Roof_Chair8( "roof", "Chair8" ),
		Roof_Chair9( "roof", "Chair9" ),
		Roof_Chair10( "roof", "Chair10" ),
		Roof_Chair11( "roof", "Chair11" ),
		Roof_Chair12( "roof", "Chair12" ),
		Roof( "roof" ),
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
