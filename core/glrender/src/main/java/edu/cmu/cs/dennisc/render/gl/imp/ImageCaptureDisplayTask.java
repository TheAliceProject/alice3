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
package edu.cmu.cs.dennisc.render.gl.imp;

import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_NO_ERROR;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;
import static com.jogamp.opengl.GL2.GL_ABGR_EXT;
import static com.jogamp.opengl.GL2ES2.GL_DEPTH_COMPONENT;

/**
 * @author Dennis Cosgrove
 */
public final class ImageCaptureDisplayTask extends DisplayTask {
	public ImageCaptureDisplayTask( edu.cmu.cs.dennisc.render.RenderTask renderTask, java.awt.Rectangle viewport, GlrImageBuffer imageBuffer, edu.cmu.cs.dennisc.render.ImageOrientationRequirement imageOrientationRequirement, edu.cmu.cs.dennisc.render.ImageCaptureObserver observer ) {
		this.renderTask = renderTask;
		this.viewport = viewport;
		this.imageBuffer = imageBuffer;
		this.imageOrientationRequirement = imageOrientationRequirement;
		this.observer = observer;
	}

	@Override
	/*package-private*/IsFrameBufferIntact handleDisplay( edu.cmu.cs.dennisc.render.gl.imp.RenderTargetImp rtImp, com.jogamp.opengl.GLAutoDrawable drawable, com.jogamp.opengl.GL2 gl ) {
		synchronized( this.imageBuffer.getImageLock() ) {

			if( this.renderTask != null ) {
				this.renderTask.render( new GlrRenderContext( this.viewport, drawable ) );
			}
			gl.glFlush();

			int x;
			int y;
			int width;
			int height;

			if( this.viewport != null ) {
				x = viewport.x;
				y = viewport.y;
				width = viewport.width;
				height = viewport.height;
			} else {
				x = 0;
				y = 0;
				java.awt.Dimension surfaceSize = rtImp.getRenderTarget().getSurfaceSize();
				width = surfaceSize.width;
				height = surfaceSize.height;
			}

			java.awt.image.BufferedImage rvImage = this.imageBuffer.acquireImage( width, height );
			java.nio.FloatBuffer rvDepth = this.imageBuffer.acquireFloatBuffer( width, height );
			boolean[] atIsRightSideUp = new boolean[ 1 ];
			try {
				java.awt.image.DataBuffer dataBuffer = rvImage.getRaster().getDataBuffer();
				if( rvDepth != null ) {
					byte[] color = ( (java.awt.image.DataBufferByte)dataBuffer ).getData();
					java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( color );
					gl.glReadPixels( x, y, width, height, GL_ABGR_EXT, GL_UNSIGNED_BYTE, buffer );

					gl.glReadPixels( x, y, width, height, GL_DEPTH_COMPONENT, GL_FLOAT, rvDepth );

					final byte ON = (byte)0;
					final byte OFF = (byte)255;
					int i = 0;
					while( rvDepth.hasRemaining() ) {
						if( rvDepth.get() == 1.0f ) {
							color[ i ] = ON;
						} else {
							color[ i ] = OFF;
						}
						i += 4;
					}
					rvDepth.rewind();

				} else {
					//java.nio.IntBuffer buffer = java.nio.IntBuffer.wrap( ((java.awt.image.DataBufferInt)dataBuffer).getData() );
					java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( ( (java.awt.image.DataBufferByte)dataBuffer ).getData() );

					//clear error buffer if necessary
					while( gl.glGetError() != GL_NO_ERROR ) {
					}

					//int format = GL_RGB;
					//int format = GL_RGBA;
					int format = GL_ABGR_EXT;
					//int format = GL_BGRA;

					//int type = GL_UNSIGNED_INT;
					int type = GL_UNSIGNED_BYTE;

					gl.glReadPixels( x, y, width, height, format, type, buffer );

					java.util.List<Integer> errors = null;
					while( true ) {
						int error = gl.glGetError();
						if( error == GL_NO_ERROR ) {
							break;
						} else {
							if( errors != null ) {
								//pass
							} else {
								errors = new java.util.LinkedList<Integer>();
							}
							errors.add( error );
						}
					}
					if( errors != null ) {
						com.jogamp.opengl.glu.GLU glu = new com.jogamp.opengl.glu.GLU();
						String description = glu.gluErrorString( errors.get( 0 ) );
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unable to capture back buffer:", description );
					}
				}
				atIsRightSideUp[ 0 ] = imageOrientationRequirement == edu.cmu.cs.dennisc.render.ImageOrientationRequirement.RIGHT_SIDE_UP_REQUIRED;
				if( atIsRightSideUp[ 0 ] ) {
					com.jogamp.opengl.util.awt.ImageUtil.flipImageVertically( rvImage );
				}
			} finally {
				this.imageBuffer.releaseImageAndFloatBuffer( atIsRightSideUp[ 0 ] );
			}
			this.observer.done( this.imageBuffer );
		}
		return IsFrameBufferIntact.FALSE;
	}

	private final edu.cmu.cs.dennisc.render.RenderTask renderTask;
	private final java.awt.Rectangle viewport;
	private final GlrImageBuffer imageBuffer;
	private final edu.cmu.cs.dennisc.render.ImageOrientationRequirement imageOrientationRequirement;
	private final edu.cmu.cs.dennisc.render.ImageCaptureObserver observer;
}
