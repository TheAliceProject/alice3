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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public class OldMesh extends Geometry {
	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingBox( boundingBox, xyzs.getValue() );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities.getBoundingSphere( boundingSphere, xyzs.getValue() );
	}

	@Override
	protected void updatePlane( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.math.Point3 translation ) {
		double[] xyzs = this.xyzs.getValue();
		float[] ijks = this.ijks.getValue();
		assert xyzs.length >= 6;
		assert ijks.length >= 3;

		forward.set( ijks[ 0 ], ijks[ 1 ], ijks[ 2 ] );
		forward.normalize();
		forward.negate();

		translation.set( xyzs[ 0 ], xyzs[ 1 ], xyzs[ 2 ] );
		upGuide.set( translation.x - xyzs[ 3 ], translation.y - xyzs[ 4 ], translation.z - xyzs[ 5 ] );
		upGuide.normalize();

	}

	@Override
	public void transform( edu.cmu.cs.dennisc.math.AbstractMatrix4x4 trans ) {
		//todo
	}

	public final edu.cmu.cs.dennisc.property.InstanceProperty<double[]> xyzs = new edu.cmu.cs.dennisc.property.InstanceProperty<double[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<float[]> ijks = new edu.cmu.cs.dennisc.property.InstanceProperty<float[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<float[]> uvs = new edu.cmu.cs.dennisc.property.InstanceProperty<float[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<short[]> xyzTriangleIndices = new edu.cmu.cs.dennisc.property.InstanceProperty<short[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<short[]> ijkTriangleIndices = new edu.cmu.cs.dennisc.property.InstanceProperty<short[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<short[]> uvTriangleIndices = new edu.cmu.cs.dennisc.property.InstanceProperty<short[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<short[]> xyzQuadrangleIndices = new edu.cmu.cs.dennisc.property.InstanceProperty<short[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<short[]> ijkQuadrangleIndices = new edu.cmu.cs.dennisc.property.InstanceProperty<short[]>( this, null );
	public final edu.cmu.cs.dennisc.property.InstanceProperty<short[]> uvQuadrangleIndices = new edu.cmu.cs.dennisc.property.InstanceProperty<short[]>( this, null );
}
