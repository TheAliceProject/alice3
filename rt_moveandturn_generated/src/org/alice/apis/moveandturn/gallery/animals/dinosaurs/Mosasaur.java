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
package org.alice.apis.moveandturn.gallery.animals.dinosaurs;
	
public class Mosasaur extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Mosasaur() {
		super( "Animals/Dinosaurs/mosasaur" );
	}
	public enum Part {
		A_B_C_D( "a", "b", "c", "d" ),
		A_B_C( "a", "b", "c" ),
		A_B( "a", "b" ),
		A_Blfin( "a", "blfin" ),
		A_Brfin( "a", "brfin" ),
		A( "a" ),
		Frfin( "frfin" ),
		Flfin( "flfin" ),
		Neck_Head_Jaw_Teeth( "neck", "head", "jaw", "teeth" ),
		Neck_Head_Jaw( "neck", "head", "jaw" ),
		Neck_Head_Object02( "neck", "head", "Object02" ),
		Neck_Head_Sphere03( "neck", "head", "Sphere03" ),
		Neck_Head_Sphere02( "neck", "head", "Sphere02" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" );
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
