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
 * a point light emits rays in all directions.<br>
 * the position is inherent from absolute transformation of its composite.<br>
 * useful in simulating a light bulb.<br>
 * <p>
 * when calculating a light's contribution to the illumination of a vertex, that light's color is multiplied a polynomial function of distance.<br>
 * this polynomial is controllable via constant, linear, and quadratic attenuation properties.<br>
 * <pre>
 *    d = distance from light to vertex position
 *    c = constant attenuation
 *    l = linear attenuation
 *    q = quadratic attenuation
 *
 *                              1
 *    attenuation factor = ------------
 *                         c + ld + qdd
 * </pre>
 * note: the default values of ( constant=1, linear=0, quadratic=0 ) reduce the attenuation factor to 1.
 *
 * @author Dennis Cosgrove
 */
public class PointLight extends Light {
	//todo Double -> Float?
	public final edu.cmu.cs.dennisc.property.DoubleProperty constantAttenuation = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 1.0 );
	//todo Double -> Float?
	public final edu.cmu.cs.dennisc.property.DoubleProperty linearAttenuation = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 0.0 );
	//todo Double -> Float?
	public final edu.cmu.cs.dennisc.property.DoubleProperty quadraticAttenuation = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 0.0 );
}
