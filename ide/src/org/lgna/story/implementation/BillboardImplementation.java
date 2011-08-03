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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public class BillboardImplementation extends ModelImplementation {
	private class Face extends edu.cmu.cs.dennisc.scenegraph.Visual {
		private org.lgna.story.Paint paint;
		private edu.cmu.cs.dennisc.scenegraph.TexturedAppearance sgAppearance = new edu.cmu.cs.dennisc.scenegraph.TexturedAppearance();
		private edu.cmu.cs.dennisc.scenegraph.QuadArray sgGeometry = new edu.cmu.cs.dennisc.scenegraph.QuadArray();
		private edu.cmu.cs.dennisc.scenegraph.Vertex[] sgVertices = new edu.cmu.cs.dennisc.scenegraph.Vertex[] {
				new edu.cmu.cs.dennisc.scenegraph.Vertex(),
				new edu.cmu.cs.dennisc.scenegraph.Vertex(),
				new edu.cmu.cs.dennisc.scenegraph.Vertex(),
				new edu.cmu.cs.dennisc.scenegraph.Vertex(),
		};
		private boolean isFront;
		
		public Face( boolean isFront ) {
			putInstance( this );
			putInstance( this.sgAppearance );
			putInstance( this.sgGeometry );
			this.isFront = isFront;
			float k = this.getK();
			for( edu.cmu.cs.dennisc.scenegraph.Vertex vertex : sgVertices ) {
				vertex.normal.set( 0, 0, k );
			}
			this.updateAspectRatio( 1.0 );
			this.sgAppearance.setDiffuseColorTextureClamped( true );
			this.sgGeometry.vertices.setValue( this.sgVertices );
			this.frontFacingAppearance.setValue( this.sgAppearance );
			this.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgGeometry } );
		}
		
		public void updateAspectRatio( double widthToHeightAspectRatio ) { 
			double x = widthToHeightAspectRatio * 0.5;
			
			final float MIN_U = 0.0f;
			final float MAX_U = 1.0f;
			final float MIN_V = 1.0f;
			final float MAX_V = 0.0f;
			
			edu.cmu.cs.dennisc.scenegraph.Vertex v0 = sgVertices[ this.getIndex( 0 ) ];
			v0.position.x = -x;
			v0.position.y = 1;
			v0.position.z = 0;
			v0.textureCoordinate0.u = MAX_U;
			v0.textureCoordinate0.v = MAX_V;
			
			edu.cmu.cs.dennisc.scenegraph.Vertex v1 = sgVertices[ this.getIndex( 1 ) ];
			v1.position.x = -x;
			v1.position.y = 0;
			v1.position.z = 0;
			v1.textureCoordinate0.u = MAX_U;
			v1.textureCoordinate0.v = MIN_V;

			edu.cmu.cs.dennisc.scenegraph.Vertex v2 = sgVertices[ this.getIndex( 2 ) ];
			v2.position.x = +x;
			v2.position.y = 0;
			v2.position.z = 0;
			v2.textureCoordinate0.u = MIN_U;
			v2.textureCoordinate0.v = MIN_V;

			edu.cmu.cs.dennisc.scenegraph.Vertex v3 = sgVertices[ this.getIndex( 3 ) ];
			v3.position.x = +x;
			v3.position.y = 1;
			v3.position.z = 0;
			v3.textureCoordinate0.u = MIN_U;
			v3.textureCoordinate0.v = MAX_V;
		}
		private int getIndex( int i ) {
			if( this.isFront ) {
				return sgVertices.length - 1 - i;
			} else {
				return i;
			}
		}
		private float getK() {
			if( this.isFront ) {
				return -1.0f;
			} else {
				return +1.0f;
			}
		}
		private edu.cmu.cs.dennisc.texture.Texture getTexture() {
			return this.sgAppearance.diffuseColorTexture.getValue();
		}
		private void setTexture( edu.cmu.cs.dennisc.texture.Texture texture ) {
			this.sgAppearance.diffuseColorTexture.setValue( texture );
			boolean isDiffuseColorTextureAlphaBlended;
			if( texture != null ) {
				isDiffuseColorTextureAlphaBlended = texture.isPotentiallyAlphaBlended();
			} else {
				isDiffuseColorTextureAlphaBlended = false;
			}
			this.sgAppearance.isDiffuseColorTextureAlphaBlended.setValue( isDiffuseColorTextureAlphaBlended );
		}
		public org.lgna.story.Paint getPaint() {
			return this.paint;
		}
		public void setPaint( org.lgna.story.Paint paint ) {
			this.sgAppearance.setDiffuseColor( org.lgna.story.ImplementationAccessor.getColor4f( paint, edu.cmu.cs.dennisc.color.Color4f.WHITE ) );
			this.setTexture( org.lgna.story.ImplementationAccessor.getTexture( paint, null ) );
		}
	}

	private final Face sgFrontFace = new Face( true );
	private final Face sgBackFace = new Face( false );
	private final edu.cmu.cs.dennisc.scenegraph.Visual[] sgVisuals = { this.sgFrontFace, this.sgBackFace };
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgAppearances = { this.sgFrontFace.sgAppearance, this.sgBackFace.sgAppearance };
	private org.lgna.story.Paint backPaint;

	private final org.lgna.story.Billboard abstraction;
	public BillboardImplementation( org.lgna.story.Billboard abstraction ) {
		this.abstraction = abstraction;
	}
	@Override
	public org.lgna.story.Billboard getAbstraction() {
		return this.abstraction;
	}

	private void updateAspectRatio() {
		edu.cmu.cs.dennisc.texture.Texture frontTexture = this.sgFrontFace.getTexture();
		edu.cmu.cs.dennisc.texture.Texture backTexture = this.sgBackFace.getTexture();

		int width;
		int height;
		if( frontTexture != null ) {
			width = frontTexture.getWidth();
			height = frontTexture.getHeight();
		} else {
			width = -1;
			height = -1;
		}
		if( width > 0 && height > 0 ) {
			//pass
		} else {
			if( backTexture != null ) {
				width = backTexture.getWidth();
				height = backTexture.getHeight();
			}
		}
		
		double widthToHeightAspectRatio;
		if( width > 0 && height > 0 ) {
			widthToHeightAspectRatio = width / (double)height;
		} else {
			widthToHeightAspectRatio = 1.0;
		}
		this.sgFrontFace.updateAspectRatio( widthToHeightAspectRatio );
		this.sgBackFace.updateAspectRatio( widthToHeightAspectRatio );
	}
	public org.lgna.story.Paint getFrontPaint() {
		return this.sgFrontFace.getPaint();
	}
	public void setFrontPaint( org.lgna.story.Paint frontPaint ) {
		this.sgFrontFace.setPaint( frontPaint );
		this.updateAspectRatio();
	}
	public org.lgna.story.Paint getBackPaint() {
		return this.sgBackFace.getPaint();
	}
	public void setBackPaint( org.lgna.story.Paint backPaint ) {
		this.sgBackFace.setPaint( backPaint );
		this.updateAspectRatio();
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
		return this.sgVisuals;
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] getSgAppearances() {
		return this.sgAppearances;
	}
	@Override
	protected double getBoundingSphereRadius() {
		return 0;
	}
}
