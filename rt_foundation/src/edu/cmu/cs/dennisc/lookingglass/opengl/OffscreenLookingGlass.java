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
				this.glPbuffer.setSize(width, height);
			}
		} else {
			javax.media.opengl.GLDrawableFactory glDrawableFactory = javax.media.opengl.GLDrawableFactory.getFactory();
			if (glDrawableFactory.canCreateGLPbuffer()) {
				javax.media.opengl.GLCapabilities glCapabilities = this.createCapabilities();
				javax.media.opengl.GLContext share;
				if (this.lookingGlassToShareContextWith != null) {
					share = this.lookingGlassToShareContextWith.getGLAutoDrawable().getContext();
				} else {
					share = null;
				}
				this.glPbuffer = glDrawableFactory.createGLPbuffer(glCapabilities, OffscreenLookingGlass.getGLCapabilitiesChooser(), width, height, share);
				assert this.glPbuffer != null;
			} else {
				throw new RuntimeException("cannot create pbuffer");
			}
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
}
