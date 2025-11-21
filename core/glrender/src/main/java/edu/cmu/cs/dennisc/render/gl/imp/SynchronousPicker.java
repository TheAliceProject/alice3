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

import com.jogamp.opengl.*;
import edu.cmu.cs.dennisc.render.PickObserver;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.render.gl.GlDrawableUtils;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.ChangeHandler;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Point3;
import org.alice.math.immutable.Ray;

import java.awt.Point;
import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

/**
 * @author Dennis Cosgrove
 */
public final class SynchronousPicker implements edu.cmu.cs.dennisc.render.SynchronousPicker {
  private static class ActualPicker {
    private static final int SELECTION_CAPACITY = 256;
    private final PickContext pickContext = new PickContext(true);
    private final IntBuffer selectionAsIntBuffer;

    private final GLCapabilitiesChooser glCapabilitiesChooser;
    private final GLCapabilities glRequestedCapabilities;
    private final GLContext glShareContext;

    private PickParameters pickParameters;

    public ActualPicker() {
      final int SIZEOF_INT = 4;
      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SIZEOF_INT * SELECTION_CAPACITY);
      byteBuffer.order(ByteOrder.nativeOrder());
      this.selectionAsIntBuffer = byteBuffer.asIntBuffer();

      GLProfile glProfile = GLProfile.getDefault();
      this.glRequestedCapabilities = new GLCapabilities(glProfile);
      this.glRequestedCapabilities.setDoubleBuffered(false);
      this.glCapabilitiesChooser = new DefaultGLCapabilitiesChooser();
      this.glShareContext = null;
    }

    public void setPickParameters(RenderTarget renderTarget, AbstractCamera sgCamera, Point mousePos, boolean isSubElementRequired, PickObserver pickObserver) {
      this.pickParameters = new PickParameters(renderTarget, sgCamera, mousePos, isSubElementRequired, pickObserver);
    }

    public void clearPickParameters() {
      this.pickParameters = null;
    }

    public List<PickResult> accessAllPickResults() {
      return this.pickParameters.accessAllPickResults();
    }

    public PickResult accessFrontMostPickResult() {
      return this.pickParameters.accessFrontMostPickResult();
    }

    private OffscreenDrawable glOffscreenDrawable;

    private synchronized OffscreenDrawable getOffscreenDrawable() {
      if (this.glOffscreenDrawable == null) {
        this.glOffscreenDrawable = OffscreenDrawable.createInstance(sharedActualPicker::performPick, glRequestedCapabilities, glCapabilitiesChooser, glShareContext);
      }
      return this.glOffscreenDrawable;
    }

