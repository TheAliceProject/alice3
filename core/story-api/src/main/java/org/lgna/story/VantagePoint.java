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
package org.lgna.story;

/**
 * @author Dennis Cosgrove
 */
public final class VantagePoint {
	public static final VantagePoint IDENTITY = new VantagePoint( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity() );
	private final edu.cmu.cs.dennisc.math.AffineMatrix4x4 internal;

	private VantagePoint( edu.cmu.cs.dennisc.math.AffineMatrix4x4 internal ) {
		this.internal = internal;
	}

	public VantagePoint( Orientation orientation, Position position ) {
		this( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( orientation.getInternal(), position.getInternal() ) );
	}

	/* package-private */static VantagePoint createInstance( edu.cmu.cs.dennisc.math.AffineMatrix4x4 internal ) {
		return internal != null ? new VantagePoint( internal ) : null;
	}

	/* package-private */edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInternal() {
		return this.internal;
	}

	/* package-private */static edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInternal( VantagePoint vantagePoint ) {
		return vantagePoint != null ? vantagePoint.internal : null;
	}

	@Override
	public boolean equals( Object obj ) {
		if( obj instanceof VantagePoint ) {
			VantagePoint other = (VantagePoint)obj;
			return this.internal.equals( other.internal );
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.internal.hashCode();
	}

	public Orientation getOrientation() {
		return Orientation.createInstance( this.internal.orientation );
	}

	public Position getPosition() {
		return Position.createInstance( this.internal.translation );
	}
}
