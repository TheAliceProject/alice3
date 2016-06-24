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

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.event.EventCriteriaManager;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.manipulator.AbstractManipulator;
import org.lgna.story.implementation.BoundingBoxUtilities;

import edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.color.animation.Color4fAnimation;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;
import edu.cmu.cs.dennisc.scenegraph.scale.Scalable;

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle3D extends Transformable implements ManipulationHandle, ManipulationListener {

	public static final Key<Object> DEBUG_PARENT_TRACKER_KEY = Key.createInstance( "DEBUG_PARENT_TRACKER_KEY" );

	protected static final double ANIMATION_DURATION = .25;

	public static final Criterion<Component> NOT_3D_HANDLE_CRITERION = new Criterion<Component>() {
		protected boolean isHandle( Component c ) {
			if( c == null ) {
				return false;
			}
			Object bonusData = c.getBonusDataFor( PickHint.PICK_HINT_KEY );
			if( ( bonusData instanceof PickHint ) && ( (PickHint)bonusData ).intersects( PickHint.PickType.THREE_D_HANDLE.pickHint() ) ) {
				return true;
			} else {
				return isHandle( c.getParent() );
			}
		}

		@Override
		public boolean accept( Component c ) {
			return !isHandle( c );
		}
	};

	protected static abstract class Color4fInterruptibleAnimation extends Color4fAnimation {
		private boolean doEpilogue = true;
		private boolean isActive = true;
		private Color4f target;

		public Color4fInterruptibleAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, Color4f d0, Color4f d1 ) {
			super( duration, style, d0, d1 );
			this.isActive = true;
			this.target = d1;
		}

		@Override
		protected void epilogue() {
			if( this.doEpilogue ) {
				super.epilogue();
			}
			this.isActive = false;
			this.target = null;
		}

		public boolean isActive() {
			return this.isActive;
		}

		public Color4f getTarget() {
			return this.target;
		}

		public boolean matchesTarget( Color4f target ) {
			return ( ( this.target != null ) && this.target.equals( target ) );
		}

		public void cancel() {
			this.doEpilogue = false;
			this.complete( null );
			this.doEpilogue = true;
		}
	}

	protected static abstract class DoubleInterruptibleAnimation extends DoubleAnimation {
		private boolean doEpilogue = true;
		private boolean isActive = true;
		private double target;

		public DoubleInterruptibleAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, Double d0, Double d1 ) {
			super( duration, style, d0, d1 );
			this.isActive = true;
			this.target = d1;
		}

		@Override
		protected void epilogue() {
			if( this.doEpilogue ) {
				super.epilogue();
			}
			this.isActive = false;
			this.target = -1;
		}

		public boolean isActive() {
			return this.isActive;
		}

		public double getTarget() {
			return this.target;
		}

		public boolean matchesTarget( double target ) {
			return this.target == target;
			//			return edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon(this.target, target);
		}

		public void cancel() {
			this.doEpilogue = false;
			this.complete( null );
			this.doEpilogue = true;
		}
	}

	public ManipulationHandle3D() {
		sgVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		setCurrentColorInternal();
		sgVisual.setParent( this );
		this.setName( this.getClass().getSimpleName() );
		this.putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.PickType.THREE_D_HANDLE.pickHint() );
		this.addAbsoluteTransformationListener( this.absoluteTransformationListener );
	}

	@Override
	public abstract ManipulationHandle3D clone();

	protected void initFromHandle( ManipulationHandle3D handle ) {
		this.manipulatedObject = handle.manipulatedObject;
		this.state = new HandleState( handle.state );
		this.handleSet.clear();
		this.handleSet.addSet( handle.handleSet );
		this.localTransformation.setValue( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( handle.localTransformation.getValue() ) );
		this.criteriaManager = handle.criteriaManager;
		this.handleManager = handle.handleManager;
		this.manipulation = handle.manipulation;
	}

	private Scalable getScalable( AbstractTransformable object ) {
		Scalable scalable = null;
		if( object instanceof Scalable ) {
			scalable = (Scalable)object;
		} else if( object != null ) {
			scalable = object.getBonusDataFor( Scalable.KEY );
		}
		return scalable;
	}

	@Override
	public void clear() {
		this.setManipulatedObject( null );
		this.setParent( null );
	}

	public void setManipulatedObject( AbstractTransformable manipulatedObjectIn ) {
		if( this.manipulatedObject != null ) {
			Scalable s = getScalable( this.manipulatedObject );
			if( s != null ) {
				s.removeScaleListener( this.scaleListener );
			}
		}
		if( this.manipulatedObject != manipulatedObjectIn ) {
			this.manipulatedObject = manipulatedObjectIn;
			this.criteriaManager.setTargetTransformable( this.manipulatedObject );
			this.setParent( this.manipulatedObject );
			if( this.manipulatedObject != null ) {
				this.setScale( this.getObjectScale() );
				this.setVisualsShowing( true );
			} else {
				this.setVisualsShowing( false );
			}
		}
		if( this.manipulatedObject != null ) {
			Scalable s = getScalable( this.manipulatedObject );
			if( s != null ) {
				s.addScaleListener( this.scaleListener );
			}
		}
		this.resizeToObject();
	}

	@Override
	public void setSelectedObject( AbstractTransformable selectedObject ) {
		this.setManipulatedObject( selectedObject );
	}

	@Override
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;
	}

	@Override
	public void setDragAdapterAndAddHandle( AbstractDragAdapter dragAdapter ) {
		this.setDragAdapter( dragAdapter );
		if( this.dragAdapter != null ) {
			this.dragAdapter.addHandle( this );
		}
	}

	public void updateCameraRelativeOpacity() {
		if( ManipulationHandle3D.this.dragAdapter != null ) {
			AbstractCamera activeCamera = ManipulationHandle3D.this.dragAdapter.getActiveCamera();
			if( activeCamera instanceof SymmetricPerspectiveCamera ) {
				Point3 cameraLocation = ( (SymmetricPerspectiveCamera)activeCamera ).getAbsoluteTransformation().translation;
				ManipulationHandle3D.this.setCameraPosition( cameraLocation );
			}
		}
	}

	protected abstract void setScale( double scale );

	protected final void setCurrentColorInternal() {
		HandleRenderState renderState = HandleRenderState.getStateForHandle( this );
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor( renderState ) );
		sgFrontFacingAppearance.opacity.setValue( new Float( this.getDesiredOpacity( renderState ) ) );
	}

	protected final void initializeAppearance() {
		this.setCurrentColorInternal();
	}

	protected void setTransformableScale( AbstractTransformable t, edu.cmu.cs.dennisc.math.Matrix3x3 scaleMatrix ) {
		Visual objectVisual = getSGVisualForTransformable( t );
		objectVisual.scale.setValue( scaleMatrix );
	}

	protected Visual getSGVisualForTransformable( AbstractTransformable object ) {
		if( object == null ) {
			return null;
		}
		for( int i = 0; i < object.getComponentCount(); i++ ) {
			Component c = object.getComponentAt( i );
			if( c instanceof Visual ) {
				return (Visual)c;
			}
		}
		return null;
	}

	@Override
	public void setParent( Composite parent ) {
		super.setParent( parent );
		this.updateCameraRelativeOpacity();
	}

	@Override
	public void addToSet( HandleSet set ) {
		this.handleSet.addSet( set );
	}

	@Override
	public HandleSet getHandleSet() {
		return this.handleSet;
	}

	@Override
	public boolean isAlwaysVisible() {
		return false;
	}

	@Override
	public void addToGroup( HandleSet.HandleGroup group ) {
		this.handleSet.addGroup( group );
	}

	@Override
	public void addToGroups( HandleSet.HandleGroup... groups ) {
		this.handleSet.addGroups( groups );
	}

	@Override
	public boolean isMemberOf( HandleSet set ) {
		if( set == null ) {
			return false;
		} else {
			boolean setIntersects = set.intersects( this.handleSet );
			boolean handleIntersects = this.handleSet.intersects( set );
			return setIntersects || handleIntersects;
		}
	}

	@Override
	public boolean isMemberOf( HandleSet.HandleGroup group ) {
		return this.handleSet.get( group.ordinal() );
	}

	public Visual getSGVisual() {
		return sgVisual;
	}

	public SimpleAppearance getSGFrontFacingAppearance() {
		return sgFrontFacingAppearance;
	}

	@Override
	public AbstractTransformable getManipulatedObject() {
		return this.manipulatedObject;
		//		return (Transformable)this.getParent();
	}

	@Override
	public void setName( String name ) {
		super.setName( name );
		sgVisual.setName( name + ".sgVisual" );
		sgFrontFacingAppearance.setName( name + ".sgFrontFacingAppearance" );
	}

	public ReferenceFrame getReferenceFrame() {
		return this.getParentTransformable();
	}

	public abstract ReferenceFrame getSnapReferenceFrame();

	public abstract void positionRelativeToObject();

	public abstract void resizeToObject();

	protected float getOpacity() {
		return this.sgFrontFacingAppearance.opacity.getValue();
	}

	protected void setOpacity( float opacity ) {
		this.sgFrontFacingAppearance.opacity.setValue( opacity );
	}

	protected void setColor( Color4f color ) {
		this.sgFrontFacingAppearance.diffuseColor.setValue( color );
	}

	protected void updateVisibleState( HandleRenderState renderState ) {
		double targetOpacity = this.isRenderable() ? this.getDesiredOpacity( renderState ) : 0.0;
		this.setOpacity( (float)targetOpacity );
		Color4f targetColor = this.getDesiredColor( renderState );
		this.setColor( targetColor );
	}

	@Override
	public boolean matches( ManipulationEvent event ) {
		return this.criteriaManager.matches( event );
	}

	@Override
	public void addCondition( ManipulationEventCriteria condition ) {
		this.criteriaManager.addCondition( condition );
	}

	@Override
	public void removeCondition( ManipulationEventCriteria condition ) {
		this.criteriaManager.removeCondition( condition );
	}

	@Override
	public void activate( ManipulationEvent event ) {
		this.setHandleActive( true );
	}

	@Override
	public void deactivate( ManipulationEvent event ) {
		this.setHandleActive( false );
	}

	protected Color4f getBaseColor() {
		return Color4f.YELLOW;
	}

	protected Color4f getDesiredColor( HandleRenderState renderState ) {
		Color4f baseColor = this.getBaseColor();
		switch( renderState ) {
		case NOT_VISIBLE:
			return baseColor;
		case VISIBLE_BUT_SIBLING_IS_ACTIVE:
			return baseColor;
		case VISIBLE_AND_ACTIVE:
			return baseColor;
		case VISIBLE_AND_ROLLOVER:
			return baseColor;
		case JUST_VISIBLE:
			return baseColor;
		default:
			return baseColor;
		}
	}

	@Override
	public void setCameraPosition( Point3 cameraPosition ) {
		this.setCameraRelativeOpacity( this.calculateCameraRelativeOpacity( cameraPosition ) );
	}

	public float calculateCameraRelativeOpacity( Point3 cameraPosition ) {
		if( ( this.getParentTransformable() != null ) && ( cameraPosition != null ) ) {
			Point3 handlePosition = this.getParentTransformable().getAbsoluteTransformation().translation;
			double distance = Point3.calculateDistanceBetween( cameraPosition, handlePosition );
			if( distance < .2 ) {
				return 0.0f;
			} else if( distance < .5 ) {
				return (float)( ( distance - .2f ) / ( .5 - .2 ) );
			}
		}
		return 1;
	}

	public void setCameraRelativeOpacity( float cameraRelativeOpacity ) {
		if( this.cameraRelativeOpacity != cameraRelativeOpacity ) {
			this.cameraRelativeOpacity = cameraRelativeOpacity;
			this.setOpacity( (float)this.getDesiredOpacity( HandleRenderState.getStateForHandle( this ) ) );
		}
	}

	protected double getDesiredOpacity( HandleRenderState renderState ) {
		//		PrintUtilities.println(this.getClass().getSimpleName()+":"+this.hashCode()+" camera opacity: "+this.cameraRelativeOpacity);
		switch( renderState ) {
		case NOT_VISIBLE:
			return 0.0d;
		case VISIBLE_BUT_SIBLING_IS_ACTIVE:
			return .5d * this.cameraRelativeOpacity;
		case VISIBLE_AND_ACTIVE:
			return 1.0d * this.cameraRelativeOpacity;
		case VISIBLE_AND_ROLLOVER:
			return .75d * this.cameraRelativeOpacity;
		case JUST_VISIBLE:
			return .6d * this.cameraRelativeOpacity;
		default:
			return 0.0d;
		}
	}

	public AffineMatrix4x4 getTransformationForAxis( Vector3 axis ) {
		double upDot = Vector3.calculateDotProduct( axis, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 transform = new AffineMatrix4x4();
		if( Math.abs( upDot ) != 1.0d ) {
			Vector3 rightAxis = Vector3.createCrossProduct( axis, Vector3.accessPositiveYAxis() );
			rightAxis.normalize();
			Vector3 upAxis = axis;
			Vector3 backwardAxis = Vector3.createCrossProduct( rightAxis, upAxis );
			backwardAxis.normalize();
			transform.orientation.set( rightAxis, upAxis, backwardAxis );
		} else if( upDot == -1.0d ) {
			transform.applyRotationAboutXAxis( new AngleInRadians( Math.PI ) );
		}
		return transform;
	}

	@Override
	public void setManipulation( AbstractManipulator manipulation ) {
		this.manipulation = manipulation;
		if( this.manipulation != null ) {
			this.setPickable( true );
		}
	}

	@Override
	public AbstractManipulator getManipulation( InputState input ) {
		return this.manipulation;
	}

	@Override
	public void setHandleManager( HandleManager handleManager ) {
		this.handleManager = handleManager;
	}

	@Override
	public HandleManager getHandleManager() {
		return this.handleManager;
	}

	@Override
	public HandleState getHandleStateCopy() {
		return new HandleState( this.state );
	}

	@Override
	public boolean isRenderable() {
		if( this.manipulatedObject == null ) {
			return false;
		}
		if( this.isAlwaysVisible() ) {
			return true;
		}
		return this.state.shouldRender();
	}

	protected double getObjectScale() {
		if( this.getManipulatedObject() == null ) {
			return 1.0d;
		}
		final double VOLUME_NORMALIZER = 1d;
		AxisAlignedBox bbox = this.getManipulatedObjectBox();
		if( ( bbox == null ) || bbox.isNaN() ) {
			return 1.0d;
		}
		Point3 max = bbox.getMaximum();
		max.y = 0d;
		Point3 min = bbox.getMinimum();
		min.y = 0d;
		double volume = Point3.createSubtraction( max, min ).calculateMagnitude();
		double scale = volume / VOLUME_NORMALIZER;
		if( Double.isNaN( scale ) ) {
			return 1;
		}
		if( scale < .25d ) {
			scale = .25d;
		}
		if( scale > 2.0d ) {
			scale = 2.0d;
		}
		return scale;

	}

	protected edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getParentTransformable() {
		if( this.manipulatedObject == null ) {
			return null;
		}
		Composite parent = this.getParent();
		if( parent instanceof edu.cmu.cs.dennisc.scenegraph.AbstractTransformable ) {
			return (edu.cmu.cs.dennisc.scenegraph.AbstractTransformable)parent;
		}
		if( parent != null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Unknown parent type for handle: " + parent );
		}
		edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "NULL parent for handle." );
		return null;
	}

	protected AxisAlignedBox getManipulatedObjectBox() {
		edu.cmu.cs.dennisc.scenegraph.AbstractTransformable manipulatedObject = this.getManipulatedObject();
		AxisAlignedBox boundingBox = BoundingBoxUtilities.getSGTransformableScaledBBox( manipulatedObject, false );
		if( boundingBox == null ) {
			boundingBox = new AxisAlignedBox( new Point3( -1, 0, -1 ), new Point3( 1, 1, 1 ) );
		}
		return boundingBox;
	}

	public void setPickable( boolean isPickable ) {
		this.isPickable = isPickable;
	}

	@Override
	public boolean isPickable() {
		if( this.isPickable ) {
			return this.state.isVisible();
		} else {
			return false;
		}
	}

	@Override
	public void setHandleActive( boolean active ) {
		this.state.setActive( active );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	@Override
	public void setHandleRollover( boolean rollover ) {
		this.state.setRollover( rollover );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	@Override
	public boolean isHandleVisible() {
		return this.state.isVisible() || this.isAlwaysVisible();
	}

	@Override
	public void setHandleVisible( boolean visible ) {
		this.state.setVisible( visible );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	@Override
	public void setVisualsShowing( boolean showing ) {
		this.getSGVisual().isShowing.setValue( showing );
	}

	@Override
	public PickHint getPickHint() {
		return PickHint.PickType.THREE_D_HANDLE.pickHint();
	}

	private final PropertyListener scaleListener = new PropertyListener() {
		@Override
		public void propertyChanged( PropertyEvent e ) {
			ManipulationHandle3D.this.setScale( ManipulationHandle3D.this.getObjectScale() );
			ManipulationHandle3D.this.resizeToObject();
			ManipulationHandle3D.this.positionRelativeToObject();
		}

		@Override
		public void propertyChanging( PropertyEvent e ) {
		}
	};

	private final AbsoluteTransformationListener absoluteTransformationListener = new AbsoluteTransformationListener() {
		@Override
		public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) {
			ManipulationHandle3D.this.updateCameraRelativeOpacity();
		}
	};

	protected Visual sgVisual = new Visual();
	protected SimpleAppearance sgFrontFacingAppearance = new SimpleAppearance();
	protected AbstractTransformable manipulatedObject;
	private EventCriteriaManager criteriaManager = new EventCriteriaManager();

	private HandleState state = new HandleState();
	private HandleManager handleManager = null;
	private HandleSet handleSet = new HandleSet();

	private DoubleInterruptibleAnimation opacityAnimation;
	private Color4fInterruptibleAnimation colorAnimation;

	private AbstractManipulator manipulation = null;
	private AbstractDragAdapter dragAdapter = null;
	private boolean isPickable = false; //This is false until a manipulation is set on the handle

	protected float cameraRelativeOpacity = 1.0f;

	//Animation stuff
	//	protected Animator animator;
}
