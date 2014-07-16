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

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;

/**
 * @author Dennis Cosgrove
 */
class GLEventAdapter implements javax.media.opengl.GLEventListener {
	private final AbstractLookingGlass lookingGlass;
	private final RenderContext renderContext = new RenderContext();

	private javax.media.opengl.GLAutoDrawable drawable;
	private int width;
	private int height;

	private java.awt.image.BufferedImage rvColorBuffer = null;
	private java.nio.FloatBuffer rvDepthBuffer = null;
	private boolean[] atIsUpsideDown = null;

	private boolean isDisplayIgnoredDueToPreviousException = false;

	private static class ReusableLookingGlassRenderEvent extends edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent {
		public ReusableLookingGlassRenderEvent( edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, Graphics2D g ) {
			super( lookingGlass, g );
		}

		@Override
		public boolean isReservedForReuse() {
			return true;
		}

		private void prologue() {
			( (Graphics2D)getGraphics2D() ).initialize( getTypedSource().getWidth(), getTypedSource().getHeight() );
		}

		private void epilogue() {
			getGraphics2D().dispose();
		}
	}

	private final ReusableLookingGlassRenderEvent reusableLookingGlassRenderEvent;

	public GLEventAdapter( AbstractLookingGlass lookingGlass ) {
		this.lookingGlass = lookingGlass;
		this.reusableLookingGlassRenderEvent = new ReusableLookingGlassRenderEvent( this.lookingGlass, new Graphics2D( this.renderContext ) );
	}

	private boolean isListening;

	public boolean isListening() {
		return this.isListening;
	}

