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
	
public class Palmtree extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Palmtree() {
		super( "Nature/palmtree" );
	}
	public enum Part {
		Leave12( "leave12" ),
		Leave11( "leave11" ),
		Leave10( "leave10" ),
		Leave09( "leave09" ),
		Leave08( "leave08" ),
		Leave06( "leave06" ),
		PalmTrunk( "palm trunk" ),
		Leave07( "leave07" ),
		Leave03( "leave03" ),
		Leave04( "leave04" ),
		Leave02( "leave02" ),
		Leave01( "leave01" ),
		Leave05( "leave05" );
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
