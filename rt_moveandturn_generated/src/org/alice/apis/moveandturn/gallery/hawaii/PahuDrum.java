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
	
public class PahuDrum extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public PahuDrum() {
		super( "Hawaii/PahuDrum" );
	}
	public enum Part {
		NGon01( "NGon01" ),
		NGon02( "NGon02" ),
		NGon03( "NGon03" ),
		NGon04( "NGon04" ),
		Circle01( "Circle01" ),
		Circle02( "Circle02" ),
		Circle03( "Circle03" ),
		Circle04( "Circle04" ),
		Circle05( "Circle05" ),
		Circle08( "Circle08" ),
		Circle09( "Circle09" ),
		Circle10( "Circle10" ),
		Circle11( "Circle11" ),
		Circle12( "Circle12" ),
		Circle13( "Circle13" ),
		Circle14( "Circle14" ),
		Circle15( "Circle15" ),
		Circle16( "Circle16" );
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
