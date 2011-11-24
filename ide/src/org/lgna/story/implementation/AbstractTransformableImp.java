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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformableImp extends EntityImp {
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
	@Override
	public void setVehicle(EntityImp vehicle) {
		assert vehicle != this;
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 absTransform = this.getScene() != null ? this.getTransformation(this.getScene()) : this.getLocalTransformation();
		super.setVehicle(vehicle);
		if (vehicle != null)
		{
			this.setTransformation(this.getScene(), absTransform);
		}
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

					AbstractTransformableImp.this.applyTranslation( xPortion, yPortion, zPortion, this.asSeenBy );

					this.xSum += xPortion;
					this.ySum += yPortion;
					this.zSum += zPortion;
				}
				
				@Override
				protected void epilogue() {
					AbstractTransformableImp.this.applyTranslation( this.x - this.xSum, this.y - this.ySum, this.z - this.zSum, this.asSeenBy );
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

					AbstractTransformableImp.this.applyRotationInRadians( this.axis, anglePortionInRadians, this.asSeenBy );

					this.angleSumInRadians += anglePortionInRadians;
				}
				@Override
				protected void epilogue() {
					AbstractTransformableImp.this.applyRotationInRadians( this.axis, this.angleInRadians - this.angleSumInRadians, this.asSeenBy );
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

	
	
	public void setPositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.getSgComposite().setTranslationOnly( offset != null ? offset : edu.cmu.cs.dennisc.math.Point3.ORIGIN, target.getSgComposite() );
	}
	public void setPositionOnly( EntityImp target ) {
		this.setPositionOnly( target, edu.cmu.cs.dennisc.math.Point3.ORIGIN );
	}
	public void animatePositionOnly( final EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setPositionOnly( target, offset );
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = this.getTransformation( target );
			perform( new edu.cmu.cs.dennisc.math.animation.Point3Animation( duration, style, m0.translation, offset != null ? offset : edu.cmu.cs.dennisc.math.Point3.ORIGIN ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.Point3 t ) {
					AbstractTransformableImp.this.setPositionOnly( target, t );
				}
			} );
		}
	}
	public void animatePositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset, double duration ) {
		this.animatePositionOnly( target, offset, duration, DEFAULT_STYLE );
	}
	public void animatePositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.animatePositionOnly( target, offset, DEFAULT_DURATION );
	}
	public void animatePositionOnly( EntityImp target ) {
		this.animatePositionOnly( target, edu.cmu.cs.dennisc.math.Point3.ORIGIN );
	}
	

	protected static final double DEFAULT_PLACE_ALONG_AXIS_OFFSET = 0.0;

	private static class PlaceAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
		private final SpatialRelationImp spatialRelation;
		private final EntityImp target;
		private final double alongAxisOffset;
		public PlaceAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style, SpatialRelationImp spatialRelation, final EntityImp target, double alongAxisOffset ) {
			super( duration, style );
			this.spatialRelation = spatialRelation;
			this.target = target;
			this.alongAxisOffset = alongAxisOffset;
		}
		@Override
		protected void prologue() {
		}
		@Override
		protected void setPortion( double portion ) {
		}
		@Override
		protected void epilogue() {
		}
	}
	public void place( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset ) {
	}
	public void place( SpatialRelationImp spatialRelation, EntityImp target ) {
		this.place( spatialRelation, target, DEFAULT_PLACE_ALONG_AXIS_OFFSET );
	}
	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.place( spatialRelation, target, alongAxisOffset );
		} else {
			perform( new PlaceAnimation( duration, style, spatialRelation, target, alongAxisOffset ) );
		}
	}
	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, double duration ) {
		this.animatePlace( spatialRelation, target, alongAxisOffset, duration, DEFAULT_STYLE );
	}
	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset ) {
		this.animatePlace( spatialRelation, target, alongAxisOffset, DEFAULT_DURATION );
	}
	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target ) {
		this.animatePlace( spatialRelation, target, DEFAULT_PLACE_ALONG_AXIS_OFFSET );
	}
	
	private static abstract class OrientationData {
		private final AbstractTransformableImp subject;

		public OrientationData( AbstractTransformableImp subject ) {
			this.subject = subject;
		}
		public AbstractTransformableImp getSubject() {
			return this.subject;
		}

		protected abstract void setM( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m );
		protected final void setQ( edu.cmu.cs.dennisc.math.UnitQuaternion q ) {
			this.setM( q.createOrthogonalMatrix3x3() );
		}
		protected abstract edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getM0();
		protected abstract edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getM1();
		protected abstract edu.cmu.cs.dennisc.math.UnitQuaternion getQ0();
		protected abstract edu.cmu.cs.dennisc.math.UnitQuaternion getQ1();

		public void setPortion( double portion ) {
			edu.cmu.cs.dennisc.math.UnitQuaternion q0 = this.getQ0();
			edu.cmu.cs.dennisc.math.UnitQuaternion q1 = this.getQ1();
			this.setQ( edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( q0, q1, portion ) );
		}
		public void epilogue() {
			this.setM( this.getM1() );
		}
	}
	private static abstract class PreSetOrientationData extends OrientationData {
		private final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m0;
		private final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m1;
		private edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private edu.cmu.cs.dennisc.math.UnitQuaternion q1;
		public PreSetOrientationData( AbstractTransformableImp subject, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m0, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m1 ) {
			super( subject );
			this.m0 = m0;
			this.m1 = m1;
		}
		@Override
		protected final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getM0() {
			return this.m0;
		}
		@Override
		protected final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getM1() {
			return this.m1;
		}
		@Override
		protected edu.cmu.cs.dennisc.math.UnitQuaternion getQ0() {
			if( this.q0 != null ) {
				//pass
			} else {
				this.q0 = this.m0.createUnitQuaternion();
			}
			return this.q0;
		}
		@Override
		protected edu.cmu.cs.dennisc.math.UnitQuaternion getQ1() {
			if( this.q1 != null ) {
				//pass
			} else {
				this.q1 = this.m1.createUnitQuaternion();
			}
			return this.q1;
		}
	}
	private static class LocalOrientationData extends PreSetOrientationData {
		public LocalOrientationData( AbstractTransformableImp subject, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m1 ) {
			super( subject, subject.getSgComposite().getLocalTransformation().orientation, m1 );
		}
		@Override
		protected void setM( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 prevM = this.getSubject().getSgComposite().getLocalTransformation();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextM = new edu.cmu.cs.dennisc.math.AffineMatrix4x4(
					orientation,
					prevM.translation
			);
			this.getSubject().getSgComposite().setLocalTransformation( nextM );
		}
	}
	private static edu.cmu.cs.dennisc.pattern.DefaultPool< StandInImp > s_standInPool = new edu.cmu.cs.dennisc.pattern.DefaultPool< StandInImp >( StandInImp.class );

	protected static StandInImp acquireStandIn( EntityImp composite ) {
		StandInImp rv = s_standInPool.acquire();
		rv.setVehicle( composite );
		rv.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
		return rv;
	}
	protected static void releaseStandIn( StandInImp standIn ) {
		s_standInPool.release( standIn );
	}

	protected static edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculateTurnToFaceAxes( AbstractTransformableImp subject, EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		//todo: handle offset
		SceneImp asSeenBy = subject.getScene();
		StandInImp standInA = acquireStandIn( asSeenBy );
		try {
			standInA.setPositionOnly( subject );
			edu.cmu.cs.dennisc.math.Point3 targetPos = target.getTransformation( standInA ).translation;
			double targetTheta = Math.atan2( targetPos.z, targetPos.x );

			StandInImp standInB = acquireStandIn( subject );
			try {
				standInB.applyTranslation( 0, 0, -1.0, standInB );
	
				edu.cmu.cs.dennisc.math.Point3 forwardPos = standInB.getTransformation( standInA ).translation;
				double forwardTheta = Math.atan2( forwardPos.z, forwardPos.x );

				standInB.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
				standInB.applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3.accessPositiveYAxis(), targetTheta - forwardTheta, standInA );
				
				return standInB.getTransformation( asSeenBy ).orientation;
			} finally {
				releaseStandIn( standInB );
			}
		} finally {
			releaseStandIn( standInA );
		}
	}
	private static class TurnToFaceOrientationData extends LocalOrientationData {
		public TurnToFaceOrientationData( AbstractTransformableImp subject, EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
			super( subject, calculateTurnToFaceAxes( subject, target, offset ) );
		}
	}

	private static class OrientToUprightData extends PreSetOrientationData {
		private final ReferenceFrame asSeenBy;
		public static OrientToUprightData createInstance( AbstractTransformableImp subject, ReferenceFrame asSeenBy ) {
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation0 = subject.getTransformation( asSeenBy ).orientation;
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation1 = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromStandUp( orientation0 );
			return new OrientToUprightData( subject, orientation0, orientation1, asSeenBy );
		}
		private OrientToUprightData( AbstractTransformableImp subject, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation0, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation1, ReferenceFrame asSeenBy ) {
			super( subject, orientation0, orientation1 );
			this.asSeenBy = asSeenBy;
		}
		@Override
		protected void setM( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m ) {
			this.getSubject().getSgComposite().setAxesOnly( m, this.asSeenBy.getSgReferenceFrame() );
		}
	}
	
	
	
	private void setOrientationOnly( OrientationData data ) {
		data.epilogue();
	}
	private void animateOrientationOnly( final OrientationData data, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			data.epilogue();
		} else {
			perform( new edu.cmu.cs.dennisc.animation.DurationBasedAnimation( duration, style ) {
				@Override
				protected void prologue() {
				}
				@Override
				protected void setPortion(double portion) {
					data.setPortion( portion );
				}
				@Override
				protected void epilogue() {
					data.epilogue();
				}
			} );
		}
	}
	public void setLocalOrientationOnly( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 localOrientation ) {
		this.setOrientationOnly( new LocalOrientationData( this, localOrientation ) );
	}
	public void animateLocalOrientationOnly( final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 localOrientation, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateOrientationOnly( new LocalOrientationData( this, localOrientation ), duration, style );
	}
	
