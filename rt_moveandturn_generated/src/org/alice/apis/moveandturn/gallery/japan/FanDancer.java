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
package org.alice.apis.moveandturn.gallery.japan;
	
public class FanDancer extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public FanDancer() {
		super( "Japan/FanDancer" );
	}
	public enum Part {
		Body_Larm_LforeArm_Rhand01_LFan( "body", "Larm", "LforeArm", "Rhand01", "LFan" ),
		Body_Larm_LforeArm_Rhand01( "body", "Larm", "LforeArm", "Rhand01" ),
		Body_Larm_LforeArm( "body", "Larm", "LforeArm" ),
		Body_Larm( "body", "Larm" ),
		Body_Rarm_RforeArm_Rhand_RFan( "body", "Rarm", "RforeArm", "Rhand", "RFan" ),
		Body_Rarm_RforeArm_Rhand( "body", "Rarm", "RforeArm", "Rhand" ),
		Body_Rarm_RforeArm( "body", "Rarm", "RforeArm" ),
		Body_Rarm( "body", "Rarm" ),
		Body_Neck_Head( "body", "neck", "head" ),
		Body_Neck( "body", "neck" ),
		Body( "body" );
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
