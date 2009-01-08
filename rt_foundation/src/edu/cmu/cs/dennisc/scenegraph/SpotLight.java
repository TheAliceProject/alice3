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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * a spot light emits a cone of light; behaving like a point light within its inner beam angle; and falling off exponentially between its inner and outer beam angles.<br>
 * useful in simulating a luxo lamp.<br>
 *
 * @author Dennis Cosgrove
 */
public class SpotLight extends PointLight {
	public final edu.cmu.cs.dennisc.math.property.AngleProperty innerBeamAngle = new edu.cmu.cs.dennisc.math.property.AngleProperty( this, new edu.cmu.cs.dennisc.math.AngleInRadians( 0.4 ) );
	public final edu.cmu.cs.dennisc.math.property.AngleProperty outerBeamAngle = new edu.cmu.cs.dennisc.math.property.AngleProperty( this, new edu.cmu.cs.dennisc.math.AngleInRadians( 0.5 ) );
	public final edu.cmu.cs.dennisc.property.DoubleProperty falloff = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 1.0 );
}
