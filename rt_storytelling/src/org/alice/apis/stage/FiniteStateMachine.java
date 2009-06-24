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

/**
 * @author Dennis Cosgrove
 */
class GenericTransition<E> {
	E m_pre;
	E m_post;
	public GenericTransition( E pre, E post ) {
		m_pre = pre;
		m_post = post;
	}
	E getPre() {
		return m_pre;
	}
	E getPost() {
		return m_post;
	}
}
/**
 * @author Dennis Cosgrove
 */
class Transition extends GenericTransition< FiniteStateMachine.State > {
	private static java.util.Map< FiniteStateMachine.State, java.util.List< Transition > > s_map = new java.util.HashMap< FiniteStateMachine.State, java.util.List<Transition> >();
	public static void add( Transition transition ) {
		FiniteStateMachine.State pre = transition.getPre();
		java.util.List< Transition > list = s_map.get( pre );
		if( list != null ) {
			//pass
		} else {
			list = new java.util.LinkedList< Transition >();
			s_map.put( pre, list );
		}
		list.add( transition );
	}

	public Transition( FiniteStateMachine.State pre, FiniteStateMachine.State post ) {
		super( pre, post );
	}
	
	public static java.util.List< Transition > get( FiniteStateMachine.State state ) {
		return s_map.get( state );
	}
}

/**
 * @author Dennis Cosgrove
 */
class TransitionAB extends GenericTransition< FiniteStateMachine.StateAB > {
	private static java.util.Map< FiniteStateMachine.StateAB, java.util.List< TransitionAB > > s_map = new java.util.HashMap< FiniteStateMachine.StateAB, java.util.List<TransitionAB> >();
	public static void add( TransitionAB transitionAB ) {
		FiniteStateMachine.StateAB pre = transitionAB.getPre();
		java.util.List< TransitionAB > list = s_map.get( pre );
		if( list != null ) {
			//pass
		} else {
			list = new java.util.LinkedList< TransitionAB >();
			s_map.put( pre, list );
		}
		list.add( transitionAB );
	}

	public TransitionAB( FiniteStateMachine.StateAB pre, FiniteStateMachine.StateAB post ) {
		super( pre, post );
	}
}
/**
 * @author Dennis Cosgrove
 */
