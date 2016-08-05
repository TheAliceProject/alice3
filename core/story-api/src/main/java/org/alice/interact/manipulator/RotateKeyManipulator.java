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
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementKey;
import org.alice.interact.MovementType;
import org.alice.interact.handle.HandleSet;

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author David Culyba
 */
public abstract class RotateKeyManipulator extends AbstractManipulator {

	private static final double TURN_RATE = 5.0d;
	private static final double CLICK_TIME = .1d;
	private static final double CLICK_MOVE_AMOUNT = .2d;

	public RotateKeyManipulator( MovementKey[] directionKeys ) {
		this.rotationKeys = directionKeys;
		java.util.List<MovementDirection> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( MovementKey direction : directionKeys ) {
			//necessary check?
			if( list.contains( direction.movementDescription.direction ) ) {
				//pass
			} else {
				list.add( direction.movementDescription.direction );
			}
		}
		this.rotateAxes = java.util.Collections.unmodifiableList( list );
	}

	@Override
	public String getUndoRedoDescription() {
		return "Object Rotate";
	}

	protected double[][] getRotateAmount( InputState input ) {
		MovementType[] movementTypes = MovementType.values();
		double[][] rotateDirs = new double[ this.rotateAxes.size() ][ movementTypes.length ];
		for( int i = 0; i < rotateDirs.length; i++ ) {
			for( int j = 0; j < rotateDirs[ i ].length; j++ ) {
				rotateDirs[ i ][ j ] = 0.0d;
			}
		}
		for( MovementKey rotationKey : this.rotationKeys ) {
			if( input.isKeyDown( rotationKey.keyValue ) ) {
				int typeIndex = rotationKey.movementDescription.type.getIndex();
				int axisIndex = this.rotateAxes.indexOf( rotationKey.movementDescription.direction );

				rotateDirs[ axisIndex ][ typeIndex ] += rotationKey.directionMultiplier;
			}
		}
		return rotateDirs;
	}

	private void rotateTransformable( double[][] rotateAmounts ) {
		for( int axisIndex = 0; axisIndex < rotateAmounts.length; axisIndex++ ) {
			for( int typeIndex = 0; typeIndex < rotateAmounts[ axisIndex ].length; typeIndex++ ) {
				if( rotateAmounts[ axisIndex ][ typeIndex ] != 0.0d ) {
					Vector3 rotateAxis = this.rotateAxes.get( axisIndex ).getVector();
					Angle rotateAngle = new AngleInDegrees( rotateAmounts[ axisIndex ][ typeIndex ] );
					MovementType movementType = MovementType.getMovementTypeForIndex( typeIndex );
					if( movementType != null ) {
						movementType.applyRotation( this.manipulatedTransformable, rotateAxis, rotateAngle );
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
			double amountToRotate = CLICK_TIME * TURN_RATE;
			double[][] rotateAmounts = getRotateAmount( previousInput );
			for( int i = 0; i < rotateAmounts.length; i++ ) {
				for( int j = 0; j < rotateAmounts[ i ].length; j++ ) {
					rotateAmounts[ i ][ j ] *= amountToRotate;
				}
			}
			this.manipulatedTransformable.setTranslationOnly( initialPoint, this.manipulatedTransformable.getRoot() );
			rotateTransformable( rotateAmounts );
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
		//		if (this.manipulatedTransformable != null)
		//		{
		//			edu.cmu.cs.dennisc.math.AffineMatrix4x4 transform = this.manipulatedTransformable.localTransformation.getValue();
		//			int xDelta = currentInput.getMouseLocation().x - previousInput.getMouseLocation().x;
		//			int yDelta = currentInput.getMouseLocation().y - previousInput.getMouseLocation().y;
		//			transform.applyTranslation( xDelta*.05d, yDelta*-.05d, 0.0d);
		//			this.manipulatedTransformable.setLocalTransformation( transform );
		//		}
	}

	@Override
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
		if( this.manipulatedTransformable != null ) {
			double[][] rotateAmounts = getRotateAmount( currentInput );
			for( int i = 0; i < rotateAmounts.length; i++ ) {
				for( int j = 0; j < rotateAmounts[ i ].length; j++ ) {
					rotateAmounts[ i ][ j ] *= TURN_RATE * dTime;
				}
			}
			rotateTransformable( rotateAmounts );
		}

	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

	private Point3 initialPoint = new Point3();
	private double startTime = 0.0d;
	private final MovementKey[] rotationKeys;
	private final java.util.List<MovementDirection> rotateAxes;
}
