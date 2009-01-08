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
package org.alice.apis.moveandturn.gallery.amusementpark;
	
public class Bottlethrow extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Bottlethrow() {
		super( "Amusement Park/bottlethrow" );
	}
	public enum Part {
		SmallShelf01( "small shelf01" ),
		SmallShelf02( "small shelf02" ),
		SmallShelf( "small shelf" ),
		LongShelf01( "long shelf01" ),
		LongShelf( "long shelf" ),
		LongShelf02( "long shelf02" ),
		SmallShelf05( "small shelf05" ),
		SmallShelf04( "small shelf04" ),
		SmallShelf03( "small shelf03" ),
		FrontCounter_MiddleCounter_Bottles1( "front counter", "middle counter", "bottles1" ),
		FrontCounter_MiddleCounter_Bottles3( "front counter", "middle counter", "bottles3" ),
		FrontCounter_MiddleCounter_Bottles2( "front counter", "middle counter", "bottles2" ),
		FrontCounter_MiddleCounter( "front counter", "middle counter" ),
		FrontCounter( "front counter" );
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
