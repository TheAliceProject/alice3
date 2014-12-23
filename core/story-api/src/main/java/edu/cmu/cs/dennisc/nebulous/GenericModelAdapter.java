/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

import edu.cmu.cs.dennisc.render.gl.imp.adapters.VisualAdapter;

/**
 * @author Dennis Cosgrove
 */
public class GenericModelAdapter<E extends Model> extends edu.cmu.cs.dennisc.render.gl.imp.adapters.GeometryAdapter<E> {
	@Override
	protected boolean isDisplayListDesired() {
		return false;
	}

	@Override
	public boolean hasOpaque() {
		return owner.synchronizedHasOpaque();
	}

	@Override
	public boolean isAlphaBlended() {
		return owner.synchronizedIsAlphaBlended();
	}

	@Override
	protected void pickGeometry( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, boolean isSubElementRequired ) {
		owner.synchronizedPick();
	}

	@Override
	protected void renderGeometry( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, VisualAdapter.RenderType renderType ) {
		float globalBrightness = rc.getGlobalBrightness();
		boolean renderAlpha = ( renderType == VisualAdapter.RenderType.ALPHA_BLENDED ) || ( renderType == VisualAdapter.RenderType.ALL );
		boolean renderOpaque = ( renderType == VisualAdapter.RenderType.OPAQUE ) || ( renderType == VisualAdapter.RenderType.ALL );
		rc.clearDiffuseColorTextureAdapter();
		owner.synchronizedRender( rc.gl, globalBrightness, renderAlpha, renderOpaque );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Point3 getIntersectionInSource( edu.cmu.cs.dennisc.math.Point3 rv, edu.cmu.cs.dennisc.math.Ray ray, edu.cmu.cs.dennisc.math.AffineMatrix4x4 m, int subElement ) {
		edu.cmu.cs.dennisc.math.Vector3 direction = edu.cmu.cs.dennisc.math.Vector3.createNegation( m.translation );
		direction.y = 0.0;
		if( direction.calculateMagnitudeSquared() == 0.0 ) {
			rv.setNaN();
		} else {
			direction.normalize();
			edu.cmu.cs.dennisc.render.gl.imp.adapters.GeometryAdapter.getIntersectionInSourceFromPlaneInLocal( rv, ray, m, 0, 0, 0, direction.x, 0, direction.z );
		}
		return rv;
	}
}
