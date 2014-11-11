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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory;

/**
 * @author Dennis Cosgrove
 */
public class LayerAdapter extends ElementAdapter<edu.cmu.cs.dennisc.scenegraph.Layer> {
	private java.util.List<GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic>> activeGraphicAdapters = java.util.Collections.synchronizedList( new java.util.LinkedList<GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic>>() );
	private java.util.List<GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic>> forgetGraphicAdapters = java.util.Collections.synchronizedList( new java.util.LinkedList<GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic>>() );

	private void handleGraphicAdded( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter ) {
		synchronized( this.activeGraphicAdapters ) {
			this.activeGraphicAdapters.add( graphicAdapter );
		}
	}

	private void handleGraphicRemoved( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter ) {
		synchronized( this.forgetGraphicAdapters ) {
			this.forgetGraphicAdapters.add( graphicAdapter );
		}
	}

	public static void handleGraphicAdded( edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent e ) {
		LayerAdapter layerAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		layerAdapter.handleGraphicAdded( graphicAdapter );
	}

	public static void handleGraphicRemoved( edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent e ) {
		LayerAdapter layerAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		layerAdapter.handleGraphicRemoved( graphicAdapter );
	}

	@Override
	public void initialize( edu.cmu.cs.dennisc.scenegraph.Layer sgLayer ) {
		super.initialize( sgLayer );
		for( edu.cmu.cs.dennisc.scenegraph.Graphic sgGraphic : sgLayer.getGraphics() ) {
			handleGraphicAdded( AdapterFactory.getAdapterFor( sgGraphic ) );
		}
	}

	/* package-private */void render( edu.cmu.cs.dennisc.render.Graphics2D g2, edu.cmu.cs.dennisc.render.RenderTarget renderTarget, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		synchronized( this.forgetGraphicAdapters ) {
			for( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter : this.forgetGraphicAdapters ) {
				graphicAdapter.forget( g2 );
				synchronized( this.activeGraphicAdapters ) {
					this.activeGraphicAdapters.remove( graphicAdapter );
				}
			}
			this.forgetGraphicAdapters.clear();
		}
		synchronized( this.activeGraphicAdapters ) {
			for( GraphicAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Graphic> graphicAdapter : this.activeGraphicAdapters ) {
				graphicAdapter.render( g2, renderTarget, actualViewport, camera );
			}
		}
	}

}
