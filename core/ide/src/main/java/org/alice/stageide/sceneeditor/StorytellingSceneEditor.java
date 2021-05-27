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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.alice.stageide.oneshot.OneShotMenuModel;
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
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractStatementWithBody;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.ArrayAccess;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.DoInOrder;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.Method;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.CameraMarker;
import org.lgna.story.Color;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.OrthographicCameraMarker;
import org.lgna.story.PerspectiveCameraMarker;
import org.lgna.story.Rider;
import org.lgna.story.SCamera;
import org.lgna.story.SCameraMarker;
import org.lgna.story.SJointedModel;
import org.lgna.story.SMarker;
import org.lgna.story.SModel;
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.SThingMarker;
import org.lgna.story.SVRHand;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraMarkerImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.MarkerImp;
import org.lgna.story.implementation.ModelImp;
import org.lgna.story.implementation.ObjectMarkerImp;
import org.lgna.story.implementation.OrthographicCameraImp;
import org.lgna.story.implementation.OrthographicCameraMarkerImp;
import org.lgna.story.implementation.PerspectiveCameraMarkerImp;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.implementation.SymmetricPerspectiveCameraImp;
import org.lgna.story.implementation.TransformableImp;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
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
 *
 */
public class StorytellingSceneEditor extends AbstractSceneEditor implements RenderTargetListener {

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

  private boolean isInitialized = false;

  private ClockBasedAnimator animator = new ClockBasedAnimator();
  private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
  private GlobalDragAdapter globalDragAdapter;
  private SymmetricPerspectiveCameraImp sceneCameraImp;
  private CameraNavigatorWidget mainCameraNavigatorWidget = null;
  private Button expandButton;
  private Button contractButton;
  private InstanceFactorySelectionPanel instanceFactorySelectionPanel = null;

  private final Button runButton = IsToolBarShowing.getValue() ? null : RunComposite.getInstance().getLaunchOperation().createButton();

  private OrthographicCameraImp orthographicCameraImp = null;
  private OrthographicCameraMarkerImp topOrthoMarkerImp = null;
  private OrthographicCameraMarkerImp frontOrthoMarkerImp = null;
  private OrthographicCameraMarkerImp sideOrthoMarkerImp = null;
  private List<OrthographicCameraMarkerImp> orthographicCameraMarkerImps = new LinkedList<OrthographicCameraMarkerImp>();
  private PerspectiveCameraMarkerImp openingSceneMarkerImp;
  private PerspectiveCameraMarkerImp sceneViewMarkerImp;

  private ComboBox<View> mainCameraViewSelector;
  private CameraMarkerTracker mainCameraViewTracker;
  private View savedSceneEditorViewSelection = null;

  private ValueListener<View> mainCameraViewSelectionObserver = new ValueListener<View>() {
    @Override
    public void valueChanged(ValueEvent<View> e) {
      StorytellingSceneEditor.this.handleMainCameraViewSelection(mainCameraViewTracker.getCameraMarker(e.getNextValue()));
    }
  };

  private ImmutableDataSingleSelectListState<View> mainCameraMarkerList = ViewListSelectionState.getInstance();

  private boolean selectionIsFromInstanceSelector = false;
  private boolean selectionIsFromMain = false;

  protected SnapGrid snapGrid;

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
        setSelectedField(null, null);
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

