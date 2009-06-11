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
package org.alice.apis.moveandturn.gallery.sports;
	
public class Pooltable extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Pooltable() {
		super( "Sports/pooltable" );
	}
	public enum Part {
		Table( "table" ),
		Ball1( "1Ball" ),
		Ball15( "15Ball" ),
		Ball2( "2Ball" ),
		Ball8( "8Ball" ),
		Ball6( "6Ball" ),
		Ball14( "14Ball" ),
		Ball12( "12Ball" ),
		Ball4( "4Ball" ),
		Ball11( "11Ball" ),
		Ball7( "7Ball" ),
		Ball3( "3Ball" ),
		Ball9( "9Ball" ),
		Ball10( "10Ball" ),
		Ball5( "5Ball" ),
		Ball13( "13Ball" ),
		Rack( "rack" ),
		Cueball( "cueball" );
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
