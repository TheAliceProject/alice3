/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.lgna.ik;

/**
 * @author Dennis Cosgrove
 */
public class Chain {
	public static Chain createInstance( Bone base, Bone endEffector ) {
		java.util.List< Element > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		Bone bone = endEffector;
		final int N = 3;
		final boolean isLinearEnabled = true;
		final boolean isAngularEnabled = true;
		while( bone != base ) {
			Bone next = bone.getNextBoneTowards( base );
			boolean isPositive = ( next == bone.getParent() );
			list.add( new Element( bone, isPositive, N, isLinearEnabled, isAngularEnabled ) );
		}
		Element[] array = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( list, Element.class );
		return new Chain( array );
	}

	private static class Element {
		private final Bone bone;
		private final boolean isPositive;
		private final edu.cmu.cs.dennisc.math.Vector3[] linearContributions; 
		private final edu.cmu.cs.dennisc.math.Vector3[] angularContributions; 
		//private final edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
		public Element( Bone bone, boolean isPositive, final int N, boolean isLinearEnabled, boolean isAngularEnabled ) {
			this.bone = bone;
			this.isPositive = isPositive;
			this.linearContributions = Solver.createAxes( isLinearEnabled, N );
			this.angularContributions = Solver.createAxes( isAngularEnabled, N );
		}
		public void updateLinearContributions( edu.cmu.cs.dennisc.math.Vector3 v ) {
			for( int i=0; i<this.linearContributions.length; i++ ) {
				edu.cmu.cs.dennisc.math.Vector3.setReturnValueToCrossProduct( this.linearContributions[ i ], this.bone.getAxes()[ i ], v );
			}
		}
		public void updateAngularContributions() {
			for( int i=0; i<this.angularContributions.length; i++ ) {
				this.angularContributions[ i ].set( this.bone.getAxes()[ i ] );
			}
		}
		public edu.cmu.cs.dennisc.math.Point3 getAnchor() {
			throw new RuntimeException( "todo" );
		}
	}
	private edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorLinearVelocity = edu.cmu.cs.dennisc.math.Vector3.createZero();
	private edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorAngularVelocity = edu.cmu.cs.dennisc.math.Vector3.createZero();
	
	private boolean isLinearVelocityEnabled() {
		return this.desiredEndEffectorLinearVelocity != null;
	}
	private boolean isAngularVelocityEnabled() {
		return this.desiredEndEffectorAngularVelocity != null;
	}
	public void setDesiredEndEffectorAngularVelocity( edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorAngularVelocity ) {
		this.desiredEndEffectorAngularVelocity = desiredEndEffectorAngularVelocity;
	}
	public void setDesiredEndEffectorLinearVelocity( edu.cmu.cs.dennisc.math.Vector3 desiredEndEffectorLinearVelocity ) {
		this.desiredEndEffectorLinearVelocity = desiredEndEffectorLinearVelocity;
	}
	
	private final Element[] elements;
	private Chain( Element[] elements ) {
		this.elements = elements;
	}
	
	private Bone getEndEffector() {
		throw new RuntimeException( "todo" );
	}
	private edu.cmu.cs.dennisc.math.Point3 getEndEffectorPosition() {
		Bone endEffector = this.getEndEffector();
		throw new RuntimeException( "todo" );
	}
	private void computeVelocityContributions() {
		edu.cmu.cs.dennisc.math.Point3 endEffectorPos = this.getEndEffectorPosition();
		for( Element element : this.elements ) {
			if( this.isLinearVelocityEnabled() ) {
				edu.cmu.cs.dennisc.math.Vector3 v = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( 
						endEffectorPos,
						element.getAnchor()
				);
				element.updateLinearContributions( v );
			}
			if( this.isAngularVelocityEnabled() ) {
				element.updateAngularContributions();
			}
		}
	}
}
