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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.scenegraph.scale.Resizer;

/**
 * @author Dennis Cosgrove
 */
public class BillboardImp extends VisualScaleModelImp {
	private static final float MIN_U = 0.0f;
	private static final float MAX_U = 1.0f;
	private static final float MIN_V = 1.0f;
	private static final float MAX_V = 0.0f;

	private class Face extends edu.cmu.cs.dennisc.scenegraph.TexturedVisual {
		public Face( boolean isFront ) {
			putInstance( this );
			putInstance( this.getAppearance() );
			putInstance( this.sgGeometry );
			this.isFront = isFront;
			float k = this.getK();
			for( edu.cmu.cs.dennisc.scenegraph.Vertex vertex : sgVertices ) {
				vertex.normal.set( 0, 0, k );
			}
			this.updateAspectRatio( 1.0 );
			this.sgGeometry.vertices.setValue( this.sgVertices );
			this.geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgGeometry } );
		}

		public void updateAspectRatio( double widthToHeightAspectRatio ) {
			double x = widthToHeightAspectRatio * 0.5;

			edu.cmu.cs.dennisc.scenegraph.Vertex v0 = sgVertices[ this.getIndex( 0 ) ];
			v0.position.x = -x;
			v0.position.y = 1;
			v0.position.z = 0;

			edu.cmu.cs.dennisc.scenegraph.Vertex v1 = sgVertices[ this.getIndex( 1 ) ];
			v1.position.x = -x;
			v1.position.y = 0;
			v1.position.z = 0;

			edu.cmu.cs.dennisc.scenegraph.Vertex v2 = sgVertices[ this.getIndex( 2 ) ];
			v2.position.x = +x;
			v2.position.y = 0;
			v2.position.z = 0;

			edu.cmu.cs.dennisc.scenegraph.Vertex v3 = sgVertices[ this.getIndex( 3 ) ];
			v3.position.x = +x;
			v3.position.y = 1;
			v3.position.z = 0;

			//Set the vertices property to trigger the property change
			this.sgGeometry.vertices.setValue( this.sgVertices );
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

		private final edu.cmu.cs.dennisc.scenegraph.QuadArray sgGeometry = new edu.cmu.cs.dennisc.scenegraph.QuadArray();
		private final edu.cmu.cs.dennisc.scenegraph.Vertex[] sgVertices = new edu.cmu.cs.dennisc.scenegraph.Vertex[] {
				edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, MIN_U, MAX_V ),
				edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, MIN_U, MIN_V ),
				edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, MAX_U, MIN_V ),
				edu.cmu.cs.dennisc.scenegraph.Vertex.createXYZIJKUV( Double.NaN, Double.NaN, Double.NaN, Float.NaN, Float.NaN, Float.NaN, MAX_U, MAX_V )
		};
		private final boolean isFront;
	}

	public BillboardImp( org.lgna.story.SBillboard abstraction ) {
		this.abstraction = abstraction;
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.sgVisuals ) {
			sgVisual.setParent( this.getSgComposite() );
		}
	}

	@Override
	public org.lgna.story.SBillboard getAbstraction() {
		return this.abstraction;
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		this.setScale( getScaleForSize( size ) );
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] getResizers() {
		return new edu.cmu.cs.dennisc.scenegraph.scale.Resizer[] { edu.cmu.cs.dennisc.scenegraph.scale.Resizer.XY_PLANE, edu.cmu.cs.dennisc.scenegraph.scale.Resizer.X_AXIS, edu.cmu.cs.dennisc.scenegraph.scale.Resizer.Y_AXIS };
	}

	@Override
	public double getValueForResizer( Resizer resizer ) {
		if( resizer == Resizer.XY_PLANE ) {
			return this.getScale().x;
		} else if( resizer == Resizer.X_AXIS ) {
			return this.getScale().x;
		} else if( resizer == Resizer.Y_AXIS ) {
			return this.getScale().y;
		} else {
			assert false : resizer;
			return Double.NaN;
		}
	}

	@Override
	public void setValueForResizer( Resizer resizer, double value ) {
		if( value > 0.0 ) {
			double zScale = this.getScale().z;
			if( resizer == Resizer.XY_PLANE ) {
				double scaleChange = value / this.getScale().x;
				this.setScale( new edu.cmu.cs.dennisc.math.Dimension3( value, this.getScale().y * scaleChange, zScale ) );
			} else if( resizer == Resizer.X_AXIS ) {
				this.setScale( new edu.cmu.cs.dennisc.math.Dimension3( value, this.getScale().y, zScale ) );
			} else if( resizer == Resizer.Y_AXIS ) {
				this.setScale( new edu.cmu.cs.dennisc.math.Dimension3( this.getScale().x, value, zScale ) );
			} else {
				assert false : resizer;
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, value );
		}
	}

	public void updateAspectRatio() {
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
		if( ( width > 0 ) && ( height > 0 ) ) {
			//pass
		} else {
			if( backTexture != null ) {
				width = backTexture.getWidth();
				height = backTexture.getHeight();
			}
		}

		double widthToHeightAspectRatio;
		if( ( width > 0 ) && ( height > 0 ) ) {
			widthToHeightAspectRatio = width / (double)height;
		} else {
			widthToHeightAspectRatio = 1.0;
		}
		this.sgFrontFace.updateAspectRatio( widthToHeightAspectRatio );
		this.sgBackFace.updateAspectRatio( widthToHeightAspectRatio );
	}

	//	public org.lgna.story.Paint getFrontPaint() {
	//		return this.sgFrontFace.getPaint();
	//	}
	//
	public void setFrontPaint( org.lgna.story.Paint frontPaint ) {
		TexturedPaintUtilities.setPaint( this.sgFrontFace, frontPaint );
		this.updateAspectRatio();
	}

	//	public org.lgna.story.Paint getBackPaint() {
	//		return this.sgBackFace.getPaint();
	//	}
	//
	public void setBackPaint( org.lgna.story.Paint backPaint ) {
		TexturedPaintUtilities.setPaint( this.sgBackFace, backPaint );
		this.updateAspectRatio();
	}

	@Override
	public edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
		return this.sgVisuals;
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] getSgPaintAppearances() {
		return this.sgPaintAppearances;
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgOpacityAppearances() {
		return this.sgOpacityAppearances;
	}

	private final org.lgna.story.SBillboard abstraction;

	private final Face sgFrontFace = new Face( true );
	private final Face sgBackFace = new Face( false );
	private final edu.cmu.cs.dennisc.scenegraph.Visual[] sgVisuals = { this.sgFrontFace, this.sgBackFace };
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgPaintAppearances = { this.sgFrontFace.getAppearance() };
	private final edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] sgOpacityAppearances = { this.sgFrontFace.getAppearance(), this.sgBackFace.getAppearance() };

	public final PaintProperty backPaint = new PaintProperty( BillboardImp.this ) {
		@Override
		protected void internalSetValue( org.lgna.story.Paint value ) {
			TexturedPaintUtilities.setPaint( BillboardImp.this.sgBackFace, value );
		}
	};
}
