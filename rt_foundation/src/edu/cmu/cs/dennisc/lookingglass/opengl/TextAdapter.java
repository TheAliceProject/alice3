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
import javax.media.opengl.glu.GLU;

/**
 * @author Dennis Cosgrove
 */
class MyTessAdapter extends javax.media.opengl.glu.GLUtessellatorCallbackAdapter {
	private GL m_gl;
	private double m_xOffset;
	private double m_yOffset;
	private double m_z;
	private boolean m_isFront;

	public void set( GL gl, double xOffset, double yOffset, double z, boolean isFront ) {
		m_gl = gl;
		m_xOffset = xOffset;
		m_yOffset = yOffset;
		m_z = z;
		m_isFront = isFront;
	}

	@Override
	public void begin( int primitiveType ) {
		m_gl.glBegin( primitiveType );
		if( m_isFront ) {
			m_gl.glNormal3d( 0.0, 0.0, -1.0 );
		} else {
			m_gl.glNormal3d( 0.0, 0.0, 1.0 );
		}
	}
	@Override
	public void vertex( Object data ) {
		double[] vertex = (double[])data;
		m_gl.glVertex3d( vertex[ 0 ] + m_xOffset, vertex[ 1 ] + m_yOffset, m_z );
	}
	@Override
	public void end() {
		m_gl.glEnd();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class TextAdapter extends GeometryAdapter< edu.cmu.cs.dennisc.scenegraph.Text > {
	private static MyTessAdapter s_tessAdapter = new MyTessAdapter();

	@Override
	public boolean isAlphaBlended() {
		return false;
	}

	private void renderFaceContours( Context context, double xOffset, double yOffset, double z, boolean isFront ) {
		synchronized( s_tessAdapter ) {

			java.util.Vector< java.util.Vector< edu.cmu.cs.dennisc.math.Point2f >> faceContours = m_element.getGlyphVector().acquireFaceContours();

			s_tessAdapter.set( context.gl, xOffset, yOffset, z, isFront );

			javax.media.opengl.glu.GLUtessellator tesselator = context.glu.gluNewTess();
			context.glu.gluTessCallback( tesselator, GLU.GLU_TESS_BEGIN, s_tessAdapter );
			context.glu.gluTessCallback( tesselator, GLU.GLU_TESS_VERTEX, s_tessAdapter );
			context.glu.gluTessCallback( tesselator, GLU.GLU_TESS_END, s_tessAdapter );
			try {
				context.glu.gluBeginPolygon( tesselator );
				for( java.util.Vector< edu.cmu.cs.dennisc.math.Point2f > faceContour : faceContours ) {
					context.glu.gluTessBeginContour( tesselator );
					int n = faceContour.size();
					for( int i = 0; i < n; i++ ) {
						edu.cmu.cs.dennisc.math.Point2f p;
						if( isFront ) {
							p = faceContour.elementAt( n - i - 1 );
						} else {
							p = faceContour.elementAt( i );
						}
						double[] xyz = { p.x, p.y, 0 };
						context.glu.gluTessVertex( tesselator, xyz, 0, xyz );
					}
					context.glu.gluTessEndContour( tesselator );
				}
				context.glu.gluTessEndPolygon( tesselator );
			} finally {
				m_element.getGlyphVector().releaseFaceContours();
			}

		}
	}

	private void glText( Context context, boolean isLightingEnabled ) {
		edu.cmu.cs.dennisc.math.Vector3 alignmentOffset = m_element.getAlignmentOffset();
		double zFront = alignmentOffset.z;
		double zBack = zFront + m_element.depth.getValue();

		renderFaceContours( context, alignmentOffset.x, alignmentOffset.y, zFront, true );
		renderFaceContours( context, alignmentOffset.x, alignmentOffset.y, zBack, false );

		if( zFront != zBack ) {
			java.util.Vector< java.util.Vector< edu.cmu.cs.dennisc.math.Point2f >> outlineLines = m_element.getGlyphVector().acquireOutlineLines();
			try {
				for( java.util.Vector< edu.cmu.cs.dennisc.math.Point2f > outlineLine : outlineLines ) {
					context.gl.glBegin( GL.GL_QUAD_STRIP );
					edu.cmu.cs.dennisc.math.Point2f prev = null;
					for( edu.cmu.cs.dennisc.math.Point2f curr : outlineLine ) {
						if( prev == null ) {
							//pass
						} else {
							if( curr.x == prev.x && curr.y == prev.y ) {
								//pass
								//System.err.println( "pass outline" );
							} else {
								if( isLightingEnabled ) {
									double xDelta = curr.x - prev.x;
									double yDelta = curr.y - prev.y;
									double lengthSquared = xDelta * xDelta + yDelta * yDelta;
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
				m_element.getGlyphVector().releaseOutlineLines();
			}
		}
	}

	@Override
	protected void renderGeometry( RenderContext rc ) {
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
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.text ) {
			setIsGeometryChanged( true );
		} else if( property == m_element.font ) {
			setIsGeometryChanged( true );
		} else if( property == m_element.depth ) {
			setIsGeometryChanged( true );
		} else if( property == m_element.leftToRightAlignment ) {
			setIsGeometryChanged( true );
		} else if( property == m_element.topToBottomAlignment ) {
			setIsGeometryChanged( true );
		} else if( property == m_element.frontToBackAlignment ) {
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
