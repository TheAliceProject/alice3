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
package edu.cmu.cs.dennisc.renderer.gl;

import edu.cmu.cs.dennisc.lookingglass.opengl.AbstractCameraAdapter;
import edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlRenderTarget implements edu.cmu.cs.dennisc.renderer.RenderTarget {
	@Override
	public edu.cmu.cs.dennisc.renderer.RenderFactory getRenderFactory() {
		return GlRenderFactory.INSTANCE;
	}

	private void handleDisplay( javax.media.opengl.GLAutoDrawable glDrawable ) {
		javax.media.opengl.GL gl = glDrawable.getGL();
		javax.media.opengl.GL2 gl2 = gl.getGL2();
		rc.gl = gl2;
		for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera : this.sgCameras ) {
			edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgCamera ).performClearAndRenderOffscreen( rc, glDrawable.getWidth(), glDrawable.getHeight() );
		}
	}

	protected abstract javax.media.opengl.GLAutoDrawable getGlAutoDrawable();

	@Override
	public int getWidth() {
		return this.getGlAutoDrawable().getWidth();
	}

	@Override
	public int getHeight() {
		return this.getGlAutoDrawable().getHeight();
	}

	@Override
	public java.awt.Dimension getSize() {
		javax.media.opengl.GLAutoDrawable glAutoDrawable = this.getGlAutoDrawable();
		return new java.awt.Dimension( glAutoDrawable.getWidth(), glAutoDrawable.getHeight() );
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setDescription( String description ) {
		this.description = description;
	}

	@Override
	public void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		this.sgCameras.add( sgCamera );
		if( this.sgCameras.size() == 1 ) {
			this.getGlAutoDrawable().addGLEventListener( this.glEventListener );
		}
	}

	@Override
	public void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( ( this.sgCameras.size() == 1 ) && this.sgCameras.contains( sgCamera ) ) {
			this.getGlAutoDrawable().removeGLEventListener( this.glEventListener );
		}
		this.sgCameras.remove( sgCamera );
	}

	@Override
	public void clearSgCameras() {
		if( this.sgCameras.size() > 0 ) {
			this.getGlAutoDrawable().removeGLEventListener( this.glEventListener );
		}
		this.sgCameras.clear();
	}

	@Override
	public int getSgCameraCount() {
		return this.sgCameras.size();
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index ) {
		return this.sgCameras.get( index );
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras() {
		return java.util.Collections.unmodifiableList( this.sgCameras );
	}

	@Override
	public void addRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.listeners.remove( listener );
	}

	@Override
	public void removeRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		this.listeners.add( listener );
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> getRenderTargetListeners() {
		return java.util.Collections.unmodifiableList( this.listeners );
	}

	private java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, edu.cmu.cs.dennisc.renderer.gl.AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter ) {
		return cameraAdapter.getActualViewport( rv, getWidth(), getHeight() );
	}

	private java.awt.Rectangle getActualViewport( edu.cmu.cs.dennisc.renderer.gl.AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter ) {
		return this.getActualViewport( new java.awt.Rectangle(), cameraAdapter );
	}

	@Override
	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.getActualViewport( rv, edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgCamera ) );
	}

	@Override
	public final java.awt.Rectangle getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.getActualViewport( new java.awt.Rectangle(), sgCamera );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		edu.cmu.cs.dennisc.renderer.gl.AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgCamera );
		java.awt.Rectangle actualViewport = this.getActualViewport( cameraAdapter );
		return cameraAdapter.getActualProjectionMatrix( rv, actualViewport );
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.getActualProjectionMatrix( new edu.cmu.cs.dennisc.math.Matrix4x4(), sgCamera );
	}

	@Override
	public final edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera sgFrustumPerspectiveCamera ) {
		edu.cmu.cs.dennisc.renderer.gl.FrustumPerspectiveCameraAdapter cameraAdapter = edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgFrustumPerspectiveCamera );
		java.awt.Rectangle actualViewport = this.getActualViewport( cameraAdapter );
		return cameraAdapter.getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), actualViewport );
	}

	@Override
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera ) {
		edu.cmu.cs.dennisc.renderer.gl.OrthographicCameraAdapter cameraAdapter = edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgOrthographicCamera );
		java.awt.Rectangle actualViewport = this.getActualViewport( cameraAdapter );
		return cameraAdapter.getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), actualViewport );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		edu.cmu.cs.dennisc.renderer.gl.SymmetricPerspectiveCameraAdapter cameraAdapter = edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgSymmetricPerspectiveCamera );
		java.awt.Rectangle actualViewport = this.getActualViewport( cameraAdapter );
		return cameraAdapter.getActualHorizontalViewingAngle( actualViewport );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgSymmetricPerspectiveCamera ) {
		edu.cmu.cs.dennisc.renderer.gl.SymmetricPerspectiveCameraAdapter cameraAdapter = edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgSymmetricPerspectiveCamera );
		java.awt.Rectangle actualViewport = this.getActualViewport( cameraAdapter );
		return cameraAdapter.getActualVerticalViewingAngle( actualViewport );
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		synchronized( this.sgCameras ) {
			java.awt.Rectangle actualViewport = new java.awt.Rectangle();
			for( int i = ( this.sgCameras.size() - 1 ); i >= 0; i-- ) {
				edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCameraI = this.sgCameras.get( i );
				this.getActualViewport( actualViewport, sgCameraI );
				if( actualViewport.contains( xPixel, yPixel ) ) {
					return sgCameraI;
				}
			}
		}
		return null;
	}

	@Override
	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		edu.cmu.cs.dennisc.renderer.gl.AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = edu.cmu.cs.dennisc.renderer.gl.AdapterFactory.getAdapterFor( sgCamera );
		java.awt.Rectangle actualViewport = this.getActualViewport( cameraAdapter );
		return cameraAdapter.getRayAtPixel( new edu.cmu.cs.dennisc.math.Ray(), xPixel, yPixel, actualViewport );
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel ) {
		return this.getRayAtPixel( xPixel, yPixel, this.getCameraAtPixel( xPixel, yPixel ) );
	}

	@Override
	public java.awt.Rectangle getSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return this.mapSgCameraToViewport.get( sgCamera );
	}

	@Override
	public void setSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, java.awt.Rectangle viewport ) {
		if( viewport != null ) {
			this.mapSgCameraToViewport.put( sgCamera, viewport );
		} else {
			if( this.mapSgCameraToViewport.containsKey( sgCamera ) ) {
				this.mapSgCameraToViewport.remove( sgCamera );
			}
		}
	}

	@Override
	public boolean isLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );
		return cameraAdapter.isLetterboxedAsOpposedToDistorted();
	}

	@Override
	public void setLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, boolean isLetterboxedAsOpposedToDistorted ) {
		AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );
		cameraAdapter.setIsLetterboxedAsOpposedToDistorted( isLetterboxedAsOpposedToDistorted );
	}

	@Override
	public Iterable<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> accessSgCameras() {
		return this.sgCameras;
	}

	@Override
	public void forgetAllCachedItems() {
		throw new RuntimeException( "todo" );
	}

	@Override
	public void clearUnusedTextures() {
		throw new RuntimeException( "todo" );
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.SynchronousPicker getSynchronousPicker() {
		throw new RuntimeException( "todo" );
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.AsynchronousPicker getAsynchronousPicker() {
		throw new RuntimeException( "todo" );
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.SynchronousImageCapturer getSynchronousImageCapturer() {
		throw new RuntimeException( "todo" );
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.AsynchronousImageCapturer getAsynchronousImageCapturer() {
		throw new RuntimeException( "todo" );
	}

	@Override
	public boolean isRenderingEnabled() {
		return this.isRenderingEnabled;
	}

	@Override
	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		if( this.isRenderingEnabled != isRenderingEnabled ) {
			this.isRenderingEnabled = isRenderingEnabled;
			this.repaintIfAppropriate();
			//			//todo
			//			if( m_isRenderingEnabled ) {
			//				if( m_glEventAdapter.isListening() ) {
			//					//pass
			//				} else {
			//					m_glEventAdapter.startListening( getGLAutoDrawable() );
			//				}
			//			} else {
			//				if( m_glEventAdapter.isListening() ) {
			//					m_glEventAdapter.stopListening( getGLAutoDrawable() );
			//				} else {
			//					//pass
			//				}
			//			}
		}
	}

	protected abstract void repaintIfAppropriate();

	private final javax.media.opengl.GLEventListener glEventListener = new javax.media.opengl.GLEventListener() {
		@Override
		public void init( javax.media.opengl.GLAutoDrawable drawable ) {
		}

		@Override
		public void reshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
		}

		@Override
		public void display( javax.media.opengl.GLAutoDrawable drawable ) {
			handleDisplay( drawable );
		}

		@Override
		public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
		}
	};

	private final edu.cmu.cs.dennisc.renderer.gl.RenderContext rc = new edu.cmu.cs.dennisc.renderer.gl.RenderContext();

	private final java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> listeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> sgCameras = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.Map<edu.cmu.cs.dennisc.scenegraph.AbstractCamera, java.awt.Rectangle> mapSgCameraToViewport = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private String description;
	private boolean isRenderingEnabled = true;
}
