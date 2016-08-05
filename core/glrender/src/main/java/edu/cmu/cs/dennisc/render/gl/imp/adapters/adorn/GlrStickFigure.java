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
package edu.cmu.cs.dennisc.render.gl.imp.adapters.adorn;

import static com.jogamp.opengl.GL.GL_LINES;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;

/**
 * @author Dennis Cosgrove
 */
public class GlrStickFigure extends GlrAdornment<edu.cmu.cs.dennisc.scenegraph.adorn.StickFigure> {
	private static final int TRANSLATION_X_INDEX = 12;
	private static final int TRANSLATION_Y_INDEX = 13;
	private static final int TRANSLATION_Z_INDEX = 14;
	private static final float[] COLOR = { 1.0f, 1.0f, 0.0f, 1.0f };

	private static void glStickFigure( com.jogamp.opengl.GL2 gl, java.nio.DoubleBuffer ltParent, edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComposite<?> glrParent ) {
		gl.glPushMatrix();
		try {
			gl.glMultMatrixd( ltParent );
			Iterable<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?>> glrChildren = glrParent.accessChildren();
			synchronized( glrChildren ) {
				for( edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?> glrChild : glrChildren ) {
					if( glrChild instanceof edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrTransformable<?> ) {
						edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrTransformable<?> glrTransformable = (edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrTransformable<?>)glrChild;
						java.nio.DoubleBuffer ltChild = glrTransformable.accessLocalTransformationAsBuffer();
						gl.glBegin( GL_LINES );
						try {
							gl.glVertex3d( 0, 0, 0 );
							gl.glVertex3d( ltChild.get( TRANSLATION_X_INDEX ), ltChild.get( TRANSLATION_Y_INDEX ), ltChild.get( TRANSLATION_Z_INDEX ) );
						} finally {
							gl.glEnd();
						}
						glStickFigure( gl, ltChild, glrTransformable );
					}
				}
			}
		} finally {
			gl.glPopMatrix();
		}
	}

	@Override
	protected void actuallyRender( edu.cmu.cs.dennisc.render.gl.imp.RenderContext rc, edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComposite<?> glrAdornmentRoot ) {
		rc.gl.glDisable( GL_LIGHTING );
		rc.setColor( COLOR, 1.0f );
		glStickFigure( rc.gl, accessAbsoluteTransformationAsBuffer(), glrAdornmentRoot );
	}
}
