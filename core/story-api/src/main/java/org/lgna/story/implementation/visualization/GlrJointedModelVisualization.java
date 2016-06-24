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

package org.lgna.story.implementation.visualization;

import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_LINE_LOOP;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;

/**
 * @author Dennis Cosgrove
 */
public class GlrJointedModelVisualization extends edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrLeaf<JointedModelVisualization> implements edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrRenderContributor {
	private static abstract class GlWalkObserver<C extends edu.cmu.cs.dennisc.render.gl.imp.Context> implements org.lgna.story.implementation.JointedModelImp.TreeWalkObserver {
		private final C context;
		private final org.lgna.story.implementation.ReferenceFrame asSeenBy;
		private final double[] array = new double[ 16 ];
		private final java.nio.DoubleBuffer buffer = java.nio.DoubleBuffer.wrap( array );
		private static final double radius = 0.025;
		private static final int SLICES = 20;
		private static final int STACKS = 20;

		public GlWalkObserver( C context, org.lgna.story.implementation.ReferenceFrame asSeenBy ) {
			this.context = context;
			this.asSeenBy = asSeenBy;
		}

		protected C getContext() {
			return this.context;
		}

		protected abstract void preJoint( org.lgna.story.implementation.JointImp joint );

		protected abstract void preBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child );

		@Override
		public void pushJoint( org.lgna.story.implementation.JointImp joint ) {

			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = joint.getTransformation( this.asSeenBy );
			m.getAsColumnMajorArray16( array );
			this.context.gl.glPushMatrix();
			this.context.gl.glMultMatrixd( buffer );
			this.preJoint( joint );
			this.context.glu.gluSphere( context.getQuadric(), radius, SLICES, STACKS );

			double axisLength = .1;
			context.gl.glDisable( GL_LIGHTING );
			context.gl.glDisable( GL_TEXTURE_2D );
			context.gl.glBegin( GL_LINES );

			context.gl.glColor3f( 1.0f, 0.0f, 0.0f );
			context.gl.glVertex3d( 0, 0, 0 );
			context.gl.glVertex3d( axisLength, 0, 0 );

			context.gl.glColor3f( 0.0f, 1.0f, 0.0f );
			context.gl.glVertex3d( 0, 0, 0 );
			context.gl.glVertex3d( 0, axisLength, 0 );

			context.gl.glColor3f( 0.0f, 0.0f, 1.0f );
			context.gl.glVertex3d( 0, 0, 0 );
			context.gl.glVertex3d( 0, 0, axisLength );
			context.gl.glEnd();

			final boolean IS_BOUNDING_BOX_DESIRED = true;
			if( IS_BOUNDING_BOX_DESIRED ) {
				edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = joint.getAxisAlignedMinimumBoundingBox();
				//              boundingBox = null;
				if( boundingBox != null ) {
					context.gl.glColor3f( 1.0f, 1.0f, 1.0f );
					edu.cmu.cs.dennisc.math.Point3 min = boundingBox.getMinimum();
					edu.cmu.cs.dennisc.math.Point3 max = boundingBox.getMaximum();

					//Bottom
					context.gl.glBegin( GL_LINE_LOOP );
					context.gl.glVertex3d( min.x, min.y, min.z );
					context.gl.glVertex3d( min.x, min.y, max.z );
					context.gl.glVertex3d( max.x, min.y, max.z );
					context.gl.glVertex3d( max.x, min.y, min.z );
					context.gl.glEnd();

					//Top
					context.gl.glBegin( GL_LINE_LOOP );
					context.gl.glVertex3d( min.x, max.y, min.z );
					context.gl.glVertex3d( min.x, max.y, max.z );
					context.gl.glVertex3d( max.x, max.y, max.z );
					context.gl.glVertex3d( max.x, max.y, min.z );
					context.gl.glEnd();

					//Sides
					context.gl.glBegin( GL_LINES );
					context.gl.glVertex3d( min.x, min.y, min.z );
					context.gl.glVertex3d( min.x, max.y, min.z );
					context.gl.glEnd();

					context.gl.glBegin( GL_LINES );
					context.gl.glVertex3d( max.x, min.y, min.z );
					context.gl.glVertex3d( max.x, max.y, min.z );
					context.gl.glEnd();

					context.gl.glBegin( GL_LINES );
					context.gl.glVertex3d( min.x, min.y, max.z );
					context.gl.glVertex3d( min.x, max.y, max.z );
					context.gl.glEnd();

					context.gl.glBegin( GL_LINES );
					context.gl.glVertex3d( max.x, min.y, max.z );
					context.gl.glVertex3d( max.x, max.y, max.z );
					context.gl.glEnd();
				}
			}
		}

