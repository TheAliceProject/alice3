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

import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.util.Arrow;

/**
 * @author David Culyba
 */
public class LinearScaleHandle extends LinearDragHandle {

	protected Arrow arrow;
	protected Color4f baseColor;
	protected Transformable standUpReference = new Transformable();
	protected boolean applyAlongAxis = false;
	protected org.lgna.story.implementation.ModelImp.Resizer resizer;

	public static LinearScaleHandle createFromResizer( org.lgna.story.implementation.ModelImp.Resizer resizer ) {
		LinearScaleHandle toReturn = null;
		switch( resizer ) {
		case UNIFORM: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.RESIZE, MovementType.STOOD_UP ), Color4f.PINK );
			break;
		}
		case X_AXIS: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.RIGHT, MovementType.LOCAL ), Color4f.MAGENTA, true );
			break;
		}
		case Y_AXIS: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.UP, MovementType.LOCAL ), Color4f.YELLOW, true );
			break;
		}
		case Z_AXIS: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.FORWARD, MovementType.LOCAL ), Color4f.CYAN, true );
			break;
		}
		case XY_PLANE: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.UP_RIGHT, MovementType.LOCAL ), Color4f.PINK );
			break;
		}
		case XZ_PLANE: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.RIGHT_FORWARD, MovementType.LOCAL ), Color4f.PINK );
			break;
		}
		case YZ_PLANE: {
			toReturn = new LinearScaleHandle( new MovementDescription( MovementDirection.UP_FORWARD, MovementType.LOCAL ), Color4f.PINK );
			break;
		}
		}
		toReturn.resizer = resizer;
		return toReturn;
	}

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
		super( handle );
		this.baseColor = handle.baseColor;
		this.applyAlongAxis = handle.applyAlongAxis;
		this.resizer = handle.resizer;
		this.initializeAppearance();
	}

	@Override
	public LinearScaleHandle clone()
	{
		LinearScaleHandle newHandle = new LinearScaleHandle( this );
		return newHandle;
	}

	public boolean applyAlongAxis()
	{
		return this.applyAlongAxis;
	}

	@Override
	protected void createShape() {
		createShape( 1.0d );
	}

	protected void createShape( double scale ) {
		this.arrow = new Arrow( .05 * scale, 0.1 * scale, 0.15 * scale, 0.15 * scale, BottomToTopAxis.POSITIVE_Y, this.sgFrontFacingAppearance, true );
		this.arrow.setParent( this );
	}

	public org.lgna.story.implementation.ModelImp.Resizer getResizer()
	{
		return this.resizer;
	}

	protected Vector3 getUniformResizeOffset()
	{
		AxisAlignedBox bbox = getManipulatedObjectBox();
		Vector3 handleOffset;
		if( bbox != null )
		{
			handleOffset = new Vector3( bbox.getMaximum() );
			handleOffset.z = 0;
			handleOffset.x *= -1;
		}
		else
		{
			handleOffset = new Vector3( 1, 1, 0 );
		}
		if( handleOffset.isZero() )
		{
			handleOffset = new Vector3( 1, 1, 0 );
		}
		return handleOffset;
	}

	protected Vector3 getUniformResizeDirection()
	{
		Vector3 direction = getUniformResizeOffset();
		direction.normalize();
		return direction;
	}

	@Override
	public void positionRelativeToObject() {
		if( this.dragDescription.direction == MovementDirection.RESIZE )
		{
			this.dragAxis = this.getUniformResizeDirection();
		}
		AffineMatrix4x4 objectTransformation = this.getTransformationForAxis( this.dragAxis );
		if( objectTransformation.isNaN() )
		{
			objectTransformation = this.getTransformationForAxis( this.dragAxis );
			assert !objectTransformation.isNaN() : "Created NaN transformation from " + this.dragAxis;
			objectTransformation = new AffineMatrix4x4();
		}
		this.setTransformation( objectTransformation, this.getReferenceFrame() );
		Vector3 handleOffset = new Vector3( this.dragAxis );
		handleOffset.multiply( this.getHandleLength() );
		this.setTranslationOnly( handleOffset, this.getReferenceFrame() );
	}

	@Override
	protected Color4f getBaseColor()
	{
		if( this.baseColor == null )
		{
			return super.getBaseColor();
		}
		return this.baseColor;
	}

	@Override
	protected Color4f getDesiredColor( HandleRenderState renderState )
	{
		Color desiredColor = new Color( this.getBaseColor().red, this.getBaseColor().green, this.getBaseColor().blue );
		switch( renderState )
		{
		case NOT_VISIBLE:
			break; //Do nothing
		case VISIBLE_BUT_SIBLING_IS_ACTIVE:
			ColorUtilities.shiftHSB( desiredColor, 0.0d, -.6d, -.5d );
			break;
		case VISIBLE_AND_ACTIVE:
			desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, 0.0d, .1d );
			break;
		case VISIBLE_AND_ROLLOVER:
			desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, -.4d, -.3d );
			break;
		case JUST_VISIBLE:
			break; //Do nothing
		default:
			break; //Do nothing
		}
		return new Color4f( desiredColor );
	}

	@Override
	protected void setScale( double scale ) {
		if( this.arrow != null )
		{
			this.arrow.setParent( null );
		}
		this.createShape( scale );
	}

	@Override
	public void setVisualsShowing( boolean showing ) {
		super.setVisualsShowing( showing );
		this.arrow.setVisualShowing( showing );
	}

}