class TransitionABC extends GenericTransition< FiniteStateMachine.StateABC > {
	private static java.util.Map< FiniteStateMachine.StateABC, java.util.List< TransitionABC > > s_map = new java.util.HashMap< FiniteStateMachine.StateABC, java.util.List<TransitionABC> >();
	public static void add( TransitionABC transitionABC ) {
		FiniteStateMachine.StateABC pre = transitionABC.getPre();
		java.util.List< TransitionABC > list = s_map.get( pre );
		if( list != null ) {
			//pass
		} else {
			list = new java.util.LinkedList< TransitionABC >();
			s_map.put( pre, list );
		}
		list.add( transitionABC );
	}
	public TransitionABC( FiniteStateMachine.StateABC pre, FiniteStateMachine.StateABC post ) {
		super( pre, post );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class FiniteStateMachine {
	public enum BindingAB {
		NOT_BOUND( false ),
		BOUND_TOGETHER( true );
		private boolean m_isABoundToB;
		BindingAB( boolean isABoundToB ) {
			m_isABoundToB = isABoundToB;
		}
		public boolean isABoundToB() {
			return m_isABoundToB;
		}
	}

	public enum BindingABC {
		NOT_BOUND( false, false, false ),
		A_IS_BOUND_TO_B_ONLY( true, false, false ),
		A_IS_BOUND_TO_C_ONLY( false, true, false ),
		B_IS_BOUND_TO_C_ONLY( false, false, true ),
		BOUND_TOGETHER( true, true, true );
		private boolean m_isABoundToB;
		private boolean m_isABoundToC;
		private boolean m_isBBoundToC;
		BindingABC( boolean isABoundToB, boolean isABoundToC, boolean isBBoundToC ) {
			m_isABoundToB = isABoundToB;
			m_isABoundToC = isABoundToC;
			m_isBBoundToC = isBBoundToC;
		}
		public boolean isABoundToB() {
			return m_isABoundToB;
		}
		public boolean isABoundToC() {
			return m_isABoundToC;
		}
		public boolean isBBoundToC() {
			return m_isBBoundToC;
		}
	}

	public interface State {
	}
	public interface StateAB {
		State getA();
		State getB();
	}
	public interface StateABC {
		State getA();
		State getB();
		State getC();
	}
	
	public interface Cycle {
		State getState();
	}
	public interface CycleAB {
		StateAB getStateAB();
		
	}
	public interface CycleABC {
		StateABC getStateABC();
	}
	
	public static void addTransition( State pre, State post ) {
		Transition.add( new Transition( pre, post ) );
	}
	public static void addTransitionsBackAndForth( State pre, State post ) {
		addTransition( pre, post );
		addTransition( post, pre );
	}

	public static void addTransitionAB( StateAB pre, StateAB post ) {
		TransitionAB.add( new TransitionAB( pre, post ) );
	}
	public static void addTransitionABsBackAndForth( StateAB pre, StateAB post ) {
		addTransitionAB( pre, post );
		addTransitionAB( post, pre );
	}
	public static void addTransitionABC( StateABC pre, StateABC post ) {
		TransitionABC.add( new TransitionABC( pre, post ) );
	}
	public static void addTransitionABCsBackAndForth( StateABC pre, StateABC post ) {
		addTransitionABC( pre, post );
		addTransitionABC( post, pre );
	}
	
	
	private Model m_owner = null;
	
	public FiniteStateMachine( Model owner ) {
		m_owner = owner;
	}

	private java.util.Stack< Transition > getRequiredTransitionAsToChangeState( State other ) {
		assert other != null;

		State currentState = m_owner.getCurrentState();
		if( currentState == other ) {
			return new java.util.Stack< Transition >();
		} else {
			java.util.Map< State, Transition > transitionMap = new java.util.HashMap< State, Transition >();
			java.util.Queue< State > queue = new java.util.LinkedList< State >();
			queue.add( currentState );
			
			while( queue.size() > 0 ) {
				State prevState = queue.remove();
				java.util.List< Transition > transitions = Transition.get( prevState );
				if( transitions != null ) {
					for( Transition transitionA : transitions ) {
						State postState = transitionA.getPost();
						if( transitionMap.containsKey( postState ) ) {
							//pass
						} else {
							queue.add( postState );
							transitionMap.put( postState, transitionA );
						}
					}
				}
			}
			
			java.util.Stack< Transition > stack = new java.util.Stack< Transition >();
			Transition transition = transitionMap.get( other );

			if( transition != null ) {
				while( true ) {
					stack.push( transition );
					State preState = transition.getPre();
					if( preState != currentState ) {
						transition = transitionMap.get( transition.getPre() );
					} else {
						break;
					}
				}
				return stack;
			} else {
				return null;
			}
		}
	}
	
	private void perform( java.util.Stack< Transition > transitions ) {
		if( transitions != null ) {
			while( transitions.size() > 0 ) {
				State preState = m_owner.getCurrentState();
				Transition transition = transitions.pop();
				State postState = transition.getPost();
				if( transition != null ) {
					m_owner.handleStateChange( transition );
				} else {
					m_owner.handleStateChange( preState, postState );
				}
				m_owner.setCurrentState( postState );
			}
		}
	}	
	
	public void setState( State state ) {
		perform( getRequiredTransitionAsToChangeState( state ) );
	}
	public void setState( StateAB state, Model b, BindingAB binding ) {
	}
	public void setState( StateABC state, Model b, Model c, BindingABC binding ) {
	}
}
