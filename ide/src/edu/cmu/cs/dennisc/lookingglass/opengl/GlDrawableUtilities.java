/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public class GlDrawableUtilities {
	private static final java.util.Map<javax.media.opengl.GLPbuffer, java.awt.Dimension> mapPixelBufferToDimension;
	static {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			mapPixelBufferToDimension = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		} else {
			mapPixelBufferToDimension = null;
		}
	}

	private GlDrawableUtilities() {
		throw new AssertionError();
	}

	private static javax.media.opengl.GLCapabilities createGLCapabilities( int desiredSampleCount ) {
		javax.media.opengl.GLCapabilities rv = new javax.media.opengl.GLCapabilities();
		boolean isMultisamplingDesired = desiredSampleCount >= 2;
		rv.setSampleBuffers( isMultisamplingDesired );
		if( isMultisamplingDesired ) {
			rv.setNumSamples( desiredSampleCount );
		}
		return rv;
	}

	private static int getDesiredOnscreenSampleCount() {
		return 1;
	}

	/* package-private */static javax.media.opengl.GLCapabilities createPerhapsMultisampledGlCapabilities() {
		return createGLCapabilities( getDesiredOnscreenSampleCount() );
	}

	/* package-private */static javax.media.opengl.GLCapabilities createDisabledMultisamplingGlCapabilities() {
		return createGLCapabilities( 1 );
	}

	private static javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser;

	/* package-private */static javax.media.opengl.GLCapabilitiesChooser getGLCapabilitiesChooser() {
		if( glCapabilitiesChooser != null ) {
			//pass
		} else {
			//todo?
			glCapabilitiesChooser = new javax.media.opengl.DefaultGLCapabilitiesChooser();
		}
		return glCapabilitiesChooser;
	}

	/* package-private */static javax.media.opengl.GLCanvas createGLCanvas() {
		return new javax.media.opengl.GLCanvas( createPerhapsMultisampledGlCapabilities(), getGLCapabilitiesChooser(), null, null );
	}

	/* package-private */static javax.media.opengl.GLJPanel createGLJPanel() {
		return new javax.media.opengl.GLJPanel( createPerhapsMultisampledGlCapabilities(), getGLCapabilitiesChooser(), null );
	}

	//	/*package-private*/ boolean canCreateExternalGLDrawable() {
	//		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
	//		return glDrawableFactory.canCreateExternalGLDrawable();
	//	}
	//	/*package-private*/ javax.media.opengl.GLDrawable createExternalGLDrawable() {
	//		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
	//		if( glDrawableFactory.canCreateExternalGLDrawable() ) {
	//			return glDrawableFactory.createExternalGLDrawable();
	//		} else {
	//			return null;
	//		}
	//	}

	/* package-private */static boolean canCreateGlPixelBuffer() {
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
		return glDrawableFactory.canCreateGLPbuffer();
	}

	/* package-private */static javax.media.opengl.GLPbuffer createGlPixelBuffer( javax.media.opengl.GLCapabilities glCapabilities, javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser, int width, int height, javax.media.opengl.GLContext share ) {
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
		if( glDrawableFactory.canCreateGLPbuffer() ) {
			javax.media.opengl.GLPbuffer buffer = glDrawableFactory.createGLPbuffer( glCapabilities, glCapabilitiesChooser, width, height, share );

			// This is a work around for Linux users.
			// Because of a bug in mesa (https://bugs.freedesktop.org/show_bug.cgi?id=24320) sometimes on Linux the method glXQueryDrawable() will
			// return 0 for information about a drawable, include getWidth and getHeight even though the drawable is the correct size.
			if( mapPixelBufferToDimension != null ) {
				mapPixelBufferToDimension.put( buffer, new java.awt.Dimension( width, height ) );
			}

			return buffer;
		} else {
			return null;
		}
	}

	/* package-private */static com.sun.opengl.impl.GLDrawableImpl createOffscreenDrawable( javax.media.opengl.GLCapabilities glCapabilities, javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser ) {
		com.sun.opengl.impl.GLDrawableFactoryImpl glDrawableFactory = com.sun.opengl.impl.GLDrawableFactoryImpl.getFactoryImpl();
		return (com.sun.opengl.impl.GLDrawableImpl)glDrawableFactory.createOffscreenDrawable( glCapabilities, glCapabilitiesChooser );
	}

	/* package-private */static int getGlDrawableWidth( javax.media.opengl.GLDrawable drawable ) {
		// Bug in linux opengl, getWidth ALWAYS returns 0
		int width = drawable.getWidth();
		if( width == 0 ) {
			if( mapPixelBufferToDimension != null ) {
				if( drawable instanceof javax.media.opengl.GLPbuffer ) {
					javax.media.opengl.GLPbuffer glPixelBuffer = (javax.media.opengl.GLPbuffer)drawable;
					java.awt.Dimension size = mapPixelBufferToDimension.get( glPixelBuffer );
					if( size != null ) {
						width = size.width;
					}
				}
			}
		}
		return width;
	}

	/* package-private */static int getGlDrawableHeight( javax.media.opengl.GLDrawable drawable ) {
		// Bug in linux opengl, getHeight ALWAYS returns 0
		int height = drawable.getHeight();
		if( height == 0 ) {
			if( mapPixelBufferToDimension != null ) {
				if( drawable instanceof javax.media.opengl.GLPbuffer ) {
					javax.media.opengl.GLPbuffer glPixelBuffer = (javax.media.opengl.GLPbuffer)drawable;
					java.awt.Dimension size = mapPixelBufferToDimension.get( glPixelBuffer );
					if( size != null ) {
						height = size.height;
					}
				}
			}
		}
		return height;
	}
}
