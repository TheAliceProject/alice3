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

import javax.media.opengl.GL;

/**
 * @author Dennis Cosgrove
 */
public class SingleAppearanceAdapter extends AppearanceAdapter< edu.cmu.cs.dennisc.scenegraph.SingleAppearance > {
	private boolean m_isShaded;
	private boolean m_isAmbientLinkedToDiffuse;
	private boolean m_isMaterialActuallyShowing;
	private boolean m_isMaterialAlphaBlended;
	private float[] m_ambient = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float[] m_diffuse = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float[] m_specular = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float[] m_emissive = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float m_opacity = Float.NaN;
	private float m_shininess = Float.NaN;
	private int m_polygonMode;
	private TextureAdapter<? extends edu.cmu.cs.dennisc.texture.Texture> m_diffuseColorTextureAdapter;
	private boolean m_isDiffuseColorTextureAlphaBlended;
	private boolean m_isDiffuseColorTextureClamped;
	private TextureAdapter<? extends edu.cmu.cs.dennisc.texture.Texture> m_bumpTextureAdapter;
	private boolean m_isEthereal;

	@Override
	public boolean isActuallyShowing() {
		return m_isMaterialActuallyShowing;
	}
	@Override
	public boolean isAlphaBlended() {
		return m_isMaterialAlphaBlended || m_isDiffuseColorTextureAlphaBlended;
	}
	@Override
	public boolean isEthereal() {
		return m_isEthereal;
	}

	@Override
	public void setPipelineState( RenderContext rc, int face ) {
		rc.setIsShadingEnabled( m_isShaded );
		//todo: investigate if specular, emissive, ambient should use an opacity of 1.0f
		if( m_isShaded ) {
			rc.gl.glMaterialf( face, GL.GL_SHININESS, m_shininess );
			rc.setMaterial( face, GL.GL_SPECULAR, m_specular, m_opacity );
			rc.setMaterial( face, GL.GL_EMISSION, m_emissive, m_opacity );
			if( m_isAmbientLinkedToDiffuse ) {
				rc.gl.glColorMaterial( face, GL.GL_AMBIENT_AND_DIFFUSE );
			} else {
				rc.setMaterial( face, GL.GL_AMBIENT, m_ambient, m_opacity );
				rc.gl.glColorMaterial( face, GL.GL_DIFFUSE );
			}
		}
		rc.setColor( m_diffuse, m_opacity );
		rc.gl.glPolygonMode( face, m_polygonMode );
		rc.setDiffuseColorTextureAdapter( m_diffuseColorTextureAdapter, m_isDiffuseColorTextureClamped );
		rc.setBumpTextureAdapter( m_bumpTextureAdapter );
		//todo
		if( m_isEthereal ) {
			rc.gl.glDepthFunc( GL.GL_LEQUAL );
		} else {
			rc.gl.glDepthFunc( GL.GL_LESS );
		}
	}

	private void updateOpacityRelatedBooleans() {
		float opacity = m_opacity * m_diffuse[ 3 ];
		m_isMaterialActuallyShowing = opacity > 0.0f;
		m_isMaterialAlphaBlended = opacity < 1.0f;
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.ambientColor ) {
			m_element.ambientColor.getValue().getAsArray( m_ambient );
			m_isAmbientLinkedToDiffuse = Float.isNaN( m_ambient[ 0 ] );
		} else if( property == m_element.diffuseColor ) {
			m_element.diffuseColor.getValue().getAsArray( m_diffuse );
			updateOpacityRelatedBooleans();
		} else if( property == m_element.fillingStyle ) {
			edu.cmu.cs.dennisc.scenegraph.FillingStyle fillingStyle = m_element.fillingStyle.getValue();
			if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.SOLID ) ) {
				m_polygonMode = GL.GL_FILL;
			} else if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME ) ) {
				m_polygonMode = GL.GL_LINE;
			} else if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.POINTS ) ) {
				m_polygonMode = GL.GL_POINT;
			} else {
				throw new RuntimeException();
			}
		} else if( property == m_element.shadingStyle ) {
			edu.cmu.cs.dennisc.scenegraph.ShadingStyle shadingStyle = m_element.shadingStyle.getValue();
			if( shadingStyle == null || shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE ) ) {
				m_isShaded = false;
			} else if( shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.FLAT ) ) {
				m_isShaded = true;
				//todo
			} else if( shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.SMOOTH ) ) {
				m_isShaded = true;
				//todo
			} else {
				throw new RuntimeException();
			}
		} else if( property == m_element.opacity ) {
			m_opacity = m_element.opacity.getValue();
			updateOpacityRelatedBooleans();
		} else if( property == m_element.specularHighlightColor ) {
			m_element.specularHighlightColor.getValue().getAsArray( m_specular );
		} else if( property == m_element.specularHighlightExponent ) {
			m_shininess = m_element.specularHighlightExponent.getValue();
		} else if( property == m_element.emissiveColor ) {
			m_element.emissiveColor.getValue().getAsArray( m_emissive );
		} else if( property == m_element.diffuseColorTexture ) {
			m_diffuseColorTextureAdapter = AdapterFactory.getAdapterFor( m_element.diffuseColorTexture.getValue() );
		} else if( property == m_element.isDiffuseColorTextureAlphaBlended ) {
			m_isDiffuseColorTextureAlphaBlended = m_element.isDiffuseColorTextureAlphaBlended.getValue();
		} else if( property == m_element.isDiffuseColorTextureClamped ) {
			m_isDiffuseColorTextureClamped = m_element.isDiffuseColorTextureClamped.getValue();
		} else if( property == m_element.bumpTexture ) {
			m_bumpTextureAdapter = AdapterFactory.getAdapterFor( m_element.bumpTexture.getValue() );
		} else if( property == m_element.isEthereal ) {
			m_isEthereal = m_element.isEthereal.getValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