  private void initializeCameraMarkers() {
    PerspectiveCameraMarker openingSceneMarker = new PerspectiveCameraMarker();
    openingSceneMarker.setColorId(Color.DARK_GRAY);
    this.openingSceneMarkerImp = EmployeesOnly.getImplementation(openingSceneMarker);
    this.openingSceneMarkerImp.setDisplayVisuals(true);
    MarkerUtilities.addIconForCameraImp(this.openingSceneMarkerImp, "mainCamera");
    MarkerUtilities.setViewForCameraImp(this.openingSceneMarkerImp, View.STARTING_CAMERA_VIEW);

    PerspectiveCameraMarker sceneViewMarker = new PerspectiveCameraMarker();
    sceneViewMarker.setColorId(Color.LIGHT_BLUE);
    this.sceneViewMarkerImp = EmployeesOnly.getImplementation(sceneViewMarker);
    this.sceneViewMarkerImp.setDisplayVisuals(true);
    MarkerUtilities.addIconForCameraImp(this.sceneViewMarkerImp, "sceneEditorCamera");
    MarkerUtilities.setViewForCameraImp(this.sceneViewMarkerImp, View.LAYOUT_SCENE_VIEW);

    this.orthographicCameraMarkerImps.clear();
    OrthographicCameraMarker topOrthoMarker = new OrthographicCameraMarker();
    this.topOrthoMarkerImp = EmployeesOnly.getImplementation(topOrthoMarker);
    MarkerUtilities.addIconForCameraImp(this.topOrthoMarkerImp, "top");
    MarkerUtilities.setViewForCameraImp(this.topOrthoMarkerImp, View.TOP);
    AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
    topTransform.translation.y = 10;
    topTransform.translation.z = -10;
    topTransform.orientation.up.set(0, 0, 1);
    topTransform.orientation.right.set(-1, 0, 0);
    topTransform.orientation.backward.set(0, 1, 0);
    assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
    this.topOrthoMarkerImp.setLocalTransformation(topTransform);
    ClippedZPlane picturePlane = new ClippedZPlane();
    picturePlane.setCenter(0, 0);
    picturePlane.setHeight(16);
    this.topOrthoMarkerImp.setPicturePlane(picturePlane);
    orthographicCameraMarkerImps.add(this.topOrthoMarkerImp);

    OrthographicCameraMarker sideOrthoMarker = new OrthographicCameraMarker();
    this.sideOrthoMarkerImp = EmployeesOnly.getImplementation(sideOrthoMarker);
    MarkerUtilities.addIconForCameraImp(this.sideOrthoMarkerImp, "side");
    MarkerUtilities.setViewForCameraImp(this.sideOrthoMarkerImp, View.SIDE);
    AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
    sideTransform.translation.x = 10;
    sideTransform.translation.y = 1;
    sideTransform.orientation.setValue(new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()));
    assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
    this.sideOrthoMarkerImp.setLocalTransformation(sideTransform);
    picturePlane.setHeight(4);
    this.sideOrthoMarkerImp.setPicturePlane(picturePlane);
    orthographicCameraMarkerImps.add(this.sideOrthoMarkerImp);

    OrthographicCameraMarker frontOrthoMarker = new OrthographicCameraMarker();
    this.frontOrthoMarkerImp = EmployeesOnly.getImplementation(frontOrthoMarker);
    MarkerUtilities.addIconForCameraImp(this.frontOrthoMarkerImp, "front");
    MarkerUtilities.setViewForCameraImp(this.frontOrthoMarkerImp, View.FRONT);
    AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
    frontTransform.translation.z = -10;
    frontTransform.translation.y = 1;
    frontTransform.orientation.setValue(new ForwardAndUpGuide(Vector3.accessPositiveZAxis(), Vector3.accessPositiveYAxis()));
    assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
    this.frontOrthoMarkerImp.setLocalTransformation(frontTransform);
    picturePlane.setHeight(4);
    this.frontOrthoMarkerImp.setPicturePlane(picturePlane);
    orthographicCameraMarkerImps.add(this.frontOrthoMarkerImp);

