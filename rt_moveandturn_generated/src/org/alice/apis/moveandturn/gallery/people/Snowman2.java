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
package org.alice.apis.moveandturn.gallery.people;
	
public class Snowman2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Snowman2() {
		super( "People/Snowman2" );
	}
	public enum Part {
		Head_TopHat( "Head", "TopHat" ),
		Head_LeftEye( "Head", "LeftEye" ),
		Head_RightEye( "Head", "RightEye" ),
		Head_CarrotNose( "Head", "CarrotNose" ),
		Head_Mouth( "Head", "Mouth" ),
		Head( "Head" ),
		LeftArm( "LeftArm" ),
		RightArm( "RightArm" ),
		Bottom( "Bottom" );
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
