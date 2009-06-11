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
	
public class LeftHand extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public LeftHand() {
		super( "People/LeftHand" );
	}
	public enum Part {
		Middle1_Middle2_Middle3( "middle1", "middle2", "middle3" ),
		Middle1_Middle2( "middle1", "middle2" ),
		Middle1( "middle1" ),
		Index1_Index2_Index3( "index1", "index2", "index3" ),
		Index1_Index2( "index1", "index2" ),
		Index1( "index1" ),
		Ring1_Ring2_Ring3( "ring1", "ring2", "ring3" ),
		Ring1_Ring2( "ring1", "ring2" ),
		Ring1( "ring1" ),
		Pinky1_Pinky2_Pinky3( "pinky1", "pinky2", "pinky3" ),
		Pinky1_Pinky2( "pinky1", "pinky2" ),
		Pinky1( "pinky1" ),
		Thumb1_Thumb2_Thumb3( "thumb1", "thumb2", "thumb3" ),
		Thumb1_Thumb2( "thumb1", "thumb2" ),
		Thumb1( "thumb1" );
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
