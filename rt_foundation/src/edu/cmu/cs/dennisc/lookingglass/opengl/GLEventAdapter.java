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

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
class SelectionBufferInfo {
	private float m_zFront;
	private float m_zBack;
	private edu.cmu.cs.dennisc.scenegraph.Visual m_sgVisual;
	private boolean m_isFrontFacing;
	private edu.cmu.cs.dennisc.scenegraph.Geometry m_sgGeometry;
	private int m_subElement;

	public SelectionBufferInfo( PickContext pc, java.nio.IntBuffer intBuffer, int offset ) {
		int nameCount = intBuffer.get( offset + 0 );
		int zFrontAsInt = intBuffer.get( offset + 1 );
		int zBackAsInt = intBuffer.get( offset + 2 );

		long zFrontAsLong = zFrontAsInt;
		zFrontAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;

		m_zFront = (float)zFrontAsLong;
		m_zFront /= (float)RenderContext.MAX_UNSIGNED_INTEGER;

		long zBackAsLong = zBackAsInt;
		zBackAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;
		
//		int[] atDepth = { -1 };
//		pc.gl.glGetIntegerv( GL.GL_DEPTH_BITS, atDepth, 0 );
//		int[] atClearValue = { -1 };
//		pc.gl.glGetIntegerv( GL.GL_DEPTH_CLEAR_VALUE, atClearValue, 0 );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "SelectionBufferInfo:", atDepth[ 0 ], Long.toHexString( atClearValue[ 0 ] ), Long.toHexString( RenderContext.MAX_UNSIGNED_INTEGER ), Integer.toHexString( zFrontAsInt ), Long.toHexString( zFrontAsLong ), Integer.toHexString( zBackAsInt ), Long.toHexString( zBackAsLong )  );

		m_zBack = (float)zBackAsLong;
		m_zBack /= (float)RenderContext.MAX_UNSIGNED_INTEGER;

		if( nameCount == 4 ) {
			int key = intBuffer.get( offset + 3 );
			VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter = pc.getPickVisualAdapterForName( key );
			if( visualAdapter != null ) {
				m_sgVisual = visualAdapter.m_element;
				m_isFrontFacing = intBuffer.get( offset + 4 ) == 1;
				m_sgGeometry = m_sgVisual.geometries.getValue()[ ( intBuffer.get( offset + 5 ) ) ];
				m_subElement = intBuffer.get( offset + 6 );
			}
		}
	}

	public float getZFront() {
		return m_zFront;
	}
	public float getZBack() {
		return m_zBack;
	}

	public edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
		return m_sgVisual;
	}
	public boolean isFrontFacing() {
		return m_isFrontFacing;
	}
	public edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgGeometry;
	}
	public int getSubElement() {
		return m_subElement;
	}
}

/**
 * @author Dennis Cosgrove
 */
class GLEventAdapter implements javax.media.opengl.GLEventListener {
	private AbstractLookingGlass m_lookingGlass;
	private javax.media.opengl.GLAutoDrawable m_drawable;
	private RenderContext m_renderContext = new RenderContext();
	private PickContext m_pickContext = new PickContext();
	private int m_width;
	private int m_height;

	private PickParameters m_pickParameters = null;
	private java.awt.image.BufferedImage m_rvColorBuffer = null;
	private java.nio.FloatBuffer m_rvDepthBuffer = null;

	private static final int SELECTION_CAPACITY = 256;
	private java.nio.IntBuffer m_selectionAsIntBuffer = null;

	private boolean m_isDisplayIgnoredDueToPreviousException = false;

	private class ReusableLookingGlassRenderEvent extends edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent {
		public ReusableLookingGlassRenderEvent( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, Graphics2D g ) {
			super( lookingGlass, g );
		}
		@Override
		public boolean isReservedForReuse() {
			return true;
		}
		
		private void prologue() {
			((Graphics2D)getGraphics2D()).initialize( getTypedSource().getWidth(), getTypedSource().getHeight() );
		}
		private void epilogue() {
			getGraphics2D().dispose();
		}
	}
	private ReusableLookingGlassRenderEvent m_reusableLookingGlassRenderEvent;

	public GLEventAdapter( AbstractLookingGlass lookingGlass ) {
		m_lookingGlass = lookingGlass;
		m_reusableLookingGlassRenderEvent = new ReusableLookingGlassRenderEvent( m_lookingGlass, new Graphics2D( m_renderContext ) );

		final int SIZEOF_INT = 4;
		java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect( SIZEOF_INT * SELECTION_CAPACITY );
		byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
		m_selectionAsIntBuffer = byteBuffer.asIntBuffer();
	}

