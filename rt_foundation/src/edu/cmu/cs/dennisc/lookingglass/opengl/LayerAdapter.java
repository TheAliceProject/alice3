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

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public class LayerAdapter extends ElementAdapter< edu.cmu.cs.dennisc.scenegraph.Layer > {
	private java.util.List<GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic>> graphicAdapters = java.util.Collections.synchronizedList( new java.util.LinkedList<GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic >>());
	private void handleGraphicAdded( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic > graphicAdapter ) {
		this.graphicAdapters.add( graphicAdapter );
	}
	private void handleGraphicRemoved( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic > graphicAdapter ) {
		this.graphicAdapters.remove( graphicAdapter );
	}
	public static void handleGraphicAdded( edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent e ) {
		LayerAdapter layerAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic > childAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		layerAdapter.handleGraphicAdded( childAdapter );
	}
	public static void handleGraphicRemoved( edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent e ) {
		LayerAdapter layerAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic > childAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		layerAdapter.handleGraphicRemoved( childAdapter );
	}

	@Override
	public void initialize( edu.cmu.cs.dennisc.scenegraph.Layer sgLayer ) {
		super.initialize( sgLayer );
		for( edu.cmu.cs.dennisc.scenegraph.Graphic sgGraphic : sgLayer.getGraphics() ) {
			handleGraphicAdded( AdapterFactory.getAdapterFor( sgGraphic ) );
		}
	}

	/*package-private*/ void render( RenderContext rc, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		for( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter : this.graphicAdapters ) {
			graphicAdapter.render( rc, actualViewport, camera, this.m_element );
		}
	}
	
}
