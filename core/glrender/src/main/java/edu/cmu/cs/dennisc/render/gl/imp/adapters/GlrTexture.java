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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.render.gl.ForgettableBinding;
import edu.cmu.cs.dennisc.render.gl.TextureBinding;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.texture.Texture;
import edu.cmu.cs.dennisc.texture.event.TextureEvent;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrTexture<T extends Texture> extends GlrObject<T> {
  /*package-private*/
  static void handleTextureChanged(TextureEvent e) {
    GlrTexture<?> textureAdapter = AdapterFactory.getAdapterFor(e.getTypedSource());
    textureAdapter.handleTextureChanged();
  }

  public boolean isPotentiallyAlphaBlended() {
    return owner.isPotentiallyAlphaBlended();
  }

  public boolean isValid() {
    if (owner != null) {
      return owner.isValid();
    } else {
      return false;
    }
  }

  public void addRenderContext(RenderContext rc) {
    this.renderContexts.add(rc);
  }

  public void removeRenderContext(RenderContext rc) {
    this.renderContexts.remove(rc);
  }

  //Simple reference counting
  //Single textures (like ground textures) can be referenced by multiple objects
  //The ref adding and removing is handled by GlrTexturedAppearance when a new adapter is acquired
  public void addReference() {
    this.refCount++;
  }

  public void removeReference() {
    if (this.refCount <= 0) {
      Logger.severe("TRYING TO REMOVE REFERENCE WHEN REFCOUNT IN <= 0 " + this.hashCode());
    } else {
      this.refCount--;
    }
  }

  public boolean isReferenced() {
    return this.refCount > 0;
  }

  @Override
  protected void handleReleased() {
    super.handleReleased();
    if (this.renderContexts.size() > 0) {
      for (RenderContext rc : this.renderContexts) {
        rc.forgetTextureAdapter(this, true);
      }
      this.renderContexts.clear();
    }
  }

  private void handleTextureChanged() {
    setDirty(true);
  }

  protected boolean isDirty() {
    return this.isTextureDataDirty;
  }

  protected void setDirty(boolean isDirty) {
    this.isTextureDataDirty = isDirty;
  }

  //todo: map u and v for non power of 2 textures?
  public float mapU(float u) {
    return u;
  }

  public float mapV(float v) {
    return v;
  }

  protected static TextureData newTextureData(GL gl, BufferedImage image, boolean isMipMapDesired) {
    TextureData rv = AWTTextureIO.newTextureData(gl.getGLProfile(), image, isMipMapDesired);
    return rv;
    //return com.jogamp.opengl.util.texture.TextureIO.newTextureData( image, isMipMapDesired );
  }

  protected abstract TextureData newTextureData(GL gl, TextureData currentTexture);

  public ForgettableBinding bindTexture(RenderContext rc) {
    if (this.isDirty()) {
      if (this.textureData != null) {
        Logger.info("new texture data", this);
      }
      this.textureData = this.newTextureData(rc.gl, this.textureData);
      this.setDirty(false);
    }
    this.textureBinding.ensureUpToDate(rc, this.textureData);
    return this.textureBinding;
  }

  private final TextureBinding textureBinding = new TextureBinding();
  private final List<RenderContext> renderContexts = Lists.newCopyOnWriteArrayList();
  private TextureData textureData;
  private boolean isTextureDataDirty = true;

  private int refCount = 0;
}
