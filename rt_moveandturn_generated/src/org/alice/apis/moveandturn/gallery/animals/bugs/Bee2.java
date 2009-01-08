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
	
public class Bee2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Bee2() {
		super( "Animals/Bugs/Bee2" );
	}
	public enum Part {
		Body_LeftHindleg( "body", "left hindleg" ),
		Body_RightHindleg( "body", "right hindleg" ),
		Body( "body" ),
		Chest_LeftFrontleg( "chest", "left frontleg" ),
		Chest_RightFrontleg( "chest", "right frontleg" ),
		Chest_Head_RightEye( "chest", "head", "right eye" ),
		Chest_Head_LeftEye( "chest", "head", "left eye" ),
		Chest_Head_Helm( "chest", "head", "helm" ),
		Chest_Head_RightAntenna( "chest", "head", "right antenna" ),
		Chest_Head_LeftAntenna( "chest", "head", "left antenna" ),
		Chest_Head( "chest", "head" ),
		Chest_RightWing( "chest", "right wing" ),
		Chest_LeftWing( "chest", "left wing" ),
		Chest_LeftArm_LeftForearm_LeftHand( "chest", "left arm", "left forearm", "left hand" ),
		Chest_LeftArm_LeftForearm( "chest", "left arm", "left forearm" ),
		Chest_LeftArm( "chest", "left arm" ),
		Chest_RightArm_RightForearm_RightHand_Spear( "chest", "right arm", "right forearm", "right hand", "spear" ),
		Chest_RightArm_RightForearm_RightHand( "chest", "right arm", "right forearm", "right hand" ),
		Chest_RightArm_RightForearm( "chest", "right arm", "right forearm" ),
		Chest_RightArm( "chest", "right arm" ),
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
