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
package org.alice.interact.handle;

import java.awt.Color;

import org.alice.interact.MovementDirection;
import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.awt.ColorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;

/**
 * @author David Culyba
 */
public class LinearScaleHandle extends LinearDragHandle{

	protected Arrow arrow;
	protected Color4f baseColor;
	protected Transformable standUpReference = new Transformable();
	protected boolean applyAlongAxis = false;
	
	public LinearScaleHandle( MovementDescription dragDescription, Color4f color )
	{
		this( dragDescription, color, false );
	}
	
	public LinearScaleHandle( MovementDescription dragDescription, Color4f color, boolean applyAlongAxis )
	{
		super( dragDescription );
		this.baseColor = color;
		this.applyAlongAxis = applyAlongAxis;
		this.initializeAppearance();
	}
	
	public LinearScaleHandle( LinearScaleHandle handle )
	{
		super(handle);
		this.baseColor = handle.baseColor;
		this.applyAlongAxis = handle.applyAlongAxis;
		this.initializeAppearance();
	}
	
	@Override
	public LinearScaleHandle clone()
	{
		LinearScaleHandle newHandle = new LinearScaleHandle(this);
		return newHandle;
	}
	
	public boolean applyAlongAxis()
	{
		return this.applyAlongAxis;
	}
	
	@Override
	protected void createShape() {
		createShape(1.0d);
	}
	
	protected void createShape(double scale) {
		this.arrow = new Arrow(.05*scale, 0.1*scale, 0.15*scale, 0.15*scale, BottomToTopAxis.POSITIVE_Y, this.sgFrontFacingAppearance, true);
		this.arrow.setParent( this );
	}
	
	protected Vector3 getUniformResizeOffset()
	{
		AxisAlignedBox bbox = getManipulatedObjectBox();
		Vector3 handleOffset = new Vector3(bbox.getMaximum());
		handleOffset.z = 0;
		handleOffset.x *= -1;
		return handleOffset;
	}
	
	protected Vector3 getUniformResizeDirection()
	{
		Vector3 direction =  getUniformResizeOffset();
		direction.normalize();
		return direction;
	}
	
	@Override
	public void positionRelativeToObject( Composite object ) {
		if (this.dragDescription.direction == MovementDirection.RESIZE)
		{
			this.dragAxis = this.getUniformResizeDirection();
		}
		AffineMatrix4x4 objectTransformation = this.getTransformationForAxis( this.dragAxis );
		if (objectTransformation.isNaN())
		{
			assert !objectTransformation.isNaN() : "Created NaN transformation from "+this.dragAxis;
			objectTransformation = new AffineMatrix4x4();
		}
		this.setTransformation( objectTransformation, this.getReferenceFrame() );
		Vector3 handleOffset = new Vector3(this.dragAxis);
		handleOffset.multiply( this.getHandleLength(this.manipulatedObject) );
		this.setTranslationOnly( handleOffset, this.getReferenceFrame() );
	}
	
	@Override
	protected Color4f getBaseColor()
	{
		if (this.baseColor == null)
		{
			return super.getBaseColor();
		}
		return this.baseColor;
	}
	
	@Override
	protected Color4f getDesiredColor(HandleRenderState renderState)
	{
		Color desiredColor = new Color(this.getBaseColor().red, this.getBaseColor().green, this.getBaseColor().blue);
		switch (renderState)
		{
		case NOT_VISIBLE : break; //Do nothing
		case VISIBLE_BUT_SIBLING_IS_ACTIVE : ColorUtilities.shiftHSB( desiredColor, 0.0d, -.6d, -.5d ); break;
		case VISIBLE_AND_ACTIVE : desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, 0.0d, .1d ); break;
		case VISIBLE_AND_ROLLOVER : desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, -.4d, -.3d ); break;
		case JUST_VISIBLE : break; //Do nothing
		default : break; //Do nothing
		}
		return new Color4f(desiredColor);
	}

	@Override
	protected void setScale( double scale ) {
		if (this.arrow != null)
		{
			this.arrow.setParent( null );
		}
		this.createShape( scale );
	}

	

}
