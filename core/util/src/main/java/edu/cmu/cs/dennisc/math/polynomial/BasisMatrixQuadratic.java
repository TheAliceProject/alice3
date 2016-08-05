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

package edu.cmu.cs.dennisc.math.polynomial;

/**
 * @author Dennis Cosgrove
 */
public abstract class BasisMatrixQuadratic implements Quadratic {
	private edu.cmu.cs.dennisc.math.Matrix3x3 m_h;
	protected edu.cmu.cs.dennisc.math.Vector3 m_g; //todo: make private?

	protected BasisMatrixQuadratic( edu.cmu.cs.dennisc.math.Matrix3x3 h, edu.cmu.cs.dennisc.math.Vector3 g ) {
		m_h = h;
		m_g = g;
	}

	@Override
	public boolean isNaN() {
		return ( m_h == null ) || m_h.isNaN() || ( m_g == null ) || m_g.isNaN();
	}

	@Override
	public double evaluate( double t ) {
		double tt = t * t;
		return ( ( ( tt * m_h.right.x ) + ( t * m_h.right.y ) + m_h.right.z ) * m_g.x ) +
				( ( ( tt * m_h.up.x ) + ( t * m_h.up.y ) + m_h.up.z ) * m_g.y ) +
				( ( ( tt * m_h.backward.x ) + ( t * m_h.backward.y ) + m_h.backward.z ) * m_g.z );
	}

	@Override
	public double evaluateDerivative( double t ) {
		double t2 = t * 2;
		return ( ( ( t2 * m_h.right.x ) + m_h.right.y ) * m_g.x ) +
				( ( ( t2 * m_h.up.x ) + m_h.up.y ) * m_g.y ) +
				( ( ( t2 * m_h.backward.x ) + m_h.backward.y ) * m_g.z );
	}
}
