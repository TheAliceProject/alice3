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
package org.alice.stageide.sceneeditor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.animation.ClockBasedAnimator;
import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import edu.cmu.cs.dennisc.render.LightweightOnscreenRenderTarget;
import edu.cmu.cs.dennisc.render.RenderCapabilities;
import edu.cmu.cs.dennisc.render.RenderFactory;
import edu.cmu.cs.dennisc.render.RenderUtils;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayEvent;
import edu.cmu.cs.dennisc.render.event.AutomaticDisplayListener;
import edu.cmu.cs.dennisc.render.event.RenderTargetListener;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Element;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocumentFrame;
import org.alice.ide.ReasonToDisableSomeAmountOfRendering;
import org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.alice.ide.preferences.IsToolBarShowing;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.interact.DragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.condition.ClickedObjectCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;
import org.alice.interact.manipulator.ManipulatorClickAdapter;
import org.alice.interact.manipulator.scenegraph.SnapGrid;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.StageIDE;
import org.alice.stageide.croquet.models.sceneditor.ViewListSelectionState;
import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.oneshot.DynamicOneShotMenuModel;
import org.alice.stageide.run.RunComposite;
import org.alice.stageide.sceneeditor.draganddrop.SceneDropSite;
import org.alice.stageide.sceneeditor.interact.CameraNavigatorWidget;
import org.alice.stageide.sceneeditor.interact.GlobalDragAdapter;
import org.alice.stageide.sceneeditor.side.SideComposite;
import org.alice.stageide.sceneeditor.snap.SnapState;
import org.alice.stageide.sceneeditor.viewmanager.CameraMarkerTracker;
import org.alice.stageide.sceneeditor.viewmanager.CameraViewCellRenderer;
import org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities;
import org.alice.stageide.sceneeditor.viewmanager.MoveActiveCameraToMarkerActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveMarkerToActiveCameraActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveMarkerToSelectedObjectActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveSelectedObjectToMarkerActionOperation;
import org.alice.stageide.sceneeditor.views.InstanceFactorySelectionPanel;
import org.alice.stageide.sceneeditor.views.SceneObjectPropertyManagerPanel;
import org.lgna.croquet.*;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.triggers.InputEventTrigger;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.ComboBox;
import org.lgna.croquet.views.CompassPointSpringPanel;
import org.lgna.croquet.views.DragComponent;
import org.lgna.croquet.views.SpringPanel.Horizontal;
import org.lgna.croquet.views.SpringPanel.Vertical;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.project.Project;
import org.lgna.project.ast.*;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.*;
import org.lgna.story.implementation.*;

import edu.cmu.cs.dennisc.math.*;
import edu.cmu.cs.dennisc.render.event.RenderTargetDisplayChangeEvent;
import edu.cmu.cs.dennisc.render.event.RenderTargetInitializeEvent;
import edu.cmu.cs.dennisc.render.event.RenderTargetRenderEvent;
import edu.cmu.cs.dennisc.render.event.RenderTargetResizeEvent;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;
import org.lgna.story.resources.ModelResource;

/**
 * @author dculyba
 */
public class StorytellingSceneEditor extends AbstractSceneEditor implements RenderTargetListener {
  private boolean isVrScene;

  private class SceneEditorDropReceptor extends AbstractDropReceptor {
    @Override
    public boolean isPotentiallyAcceptingOf(DragModel dragModel) {
      return dragModel instanceof GalleryDragModel;
    }

    @Override
    public void dragStarted(DragStep step) {
      DragModel model = step.getModel();
      DragComponent dragSource = step.getDragSource();
      dragSource.showDragProxy();
      if (model instanceof GalleryDragModel) {
        GalleryDragModel galleryDragModel = (GalleryDragModel) model;
        System.err.println("galleryNode.setDesiredTransformation(null);");
        //galleryNode.setDesiredTransformation(null);
      }
    }

    @Override
    public void dragEntered(DragStep dragAndDropContext) {
    }

    private boolean isDropLocationOverLookingGlass(DragStep dragAndDropContext) {
      MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
      Point pointInLookingGlass = SwingUtilities.convertPoint(eSource.getComponent(), eSource.getPoint(), lookingGlassPanel.getAwtComponent());
      return lookingGlassPanel.getAwtComponent().contains(pointInLookingGlass);
    }

    private boolean overLookingGlass = false;

    @Override
    public DropSite dragUpdated(DragStep dragStep) {
      if (isDropLocationOverLookingGlass(dragStep)) {
        if (!overLookingGlass) {
          overLookingGlass = true;
          globalDragAdapter.dragEntered(dragStep);
        }
        globalDragAdapter.dragUpdated(dragStep);
      } else {
        if (overLookingGlass) {
          overLookingGlass = false;
          globalDragAdapter.dragExited(dragStep);
        }
      }
      AffineMatrix4x4 t = globalDragAdapter.getDropTargetTransformation();
      return t != null ? new SceneDropSite(t) : null;
    }

    @Override
    protected Triggerable dragDroppedPostRejectorCheck(DragStep dragStep) {
      if (isDropLocationOverLookingGlass(dragStep)) {
        DropSite dropSite = new SceneDropSite(globalDragAdapter.getDropTargetTransformation());
        return dragStep.getModel().getDropOperation(dragStep, dropSite);
      }
      return null;
    }

    @Override
    public void dragExited(DragStep dragAndDropContext, boolean isDropRecipient) {
    }

    @Override
    public void dragStopped(DragStep dragStep) {
      globalDragAdapter.dragExited(dragStep);
    }

    @Override
    public TrackableShape getTrackableShape(DropSite potentialDropSite) {
      return StorytellingSceneEditor.this;
    }

    @Override
    public SwingComponentView<?> getViewController() {
      return StorytellingSceneEditor.this;
    }
  }

  private static final String SHOW_JOINTED_MODEL_VISUALIZATIONS_KEY = StorytellingSceneEditor.class.getName() + ".showJointedModelVisualizations";

  private static class SingletonHolder {
    private static StorytellingSceneEditor instance = new StorytellingSceneEditor();
  }

  public static StorytellingSceneEditor getInstance() {
    return SingletonHolder.instance;
  }

  private final SceneEditorDropReceptor dropReceptor = new SceneEditorDropReceptor();

  private StorytellingSceneEditor() {
  }

  public DropReceptor getDropReceptor() {
    return this.dropReceptor;
  }

  private static Icon EXPAND_ICON = IconUtilities.createImageIcon(StorytellingSceneEditor.class.getResource("images/24/expand.png"));
  private static Icon CONTRACT_ICON = IconUtilities.createImageIcon(StorytellingSceneEditor.class.getResource("images/24/contract.png"));

