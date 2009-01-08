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
	
public class Locomotive extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Locomotive() {
		super( "Vehicles/Locomotive" );
	}
	public enum Part {
		Wheels_Smfront_Left( "wheels", "smfront", "left" ),
		Wheels_Smfront_Right( "wheels", "smfront", "right" ),
		Wheels_Smfront( "wheels", "smfront" ),
		Wheels_Pistons_Left_Rod( "wheels", "pistons", "left", "rod" ),
		Wheels_Pistons_Left_Cylinder( "wheels", "pistons", "left", "cylinder" ),
		Wheels_Pistons_Left_Driver( "wheels", "pistons", "left", "driver" ),
		Wheels_Pistons_Left( "wheels", "pistons", "left" ),
		Wheels_Pistons_Right_Rod( "wheels", "pistons", "right", "rod" ),
		Wheels_Pistons_Right_Cylinder( "wheels", "pistons", "right", "cylinder" ),
		Wheels_Pistons_Right_Driver( "wheels", "pistons", "right", "driver" ),
		Wheels_Pistons_Right( "wheels", "pistons", "right" ),
		Wheels_Pistons( "wheels", "pistons" ),
		Wheels_Bigfront_Left_Pin( "wheels", "bigfront", "left", "pin" ),
		Wheels_Bigfront_Left_Wheel( "wheels", "bigfront", "left", "wheel" ),
		Wheels_Bigfront_Left( "wheels", "bigfront", "left" ),
		Wheels_Bigfront_Right_Pin( "wheels", "bigfront", "right", "pin" ),
		Wheels_Bigfront_Right_Wheel( "wheels", "bigfront", "right", "wheel" ),
		Wheels_Bigfront_Right( "wheels", "bigfront", "right" ),
		Wheels_Bigfront( "wheels", "bigfront" ),
		Wheels_Smback_Left( "wheels", "smback", "left" ),
		Wheels_Smback_Right( "wheels", "smback", "right" ),
		Wheels_Smback( "wheels", "smback" ),
		Wheels_Bigback_Left_Pin( "wheels", "bigback", "left", "pin" ),
		Wheels_Bigback_Left_Wheel( "wheels", "bigback", "left", "wheel" ),
		Wheels_Bigback_Left( "wheels", "bigback", "left" ),
		Wheels_Bigback_Right_Pin( "wheels", "bigback", "right", "pin" ),
		Wheels_Bigback_Right_Wheel( "wheels", "bigback", "right", "wheel" ),
		Wheels_Bigback_Right( "wheels", "bigback", "right" ),
		Wheels_Bigback( "wheels", "bigback" ),
		Wheels( "wheels" );
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
