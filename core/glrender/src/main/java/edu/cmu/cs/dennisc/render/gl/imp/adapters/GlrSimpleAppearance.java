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

import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_LESS;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL2GL3.GL_FILL;
import static com.jogamp.opengl.GL2GL3.GL_LINE;
import static com.jogamp.opengl.GL2GL3.GL_POINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_EMISSION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;
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
