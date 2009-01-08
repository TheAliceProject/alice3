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
public class DiscAdapter extends ShapeAdapter< edu.cmu.cs.dennisc.scenegraph.Disc > {
	private double m_innerRadius;
	private double m_outerRadius;
	//todo: add scenegraph hint
	private int m_slices = 50;
	private int m_loops = 1;

	private void glDisc( Context c ) {
		c.glu.gluDisk( c.getQuadric(), m_innerRadius, m_outerRadius, m_slices, m_loops );
	}
	@Override
	protected void renderGeometry( RenderContext rc ) {
		glDisc( rc );
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
		glDisc( pc );
		pc.gl.glPopName();
	}
	
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.innerRadius ) {
			m_innerRadius = m_element.innerRadius.getValue();
			setIsGeometryChanged( true );
		} else if( property == m_element.outerRadius ) {
			m_outerRadius = m_element.outerRadius.getValue();
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
}
