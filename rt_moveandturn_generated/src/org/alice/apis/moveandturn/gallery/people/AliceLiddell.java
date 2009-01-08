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
	
public class AliceLiddell extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public AliceLiddell() {
		super( "People/AliceLiddell" );
	}
	public enum Part {
		RArm_RShoulder( "rArm", "rShoulder" ),
		RArm_RForeArm_RHand_RThumb( "rArm", "rForeArm", "rHand", "rThumb" ),
		RArm_RForeArm_RHand_RMidFinger_REndFinger( "rArm", "rForeArm", "rHand", "rMidFinger", "rEndFinger" ),
		RArm_RForeArm_RHand_RMidFinger( "rArm", "rForeArm", "rHand", "rMidFinger" ),
		RArm_RForeArm_RHand( "rArm", "rForeArm", "rHand" ),
		RArm_RForeArm( "rArm", "rForeArm" ),
		RArm( "rArm" ),
		LArm_LForeArm_LHand_LThumb( "lArm", "lForeArm", "lHand", "lThumb" ),
		LArm_LForeArm_LHand_LMidFinger_LEndFinger( "lArm", "lForeArm", "lHand", "lMidFinger", "lEndFinger" ),
		LArm_LForeArm_LHand_LMidFinger( "lArm", "lForeArm", "lHand", "lMidFinger" ),
		LArm_LForeArm_LHand( "lArm", "lForeArm", "lHand" ),
		LArm_LForeArm( "lArm", "lForeArm" ),
		LArm_LShoulder( "lArm", "lShoulder" ),
		LArm( "lArm" ),
		Neck_Head_Hair( "Neck", "Head", "Hair" ),
		Neck_Head( "Neck", "Head" ),
		Neck( "Neck" ),
		Dress_RThigh_RLeg_RFoot( "Dress", "rThigh", "rLeg", "rFoot" ),
		Dress_RThigh_RLeg( "Dress", "rThigh", "rLeg" ),
		Dress_RThigh( "Dress", "rThigh" ),
		Dress_LThigh_LLeg_LFoot( "Dress", "lThigh", "lLeg", "lFoot" ),
		Dress_LThigh_LLeg( "Dress", "lThigh", "lLeg" ),
		Dress_LThigh( "Dress", "lThigh" ),
		Dress( "Dress" );
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
