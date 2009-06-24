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
public class CannedAnimation extends edu.cmu.cs.dennisc.animation.AbstractAnimation {
	private edu.cmu.cs.dennisc.nebulous.AnimationControl m_control;
	public CannedAnimation( FiniteStateMachine.Cycle cycle, Model model ) {
		try {
			m_control = new edu.cmu.cs.dennisc.nebulous.AnimationControl( cycle, model.getNebulousModel() );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "license not accepted" );
		} catch( RuntimeException re ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "unable to animate cycle: " + cycle );
		}
	}
	public CannedAnimation( Transition transition, Model model ) {
		try {
			m_control = new edu.cmu.cs.dennisc.nebulous.AnimationControl( transition, model.getNebulousModel() );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			m_control = null;
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "license not accepted" );
		} catch( RuntimeException re ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "unable to animate between: " + transition.getPre(), transition.getPost() );
		}
	}

	@Override
	protected void prologue() {
		if( m_control != null ) {
			m_control.prologue();
		}
	}
	@Override
	protected double update( double deltaSincePrologue, double deltaSinceLastUpdate, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		if( m_control != null ) {
			return m_control.update( deltaSincePrologue );
		} else {
			return 0.0;
		}
	}
	@Override
	protected void epilogue() {
		if( m_control != null ) {
			m_control.epilogue();
		}
	}
}
