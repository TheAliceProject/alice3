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
package org.alice.apis.moveandturn.gallery.scifi;
	
public class KoalaRobot extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public KoalaRobot() {
		super( "SciFi/koala_robot" );
	}
	public enum Part {
		Body_RightLeg( "Body", "RightLeg" ),
		Body_LeftLeg( "Body", "LeftLeg" ),
		Body_RightArm_RightPaw( "Body", "RightArm", "RightPaw" ),
		Body_RightArm( "Body", "RightArm" ),
		Body_LeftArm_LeftPaw( "Body", "LeftArm", "LeftPaw" ),
		Body_LeftArm( "Body", "LeftArm" ),
		Body_Head_LeftEar( "Body", "Head", "LeftEar" ),
		Body_Head_RightEar( "Body", "Head", "RightEar" ),
		Body_Head_Nose( "Body", "Head", "Nose" ),
		Body_Head( "Body", "Head" ),
		Body( "Body" ),
		Hovercraft_RightJet( "Hovercraft", "RightJet" ),
		Hovercraft_LeftJet( "Hovercraft", "LeftJet" ),
		Hovercraft_SteeringStick_SteeringSphere( "Hovercraft", "SteeringStick", "SteeringSphere" ),
		Hovercraft_SteeringStick( "Hovercraft", "SteeringStick" ),
		Hovercraft( "Hovercraft" );
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
