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

import edu.cmu.cs.dennisc.print.PrintUtilities;

/**
 * @author Dennis Cosgrove
 */
class SelectionBufferInfo {
	private float zFront;
	private float zBack;
	private edu.cmu.cs.dennisc.scenegraph.Visual sgVisual;
	private boolean isFrontFacing;
	private edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry;
	private int subElement;

	public SelectionBufferInfo( PickContext pc, java.nio.IntBuffer intBuffer, int offset ) {
		int nameCount = intBuffer.get( offset + 0 );
		int zFrontAsInt = intBuffer.get( offset + 1 );
		int zBackAsInt = intBuffer.get( offset + 2 );

		long zFrontAsLong = zFrontAsInt;
		zFrontAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;

		this.zFront = (float)zFrontAsLong;
		this.zFront /= (float)RenderContext.MAX_UNSIGNED_INTEGER;

		long zBackAsLong = zBackAsInt;
		zBackAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;
		
//		int[] atDepth = { -1 };
//		pc.gl.glGetIntegerv( GL.GL_DEPTH_BITS, atDepth, 0 );
//		int[] atClearValue = { -1 };
//		pc.gl.glGetIntegerv( GL.GL_DEPTH_CLEAR_VALUE, atClearValue, 0 );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "SelectionBufferInfo:", atDepth[ 0 ], Long.toHexString( atClearValue[ 0 ] ), Long.toHexString( RenderContext.MAX_UNSIGNED_INTEGER ), Integer.toHexString( zFrontAsInt ), Long.toHexString( zFrontAsLong ), Integer.toHexString( zBackAsInt ), Long.toHexString( zBackAsLong )  );

		this.zBack = (float)zBackAsLong;
		this.zBack /= (float)RenderContext.MAX_UNSIGNED_INTEGER;

		if( nameCount == 4 ) {
			int key = intBuffer.get( offset + 3 );
			VisualAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Visual> visualAdapter = pc.getPickVisualAdapterForName( key );
			if( visualAdapter != null ) {
				this.sgVisual = visualAdapter.m_element;
				this.isFrontFacing = intBuffer.get( offset + 4 ) == 1;
				this.sgGeometry = this.sgVisual.geometries.getValue()[ ( intBuffer.get( offset + 5 ) ) ];
				this.subElement = intBuffer.get( offset + 6 );
			}
		}
	}

	public float getZFront() {
		return this.zFront;
	}
	public float getZBack() {
		return this.zBack;
	}

	public edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
		return this.sgVisual;
	}
	public boolean isFrontFacing() {
		return this.isFrontFacing;
	}
	public edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return this.sgGeometry;
	}
	public int getSubElement() {
		return this.subElement;
	}
}

/**
 * @author Dennis Cosgrove
 */
class GLEventAdapter implements javax.media.opengl.GLEventListener {
	private AbstractLookingGlass lookingGlass;
	private javax.media.opengl.GLAutoDrawable drawable;
	private RenderContext renderContext = new RenderContext();
	private PickContext pickContext = new PickContext();
	private int width;
	private int height;

	private PickParameters pickParameters = null;
	private java.awt.image.BufferedImage rvColorBuffer = null;
	private java.nio.FloatBuffer rvDepthBuffer = null;

	private static final int SELECTION_CAPACITY = 256;
	private java.nio.IntBuffer selectionAsIntBuffer = null;

	private boolean isDisplayIgnoredDueToPreviousException = false;

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
	private ReusableLookingGlassRenderEvent reusableLookingGlassRenderEvent;

	public GLEventAdapter( AbstractLookingGlass lookingGlass ) {
		this.lookingGlass = lookingGlass;
		this.reusableLookingGlassRenderEvent = new ReusableLookingGlassRenderEvent( this.lookingGlass, new Graphics2D( this.renderContext ) );

		final int SIZEOF_INT = 4;
		java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect( SIZEOF_INT * SELECTION_CAPACITY );
		byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
		this.selectionAsIntBuffer = byteBuffer.asIntBuffer();
	}

