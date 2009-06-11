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
package org.alice.apis.moveandturn.gallery.nature;
	
public class Flower3 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Flower3() {
		super( "Nature/flower3" );
	}
	public enum Part {
		Petal09( "petal09" ),
		Center( "center" ),
		Petal01( "petal01" ),
		Petal10( "petal10" ),
		Petal11( "petal11" ),
		Petal12( "petal12" ),
		Petal13( "petal13" ),
		Petal14( "petal14" ),
		Petal15( "petal15" ),
		Petal16( "petal16" ),
		Petal17( "petal17" ),
		Petal18( "petal18" );
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
