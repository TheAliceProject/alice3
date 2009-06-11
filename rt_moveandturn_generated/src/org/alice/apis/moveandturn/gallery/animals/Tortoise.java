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
	
public class Tortoise extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Tortoise() {
		super( "Animals/Tortoise" );
	}
	public enum Part {
		RightLeg_RightShin_RightFoot( "RightLeg", "RightShin", "RightFoot" ),
		RightLeg_RightShin( "RightLeg", "RightShin" ),
		RightLeg( "RightLeg" ),
		LeftLeg_LeftShin_LeftFoot( "LeftLeg", "LeftShin", "LeftFoot" ),
		LeftLeg_LeftShin( "LeftLeg", "LeftShin" ),
		LeftLeg( "LeftLeg" ),
		LeftArm_LeftForearm( "LeftArm", "LeftForearm" ),
		LeftArm( "LeftArm" ),
		RightArm_RightForearm( "RightArm", "RightForearm" ),
		RightArm( "RightArm" ),
		Neck_Head_Upperjaw( "neck", "head", "upperjaw" ),
		Neck_Head_Lowerjaw( "neck", "head", "lowerjaw" ),
		Neck_Head_Headband( "neck", "head", "headband" ),
		Neck_Head( "neck", "head" ),
		Neck( "neck" );
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
