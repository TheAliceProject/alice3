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

//todo: fix names
/**
 * @author Dennis Cosgrove
 */
public class ScaleUtilities {
	public static Matrix3x3 set_AffectScaleXOnly( Matrix3x3 rv, double x ) {
		rv.right.x = x;
		return rv;
	}
	public static Matrix3x3 set_AffectScaleYOnly( Matrix3x3 rv, double y ) {
		rv.up.y = y;
		return rv;
	}
	public static Matrix3x3 set_AffectScaleZOnly( Matrix3x3 rv, double z ) {
		rv.backward.z = z;
		return rv;
	}
	public static Matrix3x3 set_AffectScaleOnly( Matrix3x3 rv, double x, double y, double z ) {
		set_AffectScaleXOnly( rv, x );
		set_AffectScaleYOnly( rv, y );
		set_AffectScaleZOnly( rv, z );
		return rv;
	}
	public static Matrix3x3 set_AffectScaleOnly( Matrix3x3 rv, Tuple3 t ) {
		return set_AffectScaleOnly( rv, t.x, t.y, t.z );
	}

	public static Matrix3x3 newScaleMatrix3d( Matrix3x3 rv, double x, double y, double z ) {
		rv.right.set   ( x, 0, 0 );
		rv.up.set      ( 0, y, 0 );
		rv.backward.set( 0, 0, z );
		return rv;
	}
	public static Matrix3x3 newScaleMatrix3d( double x, double y, double z ) {
		return newScaleMatrix3d( Matrix3x3.createNaN(), x, y, z );
	}
	public static Matrix3x3 newScaleMatrix3d( Matrix3x3 rv, Tuple3 t ) {
		return newScaleMatrix3d( rv, t.x, t.y, t.z );
	}
	public static Matrix3x3 newScaleMatrix3d( Tuple3 t ) {
		return newScaleMatrix3d( Matrix3x3.createNaN(), t );
	}

	public static Matrix3x3 applyScale( Matrix3x3 rv, double x, double y, double z ) {
		rv.applyMultiplication( newScaleMatrix3d( x, y, z ) );
		return rv;
	}
	public static Matrix3x3 applyScale( Matrix3x3 rv, Tuple3 t ) {
		return applyScale( rv, t.x, t.y, t.z );
	}

	// todo: add setScales when rv is not the identity
	public static Matrix4x4 set_AffectScaleXOnly( Matrix4x4 rv, double x ) {
		rv.right.x = x;
		return rv;
	}
	public static Matrix4x4 set_AffectScaleYOnly( Matrix4x4 rv, double y ) {
		rv.up.y = y;
		return rv;
	}
	public static Matrix4x4 set_AffectScaleZOnly( Matrix4x4 rv, double z ) {
		rv.backward.z = z;
		return rv;
	}
	public static Matrix4x4 set_AffectScaleOnly( Matrix4x4 rv, double x, double y, double z ) {
		set_AffectScaleXOnly( rv, x );
		set_AffectScaleYOnly( rv, y );
		set_AffectScaleZOnly( rv, z );
		return rv;
	}
	public static Matrix4x4 set_AffectScaleOnly( Matrix4x4 rv, Tuple3 t ) {
		return set_AffectScaleOnly( rv, t.x, t.y, t.z );
	}

	public static Matrix4x4 newScaleMatrix4d( Matrix4x4 rv, double x, double y, double z ) {
		rv.right.x = x;
		rv.up.x = 0;
		rv.backward.x = 0;
		rv.translation.x = 0;
		rv.right.y = 0;
		rv.up.y = y;
		rv.backward.y = 0;
		rv.translation.y = 0;
		rv.right.z = 0;
		rv.up.z = 0;
		rv.backward.z = z;
		rv.translation.z = 0;
		rv.right.w = 0;
		rv.up.w = 0;
		rv.backward.w = 0;
		rv.translation.w = 1;
		return rv;
	}
	public static Matrix4x4 newScaleMatrix4d( double x, double y, double z ) {
		return newScaleMatrix4d( new Matrix4x4(), x, y, z );
	}
	public static Matrix4x4 newScaleMatrix4d( Matrix4x4 rv, Tuple3 t ) {
		return newScaleMatrix4d( rv, t.x, t.y, t.z );
	}
	public static Matrix4x4 newScaleMatrix4d( Tuple3 t ) {
		return newScaleMatrix4d( new Matrix4x4(), t );
	}

	public static Matrix4x4 applyScale( Matrix4x4 rv, double x, double y, double z ) {
		rv.applyMultiplication( newScaleMatrix4d( x, y, z ) );
		return rv;
	}
	public static Matrix4x4 applyScale( Matrix4x4 rv, Tuple3 t ) {
		return applyScale( rv, t.x, t.y, t.z );
	}
}
