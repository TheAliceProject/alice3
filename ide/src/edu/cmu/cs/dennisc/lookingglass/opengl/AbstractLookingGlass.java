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

package edu.cmu.cs.dennisc.lookingglass.opengl;

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
abstract class AbstractLookingGlass extends edu.cmu.cs.dennisc.pattern.DefaultReleasable implements edu.cmu.cs.dennisc.lookingglass.LookingGlass {
	private static java.awt.Rectangle s_actualViewportBufferForReuse = new java.awt.Rectangle();
	private static java.awt.Dimension s_sizeBufferForReuse = new java.awt.Dimension();

	private GLEventAdapter m_glEventAdapter = new GLEventAdapter( this );

	private String m_description = new String();
	private java.util.Vector< edu.cmu.cs.dennisc.scenegraph.AbstractCamera > m_cameras = new java.util.Vector< edu.cmu.cs.dennisc.scenegraph.AbstractCamera >();
	private java.util.Vector< edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener > m_lookingGlassListeners = new java.util.Vector< edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener >();

	protected abstract javax.media.opengl.GLAutoDrawable getGLAutoDrawable();

	private LookingGlassFactory m_lookingGlassFactory;

	private boolean m_isRenderingEnabled = true;

	private final Picker picker = new Picker( this );
	
	protected AbstractLookingGlass( LookingGlassFactory lookingGlassFactory ) {
		m_lookingGlassFactory = lookingGlassFactory;
	}

	public edu.cmu.cs.dennisc.lookingglass.LookingGlassFactory getLookingGlassFactory() {
		return m_lookingGlassFactory;
	}
	
	public edu.cmu.cs.dennisc.lookingglass.Picker getPicker() {
		return this.picker;
	}
	
	//private java.util.List< TextureGraphicsCommit > m_pendingTextureGraphicsCommits = new java.util.LinkedList< TextureGraphicsCommit >();

