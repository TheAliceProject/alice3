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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Point3;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformableImp extends EntityImp {
	@Override
	public abstract edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSgComposite();

	public boolean isFacing( EntityImp other ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = other.getTransformation( this );
		return m.translation.z < 0.0;
	}

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
	protected void postCheckSetVehicle( EntityImp vehicle ) {
		SceneImp scene = this.getScene();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 absTransform = scene != null ? this.getTransformation( scene ) : null;
		super.postCheckSetVehicle( vehicle );
		if( ( vehicle != null ) && ( scene != null ) ) {
			this.setTransformation( scene, absTransform );
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
					double xPortion = ( this.x * portion ) - this.xSum;
					double yPortion = ( this.y * portion ) - this.ySum;
					double zPortion = ( this.z * portion ) - this.zSum;

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

				public RotateAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy ) {
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
					double anglePortionInRadians = ( this.angleInRadians * portion ) - this.angleSumInRadians;

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

	protected static abstract class VantagePointData {
		private final AbstractTransformableImp subject;

		public VantagePointData( AbstractTransformableImp subject ) {
			this.subject = subject;
		}

		public AbstractTransformableImp getSubject() {
			return this.subject;
		}

		protected abstract void setM( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m );

		protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 getM0();

		protected abstract edu.cmu.cs.dennisc.math.AffineMatrix4x4 getM1();

		protected abstract edu.cmu.cs.dennisc.math.Point3 getT0();

		protected abstract edu.cmu.cs.dennisc.math.Point3 getT1();

		protected abstract edu.cmu.cs.dennisc.math.UnitQuaternion getQ0();

		protected abstract edu.cmu.cs.dennisc.math.UnitQuaternion getQ1();

		public void setPortion( double portion ) {
			edu.cmu.cs.dennisc.math.Point3 t0 = this.getT0();
			edu.cmu.cs.dennisc.math.Point3 t1 = this.getT1();
			edu.cmu.cs.dennisc.math.Point3 t = edu.cmu.cs.dennisc.math.Point3.createInterpolation( t0, t1, portion );
			edu.cmu.cs.dennisc.math.UnitQuaternion q0 = this.getQ0();
			edu.cmu.cs.dennisc.math.UnitQuaternion q1 = this.getQ1();
			edu.cmu.cs.dennisc.math.UnitQuaternion q = edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( q0, q1, portion );
			this.setM( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( q, t ) );
		}

		public void epilogue() {
			this.setM( this.getM1() );
		}
	}

	protected static class PreSetVantagePointData extends VantagePointData {
		private final EntityImp other;
		private final edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0;
		private final edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q1;

		public PreSetVantagePointData( SymmetricPerspectiveCameraImp subject, EntityImp other ) {
			super( subject );
			this.other = other;
			this.m0 = subject.getTransformation( other );
			this.m1 = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
			this.q0 = this.m0.orientation.createUnitQuaternion();
			this.q1 = this.m1.orientation.createUnitQuaternion();
		}

		@Override
		protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 getM0() {
			return this.m0;
		}

		@Override
		protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 getM1() {
			return this.m1;
		}

		@Override
		protected edu.cmu.cs.dennisc.math.UnitQuaternion getQ0() {
			return this.q0;
		}

		@Override
		protected edu.cmu.cs.dennisc.math.UnitQuaternion getQ1() {
			return this.q1;
		}

		@Override
		protected edu.cmu.cs.dennisc.math.Point3 getT0() {
			return this.m0.translation;
		}

		@Override
		protected edu.cmu.cs.dennisc.math.Point3 getT1() {
			return this.m1.translation;
		}

		@Override
		protected void setM( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
			this.getSubject().getSgComposite().setTransformation( m, other.getSgReferenceFrame() );
		}
	}

	protected void setVantagePoint( VantagePointData data ) {
		data.epilogue();
	}

	protected void animateVantagePoint( final VantagePointData data, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			data.epilogue();
		} else {
			perform( new edu.cmu.cs.dennisc.animation.DurationBasedAnimation( duration, style) {
				@Override
				protected void prologue() {
				}

				@Override
				protected void setPortion( double portion ) {
					data.setPortion( portion );
				}

				@Override
				protected void epilogue() {
					data.epilogue( );
				}
			} );
		}
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
			assert q0.isNaN() == false : this;
			assert q1.isNaN() == false : this;
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
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 nextM = new edu.cmu.cs.dennisc.math.AffineMatrix4x4( orientation, prevM.translation );
			this.getSubject().getSgComposite().setLocalTransformation( nextM );
		}
	}

	private static edu.cmu.cs.dennisc.pattern.DefaultPool<StandInImp> s_standInPool = new edu.cmu.cs.dennisc.pattern.DefaultPool<StandInImp>( StandInImp.class );

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
		StandInImp standInA = acquireStandIn( subject );
		try {
			standInA.setPositionOnly( subject );
			edu.cmu.cs.dennisc.math.Point3 targetPos = target.getTransformation( subject ).translation;
			if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( targetPos.x, 0.0 ) && edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( targetPos.z, 0.0 ) ) {
				//todo
				return subject.getLocalOrientation();
			} else {
				targetPos.y = 0;
				targetPos.normalize();
				StandInImp standInB = acquireStandIn( subject );
				try {
					//Move a standin to the position of the target
					standInB.applyTranslation( targetPos.x, targetPos.y, targetPos.z, standInB );
					edu.cmu.cs.dennisc.math.Point3 standinPosition = standInB.getTransformation( subject.getVehicle() ).translation;
					//Calculate the vector pointing from the subject to the target all in the reference frame of the subject's vehicle
					edu.cmu.cs.dennisc.math.Point3 forwardPos = (edu.cmu.cs.dennisc.math.Point3)edu.cmu.cs.dennisc.math.Point3.setReturnValueToSubtraction( new edu.cmu.cs.dennisc.math.Point3(), standinPosition, subject.getLocalPosition() );
					//Take that vector and normalize it to create the new "forward" vector for the subject
					edu.cmu.cs.dennisc.math.Vector3 newForward = new edu.cmu.cs.dennisc.math.Vector3( forwardPos );
					newForward.normalize();
					//Make a new orientation using this new "forward" and the existing "up" from the subject
					edu.cmu.cs.dennisc.math.ForwardAndUpGuide fAndG = new edu.cmu.cs.dennisc.math.ForwardAndUpGuide( newForward, subject.getLocalOrientation().up );
					return new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( fAndG );
				} finally {
					releaseStandIn( standInB );
				}
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
		private final ReferenceFrame upAsSeenBy;

		public static OrientToUprightData createInstance( AbstractTransformableImp subject, ReferenceFrame upAsSeenBy ) {
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation0 = subject.getTransformation( upAsSeenBy ).orientation;
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation1 = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromStandUp( orientation0 );
			return new OrientToUprightData( subject, orientation0, orientation1, upAsSeenBy );
		}

		private OrientToUprightData( AbstractTransformableImp subject, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation0, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation1, ReferenceFrame upAsSeenBy ) {
			super( subject, orientation0, orientation1 );
			this.upAsSeenBy = upAsSeenBy;
		}

		@Override
		protected void setM( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m ) {
			this.getSubject().getSgComposite().setAxesOnly( m, this.upAsSeenBy.getSgReferenceFrame() );
		}
	}

	private static class OrientToPointAtData extends PreSetOrientationData {
		private final ReferenceFrame upAsSeenBy;

		public static OrientToPointAtData createInstance( AbstractTransformableImp subject, EntityImp target, ReferenceFrame upAsSeenBy ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = subject.getTransformation( upAsSeenBy );
			edu.cmu.cs.dennisc.math.Point3 t0 = m0.translation;
			edu.cmu.cs.dennisc.math.Point3 t1 = target.getTransformation( upAsSeenBy ).translation;
			edu.cmu.cs.dennisc.math.Vector3 forward = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( t1, t0 );
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 o1;
			if( forward.isZero() ) {
				o1 = m0.orientation;
				//no op
			} else {
				edu.cmu.cs.dennisc.math.ForwardAndUpGuide fowardAndUpGuide = new edu.cmu.cs.dennisc.math.ForwardAndUpGuide( forward, null );
				o1 = fowardAndUpGuide.createOrthogonalMatrix3x3();
			}
			return new OrientToPointAtData( subject, m0.orientation, o1, upAsSeenBy );
		}

		private OrientToPointAtData( AbstractTransformableImp subject, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation0, edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation1, ReferenceFrame upAsSeenBy ) {
			super( subject, orientation0, orientation1 );
			this.upAsSeenBy = upAsSeenBy;
		}

		@Override
		protected void setM( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m ) {
			this.getSubject().getSgComposite().setAxesOnly( m, this.upAsSeenBy.getSgReferenceFrame() );
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
			perform( new edu.cmu.cs.dennisc.animation.DurationBasedAnimation( duration, style) {
				@Override
				protected void prologue() {
				}

				@Override
				protected void setPortion( double portion ) {
					data.setPortion( portion );
				}

				@Override
				protected void epilogue() {
					data.epilogue( );
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
			perform( new edu.cmu.cs.dennisc.math.animation.UnitQuaternionAnimation( duration, style, q0, q1) {
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

	public void setOrientationToUpright( ReferenceFrame upAsSeenBy ) {
		this.setOrientationOnly( OrientToUprightData.createInstance( this, upAsSeenBy ) );
	}

	public void setOrientationToUpright() {
		this.setOrientationToUpright( AsSeenBy.SCENE );
	}

	public void animateOrientationToUpright( ReferenceFrame upAsSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateOrientationOnly( OrientToUprightData.createInstance( this, upAsSeenBy ), duration, style );
	}

	public void animateOrientationToUpright( ReferenceFrame upAsSeenBy, double duration ) {
		this.animateOrientationToUpright( upAsSeenBy, duration, DEFAULT_STYLE );
	}

	public void animateOrientationToUpright( ReferenceFrame upAsSeenBy ) {
		this.animateOrientationToUpright( upAsSeenBy, DEFAULT_DURATION );
	}

	public void animateOrientationToUpright() {
		this.animateOrientationToUpright( AsSeenBy.SCENE );
	}

	public void setOrientationToPointAt( EntityImp target, ReferenceFrame upAsSeenBy ) {
		this.setOrientationOnly( OrientToPointAtData.createInstance( this, target, upAsSeenBy ) );
	}

	public void setOrientationToPointAt( EntityImp target ) {
		this.setOrientationToPointAt( target, AsSeenBy.SCENE );
	}

	public void animateOrientationToPointAt( EntityImp target, ReferenceFrame upAsSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.animateOrientationOnly( OrientToPointAtData.createInstance( this, target, upAsSeenBy ), duration, style );
	}

	public void animateOrientationToPointAt( EntityImp target, ReferenceFrame upAsSeenBy, double duration ) {
		this.animateOrientationToPointAt( target, upAsSeenBy, duration, DEFAULT_STYLE );
	}

	public void animateOrientationToPointAt( EntityImp target, ReferenceFrame upAsSeenBy ) {
		this.animateOrientationToPointAt( target, upAsSeenBy, DEFAULT_DURATION );
	}

	public void animateOrientationToPointAt( EntityImp target ) {
		this.animateOrientationToPointAt( target, AsSeenBy.SCENE );
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

	private static final boolean DEFAULT_IS_SMOOTH = true;
	private static final double DEFAULT_PLACE_ALONG_AXIS_OFFSET = 0.0;

	private static abstract class SmoothAffineMatrix4x4Animation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
		protected final edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1;
		protected final edu.cmu.cs.dennisc.math.polynomial.HermiteCubic xHermite;
		protected final edu.cmu.cs.dennisc.math.polynomial.HermiteCubic yHermite;
		protected final edu.cmu.cs.dennisc.math.polynomial.HermiteCubic zHermite;

		public SmoothAffineMatrix4x4Animation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
			super( duration, style );
			this.m1 = m1;

			double s = -8;//this.m0.translation.calculateMagnitude();
			this.xHermite = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( m0.translation.x, m1.translation.x, s * m0.orientation.backward.x, s * m1.orientation.backward.x );
			this.yHermite = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( m0.translation.y, m1.translation.y, s * m0.orientation.backward.y, s * m1.orientation.backward.y );
			this.zHermite = new edu.cmu.cs.dennisc.math.polynomial.HermiteCubic( m0.translation.z, m1.translation.z, s * m0.orientation.backward.z, s * m1.orientation.backward.z );
		}

		@Override
		protected void prologue() {
		}
	}

	private static class SmoothPositionAnimation extends SmoothAffineMatrix4x4Animation {
		private final AbstractTransformableImp subject;
		private final ReferenceFrame asSeenBy;

		public SmoothPositionAnimation( AbstractTransformableImp subject, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1, ReferenceFrame asSeenBy, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
			super( subject.getTransformation( asSeenBy ), m1, duration, style );
			this.subject = subject;
			this.asSeenBy = asSeenBy;
		}

		@Override
		protected void setPortion( double portion ) {
			double x = this.xHermite.evaluate( portion );
			double y = this.yHermite.evaluate( portion );
			double z = this.zHermite.evaluate( portion );

			//			double dx = this.xHermite.evaluateDerivative( portion );
			//			double dy = this.yHermite.evaluateDerivative( portion );
			//			double dz = this.zHermite.evaluateDerivative( portion );

			this.subject.getSgComposite().setTranslationOnly( x, y, z, this.asSeenBy.getSgReferenceFrame() );
		}

		@Override
		protected void epilogue() {
			this.subject.getSgComposite().setTranslationOnly( this.m1.translation, this.asSeenBy.getSgReferenceFrame() );
		}
	}

	private static class PlaceData {
		private final AbstractTransformableImp subject;
		private final SpatialRelationImp spatialRelation;
		private final EntityImp target;
		private final double alongAxisOffset;
		private final ReferenceFrame asSeenBy;

		public PlaceData( AbstractTransformableImp subject, SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy ) {
			assert subject != null;
			//assert target != null;
			assert asSeenBy != null;
			assert spatialRelation != null;
			assert Double.isNaN( alongAxisOffset ) == false;
			this.subject = subject;
			this.spatialRelation = spatialRelation;
			this.target = target;
			this.alongAxisOffset = alongAxisOffset;
			this.asSeenBy = asSeenBy;
		}

		public edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateTranslation0() {
			return this.subject.getTransformation( this.asSeenBy );
		}

		public edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateTranslation1( edu.cmu.cs.dennisc.math.AffineMatrix4x4 t0 ) {
			edu.cmu.cs.dennisc.math.AxisAlignedBox bbSubject = this.subject.getAxisAlignedMinimumBoundingBox();
			if( this.target != null ) {
				edu.cmu.cs.dennisc.math.AxisAlignedBox bbTarget;
				bbTarget = this.target.getAxisAlignedMinimumBoundingBox( this.asSeenBy );
				assert bbSubject != null;
				assert bbTarget != null;
				if( bbSubject.isNaN() || bbTarget.isNaN() ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "bounding box is NaN", bbSubject, bbTarget );
					return t0;
				} else {
					edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = this.target.getTransformation( this.asSeenBy );
					m.translation.set( this.spatialRelation.getPlaceLocation( this.alongAxisOffset, bbSubject, bbTarget ) );
					return m;
				}
			} else {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
				double y;
				if( bbSubject.isNaN() ) {
					y = 0;
				} else {
					y = -bbSubject.getMinimum().y;
				}
				m.translation.set( t0.translation.x, y, t0.translation.z );
				return m;
			}
		}

		public void setTranslation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 translation ) {
			this.subject.getSgComposite().setTransformation( translation, this.asSeenBy.getSgReferenceFrame() );
		}
	}

	private static class PlaceAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
		private final PlaceData placeData;
		private edu.cmu.cs.dennisc.math.Point3 p0;
		private edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private edu.cmu.cs.dennisc.math.Point3 p1;
		private edu.cmu.cs.dennisc.math.UnitQuaternion q1;

		public PlaceAnimation( PlaceData placeData, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
			super( duration, style );
			this.placeData = placeData;
		}

		@Override
		protected void prologue() {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = this.placeData.calculateTranslation0();
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1 = this.placeData.calculateTranslation1( m0 );
			this.p0 = m0.translation;
			this.q0 = m0.orientation.createUnitQuaternion();
			this.p1 = m1.translation;
			this.q1 = m1.orientation.createUnitQuaternion();
		}

		@Override
		protected void setPortion( double portion ) {
			edu.cmu.cs.dennisc.math.Point3 p = edu.cmu.cs.dennisc.math.Point3.createInterpolation( this.p0, this.p1, portion );
			edu.cmu.cs.dennisc.math.UnitQuaternion q = edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( this.q0, this.q1, portion );
			this.placeData.setTranslation( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( q, p ) );
		}

		@Override
		protected void epilogue() {
			this.placeData.setTranslation( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( this.q1, this.p1 ) );
		}
	}

	public void setPositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.getSgComposite().setTranslationOnly( offset != null ? offset : edu.cmu.cs.dennisc.math.Point3.ORIGIN, target.getSgComposite() );
	}

	public void setPositionOnly( EntityImp target ) {
		this.setPositionOnly( target, edu.cmu.cs.dennisc.math.Point3.ORIGIN );
	}

	public void animatePositionOnly( final EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset, boolean isSmooth, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.setPositionOnly( target, offset );
		} else {
			if( isSmooth ) {
				this.perform( new SmoothPositionAnimation( this, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity(), target, duration, style ) );
			} else {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = this.getTransformation( target );
				perform( new edu.cmu.cs.dennisc.math.animation.Point3Animation( duration, style, m0.translation, offset != null ? offset : edu.cmu.cs.dennisc.math.Point3.ORIGIN) {
					@Override
					protected void updateValue( edu.cmu.cs.dennisc.math.Point3 t ) {
						AbstractTransformableImp.this.setPositionOnly( target, t );
					}
				} );
			}
		}
	}

	public void animatePositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset, boolean isSmooth, double duration ) {
		this.animatePositionOnly( target, offset, isSmooth, duration, DEFAULT_STYLE );
	}

	public void animatePositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset, boolean isSmooth ) {
		this.animatePositionOnly( target, offset, isSmooth, DEFAULT_DURATION );
	}

	public void animatePositionOnly( EntityImp target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		this.animatePositionOnly( target, offset, DEFAULT_IS_SMOOTH );
	}

	public void animatePositionOnly( EntityImp target ) {
		this.animatePositionOnly( target, edu.cmu.cs.dennisc.math.Point3.ORIGIN );
	}

	public void place( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy ) {
		PlaceData placeData = new PlaceData( this, spatialRelation, target, alongAxisOffset, asSeenBy );
		placeData.setTranslation( placeData.calculateTranslation1( placeData.calculateTranslation0() ) );
	}

	public void place( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset ) {
		this.place( spatialRelation, target, alongAxisOffset, target );
	}

	public void place( SpatialRelationImp spatialRelation, EntityImp target ) {
		this.place( spatialRelation, target, DEFAULT_PLACE_ALONG_AXIS_OFFSET );
	}

	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy, boolean isSmooth, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		PlaceData placeData = new PlaceData( this, spatialRelation, target, alongAxisOffset, asSeenBy );
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = placeData.calculateTranslation0();
			assert m0.isNaN() == false : this;
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1 = placeData.calculateTranslation1( m0 );
			assert m1.isNaN() == false : this;
			placeData.setTranslation( m1 );
		} else {
			this.perform( new PlaceAnimation( placeData, duration, style ) );
		}
	}

	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy, boolean isSmooth, double duration ) {
		this.animatePlace( spatialRelation, target, alongAxisOffset, asSeenBy, isSmooth, duration, DEFAULT_STYLE );
	}

	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy, boolean isSmooth ) {
		this.animatePlace( spatialRelation, target, alongAxisOffset, asSeenBy, isSmooth, DEFAULT_DURATION );
	}

	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset, ReferenceFrame asSeenBy ) {
		this.animatePlace( spatialRelation, target, alongAxisOffset, asSeenBy, DEFAULT_IS_SMOOTH );
	}

	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target, double alongAxisOffset ) {
		final ReferenceFrame DEFAULT_AS_SEEN_BY = target;
		this.animatePlace( spatialRelation, target, alongAxisOffset, DEFAULT_AS_SEEN_BY );
	}

	public void animatePlace( SpatialRelationImp spatialRelation, EntityImp target ) {
		this.animatePlace( spatialRelation, target, DEFAULT_PLACE_ALONG_AXIS_OFFSET );
	}

	public void setTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset ) {
		assert target != null : this;
		assert this.getSgComposite() != null : this;
		if( offset != null ) {
			//pass
		} else {
			offset = edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity();
		}
		this.getSgComposite().setTransformation( offset, target.getSgReferenceFrame() );
	}

	public void setTransformation( ReferenceFrame target ) {
		this.setTransformation( target, edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
	}

	public void animateTransformation( final ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, boolean isSmooth, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			if( ( offset != null ) && offset.isNaN() ) {
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
			//			if( isSmooth ) {
			//				this.perform( new SmoothAffineMatrix4x4Animation( duration, style, m0, m1 ) );
			//			} else {
			perform( new edu.cmu.cs.dennisc.math.animation.AffineMatrix4x4Animation( duration, style, m0, m1) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.AffineMatrix4x4 m ) {
					setTransformation( target, m );
				}
			} );
			//			}
		}
	}

	public void animateTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, boolean isSmooth, double duration ) {
		this.animateTransformation( target, offset, isSmooth, duration, DEFAULT_STYLE );
	}

	public void animateTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset, boolean isSmooth ) {
		this.animateTransformation( target, offset, isSmooth, DEFAULT_DURATION );
	}

	public void animateTransformation( ReferenceFrame target, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset ) {
		this.animateTransformation( target, offset, DEFAULT_IS_SMOOTH );
	}

	public void animateTransformation( ReferenceFrame target ) {
		this.animateTransformation( target, edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
	}

	public double getDistanceTo( AbstractTransformableImp other ) {
		edu.cmu.cs.dennisc.math.Point3 translation = this.getSgComposite().getTranslation( other.getSgComposite() );
		return translation.calculateMagnitude();
	}

	private enum SpatialRelationDimension {
		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
		FRONT,
		BACK
	}

	private double getSpatialRelationPoint( EntityImp other, SpatialRelationDimension spatialRelation, ReferenceFrame asSeenBy ) {
		if( other instanceof ModelImp ) {
			ModelImp modelImp = (ModelImp)other;
			AxisAlignedBox bbox = modelImp.getDynamicAxisAlignedMinimumBoundingBox( asSeenBy );
			switch( spatialRelation ) {
			case RIGHT:
				return bbox.getXMaximum();
			case LEFT:
				return bbox.getXMinimum();
			case TOP:
				return bbox.getYMaximum();
			case BOTTOM:
				return bbox.getYMinimum();
			case BACK:
				return bbox.getZMaximum();
			case FRONT:
				return bbox.getZMinimum();
			default:
				return 0;
			}
		} else {
			Point3 point = other.getSgComposite().getTranslation( asSeenBy.getSgReferenceFrame() );
			switch( spatialRelation ) {
			case RIGHT:
			case LEFT:
				return point.x;
			case TOP:
			case BOTTOM:
				return point.y;
			case BACK:
			case FRONT:
				return point.z;
			default:
				return 0;
			}
		}
	}

	public double getDistanceAbove( EntityImp other, ReferenceFrame asSeenBy ) {
		double thisBottomY = getSpatialRelationPoint( this, SpatialRelationDimension.BOTTOM, asSeenBy );
		double otherTopY = getSpatialRelationPoint( other, SpatialRelationDimension.TOP, asSeenBy );
		double value = thisBottomY - otherTopY;
		if( EpsilonUtilities.isWithinEpsilon( value, 0d, .01d ) ) {
			value = 0;
		}
		return value;
	}

	public double getDistanceBelow( EntityImp other, ReferenceFrame asSeenBy ) {
		double thisTopY = getSpatialRelationPoint( this, SpatialRelationDimension.TOP, asSeenBy );
		double otherBottomY = getSpatialRelationPoint( other, SpatialRelationDimension.BOTTOM, asSeenBy );
		double value = otherBottomY - thisTopY;
		if( EpsilonUtilities.isWithinEpsilon( value, 0d, .01d ) ) {
			value = 0;
		}
		return value;
	}

	public double getDistanceBehind( EntityImp other, ReferenceFrame asSeenBy ) {
		double thisFrontZ = getSpatialRelationPoint( this, SpatialRelationDimension.FRONT, asSeenBy );
		double otherBackZ = getSpatialRelationPoint( other, SpatialRelationDimension.BACK, asSeenBy );
		double value = otherBackZ - thisFrontZ;
		if( EpsilonUtilities.isWithinEpsilon( value, 0d, .01d ) ) {
			value = 0;
		}
		return value;
	}

	public double getDistanceInFrontOf( EntityImp other, ReferenceFrame asSeenBy ) {
		double thisBackZ = getSpatialRelationPoint( this, SpatialRelationDimension.BACK, asSeenBy );
		double otherFrontZ = getSpatialRelationPoint( other, SpatialRelationDimension.FRONT, asSeenBy );
		double value = thisBackZ - otherFrontZ;
		if( EpsilonUtilities.isWithinEpsilon( value, 0d, .01d ) ) {
			value = 0;
		}
		return value;
	}

	public double getDistanceToTheLeftOf( EntityImp other, ReferenceFrame asSeenBy ) {
		double thisRightX = getSpatialRelationPoint( this, SpatialRelationDimension.RIGHT, asSeenBy );
		double otherLeftX = getSpatialRelationPoint( other, SpatialRelationDimension.LEFT, asSeenBy );
		double value = thisRightX - otherLeftX;
		if( EpsilonUtilities.isWithinEpsilon( value, 0d, .01d ) ) {
			value = 0;
		}
		return value;
	}

	public double getDistanceToTheRightOf( EntityImp other, ReferenceFrame asSeenBy ) {
		double thisLeftX = getSpatialRelationPoint( this, SpatialRelationDimension.RIGHT, asSeenBy );
		double otherRightX = getSpatialRelationPoint( other, SpatialRelationDimension.LEFT, asSeenBy );
		double value = otherRightX - thisLeftX;
		if( EpsilonUtilities.isWithinEpsilon( value, 0d, .01d ) ) {
			value = 0;
		}
		return value;
	}
}
