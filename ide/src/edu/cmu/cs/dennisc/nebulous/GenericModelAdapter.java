/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class GenericModelAdapter< E extends Model > extends edu.cmu.cs.dennisc.lookingglass.opengl.GeometryAdapter< E > {
	@Override
	protected boolean isDisplayListDesired() {
		return false;
	}
	@Override
	public boolean isAlphaBlended() {
		//todo
		return false;
	}
	@Override
	protected void pickGeometry( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, boolean isSubElementRequired ) {
		m_element.pick();
	}
	@Override
	protected void renderGeometry( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		m_element.render(rc.gl, rc.getGlobalBrightness());
	}
	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource(edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement) {
		edu.cmu.cs.dennisc.math.Vector3 direction = edu.cmu.cs.dennisc.math.Vector3.createNegation( m.translation );
		direction.y = 0.0;
		if( direction.calculateMagnitudeSquared() == 0.0 ) {
			rv.setNaN();
		} else {
			direction.normalize();
			edu.cmu.cs.dennisc.lookingglass.opengl.GeometryAdapter.getIntersectionInSourceFromPlaneInLocal(rv, ray, m, 0,0,0, direction.x, 0, direction.z );
		}
		return rv;
	}
}
