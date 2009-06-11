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
	
public class Boxcar extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Boxcar() {
		super( "Vehicles/Boxcar" );
	}
	public enum Part {
		Wheels_Front_Left( "Wheels", "Front", "Left" ),
		Wheels_Front_Right( "Wheels", "Front", "Right" ),
		Wheels_Front( "Wheels", "Front" ),
		Wheels_Midfront_Left( "Wheels", "Midfront", "Left" ),
		Wheels_Midfront_Right( "Wheels", "Midfront", "Right" ),
		Wheels_Midfront( "Wheels", "Midfront" ),
		Wheels_Back_Left( "Wheels", "Back", "Left" ),
		Wheels_Back_Right( "Wheels", "Back", "Right" ),
		Wheels_Back( "Wheels", "Back" ),
		Wheels_Midback_Left( "Wheels", "Midback", "Left" ),
		Wheels_Midback_Right( "Wheels", "Midback", "Right" ),
		Wheels_Midback( "Wheels", "Midback" ),
		Wheels( "Wheels" ),
		Door1( "Door1" ),
		Door2( "Door2" );
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
