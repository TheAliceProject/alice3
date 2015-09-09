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

package org.lgna.story;

import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.ValueTemplate;
import org.lgna.project.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */
public abstract class STurnable extends SThing {
	@Override
	/* package-private */abstract org.lgna.story.implementation.AbstractTransformableImp getImplementation();

	public Boolean isFacing( SThing other ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().isFacing( other.getImplementation() );
	}

	@MethodTemplate( )
	public void turn( TurnDirection direction, @ValueTemplate( detailsEnumCls = org.lgna.story.annotation.AngleDetails.class ) Number amount, Turn.Detail... details) {

		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( direction, 0 );
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNumber( amount, 1 );

		this.getImplementation().animateApplyRotationInRevolutions( direction.getAxis(), amount.doubleValue(), AsSeenBy.getValue( details, this ).getImplementation(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	public void roll( RollDirection direction, @ValueTemplate( detailsEnumCls = org.lgna.story.annotation.AngleDetails.class ) Number amount, Roll.Detail... details) {

		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( direction, 0 );
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNumber( amount, 1 );

		this.getImplementation().animateApplyRotationInRevolutions( direction.getAxis(), amount.doubleValue(), AsSeenBy.getValue( details, this ).getImplementation(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	public void turnToFace( SThing target, TurnToFace.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( target, 0 );
		this.getImplementation().animateOrientationOnlyToFace( target.getImplementation(), null, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	public void orientToUpright( OrientToUpright.Detail... details ) {
		SThing upAsSeenBy = UpAsSeenBy.getValue( details, null );
		this.getImplementation().animateOrientationToUpright( upAsSeenBy != null ? upAsSeenBy.getImplementation() : org.lgna.story.implementation.AsSeenBy.SCENE, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	public void pointAt( SThing target, PointAt.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( target, 0 );
		SThing upAsSeenBy = UpAsSeenBy.getValue( details, null );
		this.getImplementation().animateOrientationToPointAt( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( target, 0 ).getImplementation(), upAsSeenBy != null ? upAsSeenBy.getImplementation() : org.lgna.story.implementation.AsSeenBy.SCENE, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( )
	public void orientTo( SThing target, OrientTo.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( target, 0 );
		this.getImplementation().animateOrientationOnly( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( target, 0 ).getImplementation(), null, Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public Orientation getOrientationRelativeToVehicle() {
		return Orientation.createInstance( this.getImplementation().getLocalOrientation() );
	}

	@MethodTemplate( visibility = Visibility.TUCKED_AWAY )
	public void setOrientationRelativeToVehicle( Orientation orientation, SetOrientationRelativeToVehicle.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( orientation, 0 );
		org.lgna.story.implementation.EntityImp vehicle = this.getImplementation().getVehicle();
		if( vehicle != null ) {
			this.getImplementation().animateOrientationOnly( vehicle, orientation.getInternal(), Duration.getValue( details ), AnimationStyle.getValue( details ).getInternal() );
		} else {
			edu.cmu.cs.dennisc.scenegraph.AbstractTransformable sgTransformable = this.getImplementation().getSgComposite();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = sgTransformable.getLocalTransformation();
			m.orientation.setValue( orientation.getInternal() );
			sgTransformable.setLocalTransformation( m );
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
		}
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceTo( STurnable other, GetDistanceTo.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceTo( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation() );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceAbove( SThing other, GetDistanceAbove.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceAbove( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getImplementation( details, org.lgna.story.implementation.AsSeenBy.SCENE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isAbove( SThing other, IsAbove.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceAbove( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getImplementation( details, org.lgna.story.implementation.AsSeenBy.SCENE ) ) > 0;
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceBelow( SThing other, GetDistanceBelow.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceBelow( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getImplementation( details, org.lgna.story.implementation.AsSeenBy.SCENE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isBelow( SThing other, IsBelow.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceBelow( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getImplementation( details, org.lgna.story.implementation.AsSeenBy.SCENE ) ) > 0;
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceToTheRightOf( SThing other, GetDistanceToTheRightOf.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceToTheRightOf( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getImplementation( details, org.lgna.story.implementation.AsSeenBy.SCENE ) );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isToTheRightOf( SThing other, IsToTheRightOf.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceToTheRightOf( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() ) > 0;
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceToTheLeftOf( SThing other, GetDistanceToTheLeftOf.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceToTheLeftOf( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isToTheLeftOf( SThing other, IsToTheLeftOf.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceToTheLeftOf( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() ) > 0;
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceInFrontOf( SThing other, GetDistanceInFrontOf.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceInFrontOf( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isInFrontOf( SThing other, IsInFrontOf.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceInFrontOf( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() ) > 0;
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Double getDistanceBehind( SThing other, GetDistanceBehind.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceBehind( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() );
	}

	@MethodTemplate( visibility = Visibility.PRIME_TIME )
	public Boolean isBehind( SThing other, IsBehind.Detail... details ) {
		org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 );
		return this.getImplementation().getDistanceBehind( org.lgna.common.LgnaIllegalArgumentException.checkArgumentIsNotNull( other, 0 ).getImplementation(), AsSeenBy.getValue( details, this ).getImplementation() ) > 0;
	}

}
