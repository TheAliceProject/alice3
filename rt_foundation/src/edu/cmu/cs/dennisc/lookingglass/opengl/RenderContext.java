/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.lookingglass.opengl;

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
public class RenderContext extends Context {
	public static final long MAX_UNSIGNED_INTEGER = 0xFFFFFFFFL;

	private int m_lastTime_nextLightID = GL.GL_LIGHT0;
	private int m_nextLightID;
	private boolean m_isFogEnabled;

	private float[] m_ambient = new float[ 4 ];
	private java.nio.FloatBuffer m_ambientBuffer = java.nio.FloatBuffer.wrap( m_ambient );

	private java.util.HashMap< GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry >, Integer > m_displayListMap = new java.util.HashMap< GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry >, Integer >();
	private java.util.HashMap< TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture >, com.sun.opengl.util.texture.Texture > m_textureBindingMap = new java.util.HashMap< TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture >, com.sun.opengl.util.texture.Texture >();

	private java.util.List< com.sun.opengl.util.texture.Texture > m_toBeForgottenTextures = new java.util.LinkedList< com.sun.opengl.util.texture.Texture >();
	private java.util.List< Integer > m_toBeForgottenDisplayLists = new java.util.LinkedList< Integer >();

	private TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > m_currDiffuseColorTextureAdapter;
	//private TextureAdapter m_bumpTextureAdapter;

	private boolean m_isShadingEnabled;

	private java.awt.Rectangle m_clearRect = new java.awt.Rectangle();

	private java.util.Stack< Float > m_globalOpacityStack = new java.util.Stack< Float >();
	private float m_globalOpacity = 1.0f;

	//	public float peekGlobalOpacity() {
	//		return m_globalOpacity;
	//	}
	public void pushGlobalOpacity() {
		m_globalOpacityStack.push( m_globalOpacity );
	}
	//	public void loadGlobalOpacity( float globalOpacity ) {
	//		m_globalOpacity = globalOpacity;
	//	}
	public void multiplyGlobalOpacity( float globalOpacity ) {
		m_globalOpacity *= globalOpacity;
	}
	public void popGlobalOpacity() {
		m_globalOpacity = m_globalOpacityStack.pop();
	}

	public void initialize() {
		m_clearRect.setBounds( 0, 0, 0, 0 );
		m_globalOpacity = 1.0f;
		m_globalOpacityStack.clear();
	}
	public void renderLetterboxingIfNecessary( int width, int height ) {
		if( m_clearRect.x == 0 && m_clearRect.y == 0 && m_clearRect.width == width && m_clearRect.height == height ) {
			//			gl.glClearColor( 0, 0, 0, 1 );
			//			gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		} else {
			gl.glEnable( GL.GL_SCISSOR_TEST );
			gl.glClearColor( 0, 0, 0, 1 );
			try {
				if( m_clearRect.x > 0 ) {
					gl.glScissor( 0, 0, m_clearRect.x, height );
					gl.glClear( GL.GL_COLOR_BUFFER_BIT );
				}
				if( (m_clearRect.x + m_clearRect.width) < width ) {
					gl.glScissor( m_clearRect.x + m_clearRect.width, 0, width - m_clearRect.width, height );
					gl.glClear( GL.GL_COLOR_BUFFER_BIT );
				}
				if( m_clearRect.y > 0 ) {
					gl.glScissor( 0, 0, width, m_clearRect.y );
					gl.glClear( GL.GL_COLOR_BUFFER_BIT );
				}
				if( (m_clearRect.y + m_clearRect.height) < height ) {
					gl.glScissor( 0, m_clearRect.y + m_clearRect.height, width, height - m_clearRect.height );
					gl.glClear( GL.GL_COLOR_BUFFER_BIT );
				}
			} finally {
				gl.glDisable( GL.GL_SCISSOR_TEST );
			}
		}
	}

