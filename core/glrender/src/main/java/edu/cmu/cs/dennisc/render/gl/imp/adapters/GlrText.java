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

import static com.jogamp.opengl.GL2.GL_QUAD_STRIP;
import static com.jogamp.opengl.glu.GLU.GLU_OUT_OF_MEMORY;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_BEGIN;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_COMBINE;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_COORD_TOO_LARGE;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_END;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_ERROR;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_MISSING_BEGIN_CONTOUR;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_MISSING_BEGIN_POLYGON;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_MISSING_END_CONTOUR;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_MISSING_END_POLYGON;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_NEED_COMBINE_CALLBACK;
import static com.jogamp.opengl.glu.GLU.GLU_TESS_VERTEX;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUtessellator;
import com.jogamp.opengl.glu.GLUtessellatorCallbackAdapter;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point2f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.Text;

import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class GlrText extends GlrGeometry<Text> {
	private static class MyTessAdapter extends GLUtessellatorCallbackAdapter {
		public void set( GL2 gl, double xOffset, double yOffset, double z, boolean isFront ) {
			this.gl = gl;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.z = z;
			this.isFront = isFront;
		}

		@Override
		public void begin( int primitiveType ) {
			this.gl.glBegin( primitiveType );
			if( this.isFront ) {
				this.gl.glNormal3d( 0.0, 0.0, -1.0 );
			} else {
				this.gl.glNormal3d( 0.0, 0.0, 1.0 );
			}
		}

		@Override
		public void vertex( Object data ) {
			double[] vertex = (double[])data;
			this.gl.glVertex3d( vertex[ 0 ] + this.xOffset, vertex[ 1 ] + this.yOffset, this.z );
		}

		@Override
		public void end() {
			this.gl.glEnd();
		}

		@Override
		public void combine( double[] coords, Object[] data, float[] weight, Object[] outData ) {
			outData[ 0 ] = new double[] { coords[ 0 ], coords[ 1 ], coords[ 2 ] };
		}

		private String getErrorString( int error ) {
			switch( error ) {
			case GLU_TESS_MISSING_BEGIN_POLYGON:
				return "GLU_TESS_MISSING_BEGIN_POLYGON";
			case GLU_TESS_MISSING_END_POLYGON:
				return "GLU_TESS_MISSING_END_POLYGON";
			case GLU_TESS_MISSING_BEGIN_CONTOUR:
				return "GLU_TESS_MISSING_BEGIN_CONTOUR";
			case GLU_TESS_MISSING_END_CONTOUR:
				return "GLU_TESS_MISSING_END_CONTOUR";
			case GLU_TESS_COORD_TOO_LARGE:
				return "GLU_TESS_COORD_TOO_LARGE";
			case GLU_TESS_NEED_COMBINE_CALLBACK:
				return "GLU_TESS_NEED_COMBINE_CALLBACK";
			case GLU_OUT_OF_MEMORY:
				return "GLU_OUT_OF_MEMORY";
			default:
				return "UNKNOWN";
			}
		}

		@Override
		public void error( int arg0 ) {
			super.error( arg0 );
			Logger.errln( getErrorString( arg0 ), arg0 );
		}

		private GL2 gl;
		private double xOffset;
		private double yOffset;
		private double z;
		private boolean isFront;
	}

	private static final MyTessAdapter s_tessAdapter = new MyTessAdapter();

	@Override
	public boolean isAlphaBlended() {
		return false;
	}

	private void renderFaceContours( Context context, double xOffset, double yOffset, double z, boolean isFront ) {
		synchronized( s_tessAdapter ) {

			List<List<Point2f>> faceContours = owner.getGlyphVector().acquireFaceContours();

			s_tessAdapter.set( context.gl, xOffset, yOffset, z, isFront );

			GLUtessellator tesselator = GLU.gluNewTess();
			GLU.gluTessCallback( tesselator, GLU_TESS_BEGIN, s_tessAdapter );
			GLU.gluTessCallback( tesselator, GLU_TESS_VERTEX, s_tessAdapter );
			GLU.gluTessCallback( tesselator, GLU_TESS_END, s_tessAdapter );
			GLU.gluTessCallback( tesselator, GLU_TESS_COMBINE, s_tessAdapter );
			//			com.jogamp.opengl.glu.GLU.gluTessCallback( tesselator, GLU_TESS_COMBINE_DATA, s_tessAdapter );
			final boolean IS_ERROR_OUTPUT_DESIRED = false;
			if( IS_ERROR_OUTPUT_DESIRED ) {
				GLU.gluTessCallback( tesselator, GLU_TESS_ERROR, s_tessAdapter );
			}
			//			context.glu.gluTessCallback( tesselator, GLU_TESS_ERROR_DATA, s_tessAdapter );
			try {
				GLU.gluBeginPolygon( tesselator );
				try {
					for( List<Point2f> faceContour : faceContours ) {
						GLU.gluTessBeginContour( tesselator );
						try {
							int n = faceContour.size();
							for( int i = 0; i < n; i++ ) {
								Point2f p;
								if( isFront ) {
									p = faceContour.get( n - i - 1 );
								} else {
									p = faceContour.get( i );
								}
								double[] xyz = { p.x, p.y, 0 };
								GLU.gluTessVertex( tesselator, xyz, 0, xyz );
							}
						} finally {
							GLU.gluTessEndContour( tesselator );
						}
					}
				} finally {
					GLU.gluTessEndPolygon( tesselator );
				}
			} finally {
				owner.getGlyphVector().releaseFaceContours();
			}
		}
	}

	private void glText( Context context, boolean isLightingEnabled ) {
		Vector3 alignmentOffset = owner.getAlignmentOffset();
		double zFront = alignmentOffset.z;
		double zBack = zFront + owner.depth.getValue();

		renderFaceContours( context, alignmentOffset.x, alignmentOffset.y, zFront, true );
		renderFaceContours( context, alignmentOffset.x, alignmentOffset.y, zBack, false );

		if( zFront != zBack ) {
			List<List<Point2f>> outlineLines = owner.getGlyphVector().acquireOutlineLines();
			try {
				for( List<Point2f> outlineLine : outlineLines ) {
					context.gl.glBegin( GL_QUAD_STRIP );
					Point2f prev = null;
					for( Point2f curr : outlineLine ) {
						if( prev == null ) {
							//pass
						} else {
							if( ( curr.x == prev.x ) && ( curr.y == prev.y ) ) {
								//pass
								//System.err.println( "pass outline" );
							} else {
								if( isLightingEnabled ) {
									double xDelta = curr.x - prev.x;
									double yDelta = curr.y - prev.y;
									double lengthSquared = ( xDelta * xDelta ) + ( yDelta * yDelta );
									double length = Math.sqrt( lengthSquared );
									context.gl.glNormal3d( yDelta / length, xDelta / length, 0 );
								}
								context.gl.glVertex3d( prev.x + alignmentOffset.x, prev.y + alignmentOffset.y, zFront );
								context.gl.glVertex3d( prev.x + alignmentOffset.x, prev.y + alignmentOffset.y, zBack );
							}
						}
						prev = curr;
					}
					context.gl.glVertex3d( prev.x + alignmentOffset.x, prev.y + alignmentOffset.y, zFront );
					context.gl.glVertex3d( prev.x + alignmentOffset.x, prev.y + alignmentOffset.y, zBack );
					context.gl.glEnd();
				}
			} finally {
				owner.getGlyphVector().releaseOutlineLines();
			}
		}
	}

	@Override
	protected void renderGeometry( RenderContext rc, GlrVisual.RenderType renderType ) {
		glText( rc, true );
	}

	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		int name;
		if( isSubElementRequired ) {
			name = 0;
		} else {
			name = -1;
		}
		pc.gl.glPushName( name );
		glText( pc, false );
		pc.gl.glPopName();
	}

	@Override
	protected void propertyChanged( InstanceProperty<?> property ) {
		if( property == owner.text ) {
			setIsGeometryChanged( true );
		} else if( property == owner.font ) {
			setIsGeometryChanged( true );
		} else if( property == owner.depth ) {
			setIsGeometryChanged( true );
		} else if( property == owner.leftToRightAlignment ) {
			setIsGeometryChanged( true );
		} else if( property == owner.topToBottomAlignment ) {
			setIsGeometryChanged( true );
		} else if( property == owner.frontToBackAlignment ) {
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}

	@Override
	public Point3 getIntersectionInSource( Point3 rv, Ray ray, AffineMatrix4x4 m, int subElement ) {
		Vector3 alignmentOffset = owner.getAlignmentOffset();
		double zFront = alignmentOffset.z;
		//todo: no reason to believe it hit the front 
		return GlrGeometry.getIntersectionInSourceFromPlaneInLocal( rv, ray, m, 0, 0, zFront, 0, 0, -1 );
	}
}
