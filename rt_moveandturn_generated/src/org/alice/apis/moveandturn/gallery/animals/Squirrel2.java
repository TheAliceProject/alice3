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
	
public class Squirrel2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Squirrel2() {
		super( "Animals/squirrel2" );
	}
	public enum Part {
		Tail( "tail" ),
		LeftLeg_LeftFoot( "Left_leg", "Left_foot" ),
		LeftLeg( "Left_leg" ),
		RightLeg_RightFoot( "Right_leg", "Right_foot" ),
		RightLeg( "Right_leg" ),
		LeftArm_LeftHand( "Left_arm", "Left_hand" ),
		LeftArm( "Left_arm" ),
		RightArm_RightHand( "Right_arm", "Right_hand" ),
		RightArm( "Right_arm" ),
		Head_Mouth( "head", "mouth" ),
		Head_Righttooth( "head", "righttooth" ),
		Head_Lefttooth( "head", "lefttooth" ),
		Head_Leftear( "head", "leftear" ),
		Head_Rightear( "head", "rightear" ),
		Head( "head" );
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
