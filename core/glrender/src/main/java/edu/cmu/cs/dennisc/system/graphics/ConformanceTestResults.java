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

package edu.cmu.cs.dennisc.system.graphics;

import static com.jogamp.opengl.GL.GL_CULL_FACE;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_EXTENSIONS;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_RENDERER;
import static com.jogamp.opengl.GL.GL_VENDOR;
import static com.jogamp.opengl.GL.GL_VERSION;
import static com.jogamp.opengl.GL2.GL_QUAD_STRIP;
import static com.jogamp.opengl.GL2.GL_RENDER;
import static com.jogamp.opengl.GL2.GL_SELECT;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import edu.cmu.cs.dennisc.render.gl.imp.GetUtilities;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;

/**
 * @author Dennis Cosgrove
 */
public enum ConformanceTestResults {
	SINGLETON;

	public static class SharedDetails {
		private SharedDetails( com.jogamp.opengl.GL gl ) {
			this.version = gl.glGetString( GL_VERSION );
			this.vendor = gl.glGetString( GL_VENDOR );
			this.renderer = gl.glGetString( GL_RENDERER );

			String extensionsText = gl.glGetString( GL_EXTENSIONS );
			if( extensionsText != null ) {
				this.extensions = extensionsText.split( " " );
			} else {
				this.extensions = new String[] {};
			}
			final boolean IS_COLOR_FORMAT_AND_TYPE_TRACKED = false;
			if( IS_COLOR_FORMAT_AND_TYPE_TRACKED ) {
				int format = GetUtilities.getInteger( gl, com.jogamp.opengl.GL.GL_IMPLEMENTATION_COLOR_READ_FORMAT );
				int type = GetUtilities.getInteger( gl, com.jogamp.opengl.GL.GL_IMPLEMENTATION_COLOR_READ_TYPE );
			}
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

		private final String version;
		private final String vendor;
		private final String renderer;
		private final String[] extensions;
	}

	public static abstract class PickDetails {
		private static final long FISHY_PICK_VALUE = ( PickContext.MAX_UNSIGNED_INTEGER / 2 ) + 1;

		private static long convertZValueToLong( int zValue ) {
			long rv = zValue;
			rv &= PickContext.MAX_UNSIGNED_INTEGER;
			return rv;
		}

		private static float convertZValueToFloat( long zValue ) {
			float zFront = (float)zValue;
			zFront /= (float)PickContext.MAX_UNSIGNED_INTEGER;
			return zFront;
		}

		private final boolean isPickFunctioningCorrectly;

		private PickDetails( com.jogamp.opengl.GL2 gl ) {
			//int n = GetUtilities.getInteger(gl, GL_NUM_EXTENSIONS);

			final int SELECTION_CAPACITY = 256;
			final int SIZEOF_INT = 4;
			java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect( SIZEOF_INT * SELECTION_CAPACITY );
			byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
			java.nio.IntBuffer selectionAsIntBuffer = byteBuffer.asIntBuffer();

			final float XY = 2.0f;
			final float Z = 0.5f;
			final int KEY = 11235;

			gl.glSelectBuffer( SELECTION_CAPACITY, selectionAsIntBuffer );
			gl.glRenderMode( GL_SELECT );

			gl.glClearDepth( 1.0f );
			gl.glDepthFunc( GL_LEQUAL );
			gl.glEnable( GL_DEPTH_TEST );

			gl.glDisable( GL_CULL_FACE );

			gl.glClear( GL_DEPTH_BUFFER_BIT );
			gl.glInitNames();

			gl.glMatrixMode( GL_PROJECTION );
			gl.glLoadIdentity();
			gl.glOrtho( -1, +1, -1, +1, -1, +1 );
			gl.glViewport( 0, 0, 1, 1 );

			gl.glLoadIdentity();
			gl.glMatrixMode( GL_MODELVIEW );
			gl.glLoadIdentity();

			gl.glPushName( KEY );

			gl.glBegin( GL_QUAD_STRIP );
			gl.glVertex3f( -XY, -XY, Z );
			gl.glVertex3f( +XY, -XY, Z );
			gl.glVertex3f( +XY, +XY, Z );
			gl.glVertex3f( -XY, +XY, Z );
			gl.glEnd();

			gl.glFlush();

			gl.glPopName();

			selectionAsIntBuffer.rewind();
			int length = gl.glRenderMode( GL_RENDER );

			boolean isFunctioning = false;
			if( length == 1 ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println("length", length);
				int nameCount = selectionAsIntBuffer.get( 0 );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println("nameCount", nameCount);

				int zFrontAsInt = selectionAsIntBuffer.get( 1 );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println("zFrontAsInt", "0x"+Integer.toHexString(zFrontAsInt));
				long zFrontAsLong = convertZValueToLong( zFrontAsInt );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println("zFrontAsLong", "0x"+Long.toHexString(zFrontAsLong));

				if( ( zFrontAsLong != FISHY_PICK_VALUE ) && ( zFrontAsLong != PickContext.MAX_UNSIGNED_INTEGER ) && ( zFrontAsLong != 0 ) ) {
					//float zFront = convertZValueToFloat( zFrontAsLong );;
					//edu.cmu.cs.dennisc.print.PrintUtilities.println("zFront", zFront);

					boolean IS_BACK_VALUE_OF_CONCERN = false;
					if( IS_BACK_VALUE_OF_CONCERN ) {
						int zBackAsInt = selectionAsIntBuffer.get( 2 );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println("zBackAsInt", "0x"+Integer.toHexString(zBackAsInt));
						long zBackAsLong = convertZValueToLong( zBackAsInt );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println("zBackAsLong", "0x"+Long.toHexString(zBackAsLong));
						float zBack = convertZValueToFloat( zBackAsLong );
						//edu.cmu.cs.dennisc.print.PrintUtilities.println("zBack", zBack);
					}

					if( nameCount == 1 ) {
						int key = selectionAsIntBuffer.get( 3 );
						if( key == KEY ) {
							isFunctioning = true;
							//						edu.cmu.cs.dennisc.print.PrintUtilities.println("todo: remove setting isPickFunctioningCorrectly = false");
							//						this.isPickFunctioningCorrectly = false;
						}
					}
				}
			}
			this.isPickFunctioningCorrectly = isFunctioning;

			//			if( this.isPickFunctioningCorrectly ) {
			//				//pass
			//			} else {
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl isHardwareAccelerated:", conformanceTestResults.isPickFunctioningCorrectly() );
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl isPickFunctioningCorrectly:", conformanceTestResults.isPickFunctioningCorrectly() );
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl version:", conformanceTestResults.getVersion() );
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl vendor:", conformanceTestResults.getVendor() );
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl renderer:", conformanceTestResults.getRenderer() );
			//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl extensions:", conformanceTestResults.getExtensions() );
			//			}
		}

