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
package edu.cmu.cs.dennisc.scenegraph.util;

/**
 * @author Dennis Cosgrove
 */
public class GoodLookAtUtils {
	public static edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateGoodLookAt( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, edu.cmu.cs.dennisc.math.Angle verticalViewingAngle, double aspectRatio ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox axisAlignedBox = sgVisual.getAxisAlignedMinimumBoundingBox();
		final double THRESHOLD = 100.0;
		if( axisAlignedBox.getWidth() > THRESHOLD ) {
			return null;
		} else {
			edu.cmu.cs.dennisc.math.Point3[] localPoints = axisAlignedBox.getPoints();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 absoluteM = sgVisual.getAbsoluteTransformation();

			edu.cmu.cs.dennisc.math.Point3[] transformedPoints = new edu.cmu.cs.dennisc.math.Point3[ localPoints.length ];
			for( int i = 0; i < localPoints.length; i++ ) {
				transformedPoints[ i ] = absoluteM.createTransformed( localPoints[ i ] );
			}

			edu.cmu.cs.dennisc.math.Point3 averageAbsolutePoint = edu.cmu.cs.dennisc.math.Point3.createZero();
			for( edu.cmu.cs.dennisc.math.Point3 absolutePoint : transformedPoints ) {
				averageAbsolutePoint.add( absolutePoint );
			}
			averageAbsolutePoint.divide( transformedPoints.length );

			for( edu.cmu.cs.dennisc.math.Point3 absolutePoint : transformedPoints ) {
				absolutePoint.subtract( averageAbsolutePoint );
			}

			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
			final boolean IS_STRAIGHT_ON_VIEWING_DESIRED = true;
			if( IS_STRAIGHT_ON_VIEWING_DESIRED ) {
				//pass
			} else {
				m.applyRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInDegrees( -30.0 ) );
				m.applyRotationAboutZAxis( new edu.cmu.cs.dennisc.math.AngleInDegrees( 30.0 ) );

				edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseM = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createInverse( m );

				for( int i = 0; i < localPoints.length; i++ ) {
					transformedPoints[ i ] = inverseM.createTransformed( transformedPoints[ i ] );
				}
			}

			double halfVerticalInRadians = verticalViewingAngle.getAsRadians() * 0.5;

			double maxValue = Double.MIN_VALUE;
			double sineVertical = Math.sin( halfVerticalInRadians );
			double cosineVertical = Math.cos( halfVerticalInRadians );

			for( edu.cmu.cs.dennisc.math.Point3 p : transformedPoints ) {
				double opposite = p.y;
				double hypotenuse = opposite / sineVertical;
				double adjacent = hypotenuse * cosineVertical;
				double value = adjacent - p.z;
				maxValue = Math.max( maxValue, value );
			}

			double halfHorizontalInRadians = halfVerticalInRadians * aspectRatio;
			double sineHorizontal = Math.sin( halfHorizontalInRadians );
			double cosineHorizontal = Math.cos( halfHorizontalInRadians );

			for( edu.cmu.cs.dennisc.math.Point3 p : transformedPoints ) {
				double opposite = p.x;
				double hypotenuse = opposite / sineHorizontal;
				double adjacent = hypotenuse * cosineHorizontal;
				double value = adjacent - p.z;
				maxValue = Math.max( maxValue, value );
			}

			m.translation.set( averageAbsolutePoint );
			m.applyTranslation( 0, 0, maxValue );

			m.invert();

			return m;
		}
	}
}
