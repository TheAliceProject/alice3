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
package edu.cmu.cs.dennisc.render.gl.imp;

/**
 * @author Dennis Cosgrove
 */
public class GlDrawableUtilities {
	private static final java.util.Map<javax.media.opengl.GLOffscreenAutoDrawable, java.awt.Dimension> mapPixelBufferToDimension;

	private static boolean areEquivalentIgnoringMultisample( javax.media.opengl.GLCapabilitiesImmutable a, javax.media.opengl.GLCapabilitiesImmutable b ) {
		if( a.getAccumAlphaBits() != b.getAccumAlphaBits() ) {
			return false;
		}
		if( a.getAccumBlueBits() != b.getAccumBlueBits() ) {
			return false;
		}
		if( a.getAccumGreenBits() != b.getAccumGreenBits() ) {
			return false;
		}
		if( a.getAccumRedBits() != b.getAccumRedBits() ) {
			return false;
		}
		if( a.getDepthBits() != b.getDepthBits() ) {
			return false;
		}
		if( a.getDoubleBuffered() != b.getDoubleBuffered() ) {
			return false;
		}
		if( a.getHardwareAccelerated() != b.getHardwareAccelerated() ) {
			return false;
		}
		//		if( a.getPbufferFloatingPointBuffers() != b.getPbufferFloatingPointBuffers() ) {
		//			return false;
		//		}
		//		if( a.getPbufferRenderToTexture() != b.getPbufferRenderToTexture() ) {
		//			return false;
		//		}
		//		if( a.getPbufferRenderToTextureRectangle() != b.getPbufferRenderToTextureRectangle() ) {
		//			return false;
		//		}
		if( a.getStencilBits() != b.getStencilBits() ) {
			return false;
		}
		if( a.getStereo() != b.getStereo() ) {
			return false;
		}
		if( a.isPBuffer() != b.isPBuffer() ) {
			return false;
		}
		if( a.isFBO() != b.isFBO() ) {
			return false;
		}
		return true;
	}

	private static class GlMultisampledCapabilitiesChooser extends javax.media.opengl.DefaultGLCapabilitiesChooser {
		private final int maximumMultisampleCount;

		public GlMultisampledCapabilitiesChooser( int maximumMultisampleCount ) {
			this.maximumMultisampleCount = maximumMultisampleCount;
		}

		@Override
		public int chooseCapabilities( javax.media.nativewindow.CapabilitiesImmutable desired, java.util.List<? extends javax.media.nativewindow.CapabilitiesImmutable> available, int windowSystemRecommendedChoice ) {
			int original = super.chooseCapabilities( desired, available, windowSystemRecommendedChoice );
			int rv = original;
			if( ( 0 <= rv ) && ( rv < available.size() ) ) {
				javax.media.nativewindow.CapabilitiesImmutable selected = available.get( rv );
				if( selected instanceof javax.media.opengl.GLCapabilitiesImmutable ) {
					javax.media.opengl.GLCapabilitiesImmutable glSelected = (javax.media.opengl.GLCapabilitiesImmutable)selected;
					int i = 0;
					int indexOfMax = -1;
					int max = -1;
					for( javax.media.nativewindow.CapabilitiesImmutable c : available ) {
						if( c instanceof javax.media.opengl.GLCapabilitiesImmutable ) {
							javax.media.opengl.GLCapabilitiesImmutable glCandidate = (javax.media.opengl.GLCapabilitiesImmutable)c;
							//edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "consider", i, glCandidate );
							if( glCandidate.getSampleBuffers() ) {
								if( areEquivalentIgnoringMultisample( glSelected, glCandidate ) ) {
									int numSamples = glCandidate.getNumSamples();
									if( numSamples <= this.maximumMultisampleCount ) {
										if( numSamples == this.maximumMultisampleCount ) {
											indexOfMax = i;
											break;
										} else {
											if( numSamples > max ) {
												indexOfMax = i;
												max = numSamples;
											}
										}
									}
								}
							}
						}
						i++;
					}
					if( indexOfMax != -1 ) {
						rv = indexOfMax;
					}
				}
				//				if( rv != original ) {
				//					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "original", original, available.get( original ) );
				//					edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "replacement", rv, available.get( rv ) );
				//				}
			}
			return rv;
		}
	}

