/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.interact.handle;

import java.awt.Color;

import org.alice.interact.MovementType;
import org.alice.interact.condition.MovementDescription;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.java.awt.ColorUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public abstract class LinearDragHandle extends ManipulationHandle3D implements PropertyListener {
	public LinearDragHandle( MovementDescription dragDescription, Color4f baseColor ) {
		this.dragDescription = dragDescription;
		this.baseColor = baseColor;
		this.standUpReference.setName( "Linear StandUp Reference" );
		if( org.alice.interact.debug.DebugInteractUtilities.isDebugEnabled() ) {
			this.standUpReference.putBonusDataFor( ManipulationHandle3D.DEBUG_PARENT_TRACKER_KEY, this );
		}
		this.snapReference.setName( "Linear Snap Reference" );
		if( org.alice.interact.debug.DebugInteractUtilities.isDebugEnabled() ) {
			this.snapReference.putBonusDataFor( ManipulationHandle3D.DEBUG_PARENT_TRACKER_KEY, this );
		}
		this.dragAxis = new Vector3( this.dragDescription.direction.getVector() );
		if( this.dragAxis.isNaN() ) {
			this.dragAxis = new Vector3( this.dragDescription.direction.getVector() );
		}
		this.localTransformation.setValue( this.getTransformationForAxis( this.dragAxis ) );
		this.distanceFromOrigin = 0.0d;
		createShape();
	}

	public LinearDragHandle( LinearDragHandle handle ) {
		this( handle.dragDescription, handle.baseColor );
		this.initFromHandle( handle );
		this.distanceFromOrigin = handle.distanceFromOrigin;
		this.offsetPadding = handle.offsetPadding;
	}

	public MovementDescription getMovementDescription() {
		return this.dragDescription;
	}

	@Override
	protected Color4f getBaseColor() {
		if( this.baseColor == null ) {
			return super.getBaseColor();
		} else {
			return this.baseColor;
		}
	}

	@Override
	protected Color4f getDesiredColor( HandleRenderState renderState ) {
		Color desiredColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.toAwtColor( this.getBaseColor() );
		switch( renderState ) {
		case NOT_VISIBLE:
			break; //Do nothing
		case VISIBLE_BUT_SIBLING_IS_ACTIVE:
			desiredColor = ColorUtilities.shiftHSB( desiredColor, 0.0d, -.6d, -.5d );
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
		return org.lgna.story.EmployeesOnly.createColor4f( desiredColor );
	}

	protected abstract void createShape();

	protected void setSize( double size ) {
		if( !Double.isInfinite( size ) ) {
			this.distanceFromOrigin = size;
			this.positionRelativeToObject();
		}
	}

	protected double getSize() {
		return this.distanceFromOrigin;
	}

	protected double getHandleLength() {
		if( this.getParentTransformable() != null ) {
			AxisAlignedBox boundingBox = this.getManipulatedObjectBox();

			Vector3 desiredHandleValues = new Vector3( 0.0d, 0.0d, 0.0d );
			if( this.dragAxis.x != 0 ) {
				if( this.dragAxis.x < 0 ) {
					desiredHandleValues.x = boundingBox.getMinimum().x;
				}
				else {
					desiredHandleValues.x = boundingBox.getMaximum().x;
				}
			}
			if( this.dragAxis.y != 0 ) {
				if( this.dragAxis.y < 0 ) {
					desiredHandleValues.y = boundingBox.getMinimum().y;
				}
				else {
					desiredHandleValues.y = boundingBox.getMaximum().y;
				}
			}
			if( this.dragAxis.z != 0 ) {
				if( this.dragAxis.z < 0 ) {
					desiredHandleValues.z = boundingBox.getMinimum().z;
				}
				else {
					desiredHandleValues.z = boundingBox.getMaximum().z;
				}
			}

			return desiredHandleValues.calculateMagnitude();
		}
		return 0;
	}

	@Override
	public void setManipulatedObject( AbstractTransformable manipulatedObject ) {
		if( this.manipulatedObject != manipulatedObject ) {
			if( this.manipulatedObject instanceof Transformable ) {
				( (Transformable)this.manipulatedObject ).localTransformation.removePropertyListener( this );
			}
			super.setManipulatedObject( manipulatedObject );
			if( this.manipulatedObject instanceof Transformable ) {
				( (Transformable)this.manipulatedObject ).localTransformation.addPropertyListener( this );
			}
		}
	}

	public double getCurrentHandleLength() {
		return this.distanceFromOrigin;
	}

	@Override
	protected void updateVisibleState( HandleRenderState renderState ) {
		super.updateVisibleState( renderState );
		double desiredLength = this.isRenderable() ? this.getHandleLength() : 0.0d;
		this.setSize( desiredLength );
	}

	public Vector3 getDragAxis() {
		return this.dragAxis;
	}

	@Override
	public ReferenceFrame getReferenceFrame() {
		if( this.getParentTransformable() != null ) {
			if( this.dragDescription.type == MovementType.STOOD_UP ) {
				this.standUpReference.setParent( this.getParentTransformable() );
				this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
				this.standUpReference.setAxesOnlyToStandUp();
				return this.standUpReference;
			} else if( this.dragDescription.type == MovementType.ABSOLUTE ) {
				this.standUpReference.setParent( this.getParentTransformable().getRoot() );
				AffineMatrix4x4 location = AffineMatrix4x4.createIdentity();
				location.translation.set( this.getParentTransformable().getTranslation( AsSeenBy.SCENE ) );
				this.standUpReference.localTransformation.setValue( location );
				return this.standUpReference;
			} else {
				return this.getParentTransformable();
			}
		}
		return this;
	}

	@Override
	public ReferenceFrame getSnapReferenceFrame() {
		if( this.getParentTransformable() != null ) {
			if( this.dragDescription.type == MovementType.STOOD_UP ) {
				this.snapReference.setParent( this.getParentTransformable().getRoot() );
				this.snapReference.setTransformation( this.getParentTransformable().getAbsoluteTransformation(), AsSeenBy.SCENE );
				this.snapReference.setAxesOnlyToStandUp();
				this.snapReference.setTranslationOnly( 0, 0, 0, AsSeenBy.SCENE );
				return this.snapReference;
			} else if( this.dragDescription.type == MovementType.ABSOLUTE ) {
				this.snapReference.setParent( this.getParentTransformable().getRoot() );
				AffineMatrix4x4 location = AffineMatrix4x4.createIdentity();
				this.snapReference.localTransformation.setValue( location );
				return this.snapReference;
			} else {
				this.snapReference.setParent( this.getParentTransformable().getRoot() );
				this.snapReference.setTransformation( this.getParentTransformable().getAbsoluteTransformation(), AsSeenBy.SCENE );
				this.snapReference.setTranslationOnly( 0, 0, 0, AsSeenBy.SCENE );
				return this.snapReference;
			}
		}
		return this.getRoot();
	}

	@Override
	public void positionRelativeToObject() {
		if( this.getParentTransformable() != null ) {
			Vector3 translation = Vector3.createMultiplication( this.dragAxis, this.distanceFromOrigin + this.offsetPadding );
			this.setTransformation( this.getTransformationForAxis( this.dragAxis ), this.getReferenceFrame() );
			this.setTranslationOnly( translation, this.getReferenceFrame() );
		}
	}

	@Override
	public void resizeToObject() {
		if( this.getParentTransformable() != null ) {
			if( this.lengthAnimation != null ) {
				this.lengthAnimation.complete( null );
			}
			double handleLength = this.getHandleLength();
			this.setSize( handleLength );
		}
	}

	@Override
	public void propertyChanged( PropertyEvent e ) {
		this.positionRelativeToObject();
	}

	@Override
	public void propertyChanging( PropertyEvent e ) {
	}

	protected final MovementDescription dragDescription;
	private final Color4f baseColor;

	private double offsetPadding = 0.0d;
	protected Vector3 dragAxis;
	private double distanceFromOrigin;
	private Transformable standUpReference = new Transformable();
	private Transformable snapReference = new Transformable();

	private DoubleInterruptibleAnimation lengthAnimation;
}
