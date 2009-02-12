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
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
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
	
	public enum HandlePosition
	{
		ORIGIN,
		TOP,
		BOTTOM,
		MIDDLE,
	}
	
	protected static final double MINOR_RADIUS = .075d;
	
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
	protected Vector3 handleOffset =  new Vector3();
	
	protected HandlePosition handlePosition = HandlePosition.ORIGIN;
	
	protected DoubleTargetBasedAnimation radiusAnimation;
	
	public RotationRingHandle( )
	{
		this( Vector3.accessPositiveYAxis(), HandlePosition.ORIGIN);
	}
	
	public RotationRingHandle( Vector3 rotationAxis )
	{
		this( rotationAxis, HandlePosition.ORIGIN);
	}
	
	public RotationRingHandle( Vector3 rotationAxis, HandlePosition handlePosition )
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
		this.handlePosition = handlePosition;
		this.localTransformation.setValue( this.getTransformationForAxis( this.rotationAxis ) );
		this.sgVisual.geometries.setValue( new Geometry[] { this.sgTorus } );
	}
	
	public RotationRingHandle( RotationRingHandle handle )
	{
		this(handle.rotationAxis, handle.handlePosition);
		this.initFromHandle( handle );
		this.handleOffset.set( handle.handleOffset );
	}
	
	protected void setPositionRelativeToObjectSize( Composite object )
	{
		if (manipulatedObject != null)
		{
			Object bboxObj = manipulatedObject.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY );
			if (bboxObj instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				AxisAlignedBox bbox = new AxisAlignedBox((edu.cmu.cs.dennisc.math.AxisAlignedBox)bboxObj);
				if (object instanceof Transformable)
				{
					bbox.scale( this.getTransformableScale( (Transformable)object ) );
				}
				Vector3 maxVector = VectorUtilities.projectOntoVector( new Vector3(bbox.getMaximum()), this.rotationAxis );
				Vector3 minVector = VectorUtilities.projectOntoVector( new Vector3(bbox.getMinimum()), this.rotationAxis );
				this.handleOffset.set( 0.0d, 0.0d, 0.0d );
				switch (this.handlePosition)
				{
					case TOP :
					{
						this.handleOffset.set( maxVector );
						double handleSize = this.sgTorus.minorRadius.getValue();
						Vector3 sizeOffset = new Vector3(this.rotationAxis);
						sizeOffset.normalize();
						sizeOffset.multiply( -handleSize );
						this.handleOffset.add( sizeOffset);
					} break;
					case MIDDLE :
					{
						this.handleOffset.set( maxVector );
						this.handleOffset.add( minVector );
						this.handleOffset.multiply( .5d );
					} break;
					case BOTTOM :
					{
						this.handleOffset.set( minVector );
						double handleSize = this.sgTorus.minorRadius.getValue();
						Vector3 sizeOffset = new Vector3(this.rotationAxis);
						sizeOffset.normalize();
						sizeOffset.multiply( handleSize );
						this.handleOffset.add( sizeOffset);
					} break;
				}
				this.setTranslationOnly( this.handleOffset, this.getReferenceFrame());
			}
		}
	}
	
	@Override
	public void setManipulatedObject( Transformable manipulatedObject ) {
		super.setManipulatedObject( manipulatedObject );
		this.setPositionRelativeToObjectSize( manipulatedObject );
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
		double currentRadius = this.sgTorus.majorRadius.getValue();
		if (Double.isNaN( currentRadius ))
		{
			currentRadius = 0.0d;
		}
		this.radiusAnimation = new DoubleTargetBasedAnimation( new Double(currentRadius) ){
			@Override
			protected void updateValue( Double value ) {
				if (RotationRingHandle.this.sgTorus != null)
				{
					if (value.isNaN())
					{
						value = 0.0d;
					}
					if (value < 0.0d)
					{
						value = 0.0d;
					}
					RotationRingHandle.this.setSize(value);
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
			if (Double.isNaN( endRadius ))
			{
				endRadius = 0.0d;
			}
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
	
	private void setSize(double size)
	{
		RotationRingHandle.this.sgTorus.majorRadius.setValue( size );
	}
	
	private double getMajorAxisRadius( Composite object )
	{
		if (object != null)
		{
			Object bbox = object.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY );
			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				AxisAlignedBox boundingBox = new AxisAlignedBox((edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox);
				if (object instanceof Transformable)
				{
					boundingBox.scale( this.getTransformableScale( (Transformable)object ) );
				}
				Plane planeOfRotation = new Plane(Point3.createZero(), this.rotationAxis);
				Point3 minPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMinimum() );
				Point3 maxPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMaximum() );
				double minSize = minPlanePoint.calculateMagnitude();
				double maxSize = maxPlanePoint.calculateMagnitude();
				double radius = Math.max( minSize, maxSize );
				if (Double.isNaN( radius ))
				{
					radius = 0.0d;
				}
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
	
	@Override
	public void resizeToObject( Composite object )
	{
		this.setPositionRelativeToObjectSize( object );
		double radius = this.getMajorAxisRadius( object );
		this.setSize(radius);
		if (this.radiusAnimation != null)
		{
			this.radiusAnimation.setCurrentValue( radius );
			this.radiusAnimation.setTarget( radius );
		}
	}

}
