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
package org.alice.apis.moveandturn.gallery.skatepark;
	
public class SkaterGuy extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SkaterGuy() {
		super( "Skate Park/skater_guy" );
	}
	public enum Part {
		Chest_Larm_LForarm_Lhand( "chest", "Larm", "LForarm", "Lhand" ),
		Chest_Larm_LForarm( "chest", "Larm", "LForarm" ),
		Chest_Larm( "chest", "Larm" ),
		Chest_Rarm_RForarm_Rhand( "chest", "Rarm", "RForarm", "Rhand" ),
		Chest_Rarm_RForarm( "chest", "Rarm", "RForarm" ),
		Chest_Rarm( "chest", "Rarm" ),
		Chest_Neck_Head( "chest", "neck", "head" ),
		Chest_Neck( "chest", "neck" ),
		Chest( "chest" ),
		Lleg_Lshin_Lfoot( "Lleg", "Lshin", "Lfoot" ),
		Lleg_Lshin( "Lleg", "Lshin" ),
		Lleg( "Lleg" ),
		Rleg_Rshin_Rfoot( "Rleg", "Rshin", "rfoot" ),
		Rleg_Rshin( "Rleg", "Rshin" ),
		Rleg( "Rleg" );
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
