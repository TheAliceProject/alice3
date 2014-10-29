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

package edu.cmu.cs.dennisc.renderer.gl;

import edu.cmu.cs.dennisc.renderer.gl.adapters.AbstractCameraAdapter;
import edu.cmu.cs.dennisc.renderer.gl.adapters.FrustumPerspectiveCameraAdapter;
import edu.cmu.cs.dennisc.renderer.gl.adapters.OrthographicCameraAdapter;
import edu.cmu.cs.dennisc.renderer.gl.adapters.SymmetricPerspectiveCameraAdapter;

//class TextureGraphicsCommit {
//	private TextureAdapter m_textureAdapter;
//	private java.awt.Graphics2D m_g;
//	private int m_x;
//	private int m_y;
//	private int m_width;
//	private int m_height;
//
//	public TextureGraphicsCommit( TextureAdapter textureAdapter, java.awt.Graphics2D g, int x, int y, int width, int height ) {
//		m_textureAdapter = textureAdapter;
//		m_g = g;
//		m_x = x;
//		m_y = y;
//		m_width = width;
//		m_height = height;
//	}
//	public void commit() {
//		m_textureAdapter.commitGraphics( m_g, m_x, m_y, m_width, m_height );
//	}
//}

/**
 * @author Dennis Cosgrove
 */
abstract class GlrRenderTarget extends edu.cmu.cs.dennisc.pattern.DefaultReleasable implements edu.cmu.cs.dennisc.renderer.RenderTarget {
	private static java.awt.Rectangle s_actualViewportBufferForReuse = new java.awt.Rectangle();
	private static java.awt.Dimension s_sizeBufferForReuse = new java.awt.Dimension();

	private GLEventAdapter m_glEventAdapter = new GLEventAdapter( this );

	private String m_description = new String();
	private java.util.Vector<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> m_cameras = new java.util.Vector<edu.cmu.cs.dennisc.scenegraph.AbstractCamera>();
	private java.util.Vector<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> m_lookingGlassListeners = new java.util.Vector<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener>();

	protected abstract javax.media.opengl.GLAutoDrawable getGLAutoDrawable();

	private GlrRenderer m_lookingGlassFactory;

	private boolean m_isRenderingEnabled = true;

	private final SynchronousPicker synchronousPicker = new SynchronousPicker( this );
	private final SynchronousImageCapturer synchronousImageCapturer = new SynchronousImageCapturer( this );

	private final GlrAsynchronousPicker glrAsynchronousPicker = new GlrAsynchronousPicker( this );
	private final GlrAsynchronousImageCapturer glrAsynchronousImageCapturer = new GlrAsynchronousImageCapturer( this );

	protected GlrRenderTarget( GlrRenderer lookingGlassFactory ) {
		m_lookingGlassFactory = lookingGlassFactory;
	}

