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

package edu.cmu.cs.dennisc.scenegraph;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AxisRotation;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.Orientation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractTransformable extends Composite {
	protected abstract Composite getVehicle();

	protected abstract AffineMatrix4x4 accessLocalTransformation();

	protected abstract void touchLocalTransformation( AffineMatrix4x4 m );

	public AffineMatrix4x4 getLocalTransformation( AffineMatrix4x4 rv ) {
		rv.set( this.accessLocalTransformation() );
		return rv;
	}

	public final AffineMatrix4x4 getLocalTransformation() {
		return getLocalTransformation( AffineMatrix4x4.createNaN() );
	}

	protected void setLocalTransformation( AffineMatrix4x4 transformation, TransformationAffect affect ) {
		if( transformation == null ) {
			throw new NullPointerException();
		}
		if( transformation.isNaN() ) {
			throw new RuntimeException( "isNaN" );
		}

		assert affect != null;
		AffineMatrix4x4 m = this.accessLocalTransformation();
		affect.set( m, transformation );
		this.touchLocalTransformation( m );
		fireAbsoluteTransformationChange();
	}

	public final void setLocalTransformation( AffineMatrix4x4 transformation ) {
		setLocalTransformation( transformation, TransformationAffect.AFFECT_ALL );
	}

	// todo: cache this information
	@Override
	public AffineMatrix4x4 getAbsoluteTransformation( AffineMatrix4x4 rv ) {
		Composite vehicle = getVehicle();
		if( ( vehicle == null ) || vehicle.isSceneOf( this ) ) {
			rv = getLocalTransformation( rv );
		} else {
			rv = vehicle.getAbsoluteTransformation( rv );
			rv.setToMultiplication( rv, this.accessLocalTransformation() );
		}
		return rv;
	}

	// todo: cache this information
	@Override
	public AffineMatrix4x4 getInverseAbsoluteTransformation( AffineMatrix4x4 rv ) {
		rv = getAbsoluteTransformation( rv );
		rv.invert();
		return rv;
	}

	@Override
	public AffineMatrix4x4 getTransformation( AffineMatrix4x4 rv, ReferenceFrame asSeenBy ) {
		if( asSeenBy.isVehicleOf( this ) ) {
			rv = getLocalTransformation( rv );
		} else if( asSeenBy.isSceneOf( this ) ) {
			rv = getAbsoluteTransformation( rv );
		} else if( asSeenBy.isLocalOf( this ) ) {
			rv.setIdentity();
		} else {
			asSeenBy.getInverseAbsoluteTransformation( rv );
			rv.normalizeOrientation();
			rv.multiply( getAbsoluteTransformation() );
		}
		return rv;
	}

	public void setTransformation( AffineMatrix4x4 transformation, ReferenceFrame asSeenBy, TransformationAffect affect ) {
		if( asSeenBy.isVehicleOf( this ) ) {
			setLocalTransformation( transformation, affect );
		} else if( asSeenBy.isLocalOf( this ) ) {
			applyTransformation( transformation, asSeenBy, affect );
			//		} else if( asSeenBy.isSceneOf( this ) ) {
			//			applyTransformation( transformation, asSeenBy, affectMask );
		} else {
			Composite vehicle = getVehicle();
			assert vehicle != null : this;

			//todo: optimize
			AffineMatrix4x4 m = new AffineMatrix4x4( vehicle.getInverseAbsoluteTransformation() );
			if (!asSeenBy.isSceneOf(this)) {
				final AffineMatrix4x4 seenBy = asSeenBy.getAbsoluteTransformation();
				seenBy.orientation.normalizeColumns();
				m.multiply(seenBy);
			}
			m.multiply( transformation );

			setLocalTransformation( m, affect );
			//return LinearAlgebra.multiply( transformation, LinearAlgebra.multiply( asSeenBy.getAbsoluteTransformation(), vehicleInverse ) );
			//			throw new RuntimeException( "todo.  this: " + this + "; vehicle: " + getVehicle() + "; asSeenBy: " + asSeenBy );
		}
	}

	public void setTransformation( AffineMatrix4x4 transformation, ReferenceFrame asSeenBy ) {
		setTransformation( transformation, asSeenBy, TransformationAffect.AFFECT_ALL );
	}

	public void setTranslationOnly( double x, double y, double z, ReferenceFrame asSeenBy ) {
		if( Double.isNaN( x ) ) {
			x = 0;
		}
		if( Double.isNaN( y ) ) {
			y = 0;
		}
		if( Double.isNaN( z ) ) {
			z = 0;
		}
		setTransformation( AffineMatrix4x4.createTranslation( x, y, z ), asSeenBy, TransformationAffect.getTranslationAffect( x, y, z ) );
	}

	public void setTranslationOnly( Tuple3 t, ReferenceFrame asSeenBy ) {
		setTranslationOnly( t.x, t.y, t.z, asSeenBy );
	}

	public void setAxesOnly( Orientation orientation, ReferenceFrame asSeenBy ) {
		setTransformation( AffineMatrix4x4.createOrientation( orientation ), asSeenBy, TransformationAffect.AFFECT_ORIENTAION_ONLY );
	}

	public void setAxesOnlyToPointAt( Component target ) {
		AffineMatrix4x4 mSelf = getAbsoluteTransformation();
		AffineMatrix4x4 mTarget = target.getAbsoluteTransformation();

		Vector3 forward = Vector3.createSubtraction( mTarget.translation, mSelf.translation );

		if (!(forward.calculateMagnitudeSquared() == 0)) {
			setAxesOnly( new ForwardAndUpGuide( forward, null ).createOrthogonalMatrix3x3(), AsSeenBy.SCENE );
		}
	}

	public void setAxesOnlyToStandUp( ReferenceFrame asSeenBy ) {
		OrthogonalMatrix3x3 axes = getAxes( asSeenBy );
		setAxesOnly( OrthogonalMatrix3x3.createFromStandUp( axes ), asSeenBy );
	}

	public void setAxesOnlyToStandUp() {
		setAxesOnlyToStandUp( AsSeenBy.SCENE );
	}

	private void applyTransformation( AffineMatrix4x4 transformation, ReferenceFrame asSeenBy,
																		TransformationAffect affect ) {
		//todo: handle affect
		if( asSeenBy.isLocalOf( this ) ) {
			AffineMatrix4x4 m = getLocalTransformation();
			m.setToMultiplication( m, transformation );
			setLocalTransformation( m );
		} else if( asSeenBy.isVehicleOf( this ) ) {
			AffineMatrix4x4 m = getLocalTransformation();
			m.setToMultiplication( transformation, m );
			setLocalTransformation( m );
			//todo?
			//		} else if( asSeenBy.isSceneOf( this ) ) {
		} else {
			AffineMatrix4x4 m = getTransformation( asSeenBy );
			m.setToMultiplication( transformation, m );
			setTransformation( m, asSeenBy );
		}
	}

	public void applyTransformation( AffineMatrix4x4 transformation, ReferenceFrame asSeenBy ) {
		applyTransformation( transformation, asSeenBy, TransformationAffect.AFFECT_ALL );
	}

	public void applyTranslation( double x, double y, double z, ReferenceFrame asSeenBy ) {
		applyTransformation( AffineMatrix4x4.createTranslation( x, y, z ), asSeenBy );
	}

	public void applyTranslation( Tuple3 t, ReferenceFrame asSeenBy ) {
		applyTranslation( t.x, t.y, t.z, asSeenBy );
	}

	public void applyTranslation( double x, double y, double z ) {
		applyTranslation( x, y, z, AsSeenBy.SELF );
	}

	public void applyTranslation( Tuple3 t ) {
		applyTranslation( t.x, t.y, t.z );
	}

	@Deprecated
	public void applyRotationAboutXAxisInRadians( double angleInRadians, ReferenceFrame asSeenBy ) {
		applyTransformation( AffineMatrix4x4.createRotationAboutXAxis( new AngleInRadians( angleInRadians ) ), asSeenBy );
	}

	@Deprecated
	public void applyRotationAboutXAxisInRadians( double angleInRadians ) {
		applyRotationAboutXAxisInRadians( angleInRadians, AsSeenBy.SELF );
	}

	@Deprecated
	public void applyRotationAboutYAxisInRadians( double angleInRadians, ReferenceFrame asSeenBy ) {
		applyTransformation( AffineMatrix4x4.createRotationAboutYAxis( new AngleInRadians( angleInRadians ) ), asSeenBy );
	}

	@Deprecated
	public void applyRotationAboutYAxisInRadians( double angleInRadians ) {
		applyRotationAboutYAxisInRadians( angleInRadians, AsSeenBy.SELF );
	}

	@Deprecated
	public void applyRotationAboutZAxisInRadians( double angleInRadians, ReferenceFrame asSeenBy ) {
		applyTransformation( AffineMatrix4x4.createRotationAboutZAxis( new AngleInRadians( angleInRadians ) ), asSeenBy );
	}

	@Deprecated
	public void applyRotationAboutZAxisInRadians( double angleInRadians ) {
		applyRotationAboutZAxisInRadians( angleInRadians, AsSeenBy.SELF );
	}

	@Deprecated
	public void applyRotationAboutArbitraryAxisInRadians( Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy ) {
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
			applyTransformation( AffineMatrix4x4.createOrientation( new AxisRotation( axis, new AngleInRadians( angleInRadians ) ) ), asSeenBy );
		}
	}

	@Deprecated
	public void applyRotationAboutArbitraryAxisInRadians( Vector3 axis, double angleInRadians ) {
		applyRotationAboutArbitraryAxisInRadians( axis, angleInRadians, AsSeenBy.SELF );
	}

	public void applyRotationAboutXAxis( Angle angle, ReferenceFrame asSeenBy ) {
		applyRotationAboutXAxisInRadians( angle.getAsRadians(), asSeenBy );
	}

	public void applyRotationAboutXAxis( Angle angle ) {
		applyRotationAboutXAxis( angle, AsSeenBy.SELF );
	}

	public void applyRotationAboutYAxis( Angle angle, ReferenceFrame asSeenBy ) {
		applyRotationAboutYAxisInRadians( angle.getAsRadians(), asSeenBy );
	}

	public void applyRotationAboutYAxis( Angle angle ) {
		applyRotationAboutYAxis( angle, AsSeenBy.SELF );
	}

	public void applyRotationAboutZAxis( Angle angle, ReferenceFrame asSeenBy ) {
		applyRotationAboutZAxisInRadians( angle.getAsRadians(), asSeenBy );
	}

	public void applyRotationAboutZAxis( Angle angle ) {
		applyRotationAboutZAxis( angle, AsSeenBy.SELF );
	}

	public void applyRotationAboutArbitraryAxis( Vector3 axis, Angle angle, ReferenceFrame asSeenBy ) {
		applyRotationAboutArbitraryAxisInRadians( axis, angle.getAsRadians(), asSeenBy );
	}

	public void applyRotationAboutArbitraryAxis( Vector3 axis, Angle angle ) {
		applyRotationAboutArbitraryAxis( axis, angle, AsSeenBy.SELF );
	}
}
