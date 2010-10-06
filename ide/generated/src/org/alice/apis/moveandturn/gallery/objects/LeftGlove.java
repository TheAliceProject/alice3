/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
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
