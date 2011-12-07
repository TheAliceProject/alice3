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

/**
 * @author Dennis Cosgrove
 */
public abstract class TextureBinding {
	private class RenderContextData {
		private com.sun.opengl.util.texture.Texture texture;
		private javax.media.opengl.GL gl;
		public boolean isNewTextureRequired( javax.media.opengl.GL gl ) {
			if( this.texture != null ) {
				int textureObject = this.texture.getTextureObject();
				if( this.gl != gl ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.info( "gl changed", this.gl, gl );
					return true;
				} else {
					if( gl.glIsTexture( textureObject ) ) {
						return false;
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.info( "glIsTexture is false" );
						return true;
					}
				}
			} else {
				return true;
			}
		}
		public void update( javax.media.opengl.GL gl ) {
			com.sun.opengl.util.texture.Texture nextTexture = newTexture( gl, this.texture );
			if( this.texture != nextTexture ) {
				if( this.texture != null ) {
					this.texture.dispose();
					edu.cmu.cs.dennisc.java.util.logging.Logger.info( "dispose", this.texture );
				}
				this.texture = nextTexture;
				this.gl = gl;
			}
		}
		public void bind() {
			this.texture.bind();
		}
		public void enable() {
			this.texture.enable();
		}
		public void forget() {
			if( this.texture != null ) {
				this.texture.dispose();
				edu.cmu.cs.dennisc.java.util.logging.Logger.info( "dispose", this.texture );
			}
		}
	}

	private final java.util.Map< RenderContext, RenderContextData > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	protected abstract com.sun.opengl.util.texture.Texture newTexture( javax.media.opengl.GL gl, com.sun.opengl.util.texture.Texture currentTexture );

	private RenderContextData getData( RenderContext rc ) {
		RenderContextData rv = this.map.get( rc );
		if( rv != null ) {
			//pass
		} else {
			rv = new RenderContextData();
			this.map.put( rc, rv );
		}
		return rv;
	}
	public void ensureUpToDate( RenderContext rc, boolean isDirty ) {
		RenderContextData data = this.getData( rc );
		if( isDirty || data.isNewTextureRequired( rc.gl ) ) {
			data.update( rc.gl );
		}
		data.bind();
		data.enable();
	}
	public void forget( RenderContext rc ) {
		synchronized( this.map ) {
			RenderContextData data = this.map.get( rc );
			if( data != null ) {
				data.forget();
				this.map.remove( rc );
			}
		}
	}
}
