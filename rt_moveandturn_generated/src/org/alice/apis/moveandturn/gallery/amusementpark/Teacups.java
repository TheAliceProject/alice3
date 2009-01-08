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
	
public class Teacups extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Teacups() {
		super( "Amusement Park/Teacups" );
	}
	public enum Part {
		Base_Platter1_Cup2( "Base", "Platter1", "Cup2" ),
		Base_Platter1_Cup3( "Base", "Platter1", "Cup3" ),
		Base_Platter1_Cup4( "Base", "Platter1", "Cup4" ),
		Base_Platter1_Cup1( "Base", "Platter1", "Cup1" ),
		Base_Platter1( "Base", "Platter1" ),
		Base_Platter2_Cup1( "Base", "Platter2", "Cup1" ),
		Base_Platter2_Cup2( "Base", "Platter2", "Cup2" ),
		Base_Platter2_Cup3( "Base", "Platter2", "Cup3" ),
		Base_Platter2_Cup4( "Base", "Platter2", "Cup4" ),
		Base_Platter2( "Base", "Platter2" ),
		Base_Platter3_Cup1( "Base", "Platter3", "Cup1" ),
		Base_Platter3_Cup2( "Base", "Platter3", "Cup2" ),
		Base_Platter3_Cup3( "Base", "Platter3", "Cup3" ),
		Base_Platter3_Cup4( "Base", "Platter3", "Cup4" ),
		Base_Platter3( "Base", "Platter3" ),
		Base_Platter4_Cup1( "Base", "Platter4", "Cup1" ),
		Base_Platter4_Cup2( "Base", "Platter4", "Cup2" ),
		Base_Platter4_Cup3( "Base", "Platter4", "Cup3" ),
		Base_Platter4_Cup4( "Base", "Platter4", "Cup4" ),
		Base_Platter4( "Base", "Platter4" ),
		Base( "Base" ),
		Booth( "booth" );
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
