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
package org.alice.apis.moveandturn.gallery.animals;
	
public class Squirrel extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Squirrel() {
		super( "Animals/Squirrel" );
	}
	public enum Part {
		Chest_RBicep_RForearm_RHand( "Chest", "RBicep", "RForearm", "RHand" ),
		Chest_RBicep_RForearm( "Chest", "RBicep", "RForearm" ),
		Chest_RBicep( "Chest", "RBicep" ),
		Chest_LBicep_LForearm_LHand( "Chest", "LBicep", "LForearm", "LHand" ),
		Chest_LBicep_LForearm( "Chest", "LBicep", "LForearm" ),
		Chest_LBicep( "Chest", "LBicep" ),
		Chest_Head_REar( "Chest", "Head", "REar" ),
		Chest_Head_LEar( "Chest", "Head", "LEar" ),
		Chest_Head_Mouth( "Chest", "Head", "Mouth" ),
		Chest_Head( "Chest", "Head" ),
		Chest( "Chest" ),
		TailBase_Tail2_Tail3_TailTip( "TailBase", "Tail2", "Tail3", "TailTip" ),
		TailBase_Tail2_Tail3( "TailBase", "Tail2", "Tail3" ),
		TailBase_Tail2( "TailBase", "Tail2" ),
		TailBase( "TailBase" ),
		LHip_LThigh_LCalf_LFoot( "LHip", "LThigh", "LCalf", "LFoot" ),
		LHip_LThigh_LCalf( "LHip", "LThigh", "LCalf" ),
		LHip_LThigh( "LHip", "LThigh" ),
		LHip( "LHip" ),
		RHip_RThigh_RCalf_RFoot( "RHip", "RThigh", "RCalf", "RFoot" ),
		RHip_RThigh_RCalf( "RHip", "RThigh", "RCalf" ),
		RHip_RThigh( "RHip", "RThigh" ),
		RHip( "RHip" );
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
