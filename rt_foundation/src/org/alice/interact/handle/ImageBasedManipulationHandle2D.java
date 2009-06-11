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
package org.alice.interact.handle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.alice.interact.event.ManipulationEvent;

import edu.cmu.cs.dennisc.math.Vector2;

/**
 * @author David Culyba
 */
public abstract class ImageBasedManipulationHandle2D extends ManipulationHandle2D {
	
	protected interface ImageState
	{
		public ImageIcon getIcon();
	}

	protected ImageState currentState;
	protected BufferedImage imageMask;
	
	public ImageBasedManipulationHandle2D()
	{
		this.setImageMask();
		this.setStateBasedOnManipulationStatus();
		Dimension size = new Dimension(this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
		this.setSize( size );
		this.setMinimumSize( size );
		this.setPreferredSize( size );
	}
	
	public Color getColor( int x, int y)
	{
		if (super.contains( x, y ))
		{
			if (this.imageMask != null)
			{
				int colorInt = this.imageMask.getRGB( x, y );
				Color color = new Color(colorInt, true);
				return color;
			}
		}
		return null;
	}
	
	abstract protected void setImageMask(); 
	
	@Override
	public boolean contains( int x, int y ) {
		
		
		Color color = this.getColor( x, y );
		if (color != null)
		{
			return color.getAlpha() != 0;
		}
		else
		{
			return super.contains( x, y );
		}
	}
	
	@Override
	public Vector2 getCenter()
	{
		Dimension ourSize = new Dimension();
		if (this.currentState != null)
		{
			ourSize.width = this.currentState.getIcon().getIconWidth();
			ourSize.height = this.currentState.getIcon().getIconHeight();
		}
		return new Vector2(ourSize.width*.5d, ourSize.height*.5d);
	}
	
	@Override
	protected void updateVisibleState( HandleRenderState renderState )
	{
		this.setStateBasedOnManipulationStatus();
	}
	
	private void setStateBasedOnManipulationStatus()
	{
		ImageState newState = this.getStateForManipulationStatus();
		if (newState != this.currentState)
		{
			this.currentState = newState;
			this.setIcon( this.currentState.getIcon() );
		}
	}
	
	@Override
	public void activate( ManipulationEvent event ) {
		this.setManipulationState( event, true );
		this.setStateBasedOnManipulationStatus();
	}

	@Override
	public void deactivate( ManipulationEvent event ) {
		this.setManipulationState( event, false );
		this.setStateBasedOnManipulationStatus();
	}
	
	protected abstract ImageState getStateForManipulationStatus();
	protected abstract void setManipulationState(ManipulationEvent event, boolean isActive);
}
