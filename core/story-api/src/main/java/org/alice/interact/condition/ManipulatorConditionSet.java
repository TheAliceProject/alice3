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
package org.alice.interact.condition;

import org.alice.interact.InputState;
import org.alice.interact.manipulator.AbstractManipulator;

/**
 * @author David Culyba
 */
public final class ManipulatorConditionSet {
	private static enum RunningState {
		JUST_STARTED,
		IS_RUNNING,
		JUST_ENDED,
		CHANGED,
		CLICKED,
	}

	public ManipulatorConditionSet( AbstractManipulator manipulator, String name ) {
		this.name = name;
		this.manipulator = manipulator;
	}

	public ManipulatorConditionSet( AbstractManipulator manipulator ) {
		this( manipulator, "NO NAME" );
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public void addCondition( InputCondition inputCondition ) {
		this.inputConditions.add( inputCondition );
	}

	public AbstractManipulator getManipulator() {
		return this.manipulator;
	}

	public void update( InputState current, InputState previous ) {
		for( InputCondition inputCondition : this.inputConditions ) {
			inputCondition.update( current, previous );
		}
	}

	private boolean checkCondition( RunningState state, InputState current, InputState previous ) {
		//		if (previous.isAnyMouseButtonDown() && !current.isAnyMouseButtonDown())
		//		{
		//			System.out.println("stopping? "+this.hashCode());
		//		}
		switch( state ) {
		case CHANGED:
			for( InputCondition inputCondition : this.inputConditions ) {
				if( inputCondition.stateChanged( current, previous ) ) {
					return true;
				}
			}
			break;
		case JUST_STARTED:
			for( InputCondition inputCondition : this.inputConditions ) {
				if( inputCondition.justStarted( current, previous ) ) {
					return true;
				}
			}
			break;
		case IS_RUNNING:
			for( InputCondition inputCondition : this.inputConditions ) {
				if( inputCondition.isRunning( current, previous ) ) {
					return true;
				}
			}
			break;
		case JUST_ENDED:
			for( InputCondition inputCondition : this.inputConditions ) {
				if( inputCondition.justEnded( current, previous ) ) {
					return true;
				}
			}
			break;
		case CLICKED:
			for( InputCondition inputCondition : this.inputConditions ) {
				if( inputCondition.clicked( current, previous ) ) {
					return true;
				}
			}
			break;
		}
		return false;

	}

	public boolean stateChanged( InputState current, InputState previous ) {
		return this.checkCondition( RunningState.CHANGED, current, previous );
	}

	public boolean shouldContinue( InputState current, InputState previous ) {
		return this.checkCondition( RunningState.IS_RUNNING, current, previous );
	}

	public boolean justStarted( InputState current, InputState previous ) {
		boolean someoneIsRunning = this.checkCondition( RunningState.IS_RUNNING, current, previous );
		boolean someoneJustStarted = this.checkCondition( RunningState.JUST_STARTED, current, previous );
		return ( !someoneIsRunning && someoneJustStarted );
	}

	public boolean justEnded( InputState current, InputState previous ) {
		boolean someoneIsRunning = this.checkCondition( RunningState.IS_RUNNING, current, previous );
		boolean someoneJustStarted = this.checkCondition( RunningState.JUST_STARTED, current, previous );
		boolean someoneJustEnded = this.checkCondition( RunningState.JUST_ENDED, current, previous );
		return ( !someoneIsRunning && !someoneJustStarted && someoneJustEnded );
	}

	public boolean clicked( InputState current, InputState previous ) {
		boolean clicked = this.checkCondition( RunningState.CLICKED, current, previous );
		return ( clicked );
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "ManipulatorConditionSet:" + this.name;
	}

	private final String name;
	private final AbstractManipulator manipulator;
	private final java.util.List<InputCondition> inputConditions = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private boolean enabled = true;
}
