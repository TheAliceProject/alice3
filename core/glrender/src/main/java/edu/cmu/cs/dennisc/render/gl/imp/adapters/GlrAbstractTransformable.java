/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import com.jogamp.opengl.GL2;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

import java.nio.DoubleBuffer;

public abstract class GlrAbstractTransformable<T extends AbstractTransformable> extends GlrComposite<T> {
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
				c.gl.glGetDoublev( GL2.GL_MODELVIEW_MATRIX, unscalingBuffer );
				double xScale = Tuple3.calculateMagnitude( unscaling[ X_X ], unscaling[ X_Y ], unscaling[ X_Z ] );
				double yScale = Tuple3.calculateMagnitude( unscaling[ Y_X ], unscaling[ Y_Y ], unscaling[ Y_Z ] );
				double zScale = Tuple3.calculateMagnitude( unscaling[ Z_X ], unscaling[ Z_Y ], unscaling[ Z_Z ] );

				if( EpsilonUtilities.isWithinReasonableEpsilon( xScale, 1.0 ) && EpsilonUtilities.isWithinReasonableEpsilon( yScale, 1.0 ) && EpsilonUtilities.isWithinReasonableEpsilon( zScale, 1.0 ) ) {
					//pass
					Logger.severe( this );
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
	private static final DoubleBuffer unscalingBuffer = DoubleBuffer.wrap( unscaling );

	//public abstract double[] accessLocalTransformation();

	public abstract DoubleBuffer accessLocalTransformationAsBuffer();
}
