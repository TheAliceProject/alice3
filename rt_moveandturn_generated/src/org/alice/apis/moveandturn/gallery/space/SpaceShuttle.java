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
package org.alice.apis.moveandturn.gallery.space;
	
public class SpaceShuttle extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SpaceShuttle() {
		super( "Space/SpaceShuttle" );
	}
	public enum Part {
		Wing_Top( "wing", "top" ),
		Wing_Bottom( "wing", "bottom" ),
		Wing( "wing" ),
		JetEngine( "jet_engine" ),
		JetEngine01( "jet_engine01" ),
		Rocket( "rocket" ),
		Rocket01( "rocket01" ),
		Rocket02( "rocket02" ),
		Front_Door( "Front", "Door" ),
		Front( "Front" ),
		FrontInside( "front_inside" ),
		Fuel2( "fuel2" ),
		Supplies2( "supplies2" ),
		Supplies4( "supplies4" ),
		Fuel1( "fuel1" ),
		Supplies1( "supplies1" ),
		Fuel3( "fuel3" ),
		Supplies3( "supplies3" ),
		FuelPipe3( "fuel_pipe3" ),
		FuelPipe1( "fuel_pipe1" ),
		FuelPipe2( "fuel_pipe2" ),
		TopdoorLeft( "topdoorLeft" ),
		Topdoorright( "topdoorright" ),
		FrontWheels_FrontRightWheel( "front_wheels", "Front_right_Wheel" ),
		FrontWheels_FrontLeftWheel( "front_wheels", "Front_Left_Wheel" ),
		FrontWheels( "front_wheels" ),
		BackWheels_BackLeftWheel( "back_wheels", "Back_Left_Wheel" ),
		BackWheels_BackRightWheel( "back_wheels", "Back_Right_Wheel" ),
		BackWheels( "back_wheels" );
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
