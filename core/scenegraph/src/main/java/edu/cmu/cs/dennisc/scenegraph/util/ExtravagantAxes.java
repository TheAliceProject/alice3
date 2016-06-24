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

import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Cylinder;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author Dennis Cosgrove
 */
public class ExtravagantAxes extends Transformable {
	private static final double CYLINDER_PORTION = 0.8;
	private static final double CONE_PORTION = 1.0 - CYLINDER_PORTION;

	private static double getLengthCylinder( double unit, double lengthFactor, double diameterScale ) {
		return unit * lengthFactor * CYLINDER_PORTION;
	}

	private static double getRadiusCylinder( double unit, double lengthFactor, double diameterScale ) {
		return unit * 0.025 * diameterScale;
	}

	private static double getLengthCone( double unit, double lengthFactor, double diameterScale ) {
		return unit * lengthFactor * CONE_PORTION;
	}

	private static double getRadiusCone( double unit, double lengthFactor, double diameterScale ) {
		return getRadiusCylinder( unit, lengthFactor, diameterScale ) * 2.0;
	}

	private Arrow createArrow( double unit, double lengthFactor, Cylinder.BottomToTopAxis bottomToTopAxis, double diameterScale ) {
		double lengthCylinder = getLengthCylinder( unit, lengthFactor, diameterScale );
		double radiusCylinder = getRadiusCylinder( unit, lengthFactor, diameterScale );
		double lengthCone = getLengthCone( unit, lengthFactor, diameterScale );
		double radiusCone = getRadiusCone( unit, lengthFactor, diameterScale );
		return new Arrow( lengthCylinder, radiusCylinder, lengthCone, radiusCone, bottomToTopAxis, axisToSGAppearanceMap.get( bottomToTopAxis ), false );
	}

	public ExtravagantAxes( double unitLength, double forwardFactor, double diameterScale ) {
		this.initialUnitLength = unitLength;
		this.initialForwardFactor = forwardFactor;
		this.initialDiameterScale = diameterScale;

		SimpleAppearance sgRedAppearance = new SimpleAppearance();
		SimpleAppearance sgGreenAppearance = new SimpleAppearance();
		SimpleAppearance sgBlueAppearance = new SimpleAppearance();
		SimpleAppearance sgWhiteAppearance = new SimpleAppearance();

		sgRedAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.RED );
		sgGreenAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.GREEN );
		sgBlueAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.BLUE );
		sgWhiteAppearance.setDiffuseColor( edu.cmu.cs.dennisc.color.Color4f.WHITE );

		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_X, sgRedAppearance );
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_Y, sgGreenAppearance );
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.POSITIVE_Z, sgBlueAppearance );
		axisToSGAppearanceMap.put( Cylinder.BottomToTopAxis.NEGATIVE_Z, sgWhiteAppearance );
		sgAppearances[ 0 ] = sgRedAppearance;
		sgAppearances[ 1 ] = sgGreenAppearance;
		sgAppearances[ 2 ] = sgBlueAppearance;
		sgAppearances[ 3 ] = sgWhiteAppearance;

		this.sgXAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_X, diameterScale );
		this.sgYAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Y, diameterScale );
		this.sgZAxis = createArrow( unitLength, 1.0, Cylinder.BottomToTopAxis.POSITIVE_Z, diameterScale );
		this.sgFAxis = createArrow( unitLength, forwardFactor, Cylinder.BottomToTopAxis.NEGATIVE_Z, diameterScale );

		sgVisuals = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.concatArrays( Visual.class, this.sgXAxis.getVisuals(), this.sgYAxis.getVisuals(), this.sgZAxis.getVisuals(), this.sgFAxis.getVisuals() );

		this.sgXAxis.setParent( this );
		this.sgYAxis.setParent( this );
		this.sgZAxis.setParent( this );
		this.sgFAxis.setParent( this );
	}

	public ExtravagantAxes( double unitLength, double forwardFactor ) {
		this( unitLength, 2.0, 1.0 );
	}

	public ExtravagantAxes( double unitLength ) {
		this( unitLength, 2.0 );
	}

	public void resize( double unitLength, double forwardFactor, double diameterScale ) {
		double lengthCylinder = getLengthCylinder( unitLength, 1, diameterScale );
		double radiusCylinder = getRadiusCylinder( unitLength, 1, diameterScale );
		double lengthCone = getLengthCone( unitLength, 1, diameterScale );
		double radiusCone = getRadiusCone( unitLength, 1, diameterScale );

		this.sgXAxis.resize( lengthCylinder, radiusCylinder, lengthCone, radiusCone );
		this.sgYAxis.resize( lengthCylinder, radiusCylinder, lengthCone, radiusCone );
		this.sgZAxis.resize( lengthCylinder, radiusCylinder, lengthCone, radiusCone );

		lengthCylinder = getLengthCylinder( unitLength, forwardFactor, diameterScale );
		radiusCylinder = getRadiusCylinder( unitLength, forwardFactor, diameterScale );
		lengthCone = getLengthCone( unitLength, forwardFactor, diameterScale );
		radiusCone = getRadiusCone( unitLength, forwardFactor, diameterScale );

		this.sgFAxis.resize( lengthCylinder, radiusCylinder, lengthCone, radiusCone );
	}

	public void setScale( edu.cmu.cs.dennisc.math.Matrix3x3 scale ) {
		this.scale.setValue( scale );
		double scaleVal = scale.right.x;
		resize( this.initialUnitLength * scaleVal, this.initialForwardFactor * scaleVal, this.initialDiameterScale * scaleVal );
	}

	public edu.cmu.cs.dennisc.math.Matrix3x3 getScale() {
		return this.scale.getValue();
	}

	public edu.cmu.cs.dennisc.math.property.Matrix3x3Property getScaleProperty() {
		return this.scale;
	}

	public SimpleAppearance[] getSgOpacityAppearances() {
		return sgAppearances;
	}

	public Visual[] getSgVisuals() {
		return this.sgVisuals;
	}

	public float getOpacity() {
		return axisToSGAppearanceMap.values().iterator().next().opacity.getValue();
	}

	public void setOpacity( float opacity ) {
		for( SimpleAppearance appearance : axisToSGAppearanceMap.values() ) {
			appearance.opacity.setValue( opacity );
		}
	}

	public void setIsShowing( boolean isShowing ) {
		for( Component child : this.getComponents() ) {
			if( child instanceof Arrow ) {
				Arrow a = (Arrow)child;
				for( Visual v : a.getVisuals() ) {
					v.isShowing.setValue( isShowing );
				}
			}
		}
	}

	private final edu.cmu.cs.dennisc.math.property.Matrix3x3Property scale = new edu.cmu.cs.dennisc.math.property.Matrix3x3Property( this, edu.cmu.cs.dennisc.math.Matrix3x3.createIdentity() );

	private final java.util.Map<Cylinder.BottomToTopAxis, SimpleAppearance> axisToSGAppearanceMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final SimpleAppearance[] sgAppearances = new SimpleAppearance[ 4 ];
	private final Visual[] sgVisuals;
	private final Arrow sgXAxis;
	private final Arrow sgYAxis;
	private final Arrow sgZAxis;
	private final Arrow sgFAxis;

	private final double initialUnitLength;
	private final double initialForwardFactor;
	private final double initialDiameterScale;
}
