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
package org.alice.apis.moveandturn.gallery.environments;
	
public class ConcertStage extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public ConcertStage() {
		super( "Environments/concert_stage" );
	}
	public enum Part {
		Crowd( "crowd" ),
		BassLeft( "bassLeft" ),
		BassRight( "bassRight" ),
		TrebleLeft( "trebleLeft" ),
		TrebleRight( "trebleRight" ),
		BeamLeft01( "beamLeft01" ),
		BeamLeft( "beamLeft" ),
		BeamRight( "beamRight" ),
		BeamRight01( "beamRight01" ),
		BeamCross01_Pimspot03_Bracket03_Cannister03_Lens03( "beamCross01", "pimspot03", "bracket03", "cannister03", "lens03" ),
		BeamCross01_Pimspot03_Bracket03_Cannister03( "beamCross01", "pimspot03", "bracket03", "cannister03" ),
		BeamCross01_Pimspot03_Bracket03( "beamCross01", "pimspot03", "bracket03" ),
		BeamCross01_Pimspot03( "beamCross01", "pimspot03" ),
		BeamCross01_Pimspot02_Bracket02_Cannister02_Lens02( "beamCross01", "pimspot02", "bracket02", "cannister02", "lens02" ),
		BeamCross01_Pimspot02_Bracket02_Cannister02( "beamCross01", "pimspot02", "bracket02", "cannister02" ),
		BeamCross01_Pimspot02_Bracket02( "beamCross01", "pimspot02", "bracket02" ),
		BeamCross01_Pimspot02( "beamCross01", "pimspot02" ),
		BeamCross01_Squarebracket03_Cyberlight03_HexLens03( "beamCross01", "squarebracket03", "cyberlight03", "hexLens03" ),
		BeamCross01_Squarebracket03_Cyberlight03( "beamCross01", "squarebracket03", "cyberlight03" ),
		BeamCross01_Squarebracket03( "beamCross01", "squarebracket03" ),
		BeamCross01( "beamCross01" ),
		BeamCross_Pimspot01_Bracket01_Cannister01_Lens01( "beamCross", "pimspot01", "bracket01", "cannister01", "lens01" ),
		BeamCross_Pimspot01_Bracket01_Cannister01( "beamCross", "pimspot01", "bracket01", "cannister01" ),
		BeamCross_Pimspot01_Bracket01( "beamCross", "pimspot01", "bracket01" ),
		BeamCross_Pimspot01( "beamCross", "pimspot01" ),
		BeamCross_Pimspot_Bracket_Cannister_Lens( "beamCross", "pimspot", "bracket", "cannister", "lens" ),
		BeamCross_Pimspot_Bracket_Cannister( "beamCross", "pimspot", "bracket", "cannister" ),
		BeamCross_Pimspot_Bracket( "beamCross", "pimspot", "bracket" ),
		BeamCross_Pimspot( "beamCross", "pimspot" ),
		BeamCross_Squarebracket_Cyberlight_HexLens( "beamCross", "squarebracket", "cyberlight", "hexLens" ),
		BeamCross_Squarebracket_Cyberlight( "beamCross", "squarebracket", "cyberlight" ),
		BeamCross_Squarebracket( "beamCross", "squarebracket" ),
		BeamCross_Squarebracket01_Cyberlight01_HexLens01( "beamCross", "squarebracket01", "cyberlight01", "hexLens01" ),
		BeamCross_Squarebracket01_Cyberlight01( "beamCross", "squarebracket01", "cyberlight01" ),
		BeamCross_Squarebracket01( "beamCross", "squarebracket01" ),
		BeamCross( "beamCross" ),
		Banner( "banner" );
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
