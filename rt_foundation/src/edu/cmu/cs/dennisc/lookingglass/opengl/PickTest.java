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

package edu.cmu.cs.dennisc.lookingglass.opengl;

import javax.media.opengl.GL;

class PickTest {
	public static boolean isSelectionBufferMalfunctioning() {
		javax.media.opengl.GLDrawableFactory factory = javax.media.opengl.GLDrawableFactory.getFactory();
		if (factory.canCreateGLPbuffer()) {
			javax.media.opengl.GLPbuffer glPbuffer = factory.createGLPbuffer(new javax.media.opengl.GLCapabilities(), new javax.media.opengl.DefaultGLCapabilitiesChooser(), 1, 1, null);
			javax.media.opengl.GLContext glContext = glPbuffer.getContext();
			glContext.makeCurrent();
			GL gl = glPbuffer.getGL();

			final int SELECTION_CAPACITY = 256;
			final int SIZEOF_INT = 4;
			java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect(SIZEOF_INT * SELECTION_CAPACITY);
			byteBuffer.order(java.nio.ByteOrder.nativeOrder());
			java.nio.IntBuffer selectionAsIntBuffer = byteBuffer.asIntBuffer();

			final float XY = 2.0f;
			final float Z = 0.5f;
			final int KEY = 11235;

			gl.glSelectBuffer(SELECTION_CAPACITY, selectionAsIntBuffer);
			gl.glRenderMode(GL.GL_SELECT);

			gl.glClearDepth(1.0f);
			gl.glDepthFunc( GL.GL_LEQUAL );
			gl.glEnable( GL.GL_DEPTH_TEST );

			gl.glDisable(GL.GL_CULL_FACE);
			gl.glViewport(0, 0, 1, 1);

			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
			gl.glInitNames();

			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrtho(-1, +1, -1, +1, -1, +1);

			gl.glLoadIdentity();
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glLoadIdentity();

			gl.glPushName(KEY);

			gl.glBegin(GL.GL_QUAD_STRIP);
			gl.glVertex3f(-XY, -XY, Z);
			gl.glVertex3f(+XY, -XY, Z);
			gl.glVertex3f(+XY, +XY, Z);
			gl.glVertex3f(-XY, +XY, Z);
			gl.glEnd();

			gl.glPopName();

			gl.glFlush();

			selectionAsIntBuffer.rewind();
			int length = gl.glRenderMode(GL.GL_RENDER);

			if (length == 1) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println("length", length);
				int nameCount = selectionAsIntBuffer.get(0);
				edu.cmu.cs.dennisc.print.PrintUtilities.println("nameCount", nameCount);
				int zFrontAsInt = selectionAsIntBuffer.get(1);
				edu.cmu.cs.dennisc.print.PrintUtilities.println("zFrontAsInt", Integer.toHexString(zFrontAsInt));
				int zBackAsInt = selectionAsIntBuffer.get(2);
				edu.cmu.cs.dennisc.print.PrintUtilities.println("zBackAsInt", Integer.toHexString(zBackAsInt));

				long zFrontAsLong = zFrontAsInt;
				zFrontAsLong &= RenderContext.MAX_UNSIGNED_INTEGER;
				edu.cmu.cs.dennisc.print.PrintUtilities.println("zFrontAsLong", Long.toHexString(zFrontAsLong));

				float zFront = (float) zFrontAsLong;
				zFront /= (float) RenderContext.MAX_UNSIGNED_INTEGER;
				edu.cmu.cs.dennisc.print.PrintUtilities.println("zFront", zFront);

				if (nameCount == 1) {
					int key = selectionAsIntBuffer.get(3);
					if (key == KEY) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println("key", key);
					}
				}
			} else {
				throw new RuntimeException("length: " + length);
			}

			String vendor = glPbuffer.getGL().glGetString(javax.media.opengl.GL.GL_VENDOR);
			edu.cmu.cs.dennisc.print.PrintUtilities.println("GL_VENDOR", vendor);
			glPbuffer.destroy();
			glPbuffer = null;
		}
		return false;
	}

	public static void main(String[] args) {
		if (isSelectionBufferMalfunctioning()) {
			throw new RuntimeException();
		}
	}
}
