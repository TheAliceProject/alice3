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
package edu.cmu.cs.dennisc.scenegraph.util;

//todo: move this to a different package so it won't be out of place?

/**
 * @author Dennis Cosgrove
 */
@Deprecated
public class AffineAnimation {
	private static final long SLEEP_MSEC = 25;
	private static long s_t0 = System.currentTimeMillis();

	private static void waitForLookingGlass() {
		edu.cmu.cs.dennisc.java.lang.ThreadUtilities.sleep( SLEEP_MSEC );
	}

	private static double getCurrentTime() {
		return ( System.currentTimeMillis() - s_t0 ) * 0.001;
	}

	public static void setTranslation( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double x, double y, double z, double rate ) {
		double t0 = getCurrentTime();

		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.getLocalTransformation();
		double x0 = m.translation.x;
		double y0 = m.translation.y;
		double z0 = m.translation.z;

		if( Double.isNaN( x ) ) {
			x = x0;
		}
		if( Double.isNaN( y ) ) {
			y = y0;
		}
		if( Double.isNaN( z ) ) {
			z = z0;
		}

		double xDelta = x - x0;
		double yDelta = y - y0;
		double zDelta = z - z0;

		double distance = edu.cmu.cs.dennisc.math.Tuple3.calculateMagnitude( xDelta, yDelta, zDelta );

		if( distance != 0 ) {
			double xRate = ( rate * xDelta ) / distance;
			double yRate = ( rate * yDelta ) / distance;
			double zRate = ( rate * zDelta ) / distance;

			double duration = distance / rate;

			double tComplete = t0 + duration;
			while( true ) {
				double tCurr = getCurrentTime();
				if( tCurr > tComplete ) {
					break;
				} else {
					double tDelta = tCurr - t0;

					m.translation.set( x0 + ( xRate * tDelta ), y0 + ( yRate * tDelta ), z0 + ( zRate * tDelta ) );
					transformable.setLocalTransformation( m );
					waitForLookingGlass();
				}
			}

			m.translation.set( x, y, z );
			transformable.setLocalTransformation( m );
			waitForLookingGlass();
		}
	}

	private static void rotate( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRevolutions, double duration ) {
		double t0 = getCurrentTime();

		double tComplete = t0 + duration;

		double angleSum = 0;
		while( true ) {
			double tCurr = getCurrentTime();
			if( tCurr > tComplete ) {
				break;
			} else {
				double tDelta = tCurr - t0;
				double tPortion = tDelta / duration;

				double anglePortion = ( angleInRevolutions * tPortion ) - angleSum;

				transformable.applyRotationAboutArbitraryAxis( axis, new edu.cmu.cs.dennisc.math.AngleInRevolutions( anglePortion ) );

				angleSum += anglePortion;
				waitForLookingGlass();
			}
		}
		transformable.applyRotationAboutArbitraryAxis( axis, new edu.cmu.cs.dennisc.math.AngleInRevolutions( angleInRevolutions - angleSum ) );
		waitForLookingGlass();
	}

	public static void turnLeft( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double angleInRevolutions, double duration ) {
		rotate( transformable, edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis(), angleInRevolutions, duration );
	}

	public static void turnRight( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double angleInRevolutions, double duration ) {
		turnLeft( transformable, -angleInRevolutions, duration );
	}

	public static void turnForward( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double angleInRevolutions, double duration ) {
		rotate( transformable, edu.cmu.cs.dennisc.math.Vector3.accessPositiveXAxis(), angleInRevolutions, duration );
	}

	public static void turnBackward( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double angleInRevolutions, double duration ) {
		turnForward( transformable, -angleInRevolutions, duration );
	}

	public static void rollLeft( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double angleInRevolutions, double duration ) {
		rotate( transformable, edu.cmu.cs.dennisc.math.Vector3.accessPositiveZAxis(), angleInRevolutions, duration );
	}

	public static void rollRight( edu.cmu.cs.dennisc.scenegraph.Transformable transformable, double angleInRevolutions, double duration ) {
		rollLeft( transformable, -angleInRevolutions, duration );
	}
}
