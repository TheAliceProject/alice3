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

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.StandIn;

/**
 * @author David Culyba
 */
public enum MovementType {
	STOOD_UP( 0 ),
	LOCAL( 1 ),
	ABSOLUTE( 2 );

	public static MovementType getMovementTypeForIndex( int index ) {
		MovementType[] types = MovementType.values();
		for( MovementType currentType : types ) {
			if( currentType.isIndex( index ) ) {
				return currentType;
			}
		}
		return null;
	}

	private MovementType( int index ) {
		this.index = index;
	}

	public int getIndex() {
		return this.index;
	}

	public boolean isIndex( int index ) {
		return ( this.index == index );
	}

	public void applyTranslation( AbstractTransformable transformable, Point3 translateAmount ) {
		switch( this ) {
		case STOOD_UP: {
			StandIn standIn = new StandIn();
			standIn.setVehicle( transformable );
			try {
				standIn.setAxesOnlyToStandUp();
				transformable.applyTranslation( translateAmount, standIn );
			} finally {
				standIn.setVehicle( null );
			}
			break;
		}
		case LOCAL: {
			transformable.applyTranslation( translateAmount, transformable );
			break;
		}
		case ABSOLUTE: {
			transformable.applyTranslation( translateAmount, AsSeenBy.SCENE );
			break;
		}
		}
	}

	public void applyRotation( AbstractTransformable transformable, Vector3 rotationAxis, Angle rotation ) {
		switch( this ) {
		case STOOD_UP: {
			StandIn standIn = new StandIn();
			standIn.setVehicle( transformable );
			try {
				standIn.setAxesOnlyToStandUp();
				transformable.applyRotationAboutArbitraryAxis( rotationAxis, rotation, standIn );
			} finally {
				standIn.setVehicle( null );
			}
			break;
		}
		case LOCAL: {
			transformable.applyRotationAboutArbitraryAxis( rotationAxis, rotation, transformable );
			break;
		}
		case ABSOLUTE: {
			transformable.applyRotationAboutArbitraryAxis( rotationAxis, rotation, AsSeenBy.SCENE );
			break;
		}
		}
	}

	private final int index;
}