  private AutomaticDisplayListener automaticDisplayListener = new AutomaticDisplayListener() {
    @Override
    public void automaticDisplayCompleted(AutomaticDisplayEvent e) {
      StorytellingSceneEditor.this.animator.update();
    }
  };
  private LightweightOnscreenRenderTarget onscreenRenderTarget = RenderUtils.getDefaultRenderFactory().createLightweightOnscreenRenderTarget(new RenderCapabilities.Builder().stencilBits(0).build());

  private class LookingGlassPanel extends CompassPointSpringPanel {
    @Override
    protected JPanel createJPanel() {
      return StorytellingSceneEditor.this.onscreenRenderTarget.getAwtComponent();
    }

    @Override
    public void setNorthWestComponent(AwtComponentView<?> northWestComponent) {
      super.setNorthWestComponent(northWestComponent);
      if (northWestComponent != null) {
        SpringLayout springLayout = (SpringLayout) this.getAwtComponent().getLayout();
        springLayout.putConstraint(SpringLayout.SOUTH, northWestComponent.getAwtComponent(), -this.getPad(), SpringLayout.SOUTH, this.getAwtComponent());
      }
    }
  }

  private final ValueListener<Boolean> showSnapGridListener = new ValueListener<Boolean>() {
    @Override
    public void valueChanged(ValueEvent<Boolean> e) {
      StorytellingSceneEditor.this.setShowSnapGrid(e.getNextValue());
    }
  };

  private final ValueListener<Boolean> snapEnabledListener = new ValueListener<Boolean>() {
    @Override
    public void valueChanged(ValueEvent<Boolean> e) {
      if (SnapState.getInstance().isShowSnapGridEnabled()) {
        StorytellingSceneEditor.this.setShowSnapGrid(e.getNextValue());
      }
    }
  };

  private final ValueListener<Double> snapGridSpacingListener = new ValueListener<Double>() {
    @Override
    public void valueChanged(ValueEvent<Double> e) {
      StorytellingSceneEditor.this.setSnapGridSpacing(e.getNextValue());
    }
  };

  private final ValueListener<UserField> cameraMarkerFieldSelectionListener = new ValueListener<UserField>() {
    @Override
    public void valueChanged(ValueEvent<UserField> e) {
      StorytellingSceneEditor.this.handleCameraMarkerFieldSelection(e.getNextValue());
    }
  };

  private final ValueListener<UserField> objectMarkerFieldSelectionListener = new ValueListener<UserField>() {
    @Override
    public void valueChanged(ValueEvent<UserField> e) {
      StorytellingSceneEditor.this.handleObjectMarkerFieldSelection(e.getNextValue());
    }
  };

  private final ValueListener<InstanceFactory> instanceFactorySelectionListener = new ValueListener<InstanceFactory>() {
    @Override
    public void valueChanged(ValueEvent<InstanceFactory> e) {
      StorytellingSceneEditor.this.selectionIsFromInstanceSelector = true;
      StorytellingSceneEditor.this.setSelectedInstance(e.getNextValue());
      StorytellingSceneEditor.this.selectionIsFromInstanceSelector = false;
    }
  };

  private final ValueListener<CameraOption> mainCameraViewSelectionObserver =
      e -> StorytellingSceneEditor.this.handleMainCameraViewSelection();

  private boolean isInitialized = false;

  private ClockBasedAnimator animator = new ClockBasedAnimator();
  private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
  private GlobalDragAdapter globalDragAdapter;
  // The location of the Camera or VR user ground. Same as sceneCameraImp for non VR scenes.
  private TransformableImp movableSceneCameraImp;
  // The camera or VR headset. Same as movableSceneCameraImp for non VR scenes.
  private CameraImp<SymmetricPerspectiveCamera> sceneCameraImp;
  private CameraNavigatorWidget mainCameraNavigatorWidget = null;
  private Button expandButton;
  private Button contractButton;
  private InstanceFactorySelectionPanel instanceFactorySelectionPanel = null;

  private final Button runButton = IsToolBarShowing.getValue() ? null : RunComposite.getInstance().getLaunchOperation().createButton();

  private OrthographicCameraImp orthographicCameraImp = null;
  private PerspectiveCameraMarkerImp startingCameraMarkerImp;

  private ComboBox<CameraOption> mainCameraViewSelector;
  private CameraMarkerTracker mainCameraViewTracker;
  private CameraOption savedSceneEditorViewSelection = null;

  private ImmutableDataSingleSelectListState<CameraOption> mainCameraMarkerList = ViewListSelectionState.getInstance();

  private boolean selectionIsFromInstanceSelector = false;
  private boolean selectionIsFromMain = false;

  protected SnapGrid snapGrid;

  public boolean isStartingCameraView() {
    return mainCameraViewSelector.getModel().getListSelectionState().getValue() == CameraOption.STARTING_CAMERA_VIEW;
  }

  public void setStartingCameraMarkerTransformation(AffineMatrix4x4 transform) {
    mainCameraViewTracker.setStartingCameraMarkerTransformation(transform);
  }

  public static class SceneEditorProgramImp extends ProgramImp {
    public SceneEditorProgramImp(SProgram abstraction) {
      super(abstraction, StorytellingSceneEditor.getInstance().onscreenRenderTarget);
    }

    @Override
    public Animator getAnimator() {
      return StorytellingSceneEditor.getInstance().animator;
    }
  }

  @Override
  protected UserInstance createProgramInstance() {
    ProgramImp.ACCEPTABLE_HACK_FOR_NOW_setClassForNextInstance(SceneEditorProgramImp.class);
    return super.createProgramInstance();
  }

  private void setSelectedFieldOnManipulator(UserField field) {
    if (this.globalDragAdapter != null) {
      SThing selectedEntity = this.getInstanceInJavaVMForField(field, SThing.class);
      TransformableImp transImp = null;
      if (selectedEntity != null) {
        EntityImp imp = EmployeesOnly.getImplementation(selectedEntity);
        if (imp instanceof TransformableImp) {
          transImp = (TransformableImp) imp;
        }
      }
      this.globalDragAdapter.setSelectedImplementation(transImp);
    }
  }

  private void setSelectedExpressionOnManipulator(Expression expression) {
    if (this.globalDragAdapter != null) {
      SThing selectedEntity = this.getInstanceInJavaVMForExpression(expression, SThing.class);
      AbstractTransformableImp transImp = null;
      if (selectedEntity != null) {
        EntityImp imp = EmployeesOnly.getImplementation(selectedEntity);
        if (imp instanceof AbstractTransformableImp) {
          transImp = (AbstractTransformableImp) imp;
        }
      }
      this.globalDragAdapter.setSelectedImplementation(transImp);
    }
  }

