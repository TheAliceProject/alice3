/**
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
package org.alice.interact;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;


/**
 * @author David Culyba
 */
public class RotationRingHandle extends ManipulationHandle{
	
	protected static final double MINOR_RADIUS = .05d;
	
	protected static final Color4f ACTIVE_COLOR = new Color4f(.4f, 1.0f, 0.3f, 1.0f);
	protected static final Color4f ROLLOVER_COLOR = new Color4f(1.0f, 1.0f, 0.3f, 1.0f);
	protected static final Color4f MUTED_COLOR = new Color4f(0.5f, 0.5f, 0.4f, 1.0f);
	protected static final Color4f BASE_COLOR = new Color4f(0.7f, 0.7f, 0.3f, 1.0f);
	
	protected Torus sgTorus = new Torus();
	protected Sphere sgSphere = new Sphere();
	protected Transformable sphereTransformable = new Transformable();
	protected Visual sgSphereVisual = new Visual();
	protected Vector3 rotationAxis;
	protected Vector3 sphereDirection = new Vector3();
	
	protected DoubleTargetBasedAnimation radiusAnimation;
	
	public RotationRingHandle( )
	{
		this( Vector3.accessPositiveYAxis());
	}
	
	public RotationRingHandle( Vector3 rotationAxis )
	{
		super();
		this.sgSphereVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		this.sgSphereVisual.setParent( this.sphereTransformable );
		this.sphereTransformable.setParent( this );
		this.sgSphereVisual.geometries.setValue( new Geometry[] { this.sgSphere } );
		this.sgTorus.minorRadius.setValue( MINOR_RADIUS );
		this.sgTorus.majorRadius.setValue( 0.0d );
		this.sgSphere.radius.setValue( MINOR_RADIUS * 2.0d);
		this.setSphereVisibility( false );
		this.rotationAxis = rotationAxis;
		this.rotationAxis.normalize();
		this.sphereDirection.set( 0.0d, 0.0d, -1.0d );
		this.localTransformation.setValue( this.getTransformationForAxis( this.rotationAxis ) );
		this.sgVisual.geometries.setValue( new Geometry[] { this.sgTorus } );
	}
	
	public void setSphereVisibility( boolean showSphere )
	{
		this.sgSphereVisual.isShowing.setValue( showSphere );
	}
	
	public void setSphereDirection( Vector3 direction )
	{
		this.sphereDirection = direction;
		placeSphere();
	}
	
	public Point3 getSphereLocation(ReferenceFrame referenceFrame)
	{
		return this.sphereTransformable.getTranslation( referenceFrame );
	}
	
	protected void placeSphere()
	{
		this.sphereTransformable.setTranslationOnly( Point3.createMultiplication( this.sphereDirection, this.sgTorus.majorRadius.getValue() ), this );
	}
	
	@Override
	protected void createAnimations()
	{
		super.createAnimations();
		this.radiusAnimation = new DoubleTargetBasedAnimation( new Double(this.sgTorus.majorRadius.getValue()) ){
			@Override
			protected void updateValue( Double value ) {
				if (RotationRingHandle.this.sgTorus != null)
				{
					RotationRingHandle.this.sgTorus.majorRadius.setValue( value );
				}
			}
		};
		this.animator.invokeLater( this.radiusAnimation, null );
	}
	
	@Override
	protected void updateVisibleState()
	{
		super.updateVisibleState();
		if (this.manipulatedObject != null && this.radiusAnimation != null)
		{
			double endRadius = this.isVisible() ? this.getMajorAxisRadius( this.manipulatedObject ) : 0.0d;
			this.radiusAnimation.setTarget( endRadius );
		}
	}
	
	@Override
	protected Color4f getDesiredColor()
	{
		if (this.isActive())
		{
			return ACTIVE_COLOR;
		}
		else if (this.isRollover())
		{
			return ROLLOVER_COLOR;
		}
		else if (this.isMuted())
		{
			return MUTED_COLOR;
		}
		else
		{
			return BASE_COLOR;
		}
	}
	
	private double getMajorAxisRadius( Composite object )
	{
		if (object != null)
		{
			Object bbox = object.getBonusDataFor( SceneEditor.BOUNDING_BOX_KEY );
			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = (edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox;
				Plane planeOfRotation = new Plane(Point3.createZero(), this.rotationAxis);
				Point3 minPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMinimum() );
				Point3 maxPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMaximum() );
				double minSize = minPlanePoint.calculateMagnitude();
				double maxSize = maxPlanePoint.calculateMagnitude();
				double radius = Math.max( minSize, maxSize );
				return radius;
			}
		}
		return 0.0d;
	}
	
	public Vector3 getRotationAxis()
	{
		return this.rotationAxis;
	}

	@Override
	public void positionRelativeToObject( Composite object ) {
		//These handles just use their local transform as their position
	}

}
