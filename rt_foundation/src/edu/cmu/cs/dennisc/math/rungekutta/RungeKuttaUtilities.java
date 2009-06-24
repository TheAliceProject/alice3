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
package edu.cmu.cs.dennisc.math.rungekutta;

/**
 * @author Dennis Cosgrove
 */
public class RungeKuttaUtilities {
	public static <E extends Derivative> void rk4( Function< E > f, double t, double dt ) {
		E a = f.evaluate( t );
		E b = f.evaluate( t, dt * 0.5, a );
		E c = f.evaluate( t, dt * 0.5, b );
		E d = f.evaluate( t, dt, c );
		f.update( a, b, c, d, dt );
		f.update();
	}
}
