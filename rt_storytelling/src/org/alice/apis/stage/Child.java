/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.stage;

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class Child extends Person {
	public enum StateA implements FiniteStateMachine.State {
		HANDS_AT_SIDES,
	}
	public enum CycleA implements FiniteStateMachine.Cycle {
		GET_ATTENTION( StateA.HANDS_AT_SIDES ),
		WAVE( StateA.HANDS_AT_SIDES ),
		;
		private StateA m_stateA;
		CycleA( StateA stateA ) {
			m_stateA = stateA;
		}
		public FiniteStateMachine.State getState() {
			return m_stateA;
		}
	}

	protected Child( Gender gender ) { 
		super( LifeStage.CHILD, gender );
	}
	public Child() { 
		this( null );
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	public Boolean isPregnant() {
		return Boolean.FALSE;
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	protected FiniteStateMachine.State getInitialState() {
		return Child.StateA.HANDS_AT_SIDES;
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	@Override
	protected FiniteStateMachine.State getNeutralState() {
		return Child.StateA.HANDS_AT_SIDES;
	}
	
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void getAttention() {
		perform( Child.CycleA.GET_ATTENTION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void wave() {
		perform( Child.CycleA.WAVE );
	}
}
