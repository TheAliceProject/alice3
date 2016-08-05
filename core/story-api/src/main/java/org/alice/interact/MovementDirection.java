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

import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public enum MovementDirection {

	FORWARD( 0.0d, 0.0d, -1.0d ),
	BACKWARD( 0.0d, 0.0d, 1.0d ),
	LEFT( -1.0d, 0.0d, 0.0d ),
	RIGHT( 1.0d, 0.0d, 0.0d ),
	UP( 0.0d, 1.0d, 0.0d ),
	DOWN( 0.0d, -1.0d, 0.0d ),
	UP_RIGHT( 1.0d, 1.0d, 0.0d ),
	UP_LEFT( -1.0d, 1.0d, 0.0d ),
	DOWN_RIGHT( 1.0d, -1.0d, 0.0d ),
	DOWN_LEFT( -1.0d, -1.0d, 0.0d ),
	UP_BACKWARD( 0.0d, 1.0d, 1.0d ),
	UP_FORWARD( 0.0d, 1.0d, -1.0d ),
	DOWN_BACKWARD( 0.0d, -1.0d, 1.0d ),
	DOWN_FORWARD( 0.0d, -1.0d, -1.0d ),
	RIGHT_BACKWARD( 1.0d, 0.0d, 1.0d ),
	RIGHT_FORWARD( 1.0d, 0.0d, -1.0d ),
	LEFT_BACKWARD( -1.0d, 0.0d, 1.0d ),
	LEFT_FORWARD( -1.0d, 0.0d, -1.0d ),
	RESIZE( -1.0d, 1.0d, 0.0d ), ;

	private MovementDirection( double x, double y, double z ) {
		this.directionVector = new Vector3( x, y, z );
		this.directionVector.normalize();
	}

	public Vector3 getVector() {
		return this.directionVector;
	}

	public HandleSet.HandleGroup getHandleGroup() {
		if( ( this == FORWARD ) || ( this == BACKWARD ) ) {
			return HandleSet.HandleGroup.Z_AXIS;
		} else if( ( this == UP ) || ( this == DOWN ) ) {
			return HandleSet.HandleGroup.Y_AXIS;
		} else if( ( this == LEFT ) || ( this == RIGHT ) ) {
			return HandleSet.HandleGroup.X_AXIS;
		} else if( ( this == UP_BACKWARD ) || ( this == UP_FORWARD ) || ( this == DOWN_BACKWARD ) || ( this == DOWN_FORWARD ) ) {
			return HandleSet.HandleGroup.Y_AND_Z_AXIS;
		} else if( ( this == UP_RIGHT ) || ( this == UP_LEFT ) || ( this == DOWN_RIGHT ) || ( this == DOWN_LEFT ) ) {
			return HandleSet.HandleGroup.X_AND_Y_AXIS;
		} else if( ( this == RIGHT_BACKWARD ) || ( this == LEFT_BACKWARD ) || ( this == RIGHT_FORWARD ) || ( this == LEFT_FORWARD ) ) {
			return HandleSet.HandleGroup.X_AND_Z_AXIS;
		} else if( this == RESIZE ) {
			return HandleSet.HandleGroup.RESIZE_AXIS;
		} else {
			assert false : "NO HANDLE GROUP DEFINED FOR " + this;
			return null;
		}
	}

	public MovementDirection getOpposite() {
		if( this == FORWARD ) {
			return BACKWARD;
		} else if( this == BACKWARD ) {
			return FORWARD;
		} else if( this == UP ) {
			return DOWN;
		} else if( this == DOWN ) {
			return UP;
		} else if( this == LEFT ) {
			return RIGHT;
		} else if( this == RIGHT ) {
			return LEFT;
		} else {
			return this;
		}
	}

	public boolean hasDirection( Vector3 vector ) {
		double dot = Vector3.calculateDotProduct( this.directionVector, vector );
		if( dot > 0.0d ) {
			return true;
		} else {
			return false;
		}
	}

	private final Vector3 directionVector;
}
