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
	
public class Cellphone extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Cellphone() {
		super( "Objects/Cellphone" );
	}
	public enum Part {
		DialRocker( "dialRocker" ),
		MenuRocker( "menuRocker" ),
		Opt1( "opt1" ),
		Opt2( "opt2" ),
		Button02( "button02" ),
		Button01( "button01" ),
		Button05( "button05" ),
		Button08( "button08" ),
		Button00( "button00" ),
		Button04( "button04" ),
		Button07( "button07" ),
		Button0star( "button0star" ),
		Button06( "button06" ),
		Button09( "button09" ),
		Button0pnd( "button0pnd" ),
		Button03( "button03" ),
		Volume( "volume" ),
		AC( "AC" ),
		Headphone( "headphone" ),
		Power( "power" );
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
