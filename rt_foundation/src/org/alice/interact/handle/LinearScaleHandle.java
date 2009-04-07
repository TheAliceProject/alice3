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

import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.color.ColorUtilities;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;

/**
 * @author David Culyba
 */
public class LinearScaleHandle extends LinearDragHandle implements PropertyListener{

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
		this.arrow = new Arrow(.05, 0.1, 0.15, 0.15, BottomToTopAxis.POSITIVE_Y, this.sgFrontFacingAppearance, true);
		this.arrow.setParent( this );
	}
	
	@Override
	public void setManipulatedObject( Transformable manipulatedObject ) {
		if (this.manipulatedObject != manipulatedObject)
		{
			if (this.manipulatedObject != null)
			{
				this.manipulatedObject.localTransformation.removePropertyListener( this );
				Visual visualElement = this.getSGVisualForTransformable( this.manipulatedObject );
				if (visualElement != null)
					visualElement.scale.removePropertyListener( this );
			}
			super.setManipulatedObject( manipulatedObject );
			if (this.manipulatedObject != null)
			{
				this.manipulatedObject.localTransformation.addPropertyListener( this );
				Visual visualElement = this.getSGVisualForTransformable( this.manipulatedObject );
				if (visualElement != null)
					visualElement.scale.addPropertyListener( this );
				
			}
		}
	}
	
	
	@Override
	public void positionRelativeToObject( Composite object ) {
		this.setTransformation( this.getTransformationForAxis( this.dragAxis ), this.getReferenceFrame() );
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

	

}