	private boolean m_isListening;

	public boolean isListening() {
		return m_isListening;
	}
	public void startListening( javax.media.opengl.GLAutoDrawable drawable ) {
		if( m_isListening ) {
			assert drawable == m_drawable;
			System.err.println( "request GLEventAdapter.startListening( drawable ) ignored; already listening." );
		} else {
			m_isListening = true;
			m_drawable = drawable;
			m_drawable.addGLEventListener( this );
		}
	}
	public void stopListening( javax.media.opengl.GLAutoDrawable drawable ) {
		assert drawable == m_drawable;
		if( m_isListening ) {
			m_isListening = false;
			drawable.removeGLEventListener( this );
		} else {
			System.err.println( "request GLEventAdapter.stopListening( drawable ) ignored; already not listening." );
		}
		m_drawable = null;
	}

//	private void paintOverlay() {
//		edu.cmu.cs.dennisc.lookingglass.Overlay overlay = m_lookingGlass.getOverlay();
//		if( overlay != null ) {
//			
//			m_renderContext.gl.glMatrixMode( GL.GL_PROJECTION );
//			m_renderContext.gl.glPushMatrix();
//			m_renderContext.gl.glLoadIdentity();
//			m_renderContext.gl.glOrtho( 0, m_lookingGlass.getWidth() - 1, m_lookingGlass.getHeight() - 1, 0, -1, 1 );
//			m_renderContext.gl.glMatrixMode( GL.GL_MODELVIEW );
//			m_renderContext.gl.glPushMatrix();
//			m_renderContext.gl.glLoadIdentity();
//
//			m_renderContext.gl.glDisable( GL.GL_DEPTH_TEST );
//			m_renderContext.gl.glDisable( GL.GL_LIGHTING );
//			m_renderContext.gl.glDisable( GL.GL_CULL_FACE );
//			m_renderContext.setDiffuseColorTextureAdapter( null );
//			m_renderContext.setBumpTextureAdapter( null );
//
//			
//			try {
//				overlay.paint( m_lookingGlass );
//				m_renderContext.gl.glFlush();
//			} finally {
//				m_renderContext.gl.glMatrixMode( GL.GL_PROJECTION );
//				m_renderContext.gl.glPopMatrix();
//				m_renderContext.gl.glMatrixMode( GL.GL_MODELVIEW );
//				m_renderContext.gl.glPopMatrix();
//			}
//		}
//	}
	
