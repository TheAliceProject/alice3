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
	
public class DJ extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public DJ() {
		super( "People/DJ" );
	}
	public enum Part {
		LeftPantLeg_LeftKnee_LeftShin_LeftFoot( "LeftPantLeg", "LeftKnee", "LeftShin", "LeftFoot" ),
		LeftPantLeg_LeftKnee_LeftShin( "LeftPantLeg", "LeftKnee", "LeftShin" ),
		LeftPantLeg_LeftKnee( "LeftPantLeg", "LeftKnee" ),
		LeftPantLeg( "LeftPantLeg" ),
		RightPantLeg_RightKnee_RightShin_RightFoot( "RightPantLeg", "RightKnee", "RightShin", "RightFoot" ),
		RightPantLeg_RightKnee_RightShin( "RightPantLeg", "RightKnee", "RightShin" ),
		RightPantLeg_RightKnee( "RightPantLeg", "RightKnee" ),
		RightPantLeg( "RightPantLeg" ),
		Chest_LeftShoulder_LeftElbow_LeftArm_LeftHand( "chest", "LeftShoulder", "LeftElbow", "LeftArm", "LeftHand" ),
		Chest_LeftShoulder_LeftElbow_LeftArm( "chest", "LeftShoulder", "LeftElbow", "LeftArm" ),
		Chest_LeftShoulder_LeftElbow( "chest", "LeftShoulder", "LeftElbow" ),
		Chest_LeftShoulder( "chest", "LeftShoulder" ),
		Chest_RightShoulder_RightElbow_RightArm_RightHand( "chest", "RightShoulder", "RightElbow", "RightArm", "RightHand" ),
		Chest_RightShoulder_RightElbow_RightArm( "chest", "RightShoulder", "RightElbow", "RightArm" ),
		Chest_RightShoulder_RightElbow( "chest", "RightShoulder", "RightElbow" ),
		Chest_RightShoulder( "chest", "RightShoulder" ),
		Chest_Head( "chest", "head" ),
		Chest( "chest" );
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
