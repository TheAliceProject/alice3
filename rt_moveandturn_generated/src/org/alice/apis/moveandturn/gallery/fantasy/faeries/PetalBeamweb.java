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
package org.alice.apis.moveandturn.gallery.fantasy.faeries;
	
public class PetalBeamweb extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public PetalBeamweb() {
		super( "Fantasy/Faeries/PetalBeamweb" );
	}
	public enum Part {
		ThighL_CalfL_FootL( "ThighL", "CalfL", "FootL" ),
		ThighL_CalfL( "ThighL", "CalfL" ),
		ThighL( "ThighL" ),
		ThighR_CalfR_FootR( "ThighR", "CalfR", "FootR" ),
		ThighR_CalfR( "ThighR", "CalfR" ),
		ThighR( "ThighR" ),
		Abs_Chest_TopwingR( "abs", "Chest", "TopwingR" ),
		Abs_Chest_BottomwingR( "abs", "Chest", "BottomwingR" ),
		Abs_Chest_TopwingL( "abs", "Chest", "TopwingL" ),
		Abs_Chest_BottomwingL( "abs", "Chest", "BottomwingL" ),
		Abs_Chest_Neck_Head_EyeL( "abs", "Chest", "Neck", "Head", "EyeL" ),
		Abs_Chest_Neck_Head_EyeR( "abs", "Chest", "Neck", "Head", "EyeR" ),
		Abs_Chest_Neck_Head_DummyEyeL_LowerLidL( "abs", "Chest", "Neck", "Head", "DummyEyeL", "LowerLidL" ),
		Abs_Chest_Neck_Head_DummyEyeL_LidL( "abs", "Chest", "Neck", "Head", "DummyEyeL", "LidL" ),
		Abs_Chest_Neck_Head_DummyEyeL( "abs", "Chest", "Neck", "Head", "DummyEyeL" ),
		Abs_Chest_Neck_Head_DummyEyeR_LidR( "abs", "Chest", "Neck", "Head", "DummyEyeR", "LidR" ),
		Abs_Chest_Neck_Head_DummyEyeR_LowerLidR( "abs", "Chest", "Neck", "Head", "DummyEyeR", "LowerLidR" ),
		Abs_Chest_Neck_Head_DummyEyeR( "abs", "Chest", "Neck", "Head", "DummyEyeR" ),
		Abs_Chest_Neck_Head_BrowL( "abs", "Chest", "Neck", "Head", "BrowL" ),
		Abs_Chest_Neck_Head_BrowR( "abs", "Chest", "Neck", "Head", "BrowR" ),
		Abs_Chest_Neck_Head_EarL( "abs", "Chest", "Neck", "Head", "EarL" ),
		Abs_Chest_Neck_Head_EarR( "abs", "Chest", "Neck", "Head", "EarR" ),
		Abs_Chest_Neck_Head( "abs", "Chest", "Neck", "Head" ),
		Abs_Chest_Neck( "abs", "Chest", "Neck" ),
		Abs_Chest_SleeveR_UpperarmR_ForearmR_HandR( "abs", "Chest", "SleeveR", "UpperarmR", "ForearmR", "HandR" ),
		Abs_Chest_SleeveR_UpperarmR_ForearmR( "abs", "Chest", "SleeveR", "UpperarmR", "ForearmR" ),
		Abs_Chest_SleeveR_UpperarmR( "abs", "Chest", "SleeveR", "UpperarmR" ),
		Abs_Chest_SleeveR( "abs", "Chest", "SleeveR" ),
		Abs_Chest_SleeveL_UpperarmL_ForearmL_HandL( "abs", "Chest", "SleeveL", "UpperarmL", "ForearmL", "HandL" ),
		Abs_Chest_SleeveL_UpperarmL_ForearmL( "abs", "Chest", "SleeveL", "UpperarmL", "ForearmL" ),
		Abs_Chest_SleeveL_UpperarmL( "abs", "Chest", "SleeveL", "UpperarmL" ),
		Abs_Chest_SleeveL( "abs", "Chest", "SleeveL" ),
		Abs_Chest( "abs", "Chest" ),
		Abs_Clothes( "abs", "Clothes" ),
		Abs( "abs" );
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
