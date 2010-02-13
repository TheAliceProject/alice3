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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class Billboard extends AbstractModel {
	class Face extends edu.cmu.cs.dennisc.scenegraph.Visual {
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
			putElement( this );
			putElement( this.sgAppearance );
			putElement( this.sgGeometry );
			this.isFront = isFront;
			float k = this.getK();
			for( edu.cmu.cs.dennisc.scenegraph.Vertex vertex : sgVertices ) {
				vertex.normal.set( 0, 0, k );
			}
			this.update( 1.0 );
			this.sgAppearance.setDiffuseColorTextureClamped( true );
			this.sgGeometry.vertices.setValue( this.sgVertices );
			this.frontFacingAppearance.setValue( this.sgAppearance );
			this.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgGeometry } );
		}
		
		private void update( double widthToHeightAspectRatio ) { 
			double x = widthToHeightAspectRatio * 0.5;
			
			final float MIN_U = 0.0f;
			final float MAX_U = 1.0f;
			final float MIN_V = 1.0f;
			final float MAX_V = 0.0f;
			
			edu.cmu.cs.dennisc.scenegraph.Vertex v0 = sgVertices[ this.getIndex( 0 ) ];
			v0.position.x = -x;
			v0.position.y = 1;
			v0.position.z = 0;
			v0.textureCoordinate0.u = MIN_U;
			v0.textureCoordinate0.v = MAX_V;
			
			edu.cmu.cs.dennisc.scenegraph.Vertex v1 = sgVertices[ this.getIndex( 1 ) ];
			v1.position.x = -x;
			v1.position.y = 0;
			v1.position.z = 0;
			v1.textureCoordinate0.u = MIN_U;
			v1.textureCoordinate0.v = MIN_V;

			edu.cmu.cs.dennisc.scenegraph.Vertex v2 = sgVertices[ this.getIndex( 2 ) ];
			v2.position.x = +x;
			v2.position.y = 0;
			v2.position.z = 0;
			v2.textureCoordinate0.u = MAX_U;
			v2.textureCoordinate0.v = MIN_V;

			edu.cmu.cs.dennisc.scenegraph.Vertex v3 = sgVertices[ this.getIndex( 3 ) ];
			v3.position.x = +x;
			v3.position.y = 1;
			v3.position.z = 0;
			v3.textureCoordinate0.u = MAX_U;
			v3.textureCoordinate0.v = MAX_V;
		}
		private int getIndex( int i ) {
			if( this.isFront ) {
				return i;
			} else {
				return sgVertices.length - 1 - i;
			}
		}
		private float getK() {
			if( this.isFront ) {
				return +1.0f;
			} else {
				return -1.0f;
			}
		}
		public void setTexture( edu.cmu.cs.dennisc.texture.Texture texture ) {
			this.frontFacingAppearance.getValue().setDiffuseColorTexture( texture );
			boolean isDiffuseColorTextureAlphaBlended;
			if( texture != null ) {
				isDiffuseColorTextureAlphaBlended = texture.isPotentiallyAlphaBlended();
			} else {
				isDiffuseColorTextureAlphaBlended = false;
			}
			this.sgAppearance.isDiffuseColorTextureAlphaBlended.setValue( isDiffuseColorTextureAlphaBlended );
		}
	}

	public Billboard() {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = this.getSGTransformable();
		this.sgFrontFace.setParent( sgTransformable );
		this.sgBackFace.setParent( sgTransformable );
	}
	private Face sgFrontFace = new Face( true );
	private Face sgBackFace = new Face( false );
	private ImageSource frontImageSource;
	private ImageSource backImageSource;

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Visual getSGVisual() {
		return this.sgFrontFace;
	}
	
	public ImageSource getFrontImageSource() {
		return this.frontImageSource;
	}
	public void setFrontImageSource( ImageSource frontImageSource ) {
		this.frontImageSource = frontImageSource;
		edu.cmu.cs.dennisc.texture.Texture texture;
		if( this.frontImageSource != null ) {
			texture = edu.cmu.cs.dennisc.texture.TextureFactory.getTexture( frontImageSource.getImageResource(), true );
		} else {
			texture = null;
		}
		this.sgFrontFace.setTexture( texture );
	}
	public ImageSource getBackImageSource() {
		return this.backImageSource;
	}
	public void setBackImageSource( ImageSource backImageSource ) {
		this.backImageSource = backImageSource;
		edu.cmu.cs.dennisc.texture.Texture texture;
		if( this.backImageSource != null ) {
			texture = edu.cmu.cs.dennisc.texture.TextureFactory.getTexture( backImageSource.getImageResource(), true );
		} else {
			texture = null;
		}
		this.sgBackFace.setTexture( texture );
	}
	
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean isOriginIncluded ) {
		super.updateCumulativeBound( rv, trans, isOriginIncluded );
		//todo
		return rv;
	}
}
