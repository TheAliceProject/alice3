/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.scenegraph.adorn.opengl;

/**
 * @author Dennis Cosgrove
 */
public class StickFigureAdapter extends AdornmentAdapter {
	private static final int TRANSLATION_X_INDEX = 12;
	private static final int TRANSLATION_Y_INDEX = 13;
	private static final int TRANSLATION_Z_INDEX = 14;
	private static final float[] COLOR = { 1.0f, 1.0f, 0.0f, 1.0f }; 
	private static void glStickFigure( javax.media.opengl.GL gl, java.nio.DoubleBuffer ltParent, edu.cmu.cs.dennisc.lookingglass.opengl.CompositeAdapter parent ) {
		gl.glPushMatrix();
		try {
			gl.glMultMatrixd( ltParent );
			Iterable< edu.cmu.cs.dennisc.lookingglass.opengl.ComponentAdapter > componentAdapters = parent.accessComponentAdapters();
			synchronized( componentAdapters ) {
				for( edu.cmu.cs.dennisc.lookingglass.opengl.ComponentAdapter componentAdapter : componentAdapters ) {
					if( componentAdapter instanceof edu.cmu.cs.dennisc.lookingglass.opengl.TransformableAdapter ) {
						edu.cmu.cs.dennisc.lookingglass.opengl.TransformableAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Transformable> child = (edu.cmu.cs.dennisc.lookingglass.opengl.TransformableAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Transformable>)componentAdapter;
						java.nio.DoubleBuffer ltChild = child.accessLocalTransformationAsBuffer();
						gl.glBegin( javax.media.opengl.GL.GL_LINES );
						try {
							gl.glVertex3d( 0, 0, 0 );
							gl.glVertex3d( ltChild.get( TRANSLATION_X_INDEX ), ltChild.get( TRANSLATION_Y_INDEX ), ltChild.get( TRANSLATION_Z_INDEX ) );
						} finally {
							gl.glEnd();
						}
						glStickFigure( gl, ltChild, child );
					}
				}
			}
		} finally {
			gl.glPopMatrix();
		}
	}
	@Override
	protected void actuallyRender( edu.cmu.cs.dennisc.lookingglass.opengl.RenderContext rc, edu.cmu.cs.dennisc.lookingglass.opengl.CompositeAdapter adornmentRootAdapter ) {
		rc.gl.glDisable( javax.media.opengl.GL.GL_LIGHTING );
		rc.setColor( COLOR, 1.0f );
		glStickFigure( rc.gl, accessAbsoluteTransformationAsBuffer(), adornmentRootAdapter );
	}
}
