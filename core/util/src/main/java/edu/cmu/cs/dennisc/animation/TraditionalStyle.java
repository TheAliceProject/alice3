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
package edu.cmu.cs.dennisc.animation;

/**
 * @author Dennis Cosgrove
 */
public enum TraditionalStyle implements Style {
	BEGIN_AND_END_ABRUPTLY( false, false ),
	BEGIN_GENTLY_AND_END_ABRUPTLY( true, false ),
	BEGIN_ABRUPTLY_AND_END_GENTLY( false, true ),
	BEGIN_AND_END_GENTLY( true, true );
	private boolean isSlowInDesired;
	private boolean isSlowOutDesired;

	TraditionalStyle( boolean isSlowInDesired, boolean isSlowOutDesired ) {
		this.isSlowInDesired = isSlowInDesired;
		this.isSlowOutDesired = isSlowOutDesired;
	}

	public boolean isSlowInDesired() {
		return this.isSlowInDesired;
	}

	public boolean isSlowOutDesired() {
		return this.isSlowOutDesired;
	}

	private static double gently( double x, double A, double B ) {
		double y, a3, b3, c3, m, b2;
		if( x < A ) {
			y = ( ( B - 1 ) / ( A * ( ( ( ( B * B ) - ( A * B ) ) + A ) - 1 ) ) ) * x * x;
		} else if( x > B ) {
			a3 = 1 / ( ( ( ( B * B ) - ( A * B ) ) + A ) - 1 );
			b3 = -2 * a3;
			c3 = 1 + a3;
			y = ( a3 * x * x ) + ( b3 * x ) + c3;
		} else {
			m = ( 2 * ( B - 1 ) ) / ( ( ( ( B * B ) - ( A * B ) ) + A ) - 1 );
			b2 = ( -m * A ) / 2;
			y = ( m * x ) + b2;
		}
		return y;
	}

	//todo: this method really should account for how long the animation is
	@Override
	public double calculatePortion( double timeElapsed, double timeTotal ) {
		if( timeTotal != 0 ) {
			double portion = timeElapsed / timeTotal;
			if( this.isSlowInDesired ) {
				if( this.isSlowOutDesired ) {
					return gently( portion, 0.3, 0.8 );
				} else {
					return gently( portion, 0.99, 0.999 );
				}
			} else {
				if( this.isSlowOutDesired ) {
					return gently( portion, 0.001, 0.01 );
				} else {
					return portion;
				}
			}
		}
		else {
			return 1;
		}
	}
}
