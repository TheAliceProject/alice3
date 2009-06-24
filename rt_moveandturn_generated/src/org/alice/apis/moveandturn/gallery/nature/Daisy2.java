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
	
public class Daisy2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Daisy2() {
		super( "Nature/daisy2" );
	}
	public enum Part {
		Leaf1( "leaf1" ),
		Leaf2( "leaf2" ),
		StemMiddle_StemTop_Pistil_Petal01( "stemMiddle", "stemTop", "pistil", "petal01" ),
		StemMiddle_StemTop_Pistil_Petal02( "stemMiddle", "stemTop", "pistil", "petal02" ),
		StemMiddle_StemTop_Pistil_Petal03( "stemMiddle", "stemTop", "pistil", "petal03" ),
		StemMiddle_StemTop_Pistil_Petal04( "stemMiddle", "stemTop", "pistil", "petal04" ),
		StemMiddle_StemTop_Pistil_Petal05( "stemMiddle", "stemTop", "pistil", "petal05" ),
		StemMiddle_StemTop_Pistil_Petal06( "stemMiddle", "stemTop", "pistil", "petal06" ),
		StemMiddle_StemTop_Pistil_Petal07( "stemMiddle", "stemTop", "pistil", "petal07" ),
		StemMiddle_StemTop_Pistil_Petal08( "stemMiddle", "stemTop", "pistil", "petal08" ),
		StemMiddle_StemTop_Pistil_Petal09( "stemMiddle", "stemTop", "pistil", "petal09" ),
		StemMiddle_StemTop_Pistil_Petal10( "stemMiddle", "stemTop", "pistil", "petal10" ),
		StemMiddle_StemTop_Pistil_Petal11( "stemMiddle", "stemTop", "pistil", "petal11" ),
		StemMiddle_StemTop_Pistil_Petal12( "stemMiddle", "stemTop", "pistil", "petal12" ),
		StemMiddle_StemTop_Pistil_Petal13( "stemMiddle", "stemTop", "pistil", "petal13" ),
		StemMiddle_StemTop_Pistil_Petal14( "stemMiddle", "stemTop", "pistil", "petal14" ),
		StemMiddle_StemTop_Pistil_Petal15( "stemMiddle", "stemTop", "pistil", "petal15" ),
		StemMiddle_StemTop_Pistil_Petal16( "stemMiddle", "stemTop", "pistil", "petal16" ),
		StemMiddle_StemTop_Pistil( "stemMiddle", "stemTop", "pistil" ),
		StemMiddle_StemTop( "stemMiddle", "stemTop" ),
		StemMiddle( "stemMiddle" );
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