	/*package-private*/GLEventAdapter getGlEventAdapter() {
		return m_glEventAdapter;
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.RenderFactory getRenderFactory() {
		return m_lookingGlassFactory;
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.SynchronousPicker getSynchronousPicker() {
		return this.synchronousPicker;
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.SynchronousImageCapturer getSynchronousImageCapturer() {
		return this.synchronousImageCapturer;
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.AsynchronousPicker getAsynchronousPicker() {
		return this.glrAsynchronousPicker;
	}

	@Override
	public edu.cmu.cs.dennisc.renderer.AsynchronousImageCapturer getAsynchronousImageCapturer() {
		return this.glrAsynchronousImageCapturer;
	}

	//	private com.sun.opengl.util.j2d.Overlay m_glOverlay;
	//	public boolean isContentFromPreviousValid() {
	//		if( m_glOverlay != null ) {
	//			return m_glOverlay.contentsLost() == false;
	//		} else {
	//			return false;
	//		}
	//	}
	//	public java.awt.Graphics2D createGraphics() {
	//		if( m_glOverlay != null ) {
	//			//pass
	//		} else {
	//			m_glOverlay = new com.sun.opengl.util.j2d.Overlay( m_lookingGlass.getGLAutoDrawable() );
	//		}
	//		return m_glOverlay.createGraphics();
	//	}
	//	public void beginGraphics2DRendering() {
	//		assert m_glOverlay != null;
	//		m_glOverlay.beginRendering();
	//	}
	//	public void endGraphics2DRendering() {
	//		assert m_glOverlay != null;
	//		m_glOverlay.endRendering();
	//
	//		m_glOverlay.drawAll();
	//		m_glOverlay.sync( 0, 0, m_lookingGlass.getWidth(), m_lookingGlass.getHeight() );
	//
	//		m_isFlushNecessary = true;
	//	}
	//
	//	private boolean m_isFlushNecessary = false;
	//
	//	/*package-private*/ void flushIfNecessary() {
	//		if( m_glOverlay != null ) {
	//			if( m_isFlushNecessary ) {
	////				m_glOverlay.drawAll();
	////				m_glOverlay.sync( 0, 0, m_lookingGlass.getWidth(), m_lookingGlass.getHeight() );
	//				m_isFlushNecessary = false;
	//			}
	//		}
	//	}

	//	/*package-private*/ void commitAnyPendingChanges() {
	//		synchronized( m_pendingTextureGraphicsCommits ) {
	//			for( TextureGraphicsCommit textureGraphicsCommit : m_pendingTextureGraphicsCommits ) {
	//				textureGraphicsCommit.commit();
	//			}
	//			m_pendingTextureGraphicsCommits.clear();
	//		}
	//	}

	/* package-private */void fireInitialized( edu.cmu.cs.dennisc.renderer.event.RenderTargetInitializeEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.initialized( e );
			}
		}
	}

	/* package-private */void fireCleared( edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.cleared( e );
			}
		}
	}

