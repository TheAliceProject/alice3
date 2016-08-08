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
public class Box extends Shape {
	public edu.cmu.cs.dennisc.math.Point3 getMinimum( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( xMinimum.getValue(), yMinimum.getValue(), zMinimum.getValue() );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getMinimum() {
		return getMinimum( new edu.cmu.cs.dennisc.math.Point3() );
	}

	public void setMinimum( double x, double y, double z ) {
		xMinimum.setValue( x );
		yMinimum.setValue( y );
		zMinimum.setValue( z );
	}

	public void setMinimum( edu.cmu.cs.dennisc.math.Point3 minimum ) {
		setMinimum( minimum.x, minimum.y, minimum.z );
	}

	public edu.cmu.cs.dennisc.math.Point3 getMaximum( edu.cmu.cs.dennisc.math.Point3 rv ) {
		rv.set( xMaximum.getValue(), yMaximum.getValue(), zMaximum.getValue() );
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Point3 getMaximum() {
		return getMaximum( new edu.cmu.cs.dennisc.math.Point3() );
	}

	public void setMaximum( double x, double y, double z ) {
		xMaximum.setValue( x );
		yMaximum.setValue( y );
		zMaximum.setValue( z );
	}

	public void setMaximum( edu.cmu.cs.dennisc.math.Point3 maximum ) {
		setMaximum( maximum.x, maximum.y, maximum.z );
	}

	public void set( edu.cmu.cs.dennisc.math.Point3 minimum, edu.cmu.cs.dennisc.math.Point3 maximum ) {
		setMinimum( minimum );
		setMaximum( maximum );
	}

	public void set( edu.cmu.cs.dennisc.math.AxisAlignedBox box ) {
		setMinimum( box.getXMinimum(), box.getYMinimum(), box.getZMinimum() );
		setMaximum( box.getXMaximum(), box.getYMaximum(), box.getZMaximum() );
	}

	@Override
	protected void updateBoundingBox( edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox ) {
		boundingBox.setMinimum( xMinimum.getValue(), yMinimum.getValue(), zMinimum.getValue() );
		boundingBox.setMaximum( xMaximum.getValue(), yMaximum.getValue(), zMaximum.getValue() );
	}

	@Override
	protected void updateBoundingSphere( edu.cmu.cs.dennisc.math.Sphere boundingSphere ) {
		double xCenter = ( xMinimum.getValue() + xMaximum.getValue() ) * 0.5;
		double yCenter = ( yMinimum.getValue() + yMaximum.getValue() ) * 0.5;
		double zCenter = ( zMinimum.getValue() + zMaximum.getValue() ) * 0.5;
		boundingSphere.center.set( xCenter, yCenter, zCenter );
		double width = xMaximum.getValue() - xMinimum.getValue();
		double height = yMaximum.getValue() - yMinimum.getValue();
		double depth = zMaximum.getValue() - zMinimum.getValue();
		boundingSphere.radius = Math.max( Math.max( width, height ), depth ) * 0.5;
	}

	public final BoundDoubleProperty xMinimum = new BoundDoubleProperty( this, -0.5 );
	public final BoundDoubleProperty xMaximum = new BoundDoubleProperty( this, +0.5 );
	public final BoundDoubleProperty yMinimum = new BoundDoubleProperty( this, -0.5 );
	public final BoundDoubleProperty yMaximum = new BoundDoubleProperty( this, +0.5 );
	public final BoundDoubleProperty zMinimum = new BoundDoubleProperty( this, -0.5 );
	public final BoundDoubleProperty zMaximum = new BoundDoubleProperty( this, +0.5 );
}
