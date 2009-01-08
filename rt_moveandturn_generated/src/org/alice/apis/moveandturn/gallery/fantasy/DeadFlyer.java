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
package org.alice.apis.moveandturn.gallery.fantasy;
	
public class DeadFlyer extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public DeadFlyer() {
		super( "Fantasy/DeadFlyer" );
	}
	public enum Part {
		Body_Tailseg1_Tailseg2_Tailseg3( "body", "tailseg1", "tailseg2", "tailseg3" ),
		Body_Tailseg1_Tailseg2( "body", "tailseg1", "tailseg2" ),
		Body_Tailseg1( "body", "tailseg1" ),
		Body_Leftarm_Leftjoint_Leftlowerarm( "body", "leftarm", "leftjoint", "leftlowerarm" ),
		Body_Leftarm_Leftjoint( "body", "leftarm", "leftjoint" ),
		Body_Leftarm( "body", "leftarm" ),
		Body_Headstalk_Propeller( "body", "headstalk", "propeller" ),
		Body_Headstalk( "body", "headstalk" ),
		Body_Lefteye( "body", "lefteye" ),
		Body_Mideye( "body", "mideye" ),
		Body( "body" ),
		Rightjoint_Rightlowerarm_Righthand_Rightfingers( "rightjoint", "rightlowerarm", "righthand", "rightfingers" ),
		Rightjoint_Rightlowerarm_Righthand( "rightjoint", "rightlowerarm", "righthand" ),
		Rightjoint_Rightlowerarm( "rightjoint", "rightlowerarm" ),
		Rightjoint( "rightjoint" ),
		Bloodpool( "bloodpool" );
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
