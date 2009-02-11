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

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle extends Transformable {

	protected Visual sgVisual = new Visual();
	protected SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	protected Transformable manipulatedObject;
	protected Animator animator;
	
	protected int isVisible = 0;
	protected int isActive = 0;
	protected int isRollover = 0;
	protected int isMuted = 1;
	protected int isFaded = 0;
	
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
	
	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible != 0;
	}

	/**
	 * @param isVisible the isVisible to set
	 */
	public void setVisible( boolean isVisible ) {
		int visibleValue = isVisible ? 1 : -1;
		this.isVisible += visibleValue;
		if (this.isVisible < 0)
		{
			System.err.println("Visible went below 0: "+this.isVisible);
		}
		this.updateVisibleState();
	}

	/**
	 * @return the isFaded
	 */
	public boolean isFaded() {
		return isFaded != 0;
	}

	/**
	 * @param isFaded the isFaded to set
	 */
	public void setFaded( boolean isFaded ) {
		int fadedValue = isFaded ? 1 : -1;
		this.isFaded += fadedValue;
		if (this.isFaded < 0)
		{
			System.err.println("Faded went below 0: "+this.isFaded);
		}
		this.updateVisibleState();
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive != 0;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive( boolean isActive ) {
		int activeValue = isActive ? 1 : -1;
		this.isActive += activeValue;
		if (this.isActive < 0)
		{
			System.err.println("Active went below 0: "+this.isActive);
		}
		this.updateVisibleState();
	}

	/**
	 * @return the isRollover
	 */
	public boolean isRollover() {
		return isRollover != 0;
	}

	/**
	 * @param isRollover the isRollover to set
	 */
	public void setRollover( boolean isRollover ) {
		int rolloverValue = isRollover ? 1 : -1;
		this.isRollover += rolloverValue;
		if (this.isRollover < 0)
		{
			System.err.println("Rollover went below 0: "+this.isRollover);
		}
		this.updateVisibleState();
	}

	/**
	 * @return the isMuted
	 */
	public boolean isMuted() {
		return isMuted != 0;
	}

	/**
	 * @param isMuted the isMuted to set
	 */
	public void setMuted( boolean isMuted ) {
		int mutedValue = isMuted ? 1 : -1;
		this.isMuted += mutedValue;
		if (this.isMuted < 0)
		{
			System.err.println("Muted went below 0: "+this.isMuted);
		}	
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
		if (!this.isVisible() || this.isFaded())
		{
			return 0.0d;
		}
		else if (this.isActive()) //Visible and Active
		{
			return 1.0d;
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
			return .6d;
		}
	}
	
	public AffineMatrix4x4 getTransformationForAxis( Vector3 axis )
	{
		double upDot = Vector3.calculateDotProduct( axis, Vector3.accessPositiveYAxis() );
		AffineMatrix4x4 transform = new AffineMatrix4x4();
		if ( upDot != 1.0d)
		{
			Vector3 rightAxis = Vector3.createCrossProduct( axis, Vector3.accessPositiveYAxis() );
			Vector3 upAxis = axis;
			Vector3 backwardAxis = Vector3.createCrossProduct( rightAxis, upAxis );
			transform.orientation.set( rightAxis, upAxis, backwardAxis );
		}
		return transform;
	}

}
