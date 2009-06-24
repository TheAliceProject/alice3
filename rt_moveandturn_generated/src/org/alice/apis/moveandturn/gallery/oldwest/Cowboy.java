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
	
public class Cowboy extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Cowboy() {
		super( "Old West/Cowboy" );
	}
	public enum Part {
		RLeg_Rshin_RFoot( "RLeg", "Rshin", "RFoot" ),
		RLeg_Rshin( "RLeg", "Rshin" ),
		RLeg( "RLeg" ),
		LLeg_LShin_Lfoot( "LLeg", "LShin", "Lfoot" ),
		LLeg_LShin( "LLeg", "LShin" ),
		LLeg( "LLeg" ),
		Chest_Jacket_Larm_LForArm_LHand( "Chest", "jacket", "Larm", "LForArm", "LHand" ),
		Chest_Jacket_Larm_LForArm( "Chest", "jacket", "Larm", "LForArm" ),
		Chest_Jacket_Larm( "Chest", "jacket", "Larm" ),
		Chest_Jacket_RArm_RForArm_RHand( "Chest", "jacket", "RArm", "RForArm", "RHand" ),
		Chest_Jacket_RArm_RForArm( "Chest", "jacket", "RArm", "RForArm" ),
		Chest_Jacket_RArm( "Chest", "jacket", "RArm" ),
		Chest_Jacket( "Chest", "jacket" ),
		Chest_Neck_Head_Hat( "Chest", "neck", "Head", "hat" ),
		Chest_Neck_Head_REye( "Chest", "neck", "Head", "REye" ),
		Chest_Neck_Head_LEye( "Chest", "neck", "Head", "LEye" ),
		Chest_Neck_Head( "Chest", "neck", "Head" ),
		Chest_Neck( "Chest", "neck" ),
		Chest( "Chest" );
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