	public java.awt.Graphics2D createGraphics( edu.cmu.cs.dennisc.texture.Texture texture ) {
		TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter = AdapterFactory.getAdapterFor( texture );
		return textureAdapter.createGraphics();
	}
	public void commitGraphics( edu.cmu.cs.dennisc.texture.Texture texture, java.awt.Graphics2D g, int x, int y, int width, int height ) {
		TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter = AdapterFactory.getAdapterFor( texture );
		textureAdapter.commitGraphics( g, x, y, width, height );
		//		synchronized( m_pendingTextureGraphicsCommits ) {
		//			m_pendingTextureGraphicsCommits.add( new TextureGraphicsCommit( textureAdapter, g, x, y, width, height ) );
		//		}
	}
	public void commitGraphics( edu.cmu.cs.dennisc.texture.Texture sgTexture, java.awt.Graphics2D g ) {
		commitGraphics( sgTexture, g, 0, 0, sgTexture.getWidth(), sgTexture.getHeight() );
	}
	public java.awt.Image getImage( edu.cmu.cs.dennisc.texture.Texture texture ) {
		TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter = AdapterFactory.getAdapterFor( texture );
		return textureAdapter.getImage();
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

	/*package-private*/void fireInitialized( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.initialized( e );
			}
		}
	}
	/*package-private*/void fireCleared( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.cleared( e );
			}
		}
	}
	/*package-private*/void fireRendered( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.rendered( e );
			}
		}
	}
	/*package-private*/void fireResized( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.resized( e );
			}
		}
	}
	/*package-private*/void fireDisplayChanged( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent e ) {
		synchronized( m_lookingGlassListeners ) {
			for( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener : m_lookingGlassListeners ) {
				lookingGlassListener.displayChanged( e );
			}
		}
	}

	public String getDescription() {
		return m_description;
	}
	public void setDescription( String description ) {
		m_description = description;
	}
	public void addCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
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
	public void removeCamera( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
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
	public void clearCameras() {
		if( m_cameras.size() > 0 ) {
			synchronized( m_cameras ) {
				m_cameras.clear();
			}
		}
		if( m_glEventAdapter.isListening() ) {
			m_glEventAdapter.stopListening( getGLAutoDrawable() );
		}
	}
	public int getCameraCount() {
		return m_cameras.size();
	}
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAt( int index ) {
		return m_cameras.elementAt( index );
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] getCameras( edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] rv ) {
		synchronized( m_cameras ) {
			m_cameras.copyInto( rv );
		}
		return rv;
	}
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] getCameras() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera[] rv = new edu.cmu.cs.dennisc.scenegraph.AbstractCamera[ getCameraCount() ];
		return getCameras( rv );
	}
	public Iterable< edu.cmu.cs.dennisc.scenegraph.AbstractCamera > accessCameras() {
		synchronized( m_cameras ) {
			return m_cameras;
		}
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getCameraAtPixel( int xPixel, int yPixel ) {
		synchronized( m_cameras ) {
			for( int i = (m_cameras.size() - 1); i >= 0; i-- ) {
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

	public void addLookingGlassListener( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener ) {
		synchronized( m_lookingGlassListeners ) {
			m_lookingGlassListeners.add( lookingGlassListener );
		}
	}
	public void removeLookingGlassListener( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener lookingGlassListener ) {
		synchronized( m_lookingGlassListeners ) {
			m_lookingGlassListeners.remove( lookingGlassListener );
		}
	}
	public int getLookingGlassListenerCount() {
		return m_lookingGlassListeners.size();
	}
	public edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener getLookingGlassListenerAt( int index ) {
		return m_lookingGlassListeners.elementAt( index );
	}
	public edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] getLookingGlassListeners( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] rv ) {
		synchronized( m_lookingGlassListeners ) {
			m_lookingGlassListeners.copyInto( rv );
		}
		return rv;
	}
	public edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] getLookingGlassListeners() {
		edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[] rv = new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener[ getLookingGlassListenerCount() ];
		return getLookingGlassListeners( rv );
	}
	public Iterable< edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener > accessLookingGlassListeners() {
		synchronized( m_lookingGlassListeners ) {
			return m_lookingGlassListeners;
		}
	}

	public final java.awt.Dimension getSize() {
		return getSize( new java.awt.Dimension() );

	}
	public final int getWidth() {
		synchronized( s_sizeBufferForReuse ) {
			getSize( s_sizeBufferForReuse );
			return s_sizeBufferForReuse.width;
		}
	}
	public final int getHeight() {
		synchronized( s_sizeBufferForReuse ) {
			getSize( s_sizeBufferForReuse );
			return s_sizeBufferForReuse.height;
		}
	}

	public java.awt.Rectangle getSpecifiedViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( camera );
		return cameraAdapter.getSpecifiedViewport();
	}
	public void setSpecifiedViewport( java.awt.Rectangle viewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( camera );
		cameraAdapter.setSpecifiedViewport( viewport );
	}

	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter ) {
		return cameraAdapter.getActualViewport( rv, getWidth(), getHeight() );
	}
	public java.awt.Rectangle getActualViewport( java.awt.Rectangle rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		return getActualViewport( rv, AdapterFactory.getAdapterFor( camera ) );
	}
	public final java.awt.Rectangle getActualViewport( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		return getActualViewport( new java.awt.Rectangle(), camera );
	}

	public edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.math.Matrix4x4 rv, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( camera );
			getActualViewport( s_actualViewportBufferForReuse, cameraAdapter );
			return cameraAdapter.getActualProjectionMatrix( rv, s_actualViewportBufferForReuse );
		}
	}
	public final edu.cmu.cs.dennisc.math.Matrix4x4 getActualProjectionMatrix( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		return getActualProjectionMatrix( new edu.cmu.cs.dennisc.math.Matrix4x4(), camera );
	}

	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			OrthographicCameraAdapter orthographicCameraAdapter = AdapterFactory.getAdapterFor( orthographicCamera );
			getActualViewport( s_actualViewportBufferForReuse, orthographicCameraAdapter );
			return orthographicCameraAdapter.getActualPicturePlane( rv, s_actualViewportBufferForReuse );
		}
	}
	public final edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.OrthographicCamera orthographicCamera ) {
		return getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), orthographicCamera );
	}
	public edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.math.ClippedZPlane rv, edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera frustumPerspectiveCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			FrustumPerspectiveCameraAdapter frustumPerspectiveCameraAdapter = AdapterFactory.getAdapterFor( frustumPerspectiveCamera );
			getActualViewport( s_actualViewportBufferForReuse, frustumPerspectiveCameraAdapter );
			return frustumPerspectiveCameraAdapter.getActualPicturePlane( rv, s_actualViewportBufferForReuse );
		}
	}
	public final edu.cmu.cs.dennisc.math.ClippedZPlane getActualPicturePlane( edu.cmu.cs.dennisc.scenegraph.FrustumPerspectiveCamera frustumPerspectiveCamera ) {
		return getActualPicturePlane( new edu.cmu.cs.dennisc.math.ClippedZPlane(), frustumPerspectiveCamera );
	}
	public edu.cmu.cs.dennisc.math.Angle getActualHorizontalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera symmetricPerspectiveCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			SymmetricPerspectiveCameraAdapter symmetricPerspectiveCameraAdapter = AdapterFactory.getAdapterFor( symmetricPerspectiveCamera );
			getActualViewport( s_actualViewportBufferForReuse, symmetricPerspectiveCameraAdapter );
			return symmetricPerspectiveCameraAdapter.getActualHorizontalViewingAngle( s_actualViewportBufferForReuse );
		}
	}
	public edu.cmu.cs.dennisc.math.Angle getActualVerticalViewingAngle( edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera symmetricPerspectiveCamera ) {
		synchronized( s_actualViewportBufferForReuse ) {
			SymmetricPerspectiveCameraAdapter symmetricPerspectiveCameraAdapter = AdapterFactory.getAdapterFor( symmetricPerspectiveCamera );
			getActualViewport( s_actualViewportBufferForReuse, symmetricPerspectiveCameraAdapter );
			return symmetricPerspectiveCameraAdapter.getActualVerticalViewingAngle( s_actualViewportBufferForReuse );
		}
	}

	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		if( sgCamera != null ) {
			synchronized( s_actualViewportBufferForReuse ) {
				AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );
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
	public final edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel, edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera ) {
		return getRayAtPixel( new edu.cmu.cs.dennisc.math.Ray(), xPixel, yPixel, sgCamera );
	}

	public edu.cmu.cs.dennisc.math.Ray getRayAtPixel( edu.cmu.cs.dennisc.math.Ray rv, int xPixel, int yPixel ) {
		return getRayAtPixel( rv, xPixel, yPixel, getCameraAtPixel( xPixel, yPixel ) );
	}
	public final edu.cmu.cs.dennisc.math.Ray getRayAtPixel( int xPixel, int yPixel ) {
		return getRayAtPixel( new edu.cmu.cs.dennisc.math.Ray(), xPixel, yPixel );
	}

	public boolean isLetterboxedAsOpposedToDistorted( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( camera );
		return cameraAdapter.isLetterboxedAsOpposedToDistorted();
	}
	public void setIsLetterboxedAsOpposedToDistorted( boolean isLetterboxedAsOpposedToDistorted, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera ) {
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( camera );
		cameraAdapter.setIsLetterboxedAsOpposedToDistorted( isLetterboxedAsOpposedToDistorted );
	}

	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer() {
		return m_glEventAdapter.createBufferedImageForUseAsColorBuffer();
	}
	public java.awt.image.BufferedImage getColorBuffer( java.awt.image.BufferedImage rv ) {
		return m_glEventAdapter.getColorBuffer( rv );
	}
	public final java.awt.image.BufferedImage getColorBuffer() {
		return getColorBuffer( createBufferedImageForUseAsColorBuffer() );
	}
	
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer() {
		return m_glEventAdapter.createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer();
	}
	public java.nio.FloatBuffer createFloatBufferForUseAsDepthBuffer() {
		return m_glEventAdapter.createFloatBufferForUseAsDepthBuffer();
	}
	public java.nio.FloatBuffer getDepthBuffer( java.nio.FloatBuffer rv ) {
		return m_glEventAdapter.getDepthBuffer( rv );
	}
	public final java.nio.FloatBuffer getDepthBuffer() {
		return getDepthBuffer( createFloatBufferForUseAsDepthBuffer() );
	}

	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer ) {
		return m_glEventAdapter.getColorBufferWithTransparencyBasedOnDepthBuffer( rv, depthBuffer );
	}
	public final java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer() {
		return getColorBufferWithTransparencyBasedOnDepthBuffer( createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer(), createFloatBufferForUseAsDepthBuffer() );
	}
	


