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
package org.alice.apis.moveandturn.gallery.hawaii;
	
public class Konana extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Konana() {
		super( "Hawaii/Konana" );
	}
	public enum Part {
		KonanaBoard( "konana board" ),
		Black1( "black1" ),
		White1( "white1" ),
		Black2( "black2" ),
		Black3( "black3" ),
		White3( "white3" ),
		White2( "white2" ),
		Black4( "black4" ),
		Black05( "black05" ),
		Black06( "black06" ),
		Black07( "black07" ),
		White04( "white04" ),
		White05( "white05" ),
		White06( "white06" ),
		White07( "white07" ),
		Black08( "black08" ),
		White08( "white08" ),
		Black09( "black09" ),
		Black10( "black10" ),
		White09( "white09" ),
		White10( "white10" ),
		Black11( "black11" ),
		Black12( "black12" ),
		Black13( "black13" ),
		White11( "white11" ),
		White12( "white12" ),
		Black14( "black14" ),
		White13( "white13" ),
		White14( "white14" ),
		Black15( "black15" ),
		White15( "white15" ),
		Black16( "black16" ),
		Black17( "black17" ),
		White16( "white16" ),
		White17( "white17" ),
		Black18( "black18" ),
		Black19( "black19" ),
		Black20( "black20" ),
		White18( "white18" ),
		White19( "white19" ),
		Black21( "black21" ),
		White20( "white20" ),
		White21( "white21" );
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
