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

package org.alice.interact;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.ObjectMarker;
import org.alice.apis.moveandturn.PerspectiveCameraMarker;
import org.alice.ide.ProjectApplication;
import org.alice.interact.condition.InputCondition;
import org.alice.interact.condition.ManipulatorConditionSet;
import org.alice.interact.event.ManipulationEvent;
import org.alice.interact.event.ManipulationEventManager;
import org.alice.interact.event.ManipulationListener;
import org.alice.interact.event.SelectionEvent;
import org.alice.interact.event.SelectionListener;
import org.alice.interact.handle.HandleManager;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.handle.ManipulationHandle;
import org.alice.interact.manipulator.AbstractManipulator;
import org.alice.interact.manipulator.AnimatorDependentManipulator;
import org.alice.interact.manipulator.Camera2DDragManipulator;
import org.alice.interact.manipulator.CameraInformedManipulator;
import org.alice.interact.manipulator.CameraOrbitDragManipulator;
import org.alice.interact.manipulator.ClickAdapterManipulator;
import org.alice.interact.manipulator.ManipulatorClickAdapter;
import org.alice.interact.manipulator.OnScreenLookingGlassInformedManipulator;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;
import org.alice.stageide.sceneeditor.snap.SnapState;
import org.lgna.croquet.components.DragComponent;

import edu.cmu.cs.dennisc.croquet.ListSelectionState;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.history.HistoryManager;
import edu.cmu.cs.dennisc.lookingglass.PickResult;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

/**
 * @author David Culyba
 */
public abstract class AbstractDragAdapter implements java.awt.event.MouseWheelListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener, java.awt.event.KeyListener {
	
	public enum CameraView
	{
		MAIN,
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT,
		ACTIVE_VIEW,
		PICK_CAMERA
	}
	
	private class CameraPair
	{
		public SymmetricPerspectiveCamera perspectiveCamera;
		public OrthographicCamera orthographicCamera;
		
		private AbstractCamera activeCamera;
		
		public CameraPair(SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera)
		{
			this.perspectiveCamera = perspectiveCamera;
			this.orthographicCamera = orthographicCamera;
		}
		
		public void setActiveCamera(AbstractCamera camera)
		{
			this.activeCamera = camera;
		}
		
		public AbstractCamera getActiveCamera()
		{
			return this.activeCamera;
		}
		
		public boolean hasCamera(AbstractCamera camera)
		{
			return this.perspectiveCamera == camera || this.orthographicCamera == camera;
		}
	}
	
	public ListSelectionState.ValueObserver<org.alice.stageide.sceneeditor.HandleStyle> handleStateValueObserver = new ListSelectionState.ValueObserver<org.alice.stageide.sceneeditor.HandleStyle>() {
		public void changing( org.alice.stageide.sceneeditor.HandleStyle prevValue, org.alice.stageide.sceneeditor.HandleStyle nextValue ) {
		}
		public void changed( org.alice.stageide.sceneeditor.HandleStyle prevValue, org.alice.stageide.sceneeditor.HandleStyle nextValue ) {
			PrintUtilities.println( nextValue );
			AbstractDragAdapter.this.setInteractionState( nextValue );
		}
	};
	
