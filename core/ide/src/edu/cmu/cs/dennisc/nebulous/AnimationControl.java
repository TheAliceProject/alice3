/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class AnimationControl {
	private native void initialize( Object o, Model model );

	public AnimationControl( Object o, Model model ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		Manager.initializeIfNecessary();
		initialize( o, model );
	}

	public native void prologue();

	public native double update( double deltaSincePrologue );

	public native void epilogue();
}
