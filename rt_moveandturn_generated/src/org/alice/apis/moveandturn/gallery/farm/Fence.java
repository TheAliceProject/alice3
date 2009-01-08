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
package org.alice.apis.moveandturn.gallery.farm;
	
public class Fence extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Fence() {
		super( "Farm/fence" );
	}
	public enum Part {
		CrossBar34High( "crossBar_3_4_high" ),
		Post04( "post04" ),
		Post02( "post02" ),
		Post01( "post01" ),
		CrossBar34Low( "crossBar_3_4_low" ),
		CrossBar34Mid( "crossBar_3_4_mid" ),
		Post03( "post03" ),
		CrossBar23High( "crossBar_2_3_high" ),
		CrossBar23Mid( "crossBar_2_3_mid" ),
		CrossBar23Low( "crossBar_2_3_low" ),
		CrossBar13High( "crossBar_1_3_high" ),
		CrossBar13Mid( "crossBar_1_3_mid" ),
		CrossBar13Low( "crossBar_1_3_low" );
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
