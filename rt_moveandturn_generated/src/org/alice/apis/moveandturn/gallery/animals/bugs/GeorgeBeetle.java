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
package org.alice.apis.moveandturn.gallery.animals.bugs;
	
public class GeorgeBeetle extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public GeorgeBeetle() {
		super( "Animals/Bugs/GeorgeBeetle" );
	}
	public enum Part {
		UpperBody_Head_RightAntenna( "UpperBody", "Head", "RightAntenna" ),
		UpperBody_Head_LeftAntenna( "UpperBody", "Head", "LeftAntenna" ),
		UpperBody_Head( "UpperBody", "Head" ),
		UpperBody_LeftUpperArm_LeftHand( "UpperBody", "LeftUpperArm", "LeftHand" ),
		UpperBody_LeftUpperArm( "UpperBody", "LeftUpperArm" ),
		UpperBody_RightUpperArm_RightHand( "UpperBody", "RightUpperArm", "RightHand" ),
		UpperBody_RightUpperArm( "UpperBody", "RightUpperArm" ),
		UpperBody( "UpperBody" ),
		RightLeg_RightShoe( "RightLeg", "RightShoe" ),
		RightLeg( "RightLeg" ),
		LeftLeg_LeftShoe( "LeftLeg", "LeftShoe" ),
		LeftLeg( "LeftLeg" );
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
