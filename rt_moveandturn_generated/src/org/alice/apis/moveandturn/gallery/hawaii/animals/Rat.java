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
package org.alice.apis.moveandturn.gallery.hawaii.animals;
	
public class Rat extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Rat() {
		super( "Hawaii/Animals/Rat" );
	}
	public enum Part {
		Rat( "rat" ),
		Rightear( "rightear" ),
		Lwhisker1( "Lwhisker1" ),
		Lwhisker2( "Lwhisker2" ),
		Lwhisker3( "Lwhisker3" ),
		Rwhisker2( "Rwhisker2" ),
		Rwhisker3( "Rwhisker3" ),
		Rwhisker1( "Rwhisker1" ),
		Leftear( "leftear" );
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
