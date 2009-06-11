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
package org.alice.apis.moveandturn.gallery.holidays.christmas;
	
public class MerrySign extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public MerrySign() {
		super( "Holidays/Christmas/merry_sign" );
	}
	public enum Part {
		M_R( "M", "R" ),
		M_R01( "M", "R01" ),
		M_Y( "M", "Y" ),
		M_E( "M", "E" ),
		M_Period2( "M", "period2" ),
		M_Period3( "M", "period3" ),
		M_Period1( "M", "period1" ),
		M_Period5( "M", "period5" ),
		M_Period4( "M", "period4" ),
		M( "M" ),
		C_T( "C", "T" ),
		C_A( "C", "A" ),
		C_M01( "C", "M01" ),
		C_R02( "C", "R02" ),
		C_I( "C", "I" ),
		C_S( "C", "S" ),
		C_S01( "C", "S01" ),
		C_Period6( "C", "period6" ),
		C_Period7( "C", "period7" ),
		C_Period8( "C", "period8" ),
		C_H( "C", "H" ),
		C( "C" );
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
