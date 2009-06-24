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
public class ForwardAndUpGuide implements Orientation, edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable {
	public final Vector3 forward = new Vector3();
	public final Vector3 upGuide = new Vector3();

	private ForwardAndUpGuide() {
	}
	public ForwardAndUpGuide( Vector3 forward, Vector3 upGuide ) {
		setValueFrom( forward, upGuide );
	}
	public ForwardAndUpGuide( OrthogonalMatrix3x3 other ) {
		setValue( other );
	}
	public ForwardAndUpGuide( UnitQuaternion other ) {
		setValue( other );
	}
	public ForwardAndUpGuide( AxisRotation other ) {
		setValue( other );
	}
	public ForwardAndUpGuide( EulerAngles other ) {
		setValue( other );
	}
	public ForwardAndUpGuide( ForwardAndUpGuide other ) {
		setValue( other );
	}
	public OrthogonalMatrix3x3 createOrthogonalMatrix3x3() {
		return new OrthogonalMatrix3x3( this );
	}
	public UnitQuaternion createUnitQuaternion() {
		return new UnitQuaternion( this );
	}
	public AxisRotation createAxisRotation() {
		return new AxisRotation( this );
	}
	public EulerAngles createEulerAngles() {
		return new EulerAngles( this );
	}
	public ForwardAndUpGuide createForwardAndUpGuide() {
		return new ForwardAndUpGuide( this );
	}
	
	public OrthogonalMatrix3x3 getValue( OrthogonalMatrix3x3 rv ) {
		rv.setValue( this );
		return rv;
	}
	public UnitQuaternion getValue( UnitQuaternion rv ) {
		rv.setValue( this );
		return rv;
	}
	public AxisRotation getValue( AxisRotation rv ) {
		rv.setValue( this );
		return rv;
	}
	public EulerAngles getValue( EulerAngles rv ) {
		rv.setValue( this );
		return rv;
	}
	public ForwardAndUpGuide getValue( ForwardAndUpGuide rv ) {
		rv.setValue( this );
		return rv;
	}
	
	public void decode(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
		forward.decode( binaryDecoder );
		upGuide.decode( binaryDecoder );
	}
	public void encode(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder) {
		forward.encode( binaryEncoder );
		upGuide.encode( binaryEncoder );
	}

	//NaN
	public static ForwardAndUpGuide setReturnValueToNaN( ForwardAndUpGuide rv ) {
		rv.forward.setNaN();
		rv.upGuide.setNaN();
		return rv;
	}
	public static ForwardAndUpGuide createFromNaN() {
		return setReturnValueToNaN( new ForwardAndUpGuide() );
	}
	public void setNaN() {
		setReturnValueToNaN( this );
	}
	public boolean isNaN() {
		return forward.isNaN() || upGuide.isNaN();
	}

	//Identity
	public static ForwardAndUpGuide setReturnValueToIdentity( ForwardAndUpGuide rv ) {
		rv.forward.set( 0, 0, -1 );
		rv.upGuide.set( 0, 1, 0 );
		return rv;
	}
	public static ForwardAndUpGuide createFromIdentity() {
		return setReturnValueToIdentity( ForwardAndUpGuide.createFromNaN() );
	}
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}
	public boolean isIdentity() {
		//todo?
		//return forward.isNegativeZAxis() && upGuide.y > 0.0;
		return forward.isNegativeZAxis() && upGuide.isPositiveYAxis();
	}
	
	public void setValue( OrthogonalMatrix3x3 m ) {
		this.forward.set( m.backward );
		this.forward.negate();
		this.upGuide.set( m.up );
	}
	public void setValue( UnitQuaternion q ) {
		setValue( new OrthogonalMatrix3x3( q ) );
	}
	public void setValue( AxisRotation aa ) {
		setValue( new OrthogonalMatrix3x3( aa ) );
	}
	public void setValue( EulerAngles ea ) {
		setValue( new OrthogonalMatrix3x3( ea ) );
	}
	public void setValue( ForwardAndUpGuide faug ) {
		setValueFrom( faug.forward, faug.upGuide );
	}

	public void setValueFrom( Vector3 forward, Vector3 upGuide ) {
		this.forward.set( forward );
		this.upGuide.set( upGuide );
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( ForwardAndUpGuide.class.getName() );
		sb.append( "[forward=" );
		sb.append( forward );
		sb.append( ";upGuide=" );
		sb.append( upGuide );
		sb.append( "]" );
		return sb.toString();
	}
}
