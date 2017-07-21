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
package edu.cmu.cs.dennisc.render.gl.imp;

import edu.cmu.cs.dennisc.render.gl.GlDrawableUtils;


/**
 * @author Dennis Cosgrove
 */
/* package-private */abstract class OffscreenDrawable {
	/* package-private */static final boolean IS_HARDWARE_ACCELERATION_DESIRED = true;//edu.cmu.cs.dennisc.java.lang.SystemUtilities.getBooleanProperty( "jogl.gljpanel.nohw", false ) == false;

	public static interface DisplayCallback {
		public void display( com.jogamp.opengl.GL2 gl );
	}

	public static OffscreenDrawable createInstance( DisplayCallback callback, com.jogamp.opengl.GLCapabilities glRequestedCapabilities, com.jogamp.opengl.GLCapabilitiesChooser glCapabilitiesChooser, com.jogamp.opengl.GLContext glShareContext, int width, int height ) {
		OffscreenDrawable od = null;
		if( IS_HARDWARE_ACCELERATION_DESIRED && GlDrawableUtils.canCreateGlPixelBuffer() ) {
			od = new PixelBufferOffscreenDrawable( callback );
			try {
				od.initialize( glRequestedCapabilities, glCapabilitiesChooser, glShareContext, 1, 1 );
			} catch( com.jogamp.opengl.GLException gle ) {
				try {
					od.destroy();
				} catch( Throwable t ) {
					//pass
				}
				od = null;
			}
		}
		if( od != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( callback );
			od = new SoftwareOffscreenDrawable( callback );
			try {
				od.initialize( glRequestedCapabilities, glCapabilitiesChooser, glShareContext, 1, 1 );
			} catch( com.jogamp.opengl.GLException gle ) {
				try {
					od.destroy();
				} catch( Throwable t ) {
					//pass
				}
				od = null;
				throw gle;
			}
		}
		return od;
	}

	private final DisplayCallback callback;

	public OffscreenDrawable( DisplayCallback callback ) {
		this.callback = callback;
	}

	public DisplayCallback getCallback() {
		return this.callback;
	}

	public abstract void initialize( com.jogamp.opengl.GLCapabilities glRequestedCapabilities, com.jogamp.opengl.GLCapabilitiesChooser glCapabilitiesChooser, com.jogamp.opengl.GLContext glShareContext, int width, int height );

	public abstract void destroy();

	public abstract void display();

	public final java.awt.Dimension getSize( java.awt.Dimension rv ) {
		com.jogamp.opengl.GLDrawable glDrawable = this.getGlDrawable();
		if( glDrawable != null ) {
			rv.width = GlDrawableUtils.getGlDrawableHeight( glDrawable );
			rv.height = GlDrawableUtils.getGlDrawableHeight( glDrawable );
		} else {
			//todo?
			throw new com.jogamp.opengl.GLException();
		}
		return rv;
	}

	public abstract boolean isHardwareAccelerated();

	protected abstract com.jogamp.opengl.GLDrawable getGlDrawable();

	protected final void fireDisplay( com.jogamp.opengl.GL2 gl ) {
		if( this.callback != null ) {
			this.callback.display( gl );
		}
	}
}
