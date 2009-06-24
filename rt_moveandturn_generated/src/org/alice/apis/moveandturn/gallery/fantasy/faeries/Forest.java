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
package org.alice.apis.moveandturn.gallery.fantasy.faeries;
	
public class Forest extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Forest() {
		super( "Fantasy/Faeries/Forest" );
	}
	public enum Part {
		Tree01( "tree01" ),
		Tree02( "tree02" ),
		Tree03( "tree03" ),
		Tree04( "tree04" ),
		Tree05( "tree05" ),
		Tree06( "tree06" ),
		Tree07( "tree07" ),
		Tree08( "tree08" ),
		Tree09( "tree09" ),
		Tree10( "tree10" ),
		Tree11( "tree11" ),
		Tree12( "tree12" ),
		Tree13( "tree13" ),
		Tree14( "tree14" );
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
