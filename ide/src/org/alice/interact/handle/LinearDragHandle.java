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


import org.alice.apis.moveandturn.TraditionalStyle;
import org.alice.ide.IDE;
import org.alice.interact.DoubleTargetBasedAnimation;
import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.MovementDirection;
import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.ui.DragStyle;

/**
 * @author David Culyba
 */
public abstract class LinearDragHandle extends ManipulationHandle3D implements PropertyListener{
	
	protected static final double MIN_LENGTH = .4d;
	
	protected double offsetPadding = 0.0d;
	protected MovementDescription dragDescription;
	protected Vector3 dragAxis;
	protected double distanceFromOrigin;
	protected Transformable standUpReference = new Transformable();
	protected Transformable snapReference = new Transformable();
	
	protected DoubleAnimation lengthAnimation;
	protected double lengthAnimationTarget;
	protected boolean doLengthEpilogue;
	
	public LinearDragHandle( )
	{
		this( new MovementDescription( MovementDirection.UP, MovementType.ABSOLUTE) );
	}
	
	public LinearDragHandle( MovementDescription dragDescription )
	{
		super();
		this.standUpReference.setName("Linear StandUp Reference");
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			this.standUpReference.putBonusDataFor(ManipulationHandle3D.VIRTUAL_PARENT_KEY, this);
		}
		this.snapReference.setName("Linear Snap Reference");
		if (SystemUtilities.isPropertyTrue(IDE.DEBUG_PROPERTY_KEY))
		{
			this.snapReference.putBonusDataFor(ManipulationHandle3D.VIRTUAL_PARENT_KEY, this);
		}
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
	}
	
	protected void setSize(double size)
	{
		this.distanceFromOrigin = size;
		this.positionRelativeToObject();
	}
	
	protected double getSize()
	{
		return this.distanceFromOrigin;
	}
	
	protected double getHandleLength()
	{
		if (this.getParentTransformable() != null)
		{
			AxisAlignedBox boundingBox = this.getManipulatedObjectBox();
			Vector3 desiredHandleValues = new Vector3(0.0d, 0.0d, 0.0d);
			Point3 max = boundingBox.getMaximum();
			Point3 min = boundingBox.getMinimum();
			Vector3 extents[] = new Vector3[6];
			extents[0] = new Vector3(max.x, 0, 0);
			extents[1] = new Vector3(0, max.y, 0);
			extents[2] = new Vector3(0, 0, max.z);
			extents[3] = new Vector3(min.x, 0, 0);
			extents[4] = new Vector3(0, min.y, 0);
			extents[5] = new Vector3(0, 0, min.z);
			for (int i=0; i<extents.length; i++)
			{
				double axisDot = Vector3.calculateDotProduct( this.dragAxis, extents[i] );
				if (axisDot > 0.0d)
				{
					desiredHandleValues.add( Vector3.createMultiplication( extents[i], this.dragAxis ) );
				}
			}
			return desiredHandleValues.calculateMagnitude();
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
	
	protected void animateHandleToLength(double desiredLength)
	{
		if (this.animator == null || this.getParentTransformable() == null)
		{
			return;
		}
//		PrintUtilities.println("\n"+this.hashCode()+":"+this+" "+(this.isHandleVisible()? "VISIBLE" : "INVISIBLE")+" Animating to "+desiredLength+" around "+this.manipulatedObject);
		double currentLength = this.getSize();
		if (currentLength == desiredLength)
		{
			if (this.lengthAnimationTarget != -1)
			{
				if (this.lengthAnimationTarget == desiredLength)
				{
					return;
				}
			}
			else
			{
				return;
			}
		}
		
		if (currentLength == desiredLength)
		{
//			PrintUtilities.println("  Not making a length animation from "+currentLength+" to "+desiredLength+" because they're the same.");
			return;
		}
		if (this.lengthAnimation != null)
		{
			if (this.lengthAnimationTarget == desiredLength)
			{
//				PrintUtilities.println("  returning because we already have an animation going to that value");
				return;
			}
			else
			{
//				PrintUtilities.println("  Stopping previous animation which was targeting "+this.lengthAnimationTarget+", current size is: "+currentLength);
				this.doLengthEpilogue = false;
				this.lengthAnimation.complete(null);
				this.doLengthEpilogue = true;
				this.lengthAnimationTarget = -1;
			}
		}
		this.lengthAnimationTarget = desiredLength;
//		PrintUtilities.println("  Making length animation from "+currentLength+" to "+desiredLength);
		this.lengthAnimation = new DoubleAnimation(ANIMATION_DURATION, TraditionalStyle.BEGIN_ABRUPTLY_AND_END_GENTLY, currentLength, desiredLength)
		{
			private String name = "LengthAnimation";
			@Override
			protected void updateValue(Double v) 
			{
				LinearDragHandle.this.setSize(v);
			}
			
			@Override
			protected void epilogue() {
				if (LinearDragHandle.this.doLengthEpilogue)
				{
					super.epilogue();
				}
				LinearDragHandle.this.lengthAnimationTarget = -1;
			}
		};
		this.animator.invokeLater(this.lengthAnimation, null);
	}
	
	@Override
	protected void updateVisibleState(HandleRenderState renderState)
	{
		super.updateVisibleState(renderState);
		double desiredLength = this.isRenderable() ? this.getHandleLength() : 0.0d;
		animateHandleToLength(desiredLength);
	}
	
	public Vector3 getDragAxis()
	{
		return this.dragAxis;
	}

	@Override
	public ReferenceFrame getReferenceFrame()
	{
		if (this.getParentTransformable() != null)
		{
			if (this.dragDescription.type == MovementType.STOOD_UP)
			{
				this.standUpReference.setParent( this.getParentTransformable() );
				this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
				this.standUpReference.setAxesOnlyToStandUp();
				return this.standUpReference;
			}
			else if (this.dragDescription.type == MovementType.ABSOLUTE)
			{
				this.standUpReference.setParent( this.getParentTransformable().getRoot() );
				AffineMatrix4x4 location = AffineMatrix4x4.createIdentity();
				location.translation.set( this.getParentTransformable().getTranslation( AsSeenBy.SCENE ) );
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
	public ReferenceFrame getSnapReferenceFrame()
	{
		if (this.getParentTransformable() != null)
		{
			if (this.dragDescription.type == MovementType.STOOD_UP)
			{
				this.snapReference.setParent( this.getParentTransformable().getRoot() );
				this.snapReference.setTransformation(this.getParentTransformable().getAbsoluteTransformation(), AsSeenBy.SCENE);
				this.snapReference.setAxesOnlyToStandUp();
				this.snapReference.setTranslationOnly(0, 0, 0, AsSeenBy.SCENE);
				return this.snapReference;
			}
			else if (this.dragDescription.type == MovementType.ABSOLUTE)
			{
				this.snapReference.setParent( this.getParentTransformable().getRoot() );
				AffineMatrix4x4 location = AffineMatrix4x4.createIdentity();
				this.snapReference.localTransformation.setValue( location );
				return this.snapReference;
			}
			else
			{
				this.snapReference.setParent( this.getParentTransformable().getRoot() );
				this.snapReference.setTransformation(this.getParentTransformable().getAbsoluteTransformation(), AsSeenBy.SCENE);
				this.snapReference.setTranslationOnly(0, 0, 0, AsSeenBy.SCENE);
				return this.snapReference;
			}
		}
		return this.getRoot();
	}
	
	@Override
	public void positionRelativeToObject()
	{
		if (this.getParentTransformable() != null)
		{
			Vector3 translation = Vector3.createMultiplication( this.dragAxis, this.distanceFromOrigin + this.offsetPadding );
			this.setTransformation( this.getTransformationForAxis( this.dragAxis ), this.getReferenceFrame() );
			this.setTranslationOnly( translation, this.getReferenceFrame() );
		}
	}

	@Override
	 public void resizeToObject()
	{
		if (this.getParentTransformable() != null)
		{
			if (this.lengthAnimation != null)
			{
				this.lengthAnimation.complete(null);
			}
			double handleLength = this.getHandleLength();
			this.setSize( handleLength );
		}
	}
	
	public void propertyChanged( PropertyEvent e ) {
		this.positionRelativeToObject();		
	}

	public void propertyChanging( PropertyEvent e ) {
		// TODO Auto-generated method stub
		
	}
	
}