	private void performRender() {
		if( m_lookingGlass.isRenderingEnabled() ) {
			m_renderContext.actuallyForgetTexturesIfNecessary();
			m_renderContext.actuallyForgetDisplayListsIfNecessary();
			if( m_isDisplayIgnoredDueToPreviousException ) {
				//pass
			} else {
				try {
					//todo: separate clearing and rendering
					m_reusableLookingGlassRenderEvent.prologue();
					try {
						m_lookingGlass.fireCleared( m_reusableLookingGlassRenderEvent );
					} finally {
						m_reusableLookingGlassRenderEvent.epilogue();
					}
					if( m_lookingGlass.getCameraCount() > 0 ) {
						m_renderContext.initialize();
						Iterable< edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameras = m_lookingGlass.accessCameras();
						synchronized( cameras ) {
							for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera : cameras ) {
								AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapterI = AdapterFactory.getAdapterFor( camera );
								cameraAdapterI.performClearAndRenderOffscreen( m_renderContext, m_width, m_height );
							}
						}
						m_renderContext.renderLetterboxingIfNecessary( m_width, m_height );
					} else {
						m_renderContext.gl.glClearColor( 0, 0, 0, 1 );
						m_renderContext.gl.glClear( GL.GL_COLOR_BUFFER_BIT );
					}
					m_reusableLookingGlassRenderEvent.prologue();
					try {
						m_lookingGlass.fireRendered( m_reusableLookingGlassRenderEvent );
					} finally {
						m_reusableLookingGlassRenderEvent.epilogue();
					}
					if( m_rvColorBuffer != null || m_rvDepthBuffer != null ) {
						m_renderContext.captureBuffers( m_rvColorBuffer, m_rvDepthBuffer );
					}
				} catch( RuntimeException re ) {
					System.err.println( "rendering will be disabled due to exception" );
					m_isDisplayIgnoredDueToPreviousException = true;
					throw re;
				}
			}
		}
	}

	private void performPick() {
		edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver = m_pickParameters.getPickObserver();
		if( pickObserver != null ) {
			pickObserver.prePick();
			ChangeHandler.handleBufferedChanges();
		}
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = m_pickParameters.getSGCamera();
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );

		m_selectionAsIntBuffer.rewind();
		m_pickContext.gl.glSelectBuffer( SELECTION_CAPACITY, m_selectionAsIntBuffer );

		m_pickContext.gl.glRenderMode( GL.GL_SELECT );
		m_pickContext.gl.glInitNames();

		java.awt.Rectangle actualViewport = m_lookingGlass.getActualViewport( sgCamera );
		m_pickContext.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
		cameraAdapter.performPick( m_pickContext, m_pickParameters, actualViewport );
		m_pickContext.gl.glFlush();

		m_selectionAsIntBuffer.rewind();
		int length = m_pickContext.gl.glRenderMode( GL.GL_RENDER );

		//todo: invesigate negative length
		//assert length >= 0;

		if( length > 0 ) {
			SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[ length ];
			int offset = 0;
			for( int i = 0; i < length; i++ ) {
				selectionBufferInfos[ i ] = new SelectionBufferInfo( m_pickContext, m_selectionAsIntBuffer, offset );
				offset += 7;
			}

			java.util.Arrays.sort( selectionBufferInfos, new java.util.Comparator< SelectionBufferInfo >() {
				public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
					return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
				}
			} );

			for( int i=0; i<length; i++ ) {
				//todo: perform trimmed math
				
				double x = m_pickParameters.getX();
				double y = m_pickParameters.getFlippedY( actualViewport );

				edu.cmu.cs.dennisc.math.Matrix4x4 m = new edu.cmu.cs.dennisc.math.Matrix4x4();
				m.translation.set( actualViewport.width - 2*(x-actualViewport.x), actualViewport.height - 2*(y-actualViewport.y), 0, 1 );
				edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( m, actualViewport.width, actualViewport.height, 1.0 );

				edu.cmu.cs.dennisc.math.Matrix4x4 p = new edu.cmu.cs.dennisc.math.Matrix4x4();
				cameraAdapter.getActualProjectionMatrix( p, actualViewport );
				
				m.applyMultiplication( p );
				m.invert();
				
				double z = selectionBufferInfos[ i ].getZFront();
				z *= 2;
				z -= 1;

				edu.cmu.cs.dennisc.math.Vector4 v = new edu.cmu.cs.dennisc.math.Vector4( 0, 0, z, 1 );
				m.transform( v );

				edu.cmu.cs.dennisc.math.Point3 xyzInSource = new edu.cmu.cs.dennisc.math.Point3( v.x/v.w, v.y/v.w, v.z/v.w );
				m_pickParameters.addPickResult( sgCamera, selectionBufferInfos[ i ].getSGVisual(), selectionBufferInfos[ i ].isFrontFacing(), selectionBufferInfos[ i ].getSGGeometry(), selectionBufferInfos[ i ].getSubElement(), xyzInSource );
			}
		}
		if( pickObserver != null ) {
			pickObserver.postPick();
			ChangeHandler.handleBufferedChanges();
		}

	}

	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		assert sgCamera != null;
		m_drawable.setAutoSwapBufferMode( false );
		try {
			m_pickParameters = new PickParameters( m_drawable, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
			m_drawable.display();
			return m_pickParameters.accessFrontMostPickResult();
		} finally {
			m_drawable.setAutoSwapBufferMode( true );
			m_pickParameters = null;
		}
	}
	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		assert sgCamera != null;
		m_drawable.setAutoSwapBufferMode( false );
		try {
			m_pickParameters = new PickParameters( m_drawable, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
			m_drawable.display();
			return m_pickParameters.accessAllPickResults();
		} finally {
			m_drawable.setAutoSwapBufferMode( true );
			m_pickParameters = null;
		}
	}

	private java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer( int type ) {
		if( m_width > 0 && m_height > 0 ) {
			return new java.awt.image.BufferedImage( m_width, m_height, type );
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer() {
//		boolean isClearedToCreateImage;
//		if( this.m_renderContext.gl != null ) {
//			String extensions = this.m_renderContext.gl.glGetString( GL.GL_EXTENSIONS );
//			if( extensions != null ) {
//				boolean isABGRExtensionSupported = extensions.contains( "GL_EXT_abgr" );
//				if( isABGRExtensionSupported ) {
//					//pass
//				} else {
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: capturing images from gl is expected to fail since since GL_EXT_abgr not found in: " );
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "\t" + extensions );
//				}
//				isClearedToCreateImage = isABGRExtensionSupported;
//			} else {
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: capturing images from gl is expected to fail since since gl.glGetString( GL.GL_EXTENSIONS ) returns null." );
//				isClearedToCreateImage = false;
//			}
//		} else {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: opengl is not initialized yet, so we will assume the GL_EXT_abgr extension is present." );
//			isClearedToCreateImage = true;
//		}
//		
//		
//		//todo: investigate
//		if( isClearedToCreateImage ) {
//			//pass
//		} else {
//			isClearedToCreateImage = true;
//		}
//		
//		if( isClearedToCreateImage ) {
//			//todo:
//			//int type = java.awt.image.BufferedImage.TYPE_3BYTE_ABGR;
//			int type = java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
//			//int type = java.awt.image.BufferedImage.TYPE_INT_ARGB;
//			return createBufferedImageForUseAsColorBuffer( type );
//		} else {
//			return null;
//		}
		int type = java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
		return createBufferedImageForUseAsColorBuffer( type );
	}
	public java.awt.image.BufferedImage getColorBuffer( java.awt.image.BufferedImage rv ) {
		if( rv != null ) {
			m_rvColorBuffer = rv;
			m_drawable.setAutoSwapBufferMode( false );
			try {
				m_drawable.display();
			} finally {
				m_rvColorBuffer = null;
				m_drawable.setAutoSwapBufferMode( true );
			}
		}
		return rv;
	}
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer() {
		return createBufferedImageForUseAsColorBuffer( java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
	}
	public java.nio.FloatBuffer createFloatBufferForUseAsDepthBuffer() {
		return java.nio.FloatBuffer.allocate( m_width * m_height );
	}
	public java.nio.FloatBuffer getDepthBuffer( java.nio.FloatBuffer rv ) {
		m_rvDepthBuffer = rv;
		m_drawable.setAutoSwapBufferMode( false );
		try {
			m_drawable.display();
		} finally {
			m_rvDepthBuffer = null;
			m_drawable.setAutoSwapBufferMode( true );
		}
		return rv;
	}

	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer ) {
		m_rvColorBuffer = rv;
		m_rvDepthBuffer = depthBuffer;
		m_drawable.setAutoSwapBufferMode( false );
		try {
			m_drawable.display();
		} finally {
			m_rvColorBuffer = null;
			m_rvDepthBuffer = null;
			m_drawable.setAutoSwapBufferMode( true );
		}
		return rv;
	}
	
	private void initialize( javax.media.opengl.GLAutoDrawable drawable ) {
		assert drawable == m_drawable;
		GL gl = drawable.getGL();
		
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( drawable.getChosenGLCapabilities() );
		
		final boolean USE_DEBUG_GL = false;
		if( USE_DEBUG_GL ) {
			if( gl instanceof javax.media.opengl.DebugGL ) {
				// pass
			} else {
				gl = new javax.media.opengl.DebugGL( gl );
				System.out.println( "using debug gl: " + gl );
				drawable.setGL( gl );
			}
		}
		m_renderContext.setGL( gl );
		m_pickContext.setGL( gl );
		m_lookingGlass.fireInitialized( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent( m_lookingGlass, m_drawable.getWidth(), m_drawable.getHeight() ) );
	}

	//todo: investigate not being invoked
	public void init( javax.media.opengl.GLAutoDrawable drawable ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "init", drawable );
		initialize( drawable );
	}
	public void display( javax.media.opengl.GLAutoDrawable drawable ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "display:", drawable );
		assert drawable == m_drawable;
	
		//m_lookingGlass.commitAnyPendingChanges();
		//todo?
		GL gl = drawable.getGL();
		if( m_renderContext.gl != null || m_pickContext.gl != null ) {
			//pass
		} else {
			initialize( drawable );
		}
		
//		if( m_width > 0 && m_height > 0 ) {
//			//pass
//		} else {
//			m_width = drawable.getWidth();
//			m_height = drawable.getHeight();
//		}
		if( m_pickParameters != null ) {
			//todo?
			m_pickContext.setGL( gl );
			performPick();
		} else {
			//todo?
			m_renderContext.setGL( gl );
			performRender();
			//m_lookingGlass.fireDisplayed( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassEvent( m_lookingGlass, width, height ) );
		}
	}
	public void reshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "reshape", drawable, x, y, width, height );
		assert drawable == m_drawable;
		m_width = width;
		m_height = height;
		m_lookingGlass.fireResized( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent( m_lookingGlass, width, height ) );
	}
	public void displayChanged( javax.media.opengl.GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged ) {
		assert drawable == m_drawable;
		m_lookingGlass.fireDisplayChanged( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent( m_lookingGlass, modeChanged, deviceChanged ) );
	}
}
