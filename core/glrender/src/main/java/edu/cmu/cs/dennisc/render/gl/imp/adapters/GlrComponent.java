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

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.pattern.Visitable;
import edu.cmu.cs.dennisc.pattern.Visitor;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import edu.cmu.cs.dennisc.scenegraph.event.HierarchyEvent;

import java.nio.DoubleBuffer;

/**
 * @author Dennis Cosgrove
 */
public abstract class GlrComponent<T extends Component> extends GlrElement<T> implements Visitable {
  private static AffineMatrix4x4 s_buffer = AffineMatrix4x4.createNaN();

  /*package-private*/
  static void handleAbsoluteTransformationChanged(Component component) {
    GlrComponent<? extends Component> componentAdapter = AdapterFactory.getAdapterFor(component);
    componentAdapter.handleAbsoluteTransformationChanged();
  }

  /*package-private*/
  static void handleHierarchyChanged(HierarchyEvent e) {
    GlrComponent<? extends Component> componentAdapter = AdapterFactory.getAdapterFor(e.getTypedSource());
    componentAdapter.handleHierarchyChanged();
  }

  public GlrComponent() {
    this.handleAbsoluteTransformationChanged();
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  private void handleAbsoluteTransformationChanged() {
    synchronized (this.absolute) {
      this.absolute[0] = Double.NaN;
    }
    synchronized (this.inverseAbsolute) {
      this.inverseAbsolute[0] = Double.NaN;
    }
  }

  private void handleHierarchyChanged() {
    GlrScene glrScene;
    Composite sgRoot = owner.getRoot();
    if (sgRoot instanceof Scene) {
      //edu.cmu.cs.dennisc.scenegraph.Scene sgScene = (edu.cmu.cs.dennisc.scenegraph.Scene)sgRoot;
      glrScene = AdapterFactory.getAdapterFor((Scene) sgRoot);
    } else {
      glrScene = null;
    }

    if (this.glrScene != glrScene) {
      if (this.glrScene != null) {
        this.glrScene.removeDescendant(this);
      }
      this.glrScene = glrScene;
      if (this.glrScene != null) {
        this.glrScene.addDescendant(this);
      }
    }

  }

  public GlrScene getGlrScene() {
    Composite sgRoot = owner.getRoot();
    if (sgRoot instanceof Scene) {
      return AdapterFactory.getAdapterFor((Scene) sgRoot);
    } else {
      return null;
    }
  }

  private void updateAbsoluteTransformationIfNecessary() {
    synchronized (this.absolute) {
      if (Double.isNaN(this.absolute[0])) {
        synchronized (s_buffer) {
          owner.getAbsoluteTransformation(s_buffer);
          assert s_buffer.isNaN() == false;
          s_buffer.getAsColumnMajorArray16(this.absolute);
        }
      }
    }
  }

  private void updateInverseAbsoluteTransformationIfNecessary() {
    synchronized (this.inverseAbsolute) {
      if (Double.isNaN(this.inverseAbsolute[0])) {
        synchronized (s_buffer) {
          owner.getInverseAbsoluteTransformation(s_buffer);
          assert s_buffer.isNaN() == false;
          s_buffer.getAsColumnMajorArray16(this.inverseAbsolute);
        }
      }
    }
  }

  public DoubleBuffer accessAbsoluteTransformationAsBuffer() {
    this.updateAbsoluteTransformationIfNecessary();
    return this.absoluteBuffer;
  }

  public DoubleBuffer accessInverseAbsoluteTransformationAsBuffer() {
    this.updateInverseAbsoluteTransformationIfNecessary();
    return this.inverseAbsoluteBuffer;
  }

  private final double[] absolute = new double[16];
  private final double[] inverseAbsolute = new double[16];
  private final DoubleBuffer absoluteBuffer = DoubleBuffer.wrap(this.absolute);
  private final DoubleBuffer inverseAbsoluteBuffer = DoubleBuffer.wrap(this.inverseAbsolute);

  private GlrScene glrScene;
}
