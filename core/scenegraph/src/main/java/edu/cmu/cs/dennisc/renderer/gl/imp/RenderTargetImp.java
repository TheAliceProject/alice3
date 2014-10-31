/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.renderer.gl.imp;

/**
 * @author Dennis Cosgrove
 */
public class RenderTargetImp {
	public RenderTargetImp( edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget glrRT ) {
		this.glrRT = glrRT;
		this.m_glEventAdapter = new GLEventAdapter( this );
	}

	public edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget getRenderTarget() {
		return this.glrRT;
	}

	public edu.cmu.cs.dennisc.renderer.SynchronousPicker getSynchronousPicker() {
		return this.synchronousPicker;
	}

	public edu.cmu.cs.dennisc.renderer.SynchronousImageCapturer getSynchronousImageCapturer() {
		return this.synchronousImageCapturer;
	}

	public edu.cmu.cs.dennisc.renderer.AsynchronousPicker getAsynchronousPicker() {
		return this.asynchronousPicker;
	}

	public edu.cmu.cs.dennisc.renderer.AsynchronousImageCapturer getAsynchronousImageCapturer() {
		return this.asynchronousImageCapturer;
	}

	public void addRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.renderTargetListeners.add( listener );
	}

	public void removeRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.renderTargetListeners.remove( listener );
	}

	public java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> getRenderTargetListeners() {
		return java.util.Collections.unmodifiableList( this.renderTargetListeners );
	}

	public void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		assert sgCamera != null : this;
		this.sgCameras.add( sgCamera );
		if( m_glEventAdapter.isListening() ) {
			//pass
		} else {
			javax.media.opengl.GLAutoDrawable glAutoDrawable = this.glrRT.getGLAutoDrawable();
			m_glEventAdapter.startListening( glAutoDrawable );
		}
	}

	public void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		assert sgCamera != null;
		this.sgCameras.remove( sgCamera );
		if( m_glEventAdapter.isListening() ) {
			if( this.sgCameras.isEmpty() ) {
				m_glEventAdapter.stopListening( this.glrRT.getGLAutoDrawable() );
			}
		}
	}

	public void clearSgCameras() {
		if( this.sgCameras.size() > 0 ) {
			this.sgCameras.clear();
		}
		if( m_glEventAdapter.isListening() ) {
			m_glEventAdapter.stopListening( this.glrRT.getGLAutoDrawable() );
		}
	}

	public int getSgCameraCount() {
		return this.sgCameras.size();
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index ) {
		return this.sgCameras.get( index );
	}

	public java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras() {
		return java.util.Collections.unmodifiableList( this.sgCameras );
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		java.util.ListIterator<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> iterator = this.sgCameras.listIterator( this.sgCameras.size() );
		while( iterator.hasPrevious() ) {
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = iterator.previous();
			synchronized( s_actualViewportBufferForReuse ) {
				this.glrRT.getActualViewport( s_actualViewportBufferForReuse, sgCamera );
				if( s_actualViewportBufferForReuse.contains( xPixel, yPixel ) ) {
					return sgCamera;
				}
			}
		}
		return null;
	}

	public void forgetAllCachedItems() {
		this.m_glEventAdapter.forgetAllCachedItems();
	}

	public void clearUnusedTextures() {
		this.m_glEventAdapter.clearUnusedTextures();
	}

	/*package-private*/java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> accessSgCameras() {
		return this.sgCameras;
	}

	/*package-private*/void addDisplayTask( DisplayTask displayTask ) {
		synchronized( this.displayTasks ) {
			this.displayTasks.add( displayTask );
		}
	}

	/*package-private*/GLEventAdapter getGlEventAdapter() {
		return m_glEventAdapter;
	}

	/* package-private */void fireInitialized( edu.cmu.cs.dennisc.renderer.event.RenderTargetInitializeEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.initialized( e );
		}
	}

	/* package-private */void fireCleared( edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.cleared( e );
		}
	}

	/* package-private */void fireRendered( edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.rendered( e );
		}
	}

	/* package-private */void fireResized( edu.cmu.cs.dennisc.renderer.event.RenderTargetResizeEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.resized( e );
		}
	}

	/* package-private */void fireDisplayChanged( edu.cmu.cs.dennisc.renderer.event.RenderTargetDisplayChangeEvent e ) {
		for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener rtListener : this.renderTargetListeners ) {
			rtListener.displayChanged( e );
		}
	}

	/*package-private*/void performDisplayTasks( javax.media.opengl.GLAutoDrawable drawable, javax.media.opengl.GL2 gl ) {
		synchronized( this.displayTasks ) {
			if( this.displayTasks.size() > 0 ) {
				for( DisplayTask displayTask : this.displayTasks ) {
					displayTask.handleDisplay( this, drawable, gl );
				}
			}
		}
	}

	private final edu.cmu.cs.dennisc.renderer.gl.GlrRenderTarget glrRT;

	private final GLEventAdapter m_glEventAdapter;
	private final SynchronousPicker synchronousPicker = new SynchronousPicker( this );
	private final SynchronousImageCapturer synchronousImageCapturer = new SynchronousImageCapturer( this );

	private final GlrAsynchronousPicker asynchronousPicker = new GlrAsynchronousPicker( this );
	private final GlrAsynchronousImageCapturer asynchronousImageCapturer = new GlrAsynchronousImageCapturer( this );

	private final java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> renderTargetListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> sgCameras = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<DisplayTask> displayTasks = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	//
	private static java.awt.Rectangle s_actualViewportBufferForReuse = new java.awt.Rectangle();
	//	private final javax.media.opengl.GLEventListener glEventListener = new javax.media.opengl.GLEventListener() {
	//		@Override
	//		public void init( javax.media.opengl.GLAutoDrawable drawable ) {
	//		}
	//
	//		@Override
	//		public void display( javax.media.opengl.GLAutoDrawable drawable ) {
	//		}
	//
	//		@Override
	//		public void reshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
	//		}
	//
	//		@Override
	//		public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
	//		}
	//	};
}
