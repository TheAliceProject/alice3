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
package org.alice.apis.moveandturn.gallery.hawaii.people;
	
public class Chief extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Chief() {
		super( "Hawaii/People/Chief" );
	}
	public enum Part {
		Chest_RightBi_RightFore_RightHand( "chest", "right bi", "right fore", "right hand" ),
		Chest_RightBi_RightFore( "chest", "right bi", "right fore" ),
		Chest_RightBi( "chest", "right bi" ),
		Chest_LeftBi_LeftFore_LeftHand( "chest", "left bi", "left fore", "left hand" ),
		Chest_LeftBi_LeftFore( "chest", "left bi", "left fore" ),
		Chest_LeftBi( "chest", "left bi" ),
		Chest_Neck_Head_Headdress( "chest", "neck", "head", "headdress" ),
		Chest_Neck_Head( "chest", "neck", "head" ),
		Chest_Neck( "chest", "neck" ),
		Chest_Cape( "chest", "cape" ),
		Chest( "chest" ),
		RightThi_RightShin_RightFoot( "right thi", "right shin", "right foot" ),
		RightThi_RightShin( "right thi", "right shin" ),
		RightThi( "right thi" ),
		LeftThi_LeftShin_LeftFoot( "left thi", "left shin", "left foot" ),
		LeftThi_LeftShin( "left thi", "left shin" ),
		LeftThi( "left thi" );
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