		public boolean isPickFunctioningCorrectly() {
			return this.isPickFunctioningCorrectly;
		}
	}

	public static final class SynchronousPickDetails extends PickDetails {
		private final boolean isReportingPickCanBeHardwareAccelerated;
		private final boolean isPickActuallyHardwareAccelerated;

		private SynchronousPickDetails( com.jogamp.opengl.GL2 gl, boolean isReportingPickCanBeHardwareAccelerated, boolean isPickActuallyHardwareAccelerated ) {
			super( gl );
			this.isReportingPickCanBeHardwareAccelerated = isReportingPickCanBeHardwareAccelerated;
			this.isPickActuallyHardwareAccelerated = isPickActuallyHardwareAccelerated;
		}

		public boolean isReportingPickCanBeHardwareAccelerated() {
			return this.isReportingPickCanBeHardwareAccelerated;
		}

		public boolean isPickActuallyHardwareAccelerated() {
			return this.isPickActuallyHardwareAccelerated;
		}
	}

	public static final class AsynchronousPickDetails extends PickDetails {
		public AsynchronousPickDetails( com.jogamp.opengl.GL2 gl ) {
			super( gl );
		}
	}

	private SharedDetails sharedDetails;
	private SynchronousPickDetails synchronousPickDetails;
	private AsynchronousPickDetails asynchronousPickDetails;

	private void updateSharedDetailsfNecessary( com.jogamp.opengl.GL gl ) {
		if( this.sharedDetails != null ) {
			//pass
		} else {
			this.sharedDetails = new SharedDetails( gl );
		}
	}

	public void updateRenderInformationIfNecessary( com.jogamp.opengl.GL gl ) {
		this.updateSharedDetailsfNecessary( gl );
	}

	public void updateSynchronousPickInformationIfNecessary( com.jogamp.opengl.GL2 gl, boolean isReportingPickCanBeHardwareAccelerated, boolean isPickActuallyHardwareAccelerated ) {
		this.updateSharedDetailsfNecessary( gl );
		if( this.synchronousPickDetails != null ) {
			//pass
		} else {
			this.synchronousPickDetails = new SynchronousPickDetails( gl, isReportingPickCanBeHardwareAccelerated, isPickActuallyHardwareAccelerated );
		}
	}

	public void updateAsynchronousPickInformationIfNecessary( com.jogamp.opengl.GL2 gl ) {
		this.updateSharedDetailsfNecessary( gl );
		if( this.asynchronousPickDetails != null ) {
			//pass
		} else {
			this.asynchronousPickDetails = new AsynchronousPickDetails( gl );
		}
	}

	public SharedDetails getSharedDetails() {
		return this.sharedDetails;
	}

	public SynchronousPickDetails getSynchronousPickDetails() {
		return this.synchronousPickDetails;
	}

	public AsynchronousPickDetails getAsynchronousPickDetails() {
		return this.asynchronousPickDetails;
	}
}