	private boolean isListening;

	public boolean isListening() {
		return this.isListening;
	}
	public void startListening( javax.media.opengl.GLAutoDrawable drawable ) {
		if( this.isListening ) {
			assert drawable == this.drawable;
			System.err.println( "request GLEventAdapter.startListening( drawable ) ignored; already listening." );
		} else {
			this.isListening = true;
			this.drawable = drawable;
			this.drawable.addGLEventListener( this );
		}
	}
	public void stopListening( javax.media.opengl.GLAutoDrawable drawable ) {
		assert drawable == this.drawable;
		if( this.isListening ) {
			this.isListening = false;
			drawable.removeGLEventListener( this );
		} else {
			System.err.println( "request GLEventAdapter.stopListening( drawable ) ignored; already not listening." );
		}
		this.drawable = null;
	}

//	private void paintOverlay() {
//		edu.cmu.cs.dennisc.lookingglass.Overlay overlay = this.lookingGlass.getOverlay();
//		if( overlay != null ) {
//			
//			this.renderContext.gl.glMatrixMode( GL.GL_PROJECTION );
//			this.renderContext.gl.glPushMatrix();
//			this.renderContext.gl.glLoadIdentity();
//			this.renderContext.gl.glOrtho( 0, this.lookingGlass.getWidth() - 1, this.lookingGlass.getHeight() - 1, 0, -1, 1 );
//			this.renderContext.gl.glMatrixMode( GL.GL_MODELVIEW );
//			this.renderContext.gl.glPushMatrix();
//			this.renderContext.gl.glLoadIdentity();
//
//			this.renderContext.gl.glDisable( GL.GL_DEPTH_TEST );
//			this.renderContext.gl.glDisable( GL.GL_LIGHTING );
//			this.renderContext.gl.glDisable( GL.GL_CULL_FACE );
//			this.renderContext.setDiffuseColorTextureAdapter( null );
//			this.renderContext.setBumpTextureAdapter( null );
//
//			
//			try {
//				overlay.paint( this.lookingGlass );
//				this.renderContext.gl.glFlush();
//			} finally {
//				this.renderContext.gl.glMatrixMode( GL.GL_PROJECTION );
//				this.renderContext.gl.glPopMatrix();
//				this.renderContext.gl.glMatrixMode( GL.GL_MODELVIEW );
//				this.renderContext.gl.glPopMatrix();
//			}
//		}
//	}
	