	public void captureBuffers( java.awt.image.BufferedImage rvColor, java.nio.FloatBuffer rvDepth ) {
		if( rvColor != null ) {
			int width = rvColor.getWidth();
			int height = rvColor.getHeight();
			java.awt.image.DataBuffer dataBuffer = rvColor.getRaster().getDataBuffer();
			if( rvDepth != null ) {
				byte[] color = ((java.awt.image.DataBufferByte)dataBuffer).getData();
				java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( color );
				gl.glReadPixels( 0, 0, width, height, GL.GL_ABGR_EXT, GL.GL_UNSIGNED_BYTE, buffer );
				
				gl.glReadPixels( 0, 0, width, height, GL.GL_DEPTH_COMPONENT, GL.GL_FLOAT, rvDepth );
				
				final byte ON = (byte)0;
				final byte OFF = (byte)255;
				int i=0;
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
				java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( ((java.awt.image.DataBufferByte)dataBuffer).getData() );
				//todo
				//gl.glReadPixels( 0, 0, width, height, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, buffer );
				gl.glReadPixels( 0, 0, width, height, GL.GL_ABGR_EXT, GL.GL_UNSIGNED_BYTE, buffer );
			}
			com.sun.opengl.util.ImageUtil.flipImageVertically( rvColor );
		} else {
			throw new RuntimeException( "todo" );
		}
	}
	@Override
	protected void handleGLChange() {
		forgetAllTextureAdapters();
		forgetAllGeometryAdapters();
	}

	private int m_face;
	private MultipleAppearanceAdapter m_multipleAppearanceAdapter;

	public void setMultipleAppearance( int face, MultipleAppearanceAdapter multipleAppearanceAdapter ) {
		m_face = face;
		m_multipleAppearanceAdapter = multipleAppearanceAdapter;
	}
	@Override
	public void setAppearanceIndex( int index ) {
		m_multipleAppearanceAdapter.setPipelineState( this, m_face, index );
	}

	public void actuallyForgetTexturesIfNecessary() {
		final int N = m_toBeForgottenTextures.size();
		if( N > 0 ) {
			synchronized( m_toBeForgottenTextures ) {
				//todo
				//				java.nio.IntBuffer ids = java.nio.IntBuffer.allocate( N );
				//				for( Integer toBeForgottenTexture : m_toBeForgottenTextures ) {
				//					ids.put( toBeForgottenTexture );
				//					System.out.println( "forget gl texture: " + toBeForgottenTexture );
				//				}
				//				ids.rewind();
				//				gl.glDeleteTextures( N, ids );
				m_toBeForgottenTextures.clear();
			}
		}
	}
	public void actuallyForgetDisplayListsIfNecessary() {
		final int N = m_toBeForgottenDisplayLists.size();
		if( N > 0 ) {
			synchronized( m_toBeForgottenDisplayLists ) {
				for( Integer toBeForgottenDisplayList : m_toBeForgottenDisplayLists ) {
					gl.glDeleteLists( toBeForgottenDisplayList, 1 );
					System.out.println( "forget gl display list: " + toBeForgottenDisplayList );
				}
				m_toBeForgottenDisplayLists.clear();
			}
		}
	}

	public void beginAffectorSetup() {
		m_ambient[ 0 ] = 0;
		m_ambient[ 1 ] = 0;
		m_ambient[ 2 ] = 0;
		m_ambient[ 3 ] = 1;
		m_nextLightID = GL.GL_LIGHT0;

		m_isFogEnabled = false;

		m_currDiffuseColorTextureAdapter = null;
	}
	public void endAffectorSetup() {
		m_ambient[ 0 ] *= m_globalBrightness;
		m_ambient[ 1 ] *= m_globalBrightness;
		m_ambient[ 2 ] *= m_globalBrightness;
		m_ambient[ 3 ] *= m_globalBrightness;

		gl.glLightModelfv( GL.GL_LIGHT_MODEL_AMBIENT, m_ambientBuffer );
		for( int id = m_nextLightID; id < m_lastTime_nextLightID; id++ ) {
			gl.glDisable( id );
		}
		if( m_isFogEnabled ) {
			gl.glEnable( GL.GL_FOG );
		} else {
			gl.glDisable( GL.GL_FOG );
		}

		m_lastTime_nextLightID = m_nextLightID;

		gl.glEnable( GL.GL_DEPTH_TEST );
		gl.glEnable( GL.GL_COLOR_MATERIAL );
		gl.glEnable( GL.GL_CULL_FACE );
		//		gl.glCullFace( GL.GL_BACK );
	}

	private float m_globalBrightness = 1.0f;

	public void setGlobalBrightness( float globalBrightness ) {
		m_globalBrightness = globalBrightness;
	}

	private static float[] s_color = new float[ 4 ];
	private static java.nio.FloatBuffer s_colorBuffer = java.nio.FloatBuffer.wrap( s_color );

