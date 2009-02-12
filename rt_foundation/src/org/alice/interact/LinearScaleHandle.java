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

import java.awt.Color;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.color.ColorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
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
	
	public LinearScaleHandle( Vector3 dragAxis, Color4f color )
	{
		this( dragAxis, color, false );
	}
	
	public LinearScaleHandle( Vector3 dragAxis, Color4f color, boolean applyAlongAxis )
	{
		super( dragAxis );
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
				Visual visualElement = this.getSGVisualForTransformable( this.manipulatedObject );
				if (visualElement != null)
					visualElement.scale.removePropertyListener( this );
			}
			super.setManipulatedObject( manipulatedObject );
			if (this.manipulatedObject != null)
			{
				Visual visualElement = this.getSGVisualForTransformable( this.manipulatedObject );
				if (visualElement != null)
					visualElement.scale.addPropertyListener( this );
			}
		}
	}
	
//	@Override
//	protected double getHandleLength( Composite object )
//	{
//		if (object != null)
//		{
//			Object bbox = object.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY );
//			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
//			{
//				AxisAlignedBox boundingBox = new AxisAlignedBox((edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox);
//				if (object instanceof Transformable)
//				{
//					boundingBox.scale( this.getTransformableScale( (Transformable)object ) );
//				}
//				double minSize = boundingBox.getMinimum().calculateMagnitude();
//				if (Double.isNaN( minSize ))
//					minSize = 0.0d;
//				double maxSize = boundingBox.getMaximum().calculateMagnitude();
//				if (Double.isNaN( maxSize ))
//					maxSize = 0.0d;
//				return Math.max( minSize, maxSize );
//			}
//		}
//		return 0.0d;
//	}
	
	@Override
	public void positionRelativeToObject( Composite object ) {
		this.setTransformation( this.getTransformationForAxis( this.dragAxis ), this.getReferenceFrame() );
		Vector3 handleOffset = new Vector3(this.dragAxis);
		handleOffset.multiply( this.getHandleLength(this.manipulatedObject) );
		this.setTranslationOnly( handleOffset, this.getReferenceFrame() );
	}

	public void propertyChanged( PropertyEvent e ) {
		this.positionRelativeToObject( this.getManipulatedObject() );		
	}
	
	public void propertyChanging( PropertyEvent e ) {
		// TODO Auto-generated method stub
		
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
	protected Color4f getDesiredColor()
	{
		Color desiredColor = new Color(this.getBaseColor().red, this.getBaseColor().green, this.getBaseColor().blue);
		if (this.isActive())
		{
			desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, 0.0d, .1d );
		}
		else if (this.isRollover())
		{
			desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, -.4d, -.3d );
		}
		else if (this.isMuted())
		{
			desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, -.6d, -.5d );
		}
		else
		{
			
		}
		return new Color4f(desiredColor);
	}

	

}
