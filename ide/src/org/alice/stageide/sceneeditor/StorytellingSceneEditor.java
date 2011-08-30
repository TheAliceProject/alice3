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

import org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState;
import org.alice.ide.sceneeditor.AbstractSceneEditor;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.stageide.sceneeditor.snap.SnapState;
import org.lgna.croquet.components.DragComponent;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.UserField;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.BookmarkCameraMarker;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.ObjectMarker;
import org.lgna.story.implementation.MarkerImplementation;
import org.lgna.story.implementation.ProgramImplementation;

import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author dculyba
 * 
 */
public class StorytellingSceneEditor extends AbstractSceneEditor implements
		org.lgna.croquet.DropReceptor,
		edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener {

	private static class SingletonHolder {
		private static StorytellingSceneEditor instance = new StorytellingSceneEditor();
	}
	public static StorytellingSceneEditor getInstance() {
		return SingletonHolder.instance;
	}
	private StorytellingSceneEditor() {
	}

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			StorytellingSceneEditor.this.animator.update();
		}
	};
	
	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory
			.getSingleton().createLightweightOnscreenLookingGlass();

	private class LookingGlassPanel extends
			org.lgna.croquet.components.CompassPointSpringPanel {
		@Override
		protected javax.swing.JPanel createJPanel() {
			javax.swing.JPanel rv = StorytellingSceneEditor.this.onscreenLookingGlass
					.getJPanel();
			rv.setLayout(new javax.swing.SpringLayout());
			return rv;
		}
	}
	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
	
	private org.alice.interact.GlobalDragAdapter globalDragAdapter;
	private org.lgna.story.implementation.SymmetricPerspectiveCameraImplementation sceneCameraImplementation;
	private org.alice.interact.CameraNavigatorWidget mainCameraNavigatorWidget = null;
	private org.lgna.croquet.components.PushButton expandCollapseButton;
	
	@Override
	protected void setProgramInstance(UserInstance programInstance) 
	{
		super.setProgramInstance(programInstance);
		ProgramImplementation programImplementation = ImplementationAccessor.getImplementation(getProgramInstanceInJava());
		programImplementation.setAnimator(this.animator);
		programImplementation.setOnscreenLookingGlass(this.onscreenLookingGlass);
	}
	
	protected void setSceneCamera(org.lgna.project.ast.UserField cameraField)
	{
		this.sceneCameraImplementation = getImplementation(cameraField);

		doCameraDependentInitialization();
//		this.mainCameraViewTracker.setCameras(perspectiveCamera, orthographicCamera);
//		this.snapGrid.addCamera(perspectiveCamera);
//		this.snapGrid.addCamera(orthographicCamera);
//		this.snapGrid.setCurrentCamera(perspectiveCamera);
	}
		
	private void doCameraDependentInitialization()
	{
		if (this.globalDragAdapter != null && this.sceneCameraImplementation != null)
		{
			this.globalDragAdapter.clearCameraViews();
			this.globalDragAdapter.addCameraView( CameraView.MAIN, this.sceneCameraImplementation.getSgCamera(), null );
			this.globalDragAdapter.makeCameraActive( this.sceneCameraImplementation.getSgCamera() );
		}
	}
	
	private static final int INSET = 8;
	
	@Override
	protected void handleExpandContractChange(boolean isExpanded) {
		if (isExpanded)
		{
		}
		else
		{
		}
		this.mainCameraNavigatorWidget.setExpanded(isExpanded);
		this.lookingGlassPanel.setSouthEastComponent(this.expandCollapseButton);
		this.lookingGlassPanel.setSouthComponent(this.mainCameraNavigatorWidget);
	}
	
	@Override
	protected void initializeComponents() {
		if( this.globalDragAdapter != null ) {
			//pass
		} else {
			
//			
//			this.snapGrid = new SnapGrid();
//			this.snapState = new SnapState();
//			this.snapState.getShowSnapGridState().addAndInvokeValueObserver(this.showSnapGridObserver);
//			this.snapState.getIsSnapEnabledState().addAndInvokeValueObserver(this.snapEnabledObserver);
			
			this.globalDragAdapter = new org.alice.interact.GlobalDragAdapter(this);
//			this.globalDragAdapter.setSnapState(this.snapState);
			this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
			this.onscreenLookingGlass.addLookingGlassListener(this);
			this.globalDragAdapter.setAnimator( animator );
			
			this.mainCameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter, CameraView.MAIN);
			
			this.expandCollapseButton = IsSceneEditorExpandedState.getInstance().createPushButton();
			
			doCameraDependentInitialization();
			
//			this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
//				public void selecting( org.alice.interact.event.SelectionEvent e ) {
//				}
//				public void selected( org.alice.interact.event.SelectionEvent e ) {
//					MoveAndTurnSceneEditor.this.handleSelectionEvent( e );
//				}
//			} );
//			
			
//			this.sidePane = new SidePane(this);
//			this.sidePane.setSnapState(this.snapState);
//			this.splitPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
//			this.splitPane.setResizeWeight( 1.0 );
//			this.splitPane.setDividerProportionalLocation( 1.0 );
//			this.addComponent( this.splitPane, Constraint.CENTER );
//			
//			CameraMarkerFieldListSelectionState.getInstance().addAndInvokeValueObserver(this.cameraMarkerFieldSelectionObserver);
//			ObjectMarkerFieldListSelectionState.getInstance().addAndInvokeValueObserver(this.objectMarkerFieldSelectionObserver);
//			
//			
//			ClickedObjectCondition rightMouseAndInteractive = new ClickedObjectCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickHint.MOVEABLE_OBJECTS ) );
//			ManipulatorClickAdapter rightClickAdapter = new ManipulatorClickAdapter() {
//				public void onClick(InputState clickInput) {
//					showRightClickMenuForModel(clickInput);
//					
//				}
//			};
//			this.globalDragAdapter.addClickAdapter(rightClickAdapter, rightMouseAndInteractive);
//			
//			this.mainCameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter, CameraView.MAIN);
//			this.mainCameraViewTracker = new CameraMarkerTracker(this, animator);
//			this.mainCameraMarkerList.addAndInvokeValueObserver(this.mainCameraViewTracker);
//			this.mainCameraMarkerList.addAndInvokeValueObserver(this.mainCameraViewSelectionObserver);
//			this.mainCameraViewSelector = this.mainCameraMarkerList.getPrepModel().createComboBox();
//			this.mainCameraViewSelector.setFontSize(15);
//
//			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.TOP, this.topOrthoMarker );
//			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.SIDE, this.sideOrthoMarker );
//			this.mainCameraViewTracker.mapViewToMarkerAndViceVersa( View.FRONT, this.frontOrthoMarker );
//\
//			this.mainCameraViewSelector.setRenderer(new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< View >() {
//				private final javax.swing.border.Border separatorBelowBorder = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 8, 0 );
//				private final javax.swing.border.Border emptyBorder = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 0 );
//				@Override
//				protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, View view, int index, boolean isSelected, boolean cellHasFocus) {
//					MarkerImplementation value = mainCameraViewTracker.getCameraMarker( view );
//					if( index == 0 ) {
//						rv.setBorder( separatorBelowBorder );
//					} else {
//						rv.setBorder( emptyBorder );
//					}
//					if (isSelected)
//					{
//						rv.setOpaque(true);
//						rv.setBackground(new Color(57, 105, 138));
//						rv.setForeground(Color.WHITE);
//					}
//					else
//					{
//						rv.setOpaque(false);
//						rv.setForeground(Color.BLACK);
//					}
//					return rv;
//				}
//			});
//			
//			org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState isSceneEditorExpandedState = org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance();
//			final org.lgna.croquet.components.CheckBox isSceneEditorExpandedCheckBox = isSceneEditorExpandedState.createCheckBox();
//			isSceneEditorExpandedCheckBox.getAwtComponent().setUI( new IsExpandedCheckBoxUI() );
//			final int X_PAD = 16;
//			final int Y_PAD = 10;
//			isSceneEditorExpandedCheckBox.getAwtComponent().setOpaque( false );
//			isSceneEditorExpandedCheckBox.setFontSize( 18.0f );
//			isSceneEditorExpandedCheckBox.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD, X_PAD, Y_PAD, X_PAD ) );
//			
//			PrintUtilities.println( "todo: addAndInvokeValueObserver" );
//			this.splitPane.setDividerSize( 0 );
//			isSceneEditorExpandedState.addValueObserver( new org.lgna.croquet.State.ValueObserver< Boolean >() {
//				public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
//				}
//				public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
//					handleExpandContractChange( nextValue );
//				}
//			} );
//
//			javax.swing.JPanel lgPanel = this.lookingGlassPanel.getAwtComponent();
//			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouthEast( lgPanel, isSceneEditorExpandedCheckBox.getAwtComponent(), INSET );
//			if( IS_RUN_BUTTON_DESIRED ) {
//				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthEast( lgPanel, this.getIDE().getRunOperation().createButton().getAwtComponent(), INSET );
//			}
//			
//			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, this.mainCameraViewSelector.getAwtComponent(), Horizontal.CENTER, 0, Vertical.NORTH, INSET );
//			this.mainCameraViewSelector.getAwtComponent().setVisible(isSceneEditorExpandedState.getValue());
//		
//			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouth( lgPanel, mainCameraNavigatorWidget, INSET );
//			this.fieldRadioButtons = new FieldRadioButtons( org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance() );
		}
	}
	
	@Override
	protected void setActiveScene( org.lgna.project.ast.UserField sceneField ) {
		super.setActiveScene(sceneField);
		
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setSimulationSpeedFactor( Double.POSITIVE_INFINITY );
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setOnscreenLookingGlass(this.onscreenLookingGlass);

		org.lgna.project.virtualmachine.UserInstance sceneAliceInstance = getActiveSceneInstance();
		org.lgna.story.Scene sceneJavaInstance = (org.lgna.story.Scene)sceneAliceInstance.getInstanceInJava();
		getProgramInstanceInJava().setActiveScene(sceneJavaInstance);
		
		for (org.lgna.project.ast.AbstractField field : sceneField.getValueType().getDeclaredFields())
		{
			if( field.getValueType().isAssignableTo(org.lgna.story.Camera.class)) 
			{
				this.setSceneCamera((org.lgna.project.ast.UserField)field);
			}
		}
		
		ImplementationAccessor.getImplementation(getProgramInstanceInJava()).setSimulationSpeedFactor( 1.0 );
	}
	
	@Override
	public void enableRendering(
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
		if (reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM
				|| reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK) {
			this.onscreenLookingGlass.setRenderingEnabled(true);
		}
	}

	@Override
	public void disableRendering(
			org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering) {
		if (reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM
				|| reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK) {
			this.onscreenLookingGlass.setRenderingEnabled(false);
		}
	}

	@Override
	public void putInstanceForInitializingPendingField(
			UserField field, Object instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getInstanceInJavaForUndo(UserField field) {
		// TODO Auto-generated method stub
		return null;
	}

	private void fillInAutomaticSetUpMethod( org.lgna.project.ast.StatementListProperty bodyStatementsProperty, boolean isThis, org.lgna.project.ast.AbstractField field) {
		SetUpMethodGenerator.fillInAutomaticSetUpMethod( bodyStatementsProperty, isThis, field, this.getInstanceInJavaVMForField(field), this.getActiveSceneInstance() );
	}
	
	@Override
	public void generateCodeForSetUp( StatementListProperty bodyStatementsProperty ) {
		//Set the camera to have the point of view of the opening scene marker
//		AffineMatrix4x4 currentCameraTransformable = this.sceneCameraImplementation.getAbsoluteTransformation();
//		EntityImplementation cameraParent = this.sceneCameraImplementation.getVehicle();
//		cameraParent.setTransformation(this.openingSceneMarker.getTransformation(AsSeenBy.SCENE), this.scene.getSGReferenceFrame());
		
		org.lgna.project.ast.AbstractField sceneField = this.getSceneField();
		this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
		for( org.lgna.project.ast.AbstractField field : this.getActiveSceneType() .getDeclaredFields() ) {
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
		}
		
		//Set the camera back to its original position
//		cameraParent.setTransformation(currentCameraTransformable, this.scene.getSGReferenceFrame());
	}
	
	private boolean HACK_isDisplayableAlreadyHandled = false;
	
	@Override
	protected void handleDisplayable() {
		if( HACK_isDisplayableAlreadyHandled ) {
			System.err.println( "TODO: investigate is displayed" );
		} else {
			super.handleDisplayable();
			HACK_isDisplayableAlreadyHandled = true;
		}
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( this.automaticDisplayListener );
		this.addComponent( this.lookingGlassPanel, Constraint.CENTER );
	}

	@Override
	protected void handleUndisplayable() {
		this.removeComponent( this.lookingGlassPanel );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		super.handleUndisplayable();
	}

// ######### Begin implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter
	public void initialized(LookingGlassInitializeEvent e) {
	}

	public void cleared(LookingGlassRenderEvent e) {
	}

	public void rendered(LookingGlassRenderEvent e) {
//		 if (this.onscreenLookingGlass.getCameraCount() > 0 &&
//		 this.onscreenLookingGlass.getCameraAt(0) instanceof
//		 OrthographicCamera){
//		 paintHorizonLine(e.getGraphics2D(), this.onscreenLookingGlass,
//		 (OrthographicCamera)this.onscreenLookingGlass.getCameraAt(0));
//		 }
	}

	public void resized(LookingGlassResizeEvent e) {
		// updateCameraMarkers();
	}

	public void displayChanged(LookingGlassDisplayChangeEvent e) {
	}
// ######### End implementation of edu.cmu.cs.dennisc.lookingglass.event.LookingGlassAdapter

	
// ######### Begin implementation of org.lgna.croquet.DropReceptor
	public org.lgna.croquet.resolvers.CodableResolver<StorytellingSceneEditor> getCodableResolver() {
		return new org.lgna.croquet.resolvers.SingletonResolver<StorytellingSceneEditor>(
				this);
	}

	public org.lgna.croquet.components.TrackableShape getTrackableShape(
			org.lgna.croquet.DropSite potentialDropSite) {
		return this;
	}

	public boolean isPotentiallyAcceptingOf(DragComponent source) {
		return source instanceof org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent;
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

	private boolean isDropLocationOverLookingGlass(
			org.lgna.croquet.history.DragStep dragAndDropContext) {
		java.awt.event.MouseEvent eSource = dragAndDropContext
				.getLatestMouseEvent();
		java.awt.Point pointInLookingGlass = javax.swing.SwingUtilities
				.convertPoint(eSource.getComponent(), eSource.getPoint(),
						this.lookingGlassPanel.getAwtComponent());
		return this.lookingGlassPanel.getAwtComponent().contains(
				pointInLookingGlass);
	}

	private boolean overLookingGlass = false;

	public org.lgna.croquet.DropSite dragUpdated(
			org.lgna.croquet.history.DragStep dragAndDropContext) {
		if (isDropLocationOverLookingGlass(dragAndDropContext)) {
			if (!overLookingGlass) {
				overLookingGlass = true;
				// this.globalDragAdapter.dragEntered(dragAndDropContext);
			}
			// this.globalDragAdapter.dragUpdated(dragAndDropContext);
		} else {
			if (overLookingGlass) {
				overLookingGlass = false;
				// this.globalDragAdapter.dragExited(dragAndDropContext);
			}
		}
		return null;
	}

	public org.lgna.croquet.Model dragDropped( org.lgna.croquet.history.DragStep dragStep ) {
		if (isDropLocationOverLookingGlass(dragStep)) {
			org.lgna.croquet.DropSite dropSite = null;
			org.lgna.croquet.Model model = dragStep.getModel().getDropModel( dragStep, dropSite );
//			if (model instanceof GalleryClassOperation) {
//				// AffineMatrix4x4 dropTargetPosition =
//				// this.globalDragAdapter.getDropTargetTransformation();
//				// ((GalleryClassOperation)model).setDesiredTransformation(dropTargetPosition);
//			}
			return model;
		}
		return null;
	}

	public void dragExited(
			org.lgna.croquet.history.DragStep dragAndDropContext,
			boolean isDropRecipient) {
	}

	public void dragStopped(org.lgna.croquet.history.DragStep dragAndDropContext) {
		// this.globalDragAdapter.dragExited(dragAndDropContext);
	}

	public String getTutorialNoteText(org.lgna.croquet.Model model,
			org.lgna.croquet.edits.Edit<?> edit,
			org.lgna.croquet.UserInformation userInformation) {
		return "Drop...";
	}
// ######### End implementation of org.lgna.croquet.DropReceptor

	
	public void setHandleVisibilityForObject( Transformable sgComposite, boolean b ) {
		throw new RuntimeException( "todo" );
	}
	public Tuple2< UserField, BookmarkCameraMarker > createCameraMarkerField( NamedUserType ownerType ) {
		throw new RuntimeException( "todo" );
	}
	public Tuple2< UserField, ObjectMarker > createObjectMarkerField( NamedUserType ownerType ) {
		throw new RuntimeException( "todo" );
	}
	public void switchToOthographicCamera() {
		throw new RuntimeException( "todo" );
	}
	public void switchToPerspectiveCamera() {
		throw new RuntimeException( "todo" );
	}
	public AffineMatrix4x4 getGoodPointOfViewInSceneForObject( AxisAlignedBox box ) {
		throw new RuntimeException( "todo" );
	}
	public MarkerImplementation getMarkerForField( UserField field ) {
		throw new RuntimeException( "todo" );
	}
	public SnapState getSnapState() {
		throw new RuntimeException( "todo" );
	}
	public AbstractCamera getSGCameraForCreatingThumbnails() {
		throw new RuntimeException( "todo" );
	}
	public void setSnapGridSpacing( double gridSpacing ) {
		throw new RuntimeException( "todo" );
	}
	
}
