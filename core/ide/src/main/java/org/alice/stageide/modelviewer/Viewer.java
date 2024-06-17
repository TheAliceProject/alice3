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

package org.alice.stageide.modelviewer;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.ClockBasedAnimator;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;
import edu.cmu.cs.dennisc.render.RenderFactory;
import edu.cmu.cs.dennisc.render.RenderUtils;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.implementation.SunImp;
import org.lgna.story.implementation.SymmetricPerspectiveCameraImp;

import java.awt.BorderLayout;

/**
 * @author Dennis Cosgrove
 */
abstract class Viewer extends BorderPanel {
  private OnscreenRenderTarget onscreenRenderTarget = RenderUtils.getDefaultRenderFactory().createOnscreenRenderTarget(new RenderCapabilities.Builder().build());
  private Animator animator = new ClockBasedAnimator();
  private SceneImp scene = new SceneImp(null);
  private SymmetricPerspectiveCameraImp camera = new SymmetricPerspectiveCameraImp(null);
  private SunImp sunLight = new SunImp(null);

  private AutomaticDisplayListener automaticDisplayListener = new AutomaticDisplayListener() {
    @Override
    public void automaticDisplayCompleted(AutomaticDisplayEvent e) {
      animator.update();
    }
  };

  public Viewer() {
    this.camera.setVehicle(this.scene);
    this.sunLight.setVehicle(this.scene);
    this.sunLight.applyRotationInRevolutions(Vector3.accessNegativeXAxis(), 0.25);
    this.getAwtComponent().add(this.onscreenRenderTarget.getAwtComponent(), BorderLayout.CENTER);
  }

  private boolean isInitialized = false;

  protected void initialize() {
    this.onscreenRenderTarget.addSgCamera(this.camera.getSgCamera());
  }

  protected OnscreenRenderTarget getOnscreenRenderTarget() {
    return this.onscreenRenderTarget;
  }

  protected SceneImp getScene() {
    return this.scene;
  }

  protected SymmetricPerspectiveCameraImp getCamera() {
    return this.camera;
  }

  protected SunImp getSunLight() {
    return this.sunLight;
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    if (this.isInitialized) {
      //pass
    } else {
      this.initialize();
      this.isInitialized = true;
    }
    RenderFactory renderFactory = RenderUtils.getDefaultRenderFactory();
    renderFactory.incrementAutomaticDisplayCount();
    renderFactory.addAutomaticDisplayListener(this.automaticDisplayListener);
  }

  @Override
  protected void handleUndisplayable() {
    RenderFactory renderFactory = RenderUtils.getDefaultRenderFactory();
    renderFactory.removeAutomaticDisplayListener(this.automaticDisplayListener);
    renderFactory.decrementAutomaticDisplayCount();
    super.handleUndisplayable();
  }

  protected Animator getAnimator() {
    return this.animator;
  }
}
