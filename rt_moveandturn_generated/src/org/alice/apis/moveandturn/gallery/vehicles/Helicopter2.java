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
package org.alice.apis.moveandturn.gallery.vehicles;
	
public class Helicopter2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Helicopter2() {
		super( "Vehicles/helicopter2" );
	}
	public enum Part {
		Heliwindow( "heliwindow" ),
		Motorwing( "motorwing" ),
		Wingbackup( "wingbackup" ),
		Wingbackdown( "wingbackdown" ),
		Legsupport1( "legsupport1" ),
		Legsupport2( "legsupport2" ),
		Legsupport4( "legsupport4" ),
		Legsupport3( "legsupport3" ),
		Legr( "legr" ),
		Legl( "legl" ),
		Topwingaxis_Topwing1( "topwingaxis", "topwing1" ),
		Topwingaxis_Topwing2( "topwingaxis", "topwing2" ),
		Topwingaxis_Topwing3( "topwingaxis", "topwing3" ),
		Topwingaxis( "topwingaxis" );
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
