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

import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.event.EventCriteriaManager;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.Scalable;
import org.alice.stageide.utilities.BoundingBoxUtilities;

import edu.cmu.cs.dennisc.animation.Animator;
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

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle3D extends Transformable implements ManipulationHandle, ManipulationListener {

	public static final Key<Object> DEBUG_PARENT_TRACKER_KEY = Key.createInstance( "DEBUG_PARENT_TRACKER_KEY" );

	public static final double ANIMATION_DURATION = .25;

	protected Visual sgVisual = new Visual();
	protected SimpleAppearance sgFrontFacingAppearance = new SimpleAppearance();
	protected AbstractTransformable manipulatedObject;
	protected Animator animator;
	private EventCriteriaManager criteriaManager = new EventCriteriaManager();

	protected HandleState state = new HandleState();
	protected HandleManager handleManager = null;
	protected HandleSet handleSet = new HandleSet();

	protected DoubleInterruptibleAnimation opacityAnimation;
	protected Color4fInterruptibleAnimation colorAnimation;

	protected AbstractManipulator manipulation = null;
	protected AbstractDragAdapter dragAdapter = null;
	protected boolean isPickable = false; //This is false until a manipulation is set on the handle

	protected float cameraRelativeOpacity = 1.0f;

	public static Criterion<Component> NOT_3D_HANDLE_CRITERION = new Criterion<Component>() {
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

		public boolean accept( Component c ) {
			return !isHandle( c );
		}
	};

	protected abstract class Color4fInterruptibleAnimation extends Color4fAnimation
	{
		private boolean doEpilogue = true;
		private boolean isActive = true;
		private Color4f target;

		public Color4fInterruptibleAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, Color4f d0, Color4f d1 )
		{
			super( duration, style, d0, d1 );
			this.isActive = true;
			this.target = d1;
		}

		@Override
		protected void epilogue()
		{
			if( this.doEpilogue )
			{
				super.epilogue();
			}
			this.isActive = false;
			this.target = null;
		}

		public boolean isActive()
		{
			return this.isActive;
		}

		public Color4f getTarget()
		{
			return this.target;
		}

		public boolean matchesTarget( Color4f target )
		{
			return ( ( this.target != null ) && this.target.equals( target ) );
		}

		public void cancel()
		{
			this.doEpilogue = false;
			this.complete( null );
			this.doEpilogue = true;
		}
	}

	protected abstract class DoubleInterruptibleAnimation extends DoubleAnimation
	{
		private boolean doEpilogue = true;
		private boolean isActive = true;
		private double target;

		public DoubleInterruptibleAnimation( Number duration, edu.cmu.cs.dennisc.animation.Style style, Double d0, Double d1 )
		{
			super( duration, style, d0, d1 );
			this.isActive = true;
			this.target = d1;
		}

		@Override
		protected void epilogue()
		{
			if( this.doEpilogue )
			{
				super.epilogue();
			}
			this.isActive = false;
			this.target = -1;
		}

		public boolean isActive()
		{
			return this.isActive;
		}

		public double getTarget()
		{
			return this.target;
		}

		public boolean matchesTarget( double target )
		{
			return this.target == target;
			//			return edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon(this.target, target);
		}

		public void cancel()
		{
			this.doEpilogue = false;
			this.complete( null );
			this.doEpilogue = true;
		}
	}

	protected class ScaleChangeListener implements PropertyListener
	{
		public void propertyChanged( PropertyEvent e ) {
			ManipulationHandle3D.this.setScale( ManipulationHandle3D.this.getObjectScale() );
			ManipulationHandle3D.this.resizeToObject();
			ManipulationHandle3D.this.positionRelativeToObject();
		}

		public void propertyChanging( PropertyEvent e ) {
		}
	}

	protected ScaleChangeListener scaleListener = new ScaleChangeListener();

	protected AbsoluteTransformationListener absoluteTransformationListener = new AbsoluteTransformationListener()
	{
		public void absoluteTransformationChanged(
				AbsoluteTransformationEvent absoluteTransformationEvent )
		{
			ManipulationHandle3D.this.updateCameraRelativeOpacity();
		}
	};

	private Scalable getScalable( AbstractTransformable object ) {
		Scalable scalable = null;
		if( object instanceof Scalable ) {
			scalable = (Scalable)object;
		}
		else if( object != null ) {
			scalable = object.getBonusDataFor( Scalable.KEY );
		}
		return scalable;
	}

	/**
	 * @param manipulatedObject the manipulatedObject to set
	 */
	public void setManipulatedObject( AbstractTransformable manipulatedObjectIn ) {
		if( this.manipulatedObject != null )
		{
			Scalable s = getScalable( this.manipulatedObject );
			if( s != null ) {
				s.removeScaleListener( this.scaleListener );
			}
		}
		if( this.manipulatedObject != manipulatedObjectIn )
		{
			this.manipulatedObject = manipulatedObjectIn;
			this.criteriaManager.setTargetTransformable( this.manipulatedObject );
			if( this.manipulatedObject != null )
			{
				this.setParent( this.manipulatedObject );
				//				this.setHandleShowing(true);
			}
			else
			{
				//				this.setHandleShowing(false);
			}
			this.setScale( this.getObjectScale() );
		}
		if( this.manipulatedObject != null )
		{
			Scalable s = getScalable( this.manipulatedObject );
			if( s != null ) {
				s.addScaleListener( this.scaleListener );
			}
		}
		this.resizeToObject();
	}

	public void setSelectedObject( AbstractTransformable selectedObject ) {
		this.setManipulatedObject( selectedObject );
	}

	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;
	}

	public void setDragAdapterAndAddHandle( AbstractDragAdapter dragAdapter ) {
		this.setDragAdapter( dragAdapter );
		if( this.dragAdapter != null )
		{
			this.dragAdapter.addHandle( this );
		}
	}

	public void updateCameraRelativeOpacity()
	{
		if( ManipulationHandle3D.this.dragAdapter != null )
		{
			AbstractCamera activeCamera = ManipulationHandle3D.this.dragAdapter.getActiveCamera();
			if( activeCamera instanceof SymmetricPerspectiveCamera )
			{
				Point3 cameraLocation = ( (SymmetricPerspectiveCamera)activeCamera ).getAbsoluteTransformation().translation;
				ManipulationHandle3D.this.setCameraPosition( cameraLocation );
			}
		}
	}

	@Override
	public abstract ManipulationHandle3D clone();

	protected abstract void setScale( double scale );

	protected void initFromHandle( ManipulationHandle3D handle )
	{
		this.manipulatedObject = handle.manipulatedObject;
		this.state = new HandleState( handle.state );
		this.handleSet.clear();
		this.handleSet.addSet( handle.handleSet );
		this.localTransformation.setValue( new edu.cmu.cs.dennisc.math.AffineMatrix4x4( handle.localTransformation.getValue() ) );
		this.animator = handle.animator;
		this.criteriaManager = handle.criteriaManager;
		this.handleManager = handle.handleManager;
		this.manipulation = handle.manipulation;
	}

	public ManipulationHandle3D()
	{
		sgVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		setCurrentColorInternal();
		sgVisual.setParent( this );
		this.setName( this.getClass().getSimpleName() );
		this.putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.PickType.THREE_D_HANDLE.pickHint() );
		this.addAbsoluteTransformationListener( this.absoluteTransformationListener );
	}

	protected void setCurrentColorInternal() {
		HandleRenderState renderState = HandleRenderState.getStateForHandle( this );
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor( renderState ) );
		sgFrontFacingAppearance.opacity.setValue( new Float( this.getDesiredOpacity( renderState ) ) );
	}

	protected void initializeAppearance()
	{
		HandleRenderState renderState = HandleRenderState.getStateForHandle( this );
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor( renderState ) );
		sgFrontFacingAppearance.opacity.setValue( new Float( this.getDesiredOpacity( renderState ) ) );
	}

	protected void setTransformableScale( AbstractTransformable t, edu.cmu.cs.dennisc.math.Matrix3x3 scaleMatrix )
	{
		Visual objectVisual = getSGVisualForTransformable( t );
		objectVisual.scale.setValue( scaleMatrix );
	}

	protected Visual getSGVisualForTransformable( AbstractTransformable object )
	{
		if( object == null )
		{
			return null;
		}
		for( int i = 0; i < object.getComponentCount(); i++ )
		{
			Component c = object.getComponentAt( i );
			if( c instanceof Visual )
			{
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

	public void addToSet( HandleSet set )
	{
		this.handleSet.addSet( set );
	}

	public HandleSet getHandleSet()
	{
		return this.handleSet;
	}

	public boolean isAlwaysVisible()
	{
		return false;
	}

	public void addToGroup( HandleSet.HandleGroup group )
	{
		this.handleSet.addGroup( group );
	}

	public void addToGroups( HandleSet.HandleGroup... groups )
	{
		this.handleSet.addGroups( groups );
	}

	public boolean isMemberOf( HandleSet set )
	{
		return this.handleSet.intersects( set );
	}

	public boolean isMemberOf( HandleSet.HandleGroup group )
	{
		return this.handleSet.get( group.ordinal() );
	}

	public void setAnimator( Animator animator )
	{
		assert animator != null;
		this.animator = animator;
	}

	public Visual getSGVisual() {
		return sgVisual;
	}

	public SimpleAppearance getSGFrontFacingAppearance() {
		return sgFrontFacingAppearance;
	}

	public AbstractTransformable getManipulatedObject()
	{
		return this.manipulatedObject;
		//		return (Transformable)this.getParent();
	}

	@Override
	public void setName( String name ) {
		super.setName( name );
		sgVisual.setName( name + ".sgVisual" );
		sgFrontFacingAppearance.setName( name + ".sgFrontFacingAppearance" );
	}

	public ReferenceFrame getReferenceFrame()
	{
		return this.getParentTransformable();
	}

	public abstract ReferenceFrame getSnapReferenceFrame();

	abstract public void positionRelativeToObject();

	abstract public void resizeToObject();

	protected float getOpacity()
	{
		return this.sgFrontFacingAppearance.opacity.getValue();
	}

	protected void setOpacity( float opacity )
	{
		this.sgFrontFacingAppearance.opacity.setValue( opacity );
	}

	protected void setColor( Color4f color )
	{
		this.sgFrontFacingAppearance.diffuseColor.setValue( color );
	}

	protected void animateToOpacity( double targetOpacity )
	{
		double currentOpacity = this.getOpacity();
		//Check to see if the animation is going to get us to the desired value
		if( ( this.opacityAnimation != null ) && this.opacityAnimation.isActive() && this.opacityAnimation.matchesTarget( targetOpacity ) )
		{
			return;
		}
		//Stop any existing animation
		if( ( this.opacityAnimation != null ) && this.opacityAnimation.isActive() )
		{
			this.opacityAnimation.cancel();
		}
		//The animation is not going to get us to the desired value, so see if we're already there
		//		if (edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon(currentOpacity, targetOpacity))
		if( currentOpacity == targetOpacity )
		{
			return;
		}
		//Make a new animation and launch it
		this.opacityAnimation = new DoubleInterruptibleAnimation( ANIMATION_DURATION, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_ABRUPTLY_AND_END_GENTLY, currentOpacity, targetOpacity )
		{
			@Override
			protected void updateValue( Double v )
			{
				ManipulationHandle3D.this.setOpacity( v.floatValue() );
			}
		};
		this.animator.invokeLater( this.opacityAnimation, null );
	}

	protected void animateToColor( Color4f targetColor )
	{
		Color4f currentColor = this.sgFrontFacingAppearance.diffuseColor.getValue();
		//Check to see if the animation is going to get us to the desired value
		if( ( this.colorAnimation != null ) && this.colorAnimation.isActive() && this.colorAnimation.matchesTarget( targetColor ) )
		{
			return;
		}
		//Stop any existing animation
		if( ( this.colorAnimation != null ) && this.colorAnimation.isActive() )
		{
			this.colorAnimation.cancel();
		}
		//The animation is not going to get us to the desired value, so see if we're already there
		if( currentColor.equals( targetColor ) )
		{
			return;
		}
		//Make a new animation and launch it
		this.colorAnimation = new Color4fInterruptibleAnimation( ANIMATION_DURATION, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_ABRUPTLY_AND_END_GENTLY, currentColor, targetColor )
		{
			@Override
			protected void updateValue( Color4f v )
			{
				ManipulationHandle3D.this.setColor( v );

			}
		};
		this.animator.invokeLater( this.colorAnimation, null );
	}

	protected void updateVisibleState( HandleRenderState renderState )
	{
		if( ( this.animator == null ) || ( this.getParentTransformable() == null ) )
		{
			//			PrintUtilities.println("Early exit: animator = "+this.animator+", manipulated object = "+this.manipulatedObject);
			return;
		}

		double targetOpacity = this.isRenderable() ? this.getDesiredOpacity( renderState ) : 0.0;
		//		PrintUtilities.println(this.getClass().getSimpleName()+":"+this.hashCode()+" target opacity: "+targetOpacity+", isRenderable? "+this.isRenderable()+", desiredOpacity = "+this.getDesiredOpacity(renderState)+" is showing? "+this.sgVisual.isShowing.getValue());
		this.animateToOpacity( targetOpacity );
		Color4f targetColor = this.getDesiredColor( renderState );
		this.animateToColor( targetColor );

	}

	public boolean matches( ManipulationEvent event )
	{
		return this.criteriaManager.matches( event );
	}

	public void addCondition( ManipulationEventCriteria condition )
	{
		this.criteriaManager.addCondition( condition );
	}

	public void removeCondition( ManipulationEventCriteria condition )
	{
		this.criteriaManager.removeCondition( condition );
	}

	public void activate( ManipulationEvent event )
	{
		this.setHandleActive( true );
	}

	public void deactivate( ManipulationEvent event )
	{
		this.setHandleActive( false );
	}

	protected Color4f getBaseColor()
	{
		return Color4f.YELLOW;
	}

	protected Color4f getDesiredColor( HandleRenderState renderState )
	{
		Color4f baseColor = this.getBaseColor();
		switch( renderState )
		{
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

	public void setCameraPosition( Point3 cameraPosition )
	{
		this.setCameraRelativeOpacity( this.calculateCameraRelativeOpacity( cameraPosition ) );
	}

	public float calculateCameraRelativeOpacity( Point3 cameraPosition )
	{
		if( ( this.getParentTransformable() != null ) && ( cameraPosition != null ) )
		{
			Point3 handlePosition = this.getParentTransformable().getAbsoluteTransformation().translation;
			double distance = Point3.calculateDistanceBetween( cameraPosition, handlePosition );
			if( distance < .2 )
			{
				return 0.0f;
			}
			else if( distance < .5 )
			{
				return (float)( ( distance - .2f ) / ( .5 - .2 ) );
			}
		}
		return 1;
	}

	public void setCameraRelativeOpacity( float cameraRelativeOpacity )
	{
		if( this.cameraRelativeOpacity != cameraRelativeOpacity )
		{
			this.cameraRelativeOpacity = cameraRelativeOpacity;
			this.setOpacity( (float)this.getDesiredOpacity( HandleRenderState.getStateForHandle( this ) ) );
		}
	}

	protected double getDesiredOpacity( HandleRenderState renderState )
	{
		//		PrintUtilities.println(this.getClass().getSimpleName()+":"+this.hashCode()+" camera opacity: "+this.cameraRelativeOpacity);
		switch( renderState )
		{
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

	public AffineMatrix4x4 getTransformationForAxis( Vector3 axis )
	{
		double upDot = Vector3.calculateDotProduct( axis, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 transform = new AffineMatrix4x4();
		if( Math.abs( upDot ) != 1.0d )
		{
			Vector3 rightAxis = Vector3.createCrossProduct( axis, Vector3.accessPositiveYAxis() );
			rightAxis.normalize();
			Vector3 upAxis = axis;
			Vector3 backwardAxis = Vector3.createCrossProduct( rightAxis, upAxis );
			backwardAxis.normalize();
			transform.orientation.set( rightAxis, upAxis, backwardAxis );
		}
		else if( upDot == -1.0d )
		{
			transform.applyRotationAboutXAxis( new AngleInRadians( Math.PI ) );
		}
		return transform;
	}

	public void setManipulation( AbstractManipulator manipulation )
	{
		this.manipulation = manipulation;
		if( this.manipulation != null )
		{
			this.setPickable( true );
		}
	}

	public AbstractManipulator getManipulation( InputState input )
	{
		return this.manipulation;
	}

	public void setHandleManager( HandleManager handleManager )
	{
		this.handleManager = handleManager;
	}

	public HandleManager getHandleManager()
	{
		return this.handleManager;
	}

	public HandleState getHandleStateCopy()
	{
		return new HandleState( this.state );
	}

	public boolean isRenderable() {
		if( this.manipulatedObject == null )
		{
			return false;
		}
		if( this.isAlwaysVisible() )
		{
			return true;
		}
		return this.state.shouldRender();
	}

	protected double getObjectScale()
	{
		if( this.getParentTransformable() == null )
		{
			return 1.0d;
		}
		final double VOLUME_NORMALIZER = 1d;
		AxisAlignedBox bbox = this.getManipulatedObjectBox();
		if( ( bbox == null ) || bbox.isNaN() )
		{
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
		if( scale < .25d )
		{
			scale = .25d;
		}
		if( scale > 2.0d )
		{
			scale = 2.0d;
		}
		return scale;

	}

	protected edu.cmu.cs.dennisc.scenegraph.AbstractTransformable getParentTransformable()
	{
		Composite parent = this.getParent();
		if( parent instanceof edu.cmu.cs.dennisc.scenegraph.AbstractTransformable )
		{
			return (edu.cmu.cs.dennisc.scenegraph.AbstractTransformable)parent;
		}
		if( parent != null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Unknown parent type for handle: " + parent );
		}
		return null;
	}

	protected AxisAlignedBox getManipulatedObjectBox()
	{
		edu.cmu.cs.dennisc.scenegraph.AbstractTransformable parent = this.getParentTransformable();
		AxisAlignedBox boundingBox = BoundingBoxUtilities.getSGTransformableScaledBBox( parent );
		if( boundingBox == null )
		{
			boundingBox = new AxisAlignedBox( new Point3( -1, 0, -1 ), new Point3( 1, 1, 1 ) );
		}
		return boundingBox;
	}

	public void setPickable( boolean isPickable )
	{
		this.isPickable = isPickable;
	}

	public boolean isPickable()
	{
		if( this.isPickable )
		{
			return this.state.isVisible();
		}
		return false;
	}

	public void setHandleActive( boolean active )
	{
		this.state.setActive( active );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );

	}

	public void setHandleRollover( boolean rollover ) {
		this.state.setRollover( rollover );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );

	}

	public boolean isHandleVisible()
	{
		return this.state.isVisible() || this.isAlwaysVisible();
	}

	public void setHandleVisible( boolean visible )
	{
		this.state.setVisible( visible );
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}

	public void setVisualsShowing( boolean showing )
	{
		this.getSGVisual().isShowing.setValue( showing );
	}

	public PickHint getPickHint()
	{
		return PickHint.PickType.THREE_D_HANDLE.pickHint();
	}
}
