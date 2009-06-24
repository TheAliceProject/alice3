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
	
public class RandomGirl3 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public RandomGirl3() {
		super( "People/RandomGirl3" );
	}
	public enum Part {
		LeftShoulder_LeftElbow_LeftArm_LeftHand( "LeftShoulder", "LeftElbow", "LeftArm", "LeftHand" ),
		LeftShoulder_LeftElbow_LeftArm( "LeftShoulder", "LeftElbow", "LeftArm" ),
		LeftShoulder_LeftElbow( "LeftShoulder", "LeftElbow" ),
		LeftShoulder( "LeftShoulder" ),
		RightShoulder_RightElbow_RightArm_RightHand( "RightShoulder", "RightElbow", "RightArm", "RightHand" ),
		RightShoulder_RightElbow_RightArm( "RightShoulder", "RightElbow", "RightArm" ),
		RightShoulder_RightElbow( "RightShoulder", "RightElbow" ),
		RightShoulder( "RightShoulder" ),
		Pelvis_RightPantLeg_RightShin_RightFoot( "pelvis", "RightPantLeg", "RightShin", "RightFoot" ),
		Pelvis_RightPantLeg_RightShin( "pelvis", "RightPantLeg", "RightShin" ),
		Pelvis_RightPantLeg( "pelvis", "RightPantLeg" ),
		Pelvis_LeftPantLeg_LeftShin_LeftFoot( "pelvis", "LeftPantLeg", "LeftShin", "LeftFoot" ),
		Pelvis_LeftPantLeg_LeftShin( "pelvis", "LeftPantLeg", "LeftShin" ),
		Pelvis_LeftPantLeg( "pelvis", "LeftPantLeg" ),
		Pelvis( "pelvis" ),
		Head_Ponytail( "Head", "ponytail" ),
		Head( "Head" );
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
