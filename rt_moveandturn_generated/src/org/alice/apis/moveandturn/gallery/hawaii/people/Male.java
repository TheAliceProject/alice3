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
package org.alice.apis.moveandturn.gallery.hawaii.people;
	
public class Male extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Male() {
		super( "Hawaii/People/Male" );
	}
	public enum Part {
		Chest_RBi_RFore_RHand( "chest", "r_bi", "r_fore", "r_hand" ),
		Chest_RBi_RFore( "chest", "r_bi", "r_fore" ),
		Chest_RBi( "chest", "r_bi" ),
		Chest_LBi_LFore_LHand( "chest", "l_bi", "l_fore", "l_hand" ),
		Chest_LBi_LFore( "chest", "l_bi", "l_fore" ),
		Chest_LBi( "chest", "l_bi" ),
		Chest_Neck_Head( "chest", "neck", "head" ),
		Chest_Neck( "chest", "neck" ),
		Chest( "chest" ),
		RThi_RShin_RFoot( "r_thi", "r_shin", "r_foot" ),
		RThi_RShin( "r_thi", "r_shin" ),
		RThi( "r_thi" ),
		LThi_LShin_LFoot( "l_thi", "l_shin", "l_foot" ),
		LThi_LShin( "l_thi", "l_shin" ),
		LThi( "l_thi" );
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
