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

import static javax.media.opengl.GL.*;

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
//			this.renderContext.gl.glMatrixMode( GL_PROJECTION );
//			this.renderContext.gl.glPushMatrix();
//			this.renderContext.gl.glLoadIdentity();
//			this.renderContext.gl.glOrtho( 0, this.lookingGlass.getWidth() - 1, this.lookingGlass.getHeight() - 1, 0, -1, 1 );
//			this.renderContext.gl.glMatrixMode( GL_MODELVIEW );
//			this.renderContext.gl.glPushMatrix();
//			this.renderContext.gl.glLoadIdentity();
//
//			this.renderContext.gl.glDisable( GL_DEPTH_TEST );
//			this.renderContext.gl.glDisable( GL_LIGHTING );
//			this.renderContext.gl.glDisable( GL_CULL_FACE );
//			this.renderContext.setDiffuseColorTextureAdapter( null );
//			this.renderContext.setBumpTextureAdapter( null );
//
//			
//			try {
//				overlay.paint( this.lookingGlass );
//				this.renderContext.gl.glFlush();
//			} finally {
//				this.renderContext.gl.glMatrixMode( GL_PROJECTION );
//				this.renderContext.gl.glPopMatrix();
//				this.renderContext.gl.glMatrixMode( GL_MODELVIEW );
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
			} else if (this.width == 0 || this.height == 0)
			{
				//pass
			}
			else {
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
								this.reusableLookingGlassRenderEvent.prologue();
								try {
									cameraAdapterI.postRender( this.renderContext, this.width, this.height, this.lookingGlass, this.reusableLookingGlassRenderEvent.getGraphics2D() );
								} finally {
									this.reusableLookingGlassRenderEvent.epilogue();
								}
							}
						}
						this.renderContext.renderLetterboxingIfNecessary( this.width, this.height );
					} else {
						this.renderContext.gl.glClearColor( 0, 0, 0, 1 );
						this.renderContext.gl.glClear( GL_COLOR_BUFFER_BIT );
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
				catch( Error er ) {
                    System.err.println( "rendering will be disabled due to exception" );
                    this.isDisplayIgnoredDueToPreviousException = true;
                    throw er;
                }
			}
		}
	}

	private static ConformanceTestResults conformanceTestResults = null;
	private void performPick() {
		if( conformanceTestResults != null ) {
			//pass
		} else {
			conformanceTestResults = new ConformanceTestResults( this.pickContext.gl );
			if( conformanceTestResults.isPickFunctioningCorrectly() ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl isPickFunctioningCorrectly:", conformanceTestResults.isPickFunctioningCorrectly() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl version:", conformanceTestResults.getVersion() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl vendor:", conformanceTestResults.getVendor() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl renderer:", conformanceTestResults.getRenderer() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl extensions:", conformanceTestResults.getExtensions() );
			}
		}
		
		edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver = this.pickParameters.getPickObserver();
		if( pickObserver != null ) {
			pickObserver.prePick();
			ChangeHandler.handleBufferedChanges();
		}
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = this.pickParameters.getSGCamera();
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );

		this.selectionAsIntBuffer.rewind();
		this.pickContext.gl.glSelectBuffer( SELECTION_CAPACITY, this.selectionAsIntBuffer );

		this.pickContext.gl.glRenderMode( GL_SELECT );
		this.pickContext.gl.glInitNames();

		java.awt.Rectangle actualViewport = this.lookingGlass.getActualViewport( sgCamera );
		this.pickContext.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
		cameraAdapter.performPick( this.pickContext, this.pickParameters, actualViewport, conformanceTestResults );
		this.pickContext.gl.glFlush();

		this.selectionAsIntBuffer.rewind();
		int length = this.pickContext.gl.glRenderMode( GL_RENDER );

		//todo: invesigate negative length
		//assert length >= 0;

		if( length > 0 ) {
			SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[ length ];
			int offset = 0;
			for( int i = 0; i < length; i++ ) {
				selectionBufferInfos[ i ] = new SelectionBufferInfo( this.pickContext, this.selectionAsIntBuffer, offset );
				offset += 7;
			}

			if( conformanceTestResults.isPickFunctioningCorrectly() ) {
				double x = this.pickParameters.getX();
				double y = this.pickParameters.getFlippedY( actualViewport );

				edu.cmu.cs.dennisc.math.Matrix4x4 m = new edu.cmu.cs.dennisc.math.Matrix4x4();
				m.translation.set( actualViewport.width - 2*(x-actualViewport.x), actualViewport.height - 2*(y-actualViewport.y), 0, 1 );
				edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( m, actualViewport.width, actualViewport.height, 1.0 );

				edu.cmu.cs.dennisc.math.Matrix4x4 p = new edu.cmu.cs.dennisc.math.Matrix4x4();
				cameraAdapter.getActualProjectionMatrix( p, actualViewport );
				
				m.applyMultiplication( p );
				m.invert();
				for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
					selectionBufferInfo.updatePointInSource( m );
				}
			} else {
				edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
				ray.setNaN();
				cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport);
				ray.accessDirection().normalize();
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformation = sgCamera.getInverseAbsoluteTransformation();
				for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
					selectionBufferInfo.updatePointInSource( ray, inverseAbsoluteTransformation );
				}
			}

			if( length > 1 ) {
//				float front0 = selectionBufferInfos[ 0 ].getZFront();
//				boolean isDifferentiated = false;
//				for( int i=1; i<length; i++ ) {
//					if( front0 == selectionBufferInfos[ i ].getZFront() ) {
//						//pass
//					} else {
//						isDifferentiated = true;
//						break;
//					}
//				}
//				java.util.Comparator< SelectionBufferInfo > comparator;
//				if( isDifferentiated ) {
//					comparator = new java.util.Comparator< SelectionBufferInfo >() {
//						public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
//							return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
//						}
//					};
//				} else {
//					if( conformanceTestResults.isPickFunctioningCorrectly() ) {
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: conformance test reports pick is functioning correctly" );
//						comparator = null;
//					} else { 
//						edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
//						ray.setNaN();
//						cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport);
//						for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
//							selectionBufferInfo.updatePointInSource( ray );
//						}
//						comparator = new java.util.Comparator< SelectionBufferInfo >() {
//							public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
//								return Double.compare( sbi1.getPointInSource().z, sbi2.getPointInSource().z );
//							}
//						};
//					}
//				}
				java.util.Comparator< SelectionBufferInfo > comparator;
				if( conformanceTestResults.isPickFunctioningCorrectly() ) {
					comparator = new java.util.Comparator<SelectionBufferInfo>() {
						public int compare(SelectionBufferInfo sbi1, SelectionBufferInfo sbi2) {
							return Float.compare(sbi1.getZFront(), sbi2.getZFront());
						}
					};
				} else {
					comparator = new java.util.Comparator<SelectionBufferInfo>() {
						public int compare(SelectionBufferInfo sbi1, SelectionBufferInfo sbi2) {
							double z1 = -sbi1.getPointInSource().z;
							double z2 = -sbi2.getPointInSource().z;
							return Double.compare(z1, z2);
						}
					};
				}
				java.util.Arrays.sort( selectionBufferInfos, comparator );
			}
			for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
				this.pickParameters.addPickResult( sgCamera, selectionBufferInfo.getSGVisual(), selectionBufferInfo.isFrontFacing(), selectionBufferInfo.getSGGeometry(), selectionBufferInfo.getSubElement(), selectionBufferInfo.getPointInSource() );
			}
		}
		if( pickObserver != null ) {
			pickObserver.postPick();
			ChangeHandler.handleBufferedChanges();
		}

	}

