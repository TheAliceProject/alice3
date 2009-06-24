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
package org.alice.apis.moveandturn.gallery.textbookdemos.wandatemp;
	
public class EvilStepSister2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public EvilStepSister2() {
		super( "Textbook Demos/Wanda Temp/EvilStepSister2" );
	}
	public enum Part {
		Stomach_Torso_Face_Hair( "Stomach", "Torso", "Face", "Hair" ),
		Stomach_Torso_Face_RightEar( "Stomach", "Torso", "Face", "RightEar" ),
		Stomach_Torso_Face_LeftEar( "Stomach", "Torso", "Face", "LeftEar" ),
		Stomach_Torso_Face_Nose( "Stomach", "Torso", "Face", "Nose" ),
		Stomach_Torso_Face( "Stomach", "Torso", "Face" ),
		Stomach_Torso_RightUpperArm_RightLowerArm_RightHand( "Stomach", "Torso", "RightUpperArm", "RightLowerArm", "RightHand" ),
		Stomach_Torso_RightUpperArm_RightLowerArm( "Stomach", "Torso", "RightUpperArm", "RightLowerArm" ),
		Stomach_Torso_RightUpperArm( "Stomach", "Torso", "RightUpperArm" ),
		Stomach_Torso_LeftUpperArm_LeftLowerArm_LeftHand( "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm", "LeftHand" ),
		Stomach_Torso_LeftUpperArm_LeftLowerArm( "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm" ),
		Stomach_Torso_LeftUpperArm( "Stomach", "Torso", "LeftUpperArm" ),
		Stomach_Torso( "Stomach", "Torso" ),
		Stomach_Skirt( "Stomach", "Skirt" ),
		Stomach_RightThigh_RightCalf_RightFoot( "Stomach", "RightThigh", "RightCalf", "RightFoot" ),
		Stomach_RightThigh_RightCalf( "Stomach", "RightThigh", "RightCalf" ),
		Stomach_RightThigh( "Stomach", "RightThigh" ),
		Stomach_LeftThigh_LeftCalf_LeftFoot( "Stomach", "LeftThigh", "LeftCalf", "LeftFoot" ),
		Stomach_LeftThigh_LeftCalf( "Stomach", "LeftThigh", "LeftCalf" ),
		Stomach_LeftThigh( "Stomach", "LeftThigh" ),
		Stomach( "Stomach" );
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
