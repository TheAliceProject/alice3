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
package org.alice.apis.moveandturn.gallery.japan;
	
public class Samurai extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Samurai() {
		super( "Japan/Samurai" );
	}
	public enum Part {
		Lleg_Lshin_Lfoot( "Lleg", "Lshin", "Lfoot" ),
		Lleg_Lshin( "Lleg", "Lshin" ),
		Lleg( "Lleg" ),
		Rleg_Rshin_Rfoot( "Rleg", "Rshin", "Rfoot" ),
		Rleg_Rshin( "Rleg", "Rshin" ),
		Rleg( "Rleg" ),
		Body_Belt( "body", "belt" ),
		Body_Head_Helmet_Mask( "body", "head", "helmet", "mask" ),
		Body_Head_Helmet( "body", "head", "helmet" ),
		Body_Head( "body", "head" ),
		Body_Larm_Lforearm_Lhand( "body", "Larm", "Lforearm", "Lhand" ),
		Body_Larm_Lforearm( "body", "Larm", "Lforearm" ),
		Body_Larm( "body", "Larm" ),
		Body_Rarm_Rforearm_Rhand( "body", "Rarm", "Rforearm", "Rhand" ),
		Body_Rarm_Rforearm( "body", "Rarm", "Rforearm" ),
		Body_Rarm( "body", "Rarm" ),
		Body( "body" );
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
