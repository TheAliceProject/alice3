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
package org.alice.interact;

import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class TranslateTransformable {
	protected static double MOVEMENT_RATE = 5.0d;
	protected static double CLICK_TIME = .1d;
	protected static double CLICK_MOVE_AMOUNT = .2d;
	
	protected Transformable transformable = null;
	
	protected Point3 initialPoint = new Point3();
	
	public TranslateTransformable( Transformable transformable )
	{
		this.transformable = transformable;
	}
	
	public void setInitialPoint( Point3 point )
	{
		this.initialPoint.set( point );
	}
	
}