    this.openingSceneMarkerImp.getAbstraction().setName(MarkerUtilities.getNameForCameraImp(this.openingSceneMarkerImp));
    this.sceneViewMarkerImp.getAbstraction().setName(MarkerUtilities.getNameForCameraImp(this.sceneViewMarkerImp));
    this.topOrthoMarkerImp.getAbstraction().setName(MarkerUtilities.getNameForCameraImp(this.topOrthoMarkerImp));
    this.sideOrthoMarkerImp.getAbstraction().setName(MarkerUtilities.getNameForCameraImp(this.sideOrthoMarkerImp));
    this.frontOrthoMarkerImp.getAbstraction().setName(MarkerUtilities.getNameForCameraImp(this.frontOrthoMarkerImp));
  }

  private void clearCameras() {
    this.snapGrid.stopTrackingCameras();
    if (this.onscreenRenderTarget.getSgCameraCount() > 0) {
      onscreenRenderTarget.clearSgCameras();
    }
    this.mainCameraViewTracker.setCameras(null, null);
    this.globalDragAdapter.clearCameraViews();
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
        this.mainCameraMarkerList.setValueTransactionlessly(View.STARTING_CAMERA_VIEW);
      }
      this.mainCameraViewSelector.setVisible(isExpanded);
    }
  }

  private SceneObjectPropertyManagerPanel getPropertyPanel() {
    return SideComposite.getInstance().getObjectPropertiesTab().getView();
  }

  //  private SceneCameraMarkerManagerPanel getCameraMarkerPanel()
  //  {
  //    return SideComposite.getInstance().getMarkerTab().getView().getCameraMarkerPanel();
  //  }
  //
  //  private SceneObjectMarkerManagerPanel getObjectMarkerPanel()
  //  {
  //    return SideComposite.getInstance().getMarkerTab().getView().getObjectMarkerPanel();
  //  }

  private void handleCameraMarkerFieldSelection(UserField cameraMarkerField) {
    CameraMarkerImp newMarker = (CameraMarkerImp) this.getMarkerForField(cameraMarkerField);
    this.globalDragAdapter.setSelectedCameraMarker(newMarker);
    MoveActiveCameraToMarkerActionOperation.getInstance().setMarkerField(cameraMarkerField);
    MoveMarkerToActiveCameraActionOperation.getInstance().setMarkerField(cameraMarkerField);
    //    this.getCameraMarkerPanel().updateButtons();
  }

  private void handleObjectMarkerFieldSelection(UserField objectMarkerField) {
    ObjectMarkerImp newMarker = (ObjectMarkerImp) this.getMarkerForField(objectMarkerField);
    this.globalDragAdapter.setSelectedObjectMarker(newMarker);
    MoveSelectedObjectToMarkerActionOperation.getInstance().setMarkerField(objectMarkerField);
    MoveMarkerToSelectedObjectActionOperation.getInstance().setMarkerField(objectMarkerField);
    //    this.getObjectMarkerPanel().updateButtons();
  }

  public void setSelectedObjectMarker(UserField objectMarkerField) {
    RefreshableDataSingleSelectListState<UserField> markerList = SideComposite.getInstance().getObjectMarkersTab().getMarkerListState();
    markerList.setSelectedIndex(markerList.indexOf(objectMarkerField));
  }

  public void setSelectedCameraMarker(UserField cameraMarkerField) {
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
      } else if (imp == this.openingSceneMarkerImp) {
        this.setSelectedField(this.getActiveSceneType(), this.getFieldForInstanceInJavaVM(this.sceneCameraImp.getAbstraction()));
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
        OneShotMenuModel.getInstance(instanceFactory).getPopupPrepModel().fire(InputEventTrigger.createUserActivity(clickInput.getInputEvent()));
      } else {
        Logger.severe(entityImp);
      }
    }
  }

  private void handleMainCameraViewSelection(CameraMarkerImp cameraMarker) {
    MoveActiveCameraToMarkerActionOperation.getInstance().setCameraMarker(cameraMarker);
    MoveMarkerToActiveCameraActionOperation.getInstance().setCameraMarker(cameraMarker);
  }

  private void switchToCamera(AbstractCamera camera) {
    assert camera != null;
    boolean isClearingAndAddingRequired;
    if (this.onscreenRenderTarget.getSgCameraCount() == 1) {
      if (onscreenRenderTarget.getSgCameraAt(0) == camera) {
        isClearingAndAddingRequired = false;
      } else {
        isClearingAndAddingRequired = true;
      }
    } else {
      isClearingAndAddingRequired = true;
    }
    if (isClearingAndAddingRequired) {
      onscreenRenderTarget.clearSgCameras();
      onscreenRenderTarget.addSgCamera(camera);
    }
    this.snapGrid.setCurrentCamera(camera);
    this.onscreenRenderTarget.repaint();
    this.revalidateAndRepaint();
  }

  public void switchToOthographicCamera() {
    switchToCamera(this.orthographicCameraImp.getSgCamera());
    this.globalDragAdapter.makeCameraActive(this.orthographicCameraImp.getSgCamera());
    this.mainCameraNavigatorWidget.setToOrthographicMode();
  }

  public void switchToPerspectiveCamera() {
    switchToCamera(this.sceneCameraImp.getSgCamera());
    this.globalDragAdapter.makeCameraActive(this.sceneCameraImp.getSgCamera());
    this.mainCameraNavigatorWidget.setToPerspectiveMode();
  }

  @Override
  protected void initializeComponents() {
    if (this.isInitialized) {
      //pass
    } else {

      this.snapGrid = new SnapGrid();
      SnapState.getInstance().getShowSnapGridState().addAndInvokeNewSchoolValueListener(this.showSnapGridListener);
      SnapState.getInstance().getIsSnapEnabledState().addAndInvokeNewSchoolValueListener(this.snapEnabledListener);
      SnapState.getInstance().getSnapGridSpacingState().addAndInvokeNewSchoolValueListener(this.snapGridSpacingListener);

      IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().addAndInvokeNewSchoolValueListener(this.instanceFactorySelectionListener);

      this.globalDragAdapter = new GlobalDragAdapter(this);
      this.globalDragAdapter.setOnscreenRenderTarget(onscreenRenderTarget);
      this.onscreenRenderTarget.addRenderTargetListener(this);
      this.globalDragAdapter.setAnimator(animator);
      if (this.getSelectedField() != null) {
        setSelectedFieldOnManipulator(this.getSelectedField());
      }

      this.mainCameraNavigatorWidget = new CameraNavigatorWidget(this.globalDragAdapter, CameraView.MAIN);

      IDE ide = IDE.getActiveInstance();
      this.expandButton = ide.getDocumentFrame().getSetToSetupScenePerspectiveOperation().createButton();
      this.expandButton.setClobberIcon(EXPAND_ICON);
      //todo: tool tip text
      //this.expandButton.getAwtComponent().setText( null );
      this.expandButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

      this.contractButton = ide.getDocumentFrame().getSetToCodePerspectiveOperation().createButton();
      this.contractButton.setClobberIcon(CONTRACT_ICON);
      this.contractButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
      this.instanceFactorySelectionPanel = new InstanceFactorySelectionPanel();

      this.orthographicCameraImp = new OrthographicCameraImp();
      this.orthographicCameraImp.getSgCamera().nearClippingPlaneDistance.setValue(.01d);

      initializeCameraMarkers();

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
      //      org.alice.stageide.croquet.models.sceneditor.CameraMarkerFieldListSelectionState.getInstance().addAndInvokeValueListener( this.cameraMarkerFieldSelectionObserver );
      //      org.alice.stageide.croquet.models.sceneditor.ObjectMarkerFieldListSelectionState.getInstance().addAndInvokeValueListener( this.objectMarkerFieldSelectionObserver );

      this.mainCameraViewTracker = new CameraMarkerTracker(this, animator);

      this.mainCameraViewSelector = this.mainCameraMarkerList.getPrepModel().createComboBox();
      this.mainCameraViewSelector.setRenderer(new CameraViewCellRenderer(this.mainCameraViewTracker));
      this.mainCameraViewSelector.setFontSize(15);
      this.mainCameraViewTracker.mapViewToMarkerAndViceVersa(View.STARTING_CAMERA_VIEW, this.openingSceneMarkerImp);
      this.mainCameraViewTracker.mapViewToMarkerAndViceVersa(View.LAYOUT_SCENE_VIEW, this.sceneViewMarkerImp);
      this.mainCameraViewTracker.mapViewToMarkerAndViceVersa(View.TOP, this.topOrthoMarkerImp);
      this.mainCameraViewTracker.mapViewToMarkerAndViceVersa(View.SIDE, this.sideOrthoMarkerImp);
      this.mainCameraViewTracker.mapViewToMarkerAndViceVersa(View.FRONT, this.frontOrthoMarkerImp);

      this.mainCameraMarkerList.addAndInvokeNewSchoolValueListener(this.mainCameraViewTracker);
      this.mainCameraMarkerList.addAndInvokeNewSchoolValueListener(this.mainCameraViewSelectionObserver);

      this.lookingGlassPanel.addComponent(this.mainCameraViewSelector, Horizontal.CENTER, 0, Vertical.NORTH, 20);

      this.isInitialized = true;
    }
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
        //        modelImp.opacity.setValue( 0.25f );
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
      this.getVirtualMachine().ENTRY_POINT_invoke(sceneAliceInstance, sceneAliceInstance.getType().getDeclaredMethod(StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME));

      getPropertyPanel().setSceneInstance(sceneAliceInstance);

      //      getObjectMarkerPanel().setType( sceneAliceInstance.getType() );
      //      getCameraMarkerPanel().setType( sceneAliceInstance.getType() );
      this.instanceFactorySelectionPanel.setType(sceneAliceInstance.getType());
      for (AbstractField field : sceneField.getValueType().getDeclaredFields()) {
        if (field.getValueType().isAssignableTo(SCamera.class)) {
          this.sceneCameraImp = getImplementation(field);
          break;
        }
      }

      assert ((this.globalDragAdapter != null) && (this.sceneCameraImp != null) && (this.orthographicCameraImp != null));
      this.globalDragAdapter.clearCameraViews();
      this.globalDragAdapter.addCameraView(CameraView.MAIN, this.sceneCameraImp.getSgCamera(), null);
      this.globalDragAdapter.makeCameraActive(this.sceneCameraImp.getSgCamera());

      SceneImp sceneImp = this.getActiveSceneImplementation();
      //sceneImp.mendSceneGraphIfNecessary();
      //Add and set up the snap grid (this needs to happen before setting the camera)
      sceneImp.getSgComposite().addComponent(this.snapGrid);
      this.snapGrid.setTranslationOnly(0, 0, 0, AsSeenBy.SCENE);
      this.snapGrid.setShowing(SnapState.getInstance().shouldShowSnapGrid());

      //Initialize stuff that needs a camera
      this.setCameras(this.sceneCameraImp.getSgCamera(), this.orthographicCameraImp.getSgCamera());
      MoveActiveCameraToMarkerActionOperation.getInstance().setCamera(this.sceneCameraImp);
      MoveMarkerToActiveCameraActionOperation.getInstance().setCamera(this.sceneCameraImp);

      //Add the orthographic camera to this scene
      sceneImp.getSgComposite().addComponent(this.orthographicCameraImp.getSgCamera().getParent());
      //Add the orthographic markers
      Component[] existingComponents = sceneImp.getSgComposite().getComponentsAsArray();
      for (View view : this.mainCameraMarkerList) {
        CameraMarkerImp marker = this.mainCameraViewTracker.getCameraMarker(view);
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

      AffineMatrix4x4 openingViewTransform = this.sceneCameraImp.getAbsoluteTransformation();
      this.openingSceneMarkerImp.setLocalTransformation(openingViewTransform);

      AffineMatrix4x4 sceneEditorViewTransform = new AffineMatrix4x4(openingViewTransform);
      sceneEditorViewTransform.applyTranslationAlongYAxis(12.0);
      sceneEditorViewTransform.applyTranslationAlongZAxis(10.0);
      sceneEditorViewTransform.applyRotationAboutXAxis(new AngleInDegrees(-40));
      this.sceneViewMarkerImp.setLocalTransformation(sceneEditorViewTransform);

      this.mainCameraViewTracker.startTrackingCameraView(this.mainCameraMarkerList.getValue());

      //TODO: do we need to do anything to handle marker selection on scene change?
      //Yes, we need to clear the selection on the markers so that no marker is selected on load and so no marker selection is carried over from the previous world
      this.setSelectedCameraMarker(null);
      this.setSelectedObjectMarker(null);
      //      MoveActiveCameraToMarkerActionOperation.getInstance().setMarkerField( null );
      //      MoveMarkerToActiveCameraActionOperation.getInstance().setMarkerField( null );

      //      SideComposite.getInstance().getCameraMarkersTab().getMarkerListState().addAndInvokeValueListener( this.cameraMarkerFieldSelectionObserver );
      //      SideComposite.getInstance().getObjectMarkersTab().getMarkerListState().addAndInvokeValueListener( this.objectMarkerFieldSelectionObserver );
      //      ManagedCameraMarkerFieldState.getInstance( (NamedUserType)sceneAliceInstance.getType() ).addAndInvokeValueListener( this.cameraMarkerFieldSelectionObserver );
      //      ManagedObjectMarkerFieldState.getInstance( (NamedUserType)sceneAliceInstance.getType() ).addAndInvokeValueListener( this.objectMarkerFieldSelectionObserver );

      for (AbstractField field : sceneField.getValueType().getDeclaredFields()) {
        //Turn markers on so they're visible in the scene editor (note: markers are hidden by default so that when a world runs they aren't scene. we have to manually make them visible to see them in the scene editor)
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
    if ((fieldImp == this.sceneCameraImp) && (this.mainCameraMarkerList.getValue() != View.STARTING_CAMERA_VIEW)) {
      AffineMatrix4x4 revertedTransform = fieldImp.getAbsoluteTransformation();
      this.openingSceneMarkerImp.setTransformation(this.openingSceneMarkerImp.getScene(), revertedTransform);
      this.sceneCameraImp.setTransformation(this.sceneCameraImp.getScene(), originalTransform);
    }
  }

  @Override
  public Statement getCurrentStateCodeForField(UserField field) {
    Statement rv = null;

    BlockStatement bs = new BlockStatement();

    AffineMatrix4x4 currentCameraTransformable = this.sceneCameraImp.getLocalTransformation();
    if (this.sceneCameraImp.getVehicle() != null) {
      this.sceneCameraImp.setTransformation(this.openingSceneMarkerImp);
    } else {
      Logger.severe(this.sceneCameraImp);
    }
    this.fillInAutomaticSetUpMethod(bs.statements, false, field, true);
    if (this.sceneCameraImp.getVehicle() != null) {
      this.sceneCameraImp.setLocalTransformation(currentCameraTransformable);
      sceneCameraImp.applyAnimation();
    }
    Statement setVehicleStatement = null;
    for (Statement statement : bs.statements.getValue()) {
      if (statement instanceof ExpressionStatement) {
        Expression expression = ((ExpressionStatement) statement).expression.getValue();
        if (expression instanceof MethodInvocation) {
          MethodInvocation mi = (MethodInvocation) expression;
          Method method = mi.method.getValue();
          if (method.getName().equalsIgnoreCase("setVehicle")) {
            setVehicleStatement = statement;
            break;
          }
        }
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
    AffineMatrix4x4 currentCameraTransformable = this.sceneCameraImp.getLocalTransformation();
    this.sceneCameraImp.setTransformation(this.openingSceneMarkerImp);

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
    this.sceneCameraImp.setLocalTransformation(currentCameraTransformable);
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

    //Remove the setVehicle and setTransform statements from the setup code so we can replace them with custom ones based on the fieldToCopy's vehicle and the initial transform
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

    undoStatements.add(SetUpMethodGenerator.createSetVehicleStatement(field, null, false));

    return ArrayUtilities.createArray(undoStatements, Statement.class);
  }

  @Override
  public Statement[] getDoStatementsForRemoveField(UserField field) {
    List<Statement> doStatements = Lists.newLinkedList();

    doStatements.add(SetUpMethodGenerator.createSetVehicleStatement(field, null, false));

    return ArrayUtilities.createArray(doStatements, Statement.class);
  }

  @Override
  public Statement[] getUndoStatementsForRemoveField(UserField field) {
    Object instance = this.getInstanceInJavaVMForField(field);
    AbstractField vehicleField = null;
    if (instance instanceof Rider) {
      SThing vehicleInstance = ((Rider) instance).getVehicle();
      vehicleField = this.getFieldForInstanceInJavaVM(vehicleInstance);
    }
    Statement[] setupStatements = SetUpMethodGenerator.getSetupStatementsForInstance(false, instance, this.getActiveSceneInstance(), false);
    Statement vehicleStatement = SetUpMethodGenerator.createSetVehicleStatement(field, vehicleField, false);
    Statement[] statements = new Statement[setupStatements.length + 1];
    statements[0] = vehicleStatement;
    System.arraycopy(setupStatements, 0, statements, 1, setupStatements.length);
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
    AffineMatrix4x4 cameraTransform = this.sceneCameraImp.getAbsoluteTransformation();
    return cameraTransform;
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
