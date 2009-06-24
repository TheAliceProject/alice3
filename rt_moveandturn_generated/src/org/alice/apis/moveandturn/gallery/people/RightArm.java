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
	
public class RightArm extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public RightArm() {
		super( "People/RightArm" );
	}
	public enum Part {
		Palm_Pinky3_Pinky2_Pinky1( "palm", "pinky3", "pinky2", "pinky1" ),
		Palm_Pinky3_Pinky2( "palm", "pinky3", "pinky2" ),
		Palm_Pinky3( "palm", "pinky3" ),
		Palm_Index3_Index2_Index1( "palm", "index3", "index2", "index1" ),
		Palm_Index3_Index2( "palm", "index3", "index2" ),
		Palm_Index3( "palm", "index3" ),
		Palm_Middle3_Middle2_Middle1( "palm", "middle3", "middle2", "middle1" ),
		Palm_Middle3_Middle2( "palm", "middle3", "middle2" ),
		Palm_Middle3( "palm", "middle3" ),
		Palm_Pointer3_Pointer2_Pointer1( "palm", "pointer3", "pointer2", "pointer1" ),
		Palm_Pointer3_Pointer2( "palm", "pointer3", "pointer2" ),
		Palm_Pointer3( "palm", "pointer3" ),
		Palm_Lowerthumb_Upperthumb( "palm", "lowerthumb", "upperthumb" ),
		Palm_Lowerthumb( "palm", "lowerthumb" ),
		Palm( "palm" );
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