  private void setSelectedInstance(InstanceFactory instanceFactory) {
    Expression expression = instanceFactory != null ? instanceFactory.createExpression() : null;
    if (expression instanceof FieldAccess) {
      FieldAccess fa = (FieldAccess) expression;
      AbstractField field = fa.field.getValue();
      if (field instanceof UserField) {
        UserField uf = (UserField) field;
        setSelectedField(uf.getDeclaringType(), uf);
      }
    } else if (expression instanceof MethodInvocation) {
      setSelectedExpression(expression);
    } else if (expression instanceof ArrayAccess) {
      setSelectedExpression(expression);
    } else if (expression instanceof ThisExpression) {
      UserField uf = getActiveSceneField();
      if (uf != null) {
        setSelectedField(uf.getDeclaringType(), uf);
      } else {
        return;
      }
    }
    getPropertyPanel().setSelectedInstance(instanceFactory);
  }

  public void setSelectedExpression(Expression expression) {
    if (!this.selectionIsFromMain) {
      this.selectionIsFromMain = true;
      if (this.globalDragAdapter != null) {
        setSelectedExpressionOnManipulator(expression);
      }
      this.selectionIsFromMain = false;
    }
  }

  public void centerCameraOn(UserField field) {
    mainCameraViewTracker.centerMarkersOn(field);
  }

