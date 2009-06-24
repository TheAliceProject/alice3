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
	
public class StepMother extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public StepMother() {
		super( "Textbook Demos/Wanda Temp/StepMother" );
	}
	public enum Part {
		Hips_Stomach_Torso_Face_Hair_Bun( "Hips", "Stomach", "Torso", "Face", "Hair", "Bun" ),
		Hips_Stomach_Torso_Face_Hair( "Hips", "Stomach", "Torso", "Face", "Hair" ),
		Hips_Stomach_Torso_Face_LeftEar( "Hips", "Stomach", "Torso", "Face", "LeftEar" ),
		Hips_Stomach_Torso_Face_RightEar( "Hips", "Stomach", "Torso", "Face", "RightEar" ),
		Hips_Stomach_Torso_Face_Nose( "Hips", "Stomach", "Torso", "Face", "Nose" ),
		Hips_Stomach_Torso_Face( "Hips", "Stomach", "Torso", "Face" ),
		Hips_Stomach_Torso_RightUpperArm_RightLowerArm_RightHand( "Hips", "Stomach", "Torso", "RightUpperArm", "RightLowerArm", "RightHand" ),
		Hips_Stomach_Torso_RightUpperArm_RightLowerArm( "Hips", "Stomach", "Torso", "RightUpperArm", "RightLowerArm" ),
		Hips_Stomach_Torso_RightUpperArm( "Hips", "Stomach", "Torso", "RightUpperArm" ),
		Hips_Stomach_Torso_LeftUpperArm_LeftLowerArm_LeftHand( "Hips", "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm", "LeftHand" ),
		Hips_Stomach_Torso_LeftUpperArm_LeftLowerArm( "Hips", "Stomach", "Torso", "LeftUpperArm", "LeftLowerArm" ),
		Hips_Stomach_Torso_LeftUpperArm( "Hips", "Stomach", "Torso", "LeftUpperArm" ),
		Hips_Stomach_Torso( "Hips", "Stomach", "Torso" ),
		Hips_Stomach( "Hips", "Stomach" ),
		Hips_Skirt( "Hips", "Skirt" ),
		Hips_LeftThigh_LeftCalf_LeftFoot_LeftShoe( "Hips", "LeftThigh", "LeftCalf", "LeftFoot", "LeftShoe" ),
		Hips_LeftThigh_LeftCalf_LeftFoot( "Hips", "LeftThigh", "LeftCalf", "LeftFoot" ),
		Hips_LeftThigh_LeftCalf( "Hips", "LeftThigh", "LeftCalf" ),
		Hips_LeftThigh( "Hips", "LeftThigh" ),
		Hips_RightThigh_RightCalf_RightFoot_RightShoe( "Hips", "RightThigh", "RightCalf", "RightFoot", "RightShoe" ),
		Hips_RightThigh_RightCalf_RightFoot( "Hips", "RightThigh", "RightCalf", "RightFoot" ),
		Hips_RightThigh_RightCalf( "Hips", "RightThigh", "RightCalf" ),
		Hips_RightThigh( "Hips", "RightThigh" ),
		Hips( "Hips" );
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
