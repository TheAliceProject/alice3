/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.interact;

import java.util.BitSet;

/**
 * @author David Culyba
 */
public class PickHint extends BitSet {

	public static enum PickType {
		NOTHING,
		VIEWABLE,
		SELECTABLE,
		MOVEABLE,
		TURNABLE,
		RESIZABLE,
		TWO_D_HANDLE,
		THREE_D_HANDLE,
		CAMERA_MARKER,
		OBJECT_MARKER,
		ORTHOGRAPHIC_CAMERA,
		PERSPECTIVE_CAMERA,
		JOINT,
		SUN;

		private PickType() {
		}

		public PickHint pickHint() {
			if( this.pickHint == null ) {
				this.pickHint = new PickHint( this );
			}
			return this.pickHint;
		}

		private PickHint pickHint = null;
	}

	public static final edu.cmu.cs.dennisc.scenegraph.Element.Key<PickHint> PICK_HINT_KEY = edu.cmu.cs.dennisc.scenegraph.Element.Key.createInstance( "PICK_HINT_KEY" );

	//	protected static final int NUM_TYPES = PickType.values().length;

	private static PickHint MARKERS = null;

	public static PickHint getMarkersHint() {
		if( MARKERS == null ) {
			MARKERS = new PickHint( PickType.CAMERA_MARKER, PickType.OBJECT_MARKER );
		}
		return MARKERS;
	}

	private static PickHint ANYTHING = null;

	public static PickHint getAnythingHint() {
		if( ANYTHING == null ) {
			ANYTHING = createEverythingHint();
		}
		return ANYTHING;
	}

	//	public static final PickHint MOVEABLE_OBJECTS = new PickHint( PickType.MOVEABLE_OBJECT, PickType.MARKER );

	private static PickHint ALL_HANDLES = null;

	public static PickHint getAllHandlesHint() {
		if( ALL_HANDLES == null ) {
			ALL_HANDLES = new PickHint( PickType.TWO_D_HANDLE, PickType.THREE_D_HANDLE );
		}
		return ALL_HANDLES;
	}

	private static PickHint NON_INTERACTIVE = null;

	public static PickHint getNonInteractiveHint() {
		if( NON_INTERACTIVE == null ) {
			NON_INTERACTIVE = new PickHint( PickType.NOTHING );
		}
		return NON_INTERACTIVE;
	}

	public PickHint() {
		super( getNumTypes() );
	}

	public PickHint( PickType... pickTypes ) {
		super( getNumTypes() );
		for( PickType pickType : pickTypes ) {
			this.set( pickType.ordinal() );
		}
	}

	public static PickHint createEverythingHint() {
		PickHint toReturn = new PickHint();
		for( int i = 0; i < getNumTypes(); i++ ) {
			toReturn.set( i );
		}
		return toReturn;
	}

	protected static int getNumTypes() {
		return PickType.values().length;
	}

	public boolean get( PickType pickType ) {
		return this.get( pickType.ordinal() );
	}

	public void addPickType( PickType pickType ) {
		this.set( pickType.ordinal() );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( "pick: " );
		boolean matches = false;
		for( PickType pickType : PickType.values() ) {
			if( this.intersects( pickType.pickHint() ) ) {
				matches = true;
				sb.append( pickType.toString() + " " );
			}
		}
		if( !matches ) {
			sb.append( "No Matches" );
		}
		return sb.toString();
	}
}
