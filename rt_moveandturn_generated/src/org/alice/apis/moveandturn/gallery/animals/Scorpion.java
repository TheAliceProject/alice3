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
package org.alice.apis.moveandturn.gallery.animals;
	
public class Scorpion extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Scorpion() {
		super( "Animals/scorpion" );
	}
	public enum Part {
		Tail1_Tail2( "tail1", "tail2" ),
		Tail1_Tail3( "tail1", "tail3" ),
		Tail1_Tail4( "tail1", "tail4" ),
		Tail1( "tail1" ),
		Leg08( "leg08" ),
		Leg05( "leg05" ),
		Leg06( "leg06" ),
		Leg07( "leg07" ),
		Leg01( "leg01" ),
		Leg04( "leg04" ),
		Leg03( "leg03" ),
		Leg02( "leg02" ),
		Claw1_Claw1a( "claw1", "claw1a" ),
		Claw1_Claw1b( "claw1", "claw1b" ),
		Claw1( "claw1" ),
		Claw02_Claw2a( "claw02", "claw2a" ),
		Claw02_Claw2b( "claw02", "claw2b" ),
		Claw02( "claw02" );
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
