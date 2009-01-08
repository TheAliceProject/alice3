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
public class ClippedZPlane implements java.io.Serializable {
	private double m_xMinimum = -1;
	private double m_yMinimum = Double.NaN;
	private double m_xMaximum = 1;
	private double m_yMaximum = Double.NaN;

	public ClippedZPlane() {
		this( -1, Double.NaN, 1, Double.NaN );
	}
	public ClippedZPlane( double xMinimum, double yMinimum, double xMaximum, double yMaximum ) {
		set( xMinimum, yMinimum, xMaximum, yMaximum );
	}
	public ClippedZPlane( ClippedZPlane other ) {
		set( other );
	}
	public ClippedZPlane( ClippedZPlane other, java.awt.Rectangle viewport ) {
		set( other, viewport );
	}

	public void set( double xMinimum, double yMinimum, double xMaximum, double yMaximum ) {
		setXMinimum( xMinimum );
		setYMinimum( yMinimum );
		setXMaximum( xMaximum );
		setYMaximum( yMaximum );
	}
	public void set( ClippedZPlane other ) {
		set( other.m_xMinimum, other.m_yMinimum, other.m_xMaximum, other.m_yMaximum );
	}
	public void set( ClippedZPlane other, java.awt.Rectangle viewport ) {
		double minX = other.getXMinimum();
		double minY = other.getYMinimum();
		double maxX = other.getXMaximum();
		double maxY = other.getYMaximum();
		if( Double.isNaN( minX ) || Double.isNaN( maxX ) ) {
			if( Double.isNaN( minY ) || Double.isNaN( maxY ) ) {
				minY = -1;
				maxY = 1;
			}
			double factor = viewport.width / (double) viewport.height;
			minX = factor * minY;
			maxX = factor * maxY;
		} else {
			if( Double.isNaN( minY ) || Double.isNaN( maxY ) ) {
				double factor = viewport.height / (double) viewport.width;
				minY = factor * minX;
				maxY = factor * maxY;
			}
		}
		setXMinimum( minX );
		setYMinimum( minY );
		setXMaximum( maxX );
		setYMaximum( maxY );
	}

	public double getXMinimum() {
		return m_xMinimum;
	}
	public void setXMinimum( double xMinimum ) {
		m_xMinimum = xMinimum;
	}
	public double getXMaximum() {
		return m_xMaximum;
	}
	public void setXMaximum( double xMaximum ) {
		m_xMaximum = xMaximum;
	}

	public double getYMinimum() {
		return m_yMinimum;
	}
	public void setYMinimum( double yMinimum ) {
		m_yMinimum = yMinimum;
	}
	public double getYMaximum() {
		return m_yMaximum;
	}
	public void setYMaximum( double yMaximum ) {
		m_yMaximum = yMaximum;
	}
}
