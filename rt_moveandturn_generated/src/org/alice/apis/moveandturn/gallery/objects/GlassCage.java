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
	
public class GlassCage extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public GlassCage() {
		super( "Objects/GlassCage" );
	}
	public enum Part {
		GlassLeft( "glass-left" ),
		GlassBack( "glass-back" ),
		GlassRight( "glass-right" ),
		GlassFront_Ceiling_Spike( "glass-front", "ceiling", "spike" ),
		GlassFront_Ceiling( "glass-front", "ceiling" ),
		GlassFront( "glass-front" ),
		Corner1( "corner-1" ),
		Corner2( "corner-2" ),
		Corner3( "corner-3" ),
		Corner4( "corner-4" );
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
