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
	
public class MilitaryRadio extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public MilitaryRadio() {
		super( "Objects/MilitaryRadio" );
	}
	public enum Part {
		RadioBox_Dial1_Box08( "RadioBox", "Dial1", "Box08" ),
		RadioBox_Dial1( "RadioBox", "Dial1" ),
		RadioBox_Dial2_Box09( "RadioBox", "Dial2", "Box09" ),
		RadioBox_Dial2( "RadioBox", "Dial2" ),
		RadioBox_Dial3_Box07( "RadioBox", "Dial3", "Box07" ),
		RadioBox_Dial3( "RadioBox", "Dial3" ),
		RadioBox_Dial4_Box05( "RadioBox", "Dial4", "Box05" ),
		RadioBox_Dial4( "RadioBox", "Dial4" ),
		RadioBox_Dial5_Box04( "RadioBox", "Dial5", "Box04" ),
		RadioBox_Dial5( "RadioBox", "Dial5" ),
		RadioBox_Dial6_Box06( "RadioBox", "Dial6", "Box06" ),
		RadioBox_Dial6( "RadioBox", "Dial6" ),
		RadioBox_Readout( "RadioBox", "Readout" ),
		RadioBox_Switch4_Box11( "RadioBox", "Switch4", "Box11" ),
		RadioBox_Switch4( "RadioBox", "Switch4" ),
		RadioBox_Switch2_Box12( "RadioBox", "Switch2", "Box12" ),
		RadioBox_Switch2( "RadioBox", "Switch2" ),
		RadioBox_Switch1_Box03( "RadioBox", "Switch1", "Box03" ),
		RadioBox_Switch1( "RadioBox", "Switch1" ),
		RadioBox_Switch3_Box10( "RadioBox", "Switch3", "Box10" ),
		RadioBox_Switch3( "RadioBox", "Switch3" ),
		RadioBox_Osscilisco( "RadioBox", "Osscilisco" ),
		RadioBox( "RadioBox" ),
		Speaker( "Speaker" ),
		Cord( "Cord" );
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
