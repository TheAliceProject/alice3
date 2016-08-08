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
public final class SoftwareOffscreenDrawable extends OffscreenDrawable {
	private jogamp.opengl.GLDrawableImpl glDrawable;
	private jogamp.opengl.GLContextImpl glContext;

	private final Runnable displayAdapter = new Runnable() {
		@Override
		public void run() {
			fireDisplay( glContext.getGL().getGL2() );
		}
	};
	private final Runnable initAdapter = new Runnable() {
		@Override
		public void run() {
		}
	};
	private jogamp.opengl.GLDrawableHelper drawableHelper;

	public SoftwareOffscreenDrawable( DisplayCallback callback ) {
		super( callback );
	}

	@Override
	protected com.jogamp.opengl.GLDrawable getGlDrawable() {
		return this.glDrawable;
	}

	@Override
	public void initialize( com.jogamp.opengl.GLCapabilities glRequestedCapabilities, com.jogamp.opengl.GLCapabilitiesChooser glCapabilitiesChooser, com.jogamp.opengl.GLContext glShareContext, int width, int height ) {
		assert this.glDrawable == null : this;
		com.jogamp.opengl.GLCapabilities glCapabilities;
		if( IS_HARDWARE_ACCELERATION_DESIRED ) {
			glCapabilities = glRequestedCapabilities;
		} else {
			glCapabilities = (com.jogamp.opengl.GLCapabilities)glRequestedCapabilities.clone();
			glCapabilities.setHardwareAccelerated( false );
		}
		this.glDrawable = GlDrawableUtils.createOffscreenDrawable( glCapabilities, glCapabilitiesChooser, width, height );
		this.glDrawable.setRealized( true );
		this.glContext = (jogamp.opengl.GLContextImpl)this.glDrawable.createContext( glShareContext );
		//this.glContext.setSynchronized( true );
	}

	@Override
	public void destroy() {
		assert false;
		if( this.glContext != null ) {
			this.glContext.destroy();
			this.glContext = null;
		}
		if( this.glDrawable != null ) {
			com.jogamp.nativewindow.AbstractGraphicsDevice graphicsDevice = this.glDrawable.getNativeSurface().getGraphicsConfiguration().getScreen().getDevice();
			this.glDrawable.setRealized( false );
			this.glDrawable = null;
			if( graphicsDevice != null ) {
				graphicsDevice.close();
			}
		}
	}

	@Override
	public void display() {
		if( this.drawableHelper != null ) {
			//pass
		} else {
			this.drawableHelper = new jogamp.opengl.GLDrawableHelper();
		}
		if( ( this.glDrawable != null ) && ( this.glContext != null ) && ( this.displayAdapter != null ) && ( this.initAdapter != null ) ) {
			//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( this.glContext );
			this.drawableHelper.invokeGL( this.glDrawable, this.glContext, this.displayAdapter, this.initAdapter );
		} else {
			StringBuilder sb = new StringBuilder();
			if( this.glDrawable != null ) {
				//pass
			} else {
				sb.append( "glDrawable is null;" );
			}
			if( this.glContext != null ) {
				//pass
			} else {
				sb.append( "glContext is null;" );
			}
			if( this.displayAdapter != null ) {
				//pass
			} else {
				sb.append( "displayAdapter is null;" );
			}
			if( this.initAdapter != null ) {
				//pass
			} else {
				sb.append( "initAdapter is null;" );
			}
			throw new com.jogamp.opengl.GLException( sb.toString() );
		}
	}

	@Override
	public boolean isHardwareAccelerated() {
		return false;
	}
}
