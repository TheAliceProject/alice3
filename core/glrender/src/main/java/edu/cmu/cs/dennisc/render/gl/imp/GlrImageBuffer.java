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

/**
 * @author Dennis Cosgrove
 */
public final class GlrImageBuffer implements edu.cmu.cs.dennisc.render.ImageBuffer {
	public GlrImageBuffer( edu.cmu.cs.dennisc.color.Color4f backgroundColor ) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public Object getImageLock() {
		return this.imageLock;
	}

	@Override
	public edu.cmu.cs.dennisc.color.Color4f getBackgroundColor() {
		return this.backgroundColor;
	}

	private boolean isAlphaRequired() {
		return this.backgroundColor == null;
	}

	/*package-private*/java.awt.image.BufferedImage acquireImage( int width, int height ) {
		//TODO
		//int imageType = isAlphaChannelDesired ? java.awt.image.BufferedImage.TYPE_4BYTE_ABGR : java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
		int imageType = java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
		if( this.image != null ) {
			if( ( this.image.getWidth() == width ) && ( this.image.getHeight() == height ) && ( this.image.getType() == imageType ) ) {
				//pass
			} else {
				this.image = null;
			}
		}
		if( this.image != null ) {
			//pass
		} else {
			this.image = new java.awt.image.BufferedImage( width, height, imageType );
		}
		return this.image;
	}

	/*package-private*/void releaseImageAndFloatBuffer( boolean isRightSideUp ) {
		this.isRightSideUp = true;
	}

	/*package-private*/java.nio.FloatBuffer acquireFloatBuffer( int width, int height ) {
		if( this.isAlphaRequired() ) {
			int capacity = width * height;
			if( this.depthBuffer != null ) {
				if( this.depthBuffer.capacity() == capacity ) {
					//pass
				} else {
					this.depthBuffer = null;
				}
			}
			if( this.depthBuffer != null ) {
				//pass
			} else {
				this.depthBuffer = java.nio.FloatBuffer.allocate( capacity );
			}
			return this.depthBuffer;
		} else {
			return null;
		}
	}

	@Override
	public java.awt.image.BufferedImage getImage() {
		return this.image;
	}

	@Override
	public boolean isRightSideUp() {
		return this.isRightSideUp;
	}

	private final Object imageLock = "imageLock";
	private final edu.cmu.cs.dennisc.color.Color4f backgroundColor;
	private java.awt.image.BufferedImage image;
	private boolean isRightSideUp;
	private java.nio.FloatBuffer depthBuffer;
}
