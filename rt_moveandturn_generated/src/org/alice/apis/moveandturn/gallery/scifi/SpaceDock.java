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
package org.alice.apis.moveandturn.gallery.scifi;
	
public class SpaceDock extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public SpaceDock() {
		super( "SciFi/SpaceDock" );
	}
	public enum Part {
		SpaceDock1_Rightlift( "Space_Dock1", "rightlift" ),
		SpaceDock1_Leftlift( "Space_Dock1", "leftlift" ),
		SpaceDock1_Bumpers1( "Space_Dock1", "bumpers1" ),
		SpaceDock1_Energydock1_Tube3( "Space_Dock1", "energydock1", "tube3" ),
		SpaceDock1_Energydock1_Tube1( "Space_Dock1", "energydock1", "tube1" ),
		SpaceDock1_Energydock1_Tube2( "Space_Dock1", "energydock1", "tube2" ),
		SpaceDock1_Energydock1( "Space_Dock1", "energydock1" ),
		SpaceDock1( "Space_Dock1" ),
		SubStar1_SubStar2( "SubStar1", "SubStar2" ),
		SubStar1( "SubStar1" ),
		SpaceDock2_Rightlift2( "Space_Dock2", "rightlift2" ),
		SpaceDock2_Leftlift2( "Space_Dock2", "leftlift2" ),
		SpaceDock2_Bumpers2( "Space_Dock2", "bumpers2" ),
		SpaceDock2_Energydock2_Tube6( "Space_Dock2", "energydock2", "tube6" ),
		SpaceDock2_Energydock2_Tube4( "Space_Dock2", "energydock2", "tube4" ),
		SpaceDock2_Energydock2_Tube5( "Space_Dock2", "energydock2", "tube5" ),
		SpaceDock2_Energydock2( "Space_Dock2", "energydock2" ),
		SpaceDock2( "Space_Dock2" ),
		SpaceDock3_Rightlift3( "Space_Dock3", "rightlift3" ),
		SpaceDock3_Leftlift3( "Space_Dock3", "leftlift3" ),
		SpaceDock3_Bumpers3( "Space_Dock3", "bumpers3" ),
		SpaceDock3_Energydock3_Tube9( "Space_Dock3", "energydock3", "tube9" ),
		SpaceDock3_Energydock3_Tube7( "Space_Dock3", "energydock3", "tube7" ),
		SpaceDock3_Energydock3_Tube8( "Space_Dock3", "energydock3", "tube8" ),
		SpaceDock3_Energydock3( "Space_Dock3", "energydock3" ),
		SpaceDock3( "Space_Dock3" );
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
