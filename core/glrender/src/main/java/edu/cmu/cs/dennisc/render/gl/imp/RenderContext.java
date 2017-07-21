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

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_NO_ERROR;
import static com.jogamp.opengl.GL.GL_REPEAT;
import static com.jogamp.opengl.GL.GL_SCISSOR_TEST;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL.GL_TEXTURE_WRAP_T;
import static com.jogamp.opengl.GL.GL_UNSIGNED_BYTE;
import static com.jogamp.opengl.GL2.GL_ABGR_EXT;
import static com.jogamp.opengl.GL2.GL_CLAMP;
import static com.jogamp.opengl.GL2ES1.GL_FOG;
import static com.jogamp.opengl.GL2ES1.GL_FOG_COLOR;
import static com.jogamp.opengl.GL2ES1.GL_LIGHT_MODEL_AMBIENT;
import static com.jogamp.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_COLOR_MATERIAL;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT0;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_NORMALIZE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import edu.cmu.cs.dennisc.render.gl.ForgettableBinding;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrGeometry;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrMultipleAppearance;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrTexture;

/**
 * @author Dennis Cosgrove
 */
public class RenderContext extends Context {
	public static interface UnusedTexturesListener {
		public void unusedTexturesCleared( com.jogamp.opengl.GL gl ); //todo: rename
	}

	private static final java.util.List<UnusedTexturesListener> unusedTexturesListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	public static void addUnusedTexturesListener( UnusedTexturesListener listener ) {
		unusedTexturesListeners.add( listener );
	}

	public static void removeUnusedTexturesListener( UnusedTexturesListener listener ) {
		unusedTexturesListeners.add( listener );
	}

	private final java.util.Map<GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry>, Integer> displayListMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture>, ForgettableBinding> textureBindingMap = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.List<Integer> toBeForgottenDisplayLists = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<ForgettableBinding> toBeForgottenTextures = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	private int lastTime_nextLightID = GL_LIGHT0;
	private int nextLightID;
	private boolean isFogEnabled;

	private final float[] ambient = new float[ 4 ];
	private final java.nio.FloatBuffer ambientBuffer = java.nio.FloatBuffer.wrap( this.ambient );

	private static final float[] s_color = new float[ 4 ];
	private static final java.nio.FloatBuffer s_colorBuffer = java.nio.FloatBuffer.wrap( s_color );

	private float globalBrightness = 1.0f;

	private GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> currDiffuseColorTextureAdapter;

	private boolean isShadingEnabled;

	private GlrMultipleAppearance multipleAppearanceAdapter;
	private int face;

	private final java.awt.Rectangle clearRect = new java.awt.Rectangle();

	private final edu.cmu.cs.dennisc.java.util.DStack<Float> globalOpacityStack = edu.cmu.cs.dennisc.java.util.Stacks.newStack();
	private float globalOpacity = 1.0f;

	public void pushGlobalOpacity() {
		this.globalOpacityStack.push( this.globalOpacity );
	}

	public void multiplyGlobalOpacity( float globalOpacity ) {
		this.globalOpacity *= globalOpacity;
	}

	public void popGlobalOpacity() {
		this.globalOpacity = this.globalOpacityStack.pop();
	}

	@Override
	public void initialize() {
		super.initialize();
		this.clearRect.setBounds( 0, 0, 0, 0 );
		this.globalOpacity = 1.0f;
		this.globalOpacityStack.clear();
	}

	@Override
	protected void enableNormalize() {
		this.gl.glEnable( GL_NORMALIZE );
	}

	@Override
	protected void disableNormalize() {
		this.gl.glDisable( GL_NORMALIZE );
	}

