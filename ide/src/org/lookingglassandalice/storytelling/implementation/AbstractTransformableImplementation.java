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
	
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformation() {
		return this.getSgComposite().getLocalTransformation();
	}
	public edu.cmu.cs.dennisc.math.Point3 getLocalPosition() {
		return this.getLocalTransformation().translation;
	}
	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getLocalOrientation() {
		return this.getLocalTransformation().orientation;
	}
	public void setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation ) {
		this.getSgComposite().setLocalTransformation( transformation );
	}
	public void setLocalPosition( edu.cmu.cs.dennisc.math.Point3 translation ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getLocalTransformation();
		m.translation.set( translation );
		this.setLocalTransformation( m );
	}
	public void setLocalOrientation( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getLocalTransformation();
		m.orientation.setValue( orientation );
		this.setLocalTransformation( m );
	}
	
	public void applyTranslation( double x, double y, double z, ReferenceFrame asSeenBy ) {
		this.getSgComposite().applyTranslation( x, y, z, asSeenBy.getSgReferenceFrame() );
	}
	public void applyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, ReferenceFrame asSeenBy ) {
		this.applyTranslation( translation.x, translation.y, translation.z, asSeenBy );
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert translation.isNaN() == false;
		assert duration >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
		assert style != null;
		assert asSeenBy != null;
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.applyTranslation( translation, asSeenBy );
		} else {
			class TranslateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				private ReferenceFrame asSeenBy;
				private double x;
				private double y;
				private double z;
				private double xSum;
				private double ySum;
				private double zSum;

				public TranslateAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Point3 translation, ReferenceFrame asSeenBy ) {
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
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, ReferenceFrame asSeenBy, double duration ) {
		this.animateApplyTranslation( translation, asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation, ReferenceFrame asSeenBy ) {
		this.animateApplyTranslation( translation, asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyTranslation( edu.cmu.cs.dennisc.math.Point3 translation ) {
		this.animateApplyTranslation( translation, this );
	}

	public void animateApplyTranslation( double x, double y, double z, ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyTranslation( new edu.cmu.cs.dennisc.math.Point3( x, y, z ), asSeenBy, duration, style );
	}
	public void animateApplyTranslation( double x, double y, double z, ReferenceFrame asSeenBy, double duration ) {
		this.animateApplyTranslation( new edu.cmu.cs.dennisc.math.Point3( x, y, z ), asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyTranslation( double x, double y, double z, ReferenceFrame asSeenBy ) {
		this.animateApplyTranslation( new edu.cmu.cs.dennisc.math.Point3( x, y, z ), asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyTranslation( double x, double y, double z ) {
		this.animateApplyTranslation( new edu.cmu.cs.dennisc.math.Point3( x, y, z ), this );
	}

	public void applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy ) {
		this.getSgComposite().applyRotationAboutArbitraryAxisInRadians( axis, angleInRadians, asSeenBy.getSgReferenceFrame() );
	}
	public void applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		this.applyRotationInRadians( axis, angleInRadians, this );
	}
	public void applyRotationInDegrees( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInDegrees, ReferenceFrame asSeenBy ) {
		this.applyRotationInRadians( axis, edu.cmu.cs.dennisc.math.AngleUtilities.degreesToRadians( angleInDegrees ), asSeenBy );
	}
	public void applyRotationInDegrees( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		this.applyRotationInDegrees( axis, angleInRadians, this );
	}
	public void applyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRevolutions, ReferenceFrame asSeenBy ) {
		this.applyRotationInRadians( axis, edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( angleInRevolutions ), asSeenBy );
	}
	public void applyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		this.applyRotationInRevolutions( axis, angleInRadians, this );
	}
	
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		assert axis != null;
		assert duration >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.applyRotationInRadians( axis, angleInRadians, asSeenBy );
		} else {
			class RotateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				private ReferenceFrame asSeenBy;
				private edu.cmu.cs.dennisc.math.Vector3 axis;
				private double angleInRadians;
				private double angleSumInRadians;

				public RotateAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Vector3 axis,double angleInRadians, ReferenceFrame asSeenBy ) {
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
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy, double duration ) {
		this.animateApplyRotationInRadians( axis, angleInRadians, asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy ) {
		this.animateApplyRotationInRadians( axis, angleInRadians, asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		this.animateApplyRotationInRadians( axis, angleInRadians, this );
	}
	
	public void animateApplyRotationInDegrees( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInDegrees, ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyRotationInRadians( axis, edu.cmu.cs.dennisc.math.AngleUtilities.degreesToRadians( angleInDegrees ), asSeenBy, duration, style );
	}
	public void animateApplyRotationInDegrees( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInDegrees, ReferenceFrame asSeenBy, double duration ) {
		this.animateApplyRotationInDegrees( axis, angleInDegrees, asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyRotationInDegrees( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInDegrees, ReferenceFrame asSeenBy ) {
		this.animateApplyRotationInDegrees( axis, angleInDegrees, asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyRotationInDegrees( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInDegrees ) {
		this.animateApplyRotationInDegrees( axis, angleInDegrees, this );
	}

	public void animateApplyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRevolutions, ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateApplyRotationInRadians( axis, edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( angleInRevolutions ), asSeenBy, duration, style );
	}
	public void animateApplyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRevolutions, ReferenceFrame asSeenBy, double duration ) {
		this.animateApplyRotationInRevolutions( axis, angleInRevolutions, asSeenBy, duration, DEFAULT_STYLE );
	}
	public void animateApplyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRevolutions, ReferenceFrame asSeenBy ) {
		this.animateApplyRotationInRevolutions( axis, angleInRevolutions, asSeenBy, DEFAULT_DURATION );
	}
	public void animateApplyRotationInRevolutions( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRevolutions ) {
		this.animateApplyRotationInRevolutions( axis, angleInRevolutions, this );
	}

	
	
	public void setPositionOnly( EntityImplementation target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.getSgComposite().setTranslationOnly( offset != null ? offset : edu.cmu.cs.dennisc.math.Point3.ORIGIN, target.getSgComposite() );
	}
	public void setPositionOnly( EntityImplementation target ) {
		this.setPositionOnly( target, edu.cmu.cs.dennisc.math.Point3.ORIGIN );
	}
	public void animatePositionOnly( final EntityImplementation target, edu.cmu.cs.dennisc.math.Point3 offset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setPositionOnly( target, offset );
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = this.getTransformation( target );
			perform( new edu.cmu.cs.dennisc.math.animation.Point3Animation( adjustDurationIfNecessary( duration ), style, m0.translation, offset != null ? offset : edu.cmu.cs.dennisc.math.Point3.ORIGIN ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.Point3 t ) {
					AbstractTransformableImplementation.this.setPositionOnly( target, t );
				}
			} );
		}
	}
	public void animatePositionOnly( EntityImplementation target, edu.cmu.cs.dennisc.math.Point3 offset, double duration ) {
		this.animatePositionOnly( target, offset, duration, DEFAULT_STYLE );
	}
	public void animatePositionOnly( EntityImplementation target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.animatePositionOnly( target, offset, DEFAULT_DURATION );
	}
	public void animatePositionOnly( EntityImplementation target ) {
		this.animatePositionOnly( target, edu.cmu.cs.dennisc.math.Point3.ORIGIN );
	}
	

	public void setOrientationOnly( EntityImplementation target, edu.cmu.cs.dennisc.math.Orientation offset ) {
		this.getSgComposite().setAxesOnly( offset != null ? offset : edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.accessIdentity(), target.getSgComposite() );
	}
	public void setOrientationOnly( EntityImplementation target ) {
		this.setOrientationOnly( target, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.accessIdentity() );
	}
	public void animateOrientationOnly( final EntityImplementation target, edu.cmu.cs.dennisc.math.Orientation offset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setOrientationOnly( target, offset );
		} else {
			final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 buffer = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			edu.cmu.cs.dennisc.math.UnitQuaternion q0 = new edu.cmu.cs.dennisc.math.UnitQuaternion( this.getTransformation( target ).orientation );
			edu.cmu.cs.dennisc.math.UnitQuaternion q1;
			if( offset != null ) {
				q1 = offset.createUnitQuaternion();
			} else {
				q1 = edu.cmu.cs.dennisc.math.UnitQuaternion.accessIdentity();
			}
			perform( new edu.cmu.cs.dennisc.math.animation.UnitQuaternionAnimation( adjustDurationIfNecessary( duration ), style, q0, q1 ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.UnitQuaternion q ) {
					buffer.setValue( q );
					AbstractTransformableImplementation.this.setOrientationOnly( target, buffer );
				}
			} );
		}
	}
	public void animateOrientationOnly( EntityImplementation target, edu.cmu.cs.dennisc.math.Orientation offset, double duration ) {
		this.animateOrientationOnly( target, offset, duration, DEFAULT_STYLE );
	}
	public void animateOrientationOnly( EntityImplementation target, edu.cmu.cs.dennisc.math.Orientation offset ) {
		this.animateOrientationOnly( target, offset, DEFAULT_DURATION );
	}
	public void animateOrientationOnly( EntityImplementation target ) {
		this.animateOrientationOnly( target, edu.cmu.cs.dennisc.math.UnitQuaternion.accessIdentity() );
	}

	public void setOrientationOnlyToPointAt( ReferenceFrame target ) {
		this.getSgComposite().setAxesOnlyToPointAt( target.getActualEntityImplementation( this ).getSgComposite() );
	}
	
//	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAt( ReferenceFrame target, ReferenceFrame asSeenByForUp ) {
//		return ;
//	}
//	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAt( ReferenceFrame target ) {
//		return this.calculatePointAt( target, AsSeenBy.SCENE );
//	}
	
	public void setTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset ) {
		this.getSgComposite().setTransformation( offset, target.getSgReferenceFrame() );
	}
	public void setTransformation( ReferenceFrame target ) {
		this.setTransformation( target, edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
	}
	public void animateTransformation( final ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			if( offset.isNaN() ) {
				//pass
			} else {
				this.setTransformation( target, offset );
			}
		} else {
			if( offset.isNaN() ) {
				this.alreadyAdjustedDelay( duration );
			} else {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = getTransformation( target );
				perform( new edu.cmu.cs.dennisc.math.animation.AffineMatrix4x4Animation( duration, style, m0, offset ) {
					@Override
					protected void updateValue( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
						setTransformation( target, m );
					}
				} );
			}
		}
	}
	public void animateTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, double duration ) {
		this.animateTransformation( target, offset, duration, DEFAULT_STYLE );
	}
	public void animateTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset ) {
		this.animateTransformation( target, offset, DEFAULT_DURATION );
	}
	public void animateTransformation( ReferenceFrame target ) {
		this.animateTransformation( target, edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
	}
	
	
	
	
	public void applyScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
//		class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Vector3Animation {
//			private edu.cmu.cs.dennisc.math.Vector3 m_vPrev = new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 );
//			private edu.cmu.cs.dennisc.math.Vector3 m_vBuffer = new edu.cmu.cs.dennisc.math.Vector3();
//
//			private Transformable m_subject;
//			private boolean m_isScootDesired;
//			public ScaleAnimation( double duration, Style style, edu.cmu.cs.dennisc.math.Vector3 axis, Transformable subject, boolean isScootDesired ) {
//				super( duration, style, new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 ), axis );
//				m_subject = subject;
//				m_isScootDesired = isScootDesired;
//			}
//			@Override
//
//			protected void updateValue( edu.cmu.cs.dennisc.math.Vector3 v ) {
//				edu.cmu.cs.dennisc.math.Vector3.setReturnValueToDivision( m_vBuffer, v, m_vPrev );
//				m_subject.applyScale( m_vBuffer, m_isScootDesired );
//				m_vPrev.set( v );
//			}
//		}
//		final double actualDuration = adjustDurationIfNecessary( duration );
//		java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
//		updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
//		
//		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
//			for( Transformable transformable : transformables ) {
//				transformable.applyScale( axis, transformable != this );
//			}
//		} else {
//			Runnable[] runnables = new Runnable[ transformables.size() ];
//			int i = 0;
//			for( final Transformable transformable : transformables ) {
//				runnables[ i++ ] = new Runnable() { 
//					public void run() {
//						perform( new ScaleAnimation( actualDuration, style, axis, transformable, transformable!=Transformable.this ) );
//					}
//				};
//			}
//			org.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
//		}
	}
	public void animateApplyScale( edu.cmu.cs.dennisc.math.Dimension3 scale, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		
	}

	protected abstract double getBoundingSphereRadius();
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getSgComposite().getTransformation( asSeenBy.getSgReferenceFrame() );
		return new edu.cmu.cs.dennisc.math.Sphere( m.translation, 1.0 );
	}
}
