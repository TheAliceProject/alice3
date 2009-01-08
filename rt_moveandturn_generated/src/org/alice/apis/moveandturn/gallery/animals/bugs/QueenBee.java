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
	
public class QueenBee extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public QueenBee() {
		super( "Animals/Bugs/QueenBee" );
	}
	public enum Part {
		Chest_RightArm_RightWrist_RightHand( "chest", "right arm", "right wrist", "right hand" ),
		Chest_RightArm_RightWrist( "chest", "right arm", "right wrist" ),
		Chest_RightArm( "chest", "right arm" ),
		Chest_Head_LeftAntenna( "chest", "head", "left antenna" ),
		Chest_Head_RightAntenna( "chest", "head", "right antenna" ),
		Chest_Head_LeftEye( "chest", "head", "left eye" ),
		Chest_Head_RightEye( "chest", "head", "right eye" ),
		Chest_Head_Crown( "chest", "head", "crown" ),
		Chest_Head( "chest", "head" ),
		Chest_LeftArm_LeftWrist_LeftHand( "chest", "left arm", "left wrist", "left hand" ),
		Chest_LeftArm_LeftWrist( "chest", "left arm", "left wrist" ),
		Chest_LeftArm( "chest", "left arm" ),
		Chest_LeftFrontleg( "chest", "left frontleg" ),
		Chest_RightFrontleg( "chest", "right frontleg" ),
		Chest_LeftWing( "chest", "left wing" ),
		Chest_RightWing( "chest", "right wing" ),
		Chest( "chest" ),
		LeftHindleg( "left hindleg" ),
		RightHindleg( "right hindleg" );
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
