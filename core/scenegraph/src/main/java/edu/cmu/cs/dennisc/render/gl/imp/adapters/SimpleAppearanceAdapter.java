package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LESS;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL2GL3.GL_FILL;
import static javax.media.opengl.GL2GL3.GL_LINE;
import static javax.media.opengl.GL2GL3.GL_POINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_EMISSION;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

public class SimpleAppearanceAdapter<E extends edu.cmu.cs.dennisc.scenegraph.SimpleAppearance> extends AppearanceAdapter<E> {
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
	private boolean m_isEthereal;

	@Override
	public boolean isActuallyShowing() {
		return m_isMaterialActuallyShowing;
	}

	@Override
	public boolean isAlphaBlended() {
		return m_isMaterialAlphaBlended;
	}

	@Override
	public boolean isAllAlphaBlended() {
		return m_isMaterialAlphaBlended;
	}

	@Override
	public boolean isEthereal() {
		return m_isEthereal;
	}

	@Override
	public void setPipelineState( RenderContext rc, int face ) {
		rc.setIsShadingEnabled( m_isShaded );
		rc.gl.glDisable( GL_TEXTURE_2D );
		//todo: investigate if specular, emissive, ambient should use an opacity of 1.0f
		if( m_isShaded ) {
			rc.gl.glMaterialf( face, GL_SHININESS, m_shininess );
			rc.setMaterial( face, GL_SPECULAR, m_specular, m_opacity );
			rc.setMaterial( face, GL_EMISSION, m_emissive, m_opacity );
			if( m_isAmbientLinkedToDiffuse ) {
				rc.gl.glColorMaterial( face, GL_AMBIENT_AND_DIFFUSE );
			} else {
				rc.setMaterial( face, GL_AMBIENT, m_ambient, m_opacity );
				rc.gl.glColorMaterial( face, GL_DIFFUSE );
			}
		}
		rc.setColor( m_diffuse, m_opacity );
		rc.gl.glPolygonMode( face, m_polygonMode );
		//todo
		if( m_isEthereal ) {
			rc.gl.glDepthFunc( GL_LEQUAL );
		} else {
			rc.gl.glDepthFunc( GL_LESS );
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
				m_polygonMode = GL_FILL;
			} else if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME ) ) {
				m_polygonMode = GL_LINE;
			} else if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.POINTS ) ) {
				m_polygonMode = GL_POINT;
			} else {
				throw new RuntimeException();
			}
		} else if( property == m_element.shadingStyle ) {
			edu.cmu.cs.dennisc.scenegraph.ShadingStyle shadingStyle = m_element.shadingStyle.getValue();
			if( ( shadingStyle == null ) || shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE ) ) {
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
		} else if( property == m_element.isEthereal ) {
			m_isEthereal = m_element.isEthereal.getValue();
		} else {
			super.propertyChanged( property );
		}
	}
}
