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
public class EulerNumbers implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public double pitch;
	public double yaw;
	public double roll;

	public EulerNumbers() {
		pitch = 0.0;
		yaw = 0.0;
		roll = 0.0;
	}
	public EulerNumbers( double[] array, EulerAngles.Order order ) {
		set( array, order );
	}
	public EulerNumbers( double a, double b, double c, EulerAngles.Order order ) {
		set( a, b, c, order );
	}
	public EulerNumbers( EulerNumbers other ) {
		set( other );
	}

	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		pitch = binaryDecoder.decodeDouble();
		yaw = binaryDecoder.decodeDouble();
		roll = binaryDecoder.decodeDouble();
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		binaryEncoder.encode( pitch );
		binaryEncoder.encode( yaw );
		binaryEncoder.encode( roll );
	}

	public boolean isNaN() {
		return Double.isNaN( pitch ) || Double.isNaN( yaw ) || Double.isNaN( roll );
	}
	public void setNaN() {
		pitch = Double.NaN;
		yaw = Double.NaN;
		roll = Double.NaN;
	}
	
	public void set( double a, double b, double c, EulerAngles.Order order ) {
		this.pitch = order.getPitch( a, b, c );
		this.yaw = order.getYaw( a, b, c );
		this.roll = order.getRoll( a, b, c );
	}
	public void set( double[] pyr, EulerAngles.Order order ) {
		set( pyr[0], pyr[1], pyr[2], order );
	}
	public void set( EulerNumbers other ) {
		this.pitch = other.pitch;
		this.yaw = other.yaw;
		this.roll = other.roll;
	}

	@Override
	public String toString() {
		return EulerNumbers.class.getName()+"[pitch=" + pitch + ",yaw=" + yaw + ",roll=" + roll + "]";
	}

	public static EulerNumbers valueOf( String s ) {
		String[] markers = { EulerNumbers.class.getName()+"[pitch=", ",yaw=", ",roll=", "]" };
		double[] values = new double[ markers.length - 1 ];
		for( int i = 0; i < values.length; i++ ) {
			int begin = s.indexOf( markers[ i ] ) + markers[ i ].length();
			int end = s.indexOf( markers[ i + 1 ] );
			values[ i ] = Double.valueOf( s.substring( begin, end ) ).doubleValue();
		}
		return new EulerNumbers( values, EulerAngles.Order.PITCH_YAW_ROLL );
	}
	
}
