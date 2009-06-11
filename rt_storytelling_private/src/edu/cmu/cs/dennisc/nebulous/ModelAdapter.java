/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class ModelAdapter< E extends Model > extends edu.cmu.cs.dennisc.lookingglass.opengl.GeometryAdapter< E > {
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
//		if( rc.isGLChanged() ) {
//			m_element.forget();
//		}
		m_element.render();
	}
}
