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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.MethodTemplate;
import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public class SymmetricPerspectiveCamera extends AbstractPerspectiveCamera {
//	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > HORIZONTAL_VIEWING_ANGLE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( SpotLight.class, "HorizontalViewingAngle" );
//	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > VERTICAL_VIEWING_ANGLE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( SpotLight.class, "VerticalViewingAngle" );
	private edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera m_sgCamera = null;

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractPerspectiveCamera getSGPerspectiveCamera() {
		return getSGSymmetricPerspectiveCamera();
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera getSGSymmetricPerspectiveCamera() {
		if( m_sgCamera != null ) {
			//pass
		} else {
			m_sgCamera = new edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera();
			m_sgCamera.farClippingPlaneDistance.setValue( 1000.0 );
			m_sgCamera.setParent( getSGTransformable() );
			putElement( m_sgCamera );
		}
		return m_sgCamera;
	}
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Double getHorizontalViewingAngle() {
		return getSGSymmetricPerspectiveCamera().horizontalViewingAngle.getValue().getAsRevolutions();
	}
	public void setHorizontalViewingAngle( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number horizontalViewingAngle 
		) {
		getSGSymmetricPerspectiveCamera().horizontalViewingAngle.setValue( new edu.cmu.cs.dennisc.math.AngleInRevolutions( horizontalViewingAngle.doubleValue() ) );
	}
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Double getVerticalViewingAngle() {
		return getSGSymmetricPerspectiveCamera().verticalViewingAngle.getValue().getAsRevolutions();
	}
	public void setVerticalViewingAngle( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number verticalViewingAngle 
		) {
		getSGSymmetricPerspectiveCamera().verticalViewingAngle.setValue( new edu.cmu.cs.dennisc.math.AngleInRevolutions( verticalViewingAngle.doubleValue() ) );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle() {
		edu.cmu.cs.dennisc.lookingglass.LookingGlass lg = getLookingGlass();
		assert lg != null;
		return lg.getActualHorizontalViewingAngle( getSGSymmetricPerspectiveCamera() );
	}
	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle() {
		edu.cmu.cs.dennisc.lookingglass.LookingGlass lg = getLookingGlass();
		assert lg != null;
		return lg.getActualVerticalViewingAngle( getSGSymmetricPerspectiveCamera() );
	}

	private static double getMinAngleAsRadians( edu.cmu.cs.dennisc.math.Angle horizontal, edu.cmu.cs.dennisc.math.Angle vertical ) {
		if( horizontal.isNaN() ) {
			if( vertical.isNaN() ) {
				return Math.PI * 0.25;
			} else {
				return vertical.getAsRadians();
			}
		} else {
			if( vertical.isNaN() ) {
				return vertical.getAsRadians();
			} else {
				return Math.min( vertical.getAsRadians(), horizontal.getAsRadians() );
			}
		}
	}
	protected double getViewingAngleInRadiansForGoodLookAt() {
		edu.cmu.cs.dennisc.lookingglass.LookingGlass lg = getLookingGlass();
		if( lg != null ) {
			return getMinAngleAsRadians( lg.getActualHorizontalViewingAngle( getSGSymmetricPerspectiveCamera() ), lg.getActualVerticalViewingAngle( getSGSymmetricPerspectiveCamera() ) );
		} else {
			return getMinAngleAsRadians( getSGSymmetricPerspectiveCamera().horizontalViewingAngle.getValue(), getSGSymmetricPerspectiveCamera().verticalViewingAngle.getValue() );
		}
	}
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateGoodLookAt( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, Model target, HowMuch howMuch, ReferenceFrame asSeenBy ) {
		//			edu.cmu.cs.dennisc.math.SphereD boundingSphere = target.getBoundingSphere( howMuch, asSeenBy );

		edu.cmu.cs.dennisc.math.Sphere boundingSphere = new edu.cmu.cs.dennisc.math.Sphere();
		edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = target.getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF, howMuch );
		boundingBox.getCenter( boundingSphere.center );
		double diameter = Math.max( boundingBox.getWidth(), Math.max( boundingBox.getHeight(), boundingBox.getDepth() ) );
		boundingSphere.radius = diameter * 0.5;
		if( boundingSphere.isNaN() ) {
			boundingSphere.center.set( 0, 0, 0 );
			boundingSphere.radius = 1.0;
		}

		double theta = getViewingAngleInRadiansForGoodLookAt();
		double distance = boundingSphere.radius / Math.sin( theta * 0.5 );
		
		distance *= 1.5;
		
		double offset = distance / Math.sqrt( 3.0 );

		StandIn standInA = acquireStandIn( target );
		try {
			standInA.moveTo( target.createOffsetStandIn( boundingSphere.center.x + offset, boundingSphere.center.y + offset, boundingSphere.center.z - offset ), RIGHT_NOW );
			standInA.pointAt( target.createOffsetStandIn( boundingSphere.center ), RIGHT_NOW );
//			standInA.moveTo( target.createOffsetStandIn( boundingSphere.center.x + 0.1, boundingSphere.center.y + 0.1, boundingSphere.center.z - 0.1 ), RIGHT_NOW );
//			standInA.pointAt( target.createOffsetStandIn( boundingSphere.center ), RIGHT_NOW );
//			standInA.move( MoveDirection.BACKWARD, distance, RIGHT_NOW );
			return standInA.getTransformation( rv, asSeenBy );
		} finally {
			releaseStandIn( standInA );
		}
	}
	protected final edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateGoodLookAt( Model target, HowMuch howMuch, ReferenceFrame referenceFrame ) {
		return calculateGoodLookAt( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), target, howMuch, referenceFrame );
	}
	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void getGoodLookAt( Model target, Number duration, ReferenceFrame asSeenBy, Style style, HowMuch howMuch ) {
		setPointOfView( calculateGoodLookAt( target, howMuch, asSeenBy ), duration, asSeenBy, style );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void getGoodLookAt( Model target, Number duration, ReferenceFrame asSeenBy, Style style ) {
		getGoodLookAt( target, duration, asSeenBy, style, DEFAULT_HOW_MUCH );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void getGoodLookAt( Model target, Number duration, ReferenceFrame asSeenBy ) {
		getGoodLookAt( target, duration, asSeenBy, DEFAULT_STYLE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void getGoodLookAt( Model target, Number duration ) {
		//getGoodLookAt( target, duration, AsSeenBy.SCENE );
		getGoodLookAt( target, duration, target );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void getGoodLookAt( Model target ) {
		getGoodLookAt( target, DEFAULT_DURATION );
	}

}