	/* package-private */void fireRendered( edu.cmu.cs.dennisc.renderer.event.RenderTargetRenderEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.rendered( e );
			}
		}
	}

	/* package-private */void fireResized( edu.cmu.cs.dennisc.renderer.event.RenderTargetResizeEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.resized( e );
			}
		}
	}

	/* package-private */void fireDisplayChanged( edu.cmu.cs.dennisc.renderer.event.RenderTargetDisplayChangeEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.displayChanged( e );
			}
		}
	}

	@Override
	public String getDescription() {
		return m_description;
	}

	@Override
	public void setDescription( String description ) {
		m_description = description;
	}

	@Override
	public void addSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		assert camera != null;
		synchronized( m_cameras ) {
			m_cameras.add( camera );
		}
		if( m_glEventAdapter.isListening() ) {
			//pass
		} else {
			javax.media.opengl.GLAutoDrawable glAutoDrawable = this.getGLAutoDrawable();
			m_glEventAdapter.startListening( glAutoDrawable );
		}
	}

	@Override
	public void removeSgCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		assert camera != null;
		synchronized( m_cameras ) {
			m_cameras.remove( camera );
		}
		if( m_glEventAdapter.isListening() ) {
			if( m_cameras.isEmpty() ) {
				m_glEventAdapter.stopListening( getGLAutoDrawable() );
			}
		}
	}

	@Override
	public void clearSgCameras() {
		if( m_cameras.size() > 0 ) {
			synchronized( m_cameras ) {
				m_cameras.clear();
			}
		}
		if( m_glEventAdapter.isListening() ) {
			m_glEventAdapter.stopListening( getGLAutoDrawable() );
		}
	}

	@Override
	public int getSgCameraCount() {
		return m_cameras.size();
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSgCameraAt( int index ) {
		return m_cameras.elementAt( index );
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> getSgCameras() {
		return java.util.Collections.unmodifiableList( m_cameras );
	}

	@Override
	public Iterable<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> accessSgCameras() {
		synchronized( m_cameras ) {
			return m_cameras;
		}
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		synchronized( m_cameras ) {
			for( int i = ( m_cameras.size() - 1 ); i >= 0; i-- ) {
				edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCameraI = m_cameras.elementAt( i );
				synchronized( s_actualViewportBufferForReuse ) {
					getActualViewport( s_actualViewportBufferForReuse, sgCameraI );
					if( s_actualViewportBufferForReuse.contains( xPixel, yPixel ) ) {
						return sgCameraI;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void addRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		synchronized( m_lookingGlassListeners ) {
			m_lookingGlassListeners.add( listener );
		}
	}

	@Override
	public void removeRenderTargetListener( edu.cmu.cs.dennisc.renderer.event.RenderTargetListener listener ) {
		synchronized( m_lookingGlassListeners ) {
			m_lookingGlassListeners.remove( listener );
		}
	}

	@Override
	public java.util.List<edu.cmu.cs.dennisc.renderer.event.RenderTargetListener> getRenderTargetListeners() {
		return java.util.Collections.unmodifiableList( m_lookingGlassListeners );
	}

	protected abstract java.awt.Dimension getSize( java.awt.Dimension rv );

	@Override
	public final java.awt.Dimension getSize() {
		return getSize( new java.awt.Dimension() );

	}

	@Override
	public final int getWidth() {
		synchronized( s_sizeBufferForReuse ) {
			getSize( s_sizeBufferForReuse );
			return s_sizeBufferForReuse.width;
		}
	}

	@Override
	public final int getHeight() {
		synchronized( s_sizeBufferForReuse ) {
			getSize( s_sizeBufferForReuse );
			return s_sizeBufferForReuse.height;
		}
	}

	@Override
	public java.awt.Rectangle getSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( camera );
		return cameraAdapter.getSpecifiedViewport();
	}

	@Override
	public void setSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, java.awt.Rectangle viewport ) {
		AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( camera );
		cameraAdapter.setSpecifiedViewport( viewport );
	}

	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter ) {
		return cameraAdapter.getActualViewport( rv, getWidth(), getHeight() );
	}

	@Override
	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		return getActualViewport( rv, AdapterFactory.getAdapterFor( camera ) );
	}

	@Override
	public final java.awt.Rectangle getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		return getActualViewport( new java.awt.Rectangle(), camera );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( camera );
			getActualViewport( s_actualViewportBufferForReuse, cameraAdapter );
			return cameraAdapter.getActualProjectionMatrix( rv, s_actualViewportBufferForReuse );
		}
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		return getActualProjectionMatrix( new edu.cmu.cs.dennisc.math.Matrix4x4(), camera );
	}

	private edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			OrthographicCameraAdapter orthographicCameraAdapter = AdapterFactory.getAdapterFor( orthographicCamera );
			getActualViewport( s_actualViewportBufferForReuse, orthographicCameraAdapter );
			return orthographicCameraAdapter.getActualPicturePlane( rv, s_actualViewportBufferForReuse );
		}
	}

	@Override
	public final edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera ) {
		return getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), orthographicCamera );
	}

	private edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera frustumPerspectiveCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			FrustumPerspectiveCameraAdapter frustumPerspectiveCameraAdapter = AdapterFactory.getAdapterFor( frustumPerspectiveCamera );
			getActualViewport( s_actualViewportBufferForReuse, frustumPerspectiveCameraAdapter );
			return frustumPerspectiveCameraAdapter.getActualPicturePlane( rv, s_actualViewportBufferForReuse );
		}
	}

	@Override
	public final edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera frustumPerspectiveCamera ) {
		return getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), frustumPerspectiveCamera );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera symmetricPerspectiveCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			SymmetricPerspectiveCameraAdapter symmetricPerspectiveCameraAdapter = AdapterFactory.getAdapterFor( symmetricPerspectiveCamera );
			getActualViewport( s_actualViewportBufferForReuse, symmetricPerspectiveCameraAdapter );
			return symmetricPerspectiveCameraAdapter.getActualHorizontalViewingAngle( s_actualViewportBufferForReuse );
		}
	}

	@Override
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera symmetricPerspectiveCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			SymmetricPerspectiveCameraAdapter symmetricPerspectiveCameraAdapter = AdapterFactory.getAdapterFor( symmetricPerspectiveCamera );
			getActualViewport( s_actualViewportBufferForReuse, symmetricPerspectiveCameraAdapter );
			return symmetricPerspectiveCameraAdapter.getActualVerticalViewingAngle( s_actualViewportBufferForReuse );
		}
	}

	private edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( sgCamera != null ) {
			synchronized( s_actualViewportBufferForReuse ) {
				AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );
				getActualViewport( s_actualViewportBufferForReuse, cameraAdapter );
				//			double halfWidth = s_actualViewportBufferForReuse.width / 2.0;
				//			double halfHeight = s_actualViewportBufferForReuse.height / 2.0;
				//			double xInPlane = (xPixel + 0.5 - halfWidth) / halfWidth;
				//			double yInPlane = -(yPixel + 0.5 - halfHeight) / halfHeight;
				cameraAdapter.getRayAtPixel( rv, xPixel, yPixel, s_actualViewportBufferForReuse );
			}
		} else {
			rv.setNaN();
		}
		//		java.awt.Rectangle viewport = getActualViewport( camera );
		//		double halfWidth = viewport.width / 2.0;
		//		double halfHeight = viewport.height / 2.0;
		//		double x = (xPixel + 0.5 - halfWidth) / halfWidth;
		//		double y = -(yPixel + 0.5 - halfHeight) / halfHeight;
		//
		//		edu.cmu.cs.dennisc.math.Matrix4d inverseProjection = getActualProjectionMatrix( camera );
		//		inverseProjection.invert();
		//
		//		edu.cmu.cs.dennisc.math.Point3d origin = new edu.cmu.cs.dennisc.math.Point3d(
		//				inverseProjection.backward.x / inverseProjection.backward.w,
		//				inverseProjection.backward.y / inverseProjection.backward.w,
		//				inverseProjection.backward.z / inverseProjection.backward.w
		//		);
		//
		//		edu.cmu.cs.dennisc.math.Vector4d qs = new edu.cmu.cs.dennisc.math.Vector4d( x, y, 0, 1 );
		//		edu.cmu.cs.dennisc.math.Vector4d qw = edu.cmu.cs.dennisc.math.LinearAlgebra.multiply( qs, inverseProjection );
		//
		//		edu.cmu.cs.dennisc.math.Vector3d direction = new edu.cmu.cs.dennisc.math.Vector3d(
		//				qw.x * inverseProjection.backward.w - qw.w * inverseProjection.backward.x,
		//				qw.y * inverseProjection.backward.w - qw.w * inverseProjection.backward.y,
		//				qw.z * inverseProjection.backward.w - qw.w * inverseProjection.backward.z
		//		);
		//		direction.normalize();
		//
		//		rv.setOrigin( origin );
		//		rv.setDirection( direction );
		//		return rv;
		return rv;
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return getRayAtPixel( new edu.cmu.cs.dennisc.math.Ray(), xPixel, yPixel, sgCamera );
	}

	private edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel ) {
		return getRayAtPixel( rv, xPixel, yPixel, getCameraAtPixel( xPixel, yPixel ) );
	}

	@Override
	public final edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel ) {
		return getRayAtPixel( new edu.cmu.cs.dennisc.math.Ray(), xPixel, yPixel );
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

	public double[] getActualPlane( double[] rv, java.awt.Dimension size, edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera ) {
		throw new RuntimeException( "todo" );
		//		OrthographicCameraAdapter orthographicCameraAdapter = ElementAdapter.getAdapterFor( orthographicCamera );
		//		return orthographicCameraAdapter.getActualPlane( rv, size );
	}

	public double[] getActualPlane( double[] rv, java.awt.Dimension size, edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera perspectiveCamera ) {
		throw new RuntimeException( "todo" );
		//		PerspectiveCameraAdapter perspectiveCameraAdapter = ElementAdapter.getAdapterFor( perspectiveCamera );
		//		return perspectiveCameraAdapter.getActualPlane( rv, size );
	}

	@Override
	public boolean isRenderingEnabled() {
		return m_isRenderingEnabled;
	}

	protected abstract void repaintIfAppropriate();

	@Override
	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		if( m_isRenderingEnabled != isRenderingEnabled ) {
			m_isRenderingEnabled = isRenderingEnabled;
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

	@Override
	public void forgetAllCachedItems() {
		if( m_glEventAdapter != null ) {
			m_glEventAdapter.forgetAllCachedItems();
		}
	}

	@Override
	public void clearUnusedTextures() {
		if( m_glEventAdapter != null ) {
			m_glEventAdapter.clearUnusedTextures();
		}
	}
}
