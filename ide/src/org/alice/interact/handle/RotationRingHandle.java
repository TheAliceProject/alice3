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

import org.lgna.story.AnimationStyle;
import org.alice.interact.MovementDirection;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.VectorUtilities;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;


/**
 * @author David Culyba
 */
public class RotationRingHandle extends ManipulationHandle3D{
	
	public enum HandlePosition
	{
		ORIGIN,
		TOP,
		BOTTOM,
		MIDDLE,
	}
	
	protected static final double MINOR_RADIUS = .075d;
	protected static final double MIN_RADIUS = .4d;
	
	protected static final Color4f ACTIVE_COLOR = new Color4f(.4f, 1.0f, 0.3f, 1.0f);
	protected static final Color4f ROLLOVER_COLOR = new Color4f(1.0f, 1.0f, 0.3f, 1.0f);
	protected static final Color4f MUTED_COLOR = new Color4f(0.5f, 0.5f, 0.4f, 1.0f);
	protected static final Color4f BASE_COLOR = new Color4f(0.7f, 0.7f, 0.3f, 1.0f);
	
	protected Torus sgTorus = new Torus();
	protected Sphere sgSphere = new Sphere();
	protected Transformable sphereTransformable = new Transformable();
	protected Visual sgSphereVisual = new Visual();
	protected Vector3 rotationAxis;
	protected MovementDirection rotationAxisDirection;
	protected Vector3 sphereDirection = new Vector3();
	protected Vector3 handleOffset =  new Vector3();
	
	protected Transformable snapReference = new Transformable();
	
	protected HandlePosition handlePosition = HandlePosition.ORIGIN;
	
	protected DoubleInterruptibleAnimation radiusAnimation;
	
	public RotationRingHandle( )
	{
		this( MovementDirection.UP, HandlePosition.ORIGIN);
	}
	
	public RotationRingHandle( MovementDirection rotationAxisDirection )
	{
		this( rotationAxisDirection, HandlePosition.ORIGIN);
	}
	
