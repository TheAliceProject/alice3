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
package org.alice.apis.moveandturn.gallery.nature;
	
public class LotusFlower extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public LotusFlower() {
		super( "Nature/lotusFlower" );
	}
	public enum Part {
		Stem_Branch_Leaf( "stem", "branch", "leaf" ),
		Stem_Branch( "stem", "branch" ),
		Stem_Mesh41( "stem", "Mesh41" ),
		Stem_Mesh38( "stem", "Mesh38" ),
		Stem_Mesh33( "stem", "Mesh33" ),
		Stem_Mesh39( "stem", "Mesh39" ),
		Stem_Mesh42( "stem", "Mesh42" ),
		Stem_Mesh45( "stem", "Mesh45" ),
		Stem_Mesh37( "stem", "Mesh37" ),
		Stem_Mesh44( "stem", "Mesh44" ),
		Stem_Mesh34( "stem", "Mesh34" ),
		Stem_Mesh40( "stem", "Mesh40" ),
		Stem_Mesh36( "stem", "Mesh36" ),
		Stem_Mesh35( "stem", "Mesh35" ),
		Stem_Mesh43( "stem", "Mesh43" ),
		Stem_Mesh47( "stem", "Mesh47" ),
		Stem_Mesh48( "stem", "Mesh48" ),
		Stem_Mesh49( "stem", "Mesh49" ),
		Stem( "stem" );
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