	private void performRender() {
		if( this.lookingGlass.isRenderingEnabled() ) {
			this.renderContext.actuallyForgetTexturesIfNecessary();
			this.renderContext.actuallyForgetDisplayListsIfNecessary();
			if( this.isDisplayIgnoredDueToPreviousException ) {
				//pass
			} else {
				try {
					//todo: separate clearing and rendering
					this.reusableLookingGlassRenderEvent.prologue();
					try {
						this.lookingGlass.fireCleared( this.reusableLookingGlassRenderEvent );
					} finally {
						this.reusableLookingGlassRenderEvent.epilogue();
					}
					if( this.lookingGlass.getCameraCount() > 0 ) {
						this.renderContext.initialize();
						Iterable< edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameras = this.lookingGlass.accessCameras();
						synchronized( cameras ) {
							for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera : cameras ) {
								AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapterI = AdapterFactory.getAdapterFor( camera );
								cameraAdapterI.performClearAndRenderOffscreen( this.renderContext, this.width, this.height );
							}
						}
						this.renderContext.renderLetterboxingIfNecessary( this.width, this.height );
					} else {
						this.renderContext.gl.glClearColor( 0, 0, 0, 1 );
						this.renderContext.gl.glClear( GL.GL_COLOR_BUFFER_BIT );
					}
					this.reusableLookingGlassRenderEvent.prologue();
					try {
						this.lookingGlass.fireRendered( this.reusableLookingGlassRenderEvent );
					} finally {
						this.reusableLookingGlassRenderEvent.epilogue();
					}
					if( this.rvColorBuffer != null || this.rvDepthBuffer != null ) {
						this.renderContext.captureBuffers( this.rvColorBuffer, this.rvDepthBuffer );
					}
				} catch( RuntimeException re ) {
					System.err.println( "rendering will be disabled due to exception" );
					this.isDisplayIgnoredDueToPreviousException = true;
					throw re;
				}
			}
		}
	}

	private void performPick() {
		edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver = this.pickParameters.getPickObserver();
		if( pickObserver != null ) {
			pickObserver.prePick();
			ChangeHandler.handleBufferedChanges();
		}
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = this.pickParameters.getSGCamera();
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );

		this.selectionAsIntBuffer.rewind();
		this.pickContext.gl.glSelectBuffer( SELECTION_CAPACITY, this.selectionAsIntBuffer );

		this.pickContext.gl.glRenderMode( GL.GL_SELECT );
		this.pickContext.gl.glInitNames();

		java.awt.Rectangle actualViewport = this.lookingGlass.getActualViewport( sgCamera );
		this.pickContext.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
		cameraAdapter.performPick( this.pickContext, this.pickParameters, actualViewport );
		this.pickContext.gl.glFlush();

		this.selectionAsIntBuffer.rewind();
		int length = this.pickContext.gl.glRenderMode( GL.GL_RENDER );

		//todo: invesigate negative length
		//assert length >= 0;

		if( length > 0 ) {
			SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[ length ];
			int offset = 0;
			for( int i = 0; i < length; i++ ) {
				selectionBufferInfos[ i ] = new SelectionBufferInfo( this.pickContext, this.selectionAsIntBuffer, offset );
				offset += 7;
			}

			if( length > 1 ) {
				float front0 = selectionBufferInfos[ 0 ].getZFront();
				boolean isDifferentiated = false;
				for( int i=1; i<length; i++ ) {
					if( front0 == selectionBufferInfos[ i ].getZFront() ) {
						//pass
					} else {
						isDifferentiated = true;
						break;
					}
				}
				if( isDifferentiated ) {
					java.util.Arrays.sort( selectionBufferInfos, new java.util.Comparator< SelectionBufferInfo >() {
						public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
							return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
						}
					} );
				} else {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: account for video card driver bug" );
				}
			}

			for( int i=0; i<length; i++ ) {
				//todo: perform trimmed math
				
				double x = this.pickParameters.getX();
				double y = this.pickParameters.getFlippedY( actualViewport );

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
				this.pickParameters.addPickResult( sgCamera, selectionBufferInfos[ i ].getSGVisual(), selectionBufferInfos[ i ].isFrontFacing(), selectionBufferInfos[ i ].getSGGeometry(), selectionBufferInfos[ i ].getSubElement(), xyzInSource );
			}
		}
		if( pickObserver != null ) {
			pickObserver.postPick();
			ChangeHandler.handleBufferedChanges();
		}

	}

	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		assert sgCamera != null;
		assert this.drawable != null;
		this.drawable.setAutoSwapBufferMode( false );
		try {
			this.pickParameters = new PickParameters( this.drawable, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
			this.drawable.display();
			return this.pickParameters.accessFrontMostPickResult();
		} finally {
			this.drawable.setAutoSwapBufferMode( true );
			this.pickParameters = null;
		}
	}
	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		assert sgCamera != null;
		this.drawable.setAutoSwapBufferMode( false );
		try {
			this.pickParameters = new PickParameters( this.drawable, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
			this.drawable.display();
			return this.pickParameters.accessAllPickResults();
		} finally {
			this.drawable.setAutoSwapBufferMode( true );
			this.pickParameters = null;
		}
	}

	private java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer( int type ) {
		if( this.drawable != null ) {
			if( this.width != this.drawable.getWidth() || this.height != this.drawable.getHeight() ) {
				PrintUtilities.println( "warning: createBufferedImageForUseAsColorBuffer size mismatch" );
				this.width = this.drawable.getWidth();
				this.height = this.drawable.getHeight();
			}
		} else {
			PrintUtilities.println( "warning: drawable null" );
		}
		
		if( this.width > 0 && this.height > 0 ) {
			return new java.awt.image.BufferedImage( this.width, this.height, type );
		} else {
			return null;
		}
	}
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer() {
//		boolean isClearedToCreateImage;
//		if( this.this.renderContext.gl != null ) {
//			String extensions = this.this.renderContext.gl.glGetString( GL.GL_EXTENSIONS );
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
			this.rvColorBuffer = rv;
			this.drawable.setAutoSwapBufferMode( false );
			try {
				this.drawable.display();
			} finally {
				this.rvColorBuffer = null;
				this.drawable.setAutoSwapBufferMode( true );
			}
		}
		return rv;
	}
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer() {
		return createBufferedImageForUseAsColorBuffer( java.awt.image.BufferedImage.TYPE_4BYTE_ABGR );
	}
	public java.nio.FloatBuffer createFloatBufferForUseAsDepthBuffer() {
		return java.nio.FloatBuffer.allocate( this.width * this.height );
	}
	public java.nio.FloatBuffer getDepthBuffer( java.nio.FloatBuffer rv ) {
		this.rvDepthBuffer = rv;
		this.drawable.setAutoSwapBufferMode( false );
		try {
			this.drawable.display();
		} finally {
			this.rvDepthBuffer = null;
			this.drawable.setAutoSwapBufferMode( true );
		}
		return rv;
	}

	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer ) {
		this.rvColorBuffer = rv;
		this.rvDepthBuffer = depthBuffer;
		this.drawable.setAutoSwapBufferMode( false );
		try {
			this.drawable.display();
		} finally {
			this.rvColorBuffer = null;
			this.rvDepthBuffer = null;
			this.drawable.setAutoSwapBufferMode( true );
		}
		return rv;
	}
	
	private void initialize( javax.media.opengl.GLAutoDrawable drawable ) {
		assert drawable == this.drawable;
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
		this.renderContext.setGL( gl );
		this.pickContext.setGL( gl );
		this.lookingGlass.fireInitialized( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent( this.lookingGlass, this.drawable.getWidth(), this.drawable.getHeight() ) );
	}

	//todo: investigate not being invoked
	public void init( javax.media.opengl.GLAutoDrawable drawable ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "init", drawable );
		initialize( drawable );
	}
	public void display( javax.media.opengl.GLAutoDrawable drawable ) {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "display:", drawable );
		assert drawable == this.drawable;
	
		//this.lookingGlass.commitAnyPendingChanges();
		//todo?
		GL gl = drawable.getGL();
		if( this.renderContext.gl != null || this.pickContext.gl != null ) {
			//pass
		} else {
			initialize( drawable );
		}
		
//		if( this.width > 0 && this.height > 0 ) {
//			//pass
//		} else {
//			this.width = drawable.getWidth();
//			this.height = drawable.getHeight();
//		}
		if( this.pickParameters != null ) {
			//todo?
			this.pickContext.setGL( gl );
			performPick();
		} else {
			//todo?
			this.renderContext.setGL( gl );
			performRender();
			//this.lookingGlass.fireDisplayed( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassEvent( this.lookingGlass, width, height ) );
		}
	}
	public void reshape( javax.media.opengl.GLAutoDrawable drawable, int x, int y, int width, int height ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "reshape", drawable, x, y, width, height );
		assert drawable == this.drawable;
		this.width = width;
		this.height = height;
		this.lookingGlass.fireResized( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent( this.lookingGlass, width, height ) );
	}
	public void displayChanged( javax.media.opengl.GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged ) {
		assert drawable == this.drawable;
		this.lookingGlass.fireDisplayChanged( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent( this.lookingGlass, modeChanged, deviceChanged ) );
	}
}
