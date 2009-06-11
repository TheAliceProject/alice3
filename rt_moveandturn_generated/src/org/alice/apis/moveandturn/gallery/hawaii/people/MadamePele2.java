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
	
public class MadamePele2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public MadamePele2() {
		super( "Hawaii/People/MadamePele2" );
	}
	public enum Part {
		Chest_Head( "chest", "head" ),
		Chest_Upperrightarm_Lowerrightarm_Righthand( "chest", "upperrightarm", "lowerrightarm", "righthand" ),
		Chest_Upperrightarm_Lowerrightarm( "chest", "upperrightarm", "lowerrightarm" ),
		Chest_Upperrightarm( "chest", "upperrightarm" ),
		Chest_Upperleftarm_Lowerleftarm_Lefthand( "chest", "upperleftarm", "lowerleftarm", "lefthand" ),
		Chest_Upperleftarm_Lowerleftarm( "chest", "upperleftarm", "lowerleftarm" ),
		Chest_Upperleftarm( "chest", "upperleftarm" ),
		Chest_Skirt( "chest", "skirt" ),
		Chest_Upperrightleg_Lowerrightleg_Rightfoot( "chest", "upperrightleg", "lowerrightleg", "rightfoot" ),
		Chest_Upperrightleg_Lowerrightleg( "chest", "upperrightleg", "lowerrightleg" ),
		Chest_Upperrightleg( "chest", "upperrightleg" ),
		Chest_Upperleftleg_Lowerleftleg_Leftfoot( "chest", "upperleftleg", "lowerleftleg", "leftfoot" ),
		Chest_Upperleftleg_Lowerleftleg( "chest", "upperleftleg", "lowerleftleg" ),
		Chest_Upperleftleg( "chest", "upperleftleg" ),
		Chest( "chest" ),
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
