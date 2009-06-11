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
	
public class Lemur extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Lemur() {
		super( "Animals/lemur" );
	}
	public enum Part {
		RUpperarm_RForearm_RHand( "R_upperarm", "R_forearm", "R_hand" ),
		RUpperarm_RForearm( "R_upperarm", "R_forearm" ),
		RUpperarm( "R_upperarm" ),
		LUpperarm_LForearm_LHand( "L_upperarm", "L_forearm", "L_hand" ),
		LUpperarm_LForearm( "L_upperarm", "L_forearm" ),
		LUpperarm( "L_upperarm" ),
		Head_REar( "head", "R_ear" ),
		Head_LEar( "head", "L_ear" ),
		Head( "head" ),
		LThigh_LCalf_LFoot( "L_thigh", "L_calf", "L_foot" ),
		LThigh_LCalf( "L_thigh", "L_calf" ),
		LThigh( "L_thigh" ),
		RThigh_RCalf_RFoot( "R_thigh", "R_calf", "R_foot" ),
		RThigh_RCalf( "R_thigh", "R_calf" ),
		RThigh( "R_thigh" ),
		Tail( "tail" );
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
