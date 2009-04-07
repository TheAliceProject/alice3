/**
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
package org.alice.interact.manipulator;

import java.awt.Point;

import org.alice.interact.InputState;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Vector2;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class CameraDragDriveManipulator extends CameraManipulator {

	private Point initialPoint = new Point();
	private Transformable standUpReference = new Transformable();
	
	@Override
	public void doDataUpdateManipulator( InputState currentInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEndManipulator( InputState endInput, InputState previousInput ) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doStartManipulator( InputState startInput ) {
		if (this.getCamera() != null)
		{
			this.initialPoint.setLocation( startInput.getMouseLocation() );
			this.standUpReference.setParent( this.getCamera().getParent() );
			this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
			this.standUpReference.setAxesOnlyToStandUp();
			return true;
		}
		return false;
	}

	@Override
	public void doTimeUpdateManipulator( double time, InputState currentInput ) {
		Vector2 toMouse = new Vector2( currentInput.getMouseLocation().x, currentInput.getMouseLocation().y);
		toMouse.subtract( new Vector2( initialPoint.x, initialPoint.y ) );
		double magnitude = toMouse.calculateMagnitude();
		toMouse.divide( magnitude );
		System.out.println("to mouse "+toMouse+", mag: "+magnitude);

	}

}