//	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
//		assert sgCamera != null;
//		assert this.drawable != null;
//		this.drawable.setAutoSwapBufferMode( false );
//		try {
//			this.pickParameters = new PickParameters( this.drawable, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
//			if( this.drawable.isRealized() ) {
//				this.drawable.display();
//			} else {
//				Thread.dumpStack();
//			}
//			return this.pickParameters.accessFrontMostPickResult();
//		} finally {
//			this.drawable.setAutoSwapBufferMode( true );
//			this.pickParameters = null;
//		}
//	}
//	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
//		assert sgCamera != null;
//		this.drawable.setAutoSwapBufferMode( false );
//		try {
//			this.pickParameters = new PickParameters( this.drawable, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
//			this.drawable.display();
//			return this.pickParameters.accessAllPickResults();
//		} finally {
//			this.drawable.setAutoSwapBufferMode( true );
//			this.pickParameters = null;
//		}
//	}

	private java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer( int type ) {
		if( this.drawable != null ) {
			if( this.width != this.drawable.getWidth() || this.height != this.drawable.getHeight() ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: createBufferedImageForUseAsColorBuffer size mismatch" );
				this.width = this.drawable.getWidth();
				this.height = this.drawable.getHeight();
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: drawable null" );
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
//			String extensions = this.this.renderContext.gl.glGetString( GL_EXTENSIONS );
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
//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "createBufferedImageForUseAsColorBuffer: capturing images from gl is expected to fail since since gl.glGetString( GL_EXTENSIONS ) returns null." );
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
		return this.getColorBufferWithTransparencyBasedOnDepthBuffer( rv, null );
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
		if( this.drawable.getContext() == javax.media.opengl.GLContext.getCurrent() ) {
			this.renderContext.captureBuffers( rv, depthBuffer );
		} else {
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
		}
		return rv;
	}
	
	private void initialize( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "initialize", drawable );
		assert drawable == this.drawable;
		javax.media.opengl.GL gl = drawable.getGL();
		
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( drawable.getChosenGLCapabilities() );
		
//		final boolean USE_DEBUG_GL = false;
//		if( USE_DEBUG_GL ) {
//			if( gl instanceof javax.media.opengl.DebugGL2 ) {
//				// pass
//			} else {
//				gl = new javax.media.opengl.DebugGL2( gl );
//				System.out.println( "using debug gl: " + gl );
//				drawable.setGL( gl );
//			}
//		}
		this.renderContext.setGL( gl );
		this.pickContext.setGL( gl );
		this.lookingGlass.fireInitialized( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent( this.lookingGlass, this.drawable.getWidth(), this.drawable.getHeight() ) );
	}

	//todo: investigate not being invoked
	public void init( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "init", drawable );
		initialize( drawable );
	}
	public void display( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "display:", drawable );
		assert drawable == this.drawable;
	
		//this.lookingGlass.commitAnyPendingChanges();
		//todo?
		javax.media.opengl.GL gl = drawable.getGL();
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
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "displayChanged", drawable, modeChanged, deviceChanged );
		assert drawable == this.drawable;
		this.lookingGlass.fireDisplayChanged( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent( this.lookingGlass, modeChanged, deviceChanged ) );
	}
	public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
		System.err.println( "todo: dispose " + drawable );
	}

	public void forgetAllCachedItems() {
		if( this.renderContext != null ) {
			this.renderContext.forgetAllCachedItems();
		}
	}
}
