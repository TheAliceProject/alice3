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

/**
 * @author Dennis Cosgrove
 */
public class BoxAdapter extends ShapeAdapter< edu.cmu.cs.dennisc.scenegraph.Box > {
	private double m_xMin;
	private double m_xMax;
	private double m_yMin;
	private double m_yMax;
	private double m_zMin;
	private double m_zMax;

	private void glBox( Context c, boolean isLightingEnabled, boolean isSubElementRequired ) {
		//todo: revist vertex ordering
		//xMin face
		//c.gl.glColor3d( 1,1,1 );
		int id = 0;

		c.gl.glBegin( javax.media.opengl.GL.GL_QUADS );
		if( isSubElementRequired ) {
			c.gl.glLoadName( id++ );
		}
		if( isLightingEnabled ) {
			c.gl.glNormal3d( -1, 0, 0 );
		}
		c.gl.glVertex3d( m_xMin, m_yMin, m_zMax );
		c.gl.glVertex3d( m_xMin, m_yMax, m_zMax );
		c.gl.glVertex3d( m_xMin, m_yMax, m_zMin );
		c.gl.glVertex3d( m_xMin, m_yMin, m_zMin );

		//xMax face
		//c.gl.glColor3d( 1,0,0 );
		if( isSubElementRequired ) {
			c.gl.glLoadName( id++ );
		}
		if( isLightingEnabled ) {
			c.gl.glNormal3d( 1, 0, 0 );
		}
		c.gl.glVertex3d( m_xMax, m_yMin, m_zMin );
		c.gl.glVertex3d( m_xMax, m_yMax, m_zMin );
		c.gl.glVertex3d( m_xMax, m_yMax, m_zMax );
		c.gl.glVertex3d( m_xMax, m_yMin, m_zMax );

		//yMin face
		//c.gl.glColor3d( 1,1,1 );
		if( isSubElementRequired ) {
			c.gl.glLoadName( id++ );
		}
		if( isLightingEnabled ) {
			c.gl.glNormal3d( 0, -1, 0 );
		}
		c.gl.glVertex3d( m_xMin, m_yMin, m_zMin );
		c.gl.glVertex3d( m_xMax, m_yMin, m_zMin );
		c.gl.glVertex3d( m_xMax, m_yMin, m_zMax );
		c.gl.glVertex3d( m_xMin, m_yMin, m_zMax );

		//yMax face
		//c.gl.glColor3d( 0,1,0 );
		if( isSubElementRequired ) {
			c.gl.glLoadName( id++ );
		}
		if( isLightingEnabled ) {
			c.gl.glNormal3d( 0, 1, 0 );
		}
		c.gl.glVertex3d( m_xMin, m_yMax, m_zMax );
		c.gl.glVertex3d( m_xMax, m_yMax, m_zMax );
		c.gl.glVertex3d( m_xMax, m_yMax, m_zMin );
		c.gl.glVertex3d( m_xMin, m_yMax, m_zMin );

		//zMin face
		//c.gl.glColor3d( 1,1,1 );
		if( isSubElementRequired ) {
			c.gl.glLoadName( id++ );
		}
		if( isLightingEnabled ) {
			c.gl.glNormal3d( 0, 0, -1 );
		}
		c.gl.glVertex3d( m_xMin, m_yMax, m_zMin );
		c.gl.glVertex3d( m_xMax, m_yMax, m_zMin );
		c.gl.glVertex3d( m_xMax, m_yMin, m_zMin );
		c.gl.glVertex3d( m_xMin, m_yMin, m_zMin );

		//zMax face
		//c.gl.glColor3d( 0,0,1 );
		if( isSubElementRequired ) {
			c.gl.glLoadName( id++ );
		}
		if( isLightingEnabled ) {
			c.gl.glNormal3d( 0, 0, 1 );
		}
		c.gl.glVertex3d( m_xMin, m_yMin, m_zMax );
		c.gl.glVertex3d( m_xMax, m_yMin, m_zMax );
		c.gl.glVertex3d( m_xMax, m_yMax, m_zMax );
		c.gl.glVertex3d( m_xMin, m_yMax, m_zMax );

		c.gl.glEnd();
	}

	@Override
	protected void renderGeometry( RenderContext rc ) {
		glBox( rc, true, false );
	}
	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		pc.gl.glPushName( -1 );
		glBox( pc, false, isSubElementRequired );
		pc.gl.glPopName();
	}
	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource(edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement) {
		edu.cmu.cs.dennisc.math.Point3 origin = new edu.cmu.cs.dennisc.math.Point3( 0, 0, 0 );
		edu.cmu.cs.dennisc.math.Vector3 direction = new edu.cmu.cs.dennisc.math.Vector3( 0, 0, 0 );
		switch( subElement ) {
		case 0:
			origin.x = m_xMin;
			direction.x = -1;
			break;
		case 1:
			origin.x = m_xMax;
			direction.x = 1;
			break;
		case 2:
			origin.y = m_yMin;
			direction.y = -1;
			break;
		case 3:
			origin.y = m_yMax;
			direction.y = 1;
			break;
		case 4:
			origin.z = m_zMin;
			direction.z = -1;
			break;
		case 5:
			origin.z = m_zMax;
			direction.z = 1;
			break;
		default:
			rv.setNaN();
			return rv;
		}
		GeometryAdapter.getIntersectionInSourceFromPlaneInLocal(rv, ray, m, origin.x, origin.y, origin.z, direction.x, direction.y, direction.z);
		return rv;
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.xMinimum ) {
			m_xMin = m_element.xMinimum.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.xMaximum ) {
			m_xMax = m_element.xMaximum.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.yMinimum ) {
			m_yMin = m_element.yMinimum.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.yMaximum ) {
			m_yMax = m_element.yMaximum.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.zMinimum ) {
			m_zMin = m_element.zMinimum.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.zMaximum ) {
			m_zMax = m_element.zMaximum.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
