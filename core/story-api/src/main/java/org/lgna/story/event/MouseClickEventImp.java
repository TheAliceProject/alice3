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
package org.lgna.story.event;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.PickResult;
import org.alice.interact.PickUtilities;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SModel;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.implementation.SceneImp;

import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

/**
 * @author Dennis Cosgrove
 */
public class MouseClickEventImp {
  private final MouseEvent e;
  private final SScene scene;

  private Rectangle viewport;
  private boolean isPickPerformed = false;
  private SModel modelAtMouseLocation;

  public MouseClickEventImp(MouseEvent e, SScene scene) {
    this.e = e;
    this.scene = scene;
  }

  private OnscreenRenderTarget<?> getOnscreenRenderTarget() {
    if (this.scene != null) {
      SceneImp sceneImp = EmployeesOnly.getImplementation(this.scene);
      ProgramImp programImp = sceneImp.getProgram();
      if (programImp != null) {
        return programImp.getOnscreenRenderTarget();
      }
    }
    return null;
  }

  private Rectangle getActualViewport() {
    if (this.viewport == null) {
      OnscreenRenderTarget<?> rt = this.getOnscreenRenderTarget();
      //todo: search through cameras for the one that contains mouse point, or default to [0] if outside
      AbstractCamera sgCamera = rt.getSgCameraAt(0);
      this.viewport = rt.getActualViewportAsAwtRectangle(sgCamera);
    }
    return this.viewport;
  }

  protected synchronized void pickIfNecessary() {
    if (this.isPickPerformed) {
      //pass
    } else {
      if (this.scene != null) {
        OnscreenRenderTarget<?> rt = this.getOnscreenRenderTarget();
        if (rt != null) {
          PickResult pickResult = rt.getSynchronousPicker().pickFrontMost(e.getX(), e.getY(), PickSubElementPolicy.NOT_REQUIRED);
          if (pickResult != null) {
            SThing e = PickUtilities.getEntityFromPickedObject(pickResult.getVisual());
            if (e instanceof SModel) {
              this.modelAtMouseLocation = (SModel) e;
            }
          }
        }
      }
      this.isPickPerformed = true;
    }
  }

  //  public org.lookingglassandalice.storytelling.Model getPartAtMouseLocation() {
  //    this.pickIfNecessary();
  //    return this.partAtMouseLocation;
  //  }
  public SModel getModelAtMouseLocation() {
    this.pickIfNecessary();
    return this.modelAtMouseLocation;
  }

  public Double getScreenDistanceFromLeft() {
    Rectangle viewport = this.getActualViewport();
    return (this.e.getX() - viewport.x) / (double) this.viewport.width;
  }

  public Double getScreenDistanceFromBottom() {
    Rectangle viewport = this.getActualViewport();
    return 1.0 - ((this.e.getY() - viewport.y) / (double) this.viewport.height);
  }

  @Deprecated
  @MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
  public Double[] getRelativeXYPosition() {
    Double[] rv = {this.getScreenDistanceFromLeft(), this.getScreenDistanceFromBottom()};
    return rv;
  }

  public SScene getScene() {
    return this.scene;
  }

  public MouseEvent getEvent() {
    return this.e;
  }
}
