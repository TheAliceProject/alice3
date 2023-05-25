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
package org.alice.stageide.personresource.views;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import org.alice.interact.DragAdapter;
import org.alice.stageide.modelviewer.ModelViewer;
import org.alice.stageide.personresource.PersonImp;
import org.lgna.story.resources.sims2.LifeStage;

/**
 * @author Dennis Cosgrove
 */
public class PersonViewer extends ModelViewer {
  private final PersonImp personImp = new PersonImp();
  private final CreateAPersonDragAdapter dragAdapter = new CreateAPersonDragAdapter();

  public PersonViewer() {
    this.setMinimumPreferredWidth(300);
    this.setPerson(this.personImp);
  }

  private void positionAndOrientCamera(double height, int index, double duration) {
    assert Double.isNaN(height) == false;
    double xzFactor;
    if (index == 0) {
      xzFactor = 2.0 * height;
    } else {
      xzFactor = 1.0;
    }
    double pointAtFactor;
    double yFactor = 1.5;
    if (index == 0) {
      pointAtFactor = 0.5;
    } else {
      pointAtFactor = 0.9;
    }
    yFactor *= 0.65;
    xzFactor *= 0.65;
    if (this.getScene() != null) {
      AffineMatrix4x4 prevPOV = this.getCamera().getLocalTransformation();
      this.getCamera().setTransformation(this.getScene().createOffsetStandIn(-0.3 * xzFactor, height * yFactor, -height * xzFactor));
      this.getCamera().setOrientationOnlyToPointAt(this.getScene().createOffsetStandIn(0, height * pointAtFactor, 0));
      Animator animator = this.getAnimator();
      if ((duration > 0.0) && (animator != null)) {
        AffineMatrix4x4 nextPOV = this.getCamera().getLocalTransformation();
        this.getCamera().setLocalTransformation(prevPOV);

        PointOfViewAnimation povAnimation = new PointOfViewAnimation(this.getCamera().getSgComposite(), AsSeenBy.PARENT, null, nextPOV);
        povAnimation.setDuration(duration);

        animator.completeAll();
        animator.invokeLater(povAnimation, null);
      }
    }
  }

  private final double ADULT_HEIGHT = 1.7;
  private final double TEEN_HEIGHT = 1.6;
  private final double CHILD_HEIGHT = 1.2;
  private final double TODDLER_HEIGHT = .75;

  public void setCameraToFullView(LifeStage lifeStage) {
    //    this.positionAndOrientCamera( ADULT_HEIGHT, 0, 0.5 );
    double height;
    if ((lifeStage == LifeStage.ELDER) || (lifeStage == LifeStage.ADULT) || (lifeStage == LifeStage.TEEN) || (lifeStage == LifeStage.CHILD)) {
      height = ADULT_HEIGHT;
    } else if (lifeStage == LifeStage.TODDLER) {
      height = ADULT_HEIGHT;
    } else {
      height = ADULT_HEIGHT;
    }
    this.positionAndOrientCamera(height, 0, 0.5);
  }

  public void setCameraToCloseUp(LifeStage lifeStage) {
    double height;
    if ((lifeStage == LifeStage.ADULT) || (lifeStage == LifeStage.ELDER)) {
      height = ADULT_HEIGHT;
    } else if (lifeStage == LifeStage.TEEN) {
      height = TEEN_HEIGHT;
    } else if (lifeStage == LifeStage.CHILD) {
      height = CHILD_HEIGHT;
    } else {
      height = TODDLER_HEIGHT;
    }
    this.positionAndOrientCamera(height, 1, 0.5);
  }

  public PersonImp getPerson() {
    return (PersonImp) this.getModel();
  }

  public void setPerson(PersonImp person) {
    this.setModel(person);
    this.dragAdapter.setSelectedImplementation(person);
    this.positionAndOrientCamera(ADULT_HEIGHT, 0, 0.0);
  }

  @Override
  protected void initialize() {
    super.initialize();
    this.dragAdapter.setOnscreenRenderTarget(this.getOnscreenRenderTarget());
    this.dragAdapter.addCameraView(DragAdapter.CameraView.MAIN, this.getCamera().getSgCamera(), null);
    this.dragAdapter.makeCameraActive(this.getCamera().getSgCamera());
  }
}
