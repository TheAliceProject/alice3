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
package org.alice.apis.moveandturn.gallery.egypt;
	
public class Pharoah extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Pharoah() {
		super( "Egypt/Pharoah" );
	}
	public enum Part {
		Body_Rightarm_Rightforearm_RightHand( "body", "rightarm", "rightforearm", "rightHand" ),
		Body_Rightarm_Rightforearm( "body", "rightarm", "rightforearm" ),
		Body_Rightarm( "body", "rightarm" ),
		Body_Leftarm_Leftforearm_LeftHand( "body", "leftarm", "leftforearm", "LeftHand" ),
		Body_Leftarm_Leftforearm( "body", "leftarm", "leftforearm" ),
		Body_Leftarm( "body", "leftarm" ),
		Body_Head_Hat( "body", "head", "hat" ),
		Body_Head_Beard( "body", "head", "beard" ),
		Body_Head_Nose( "body", "head", "nose" ),
		Body_Head( "body", "head" ),
		Body( "body" ),
		Rightleg_Rightshin_Rightfoot( "rightleg", "rightshin", "rightfoot" ),
		Rightleg_Rightshin( "rightleg", "rightshin" ),
		Rightleg( "rightleg" ),
		Leftleg_Leftshin_Leftfoot( "leftleg", "leftshin", "leftfoot" ),
		Leftleg_Leftshin( "leftleg", "leftshin" ),
		Leftleg( "leftleg" );
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
