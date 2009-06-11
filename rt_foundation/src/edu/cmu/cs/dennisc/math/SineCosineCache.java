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
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class SineCosineCache {
	public double dTheta;
	public double[] cosines;
	public double[] sines;
	public double[] angles;

	public SineCosineCache( int length ) {
		cosines = new double[ length ];
		sines = new double[ length ];
		angles = new double[ length ];
		double theta = 0;
		double dtheta = (Math.PI / 2.0) / cosines.length;
		for( int i = 0; i < length; i++ ) {
			angles[ i ] = theta;
			cosines[ i ] = Math.cos( theta );
			sines[ i ] = Math.sin( theta );
			theta += dtheta;
		}
	}
	
	public double getSine( int quadrant, int i ) {
		int max = sines.length - 1;
		switch( quadrant ) {
		case 0:
			return sines[ i ];
		case 1:
			return sines[ max - i ];
		case 2:
			return -sines[ i ];
		case 3:
			return -sines[ max - i ];
		default:
			throw new IllegalArgumentException();
		}
	}

	public double getCosine( int quadrant, int i ) {
		int max = sines.length - 1;
		switch( quadrant ) {
		case 0:
			return cosines[ i ];
		case 1:
			return -cosines[ max - i ];
		case 2:
			return -cosines[ i ];
		case 3:
			return cosines[ max - i ];
		default:
			throw new IllegalArgumentException();
		}
	}
	
	public double getAngle( int quadrant, int i ) {
		return Math.PI*0.5*quadrant + angles[ i ];
	}
}
