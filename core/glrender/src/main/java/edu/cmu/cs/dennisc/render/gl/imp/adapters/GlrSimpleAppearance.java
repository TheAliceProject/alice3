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

public class GlrSimpleAppearance<T extends edu.cmu.cs.dennisc.scenegraph.SimpleAppearance> extends GlrAppearance<T> {
	@Override
	public boolean isActuallyShowing() {
		return this.isMaterialActuallyShowing;
	}

	@Override
	public boolean isAlphaBlended() {
		return this.isMaterialAlphaBlended;
	}

	@Override
	public boolean isAllAlphaBlended() {
		return this.isMaterialAlphaBlended;
	}

	@Override
	public boolean isEthereal() {
		return this.isEthereal;
	}

	@Override
	public void setPipelineState( RenderContext rc, int face ) {
		rc.setIsShadingEnabled( this.isShaded );
		rc.gl.glDisable( GL_TEXTURE_2D );
		//todo: investigate if specular, emissive, ambient should use an opacity of 1.0f
		if( this.isShaded ) {
			rc.gl.glMaterialf( face, GL_SHININESS, this.shininess );
			rc.setMaterial( face, GL_SPECULAR, this.specular, this.opacity );
			rc.setMaterial( face, GL_EMISSION, this.emissive, this.opacity );
			if( this.isAmbientLinkedToDiffuse ) {
				rc.gl.glColorMaterial( face, GL_AMBIENT_AND_DIFFUSE );
			} else {
				rc.setMaterial( face, GL_AMBIENT, this.ambient, this.opacity );
				rc.gl.glColorMaterial( face, GL_DIFFUSE );
			}
		}
		rc.setColor( this.diffuse, this.opacity );
		rc.gl.glPolygonMode( face, this.polygonMode );
		//todo
		if( this.isEthereal ) {
			rc.gl.glDepthFunc( GL_LEQUAL );
		} else {
			rc.gl.glDepthFunc( GL_LESS );
		}
	}

	private void updateOpacityRelatedBooleans() {
		float opacity = this.opacity * this.diffuse[ 3 ];
		this.isMaterialActuallyShowing = opacity > 0.0f;
		this.isMaterialAlphaBlended = opacity < 1.0f;
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.ambientColor ) {
			owner.ambientColor.getValue().getAsArray( this.ambient );
			this.isAmbientLinkedToDiffuse = Float.isNaN( this.ambient[ 0 ] );
		} else if( property == owner.diffuseColor ) {
			owner.diffuseColor.getValue().getAsArray( this.diffuse );
			this.updateOpacityRelatedBooleans();
		} else if( property == owner.opacity ) {
			this.opacity = owner.opacity.getValue();
			this.updateOpacityRelatedBooleans();
		} else if( property == owner.specularHighlightColor ) {
			owner.specularHighlightColor.getValue().getAsArray( this.specular );
		} else if( property == owner.specularHighlightExponent ) {
			this.shininess = owner.specularHighlightExponent.getValue();
		} else if( property == owner.emissiveColor ) {
			owner.emissiveColor.getValue().getAsArray( this.emissive );
		} else if( property == owner.fillingStyle ) {
			edu.cmu.cs.dennisc.scenegraph.FillingStyle fillingStyle = owner.fillingStyle.getValue();
			if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.SOLID ) ) {
				this.polygonMode = GL_FILL;
			} else if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.WIREFRAME ) ) {
				this.polygonMode = GL_LINE;
			} else if( fillingStyle.equals( edu.cmu.cs.dennisc.scenegraph.FillingStyle.POINTS ) ) {
				this.polygonMode = GL_POINT;
			} else {
				throw new RuntimeException();
			}
		} else if( property == owner.shadingStyle ) {
			edu.cmu.cs.dennisc.scenegraph.ShadingStyle shadingStyle = owner.shadingStyle.getValue();
			if( ( shadingStyle == null ) || shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.NONE ) ) {
				this.isShaded = false;
			} else if( shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.FLAT ) ) {
				this.isShaded = true;
				//todo
			} else if( shadingStyle.equals( edu.cmu.cs.dennisc.scenegraph.ShadingStyle.SMOOTH ) ) {
				this.isShaded = true;
				//todo
			} else {
				throw new RuntimeException();
			}
		} else if( property == owner.isEthereal ) {
			this.isEthereal = owner.isEthereal.getValue();
		} else {
			super.propertyChanged( property );
		}
	}

	private boolean isShaded;
	private boolean isAmbientLinkedToDiffuse;
	private boolean isMaterialActuallyShowing;
	private boolean isMaterialAlphaBlended;
	private final float[] ambient = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private final float[] diffuse = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private final float[] specular = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private final float[] emissive = { Float.NaN, Float.NaN, Float.NaN, Float.NaN };
	private float opacity = Float.NaN;
	private float shininess = Float.NaN;
	private int polygonMode;
	private boolean isEthereal;
}
