package edu.cmu.cs.dennisc.nebulous;

import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrVisual;

/**
 * @author Dennis Cosgrove
 */
public class ModelAdapter<T extends Model> extends GenericModelAdapter<T> {
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
	protected void pickGeometry( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, boolean isSubElementRequired ) {
		owner.synchronizedPick();
	}

	@Override
	protected void renderGeometry( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, GlrVisual.RenderType renderType ) {
		//		if( rc.isGLChanged() ) {
		//			m_element.forget();
		//		}
		float globalBrightness = rc.getGlobalBrightness();
		boolean renderAlpha = ( renderType == GlrVisual.RenderType.ALPHA_BLENDED ) || ( renderType == GlrVisual.RenderType.ALL );
		boolean renderOpaque = ( renderType == GlrVisual.RenderType.OPAQUE ) || ( renderType == GlrVisual.RenderType.ALL );
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
			edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrGeometry.getIntersectionInSourceFromPlaneInLocal( rv, ray, m, 0, 0, 0, direction.x, 0, direction.z );
		}
		return rv;
	}
}
