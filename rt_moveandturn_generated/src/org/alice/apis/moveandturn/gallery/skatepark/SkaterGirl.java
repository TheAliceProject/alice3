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
	
public class SkaterGirl extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SkaterGirl() {
		super( "Skate Park/skater_girl" );
	}
	public enum Part {
		Body_Larm_Lforarm_Lhand( "body", "Larm", "Lforarm", "Lhand" ),
		Body_Larm_Lforarm( "body", "Larm", "Lforarm" ),
		Body_Larm( "body", "Larm" ),
		Body_Rarm_Rforarm_Rhand( "body", "Rarm", "Rforarm", "Rhand" ),
		Body_Rarm_Rforarm( "body", "Rarm", "Rforarm" ),
		Body_Rarm( "body", "Rarm" ),
		Body_Neck_Head_Ponytail( "body", "neck", "head", "ponytail" ),
		Body_Neck_Head( "body", "neck", "head" ),
		Body_Neck( "body", "neck" ),
		Body( "body" ),
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
