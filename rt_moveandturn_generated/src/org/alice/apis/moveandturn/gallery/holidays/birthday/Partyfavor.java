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
package org.alice.apis.moveandturn.gallery.holidays.birthday;
	
public class Partyfavor extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Partyfavor() {
		super( "Holidays/Birthday/partyfavor" );
	}
	public enum Part {
		Extension1_Extension2_Extension3_Extension4_Extension5_Extension6_Extension7_Extension8( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5", "Extension6", "Extension7", "Extension8" ),
		Extension1_Extension2_Extension3_Extension4_Extension5_Extension6_Extension7( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5", "Extension6", "Extension7" ),
		Extension1_Extension2_Extension3_Extension4_Extension5_Extension6( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5", "Extension6" ),
		Extension1_Extension2_Extension3_Extension4_Extension5( "Extension1", "Extension2", "Extension3", "Extension4", "Extension5" ),
		Extension1_Extension2_Extension3_Extension4( "Extension1", "Extension2", "Extension3", "Extension4" ),
		Extension1_Extension2_Extension3( "Extension1", "Extension2", "Extension3" ),
		Extension1_Extension2( "Extension1", "Extension2" ),
		Extension1( "Extension1" ),
		MouthPiece_Ribbon08( "mouth_piece", "ribbon08" ),
		MouthPiece_Ribbon03( "mouth_piece", "ribbon03" ),
		MouthPiece_Ribbon( "mouth_piece", "ribbon" ),
		MouthPiece_Ribbon05( "mouth_piece", "ribbon05" ),
		MouthPiece_Ribbon07( "mouth_piece", "ribbon07" ),
		MouthPiece_Ribbon06( "mouth_piece", "ribbon06" ),
		MouthPiece_Ribbon04( "mouth_piece", "ribbon04" ),
		MouthPiece_Ribbon2( "mouth_piece", "ribbon2" ),
		MouthPiece( "mouth_piece" );
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