		@Override
		public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
			edu.cmu.cs.dennisc.math.Point3 xyz = child.getLocalPosition();
			this.preBone( parent, child );
			context.gl.glBegin( GL_LINES );
			context.gl.glVertex3d( 0.0, 0.0, 0.0 );
			context.gl.glVertex3d( xyz.x, xyz.y, xyz.z );
			context.gl.glEnd();
		}

		@Override
		public void popJoint( org.lgna.story.implementation.JointImp joint ) {
			context.gl.glPopMatrix();
		}
	}

	private static class RenderWalkObserver extends GlWalkObserver<edu.cmu.cs.dennisc.render.gl.imp.RenderContext> {
		public RenderWalkObserver( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, org.lgna.story.implementation.ReferenceFrame asSeenBy ) {
			super( rc, asSeenBy );
		}

		@Override
		protected void preJoint( org.lgna.story.implementation.JointImp joint ) {
			this.getContext().gl.glColor3f( 0.0f, 1.0f, 0.0f );
		}

		@Override
		protected void preBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
			this.getContext().gl.glColor3f( 1.0f, 0.0f, 0.0f );
		}
	}

	private static class PickWalkObserver extends GlWalkObserver<edu.cmu.cs.dennisc.render.gl.imp.PickContext> {
		private final edu.cmu.cs.dennisc.render.gl.imp.PickParameters pickParameters;

		public PickWalkObserver( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, org.lgna.story.implementation.ReferenceFrame asSeenBy, edu.cmu.cs.dennisc.render.gl.imp.PickParameters pickParameters ) {
			super( pc, asSeenBy );
			this.pickParameters = pickParameters;
		}

		@Override
		protected void preJoint( org.lgna.story.implementation.JointImp joint ) {
			if( this.pickParameters.isSubElementRequired() ) {
				org.lgna.story.resources.JointId jointId = joint.getJointId();
				int name;
				if( jointId != null ) {
					name = jointId.hashCode();
				} else {
					name = -1;
				}
				this.getContext().gl.glLoadName( name );
			}
		}

		@Override
		protected void preBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
		}
	}

	private void pushOffset( javax.media.opengl.GL2 gl ) {
		gl.glPushMatrix();
		//		gl.glTranslated( 1,0,0 );		
	}

	private void popOffset( javax.media.opengl.GL2 gl ) {
		gl.glPopMatrix();
	}

	@Override
	public void pick( edu.cmu.cs.dennisc.render.gl.imp.PickContext pc, edu.cmu.cs.dennisc.render.gl.imp.PickParameters pickParameters ) {
		this.pushOffset( pc.gl );
		org.lgna.story.implementation.JointedModelImp implementation = this.owner.getImplementation();
		pc.gl.glPushName( -1 ); // visual
		try {
			pc.gl.glPushName( 1 ); // isFrontFacing
			try {
				pc.gl.glPushName( -1 ); // geometry
				try {
					pc.gl.glPushName( -1 ); // subElement
					try {
						implementation.treeWalk( new PickWalkObserver( pc, implementation, pickParameters ) );
					} finally {
						pc.gl.glPopName();
					}
				} finally {
					pc.gl.glPopName();
				}
			} finally {
				pc.gl.glPopName();
			}
		} finally {
			pc.gl.glPopName();
		}
		this.popOffset( pc.gl );
	}

	@Override
	public void renderGhost( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrGhost root ) {
	}

	@Override
	public void renderOpaque( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc ) {
		rc.gl.glEnable( GL_LIGHTING );
		this.pushOffset( rc.gl );
		org.lgna.story.implementation.JointedModelImp implementation = this.owner.getImplementation();

		edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox = implementation.getAxisAlignedMinimumBoundingBox();
		//              boundingBox = null;
		if( boundingBox != null ) {
			rc.gl.glDisable( GL_LIGHTING );
			rc.gl.glDisable( GL_TEXTURE_2D );
			rc.gl.glColor3f( 1.0f, 1.0f, 1.0f );
			edu.cmu.cs.dennisc.math.Point3 min = boundingBox.getMinimum();
			edu.cmu.cs.dennisc.math.Point3 max = boundingBox.getMaximum();

			//Bottom
			rc.gl.glBegin( GL_LINE_LOOP );
			rc.gl.glVertex3d( min.x, min.y, min.z );
			rc.gl.glVertex3d( min.x, min.y, max.z );
			rc.gl.glVertex3d( max.x, min.y, max.z );
			rc.gl.glVertex3d( max.x, min.y, min.z );
			rc.gl.glEnd();

			//Top
			rc.gl.glBegin( GL_LINE_LOOP );
			rc.gl.glVertex3d( min.x, max.y, min.z );
			rc.gl.glVertex3d( min.x, max.y, max.z );
			rc.gl.glVertex3d( max.x, max.y, max.z );
			rc.gl.glVertex3d( max.x, max.y, min.z );
			rc.gl.glEnd();

			//Sides
			rc.gl.glBegin( GL_LINES );
			rc.gl.glVertex3d( min.x, min.y, min.z );
			rc.gl.glVertex3d( min.x, max.y, min.z );
			rc.gl.glEnd();

			rc.gl.glBegin( GL_LINES );
			rc.gl.glVertex3d( max.x, min.y, min.z );
			rc.gl.glVertex3d( max.x, max.y, min.z );
			rc.gl.glEnd();

			rc.gl.glBegin( GL_LINES );
			rc.gl.glVertex3d( min.x, min.y, max.z );
			rc.gl.glVertex3d( min.x, max.y, max.z );
			rc.gl.glEnd();

			rc.gl.glBegin( GL_LINES );
			rc.gl.glVertex3d( max.x, min.y, max.z );
			rc.gl.glVertex3d( max.x, max.y, max.z );
			rc.gl.glEnd();
		}

		implementation.treeWalk( new RenderWalkObserver( rc, implementation ) );
		this.popOffset( rc.gl );
	}
}