	private AbsoluteTransformationListener cameraTransformationListener = new AbsoluteTransformationListener() {
		
		public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent ) 
		{
			if (absoluteTransformationEvent.getSource() instanceof SymmetricPerspectiveCamera)
			{
				SymmetricPerspectiveCamera camera = (SymmetricPerspectiveCamera)absoluteTransformationEvent.getSource();
				AbstractDragAdapter.this.handleManager.updateCameraPosition(camera.getAbsoluteTransformation().translation);
			}
		}
	};
	
	private static double MOUSE_WHEEL_TIMEOUT_TIME = 1.0;
	private static double CANCEL_MOUSE_WHEEL_DISTANCE = 3;
	
	protected double mouseWheelTimeoutTime = 0;
	protected Point mouseWheelStartLocation = null;
	
	protected MoveAndTurnSceneEditor sceneEditor;
	
	private Map<CameraView, CameraPair> cameraMap = new HashMap<CameraView, CameraPair>();
	
	public static final String BOUNDING_BOX_KEY = "BOUNDING_BOX_KEY";
	
	protected java.util.Vector< ManipulatorConditionSet > manipulators = new java.util.Vector< ManipulatorConditionSet >();

	protected edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
	protected edu.cmu.cs.dennisc.animation.Animator animator;
	private java.awt.Component lookingGlassComponent = null;
	
	protected InputState currentInputState = new InputState();
	protected InputState previousInputState = new InputState(); 
	private double timePrev = Double.NaN;
	private boolean hasSetCameraTransformables = false;
	
	private boolean isInStageChange = false;
	private Transformable toBeSelected = null;
	private boolean hasObjectToBeSelected = false;
	
	private Component currentRolloverComponent = null;
	
	
	protected HandleManager handleManager = new HandleManager();
	protected InteractionGroup currentInteractionState = null;
	protected ManipulationEventManager manipulationEventManager = new ManipulationEventManager();
	
	protected SnapState snapState = null;
	
	private Transformable selectedObject = null;
	private CameraMarker selectedCameraMarker = null;
	private ObjectMarker selectedObjectMarker = null;

	protected java.util.Map< org.alice.stageide.sceneeditor.HandleStyle, InteractionGroup > mapHandleStyleToInteractionGroup = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	private List< SelectionListener > selectionListeners = new java.util.LinkedList< SelectionListener >(); 
	public void addPropertyListener( SelectionListener selectionListener ) {
		synchronized( this.selectionListeners ) {
			this.selectionListeners.add( selectionListener );
		}
	}
	public void removeSelectionListener( SelectionListener selectionListener ) {
		synchronized( this.selectionListeners ) {
			this.selectionListeners.remove( selectionListener );
		}
	}
	public Iterable< SelectionListener > getSelectionListeners() {
		return this.selectionListeners;
	}

	private void fireSelecting( SelectionEvent e ) {
		synchronized( this.selectionListeners ) {
			for( SelectionListener selectionListener : this.selectionListeners ) {
				selectionListener.selecting( e );
			}
		}
	}
	private void fireSelected( SelectionEvent e ) {
		synchronized( this.selectionListeners ) {
			for( SelectionListener selectionListener : this.selectionListeners ) {
				selectionListener.selected( e );
			}
		}
	}
	
	public AbstractDragAdapter()
	{
		this(null);
	}	
	
	public AbstractDragAdapter( MoveAndTurnSceneEditor sceneEditor )
	{
		this.sceneEditor = sceneEditor;
		this.setUpControls();
	}	
	
	protected void preUndo()
	{
		stopMouseWheel();
		handleStateChange();
	}
	
	protected abstract void setUpControls();
	
	protected void handleCameraMoved()
	{
		
	}
	
	public boolean hasSceneEditor()
	{
		return this.sceneEditor != null;
	}
	
