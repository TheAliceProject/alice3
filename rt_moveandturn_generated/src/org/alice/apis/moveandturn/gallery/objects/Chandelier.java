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
	
public class Chandelier extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Chandelier() {
		super( "Objects/chandelier" );
	}
	public enum Part {
		Strut5( "Strut5" ),
		Strut6( "Strut6" ),
		Strut7( "Strut7" ),
		Strut8( "Strut8" ),
		Strut1( "Strut1" ),
		Strut2( "Strut2" ),
		Strut3( "Strut3" ),
		Strut4( "Strut4" ),
		Lamp5( "Lamp5" ),
		Lamp6( "Lamp6" ),
		Lamp7( "Lamp7" ),
		Lamp8( "Lamp8" ),
		Lamp1( "Lamp1" ),
		Lamp2( "Lamp2" ),
		Lamp3( "Lamp3" ),
		Lamp4( "Lamp4" ),
		CChain_WCSmall_LittleS8( "CChain", "WCSmall", "LittleS8" ),
		CChain_WCSmall_LittleS1( "CChain", "WCSmall", "LittleS1" ),
		CChain_WCSmall_LittleS2( "CChain", "WCSmall", "LittleS2" ),
		CChain_WCSmall_LittleS3( "CChain", "WCSmall", "LittleS3" ),
		CChain_WCSmall_LittleS4( "CChain", "WCSmall", "LittleS4" ),
		CChain_WCSmall_LittleS5( "CChain", "WCSmall", "LittleS5" ),
		CChain_WCSmall_LittleS6( "CChain", "WCSmall", "LittleS6" ),
		CChain_WCSmall_LittleS7( "CChain", "WCSmall", "LittleS7" ),
		CChain_WCSmall( "CChain", "WCSmall" ),
		CChain( "CChain" ),
		RChain( "RChain" ),
		LChain( "LChain" );
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
