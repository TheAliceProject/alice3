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
package org.alice.apis.moveandturn.gallery.environments;
	
public class Oasis extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Oasis() {
		super( "Environments/oasis" );
	}
	public enum Part {
		Stone2( "stone2" ),
		Stone1( "stone1" ),
		Stone04( "stone04" ),
		Stone05( "stone05" ),
		Stone06( "stone06" ),
		Stone10( "stone10" ),
		Stone15( "stone15" ),
		Stone14( "stone14" ),
		Stone16( "stone16" ),
		Stone99( "stone99" ),
		Stone13( "stone13" ),
		Stone12( "stone12" ),
		Stone29( "stone29" ),
		Stone20( "stone20" ),
		Stone101( "stone101" ),
		Stone102( "stone102" ),
		Stone11( "stone11" ),
		Stone17( "stone17" ),
		Stone104( "stone104" ),
		Stone105( "stone105" ),
		Stone18( "stone18" ),
		Stone107( "stone107" ),
		Stone108( "stone108" ),
		Stone19( "stone19" ),
		Stone02( "stone02" );
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
