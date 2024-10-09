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

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.render.RenderTarget;
import edu.cmu.cs.dennisc.render.VisualInclusionCriterion;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrAbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;
import org.alice.math.immutable.AffineMatrix4x4;
import org.alice.math.immutable.Matrix4x4;
import org.alice.math.immutable.Ray;
import org.alice.math.immutable.Vector3;

import java.awt.Rectangle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/abstract class PickDisplayTask extends DisplayTask {
  public PickDisplayTask(int x, int y, PickSubElementPolicy pickSubElementPolicy, VisualInclusionCriterion criterion) {
    this.x = x;
    this.y = y;
    this.pickSubElementPolicy = pickSubElementPolicy;
    this.criterion = criterion;

    final int SIZEOF_INT = 4;
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(SIZEOF_INT * SELECTION_CAPACITY);
    byteBuffer.order(ByteOrder.nativeOrder());
    this.selectionAsIntBuffer = byteBuffer.asIntBuffer();
  }

  protected abstract void fireDone(PickParameters pickParameters);

  @Override
  public final IsFrameBufferIntact handleDisplay(RenderTargetImp rtImp, GLAutoDrawable drawable, GL2 gl) {
    this.pickContext.gl = gl;

    //todo:
    ConformanceTestResults.SINGLETON.updateAsynchronousPickInformationIfNecessary(gl);

    RenderTarget rt = rtImp.getRenderTarget();
    AbstractCamera sgCamera = rtImp.getCameraAtPixel(this.x, this.y);
    PickParameters pickParameters = new PickParameters(rt, sgCamera, this.x, this.y, this.pickSubElementPolicy == PickSubElementPolicy.REQUIRED, null);

    GlrAbstractCamera<? extends AbstractCamera> cameraAdapter = AdapterFactory.getAdapterFor(sgCamera);

    this.selectionAsIntBuffer.rewind();
    this.pickContext.gl.glSelectBuffer(SELECTION_CAPACITY, this.selectionAsIntBuffer);

    this.pickContext.gl.glRenderMode(GL2.GL_SELECT);
    this.pickContext.gl.glInitNames();

    Rectangle actualViewport = rt.getActualViewportAsAwtRectangle(sgCamera);
    this.pickContext.gl.glViewport(actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height);
    cameraAdapter.performPick(this.pickContext, pickParameters, actualViewport);
    this.pickContext.gl.glFlush();

    this.selectionAsIntBuffer.rewind();
    int length = this.pickContext.gl.glRenderMode(GL2.GL_RENDER);
    //todo: invesigate negative length
    //assert length >= 0;

    if (length > 0) {
      SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[length];
      int offset = 0;
      for (int i = 0; i < length; i++) {
        selectionBufferInfos[i] = new SelectionBufferInfo(this.pickContext, this.selectionAsIntBuffer, offset);
        offset += 7;
      }

      final boolean isPickFunctioningCorrectly = true; //TODO
      if (isPickFunctioningCorrectly) {
        double x = pickParameters.getX();
        double y = pickParameters.getFlippedY(actualViewport);

        Matrix4x4 m = Matrix4x4.fromTranslation(new Vector3(
            actualViewport.width - (2 * (x - actualViewport.x)),
            actualViewport.height - (2 * (y - actualViewport.y)),
            0));

        Matrix4x4 scale = Matrix4x4.fromScale(actualViewport.width, actualViewport.height, 1.0);
        Matrix4x4 p = cameraAdapter.getActualProjectionMatrix(actualViewport);

        m = m.times(scale).times(p).invert();
        for (SelectionBufferInfo selectionBufferInfo : selectionBufferInfos) {
          selectionBufferInfo.updatePointInSource(m);
        }
      } else {
        Ray ray = cameraAdapter.getRayAtPixel(pickParameters.getX(), pickParameters.getY(), actualViewport);
        ray = ray.normalized();
        AffineMatrix4x4 inverseAbsoluteTransformation = sgCamera.getInverseAbsoluteTransformation().immutable();
        for (SelectionBufferInfo selectionBufferInfo : selectionBufferInfos) {
          selectionBufferInfo.updatePointInSource(ray, inverseAbsoluteTransformation);
        }
      }

      if (length > 1) {
        //        float front0 = selectionBufferInfos[ 0 ].getZFront();
        //        boolean isDifferentiated = false;
        //        for( int i=1; i<length; i++ ) {
        //          if( front0 == selectionBufferInfos[ i ].getZFront() ) {
        //            //pass
        //          } else {
        //            isDifferentiated = true;
        //            break;
        //          }
        //        }
        //        java.util.Comparator< SelectionBufferInfo > comparator;
        //        if( isDifferentiated ) {
        //          comparator = new java.util.Comparator< SelectionBufferInfo >() {
        //            public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
        //              return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
        //            }
        //          };
        //        } else {
        //          if( conformanceTestResults.isPickFunctioningCorrectly() ) {
        //            edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: conformance test reports pick is functioning correctly" );
        //            comparator = null;
        //          } else {
        //            edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
        //            ray.setNaN();
        //            cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport);
        //            for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
        //              selectionBufferInfo.updatePointInSource( ray );
        //            }
        //            comparator = new java.util.Comparator< SelectionBufferInfo >() {
        //              public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
        //                return Double.compare( sbi1.getPointInSource().z, sbi2.getPointInSource().z );
        //              }
        //            };
        //          }
        //        }
        Comparator<SelectionBufferInfo> comparator;
        if (isPickFunctioningCorrectly) {
          comparator = new Comparator<SelectionBufferInfo>() {
            @Override
            public int compare(SelectionBufferInfo sbi1, SelectionBufferInfo sbi2) {
              return Float.compare(sbi1.getZFront(), sbi2.getZFront());
            }
          };
        } else {
          comparator = new Comparator<SelectionBufferInfo>() {
            @Override
            public int compare(SelectionBufferInfo sbi1, SelectionBufferInfo sbi2) {
              double z1 = -sbi1.getPointInSource().z();
              double z2 = -sbi2.getPointInSource().z();
              return Double.compare(z1, z2);
            }
          };
        }
        Arrays.sort(selectionBufferInfos, comparator);
      }
      for (SelectionBufferInfo selectionBufferInfo : selectionBufferInfos) {
        pickParameters.addPickResult(sgCamera, selectionBufferInfo.getSgVisual(), selectionBufferInfo.isFrontFacing(), selectionBufferInfo.getSGGeometry(), selectionBufferInfo.getSubElement(), selectionBufferInfo.getPointInSource());
      }
    }

    this.fireDone(pickParameters);
    return IsFrameBufferIntact.TRUE;
  }

  private final int x;
  private final int y;
  private final PickSubElementPolicy pickSubElementPolicy;
  private final VisualInclusionCriterion criterion;

  private static final int SELECTION_CAPACITY = 256;
  private final PickContext pickContext = new PickContext(false);
  private final IntBuffer selectionAsIntBuffer;
}
