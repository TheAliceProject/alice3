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
package edu.cmu.cs.dennisc.scenegraph.bound;

/**
 * @author Dennis Cosgrove
 */
public class CumulativeBound {
	private java.util.Vector<edu.cmu.cs.dennisc.math.Point3> m_transformedPoints = new java.util.Vector<edu.cmu.cs.dennisc.math.Point3>();

	//	public CumulativeBound() {
	//	}
	//	public CumulativeBound( edu.cmu.cs.dennisc.scenegraph.Composite sgRoot, final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
	//		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : edu.cmu.cs.dennisc.pattern.VisitUtilities.getAll( sgRoot, edu.cmu.cs.dennisc.scenegraph.Visual.class ) ) {
	//			if( sgVisual.isShowing.getValue() ) {
	//				add( sgVisual, sgVisual.getTransformation( asSeenBy ) );
	//				//add( sgVisual, asSeenBy.getTransformation( sgVisual ) );
	//			}
	//		}
	//	}
	private void addPoint( edu.cmu.cs.dennisc.math.Point3 p, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		assert p.isNaN() == false;
		assert trans.isNaN() == false;
		trans.transform( p );
		m_transformedPoints.addElement( p );
	}

	public void add( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox box = sgVisual.getAxisAlignedMinimumBoundingBox();
		this.addBoundingBox( box, trans );
	}

	public void addSkeletonVisual( edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean ignoreJointOrientations ) {
		edu.cmu.cs.dennisc.math.AxisAlignedBox box = sgSkeletonVisual.getAxisAlignedMinimumBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox(), ignoreJointOrientations );
		this.addBoundingBox( box, trans );
	}

	public void addOrigin( edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		addPoint( edu.cmu.cs.dennisc.math.Point3.createZero(), trans );
	}

	public void addBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox box, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		if( box.isNaN() ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.math.Hexahedron hexahedron = box.getHexahedron();
			for( int i = 0; i < 8; i++ ) {
				addPoint( hexahedron.getPointAt( i ), trans );
			}
		}
	}

	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( edu.cmu.cs.dennisc.math.Sphere rv ) {
		return BoundUtilities.getBoundingSphere( rv, m_transformedPoints );
	}

	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere() {
		return getBoundingSphere( new edu.cmu.cs.dennisc.math.Sphere() );
	}

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox rv ) {
		return BoundUtilities.getBoundingBox( rv, m_transformedPoints );
	}

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getBoundingBox() {
		return getBoundingBox( new edu.cmu.cs.dennisc.math.AxisAlignedBox() );
	}
}
