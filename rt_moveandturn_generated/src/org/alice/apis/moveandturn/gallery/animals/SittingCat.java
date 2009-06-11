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
	
public class SittingCat extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SittingCat() {
		super( "Animals/sitting_cat" );
	}
	public enum Part {
		Rightbackleg( "rightbackleg" ),
		Leftbackleg( "leftbackleg" ),
		Leftfrontleg( "leftfrontleg" ),
		Rightfrontleg( "rightfrontleg" ),
		Head_Leftear( "head", "leftear" ),
		Head_Rightear( "head", "rightear" ),
		Head_Lefttopwhisker( "head", "lefttopwhisker" ),
		Head_Leftbottomwhisker( "head", "leftbottomwhisker" ),
		Head_Rightbottomwhisker( "head", "rightbottomwhisker" ),
		Head_Righttopwhisker( "head", "righttopwhisker" ),
		Head( "head" ),
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