//	private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 createOrientation( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset ) {
//		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getTransformation( target );
//		if( offset != null ) {
//			//todo
//		}
//		return m.orientation;
//	}
	public void setOrientationOnly( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset ) {
		this.getSgComposite().setAxesOnly( offset != null ? offset : edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.accessIdentity(), target.getSgComposite() );
	}
	public void setOrientationOnly( EntityImp target ) {
		this.setOrientationOnly( target, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.accessIdentity() );
	}
	public void animateOrientationOnly( final EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
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
			perform( new edu.cmu.cs.dennisc.math.animation.UnitQuaternionAnimation( duration, style, q0, q1 ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.UnitQuaternion q ) {
					buffer.setValue( q );
					AbstractTransformableImp.this.setOrientationOnly( target, buffer );
				}
			} );
		}
	}
	public void animateOrientationOnly( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset, double duration ) {
		this.animateOrientationOnly( target, offset, duration, DEFAULT_STYLE );
	}
	public void animateOrientationOnly( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset ) {
		this.animateOrientationOnly( target, offset, DEFAULT_DURATION );
	}
	public void animateOrientationOnly( EntityImp target ) {
		this.animateOrientationOnly( target, edu.cmu.cs.dennisc.math.UnitQuaternion.accessIdentity() );
	}

	public void setOrientationOnlyToFace( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.setOrientationOnly( new TurnToFaceOrientationData( this, target, offset ) );
	}
	public void animateOrientationOnlyToFace( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateOrientationOnly( new TurnToFaceOrientationData( this, target, offset ), duration, style );
	}
	public void animateOrientationOnlyToFace( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset, double duration ) {
		this.animateOrientationOnly( target, offset, duration, DEFAULT_STYLE );
	}
	public void animateOrientationOnlyToFace( EntityImp target, edu.cmu.cs.dennisc.math.Orientation offset ) {
		this.animateOrientationOnly( target, offset, DEFAULT_DURATION );
	}
	public void animateOrientationOnlyToFace( EntityImp target ) {
		this.animateOrientationOnly( target, edu.cmu.cs.dennisc.math.UnitQuaternion.accessIdentity() );
	}

	public void setOrientationToUpright( ReferenceFrame asSeenBy ) {
		this.setOrientationOnly( OrientToUprightData.createInstance( this, asSeenBy ) );
	}
	public void setOrientationToUpright() {
		this.setOrientationToUpright( AsSeenBy.SCENE );
	}
	public void animateOrientationToUpright( ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateOrientationOnly( OrientToUprightData.createInstance( this, asSeenBy ), duration, style );
	}

