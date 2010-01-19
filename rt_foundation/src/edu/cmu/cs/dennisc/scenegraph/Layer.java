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

package edu.cmu.cs.dennisc.scenegraph;

import javax.media.opengl.GL;

import edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext;
import edu.cmu.cs.dennisc.lookingglass.opengl.SceneAdapter;

public class Layer extends Element {
	private java.util.List< Graphic > graphics = java.util.Collections.synchronizedList( new java.util.LinkedList< Graphic >() );
	private java.util.List< edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener > graphicsListeners = java.util.Collections.synchronizedList( new java.util.LinkedList< edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener >() );
	public void addGraphic( Graphic graphic ) {
		graphic.setParent( this );
	}
	public void removeGraphic( Graphic graphic ) {
		if( graphic.getParent() == this ) {
			graphic.setParent( null );
		} else {
			throw new RuntimeException();
		}
	}
	public Iterable<Graphic> getGraphics() {
		return this.graphics;
	}
	
	/*package-private*/ void addGraphicAndFireListeners( Graphic graphic ) {
		this.graphics.add( graphic );
		edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent e = new edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent( this, graphic );
		for( edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener l : this.graphicsListeners ) {
			l.graphicAdded( e );
		}
	}
	/*package-private*/ void removeGraphicAndFireListeners( Graphic graphic ) {
		this.graphics.remove( graphic );
		edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent e = new edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent( this, graphic );
		for( edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener l : this.graphicsListeners ) {
			l.graphicRemoved( e );
		}
	}	
}
