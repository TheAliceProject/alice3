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
public class SynchronousImageCapturer implements edu.cmu.cs.dennisc.render.SynchronousImageCapturer {
	public SynchronousImageCapturer( RenderTargetImp rtImp ) {
		this.rtImp = rtImp;
	}

	@Override
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBuffer() {
		return this.rtImp.createBufferedImageForUseAsColorBuffer();
	}

	@Override
	public java.awt.image.BufferedImage getColorBufferNotBotheringToFlipVertically( java.awt.image.BufferedImage rv, boolean[] atIsUpsideDown ) {
		return this.rtImp.getColorBuffer( rv, atIsUpsideDown );
	}

	@Override
	public java.awt.image.BufferedImage getColorBuffer( java.awt.image.BufferedImage rv ) {
		return this.rtImp.getColorBuffer( rv, null );
	}

	@Override
	public final java.awt.image.BufferedImage getColorBuffer() {
		return this.getColorBuffer( createBufferedImageForUseAsColorBuffer() );
	}

	@Override
	public java.awt.image.BufferedImage createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer() {
		return this.rtImp.createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer();
	}

	@Override
	public java.nio.FloatBuffer createFloatBufferForUseAsDepthBuffer() {
		return this.rtImp.createFloatBufferForUseAsDepthBuffer();
	}

	@Override
	public java.nio.FloatBuffer getDepthBuffer( java.nio.FloatBuffer rv ) {
		return this.rtImp.getDepthBuffer( rv );
	}

	@Override
	public final java.nio.FloatBuffer getDepthBuffer() {
		return this.getDepthBuffer( createFloatBufferForUseAsDepthBuffer() );
	}

	@Override
	public java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer( java.awt.image.BufferedImage rv, java.nio.FloatBuffer depthBuffer ) {
		return this.rtImp.getColorBufferWithTransparencyBasedOnDepthBuffer( rv, depthBuffer, null );
	}

	@Override
	public final java.awt.image.BufferedImage getColorBufferWithTransparencyBasedOnDepthBuffer() {
		return getColorBufferWithTransparencyBasedOnDepthBuffer( createBufferedImageForUseAsColorBufferWithTransparencyBasedOnDepthBuffer(), createFloatBufferForUseAsDepthBuffer() );
	}

	private final RenderTargetImp rtImp;
}
