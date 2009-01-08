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
	
public class Crane extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Crane() {
		super( "Vehicles/Crane" );
	}
	public enum Part {
		Cabin_Support( "Cabin", "Support" ),
		Cabin_LeftMotor( "Cabin", "LeftMotor" ),
		Cabin_RightMotor( "Cabin", "RightMotor" ),
		Cabin_Cord1_Cord2_Cord3_Magnet( "Cabin", "Cord1", "Cord2", "Cord3", "Magnet" ),
		Cabin_Cord1_Cord2_Cord3( "Cabin", "Cord1", "Cord2", "Cord3" ),
		Cabin_Cord1_Cord2( "Cabin", "Cord1", "Cord2" ),
		Cabin_Cord1( "Cabin", "Cord1" ),
		Cabin_CockpitGlass3( "Cabin", "cockpitGlass3" ),
		Cabin_CockpitGlass2( "Cabin", "cockpitGlass2" ),
		Cabin_CockpitGlass1( "Cabin", "cockpitGlass1" ),
		Cabin( "Cabin" );
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
