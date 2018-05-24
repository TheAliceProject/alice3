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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.util.awt.TextureRenderer;
import com.jogamp.opengl.util.texture.TextureData;
import edu.cmu.cs.dennisc.image.ImageUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.texture.CustomTexture;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class GlrCustomTexture extends GlrTexture<CustomTexture> {
	@Override
	protected boolean isDirty() {
		return owner.isAnimated() || super.isDirty();
	}

	public Graphics2D createGraphics() {
		//		edu.cmu.cs.dennisc.scenegraph.CustomTexture sgCustomTexture = this.sgE;
		//		if( this.textureRenderer != null ) {
		//			this.textureRenderer.dispose();
		//		}
		//		this.textureRenderer = new com.sun.opengl.util.awt.TextureRenderer( sgCustomTexture.getWidth(), sgCustomTexture.getHeight(), sgCustomTexture.isPotentiallyAlphaBlended() );
		Graphics2D g = this.textureRenderer.createGraphics();
		//		sgCustomTexture.paint( g );
		//this.textureRenderer.beginOrthoRendering( this.textureRenderer.getWidth(), this.textureRenderer.getHeight() );
		return g;

	}

	public void commitGraphics( Graphics2D g, int x, int y, int width, int height ) {
		//this.textureRenderer.drawOrthoRect( width, height );
		//this.textureRenderer.sync( x, y, width, height );
		this.textureRenderer.markDirty( x, y, width, height );
		//this.textureRenderer.endOrthoRendering();
	}

	public Image getImage() {
		return this.textureRenderer.getImage();
	}

	@Override
	protected TextureData newTextureData( GL gl, TextureData currentTextureData ) {
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
			this.textureRenderer = new TextureRenderer( owner.getWidth(), owner.getHeight(), owner.isPotentiallyAlphaBlended() );
		}
		Graphics2D g = this.textureRenderer.createGraphics();
		owner.paint( g, this.textureRenderer.getWidth(), this.textureRenderer.getHeight() );
		g.dispose();
		this.textureRenderer.markDirty( 0, 0, this.textureRenderer.getWidth(), this.textureRenderer.getHeight() );

		if( owner.isMipMappingDesired() ) {
			Image image = this.textureRenderer.getImage();
			if( image instanceof BufferedImage ) {
				BufferedImage bufferedImage = (BufferedImage)image;
				if( owner.isPotentiallyAlphaBlended() ) {
					//pass
				} else {
					try {
						return newTextureData( gl, bufferedImage, true );
					} catch( AssertionError ae ) {
						Logger.warning( "unable to directly generate mipmapped texture." );
					} catch( RuntimeException re ) {
						Logger.warning( "unable to directly generate mipmapped texture." );
					}
				}
				BufferedImage hackBI = new BufferedImage( bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE );
				Graphics2D hackG = hackBI.createGraphics();
				hackG.drawImage( bufferedImage, 0, 0, ImageUtilities.accessImageObserver() );
				hackG.dispose();
				bufferedImage = hackBI;
				return newTextureData( gl, bufferedImage, true );
			} else {
				Logger.todo( "textureRenderer.getTextureData()" );
				return null;
				//return this.textureRenderer.getTextureData();
			}
		} else {
			Logger.todo( "textureRenderer.getTextureData()" );
			return null;
		}
	}

	private TextureRenderer textureRenderer = null;
}
