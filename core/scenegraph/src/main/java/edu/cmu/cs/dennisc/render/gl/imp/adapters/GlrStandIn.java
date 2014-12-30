package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

public class GlrStandIn<E extends edu.cmu.cs.dennisc.scenegraph.Transformable> extends GlrComposite<E> {
	@Override
	public void renderOpaque( RenderContext rc ) {
		rc.gl.glPushMatrix();
		try {
			rc.gl.glMultMatrixd( this.localTransformationBuffer );
			super.renderOpaque( rc );
		} finally {
			rc.gl.glPopMatrix();
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GlrGhost root ) {
		//		rc.gl.glPushMatrix();
		//		try {
		//			if( this == root ) {
		//				rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
		//			} else {
		//				rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
		//			}
		//			super.renderGhost( rc, root );
		//		} finally {
		//			rc.gl.glPopMatrix();
		//		}
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		pc.gl.glPushMatrix();
		try {
			pc.gl.glMultMatrixd( this.localTransformationBuffer );
			super.pick( pc, pickParameters );
		} finally {
			pc.gl.glPopMatrix();
		}
	}

	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty<?> property ) {
		if( property == owner.localTransformation ) {
			owner.localTransformation.getValue().getAsColumnMajorArray16( this.localTransformation );
		} else {
			super.propertyChanged( property );
		}
	}

	private final double[] localTransformation = new double[ 16 ];
	private final java.nio.DoubleBuffer localTransformationBuffer = java.nio.DoubleBuffer.wrap( this.localTransformation );
}