	public void renderLetterboxingIfNecessary( int width, int height ) {
		if( ( this.clearRect.x == 0 ) && ( this.clearRect.y == 0 ) && ( this.clearRect.width == width ) && ( this.clearRect.height == height ) ) {
			//			gl.glClearColor( 0, 0, 0, 1 );
			//			gl.glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		} else {
			gl.glEnable( GL_SCISSOR_TEST );
			gl.glClearColor( 0, 0, 0, 1 );
			try {
				if( this.clearRect.x > 0 ) {
					gl.glScissor( 0, 0, this.clearRect.x, height );
					gl.glClear( GL_COLOR_BUFFER_BIT );
				}
				if( ( this.clearRect.x + this.clearRect.width ) < width ) {
					gl.glScissor( this.clearRect.x + this.clearRect.width, 0, width - this.clearRect.width, height );
					gl.glClear( GL_COLOR_BUFFER_BIT );
				}
				if( this.clearRect.y > 0 ) {
					gl.glScissor( 0, 0, width, this.clearRect.y );
					gl.glClear( GL_COLOR_BUFFER_BIT );
				}
				if( ( this.clearRect.y + this.clearRect.height ) < height ) {
					gl.glScissor( 0, this.clearRect.y + this.clearRect.height, width, height - this.clearRect.height );
					gl.glClear( GL_COLOR_BUFFER_BIT );
				}
			} finally {
				gl.glDisable( GL_SCISSOR_TEST );
			}
		}
	}