//	public abstract ListSelectionState<org.alice.stageide.sceneeditor.HandleStyle> getInteractionSelectionStateList();
	
	public void triggerManipulationEvent( ManipulationEvent event, boolean isActivate )
	{
		event.setInputState( this.currentInputState );
		this.manipulationEventManager.triggerEvent( event, isActivate );
	}
	
	protected java.awt.Component getAWTComponentToAddListenersTo( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		if( onscreenLookingGlass != null ) {
			return onscreenLookingGlass.getAWTComponent();
		} else {
			return null;
		}
	}
	
	public AffineMatrix4x4 getDropTargetTransformation()
	{
		return null;
	}
	
	public boolean isInStateChange()
	{
		return this.isInStageChange;
	}
	
	public void setToBeSelected(Transformable toBeSelected)
	{
		this.toBeSelected = toBeSelected;
		this.hasObjectToBeSelected = true;
	}
	
	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}
	
	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayAdapter = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			AbstractDragAdapter.this.handleDisplayed();
		}
	};
	
	public void setOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass ) {
		if( this.onscreenLookingGlass != null ) {
			this.onscreenLookingGlass.getLookingGlassFactory().removeAutomaticDisplayListener( this.automaticDisplayAdapter );
		}
		this.onscreenLookingGlass = onscreenLookingGlass;
		setAWTComponent( getAWTComponentToAddListenersTo( this.onscreenLookingGlass ) );
		if( this.onscreenLookingGlass != null ) {
			this.onscreenLookingGlass.getLookingGlassFactory().addAutomaticDisplayListener( this.automaticDisplayAdapter );
		}
	}
	
	protected java.awt.Component getAWTComponent() {
		return this.lookingGlassComponent;
	}
	public void setAWTComponent( java.awt.Component awtComponent ) {
		if( this.lookingGlassComponent != null ) {
			removeListeners( this.lookingGlassComponent  );
		}
		this.lookingGlassComponent  = awtComponent;
		if( this.lookingGlassComponent  != null ) {
			addListeners( awtComponent );
		}
	}
	
	public void clearCameraViews()
	{
		for (CameraPair cameraPair : this.cameraMap.values())
		{
			if (cameraPair.perspectiveCamera != null)
			{
				cameraPair.perspectiveCamera.removeAbsoluteTransformationListener(this.cameraTransformationListener);
			}
		}
		this.cameraMap.clear();
	}
	
	public void addCameraView(CameraView viewType, SymmetricPerspectiveCamera perspectiveCamera, OrthographicCamera orthographicCamera)
	{
		addCameraView(viewType, new CameraPair(perspectiveCamera, orthographicCamera));
	}
	
	protected void addCameraView(CameraView viewType, CameraPair cameras)
	{
		
		if (cameras.perspectiveCamera != null)
		{
			cameras.perspectiveCamera.addAbsoluteTransformationListener(this.cameraTransformationListener);
			AbstractDragAdapter.this.handleManager.updateCameraPosition(cameras.perspectiveCamera.getAbsoluteTransformation().translation);
		}
		this.cameraMap.put( viewType, cameras );
	}
	
	public void setAnimator( Animator animator )
	{
		this.animator = animator;
		this.handleManager.setAnimator(animator);
		for (ManipulatorConditionSet manipulatorSet : this.manipulators)
		{
			if (manipulatorSet.getManipulator() instanceof AnimatorDependentManipulator)
			{
				((AnimatorDependentManipulator)manipulatorSet.getManipulator()).setAnimator(this.animator);
			}
		}
	}
	
	public Animator getAnimator()
	{
		return this.animator;
	}
	
	public Transformable getSelectedObject()
	{
		return this.selectedObject;
	}
	
	public void setHandleShowingForObject( Transformable object, boolean handlesShowing)
	{
//		if (this.selectedObject == object)
//		{
//			this.handleManager.setHandlesShowing(handlesShowing);
//		}
	}
	
	public void triggerSelection(Transformable selected)
	{	
		if (this.selectedObject != selected)
		{
			this.fireSelected( new SelectionEvent(this, selected) );
		}
	}
	
	private static double MARKER_SELECTION_DURATION = .25;
	
	public void setSelectedCameraMarker( CameraMarker selected )
	{
		if (selected != this.selectedCameraMarker)
		{
			this.fireSelecting( new SelectionEvent(this, selected) );
			if (this.selectedCameraMarker != null)
			{
				this.selectedCameraMarker.setOpacity(.3f, MARKER_SELECTION_DURATION);
				if (this.selectedCameraMarker instanceof PerspectiveCameraMarker)
				{
					((PerspectiveCameraMarker)this.selectedCameraMarker).setDetailedViewShowing(false);
				}
			}
			this.selectedCameraMarker = selected;
			if (this.selectedCameraMarker != null)
			{
				this.selectedCameraMarker.setOpacity(1f, MARKER_SELECTION_DURATION);
				if (this.sceneEditor != null && this.selectedCameraMarker instanceof PerspectiveCameraMarker)
				{
					boolean isNewSelectedActiveCameraMarker = this.sceneEditor.isCameraMarkerActive((PerspectiveCameraMarker)this.selectedCameraMarker);
					if (!isNewSelectedActiveCameraMarker)
					{
						((PerspectiveCameraMarker)this.selectedCameraMarker).setDetailedViewShowing(true);
					}
				}
			}
		}
	}
	
	public void setSelectedCameraMarker( Transformable selected )
	{
		CameraMarker cameraMarker = null;
		if (selected != null)
		{
			org.alice.apis.moveandturn.Element selectedMoveAndTurnObject = org.alice.apis.moveandturn.Element.getElement( selected );
			if (selectedMoveAndTurnObject instanceof CameraMarker)
			{
				cameraMarker = (CameraMarker)selectedMoveAndTurnObject;
			}
		}
		setSelectedCameraMarker(cameraMarker);
	}
	
	public void setSelectedObjectMarker( ObjectMarker selected )
	{
		if (selected != this.selectedObjectMarker)
		{
			this.fireSelecting( new SelectionEvent(this, selected) );
			if (this.selectedObjectMarker != null)
			{
				this.selectedObjectMarker.setOpacity(.3f, MARKER_SELECTION_DURATION);
			}
			this.selectedObjectMarker = selected;
			if (this.selectedObjectMarker != null)
			{
				this.selectedObjectMarker.setOpacity(1f, MARKER_SELECTION_DURATION);
			}
		}
	}
	
	public void setSelectedObjectMarker( Transformable selected )
	{
		ObjectMarker objectMarker = null;
		if (selected != null)
		{
			org.alice.apis.moveandturn.Element selectedMoveAndTurnObject = org.alice.apis.moveandturn.Element.getElement( selected );
			if (selectedMoveAndTurnObject instanceof ObjectMarker)
			{
				objectMarker = (ObjectMarker)selectedMoveAndTurnObject;
			}
		}
		setSelectedObjectMarker(objectMarker);
	}
	
	public void setSelectedSceneObject( Transformable selected )
	{
		if (this.selectedObject != selected)
		{
			this.fireSelecting( new SelectionEvent(this, selected) );
			if (HandleManager.canHaveHandles(selected))
			{
				this.handleManager.setHandlesShowing(true);
				this.handleManager.setSelectedObject( selected );
			}
			else
			{
				this.handleManager.setSelectedObject( null );
			}
			this.currentInputState.setCurrentlySelectedObject( selected );
			this.currentInputState.setTimeCaptured();
			selectedObject = selected;
			this.handleStateChange();
		}
	}
	
	public void setSelectedObject(Transformable selected)
	{
		if (this.isInStateChange())
		{
			this.setToBeSelected(selected);
			return;
		}
		if (selected != null)
		{
			org.alice.apis.moveandturn.Element selectedMoveAndTurnObject = org.alice.apis.moveandturn.Element.getElement( selected );
			if (selectedMoveAndTurnObject instanceof ObjectMarker)
			{
				setSelectedObjectMarker(selected);
			}
			else if (selectedMoveAndTurnObject instanceof CameraMarker)
			{
				setSelectedCameraMarker(selected);
			}
			else
			{
				setSelectedSceneObject(selected);
			}
		}
		else
		{
			setSelectedSceneObject(null);
		}
		
	}
	
	public void setInteractionState( org.alice.stageide.sceneeditor.HandleStyle handleStyle )
	{
		if (this.currentInteractionState != null)
		{
			this.currentInteractionState.enabledManipulators(false);
		}
		InteractionGroup interactionState = this.mapHandleStyleToInteractionGroup.get( handleStyle );
		this.currentInteractionState = interactionState;
		if( this.currentInteractionState != null ) {
			this.handleManager.setHandleSet( this.currentInteractionState.getHandleSet() );
			this.currentInteractionState.enabledManipulators(true);
		}
	}
	
	public void pushHandleSet( HandleSet handleSet )
	{
		this.handleManager.pushNewHandleSet( handleSet );
	}
	
	public HandleSet popHandleSet()
	{
		return this.handleManager.popHandleSet();
	}
	
	public void setCameraOnManipulator(CameraInformedManipulator manipulator, InputState startInput)
	{
		//The pick camera can be null if we roll over a 2D handle while we're moving
		if (manipulator.getDesiredCameraView() == CameraView.PICK_CAMERA && this.currentInputState.getPickCamera() != null)
		{
			manipulator.setCamera( this.currentInputState.getPickCamera() );
		}
		else
		{
			manipulator.setCamera( this.getCameraForManipulator(manipulator) );
		}
	}
	
	
	public void setLookingGlassOnManipulator( OnScreenLookingGlassInformedManipulator manipulator )
	{
		manipulator.setOnscreenLookingGlass(this.onscreenLookingGlass);
	}
	
	public void addClickAdapter( ManipulatorClickAdapter clickAdapter, InputCondition... conditions )
	{
		ManipulatorConditionSet conditionSet = new ManipulatorConditionSet( new ClickAdapterManipulator(clickAdapter) );
		for (InputCondition condition : conditions)
		{
			conditionSet.addCondition(condition);
		}
		this.manipulators.add(conditionSet);
	}
	
	protected void setManipulatorStartState(AbstractManipulator manipulator, InputState startState)
	{			
		if (manipulator instanceof OnScreenLookingGlassInformedManipulator)
		{
			OnScreenLookingGlassInformedManipulator lookingGlassManipulator = (OnScreenLookingGlassInformedManipulator)manipulator;
			this.setLookingGlassOnManipulator(lookingGlassManipulator);
		}
		if (manipulator instanceof CameraInformedManipulator)
		{
			CameraInformedManipulator cameraInformed = (CameraInformedManipulator)manipulator;
			this.setCameraOnManipulator(cameraInformed, startState);
		}
	}
	
	protected void handleStateChange()
	{
		this.isInStageChange = true;
		java.util.Vector< AbstractManipulator > toStart = new java.util.Vector< AbstractManipulator >();
		java.util.Vector< AbstractManipulator > toEnd = new java.util.Vector< AbstractManipulator >();
		java.util.Vector< AbstractManipulator > toUpdate = new java.util.Vector< AbstractManipulator >();
		java.util.Vector< AbstractManipulator > toClick = new java.util.Vector< AbstractManipulator >();
		for (int i=0; i<this.manipulators.size(); i++)
		{
			ManipulatorConditionSet currentManipulatorSet = this.manipulators.get( i );
//			System.out.println(currentManipulatorSet.getManipulator()+": "+currentManipulatorSet.getCondition(0));
			currentManipulatorSet.update(this.currentInputState, this.previousInputState );
			if (currentManipulatorSet.isEnabled())
			{
				if (currentManipulatorSet.stateChanged( this.currentInputState, this.previousInputState))
				{
					if ( currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ))
					{
						toUpdate.add( currentManipulatorSet.getManipulator() );
					}
					else if ( currentManipulatorSet.justStarted( this.currentInputState, this.previousInputState ) )
					{
//						System.out.println("Just starting "+currentManipulatorSet.getManipulator());
						toStart.add( currentManipulatorSet.getManipulator() );
					}
					else if ( currentManipulatorSet.justEnded( this.currentInputState, this.previousInputState ) )
					{
						toEnd.add( currentManipulatorSet.getManipulator() );
					}
					else if ( currentManipulatorSet.clicked( this.currentInputState, this.previousInputState ) )
					{
						toClick.add( currentManipulatorSet.getManipulator() );
					}
				}
				else
				{
					boolean whyFailed = currentManipulatorSet.stateChanged( this.currentInputState, this.previousInputState);
				}
			}
			else //Manipulator is not enabled
			{
				if (currentManipulatorSet.getManipulator().hasStarted())
				{
					toEnd.add( currentManipulatorSet.getManipulator() );
				}
			}
		}
		
