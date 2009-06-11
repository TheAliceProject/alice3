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
/*
 * Copyright (c) 1999-2003, Carnegie Mellon University. All rights reserved.
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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.property.PropertyOwner;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformable extends Composite {
	public final edu.cmu.cs.dennisc.math.property.AffineMatrix4x4Property localTransformation = new edu.cmu.cs.dennisc.math.property.AffineMatrix4x4Property( this, edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity() ) {
		@Override
		public void setValue( PropertyOwner owner, edu.cmu.cs.dennisc.math.AffineMatrix4x4 value ) {
			super.setValue( owner, value );
			AbstractTransformable.this.fireAbsoluteTransformationChange();
		}
	};

	protected abstract Composite getVehicle();
	// todo: cache this information
	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		Composite vehicle = getVehicle();
		if( vehicle == null || vehicle.isSceneOf( this ) ) {
			rv = getLocalTransformation( rv );
		} else {
			rv = vehicle.getAbsoluteTransformation( rv );
			rv.setToMultiplication( rv, localTransformation.getValue() );
		}
		return rv;
	}

	// todo: cache this information
	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		rv = getAbsoluteTransformation( rv );
		rv.invert();
		return rv;
	}

	@Deprecated
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		rv.set( localTransformation.getValue() );
		return rv;
	}
	@Deprecated
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformation() {
		return getLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN() );
	}

	protected void setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, TransformationAffect affect ) {
		if( transformation == null ) {
			throw new NullPointerException();
		}
		if( transformation.isNaN() ) {
			throw new RuntimeException( "isNaN" );
		}
		
		assert affect != null;
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = localTransformation.getValue();
		affect.set( m, transformation );
		localTransformation.touch();
		fireAbsoluteTransformationChange();
	}
	public final void setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation ) {
		setLocalTransformation( transformation, TransformationAffect.AFFECT_ALL );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		if( asSeenBy.isVehicleOf( this ) ) {
			rv = getLocalTransformation( rv );
		} else if( asSeenBy.isSceneOf( this ) ) {
			rv = getAbsoluteTransformation( rv );
		} else if( asSeenBy.isLocalOf( this ) ) {
			rv.setIdentity();
		} else {
			asSeenBy.getInverseAbsoluteTransformation( rv );
			rv.multiply( getAbsoluteTransformation() );
		}
		return rv;
	}
//		boolean isReturnValueAlreadySet = false;
//		if( asSeenBy instanceof edu.cmu.cs.dennisc.referenceframe.ReferenceFrame ) {
//			edu.cmu.cs.dennisc.referenceframe.ReferenceFrame referenceFrame = (edu.cmu.cs.dennisc.referenceframe.ReferenceFrame)asSeenBy;
//			if( referenceFrame.isParentOf( this ) ) {
//				rv = getLocalTransformation( rv );
//				isReturnValueAlreadySet = true;
//			} else if( referenceFrame.isSceneOf( this ) ) {
//				rv = getAbsoluteTransformation( rv );
//				isReturnValueAlreadySet = true;
//			} else if( referenceFrame.isLocalOf( this ) ) {
//				rv.setIdentity();
//				isReturnValueAlreadySet = true;
//			// todo: handle this case? might not be worth it if caching abolute and inverseAbsolute
//			// } else if( asSeenBy.isAncestorOf( this ) ) {
//			//
//			}
//		}
//		if( isReturnValueAlreadySet ) {
//			//pass
//		} else {
////			todo: optimize
////			rv.setIdentity();
////			LinearAlgebra.applyTransformation( rv, asSeenBy.getInverseAbsoluteTransformation() );
////			LinearAlgebra.applyTransformation( rv, getAbsoluteTransformation() );
//			rv.set( asSeenBy.getInverseAbsoluteTransformation() );
//			LinearAlgebra.applyTransformation( rv, getAbsoluteTransformation() );
//		}
//		return rv;
//	}

	public void setTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy, TransformationAffect affect ) {
		if( asSeenBy.isVehicleOf( this ) ) {
			setLocalTransformation( transformation, affect );
		} else if( asSeenBy.isLocalOf( this ) ) {
			applyTransformation( transformation, asSeenBy, affect );
			//		} else if( asSeenBy.isSceneOf( this ) ) {
			//			applyTransformation( transformation, asSeenBy, affectMask );
		} else {
			Composite vehicle = getVehicle();
			assert vehicle != null;

			//todo: optimize
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = new edu.cmu.cs.dennisc.math.AffineMatrix4x4( vehicle.getInverseAbsoluteTransformation() );
			if( asSeenBy.isSceneOf( this ) ) {
				//pass
			} else {
				m.multiply( asSeenBy.getAbsoluteTransformation() );
			}
			m.multiply( transformation );

			setLocalTransformation( m, affect );
			//return LinearAlgebra.multiply( transformation, LinearAlgebra.multiply( asSeenBy.getAbsoluteTransformation(), vehicleInverse ) );
			//			throw new RuntimeException( "todo.  this: " + this + "; vehicle: " + getVehicle() + "; asSeenBy: " + asSeenBy );
		}
	}

	public void setTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		setTransformation( transformation, asSeenBy, TransformationAffect.AFFECT_ALL );
	}

	public void setTranslationOnly( double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		if( Double.isNaN( x ) ) {
			x = 0;
		}
		if( Double.isNaN( y ) ) {
			y = 0;
		}
		if( Double.isNaN( z ) ) {
			z = 0;
		}
		setTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( x, y, z ), asSeenBy, TransformationAffect.getTranslationAffect( x, y, z ) );
	}
	public void setTranslationOnly( edu.cmu.cs.dennisc.math.Tuple3 t, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		setTranslationOnly( t.x, t.y, t.z, asSeenBy );
	}

	public void setAxesOnly( edu.cmu.cs.dennisc.math.Orientation orientation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		setTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createOrientation( orientation ), asSeenBy, TransformationAffect.AFFECT_ORIENTAION_ONLY );
	}
//	public void setAxesOnly( edu.cmu.cs.dennisc.math.EulerAngles ea, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
//		setAxesOnly( new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( ea ), asSeenBy );
//	}
//	public void setAxesOnly( edu.cmu.cs.dennisc.math.UnitQuaternion q, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
//		setAxesOnly( new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( q ), asSeenBy );
//	}
//	public void setAxesOnly( edu.cmu.cs.dennisc.math.ForwardAndUpGuide faug, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
//		setAxesOnly( new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( faug ), asSeenBy );
//	}
//	public void setAxesOnly( edu.cmu.cs.dennisc.math.AxisRotation ar, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
//		setAxesOnly( new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3( ar ), asSeenBy );
//	}
////	public void setAxesOnlyFromForwardAndUpGuide( edu.cmu.cs.dennisc.math.Vector3 forward, edu.cmu.cs.dennisc.math.Vector3 upGuide, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
////		setAxesOnly( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromForwardAndUpGuide( forward, upGuide ), asSeenBy );
////	}

	public void setAxesOnlyToPointAt( Component target, edu.cmu.cs.dennisc.math.Point3 targetOffset, edu.cmu.cs.dennisc.math.Vector3 upGuide ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 mSelf = getAbsoluteTransformation();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 mTarget = target.getAbsoluteTransformation();
		if( targetOffset != null ) {
			mTarget.applyTranslation( targetOffset );
		}

		edu.cmu.cs.dennisc.math.Vector3 forward = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( mTarget.translation, mSelf.translation );

		if( forward.calculateMagnitudeSquared() == 0 ) {
			//pass 
		} else {
			setAxesOnly( new edu.cmu.cs.dennisc.math.ForwardAndUpGuide( forward, null ).createOrthogonalMatrix3x3(), edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
		}
	}
	public void setAxesOnlyToPointAt( Component target, edu.cmu.cs.dennisc.math.Point3 offset ) {
		setAxesOnlyToPointAt( target, offset, null );
	}
	public void setAxesOnlyToPointAt( Component target ) {
		setAxesOnlyToPointAt( target, null );
	}

	public void setAxesOnlyToStandUp( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes = getAxes( asSeenBy );
		setAxesOnly( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createFromStandUp( axes ), asSeenBy );
	}
	public void setAxesOnlyToStandUp() {
		setAxesOnlyToStandUp( edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
	}

	public void applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy, TransformationAffect affect ) {
		//todo: handle affect
		if( asSeenBy.isLocalOf( this ) ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getLocalTransformation();
			m.setToMultiplication( m, transformation );
			setLocalTransformation( m );
		} else if( asSeenBy.isVehicleOf( this ) ) {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getLocalTransformation();
			m.setToMultiplication( transformation, m );
			setLocalTransformation( m );
			//todo?
			//		} else if( asSeenBy.isSceneOf( this ) ) {
		} else {
			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = getTransformation( asSeenBy );
			m.setToMultiplication( transformation, m );
			setTransformation( m, asSeenBy );
		}
	}
	public void applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyTransformation( transformation, asSeenBy, TransformationAffect.AFFECT_ALL );
	}

	public void applyTranslation( double x, double y, double z, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createTranslation( x, y, z ), asSeenBy );
	}
	public void applyTranslation( edu.cmu.cs.dennisc.math.Tuple3 t, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyTranslation( t.x, t.y, t.z, asSeenBy );
	}
	public void applyTranslation( double x, double y, double z ) {
		applyTranslation( x, y, z, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	public void applyTranslation( edu.cmu.cs.dennisc.math.Tuple3 t ) {
		applyTranslation( t.x, t.y, t.z );
	}

	@Deprecated
	public void applyRotationAboutXAxisInRadians( double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutXAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ), asSeenBy );
	}
	@Deprecated
	public void applyRotationAboutXAxisInRadians( double angleInRadians ) {
		applyRotationAboutXAxisInRadians( angleInRadians, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	@Deprecated
	public void applyRotationAboutYAxisInRadians( double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutYAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ), asSeenBy );
	}
	@Deprecated
	public void applyRotationAboutYAxisInRadians( double angleInRadians ) {
		applyRotationAboutYAxisInRadians( angleInRadians, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	@Deprecated
	public void applyRotationAboutZAxisInRadians( double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createRotationAboutZAxis( new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ), asSeenBy );
	}
	@Deprecated
	public void applyRotationAboutZAxisInRadians( double angleInRadians ) {
		applyRotationAboutZAxisInRadians( angleInRadians, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	@Deprecated
	public void applyRotationAboutArbitraryAxisInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		if( axis.isPositiveXAxis() ) {
			applyRotationAboutXAxisInRadians( angleInRadians, asSeenBy );
		} else if( axis.isNegativeXAxis() ) {
			applyRotationAboutXAxisInRadians( -angleInRadians, asSeenBy );
		} else if( axis.isPositiveYAxis() ) {
			applyRotationAboutYAxisInRadians( angleInRadians, asSeenBy );
		} else if( axis.isNegativeYAxis() ) {
			applyRotationAboutYAxisInRadians( -angleInRadians, asSeenBy );
		} else if( axis.isPositiveZAxis() ) {
			applyRotationAboutZAxisInRadians( angleInRadians, asSeenBy );
		} else if( axis.isNegativeZAxis() ) {
			applyRotationAboutZAxisInRadians( -angleInRadians, asSeenBy );
		} else {
			applyTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createOrientation( new edu.cmu.cs.dennisc.math.AxisRotation( axis, new edu.cmu.cs.dennisc.math.AngleInRadians( angleInRadians ) ) ), asSeenBy );
		}
	}
	@Deprecated
	public void applyRotationAboutArbitraryAxisInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians ) {
		applyRotationAboutArbitraryAxisInRadians( axis, angleInRadians, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	
	public void applyRotationAboutXAxis( edu.cmu.cs.dennisc.math.Angle angle, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyRotationAboutXAxisInRadians( angle.getAsRadians(), asSeenBy );
	}
	public void applyRotationAboutXAxis( edu.cmu.cs.dennisc.math.Angle angle ) {
		applyRotationAboutXAxis( angle, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	public void applyRotationAboutYAxis( edu.cmu.cs.dennisc.math.Angle angle, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyRotationAboutYAxisInRadians( angle.getAsRadians(), asSeenBy );
	}
	public void applyRotationAboutYAxis( edu.cmu.cs.dennisc.math.Angle angle ) {
		applyRotationAboutYAxis( angle, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	public void applyRotationAboutZAxis( edu.cmu.cs.dennisc.math.Angle angle, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyRotationAboutZAxisInRadians( angle.getAsRadians(), asSeenBy );
	}
	public void applyRotationAboutZAxis( edu.cmu.cs.dennisc.math.Angle angle ) {
		applyRotationAboutZAxis( angle, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
	public void applyRotationAboutArbitraryAxis( edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.math.Angle angle, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		applyRotationAboutArbitraryAxisInRadians( axis, angle.getAsRadians(), asSeenBy );
	}
	public void applyRotationAboutArbitraryAxis( edu.cmu.cs.dennisc.math.Vector3 axis, edu.cmu.cs.dennisc.math.Angle angle ) {
		applyRotationAboutArbitraryAxis( axis, angle, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SELF );
	}
}
