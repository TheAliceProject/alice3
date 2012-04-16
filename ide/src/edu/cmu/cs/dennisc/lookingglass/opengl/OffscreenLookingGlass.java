/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
class OffscreenLookingGlass extends AbstractLookingGlass implements edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass {
	private javax.media.opengl.GLPbuffer glPbuffer;
	private AbstractLookingGlass lookingGlassToShareContextWith;

	/* package-private */OffscreenLookingGlass(LookingGlassFactory lookingGlassFactory, AbstractLookingGlass lookingGlassToShareContextWith) {
		super(lookingGlassFactory);
		this.lookingGlassToShareContextWith = lookingGlassToShareContextWith;
	}

	public java.awt.Dimension getSize(java.awt.Dimension rv) {
		if (this.glPbuffer != null) {
			rv.setSize(this.glPbuffer.getWidth(), this.glPbuffer.getHeight());
		} else {
			rv.setSize(0, 0);
		}
		return rv;
	}

	public void setSize(int width, int height) {
		assert width > 0;
		assert height > 0;
		if (this.glPbuffer != null) {
			if (width != this.glPbuffer.getWidth() || height != this.glPbuffer.getHeight()) {
				javax.media.opengl.GLContext share = this.glPbuffer.getContext();
				this.glPbuffer.destroy();
				this.glPbuffer = LookingGlassFactory.getInstance().createGLPbuffer( width, height, LookingGlassFactory.getSampleCountForDisabledMultisampling(), share );
				if( this.glPbuffer != null ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "create external drawable" );
				}
			}
		} else {
			javax.media.opengl.GLContext share;
			if (this.lookingGlassToShareContextWith != null) {
				share = this.lookingGlassToShareContextWith.getGLAutoDrawable().getContext();
			} else {
				share = null;
			}
			this.glPbuffer = LookingGlassFactory.getInstance().createGLPbuffer( width, height, LookingGlassFactory.getSampleCountForDisabledMultisampling(), share );
		}
	}

	public void clearAndRenderOffscreen() {
		getGLAutoDrawable().display();
	}

	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		if (this.glPbuffer != null) {
			this.glPbuffer.destroy();
		}
	}

	@Override
	protected javax.media.opengl.GLAutoDrawable getGLAutoDrawable() {
		assert this.glPbuffer != null;
		return this.glPbuffer;
	}
	
	@Override
	protected void repaintIfAppropriate() {
	}
}
