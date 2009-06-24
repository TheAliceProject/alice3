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
package org.alice.apis.moveandturn.gallery.fantasy;
	
public class Dragon extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Dragon() {
		super( "Fantasy/dragon" );
	}
	public enum Part {
		Neck_Head_LowerJaw( "neck", "head", "lower jaw" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" ),
		LeftArm_LeftForearm_LeftPaw_LeftPawClaw1( "left arm", "left forearm", "left paw", "left paw claw 1" ),
		LeftArm_LeftForearm_LeftPaw_LeftPawClaw2( "left arm", "left forearm", "left paw", "left paw claw 2" ),
		LeftArm_LeftForearm_LeftPaw( "left arm", "left forearm", "left paw" ),
		LeftArm_LeftForearm( "left arm", "left forearm" ),
		LeftArm( "left arm" ),
		RightArm_RightForearm_RightPaw_RightPawClaw2( "right arm", "right forearm", "right paw", "right paw claw 2" ),
		RightArm_RightForearm_RightPaw_RightPawClaw1( "right arm", "right forearm", "right paw", "right paw claw 1" ),
		RightArm_RightForearm_RightPaw( "right arm", "right forearm", "right paw" ),
		RightArm_RightForearm( "right arm", "right forearm" ),
		RightArm( "right arm" ),
		LeftLeg_LeftLowerLeg_LeftFoot_LeftFootClaw1( "left leg", "left lower leg", "left foot", "left foot claw 1" ),
		LeftLeg_LeftLowerLeg_LeftFoot_LeftFootClaw2( "left leg", "left lower leg", "left foot", "left foot claw 2" ),
		LeftLeg_LeftLowerLeg_LeftFoot( "left leg", "left lower leg", "left foot" ),
		LeftLeg_LeftLowerLeg( "left leg", "left lower leg" ),
		LeftLeg( "left leg" ),
		Taill( "taill" ),
		RightLeg_RightLowerLeg_RightFoot_RightFootClaw1( "right leg", "right lower leg", "right foot", "right foot claw 1" ),
		RightLeg_RightLowerLeg_RightFoot_RightFootClaw2( "right leg", "right lower leg", "right foot", "right foot claw 2" ),
		RightLeg_RightLowerLeg_RightFoot( "right leg", "right lower leg", "right foot" ),
		RightLeg_RightLowerLeg( "right leg", "right lower leg" ),
		RightLeg( "right leg" ),
		LeftWing_LeftWingSinew( "left wing", "left wing sinew" ),
		LeftWing( "left wing" ),
		RightWing_RightWingSinew( "right wing", "right wing sinew" ),
		RightWing( "right wing" );
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
