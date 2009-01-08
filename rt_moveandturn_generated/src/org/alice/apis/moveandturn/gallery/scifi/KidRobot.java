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
	
public class KidRobot extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public KidRobot() {
		super( "SciFi/kidRobot" );
	}
	public enum Part {
		Torso_RightArm_RightForeArm_Hand( "torso", "rightArm", "rightForeArm", "hand" ),
		Torso_RightArm_RightForeArm( "torso", "rightArm", "rightForeArm" ),
		Torso_RightArm( "torso", "rightArm" ),
		Torso_LeftArm_LeftForeArm_LeftWrist_Claw2( "torso", "leftArm", "leftForeArm", "leftWrist", "claw2" ),
		Torso_LeftArm_LeftForeArm_LeftWrist_Claw1( "torso", "leftArm", "leftForeArm", "leftWrist", "claw1" ),
		Torso_LeftArm_LeftForeArm_LeftWrist( "torso", "leftArm", "leftForeArm", "leftWrist" ),
		Torso_LeftArm_LeftForeArm( "torso", "leftArm", "leftForeArm" ),
		Torso_LeftArm( "torso", "leftArm" ),
		Torso_Neck_Head_Tube1( "torso", "Neck", "head", "tube1" ),
		Torso_Neck_Head_Tube2( "torso", "Neck", "head", "tube2" ),
		Torso_Neck_Head_Tube3( "torso", "Neck", "head", "tube3" ),
		Torso_Neck_Head_Nose( "torso", "Neck", "head", "nose" ),
		Torso_Neck_Head_Lens( "torso", "Neck", "head", "lens" ),
		Torso_Neck_Head( "torso", "Neck", "head" ),
		Torso_Neck( "torso", "Neck" ),
		Torso( "torso" ),
		Bottom( "bottom" );
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
