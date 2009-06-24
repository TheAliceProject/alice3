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
	
public class CartoonDesert extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public CartoonDesert() {
		super( "Environments/cartoon_desert" );
	}
	public enum Part {
		Boulder4( "boulder4" ),
		Boulder3( "boulder3" ),
		Freebirdse( "freebirdse" ),
		Claycliffs( "claycliffs" ),
		Claycliff0( "claycliff0" ),
		Boulder2( "boulder2" ),
		Boulder6( "boulder6" ),
		Cactus1( "cactus1" ),
		Cactus2( "cactus2" ),
		Cactus3( "cactus3" ),
		Cactus4( "cactus4" ),
		Boulder1( "boulder1" ),
		Boulder9( "boulder9" ),
		Boulder7( "boulder7" ),
		Boulder8( "boulder8" ),
		Weed1( "weed1" ),
		Bush( "bush" ),
		Boulder5( "boulder5" ),
		Boulder10( "boulder10" ),
		Tree( "tree" ),
		ACMEbox( "ACMEbox" ),
		Plant3( "plant3" ),
		Weed3( "weed3" ),
		Plant1( "plant1" ),
		Plant2( "plant2" ),
		Weed2( "weed2" ),
		Claycliff2( "claycliff2" ),
		Claycliff1( "claycliff1" );
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
