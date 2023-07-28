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
package org.alice.interact;

import com.jogamp.opengl.GLException;
import edu.cmu.cs.dennisc.clock.Clock;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInRadians;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.PickFrontMostObserver;
import edu.cmu.cs.dennisc.render.PickResult;
import edu.cmu.cs.dennisc.render.PickSubElementPolicy;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.Silhouette;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventManager;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;
import org.alice.interact.handle.HandleManager;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.HandleStyle;
import org.alice.interact.handle.ManipulationHandle;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.AnimatorDependentManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;
import org.alice.interact.manipulator.OnscreenPicturePlaneInformedManipulator;

import edu.cmu.cs.dennisc.animation.Animator;
import org.lgna.story.implementation.*;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public abstract class DragAdapter {
  public static final Element.Key<AxisAlignedBox> BOUNDING_BOX_KEY = Element.Key.createInstance("BOUNDING_BOX_KEY");
  private static final double MOUSE_WHEEL_TIMEOUT_TIME = 1.0;
  private static final double CANCEL_MOUSE_WHEEL_DISTANCE = 3;
  protected final Map<HandleStyle, InteractionGroup> mapHandleStyleToInteractionGroup = Maps.newHashMap();
  private final HandleManager handleManager = new HandleManager();
  private final List<SelectionListener> selectionListeners = Lists.newCopyOnWriteArrayList();
  private final AbsoluteTransformationListener cameraTransformationListener = absoluteTransformationEvent -> {
    if (absoluteTransformationEvent.getSource() instanceof SymmetricPerspectiveCamera) {
      SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera) absoluteTransformationEvent.getSource();
      if (getActiveCamera() == camera) {
        DragAdapter.this.handleManager.updateCameraPosition(camera.getAbsoluteTransformation().translation);
      }
    }
  };
  private final Map<CameraView, CameraSet> cameraMap = Maps.newHashMap();
  private AbstractTransformableImp toBeSelected = null;
  private boolean hasObjectToBeSelected = false;
  private double timePrev = Double.NaN;
  private boolean hasSetCameraTransformables = false;
  private InteractionGroup currentInteractionState = null;
  private AbstractTransformableImp selectedObject = null;
  private Silhouette sgSilhouette;
  private CameraMarkerImp selectedCameraMarker = null;
  private ObjectMarkerImp selectedObjectMarker = null;

  private boolean isComponentListener(Component component) {
    for (MouseListener listener : component.getMouseListeners()) {
      if (listener == mouseListener) {
        return true;
      }
    }
    for (MouseMotionListener listener : component.getMouseMotionListeners()) {
      if (listener == mouseMotionListener) {
        return true;
      }
    }
    for (KeyListener listener : component.getKeyListeners()) {
      if (listener == keyListener) {
        return true;
      }
    }
    for (MouseWheelListener listener : component.getMouseWheelListeners()) {
      if (listener == mouseWheelListener) {
        return true;
      }
    }
    return false;
  }

  public void addListeners(Component component) {
    if (!this.isComponentListener(component)) {
      component.addMouseListener(this.mouseListener);
      component.addMouseMotionListener(this.mouseMotionListener);
      component.addKeyListener(this.keyListener);
      component.addMouseWheelListener(this.mouseWheelListener);
    }
  }

  public void removeListeners(Component component) {
    if (this.isComponentListener(component)) {
      component.removeMouseListener(this.mouseListener);
      component.removeMouseMotionListener(this.mouseMotionListener);
      component.removeKeyListener(this.keyListener);
      component.removeMouseWheelListener(this.mouseWheelListener);
    }
  }

  public void addManipulationListener(ManipulationListener listener) {
    this.manipulationEventManager.addManipulationListener(listener);
  }

  public void removeManipulationListener(ManipulationListener listener) {
    this.manipulationEventManager.removeManipulationListener(listener);
  }

  public void triggerManipulationEvent(ManipulationEvent event, boolean isActivate) {
    event.setInputState(this.currentInputState);
    this.manipulationEventManager.triggerEvent(event, isActivate);
  }

  public void addManipulatorConditionSet(ManipulatorConditionSet manipulator) {
    this.manipulators.add(manipulator);
  }

  protected Iterable<ManipulatorConditionSet> getManipulatorConditionSets() {
    return this.manipulators;
  }

  private Component getAWTComponentToAddListenersTo(OnscreenRenderTarget<?> onscreenRenderTarget) {
    if (onscreenRenderTarget != null) {
      return onscreenRenderTarget.getAwtComponent();
    } else {
      return null;
    }
  }

  public OnscreenRenderTarget<?> getOnscreenRenderTarget() {
    return this.onscreenRenderTarget;
  }

  public void setOnscreenRenderTarget(OnscreenRenderTarget<?> onscreenRenderTarget) {
    if (this.onscreenRenderTarget != null) {
      this.onscreenRenderTarget.getRenderFactory().removeAutomaticDisplayListener(this.automaticDisplayAdapter);
    }
    this.onscreenRenderTarget = onscreenRenderTarget;
    setAWTComponent(getAWTComponentToAddListenersTo(this.onscreenRenderTarget));
    if (this.onscreenRenderTarget != null) {
      this.onscreenRenderTarget.getRenderFactory().addAutomaticDisplayListener(this.automaticDisplayAdapter);
    }
  }

  protected Component getAWTComponent() {
    return this.lookingGlassComponent;
  }

  private void setAWTComponent(Component awtComponent) {
    if (this.lookingGlassComponent != null) {
      removeListeners(this.lookingGlassComponent);
    }
    this.lookingGlassComponent = awtComponent;
    if (this.lookingGlassComponent != null) {
      addListeners(awtComponent);
    }
  }

  public Animator getAnimator() {
    return this.animator;
  }

  public void setAnimator(Animator animator) {
    this.animator = animator;
    for (ManipulatorConditionSet manipulatorSet : this.manipulators) {
      if (manipulatorSet.getManipulator() instanceof AnimatorDependentManipulator) {
        ((AnimatorDependentManipulator) manipulatorSet.getManipulator()).setAnimator(this.animator);
      }
    }
  }

  public void setLookingGlassOnManipulator(OnscreenPicturePlaneInformedManipulator manipulator) {
    manipulator.setOnscreenRenderTarget(this.onscreenRenderTarget);
  }

  private void setCurrentInteractionState(InteractionGroup interactionState) {
    this.currentInteractionState = interactionState;
    if (this.currentInteractionState != null) {
      InteractionGroup.InteractionInfo interactionInfo = this.currentInteractionState.getMatchingInfo(ObjectType.getObjectType(this.selectedObject));
      if (interactionInfo != null) {
        this.handleManager.setHandleSet(interactionInfo.getHandleSet());
      }
      this.currentInteractionState.enabledManipulators(true);
    }
  }

  public void setInteractionState(HandleStyle handleStyle) {
    if (this.currentInteractionState != null) {
      this.currentInteractionState.enabledManipulators(false);
    }
    setCurrentInteractionState(this.mapHandleStyleToInteractionGroup.get(handleStyle));
  }

  public void makeCameraActive(AbstractCamera camera) {
    for (Map.Entry<CameraView, CameraSet> cameras : this.cameraMap.entrySet()) {
      if (cameras.getValue().hasCamera(camera)) {
        cameras.getValue().setActiveCamera(camera);
      }
    }
    if (camera instanceof SymmetricPerspectiveCamera) {
      this.handleManager.updateCameraPosition(camera.getAbsoluteTransformation().translation);
    } else {
      this.handleManager.updateCameraPosition(null);
    }
  }

  public AbstractCamera getActiveCamera() {
    //TODO: introduce a true sense of "active"
    CameraSet activeCameraSet = this.cameraMap.get(CameraView.MAIN);
    if ((activeCameraSet != null) && (activeCameraSet.getActiveCamera() != null)) {
      return activeCameraSet.getActiveCamera();
    } else {
      return null;
    }
  }

  private AbstractCamera getCameraForManipulator(CameraInformedManipulator cameraManipulator) {
    CameraView cameraView = cameraManipulator.getDesiredCameraView();
    AbstractCamera cameraToReturn;
    if ((cameraView == CameraView.ACTIVE_VIEW) || (cameraView == CameraView.PICK_CAMERA)) {
      cameraToReturn = getActiveCamera();
    } else {
      CameraSet cameras = this.cameraMap.get(cameraView);
      if (cameras != null) {
        cameraToReturn = cameras.getActiveCamera();
      } else {
        cameraToReturn = null;
      }
    }
    return cameraToReturn;
  }

  public void setCameraOnManipulator(CameraInformedManipulator manipulator) {
    //The pick camera can be null if we roll over a 2D handle while we're moving
    if ((manipulator.getDesiredCameraView() == CameraView.PICK_CAMERA) && (this.currentInputState.getPickCamera() != null)) {
      manipulator.setCamera(this.currentInputState.getPickCamera());
    } else {
      manipulator.setCamera(this.getCameraForManipulator(manipulator));
    }
  }

  private void setManipulatorStartState(AbstractManipulator manipulator) {
    if (manipulator instanceof OnscreenPicturePlaneInformedManipulator) {
      OnscreenPicturePlaneInformedManipulator lookingGlassManipulator = (OnscreenPicturePlaneInformedManipulator) manipulator;
      this.setLookingGlassOnManipulator(lookingGlassManipulator);
    }
    if (manipulator instanceof CameraInformedManipulator) {
      CameraInformedManipulator cameraInformed = (CameraInformedManipulator) manipulator;
      this.setCameraOnManipulator(cameraInformed);
    }
  }

  public void addSelectionListener(SelectionListener selectionListener) {
    this.selectionListeners.add(selectionListener);
  }

  private void fireSelecting(SelectionEvent e) {
    for (SelectionListener selectionListener : this.selectionListeners) {
      selectionListener.selecting(e);
    }
  }

  private void fireSelected(SelectionEvent e) {
    for (SelectionListener selectionListener : this.selectionListeners) {
      selectionListener.selected(e);
    }
  }

  public void pushHandleSet(HandleSet handleSet) {
    this.handleManager.pushNewHandleSet(handleSet);
  }

  public void popHandleSet() {
    this.handleManager.popHandleSet();
  }

  private void setToBeSelected(AbstractTransformableImp toBeSelected) {
    this.toBeSelected = toBeSelected;
    this.hasObjectToBeSelected = true;
  }

  protected abstract void updateHandleSelection(AbstractTransformableImp selected);

  public boolean hasSceneEditor() {
    return false;
  }

  public void clear() {
    this.clearCameraViews();
    this.handleManager.clear();
  }

  public void clearCameraViews() {
    for (CameraSet cameraSet : this.cameraMap.values()) {
      if (cameraSet.mainCamera != null) {
        cameraSet.mainCamera.removeAbsoluteTransformationListener(this.cameraTransformationListener);
      }
    }
    this.cameraMap.clear();
  }

  public void addCameraView(CameraView viewType, SymmetricPerspectiveCamera mainCamera) {
    addCameraView(viewType, mainCamera, null, null);
  }

  public void addCameraView(CameraView viewType, SymmetricPerspectiveCamera mainCamera, SymmetricPerspectiveCamera  layoutCamera, OrthographicCamera orthographicCamera) {
    addCameraView(viewType, new CameraSet(mainCamera, layoutCamera, orthographicCamera));
  }

  private void addCameraView(CameraView viewType, CameraSet cameras) {
    if (cameras.mainCamera != null) {
      cameras.mainCamera.addAbsoluteTransformationListener(this.cameraTransformationListener);
      this.handleManager.updateCameraPosition(cameras.mainCamera.getAbsoluteTransformation().translation);
    }
    this.cameraMap.put(viewType, cameras);
  }

  public void setSelectedCameraMarker(CameraMarkerImp selected) {
    if (selected != this.selectedCameraMarker) {
      this.fireSelecting(new SelectionEvent(this, selected));
      if (this.selectedCameraMarker != null) {
        this.selectedCameraMarker.opacity.setValue(.3f);
        if (this.selectedCameraMarker instanceof PerspectiveCameraMarkerImp) {
          ((PerspectiveCameraMarkerImp) this.selectedCameraMarker).setDetailedViewShowing(false);
        }
      }
      this.selectedCameraMarker = selected;
      if (this.selectedCameraMarker != null) {
        this.selectedCameraMarker.opacity.setValue(1f);
        if (this.hasSceneEditor() && (this.selectedCameraMarker instanceof PerspectiveCameraMarkerImp)) {
          //TODO: Resolve the issue of showing the selection details of an active camera mark (active meaning it's currently attached to the camera)
          //          boolean isNewSelectedActiveCameraMarker = this.sceneEditor.isCameraMarkerActive(this.selectedCameraMarker);
          //          if (!isNewSelectedActiveCameraMarker) {
          ((PerspectiveCameraMarkerImp) this.selectedCameraMarker).setDetailedViewShowing(true);
          //}
        }
      }
    }
  }

  public void setSelectedObjectMarker(ObjectMarkerImp selected) {
    if (selected != this.selectedObjectMarker) {
      this.fireSelecting(new SelectionEvent(this, selected));
      if (this.selectedObjectMarker != null) {
        this.selectedObjectMarker.opacity.setValue(.3f);
      }
      this.selectedObjectMarker = selected;
      if (this.selectedObjectMarker != null) {
        this.selectedObjectMarker.opacity.setValue(1f);
      }
    }
  }

  protected void setHandleSelectionState(HandleStyle handleStyle) {
    //Default behavior is to set the interaction state directly
    //CroquetSupportingDragAdapter sets the croquet selection state object to make this happen
    this.setInteractionState(handleStyle);
  }

  public void setSelectedImplementation(AbstractTransformableImp selected) {
    if (this.isInStateChange()) {
      this.setToBeSelected(selected);
      return;
    }
    if (selected != null) {
      if (selected.getSgComposite() instanceof Joint) {
        //If we're selecting a joint for the first time or from a selection that wasn't a joint, set the handle state to rotation
        if ((this.selectedObject == null) || !(this.selectedObject.getSgComposite() instanceof Joint)) {
          if (this.getDefaultJointHandleStyle() != null) {
            this.setHandleSelectionState(this.getDefaultJointHandleStyle());
          }
        }
      }
      if (selected instanceof ObjectMarkerImp) {
        setSelectedObjectMarker((ObjectMarkerImp) selected);
      } else if (selected instanceof CameraMarkerImp) {
        setSelectedCameraMarker((CameraMarkerImp) selected);
      } else {
        setSelectedSceneObjectImplementation(selected);
      }

      //If the current handle state is null, force it to update by setting the interaction state again
      //The handle state can be null if the previous selected object didn't match the selected state
      if (this.handleManager.getCurrentHandleSet() == null) {
        this.setCurrentInteractionState(this.currentInteractionState);
      }
      updateHandleSelection(selected);
    } else {
      setSelectedSceneObjectImplementation(null);
    }
  }

  private void handleStateChange() {
    List<AbstractManipulator> toStart = Lists.newLinkedList();
    List<AbstractManipulator> toEnd = Lists.newLinkedList();
    List<AbstractManipulator> toUpdate = Lists.newLinkedList();
    List<AbstractManipulator> toClick = Lists.newLinkedList();
    for (ManipulatorConditionSet currentManipulatorSet : this.manipulators) {
      //      System.out.println(currentManipulatorSet.getManipulator()+": "+currentManipulatorSet.getCondition(0));
      currentManipulatorSet.update(this.currentInputState, this.previousInputState);
      if (currentManipulatorSet.isEnabled()) {
        if (currentManipulatorSet.stateChanged(this.currentInputState, this.previousInputState)) {
          if (currentManipulatorSet.shouldContinue(this.currentInputState, this.previousInputState)) {
            toUpdate.add(currentManipulatorSet.getManipulator());
          } else if (currentManipulatorSet.justStarted(this.currentInputState, this.previousInputState)) {
            //            System.out.println("Just starting "+currentManipulatorSet.getManipulator());
            toStart.add(currentManipulatorSet.getManipulator());
          } else if (currentManipulatorSet.justEnded(this.currentInputState, this.previousInputState)) {
            toEnd.add(currentManipulatorSet.getManipulator());
          } else if (currentManipulatorSet.clicked(this.currentInputState, this.previousInputState)) {
            toClick.add(currentManipulatorSet.getManipulator());
          }
        }
      } else { //Manipulator is not enabled
        if (currentManipulatorSet.getManipulator().hasStarted()) {
          toEnd.add(currentManipulatorSet.getManipulator());
        }
      }
    }

    //End manipulators first
    for (AbstractManipulator toEndManipulator : toEnd) {
      toEndManipulator.endManipulator(this.currentInputState, this.previousInputState);
    }
    for (AbstractManipulator toClickManipulator : toClick) {
      this.setManipulatorStartState(toClickManipulator);
      toClickManipulator.clickManipulator(this.currentInputState, this.previousInputState);
    }
    for (AbstractManipulator toStartManipulator : toStart) {
      this.setManipulatorStartState(toStartManipulator);
      toStartManipulator.startManipulator(this.currentInputState);
    }
    for (AbstractManipulator toUpdateManipulator : toUpdate) {
      //If the manipulator we're updating was just started, don't update it with previous data (it's out of scope for the manipulator)
      if (toStart.contains(toUpdateManipulator)) {
        toUpdateManipulator.dataUpdateManipulator(this.currentInputState, this.currentInputState);
      } else {
        toUpdateManipulator.dataUpdateManipulator(this.currentInputState, this.previousInputState);
      }
    }
    updateRollover();
    this.previousInputState.copyState(this.currentInputState);
  }

  private void updateRollover() {
    if (this.currentInputState.getRolloverHandle() != this.previousInputState.getRolloverHandle()) {
      if (this.currentInputState.getRolloverHandle() != null) {
        this.handleManager.setHandleRollover(this.currentInputState.getRolloverHandle(), true);
      }
      if (this.previousInputState.getRolloverHandle() != null) {
        this.handleManager.setHandleRollover(this.previousInputState.getRolloverHandle(), false);
      }
    }

    if (!this.hasObjectToBeSelected && (this.currentInputState.getCurrentlySelectedObject() != this.previousInputState.getCurrentlySelectedObject())) {
      this.triggerImplementationSelection(EntityImp.getInstance(this.currentInputState.getCurrentlySelectedObject(), AbstractTransformableImp.class));
    }
  }

  protected void fireStateChange() {
    this.isInStageChange = true;
    try {
      this.handleStateChange();
    } finally {
      this.isInStageChange = false;
    }
    if (this.hasObjectToBeSelected) {
      this.hasObjectToBeSelected = false;
      this.setSelectedImplementation(this.toBeSelected);
    }
  }

  private boolean isMouseWheelActive() {
    return (this.mouseWheelTimeoutTime > 0);
  }

  private void stopMouseWheel() {
    this.mouseWheelTimeoutTime = 0;
    this.currentInputState.setMouseWheelState(0);
    this.mouseWheelStartLocation = null;
  }

  private boolean shouldStopMouseWheel(Point currentMouse) {
    if (this.mouseWheelStartLocation != null) {
      double distance = currentMouse.distance(this.mouseWheelStartLocation);
      return distance > CANCEL_MOUSE_WHEEL_DISTANCE;
    }
    return false;
  }

  private boolean isInStateChange() {
    return this.isInStageChange;
  }

  private ManipulationHandle getHandleForComponent(Component c) {
    if (c == null) {
      return null;
    }
    if (c instanceof ManipulationHandle) {
      return (ManipulationHandle) c;
    } else {
      return getHandleForComponent(c.getParent());
    }
  }

  private void setSelectedObjectSilhouetteIfAppropriate(boolean isHaloed) {
    if (this.sgSilhouette != null) {
      if (this.selectedObject instanceof ModelImp) {
        ModelImp modelImp = (ModelImp) this.selectedObject;
        for (Visual sgVisual : modelImp.getSgVisuals()) {
          sgVisual.silouette.setValue(isHaloed ? this.sgSilhouette : null);
        }
      }
    }
  }

  private void setSelectedSceneObjectImplementation(AbstractTransformableImp selected) {
    if (this.selectedObject != selected) {
      this.fireSelecting(new SelectionEvent(this, selected));

      this.setSelectedObjectSilhouetteIfAppropriate(false);

      AbstractTransformable sgTransformable = selected != null ? selected.getSgComposite() : null;
      if (HandleManager.isSelectable(sgTransformable)) {
        this.handleManager.setHandlesShowing(true);
        this.handleManager.setSelectedObject(sgTransformable);
      } else {
        this.handleManager.setSelectedObject(null);
      }
      this.currentInputState.setCurrentlySelectedObject(sgTransformable);
      this.currentInputState.setTimeCaptured();
      selectedObject = selected;

      this.setSelectedObjectSilhouetteIfAppropriate(true);

      this.fireStateChange();
    }
  }

  public void setHandleShowingForSelectedImplementation(AbstractTransformableImp object, boolean handlesShowing) {
    if (this.selectedObject == object) {
      this.handleManager.setHandlesShowing(handlesShowing);
    }
  }

  public void setHandleVisibility(boolean isVisible) {
    this.handleManager.setHandlesShowing(isVisible);
  }

  public void triggerImplementationSelection(AbstractTransformableImp selected) {
    if (this.selectedObject != selected) {
      this.fireSelected(new SelectionEvent(this, selected));
    }
  }

  public void triggerSgObjectSelection(AbstractTransformable selected) {
    triggerImplementationSelection(EntityImp.getInstance(selected, AbstractTransformableImp.class));
  }

  protected void setSgSilhouette(Silhouette sgSilhouette) {
    this.sgSilhouette = sgSilhouette;
  }

  private AbstractCamera getSGCamera() {
    OnscreenRenderTarget<?> onscreenRenderTarget = this.getOnscreenRenderTarget();
    if (onscreenRenderTarget != null && 0 < onscreenRenderTarget.getSgCameraCount()) {
      return onscreenRenderTarget.getSgCameraAt(0);
    }
    return null;
  }

  public void addHandle(ManipulationHandle handle) {
    this.handleManager.addHandle(handle);
  }

  private HandleStyle getDefaultJointHandleStyle() {
    return HandleStyle.ROTATION;
  }

  public boolean shouldSnapToGround() {
    return false;
  }

  public boolean shouldSnapToGrid() {
    return false;
  }

  public boolean shouldSnapToRotation() {
    return false;
  }

  public double getGridSpacing() {
    return 1.0;
  }

  public Angle getRotationSnapAngle() {
    return new AngleInRadians(Math.PI / 16.0);
  }

  public abstract void undoRedoEndManipulation(AbstractManipulator manipulator, AffineMatrix4x4 originalTransformation);

  private void pickIntoSceneSuppressingErrors(Point mouseLocation, PickFrontMostObserver observer) {
    try {
      pickIntoScene(mouseLocation, observer);
    } catch (GLException gle) {
      Logger.errln("Error picking into scene", gle);
    }
  }

  private void pickIntoScene(Point mouseLocation, PickFrontMostObserver observer) {
    OnscreenRenderTarget<?> onscreenRenderTarget = this.getOnscreenRenderTarget();
    assert onscreenRenderTarget != null;
    // Once IS_ASYNCHRONOUS_PICK_READY_FOR_PRIME_TIME we could switch to
    // getOnscreenRenderTarget().getAsynchronousPicker().pickFrontMost( mouseLocation.x, mouseLocation.y, PickSubElementPolicy.NOT_REQUIRED, null, observer );
    PickResult pickResult = onscreenRenderTarget.getSynchronousPicker().pickFrontMost(mouseLocation.x, mouseLocation.y, PickSubElementPolicy.NOT_REQUIRED);
    observer.done(pickResult);
  }

  public void clearMouseAndKeyboardState() {
    this.currentInputState.clearKeyState();
    this.currentInputState.clearMouseState();
    this.currentInputState.clearMouseWheelState();

    this.fireStateChange();
  }

  protected void handleMouseEntered(MouseEvent e) {
    this.currentRolloverComponent = e.getComponent();
    if (!this.currentInputState.isAnyMouseButtonDown()) {
      this.currentInputState.setMouseLocation(e.getPoint());
      if (e.getComponent() == this.lookingGlassComponent) {
        this.pickIntoSceneSuppressingErrors(e.getPoint(), currentInputState::setRolloverPickResult);
      } else {
        this.currentInputState.setRolloverHandle(this.getHandleForComponent(e.getComponent()));
      }
      this.currentInputState.setTimeCaptured();
      this.currentInputState.setInputEvent(e);
      this.fireStateChange();
    }
  }

  private void handleMouseExited(MouseEvent e) {
    this.currentRolloverComponent = null;
    if (!this.currentInputState.isAnyMouseButtonDown()) {
      this.currentInputState.setMouseLocation(e.getPoint());
      this.currentInputState.setRolloverHandle(null);
      this.currentInputState.setRolloverPickResult(null);
      this.currentInputState.setTimeCaptured();
      this.currentInputState.setInputEvent(e);
      this.fireStateChange();
    }

  }

  private void handleMousePressed(MouseEvent e) {
    this.currentInputState.setMouseState(e.getButton(), true);
    this.currentInputState.setMouseLocation(e.getPoint());
    this.currentInputState.setInputEventType(InputState.InputEventType.MOUSE_DOWN);
    this.currentInputState.setInputEvent(e);
    e.getComponent().requestFocus();

    if (e.getComponent() == this.lookingGlassComponent) {
      this.pickIntoScene(e.getPoint(), currentInputState::setClickPickResult);
    } else {
      this.currentInputState.setClickHandle(this.getHandleForComponent(e.getComponent()));
    }
    this.currentInputState.setTimeCaptured();
    this.stopMouseWheel();
    this.fireStateChange();
  }

  private void handleMouseReleased(MouseEvent e) {
    this.currentInputState.setMouseState(e.getButton(), false);
    this.currentInputState.setMouseLocation(e.getPoint());
    this.currentInputState.setInputEventType(InputState.InputEventType.MOUSE_UP);
    this.currentInputState.setInputEvent(e);
    if (this.currentRolloverComponent == this.lookingGlassComponent) {
      this.pickIntoScene(e.getPoint(), currentInputState::setRolloverPickResult);
    } else {
      this.currentInputState.setRolloverHandle(this.getHandleForComponent(this.currentRolloverComponent));
    }
    this.currentInputState.setTimeCaptured();
    this.fireStateChange();
  }

  private void handleMouseDragged(MouseEvent e) {
    try {
      this.currentInputState.setMouseLocation(e.getPoint());
      this.currentInputState.setInputEventType(InputState.InputEventType.MOUSE_DRAGGED);
      this.currentInputState.setTimeCaptured();
      this.currentInputState.setInputEvent(e);
      this.fireStateChange();
    } catch (RuntimeException re) {
      re.printStackTrace();
    }
  }

  protected void handleMouseMoved(MouseEvent e) {
    if (!this.currentInputState.getIsDragEvent()) { //If we haven't already handled it through dragAndDrop
      //java.awt.Component c = e.getComponent();
      this.currentInputState.setMouseLocation(e.getPoint());
      if (e.getComponent() == this.lookingGlassComponent) {
        //Don't pick into the scene if a mouse button is already down
        if (!this.currentInputState.isAnyMouseButtonDown()) {
          this.pickIntoSceneSuppressingErrors(e.getPoint(), currentInputState::setRolloverPickResult);
        }
      } else {
        this.currentInputState.setRolloverHandle(this.getHandleForComponent(e.getComponent()));
      }
      this.currentInputState.setTimeCaptured();
      this.currentInputState.setInputEvent(e);
      if (shouldStopMouseWheel(e.getPoint())) {
        this.stopMouseWheel();
      }
      this.fireStateChange();
    }
  }

  private void handleMouseWheelMoved(MouseWheelEvent e) {
    this.currentInputState.setMouseWheelState(e.getWheelRotation());
    this.currentInputState.setInputEventType(InputState.InputEventType.MOUSE_WHEEL);
    this.currentInputState.setTimeCaptured();
    this.currentInputState.setInputEvent(e);
    if (this.mouseWheelStartLocation == null) {
      this.mouseWheelStartLocation = new Point(e.getPoint());
    }
    this.mouseWheelTimeoutTime = MOUSE_WHEEL_TIMEOUT_TIME;
    this.fireStateChange();
  }

  private void handleKeyPressed(KeyEvent e) {
    this.currentInputState.setKeyState(e.getKeyCode(), true);
    this.currentInputState.setInputEventType(InputState.InputEventType.KEY_DOWN);
    this.currentInputState.setTimeCaptured();
    this.currentInputState.setInputEvent(e);
    this.fireStateChange();

  }

  private void handleKeyReleased(KeyEvent e) {
    this.currentInputState.setKeyState(e.getKeyCode(), false);
    this.currentInputState.setInputEventType(InputState.InputEventType.KEY_UP);
    this.currentInputState.setTimeCaptured();
    this.currentInputState.setInputEvent(e);
    this.fireStateChange();

  }

  public void setSGCamera(AbstractCamera camera) {
  }

  protected void update(double timeDelta) {
    if (isMouseWheelActive()) {
      mouseWheelTimeoutTime -= timeDelta;
      if (!isMouseWheelActive()) {
        stopMouseWheel();
        fireStateChange();
      }
    }
    for (ManipulatorConditionSet currentManipulatorSet : this.manipulators) {
      if (currentManipulatorSet.getManipulator().hasStarted() && currentManipulatorSet.shouldContinue(this.currentInputState, this.previousInputState)) {
        currentManipulatorSet.getManipulator().timeUpdateManipulator(timeDelta, this.currentInputState);
      }
    }
  }

  private void handleAutomaticDisplayCompleted(AutomaticDisplayEvent e) {
    AbstractCamera sgCamera = getSGCamera();
    if (sgCamera != null) {
      if (!hasSetCameraTransformables) {
        setSGCamera(sgCamera);
        hasSetCameraTransformables = true;
      }
      double timeCurr = Clock.getCurrentTime();
      if (Double.isNaN(this.timePrev)) {
        this.timePrev = Clock.getCurrentTime();
      }
      double timeDelta = timeCurr - this.timePrev;
      update(timeDelta);
      this.timePrev = timeCurr;
    }
  }

  private final AutomaticDisplayListener automaticDisplayAdapter = this::handleAutomaticDisplayCompleted;

  private final MouseWheelListener mouseWheelListener = this::handleMouseWheelMoved;
  private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
    @Override
    public void mouseMoved(MouseEvent e) {
      handleMouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      handleMouseDragged(e);
    }
  };
  private final MouseListener mouseListener = new MouseListener() {
    @Override
    public void mouseEntered(MouseEvent e) {
      handleMouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
      handleMouseExited(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
      handleMousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      handleMouseReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
  };
  private final KeyListener keyListener = new KeyListener() {
    @Override
    public void keyPressed(KeyEvent e) {
      handleKeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
      handleKeyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
  };

  protected/*private*/ final List<ManipulatorConditionSet> manipulators = Lists.newCopyOnWriteArrayList();
  private final ManipulationEventManager manipulationEventManager = new ManipulationEventManager();
  private OnscreenRenderTarget<?> onscreenRenderTarget;
  private Component lookingGlassComponent = null;
  private Component currentRolloverComponent = null;
  private Animator animator;
  protected final/*private*/ InputState currentInputState = new InputState();
  private final InputState previousInputState = new InputState();
  private boolean isInStageChange = false;
  private double mouseWheelTimeoutTime = 0;
  private Point mouseWheelStartLocation = null;

  public enum ObjectType {
    JOINT, MODEL, CAMERA_MARKER, OBJECT_MARKER, MAIN_CAMERA, UNKNOWN, ANY;

    public static ObjectType getObjectType(AbstractTransformableImp selected) {
      if (selected instanceof JointImp) {
        return ObjectType.JOINT;
      } else if (selected instanceof ObjectMarkerImp) {
        return ObjectType.OBJECT_MARKER;
      } else if (selected instanceof CameraMarkerImp) {
        return ObjectType.CAMERA_MARKER;
      } else if (selected instanceof ModelImp) {
        return ObjectType.MODEL;
      } else if (selected instanceof CameraImp || selected instanceof VrUserImp) {
        return ObjectType.MAIN_CAMERA;
      } else {
        return ObjectType.UNKNOWN;
      }
    }
  }

  public enum CameraView {
    MAIN, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, ACTIVE_VIEW, PICK_CAMERA
  }

  private static final class CameraSet {
    CameraSet(SymmetricPerspectiveCamera mainCamera,
              SymmetricPerspectiveCamera layoutCamera,
              OrthographicCamera orthographicCamera) {
      this.mainCamera = mainCamera;
      this.layoutCamera = layoutCamera;
      this.orthographicCamera = orthographicCamera;
    }

    void setActiveCamera(AbstractCamera camera) {
      this.activeCamera = camera;
    }

    AbstractCamera getActiveCamera() {
      return this.activeCamera;
    }

    boolean hasCamera(AbstractCamera camera) {
      return mainCamera == camera
          || layoutCamera == camera
          || orthographicCamera == camera;
    }

    private final SymmetricPerspectiveCamera mainCamera;
    private final SymmetricPerspectiveCamera layoutCamera;
    private final OrthographicCamera orthographicCamera;

    private AbstractCamera activeCamera;
  }
}
