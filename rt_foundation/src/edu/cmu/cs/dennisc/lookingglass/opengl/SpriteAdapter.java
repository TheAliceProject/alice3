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
public class SpriteAdapter extends GeometryAdapter< edu.cmu.cs.dennisc.scenegraph.Sprite > {
	@Override
    public boolean isAlphaBlended() {
    	return false;
    }
	@Override
	protected void renderGeometry( RenderContext rc ) {
		throw new RuntimeException( "todo" );
    }
	@Override
	protected void pickGeometry( PickContext pc, boolean isSubElementRequired ) {
		throw new RuntimeException( "todo" );
	}    
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == m_element.radius ) {
            //todo
			setIsGeometryChanged( true );
		} else {
			super.propertyChanged( property );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource(edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement) {
		rv.setNaN();
		return rv;
	}
}
