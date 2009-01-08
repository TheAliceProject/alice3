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
package org.alice.apis.moveandturn.gallery.hawaii.transportation;
	
public class CanoeDouble extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public CanoeDouble() {
		super( "Hawaii/Transportation/CanoeDouble" );
	}
	public enum Part {
		Cylinder01( "Cylinder01" ),
		Line03( "Line03" ),
		Loft03( "Loft03" ),
		Cylinder02( "Cylinder02" ),
		Line04( "Line04" ),
		Cylinder03( "Cylinder03" ),
		Line05( "Line05" ),
		TriPatch02( "TriPatch02" ),
		Box03( "Box03" ),
		Line12( "Line12" ),
		Line18( "Line18" ),
		Box02( "Box02" ),
		Cap2( "cap2" ),
		Cap04( "cap04" ),
		Line07( "Line07" ),
		Loft09( "Loft09" ),
		Loft11( "Loft11" ),
		Loft13( "Loft13" ),
		Box18( "Box18" ),
		Box19( "Box19" ),
		TriPatch03( "TriPatch03" ),
		NGon02( "NGon02" ),
		NGon03( "NGon03" );
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