//		if (toStart.size() > 0)
//		{
//			PrintUtilities.println("STATE CHANGE: ");
//			Thread.dumpStack();
//		}
//		
		//End manipulators first
		for (int i=0; i<toEnd.size(); i++)
		{
//			PrintUtilities.println("Ending: "+toEnd.get(i) + " because of "+this.currentInputState);
			toEnd.get( i ).endManipulator( this.currentInputState, this.previousInputState );
		}
		for (int i=0; i<toClick.size(); i++)
		{
//			PrintUtilities.println("Clicking: "+toClick.get(i) + " because of "+this.currentInputState);
			setManipulatorStartState(toClick.get( i ), this.currentInputState);
			toClick.get( i ).clickManipulator( this.currentInputState, this.previousInputState );
		}
		for (int i=0; i<toStart.size(); i++)
		{
//			PrintUtilities.println(i+": Beginning: "+toStart.get(i) + " at "+System.currentTimeMillis());
			setManipulatorStartState(toStart.get( i ), this.currentInputState);
			toStart.get( i ).startManipulator( this.currentInputState );
		}
		for (int i=0; i<toUpdate.size(); i++)
		{
//			PrintUtilities.println("Updating: "+toUpdate.get(i) + " at "+System.currentTimeMillis());
			//If the manipulator we're updating was just started, don't update it with previous data (it's out of scope for the manipulator)
			if (toStart.contains( toUpdate.get( i ) ))
			{
				toUpdate.get( i ).dataUpdateManipulator( this.currentInputState, this.currentInputState );
			}
			else
			{
				toUpdate.get( i ).dataUpdateManipulator( this.currentInputState, this.previousInputState );
			}
		}
		
		
		if (this.currentInputState.getRolloverHandle() != this.previousInputState.getRolloverHandle())
		{
			if (this.currentInputState.getRolloverHandle() != null){
				this.handleManager.setHandleRollover( this.currentInputState.getRolloverHandle(), true );
			}
			if (this.previousInputState.getRolloverHandle() != null){
				this.handleManager.setHandleRollover( this.previousInputState.getRolloverHandle(), false );
			}
		}
		
		
		if (!this.hasObjectToBeSelected && this.currentInputState.getCurrentlySelectedObject() != this.previousInputState.getCurrentlySelectedObject())
		{
			this.triggerSelection( this.currentInputState.getCurrentlySelectedObject() );
		}
		
		this.previousInputState.copyState(this.currentInputState);
		this.isInStageChange = false;
		if (this.hasObjectToBeSelected)
		{
			this.hasObjectToBeSelected = false;
			this.setSelectedObject( this.toBeSelected );
		}
	}
	
	protected boolean isMouseWheelActive()
	{
		return (this.mouseWheelTimeoutTime > 0);
	}
	
	protected void stopMouseWheel()
	{
		this.mouseWheelTimeoutTime = 0;
		this.currentInputState.setMouseWheelState(0);
		this.mouseWheelStartLocation = null;
	}
	
	protected boolean shouldStopMouseWheel(Point currentMouse)
	{
		if (this.mouseWheelStartLocation != null)
		{
			double distance = currentMouse.distance(this.mouseWheelStartLocation);
			if (distance > CANCEL_MOUSE_WHEEL_DISTANCE)
			{
				return true;
			}
		}
		return false;
	}
	
	protected void update( double timeDelta )
	{
		if (isMouseWheelActive())
		{
			mouseWheelTimeoutTime -= timeDelta;
			if (!isMouseWheelActive())
			{
				stopMouseWheel();
				handleStateChange();
			}
		}
		for (int i=0; i<this.manipulators.size(); i++)
		{
			ManipulatorConditionSet currentManipulatorSet = this.manipulators.get( i );
			if ( currentManipulatorSet.getManipulator().hasStarted() && currentManipulatorSet.shouldContinue( this.currentInputState, this.previousInputState ))
			{
				currentManipulatorSet.getManipulator().timeUpdateManipulator( timeDelta, this.currentInputState );
			}
		}
	}
	
	public void makeCameraActive(AbstractCamera camera)
	{
		boolean activated = false;
		for (Entry<CameraView, CameraPair> cameras : this.cameraMap.entrySet())
		{
			if (cameras.getValue().hasCamera( camera ))
			{
				cameras.getValue().setActiveCamera( camera );
				activated = true;
			}
		}
		if (camera instanceof SymmetricPerspectiveCamera)
		{
			this.handleManager.updateCameraPosition(camera.getAbsoluteTransformation().translation);
		}
		else
		{
			this.handleManager.updateCameraPosition(null);
		}
	}

	public AbstractCamera getActiveCamera()
	{
		//TODO: introduce a true sense of "active"
		CameraPair activeCameraPair = this.cameraMap.get( CameraView.MAIN );
		if ( activeCameraPair != null && activeCameraPair.getActiveCamera() != null )
		{
			return activeCameraPair.getActiveCamera();
		}
		else
		{
			return null;
		}
	}
	
	public AbstractCamera getCameraForManipulator(CameraInformedManipulator cameraManipulator)
	{
		CameraView cameraView = cameraManipulator.getDesiredCameraView();
		AbstractCamera cameraToReturn = null;
		if (cameraView == CameraView.ACTIVE_VIEW || cameraView == CameraView.PICK_CAMERA)
		{
			cameraToReturn = getActiveCamera();
		}
		else
		{
			CameraPair cameras = this.cameraMap.get( cameraView );
			if (cameras != null)
			{
				cameraToReturn = cameras.getActiveCamera();
			}
			else
			{
				cameraToReturn = null;
			}
		}
		return cameraToReturn;
	}
	
	public void setSGCamera( AbstractCamera camera )
	{
//		assert camera != null;
//		System.out.println("Setting Camera from DragAdapter: ");
//		Thread.dumpStack();
//		for (int i=0; i<this.manipulators.size(); i++)
//		{
//			if (this.manipulators.get( i ).getManipulator() instanceof CameraInformedManipulator)
//			{
//				CameraInformedManipulator cameraManipulator = ((CameraInformedManipulator)this.manipulators.get( i ).getManipulator());
//				cameraManipulator.setCamera( getCameraForManipulator( cameraManipulator ) );
//			}
//			if (this.manipulators.get( i ).getManipulator() instanceof OnScreenLookingGlassInformedManipulator)
//			{
//				((OnScreenLookingGlassInformedManipulator)this.manipulators.get( i ).getManipulator()).setOnscreenLookingGlass( getOnscreenLookingGlass() );
//			}
//		}
	}
	
	protected void handleDisplayed() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			if (!hasSetCameraTransformables)
			{
				setSGCamera(sgCamera);
				hasSetCameraTransformables = true;
			}
			double timeCurr = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
			if( sgCamera != null ) {
				if( Double.isNaN( this.timePrev ) ) {
					this.timePrev = edu.cmu.cs.dennisc.clock.Clock.getCurrentTime();
				}
				double timeDelta = timeCurr - this.timePrev;
				update( timeDelta );
			}
			this.timePrev = timeCurr;
		}
	}
	
	private int cameraIndex = 0;
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCamera() {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		if( onscreenLookingGlass != null ) {
			if( this.cameraIndex < onscreenLookingGlass.getCameraCount() ) {
				return onscreenLookingGlass.getCameraAt( this.cameraIndex );
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.scenegraph.Transformable getSGCameraTransformable() {
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = getSGCamera();
		if( sgCamera != null ) {
			return (edu.cmu.cs.dennisc.scenegraph.Transformable)sgCamera.getParent();
		} else {
			return null;
		}
	}

	protected boolean isComponentListener(java.awt.Component component)
	{
		for (MouseListener listener : component.getMouseListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		for (MouseMotionListener listener : component.getMouseMotionListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		for (KeyListener listener : component.getKeyListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		for (MouseWheelListener listener : component.getMouseWheelListeners())
		{
			if (listener == this)
			{
				return true;
			}
		}
		return false;
	}
	
	public void addManipulator( ManipulatorConditionSet manipulator )
	{
		this.manipulators.add(manipulator);
	}
	
	public void addListeners( java.awt.Component component ) {
		if (!this.isComponentListener(component))
		{
			component.addMouseListener( this );
			component.addMouseMotionListener( this );
			component.addKeyListener( this );
			component.addMouseWheelListener( this );
		}
	}
	
	public void removeListeners( java.awt.Component component ) {
		if (this.isComponentListener(component))
		{
			component.removeMouseListener( this );
			component.removeMouseMotionListener( this );
			component.removeKeyListener( this );
			component.removeMouseWheelListener( this );
		}
	}
	
	public void addManipulationListener( ManipulationListener listener )
	{
		this.manipulationEventManager.addManipulationListener( listener );
	}
	
	public void removeManipulationListener( ManipulationListener listener )
	{
		this.manipulationEventManager.removeManipulationListener( listener );
	}
	
	public void addHandle(ManipulationHandle handle)
	{
		this.handleManager.addHandle( handle );
	}

	private ManipulationHandle getHandleForComponent( Component c)
	{
		if (c == null)
		{
			return null;
		}
		else if (c instanceof ManipulationHandle)
		{
			return (ManipulationHandle)c;
		}
		else
		{
			return getHandleForComponent(c.getParent());
		}
	}
	
	public void mousePressed( java.awt.event.MouseEvent e ) 
	{
		this.currentInputState.setMouseState( e.getButton(), true );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DOWN );
		this.currentInputState.setInputEvent(e);
		e.getComponent().requestFocus();
		
		
		if (e.getComponent() == this.lookingGlassComponent)
		{
			this.currentInputState.setClickPickResult( pickIntoScene( e.getPoint() ) );
		}
		else
		{
			this.currentInputState.setClickHandle( this.getHandleForComponent( e.getComponent() ) );
		}
		this.currentInputState.setTimeCaptured();
		this.stopMouseWheel();
		this.handleStateChange();
	}
	
	public void mouseReleased( java.awt.event.MouseEvent e) {
		this.currentInputState.setMouseState( e.getButton(), false );
		this.currentInputState.setMouseLocation( e.getPoint() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_UP );
		this.currentInputState.setInputEvent(e);
		if (this.currentRolloverComponent == this.lookingGlassComponent)
		{
			this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
		}
		else
		{
			this.currentInputState.setRolloverHandle( this.getHandleForComponent( this.currentRolloverComponent ));
		}
		this.currentInputState.setTimeCaptured();
		this.handleStateChange();
	}
	
	private PickResult pickIntoScene( Point mouseLocation )
	{
//		PrintUtilities.println(System.currentTimeMillis()+": Picking object");
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
		assert onscreenLookingGlass != null;
		edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = onscreenLookingGlass.pickFrontMost( mouseLocation.x, mouseLocation.y, /*isSubElementRequired=*/false );
//		PrintUtilities.println(System.currentTimeMillis()+": Done picking object");
		return pickResult;
	}
	
	public void mouseDragged( java.awt.event.MouseEvent e ) {
		try {
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_DRAGGED );
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent(e);
			this.handleStateChange();
		} catch( RuntimeException re ) {
			re.printStackTrace();
		}
	}
	
	public void mouseMoved( java.awt.event.MouseEvent e ) {
		if (!this.currentInputState.getIsDragEvent()) //If we haven't already handled it through dragAndDrop
		{
			this.currentInputState.setMouseLocation( e.getPoint() );
			if (e.getComponent() == this.lookingGlassComponent)
			{
				//Don't pick into the scene if a mouse button is already down 
				if (!this.currentInputState.isAnyMouseButtonDown())
				{			
					this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
				}
			}
			else
			{
				this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ));
			}
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent(e);
			if (shouldStopMouseWheel(e.getPoint()))
			{
				this.stopMouseWheel();
			}
			this.handleStateChange();
		}
	}
	
	private Point getDragAndDropPoint(org.lgna.croquet.steps.DragStep dragAndDropContext)
	{
		java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		java.awt.Point pointInLookingGlass = javax.swing.SwingUtilities.convertPoint( eSource.getComponent(), eSource.getPoint(), this.getAWTComponent() );
		return pointInLookingGlass;
	}
	
	public void dragUpdated(org.lgna.croquet.steps.DragStep dragAndDropContext) {
		this.currentInputState.setDragAndDropContext(dragAndDropContext);
		this.currentInputState.setIsDragEvent(true);
		this.currentInputState.setMouseLocation( getDragAndDropPoint(dragAndDropContext) );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent(dragAndDropContext.getLatestMouseEvent());
		handleStateChange();
	}
	
	public void dragEntered(org.lgna.croquet.steps.DragStep dragAndDropContext) {
		this.currentInputState.setDragAndDropContext(dragAndDropContext);
		this.currentInputState.setIsDragEvent(true);
		this.currentInputState.setMouseLocation( getDragAndDropPoint(dragAndDropContext) );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent(dragAndDropContext.getLatestMouseEvent());
		handleStateChange();
	}
	
	public void dragExited(org.lgna.croquet.steps.DragStep dragAndDropContext) {		
		this.currentInputState.setDragAndDropContext(dragAndDropContext); //We need a valid dragAndDropContext when we handle the update
		this.currentInputState.setIsDragEvent(false);
		this.currentInputState.setMouseLocation( getDragAndDropPoint(dragAndDropContext) );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent(dragAndDropContext.getLatestMouseEvent());
		handleStateChange();
		this.currentInputState.setDragAndDropContext(null);
	}
	
	public void mouseWheelMoved( MouseWheelEvent e ) {
		this.currentInputState.setMouseWheelState( e.getWheelRotation() );
		this.currentInputState.setInputEventType( InputState.InputEventType.MOUSE_WHEEL );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent(e);
		if (this.mouseWheelStartLocation == null)
		{
			this.mouseWheelStartLocation = new Point(e.getPoint());
		}
		this.mouseWheelTimeoutTime = MOUSE_WHEEL_TIMEOUT_TIME;
		this.handleStateChange();
	}
	
	public void keyPressed( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), true );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_DOWN );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent(e);
		handleStateChange();
		
	}
	
	public void keyReleased( java.awt.event.KeyEvent e ) {
		this.currentInputState.setKeyState( e.getKeyCode(), false );
		this.currentInputState.setInputEventType( InputState.InputEventType.KEY_UP );
		this.currentInputState.setTimeCaptured();
		this.currentInputState.setInputEvent(e);
		handleStateChange();

	}

	public void mouseClicked( MouseEvent e ) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered( MouseEvent e ) {
		this.currentRolloverComponent = e.getComponent();
		if (!this.currentInputState.isAnyMouseButtonDown())
		{
			this.currentInputState.setMouseLocation( e.getPoint() );
			if (e.getComponent() == this.lookingGlassComponent)
			{
				this.currentInputState.setRolloverPickResult( pickIntoScene( e.getPoint() ) );
			}
			else
			{
				this.currentInputState.setRolloverHandle( this.getHandleForComponent( e.getComponent() ));
			}
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent(e);
			this.handleStateChange();
		}
	}

	public void mouseExited( MouseEvent e ) {
		this.currentRolloverComponent = null;
		if (!this.currentInputState.isAnyMouseButtonDown())
		{
			this.currentInputState.setMouseLocation( e.getPoint() );
			this.currentInputState.setRolloverHandle( null );
			this.currentInputState.setRolloverPickResult( null );
			this.currentInputState.setTimeCaptured();
			this.currentInputState.setInputEvent(e);
			this.handleStateChange();
		}
		
	}
	
	public HandleManager getHandleManager()
	{
		return this.handleManager;
	}

	public void keyTyped( KeyEvent e ) {
		//System.out.println("Key typed!");
		
	}

	public void setSnapState(SnapState snapState)
	{
		this.snapState = snapState;
	}
	
	public SnapState getSnapState()
	{
		return this.snapState;
	}
	
}
