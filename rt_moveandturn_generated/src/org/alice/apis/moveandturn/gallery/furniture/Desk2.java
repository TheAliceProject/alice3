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
package org.alice.apis.moveandturn.gallery.furniture;
	
public class Desk2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Desk2() {
		super( "Furniture/desk2" );
	}
	public enum Part {
		Shelf06( "shelf06" ),
		Support06( "support06" ),
		Support09( "support09" ),
		Support01( "support01" ),
		Support05( "support05" ),
		Shelf01( "shelf01" ),
		Shelf03( "shelf03" ),
		Support04( "support04" ),
		Shelf04( "shelf04" ),
		Shelf05( "shelf05" ),
		Shelf02( "shelf02" ),
		Shelf08( "shelf08" ),
		Shelf07( "shelf07" ),
		Support08( "support08" ),
		Support02( "support02" ),
		Support03( "support03" ),
		Drawer01( "drawer01" ),
		Drawer03( "drawer03" ),
		Drawer02( "drawer02" ),
		Door_Handle( "door", "handle" ),
		Door( "door" ),
		Back01( "back01" ),
		Back03( "back03" ),
		Back02( "back02" );
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