	public void captureBuffers( java.awt.image.BufferedImage rvColor, java.nio.FloatBuffer rvDepth, boolean[] atIsUpsideDown ) {
		if( rvColor != null ) {
			int width = rvColor.getWidth();
			int height = rvColor.getHeight();
			java.awt.image.DataBuffer dataBuffer = rvColor.getRaster().getDataBuffer();
			if( rvDepth != null ) {
				byte[] color = ( (java.awt.image.DataBufferByte)dataBuffer ).getData();
				java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( color );
				gl.glReadPixels( 0, 0, width, height, GL_ABGR_EXT, GL_UNSIGNED_BYTE, buffer );

				gl.glReadPixels( 0, 0, width, height, GL_DEPTH_COMPONENT, GL_FLOAT, rvDepth );

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

				gl.glReadPixels( 0, 0, width, height, format, type, buffer );

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
					String description = glu.gluErrorString( errors.get( 0 ) );
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unable to capture back buffer:", description );
				}
			}
			if( atIsUpsideDown != null ) {
				atIsUpsideDown[ 0 ] = true;
			} else {
				com.jogamp.opengl.util.awt.ImageUtil.flipImageVertically( rvColor );
			}
		} else {
			throw new RuntimeException( "todo" );
		}
	}

	@Override
	protected void handleGLChange() {
		//forgetAllTextureAdapters();
		//forgetAllGeometryAdapters();
	}

	public void setMultipleAppearance( int face, GlrMultipleAppearance multipleAppearanceAdapter ) {
		this.face = face;
		this.multipleAppearanceAdapter = multipleAppearanceAdapter;
	}

	@Override
	public void setAppearanceIndex( int index ) {
		this.multipleAppearanceAdapter.setPipelineState( this, this.face, index );
	}

	public void actuallyForgetTexturesIfNecessary() {
		final int N = this.toBeForgottenTextures.size();
		if( N > 0 ) {
			synchronized( this.toBeForgottenTextures ) {
				//java.nio.IntBuffer ids = java.nio.IntBuffer.allocate( N );
				for( ForgettableBinding toBeForgottenTexture : this.toBeForgottenTextures ) {
					//toBeForgottenTexture.destroy( this.gl );
					toBeForgottenTexture.forget( this );
					//ids.put( toBeForgottenTexture );
					//System.out.println( "forget gl texture: " + toBeForgottenTexture );
				}
				//ids.rewind();
				//gl.glDeleteTextures( N, ids );
				this.toBeForgottenTextures.clear();
			}
		}
	}

	public void actuallyForgetDisplayListsIfNecessary() {
		final int N = this.toBeForgottenDisplayLists.size();
		if( N > 0 ) {
			synchronized( this.toBeForgottenDisplayLists ) {
				for( Integer toBeForgottenDisplayList : this.toBeForgottenDisplayLists ) {
					gl.glDeleteLists( toBeForgottenDisplayList, 1 );
					//System.out.println( "forget gl display list: " + toBeForgottenDisplayList );
				}
				this.toBeForgottenDisplayLists.clear();
			}
		}
	}

	public void beginAffectorSetup() {
		this.ambient[ 0 ] = 0;
		this.ambient[ 1 ] = 0;
		this.ambient[ 2 ] = 0;
		this.ambient[ 3 ] = 1;
		this.nextLightID = GL_LIGHT0;

		this.isFogEnabled = false;

		this.currDiffuseColorTextureAdapter = null;
	}

	public void endAffectorSetup() {
		this.ambient[ 0 ] *= this.globalBrightness;
		this.ambient[ 1 ] *= this.globalBrightness;
		this.ambient[ 2 ] *= this.globalBrightness;
		this.ambient[ 3 ] *= this.globalBrightness;

		gl.glLightModelfv( GL_LIGHT_MODEL_AMBIENT, this.ambientBuffer );
		for( int id = this.nextLightID; id < this.lastTime_nextLightID; id++ ) {
			gl.glDisable( id );
		}
		if( this.isFogEnabled ) {
			gl.glEnable( GL_FOG );
		} else {
			gl.glDisable( GL_FOG );
		}

		this.lastTime_nextLightID = this.nextLightID;

		gl.glEnable( GL_DEPTH_TEST );
		gl.glEnable( GL_COLOR_MATERIAL );
		gl.glEnable( GL_CULL_FACE );
		//		gl.glCullFace( GL_BACK );
	}

	public float getGlobalBrightness() {
		return this.globalBrightness;
	}

	public void setGlobalBrightness( float globalBrightness ) {
		this.globalBrightness = globalBrightness;
	}

	public float[] getAmbient( float[] rv ) {
		rv[ 0 ] = this.ambient[ 0 ];
		rv[ 1 ] = this.ambient[ 1 ];
		rv[ 2 ] = this.ambient[ 2 ];
		rv[ 3 ] = this.ambient[ 3 ];
		return rv;
	}

	public void setLightColor( int id, float[] color, float brightness ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = color[ 0 ] * brightness * this.globalBrightness;
			s_color[ 1 ] = color[ 1 ] * brightness * this.globalBrightness;
			s_color[ 2 ] = color[ 2 ] * brightness * this.globalBrightness;
			s_color[ 3 ] = color[ 3 ] * brightness * this.globalBrightness;
			gl.glLightfv( id, GL_DIFFUSE, s_colorBuffer );
			gl.glLightfv( id, GL_SPECULAR, s_colorBuffer );
		}
	}

	public void setFogColor( float[] fogColor ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = fogColor[ 0 ] * this.globalBrightness;
			s_color[ 1 ] = fogColor[ 1 ] * this.globalBrightness;
			s_color[ 2 ] = fogColor[ 2 ] * this.globalBrightness;
			s_color[ 3 ] = fogColor[ 3 ] * this.globalBrightness;
			gl.glFogfv( GL_FOG_COLOR, s_colorBuffer );
		}
	}

	public void setColor( float[] color, float opacity ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = color[ 0 ] * this.globalBrightness;
			s_color[ 1 ] = color[ 1 ] * this.globalBrightness;
			s_color[ 2 ] = color[ 2 ] * this.globalBrightness;
			s_color[ 3 ] = color[ 3 ] * opacity * this.globalOpacity;
			gl.glColor4fv( s_colorBuffer );
		}
	}

	public void setMaterial( int face, int name, float[] color, float opacity ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = color[ 0 ] * this.globalBrightness;
			s_color[ 1 ] = color[ 1 ] * this.globalBrightness;
			s_color[ 2 ] = color[ 2 ] * this.globalBrightness;
			s_color[ 3 ] = color[ 3 ] * opacity * this.globalOpacity;
			gl.glMaterialfv( face, name, s_colorBuffer );
		}
	}

	public void setClearColor( float[] color ) {
		gl.glClearColor( color[ 0 ] * this.globalBrightness, color[ 1 ] * this.globalBrightness, color[ 2 ] * this.globalBrightness, color[ 3 ] * this.globalBrightness );
	}

	public void setViewportAndAddToClearRect( java.awt.Rectangle viewport ) {
		gl.glViewport( viewport.x, viewport.y, viewport.width, viewport.height );
		if( ( this.clearRect.width == 0 ) || ( this.clearRect.height == 0 ) ) {
			this.clearRect.setBounds( viewport );
		} else {
			this.clearRect.union( viewport );
		}
	}

	public boolean isFogEnabled() {
		return this.isFogEnabled;
	}

	public void setIsFogEnabled( boolean isFogEnabled ) {
		this.isFogEnabled = isFogEnabled;
	}

	public void addAmbient( float[] color, float brightness ) {
		this.ambient[ 0 ] += color[ 0 ] * brightness;
		this.ambient[ 1 ] += color[ 1 ] * brightness;
		this.ambient[ 2 ] += color[ 2 ] * brightness;
	}

	public int getNextLightID() {
		int id = this.nextLightID;
		this.nextLightID++;
		return id;
	}

	public Integer getDisplayListID( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter ) {
		synchronized( this.displayListMap ) {
			Integer rv = this.displayListMap.get( geometryAdapter );
			//			if( this.gl.glIsList( rv ) ) {
			//				//pass
			//			} else {
			//				this.displayListMap.remove( geometryAdapter );
			//				rv = null;
			//			}
			return rv;
		}
	}

	public Integer generateDisplayListID( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter ) {
		Integer id = new Integer( gl.glGenLists( 1 ) );
		synchronized( this.displayListMap ) {
			this.displayListMap.put( geometryAdapter, id );
		}
		geometryAdapter.addRenderContext( this );
		return id;
	}

	private void forgetAllGeometryAdapters() {
		synchronized( this.displayListMap ) {
			for( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter : this.displayListMap.keySet() ) {
				forgetGeometryAdapter( geometryAdapter, false );
			}
			this.displayListMap.clear();
		}
	}

	public void forgetGeometryAdapter( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter, boolean removeFromMap ) {
		synchronized( this.displayListMap ) {
			Integer value = this.displayListMap.get( geometryAdapter );
			if( value != null ) {
				this.toBeForgottenDisplayLists.add( value );
				if( removeFromMap ) {
					this.displayListMap.remove( geometryAdapter );
				}
				geometryAdapter.removeRenderContext( this );
			} else {
				// todo?
			}
		}
	}

	public void forgetGeometryAdapter( GlrGeometry<? extends edu.cmu.cs.dennisc.scenegraph.Geometry> geometryAdapter ) {
		forgetGeometryAdapter( geometryAdapter, true );
	}

	private void forgetTextureBindingID( GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> textureAdapter, ForgettableBinding value, boolean removeFromMap ) {
		if( value != null ) {
			this.toBeForgottenTextures.add( value );
			if( removeFromMap ) {
				this.textureBindingMap.remove( textureAdapter );
			}
			textureAdapter.removeRenderContext( this );
			edu.cmu.cs.dennisc.java.util.logging.Logger.info( "texture adapter forgotten:", textureAdapter, value );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "no id for texture adapter:", textureAdapter );
		}
	}

	private void forgetAllTextureAdapters() {
		synchronized( this.textureBindingMap ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( this.textureBindingMap );
			for( GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> textureAdapter : this.textureBindingMap.keySet() ) {
				forgetTextureBindingID( textureAdapter, this.textureBindingMap.get( textureAdapter ), false );
			}
			this.textureBindingMap.clear();
		}
	}

	public void forgetTextureAdapter( GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> textureAdapter, boolean removeFromMap ) {
		synchronized( this.textureBindingMap ) {
			forgetTextureBindingID( textureAdapter, this.textureBindingMap.get( textureAdapter ), removeFromMap );
		}
	}

	public void forgetTextureAdapter( GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> textureAdapter ) {
		forgetTextureAdapter( textureAdapter, true );
	}

	public void forgetAllCachedItems() {
		this.forgetAllGeometryAdapters();
		this.forgetAllTextureAdapters();
	}

	public void clearUnusedTextures() {
		for( UnusedTexturesListener listener : unusedTexturesListeners ) {
			listener.unusedTexturesCleared( gl );
		}
	}

	//	//todo: better name
	//	public void put( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter, com.sun.opengl.util.texture.Texture glTexture ) {
	//		this.textureBindingMap.put( textureAdapter, glTexture );
	//	}

	public boolean isTextureEnabled() {
		return this.currDiffuseColorTextureAdapter != null;
	}

	//Forget information that might be used across renders
	//This is primarily for cases where we're rendering on the native side and setting up textures and whatnot
	public void clearDiffuseColorTextureAdapter() {
		this.currDiffuseColorTextureAdapter = null;
	}

	public void setDiffuseColorTextureAdapter( GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> diffuseColorTextureAdapter, boolean isDiffuseColorTextureClamped ) {
		if( ( diffuseColorTextureAdapter != null ) && diffuseColorTextureAdapter.isValid() ) {
			gl.glEnable( GL_TEXTURE_2D );
			if( this.currDiffuseColorTextureAdapter != diffuseColorTextureAdapter ) {
				diffuseColorTextureAdapter.bindTexture( this );
				//System.out.println("Bound texture "+diffuseColorTextureAdapter.hashCode()+", context: "+this.hashCode()+" to texture "+texture.getTextureObject());

				int value = isDiffuseColorTextureClamped ? GL_CLAMP : GL_REPEAT;
				gl.glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, value );
				gl.glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, value );
				//				gl.glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR );
				//				gl.glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
				this.currDiffuseColorTextureAdapter = diffuseColorTextureAdapter;
			}
		} else {
			gl.glDisable( GL_TEXTURE_2D );
			this.currDiffuseColorTextureAdapter = null;
		}
	}

	public void setBumpTextureAdapter( GlrTexture<? extends edu.cmu.cs.dennisc.texture.Texture> bumpTextureAdapter ) {
		if( ( bumpTextureAdapter != null ) && bumpTextureAdapter.isValid() ) {
			//todo:
			//String extensions = gl.glGetString(GL_EXTENSIONS);
		}
	}

	public boolean isShadingEnabled() {
		return this.isShadingEnabled;
	}

	public void setIsShadingEnabled( boolean isShadingEnabled ) {
		this.isShadingEnabled = isShadingEnabled;
		if( this.isShadingEnabled ) {
			// todo: handle this condition globally
			// if( this.currMaxLightID == INIT_LIGHT_ID ) {
			// gl.glDisable( GL_LIGHTING );
			// } else {
			// gl.glEnable( GL_LIGHTING );
			// }

			gl.glEnable( GL_LIGHTING );
		} else {
			gl.glDisable( GL_LIGHTING );
		}
	}

	public float getURatio() {
		if( this.currDiffuseColorTextureAdapter != null ) {
			return this.currDiffuseColorTextureAdapter.mapU( 1 );
		} else {
			return Float.NaN;
		}
	}

	public float getVRatio() {
		if( this.currDiffuseColorTextureAdapter != null ) {
			return this.currDiffuseColorTextureAdapter.mapV( 1 );
		} else {
			return Float.NaN;
		}
	}

	public void renderVertex( edu.cmu.cs.dennisc.scenegraph.Vertex vertex ) {
		if( this.currDiffuseColorTextureAdapter != null ) {
			if( vertex.textureCoordinate0.isNaN() == false ) {
				float u = this.currDiffuseColorTextureAdapter.mapU( vertex.textureCoordinate0.u );
				float v = this.currDiffuseColorTextureAdapter.mapV( vertex.textureCoordinate0.v );
				gl.glTexCoord2f( u, v );
			}
		}
		if( vertex.diffuseColor.isNaN() == false ) {
			gl.glColor4f( vertex.diffuseColor.red, vertex.diffuseColor.green, vertex.diffuseColor.blue, vertex.diffuseColor.alpha );
		}
		if( this.isShadingEnabled ) {
			gl.glNormal3f( vertex.normal.x, vertex.normal.y, vertex.normal.z );
		}
		gl.glVertex3d( vertex.position.x, vertex.position.y, vertex.position.z );
	}
}
