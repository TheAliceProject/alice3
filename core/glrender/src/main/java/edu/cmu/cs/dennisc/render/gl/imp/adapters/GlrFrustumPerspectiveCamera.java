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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.Context;

/**
 * @author Dennis Cosgrove
 */
public class GlrFrustumPerspectiveCamera extends GlrAbstractPerspectiveCamera<edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera> {
	private static final edu.cmu.cs.dennisc.math.ClippedZPlane s_actualPicturePlaneBufferForReuse = edu.cmu.cs.dennisc.math.ClippedZPlane.createNaN();

	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, java.awt.Rectangle actualViewport ) {
		throw new RuntimeException( "TODO" );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, java.awt.Rectangle actualViewport ) {
		edu.cmu.cs.dennisc.math.ClippedZPlane actualPicturePlane = getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), actualViewport );
		double left = actualPicturePlane.getXMinimum();
		double right = actualPicturePlane.getXMaximum();
		double bottom = actualPicturePlane.getYMinimum();
		double top = actualPicturePlane.getYMaximum();

		double zNear = owner.nearClippingPlaneDistance.getValue();
		double zFar = owner.farClippingPlaneDistance.getValue();

		rv.right.set( 2 * zNear, 0, 0, 0 );
		rv.up.set( 0, ( 2 * zNear ) / ( top - bottom ), 0, 0 );
		rv.backward.set( ( right + left ) / ( right - left ), ( top + bottom ) / ( top - bottom ), -( zFar + zNear ) / ( zFar + zNear ), -1 );
		rv.translation.set( 0, 0, -( 2 * zFar * zNear ) / ( zFar - zNear ), 0 );

		return rv;
	}

	@Override
	protected java.awt.Rectangle performLetterboxing( java.awt.Rectangle rv ) {
		//todo: handle NaN
		return rv;
	}

	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, java.awt.Rectangle actualViewport ) {
		rv.set( owner.picturePlane.getValue(), edu.cmu.cs.dennisc.java.awt.RectangleUtilities.toMRectangleI( actualViewport ) );
		return rv;
	}

	@Override
	protected void setupProjection( Context context, java.awt.Rectangle actualViewport, float near, float far ) {
		synchronized( s_actualPicturePlaneBufferForReuse ) {
			getActualPicturePlane( s_actualPicturePlaneBufferForReuse, actualViewport );
			context.gl.glFrustum( s_actualPicturePlaneBufferForReuse.getXMinimum(), s_actualPicturePlaneBufferForReuse.getXMaximum(), s_actualPicturePlaneBufferForReuse.getYMinimum(), s_actualPicturePlaneBufferForReuse.getYMaximum(), near, far );
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.picturePlane ) {
			//pass
		} else {
			super.propertyChanged( property );
		}
	}
}
