/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package edu.cmu.cs.dennisc.math.immutable;

/**
 * @author Dennis Cosgrove
 */
public final class MPlane {
	private static MPlane createInstance( double xPosition, double yPosition, double zPosition, double xNormal, double yNormal, double zNormal ) {
		final double EPSILON = 0.01;
		double magnitudeSquared = edu.cmu.cs.dennisc.math.Tuple3.calculateMagnitudeSquared( xNormal, yNormal, zNormal );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinEpsilonOf1InSquaredSpace( magnitudeSquared, EPSILON ) ) {
			//pass
		} else {
			double magnitude = Math.sqrt( magnitudeSquared );
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( magnitude, xNormal, yNormal, zNormal );
			xNormal /= magnitude;
			yNormal /= magnitude;
			zNormal /= magnitude;
		}
		return new MPlane( xNormal, yNormal, zNormal, -( ( xNormal * xPosition ) + ( yNormal * yPosition ) + ( zNormal * zPosition ) ) );
	}

	public static MPlane createInstance( MPoint3 position, MVector3 normal ) {
		assert position.isNaN() == false;
		assert normal.isNaN() == false;
		return createInstance( position.x, position.y, position.z, normal.x, normal.y, normal.z );
	}

	public static MPlane createInstance( MAffineMatrix4x4 m ) {
		assert m.isNaN() == false;
		return createInstance( m.translation.x, m.translation.y, m.translation.z, -m.orientation.backward.x, -m.orientation.backward.y, -m.orientation.backward.z );
	}

	public static MPlane createInstance( MPoint3 a, MPoint3 b, MPoint3 c ) {
		assert a.isNaN() == false;
		assert b.isNaN() == false;
		assert c.isNaN() == false;
		MVector3 ac = MVector3.createSubtraction( c, a );
		MVector3 ab = MVector3.createSubtraction( b, a );
		ac = ac.createNormal();
		ab = ab.createNormal();
		MVector3 normal = MVector3.createCrossProduct( ac, ab );
		return createInstance( a, normal );
	}

	private MPlane( double a, double b, double c, double d ) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public double intersect( MRay ray ) {
		double denom = ( this.a * ray.direction.x ) + ( this.b * ray.direction.y ) + ( this.c * ray.direction.z );
		if( denom == 0.0 ) {
			return Double.NaN;
		} else {
			double numer = ( this.a * ray.origin.x ) + ( this.b * ray.origin.y ) + ( this.c * ray.origin.z ) + this.d;
			return -numer / denom;
		}
	}

	public double evaluate( MPoint3 p ) {
		return ( this.a * p.x ) + ( this.b * p.y ) + ( this.c * p.z ) + this.d;
	}

	public final double a;
	public final double b;
	public final double c;
	public final double d;
}
