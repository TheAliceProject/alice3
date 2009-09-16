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

/**
 * @author Dennis Cosgrove
 */
public class ConformanceTestResults {
//	private static ConformanceTestResults singleton;
//	public static ConformanceTestResults getSingleton() {
//		if( singleton != null ) {
//			//pass
//		} else {
//			ConformanceTestResults.singleton = new ConformanceTestResults(); 
//		}
//		return ConformanceTestResults.singleton;
//	}
	
	private static long convertZValueToLong( int zValue ) {
		long rv = zValue;
		rv &= RenderContext.MAX_UNSIGNED_INTEGER;
		return rv;
	}
	private static float convertZValueToFloat( long zValue ) {
		float zFront = (float)zValue;
		zFront /= (float)RenderContext.MAX_UNSIGNED_INTEGER;
		return zFront;
	}

	private String version;
	private String vendor;
	private String renderer;
	private String[] extensions;
	
	private boolean isPickFunctioningCorrectly;
	private boolean isValid;

	private ConformanceTestResults() {
		javax.media.opengl.GLDrawableFactory factory = javax.media.opengl.GLDrawableFactory.getFactory();
		if (factory.canCreateGLPbuffer()) {
			javax.media.opengl.GLCapabilities glDesiredCapabilities = new javax.media.opengl.GLCapabilities();
			javax.media.opengl.GLPbuffer glPbuffer = factory.createGLPbuffer(glDesiredCapabilities, new javax.media.opengl.DefaultGLCapabilitiesChooser(), 1, 1, null);
			javax.media.opengl.GLContext glContext = glPbuffer.getContext();
			glContext.makeCurrent();
			GL gl = glPbuffer.getGL();
			inititialize( gl );
		}
	}
	public ConformanceTestResults( GL gl ) {
		inititialize( gl );
	}

	private void inititialize( GL gl ) {
		edu.cmu.cs.dennisc.timing.Timer timer = new edu.cmu.cs.dennisc.timing.Timer();
		timer.start();
		timer.mark( gl );

		this.version = gl.glGetString(javax.media.opengl.GL.GL_VERSION);
		this.vendor = gl.glGetString(javax.media.opengl.GL.GL_VENDOR);
		this.renderer = gl.glGetString(javax.media.opengl.GL.GL_RENDERER);

		String extensionsText = gl.glGetString(javax.media.opengl.GL.GL_EXTENSIONS);
		this.extensions = extensionsText.split( " " );
		
		//int n = GetUtilities.getInteger(gl, GL.GL_NUM_EXTENSIONS);
		
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

		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glInitNames();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-1, +1, -1, +1, -1, +1);
		gl.glViewport(0, 0, 1, 1);

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

		gl.glFlush();

		gl.glPopName();

		timer.mark( "picked" );

		selectionAsIntBuffer.rewind();
		int length = gl.glRenderMode(GL.GL_RENDER);

		if (length == 1) {
			//edu.cmu.cs.dennisc.print.PrintUtilities.println("length", length);
			int nameCount = selectionAsIntBuffer.get(0);
			//edu.cmu.cs.dennisc.print.PrintUtilities.println("nameCount", nameCount);
			
			int zFrontAsInt = selectionAsIntBuffer.get(1);
			//edu.cmu.cs.dennisc.print.PrintUtilities.println("zFrontAsInt", "0x"+Integer.toHexString(zFrontAsInt));
			long zFrontAsLong = convertZValueToLong( zFrontAsInt );
			//edu.cmu.cs.dennisc.print.PrintUtilities.println("zFrontAsLong", "0x"+Long.toHexString(zFrontAsLong));
			
			if( zFrontAsLong != RenderContext.MAX_UNSIGNED_INTEGER && zFrontAsLong != 0 ) {
				//float zFront = convertZValueToFloat( zFrontAsLong );;
				//edu.cmu.cs.dennisc.print.PrintUtilities.println("zFront", zFront);

				boolean IS_BACK_VALUE_OF_CONCERN = false;
				if( IS_BACK_VALUE_OF_CONCERN ) {
					int zBackAsInt = selectionAsIntBuffer.get(2);
					//edu.cmu.cs.dennisc.print.PrintUtilities.println("zBackAsInt", "0x"+Integer.toHexString(zBackAsInt));
					long zBackAsLong = convertZValueToLong( zBackAsInt );
					//edu.cmu.cs.dennisc.print.PrintUtilities.println("zBackAsLong", "0x"+Long.toHexString(zBackAsLong));
					float zBack = convertZValueToFloat( zBackAsLong );;
					edu.cmu.cs.dennisc.print.PrintUtilities.println("zBack", zBack);
				}
				
				if (nameCount == 1) {
					int key = selectionAsIntBuffer.get(3);
					if (key == KEY) {
						this.isPickFunctioningCorrectly = true;
//						edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: remove setting isPickFunctioningCorrectly = false");
//						this.isPickFunctioningCorrectly = false;
					}
				}
			}
		}
		timer.mark( "processed" );
		timer.stopAndPrintResults();
		this.isValid = true;
	}

	public boolean isValid() {
		return this.isValid;
	}
	public boolean isPickFunctioningCorrectly() {
		return this.isPickFunctioningCorrectly;
	}
	
	
	public String getVersion() {
		return this.version;
	}
	public String getVendor() {
		return this.vendor;
	}
	public String getRenderer() {
		return this.renderer;
	}
	public String[] getExtensions() {
		return this.extensions;
	}
	
	public static void main(String[] args) {
		//ConformanceTestResults conformanceTestResults = ConformanceTestResults.getSingleton();
		ConformanceTestResults conformanceTestResults = new ConformanceTestResults();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "isValid:", conformanceTestResults.isValid() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "isPickFunctioningCorrectly:", conformanceTestResults.isPickFunctioningCorrectly() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "version:", conformanceTestResults.getVersion() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "vendor:", conformanceTestResults.getVendor() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "renderer:", conformanceTestResults.getRenderer() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "extensions:", conformanceTestResults.getExtensions() );
		System.exit(0);
	}
}