	public RotationRingHandle( MovementDirection rotationAxisDirection, HandlePosition handlePosition )
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
		this.rotationAxisDirection = rotationAxisDirection;
		this.rotationAxis = this.rotationAxisDirection.getVector();
		this.rotationAxis.normalize();
		this.sphereDirection.set( 0.0d, 0.0d, -1.0d );
		this.handlePosition = handlePosition;
		this.localTransformation.setValue( this.getTransformationForAxis( this.rotationAxis ) );
		this.sgVisual.geometries.setValue( new Geometry[] { this.sgTorus } );
	}
	
	public RotationRingHandle( RotationRingHandle handle )
	{
		this(handle.rotationAxisDirection, handle.handlePosition);
		this.initFromHandle( handle );
		this.handleOffset.set( handle.handleOffset );
	}
	
	@Override
	public RotationRingHandle clone()
	{
		RotationRingHandle newHandle = new RotationRingHandle(this);
		return newHandle;
	}
	
	protected void setPositionRelativeToObjectSize( )
	{
		if (this.getParentTransformable() != null)
		{
			AxisAlignedBox bbox = this.getManipulatedObjectBox();
			
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
			if (this.handleOffset.isNaN())
			{
				this.handleOffset.set( 0.0d, 0.0d, 0.0d );
			}
			this.setTranslationOnly( this.handleOffset, this.getReferenceFrame());
		}
	}
	
	public void initializeSnapReferenceFrame()
	{
		this.snapReference.setParent( this.manipulatedObject.getRoot() );
		this.snapReference.setTransformation(this.manipulatedObject.getAbsoluteTransformation(), AsSeenBy.SCENE);
		this.snapReference.setTranslationOnly(0, 0, 0, AsSeenBy.SCENE);
	}
	
	@Override
	public ReferenceFrame getSnapReferenceFrame()
	{
		return this.snapReference;
	}
	
	@Override
	public void setManipulatedObject( Transformable manipulatedObject ) {
		super.setManipulatedObject( manipulatedObject );
		this.setPositionRelativeToObjectSize( );
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
	
	public double getRadius()
	{
		return this.sgTorus.majorRadius.getValue();
	}
	
	protected void placeSphere()
	{
		this.sphereTransformable.setTranslationOnly( Point3.createMultiplication( this.sphereDirection, this.sgTorus.majorRadius.getValue() ), this );
	}
	
	protected void animateHandleToRadius(double desiredRadius)
	{
		if (this.animator == null || this.getParentTransformable() == null)
		{
//			PrintUtilities.println("Early exit: animator = "+this.animator+", manipulated object = "+this.manipulatedObject);
			return;
		}
//		PrintUtilities.println("\n"+this.hashCode()+":"+this+" "+(this.isHandleVisible()? "VISIBLE" : "INVISIBLE")+" Animating to "+desiredRadius+" around "+this.manipulatedObject);
		double currentRadius = this.getSize();
		//Check to see if the animation is going to get us to the desired value
		if (this.radiusAnimation != null && this.radiusAnimation.isActive() && this.radiusAnimation.matchesTarget(desiredRadius))
		{
			return;
		}
		//Stop any existing animation
		if (this.radiusAnimation != null && this.radiusAnimation.isActive())
		{
			this.radiusAnimation.cancel();
		}
		//The animation is not going to get us to the desired value, so see if we're already there
		if (currentRadius == desiredRadius)
		{
			return;
		}
		//Make a new animation and launch it
		this.radiusAnimation = new DoubleInterruptibleAnimation(ANIMATION_DURATION, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_ABRUPTLY_AND_END_GENTLY, currentRadius, desiredRadius)
		{
			@Override
			protected void updateValue(Double v) 
			{
				if (RotationRingHandle.this.sgTorus != null)
				{
					if (v.isNaN())
					{
						v = 0.0d;
					}
					if (v < 0.0d)
					{
						v = 0.0d;
					}
					RotationRingHandle.this.setSize(v);
				}
			}
		};
		this.animator.invokeLater(this.radiusAnimation, null);
	}
	
	@Override
	protected void updateVisibleState(HandleRenderState renderState)
	{
		super.updateVisibleState(renderState);
		double endRadius = this.isRenderable() ? this.getMajorAxisRadius() : 0.0d;
//		if (endRadius == 0 && this.opacityAnimationTarget != 0)
//		{
//			double targetOpacity = this.isRenderable() ? this.getDesiredOpacity(renderState) : 0.0;
//			PrintUtilities.println("Huh?");
//		}
		animateHandleToRadius(endRadius);
	}
	
	
	@Override
	protected Color4f getDesiredColor(HandleRenderState renderState)
	{
		switch (renderState)
		{
		case NOT_VISIBLE : return BASE_COLOR;
		case VISIBLE_BUT_SIBLING_IS_ACTIVE : return MUTED_COLOR;
		case VISIBLE_AND_ACTIVE : return ACTIVE_COLOR;
		case VISIBLE_AND_ROLLOVER : return ROLLOVER_COLOR;
		case JUST_VISIBLE : return BASE_COLOR;
		default : return BASE_COLOR;
		}
	}
	
	private void setSize(double size)
	{
		RotationRingHandle.this.sgTorus.majorRadius.setValue( size );
		this.placeSphere();
	}
	
	private double getSize()
	{
		return RotationRingHandle.this.sgTorus.majorRadius.getValue();
	}
	
	private double getMajorAxisRadius( )
	{
		if (this.getParentTransformable() != null)
		{
			AxisAlignedBox boundingBox = this.getManipulatedObjectBox();
			Plane planeOfRotation = new Plane(Point3.createZero(), this.rotationAxis);
			Point3 minPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMinimum() );
			Point3 maxPlanePoint = PlaneUtilities.projectPointIntoPlane( planeOfRotation, boundingBox.getMaximum() );
			double minSize = minPlanePoint.calculateMagnitude();
			double maxSize = maxPlanePoint.calculateMagnitude();
			double radius = Math.max( minSize, maxSize );
			if (Double.isNaN( radius ) || radius < MIN_RADIUS)
			{
				radius = MIN_RADIUS;
			}
			return radius;
		}
		return 0.0d;
	}
	
	public Vector3 getRotationAxis()
	{
		return this.rotationAxis;
	}
	
	public MovementDirection getRotationDirection()
	{
		return this.rotationAxisDirection;
	}

	@Override
	public void positionRelativeToObject( ) {
		//These handles just use their local transform as their position
	}
	
	@Override
	public void resizeToObject()
	{
		if (this.radiusAnimation != null)
		{
			this.radiusAnimation.complete(null);
		}
		this.setPositionRelativeToObjectSize( );
		double radius = this.getMajorAxisRadius( );
		this.setSize(radius);
	}
	
	@Override
	protected void setScale( double scale ) {
		if (this.sgTorus != null)
		{
			this.sgTorus.minorRadius.setValue( MINOR_RADIUS * scale );
		}
		if (this.sgSphere != null)
		{
			this.sgSphere.radius.setValue( MINOR_RADIUS * 2.0d * scale);
		}
	}
	
	@Override
	public void setHandleShowing(boolean showing) {
		super.setHandleShowing(showing);
	}

}
