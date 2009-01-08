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
	
public class Sherrif extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Sherrif() {
		super( "Old West/sherrif" );
	}
	public enum Part {
		Belt_Sling( "belt", "sling" ),
		Belt( "belt" ),
		LLeg_LShin_Lfoot( "LLeg", "LShin", "Lfoot" ),
		LLeg_LShin( "LLeg", "LShin" ),
		LLeg( "LLeg" ),
		RLeg_Rshin_RFoot( "RLeg", "Rshin", "RFoot" ),
		RLeg_Rshin( "RLeg", "Rshin" ),
		RLeg( "RLeg" ),
		Chest_LArm_LForarm_LHand( "chest", "LArm", "LForarm", "LHand" ),
		Chest_LArm_LForarm( "chest", "LArm", "LForarm" ),
		Chest_LArm( "chest", "LArm" ),
		Chest_Neck_Head_Hat( "chest", "neck", "head", "hat" ),
		Chest_Neck_Head( "chest", "neck", "head" ),
		Chest_Neck( "chest", "neck" ),
		Chest_RArm_RForarm_RHand( "chest", "RArm", "RForarm", "RHand" ),
		Chest_RArm_RForarm( "chest", "RArm", "RForarm" ),
		Chest_RArm( "chest", "RArm" ),
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
