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
package org.alice.apis.moveandturn.gallery.medieval;
	
public class Crispy extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Crispy() {
		super( "Medieval/crispy" );
	}
	public enum Part {
		RightThigh_RightKnee_RightGreave_RightSabaton( "rightThigh", "rightKnee", "rightGreave", "rightSabaton" ),
		RightThigh_RightKnee_RightGreave( "rightThigh", "rightKnee", "rightGreave" ),
		RightThigh_RightKnee( "rightThigh", "rightKnee" ),
		RightThigh( "rightThigh" ),
		LeftThigh_LeftKnee_LeftGreave_LeftSabaton( "leftThigh", "leftKnee", "leftGreave", "leftSabaton" ),
		LeftThigh_LeftKnee_LeftGreave( "leftThigh", "leftKnee", "leftGreave" ),
		LeftThigh_LeftKnee( "leftThigh", "leftKnee" ),
		LeftThigh( "leftThigh" ),
		Cuirass_Head( "cuirass", "Head" ),
		Cuirass_RightUpperArm_RightCouter_RightForearm_RightHand( "cuirass", "rightUpperArm", "rightCouter", "rightForearm", "rightHand" ),
		Cuirass_RightUpperArm_RightCouter_RightForearm( "cuirass", "rightUpperArm", "rightCouter", "rightForearm" ),
		Cuirass_RightUpperArm_RightCouter( "cuirass", "rightUpperArm", "rightCouter" ),
		Cuirass_RightUpperArm( "cuirass", "rightUpperArm" ),
		Cuirass_LeftUpperArm_LeftCouter_LeftForearm_LeftHand( "cuirass", "leftUpperArm", "leftCouter", "leftForearm", "leftHand" ),
		Cuirass_LeftUpperArm_LeftCouter_LeftForearm( "cuirass", "leftUpperArm", "leftCouter", "leftForearm" ),
		Cuirass_LeftUpperArm_LeftCouter( "cuirass", "leftUpperArm", "leftCouter" ),
		Cuirass_LeftUpperArm( "cuirass", "leftUpperArm" ),
		Cuirass( "cuirass" );
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
