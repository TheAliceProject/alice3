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
	
public class Whip extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Whip() {
		super( "Objects/whip" );
	}
	public enum Part {
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15_Piece16_Piece17_Piece18_Piece19_Piece20_Piece21( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15", "piece16", "piece17", "piece18", "piece19", "piece20", "piece21" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15_Piece16_Piece17_Piece18_Piece19_Piece20( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15", "piece16", "piece17", "piece18", "piece19", "piece20" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15_Piece16_Piece17_Piece18_Piece19( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15", "piece16", "piece17", "piece18", "piece19" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15_Piece16_Piece17_Piece18( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15", "piece16", "piece17", "piece18" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15_Piece16_Piece17( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15", "piece16", "piece17" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15_Piece16( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15", "piece16" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14_Piece15( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14", "piece15" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13_Piece14( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13", "piece14" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12_Piece13( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12", "piece13" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11_Piece12( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11", "piece12" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10_Piece11( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10", "piece11" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09_Piece10( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09", "piece10" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08_Piece09( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08", "piece09" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07_Piece08( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07", "piece08" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06_Piece07( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06", "piece07" ),
		Piece01_Piece02_Piece03_Piece04_Piece05_Piece06( "piece01", "piece02", "piece03", "piece04", "piece05", "piece06" ),
		Piece01_Piece02_Piece03_Piece04_Piece05( "piece01", "piece02", "piece03", "piece04", "piece05" ),
		Piece01_Piece02_Piece03_Piece04( "piece01", "piece02", "piece03", "piece04" ),
		Piece01_Piece02_Piece03( "piece01", "piece02", "piece03" ),
		Piece01_Piece02( "piece01", "piece02" ),
		Piece01( "piece01" );
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
