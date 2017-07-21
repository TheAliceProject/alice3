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
package org.alice.interact.manipulator;

import org.alice.interact.InputState;
import org.alice.interact.MovementKey;
import org.alice.interact.MovementType;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;

/**
 * @author David Culyba
 */
public class TranslateKeyManipulator extends AbstractManipulator {

	private static double MOVEMENT_RATE = 5.0d;
	private static double CLICK_TIME = .1d;
	private static double CLICK_MOVE_AMOUNT = .2d;

	public TranslateKeyManipulator() {
		directionKeys = new MovementKey[ 0 ];
	}

	public TranslateKeyManipulator( MovementKey[] directionKeys ) {
		setKeys( directionKeys );
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Move";
	}

	public void setBounds( AxisAlignedBox bounds ) {
		this.bounds = bounds;
	}

	public void setKeys( MovementKey[] directionKeys ) {
		this.directionKeys = directionKeys;
	}

	public void addKeys( MovementKey[] directionKeys ) {
		if( ( this.directionKeys != null ) && ( directionKeys != null ) ) {
			MovementKey[] combinedKeys = new MovementKey[ this.directionKeys.length + directionKeys.length ];
			for( int i = 0; i < this.directionKeys.length; i++ ) {
				combinedKeys[ i ] = this.directionKeys[ i ];
			}
			for( int i = 0; i < directionKeys.length; i++ ) {
				combinedKeys[ this.directionKeys.length + i ] = directionKeys[ i ];
			}
			setKeys( combinedKeys );
		} else {
			setKeys( directionKeys );
		}
	}

	protected Point3[] getMoveDirection( InputState input ) {
		MovementType[] movementTypes = MovementType.values();
		Point3[] moveDirs = new Point3[ movementTypes.length ];
		for( int i = 0; i < moveDirs.length; i++ ) {
			moveDirs[ i ] = new Point3( 0.0d, 0.0d, 0.0d );
		}
		for( MovementKey directionKey : this.directionKeys ) {
			if( input.isKeyDown( directionKey.keyValue ) ) {
				Point3 multipliedPoint = Point3.createMultiplication( directionKey.movementDescription.direction.getVector(), directionKey.directionMultiplier );
				moveDirs[ directionKey.movementDescription.type.getIndex() ].add( multipliedPoint );
			}
		}
		return moveDirs;
	}

	private void moveTransformable( Point3[] movementAmounts ) {
		for( int i = 0; i < movementAmounts.length; i++ ) {
			if( !( ( movementAmounts[ i ].x == 0.0d ) && ( movementAmounts[ i ].y == 0.0d ) && ( movementAmounts[ i ].z == 0.0d ) ) ) {
				MovementType movementType = MovementType.getMovementTypeForIndex( i );
				if( movementType != null ) {
					movementType.applyTranslation( this.manipulatedTransformable, movementAmounts[ i ] );
					if( this.bounds != null ) {
						Point3 currentPos = this.manipulatedTransformable.getTranslation( AsSeenBy.SCENE );
						if( currentPos.x > this.bounds.getXMaximum() ) {
							currentPos.x = this.bounds.getXMaximum();
						}
						if( currentPos.x < this.bounds.getXMinimum() ) {
							currentPos.x = this.bounds.getXMinimum();
						}
						if( currentPos.y > this.bounds.getYMaximum() ) {
							currentPos.y = this.bounds.getYMaximum();
						}
						if( currentPos.y < this.bounds.getYMinimum() ) {
							currentPos.y = this.bounds.getYMinimum();
						}
						if( currentPos.z > this.bounds.getZMaximum() ) {
							currentPos.z = this.bounds.getZMaximum();
						}
						if( currentPos.z < this.bounds.getZMinimum() ) {
							currentPos.z = this.bounds.getZMinimum();
						}

						this.manipulatedTransformable.setTranslationOnly( currentPos, AsSeenBy.SCENE );
					}
				}
			}
		}
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Do nothing
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		double currentTime = System.currentTimeMillis() * .001d;
		if( ( currentTime - this.startTime ) < CLICK_TIME ) {
			double distanceToMove = CLICK_TIME * MOVEMENT_RATE;
			Point3 positionDif = Point3.createSubtraction( this.manipulatedTransformable.getAbsoluteTransformation().translation, initialPoint );
			double distanceAlreadyMoved = positionDif.calculateMagnitude();
			if( distanceToMove > distanceAlreadyMoved ) {
				Point3[] translateDirections = getMoveDirection( previousInput );
				for( Point3 direction : translateDirections ) {
					direction.multiply( distanceToMove );
				}
				this.manipulatedTransformable.setTranslationOnly( initialPoint, this.manipulatedTransformable.getRoot() );
				moveTransformable( translateDirections );
			}
		}
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( this.manipulatedTransformable != null ) {
			this.startTime = System.currentTimeMillis() * .001d;
			this.initialPoint.set( this.manipulatedTransformable.getAbsoluteTransformation().translation );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
	}

	@Override
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
		if( this.manipulatedTransformable != null ) {
			Point3[] translateDirections = getMoveDirection( currentInput );
			for( Point3 direction : translateDirections ) {
				direction.multiply( MOVEMENT_RATE * dTime );
			}
			moveTransformable( translateDirections );
		}

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

	private Point3 initialPoint = new Point3();
	private double startTime = 0.0d;
	private MovementKey[] directionKeys;

	private AxisAlignedBox bounds;
}