//	/*package-private*/ edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
//		//javax.media.opengl.GLCapabilitiesImmutable glCapabilities = this.getGLAutoDrawable().getChosenGLCapabilities();
//		//PrintUtilities.println( "glCapabilities", glCapabilities );
//		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getCameraAtPixel( xPixel, yPixel );
//		edu.cmu.cs.dennisc.lookingglass.PickResult rv;
//		if( sgCamera != null ) {
//			this.m_lookingGlassFactory.acquireRenderingLock();
//			try {
//				rv = m_glEventAdapter.pickFrontMost( sgCamera, xPixel, yPixel, pickSubElementPolicy == edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED, pickObserver );
//			} finally {
//				this.m_lookingGlassFactory.releaseRenderingLock();
//			}
//		} else {
//			rv = new edu.cmu.cs.dennisc.lookingglass.PickResult();
//			rv.setNaN();
//		}
//		return rv;
//	}
//	/*package-private*/ java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
//		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getCameraAtPixel( xPixel, yPixel );
//		if( sgCamera != null ) {
//			this.m_lookingGlassFactory.acquireRenderingLock();
//			try {
//				return m_glEventAdapter.pickAll( sgCamera, xPixel, yPixel, pickSubElementPolicy == edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED, pickObserver );
//			} finally {
//				this.m_lookingGlassFactory.releaseRenderingLock();
//			}
//		} else {
//			return new java.util.LinkedList< edu.cmu.cs.dennisc.lookingglass.PickResult >();
//		}
//	}

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

	public boolean isRenderingEnabled() {
		return m_isRenderingEnabled;
	}

	protected abstract void repaintIfAppropriate();
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
	
	public void forgetAllCachedItems() {
		if( m_glEventAdapter != null ) {
			m_glEventAdapter.forgetAllCachedItems();
		}
	}
}
