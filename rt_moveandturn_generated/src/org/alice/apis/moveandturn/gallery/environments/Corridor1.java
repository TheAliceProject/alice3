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
package org.alice.apis.moveandturn.gallery.environments;
	
public class Corridor1 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Corridor1() {
		super( "Environments/Corridor1" );
	}
	public enum Part {
		Corridor1DoorSill_Door1_DoorFrontKnobBase_DoorFrontDoorKnob( "Corridor1DoorSill", "Door1", "DoorFrontKnobBase", "DoorFrontDoorKnob" ),
		Corridor1DoorSill_Door1_DoorFrontKnobBase( "Corridor1DoorSill", "Door1", "DoorFrontKnobBase" ),
		Corridor1DoorSill_Door1_Door1RearKnobBase_Door1RearDoorKnob( "Corridor1DoorSill", "Door1", "Door1RearKnobBase", "Door1RearDoorKnob" ),
		Corridor1DoorSill_Door1_Door1RearKnobBase( "Corridor1DoorSill", "Door1", "Door1RearKnobBase" ),
		Corridor1DoorSill_Door1( "Corridor1DoorSill", "Door1" ),
		Corridor1DoorSill_Door1Bottom( "Corridor1DoorSill", "Door1Bottom" ),
		Corridor1DoorSill_Door2Bottom( "Corridor1DoorSill", "Door2Bottom" ),
		Corridor1DoorSill( "Corridor1DoorSill" );
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
