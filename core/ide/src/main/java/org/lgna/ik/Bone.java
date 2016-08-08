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

package org.lgna.ik;

/**
 * @author Dennis Cosgrove
 */
public class Bone {
	/* package-private */static edu.cmu.cs.dennisc.math.Vector3[] createAxes( boolean b, final int N ) {
		if( b ) {
			edu.cmu.cs.dennisc.math.Vector3[] rv = new edu.cmu.cs.dennisc.math.Vector3[ N ];
			for( int i = 0; i < N; i++ ) {
				rv[ i ] = edu.cmu.cs.dennisc.math.Vector3.createZero();
			}
			return rv;
		} else {
			return null;
		}
	}

	/* package-private */static double[] createVelocities( boolean isNotToBeNull, final int N ) {
		if( isNotToBeNull ) {
			return new double[ N ];
		} else {
			return null;
		}
	}

	private static class Axis {
		private final edu.cmu.cs.dennisc.math.Vector3 axis;
		private double angularVelocity;
		private final edu.cmu.cs.dennisc.math.Vector3 linearContribution;
		private final edu.cmu.cs.dennisc.math.Vector3 angularContribution;

		public Axis( boolean isLinearEnabled, boolean isAngularEnabled ) {
			this.axis = edu.cmu.cs.dennisc.math.Vector3.createZero();
			if( isLinearEnabled ) {
				this.linearContribution = edu.cmu.cs.dennisc.math.Vector3.createZero();
			} else {
				this.linearContribution = null;
			}
			if( isAngularEnabled ) {
				this.angularContribution = edu.cmu.cs.dennisc.math.Vector3.createZero();
				this.angularVelocity = 0.0;
			} else {
				this.angularContribution = null;
				this.angularVelocity = Double.NaN;
			}
		}

		public void updateLinearContributions( edu.cmu.cs.dennisc.math.Vector3 v ) {
			if( this.linearContribution != null ) {
				edu.cmu.cs.dennisc.math.Vector3.setReturnValueToCrossProduct( this.linearContribution, this.axis, v );
			}
		}

		public void updateAngularContributions() {
			if( this.angularContribution != null ) {
				this.angularContribution.set( this.axis );
			}
		}
	}

	private final Chain chain;
	private final int index;
	private final Axis[] axes = new Axis[ 3 ];

	public Bone( Chain chain, int index, boolean isLinearEnabled, boolean isAngularEnabled ) {
		this.chain = chain;
		this.index = index;

		org.lgna.story.implementation.JointImp a = this.getA();
		if( a.isFreeInX() ) {
			this.axes[ 0 ] = new Axis( isLinearEnabled, isAngularEnabled );
		}
		if( a.isFreeInY() ) {
			this.axes[ 1 ] = new Axis( isLinearEnabled, isAngularEnabled );
		}
		if( a.isFreeInZ() ) {
			this.axes[ 2 ] = new Axis( isLinearEnabled, isAngularEnabled );
		}
	}

	public org.lgna.story.implementation.JointImp getA() {
		return this.chain.getJointImpAt( this.index );
	}

	public org.lgna.story.implementation.JointImp getB() {
		return this.chain.getJointImpAt( this.index + 1 );
	}

	public int getDegreesOfFreedom() {
		int rv = 0;
		for( Axis axis : axes ) {
			if( axis != null ) {
				rv++;
			}
		}
		return rv;
	}

	public void updateLinearContributions( edu.cmu.cs.dennisc.math.Vector3 v ) {
		for( Axis axis : axes ) {
			if( axis != null ) {
				axis.updateLinearContributions( v );
			}
		}
	}

	public void updateAngularContributions() {
		for( Axis axis : axes ) {
			if( axis != null ) {
				axis.updateAngularContributions();
			}
		}
	}

	@Override
	public String toString() {
		org.lgna.story.implementation.JointImp a = this.getA();
		org.lgna.story.implementation.JointImp b = this.getB();
		StringBuilder sb = new StringBuilder();
		sb.append( "Bone[" );
		sb.append( a.getJointId() );
		sb.append( "->" );
		sb.append( b.getJointId() );
		sb.append( "]" );
		return sb.toString();
	}

}
