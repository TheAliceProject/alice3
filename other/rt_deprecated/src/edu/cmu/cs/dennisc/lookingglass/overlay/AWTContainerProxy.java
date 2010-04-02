/*
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
package edu.cmu.cs.dennisc.lookingglass.overlay;

/**
 * @author Dennis Cosgrove
 */
class ComponentTexture extends edu.cmu.cs.dennisc.texture.CustomTexture {
	private java.awt.Component component;

	public ComponentTexture( java.awt.Component component ) {
		this.component = component;
	}
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		throw new RuntimeException();
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		throw new RuntimeException();
	}
	@Override
	public boolean isPotentiallyAlphaBlended() {
		return true;
	}
	@Override
	public boolean isAnimated() {
		return true;
	}
	@Override
	public edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy getMipMapGenerationPolicy() {
		//return edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy.PAINT_ONLY_HIGHEST_LEVEL_THEN_SCALE_REMAINING;
		return edu.cmu.cs.dennisc.texture.MipMapGenerationPolicy.PAINT_EACH_INDIVIDUAL_LEVEL;
	}
	@Override
	public int getWidth() {
		return this.component.getWidth();
	}
	@Override
	public int getHeight() {
		return this.component.getHeight();
	}
	@Override
	public boolean isMipMappingDesired() {
		return false;
	}
	@Override
	public void paint( java.awt.Graphics2D g2, int width, int height ) {
		this.component.print( g2 );
	}
	@Override
	public void layoutIfNecessary( java.awt.Graphics2D g2 ) {
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AWTContainerProxy implements java.awt.event.ComponentListener {
	private java.awt.Container container;
	private java.util.Map< java.awt.Component, ComponentTexture > mapComponentToTexture = new java.util.HashMap< java.awt.Component, ComponentTexture >();
	public AWTContainerProxy( java.awt.Container container ) {
		this.container = container;
		for( java.awt.Component component : this.container.getComponents() ) {
			handleComponentAdded( component );
		}
		this.container.addContainerListener( new java.awt.event.ContainerListener() {
			public void componentAdded( java.awt.event.ContainerEvent e ) {
				AWTContainerProxy.this.handleComponentAdded( e.getChild() );
			}
			public void componentRemoved( java.awt.event.ContainerEvent e ) {
				AWTContainerProxy.this.handleComponentRemoved( e.getChild() );
			}
		} );
	}
	public void componentShown( java.awt.event.ComponentEvent e ) {
	}
	public void componentHidden( java.awt.event.ComponentEvent e ) {
	}
	public void componentMoved( java.awt.event.ComponentEvent e ) {
	}
	public void componentResized( java.awt.event.ComponentEvent e ) {
		ComponentTexture componentTexture = mapComponentToTexture.get( e.getSource() );
		if( componentTexture != null ) {
			componentTexture.fireTextureChanged();
		}
	}
	private void handleComponentAdded( java.awt.Component component ) {
		component.addComponentListener( this );
	}
	private void handleComponentRemoved( java.awt.Component component ) {
		component.removeComponentListener( this );
	}
	public void paint( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2 ) {
		java.awt.PointerInfo pointerInfo = java.awt.MouseInfo.getPointerInfo();
		java.awt.Point p = pointerInfo.getLocation();
		javax.swing.SwingUtilities.convertPointFromScreen( p, this.container );
		for( java.awt.Component component : this.container.getComponents() ) {
			if( component.getWidth() > 0 && component.getHeight() > 0 ) {
				ComponentTexture componentTexture = this.mapComponentToTexture.get( component );
				if( componentTexture != null ) {
					//pass
				} else {
					componentTexture = new ComponentTexture( component );
					g2.remember( componentTexture );
					this.mapComponentToTexture.put( component, componentTexture );
				}
				boolean isMouseWithin = component.contains( javax.swing.SwingUtilities.convertPoint( this.container, p, component ) );
				float opacity;
				if( isMouseWithin ) {
					opacity = 1.0f;
				} else {
					opacity = 0.5f;
				}
				g2.paint( componentTexture, component.getX(), component.getY(), opacity );
				//g2.forget( componentTexture );
			}
		}
	}
}