//	public void setOrientationOnlyToUpright() {
//		this.getSgComposite().setAxesOnlyToStandUp();
//	}
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
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1;
			if( offset != null ) {
				m1 = offset;
			} else {
				m1 = edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity();
			}
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = getTransformation( target );
			perform( new edu.cmu.cs.dennisc.math.animation.AffineMatrix4x4Animation( duration, style, m0, m1 ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
					setTransformation( target, m );
				}
			} );
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
	
	
	
//	
//	public void applyScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
////		class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Vector3Animation {
////			private edu.cmu.cs.dennisc.math.Vector3 m_vPrev = new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 );
////			private edu.cmu.cs.dennisc.math.Vector3 m_vBuffer = new edu.cmu.cs.dennisc.math.Vector3();
////
////			private Transformable m_subject;
////			private boolean m_isScootDesired;
////			public ScaleAnimation( double duration, Style style, edu.cmu.cs.dennisc.math.Vector3 axis, Transformable subject, boolean isScootDesired ) {
////				super( duration, style, new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 ), axis );
////				m_subject = subject;
////				m_isScootDesired = isScootDesired;
////			}
////			@Override
////
////			protected void updateValue( edu.cmu.cs.dennisc.math.Vector3 v ) {
////				edu.cmu.cs.dennisc.math.Vector3.setReturnValueToDivision( m_vBuffer, v, m_vPrev );
////				m_subject.applyScale( m_vBuffer, m_isScootDesired );
////				m_vPrev.set( v );
////			}
////		}
////		final double actualDuration = adjustDurationIfNecessary( duration );
////		java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
////		updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
////		
////		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( actualDuration, RIGHT_NOW ) ) {
////			for( Transformable transformable : transformables ) {
////				transformable.applyScale( axis, transformable != this );
////			}
////		} else {
////			Runnable[] runnables = new Runnable[ transformables.size() ];
////			int i = 0;
////			for( final Transformable transformable : transformables ) {
////				runnables[ i++ ] = new Runnable() { 
////					public void run() {
////						perform( new ScaleAnimation( actualDuration, style, axis, transformable, transformable!=Transformable.this ) );
////					}
////				};
////			}
////			org.alice.virtualmachine.DoTogether.invokeAndWait( runnables );
////		}
//	}
//	public void animateApplyScale( edu.cmu.cs.dennisc.math.Dimension3 scale, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
//		
//	}

	protected abstract double getBoundingSphereRadius();
	public edu.cmu.cs.dennisc.math.Sphere getBoundingSphere( ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.getSgComposite().getTransformation( asSeenBy.getSgReferenceFrame() );
		return new edu.cmu.cs.dennisc.math.Sphere( m.translation, 1.0 );
	}
}
