package edu.cmu.cs.dennisc.lookingglass.opengl;

public abstract class AbstractTransformableAdapter< E extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable > extends CompositeAdapter< E > {

	public abstract double[] accessLocalTransformation();
	public abstract java.nio.DoubleBuffer accessLocalTransformationAsBuffer();
		
	@Override
	public void renderOpaque( RenderContext rc ) {
		rc.gl.glPushMatrix();
		try {
			rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			super.renderOpaque( rc );
		} finally {
			rc.gl.glPopMatrix();
		}
	}
	
	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		rc.gl.glPushMatrix();
		try {
			if( this == root ) {
				rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			} else {
				rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			}
			super.renderGhost( rc, root );
		} finally {
			rc.gl.glPopMatrix();
		}
	}
	
	
	@Override
	public void pick( PickContext pc, PickParameters pickParameters, ConformanceTestResults conformanceTestResults ) {
		pc.gl.glPushMatrix();
		try {
			pc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			super.pick( pc, pickParameters, conformanceTestResults );
		} finally {
			pc.gl.glPopMatrix();
		}
	}
}