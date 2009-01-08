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
	
public class Mouse extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Mouse() {
		super( "Animals/Mouse" );
	}
	public enum Part {
		MouseBody_LeftFoot( "MouseBody", "LeftFoot" ),
		MouseBody_RightFoot( "MouseBody", "RightFoot" ),
		MouseBody_RightArm( "MouseBody", "RightArm" ),
		MouseBody_LeftArm( "MouseBody", "LeftArm" ),
		MouseBody_Tail( "MouseBody", "Tail" ),
		MouseBody_Head_LeftEar( "MouseBody", "head", "LeftEar" ),
		MouseBody_Head_LeftEye( "MouseBody", "head", "LeftEye" ),
		MouseBody_Head_RightEye( "MouseBody", "head", "RightEye" ),
		MouseBody_Head_RightEar( "MouseBody", "head", "RightEar" ),
		MouseBody_Head_Nose( "MouseBody", "head", "Nose" ),
		MouseBody_Head( "MouseBody", "head" ),
		MouseBody( "MouseBody" );
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
