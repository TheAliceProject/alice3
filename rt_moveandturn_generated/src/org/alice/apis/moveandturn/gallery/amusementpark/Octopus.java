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
package org.alice.apis.moveandturn.gallery.amusementpark;
	
public class Octopus extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Octopus() {
		super( "Amusement Park/Octopus" );
	}
	public enum Part {
		Center_Arm1_Pods( "Center", "Arm1", "pods" ),
		Center_Arm1( "Center", "Arm1" ),
		Center_Arm2_Pods( "Center", "Arm2", "pods" ),
		Center_Arm2( "Center", "Arm2" ),
		Center_Arm3_Pods( "Center", "Arm3", "pods" ),
		Center_Arm3( "Center", "Arm3" ),
		Center_Arm4_Pods( "Center", "Arm4", "pods" ),
		Center_Arm4( "Center", "Arm4" ),
		Center_Arm5_Pods( "Center", "Arm5", "pods" ),
		Center_Arm5( "Center", "Arm5" ),
		Center_Arm6_Pods( "Center", "Arm6", "pods" ),
		Center_Arm6( "Center", "Arm6" ),
		Center_Arm7_Pods( "Center", "Arm7", "pods" ),
		Center_Arm7( "Center", "Arm7" ),
		Center_Arm8_Pods( "Center", "Arm8", "pods" ),
		Center_Arm8( "Center", "Arm8" ),
		Center( "Center" ),
		Booth( "booth" );
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
