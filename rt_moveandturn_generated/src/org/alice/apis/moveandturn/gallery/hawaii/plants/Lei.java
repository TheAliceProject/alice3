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
package org.alice.apis.moveandturn.gallery.hawaii.plants;
	
public class Lei extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Lei() {
		super( "Hawaii/Plants/Lei" );
	}
	public enum Part {
		QuadPatch01( "QuadPatch01" ),
		QuadPatch22( "QuadPatch22" ),
		QuadPatch02( "QuadPatch02" ),
		QuadPatch03( "QuadPatch03" ),
		QuadPatch04( "QuadPatch04" ),
		QuadPatch05( "QuadPatch05" ),
		QuadPatch06( "QuadPatch06" ),
		QuadPatch07( "QuadPatch07" ),
		QuadPatch08( "QuadPatch08" ),
		QuadPatch09( "QuadPatch09" ),
		QuadPatch10( "QuadPatch10" ),
		QuadPatch11( "QuadPatch11" ),
		QuadPatch12( "QuadPatch12" ),
		QuadPatch13( "QuadPatch13" ),
		QuadPatch14( "QuadPatch14" ),
		QuadPatch15( "QuadPatch15" ),
		QuadPatch21( "QuadPatch21" ),
		QuadPatch20( "QuadPatch20" ),
		QuadPatch18( "QuadPatch18" ),
		QuadPatch19( "QuadPatch19" ),
		QuadPatch23( "QuadPatch23" ),
		QuadPatch16( "QuadPatch16" ),
		QuadPatch17( "QuadPatch17" );
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
