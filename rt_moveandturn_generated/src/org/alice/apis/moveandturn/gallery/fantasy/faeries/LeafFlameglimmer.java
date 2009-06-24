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
	
public class LeafFlameglimmer extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public LeafFlameglimmer() {
		super( "Fantasy/Faeries/LeafFlameglimmer" );
	}
	public enum Part {
		ThighL_CalfL_FootL( "ThighL", "CalfL", "FootL" ),
		ThighL_CalfL( "ThighL", "CalfL" ),
		ThighL( "ThighL" ),
		ThighR_CalfR_FootR( "ThighR", "CalfR", "FootR" ),
		ThighR_CalfR( "ThighR", "CalfR" ),
		ThighR( "ThighR" ),
		Abs_Chest_TopWingR( "Abs", "Chest", "TopWingR" ),
		Abs_Chest_BottomWingR( "Abs", "Chest", "BottomWingR" ),
		Abs_Chest_TopWingL( "Abs", "Chest", "TopWingL" ),
		Abs_Chest_BottomWingL( "Abs", "Chest", "BottomWingL" ),
		Abs_Chest_Neck_Head_EyeL( "Abs", "Chest", "Neck", "Head", "EyeL" ),
		Abs_Chest_Neck_Head_EyeR( "Abs", "Chest", "Neck", "Head", "EyeR" ),
		Abs_Chest_Neck_Head_DummyEyeL_LowerLidL( "Abs", "Chest", "Neck", "Head", "DummyEyeL", "LowerLidL" ),
		Abs_Chest_Neck_Head_DummyEyeL_LidL( "Abs", "Chest", "Neck", "Head", "DummyEyeL", "LidL" ),
		Abs_Chest_Neck_Head_DummyEyeL( "Abs", "Chest", "Neck", "Head", "DummyEyeL" ),
		Abs_Chest_Neck_Head_DummyEyeR_LidR( "Abs", "Chest", "Neck", "Head", "DummyEyeR", "LidR" ),
		Abs_Chest_Neck_Head_DummyEyeR_LowerLidR( "Abs", "Chest", "Neck", "Head", "DummyEyeR", "LowerLidR" ),
		Abs_Chest_Neck_Head_DummyEyeR( "Abs", "Chest", "Neck", "Head", "DummyEyeR" ),
		Abs_Chest_Neck_Head_BrowL( "Abs", "Chest", "Neck", "Head", "BrowL" ),
		Abs_Chest_Neck_Head_BrowR( "Abs", "Chest", "Neck", "Head", "BrowR" ),
		Abs_Chest_Neck_Head_EarL( "Abs", "Chest", "Neck", "Head", "EarL" ),
		Abs_Chest_Neck_Head_EaR( "Abs", "Chest", "Neck", "Head", "EaR" ),
		Abs_Chest_Neck_Head( "Abs", "Chest", "Neck", "Head" ),
		Abs_Chest_Neck( "Abs", "Chest", "Neck" ),
		Abs_Chest_SleeveR_UpperarmR_ForearmR_HandR( "Abs", "Chest", "SleeveR", "UpperarmR", "ForearmR", "HandR" ),
		Abs_Chest_SleeveR_UpperarmR_ForearmR( "Abs", "Chest", "SleeveR", "UpperarmR", "ForearmR" ),
		Abs_Chest_SleeveR_UpperarmR( "Abs", "Chest", "SleeveR", "UpperarmR" ),
		Abs_Chest_SleeveR( "Abs", "Chest", "SleeveR" ),
		Abs_Chest_SleeveL_UpperarmL_ForearmL_HandL( "Abs", "Chest", "SleeveL", "UpperarmL", "ForearmL", "HandL" ),
		Abs_Chest_SleeveL_UpperarmL_ForearmL( "Abs", "Chest", "SleeveL", "UpperarmL", "ForearmL" ),
		Abs_Chest_SleeveL_UpperarmL( "Abs", "Chest", "SleeveL", "UpperarmL" ),
		Abs_Chest_SleeveL( "Abs", "Chest", "SleeveL" ),
		Abs_Chest( "Abs", "Chest" ),
		Abs_Clothes( "Abs", "Clothes" ),
		Abs( "Abs" );
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
