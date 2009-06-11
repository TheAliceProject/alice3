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
	
public class Mana extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Mana() {
		super( "People/Mana" );
	}
	public enum Part {
		UpperLegL_LowerLegL_ShoeL( "Upper Leg L", "Lower Leg L", "Shoe L" ),
		UpperLegL_LowerLegL( "Upper Leg L", "Lower Leg L" ),
		UpperLegL( "Upper Leg L" ),
		UpperLegR_LowerLegR_ShoeR( "Upper Leg R", "Lower Leg R", "Shoe R" ),
		UpperLegR_LowerLegR( "Upper Leg R", "Lower Leg R" ),
		UpperLegR( "Upper Leg R" ),
		Torso_UpperArmL_LowerArmL_HandsL( "Torso", "Upper Arm L", "Lower Arm L", "Hands L" ),
		Torso_UpperArmL_LowerArmL( "Torso", "Upper Arm L", "Lower Arm L" ),
		Torso_UpperArmL( "Torso", "Upper Arm L" ),
		Torso_UpperArmR_LowerArmR_HandsR( "Torso", "Upper Arm R", "Lower Arm R", "Hands R" ),
		Torso_UpperArmR_LowerArmR( "Torso", "Upper Arm R", "Lower Arm R" ),
		Torso_UpperArmR( "Torso", "Upper Arm R" ),
		Torso_Dress( "Torso", "Dress" ),
		Torso_Head_Hair( "Torso", "Head", "Hair" ),
		Torso_Head( "Torso", "Head" ),
		Torso( "Torso" );
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
