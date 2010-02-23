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
public class Elder extends Person {
	protected Elder( Gender gender ) { 
		super( LifeStage.ELDER, gender );
	}
	public Elder() { 
		this( null );
	}
	@Override
	public Boolean isPregnant() {
		return Boolean.FALSE;
	}
	@Override
	protected FiniteStateMachine.State getInitialState() {
		return null;
	}
	@Override
	protected FiniteStateMachine.State getNeutralState() {
		return null;
	}

	@Override
	protected edu.cmu.cs.dennisc.math.AxisAlignedBox getLocalAxisAlignedMinimumBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		final double X = 0.208;
		final double Y = 1.7;
		final double Z = 0.131;
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: Elder getLocalAxisAlignedMinimumBoundingBox" );
		rv.setMinimum( -X, 0.0, -Z );
		rv.setMaximum( +X,   Y, +Z );
		return rv;
	}	
	
}
