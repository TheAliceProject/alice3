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
package org.alice.apis.moveandturn.gallery.oldwest;
	
public class Buffalo2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Buffalo2() {
		super( "Old West/buffalo2" );
	}
	public enum Part {
		Head_LeftHorn( "head", "left_horn" ),
		Head_RightHorn( "head", "right_horn" ),
		Head( "head" ),
		LeftFrontLeg( "left_front_leg" ),
		LeftBackLeg( "left_back_leg" ),
		RightBackLeg( "right_back_leg" ),
		RightFrontLeg( "right_front_leg" );
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
