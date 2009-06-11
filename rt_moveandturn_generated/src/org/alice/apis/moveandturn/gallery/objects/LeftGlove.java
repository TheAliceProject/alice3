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
	
public class LeftGlove extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public LeftGlove() {
		super( "Objects/leftGlove" );
	}
	public enum Part {
		GloveFist_PinkyKnuckle_PinkyBottom_PinkyMiddle_PinkyEnd( "gloveFist", "pinkyKnuckle", "pinkyBottom", "pinkyMiddle", "pinkyEnd" ),
		GloveFist_PinkyKnuckle_PinkyBottom_PinkyMiddle( "gloveFist", "pinkyKnuckle", "pinkyBottom", "pinkyMiddle" ),
		GloveFist_PinkyKnuckle_PinkyBottom( "gloveFist", "pinkyKnuckle", "pinkyBottom" ),
		GloveFist_PinkyKnuckle( "gloveFist", "pinkyKnuckle" ),
		GloveFist_RingKnuckle_RingBottom_RingMiddle_RingEnd( "gloveFist", "ringKnuckle", "ringBottom", "ringMiddle", "ringEnd" ),
		GloveFist_RingKnuckle_RingBottom_RingMiddle( "gloveFist", "ringKnuckle", "ringBottom", "ringMiddle" ),
		GloveFist_RingKnuckle_RingBottom( "gloveFist", "ringKnuckle", "ringBottom" ),
		GloveFist_RingKnuckle( "gloveFist", "ringKnuckle" ),
		GloveFist_MiddleKnuckle_MiddleBottom_MiddleMiddle_MiddleTop( "gloveFist", "middleKnuckle", "middleBottom", "middleMiddle", "middleTop" ),
		GloveFist_MiddleKnuckle_MiddleBottom_MiddleMiddle( "gloveFist", "middleKnuckle", "middleBottom", "middleMiddle" ),
		GloveFist_MiddleKnuckle_MiddleBottom( "gloveFist", "middleKnuckle", "middleBottom" ),
		GloveFist_MiddleKnuckle( "gloveFist", "middleKnuckle" ),
		GloveFist_PointerKnuckle_PointerBottom_PointerMiddle_PointerEnd( "gloveFist", "pointerKnuckle", "pointerBottom", "pointerMiddle", "pointerEnd" ),
		GloveFist_PointerKnuckle_PointerBottom_PointerMiddle( "gloveFist", "pointerKnuckle", "pointerBottom", "pointerMiddle" ),
		GloveFist_PointerKnuckle_PointerBottom( "gloveFist", "pointerKnuckle", "pointerBottom" ),
		GloveFist_PointerKnuckle( "gloveFist", "pointerKnuckle" ),
		GloveFist_ThumbKnuckle_ThumbBottom_ThumbTop( "gloveFist", "thumbKnuckle", "thumbBottom", "thumbTop" ),
		GloveFist_ThumbKnuckle_ThumbBottom( "gloveFist", "thumbKnuckle", "thumbBottom" ),
		GloveFist_ThumbKnuckle( "gloveFist", "thumbKnuckle" ),
		GloveFist( "gloveFist" ),
		Arm( "arm" );
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
