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
	
public class RedFlower extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public RedFlower() {
		super( "Nature/RedFlower" );
	}
	public enum Part {
		StemMiddle_StemTop_Pistil_Petal01( "stemMiddle", "stemTop", "pistil", "petal01" ),
		StemMiddle_StemTop_Pistil_Petal02( "stemMiddle", "stemTop", "pistil", "petal02" ),
		StemMiddle_StemTop_Pistil_Petal03( "stemMiddle", "stemTop", "pistil", "petal03" ),
		StemMiddle_StemTop_Pistil_Petal05( "stemMiddle", "stemTop", "pistil", "petal05" ),
		StemMiddle_StemTop_Pistil_Petal04( "stemMiddle", "stemTop", "pistil", "petal04" ),
		StemMiddle_StemTop_Pistil( "stemMiddle", "stemTop", "pistil" ),
		StemMiddle_StemTop( "stemMiddle", "stemTop" ),
		StemMiddle_Leaf2( "stemMiddle", "leaf2" ),
		StemMiddle( "stemMiddle" ),
		Leaf1( "leaf1" );
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
