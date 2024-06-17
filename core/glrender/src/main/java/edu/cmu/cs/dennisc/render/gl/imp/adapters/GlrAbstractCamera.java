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

import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import edu.cmu.cs.dennisc.math.Matrix4x4;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.render.Graphics2D;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.render.gl.imp.Context;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

import java.awt.Rectangle;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrAbstractCamera<T extends AbstractCamera> extends GlrLeaf<T> {
  public abstract Ray getRayAtPixel(Ray rv, int xPixel, int yPixel, Rectangle actualViewport);

  protected abstract Rectangle performLetterboxing(Rectangle rv);

  public Rectangle getActualViewport(Rectangle rv, int surfaceWidth, int surfaceHeight) {
    if (this.specifiedViewport != null) {
      rv.setBounds(this.specifiedViewport);
    } else {
      rv.setBounds(0, 0, surfaceWidth, surfaceHeight);
    }
    if (this.isLetterboxed) {
      performLetterboxing(rv);
    }
    return rv;
  }

  public abstract Matrix4x4 getActualProjectionMatrix(Matrix4x4 rv, Rectangle actualViewport);

  public boolean isLetterboxed() {
    return this.isLetterboxed;
  }

  public void setIsLetterboxed(boolean isLetterboxed) {
    this.isLetterboxed = isLetterboxed;
  }

  protected abstract void setupProjection(Context context, Rectangle actualViewport);

  public void performClearAndRenderOffscreen(RenderContext rc, int surfaceWidth, int surfaceHeight) {
    GlrScene sceneAdapter = getGlrScene();
    if (sceneAdapter != null) {
      Rectangle actualViewport = getActualViewport(new Rectangle(), surfaceWidth, surfaceHeight);
      rc.gl.glMatrixMode(GL_PROJECTION);
      rc.gl.glLoadIdentity();
      setupProjection(rc, actualViewport);
      rc.setViewportAndAddToClearRect(actualViewport);
      sceneAdapter.renderScene(rc, this, this.glrBackground);
    }
  }

  public void postRender(RenderContext rc, int surfaceWidth, int surfaceHeight, RenderTarget renderTarget, Graphics2D g2) {
    if (this.glrLayers != null) {
      Rectangle actualViewport = getActualViewport(new Rectangle(), surfaceWidth, surfaceHeight);
      for (GlrLayer layerAdapter : this.glrLayers) {
        layerAdapter.render(g2, renderTarget, actualViewport, this.owner);
      }
    }
  }

  public void performPick(PickContext pc, PickParameters pickParameters, Rectangle actualViewport) {
    GlrScene sceneAdapter = getGlrScene();
    if (sceneAdapter != null) {

      pc.gl.glViewport(actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height);

      pc.gl.glMatrixMode(GL_PROJECTION);
      pc.gl.glLoadIdentity();

      // actualViewport.x & y are set > 0 when letterboxing
      double tx = actualViewport.width - (2 * (pickParameters.getX() - actualViewport.x));
      double ty = actualViewport.height - (2 * (pickParameters.getFlippedY(actualViewport) + actualViewport.y));
      pc.gl.glTranslated(tx, ty, 0.0);
      pc.gl.glScaled(actualViewport.width, actualViewport.height, 1.0);
      //      int[] vp = { actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height };
      //      java.nio.IntBuffer vpBuffer = java.nio.IntBuffer.wrap( vp );
      //      pc.glu.gluPickMatrix( pickParameters.getX(), pickParameters.getFlippedY( actualViewport ), 1.0, 1.0, vpBuffer );

      setupProjection(pc, actualViewport);

      pc.pickScene(this, sceneAdapter, pickParameters);
    }
  }

  @Override
  protected void propertyChanged(InstanceProperty<?> property) {
    if (property == owner.background) {
      this.glrBackground = AdapterFactory.getAdapterFor(owner.background.getValue());
    } else if (property == owner.postRenderLayers) {
      this.glrLayers = AdapterFactory.getAdaptersFor(owner.postRenderLayers.getValue(), GlrLayer.class);
    } else {
      super.propertyChanged(property);
    }
  }

  private GlrBackground glrBackground;
  private GlrLayer[] glrLayers;

  private Rectangle specifiedViewport;
  private boolean isLetterboxed = true;
}
