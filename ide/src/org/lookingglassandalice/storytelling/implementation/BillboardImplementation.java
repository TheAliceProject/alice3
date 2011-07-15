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

package org.lookingglassandalice.storytelling.implementation;

/**
 * @author Dennis Cosgrove
 */
public class BillboardImplementation extends ModelImplementation {
	private class Face extends edu.cmu.cs.dennisc.scenegraph.Visual {
		private edu.cmu.cs.dennisc.scenegraph.SingleAppearance sgAppearance = new edu.cmu.cs.dennisc.scenegraph.SingleAppearance();
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
		public edu.cmu.cs.dennisc.texture.Texture getTexture() {
			return this.sgAppearance.diffuseColorTexture.getValue();
		}
		public void setTexture( edu.cmu.cs.dennisc.texture.Texture texture ) {
			this.sgAppearance.diffuseColorTexture.setValue( texture );
			boolean isDiffuseColorTextureAlphaBlended;
			if( texture != null ) {
				isDiffuseColorTextureAlphaBlended = texture.isPotentiallyAlphaBlended();
			} else {
				isDiffuseColorTextureAlphaBlended = false;
			}
			this.sgAppearance.isDiffuseColorTextureAlphaBlended.setValue( isDiffuseColorTextureAlphaBlended );
		}
	}

	private final Face sgFrontFace = new Face( true );
	private final Face sgBackFace = new Face( false );
	private final edu.cmu.cs.dennisc.scenegraph.Visual[] sgVisuals = { this.sgFrontFace, this.sgBackFace };
	private final edu.cmu.cs.dennisc.scenegraph.SingleAppearance[] sgAppearances = { this.sgFrontFace.sgAppearance, this.sgBackFace.sgAppearance };
	private org.lookingglassandalice.storytelling.ImageSource frontImageSource;
	private org.lookingglassandalice.storytelling.ImageSource backImageSource;

	private final org.lookingglassandalice.storytelling.Billboard abstraction;
	public BillboardImplementation( org.lookingglassandalice.storytelling.Billboard abstraction ) {
		this.abstraction = abstraction;
	}
	@Override
	public org.lookingglassandalice.storytelling.Billboard getAbstraction() {
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
	public org.lookingglassandalice.storytelling.ImageSource getFrontImageSource() {
		return this.frontImageSource;
	}
	public void setFrontImageSource( org.lookingglassandalice.storytelling.ImageSource frontImageSource ) {
		this.frontImageSource = frontImageSource;
		edu.cmu.cs.dennisc.texture.Texture texture;
		if( this.frontImageSource != null ) {
			texture = edu.cmu.cs.dennisc.texture.TextureFactory.getTexture( frontImageSource.getImageResource(), true );
		} else {
			texture = null;
		}
		this.updateAspectRatio();
		this.sgFrontFace.setTexture( texture );
	}
	public org.lookingglassandalice.storytelling.ImageSource getBackImageSource() {
		return this.backImageSource;
	}
	public void setBackImageSource( org.lookingglassandalice.storytelling.ImageSource backImageSource ) {
		this.backImageSource = backImageSource;
		edu.cmu.cs.dennisc.texture.Texture texture;
		if( this.backImageSource != null ) {
			texture = edu.cmu.cs.dennisc.texture.TextureFactory.getTexture( backImageSource.getImageResource(), true );
		} else {
			texture = null;
		}
		this.updateAspectRatio();
		this.sgBackFace.setTexture( texture );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
		return this.sgVisuals;
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.SingleAppearance[] getSgAppearances() {
		return this.sgAppearances;
	}
}
