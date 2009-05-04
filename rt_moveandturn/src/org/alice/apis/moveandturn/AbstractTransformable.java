/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dennis Cosgrove
 */
@ClassTemplate(isFollowToSuperClassDesired = true, isConsumptionBySubClassDesired=true)
public abstract class AbstractTransformable extends Composite {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< PointOfView > LOCAL_POINT_OF_VIEW_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< PointOfView >( Transformable.class, "LocalPointOfView" );

	@Override
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.scenegraph.Composite getSGComposite() {
		return getSGAbstractTransformable();
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public abstract edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getSGAbstractTransformable();

	@PropertyGetterTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public AffineMatrix4x4 getLocalTransformation() {
		return getSGAbstractTransformable().getLocalTransformation();
	}
	public void setLocalTransformation( AffineMatrix4x4 transformation ) {
		getSGAbstractTransformable().setLocalTransformation( transformation );
	}

	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public PointOfView getLocalPointOfView() {
		return new PointOfView( getLocalTransformation() );
	}
	public void setLocalPointOfView( PointOfView pointOfView ) {
		setLocalTransformation( pointOfView.getInternal() );
	}


	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, ReferenceFrame asSeenBy ) {
		assert asSeenBy != null;
		return getSGAbstractTransformable().getTransformation( rv, asSeenBy.getSGReferenceFrame() );
	}
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( ReferenceFrame asSeenBy ) {
		return getTransformation( new edu.cmu.cs.dennisc.math.AffineMatrix4x4(), asSeenBy );
	}