	public void startListening( javax.media.opengl.GLAutoDrawable drawable ) {
		if( this.isListening ) {
			if( drawable == this.drawable ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( drawable, this.drawable );
			}
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "request GLEventAdapter.startListening( drawable ) ignored; already listening." );
		} else {
			this.isListening = true;
			this.drawable = drawable;
			this.drawable.addGLEventListener( this );
		}
	}

	public void stopListening( javax.media.opengl.GLAutoDrawable drawable ) {
		if( drawable == this.drawable ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( drawable, this.drawable );
		}
		if( this.isListening ) {
			this.isListening = false;
			drawable.removeGLEventListener( this );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "request GLEventAdapter.stopListening( drawable ) ignored; already not listening." );
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
			} else if( ( this.width == 0 ) || ( this.height == 0 ) ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.width, this.height, this.lookingGlass.getSize() );
			} else {
				try {
					//todo: separate clearing and rendering
					this.reusableLookingGlassRenderEvent.prologue();
					try {
						this.lookingGlass.fireCleared( this.reusableLookingGlassRenderEvent );
					} finally {
						this.reusableLookingGlassRenderEvent.epilogue();
					}
					if( this.lookingGlass.getSgCameraCount() > 0 ) {
						this.renderContext.initialize();
						Iterable<edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameras = this.lookingGlass.accessSgCameras();
						synchronized( cameras ) {
							for( edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera : cameras ) {
								AbstractCameraAdapter<? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera> cameraAdapterI = AdapterFactory.getAdapterFor( camera );
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
					if( ( this.rvColorBuffer != null ) || ( this.rvDepthBuffer != null ) ) {
						this.renderContext.captureBuffers( this.rvColorBuffer, this.rvDepthBuffer, this.atIsUpsideDown );
					}

				} catch( RuntimeException re ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "rendering will be disabled due to exception" );
					this.isDisplayIgnoredDueToPreviousException = true;
					re.printStackTrace();
					throw re;
				} catch( Error er ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "rendering will be disabled due to exception" );
					this.isDisplayIgnoredDueToPreviousException = true;
					er.printStackTrace();
					throw er;
				}
			}
		}
	}

	private java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer( int type ) {
		if( this.drawable != null ) {
			if( ( this.width != GlDrawableUtilities.getGlDrawableWidth( this.drawable ) ) || ( this.height != GlDrawableUtilities.getGlDrawableHeight( this.drawable ) ) ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: createBufferedImageForUseAsColorBuffer size mismatch" );
				this.width = GlDrawableUtilities.getGlDrawableWidth( this.drawable );
				this.height = GlDrawableUtilities.getGlDrawableHeight( this.drawable );
			}
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "warning: drawable null" );
		}

		if( ( this.width > 0 ) && ( this.height > 0 ) ) {
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

	public java.awt.image.BufferedImage getColorBuffer( java.awt.image.BufferedImage rv, boolean[] atIsUpsideDown ) {
		return this.getColorBufferWithTransparencyBasedOnDepthBuffer( rv, null, atIsUpsideDown );
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

	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer, boolean[] atIsUpsideDown ) {
		if( this.drawable.getContext() == javax.media.opengl.GLContext.getCurrent() ) {
			this.renderContext.captureBuffers( rv, depthBuffer, atIsUpsideDown );
		} else {
			if( this.rvColorBuffer != null ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.rvColorBuffer );
			}
			this.rvColorBuffer = rv;
			this.rvDepthBuffer = depthBuffer;
			this.atIsUpsideDown = atIsUpsideDown;
			this.drawable.setAutoSwapBufferMode( false );
			try {
				this.drawable.display();
			} finally {
				this.rvColorBuffer = null;
				this.rvDepthBuffer = null;
				this.atIsUpsideDown = null;
				this.drawable.setAutoSwapBufferMode( true );
			}
		}
		return rv;
	}

	private void initialize( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "initialize", drawable );
		assert drawable == this.drawable;
		javax.media.opengl.GL2 gl = drawable.getGL().getGL2();
		ConformanceTestResults.SINGLETON.updateRenderInformationIfNecessary( gl );

		//edu.cmu.cs.dennisc.print.PrintUtilities.println( drawable.getChosenGLCapabilities() );

		final boolean USE_DEBUG_GL = false;
		if( USE_DEBUG_GL ) {
			if( gl instanceof javax.media.opengl.DebugGL2 ) {
				// pass
			} else {
				gl = new javax.media.opengl.DebugGL2( gl );
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "using debug gl: ", gl );
				drawable.setGL( gl );
			}
		}

		this.width = GlDrawableUtilities.getGlDrawableWidth( drawable );
		this.height = GlDrawableUtilities.getGlDrawableHeight( drawable );

		this.renderContext.setGL( gl );
		this.lookingGlass.fireInitialized( new edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent( this.lookingGlass, GlDrawableUtilities.getGlDrawableWidth( this.drawable ), GlDrawableUtilities.getGlDrawableHeight( this.drawable ) ) );
	}

	//todo: investigate not being invoked
	@Override
	public void init( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "init", drawable );
		initialize( drawable );
	}

	@Override
	public void display( javax.media.opengl.GLAutoDrawable drawable ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "display:", drawable );
		assert drawable == this.drawable;
		//this.lookingGlass.commitAnyPendingChanges();
		//todo?
		javax.media.opengl.GL2 gl = drawable.getGL().getGL2();
		if( this.renderContext.gl != null ) {
			//pass
		} else {
			initialize( drawable );
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "note: initialize necessary from display" );
		}
		if( ( this.width > 0 ) && ( this.height > 0 ) ) {
			//pass
		} else {
			int nextWidth = GlDrawableUtilities.getGlDrawableWidth( drawable );
			int nextHeight = GlDrawableUtilities.getGlDrawableHeight( drawable );
			if( ( this.width != nextWidth ) || ( this.height != nextHeight ) ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this.width, this.height, nextWidth, nextHeight );
				this.width = nextWidth;
				this.height = nextHeight;
			}
		}
		this.renderContext.setGL( gl );
		performRender();
	}

	@Override
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

	@Override
	public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( drawable );
	}

	public void forgetAllCachedItems() {
		if( this.renderContext != null ) {
			this.renderContext.forgetAllCachedItems();
		}
	}

	public void clearUnusedTextures() {
		if( this.renderContext != null ) {
			this.renderContext.clearUnusedTextures();
		}
	}
}
