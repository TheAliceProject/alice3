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
package org.alice.apis.moveandturn.gallery.people;
	
public class Roommate extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Roommate() {
		super( "People/Roommate" );
	}
	public enum Part {
		RMLeftUpperArm_RMRightLowerArm01_RMRightHand01( "RMLeftUpperArm", "RMRightLowerArm01", "RMRightHand01" ),
		RMLeftUpperArm_RMRightLowerArm01( "RMLeftUpperArm", "RMRightLowerArm01" ),
		RMLeftUpperArm( "RMLeftUpperArm" ),
		RMRightUpperArm_RMRightLowerArm_RMRightHand( "RMRightUpperArm", "RMRightLowerArm", "RMRightHand" ),
		RMRightUpperArm_RMRightLowerArm( "RMRightUpperArm", "RMRightLowerArm" ),
		RMRightUpperArm( "RMRightUpperArm" ),
		RMHead01( "RMHead01" ),
		RMRightThigh_RMRightLowerLeg_RMRightFoot( "RMRightThigh", "RMRightLowerLeg", "RMRightFoot" ),
		RMRightThigh_RMRightLowerLeg( "RMRightThigh", "RMRightLowerLeg" ),
		RMRightThigh( "RMRightThigh" ),
		RMLeftThigh_RMRightLowerLeg01_RMRightFoot01( "RMLeftThigh", "RMRightLowerLeg01", "RMRightFoot01" ),
		RMLeftThigh_RMRightLowerLeg01( "RMLeftThigh", "RMRightLowerLeg01" ),
		RMLeftThigh( "RMLeftThigh" );
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
