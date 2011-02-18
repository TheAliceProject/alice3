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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class Component extends Element implements edu.cmu.cs.dennisc.pattern.Visitable, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame {
	//public static final String VEHICLE_PROPERTY_NAME = "Vehicle"; 
	private Composite m_vehicle = null;
	private java.util.Vector< edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener > m_absoluteTransformationListeners = new java.util.Vector< edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener >();
	private java.util.Vector< edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener > m_hierarchyListeners = new java.util.Vector< edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener >();

	public boolean isLocalOf( Component other ) {
		return this == other;
	}
	public boolean isVehicleOf(Component other) {
		return this == other.getParent();
	}
	public boolean isSceneOf(Component other) {
		return this == other.getRoot();
	}
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		visitor.visit( this );
	}

	public Composite getRoot() {
		if( m_vehicle != null ) {
			return m_vehicle.getRoot();
		} else {
			return null;
		}
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		if( m_vehicle != null ) {
			rv = m_vehicle.getAbsoluteTransformation( rv );
		} else {
			rv.setIdentity();
		}
		return rv;
	}
	public final edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation() {
		return getAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN() );
	}

	private static edu.cmu.cs.dennisc.math.AffineMatrix4x4 s_bufferMatrixForReuse = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN();

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv ) {
		if( m_vehicle != null ) {
			rv = m_vehicle.getInverseAbsoluteTransformation( rv );
		} else {
			rv.setIdentity();
		}
		return rv;
	}
	public final edu.cmu.cs.dennisc.math.AffineMatrix4x4 getInverseAbsoluteTransformation() {
		return getInverseAbsoluteTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN() );
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame asSeenBy ) {
		if( m_vehicle != null ) {
			return m_vehicle.getTransformation( rv, asSeenBy );
		} else {
			return asSeenBy.getInverseAbsoluteTransformation( rv );
		}
	}
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
		return m_vehicle;
	}
	public void setParent( Composite parent ) {
		if( m_vehicle != parent ) {
			if( m_vehicle != null ) {
				m_vehicle.fireChildRemoved( this );
			}
			m_vehicle = parent;
			if( m_vehicle != null ) {
				m_vehicle.fireChildAdded( this );
			}
			//firePropertyChange( VEHICLE_PROPERTY_NAME );
			fireAbsoluteTransformationChange();
			fireHierarchyChanged();
		}
	}

	public boolean isDescendantOf( Composite possibleAncestor ) {
		if( possibleAncestor == null ) {
			return false;
		}
		if( m_vehicle == possibleAncestor ) {
			return true;
		} else {
			if( m_vehicle == null ) {
				return false;
			} else {
				return m_vehicle.isDescendantOf( possibleAncestor );
			}
		}
	}

	public void addAbsoluteTransformationListener( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener absoluteTransformationListener ) {
		m_absoluteTransformationListeners.addElement( absoluteTransformationListener );
	}
	public void removeAbsoluteTransformationListener( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener absoluteTransformationListener ) {
		m_absoluteTransformationListeners.removeElement( absoluteTransformationListener );
	}
	public Iterable< edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener > accessAbsoluteTransformationListeners() {
		return m_absoluteTransformationListeners;
	}
	private void fireAbsoluteTransformationChange( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent absoluteTransformationEvent ) {
		for( edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener atl : m_absoluteTransformationListeners ) {
			atl.absoluteTransformationChanged( absoluteTransformationEvent );
		}
	}
	protected void fireAbsoluteTransformationChange() {
		fireAbsoluteTransformationChange( new edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent( this ) );
	}

	public void addHierarchyListener( edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener hierarchyListener ) {
		m_hierarchyListeners.addElement( hierarchyListener );
	}
	public void removeHierarchyListener( edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener hierarchyListener ) {
		m_hierarchyListeners.removeElement( hierarchyListener );
	}
	public Iterable< edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener > accessHierarchyListeners() {
		return m_hierarchyListeners;
	}
	private void fireHierarchyChanged( edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent hierarchyEvent ) {
		for( edu.cmu.cs.dennisc.scenegraph.event.HierarchyListener hl : m_hierarchyListeners ) {
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

	public java.awt.Point transformToAWT( java.awt.Point rv, edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, AbstractCamera camera ) {
		synchronized( s_buffer ) {
			if( this != camera ) {
				transformTo( s_buffer, xyzw, camera );
			} else {
				s_buffer.set( xyzw );
			}
			edu.cmu.cs.dennisc.lookingglass.util.TransformationUtilities.transformFromCameraToAWT( rv, s_buffer, lookingGlass, camera );
		}
		return rv;
	}
	public edu.cmu.cs.dennisc.math.Vector4 transformFromAWT( edu.cmu.cs.dennisc.math.Vector4 rv, java.awt.Point p, double z, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, AbstractCamera camera ) {
		edu.cmu.cs.dennisc.lookingglass.util.TransformationUtilities.transformFromAWTToCamera( rv, p, z, lookingGlass, camera );
		if( this != camera ) {
			transformFrom_AffectReturnValuePassedIn( rv, camera );
		}
		return rv;
	}
	public java.awt.Point transformToAWT_New( edu.cmu.cs.dennisc.math.Vector4 xyzw, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, AbstractCamera camera ) {
		return transformToAWT( new java.awt.Point(), xyzw, lookingGlass, camera );
	}
	public java.awt.Point transformToAWT_New( edu.cmu.cs.dennisc.math.Point3 xyz, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, AbstractCamera camera ) {
		return transformToAWT_New( new edu.cmu.cs.dennisc.math.Vector4( xyz.x, xyz.y, xyz.z, 1.0 ), lookingGlass, camera );
	}
	public edu.cmu.cs.dennisc.math.Vector4 transformFromAWT_NewVectorD4( java.awt.Point p, double z, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, AbstractCamera camera ) {
		return transformFromAWT( new edu.cmu.cs.dennisc.math.Vector4(), p, z, lookingGlass, camera );
	}
	public edu.cmu.cs.dennisc.math.Point3 transformFromAWT_NewPointD3( java.awt.Point p, double z, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, AbstractCamera camera ) {
		return edu.cmu.cs.dennisc.math.Point3.createFromXYZW( transformFromAWT_NewVectorD4( p, z, lookingGlass, camera ) );
	}

//	public <E extends Component> E getFirst( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
//		return edu.cmu.cs.dennisc.pattern.util.ComponentUtilities.getFirstToAccept( candidateMask, this, cls, criterions );
//	}
//	public <E extends Component> E getFirst( Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
//		return getFirst( edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS, cls, criterions );
//	}
//	public <E extends Component> E getFirst( Class< E > cls ) {
//		return getFirst( cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
//	}
//	public Component getFirst( edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
//		return getFirst( null, criterions );
//	}
//
//	public <E extends Component> java.util.List< E > getAll( edu.cmu.cs.dennisc.pattern.HowMuch candidateMask, Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
//		java.util.List< E > list = new java.util.LinkedList< E >();
//		edu.cmu.cs.dennisc.pattern.util.ComponentUtilities.updateAllToAccept( candidateMask, list, this, cls, criterions );
//		return list;
//	}
//	public <E extends Component> java.util.List< E > getAll( Class< E > cls, edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
//		return getAll( edu.cmu.cs.dennisc.pattern.HowMuch.COMPONENT_AND_DESCENDANTS, cls, criterions );
//	}
//	public <E extends Component> java.util.List< E > getAll( Class< E > cls ) {
//		return getAll( cls, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
//	}
//	public java.util.List< Component > getAll( edu.cmu.cs.dennisc.pattern.Criterion< ? >... criterions ) {
//		return getAll( null, criterions );
//	}
//	public java.util.List< Component > getAll() {
//		return getAll( null, (edu.cmu.cs.dennisc.pattern.Criterion< ? >[])null );
//	}
}
