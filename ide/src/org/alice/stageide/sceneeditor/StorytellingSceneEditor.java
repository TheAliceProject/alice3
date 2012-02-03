/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.stageide.sceneeditor;


import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import org.alice.ide.declarationseditor.type.ManagedCameraMarkerFieldState;
import org.alice.ide.declarationseditor.type.ManagedObjectMarkerFieldState;
import org.alice.ide.instancefactory.croquet.InstanceFactoryState;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.InputState;
import org.alice.interact.PickHint;
import org.alice.interact.SnapGrid;
import org.alice.interact.condition.ClickedObjectCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.manipulator.ManipulatorClickAdapter;
import org.alice.stageide.croquet.models.declaration.ObjectMarkerFieldDeclarationOperation;
import org.alice.stageide.croquet.models.sceneditor.MarkerPanelTab;
import org.alice.stageide.croquet.models.sceneditor.ObjectPropertiesTab;
import org.alice.stageide.sceneeditor.draganddrop.SceneDropSite;
import org.alice.stageide.sceneeditor.snap.SnapState;
import org.alice.stageide.sceneeditor.viewmanager.CameraMarkerTracker;
import org.alice.stageide.sceneeditor.viewmanager.CameraViewCellRenderer;
import org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities;
import org.alice.stageide.sceneeditor.viewmanager.MoveActiveCameraToMarkerActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveMarkerToActiveCameraActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveMarkerToSelectedObjectActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveSelectedObjectToMarkerActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.SceneCameraMarkerManagerPanel;
import org.alice.stageide.sceneeditor.viewmanager.SceneObjectMarkerManagerPanel;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.components.ComboBox;
import org.lgna.croquet.components.DragComponent;
import org.lgna.croquet.components.HorizontalSplitPane;
import org.lgna.croquet.components.SpringPanel.Horizontal;
import org.lgna.croquet.components.SpringPanel.Vertical;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserType;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.Entity;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Marker;
import org.lgna.story.OrthographicCameraMarker;
import org.lgna.story.PerspectiveCameraMarker;
import org.lgna.story.implementation.AbstractTransformableImp;
import org.lgna.story.implementation.CameraMarkerImp;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.MarkerImp;
import org.lgna.story.implementation.ObjectMarkerImp;
import org.lgna.story.implementation.OrthographicCameraImp;
import org.lgna.story.implementation.OrthographicCameraMarkerImp;
import org.lgna.story.implementation.PerspectiveCameraMarkerImp;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.implementation.SceneImp;
import org.lgna.story.implementation.TransformableImp;

import edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author dculyba
 * 
 */
