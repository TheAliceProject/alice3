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
import org.alice.interact.ColorTargetBasedAnimation;
import org.alice.interact.DoubleTargetBasedAnimation;
import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.event.EventCriteriaManager;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventCriteria;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.handle.HandleState;
import org.alice.interact.manipulator.AbstractManipulator;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
//import org.alice.apis.moveandturn.Model;

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle3D extends Transformable implements ManipulationHandle, ManipulationListener, Cloneable{

	protected Visual sgVisual = new Visual();
	protected SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	protected Transformable manipulatedObject;
	protected Animator animator;
	private EventCriteriaManager criteriaManager = new EventCriteriaManager();
	
	protected HandleState state = new HandleState();
	protected HandleManager handleManager = null;
	protected HandleSet handleSet = new HandleSet();
	
	protected DoubleTargetBasedAnimation opacityAnimation;
	protected ColorTargetBasedAnimation colorAnimation;
	
	protected AbstractManipulator manipulation = null;
	protected AbstractDragAdapter dragAdapter = null;
	protected boolean isPickable = false; //This is false until a manipulation is set on the handle
	
	protected class ScaleChangeListener implements PropertyListener
	{
		public void propertyChanged( PropertyEvent e ) {
			ManipulationHandle3D.this.setScale( ManipulationHandle3D.this.getObjectScale() );
			ManipulationHandle3D.this.positionRelativeToObject();
		}

		public void propertyChanging( PropertyEvent e ) {
		}
	}
	
	protected ScaleChangeListener scaleListener = new ScaleChangeListener();
	
	/**
	 * @param manipulatedObject the manipulatedObject to set
	 */
	public void setManipulatedObject( Transformable manipulatedObjectIn ) {
//		String manipulatedObjectString = "null";
//		if (manipulatedObjectIn != null)
//		{
//			manipulatedObjectString = manipulatedObjectIn.getName();
//		}
//		String currentObjectString = "null";
//		if (this.manipulatedObject != null)
//		{
//			currentObjectString = this.manipulatedObject.getName();
//		}
//		System.out.println("trying to set manipulated object from "+currentObjectString+" to "+manipulatedObjectString);
		if (this.manipulatedObject != null)
		{
			Visual visualElement = this.getSGVisualForTransformable( this.manipulatedObject );
			if (visualElement != null)
			{
				visualElement.scale.removePropertyListener( this.scaleListener );
			}
		}
		if (this.manipulatedObject != manipulatedObjectIn)
		{
			this.manipulatedObject = manipulatedObjectIn;
			this.setParent( this.manipulatedObject );
			this.setScale( this.getObjectScale() );
		}
		if (this.manipulatedObject != null)
		{
			Visual visualElement = this.getSGVisualForTransformable( this.manipulatedObject );
			if (visualElement != null)
			{
				visualElement.scale.addPropertyListener( this.scaleListener );
			}
		}
	}
	
	public void setSelectedObject( Transformable selectedObject ) {
		this.setManipulatedObject( selectedObject );
	}
	
	public void setDragAdapter( AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;	
		if (this.dragAdapter != null)
		{
			this.dragAdapter.addHandle( this );
		}
	}
	
	
	@Override
	public abstract ManipulationHandle3D clone();
	
	protected abstract void setScale(double scale);
	
	protected void initFromHandle( ManipulationHandle3D handle )
	{
		this.manipulatedObject = handle.manipulatedObject;
		this.state = new HandleState(handle.state);
		this.handleSet.clear();
		this.handleSet.addSet( handle.handleSet );
		this.localTransformation.setValue( new edu.cmu.cs.dennisc.math.AffineMatrix4x4(handle.localTransformation.getValue()) );
		this.animator = handle.animator;
		this.criteriaManager = handle.criteriaManager;
		this.handleManager = handle.handleManager;
		this.manipulation = handle.manipulation;
	}

	public ManipulationHandle3D( )
	{
		HandleRenderState renderState = HandleRenderState.getStateForHandle( this );
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor(renderState) );
		sgFrontFacingAppearance.opacity.setValue( new Float(this.getDesiredOpacity(renderState)) );
		sgVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		sgVisual.setParent( this );
		this.setParent( null );
		this.putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.THREE_D_HANDLES );
	}

	protected void initializeAppearance()
	{
		HandleRenderState renderState = HandleRenderState.getStateForHandle( this );
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor(renderState) );
		sgFrontFacingAppearance.opacity.setValue( new Float(this.getDesiredOpacity(renderState)) );
	}
	
	protected edu.cmu.cs.dennisc.math.Matrix3x3 getTransformableScale( Transformable t )
	{
		edu.cmu.cs.dennisc.math.Matrix3x3 returnScale;
		Visual objectVisual = getSGVisualForTransformable( t );
		if (objectVisual != null)
		{
			returnScale = new edu.cmu.cs.dennisc.math.Matrix3x3();
			returnScale.setValue( objectVisual.scale.getValue() );
		}
		else
		{
			returnScale = edu.cmu.cs.dennisc.math.ScaleUtilities.newScaleMatrix3d( 1.0d, 1.0d, 1.0d );
		}
		return returnScale;
		
	}
	
	protected void setTransformableScale( Transformable t, edu.cmu.cs.dennisc.math.Matrix3x3 scaleMatrix )
	{
		Visual objectVisual = getSGVisualForTransformable( t );
		objectVisual.scale.setValue( scaleMatrix );
	}
	
	protected Visual getSGVisualForTransformable( Transformable object )
	{
		if (object == null)
		{
			return null;
		}
		for (int i=0; i<object.getComponentCount(); i++)
		{
			Component c = object.getComponentAt( i );
			if (c instanceof Visual)
			{
				return (Visual)c;
			}
		}
		return null;
	}
	
	protected void createAnimations()
	{
		this.opacityAnimation  = new DoubleTargetBasedAnimation( new Double(sgFrontFacingAppearance.opacity.getValue()) ){
			@Override
			protected void updateValue( Double value ) {
				if (ManipulationHandle3D.this.manipulatedObject != null)
				{
					ManipulationHandle3D.this.sgFrontFacingAppearance.opacity.setValue( value.floatValue() );
				}
			}
		};
		this.colorAnimation  = new ColorTargetBasedAnimation( sgFrontFacingAppearance.diffuseColor.getValue() ){
			@Override
			protected void updateValue( Color4f value ) {
				if (ManipulationHandle3D.this.manipulatedObject != null)
				{
					ManipulationHandle3D.this.sgFrontFacingAppearance.diffuseColor.setValue( value );
				}
			}
		};
		this.animator.addFrameObserver( this.opacityAnimation );
		this.animator.addFrameObserver( this.colorAnimation );
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
	
	public void addToGroups( HandleSet.HandleGroup...groups )
	{
		this.handleSet.addGroups( groups );
	}
	
	public boolean isMemberOf( HandleSet set)
	{
		return this.handleSet.intersects( set );
	}
	
	public boolean isMemberOf( HandleSet.HandleGroup group)
	{
		return this.handleSet.get( group.getIndex() );
	}
	
	public void setAnimator( Animator animator )
	{
		assert animator != null;
		this.animator = animator;
		this.createAnimations();
	}
	
	public Visual getSGVisual() {
		return sgVisual;
	}
	public SingleAppearance getSGFrontFacingAppearance() {
		return sgFrontFacingAppearance;
	}
	
	public Transformable getManipulatedObject()
	{
		return (Transformable)this.getParent();
	}
	
	@Override
	public void setName( String name ) {
		super.setName( name );
		sgVisual.setName( name + ".sgVisual" );
		sgFrontFacingAppearance.setName( name + ".sgFrontFacingAppearance" );
	}
	
	public ReferenceFrame getReferenceFrame()
	{
		return this.getManipulatedObject();
	}
	
	public abstract ReferenceFrame getSnapReferenceFrame();
	
	abstract public void positionRelativeToObject();
	abstract public void resizeToObject();
	
	protected void updateVisibleState(HandleRenderState renderState)
	{
		if (this.opacityAnimation != null)
		{		
			this.opacityAnimation.setTarget( this.getDesiredOpacity(renderState) );
		}
		if (this.colorAnimation != null)
		{
			this.colorAnimation.setTarget( this.getDesiredColor(renderState) );
		}
	}
	
	public boolean matches(ManipulationEvent event)
	{
		return this.criteriaManager.matches( event );
	}
	
	public void addCondition(ManipulationEventCriteria condition)
	{
		this.criteriaManager.addCondition( condition );
	}
	
	public void removeCondition(ManipulationEventCriteria condition)
	{
		this.criteriaManager.removeCondition( condition );
	}
	
	public void activate(ManipulationEvent event)
	{
		this.setHandleActive( true );
	}
	
	public void deactivate(ManipulationEvent event)
	{
		this.setHandleActive( false );
	}
	
	protected Color4f getBaseColor()
	{
		return Color4f.YELLOW;
	}
	
	protected Color4f getDesiredColor(HandleRenderState renderState)
	{
		Color4f baseColor = this.getBaseColor();
		switch (renderState)
		{
		case NOT_VISIBLE : return baseColor;
		case VISIBLE_BUT_SIBLING_IS_ACTIVE : return baseColor;
		case VISIBLE_AND_ACTIVE : return baseColor;
		case VISIBLE_AND_ROLLOVER : return baseColor;
		case JUST_VISIBLE : return baseColor;
		default : return baseColor;
		}
	}
	
	protected double getDesiredOpacity(HandleRenderState renderState)
	{
		
		switch (renderState)
		{
		case NOT_VISIBLE : return 0.0d;
		case VISIBLE_BUT_SIBLING_IS_ACTIVE : return .5d;
		case VISIBLE_AND_ACTIVE : return 1.0d;
		case VISIBLE_AND_ROLLOVER : return .75d;
		case JUST_VISIBLE : return .6d;
		default : return 0.0d;
		}
	}
	
	public AffineMatrix4x4 getTransformationForAxis( Vector3 axis )
	{
		double upDot = Vector3.calculateDotProduct( axis, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 transform = new AffineMatrix4x4();
		if ( Math.abs( upDot ) != 1.0d )
		{
			Vector3 rightAxis = Vector3.createCrossProduct( axis, Vector3.accessPositiveYAxis() );
			rightAxis.normalize();
			Vector3 upAxis = axis;
			Vector3 backwardAxis = Vector3.createCrossProduct( rightAxis, upAxis );
			backwardAxis.normalize();
			transform.orientation.set( rightAxis, upAxis, backwardAxis );
		}
		else if (upDot == -1.0d)
		{
			transform.applyRotationAboutXAxis( new AngleInRadians(Math.PI));
		}
		return transform;
	}
	
	public void setManipulation(AbstractManipulator manipulation)
	{
		this.manipulation = manipulation;
		if (this.manipulation != null)
		{
			this.setPickable( true );
		}
	}
	
	public AbstractManipulator getManipulation( InputState input )
	{
		return this.manipulation;
	}

	public void setHandleManager(HandleManager handleManager)
	{
		this.handleManager = handleManager;
	}
	
	public HandleManager getHandleManager()
	{
		return this.handleManager;
	}

	public HandleState getHandleStateCopy()
	{
		return new HandleState(this.state);
	}
	
	public boolean isRenderable() {
		if (this.isAlwaysVisible())
		{
			return true;
		}
		return this.state.shouldRender();
	}

	protected double getObjectScale()
	{
		if (this.manipulatedObject == null)
		{
			return 1.0d;
		}
		final double VOLUME_NORMALIZER = 1d;
		AxisAlignedBox bbox = this.getManipulatedObjectBox();
		if (bbox == null)
		{
			return 1.0d;
		}
		Point3 max = bbox.getMaximum();
		max.y = 0d;
		Point3 min = bbox.getMinimum();
		min.y = 0d;
		double volume = Point3.createSubtraction( max, min ).calculateMagnitude();
		double scale = volume / VOLUME_NORMALIZER;
		if (scale < .25d)
		{
			scale = .25d;
		}
		if (scale > 2.0d)
		{
			scale = 2.0d;
		}
		return scale;
		
	}
	
	protected AxisAlignedBox getManipulatedObjectBox()
	{
		AxisAlignedBox boundingBox = null;
		if (this.manipulatedObject != null)
		{
			Object bbox = this.manipulatedObject.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY );
			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				boundingBox = new AxisAlignedBox((edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox);
				if (boundingBox.isNaN())
				{
					boundingBox = null;
				}
			}
		}
		if (boundingBox == null)
		{
			boundingBox = new AxisAlignedBox(new Point3(-1, 0, -1), new Point3(1, 1, 1));
		}
		if (boundingBox != null)
		{
			boundingBox.scale( this.getTransformableScale( this.manipulatedObject ) );
		}
		return boundingBox;
	}
	
	public void setPickable(boolean isPickable)
	{
		this.isPickable = isPickable;
	}
	
	public boolean isPickable()
	{
		if (this.isPickable)
		{
			return this.state.isVisible();
		}
		return false;
	}
	
	public void setHandleActive( boolean active ) {
//		if (active && this.manipulatedObject == null)
//		{
//			PrintUtilities.println("HANDLES ARE NOT ASSIGNED AND BEING SET TO VISIBLE");
////			return;
//		}
		this.state.setActive(active);
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
		
	}

	public void setHandleRollover( boolean rollover ) {
		this.state.setRollover(rollover);
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
		
	}

	public void setHandleVisible( boolean visible ) {
//		if (visible && this.manipulatedObject == null)
//		{
//			PrintUtilities.println("HANDLES ARE NOT ASSIGNED AND BEING SET TO VISIBLE");
////			return;
//		}
		this.state.setVisible(visible);
		this.updateVisibleState( HandleRenderState.getStateForHandle( this ) );
	}
	
	public PickHint getPickHint()
	{
		return PickCondition.getPickType( this );
	}
}
