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
	
public class Shackles extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Shackles() {
		super( "Objects/shackles" );
	}
	public enum Part {
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13_Link14_Link15_Link16_Link17_Link18_Anchor( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13", "link14", "link15", "link16", "link17", "link18", "anchor" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13_Link14_Link15_Link16_Link17_Link18( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13", "link14", "link15", "link16", "link17", "link18" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13_Link14_Link15_Link16_Link17( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13", "link14", "link15", "link16", "link17" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13_Link14_Link15_Link16( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13", "link14", "link15", "link16" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13_Link14_Link15( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13", "link14", "link15" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13_Link14( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13", "link14" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12_Link13( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12", "link13" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11_Link12( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11", "link12" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10_Link11( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10", "link11" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09_Link10( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09", "link10" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08_Link09( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08", "link09" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07_Link08( "link01", "link02", "link03", "link04", "link05", "link06", "link07", "link08" ),
		Link01_Link02_Link03_Link04_Link05_Link06_Link07( "link01", "link02", "link03", "link04", "link05", "link06", "link07" ),
		Link01_Link02_Link03_Link04_Link05_Link06( "link01", "link02", "link03", "link04", "link05", "link06" ),
		Link01_Link02_Link03_Link04_Link05( "link01", "link02", "link03", "link04", "link05" ),
		Link01_Link02_Link03_Link04( "link01", "link02", "link03", "link04" ),
		Link01_Link02_Link03( "link01", "link02", "link03" ),
		Link01_Link02( "link01", "link02" ),
		Link01( "link01" ),
		RightShackle( "rightShackle" ),
		LeftShackle( "leftShackle" );
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
