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
public class CylinderAdapter extends ShapeAdapter< edu.cmu.cs.dennisc.scenegraph.Cylinder > {
	private double m_length;
	private double m_bottomRadius;
	private double m_topRadius;
	private boolean m_hasBottomCap;
	private boolean m_hasTopCap;
	private edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment m_originAlignment;
	private edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis m_bottomToTopAxis;

	//todo: add scenegraph hint
	private int m_slices = 50;
	private int m_stacks = 1;
	private int m_loops = 1;

	private void glCylinder( Context c ) {
		double topRadius;
		if( Double.isNaN( m_topRadius ) ) {
			topRadius = m_bottomRadius;
		} else {
			topRadius = m_topRadius;
		}
		c.gl.glPushMatrix();
		try {
			if( m_bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_X ) {
				c.gl.glRotated( +90, 0, 1, 0 );
			} else if( m_bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Y ) {
				c.gl.glRotated( -90, 1, 0, 0 );
			} else if( m_bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.POSITIVE_Z ) {
				//pass
			} else if( m_bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_X ) {
				c.gl.glRotated( -90, 0, 1, 0 );
			} else if( m_bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Y ) {
				c.gl.glRotated( +90, 1, 0, 0 );
			} else if( m_bottomToTopAxis == edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis.NEGATIVE_Z ) {
				c.gl.glRotated( 180, 1, 0, 0 );
			} else {
				//todo?
				throw new RuntimeException();
			}
			
			double z;
			if( m_originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.BOTTOM ) {
				z = 0;
			} else if( m_originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.CENTER ) {
				z = -m_length * 0.5;
			} else if( m_originAlignment == edu.cmu.cs.dennisc.scenegraph.Cylinder.OriginAlignment.TOP ) {
				z = -m_length;
			} else {
				//todo?
				throw new RuntimeException();
			}
			c.gl.glTranslated( 0, 0, z );

			c.glu.gluCylinder( c.getQuadric(), m_bottomRadius, topRadius, m_length, m_slices, m_stacks );
			if( m_hasBottomCap && m_bottomRadius > 0 ) {
				c.gl.glRotated( 180, 1, 0, 0 );
				c.glu.gluDisk( c.getQuadric(), 0, m_bottomRadius, m_slices, m_stacks );
				c.gl.glRotated( 180, 1, 0, 0 );
			}
			if( m_hasTopCap && topRadius > 0 ) {
				c.gl.glTranslated( 0, 0, +m_length );
				c.glu.gluDisk( c.getQuadric(), 0, topRadius, m_slices, m_loops );
				c.gl.glTranslated( 0, 0, -m_length );
			}
		} finally {
			c.gl.glPopMatrix();
		}
	}
	@Override
	protected void renderGeometry( RenderContext rc ) {
		glCylinder( rc );
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
		glCylinder( pc );
		pc.gl.glPopName();
	}
	
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.length ) {
			m_length = m_element.length.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.bottomRadius ) {
			m_bottomRadius = m_element.bottomRadius.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.topRadius ) {
			m_topRadius = m_element.topRadius.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.hasBottomCap ) {
			m_hasBottomCap = m_element.hasBottomCap.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.hasTopCap ) {
			m_hasTopCap = m_element.hasTopCap.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.originAlignment ) {
			m_originAlignment = m_element.originAlignment.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.bottomToTopAxis ) {
			m_bottomToTopAxis = m_element.bottomToTopAxis.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
