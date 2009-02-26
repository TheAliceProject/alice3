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

import org.alice.apis.moveandturn.gallery.GalleryRootUtilities;

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public abstract class Model extends org.alice.apis.moveandturn.Model {
	static {
		java.io.File root = GalleryRootUtilities.calculateGalleryRootDirectory( Model.class, "/Alice/3.beta.0025/gallery", "gallery", "assets", "org.alice.apis.stage", "Cannot find The Sims (TM) 2 Art Assets", "Alice" );
		java.io.File directory = new java.io.File( root, "assets/org.alice.apis.stage" ); 
		if( directory.exists() ) {
			for( java.io.File file : directory.listFiles() ) {
				try {
					if( file.getName().endsWith( "txt" ) ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.nebulous.Manager.addBundle( file );
					}
				} catch( Throwable t ) {
					t.printStackTrace();
				}
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog( null, "cannot find The Sims (TM) 2 Art Assets" );
		}
	}

//public abstract class Model extends edu.wustl.cse.lookingglass.apis.walkandtouch.Person {
	protected abstract boolean isEasilyTransformed();
	protected abstract edu.cmu.cs.dennisc.nebulous.Model getNebulousModel();
	
	protected abstract FiniteStateMachine.State getInitialState();
	protected abstract FiniteStateMachine.State getNeutralState();

	private FiniteStateMachine.State m_state = getInitialState();

	public FiniteStateMachine.State getCurrentState() {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "getCurrentState:", m_state );
		return m_state;
	}
	public void setCurrentState( FiniteStateMachine.State state ) {
		m_state = state;
	}

	private FiniteStateMachine m_fsm = new FiniteStateMachine( this );

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void handleStateChange( Transition transition ) {
		perform( new CannedAnimation( transition, this ) );
		setCurrentState( transition.getPost() );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void handleStateChange( FiniteStateMachine.State currentState, FiniteStateMachine.State nextState ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleStateChange", currentState, nextState );
		//perform( new StateAnimation( this, nextState ) );
		setCurrentState( nextState );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void performStateTransition( FiniteStateMachine.State state ) {
		m_fsm.setState( state );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void routeTo( org.alice.apis.moveandturn.Transformable target ) {
		//todo
		perform( new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( getSGTransformable(), target.getSGTransformable(), null, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity() ) );
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void perform( FiniteStateMachine.Cycle cycle ) {
		m_fsm.setState( cycle.getState() );
		perform( new CannedAnimation( cycle, this ) );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void perform( FiniteStateMachine.CycleAB cycleAB, Model b, FiniteStateMachine.BindingAB binding ) {
		m_fsm.setState( cycleAB.getStateAB(), b, binding );
//		do together
//			perform( new CannedAnimation( cycleABC.getA(), this ) );
//			perform( new CannedAnimation( cycleABC.getB(), b ) );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void perform( FiniteStateMachine.CycleABC cycleABC, Model b, Model c, FiniteStateMachine.BindingABC binding ) {
		m_fsm.setState( cycleABC.getStateABC(), b, c, binding );
//		do together
//			perform( new CannedAnimation( cycleABC.getA(), this ) );
//			perform( new CannedAnimation( cycleABC.getB(), b ) );
//			perform( new CannedAnimation( cycleABC.getC(), c ) );
	}
	
	private class SetStateRunnable implements Runnable {
		private Model m_subject;
		private FiniteStateMachine.State m_state;

		public SetStateRunnable( Model subject, FiniteStateMachine.State state ) {
			m_subject = subject;
			m_state = state;
		}
		protected org.alice.apis.moveandturn.Transformable getSubject() {
			return m_subject;
		}
		protected FiniteStateMachine.State getState() {
			return m_state;
		}
		public void run() {
			throw new RuntimeException( "todo" );
			//m_subject.performStateTransitionByPassingFSM( m_state );
		}
	}

	private class RouteToAndSetStateRunnable extends SetStateRunnable {
		private org.alice.apis.moveandturn.Transformable m_target;

		public RouteToAndSetStateRunnable( Model subject, FiniteStateMachine.State state, Model target ) {
			super( subject, state );
			m_target = target;
		}
		@Override
		public void run() {
			edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( new Runnable() {
				public void run() {
					Model.this.routeTo( m_target );
				}
			}, new Runnable() {
				public void run() {
					throw new RuntimeException( "todo" );
					//getSubject().performStateTransitionByPassingFSM( getState() );
				}
			} );
		}
	}

	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void performStateTransition( FiniteStateMachine.StateAB state, Model b, FiniteStateMachine.BindingAB binding ) {
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( 
				new RouteToAndSetStateRunnable( this, state.getA(), b ), 
				new SetStateRunnable( b, state.getB() ) 
		);

		//m_fsm.setState( state, b );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public void performStateTransition(  FiniteStateMachine.StateABC state, Model b, Model c, FiniteStateMachine.BindingABC binding ) {
		edu.cmu.cs.dennisc.alice.virtualmachine.DoTogether.invokeAndWait( 
				new RouteToAndSetStateRunnable( this, state.getA(), b ), 
				new SetStateRunnable( b, state.getB() ),
				new RouteToAndSetStateRunnable( c, state.getC(), b ) );
		//m_fsm.setState( state, b, c );
	}

}
