package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

public abstract class GlrAbstractTransformable<T extends edu.cmu.cs.dennisc.scenegraph.AbstractTransformable> extends GlrComposite<T> {
	private static final int X_X = 0;
	private static final int X_Y = 1;
	private static final int X_Z = 2;

	private static final int Y_X = 4;
	private static final int Y_Y = 5;
	private static final int Y_Z = 6;

	private static final int Z_X = 8;
	private static final int Z_Y = 9;
	private static final int Z_Z = 10;

	private boolean isUnscalingDesired( Context c ) {
		//todo
		return false;
		//		edu.cmu.cs.dennisc.scenegraph.Composite parent = this.m_element.getParent();
		//		return 
		//				c.isScaled() 
		//					&&
		//				( this.m_element instanceof edu.cmu.cs.dennisc.scenegraph.Joint == false )
		//					&&
		//				( parent instanceof edu.cmu.cs.dennisc.scenegraph.Scalable || parent instanceof edu.cmu.cs.dennisc.scenegraph.Joint );
	}

	private boolean unscaleIfDesired( Context c ) {
		boolean isUnscaling = this.isUnscalingDesired( c );
		if( isUnscaling ) {
			synchronized( unscalingBuffer ) {
				c.gl.glGetDoublev( javax.media.opengl.GL2.GL_MODELVIEW_MATRIX, unscalingBuffer );
				double xScale = edu.cmu.cs.dennisc.math.Tuple3.calculateMagnitude( unscaling[ X_X ], unscaling[ X_Y ], unscaling[ X_Z ] );
				double yScale = edu.cmu.cs.dennisc.math.Tuple3.calculateMagnitude( unscaling[ Y_X ], unscaling[ Y_Y ], unscaling[ Y_Z ] );
				double zScale = edu.cmu.cs.dennisc.math.Tuple3.calculateMagnitude( unscaling[ Z_X ], unscaling[ Z_Y ], unscaling[ Z_Z ] );

				if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( xScale, 1.0 ) && edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( yScale, 1.0 ) && edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( zScale, 1.0 ) ) {
					//pass
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
					isUnscaling = false;
				} else {
					xScale = 1 / xScale;
					yScale = 1 / yScale;
					zScale = 1 / zScale;

					unscaling[ X_X ] *= xScale;
					unscaling[ X_Y ] *= xScale;
					unscaling[ X_Z ] *= xScale;

					unscaling[ Y_X ] *= yScale;
					unscaling[ Y_Y ] *= yScale;
					unscaling[ Y_Z ] *= yScale;

					unscaling[ Z_X ] *= zScale;
					unscaling[ Z_Y ] *= zScale;
					unscaling[ Z_Z ] *= zScale;

					c.gl.glLoadMatrixd( unscalingBuffer );
				}
			}
		}
		return isUnscaling;
	}

	public void EPIC_HACK_FOR_ICON_CAPTURE_renderOpaque( RenderContext rc ) {
		rc.gl.glPushMatrix();
		try {
			rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			super.renderOpaque( rc );
		} finally {
			rc.gl.glPopMatrix();
		}
	}

	@Override
	public void renderOpaque( RenderContext rc ) {
		rc.gl.glPushMatrix();
		try {
			rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			boolean isUnscaling = this.unscaleIfDesired( rc );
			if( isUnscaling ) {
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "unscaling" + this.m_element );
				rc.pushScaledCountAndSetToZero();
			}
			super.renderOpaque( rc );
			if( isUnscaling ) {
				rc.popAndRestoreScaledCount();
			}
		} finally {
			rc.gl.glPopMatrix();
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GlrGhost root ) {
		rc.gl.glPushMatrix();
		try {
			if( this == root ) {
				rc.gl.glMultMatrixd( accessAbsoluteTransformationAsBuffer() );
			} else {
				rc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			}
			boolean isUnscaling = this.unscaleIfDesired( rc );
			if( isUnscaling ) {
				rc.pushScaledCountAndSetToZero();
			}
			super.renderGhost( rc, root );
			if( isUnscaling ) {
				rc.popAndRestoreScaledCount();
			}
		} finally {
			rc.gl.glPopMatrix();
		}
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		pc.gl.glPushMatrix();
		try {
			pc.gl.glMultMatrixd( accessLocalTransformationAsBuffer() );
			boolean isUnscaling = this.unscaleIfDesired( pc );
			if( isUnscaling ) {
				pc.pushScaledCountAndSetToZero();
			}
			super.pick( pc, pickParameters );
			if( isUnscaling ) {
				pc.popAndRestoreScaledCount();
			}
		} finally {
			pc.gl.glPopMatrix();
		}
	}

	private static final double[] unscaling = new double[ 16 ];
	private static final java.nio.DoubleBuffer unscalingBuffer = java.nio.DoubleBuffer.wrap( unscaling );

	//public abstract double[] accessLocalTransformation();

	public abstract java.nio.DoubleBuffer accessLocalTransformationAsBuffer();
}
