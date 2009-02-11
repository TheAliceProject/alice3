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
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;

/**
 * @author David Culyba
 */
public abstract class LinearDragHandle extends ManipulationHandle {
	
	protected double offsetPadding = .1d;
	protected edu.cmu.cs.dennisc.scenegraph.Shape sgShape = null;
	protected Vector3 dragAxis;
	protected double distanceFromOrigin;

	protected DoubleTargetBasedAnimation lengthAnimation;
	
	public LinearDragHandle( )
	{
		this( Vector3.accessPositiveYAxis());
	}
	
	public LinearDragHandle( Vector3 dragAxis )
	{
		super();
		createShape();
		this.sgVisual.geometries.setValue( new Geometry[] { this.sgShape } );
		
		this.dragAxis = new Vector3(dragAxis);
		this.dragAxis.normalize();
		this.localTransformation.setValue( this.getTransformationForAxis( this.dragAxis ) );
		this.distanceFromOrigin = 0.0d;
	}

	protected abstract void createShape();
	
	@Override
	protected void createAnimations()
	{
		super.createAnimations();
		this.lengthAnimation = new DoubleTargetBasedAnimation( new Double(this.distanceFromOrigin) ){
			@Override
			protected void updateValue( Double value ) {
				LinearDragHandle.this.distanceFromOrigin = value;
				LinearDragHandle.this.positionRelativeToObject( LinearDragHandle.this.getManipulatedObject() );
			}
		};
		this.animator.invokeLater( this.lengthAnimation, null );
	}
	
	protected double getHandleLength( Composite object )
	{
		if (object != null)
		{
			Object bbox = object.getBonusDataFor( SceneEditor.BOUNDING_BOX_KEY );
			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = (edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox;
				Vector3 maxVector = VectorUtilities.projectOntoVector( new Vector3(boundingBox.getMaximum()), this.dragAxis );
				Vector3 minVector = VectorUtilities.projectOntoVector( new Vector3(boundingBox.getMinimum()), this.dragAxis );
				
				double maxDot = Vector3.calculateDotProduct( maxVector, this.dragAxis  );
				double minDot = Vector3.calculateDotProduct( minVector, this.dragAxis  );
				if (maxDot > minDot)
				{
					return Math.abs( maxDot );
				}
				else
				{
					return Math.abs( minDot );
				}
			}
		}
		return 0.0d;
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
	public void positionRelativeToObject( Composite object )
	{
		if (object != null)
		{
			Vector3 translation = Vector3.createMultiplication( this.dragAxis, this.distanceFromOrigin + this.offsetPadding );
			this.setTranslationOnly( translation, object );
		}
	}

}
