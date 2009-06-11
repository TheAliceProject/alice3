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
package org.alice.apis.moveandturn.gallery.objects;
	
public class RackWeightholder extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public RackWeightholder() {
		super( "Objects/rack_weightholder" );
	}
	public enum Part {
		Weightbrace2_Box05( "weightbrace2", "Box05" ),
		Weightbrace2_Box04( "weightbrace2", "Box04" ),
		Weightbrace2( "weightbrace2" ),
		Weightbrace1( "weightbrace1" ),
		Weightbar02_Weight05( "weightbar02", "weight05" ),
		Weightbar02_Weight06( "weightbar02", "weight06" ),
		Weightbar02( "weightbar02" ),
		Weightbar01_Weight03( "weightbar01", "weight03" ),
		Weightbar01_Weight04( "weightbar01", "weight04" ),
		Weightbar01( "weightbar01" ),
		Weightbar_Weight2( "weightbar", "weight2" ),
		Weightbar_Weight1( "weightbar", "weight1" ),
		Weightbar( "weightbar" );
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