    private void performPick(GL2 gl) {
      pickContext.gl = gl;
      ConformanceTestResults.SINGLETON.updateSynchronousPickInformationIfNecessary(gl, GlDrawableUtils.canCreateGlPixelBuffer(), glOffscreenDrawable instanceof NativeOffscreenDrawable);

      ConformanceTestResults.SynchronousPickDetails pickDetails = ConformanceTestResults.SINGLETON.getSynchronousPickDetails();

      if (pickParameters != null) {
        PickObserver pickObserver = pickParameters.getPickObserver();
        if (pickObserver != null) {
          pickObserver.prePick();
        }
        ChangeHandler.handleBufferedChanges();
        AbstractCamera sgCamera = pickParameters.getSGCamera();
        GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);

        selectionAsIntBuffer.rewind();
        pickContext.gl.glSelectBuffer(SELECTION_CAPACITY, selectionAsIntBuffer);

        pickContext.gl.glRenderMode(GL2.GL_SELECT);
        pickContext.gl.glInitNames();

        RenderTarget renderTarget = pickParameters.getRenderTarget();
        Rectangle actualViewport = renderTarget.getActualViewportAsAwtRectangle(sgCamera);
        pickContext.gl.glViewport(actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height);

        GlrScene sceneAdapter = cameraAdapter.getGlrScene();
        if (sceneAdapter != null) {
          pickContext.gl.glMatrixMode(GL_PROJECTION);
          pickContext.gl.glLoadIdentity();

          // actualViewport.x & y are set > 0 when letterboxing
          double tx = actualViewport.width - (2 * (pickParameters.getX() - actualViewport.x));
          double ty = actualViewport.height - (2 * (pickParameters.getFlippedY(actualViewport) + actualViewport.y));
          pickContext.gl.glTranslated(tx, ty, 0.0);
          pickContext.gl.glScaled(actualViewport.width, actualViewport.height, 1.0);

          cameraAdapter.setupProjection(pickContext, actualViewport);
          pickContext.pickScene(cameraAdapter, sceneAdapter, pickParameters);
        }
        pickContext.gl.glFlush();

        selectionAsIntBuffer.rewind();
        int length = pickContext.gl.glRenderMode(GL2.GL_RENDER);
        //todo: investigate negative length
        //assert length >= 0;

        if (length > 0) {
          SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[length];
          int offset = 0;
          for (int i = 0; i < length; i++) {
            selectionBufferInfos[i] = new SelectionBufferInfo(pickContext, selectionAsIntBuffer, offset);
            offset += 7;
          }

          if (pickDetails.isPickFunctioningCorrectly()) {
            double x = pickParameters.getX();
            double y = pickParameters.getFlippedY(actualViewport);

            Point3 translation = new Point3(
                actualViewport.width - (2 * (x - actualViewport.x)),
                actualViewport.height - (2 * (y - actualViewport.y)),
                0);
            Matrix4x4 m = Matrix4x4.fromTranslation(translation);
            Matrix4x4 scale = Matrix4x4.fromScale(actualViewport.width, actualViewport.height, 1.0);
            Matrix4x4 p = cameraAdapter.getActualProjectionMatrix(actualViewport);

            m = m.times(scale).times(p).invert();
            for (SelectionBufferInfo selectionBufferInfo : selectionBufferInfos) {
              selectionBufferInfo.updatePointInSource(m);
            }
          } else {
            Ray ray = cameraAdapter.getRayAtViewportPixel(pickParameters.getX(), pickParameters.getFlippedY(actualViewport), actualViewport).normalized();
            AffineMatrix4x4 inverseAbsoluteTransformation = sgCamera.getInverseAbsoluteTransformation();
            for (SelectionBufferInfo selectionBufferInfo : selectionBufferInfos) {
              selectionBufferInfo.updatePointInSource(ray, inverseAbsoluteTransformation);
            }
          }

          if (length > 1) {
            Comparator<SelectionBufferInfo> comparator;
            if (pickDetails.isPickFunctioningCorrectly()) {
              comparator = (sbi1, sbi2) -> Float.compare(sbi1.getZFront(), sbi2.getZFront());
            } else {
              comparator = (sbi1, sbi2) -> {
                double z1 = -sbi1.getPointInSource().z();
                double z2 = -sbi2.getPointInSource().z();
                return Double.compare(z1, z2);
              };
            }
            Arrays.sort(selectionBufferInfos, comparator);
          }
          for (SelectionBufferInfo selectionBufferInfo : selectionBufferInfos) {
            pickParameters.addPickResult(sgCamera, selectionBufferInfo.getSgVisual(), selectionBufferInfo.isFrontFacing(), selectionBufferInfo.getSGGeometry(), selectionBufferInfo.getSubElement(), selectionBufferInfo.getPointInSource());
          }
        }
        if (pickObserver != null) {
          pickObserver.postPick();
        }
        ChangeHandler.handleBufferedChanges();
      }
    }

    private PickResult pickFrontMost(RenderTargetImp rtImp, Point mousePos, boolean isSubElementRequired, PickObserver pickObserver) {
      AbstractCamera sgCamera = rtImp.getCameraAtAwtPoint(mousePos);
      OffscreenDrawable impl = this.getOffscreenDrawable();
      if (impl != null) {
        this.setPickParameters(rtImp.getRenderTarget(), sgCamera, mousePos, isSubElementRequired, pickObserver);
        try {
          if (sgCamera != null) {
            impl.display();
          }
          return this.accessFrontMostPickResult();
        } finally {
          this.clearPickParameters();
        }
      } else {
        return new PickResult(sgCamera);
      }
    }

    private List<PickResult> pickAll(RenderTargetImp rtImp, Point mousePos, boolean isSubElementRequired, PickObserver pickObserver) {
      AbstractCamera sgCamera = rtImp.getCameraAtAwtPoint(mousePos);
      OffscreenDrawable impl = this.getOffscreenDrawable();
      if (impl != null) {
        this.setPickParameters(rtImp.getRenderTarget(), sgCamera, mousePos, isSubElementRequired, pickObserver);
        try {
          if (sgCamera != null) {
            impl.display();
          }
          return this.accessAllPickResults();
        } finally {
          this.clearPickParameters();
        }
      } else {
        return Collections.emptyList();
      }
    }
  }

  private static final ActualPicker sharedActualPicker = new ActualPicker();

  public SynchronousPicker(RenderTargetImp rtImp) {
    this.rtImp = rtImp;
  }

  @Override
  public List<PickResult> pickAll(Point mousePos, PickSubElementPolicy pickSubElementPolicy) {
    return this.pickAll(mousePos, pickSubElementPolicy, null);
  }

  @Override
  public List<PickResult> pickAll(Point mousePos, PickSubElementPolicy pickSubElementPolicy, PickObserver pickObserver) {
    synchronized (sharedActualPicker) {
      return sharedActualPicker.pickAll(this.rtImp, mousePos, pickSubElementPolicy == PickSubElementPolicy.REQUIRED, pickObserver);
    }
  }

  @Override
  public PickResult pickFrontMost(Point mousePos, PickSubElementPolicy pickSubElementPolicy) {
    return this.pickFrontMost(mousePos, pickSubElementPolicy, null);
  }

  @Override
  public PickResult pickFrontMost(Point mousePos, PickSubElementPolicy pickSubElementPolicy, PickObserver pickObserver) {
    synchronized (sharedActualPicker) {
      return sharedActualPicker.pickFrontMost(this.rtImp, mousePos, pickSubElementPolicy == PickSubElementPolicy.REQUIRED, pickObserver);
    }
  }

  private final RenderTargetImp rtImp;
}