public class StorytellingSceneEditor extends AbstractSceneEditor implements
		org.lgna.croquet.DropReceptor,
		edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener {

	private static final String SHOW_JOINTED_MODEL_VISUALIZATIONS_KEY = StorytellingSceneEditor.class.getName() + ".showJointedModelVisualizations";
	
	private static class SingletonHolder {
		private static StorytellingSceneEditor instance = new StorytellingSceneEditor();
	}
	public static StorytellingSceneEditor getInstance() {
		return SingletonHolder.instance;
	}
	private StorytellingSceneEditor() {
	}

	private static javax.swing.Icon EXPAND_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( StorytellingSceneEditor.class.getResource( "images/24/expand.png" ) );
	private static javax.swing.Icon CONTRACT_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( StorytellingSceneEditor.class.getResource( "images/24/contract.png" ) );

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			StorytellingSceneEditor.this.animator.update();
		}
	};
	
	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory
			.getInstance().createLightweightOnscreenLookingGlass();

	private class LookingGlassPanel extends
			org.lgna.croquet.components.CompassPointSpringPanel {
		@Override
		protected javax.swing.JPanel createJPanel() {
			javax.swing.JPanel rv = StorytellingSceneEditor.this.onscreenLookingGlass.getJPanel();
			rv.setLayout( new javax.swing.SpringLayout() );
			return rv;
		}
	}
	
	private org.lgna.croquet.State.ValueListener<Boolean> showSnapGridObserver = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			StorytellingSceneEditor.this.setShowSnapGrid(nextValue);	
		}
	};
	
	private org.lgna.croquet.State.ValueListener<Boolean> snapEnabledObserver = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
            if (SnapState.getInstance().isShowSnapGridEnabled())
            {
            	StorytellingSceneEditor.this.setShowSnapGrid(nextValue);
            }
        }
    };
    
    private org.lgna.croquet.State.ValueListener<Double> snapGridSpacingObserver = new org.lgna.croquet.State.ValueListener<Double>() {
		public void changing( org.lgna.croquet.State< Double > state, Double prevValue, Double nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Double > state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			StorytellingSceneEditor.this.setSnapGridSpacing(nextValue);
		}
	};
	
	private org.lgna.croquet.State.ValueListener<UserField> cameraMarkerFieldSelectionObserver = new org.lgna.croquet.State.ValueListener<UserField>() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.UserField > state, org.lgna.project.ast.UserField prevValue, org.lgna.project.ast.UserField nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.UserField > state, org.lgna.project.ast.UserField prevValue, org.lgna.project.ast.UserField nextValue, boolean isAdjusting ) {
			StorytellingSceneEditor.this.handleCameraMarkerFieldSelection( nextValue );
		}
	};
	
	private org.lgna.croquet.State.ValueListener<UserField> objectMarkerFieldSelectionObserver = new org.lgna.croquet.State.ValueListener<UserField>() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.UserField > state, org.lgna.project.ast.UserField prevValue, org.lgna.project.ast.UserField nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.UserField > state, org.lgna.project.ast.UserField prevValue, org.lgna.project.ast.UserField nextValue, boolean isAdjusting ) {
			StorytellingSceneEditor.this.handleObjectMarkerFieldSelection( nextValue );
		}
	};
	
	private org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactorySelectionObserver = new org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
		public void changing( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
			StorytellingSceneEditor.this.selectionIsFromInstanceSelector = true;
			StorytellingSceneEditor.this.setSelectedInstance(nextValue);
			StorytellingSceneEditor.this.selectionIsFromInstanceSelector = false;
		}
	};
	
	private boolean isInitialized = false;
	
	private edu.cmu.cs.dennisc.animation.ClockBasedAnimator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	private org.lgna.croquet.components.BorderPanel mainPanel = new org.lgna.croquet.components.BorderPanel();
	private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
	private SidePane sidePanel = new SidePane();
	private org.lgna.croquet.components.HorizontalSplitPane propertiesSplitPane = new HorizontalSplitPane();
	private org.alice.interact.GlobalDragAdapter globalDragAdapter;
	private org.lgna.story.implementation.SymmetricPerspectiveCameraImp sceneCameraImp;
	private org.alice.interact.CameraNavigatorWidget mainCameraNavigatorWidget = null;
	private org.lgna.croquet.components.Button expandButton;
	private org.lgna.croquet.components.Button contractButton;
	private InstanceFactorySelectionPanel instanceFactorySelectionPanel = null;
	
	private org.lgna.croquet.components.Button runButton = org.alice.stageide.croquet.models.run.RunOperation.getInstance().createButton();
	
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
	
	private org.lgna.croquet.ListSelectionState.ValueListener<View> mainCameraViewSelectionObserver = new org.lgna.croquet.ListSelectionState.ValueListener<View>() {
		public void changing( org.lgna.croquet.State< org.alice.stageide.sceneeditor.View > state, org.alice.stageide.sceneeditor.View prevValue, org.alice.stageide.sceneeditor.View nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.stageide.sceneeditor.View > state, org.alice.stageide.sceneeditor.View prevValue, org.alice.stageide.sceneeditor.View nextValue, boolean isAdjusting ) {
			StorytellingSceneEditor.this.handleMainCameraViewSelection( mainCameraViewTracker.getCameraMarker( nextValue ) );
		}
	};
	
	private ListSelectionState<View> mainCameraMarkerList = org.alice.stageide.croquet.models.sceneditor.ViewListSelectionState.getInstance();
	
	private boolean selectionIsFromInstanceSelector = false;
	private boolean selectionIsFromMain = false;
	
	protected SnapGrid snapGrid;
	
	
	@Override
	protected void setProgramInstance(UserInstance programInstance) 
	{
		super.setProgramInstance(programInstance);
		ProgramImp programImplementation = ImplementationAccessor.getImplementation(getProgramInstanceInJava());
		programImplementation.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_setClockBasedAnimator(this.animator);
		programImplementation.setOnscreenLookingGlass(this.onscreenLookingGlass);
	}
	
	private void setSelectedFieldOnManipulator(UserField field)
	{
		if (this.globalDragAdapter != null)
		{
			Entity selectedEntity = this.getInstanceInJavaVMForField(field, Entity.class);
			TransformableImp transImp = null;
			if (selectedEntity != null)
			{
				EntityImp imp = ImplementationAccessor.getImplementation(selectedEntity);
				if (imp instanceof TransformableImp)
				{
					transImp = (TransformableImp)imp;
				}
			}
			this.globalDragAdapter.setSelectedImplementation(transImp);
		}
	}
	
	private void setSelectedMethodOnManipulator(MethodInvocation method)
	{
		if (this.globalDragAdapter != null)
		{
			Entity selectedEntity = this.getInstanceInJavaVMForExpression(method, Entity.class);
			AbstractTransformableImp transImp = null;
			if (selectedEntity != null)
			{
				EntityImp imp = ImplementationAccessor.getImplementation(selectedEntity);
				if (imp instanceof AbstractTransformableImp)
				{
					transImp = (AbstractTransformableImp)imp;
				}
			}
			this.globalDragAdapter.setSelectedImplementation(transImp);
		}
	}
	
	public void setSelectedInstance(org.alice.ide.instancefactory.InstanceFactory instanceFactory) {
		org.lgna.project.ast.Expression expression = instanceFactory != null ? instanceFactory.createExpression() : null;
		if (expression instanceof FieldAccess)
		{
			FieldAccess fa = (FieldAccess)expression;
			AbstractField field = fa.field.getValue();
			if (field instanceof UserField)
			{
				UserField uf = (UserField)field;
				StorytellingSceneEditor.this.setSelectedField(uf.getDeclaringType(), uf);
			}
		}
		else if (expression instanceof MethodInvocation)
		{
			StorytellingSceneEditor.this.setSelectedMethod((MethodInvocation)expression);
			
		}
		else if (expression instanceof ThisExpression)
		{
			UserField uf = StorytellingSceneEditor.this.getActiveSceneField();
			StorytellingSceneEditor.this.setSelectedField(uf.getDeclaringType(), uf);
		}
		getPropertyPanel().setSelectedInstance(instanceFactory);
	}
	
	public void setSelectedMethod(MethodInvocation method) {
		if (!this.selectionIsFromMain)
		{
			this.selectionIsFromMain = true;
			if (this.globalDragAdapter != null)
			{
				setSelectedMethodOnManipulator(method);
			}
			this.selectionIsFromMain = false;
		}
	}
	
	@Override
	public void setSelectedField(UserType<?> declaringType, UserField field) {
		if (!this.selectionIsFromMain)
		{
			this.selectionIsFromMain = true;
			super.setSelectedField(declaringType, field);
			
			MoveSelectedObjectToMarkerActionOperation.getInstance().setSelectedField(field);
			MoveMarkerToSelectedObjectActionOperation.getInstance().setSelectedField(field);
			ObjectMarkerFieldDeclarationOperation.getInstance().setSelectedField(field);
			
			this.getCameraMarkerPanel().revalidateAndRepaint();
			this.getObjectMarkerPanel().revalidateAndRepaint();
			
			if (!this.selectionIsFromInstanceSelector)
			{
				if (field == this.getActiveSceneField() )
				{
					InstanceFactoryState.getInstance().setValueTransactionlessly(org.alice.ide.instancefactory.ThisInstanceFactory.getInstance());
				}
				else if (field != null)
				{
					InstanceFactoryState.getInstance().setValueTransactionlessly(org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance(field));
				}
			}
			if (this.globalDragAdapter != null)
			{
				setSelectedFieldOnManipulator(field);
			}
			this.selectionIsFromMain = false;
		}
	}
	
	private void initializeCameraMarkers()
	{
		PerspectiveCameraMarker openingSceneMarker = new PerspectiveCameraMarker();
		openingSceneMarker.setColorId(org.lgna.story.Color.DARK_GRAY);
		this.openingSceneMarkerImp = ImplementationAccessor.getImplementation(openingSceneMarker);
		this.openingSceneMarkerImp.setDisplayVisuals(true);
		MarkerUtilities.addIconForCameraImp(this.openingSceneMarkerImp, "mainCamera");
		MarkerUtilities.setViewForCameraImp(this.openingSceneMarkerImp, View.STARTING_CAMERA_VIEW);
		
		
		PerspectiveCameraMarker sceneViewMarker = new PerspectiveCameraMarker();
		sceneViewMarker.setColorId(org.lgna.story.Color.LIGHT_BLUE);
		this.sceneViewMarkerImp = ImplementationAccessor.getImplementation(sceneViewMarker);
		this.sceneViewMarkerImp.setDisplayVisuals(true);
		MarkerUtilities.addIconForCameraImp(this.sceneViewMarkerImp, "sceneEditorCamera");
		MarkerUtilities.setViewForCameraImp(this.sceneViewMarkerImp, View.LAYOUT_SCENE_VIEW);
		
		this.orthographicCameraMarkerImps.clear();
		OrthographicCameraMarker topOrthoMarker = new OrthographicCameraMarker();
		this.topOrthoMarkerImp = ImplementationAccessor.getImplementation(topOrthoMarker);
		MarkerUtilities.addIconForCameraImp(this.topOrthoMarkerImp, "top");
		MarkerUtilities.setViewForCameraImp(this.topOrthoMarkerImp, View.TOP);
		AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
		topTransform.translation.y = 10;
		topTransform.translation.z = -10;
		topTransform.orientation.up.set( 0, 0, 1 );
		topTransform.orientation.right.set( -1, 0, 0 );
		topTransform.orientation.backward.set( 0, 1, 0 );
		assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		this.topOrthoMarkerImp.setLocalTransformation( topTransform );
		ClippedZPlane picturePlane = new ClippedZPlane();
		picturePlane.setCenter(0, 0);
		picturePlane.setHeight(16);
		this.topOrthoMarkerImp.setPicturePlane(picturePlane);
		orthographicCameraMarkerImps.add(this.topOrthoMarkerImp);

		OrthographicCameraMarker sideOrthoMarker = new OrthographicCameraMarker();
		this.sideOrthoMarkerImp = ImplementationAccessor.getImplementation(sideOrthoMarker);
		MarkerUtilities.addIconForCameraImp(this.sideOrthoMarkerImp, "side");
		MarkerUtilities.setViewForCameraImp(this.sideOrthoMarkerImp, View.SIDE);
		AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
		sideTransform.translation.x = 10;
		sideTransform.translation.y = 1;
		sideTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()) );
		assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		this.sideOrthoMarkerImp .setLocalTransformation( sideTransform );
		picturePlane.setHeight(4);
		this.sideOrthoMarkerImp .setPicturePlane(picturePlane);
		orthographicCameraMarkerImps.add(this.sideOrthoMarkerImp);

		OrthographicCameraMarker frontOrthoMarker = new OrthographicCameraMarker();
		this.frontOrthoMarkerImp = ImplementationAccessor.getImplementation(frontOrthoMarker);
		MarkerUtilities.addIconForCameraImp(this.frontOrthoMarkerImp, "front");
		MarkerUtilities.setViewForCameraImp(this.frontOrthoMarkerImp, View.FRONT);
		AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
		frontTransform.translation.z = -10;
		frontTransform.translation.y = 1;
		frontTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessPositiveZAxis(), Vector3.accessPositiveYAxis()) );
		assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		this.frontOrthoMarkerImp.setLocalTransformation( frontTransform );
		picturePlane.setHeight(4);
		this.frontOrthoMarkerImp.setPicturePlane(picturePlane);
		orthographicCameraMarkerImps.add(this.frontOrthoMarkerImp);
		
		this.openingSceneMarkerImp.getAbstraction().setName( MarkerUtilities.getNameForCameraImp(this.openingSceneMarkerImp) );
		this.sceneViewMarkerImp.getAbstraction().setName( MarkerUtilities.getNameForCameraImp(this.sceneViewMarkerImp) );
		this.topOrthoMarkerImp.getAbstraction().setName( MarkerUtilities.getNameForCameraImp(this.topOrthoMarkerImp) );
		this.sideOrthoMarkerImp.getAbstraction().setName( MarkerUtilities.getNameForCameraImp(this.sideOrthoMarkerImp) );
		this.frontOrthoMarkerImp.getAbstraction().setName( MarkerUtilities.getNameForCameraImp(this.frontOrthoMarkerImp) );
	}
	
	private void clearCameras()
	{
		this.snapGrid.stopTrackingCameras();
		if( this.onscreenLookingGlass.getCameraCount() > 0 ) {
			onscreenLookingGlass.clearCameras();
		}
		this.mainCameraViewTracker.setCameras(null, null);
		this.globalDragAdapter.clearCameraViews();
	}
	
	private void setCameras(SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera)
	{
		this.globalDragAdapter.addCameraView( CameraView.MAIN, perspectiveCamera, orthographicCamera );
		this.globalDragAdapter.makeCameraActive( perspectiveCamera );
		this.mainCameraViewTracker.setCameras(perspectiveCamera, orthographicCamera);
		this.snapGrid.addCamera(perspectiveCamera);
		this.snapGrid.addCamera(orthographicCamera);
		this.snapGrid.setCurrentCamera(perspectiveCamera);
	}
	
	
	private void showLookingGlassPanel()
	{
		this.addComponent( this.mainPanel, Constraint.CENTER );
	}
	
	private void hideLookingGlassPanel()
	{
		this.removeComponent( this.mainPanel );
	}
	
	@Override
	protected void handleExpandContractChange(boolean isExpanded) {
		//todo
		synchronized( this.getTreeLock() ) {
			this.mainPanel.removeAllComponents();
			this.mainCameraNavigatorWidget.setExpanded(isExpanded);
			this.lookingGlassPanel.setNorthEastComponent( this.runButton );
			if (isExpanded)
			{
				this.lookingGlassPanel.setNorthWestComponent( this.instanceFactorySelectionPanel );
				this.propertiesSplitPane.setLeadingComponent(this.lookingGlassPanel);
				this.propertiesSplitPane.setTrailingComponent(this.sidePanel);
				this.mainPanel.addComponent(this.propertiesSplitPane, Constraint.CENTER);
				this.lookingGlassPanel.setSouthEastComponent(this.contractButton);

				this.lookingGlassPanel.setSouthComponent(this.mainCameraNavigatorWidget);
				
				if (this.savedSceneEditorViewSelection != null) {
					this.mainCameraMarkerList.setSelectedItem(this.savedSceneEditorViewSelection);
				}
			}
			else
			{
				this.mainPanel.addComponent(this.lookingGlassPanel, Constraint.CENTER);
				this.lookingGlassPanel.setNorthWestComponent( null );
				this.lookingGlassPanel.setSouthEastComponent(this.expandButton);
				this.lookingGlassPanel.setSouthComponent(null);
				
				
				this.savedSceneEditorViewSelection = this.mainCameraMarkerList.getSelectedItem();
				this.mainCameraMarkerList.setSelectedItem(View.STARTING_CAMERA_VIEW);
			}
			this.mainCameraViewSelector.setVisible(isExpanded);
		}
	}
	
	
	private SceneObjectPropertyManagerPanel getPropertyPanel()
	{
		return ObjectPropertiesTab.getInstance().getView();
	}
	
	private SceneCameraMarkerManagerPanel getCameraMarkerPanel()
	{
		return MarkerPanelTab.getInstance().getView().getCameraMarkerPanel();
	}
	
	private SceneObjectMarkerManagerPanel getObjectMarkerPanel()
	{
		return MarkerPanelTab.getInstance().getView().getObjectMarkerPanel();
	}
	
	private void handleCameraMarkerFieldSelection( UserField cameraMarkerField )
	{
		CameraMarkerImp newMarker = (CameraMarkerImp)this.getMarkerForField(cameraMarkerField);
		this.globalDragAdapter.setSelectedCameraMarker(newMarker);
		MoveActiveCameraToMarkerActionOperation.getInstance().setMarkerField(cameraMarkerField);
		MoveMarkerToActiveCameraActionOperation.getInstance().setMarkerField(cameraMarkerField);
		MarkerPanelTab.getInstance().getView().getCameraMarkerPanel().updateButtons();
	}
	
	private void handleObjectMarkerFieldSelection( UserField objectMarkerField )
	{
		ObjectMarkerImp newMarker = (ObjectMarkerImp)this.getMarkerForField(objectMarkerField);
		this.globalDragAdapter.setSelectedObjectMarker(newMarker);
		MoveSelectedObjectToMarkerActionOperation.getInstance().setMarkerField(objectMarkerField);
		MoveMarkerToSelectedObjectActionOperation.getInstance().setMarkerField(objectMarkerField);
		MarkerPanelTab.getInstance().getView().getObjectMarkerPanel().updateButtons();
	}
	
	private void handleManipulatorSelection(org.alice.interact.event.SelectionEvent e)
	{
		EntityImp imp = e.getTransformable();
		if (imp != null)
		{
			UserField field = this.getFieldForInstanceInJavaVM(imp.getAbstraction());
			if (field != null) {
				if (field.getValueType().isAssignableFrom(org.lgna.story.CameraMarker.class)) {
					this.setSelectedCameraMarker(field);
				}
				else if (field.getValueType().isAssignableFrom(org.lgna.story.ObjectMarker.class)) {
					this.setSelectedObjectMarker(field);
				}
				else {
					this.setSelectedField(field.getDeclaringType(), field);
				}
			}
			else if (imp == this.openingSceneMarkerImp) {
				this.setSelectedField(this.getActiveSceneType(), this.getFieldForInstanceInJavaVM(this.sceneCameraImp.getAbstraction()));
			}
		}
		else
		{
			UserField uf = StorytellingSceneEditor.this.getActiveSceneField();
			StorytellingSceneEditor.this.setSelectedField(uf.getDeclaringType(), uf);
		}
	}
	
	private void showRightClickMenuForModel(InputState clickInput)
	{
		edu.cmu.cs.dennisc.scenegraph.Element element = clickInput.getClickPickedTransformable(true);
		if (element != null)
		{
			EntityImp entityImp = EntityImp.getInstance(element);
			Entity entity = entityImp.getAbstraction();
			UserField field = this.getFieldForInstanceInJavaVM(entity);
			org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field );
			org.alice.stageide.operations.ast.oneshot.OneShotMenuModel.getInstance( instanceFactory ).getPopupPrepModel().fire( new org.lgna.croquet.triggers.InputEventTrigger( clickInput.getInputEvent() ) );
		}
	}
	
	private void handleMainCameraViewSelection( CameraMarkerImp cameraMarker )
	{
		MoveActiveCameraToMarkerActionOperation.getInstance().setCameraMarker(cameraMarker);
		MoveMarkerToActiveCameraActionOperation.getInstance().setCameraMarker(cameraMarker);
	}
	
	private void switchToCamera( AbstractCamera camera ) {
		assert camera != null;
		boolean isClearingAndAddingRequired;
		if( this.onscreenLookingGlass.getCameraCount() == 1 ) {
			if( onscreenLookingGlass.getCameraAt( 0 ) == camera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			onscreenLookingGlass.clearCameras();
			onscreenLookingGlass.addCamera( camera );
		}
		this.snapGrid.setCurrentCamera(camera);
		this.onscreenLookingGlass.repaint();
		this.revalidateAndRepaint();
	}

	public void switchToOthographicCamera() {
		switchToCamera( this.orthographicCameraImp.getSgCamera() );
		this.globalDragAdapter.makeCameraActive( this.orthographicCameraImp.getSgCamera());
		this.mainCameraNavigatorWidget.setToOrthographicMode();
	}

	public void switchToPerspectiveCamera() {
		switchToCamera( this.sceneCameraImp.getSgCamera() );
		this.globalDragAdapter.makeCameraActive( this.sceneCameraImp.getSgCamera() );
		this.mainCameraNavigatorWidget.setToPerspectiveMode();
	}
	
	@Override
	protected void initializeComponents() {
		if( this.isInitialized ) {
			//pass
		} else {
			
			this.snapGrid = new SnapGrid();
			SnapState.getInstance().getShowSnapGridState().addAndInvokeValueListener(this.showSnapGridObserver);
			SnapState.getInstance().getIsSnapEnabledState().addAndInvokeValueListener(this.snapEnabledObserver);
			SnapState.getInstance().getSnapGridSpacingState().addAndInvokeValueListener(this.snapGridSpacingObserver);
			
			
			InstanceFactoryState.getInstance().addAndInvokeValueListener(this.instanceFactorySelectionObserver);
			
			this.globalDragAdapter = new org.alice.interact.GlobalDragAdapter(this);
			this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
			this.onscreenLookingGlass.addLookingGlassListener(this);
			this.globalDragAdapter.setAnimator( animator );
			if (this.getSelectedField() != null)
			{
				setSelectedFieldOnManipulator(this.getSelectedField());
			}
			
			this.mainCameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter, CameraView.MAIN);
			
			this.expandButton = org.alice.ide.perspectives.ChangePerspectiveOperation.getInstance( org.alice.stageide.perspectives.SetupScenePerspective.getInstance() ).createButton();
			this.expandButton.setIcon( EXPAND_ICON );
			//todo: tool tip text
			//this.expandButton.getAwtComponent().setText( null );
			this.expandButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 8 ) );

			this.contractButton = org.alice.ide.perspectives.ChangePerspectiveOperation.getInstance( org.alice.stageide.perspectives.CodePerspective.getInstance() ).createButton();
			this.contractButton.setIcon( CONTRACT_ICON );
			this.contractButton.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 8 ) );
			this.instanceFactorySelectionPanel = new InstanceFactorySelectionPanel();
			
			this.propertiesSplitPane.setResizeWeight(1.0);
			
			this.orthographicCameraImp = new OrthographicCameraImp();
			this.orthographicCameraImp.getSgCamera().nearClippingPlaneDistance.setValue(.01d);
			
			initializeCameraMarkers();
			
			this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
				public void selecting( org.alice.interact.event.SelectionEvent e ) {
				}
				public void selected( org.alice.interact.event.SelectionEvent e ) {
					StorytellingSceneEditor.this.handleManipulatorSelection( e );
				}
			} );
			
			ClickedObjectCondition rightMouseAndInteractive = new ClickedObjectCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickHint.PickType.TURNABLE.pickHint() ) );
			ManipulatorClickAdapter rightClickAdapter = new ManipulatorClickAdapter() {
				public void onClick(InputState clickInput) {
					showRightClickMenuForModel(clickInput);
					
				}
			};
			this.globalDragAdapter.addClickAdapter(rightClickAdapter, rightMouseAndInteractive);
			
			org.alice.stageide.croquet.models.sceneditor.CameraMarkerFieldListSelectionState.getInstance().addAndInvokeValueListener(this.cameraMarkerFieldSelectionObserver);
			org.alice.stageide.croquet.models.sceneditor.ObjectMarkerFieldListSelectionState.getInstance().addAndInvokeValueListener(this.objectMarkerFieldSelectionObserver);
			
			this.mainCameraViewTracker = new CameraMarkerTracker(this, animator);
			
			this.mainCameraViewSelector = this.mainCameraMarkerList.getPrepModel().createComboBox();
			this.mainCameraViewSelector.setFontSize(15);
			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.STARTING_CAMERA_VIEW, this.openingSceneMarkerImp );
			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.LAYOUT_SCENE_VIEW, this.sceneViewMarkerImp );
			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.TOP, this.topOrthoMarkerImp );
			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.SIDE, this.sideOrthoMarkerImp );
			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.FRONT, this.frontOrthoMarkerImp );
			this.mainCameraViewSelector.setRenderer(new CameraViewCellRenderer(this.mainCameraViewTracker));

			this.mainCameraMarkerList.addAndInvokeValueListener(this.mainCameraViewTracker);
			this.mainCameraMarkerList.addAndInvokeValueListener(this.mainCameraViewSelectionObserver);
			
			this.lookingGlassPanel.addComponent(this.mainCameraViewSelector, Horizontal.CENTER, 0, Vertical.NORTH, 20);
			
			this.isInitialized = true;
		}
	}
	
	public void setSelectedObjectMarker( UserField objectMarkerField ) {
		ManagedObjectMarkerFieldState.getInstance((NamedUserType)getActiveSceneInstance().getType()).setSelectedItem(objectMarkerField);
	}
	
	public void setSelectedCameraMarker( UserField cameraMarkerField ) {
		ManagedCameraMarkerFieldState.getInstance((NamedUserType)getActiveSceneInstance().getType()).setSelectedItem(cameraMarkerField);
	}
	
	@Override
	public void addField(UserType<?> declaringType, UserField field, Statement... statements) {
		super.addField(declaringType, field, statements);
		if (field.getValueType().isAssignableTo(org.lgna.story.Marker.class)) {
			org.lgna.story.Marker marker = this.getInstanceInJavaVMForField(field, org.lgna.story.Marker.class);
			MarkerImp markerImp = ImplementationAccessor.getImplementation(marker);
			markerImp.setDisplayVisuals(true);
			markerImp.setShowing(true);
			
			if (field.getValueType().isAssignableTo(org.lgna.story.CameraMarker.class)) {
				this.setSelectedCameraMarker(field);
			}
			else if (field.getValueType().isAssignableTo(org.lgna.story.ObjectMarker.class)) {
				this.setSelectedObjectMarker(field);
			}
		}
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( SHOW_JOINTED_MODEL_VISUALIZATIONS_KEY ) ) {
			if( field.getValueType().isAssignableTo( org.lgna.story.JointedModel.class ) ) {
				org.lgna.story.JointedModel jointedModel = this.getInstanceInJavaVMForField( field, org.lgna.story.JointedModel.class );
				org.lgna.story.implementation.JointedModelImp jointedModelImp = ImplementationAccessor.getImplementation( jointedModel );
				jointedModelImp.opacity.setValue( 0.25f );
				jointedModelImp.showVisualization();
			}
		}
	}
	
	
	@Override
	protected void setActiveScene( org.lgna.project.ast.UserField sceneField ) {
		super.setActiveScene(sceneField);
		
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setSimulationSpeedFactor( Double.POSITIVE_INFINITY );
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setOnscreenLookingGlass(this.onscreenLookingGlass);

		org.lgna.project.virtualmachine.UserInstance sceneAliceInstance = getActiveSceneInstance();
		org.lgna.story.Scene sceneJavaInstance = (org.lgna.story.Scene)sceneAliceInstance.getJavaInstance();

		org.lgna.story.Program program = getProgramInstanceInJava();
		org.lgna.story.Scene scene = sceneAliceInstance.getJavaInstance( org.lgna.story.Scene.class );
		SceneImp ACCEPTABLE_HACK_sceneImp = ImplementationAccessor.getImplementation( scene );
		ACCEPTABLE_HACK_sceneImp.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_pushPerformMinimalInitialization();
		try {
			program.setActiveScene(sceneJavaInstance);
		} finally {
			ACCEPTABLE_HACK_sceneImp.ACCEPTABLE_HACK_FOR_SCENE_EDITOR_popPerformMinimalInitialization();
		}
		this.getVM().ENTRY_POINT_invoke( sceneAliceInstance, sceneAliceInstance.getType().getDeclaredMethod( org.alice.stageide.StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME ) );
		
		getPropertyPanel().setSceneInstance(sceneAliceInstance);
		getObjectMarkerPanel().setType(sceneAliceInstance.getType());
		getCameraMarkerPanel().setType(sceneAliceInstance.getType());
		this.instanceFactorySelectionPanel.setType( sceneAliceInstance.getType() );
		for (org.lgna.project.ast.AbstractField field : sceneField.getValueType().getDeclaredFields())
		{
			if( field.getValueType().isAssignableTo(org.lgna.story.Camera.class)) 
			{
				this.sceneCameraImp = getImplementation(field);
				break;
			}
		}
		
		assert (this.globalDragAdapter != null && this.sceneCameraImp != null && this.orthographicCameraImp != null);
		{
			this.globalDragAdapter.clearCameraViews();
			this.globalDragAdapter.addCameraView( CameraView.MAIN, this.sceneCameraImp.getSgCamera(), null );
			this.globalDragAdapter.makeCameraActive( this.sceneCameraImp.getSgCamera() );
		
			SceneImp sceneImp =  this.getActiveSceneImplementation();
			//Add and set up the snap grid (this needs to happen before setting the camera)
			sceneImp.getSgComposite().addComponent(this.snapGrid);
			this.snapGrid.setTranslationOnly(0, 0, 0, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE);
			this.snapGrid.setShowing(SnapState.getInstance().shouldShowSnapGrid());
			
			//Initialize stuff that needs a camera
			this.setCameras(this.sceneCameraImp.getSgCamera(), this.orthographicCameraImp.getSgCamera());
			MoveActiveCameraToMarkerActionOperation.getInstance().setCamera(this.sceneCameraImp);
			MoveMarkerToActiveCameraActionOperation.getInstance().setCamera(this.sceneCameraImp);
			
			//Add the orthographic camera to this scene
			sceneImp.getSgComposite().addComponent( this.orthographicCameraImp.getSgCamera().getParent() );
			//Add the orthographic markers			
			Component[] existingComponents = sceneImp.getSgComposite().getComponentsAsArray();
			for (View view : this.mainCameraMarkerList)
			{
				CameraMarkerImp marker = this.mainCameraViewTracker.getCameraMarker( view );
				boolean alreadyHasIt = false;
				for (Component c : existingComponents){
					if (c == marker.getSgComposite()) {
						alreadyHasIt = true;
						break;
					}
				}
				if (!alreadyHasIt){
					if (marker.getVehicle() != null){
						marker.setVehicle(null);
					}
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
				
			
			this.mainCameraViewTracker.startTrackingCameraView(this.mainCameraMarkerList.getSelectedItem());
				
		}
		
		ManagedCameraMarkerFieldState.getInstance((NamedUserType)sceneAliceInstance.getType()).addAndInvokeValueListener(this.cameraMarkerFieldSelectionObserver);
		ManagedObjectMarkerFieldState.getInstance((NamedUserType)sceneAliceInstance.getType()).addAndInvokeValueListener(this.objectMarkerFieldSelectionObserver);
		
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setSimulationSpeedFactor( 1.0 );
	}
	
	@Override
	public void enableRendering(
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
		if (reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN
				|| reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK) {
			this.onscreenLookingGlass.setRenderingEnabled(true);
		}
	}

	@Override
	public void disableRendering(
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
		if (reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN
				|| reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK) {
			this.onscreenLookingGlass.setRenderingEnabled(false);
		}
	}
	private void fillInAutomaticSetUpMethod( org.lgna.project.ast.StatementListProperty bodyStatementsProperty, boolean isThis, org.lgna.project.ast.AbstractField field) {
		SetUpMethodGenerator.fillInAutomaticSetUpMethod( bodyStatementsProperty, isThis, field, this.getInstanceInJavaVMForField(field), this.getActiveSceneInstance() );
	}
	
	@Override
	public void generateCodeForSetUp( StatementListProperty bodyStatementsProperty ) {
		//Set the camera to have the point of view of the opening scene marker
		AffineMatrix4x4 currentCameraTransformable = this.sceneCameraImp.getLocalTransformation();
		this.sceneCameraImp.setTransformation(this.openingSceneMarkerImp);
		
		org.lgna.project.ast.AbstractField sceneField = this.getActiveSceneField();
		this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
		for( org.lgna.project.ast.AbstractField field : this.getActiveSceneType() .getDeclaredFields() ) {
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
		}
		
		//Set the camera back to its original position
		this.sceneCameraImp.setLocalTransformation(currentCameraTransformable);
	}
	
	@Override
	public org.lgna.project.ast.Statement[] getDoStatementsForAddField(org.lgna.project.ast.UserField field, AffineMatrix4x4 initialTransform) {
		return this.getDoStatementsForAddField(field, initialTransform, null);
	}
	
	public org.lgna.project.ast.Statement[] getDoStatementsForAddField(org.lgna.project.ast.UserField field, AffineMatrix4x4 initialTransform, org.lgna.story.Paint initialPaint) {
		if (initialTransform == null && field.getValueType().isAssignableTo(org.lgna.story.Model.class))
		{
			org.lgna.project.ast.AbstractType<?,?,?> type = field.getValueType();
			JavaType javaType = type.getFirstEncounteredJavaType();
			Class<?> cls = javaType.getClassReflectionProxy().getReification();
			AxisAlignedBox box = org.lgna.story.implementation.alice.AliceResourceUtilties.getBoundingBox(cls);
			double y = box != null ? -box.getXMinimum() : 0;
			Point3 location = new Point3(0, y, 0);
			initialTransform = new AffineMatrix4x4(OrthogonalMatrix3x3.createIdentity(), location);
		}
		return SetUpMethodGenerator.getSetupStatementsForField(false, field, this.getActiveSceneInstance(), null, initialTransform, initialPaint);
	}
	
	@Override
	public org.lgna.project.ast.Statement[] getUndoStatementsForAddField(org.lgna.project.ast.UserField field) {
		java.util.List< org.lgna.project.ast.Statement > undoStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		undoStatements.add(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetVehicleStatement( field, null, false));
		
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( undoStatements, org.lgna.project.ast.Statement.class );
	}
	
	@Override
	public org.lgna.project.ast.Statement[] getDoStatementsForRemoveField(org.lgna.project.ast.UserField field) {
		java.util.List< org.lgna.project.ast.Statement > doStatements = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		doStatements.add(org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSetVehicleStatement( field, null, false));
		
		return edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray( doStatements, org.lgna.project.ast.Statement.class );
	}
	@Override
	public org.lgna.project.ast.Statement[] getUndoStatementsForRemoveField(org.lgna.project.ast.UserField field) {
		Object instance = this.getInstanceInJavaVMForField(field);
		return org.alice.stageide.sceneeditor.SetUpMethodGenerator.getSetupStatementsForInstance(false, instance, this.getActiveSceneInstance());
	}
	
	@Override
	protected void handleProjectOpened( org.lgna.project.Project nextProject ) {
		if( this.onscreenLookingGlass != null ) {
			this.onscreenLookingGlass.forgetAllCachedItems();
			edu.cmu.cs.dennisc.nebulous.Manager.unloadNebulousModelData();
		}
		super.handleProjectOpened( nextProject );
	}	
//	private boolean HACK_isDisplayableAlreadyHandled = false;
//	
	@Override
	protected void handleDisplayable() {
//		if( HACK_isDisplayableAlreadyHandled ) {
//			System.err.println( "TODO: investigate is displayed" );
//		} else {
			super.handleDisplayable();
//			HACK_isDisplayableAlreadyHandled = true;
//		}
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().addAutomaticDisplayListener( this.automaticDisplayListener );
		this.showLookingGlassPanel();
	}

	@Override
	protected void handleUndisplayable() {
		this.hideLookingGlassPanel();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().decrementAutomaticDisplayCount();
		super.handleUndisplayable();
	}
	
	private void paintHorizonLine(Graphics graphics, LightweightOnscreenLookingGlass lookingGlass, OrthographicCamera camera)
	{
		AffineMatrix4x4 cameraTransform = camera.getAbsoluteTransformation();
		double dotProd = Vector3.calculateDotProduct(cameraTransform.orientation.up, Vector3.accessPositiveYAxis());
		if (dotProd == 1 || dotProd == -1)
		{
			Dimension lookingGlassSize = lookingGlass.getSize();
			
			Point3 cameraPosition = camera.getAbsoluteTransformation().translation;
			
			ClippedZPlane dummyPlane = new ClippedZPlane(camera.picturePlane.getValue(), lookingGlass.getActualViewport(camera));
			
			double lookingGlassHeight = lookingGlassSize.getHeight();
			
			double yRatio = this.onscreenLookingGlass.getHeight() / dummyPlane.getHeight();
			double horizonInCameraSpace = 0.0d - cameraPosition.y;
			double distanceFromMaxY = dummyPlane.getYMaximum() - horizonInCameraSpace;
			int horizonLinePixelVal = (int)(yRatio * distanceFromMaxY);
			if (horizonLinePixelVal >= 0 && horizonLinePixelVal <= lookingGlassHeight)
			{
				graphics.setColor(java.awt.Color.BLACK);
				graphics.drawLine(0, horizonLinePixelVal, lookingGlassSize.width, horizonLinePixelVal);
			}
		}
	}

// ######### Begin implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter
	public void initialized(LookingGlassInitializeEvent e) {
	}

	public void cleared(LookingGlassRenderEvent e) {
	}

	public void rendered(LookingGlassRenderEvent e) {
		 if (this.onscreenLookingGlass.getCameraCount() > 0 && this.onscreenLookingGlass.getCameraAt(0) instanceof OrthographicCamera){
			 paintHorizonLine(e.getGraphics2D(), this.onscreenLookingGlass, (OrthographicCamera)this.onscreenLookingGlass.getCameraAt(0));
		 }
	}

	public void resized(LookingGlassResizeEvent e) {
		// updateCameraMarkers();
	}

	public void displayChanged(LookingGlassDisplayChangeEvent e) {
	}
// ######### End implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter

	
// ######### Begin implementation of org.lgna.croquet.DropReceptor
	public org.lgna.croquet.resolvers.Resolver<StorytellingSceneEditor> getResolver() {
		return new org.lgna.croquet.resolvers.SingletonResolver<StorytellingSceneEditor>(this);
	}

	public org.lgna.croquet.components.TrackableShape getTrackableShape(
			org.lgna.croquet.DropSite potentialDropSite) {
		return this;
	}

	public boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
		return dragModel instanceof org.alice.ide.croquet.models.gallerybrowser.GalleryDragModel;
	}

	public org.lgna.croquet.components.JComponent<?> getViewController() {
		return this;
	}

	public void dragStarted(org.lgna.croquet.history.DragStep step) {
		org.lgna.croquet.DragModel model = step.getModel();
		DragComponent dragSource = step.getDragSource();
		dragSource.showDragProxy();
		if (model instanceof org.alice.ide.croquet.models.gallerybrowser.GalleryNode) {
			org.alice.ide.croquet.models.gallerybrowser.GalleryNode galleryNode = (org.alice.ide.croquet.models.gallerybrowser.GalleryNode)model;
			System.err.println( "galleryNode.setDesiredTransformation(null);" );
			//galleryNode.setDesiredTransformation(null);
		}
	}

	public void dragEntered(org.lgna.croquet.history.DragStep dragAndDropContext) {
	}

	private boolean isDropLocationOverLookingGlass(org.lgna.croquet.history.DragStep dragAndDropContext) {
		java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		java.awt.Point pointInLookingGlass = javax.swing.SwingUtilities.convertPoint(eSource.getComponent(), eSource.getPoint(), this.lookingGlassPanel.getAwtComponent());
		return this.lookingGlassPanel.getAwtComponent().contains(pointInLookingGlass);
	}

	private boolean overLookingGlass = false;

	public org.lgna.croquet.DropSite dragUpdated(org.lgna.croquet.history.DragStep dragAndDropContext) {
		if (isDropLocationOverLookingGlass(dragAndDropContext)) {
			if (!overLookingGlass) {
				overLookingGlass = true;
				this.globalDragAdapter.dragEntered(dragAndDropContext);
			}
			this.globalDragAdapter.dragUpdated(dragAndDropContext);
		} else {
			if (overLookingGlass) {
				overLookingGlass = false;
				this.globalDragAdapter.dragExited(dragAndDropContext);
			}
		}
		return null;
	}

	public org.lgna.croquet.Model dragDropped( org.lgna.croquet.history.DragStep dragStep ) {
		if (isDropLocationOverLookingGlass(dragStep)) {
			org.lgna.croquet.DropSite dropSite = new SceneDropSite(this.globalDragAdapter.getDropTargetTransformation());
			org.lgna.croquet.Model model = dragStep.getModel().getDropModel( dragStep, dropSite );
			return model;
		}
		return null;
	}

	public void dragExited(
			org.lgna.croquet.history.DragStep dragAndDropContext,
			boolean isDropRecipient) {
	}

	public void dragStopped(org.lgna.croquet.history.DragStep dragAndDropContext) {
		 this.globalDragAdapter.dragExited(dragAndDropContext);
	}

	public String getTutorialNoteText(org.lgna.croquet.Model model,
			org.lgna.croquet.edits.Edit<?> edit,
			org.lgna.croquet.UserInformation userInformation) {
		return "Drop...";
	}
// ######### End implementation of org.lgna.croquet.DropReceptor

	
	public void setHandleVisibilityForObject( TransformableImp imp, boolean b ) {
		this.globalDragAdapter.setHandleShowingForSelectedImplementation(imp, b);
	}
	
	public AffineMatrix4x4 getTransformForNewCameraMarker() {
		AffineMatrix4x4 cameraTransform = this.sceneCameraImp.getAbsoluteTransformation();
		return cameraTransform;
	}
	
	public AffineMatrix4x4 getTransformForNewObjectMarker() {
		org.lgna.story.implementation.EntityImp selectedImp = this.getImplementation(this.getSelectedField());
		AffineMatrix4x4 initialTransform = null;
		if (selectedImp != null)
		{
			initialTransform = selectedImp.getAbsoluteTransformation();
		}
		else
		{
			initialTransform = AffineMatrix4x4.createIdentity();
		}
		return initialTransform;
	}
	
	public String getSuggestedNameForNewObjectMarker(org.lgna.story.Color color) {
		return MarkerUtilities.getNameForObjectMarker( this.getActiveSceneType(), this.getSelectedField(), color );
	}
	
	public String getSuggestedNameForNewCameraMarker(org.lgna.story.Color color) {
		return MarkerUtilities.getNameForCameraMarker( this.getActiveSceneType(), color );
	}
	
	public org.lgna.story.Color getColorForNewObjectMarker() {
		return MarkerUtilities.getNewObjectMarkerColor();
	}
	
	public org.lgna.story.Color getColorForNewCameraMarker() {
		return MarkerUtilities.getNewCameraMarkerColor();
	}
	
	public AffineMatrix4x4 getGoodPointOfViewInSceneForObject( AxisAlignedBox box ) {
		throw new RuntimeException( "todo" );
	}
	public MarkerImp getMarkerForField( UserField field ) {
		Object obj = this.getInstanceInJavaVMForField(field);
		if (obj instanceof Marker)
		{
			return ImplementationAccessor.getImplementation((Marker)obj);
		}
		return null;
	}
	public AbstractCamera getSgCameraForCreatingThumbnails() {
		if( this.sceneCameraImp != null ) {
			return this.sceneCameraImp.getSgCamera();
		} else {
			return null;
		}
	}
	
	public void setShowSnapGrid( boolean showSnapGrid ) {
		if (this.snapGrid != null)
		{
			this.snapGrid.setShowing(showSnapGrid);
		}
	}
	public void setSnapGridSpacing( double gridSpacing ) {
		if (this.snapGrid != null)
		{
			this.snapGrid.setSpacing(gridSpacing);
		}
	}
}
