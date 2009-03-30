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
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;

/**
 * @author David Culyba
 */
public class LinearTranslateHandle extends LinearDragHandle {

	protected Color4f baseColor;
	protected Arrow arrow;
	
	
	public LinearTranslateHandle( MovementDescription dragDescription, Color4f color )
	{
		super( dragDescription );
		this.baseColor = color;
		this.initializeAppearance();
	}
	
	public LinearTranslateHandle( LinearTranslateHandle handle )
	{
		super(handle);
		this.baseColor = handle.baseColor;
		this.initializeAppearance();
	}
	
	@Override
	public LinearTranslateHandle clone()
	{
		LinearTranslateHandle newHandle = new LinearTranslateHandle(this);
		return newHandle;
	}
	
	@Override
	protected void createShape() {
		this.arrow = new Arrow(.15, 0.075, 0.15, 0.15, BottomToTopAxis.POSITIVE_Y, this.sgFrontFacingAppearance, true);
		this.arrow.setParent( this );
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
