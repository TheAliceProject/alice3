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

import org.lgna.project.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class MovableTurnable extends Turnable {
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public Position getPositionRelativeToVehicle() {
		return Position.createInstance( this.getImplementation().getLocalPosition() );
	}
	@MethodTemplate(visibility = Visibility.TUCKED_AWAY)
	public void setPositionRelativeToVehicle( Position position ) {
		this.getImplementation().setLocalPosition( position.getInternal() );
	}

	@MethodTemplate( isFollowedByLongerMethod=true )
	public void move( MoveDirection direction, Number amount ) {
		this.move( direction, amount, new RelativeVantagePointAnimationDetails() );
	}
	@MethodTemplate()
	public void move( MoveDirection direction, Number amount, RelativeVantagePointAnimationDetails details ) {
		this.getImplementation().animateApplyTranslation( direction.createTranslation( amount.doubleValue() ), details.getAsSeenBy( this ).getImplementation(), details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void moveToward( Entity target, Number amount ) {
		this.moveToward( target, amount, new AnimationDetails() );
	}
	@MethodTemplate()
	public void moveToward( Entity target, Number amount, AnimationDetails details ) {
		edu.cmu.cs.dennisc.math.Point3 tThis = this.getImplementation().getAbsoluteTransformation().translation;
		edu.cmu.cs.dennisc.math.Point3 tTarget = target.getImplementation().getAbsoluteTransformation().translation;
		edu.cmu.cs.dennisc.math.Vector3 v = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( tTarget, tThis );
		double length = v.calculateMagnitude();
		if( length > 0 ) {
			v.multiply( amount.doubleValue() / length );
		} else {
			v.set( 0, 0, amount.doubleValue() );
		}
		this.getImplementation().animateApplyTranslation( v.x, v.y, v.z, org.lgna.story.implementation.AsSeenBy.SCENE, details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void moveAwayFrom( Entity target, Number amount ) {
		this.moveAwayFrom( target, amount, new AnimationDetails() );
	}
	@MethodTemplate()
	public void moveAwayFrom( Entity target, Number amount, AnimationDetails details ) {
		this.moveToward( target, -amount.doubleValue(), details );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void moveTo( Entity target ) {
		this.moveTo( target, new AnimationDetails() );
	}
	@MethodTemplate()
	public void moveTo( Entity target, AnimationDetails details ) {
		this.getImplementation().animatePositionOnly( target.getImplementation(), null, details.getDuration(), details.getStyle() );
	}
	@MethodTemplate(isFollowedByLongerMethod = true)
	public void moveAndOrientTo( Entity target ) {
		this.moveAndOrientTo( target, new AnimationDetails() );
	}
	@MethodTemplate()
	public void moveAndOrientTo( Entity target, AnimationDetails details ) {
		this.getImplementation().animateTransformation( target.getImplementation(), null, details.getDuration(), details.getStyle() );
	}
}
