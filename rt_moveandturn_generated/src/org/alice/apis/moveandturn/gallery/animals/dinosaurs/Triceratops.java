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
	
public class Triceratops extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Triceratops() {
		super( "Animals/Dinosaurs/triceratops" );
	}
	public enum Part {
		Tail1_Tail2_Tail3( "tail1", "tail2", "tail3" ),
		Tail1_Tail2( "tail1", "tail2" ),
		Tail1( "tail1" ),
		Rifhtrearleg_Rightrearmiddleleg_RRearfoot( "rifhtrearleg", "rightrearmiddleleg", "rRearfoot" ),
		Rifhtrearleg_Rightrearmiddleleg( "rifhtrearleg", "rightrearmiddleleg" ),
		Rifhtrearleg( "rifhtrearleg" ),
		Rightfrontleg_Rightfrontmiddleleg_Rfrontfoot( "rightfrontleg", "rightfrontmiddleleg", "Rfrontfoot" ),
		Rightfrontleg_Rightfrontmiddleleg( "rightfrontleg", "rightfrontmiddleleg" ),
		Rightfrontleg( "rightfrontleg" ),
		Leftrearleg_LeftRearMiddleleg_Leftrearfoot( "leftrearleg", "LeftRearMiddleleg", "leftrearfoot" ),
		Leftrearleg_LeftRearMiddleleg( "leftrearleg", "LeftRearMiddleleg" ),
		Leftrearleg( "leftrearleg" ),
		Leftfrontleg_Leftfrontmiddleleg_Leftfrontfoot( "leftfrontleg", "leftfrontmiddleleg", "leftfrontfoot" ),
		Leftfrontleg_Leftfrontmiddleleg( "leftfrontleg", "leftfrontmiddleleg" ),
		Leftfrontleg( "leftfrontleg" ),
		Neck( "neck" ),
		Head_Leyelid( "head", "leyelid" ),
		Head_Leye( "head", "leye" ),
		Head_Fronthorn( "head", "fronthorn" ),
		Head_Bottomhorn( "head", "bottomhorn" ),
		Head_Tophorns( "head", "tophorns" ),
		Head_Reyelid( "head", "reyelid" ),
		Head_Reye( "head", "reye" ),
		Head_Jaw( "head", "jaw" ),
		Head( "head" );
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
