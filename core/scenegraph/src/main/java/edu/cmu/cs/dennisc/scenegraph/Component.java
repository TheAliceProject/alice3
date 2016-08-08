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

/**
 * @author Dennis Cosgrove
 */
public abstract class Component extends Element implements edu.cmu.cs.dennisc.pattern.Visitable, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame {
	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		visitor.visit( this );
	}

	public Composite getRoot() {
		if( this.vehicle != null ) {
			return this.vehicle.getRoot();
		} else {
			return null;
		}
	}

	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		if( this.vehicle != null ) {
			rv = this.vehicle.getAbsoluteTransformation( rv );
		} else {
			rv.setIdentity();
		}
		return rv;
	}

	@Override
	public final edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation() {
		return getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN() );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		if( this.vehicle != null ) {
			rv = this.vehicle.getInverseAbsoluteTransformation( rv );
		} else {
			rv.setIdentity();
		}
		return rv;
	}

	@Override
	public final edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation() {
		return getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN() );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		if( this.vehicle != null ) {
			return this.vehicle.getTransformation( rv, asSeenBy );
		} else {
			return asSeenBy.getInverseAbsoluteTransformation( rv );
		}
	}

	@Override
	public final edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		return getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), asSeenBy );
	}

	public edu.cmu.cs.dennisc.math.Point3 getTranslation( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		rv.set( getTransformation( asSeenBy ).translation );
		return rv;
	}

	public final edu.cmu.cs.dennisc.math.Point3 getTranslation( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		return getTranslation( new edu.cmu.cs.dennisc.math.Point3(), asSeenBy );
	}

	public edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getAxes( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		rv.setValue( getTransformation( asSeenBy ).orientation );
		return rv;
	}

	public final edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 getAxes( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		return getAxes( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3.createNaN(), asSeenBy );
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getUnitQuaternionD( edu.cmu.cs.dennisc.math.UnitQuaternion rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		rv.setValue( getTransformation( asSeenBy ).orientation );
		return rv;
	}

	public final edu.cmu.cs.dennisc.math.UnitQuaternion getUnitQuaternionD( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		return getUnitQuaternionD( edu.cmu.cs.dennisc.math.UnitQuaternion.createNaN(), asSeenBy );
	}

	public edu.cmu.cs.dennisc.math.EulerAngles getEulerAnglesD( edu.cmu.cs.dennisc.math.EulerAngles rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		rv.setValue( getTransformation( asSeenBy ).orientation );
		return rv;
	}

	public final edu.cmu.cs.dennisc.math.EulerAngles getEulerAnglesD( edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		return getEulerAnglesD( edu.cmu.cs.dennisc.math.EulerAngles.createNaN(), asSeenBy );
	}

	public Composite getParent() {
		return this.vehicle;
	}

	public void setParent( Composite parent ) {
		if( this.vehicle != parent ) {
			if( this.vehicle != null ) {
				this.vehicle.fireChildRemoved( this );
			}
			this.vehicle = parent;
			if( this.vehicle != null ) {
				this.vehicle.fireChildAdded( this );
			}
			//firePropertyChange( VEHICLE_PROPERTY_NAME );
			fireAbsoluteTransformationChange();
			fireHierarchyChanged();
		}
	}

	@Override
	public boolean isLocalOf( Component other ) {
		return this == other;
	}

	@Override
	public boolean isVehicleOf( Component other ) {
		return this == other.getParent();
	}

	@Override
	public boolean isSceneOf( Component other ) {
		return this == other.getRoot();
	}

	public boolean isDescendantOf( Composite possibleAncestor ) {
		if( possibleAncestor == null ) {
			return false;
		}
		if( this.vehicle == possibleAncestor ) {
			return true;
		} else {
			if( this.vehicle == null ) {
				return false;
			} else {
				return this.vehicle.isDescendantOf( possibleAncestor );
			}
		}
	}

	public void addAbsoluteTransformationListener( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener absoluteTransformationListener ) {
		this.absoluteTransformationListeners.add( absoluteTransformationListener );
	}

	public void removeAbsoluteTransformationListener( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener absoluteTransformationListener ) {
		this.absoluteTransformationListeners.remove( absoluteTransformationListener );
	}

	private void fireAbsoluteTransformationChange( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent ) {
		for( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener atl : this.absoluteTransformationListeners ) {
			atl.absoluteTransformationChanged( absoluteTransformationEvent );
		}
	}

	protected void fireAbsoluteTransformationChange() {
		fireAbsoluteTransformationChange( new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent( this ) );
	}

	public void addHierarchyListener( edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener hierarchyListener ) {
		this.hierarchyListeners.add( hierarchyListener );
	}

	public void removeHierarchyListener( edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener hierarchyListener ) {
		this.hierarchyListeners.remove( hierarchyListener );
	}

	private void fireHierarchyChanged( edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent hierarchyEvent ) {
		for( edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener hl : this.hierarchyListeners ) {
			hl.hierarchyChanged( hierarchyEvent );
		}
	}

	protected void fireHierarchyChanged() {
		fireHierarchyChanged( new edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent( this ) );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformTo( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo( rv, xyzw, this, to );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformFrom( edu.cmu.cs.dennisc.math.Vector4 rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, Component from ) {
		return from.transformTo( rv, xyzw, this );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformTo( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Point3 xyz, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo( rv, xyz, this, to );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformFrom( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Point3 xyz, Component from ) {
		return from.transformTo( rv, xyz, this );
	}

	public edu.cmu.cs.dennisc.math.Vector3 transformTo( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.math.Vector3 xyz, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo( rv, xyz, this, to );
	}

	public edu.cmu.cs.dennisc.math.Vector3 transformFrom( edu.cmu.cs.dennisc.math.Vector3 rv, edu.cmu.cs.dennisc.math.Vector3 xyz, Component from ) {
		return from.transformTo( rv, xyz, this );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformTo_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo_New( xyzw, this, to );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformFrom_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, Component from ) {
		return from.transformTo_New( xyzw, this );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformTo_New( edu.cmu.cs.dennisc.math.Point3 xyz, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo_New( xyz, this, to );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformFrom_New( edu.cmu.cs.dennisc.math.Point3 xyz, Component from ) {
		return from.transformTo_New( xyz, this );
	}

	public edu.cmu.cs.dennisc.math.Vector3 transformTo_New( edu.cmu.cs.dennisc.math.Vector3 xyz, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo_New( xyz, this, to );
	}

	public edu.cmu.cs.dennisc.math.Vector3 transformFrom_New( edu.cmu.cs.dennisc.math.Vector3 xyz, Component from ) {
		return from.transformTo_New( xyz, this );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformTo_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 xyzw, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo_AffectReturnValuePassedIn( xyzw, this, to );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformFrom_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector4 xyzw, Component from ) {
		return from.transformTo_AffectReturnValuePassedIn( xyzw, this );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformTo_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Point3 xyz, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo_AffectReturnValuePassedIn( xyz, this, to );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformFrom_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Point3 xyz, Component from ) {
		return from.transformTo_AffectReturnValuePassedIn( xyz, this );
	}

	public edu.cmu.cs.dennisc.math.Vector3 transformTo_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector3 xyz, Component to ) {
		return edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities.transformTo_AffectReturnValuePassedIn( xyz, this, to );
	}

	public edu.cmu.cs.dennisc.math.Vector3 transformFrom_AffectReturnValuePassedIn( edu.cmu.cs.dennisc.math.Vector3 xyz, Component from ) {
		return from.transformTo_AffectReturnValuePassedIn( xyz, this );
	}

	private static final edu.cmu.cs.dennisc.math.Vector4 s_buffer = new edu.cmu.cs.dennisc.math.Vector4();

	public java.awt.Point transformToAWT( java.awt.Point rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, AbstractCamera camera ) {
		synchronized( s_buffer ) {
			if( this != camera ) {
				transformTo( s_buffer, xyzw, camera );
			} else {
				s_buffer.set( xyzw );
			}
			edu.cmu.cs.dennisc.render.PicturePlaneUtils.transformFromCameraToAWT( rv, s_buffer, renderTarget, camera );
		}
		return rv;
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformFromAWT( edu.cmu.cs.dennisc.math.Vector4 rv, java.awt.Point p, double z, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, AbstractCamera camera ) {
		edu.cmu.cs.dennisc.render.PicturePlaneUtils.transformFromAWTToCamera( rv, p, z, renderTarget, camera );
		if( this != camera ) {
			transformFrom_AffectReturnValuePassedIn( rv, camera );
		}
		return rv;
	}

	public java.awt.Point transformToAWT_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, AbstractCamera camera ) {
		return transformToAWT( new java.awt.Point(), xyzw, renderTarget, camera );
	}

	public java.awt.Point transformToAWT_New( edu.cmu.cs.dennisc.math.Point3 xyz, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, AbstractCamera camera ) {
		return transformToAWT_New( new edu.cmu.cs.dennisc.math.Vector4( xyz.x, xyz.y, xyz.z, 1.0 ), renderTarget, camera );
	}

	public edu.cmu.cs.dennisc.math.Vector4 transformFromAWT_NewVectorD4( java.awt.Point p, double z, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, AbstractCamera camera ) {
		return transformFromAWT( new edu.cmu.cs.dennisc.math.Vector4(), p, z, renderTarget, camera );
	}

	public edu.cmu.cs.dennisc.math.Point3 transformFromAWT_NewPointD3( java.awt.Point p, double z, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, AbstractCamera camera ) {
		return edu.cmu.cs.dennisc.math.Point3.createFromXYZW( transformFromAWT_NewVectorD4( p, z, renderTarget, camera ) );
	}

	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener> absoluteTransformationListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener> hierarchyListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private Composite vehicle = null;
}
