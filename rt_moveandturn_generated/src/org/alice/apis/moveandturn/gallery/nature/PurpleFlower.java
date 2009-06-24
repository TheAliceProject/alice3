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
	
public class PurpleFlower extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public PurpleFlower() {
		super( "Nature/PurpleFlower" );
	}
	public enum Part {
		IStemMiddle_IStemTop_IHPistil_IHPetal03( "i_stemMiddle", "i_stemTop", "iH_pistil", "iH_petal03" ),
		IStemMiddle_IStemTop_IHPistil_IHPetal02( "i_stemMiddle", "i_stemTop", "iH_pistil", "iH_petal02" ),
		IStemMiddle_IStemTop_IHPistil_IHPetal01( "i_stemMiddle", "i_stemTop", "iH_pistil", "iH_petal01" ),
		IStemMiddle_IStemTop_IHPistil_IHPetal05( "i_stemMiddle", "i_stemTop", "iH_pistil", "iH_petal05" ),
		IStemMiddle_IStemTop_IHPistil_IHPetal04( "i_stemMiddle", "i_stemTop", "iH_pistil", "iH_petal04" ),
		IStemMiddle_IStemTop_IHPistil( "i_stemMiddle", "i_stemTop", "iH_pistil" ),
		IStemMiddle_IStemTop_IHLeafbase05_IHLeaftip05( "i_stemMiddle", "i_stemTop", "iH_leafbase05", "iH_leaftip05" ),
		IStemMiddle_IStemTop_IHLeafbase05( "i_stemMiddle", "i_stemTop", "iH_leafbase05" ),
		IStemMiddle_IStemTop( "i_stemMiddle", "i_stemTop" ),
		IStemMiddle_IHLeafbase03_IHLeaftip03( "i_stemMiddle", "iH_leafbase03", "iH_leaftip03" ),
		IStemMiddle_IHLeafbase03( "i_stemMiddle", "iH_leafbase03" ),
		IStemMiddle_IHLeafbase01_IHLeaftip01( "i_stemMiddle", "iH_leafbase01", "iH_leaftip01" ),
		IStemMiddle_IHLeafbase01( "i_stemMiddle", "iH_leafbase01" ),
		IStemMiddle( "i_stemMiddle" ),
		IHLeafbase02_IHLeaftip02( "iH_leafbase02", "iH_leaftip02" ),
		IHLeafbase02( "iH_leafbase02" ),
		IHLeafbase04_IHLeaftip04( "iH_leafbase04", "iH_leaftip04" ),
		IHLeafbase04( "iH_leafbase04" );
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
