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

package edu.cmu.cs.dennisc.scenegraph;

public class Layer extends Element {
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

	public java.util.Collection<Graphic> getGraphics() {
		return java.util.Collections.unmodifiableCollection( this.graphics );
	}

	public void addGraphicsListener( edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener l ) {
		this.graphicsListeners.add( l );
	}

	public void removeGraphicsListener( edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener l ) {
		this.graphicsListeners.remove( l );
	}

	public java.util.Collection<edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener> getGraphicsListeners() {
		return java.util.Collections.unmodifiableCollection( this.graphicsListeners );
	}

	/* package-private */void addGraphicAndFireListeners( Graphic graphic ) {
		this.graphics.add( graphic );
		if( this.graphicsListeners.size() > 0 ) {
			edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent e = new edu.cmu.cs.dennisc.scenegraph.event.GraphicAddedEvent( this, graphic );
			for( edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener l : this.graphicsListeners ) {
				l.graphicAdded( e );
			}
		}
	}

	/* package-private */void removeGraphicAndFireListeners( Graphic graphic ) {
		this.graphics.remove( graphic );
		if( this.graphicsListeners.size() > 0 ) {
			edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent e = new edu.cmu.cs.dennisc.scenegraph.event.GraphicRemovedEvent( this, graphic );
			for( edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener l : this.graphicsListeners ) {
				l.graphicRemoved( e );
			}
		}
	}

	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.event.GraphicsListener> graphicsListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();;
	private final java.util.List<Graphic> graphics = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
}
