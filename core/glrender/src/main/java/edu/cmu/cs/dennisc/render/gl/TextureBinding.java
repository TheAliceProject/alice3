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

package edu.cmu.cs.dennisc.render.gl;

import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public final class TextureBinding implements ForgettableBinding {
	//todo: investigate shared drawables
	private static class Data {
		private com.jogamp.opengl.util.texture.Texture texture;
		private com.jogamp.opengl.util.texture.TextureData textureData;
		private com.jogamp.opengl.GL gl;

		private void disposeTextureIdIfAppropriate( com.jogamp.opengl.GL gl ) {
			if( this.texture != null ) {
				if( this.gl != gl ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.info( "disposing texture id", this.texture.getTextureObject( this.gl ) );
					this.texture.destroy( this.gl );
				}
			}
		}

		private boolean isUpdateNecessary( com.jogamp.opengl.GL gl, com.jogamp.opengl.util.texture.TextureData textureData ) {
			if( this.texture != null ) {
				if( this.textureData != textureData ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.info( "textureData changed", this.textureData, textureData );
					return true;
				} else {
					if( this.gl != gl ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.info( "gl changed", this.gl, gl );
						return true;
					} else {
						int textureObject = this.texture.getTextureObject( this.gl );
						if( gl.glIsTexture( textureObject ) ) {
							return false;
						} else {
							edu.cmu.cs.dennisc.java.util.logging.Logger.info( "glIsTexture is false" );
							return true;
						}
					}
				}
			} else {
				return true;
			}
		}

		public void updateIfNecessary( com.jogamp.opengl.GL gl, com.jogamp.opengl.util.texture.TextureData textureData ) {
			if( this.isUpdateNecessary( gl, textureData ) ) {
				this.disposeTextureIdIfAppropriate( gl );
				this.textureData = textureData;
				this.gl = gl;
				this.texture = com.jogamp.opengl.util.texture.TextureIO.newTexture( this.textureData );
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "allocated texture id", this.texture.getTextureObject( this.gl ), "gl", gl.hashCode() );
				//				for( RenderContextData value : map.values() ) {
				//					System.err.print( value.gl.hashCode() + " " );
				//				}
				//				System.err.println();
			}
		}

		public void bind() {
			this.texture.bind( this.gl );
		}

		public void enable() {
			this.texture.enable( this.gl );
		}

		public void forget( com.jogamp.opengl.GL gl ) {
			if( this.texture != null ) {
				this.disposeTextureIdIfAppropriate( gl );
				this.texture = null;
				this.textureData = null;
				this.gl = null;
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "dispose", this.texture );
			}
		}
	}

	private final java.util.Map<RenderContext, Data> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private Data getData( RenderContext rc ) {
		Data rv = this.map.get( rc );
		if( rv != null ) {
			//pass
		} else {
			rv = new Data();
			this.map.put( rc, rv );
		}
		return rv;
	}

	public void ensureUpToDate( RenderContext rc, com.jogamp.opengl.util.texture.TextureData textureData ) {
		Data data = this.getData( rc );
		data.updateIfNecessary( rc.gl, textureData );
		data.bind();
		data.enable();
	}

	@Override
	public void forget( RenderContext rc ) {
		synchronized( this.map ) {
			Data data = this.map.get( rc );
			if( data != null ) {
				data.forget( rc.gl );
				this.map.remove( rc );
			}
		}
	}
}