  @Override
  public void setSelectedField(UserType<?> declaringType, UserField field) {
    if (!this.selectionIsFromMain) {
      this.selectionIsFromMain = true;
      final AbstractType<?, ?, ?> valueType = field.getValueType();
      if (isSelectableType(valueType)) {
        super.setSelectedField(declaringType, field);

        MoveSelectedObjectToMarkerActionOperation.getInstance().setSelectedField(field);
        MoveMarkerToSelectedObjectActionOperation.getInstance().setSelectedField(field);
        if (!this.selectionIsFromInstanceSelector) {
          StageIDE ide = StageIDE.getActiveInstance();
          InstanceFactoryState instanceFactoryState = ide.getDocumentFrame().getInstanceFactoryState();
          if (field == this.getActiveSceneField()) {
            instanceFactoryState.setValueTransactionlessly(ide.getInstanceFactoryForScene());
          } else {
            instanceFactoryState.setValueTransactionlessly(ide.getInstanceFactoryForSceneField(field));
          }
        }
      }
      if (this.globalDragAdapter != null) {
        setSelectedFieldOnManipulator(field);
      }
      this.selectionIsFromMain = false;
    }

    //TEST
    Runnable refresher = new Runnable() {
      @Override
      public void run() {
        StorytellingSceneEditor.this.revalidateAndRepaint();
        SideComposite.getInstance().getObjectPropertiesTab().getView().revalidateAndRepaint();
        SideComposite.getInstance().getObjectMarkersTab().getView().revalidateAndRepaint();
      }
    };
    try {
      SwingUtilities.invokeLater(refresher);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private boolean isSelectableType(AbstractType<?, ?, ?> valueType) {
    return !valueType.isAssignableFrom(SThingMarker.class)
            && !valueType.isAssignableFrom(SCameraMarker.class)
            && !valueType.isAssignableFrom(SVRHand.class);
  }

  public Boolean isVrActive() {
    return isVrScene;
  }

  private void setIsVrActive(boolean isActive) {
    isVrScene = isActive;
    mainCameraViewTracker.setIsVrActive(isActive);

    // Update camera marker move buttons
    MoveActiveCameraToMarkerActionOperation.getInstance().updateDisplay();
    MoveMarkerToActiveCameraActionOperation.getInstance().updateDisplay();
  }

  private void setCameras(SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera) {
    this.globalDragAdapter.addCameraView(CameraView.MAIN, perspectiveCamera, orthographicCamera);
    this.globalDragAdapter.makeCameraActive(perspectiveCamera);
    this.mainCameraViewTracker.setCameras(perspectiveCamera, orthographicCamera);
    this.snapGrid.addCamera(perspectiveCamera);
    this.snapGrid.addCamera(orthographicCamera);
    this.snapGrid.setCurrentCamera(perspectiveCamera);
  }

  private void showLookingGlassPanel() {
    synchronized (this.getTreeLock()) {
      this.addCenterComponent(this.lookingGlassPanel);
    }
  }

  private void hideLookingGlassPanel() {
    synchronized (this.getTreeLock()) {
      this.removeComponent(this.lookingGlassPanel);
    }
  }

  @Override
  protected void handleExpandContractChange(boolean isExpanded) {
    //todo
    synchronized (this.getTreeLock()) {
      this.mainCameraNavigatorWidget.setExpanded(isExpanded);
      if (this.runButton != null) {
        this.lookingGlassPanel.setNorthEastComponent(this.runButton);
      }
      if (isExpanded) {
        this.lookingGlassPanel.setNorthWestComponent(this.instanceFactorySelectionPanel);
        this.lookingGlassPanel.setSouthEastComponent(this.contractButton);

        this.lookingGlassPanel.setSouthComponent(this.mainCameraNavigatorWidget);

        if (this.savedSceneEditorViewSelection != null) {
          this.mainCameraMarkerList.setValueTransactionlessly(this.savedSceneEditorViewSelection);
        }
      } else {
        this.lookingGlassPanel.setNorthWestComponent(null);
        this.lookingGlassPanel.setSouthEastComponent(this.expandButton);
        this.lookingGlassPanel.setSouthComponent(null);

        this.savedSceneEditorViewSelection = this.mainCameraMarkerList.getValue();
        this.mainCameraMarkerList.setValueTransactionlessly(CameraOption.STARTING_CAMERA_VIEW);
      }
      this.mainCameraViewSelector.setVisible(isExpanded);
    }
  }

  private SceneObjectPropertyManagerPanel getPropertyPanel() {
    return SideComposite.getInstance().getObjectPropertiesTab().getView();
  }

  private void handleCameraMarkerFieldSelection(UserField cameraMarkerField) {
    CameraMarkerImp newMarker = (CameraMarkerImp) this.getMarkerForField(cameraMarkerField);
    this.globalDragAdapter.setSelectedCameraMarker(newMarker);
    MoveActiveCameraToMarkerActionOperation.getInstance().setMarkerField(cameraMarkerField);
    MoveMarkerToActiveCameraActionOperation.getInstance().setMarkerField(cameraMarkerField);
  }

  private void handleObjectMarkerFieldSelection(UserField objectMarkerField) {
    ObjectMarkerImp newMarker = (ObjectMarkerImp) this.getMarkerForField(objectMarkerField);
    this.globalDragAdapter.setSelectedObjectMarker(newMarker);
    MoveSelectedObjectToMarkerActionOperation.getInstance().setMarkerField(objectMarkerField);
    MoveMarkerToSelectedObjectActionOperation.getInstance().setMarkerField(objectMarkerField);
  }

  public void setSelectedObjectMarker(UserField objectMarkerField) {
    RefreshableDataSingleSelectListState<UserField> markerList = SideComposite.getInstance().getObjectMarkersTab().getMarkerListState();
    markerList.setSelectedIndex(markerList.indexOf(objectMarkerField));
  }

 private void setSelectedCameraMarker(UserField cameraMarkerField) {
    RefreshableDataSingleSelectListState<UserField> markerList = SideComposite.getInstance().getCameraMarkersTab().getMarkerListState();
    markerList.setSelectedIndex(markerList.indexOf(cameraMarkerField));
  }

  private void handleManipulatorSelection(SelectionEvent e) {
    EntityImp imp = e.getTransformable();
    if (imp != null) {
      UserField field = this.getFieldForInstanceInJavaVM(imp.getAbstraction());
      if (field != null) {
        if (field.getValueType().isAssignableFrom(SCameraMarker.class)) {
          this.setSelectedCameraMarker(field);
        } else if (field.getValueType().isAssignableFrom(SThingMarker.class)) {
          this.setSelectedObjectMarker(field);
        } else {
          this.setSelectedField(field.getDeclaringType(), field);
        }
      } else if (imp == this.startingCameraMarkerImp) {
        this.setSelectedField(this.getActiveSceneType(), this.getFieldForInstanceInJavaVM(movableSceneCameraImp.getAbstraction()));
      }
    } else {
      UserField uf = getActiveSceneField();
      setSelectedField(uf.getDeclaringType(), uf);
    }
  }

  private void showRightClickMenuForModel(InputState clickInput) {
    Element element = clickInput.getClickPickedTransformable(true);
    if (element != null) {
      EntityImp entityImp = EntityImp.getInstance(element);
      SThing entity = entityImp.getAbstraction();
      UserField field;
      if (entity != null) {
        field = this.getFieldForInstanceInJavaVM(entity);
      } else {
        //todo: handle camera
        field = null;
      }
      if (field != null) {
        InstanceFactory instanceFactory = ThisFieldAccessFactory.getInstance(field);

        DynamicOneShotMenuModel.getInstance().getPopupPrepModel().fire(InputEventTrigger.createUserActivity(clickInput.getInputEvent()));
      } else {
        Logger.severe(entityImp);
      }
    }
  }

  private void handleMainCameraViewSelection() {
    StageIDE ide = StageIDE.getActiveInstance();
    InstanceFactoryState instanceFactoryState = ide.getDocumentFrame().getInstanceFactoryState();
    UserField field = getSelectedField();
    if (field != this.getActiveSceneField()) {
      instanceFactoryState.setValueTransactionlessly(ide.getInstanceFactoryForSceneField(field));
    }
  }

  private void switchToCamera(AbstractCamera camera) {
    if (onscreenRenderTarget.getSgCameraCount() != 1
            || onscreenRenderTarget.getSgCameraAt(0) != camera) {
      onscreenRenderTarget.clearSgCameras();
      onscreenRenderTarget.addSgCamera(camera);
    }
    snapGrid.setCurrentCamera(camera);
    globalDragAdapter.makeCameraActive(camera);
    onscreenRenderTarget.repaint();
    revalidateAndRepaint();
  }

  public void switchToOrthographicCamera() {
    switchToCamera(orthographicCameraImp.getSgCamera());
    mainCameraNavigatorWidget.setToOrthographicMode();
  }

  public void switchToPerspectiveCamera() {
    switchToCamera(sceneCameraImp.getSgCamera());
    mainCameraNavigatorWidget.setToPerspectiveMode();
  }

  @Override
  protected void initializeComponents() {
    if (this.isInitialized) {
      return;
    }
    this.snapGrid = new SnapGrid();
    SnapState.getInstance().getShowSnapGridState().addAndInvokeNewSchoolValueListener(this.showSnapGridListener);
    SnapState.getInstance().getIsSnapEnabledState().addAndInvokeNewSchoolValueListener(this.snapEnabledListener);
    SnapState.getInstance().getSnapGridSpacingState().addAndInvokeNewSchoolValueListener(this.snapGridSpacingListener);

    ProjectDocumentFrame docFrame = IDE.getActiveInstance().getDocumentFrame();
    docFrame.getInstanceFactoryState().addAndInvokeNewSchoolValueListener(this.instanceFactorySelectionListener);

    this.globalDragAdapter = new GlobalDragAdapter(this);
    this.globalDragAdapter.setOnscreenRenderTarget(onscreenRenderTarget);
    this.onscreenRenderTarget.addRenderTargetListener(this);
    this.globalDragAdapter.setAnimator(animator);
    if (this.getSelectedField() != null) {
      setSelectedFieldOnManipulator(this.getSelectedField());
    }

    this.mainCameraNavigatorWidget = new CameraNavigatorWidget(this.globalDragAdapter, CameraView.MAIN);

    this.expandButton = docFrame.getSetToSetupScenePerspectiveOperation().createButton();
    this.expandButton.setClobberIcon(EXPAND_ICON);
    //todo: tool tip text
    //this.expandButton.getAwtComponent().setText( null );
    this.expandButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    this.contractButton = docFrame.getSetToCodePerspectiveOperation().createButton();
    this.contractButton.setClobberIcon(CONTRACT_ICON);
    this.contractButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
    this.instanceFactorySelectionPanel = new InstanceFactorySelectionPanel();
    this.orthographicCameraImp = new OrthographicCameraImp();
    this.orthographicCameraImp.getSgCamera().nearClippingPlaneDistance.setValue(.01d);

    this.globalDragAdapter.addSelectionListener(new SelectionListener() {
      @Override
      public void selecting(SelectionEvent e) {
      }

      @Override
      public void selected(SelectionEvent e) {
        StorytellingSceneEditor.this.handleManipulatorSelection(e);
      }
    });

    ClickedObjectCondition rightMouseAndInteractive = new ClickedObjectCondition(MouseEvent.BUTTON3, new PickCondition(PickHint.PickType.TURNABLE.pickHint()));
    ManipulatorClickAdapter rightClickAdapter = new ManipulatorClickAdapter() {
      @Override
      public void onClick(InputState clickInput) {
        showRightClickMenuForModel(clickInput);

      }
    };
    this.globalDragAdapter.addClickAdapter(rightClickAdapter, rightMouseAndInteractive);

    SideComposite.getInstance().getCameraMarkersTab().getMarkerListState().addAndInvokeNewSchoolValueListener(this.cameraMarkerFieldSelectionListener);
    SideComposite.getInstance().getObjectMarkersTab().getMarkerListState().addAndInvokeNewSchoolValueListener(this.objectMarkerFieldSelectionListener);

    this.mainCameraViewTracker = new CameraMarkerTracker(this, animator);
    startingCameraMarkerImp = mainCameraViewTracker.startingCameraMarkerImp;
    this.mainCameraViewSelector = this.mainCameraMarkerList.getPrepModel().createComboBox();
    this.mainCameraViewSelector.setRenderer(new CameraViewCellRenderer(this.mainCameraViewTracker));
    this.mainCameraViewSelector.setFontSize(15);
    this.mainCameraMarkerList.addAndInvokeNewSchoolValueListener(this.mainCameraViewTracker);
    this.mainCameraMarkerList.addAndInvokeNewSchoolValueListener(this.mainCameraViewSelectionObserver);
    this.lookingGlassPanel.addComponent(this.mainCameraViewSelector, Horizontal.CENTER, 0, Vertical.NORTH, 20);
    this.isInitialized = true;
  }

  @Override
  public void addField(UserType<?> declaringType, UserField field, int index, Statement... statements) {
    super.addField(declaringType, field, index, statements);
    if (field.getValueType().isAssignableTo(SMarker.class)) {
      SMarker marker = this.getInstanceInJavaVMForField(field, SMarker.class);
      MarkerImp markerImp = EmployeesOnly.getImplementation(marker);
      markerImp.setDisplayVisuals(true);
      markerImp.setShowing(true);

      if (field.getValueType().isAssignableTo(CameraMarker.class)) {
        this.setSelectedCameraMarker(field);
        ((PerspectiveCameraMarkerImp) markerImp).setVrActive(isVrActive());
      } else if (field.getValueType().isAssignableTo(SThingMarker.class)) {
        this.setSelectedObjectMarker(field);
      }
    }
    setInitialCodeStateForField(field, getCurrentStateCodeForField(field));
    if (SystemUtilities.isPropertyTrue(SHOW_JOINTED_MODEL_VISUALIZATIONS_KEY)) {
      if (field.getValueType().isAssignableTo(SJointedModel.class)) {
        SJointedModel jointedModel = this.getInstanceInJavaVMForField(field, SJointedModel.class);
        JointedModelImp jointedModelImp = EmployeesOnly.getImplementation(jointedModel);
        jointedModelImp.opacity.setValue(0.25f);
        jointedModelImp.showVisualization();
      } else if (field.getValueType().isAssignableTo(SModel.class)) {
        SModel model = this.getInstanceInJavaVMForField(field, SModel.class);
        ModelImp modelImp = EmployeesOnly.getImplementation(model);
        modelImp.showVisualization();
      }
    }
  }

  @Override
  protected void setActiveScene(UserField sceneField) {
    super.setActiveScene(sceneField);

    if (sceneField != null) {
      EmployeesOnly.getImplementation(getProgramInstanceInJava()).setSimulationSpeedFactor(Double.POSITIVE_INFINITY);

      UserInstance sceneAliceInstance = getActiveSceneInstance();
      SProgram program = getProgramInstanceInJava();
      SScene scene = sceneAliceInstance.getJavaInstance(SScene.class);

      SceneImp ACCEPTABLE_HACK_sceneImp = EmployeesOnly.getImplementation(scene);
      ACCEPTABLE_HACK_sceneImp.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushPerformMinimalInitialization();
      try {
        program.setActiveScene(scene);
      } finally {
        ACCEPTABLE_HACK_sceneImp.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_popPerformMinimalInitialization();
      }
      UserMethod generatedSetupMethod = sceneAliceInstance.getType().getDeclaredMethod(StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME);
      useSceneAsVehicleForDisconnectedModels(generatedSetupMethod);
      this.getVirtualMachine().ENTRY_POINT_invoke(sceneAliceInstance, generatedSetupMethod);

      getPropertyPanel().setSceneInstance(sceneAliceInstance);

      this.instanceFactorySelectionPanel.setType(sceneAliceInstance.getType());
      for (AbstractField field : sceneField.getValueType().getDeclaredFields()) {
        if (field.getValueType().isAssignableTo(SCamera.class)) {
          sceneCameraImp = getImplementation(field);
          movableSceneCameraImp = sceneCameraImp;
          setIsVrActive(false);
          break;
        }
        if (field.getValueType().isAssignableTo(SVRUser.class)) {
          VrUserImp vrUserImp = getImplementation(field);
          sceneCameraImp = EmployeesOnly.getImplementation(vrUserImp.getAbstraction().getHeadset());
          movableSceneCameraImp = vrUserImp;
          setIsVrActive(true);
          break;
        }
      }

      assert ((this.globalDragAdapter != null) && (this.sceneCameraImp != null) && (this.orthographicCameraImp != null));
      this.globalDragAdapter.clearCameraViews();
      this.globalDragAdapter.addCameraView(CameraView.MAIN, this.sceneCameraImp.getSgCamera(), null);
      this.globalDragAdapter.makeCameraActive(this.sceneCameraImp.getSgCamera());

      SceneImp sceneImp = this.getActiveSceneImplementation();
      //Add and set up the snap grid (this needs to happen before setting the camera)
      sceneImp.getSgComposite().addComponent(this.snapGrid);
      this.snapGrid.setTranslationOnly(0, 0, 0, AsSeenBy.SCENE);
      this.snapGrid.setShowing(SnapState.getInstance().shouldShowSnapGrid());

      // Initialize stuff that needs a camera
      this.setCameras(this.sceneCameraImp.getSgCamera(), this.orthographicCameraImp.getSgCamera());

      MoveActiveCameraToMarkerActionOperation.getInstance().setCamera(movableSceneCameraImp);
      MoveMarkerToActiveCameraActionOperation.getInstance().setCamera(movableSceneCameraImp);
      MoveActiveCameraToMarkerActionOperation.getInstance().setCameraMarker(startingCameraMarkerImp);
      MoveMarkerToActiveCameraActionOperation.getInstance().setCameraMarker(startingCameraMarkerImp);

      // Add orthographic cameras and markers
      sceneImp.getSgComposite().addComponent(this.orthographicCameraImp.getSgCamera().getParent());
      Component[] existingComponents = sceneImp.getSgComposite().getComponentsAsArray();
      for (CameraOption cameraOption : this.mainCameraMarkerList) {
        CameraMarkerImp marker = this.mainCameraViewTracker.getCameraMarker(cameraOption);
        boolean alreadyHasIt = false;
        for (Component c : existingComponents) {
          if (c == marker.getSgComposite()) {
            alreadyHasIt = true;
            break;
          }
        }
        if (!alreadyHasIt) {
          marker.setVehicle(sceneImp);
        }
      }

      mainCameraViewTracker.updateLayoutCameraForScene(movableSceneCameraImp);

      savedSceneEditorViewSelection = null;
      mainCameraViewTracker.trackStartingCameraView();

      this.setSelectedCameraMarker(null);
      this.setSelectedObjectMarker(null);

      for (AbstractField field : sceneField.getValueType().getDeclaredFields()) {
        // Turn markers on, so they're visible in the scene editor (note: markers are hidden by default so that when a world runs they aren't seen.
        // we have to manually make them visible to see them in the scene editor)
        if (field.getValueType() != null && field.getValueType().isAssignableTo(SMarker.class)) {
          SMarker marker = this.getInstanceInJavaVMForField(field, SMarker.class);
          MarkerImp markerImp = EmployeesOnly.getImplementation(marker);
          markerImp.setDisplayVisuals(true);
          markerImp.setShowing(true);
        }
        if (field instanceof UserField) {
          UserField userField = (UserField) field;
          if (userField.getManagementLevel() == ManagementLevel.MANAGED) {
            this.setInitialCodeStateForField(userField, getCurrentStateCodeForField(userField));
          }
        }
      }
      EmployeesOnly.getImplementation(getProgramInstanceInJava()).setSimulationSpeedFactor(1.0);
    }
  }

  private void useSceneAsVehicleForDisconnectedModels(UserMethod generatedSetupMethod) {
    for (Statement statement : generatedSetupMethod.body.getValue().statements.getValue()) {
      MethodInvocation setVehicleCall = asSetVehicleCall(statement);
      if (setVehicleCall == null) {
        continue;
      }
      ArrayList<SimpleArgument> args = setVehicleCall.requiredArguments.getValue();
      if (args.size() == 1 && args.get(0).expression.getValue() instanceof NullLiteral) {
        args.get(0).expression.setValue(new ThisExpression());
      }
    }
  }

  private MethodInvocation asSetVehicleCall(Statement statement) {
    if (statement instanceof ExpressionStatement) {
      Expression expression = ((ExpressionStatement) statement).expression.getValue();
      if (expression instanceof MethodInvocation) {
        MethodInvocation mi = (MethodInvocation) expression;
        Method method = mi.method.getValue();
        if (method.getName().equalsIgnoreCase("setVehicle")) {
          return mi;
        }
      }
    }
    return null;
  }

  private boolean isSetVehicleInvocation(Statement statement) {
    return asSetVehicleCall(statement) != null;
  }

  @Override
  public void enableRendering(ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
    if ((reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN) || (reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK)) {
      this.onscreenRenderTarget.setRenderingEnabled(true);
    }
  }

  @Override
  public void disableRendering(ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
    if ((reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN) || (reasonToDisableSomeAmountOfRendering == ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK)) {
      this.onscreenRenderTarget.setRenderingEnabled(false);
    }
  }

  @Override
  public void preScreenCapture() {
    this.globalDragAdapter.setHandleVisibility(false);
  }

  @Override
  public void postScreenCapture() {
    this.globalDragAdapter.setHandleVisibility(true);
  }

  private void fillInAutomaticSetUpMethod(StatementListProperty bodyStatementsProperty, boolean isThis, AbstractField field, boolean getFullFieldState) {
    SetUpMethodGenerator.fillInAutomaticSetUpMethod(bodyStatementsProperty, isThis, field, this.getInstanceInJavaVMForField(field), this.getActiveSceneInstance(), getFullFieldState);
  }

  @Override
  public void setFieldToState(UserField field, Statement... statements) {
    EntityImp fieldImp = getImplementation(field);
    AffineMatrix4x4 originalTransform = fieldImp.getAbsoluteTransformation();
    super.setFieldToState(field, statements);
    if ((fieldImp == movableSceneCameraImp) && (mainCameraMarkerList.getValue() != CameraOption.STARTING_CAMERA_VIEW)) {
      startingCameraMarkerImp.setTransformation(startingCameraMarkerImp.getScene(), fieldImp.getAbsoluteTransformation());
      movableSceneCameraImp.setTransformation(movableSceneCameraImp.getScene(), originalTransform);
    }
  }

  @Override
  public Statement getCurrentStateCodeForField(UserField field) {
    Statement rv = null;

    BlockStatement bs = new BlockStatement();

    AffineMatrix4x4 currentCameraTransformable = movableSceneCameraImp.getLocalTransformation();
    if (movableSceneCameraImp.getVehicle() != null) {
      movableSceneCameraImp.setTransformation(startingCameraMarkerImp);
    } else {
      Logger.severe(movableSceneCameraImp);
    }
    this.fillInAutomaticSetUpMethod(bs.statements, false, field, true);
    if (movableSceneCameraImp.getVehicle() != null) {
      movableSceneCameraImp.setLocalTransformation(currentCameraTransformable);
      movableSceneCameraImp.applyAnimation();
    }

    Statement setVehicleStatement = null;
    for (Statement statement : bs.statements.getValue()) {
      if (isSetVehicleInvocation(statement)) {
        setVehicleStatement = statement;
        break;
      }
    }
    if (setVehicleStatement != null) {
      bs.statements.getValue().remove(setVehicleStatement);
    }
    DoTogether dt = new DoTogether(bs);
    if (setVehicleStatement != null) {
      DoInOrder dio = new DoInOrder(new BlockStatement(setVehicleStatement, dt));
      rv = dio;
    } else {
      rv = dt;
    }

    return rv;
  }

  @Override
  public void generateCodeForSetUp(StatementListProperty bodyStatementsProperty) {
    //Set the camera to have the point of view of the opening scene marker
    AffineMatrix4x4 currentCameraTransformable = movableSceneCameraImp.getLocalTransformation();
    movableSceneCameraImp.setTransformation(startingCameraMarkerImp);

    AbstractField sceneField = this.getActiveSceneField();
    this.fillInAutomaticSetUpMethod(bodyStatementsProperty, true, sceneField, false);
    for (AbstractField field : this.getActiveSceneType().getDeclaredFields()) {
      if (field instanceof UserField) {
        UserField userField = (UserField) field;
        if (userField.getManagementLevel() == ManagementLevel.MANAGED) {
          this.fillInAutomaticSetUpMethod(bodyStatementsProperty, false, field, false);
        }
      }
    }

    //Set the camera back to its original position
    movableSceneCameraImp.setLocalTransformation(currentCameraTransformable);
  }

  private Statement replaceReferencesInExpression(UserField fieldToReplace, UserField replacement, Statement statement) {
    IsInstanceCrawler<FieldAccess> crawler = IsInstanceCrawler.createInstance(FieldAccess.class);
    statement.crawl(crawler, CrawlPolicy.COMPLETE, null);

    Map<AbstractField, AbstractField> map = Maps.newHashMap();
    for (FieldAccess fieldAccess : crawler.getList()) {
      AbstractField field = fieldAccess.field.getValue();
      if (field == fieldToReplace) {
        fieldAccess.field.setValue(replacement);
      }
    }
    return statement;
  }

  @Override
  public Statement[] getDoStatementsForCopyField(UserField fieldToCopy, UserField newField, AffineMatrix4x4 initialTransform) {
    Statement stateCodeStatement = this.getCurrentStateCodeForField(fieldToCopy);
    stateCodeStatement = replaceReferencesInExpression(fieldToCopy, newField, stateCodeStatement);

    //Remove the setVehicle and setTransform statements from the setup code, so we can replace them with custom ones based on the fieldToCopy's vehicle and the initial transform
    List<BlockStatement> blockStatements = new LinkedList<BlockStatement>();
    if (stateCodeStatement instanceof BlockStatement) {
      blockStatements.add((BlockStatement) stateCodeStatement);
    } else if (stateCodeStatement instanceof AbstractStatementWithBody) {
      blockStatements.add(((AbstractStatementWithBody) stateCodeStatement).body.getValue());
    }
    while (!blockStatements.isEmpty()) {
      BlockStatement bs = blockStatements.remove(0);
      Statement setVehicleStatement = null;
      Statement setPositionStatement = null;
      Statement setOrientationStatement = null;
      for (Statement s : bs.statements.getValue()) {
        if (s instanceof BlockStatement) {
          blockStatements.add((BlockStatement) s);
        } else if (s instanceof AbstractStatementWithBody) {
          blockStatements.add(((AbstractStatementWithBody) s).body.getValue());
        } else if (s instanceof ExpressionStatement) {
          Expression expression = ((ExpressionStatement) s).expression.getValue();
          if (expression instanceof MethodInvocation) {
            MethodInvocation mi = (MethodInvocation) expression;
            Method method = mi.method.getValue();
            //Look for the setVehicle, setOrientation, and setPositions for the field. Note that we need to make sure these calls are being called on the field and not the joints, hence the check for FieldAccess (joints are called off of getJoint and resolve as a MethodInvocation)
            if (method.getName().equalsIgnoreCase("setVehicle") && (mi.expression.getValue() instanceof FieldAccess)) {
              setVehicleStatement = s;
            } else if (method.getName().equalsIgnoreCase("setOrientationRelativeToVehicle") && (mi.expression.getValue() instanceof FieldAccess)) {
              setOrientationStatement = s;
            } else if (method.getName().equalsIgnoreCase("setPositionRelativeToVehicle") && (mi.expression.getValue() instanceof FieldAccess)) {
              setPositionStatement = s;
            }
          }
        }
      }
      if (setVehicleStatement != null) {
        bs.statements.getValue().remove(setVehicleStatement);
      }
      if (setPositionStatement != null) {
        bs.statements.getValue().remove(setPositionStatement);
      }
      if (setOrientationStatement != null) {
        bs.statements.getValue().remove(setOrientationStatement);
      }
    }

    Object toCopyInstance = this.getInstanceInJavaVMForField(fieldToCopy);
    AbstractField toCopyVehicleField = null;
    if (toCopyInstance instanceof Rider) {
      SThing vehicleInstance = ((Rider) toCopyInstance).getVehicle();
      toCopyVehicleField = this.getFieldForInstanceInJavaVM(vehicleInstance);
    }
    Statement[] initializeStatements = SetUpMethodGenerator.getSetupStatementsForField(false, newField, this.getActiveSceneInstance(), toCopyVehicleField, initialTransform);
    Statement[] statementsToReturn = new Statement[initializeStatements.length + 1];
    for (int i = 0; i < initializeStatements.length; i++) {
      statementsToReturn[i] = initializeStatements[i];
    }
    statementsToReturn[initializeStatements.length] = stateCodeStatement;
    return statementsToReturn;
  }

  @Override
  public Statement[] getDoStatementsForAddField(UserField field, AffineMatrix4x4 initialTransform) {
    if ((initialTransform == null) && field.getValueType().isAssignableTo(SModel.class)) {
      AbstractType<?, ?, ?> type = field.getValueType();
      JavaType javaType = type.getFirstEncounteredJavaType();
      Class<?> cls = javaType.getClassReflectionProxy().getReification();
      Class<? extends ModelResource> resourceCls = null;
      if (SModel.class.isAssignableFrom(cls)) {
        resourceCls = AliceResourceClassUtilities.getResourceClassForModelClass((Class<? extends SModel>) cls);
      }
      Point3 location;
      if (resourceCls != null) {
        ClassResourceKey childKey = new ClassResourceKey((Class<? extends ModelResource>) cls);
        AxisAlignedBox box = childKey.getBoundingBox();
        boolean shouldPlaceOnGround = childKey.getPlaceOnGround();
        double y = (box != null) && shouldPlaceOnGround ? -box.getXMinimum() : 0;
        location = new Point3(0, y, 0);
      } else {
        location = Point3.createZero();
      }

      initialTransform = new AffineMatrix4x4(OrthogonalMatrix3x3.createIdentity(), location);
    }
    return SetUpMethodGenerator.getSetupStatementsForField(false, field, this.getActiveSceneInstance(), null, initialTransform);
  }

  @Override
  public Statement[] getUndoStatementsForAddField(UserField field) {
    List<Statement> undoStatements = Lists.newLinkedList();

    undoStatements.add(SetUpMethodGenerator.createSetVehicleNullStatement(field));

    return ArrayUtilities.createArray(undoStatements, Statement.class);
  }

  public Map<AbstractField, Statement> getRiders(UserField vehicle) {
    IDE.getActiveInstance().ensureProjectCodeUpToDate();
    UserInstance sceneAliceInstance = getActiveSceneInstance();
    UserMethod generatedSetupMethod = sceneAliceInstance.getType().getDeclaredMethod(StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME);
    Map<AbstractField, Statement> riders = new HashMap<>();
    for (Statement statement : generatedSetupMethod.body.getValue().statements.getValue()) {
      MethodInvocation setVehicleCall = asSetVehicleCall(statement);
      if (setVehicleCall != null && doesSetVehicleImplyVehicle(setVehicleCall, vehicle)) {
        FieldAccess riderAccess = (FieldAccess) setVehicleCall.expression.getValue();
        riders.put(riderAccess.field.getValue(), statement);
      }
    }
    return riders;
  }

  private boolean doesSetVehicleImplyVehicle(MethodInvocation setVehicleCall, UserField vehicle) {
    ArrayList<SimpleArgument> args = setVehicleCall.requiredArguments.getValue();
    if (args.size() == 1 && setVehicleCall.expression.getValue() instanceof FieldAccess) {
      Expression vehicleExpr = args.get(0).expression.getValue();
      return isDirectRider(vehicle, vehicleExpr) || isJointRider(vehicle, vehicleExpr);
    }
    return false;
  }

  private boolean isDirectRider(UserField vehicle, Expression vehicleExpr) {
    return vehicleExpr instanceof FieldAccess && ((FieldAccess) vehicleExpr).field.getValue() == vehicle;
  }

  private boolean isJointRider(UserField vehicle, Expression vehicleExpr) {
    if (vehicleExpr instanceof MethodInvocation) {
      MethodInvocation vehicleMethod = (MethodInvocation) vehicleExpr;
      if (vehicleMethod.expression.getValue() instanceof FieldAccess) {
        FieldAccess target = (FieldAccess) vehicleMethod.expression.getValue();
        return target.field.getValue() == vehicle;
      }
    }
    return false;
  }

  @Override
  public Statement[] getDoStatementsForRemoveField(UserField field, Map<AbstractField, Statement> riders) {
    List<Statement> doStatements = Lists.newLinkedList();
    for (AbstractField rider : riders.keySet()) {
      doStatements.add(SetUpMethodGenerator.createSetVehicleSceneStatement(rider));
    }
    doStatements.add(SetUpMethodGenerator.createSetVehicleNullStatement(field));
    return ArrayUtilities.createArray(doStatements, Statement.class);
  }

  @Override
  public Statement[] getUndoStatementsForRemoveField(UserField field, Map<AbstractField, Statement> riders) {
    Object instance = this.getInstanceInJavaVMForField(field);
    AbstractField vehicleField = null;
    if (instance instanceof Rider) {
      SThing vehicleInstance = ((Rider) instance).getVehicle();
      vehicleField = this.getFieldForInstanceInJavaVM(vehicleInstance);
    }
    Statement[] setupStatements = SetUpMethodGenerator.getSetupStatementsForInstance(false, instance, this.getActiveSceneInstance(), false);
    Statement vehicleStatement = SetUpMethodGenerator.createSetVehicleFieldStatement(field, vehicleField);
    Statement[] statements = new Statement[setupStatements.length + 1 + riders.size()];
    statements[0] = vehicleStatement;
    System.arraycopy(setupStatements, 0, statements, 1, setupStatements.length);

    int i = setupStatements.length + 1;
    for (Statement setVehicleStatement : riders.values()) {
      statements[i++] = setVehicleStatement;
    }
    return statements;
  }

  @Override
  protected void handleProjectOpened(Project nextProject) {
    if (this.onscreenRenderTarget != null) {
      this.onscreenRenderTarget.forgetAllCachedItems();
      NebulousIde.nonfree.unloadNebulousModelData();
    }

    NebulousIde.nonfree.unloadPerson();
    if (this.globalDragAdapter != null) {
      this.globalDragAdapter.clear();
    }
    super.handleProjectOpened(nextProject);
  }

  public void handleShowing() {
    RenderFactory renderFactory = RenderUtils.getDefaultRenderFactory();
    renderFactory.incrementAutomaticDisplayCount();
    renderFactory.addAutomaticDisplayListener(this.automaticDisplayListener);
    this.showLookingGlassPanel();
  }

  public void handleHiding() {
    this.hideLookingGlassPanel();
    RenderFactory renderFactory = RenderUtils.getDefaultRenderFactory();
    renderFactory.removeAutomaticDisplayListener(this.automaticDisplayListener);
    renderFactory.decrementAutomaticDisplayCount();
  }

  private void paintHorizonLine(Graphics graphics, LightweightOnscreenRenderTarget renderTarget, OrthographicCamera camera) {
    AffineMatrix4x4 cameraTransform = camera.getAbsoluteTransformation();
    double dotProd = Vector3.calculateDotProduct(cameraTransform.orientation.up, Vector3.accessPositiveYAxis());
    if ((dotProd == 1) || (dotProd == -1)) {
      //TODO: Make this handle retina displays and the fact that surface size and screen size may be different
      Dimension lookingGlassSize = renderTarget.getSurfaceSize();

      Point3 cameraPosition = camera.getAbsoluteTransformation().translation;

      ClippedZPlane dummyPlane = new ClippedZPlane(camera.picturePlane.getValue(), renderTarget.getActualViewport(camera));

      double lookingGlassHeight = lookingGlassSize.getHeight();

      double yRatio = this.onscreenRenderTarget.getSurfaceHeight() / dummyPlane.getHeight();
      double horizonInCameraSpace = 0.0d - cameraPosition.y;
      double distanceFromMaxY = dummyPlane.getYMaximum() - horizonInCameraSpace;
      int horizonLinePixelVal = (int) (yRatio * distanceFromMaxY);
      if ((horizonLinePixelVal >= 0) && (horizonLinePixelVal <= lookingGlassHeight)) {
        graphics.setColor(java.awt.Color.BLACK);
        graphics.drawLine(0, horizonLinePixelVal, lookingGlassSize.width, horizonLinePixelVal);
      }
    }
  }

  // ######### Begin implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter
  @Override
  public void initialized(RenderTargetInitializeEvent e) {
  }

  @Override
  public void cleared(RenderTargetRenderEvent e) {
  }

  @Override
  public void rendered(RenderTargetRenderEvent e) {
    if ((this.onscreenRenderTarget.getSgCameraCount() > 0) && (this.onscreenRenderTarget.getSgCameraAt(0) instanceof OrthographicCamera)) {
      paintHorizonLine(e.getGraphics2D(), this.onscreenRenderTarget, (OrthographicCamera) this.onscreenRenderTarget.getSgCameraAt(0));
    }
  }

  @Override
  public void resized(RenderTargetResizeEvent e) {
    // updateCameraMarkers();
  }

  @Override
  public void displayChanged(RenderTargetDisplayChangeEvent e) {
  }
  // ######### End implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter

  public void setHandleVisibilityForObject(TransformableImp imp, boolean b) {
    this.globalDragAdapter.setHandleShowingForSelectedImplementation(imp, b);
  }

  public AffineMatrix4x4 getTransformForNewCameraMarker() {
    return mainCameraViewTracker.getTransformForNewCameraMarker();
  }

  public AffineMatrix4x4 getTransformForNewObjectMarker() {
    EntityImp selectedImp = this.getImplementation(this.getSelectedField());
    AffineMatrix4x4 initialTransform = null;
    if (selectedImp != null) {
      initialTransform = selectedImp.getAbsoluteTransformation();
    } else {
      initialTransform = AffineMatrix4x4.createIdentity();
    }
    return initialTransform;
  }

  public Color getColorForNewObjectMarker() {
    return MarkerUtilities.getNewObjectMarkerColor();
  }

  public Color getColorForNewCameraMarker() {
    return MarkerUtilities.getNewCameraMarkerColor();
  }

  public AffineMatrix4x4 getGoodPointOfViewInSceneForObject(AxisAlignedBox box) {
    throw new RuntimeException("todo");
  }

  public MarkerImp getMarkerForField(UserField field) {
    Object obj = this.getInstanceInJavaVMForField(field);
    if (obj instanceof SMarker) {
      return EmployeesOnly.getImplementation((SMarker) obj);
    }
    return null;
  }

  public AbstractCamera getSgCameraForCreatingThumbnails() {
    if (this.sceneCameraImp != null) {
      return this.sceneCameraImp.getSgCamera();
    } else {
      return null;
    }
  }

  public void setShowSnapGrid(boolean showSnapGrid) {
    if (this.snapGrid != null) {
      this.snapGrid.setShowing(showSnapGrid);
    }
  }

  public void setSnapGridSpacing(double gridSpacing) {
    if (this.snapGrid != null) {
      this.snapGrid.setSpacing(gridSpacing);
    }
  }

  public LightweightOnscreenRenderTarget getOnscreenRenderTarget() {
    return this.onscreenRenderTarget;
  }
}