	public abstract Composite getVehicle();
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	@Override
	public Scene getScene() {
		if( getVehicle() != null ) {
			return getVehicle().getScene();
		} else {
			return null;
		}
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	@Override
	public SceneOwner getOwner() {
		Scene scene = getScene();
		if( scene != null ) {
			return scene.getOwner();
		} else {
			return null;
		}
	}

	protected StandIn acquireStandIn( ReferenceFrame referenceFrame ) {
		Composite composite;
		if( referenceFrame instanceof Composite ) {
			composite = (Composite)referenceFrame;
		} else {
			if( referenceFrame == AsSeenBy.SCENE ) {
				composite = this.getScene();
			} else if( referenceFrame == AsSeenBy.PARENT ) {
				composite = this.getVehicle();
			} else if( referenceFrame == AsSeenBy.SELF ) {
				composite = this;
			} else {
				throw new RuntimeException();
			}
		}
		return Composite.acquireStandIn( composite );
	}
	protected StandIn acquireStandIn( ReferenceFrame referenceFrame, edu.cmu.cs.dennisc.math.Point3 offset ) {
		StandIn rv = acquireStandIn( referenceFrame );
		assert offset != null;
		//todo
		rv.getSGAbstractTransformable().setTranslationOnly( offset, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT );
		return rv;
	}
	protected StandIn acquireStandIn( ReferenceFrame referenceFrame, edu.cmu.cs.dennisc.math.Orientation offset ) {
		StandIn rv = acquireStandIn( referenceFrame );
		assert offset != null;
		//todo
		rv.getSGAbstractTransformable().setAxesOnly( offset, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT );
		return rv;
	}
	protected StandIn acquireStandIn( ReferenceFrame referenceFrame, edu.cmu.cs.dennisc.math.AffineMatrix4x4 offset ) {
		StandIn rv = acquireStandIn( referenceFrame );
		assert offset != null;
		//todo
		rv.getSGAbstractTransformable().setLocalTransformation( offset );
		return rv;
	}

	private void applyTranslation( double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		getSGAbstractTransformable().applyTranslation( x, y, z, sgAsSeenBy );
	}
	protected void applyTranslation( double x, double y, double z, Number duration, ReferenceFrame asSeenBy, Style style ) {
		assert Double.isNaN( x ) == false;
		assert Double.isNaN( y ) == false;
		assert Double.isNaN( z ) == false;
		assert duration.doubleValue() >= 0 : "Invalid argument: duration " + duration + " must be >= 0";
		assert style != null;
		assert asSeenBy != null;

		class TranslateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
			private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgAsSeenBy;
			private double m_x;
			private double m_y;
			private double m_z;
			private double m_xSum;
			private double m_ySum;
			private double m_zSum;

			public TranslateAnimation( Number duration, Style style, double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
				super( duration, style );
				m_x = x;
				m_y = y;
				m_z = z;
				m_sgAsSeenBy = sgAsSeenBy;
			}
			
			@Override
			protected void prologue() {
				m_xSum = 0;
				m_ySum = 0;
				m_zSum = 0;
			}
			
			@Override
			protected void setPortion( double portion ) {
				double xPortion = (m_x * portion) - m_xSum;
				double yPortion = (m_y * portion) - m_ySum;
				double zPortion = (m_z * portion) - m_zSum;

				AbstractTransformable.this.applyTranslation( xPortion, yPortion, zPortion, m_sgAsSeenBy );

				m_xSum += xPortion;
				m_ySum += yPortion;
				m_zSum += zPortion;
			}
			
			@Override
			protected void epilogue() {
				AbstractTransformable.this.applyTranslation( m_x - m_xSum, m_y - m_ySum, m_z - m_zSum, m_sgAsSeenBy );
			}
		}

		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			applyTranslation( x, y, z, asSeenBy.getSGReferenceFrame() );
		} else {
			perform( new TranslateAnimation( duration, style, x, y, z, asSeenBy.getSGReferenceFrame() ) );
		}
	}

	private void applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		getSGAbstractTransformable().applyRotationAboutArbitraryAxisInRadians( axis, angleInRadians, sgAsSeenBy );
	}
	protected void applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, Number duration, ReferenceFrame asSeenBy, Style style ) {
		assert axis != null;
		assert duration.doubleValue() >= 0 : "Invalid argument: duration " + duration + " must be >= 0";

		class RotateAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
			private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgAsSeenBy;
			private edu.cmu.cs.dennisc.math.Vector3 m_axis;
			private double m_angleInRadians;
			private double m_angleSumInRadians;

			public RotateAnimation( Number duration, Style style, edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
				super( duration, style );
				m_axis = axis;
				m_angleInRadians = angleInRadians;
				m_sgAsSeenBy = sgAsSeenBy;
			}
			@Override
			protected void prologue() {
				m_angleSumInRadians = 0;
			}
			@Override
			protected void setPortion( double portion ) {
				double anglePortionInRadians = (m_angleInRadians * portion) - m_angleSumInRadians;

				applyRotationInRadians( m_axis, anglePortionInRadians, m_sgAsSeenBy );

				m_angleSumInRadians += anglePortionInRadians;
			}
			@Override
			protected void epilogue() {
				applyRotationInRadians( m_axis, m_angleInRadians - m_angleSumInRadians, m_sgAsSeenBy );
			}
		}

		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			applyRotationInRadians( axis, angleInRadians, asSeenBy.getSGReferenceFrame() );
		} else {
			perform( new RotateAnimation( duration, style, axis, angleInRadians, asSeenBy.getSGReferenceFrame() ) );
		}
	}

	private void setTranslationOnly( edu.cmu.cs.dennisc.math.Point3 t, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame ) {
		getSGAbstractTransformable().setTranslationOnly( t, sgReferenceFrame );
	}
	protected void setTranslationOnly( ReferenceFrame target, Number duration, Style style ) {
		final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame = edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE;
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m1 = target.getSGReferenceFrame().getAbsoluteTransformation();
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			setTranslationOnly( m1.translation, sgReferenceFrame );
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = getSGAbstractTransformable().getAbsoluteTransformation();
			perform( new edu.cmu.cs.dennisc.math.animation.Point3Animation( adjustDurationIfNecessary( duration ), style, m0.translation, m1.translation ) {
				@Override
				protected void updateValue( edu.cmu.cs.dennisc.math.Point3 t ) {
					setTranslationOnly( t, sgReferenceFrame );
				}
			} );
		}
	}
	private void setAxesOnly( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy ) {
		getSGAbstractTransformable().setAxesOnly( orientation, sgAsSeenBy );
	}
	private class SetAxesOnlyAnimation extends edu.cmu.cs.dennisc.math.animation.UnitQuaternionAnimation {
		private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgReferenceFrame;
		private edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 m_buffer = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
		public SetAxesOnlyAnimation( Number duration, Style style, edu.cmu.cs.dennisc.math.UnitQuaternion q0, edu.cmu.cs.dennisc.math.UnitQuaternion q1, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame ) {
			super( duration, style, q0, q1 );
			m_sgReferenceFrame = sgReferenceFrame;
		}
		@Override
		protected void updateValue( edu.cmu.cs.dennisc.math.UnitQuaternion q ) {
			m_buffer.setValue( q );
			setAxesOnly( m_buffer, m_sgReferenceFrame );
		}
	}
	protected void setAxesOnly( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes, Number duration, Style style ) {
		final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame = edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE;
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			if( axes.isNaN() ) {
				//pass
			} else {
				setAxesOnly( axes, sgReferenceFrame );
			}
		} else {
			if( axes.isNaN() ) {
				alreadyAdjustedDelay( duration );
			} else {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = getSGAbstractTransformable().getAbsoluteTransformation();
				perform( new SetAxesOnlyAnimation( duration, style, m0.orientation.createUnitQuaternion(), axes.createUnitQuaternion(), sgReferenceFrame ) );
			}
		}
	}
	protected void setAxesOnly( ReferenceFrame target, Number duration, Style style ) {
		setAxesOnly( target.getSGReferenceFrame().getAbsoluteTransformation().orientation, duration, style );
	}
	protected void setAxesOnlyToUpright( Number duration, ReferenceFrame asSeenBy, Style style ) {
		final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame = asSeenBy.getSGReferenceFrame();
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes0 = getSGAbstractTransformable().getAxes( sgReferenceFrame );
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes1 = edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromStandUp( axes0 );
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			setAxesOnly( axes1, sgReferenceFrame );
		} else {
			perform( new SetAxesOnlyAnimation( duration, style, axes0.createUnitQuaternion(), axes1.createUnitQuaternion(), sgReferenceFrame ) );
		}
	}

	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAtAxes( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rv, ReferenceFrame target ) {
		edu.cmu.cs.dennisc.math.Point3 t0 = this.getTransformation( AsSeenBy.SCENE ).translation;
		edu.cmu.cs.dennisc.math.Point3 t1 = target.getTransformation( AsSeenBy.SCENE ).translation;
		edu.cmu.cs.dennisc.math.Vector3 forward = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( t1, t0 );
		edu.cmu.cs.dennisc.math.ForwardAndUpGuide fowardAndUpGuide = new edu.cmu.cs.dennisc.math.ForwardAndUpGuide( forward, null );
		rv.setValue( fowardAndUpGuide );
		return rv;
	}
	protected final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculatePointAtAxes( ReferenceFrame target ) {
		return calculatePointAtAxes( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createNaN(), target );
	}
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculateTurnToFaceAxes( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rv, ReferenceFrame target ) {
		Scene asSeenBy = getScene();
		StandIn standInA = acquireStandIn( asSeenBy );
		try {
			standInA.moveTo( this, RIGHT_NOW );
			edu.cmu.cs.dennisc.math.Point3 targetPos = target.getTransformation( standInA ).translation;
			double targetTheta = Math.atan2( targetPos.z, targetPos.x );

			StandIn standInB = acquireStandIn( this );
			try {
				standInB.move( MoveDirection.FORWARD, 1.0, RIGHT_NOW );
	
				edu.cmu.cs.dennisc.math.Point3 forwardPos = standInB.getTransformation( standInA ).translation;
				double forwardTheta = Math.atan2( forwardPos.z, forwardPos.x );
				
				standInB.setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.accessIdentity() );
				standInB.turn( TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRadians( targetTheta - forwardTheta ), RIGHT_NOW, standInA );
				
				rv.setValue( standInB.getTransformation( asSeenBy ).orientation );
				return rv;
			} finally {
				releaseStandIn( standInB );
			}
		} finally {
			releaseStandIn( standInA );
		}
	}
	protected edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 calculateTurnToFaceAxes( ReferenceFrame target ) {
		return calculateTurnToFaceAxes( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createNaN(), target );
	}

	private void setPointOfView( edu.cmu.cs.dennisc.math.AffineMatrix4x4 pointOfView, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame ) {
		getSGAbstractTransformable().setTransformation( pointOfView, sgReferenceFrame );
	}
	protected void setPointOfView( edu.cmu.cs.dennisc.math.AffineMatrix4x4 pointOfView, Number duration, ReferenceFrame asSeenBy, Style style ) {
		final edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgReferenceFrame = asSeenBy.getSGReferenceFrame();
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			if( pointOfView.isNaN() ) {
				//pass
			} else {
				setPointOfView( pointOfView, sgReferenceFrame );
			}
		} else {
			if( pointOfView.isNaN() ) {
				alreadyAdjustedDelay( duration );
			} else {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m0 = getTransformation( asSeenBy );
				perform( new edu.cmu.cs.dennisc.math.animation.AffineMatrix4x4Animation( duration, style, m0, pointOfView ) {
					@Override
					protected void updateValue( AffineMatrix4x4 pointOfView ) {
						setPointOfView( pointOfView, sgReferenceFrame );
					}
				} );
			}
		}
	}
	

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void move( MoveDirection direction, Number amount, Number duration, ReferenceFrame asSeenBy, Style style ) {
		edu.cmu.cs.dennisc.math.Vector3 axis = direction.getAxis();
		applyTranslation( axis.x * amount.doubleValue(), axis.y * amount.doubleValue(), axis.z * amount.doubleValue(), duration, asSeenBy, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void move( MoveDirection direction, Number amount, Number duration, ReferenceFrame asSeenBy ) {
		move( direction, amount, duration, asSeenBy, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void move( MoveDirection direction, Number amount, Number duration ) {
		move( direction, amount, duration, AsSeenBy.SELF );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void move( MoveDirection direction, Number amount ) {
		move( direction, amount, DEFAULT_DURATION );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void moveToward( Number amount, Composite target, Number duration, Style style ) {
		edu.cmu.cs.dennisc.math.Point3 tThis = this.getSGComposite().getAbsoluteTransformation().translation;
		edu.cmu.cs.dennisc.math.Point3 tTarget = target.getSGComposite().getAbsoluteTransformation().translation;
		edu.cmu.cs.dennisc.math.Vector3 v = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( tTarget, tThis );
		double length = v.calculateMagnitude();
		if( length > 0 ) {
			v.multiply( amount.doubleValue() / length );
		} else {
			v.set( 0, 0, amount.doubleValue() );
		}
		applyTranslation( v.x, v.y, v.z, duration, AsSeenBy.SCENE, style );

	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveToward( Number amount, Composite target, Number duration ) {
		moveToward( amount, target, duration, DEFAULT_STYLE );

	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveToward( Number amount, Composite target ) {
		moveToward( amount, target, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void moveAwayFrom( Number amount, Composite target, Number duration, Style style ) {
		moveToward( -amount.doubleValue(), target, duration, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveAwayFrom( Number amount, Composite target, Number duration ) {
		moveAwayFrom( amount, target, duration, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveAwayFrom( Number amount, Composite target ) {
		moveAwayFrom( amount, target, DEFAULT_DURATION );
	}

	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void moveAtSpeed( MoveDirection direction, Number speed, Number duration, ReferenceFrame asSeenBy ) {
		double amount = speed.doubleValue() * duration.doubleValue();
		edu.cmu.cs.dennisc.math.Vector3 axis = direction.getAxis();
		applyTranslation( axis.x * amount, axis.y * amount, axis.z * amount, duration, asSeenBy, DEFAULT_SPEED_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveAtSpeed( MoveDirection direction, Number speed, Number duration ) {
		moveAtSpeed( direction, speed, duration, AsSeenBy.SELF );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveAtSpeed( MoveDirection direction, Number speed ) {
		moveAtSpeed( direction, speed, DEFAULT_DURATION );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void turn( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount, 
			Number duration, 
			ReferenceFrame asSeenBy, 
			Style style 
		) {
		applyRotationInRadians( direction.getAxis(), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( amount.doubleValue() ), duration, asSeenBy, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void turn( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount, 
			Number duration, 
			ReferenceFrame asSeenBy 
		) {
		turn( direction, amount, duration, asSeenBy, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void turn( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount, 
			Number duration 
		) {
		turn( direction, amount, duration, AsSeenBy.SELF );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void turn( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount 
		) {
		turn( direction, amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void turnAtSpeed( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number speed, 
			Number duration, 
			ReferenceFrame asSeenBy
		) {
		applyRotationInRadians( direction.getAxis(), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( speed.doubleValue() ) * duration.doubleValue(), duration, asSeenBy, DEFAULT_SPEED_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void turnAtSpeed( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number speed, 
			Number duration 
		) {
		turnAtSpeed( direction, speed, duration, AsSeenBy.SELF );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void turnAtSpeed( 
			TurnDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number speed
		) {
		turnAtSpeed( direction, speed, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void roll( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount, 
			Number duration, 
			ReferenceFrame asSeenBy, 
			Style style 
		) {
		applyRotationInRadians( direction.getAxis(), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( amount.doubleValue() ), duration, asSeenBy, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void roll( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount, 
			Number duration, 
			ReferenceFrame asSeenBy 
		) {
		roll( direction, amount, duration, asSeenBy, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void roll( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount, 
			Number duration 
		) {
		roll( direction, amount, duration, AsSeenBy.SELF );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void roll( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number amount 
		) {
		roll( direction, amount, DEFAULT_DURATION );
	}
	@MethodTemplate( visibility=Visibility.TUCKED_AWAY )
	public void rollAtSpeed( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number speed, 
			Number duration, 
			ReferenceFrame asSeenBy
		) {
		applyRotationInRadians( direction.getAxis(), edu.cmu.cs.dennisc.math.AngleUtilities.revolutionsToRadians( speed.doubleValue() ) * duration.doubleValue(), duration, asSeenBy, DEFAULT_SPEED_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void rollAtSpeed( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number speed, 
			Number duration 
		) {
		rollAtSpeed( direction, speed, duration, AsSeenBy.SELF );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void rollAtSpeed( 
			RollDirection direction, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number speed
		) {
		rollAtSpeed( direction, speed, DEFAULT_DURATION );
	}

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public void moveTo( ReferenceFrame target, Number duration, Style style ) {
		setTranslationOnly( target, duration, style );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveTo( ReferenceFrame target, Number duration ) {
		moveTo( target, duration, DEFAULT_STYLE );
	}
	@MethodTemplate( visibility=Visibility.CHAINED )
	public void moveTo( ReferenceFrame target ) {
		moveTo( target, DEFAULT_DURATION );
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void orientTo( ReferenceFrame target, Number duration, Style style ) {
		setAxesOnly( target, duration, style );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void orientTo( ReferenceFrame target, Number duration ) {
		orientTo( target, duration, DEFAULT_STYLE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void orientTo( ReferenceFrame target ) {
		orientTo( target, DEFAULT_DURATION );
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void moveAndOrientTo( ReferenceFrame target, Number duration, Style style ) {
		setPointOfView( AffineMatrix4x4.accessIdentity(), duration, target, style );
	}
	@MethodTemplate(visibility = Visibility.CHAINED )
	public void moveAndOrientTo( ReferenceFrame target, Number duration ) {
		moveAndOrientTo( target, duration, DEFAULT_STYLE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED )
	public void moveAndOrientTo( ReferenceFrame target ) {
		moveAndOrientTo( target, DEFAULT_DURATION );
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void pointAt( ReferenceFrame target, Number duration, Style style ) {
		setAxesOnly( calculatePointAtAxes( target ), duration, style );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void pointAt( ReferenceFrame target, Number duration ) {
		pointAt( target, duration, DEFAULT_STYLE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void pointAt( ReferenceFrame target ) {
		pointAt( target, DEFAULT_DURATION );
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void turnToFace( ReferenceFrame target, Number duration, Style style ) {
		setAxesOnly( calculateTurnToFaceAxes( target ), duration, style );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void turnToFace( ReferenceFrame target, Number duration ) {
		turnToFace( target, duration, DEFAULT_STYLE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void turnToFace( ReferenceFrame target ) {
		turnToFace( target, DEFAULT_DURATION );
	}

	@MethodTemplate(visibility = Visibility.PRIME_TIME)
	public void orientToUpright( Number duration, ReferenceFrame asSeenBy, Style style ) {
		setAxesOnlyToUpright( duration, asSeenBy, style );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void orientToUpright( Number duration, ReferenceFrame asSeenBy ) {
		orientToUpright( duration, asSeenBy, DEFAULT_STYLE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void orientToUpright( Number duration ) {
		orientToUpright( duration, AsSeenBy.SCENE );
	}
	@MethodTemplate(visibility = Visibility.CHAINED)
	public void orientToUpright() {
		orientToUpright( DEFAULT_DURATION );
	}	

	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public edu.cmu.cs.dennisc.math.Point3 getPosition( ReferenceFrame asSeenBy ) {
		return new edu.cmu.cs.dennisc.math.Point3( getTransformation( asSeenBy ).translation );
	}
	@MethodTemplate( visibility=Visibility.PRIME_TIME )
	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getOrientation( ReferenceFrame asSeenBy ) {
		return getTransformation( asSeenBy ).orientation.createOrthogonalMatrix3x3();
	}
	
	private edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes m_sgAxes = null;
	@PropertyGetterTemplate( visibility=Visibility.TUCKED_AWAY )
	public Boolean isPivotShowing() {
		if( m_sgAxes != null ) {
			return m_sgAxes.getParent() != null;
		} else {
			return false;
		}
		
	}
	public void setPivotShowing( Boolean isPivotShowing ) {
		if( isPivotShowing ) {
			if( m_sgAxes != null ) {
				//pass
			} else {
				m_sgAxes = new edu.cmu.cs.dennisc.scenegraph.util.ExtravagantAxes( 1.0 );
			}
			m_sgAxes.setParent( getSGComposite() );
		} else {
			if( m_sgAxes != null ) {
				m_sgAxes.setParent( null );
			} else {
				//pass
			}
		}
	}
	
}
