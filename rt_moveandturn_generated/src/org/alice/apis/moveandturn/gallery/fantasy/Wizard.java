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
	
public class Wizard extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Wizard() {
		super( "Fantasy/wizard" );
	}
	public enum Part {
		Torso_Neck_Head( "torso", "neck", "head" ),
		Torso_Neck( "torso", "neck" ),
		Torso_RightArm_Forearm_Hand( "torso", "right arm", "forearm", "hand" ),
		Torso_RightArm_Forearm( "torso", "right arm", "forearm" ),
		Torso_RightArm( "torso", "right arm" ),
		Torso_LeftArm_Forearm_Hand( "torso", "left arm", "forearm", "hand" ),
		Torso_LeftArm_Forearm( "torso", "left arm", "forearm" ),
		Torso_LeftArm( "torso", "left arm" ),
		Torso_TopCape_BottomCape( "torso", "top cape", "bottom cape" ),
		Torso_TopCape( "torso", "top cape" ),
		Torso( "torso" );
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
