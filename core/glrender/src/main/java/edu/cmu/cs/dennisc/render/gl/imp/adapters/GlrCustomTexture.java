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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

/**
 * @author Dennis Cosgrove
 */
public class GlrCustomTexture extends GlrTexture<edu.cmu.cs.dennisc.texture.CustomTexture> {
	@Override
	protected boolean isDirty() {
		return owner.isAnimated() || super.isDirty();
	}

	public java.awt.Graphics2D createGraphics() {
		//		edu.cmu.cs.dennisc.scenegraph.CustomTexture sgCustomTexture = this.sgE;
		//		if( this.textureRenderer != null ) {
		//			this.textureRenderer.dispose();
		//		}
		//		this.textureRenderer = new com.sun.opengl.util.awt.TextureRenderer( sgCustomTexture.getWidth(), sgCustomTexture.getHeight(), sgCustomTexture.isPotentiallyAlphaBlended() );
		java.awt.Graphics2D g = this.textureRenderer.createGraphics();
		//		sgCustomTexture.paint( g );
		//this.textureRenderer.beginOrthoRendering( this.textureRenderer.getWidth(), this.textureRenderer.getHeight() );
		return g;

	}

	public void commitGraphics( java.awt.Graphics2D g, int x, int y, int width, int height ) {
		//this.textureRenderer.drawOrthoRect( width, height );
		//this.textureRenderer.sync( x, y, width, height );
		this.textureRenderer.markDirty( x, y, width, height );
		//this.textureRenderer.endOrthoRendering();
	}

	public java.awt.Image getImage() {
		return this.textureRenderer.getImage();
	}

	@Override
	protected com.jogamp.opengl.util.texture.TextureData newTextureData( com.jogamp.opengl.GL gl, com.jogamp.opengl.util.texture.TextureData currentTextureData ) {
		boolean isNewTextureRendererRequired;
		if( currentTextureData != null ) {
			if( this.textureRenderer != null ) {
				//todo: check mip mapping
				isNewTextureRendererRequired = ( currentTextureData.getWidth() != this.textureRenderer.getWidth() ) || ( currentTextureData.getHeight() != this.textureRenderer.getHeight() );
			} else {
				isNewTextureRendererRequired = true;
			}
		} else {
			isNewTextureRendererRequired = true;
		}
		if( isNewTextureRendererRequired ) {
			if( this.textureRenderer != null ) {
				this.textureRenderer.dispose();
			}
			this.textureRenderer = new com.jogamp.opengl.util.awt.TextureRenderer( owner.getWidth(), owner.getHeight(), owner.isPotentiallyAlphaBlended() );
		}
		java.awt.Graphics2D g = this.textureRenderer.createGraphics();
		owner.paint( g, this.textureRenderer.getWidth(), this.textureRenderer.getHeight() );
		g.dispose();
		this.textureRenderer.markDirty( 0, 0, this.textureRenderer.getWidth(), this.textureRenderer.getHeight() );

		if( owner.isMipMappingDesired() ) {
			java.awt.Image image = this.textureRenderer.getImage();
			if( image instanceof java.awt.image.BufferedImage ) {
				java.awt.image.BufferedImage bufferedImage = (java.awt.image.BufferedImage)image;
				if( owner.isPotentiallyAlphaBlended() ) {
					//pass
				} else {
					try {
						return newTextureData( gl, bufferedImage, true );
					} catch( AssertionError ae ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "unable to directly generate mipmapped texture." );
					} catch( RuntimeException re ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "unable to directly generate mipmapped texture." );
					}
				}
				java.awt.image.BufferedImage hackBI = new java.awt.image.BufferedImage( bufferedImage.getWidth(), bufferedImage.getHeight(), java.awt.image.BufferedImage.TYPE_4BYTE_ABGR_PRE );
				java.awt.Graphics2D hackG = hackBI.createGraphics();
				hackG.drawImage( bufferedImage, 0, 0, edu.cmu.cs.dennisc.image.ImageUtilities.accessImageObserver() );
				hackG.dispose();
				bufferedImage = hackBI;
				return newTextureData( gl, bufferedImage, true );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "textureRenderer.getTextureData()" );
				return null;
				//return this.textureRenderer.getTextureData();
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "textureRenderer.getTextureData()" );
			return null;
		}
	}

	private com.jogamp.opengl.util.awt.TextureRenderer textureRenderer = null;
}
