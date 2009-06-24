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
package org.alice.apis.moveandturn.gallery.nature;
	
public class Garden extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Garden() {
		super( "Nature/garden" );
	}
	public enum Part {
		BrickCircle_BrickTransRight( "brick-circle", "brick-trans-right" ),
		BrickCircle_BrickTransLeft_BrickOvalOuter_BrickEntryRight( "brick-circle", "brick-trans-left", "brick-oval-outer", "brick-entry-right" ),
		BrickCircle_BrickTransLeft_BrickOvalOuter_BrickEntryLeft( "brick-circle", "brick-trans-left", "brick-oval-outer", "brick-entry-left" ),
		BrickCircle_BrickTransLeft_BrickOvalOuter_BrickOvalInner( "brick-circle", "brick-trans-left", "brick-oval-outer", "brick-oval-inner" ),
		BrickCircle_BrickTransLeft_BrickOvalOuter( "brick-circle", "brick-trans-left", "brick-oval-outer" ),
		BrickCircle_BrickTransLeft( "brick-circle", "brick-trans-left" ),
		BrickCircle_BrickExitLeft( "brick-circle", "brick-exit-left" ),
		BrickCircle_BrickExitRight( "brick-circle", "brick-exit-right" ),
		BrickCircle( "brick-circle" ),
		WallFront_WallFrontTop( "wall-front", "wall-front-top" ),
		WallFront( "wall-front" ),
		WallLeft_WallLeftTop( "wall-left", "wall-left-top" ),
		WallLeft( "wall-left" ),
		WallRear_WallRearTop( "wall-rear", "wall-rear-top" ),
		WallRear( "wall-rear" ),
		WallRight_WallRightTop( "wall-right", "wall-right-top" ),
		WallRight( "wall-right" );
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