	public void setLightColor( int id, float[] color, float brightness ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = color[ 0 ] * brightness * m_globalBrightness;
			s_color[ 1 ] = color[ 1 ] * brightness * m_globalBrightness;
			s_color[ 2 ] = color[ 2 ] * brightness * m_globalBrightness;
			s_color[ 3 ] = color[ 3 ] * brightness * m_globalBrightness;
			gl.glLightfv( id, GL.GL_DIFFUSE, s_colorBuffer );
			gl.glLightfv( id, GL.GL_SPECULAR, s_colorBuffer );
		}
	}
	public void setFogColor( float[] fogColor ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = fogColor[ 0 ] * m_globalBrightness;
			s_color[ 1 ] = fogColor[ 1 ] * m_globalBrightness;
			s_color[ 2 ] = fogColor[ 2 ] * m_globalBrightness;
			s_color[ 3 ] = fogColor[ 3 ] * m_globalBrightness;
			gl.glFogfv( GL.GL_FOG_COLOR, s_colorBuffer );
		}
	}
	public void setColor( float[] color, float opacity ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = color[ 0 ] * m_globalBrightness;
			s_color[ 1 ] = color[ 1 ] * m_globalBrightness;
			s_color[ 2 ] = color[ 2 ] * m_globalBrightness;
			s_color[ 3 ] = color[ 3 ] * opacity * m_globalOpacity;
			gl.glColor4fv( s_colorBuffer );
		}
	}
	public void setMaterial( int face, int name, float[] color, float opacity ) {
		synchronized( s_colorBuffer ) {
			s_color[ 0 ] = color[ 0 ] * m_globalBrightness;
			s_color[ 1 ] = color[ 1 ] * m_globalBrightness;
			s_color[ 2 ] = color[ 2 ] * m_globalBrightness;
			s_color[ 3 ] = color[ 3 ] * opacity * m_globalOpacity;
			gl.glMaterialfv( face, name, s_colorBuffer );
		}
	}
	public void setClearColor( float[] color ) {
		gl.glClearColor( color[ 0 ] * m_globalBrightness, color[ 1 ] * m_globalBrightness, color[ 2 ] * m_globalBrightness, color[ 3 ] * m_globalBrightness );
	}

	public void setViewportAndAddToClearRect( java.awt.Rectangle viewport ) {
		gl.glViewport( viewport.x, viewport.y, viewport.width, viewport.height );
		if( m_clearRect.width == 0 || m_clearRect.height == 0 ) {
			m_clearRect.setBounds( viewport );
		} else {
			m_clearRect.union( viewport );
		}
	}

	public boolean isFogEnabled() {
		return m_isFogEnabled;
	}
	public void setIsFogEnabled( boolean isFogEnabled ) {
		m_isFogEnabled = isFogEnabled;
	}
	public void addAmbient( float[] color, float brightness ) {
		m_ambient[ 0 ] += color[ 0 ] * brightness;
		m_ambient[ 1 ] += color[ 1 ] * brightness;
		m_ambient[ 2 ] += color[ 2 ] * brightness;
	}
	public int getNextLightID() {
		int id = m_nextLightID;
		m_nextLightID++;
		return id;
	}

	public Integer getDisplayListID( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter ) {
		synchronized( m_displayListMap ) {
			return m_displayListMap.get( geometryAdapter );
		}
	}
	public Integer generateDisplayListID( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter ) {
		Integer id = new Integer( gl.glGenLists( 1 ) );
		synchronized( m_displayListMap ) {
			m_displayListMap.put( geometryAdapter, id );
		}
		geometryAdapter.addRenderContext( this );
		return id;
	}

	private void forgetAllGeometryAdapters() {
		synchronized( m_displayListMap ) {
			for( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter : m_displayListMap.keySet() ) {
				forgetGeometryAdapter( geometryAdapter, false );
			}
			m_displayListMap.clear();
		}
	}

	public void forgetGeometryAdapter( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter, boolean removeFromMap ) {
		synchronized( m_displayListMap ) {
			Integer value = m_displayListMap.get( geometryAdapter );
			if( value != null ) {
				m_toBeForgottenDisplayLists.add( value );
				if( removeFromMap ) {
					m_displayListMap.remove( geometryAdapter );
				}
				geometryAdapter.removeRenderContext( this );
			} else {
				// todo?
			}
		}
	}
	public void forgetGeometryAdapter( GeometryAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.Geometry > geometryAdapter ) {
		forgetGeometryAdapter( geometryAdapter, true );
	}

	private void forgetTextureBindingID( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter, com.sun.opengl.util.texture.Texture value, boolean removeFromMap ) {
		if( value != null ) {
			m_toBeForgottenTextures.add( value );
			if( removeFromMap ) {
				m_textureBindingMap.remove( textureAdapter );
			}
			textureAdapter.removeRenderContext( this );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "FYI: texture adapter forgotten:", textureAdapter, value );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: no id for texture adapter:", textureAdapter );
		}
	}

	private void forgetAllTextureAdapters() {
		synchronized( m_textureBindingMap ) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println( m_textureBindingMap );
			for( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter : m_textureBindingMap.keySet() ) {
				forgetTextureBindingID( textureAdapter, m_textureBindingMap.get( textureAdapter ), false );
			}
			m_textureBindingMap.clear();
		}
	}

	public void forgetTextureAdapter( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter, boolean removeFromMap ) {
		synchronized( m_textureBindingMap ) {
			forgetTextureBindingID( textureAdapter, m_textureBindingMap.get( textureAdapter ), removeFromMap );
		}
	}
	public void forgetTextureAdapter( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter ) {
		forgetTextureAdapter( textureAdapter, true );
	}

	//todo: better name
	public void put( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > textureAdapter, com.sun.opengl.util.texture.Texture glTexture ) {
		m_textureBindingMap.put( textureAdapter, glTexture );
	}

	public void setDiffuseColorTextureAdapter( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > diffuseColorTextureAdapter ) {
		if( diffuseColorTextureAdapter != null && diffuseColorTextureAdapter.isValid() ) {
			gl.glEnable( GL.GL_TEXTURE_2D );
			if( m_currDiffuseColorTextureAdapter != diffuseColorTextureAdapter ) {
				com.sun.opengl.util.texture.Texture texture = diffuseColorTextureAdapter.getTexture( this );
				texture.bind();
				texture.enable();
				//todo: allow clamp
				gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
				gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
				m_currDiffuseColorTextureAdapter = diffuseColorTextureAdapter;
			}
		} else {
			gl.glDisable( GL.GL_TEXTURE_2D );
		}
	}
	public void setBumpTextureAdapter( TextureAdapter< ? extends edu.cmu.cs.dennisc.texture.Texture > bumpTextureAdapter ) {
		if( bumpTextureAdapter != null && bumpTextureAdapter.isValid() ) {
			//todo:
			//String extensions = gl.glGetString(GL.GL_EXTENSIONS);
		}
	}

	public boolean isShadingEnabled() {
		return m_isShadingEnabled;
	}
	public void setIsShadingEnabled( boolean isShadingEnabled ) {
		m_isShadingEnabled = isShadingEnabled;
		if( m_isShadingEnabled ) {
			// todo: handle this condition globally
			// if( m_currMaxLightID == INIT_LIGHT_ID ) {
			// gl.glDisable( GL.GL_LIGHTING );
			// } else {
			// gl.glEnable( GL.GL_LIGHTING );
			// }

			gl.glEnable( GL.GL_LIGHTING );
		} else {
			gl.glDisable( GL.GL_LIGHTING );
		}
	}

	public float getURatio() {
		if( m_currDiffuseColorTextureAdapter != null ) {
			return m_currDiffuseColorTextureAdapter.mapU( 1 );
		} else {
			return Float.NaN;
		}
	}
	public float getVRatio() {
		if( m_currDiffuseColorTextureAdapter != null ) {
			return m_currDiffuseColorTextureAdapter.mapV( 1 );
		} else {
			return Float.NaN;
		}
	}

	protected void renderVertex( edu.cmu.cs.dennisc.scenegraph.Vertex vertex ) {
		if( m_currDiffuseColorTextureAdapter != null ) {
			if( vertex.textureCoordinate0.isNaN() == false ) {
				float u = m_currDiffuseColorTextureAdapter.mapU( vertex.textureCoordinate0.u );
				float v = m_currDiffuseColorTextureAdapter.mapV( vertex.textureCoordinate0.v );
				gl.glTexCoord2f( u, v );
			}
		}
		if( vertex.diffuseColor.isNaN() == false ) {
			gl.glColor4f( vertex.diffuseColor.red, vertex.diffuseColor.green, vertex.diffuseColor.blue, vertex.diffuseColor.alpha );
		}
		if( m_isShadingEnabled ) {
			gl.glNormal3f( vertex.normal.x, vertex.normal.y, vertex.normal.z );
		}
		gl.glVertex3d( vertex.position.x, vertex.position.y, vertex.position.z );
	}
}
