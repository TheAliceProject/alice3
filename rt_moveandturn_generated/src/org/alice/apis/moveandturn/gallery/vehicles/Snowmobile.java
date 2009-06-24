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
	
public class Snowmobile extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Snowmobile() {
		super( "Vehicles/Snowmobile" );
	}
	public enum Part {
		Chassis_Poler_Skyir( "Chassis", "poler", "skyir" ),
		Chassis_Poler( "Chassis", "poler" ),
		Chassis_Seat( "Chassis", "seat" ),
		Chassis_Footarea( "Chassis", "footarea" ),
		Chassis_Engine_Wheelr_Wheel1r( "Chassis", "engine", "wheelr", "wheel1r" ),
		Chassis_Engine_Wheelr_Wheel2r( "Chassis", "engine", "wheelr", "wheel2r" ),
		Chassis_Engine_Wheelr_Wheel3r( "Chassis", "engine", "wheelr", "wheel3r" ),
		Chassis_Engine_Wheelr( "Chassis", "engine", "wheelr" ),
		Chassis_Engine_Wheell_Wheel1l( "Chassis", "engine", "wheell", "wheel1l" ),
		Chassis_Engine_Wheell_Wheel2l( "Chassis", "engine", "wheell", "wheel2l" ),
		Chassis_Engine_Wheell_Wheel3l( "Chassis", "engine", "wheell", "wheel3l" ),
		Chassis_Engine_Wheell( "Chassis", "engine", "wheell" ),
		Chassis_Engine( "Chassis", "engine" ),
		Chassis_Polel_Skil( "Chassis", "polel", "skil" ),
		Chassis_Polel( "Chassis", "polel" ),
		Chassis_CarFront_Windshield( "Chassis", "car_front", "windshield" ),
		Chassis_CarFront( "Chassis", "car_front" ),
		Chassis_Panel_Handelr_Handletopr( "Chassis", "panel", "handelr", "handletopr" ),
		Chassis_Panel_Handelr( "Chassis", "panel", "handelr" ),
		Chassis_Panel( "Chassis", "panel" ),
		Chassis( "Chassis" ),
		Flame( "Flame" ),
		FrontBox( "FrontBox" ),
		Blowtorch( "Blowtorch" );
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