	private static final javax.media.opengl.GLCapabilitiesChooser glDefaultCapabilitiesChooser = new javax.media.opengl.DefaultGLCapabilitiesChooser();
	private static final javax.media.opengl.GLCapabilitiesChooser glMultisampleCapabilitiesChooser;

	static {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			mapPixelBufferToDimension = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		} else {
			mapPixelBufferToDimension = null;
		}

		final int MAXIMUM_MULTISAMPLE_COUNT = 0;//4;
		if( MAXIMUM_MULTISAMPLE_COUNT > 1 ) {
			glMultisampleCapabilitiesChooser = new GlMultisampledCapabilitiesChooser( MAXIMUM_MULTISAMPLE_COUNT );
		} else {
			glMultisampleCapabilitiesChooser = null;
		}
	}

	private GlDrawableUtilities() {
		throw new AssertionError();
	}

	//private GLCapabilities glCapabilities;
	public static javax.media.opengl.GLCapabilities createGlCapabilities() {
		javax.media.opengl.GLProfile profile = javax.media.opengl.GLProfile.getDefault();

		javax.media.opengl.GLCapabilities rv = new javax.media.opengl.GLCapabilities( profile );
		//rv.setSampleBuffers( true );
		//rv.setNumSamples( 8 );

		//		javax.media.opengl.GLCapabilitiesChooser chooser = getGLCapabilitiesChooser();
		//		if( chooser instanceof edu.cmu.cs.dennisc.javax.media.opengl.HardwareAccellerationEschewingGLCapabilitiesChooser ) {
		//			rv.setHardwareAccelerated( false );
		//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: force hardware acceleration off" );
		//			rv.setDepthBits( 32 );
		//		}
		return rv;
	}

	public static javax.media.opengl.GLCapabilities createGlCapabilitiesForLightweightComponent() {
		javax.media.opengl.GLCapabilities rv = createGlCapabilities();
		final boolean IS_STENCIL_DESIRED = false;
		if( IS_STENCIL_DESIRED ) {
			rv.setStencilBits( 1 );
		}
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			//pass
		} else {
			rv.setPBuffer( true );
		}
		return rv;
	}

	public static javax.media.opengl.GLCapabilitiesChooser getPerhapsMultisampledGlCapabilitiesChooser() {
		if( glMultisampleCapabilitiesChooser != null ) {
			return glMultisampleCapabilitiesChooser;
		} else {
			return glDefaultCapabilitiesChooser;
		}
	}

	public static javax.media.opengl.GLCapabilitiesChooser getNotMultisampledGlCapabilitiesChooser() {
		return glDefaultCapabilitiesChooser;
	}

	public static javax.media.opengl.awt.GLCanvas createGLCanvas() {
		return new javax.media.opengl.awt.GLCanvas( createGlCapabilities(), getPerhapsMultisampledGlCapabilitiesChooser(), null );
	}

	public static javax.media.opengl.awt.GLJPanel createGLJPanel() {
		return new javax.media.opengl.awt.GLJPanel( createGlCapabilitiesForLightweightComponent(), getPerhapsMultisampledGlCapabilitiesChooser() );
	}

	public static boolean canCreateExternalGLDrawable() {
		javax.media.opengl.GLProfile profile = javax.media.opengl.GLProfile.getDefault();
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( profile );
		return glDrawableFactory.canCreateExternalGLDrawable( javax.media.opengl.GLProfile.getDefaultDevice() );
	}

	public static javax.media.opengl.GLDrawable createExternalGLDrawable() {
		javax.media.opengl.GLProfile profile = javax.media.opengl.GLProfile.getDefault();
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( profile );
		if( glDrawableFactory.canCreateExternalGLDrawable( javax.media.opengl.GLProfile.getDefaultDevice() ) ) {
			return glDrawableFactory.createExternalGLDrawable();
		} else {
			return null;
		}
	}

	public javax.media.opengl.GLContext createExternalGLContext() {
		javax.media.opengl.GLProfile profile = javax.media.opengl.GLProfile.getDefault();
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( profile );
		if( glDrawableFactory.canCreateExternalGLDrawable( javax.media.opengl.GLProfile.getDefaultDevice() ) ) {
			return glDrawableFactory.createExternalGLContext();
		} else {
			return null;
		}
	}

	public static boolean canCreateGlPixelBuffer() {
		javax.media.opengl.GLProfile glProfile = javax.media.opengl.GLProfile.getDefault();
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( glProfile );
		return glDrawableFactory.canCreateGLPbuffer( glDrawableFactory.getDefaultDevice(), glProfile );
	}

	public static javax.media.opengl.GLOffscreenAutoDrawable createGlPixelBuffer( javax.media.opengl.GLCapabilities glCapabilities, javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser, int width, int height, javax.media.opengl.GLContext share ) {
		javax.media.opengl.GLProfile glProfile = javax.media.opengl.GLProfile.getDefault();
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( glProfile );
		if( glDrawableFactory.canCreateGLPbuffer( glDrawableFactory.getDefaultDevice(), glProfile ) ) {
			javax.media.opengl.GLOffscreenAutoDrawable buffer = glDrawableFactory.createOffscreenAutoDrawable( glDrawableFactory.getDefaultDevice(), glCapabilities, glCapabilitiesChooser, width, height );//, share );

			// This is a work around for Linux users.
			// Because of a bug in mesa (https://bugs.freedesktop.org/show_bug.cgi?id=24320) sometimes on Linux the method glXQueryDrawable() will
			// return 0 for information about a drawable, include getWidth and getHeight even though the drawable is the correct size.
			if( mapPixelBufferToDimension != null ) {
				mapPixelBufferToDimension.put( buffer, new java.awt.Dimension( width, height ) );
			}

			return buffer;
		} else {
			//			throw new RuntimeException("cannot create pbuffer");
			return null;
		}
	}

	public static jogamp.opengl.GLDrawableImpl createOffscreenDrawable( javax.media.opengl.GLCapabilities glCapabilities, javax.media.opengl.GLCapabilitiesChooser glCapabilitiesChooser, int width, int height ) {
		javax.media.opengl.GLProfile glProfile = javax.media.opengl.GLProfile.getDefault();
		javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory( glProfile );
		return (jogamp.opengl.GLDrawableImpl)glDrawableFactory.createOffscreenDrawable( null, glCapabilities, glCapabilitiesChooser, width, height );
	}

	public static int getGlDrawableWidth( javax.media.opengl.GLDrawable drawable ) {
		// Bug in linux opengl, getWidth ALWAYS returns 0
		int width = drawable.getSurfaceWidth();
		if( width == 0 ) {
			if( mapPixelBufferToDimension != null ) {
				if( drawable instanceof javax.media.opengl.GLOffscreenAutoDrawable ) {
					javax.media.opengl.GLOffscreenAutoDrawable glPixelBuffer = (javax.media.opengl.GLOffscreenAutoDrawable)drawable;
					java.awt.Dimension size = mapPixelBufferToDimension.get( glPixelBuffer );
					if( size != null ) {
						width = size.width;
					}
				}
			}
		}
		return width;
	}

	public static int getGlDrawableHeight( javax.media.opengl.GLDrawable drawable ) {
		// Bug in linux opengl, getHeight ALWAYS returns 0
		int height = drawable.getSurfaceHeight();
		if( height == 0 ) {
			if( mapPixelBufferToDimension != null ) {
				if( drawable instanceof javax.media.opengl.GLOffscreenAutoDrawable ) {
					javax.media.opengl.GLOffscreenAutoDrawable glPixelBuffer = (javax.media.opengl.GLOffscreenAutoDrawable)drawable;
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
