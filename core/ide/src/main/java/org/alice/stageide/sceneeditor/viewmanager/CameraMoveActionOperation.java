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
package org.alice.stageide.sceneeditor.viewmanager;

import java.text.MessageFormat;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.JComponent;

import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.alice.stageide.sceneeditor.viewmanager.edits.MoveAndOrientToEdit;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.UserField;
import org.lgna.story.CameraMarker;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.implementation.CameraImp;
import org.lgna.story.implementation.CameraMarkerImp;
import org.lgna.story.implementation.PerspectiveCameraMarkerImp;
import org.lgna.story.implementation.TransformableImp;

/**
 * @author dculyba
 *
 */
public abstract class CameraMoveActionOperation extends ActionOperation {

  private UserField markerField;
  private CameraMarkerImp cameraMarker;
  private CameraImp<SymmetricPerspectiveCamera> camera;

  private TransformableImp toMoveImp;
  private TransformableImp toMoveToImp;
  private String toMoveToName;
  private String toMoveName;

  private MoveToImageIcon imageIcon;

  protected CameraMoveActionOperation(UUID id) {
    super(Application.PROJECT_GROUP, id);
    this.markerField = null;
    this.cameraMarker = null;
    this.imageIcon = new MoveToImageIcon();
    this.setButtonIcon(imageIcon);
    this.updateBasedOnSettings();
  }

  protected abstract void updateMoveFields(UserField markerField, CameraMarkerImp cameraMarkerImp);

  protected void setToMoveToImp(TransformableImp toMoveTo, Icon icon, String toMoveToName) {
    this.toMoveToImp = toMoveTo;
    this.toMoveToName = toMoveToName;
    if (this.toMoveToImp != null) {
      this.imageIcon.setRightImage(icon);
    } else {
      this.imageIcon.setRightImage(null);
    }
  }

  protected void setToMoveImp(TransformableImp toMove, Icon icon, String toMoveName) {
    this.toMoveImp = toMove;
    this.toMoveName = toMoveName;
    if (this.toMoveImp != null) {
      this.imageIcon.setLeftImage(icon);
    } else {
      this.imageIcon.setLeftImage(null);
    }
  }

  public void updateDisplay() {
    updateMoveFields(markerField, cameraMarker);
    updateBasedOnSettings();
  }

  private void updateBasedOnSettings() {
    if ((this.toMoveImp != null) && (this.toMoveToImp != null)) {
      String unformattedTooltipText = this.findLocalizedText("tooltip");
      MessageFormat formatter = new MessageFormat("");
      formatter.setLocale(JComponent.getDefaultLocale());
      formatter.applyPattern(unformattedTooltipText);
      String tooltipText = formatter.format(new Object[] {this.toMoveName, this.toMoveToName});
      this.setToolTipText(tooltipText);
      this.setEnabled(true);
    } else {
      this.setToolTipText(this.findLocalizedText("disabledTooltip"));
      this.setEnabled(false);
    }
    this.setButtonIcon(this.imageIcon);

  }

  public void setCamera(CameraImp<SymmetricPerspectiveCamera> camera) {
    this.camera = camera;
  }

  protected CameraImp<SymmetricPerspectiveCamera> getCamera() {
    return this.camera;
  }

  public void setMarkerField(UserField markerField) {
    if ((markerField == null) || markerField.getValueType().isAssignableTo(CameraMarker.class)) {
      this.markerField = markerField;
    } else {
      this.markerField = null;
    }
    updateDisplay();
  }

  public void setCameraMarker(CameraMarkerImp cameraMarker) {
    if ((cameraMarker != null) && (cameraMarker instanceof PerspectiveCameraMarkerImp)) {
      this.cameraMarker = cameraMarker;
    } else {
      this.cameraMarker = null;
    }
    updateDisplay();
  }

  @Override
  protected void perform(UserActivity activity) {
    if ((this.toMoveImp != null) && (this.toMoveToImp != null) && (this.toMoveImp.getAbstraction() instanceof SMovableTurnable) && (this.toMoveToImp.getAbstraction() != null)) {

      MoveAndOrientToEdit edit = new MoveAndOrientToEdit(activity, (SMovableTurnable) this.toMoveImp.getAbstraction(), this.toMoveToImp.getAbstraction());
      activity.commitAndInvokeDo(edit);
    } else {
      activity.cancel();
    }
  }

}
