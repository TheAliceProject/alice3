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
package org.alice.interact.handle;

import java.awt.Color;

import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
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
		this.createShape(1.0d);
	}
	
	protected void createShape(double scale) {
		if (this.arrow == null)
		{
			this.arrow = new Arrow(.05*scale, 0.1*scale, 0.15*scale, 0.15*scale, BottomToTopAxis.POSITIVE_Y, this.sgFrontFacingAppearance, true);
		}
		else
		{
			this.arrow.resize(.05*scale, 0.1*scale, 0.15*scale, 0.15*scale);
		}
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
	
	@Override
	public void setHandleShowing(boolean showing) {
		super.setHandleShowing(showing);
		this.arrow.setVisualShowing(showing);
	}

}
