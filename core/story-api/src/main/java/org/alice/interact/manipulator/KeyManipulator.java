/*******************************************************************************
 * Copyright (c) 2018 Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.math.Point3;
import org.alice.interact.InputState;
import org.alice.interact.MovementKey;
import org.alice.interact.handle.HandleSet;

public abstract class KeyManipulator extends AbstractManipulator {

	private static final double RATE = 5.0d;
	private static final double CLICK_TIME = .1d;

	KeyManipulator( MovementKey[] keys ) {
		this.keys = keys;
	}

	@Override
	public void doClickManipulator( InputState clickInput, InputState previousInput ) {
		//Key only. Do nothing.
	}

	@Override
	protected HandleSet getHandleSetToEnable() {
		return null;
	}

	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		//Key only. Do nothing.
	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if( manipulatedTransformable != null ) {
			startTime = System.currentTimeMillis() * .001d;
			initialPoint.set( manipulatedTransformable.getAbsoluteTransformation().translation );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void doTimeUpdateManipulator( double dTime, InputState currentInput ) {
		if( manipulatedTransformable != null ) {
			applyInput( currentInput, RATE * dTime );
		}
	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		double currentTime = System.currentTimeMillis() * .001d;
		double amountToMove = CLICK_TIME * RATE;
		if( shouldApplyEnding( currentTime, amountToMove ) ) {
			manipulatedTransformable.setTranslationOnly( initialPoint, manipulatedTransformable.getRoot() );
			applyInput( previousInput, amountToMove );
		}
	}

	protected boolean shouldApplyEnding( double currentTime, double amountToMove ) {
		return ( currentTime - startTime ) < CLICK_TIME;
	}

	private void applyInput( InputState input, double amountToMove ) {
		for( MovementKey key : keys ) {
			if( input.isKeyDown( key.keyValue ) ) {
				manipulate( amountToMove, key );
			}
		}
	}

	protected abstract void manipulate( double amountToMove, MovementKey key );

	Point3 initialPoint = new Point3();
	private double startTime = 0.0d;
	private MovementKey[] keys;
}
