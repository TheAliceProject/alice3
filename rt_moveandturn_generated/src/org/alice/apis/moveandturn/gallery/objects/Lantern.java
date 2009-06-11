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
package org.alice.apis.moveandturn.gallery.objects;
	
public class Lantern extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Lantern() {
		super( "Objects/lantern" );
	}
	public enum Part {
		Outer_Inner( "outer", "inner" ),
		Outer( "outer" ),
		Bar05( "bar05" ),
		Bar02( "bar02" ),
		Bar08( "bar08" ),
		Bar06( "bar06" ),
		Bar09( "bar09" ),
		Bar03( "bar03" ),
		Bar04( "bar04" ),
		Bar07( "bar07" ),
		Roof_Ball( "roof", "ball" ),
		Roof_Handle( "roof", "handle" ),
		Roof( "roof" ),
		Flame( "flame" );
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
