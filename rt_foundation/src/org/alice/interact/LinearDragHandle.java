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
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class LinearDragHandle extends ManipulationHandle implements PropertyListener{
	
	protected static final double MIN_LENGTH = .4d;
	
	protected double offsetPadding = 0.0d;
	protected MovementDescription dragDescription;
	protected Vector3 dragAxis;
	protected double distanceFromOrigin;
	protected Transformable standUpReference = new Transformable();

	protected DoubleTargetBasedAnimation lengthAnimation;
	
	public LinearDragHandle( )
	{
		this( new MovementDescription( MovementDirection.UP, MovementType.ABSOLUTE) );
	}
	
	public LinearDragHandle( MovementDescription dragDescription )
	{
		super();
		this.dragDescription = dragDescription;
		this.dragAxis = new Vector3(this.dragDescription.direction.getVector());
		this.localTransformation.setValue( this.getTransformationForAxis( this.dragAxis ) );
		this.distanceFromOrigin = 0.0d;
		createShape();
	}
	
	public LinearDragHandle( LinearDragHandle handle)
	{
		this(handle.dragDescription);
		this.initFromHandle( handle );
		this.distanceFromOrigin = handle.distanceFromOrigin;
		this.offsetPadding = handle.offsetPadding;
	}
	
	public MovementDescription getMovementDescription()
	{
		return this.dragDescription;
	}
	
	protected abstract void createShape();
	
	@Override
	protected void createAnimations()
	{
		super.createAnimations();
		this.lengthAnimation = new DoubleTargetBasedAnimation( new Double(this.distanceFromOrigin) ){
			@Override
			protected void updateValue( Double value ) {
				LinearDragHandle.this.setSize(value);
			}
		};
		this.animator.invokeLater( this.lengthAnimation, null );
	}
	
	protected void setSize(double size)
	{
		this.distanceFromOrigin = size;
		this.positionRelativeToObject( this.getManipulatedObject() );
	}
	
	protected double getHandleLength( Composite object )
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

				Vector3 desiredHandleValues = new Vector3(0.0d, 0.0d, 0.0d);
				Vector3 extents[] = { new Vector3(boundingBox.getMaximum()), new Vector3(boundingBox.getMinimum()) };
				for (int i=0; i<extents.length; i++)
				{
					double axisDot = Vector3.calculateDotProduct( this.dragAxis, extents[i] );
					if (axisDot > 0.0d)
					{
						desiredHandleValues.add( Vector3.createMultiplication( extents[i], this.dragAxis ) );
					}
//					if (Math.signum( extents[i].x ) == Math.signum( this.dragAxis.x ))
//						if ( Math.abs(extents[i].x) > Math.abs(desiredHandleValues.x) )
//							desiredHandleValues.x = extents[i].x;
//					
//					if (Math.signum( extents[i].y ) == Math.signum( this.dragAxis.y ))
//						if ( Math.abs(extents[i].y) > Math.abs(desiredHandleValues.y) )
//							desiredHandleValues.y = extents[i].y;
//					
//					if (Math.signum( extents[i].z ) == Math.signum( this.dragAxis.z ))
//						if ( Math.abs(extents[i].z) > Math.abs(desiredHandleValues.z) )
//							desiredHandleValues.z = extents[i].z;
				}
				
//				double xLength = desiredHandleValues.x / this.dragAxis.x;
//				if (Double.isNaN( xLength ))
//					xLength = 0.0d;
//				double yLength = desiredHandleValues.y / this.dragAxis.y;
//				if (Double.isNaN( yLength ))
//					yLength = 0.0d;
//				double zLength = desiredHandleValues.z / this.dragAxis.z;
//				if (Double.isNaN( zLength ))
//					zLength = 0.0d;
//				
//				double length = Math.max( xLength, Math.max( yLength, zLength ) );
//				if (length < MIN_LENGTH)
//				{
//					length = MIN_LENGTH;
//				}
				return desiredHandleValues.calculateMagnitude();
			}
		}
		return 0.0d;
	}
	
	@Override
	public void setManipulatedObject( Transformable manipulatedObject ) {
		if (this.manipulatedObject != manipulatedObject)
		{
			if (this.manipulatedObject != null)
			{
				this.manipulatedObject.localTransformation.removePropertyListener( this );
			}
			super.setManipulatedObject( manipulatedObject );
			if (this.manipulatedObject != null)
			{
				this.manipulatedObject.localTransformation.addPropertyListener( this );
			}
		}
	}
	
	public double getCurrentHandleLength()
	{
		return this.distanceFromOrigin;
	}
	
	@Override
	protected void updateVisibleState()
	{
		super.updateVisibleState();
		if (this.manipulatedObject != null && this.lengthAnimation != null)
		{
			double endHandleLength = this.isVisible() ? this.getHandleLength( this.manipulatedObject ) : 0.0d;
			this.lengthAnimation.setTarget( endHandleLength );
		}
	}
	
	public Vector3 getDragAxis()
	{
		return this.dragAxis;
	}

	@Override
	public ReferenceFrame getReferenceFrame()
	{
		if (this.manipulatedObject != null)
		{
			if (this.dragDescription.type == MovementType.STOOD_UP)
			{
				this.standUpReference.setParent( this.manipulatedObject );
				this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
				this.standUpReference.setAxesOnlyToStandUp();
				return this.standUpReference;
			}
			else if (this.dragDescription.type == MovementType.ABSOLUTE)
			{
				this.standUpReference.setParent( this.manipulatedObject.getRoot() );
				AffineMatrix4x4 location = AffineMatrix4x4.createIdentity();
				location.translation.set( this.manipulatedObject.getTranslation( AsSeenBy.SCENE ) );
				this.standUpReference.localTransformation.setValue( location );
				return this.standUpReference;
			}
			else
			{
				return this.manipulatedObject;
			}
		}
		return this;
	}
	
	@Override
	public void positionRelativeToObject( Composite object )
	{
		if (object != null)
		{
			Vector3 translation = Vector3.createMultiplication( this.dragAxis, this.distanceFromOrigin + this.offsetPadding );
			this.setTransformation( this.getTransformationForAxis( this.dragAxis ), this.getReferenceFrame() );
			this.setTranslationOnly( translation, this.getReferenceFrame() );
		}
	}

	@Override
	 public void resizeToObject( Composite object )
	{
		if (object != null)
		{
			double handleLength = this.getHandleLength( object );
			this.setSize( handleLength );
			if (this.lengthAnimation != null)
			{
				this.lengthAnimation.setCurrentValue( this.distanceFromOrigin );
				this.lengthAnimation.setTarget( this.distanceFromOrigin );
			}
		}
	}
	
	public void propertyChanged( PropertyEvent e ) {
		this.positionRelativeToObject( this.getManipulatedObject() );		
	}

	public void propertyChanging( PropertyEvent e ) {
		// TODO Auto-generated method stub
		
	}
	
}
