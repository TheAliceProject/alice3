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

import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationListener;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle extends Transformable implements ManipulationListener, Cloneable{

	protected Visual sgVisual = new Visual();
	protected SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	protected Transformable manipulatedObject;
	protected Animator animator;
	
	protected HandleState currentState = new HandleState();
	protected HandleState previousState = new HandleState();
	
	protected java.util.BitSet groupMembership = new java.util.BitSet();
	
	
	protected DoubleTargetBasedAnimation opacityAnimation;
	protected ColorTargetBasedAnimation colorAnimation;
	
	/**
	 * @param manipulatedObject the manipulatedObject to set
	 */
	public void setManipulatedObject( Transformable manipulatedObject ) {
		if (this.manipulatedObject != manipulatedObject)
		{
			this.manipulatedObject = manipulatedObject;
			this.setParent( manipulatedObject );
		}
	}
	
	@Override
	public abstract ManipulationHandle clone();
	
	protected void initFromHandle( ManipulationHandle handle )
	{
		this.manipulatedObject = handle.manipulatedObject;
		this.currentState.setState( handle.currentState );
		this.groupMembership.clear();
		this.groupMembership.or( handle.groupMembership );
		this.localTransformation.setValue( new edu.cmu.cs.dennisc.math.AffineMatrix4x4(handle.localTransformation.getValue()) );
	}

	public ManipulationHandle( )
	{
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor() );
		sgFrontFacingAppearance.opacity.setValue( new Float(this.getDesiredOpacity()) );
		sgVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		sgVisual.setParent( this );
		this.setParent( null );
		this.putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.HANDLES );
	}

	protected void initializeAppearance()
	{
		sgFrontFacingAppearance.diffuseColor.setValue( this.getDesiredColor() );
		sgFrontFacingAppearance.opacity.setValue( new Float(this.getDesiredOpacity()) );
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
	
	protected Visual getSGVisualForTransformable( Transformable object )
	{
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
				ManipulationHandle.this.sgFrontFacingAppearance.opacity.setValue( value.floatValue() );
			}
		};
		this.colorAnimation  = new ColorTargetBasedAnimation( sgFrontFacingAppearance.diffuseColor.getValue() ){
			@Override
			protected void updateValue( Color4f value ) {
				ManipulationHandle.this.sgFrontFacingAppearance.diffuseColor.setValue( value );
			}
		};
		this.animator.invokeLater( this.opacityAnimation, null );
		this.animator.invokeLater( this.colorAnimation, null );
	}
	
	public void addToGroup( int groupIndex )
	{
		this.groupMembership.set( groupIndex );
	}
	
	public void addToGroup( HandleGroup group )
	{
		this.groupMembership.set(  group.getIndex() );
	}
	
	public void setGroupMembership( java.util.BitSet groupMembership)
	{
		this.groupMembership.clear();
		this.groupMembership.or( groupMembership );
	}
	
	public void addToGroups( HandleGroup...groups)
	{
		for (int i=0; i<groups.length; i++)
		{
			this.groupMembership.set( groups[i].getIndex() );
		}
	}
	
	public boolean isMemberOf( java.util.BitSet group)
	{
		return this.groupMembership.intersects( group );
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
	
	abstract public void positionRelativeToObject( Composite object );
	abstract public void resizeToObject( Composite object );
	
	protected void updateVisibleState()
	{
		if (this.opacityAnimation != null)
		{		
			this.opacityAnimation.setTarget( this.getDesiredOpacity() );
		}
		if (this.colorAnimation != null)
		{
			this.colorAnimation.setTarget( this.getDesiredColor() );
		}
	}
	
	public void activate(ManipulationEvent event)
	{
		if (!this.isActive())
		{
			this.previousState.setState(this.currentState);
			this.setActive(true);
			if (!this.isVisible())
			{
				this.setVisible( true );
			}
			if (this.opacityAnimation != null)
			{		
				double desiredOpacity = this.getDesiredOpacity();				
				this.opacityAnimation.setTarget( desiredOpacity );
				this.opacityAnimation.setCurrentValue( desiredOpacity );
				this.opacityAnimation.forceValueUpdate();
			}
		}
	}
	
	public void deactivate(ManipulationEvent event)
	{
		if (this.isActive())
		{
			this.setActive(false);
			this.setState(this.previousState);
			if (this.opacityAnimation != null)
			{		
				this.opacityAnimation.setTarget( this.getDesiredOpacity() );
				this.opacityAnimation.setCurrentValue( this.getDesiredOpacity() );
				this.opacityAnimation.forceValueUpdate();
			}
		}
	}
	
	public void setState( HandleState state )
	{
	
		if (this.currentState.isVisible() != state.isVisible() )
		{
			this.setVisible(state.isVisible());
		}
		if (this.currentState.isActive() != state.isActive() )
		{
			this.setActive(state.isActive());
		}
		if (this.currentState.isRollover() != state.isRollover() )
		{
			this.setRollover(state.isRollover());
		}
		if (this.currentState.isMuted() != state.isMuted() )
		{
			this.setMuted(state.isMuted());
		}
		if (this.currentState.isFaded() != state.isFaded() )
		{
			this.setFaded(state.isFaded());
		}
	}
	
	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return this.currentState.isVisible();
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible( boolean isVisible ) {	
		this.currentState.setVisible( isVisible );
		this.updateVisibleState();
	}

	/**
	 * @return the isFaded
	 */
	public boolean isFaded() {
		return this.currentState.isFaded();
	}

	/**
	 * @param isFaded the isFaded to set
	 */
	public void setFaded( boolean isFaded ) {
		boolean wasFaded = this.isFaded();
		this.currentState.setFaded( isFaded );
		if (!this.isFaded() && wasFaded)
		{
			resizeToObject( this.getManipulatedObject() );
		}
		this.updateVisibleState();
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return this.currentState.isActive();
	}
	
	/**
	 * @param isActive the isActive to set
	 */
	public void setActive( boolean isActive ) {
		this.currentState.setActive(isActive);
		this.updateVisibleState();
	}

	/**
	 * @return the isRollover
	 */
	public boolean isRollover() {
		return this.currentState.isRollover();
	}

	/**
	 * @param isRollover the isRollover to set
	 */
	public void setRollover( boolean isRollover ) {
		this.currentState.setRollover(isRollover);
		this.updateVisibleState();
	}

	/**
	 * @return the isMuted
	 */
	public boolean isMuted() {
		return this.currentState.isMuted();
	}

	/**
	 * @param isMuted the isMuted to set
	 */
	public void setMuted( boolean isMuted ) {
		this.currentState.setMuted(isMuted);
		this.updateVisibleState();
	}

	protected Color4f getBaseColor()
	{
		return Color4f.YELLOW;
	}
	
	protected Color4f getDesiredColor()
	{
		Color4f baseColor = this.getBaseColor();
		if (!this.isVisible())
		{
			return baseColor;
		}
		else if (this.isActive()) //Visible and Active
		{
			return baseColor;
		}
		else if (this.isRollover()) //Visible and Rollover
		{
			return baseColor;
		}
		else if (this.isMuted())
		{
			return baseColor;
		}
		else //Visible and not Muted, not Active, and not Rollover
		{
			return baseColor;
		}
	}
	
	protected double getDesiredOpacity()
	{
		if (this.isActive()) //Active trumps everything
		{
			return 1.0d;
		}
		else if (!this.isVisible() || (this.isFaded()))
		{
			return 0.0d;
		}
		else if (this.isRollover()) //Visible and Rollover
		{
			return .75d;
		}
		else if (this.isMuted())
		{
			return .5d;
		}
		else //Visible and not Muted, not Active, and not Rollover
		{
			return 1.0d;
		}
	}
	
	public AffineMatrix4x4 getTransformationForAxis( Vector3 axis )
	{
		double upDot = Vector3.calculateDotProduct( axis, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 transform = new AffineMatrix4x4();
		if ( Math.abs( upDot ) != 1.0d )
		{
			Vector3 rightAxis = Vector3.createCrossProduct( axis, Vector3.accessPositiveYAxis() );
			Vector3 upAxis = axis;
			Vector3 backwardAxis = Vector3.createCrossProduct( rightAxis, upAxis );
			transform.orientation.set( rightAxis, upAxis, backwardAxis );
		}
		else if (upDot == -1.0d)
		{
			transform.applyRotationAboutXAxis( new AngleInRadians(Math.PI));
		}
		return transform;
	}

}
