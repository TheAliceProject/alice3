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
	
public class Sunflower extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Sunflower() {
		super( "Nature/Sunflower" );
	}
	public enum Part {
		Blossom_Petals_Petal1( "Blossom", "Petals", "Petal1" ),
		Blossom_Petals_Petal9( "Blossom", "Petals", "Petal9" ),
		Blossom_Petals_Petal6( "Blossom", "Petals", "Petal6" ),
		Blossom_Petals_Petal2( "Blossom", "Petals", "Petal2" ),
		Blossom_Petals_Petal3( "Blossom", "Petals", "Petal3" ),
		Blossom_Petals_Petal10( "Blossom", "Petals", "Petal10" ),
		Blossom_Petals_Petal11( "Blossom", "Petals", "Petal11" ),
		Blossom_Petals_Petal12( "Blossom", "Petals", "Petal12" ),
		Blossom_Petals_Petal7( "Blossom", "Petals", "Petal7" ),
		Blossom_Petals_Petal4( "Blossom", "Petals", "Petal4" ),
		Blossom_Petals_Petal5( "Blossom", "Petals", "Petal5" ),
		Blossom_Petals_Petal8( "Blossom", "Petals", "Petal8" ),
		Blossom_Petals( "Blossom", "Petals" ),
		Blossom_Iris( "Blossom", "Iris" ),
		Blossom_Back( "Blossom", "Back" ),
		Blossom( "Blossom" ),
		Stem_Leaves( "Stem", "Leaves" ),
		Stem( "Stem" );
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
