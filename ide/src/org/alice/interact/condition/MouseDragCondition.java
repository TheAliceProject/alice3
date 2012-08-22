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
package org.alice.interact.condition;

import java.awt.Point;

import org.alice.interact.InputState;
import org.alice.interact.ModifierMask;

/**
 * @author David Culyba
 */
public class MouseDragCondition extends MousePickBasedCondition {

	protected static final double MIN_MOUSE_MOVE = 2.0d;
	protected Point mouseDownLocation;
	protected boolean hasStarted = false;

	public MouseDragCondition( int mouseButton, PickCondition pickCondition )
	{
		this( mouseButton, pickCondition, null );
	}

	public MouseDragCondition( int mouseButton, PickCondition pickCondition, ModifierMask modifierMask )
	{
		super( mouseButton, pickCondition, modifierMask );
	}

	@Override
	public boolean stateChanged( InputState currentState, InputState previousState ) {
		//Null out the cached mouseDownLocation when the current state becomes invalid
		if( !testInputs( currentState ) )
		{
			this.mouseDownLocation = null;
		}
		return ( super.stateChanged( currentState, previousState ) || !currentState.getMouseLocation().equals( previousState.getMouseLocation() ) );
	}

	@Override
	public boolean isRunning( InputState currentState, InputState previousState ) {
		if( this.hasStarted && testState( currentState ) && testState( previousState ) )
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean justStarted( InputState currentState, InputState previousState )
	{
		boolean testClickVal = testInputsAndPick( currentState );
		boolean testPreviousInputVal = testInputs( previousState );
		//System.out.println("Checking justStarted in mouse drag.\n  click val: "+testClickVal+", previous input: "+testPreviousInputVal);
		if( testClickVal && !testPreviousInputVal )
		{
			//			System.out.println("Setting mouseDownLocation: "+this.hashCode());
			this.mouseDownLocation = new Point( currentState.getMouseLocation() );
		}
		boolean testCurrentInputs = testInputs( currentState );
		//System.out.println("  current input: "+testCurrentInputs);
		if( testCurrentInputs )
		{
			if( ( this.mouseDownLocation != null ) && ( currentState.getMouseLocation().distance( this.mouseDownLocation ) >= MIN_MOUSE_MOVE ) )
			{
				//				System.out.println("valid drag: "+this.hashCode());
				this.mouseDownLocation = null;
				this.hasStarted = true;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean justEnded( InputState currentState, InputState previousState ) {
		if( this.hasStarted && !testState( currentState ) && testState( previousState ) )
		{
			this.hasStarted = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean clicked( InputState currentState, InputState previousState ) {
		//		if (!this.hasStarted && !testState(currentState) && testState(previousState))
		//		{
		//			if (!testMouse(currentState) && testMouse(previousState))
		//			{
		//				return true;
		//			}
		//		}
		return false;
	}

}
