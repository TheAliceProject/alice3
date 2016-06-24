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
package edu.cmu.cs.dennisc.render.gl;

/**
 * @author Dennis Cosgrove
 */
public class RendererNativeLibraryLoader {
	private RendererNativeLibraryLoader() {
		throw new AssertionError();
	}

	private static boolean isInitializationAttempted;

	public static synchronized void initializeIfNecessary() {
		if( isInitializationAttempted ) {
			//pass
		} else {
			try {
				com.jogamp.common.jvm.JNILibLoaderBase.setLoadingAction( new com.jogamp.common.jvm.JNILibLoaderBase.LoaderAction() {
					private final java.util.Set<String> loaded = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();

					private boolean loadLibrary( String libraryName, boolean isIgnoringError ) {
						edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle loadLibraryReportStyle = isIgnoringError ? edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle.SILENT : edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle.EXCEPTION;
						//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( libraryName, loadLibraryReportStyle );
						try {
							edu.cmu.cs.dennisc.java.lang.SystemUtilities.loadLibrary( "jogl", libraryName, loadLibraryReportStyle );
						} catch( UnsatisfiedLinkError ule ) {
							String message = ule.getMessage();
							if( isIgnoringError || ( ( message != null ) && message.contains( "already loaded" ) ) ) {
								return false;
							} else {
								System.err.println( libraryName );
								throw ule;
							}
						}
						return true;
					}

					private boolean loadLibrary( String libraryName, boolean isIgnoringError, boolean isPlatformAttemptedFirst ) {
						boolean isSuccessful;
						synchronized( this.loaded ) {
							if( this.loaded.contains( libraryName ) ) {
								isSuccessful = true;
							} else {
								isSuccessful = this.loadLibrary( libraryName, isIgnoringError );
								if( isSuccessful ) {
									this.loaded.add( libraryName );
								}
							}
						}
						return isSuccessful;
					}

					@Override
					public boolean loadLibrary( String libname, boolean isIgnoringError, ClassLoader classLoader ) {
						return this.loadLibrary( libname, isIgnoringError, true );
					}

					@Override
					public void loadLibrary( String libname, String[] preloadLibraryNames, boolean isIgnoringError, ClassLoader classLoader ) {
						if( preloadLibraryNames != null ) {
							for( String preloadLibraryName : preloadLibraryNames ) {
								this.loadLibrary( preloadLibraryName, isIgnoringError, false );
							}
						}
						this.loadLibrary( libname, isIgnoringError, true );
					}
				} );

				//edu.cmu.cs.dennisc.timing.Timer timer = new edu.cmu.cs.dennisc.timing.Timer( "initialize jogl" );
				//timer.start();
				javax.media.opengl.GLProfile.initSingleton();
				//timer.stopAndPrintResults();
			} finally {
				isInitializationAttempted = true;
			}
		}
	}

	public static void main( String[] args ) {
		initializeIfNecessary();
	}
}
