/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.math;

/**
 * @author Dennis Cosgrove
 */
public class ForwardAndUpGuide implements Orientation {
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

	@Override
	public OrthogonalMatrix3x3 createOrthogonalMatrix3x3() {
		return new OrthogonalMatrix3x3( this );
	}

	@Override
	public UnitQuaternion createUnitQuaternion() {
		return new UnitQuaternion( this );
	}

	@Override
	public AxisRotation createAxisRotation() {
		return new AxisRotation( this );
	}

	@Override
	public EulerAngles createEulerAngles() {
		return new EulerAngles( this );
	}

	@Override
	public ForwardAndUpGuide createForwardAndUpGuide() {
		return new ForwardAndUpGuide( this );
	}

	@Override
	public OrthogonalMatrix3x3 getValue( OrthogonalMatrix3x3 rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public UnitQuaternion getValue( UnitQuaternion rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public AxisRotation getValue( AxisRotation rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public EulerAngles getValue( EulerAngles rv ) {
		rv.setValue( this );
		return rv;
	}

	@Override
	public ForwardAndUpGuide getValue( ForwardAndUpGuide rv ) {
		rv.setValue( this );
		return rv;
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		forward.decode( binaryDecoder );
		upGuide.decode( binaryDecoder );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
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

	@Override
	public void setNaN() {
		setReturnValueToNaN( this );
	}

	@Override
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

	@Override
	public void setIdentity() {
		setReturnValueToIdentity( this );
	}

	@Override
	public boolean isIdentity() {
		//todo?
		//return forward.isNegativeZAxis() && upGuide.y > 0.0;
		return forward.isNegativeZAxis() && upGuide.isPositiveYAxis();
	}

	@Override
	public void setValue( OrthogonalMatrix3x3 m ) {
		this.forward.set( m.backward );
		this.forward.negate();
		this.upGuide.set( m.up );
	}

	@Override
	public void setValue( UnitQuaternion q ) {
		setValue( new OrthogonalMatrix3x3( q ) );
	}

	@Override
	public void setValue( AxisRotation aa ) {
		setValue( new OrthogonalMatrix3x3( aa ) );
	}

	@Override
	public void setValue( EulerAngles ea ) {
		setValue( new OrthogonalMatrix3x3( ea ) );
	}

	@Override
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
