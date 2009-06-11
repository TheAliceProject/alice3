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
package org.alice.apis.moveandturn.gallery.spooky;
	
public class Zombie extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Zombie() {
		super( "Spooky/zombie" );
	}
	public enum Part {
		Pelvis_Chest_Rarm_Rforarm_Rhand( "pelvis", "chest", "Rarm", "Rforarm", "Rhand" ),
		Pelvis_Chest_Rarm_Rforarm( "pelvis", "chest", "Rarm", "Rforarm" ),
		Pelvis_Chest_Rarm( "pelvis", "chest", "Rarm" ),
		Pelvis_Chest_Larm_LForarm_Lhand( "pelvis", "chest", "Larm", "LForarm", "Lhand" ),
		Pelvis_Chest_Larm_LForarm( "pelvis", "chest", "Larm", "LForarm" ),
		Pelvis_Chest_Larm( "pelvis", "chest", "Larm" ),
		Pelvis_Chest_Neck_Head_TopTeeth( "pelvis", "chest", "neck", "head", "TopTeeth" ),
		Pelvis_Chest_Neck_Head_BottomTeeth( "pelvis", "chest", "neck", "head", "BottomTeeth" ),
		Pelvis_Chest_Neck_Head( "pelvis", "chest", "neck", "head" ),
		Pelvis_Chest_Neck( "pelvis", "chest", "neck" ),
		Pelvis_Chest( "pelvis", "chest" ),
		Pelvis_RLeg_Rshin_Lfoot01( "pelvis", "rLeg", "rshin", "Lfoot01" ),
		Pelvis_RLeg_Rshin( "pelvis", "rLeg", "rshin" ),
		Pelvis_RLeg( "pelvis", "rLeg" ),
		Pelvis_LLeg_Lshin_Lfoot02( "pelvis", "lLeg", "Lshin", "Lfoot02" ),
		Pelvis_LLeg_Lshin( "pelvis", "lLeg", "Lshin" ),
		Pelvis_LLeg( "pelvis", "lLeg" ),
		Pelvis( "pelvis" );
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
