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

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformableImplementation extends EntityImplementation {
	@Override
	public abstract edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSgComposite();
	public void applyTranslation( double x, double y, double z, EntityImplementation asSeenBy ) {
		this.getSgComposite().applyTranslation( x, y, z, asSeenBy.getSgComposite() );
	}
	public void applyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, EntityImplementation asSeenBy ) {
		this.applyTranslation( translation.x, translation.y, translation.z, asSeenBy );
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, EntityImplementation asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert translation.isNaN() == false;
		assert duration >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
		assert style != null;
		assert asSeenBy != null;
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.applyTranslation( translation, asSeenBy );
		} else {
			class TranslateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				private EntityImplementation asSeenBy;
				private double x;
				private double y;
				private double z;
				private double xSum;
				private double ySum;
				private double zSum;

				public TranslateAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Point3 translation, EntityImplementation asSeenBy ) {
					super( duration, style );
					this.x = translation.x;
					this.y = translation.y;
					this.z = translation.z;
					this.asSeenBy = asSeenBy;
				}
				
				@Override
				protected void prologue() {
					this.xSum = 0;
					this.ySum = 0;
					this.zSum = 0;
				}
				
				@Override
				protected void setPortion( double portion ) {
					double xPortion = (this.x * portion) - this.xSum;
					double yPortion = (this.y * portion) - this.ySum;
					double zPortion = (this.z * portion) - this.zSum;

					AbstractTransformableImplementation.this.applyTranslation( xPortion, yPortion, zPortion, this.asSeenBy );

					this.xSum += xPortion;
					this.ySum += yPortion;
					this.zSum += zPortion;
				}
				
				@Override
				protected void epilogue() {
					AbstractTransformableImplementation.this.applyTranslation( this.x - this.xSum, this.y - this.ySum, this.z - this.zSum, this.asSeenBy );
				}
			}
			this.perform( new TranslateAnimation( duration, style, translation, asSeenBy ) );
		}
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, EntityImplementation asSeenBy, double duration ) {
		this.animateApplyTranslation( translation, asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, EntityImplementation asSeenBy ) {
		this.animateApplyTranslation( translation, asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation ) {
		this.animateApplyTranslation( translation, this );
	}

	public void applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, EntityImplementation asSeenBy ) {
		this.getSgComposite().applyRotationAboutArbitraryAxisInRadians( axis, angleInRadians, asSeenBy.getSgComposite() );
	}
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, EntityImplementation asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert axis != null;
		assert duration >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.applyRotationInRadians( axis, angleInRadians, asSeenBy );
		} else {
			class RotateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				private EntityImplementation asSeenBy;
				private edu.cmu.cs.dennisc.math.Vector3 axis;
				private double angleInRadians;
				private double angleSumInRadians;

				public RotateAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Vector3 axis,double angleInRadians, EntityImplementation asSeenBy ) {
					super( duration, style );
					this.axis = axis;
					this.angleInRadians = angleInRadians;
					this.asSeenBy = asSeenBy;
				}
				@Override
				protected void prologue() {
					this.angleSumInRadians = 0;
				}
				@Override
				protected void setPortion( double portion ) {
					double anglePortionInRadians = (this.angleInRadians * portion) - this.angleSumInRadians;

					AbstractTransformableImplementation.this.applyRotationInRadians( this.axis, anglePortionInRadians, this.asSeenBy );

					this.angleSumInRadians += anglePortionInRadians;
				}
				@Override
				protected void epilogue() {
					AbstractTransformableImplementation.this.applyRotationInRadians( this.axis, this.angleInRadians - this.angleSumInRadians, this.asSeenBy );
				}
			}
			this.perform( new RotateAnimation( duration, style, axis, angleInRadians, asSeenBy ) );
		}
	}
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, EntityImplementation asSeenBy, double duration ) {
		this.animateApplyRotationInRadians( axis, angleInRadians, asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, EntityImplementation asSeenBy ) {
		this.animateApplyRotationInRadians( axis, angleInRadians, asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		this.animateApplyRotationInRadians( axis, angleInRadians, this );
	}
	
	
	
	
	
//	//protected abstract double getBoundingSphereRadius();
//	protected double getBoundingSphereRadius() {
//		return 0.25;
//	}
//	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( EntityImplementation asSeenBy ) {
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getSgComposite().getTransformation( asSeenBy.getSgComposite() );
//		return new edu.cmu.cs.dennisc.math.Sphere( m.translation, 1.0 );
//	}
}
