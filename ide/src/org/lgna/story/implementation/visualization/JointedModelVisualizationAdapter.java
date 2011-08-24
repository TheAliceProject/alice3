/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.lgna.story.implementation.visualization;

/**
 * @author Dennis Cosgrove
 */
public class JointedModelVisualizationAdapter extends edu.cmu.cs.dennisc.lookingglass.opengl.ComponentAdapter< JointedModelVisualization > {
	private void gl( final edu.cmu.cs.dennisc.lookingglass.opengl.Context context ) {
		final org.lgna.story.implementation.JointedModelImplementation implementation = this.m_element.getImplementation();
		final double[] array = new double[ 16 ];
		final java.nio.DoubleBuffer buffer = java.nio.DoubleBuffer.wrap( array );
		final double radius = 0.05;
		final int SLICES = 20;
		final int STACKS = 20;
		implementation.walk( new org.lgna.story.implementation.JointedModelImplementation.WalkObserver() {
			public void pushJoint( org.lgna.story.implementation.JointImplementation joint ) {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = joint.getTransformation( implementation );
				m.getAsColumnMajorArray16( array );
				context.gl.glPushMatrix();
				context.gl.glMultMatrixd( buffer );
				context.gl.glColor3f( 0.0f, 1.0f, 0.0f );
				context.glu.gluSphere( context.getQuadric(), radius, SLICES, STACKS );
			}
			public void handleBone( org.lgna.story.implementation.JointImplementation parent, org.lgna.story.implementation.JointImplementation child ) {
				edu.cmu.cs.dennisc.math.Point3 xyz = child.getLocalPosition();
				context.gl.glColor3f( 1.0f, 0.0f, 0.0f );
				context.gl.glBegin( javax.media.opengl.GL2.GL_LINES );
				context.gl.glVertex3d( 0.0, 0.0, 0.0 );
				context.gl.glVertex3d( xyz.x, xyz.y, xyz.z );
				context.gl.glEnd();
			}
			public void popJoint(org.lgna.story.implementation.JointImplementation joint) {
				context.gl.glPopMatrix();
			}
		} );
	}
	@Override
	public void pick( edu.cmu.cs.dennisc.lookingglass.opengl.PickContext pc, edu.cmu.cs.dennisc.lookingglass.opengl.PickParameters pickParameters, edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults conformanceTestResults ) {
	}
	@Override
	public void renderGhost( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, edu.cmu.cs.dennisc.lookingglass.opengl.GhostAdapter root ) {
	}
	@Override
	public void renderOpaque( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
		this.gl( rc );
	}
	@Override
	public void setup( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc ) {
	}
}
