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
package org.alice.apis.moveandturn.gallery.buildings;
	
public class Airport extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Airport() {
		super( "Buildings/Airport" );
	}
	public enum Part {
		Walkway10( "walkway10" ),
		Walkway09( "walkway09" ),
		Walkway08( "walkway08" ),
		Walkway07( "walkway07" ),
		Walkway06( "walkway06" ),
		Walkway02( "walkway02" ),
		Walkway01( "walkway01" ),
		Walkway( "walkway" ),
		Walkway03( "walkway03" ),
		Walkway05( "walkway05" ),
		RunwayAndParking_Light43( "runway_and_parking", "light43" ),
		RunwayAndParking_Light37( "runway_and_parking", "light37" ),
		RunwayAndParking_Garage02( "runway_and_parking", "Garage02" ),
		RunwayAndParking_Garage03( "runway_and_parking", "Garage03" ),
		RunwayAndParking_Light34( "runway_and_parking", "light34" ),
		RunwayAndParking_Light35( "runway_and_parking", "light35" ),
		RunwayAndParking_Light45( "runway_and_parking", "light45" ),
		RunwayAndParking_Garage01( "runway_and_parking", "Garage01" ),
		RunwayAndParking_Light42( "runway_and_parking", "light42" ),
		RunwayAndParking_Garage( "runway_and_parking", "Garage" ),
		RunwayAndParking_Light46( "runway_and_parking", "light46" ),
		RunwayAndParking_Light36( "runway_and_parking", "light36" ),
		RunwayAndParking_Light47( "runway_and_parking", "light47" ),
		RunwayAndParking_Light44( "runway_and_parking", "light44" ),
		RunwayAndParking_Light04( "runway_and_parking", "light04" ),
		RunwayAndParking_Light05( "runway_and_parking", "light05" ),
		RunwayAndParking_Light06( "runway_and_parking", "light06" ),
		RunwayAndParking_Light07( "runway_and_parking", "light07" ),
		RunwayAndParking_Light08( "runway_and_parking", "light08" ),
		RunwayAndParking_Light09( "runway_and_parking", "light09" ),
		RunwayAndParking_Light02( "runway_and_parking", "light02" ),
		RunwayAndParking_Light03( "runway_and_parking", "light03" ),
		RunwayAndParking_Light50( "runway_and_parking", "light50" ),
		RunwayAndParking_Light49( "runway_and_parking", "light49" ),
		RunwayAndParking_Light55( "runway_and_parking", "light55" ),
		RunwayAndParking_Light52( "runway_and_parking", "light52" ),
		RunwayAndParking_Light53( "runway_and_parking", "light53" ),
		RunwayAndParking_Light54( "runway_and_parking", "light54" ),
		RunwayAndParking_Light48( "runway_and_parking", "light48" ),
		RunwayAndParking_Light51( "runway_and_parking", "light51" ),
		RunwayAndParking_Light( "runway_and_parking", "light" ),
		RunwayAndParking_Light01( "runway_and_parking", "light01" ),
		RunwayAndParking( "runway_and_parking" );
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
