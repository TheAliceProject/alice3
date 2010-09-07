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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

import org.alice.apis.moveandturn.AngleInDegrees;
import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.Element;
import org.alice.apis.moveandturn.HowMuch;
import org.alice.apis.moveandturn.Model;
import org.alice.apis.moveandturn.OrthographicCameraMarker;
import org.alice.apis.moveandturn.PerspectiveCameraMarker;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.StandIn;
import org.alice.apis.moveandturn.Transformable;
import org.alice.ide.IDE;
import org.alice.ide.ProjectApplication;
import org.alice.ide.declarationpanes.CreateFieldFromGalleryPane;
import org.alice.ide.name.validators.FieldNameValidator;
import org.alice.ide.sceneeditor.FieldAndInstanceMapper;
import org.alice.interact.AbstractDragAdapter;
import org.alice.interact.InputState;
import org.alice.interact.InteractionGroup;
import org.alice.interact.PickHint;
import org.alice.interact.PlaneUtilities;
import org.alice.interact.SnapGrid;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.interact.condition.MouseDragCondition;
import org.alice.interact.condition.PickCondition;
import org.alice.interact.handle.HandleSet;
import org.alice.interact.manipulator.ManipulatorClickAdapter;
import org.alice.stageide.croquet.models.gallerybrowser.GalleryFileOperation;
import org.alice.stageide.sceneeditor.snap.SnapState;
import org.alice.stageide.sceneeditor.viewmanager.CameraMarkerFieldTile;
import org.alice.stageide.sceneeditor.viewmanager.CameraMarkerTracker;
import org.alice.stageide.sceneeditor.viewmanager.CreateCameraMarkerActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveActiveCameraToMarkerActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.MoveMarkerToActiveCameraActionOperation;
import org.alice.stageide.sceneeditor.viewmanager.SceneViewManagerPanel;
import org.apache.axis.utils.ArrayUtil;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.alice.ast.Accessible;
import edu.cmu.cs.dennisc.alice.ast.Argument;
import edu.cmu.cs.dennisc.alice.ast.Expression;
import edu.cmu.cs.dennisc.alice.ast.ExpressionStatement;
import edu.cmu.cs.dennisc.alice.ast.FieldAccess;
import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInJava;
import edu.cmu.cs.dennisc.alice.ast.MethodInvocation;
import edu.cmu.cs.dennisc.alice.ast.MethodReflectionProxy;
import edu.cmu.cs.dennisc.alice.ast.Statement;
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.animation.Animator;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.croquet.AbstractButton;
import edu.cmu.cs.dennisc.croquet.PopupMenuOperation;
import edu.cmu.cs.dennisc.croquet.Application;
import edu.cmu.cs.dennisc.croquet.BooleanState;
import edu.cmu.cs.dennisc.croquet.ComboBox;
import edu.cmu.cs.dennisc.croquet.DragAndDropContext;
import edu.cmu.cs.dennisc.croquet.DragComponent;
import edu.cmu.cs.dennisc.croquet.ListSelectionState;
import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.javax.swing.SwingUtilities;import edu.cmu.cs.dennisc.croquet.Operation;
import edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver;
import edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Horizontal;
import edu.cmu.cs.dennisc.javax.swing.SpringUtilities.Vertical;
import edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassDisplayChangeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassInitializeEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassRenderEvent;
import edu.cmu.cs.dennisc.lookingglass.event.LookingGlassResizeEvent;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.ClippedZPlane;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor implements LookingGlassListener{
	public static interface SceneEditorFieldObserver
	{
		public void fieldAdded( FieldDeclaredInAlice addedField );
		public void fieldRemoved( FieldDeclaredInAlice removedField );
		public void fieldsSet(Collection<? extends FieldDeclaredInAlice> newFields);
	}
	
	private static final int INSET = 8;

	private java.util.List< SceneEditorFieldObserver > fieldObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createLightweightOnscreenLookingGlass();
	private class LookingGlassPanel extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JPanel > {
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = MoveAndTurnSceneEditor.this.onscreenLookingGlass.getJPanel();
			rv.setLayout( new javax.swing.SpringLayout() );
			return rv;
		}
	}

	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane splitPane = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane();
	private SidePane sidePane;
	private org.alice.interact.CameraNavigatorWidget mainCameraNavigatorWidget = null;
	private CameraMarker expandedViewSelectedMarker = null;
	//SNAP STATE
	protected SnapState snapState;
	protected SnapGrid snapGrid;
	private org.alice.interact.GlobalDragAdapter globalDragAdapter;
	private org.alice.apis.moveandturn.Scene scene;
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = null;
	private edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera = null;
	private edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgPerspectiveCamera = null;
	
	private FieldRadioButtons fieldRadioButtons;
	private OrthographicCameraMarker topOrthoMarker = null;
	private OrthographicCameraMarker frontOrthoMarker = null;
	private OrthographicCameraMarker sideOrthoMarker = null;
	private List<CameraMarker> orthographicCameraMarkers = new LinkedList<CameraMarker>();
	private PerspectiveCameraMarker openingSceneMarker;
	private PerspectiveCameraMarker sceneViewMarker;
	
	private ComboBox<CameraMarker> mainCameraViewSelector;
	private CameraMarkerTracker mainCameraViewTracker;
	private ListSelectionState<CameraMarker> mainCameraMarkerList = org.alice.stageide.croquet.models.sceneditor.ViewListSelectionState.getInstance();
	private ListSelectionState<FieldDeclaredInAlice> sceneMarkerFieldList = org.alice.stageide.croquet.models.sceneditor.CameraMarkerFieldListSelectionState.getInstance();
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
		public void changed( edu.cmu.cs.dennisc.alice.ast.AbstractCode next ) {
			MoveAndTurnSceneEditor.this.handleFocusedCodeChanged( next );
		}
	};
	
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<CameraMarker> mainCameraViewSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<CameraMarker>() {
		public void changed(CameraMarker nextValue) {
			MoveAndTurnSceneEditor.this.handleMainCameraViewSelection( nextValue );
		}
	};
	
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<FieldDeclaredInAlice> cameraMarkerFieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<FieldDeclaredInAlice>() {
		public void changed(FieldDeclaredInAlice nextValue) {
			MoveAndTurnSceneEditor.this.handleCameraMarkerFieldSelection( nextValue );
		}
	};
	
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible> fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible>() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.Accessible nextValue) {
			MoveAndTurnSceneEditor.this.handleAccessibleSelection( nextValue );
		}
	};
	
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > globalFieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleGlobalFieldsAdded(e.getElements());
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleGlobalFieldsCleared();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleGlobalFieldsRemoved(e.getElements());
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleGlobalFieldsSet(e.getElements());
		}
	};
	
	private BooleanState.ValueObserver showSnapGridObserver = new BooleanState.ValueObserver() {
		public void changing(boolean nextValue) {
		}
		public void changed(boolean nextValue) {
			MoveAndTurnSceneEditor.this.setShowSnapGrid(nextValue);	
		}
	};
	
	private SceneEditorFieldObserver sceneEditorFieldObserver = new SceneEditorFieldObserver() {
		public void fieldsSet(Collection<? extends FieldDeclaredInAlice> newFields) {
			MoveAndTurnSceneEditor.this.handleFieldsSet(newFields);
		}
		
		public void fieldRemoved(FieldDeclaredInAlice removedField) {
			MoveAndTurnSceneEditor.this.handleFieldRemoved(removedField);
		}
		
		public void fieldAdded(FieldDeclaredInAlice addedField) {
			MoveAndTurnSceneEditor.this.handleFieldAdded(addedField);
		}
	};
	
	private static class FieldRadioButtons extends edu.cmu.cs.dennisc.croquet.CustomRadioButtons< edu.cmu.cs.dennisc.alice.ast.Accessible > {
		private static final int SUB_FIELD_LEFT_INSET = 10;
		private static final int INTRA_FIELD_PAD = 1;
		private javax.swing.SpringLayout springLayout;
		public FieldRadioButtons( ListSelectionState<edu.cmu.cs.dennisc.alice.ast.Accessible> model ) {
			super( model );
		}
		@Override
		protected java.awt.LayoutManager createLayoutManager(javax.swing.JPanel jPanel) {
			this.springLayout = new javax.swing.SpringLayout() {
				@Override
				public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
					java.awt.Dimension rv = new java.awt.Dimension( 0, 0 );
					for( java.awt.Component component : parent.getComponents() ) {
						java.awt.Dimension componentSize = component.getPreferredSize();
						rv.width = Math.max( rv.width, componentSize.width );
						rv.height += componentSize.height;
						rv.height += INTRA_FIELD_PAD;
					}
					rv.width += SUB_FIELD_LEFT_INSET;
					rv.width += INSET;
					rv.height += INSET;

//					java.awt.Dimension superSize = super.preferredLayoutSize( parent );
//					rv.width += superSize.width;
//					rv.height += superSize.height;
					return rv;
				}
			};
			return this.springLayout;
		}
		
		@Override
		protected edu.cmu.cs.dennisc.croquet.BooleanStateButton< ? > createBooleanStateButton( edu.cmu.cs.dennisc.alice.ast.Accessible item, BooleanState booleanState ) {
			return new FieldTile( item, booleanState );
		}
		
		private edu.cmu.cs.dennisc.croquet.Component<?> previousComponent;
		private edu.cmu.cs.dennisc.croquet.Component<?> rootComponent;
		@Override
		protected void removeAllDetails() {
			this.internalRemoveAllComponents();
		}
		@Override
		protected void addPrologue(int count) {
			this.previousComponent = null;
			this.rootComponent = null;
		}
		@Override
		protected void addItem( edu.cmu.cs.dennisc.croquet.ItemSelectablePanel.ItemDetails itemDetails) {
			AbstractButton<?,?> button = itemDetails.getButton();
			if( this.previousComponent != null ) {
				this.springLayout.putConstraint( javax.swing.SpringLayout.NORTH, button.getAwtComponent(), INTRA_FIELD_PAD, javax.swing.SpringLayout.SOUTH, this.previousComponent.getAwtComponent() );
				this.springLayout.putConstraint( javax.swing.SpringLayout.WEST, button.getAwtComponent(), SUB_FIELD_LEFT_INSET, javax.swing.SpringLayout.WEST, this.rootComponent.getAwtComponent() );
				this.internalAddComponent(button);
			} else {
				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( this.getAwtComponent(), button.getAwtComponent(), INSET );
				this.rootComponent = button;
			}
			this.previousComponent = button;
		}
		@Override
		protected void addEpilogue() {
			this.previousComponent = null;
			this.rootComponent = null;
			this.getPopupMenuOperation();
		}
		
		public FieldTile getFieldTileForField(FieldDeclaredInAlice field)
		{
			for (edu.cmu.cs.dennisc.croquet.ItemSelectablePanel.ItemDetails item : this.getAllItemDetails())
			{
				FieldTile fieldTile = (FieldTile)item.getButton();
				Accessible itemField = fieldTile.getAccessible();
				if (itemField == field)
				{
					return fieldTile;
				}
			}
			return null;
		}
	}
	

	private static class SingletonHolder {
		private static MoveAndTurnSceneEditor instance = new MoveAndTurnSceneEditor();
	}
	public static MoveAndTurnSceneEditor getInstance() {
		return SingletonHolder.instance;
	}
	private MoveAndTurnSceneEditor()
	{
	}
	
	public edu.cmu.cs.dennisc.croquet.CodableResolver< MoveAndTurnSceneEditor > getCodableResolver() {
		return new edu.cmu.cs.dennisc.croquet.SingletonResolver< MoveAndTurnSceneEditor >( this );
	}
	public edu.cmu.cs.dennisc.croquet.TrackableShape getTrackableShape( edu.cmu.cs.dennisc.croquet.DropSite potentialDropSite ) {
		return this;
	}

	public ListSelectionState<CameraMarker> getMainCameraMarkerList()
	{
		return this.mainCameraMarkerList;
	}
	
	public ListSelectionState<FieldDeclaredInAlice> getSceneMarkerFieldList()
	{
		return this.sceneMarkerFieldList;
	}
	
	public void addSceneEditorFieldObserver( SceneEditorFieldObserver fieldObserver ) {
		this.fieldObservers.add( fieldObserver );
	}
	
	public void addAndInvokeSceneEditorFieldObserver( SceneEditorFieldObserver fieldObserver ) {
		this.addSceneEditorFieldObserver(fieldObserver);
		if (this.sceneType != null)
		{
			fieldObserver.fieldsSet(this.sceneType.fields.getValue());
		}
	}
	
	public void removeSceneEditorFieldObserver( SceneEditorFieldObserver fieldObserver )
	{
		this.fieldObservers.remove(fieldObserver);
	}
	
	public Animator getAnimator()
	{
		return this.animator;
	}
	
	
	/**
	 * Selection Handling
	 */
	
	private void handleCameraMarkerFieldSelection( FieldDeclaredInAlice cameraMarkerField )
	{
//		CameraMarker oldMarker = this.getCameraMarkerForField(currentSelectedCameraMarkerField);
//		if (oldMarker != null)
//		{
//			oldMarker.setShowing(false);
//		}
		CameraMarker newMarker = this.getCameraMarkerForField(cameraMarkerField);
		if (newMarker != null)
		{
			//Camera markers are not in the same selection state as things in the scene, so null out the field selection state if we're selecting a marker
			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setSelectedItem(null);
			this.globalDragAdapter.setSelectedObject(newMarker.getSGTransformable());
		}
		else
		{
			this.globalDragAdapter.setSelectedObject(null);
		}
		MoveActiveCameraToMarkerActionOperation.getInstance().setMarkerField(cameraMarkerField);
		MoveMarkerToActiveCameraActionOperation.getInstance().setMarkerField(cameraMarkerField);
		this.sidePane.getViewManager().updateButtons();
		
	}
	
	private void handleMainCameraViewSelection( CameraMarker cameraMarker )
	{
		MoveActiveCameraToMarkerActionOperation.getInstance().setCameraMarker(cameraMarker);
		MoveMarkerToActiveCameraActionOperation.getInstance().setCameraMarker(cameraMarker);
	}
	
	//Called when a selection changes on org.alice.ide.IDE.getSingleton().getAccessibleListState()
	private void handleAccessibleSelection( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		if( accessible instanceof AbstractField ) {
			AbstractField field = (AbstractField)accessible;
			Object instance = this.getInstanceInAliceVMForField( field );
			if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
				edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
				instance = instanceInAlice.getInstanceInJava();
			}
//			PrintUtilities.println("Selected "+instance+":"+instance.getClass().getSimpleName());
			if( instance instanceof org.alice.apis.moveandturn.Model ) {
				org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instance;
				this.globalDragAdapter.setSelectedObject( model.getSGTransformable() );
			}
			else if( instance instanceof org.alice.apis.moveandturn.Marker ) {
				org.alice.apis.moveandturn.Marker marker = (org.alice.apis.moveandturn.Marker)instance;
				this.globalDragAdapter.setSelectedObject( marker.getSGTransformable() );
			} else if (instance instanceof org.alice.apis.moveandturn.SymmetricPerspectiveCamera)
			{
				org.alice.apis.moveandturn.SymmetricPerspectiveCamera mtCamera = (org.alice.apis.moveandturn.SymmetricPerspectiveCamera)instance;
				if (mtCamera.getSGCamera() == this.sgPerspectiveCamera)
				{
					this.globalDragAdapter.setSelectedObject( this.openingSceneMarker.getSGTransformable() );
				}
			}
			else if (instance instanceof Scene)
			{
				this.globalDragAdapter.setSelectedObject(null);
			}
			this.updateFieldLabels();
		}
	}
	
	private void handleSelectionEvent( org.alice.interact.event.SelectionEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = e.getTransformable();
		Object objectInJava = null;
		if (sgTransformable == null)
		{
			objectInJava = this.scene;
		}
		else
		{
			objectInJava = org.alice.apis.moveandturn.Element.getElement( sgTransformable );
		}
		if (objectInJava == this.openingSceneMarker)
		{
			objectInJava = org.alice.apis.moveandturn.Element.getElement(this.sgPerspectiveCamera);
		}
//		PrintUtilities.println("trying to select "+objectInJava+":"+objectInJava.hashCode());
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldForInstanceInJavaVM( objectInJava );
		if (objectInJava instanceof CameraMarker)
		{
			this.sceneMarkerFieldList.setSelectedItem((FieldDeclaredInAlice)field);			
		}
		else
		{
			this.sceneMarkerFieldList.setSelectedItem(null);
			org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setSelectedItem(field);
		}
	}
	
	/**
	 * End Selection Handling
	 */
	

	/**
	 * Field Added/Removed Handling
	 */

	public AffineMatrix4x4 getGoodPointOfViewInSceneForObject(edu.cmu.cs.dennisc.math.AxisAlignedBox boundingBox)
	{
		AffineMatrix4x4 goodPointOfView = new AffineMatrix4x4();
		CameraMarker selectedCameraMarker = getActiveCameraMarker();
		AffineMatrix4x4 cameraTransform = selectedCameraMarker.getSGTransformable().getAbsoluteTransformation();
		
		//PrintUtilities.println("Camera marker: "+selectedCameraMarker.getName());
		
		if (selectedCameraMarker instanceof PerspectiveCameraMarker)
		{
			AbstractCamera selectedCamera = this.getSGPerspectiveCamera();
			Vector3 cameraBackward = cameraTransform.orientation.backward;
			Vector3 cameraForward = Vector3.createMultiplication(cameraBackward, -1);
			Vector3 objectBackward = cameraTransform.orientation.backward;
			objectBackward.y = 0.0;
			objectBackward.normalize();
			if (objectBackward.isNaN())
			{
				objectBackward = cameraTransform.orientation.up;
				objectBackward.multiply(-1);
				objectBackward.y = 0.0;
				objectBackward.normalize();
			}
			double dotWithVertical = Math.abs(Vector3.calculateDotProduct(cameraForward, Vector3.accessPositiveYAxis()));
			if (dotWithVertical < .5)
			{
				double downwardShiftFactor = ((.5 - dotWithVertical)/.5) * -.2;
				cameraForward.add(new Vector3(0, downwardShiftFactor, 0));
				cameraForward.normalize();
			}
			Point3 pickPoint = PlaneUtilities.getPointInPlane( PlaneUtilities.GROUND_PLANE, new edu.cmu.cs.dennisc.math.Ray(selectedCamera.getAbsoluteTransformation().translation, cameraForward));
			if (pickPoint == null)
			{
				//Create a pick ray that picks mostly down (well, in the direction of the ground) in the direction the camera is facing
				Point3 pickOrigin = cameraTransform.translation;
				Vector3 pickDirection = new Vector3(cameraForward);
				pickDirection.y = 0;
				pickDirection.normalize();
				if (pickOrigin.y < 0)
				{
					pickDirection.y = .7;
				}
				else
				{
					pickDirection.y = -.7;
				}
				pickDirection.normalize();
				pickPoint = PlaneUtilities.getPointInPlane( PlaneUtilities.GROUND_PLANE, new edu.cmu.cs.dennisc.math.Ray(pickOrigin, pickDirection));
				//TODO: handle out of view
			}
			assert pickPoint != null;
			if ( pickPoint != null)
			{
				OrthogonalMatrix3x3 facingCameraOrientation = new OrthogonalMatrix3x3(new ForwardAndUpGuide(objectBackward, Vector3.accessPositiveYAxis()));
				goodPointOfView.translation.set(pickPoint);
				goodPointOfView.orientation.setValue(facingCameraOrientation);
			}
		}
		else
		{
			Point3 cameraPosition = cameraTransform.translation;
			Vector3 goodBackward = cameraTransform.orientation.backward;
			if (selectedCameraMarker == this.topOrthoMarker)
			{
				goodBackward = cameraTransform.orientation.up;
				goodPointOfView.translation.set(cameraPosition.x, 0, cameraPosition.z);
			}
			else if (selectedCameraMarker == this.sideOrthoMarker)
			{
				goodPointOfView.translation.set(0, 0, cameraPosition.z);
			}
			else if (selectedCameraMarker == this.frontOrthoMarker)
			{
				goodPointOfView.translation.set(cameraPosition.x, 0, 0);
			}
			OrthogonalMatrix3x3 facingCameraOrientation = new OrthogonalMatrix3x3(new ForwardAndUpGuide(goodBackward, Vector3.accessPositiveYAxis()));
			goodPointOfView.orientation.setValue(facingCameraOrientation);
		}
		if (boundingBox != null)
		{
			goodPointOfView.translation.y -= boundingBox.getYMinimum();
		}
		return goodPointOfView;
	}
	
	private void handleFieldAdded(final FieldDeclaredInAlice addedField)
	{
		Object instance = this.getInstanceInJavaVMForField( addedField );
		if( instance instanceof org.alice.apis.moveandturn.Transformable ) {
			final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;

			this.putInstanceForField( addedField, transformable );
			final org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = this.scene.findFirstMatch( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class );
			boolean isAnimationDesired = true;
			if( isAnimationDesired && camera != null && 
				(transformable instanceof org.alice.apis.moveandturn.Model  || transformable instanceof org.alice.apis.moveandturn.BookmarkCameraMarker)
			  )	
			{
				new Thread() {
					@Override
					public void run() {
						if (transformable instanceof org.alice.apis.moveandturn.Model)
						{
							MoveAndTurnSceneEditor.this.getGoodLookAtShowInstanceAndReturnCamera( camera, (org.alice.apis.moveandturn.Model)transformable );
						}
						else if (transformable instanceof org.alice.apis.moveandturn.BookmarkCameraMarker)
						{
							MoveAndTurnSceneEditor.this.getGoodLookAtShowInstanceAndReturnCamera( camera, (org.alice.apis.moveandturn.BookmarkCameraMarker)transformable );
							CreateCameraMarkerActionOperation.getInstance().setEnabled(true);
						}
						ListSelectionState<Accessible> accessibleListSelectionState = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance();
						if( accessibleListSelectionState.containsItem( addedField ) ) {
							accessibleListSelectionState.setSelectedItem( addedField );
						}
					}
				}.start();
			} else {
				this.scene.addComponent( transformable );
			}
		} else {
			PrintUtilities.println( "handleFieldAdded", instance );
		}
	}
	
	private void handleFieldRemoved( FieldDeclaredInAlice removedField )
	{
		PrintUtilities.println( "todo: have handleFieldRemoved just remove the passed in field rather than check for inconsistencies." );

		Object instance = this.getInstanceInJavaVMForField( removedField );
		if( instance instanceof org.alice.apis.moveandturn.Transformable ) {
			final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;
			this.scene.removeComponent( transformable );
			this.removeField(removedField);
		} else {
			PrintUtilities.println( "handleFieldRemoved", instance );
		}
	}
	
	private void handleFieldsSet(Collection<? extends FieldDeclaredInAlice> newFields)
	{
		//Sets come in and are already in the scene, so we don't really need to worry about them
		//The object tree is handled by the FieldRadioButtons
	}
	
	private void handleGlobalFieldsAdded(Collection<? extends FieldDeclaredInAlice> addedFields)
	{
		refreshMarkerLists();
		for (SceneEditorFieldObserver observer : MoveAndTurnSceneEditor.this.fieldObservers)
		{
			
			for (FieldDeclaredInAlice addedField : addedFields)
			{
				observer.fieldAdded(addedField);
			}
		}
	}
	
	private void handleGlobalFieldsRemoved(Collection<? extends FieldDeclaredInAlice> removedFields)
	{
		refreshMarkerLists();
		for (SceneEditorFieldObserver observer : MoveAndTurnSceneEditor.this.fieldObservers)
		{
			for (FieldDeclaredInAlice removedField : removedFields)
			{
				observer.fieldRemoved(removedField);
			}
		}
	}
	
	private void handleGlobalFieldsCleared()
	{
		refreshMarkerLists();
		for (SceneEditorFieldObserver observer : MoveAndTurnSceneEditor.this.fieldObservers)
		{
			observer.fieldsSet(null);
		}
	}
	
	private void handleGlobalFieldsSet(Collection<? extends FieldDeclaredInAlice> newFields)
	{
		refreshMarkerLists();
		for (SceneEditorFieldObserver observer : MoveAndTurnSceneEditor.this.fieldObservers)
		{
			observer.fieldsSet(newFields);
		}
	}
	
	private void refreshMarkerLists()
	{
		List<FieldDeclaredInAlice> cameraMarkerFields =  edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		List<FieldDeclaredInAlice> declaredFields = this.getDeclaredFields();
		for( FieldDeclaredInAlice field : declaredFields) 
		{
			if (field.getValueType().isAssignableTo( org.alice.apis.moveandturn.CameraMarker.class ))
			{
				//PrintUtilities.println("Camera marker: "+field.getName());
				cameraMarkerFields.add(field);
			}
		}
		
		FieldDeclaredInAlice sceneMarkerSelectedValue = this.sceneMarkerFieldList.getSelectedItem();
		int sceneMarkerSelectedIndex = -1;
		if (sceneMarkerSelectedValue != null)
		{
			sceneMarkerSelectedIndex = cameraMarkerFields.indexOf(sceneMarkerSelectedValue);
		}
		this.sceneMarkerFieldList.setListData(sceneMarkerSelectedIndex, cameraMarkerFields);
	}
	
	/**
	 * End Field Added/Removed Handling
	 */

	@Override
	public void enableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
		if( reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM || reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK ) {
			this.onscreenLookingGlass.setRenderingEnabled( true );
		}
	}
	@Override
	public void disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering ) {
		if( reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM || reasonToDisableSomeAmountOfRendering == org.alice.ide.ReasonToDisableSomeAmountOfRendering.CLICK_AND_CLACK ) {
			this.onscreenLookingGlass.setRenderingEnabled( false );
		}
	}

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			MoveAndTurnSceneEditor.this.animator.update();
		}
	};

	private void addFieldListening()
	{
		this.addAndInvokeSceneEditorFieldObserver(this.sceneEditorFieldObserver);
	}
	
	private void removeFieldListening()
	{
		this.removeSceneEditorFieldObserver(this.sceneEditorFieldObserver);
	}
	
	private javax.swing.event.ListDataListener listDataListener = new javax.swing.event.ListDataListener() {
		public void contentsChanged(javax.swing.event.ListDataEvent e) {
		}
		public void intervalAdded(javax.swing.event.ListDataEvent e) {
		}
		public void intervalRemoved(javax.swing.event.ListDataEvent e) {
			MoveAndTurnSceneEditor.this.EPIC_HACK_handleMarkerRemoved( e );
		}
	};
	
	private void EPIC_HACK_handleMarkerRemoved( javax.swing.event.ListDataEvent e ) {
		PrintUtilities.println( "todo: remove epic hack to handle camera marker removal" );
		List<FieldDeclaredInAlice> declaredFields = this.getDeclaredFields();
		for( FieldDeclaredInAlice field : declaredFields) {
			if (field.getValueType().isAssignableTo( org.alice.apis.moveandturn.CameraMarker.class )) {
				if( this.sceneMarkerFieldList.containsItem( field ) ) {
					//pass
				} else {
					int index = org.alice.ide.IDE.getSingleton().getSceneType().fields.indexOf( field );
					assert index != -1;
					org.alice.ide.IDE.getSingleton().getSceneType().fields.remove( index );
				}
			}
		}
	}
	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		this.initializeIfNecessary();
		this.addFieldListening();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( this.automaticDisplayListener );
		this.splitPane.setLeftComponent( this.lookingGlassPanel );
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().addAndInvokeValueObserver( this.codeSelectionObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().addAndInvokeValueObserver( this.fieldSelectionObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().addAndInvokeValueObserver( this.sidePane.getPropertyManager() );
		this.sceneMarkerFieldList.addListDataListener( this.listDataListener );
	}

	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.sceneMarkerFieldList.removeListDataListener( this.listDataListener );
		this.splitPane.setLeftComponent( null );
		this.removeFieldListening();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().removeValueObserver( this.codeSelectionObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().removeValueObserver( this.fieldSelectionObserver );
		org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().removeValueObserver( this.sidePane.getPropertyManager() );
		super.handleRemovedFrom( parent );
	}
	
	public AbstractDragAdapter getDragAdapter()
	{
		return this.globalDragAdapter;
	}
	
	public CameraMarker getCameraMarkerForField( FieldDeclaredInAlice field )
	{
		CameraMarker cameraMarker = this.getInstanceInJavaForField(field, org.alice.apis.moveandturn.CameraMarker.class);
		return cameraMarker;
	}
	
	private void initializeIfNecessary() {
		if( this.globalDragAdapter != null ) {
			//pass
		} else {
			this.openingSceneMarker = new PerspectiveCameraMarker();
			this.openingSceneMarker.setIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/mainCameraIcon.png")));
			this.openingSceneMarker.setHighlightedIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/mainCameraIcon_highlighted.png")));
			this.sceneViewMarker = new PerspectiveCameraMarker();
			this.sceneViewMarker.setDisplayVisuals(false);
			this.sceneViewMarker.setIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/sceneEditorCameraIcon.png")));
			this.sceneViewMarker.setHighlightedIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/sceneEditorCameraIcon_highlighted.png")));
			
			createOrthographicCamera();
			createOrthographicCameraMarkers();
			
			List<CameraMarker> cameraMarkerFieldsAndOrthoOptions =  edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			cameraMarkerFieldsAndOrthoOptions.add(this.openingSceneMarker);
			cameraMarkerFieldsAndOrthoOptions.add(this.sceneViewMarker);
			cameraMarkerFieldsAndOrthoOptions.addAll(this.orthographicCameraMarkers);
			this.mainCameraMarkerList.setListData(0, cameraMarkerFieldsAndOrthoOptions);
			
			this.snapGrid = new SnapGrid();
			this.snapState = new SnapState();
			this.snapState.getShowSnapGridState().addAndInvokeValueObserver(this.showSnapGridObserver);
			
			this.globalDragAdapter = new org.alice.interact.GlobalDragAdapter(this);
			this.globalDragAdapter.setSnapState(this.snapState);
			this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
			
			this.sidePane = new SidePane(this);
			this.sidePane.setSnapState(this.snapState);
			this.splitPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
			this.splitPane.setResizeWeight( 1.0 );
			this.splitPane.setDividerProportionalLocation( 1.0 );
			this.addComponent( this.splitPane, Constraint.CENTER );
			
			this.sceneMarkerFieldList.addAndInvokeValueObserver(this.cameraMarkerFieldSelectionObserver);
			
			
			MouseDragCondition rightMouseAndInteractive = new MouseDragCondition( java.awt.event.MouseEvent.BUTTON3 , new PickCondition( PickHint.MOVEABLE_OBJECTS ) );
			ManipulatorClickAdapter rightClickAdapter = new ManipulatorClickAdapter() {
				public void onClick(InputState clickInput) {
					showRightClickMenuForModel(clickInput);
					
				}
			};
			this.globalDragAdapter.addClickAdapter(rightClickAdapter, rightMouseAndInteractive);
			this.onscreenLookingGlass.addLookingGlassListener(this);
			this.mainCameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter, CameraView.MAIN);
			this.mainCameraViewTracker = new CameraMarkerTracker(this, animator);
			this.mainCameraMarkerList.addAndInvokeValueObserver(this.mainCameraViewTracker);
			this.mainCameraMarkerList.addAndInvokeValueObserver(this.mainCameraViewSelectionObserver);
			this.mainCameraViewSelector = this.mainCameraMarkerList.createComboBox();
			this.mainCameraViewSelector.setFontSize(15);
			
//			final Color BLUE_PRINT_COLOR = new Color(149,166,216);
			this.mainCameraViewSelector.setRenderer(new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer< CameraMarker >() {
//				private final javax.swing.border.Border separatorBelowBorder = new javax.swing.border.EmptyBorder( 0,0,7,0) {
//					@Override
//					public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
//						g.setColor( Color.BLACK );
//						g.fillRect( x, y+height-6, width, 3 );
//						g.setColor( BLUE_PRINT_COLOR );
//						g.fillRect( x, y+height-3, width, 3 );
//					}
//				};
				private final javax.swing.border.Border separatorBelowBorder = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 8, 0 );
				private final javax.swing.border.Border emptyBorder = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 0 );
				@Override
				protected javax.swing.JLabel getListCellRendererComponent(javax.swing.JLabel rv, javax.swing.JList list, CameraMarker value, int index, boolean isSelected, boolean cellHasFocus) {
					rv.setText(value.getName());
					
					if( index == 0 ) {
						rv.setBorder( separatorBelowBorder );
					} else {
						rv.setBorder( emptyBorder );
					}
					if (isSelected)
					{
						rv.setOpaque(true);
						rv.setBackground(new Color(57, 105, 138));
						rv.setForeground(Color.WHITE);
					}
					else
					{
						rv.setOpaque(false);
						rv.setForeground(Color.BLACK);
					}
					if (isSelected && value.getHighlightedIcon() != null)
					{
						rv.setIcon(value.getHighlightedIcon());
					}
					else
					{
						rv.setIcon(value.getIcon());
					}
					
//					if( value == openingSceneMarker ) {
//						//pass
//					} else {
//						final float SATURATION_SCALE_FACTOR = 0.85f;
//						final float BRIGHTNESS_SCALE_FACTOR = 0.85f;
//						rv.setForeground( edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( rv.getForeground(), 1.0f, SATURATION_SCALE_FACTOR, BRIGHTNESS_SCALE_FACTOR ) );
//						rv.setBackground( edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( rv.getBackground(), 1.0f, SATURATION_SCALE_FACTOR, BRIGHTNESS_SCALE_FACTOR ) );
//					}
					return rv;
				}
			});
			
			org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState isSceneEditorExpandedState = org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance();
			final edu.cmu.cs.dennisc.croquet.CheckBox isSceneEditorExpandedCheckBox = isSceneEditorExpandedState.createCheckBox();
			isSceneEditorExpandedCheckBox.getAwtComponent().setUI( new IsExpandedCheckBoxUI() );
			final int X_PAD = 16;
			final int Y_PAD = 10;
			isSceneEditorExpandedCheckBox.getAwtComponent().setOpaque( false );
			isSceneEditorExpandedCheckBox.setFontSize( 18.0f );
			isSceneEditorExpandedCheckBox.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD, X_PAD, Y_PAD, X_PAD ) );
			
			PrintUtilities.println( "todo: addAndInvokeValueObserver" );
			this.splitPane.setDividerSize( 0 );
			isSceneEditorExpandedState.addValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
				public void changing(boolean nextValue) {
				}
				public void changed(boolean nextValue) {
					handleExpandContractChange( nextValue );
				}
			} );

			javax.swing.JPanel lgPanel = this.lookingGlassPanel.getAwtComponent();
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouthEast( lgPanel, isSceneEditorExpandedCheckBox.getAwtComponent(), INSET );
			final boolean IS_RUN_BUTTON_DESIRED = true;
			if( IS_RUN_BUTTON_DESIRED ) {
				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthEast( lgPanel, this.getIDE().getRunOperation().createButton().getAwtComponent(), INSET );
			}
			
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, this.mainCameraViewSelector.getAwtComponent(), Horizontal.CENTER, 0, Vertical.NORTH, INSET );
			this.mainCameraViewSelector.getAwtComponent().setVisible(isSceneEditorExpandedState.getValue());
			

			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouth( lgPanel, mainCameraNavigatorWidget, INSET );

			this.fieldRadioButtons = new FieldRadioButtons( org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance() );
			this.globalDragAdapter.setAnimator( animator );
			this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
				public void selecting( org.alice.interact.event.SelectionEvent e ) {
				}
				public void selected( org.alice.interact.event.SelectionEvent e ) {
					MoveAndTurnSceneEditor.this.handleSelectionEvent( e );
				}
			} );
		}
	}
	
	
	private void createOrthographicCamera()
	{
		if( this.sgOrthographicCamera != null ) {
			//pass
		} else {
			//We need to make a MoveAndTurn orthographic camera because the scene is fundamentally a MoveAndTurn scene
			org.alice.apis.moveandturn.OrthographicCamera moveAndTurnOrthographicCamera = new org.alice.apis.moveandturn.OrthographicCamera();
			this.sgOrthographicCamera = moveAndTurnOrthographicCamera.getSGOrthographicCamera();
			this.sgOrthographicCamera.nearClippingPlaneDistance.setValue(.01d);
			edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = moveAndTurnOrthographicCamera.getSGTransformable();
			sgTransformable.putBonusDataFor(org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.ORTHOGRAPHIC_CAMERA);
		}
	}
	
	private void handleExpandContractChange( boolean isExpanded ) {
		this.sidePane.setExpanded( isExpanded );
		javax.swing.JPanel lgPanel = this.lookingGlassPanel.getAwtComponent();
		if( mainCameraNavigatorWidget != null ) {
			mainCameraNavigatorWidget.setExpanded( isExpanded );
		}
		if( isExpanded ) {
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( lgPanel, fieldRadioButtons.getAwtComponent(), INSET );
		}
		this.mainCameraViewSelector.getAwtComponent().setVisible(isExpanded);
		if( isExpanded ) {
			//Add the camera view controls into the split pane and show it
			this.splitPane.setRightComponent( this.sidePane );
			this.splitPane.setDividerSize( 10 );
				//Switch the main view to the previously selected index
			if (this.expandedViewSelectedMarker != null)
			{
				this.mainCameraMarkerList.setSelectedItem(this.expandedViewSelectedMarker);
			} 
			else
			{
				this.mainCameraMarkerList.setSelectedItem(this.openingSceneMarker);
			}
			
		} else {
			lgPanel.remove( fieldRadioButtons.getAwtComponent() );
			this.splitPane.setRightComponent( null );
			this.splitPane.setDividerSize( 0 );
		
			//Cache the currently selected view index
			this.expandedViewSelectedMarker = this.mainCameraMarkerList.getSelectedItem();
			this.globalDragAdapter.getInteractionSelectionStateList().setSelectedItem(this.globalDragAdapter.getInteractionSelectionStateList().getItemAt(0));
			
			//Switch the main view to the starting view
			this.mainCameraMarkerList.setSelectedItem(this.openingSceneMarker);
			
		}
		this.updateSceneBasedOnScope();
		this.mainCameraViewSelector.revalidateAndRepaint();
		this.lookingGlassPanel.getAwtComponent().revalidate();
		this.sidePane.revalidateAndRepaint();
		this.revalidateAndRepaint();
	}

	private FieldTile getFieldTileForClick(InputState input)
	{
		edu.cmu.cs.dennisc.scenegraph.Transformable t = input.getClickPickTransformable();
		org.alice.apis.moveandturn.Element element = org.alice.apis.moveandturn.Element.getElement(t);
		FieldDeclaredInAlice clickedJavaField = (FieldDeclaredInAlice)this.getFieldForInstanceInJavaVM(element);
		FieldTile tile = this.fieldRadioButtons.getFieldTileForField(clickedJavaField);
		if (tile != null)
		{
			return tile;
		}
		return null;
	}
	
	private void showRightClickMenuForModel( InputState clickState )
	{
		FieldTile fieldTile = this.getFieldTileForClick(clickState);
		if (fieldTile != null)
		{
			PopupMenuOperation popUp = fieldTile.getPopupMenuOperation();
			if (popUp != null)
			{
				if( fieldTile.getAwtComponent().isShowing() ) {
					MouseEvent convertedEvent = SwingUtilities.convertMouseEvent((Component)clickState.getInputEvent().getSource(), (MouseEvent)clickState.getInputEvent(), fieldTile.getAwtComponent());
					popUp.fire(convertedEvent, fieldTile);
				} else {
					popUp.fire( clickState.getInputEvent() );
				}
			}
		}
	}
		
	private void getGoodLookAtShowInstanceAndReturnCamera( org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera, org.alice.apis.moveandturn.Model model ) {
		model.setOpacity( 0.0, org.alice.apis.moveandturn.Composite.RIGHT_NOW );
		org.alice.apis.moveandturn.PointOfView pov = camera.getPointOfView( this.scene );
		camera.getGoodLookAt( model, 0.5 );
		this.scene.addComponent( model );
		model.setOpacity( 1.0 );
		camera.moveAndOrientTo( this.scene.createOffsetStandIn( pov.getInternal() ), 0.5 );
	}
	
	protected edu.cmu.cs.dennisc.math.AffineMatrix4x4 calculateMarkerGoodLookAt( edu.cmu.cs.dennisc.math.AffineMatrix4x4 rv, PerspectiveCameraMarker target) {
		AffineMatrix4x4 markerTransform = target.getTransformation(AsSeenBy.SCENE);
		Point3 newLocation = new Point3(markerTransform.translation);
		newLocation.add( Vector3.createMultiplication(markerTransform.orientation.backward, 3.0));
		newLocation.add( Vector3.createMultiplication(markerTransform.orientation.up, 3.0));
		newLocation.add( Vector3.createMultiplication(markerTransform.orientation.right, -1.0));
		Vector3 pointAtMarker = Vector3.createSubtraction( markerTransform.translation, newLocation );
		pointAtMarker.normalize();
		
		OrthogonalMatrix3x3 pointAtOrientation = OrthogonalMatrix3x3.createFromForwardAndUpGuide(pointAtMarker, markerTransform.orientation.up);
		rv.translation.set(newLocation);
		rv.orientation.setValue(pointAtOrientation);
		return rv;
		
	}
	
	private void getGoodLookAtShowInstanceAndReturnCamera( org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera, org.alice.apis.moveandturn.PerspectiveCameraMarker marker ) {
		boolean wasShowing = marker.isShowing();
		marker.setShowing(true);
		marker.setOpacity( 0.0, org.alice.apis.moveandturn.Composite.RIGHT_NOW );
		org.alice.apis.moveandturn.PointOfView pov = camera.getPointOfView( this.scene );
		camera.moveAndOrientTo(this.scene.createOffsetStandIn( calculateMarkerGoodLookAt(edu.cmu.cs.dennisc.math.AffineMatrix4x4.createNaN(), marker) ), .5);
		this.scene.addComponent( marker );
		marker.setOpacity( 1.0 );
		camera.moveAndOrientTo( this.scene.createOffsetStandIn( pov.getInternal() ), 1.0 );
		marker.setShowing(wasShowing);
	}


	private edu.cmu.cs.dennisc.scenegraph.Background cameraBackground = new edu.cmu.cs.dennisc.scenegraph.Background();

	protected void updateSceneBasedOnScope() {
//		//		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.getIDE().getFocusedCode();
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = this.getIDE().getTypeInScope();
//		if( type != null ) {
//			edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
//			if( sceneField != null ) {
//				edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> sceneType = sceneField.getValueType();
//				boolean isSceneScope = type.isAssignableFrom( sceneType ) || this.sidePane.isExpanded();
//
//				java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneType.getDeclaredFields();
//
//				try {
//					edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = this.getOnscreenLookingGlass().getCameraAt( 0 );
//					if( isSceneScope ) {
//						camera.background.setValue( null );
//					} else {
//						cameraBackground.color.setValue( edu.cmu.cs.dennisc.color.Color4f.BLACK );
//						camera.background.setValue( cameraBackground );
//					}
//				} catch( Throwable t ) {
//					//pass
//				}
//				//				Object sceneInstanceInJava = this.getInstanceInJavaForField( sceneField );
//				//				if( sceneInstanceInJava instanceof org.alice.apis.moveandturn.Scene ) {
//				//					org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)sceneInstanceInJava;
//				//					if( isSceneScope ) {
//				//						scene.setAtmosphereColor( new org.alice.apis.moveandturn.Color( 0.75f, 0.75f, 1.0f ), org.alice.apis.moveandturn.Scene.RIGHT_NOW );
//				//					} else {
//				//						scene.setAtmosphereColor( org.alice.apis.moveandturn.Color.BLACK, org.alice.apis.moveandturn.Scene.RIGHT_NOW );
//				//					}
//				//				}
//				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : fields ) {
//					Object instanceInJava = this.getInstanceInJavaForField( field );
//					if( instanceInJava instanceof org.alice.apis.moveandturn.Model ) {
//						org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instanceInJava;
//						if( isSceneScope || type.isAssignableTo( model.getClass() ) ) {
//							model.setColor( org.alice.apis.moveandturn.Color.WHITE, org.alice.apis.moveandturn.Model.RIGHT_NOW );
//							//							model.setOpacity( 1.0f, org.alice.apis.moveandturn.Model.RIGHT_NOW );
//						} else {
//							model.setColor( new org.alice.apis.moveandturn.Color( 0.25, 0.25, 0.25 ), org.alice.apis.moveandturn.Model.RIGHT_NOW );
//							//							model.setOpacity( 0.125f, org.alice.apis.moveandturn.Model.RIGHT_NOW );
//						}
//					}
//				}
//			}
//		}
//		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "force repaint" );
//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
//			public void run() {
//				if( onscreenLookingGlass != null ) {
//					onscreenLookingGlass.repaint();
//				}
//			}
//		} );
	}

	private void handleFocusedCodeChanged( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		this.updateSceneBasedOnScope();
		if( code != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = code.getDeclaringType();
			if( type != null ) {
				edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().getSelectedItem();
				if( accessible instanceof AbstractField ) {
					AbstractField selectedField = (AbstractField)accessible;
					if( selectedField != null ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> selectedType = selectedField.getValueType();
						if( type.isAssignableFrom( selectedType ) ) {
							//pass
						} else {
							edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
							if( sceneField != null ) {
								edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> sceneType = sceneField.getValueType();
								if( type.isAssignableFrom( sceneType ) ) {
									//pass
								} else {
									for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
										if( type.isAssignableFrom( field.getValueType() ) ) {
											org.alice.ide.croquet.models.ui.AccessibleListSelectionState.getInstance().setSelectedItem( field );
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		this.updateFieldLabels();
	}

	private void updateFieldLabels() {
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "update field labels" );
	}
	
	@Override
	protected void putFieldForInstanceInJava( Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super.putFieldForInstanceInJava( instanceInJava, field );
		if( instanceInJava instanceof org.alice.apis.moveandturn.Transformable ) {
			org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instanceInJava;
			transformable.realizeIfNecessary();
			org.alice.apis.moveandturn.PickHintUtilities.setPickHint( transformable );
		}
	}

	private void setCameraMarkerVisibility(boolean isShowing)
	{
		for (AbstractField field : this.sceneType.fields)
		{
			if (field.getDesiredValueType().isAssignableTo(CameraMarker.class))
			{
				CameraMarker marker = this.getInstanceInJavaForField(field, org.alice.apis.moveandturn.CameraMarker.class);
				if (marker != null)
				{
					marker.setShowing(isShowing);
				}
			}
		}
	}
	
	private void updateCameraMarkers()
	{
		for (AbstractField field : this.sceneType.fields)
		{
			if (field.getDesiredValueType().isAssignableTo(CameraMarker.class))
			{
				CameraMarker marker = this.getInstanceInJavaForField(field, org.alice.apis.moveandturn.CameraMarker.class);
				if (marker != null)
				{
					if (marker instanceof PerspectiveCameraMarker)
					{
						updateCameraMarkerToCamera((PerspectiveCameraMarker)marker, (SymmetricPerspectiveCamera)getSGCameraForCreatingMarker());
					}
				}
			}
		}
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
	
	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		//Detach the previous scene's cameras
		clearCameras();
		this.scene = null;
		//Stop listening to the fields on the previous scene
		if( this.sceneType != null ) {
			this.sceneType.fields.removeListPropertyListener( this.globalFieldsAdapter );
		}		
		
		//Make the new scene
		Object rv = super.createScene( sceneField );
		this.sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneField.getValueType();
		
		//Start listening to fields on the new scene
		//Trigger a "fieldsSet" on all sceneEditorFieldObservers to have them update to the new fields
		if( this.sceneType != null ) {
			this.sceneType.fields.addListPropertyListener( this.globalFieldsAdapter );
			for (SceneEditorFieldObserver observer : this.fieldObservers)
			{
				observer.fieldsSet(this.sceneType.fields.getValue());
			}
		}
		else
		{
			for (SceneEditorFieldObserver observer : this.fieldObservers)
			{
				observer.fieldsSet(null);
			}
		}
		
		//Run the GeneratedSetUp method to get everything in its starting place
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = getIDE().getPerformEditorGeneratedSetUpMethod();
		this.getVM().invokeEntryPoint( method, rv );
		
		
		refreshMarkerLists();
		
		//Set up the scene
		this.scene = (org.alice.apis.moveandturn.Scene)((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)rv).getInstanceInJava();
		this.scene.setOwner( new org.alice.apis.moveandturn.SceneOwner() {
			private double simulationSpeedFactor = 1.0;

			public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
				return onscreenLookingGlass;
			}
			public void setSimulationSpeedFactor( Number simulationSpeedFactor ) {
				this.simulationSpeedFactor = simulationSpeedFactor.doubleValue();
			}
			public Double getSimulationSpeedFactor() {
				return this.simulationSpeedFactor;
			}
			public void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
				try {
					animator.invokeAndWait( animation, animationObserver );
				} catch( InterruptedException ie ) {
					throw new RuntimeException( ie );
				} catch( java.lang.reflect.InvocationTargetException ite ) {
					throw new RuntimeException( ite );
				}
			}
		} );
		
		//Turn animation off on the main view selector so all sets make it go instantly
		this.mainCameraViewTracker.setShouldAnimate(false);
		CameraMarker selectedCameraMarker = this.mainCameraMarkerList.getSelectedItem();
		this.mainCameraMarkerList.setSelectedItem(null);
		
		//Find the new scene's main perspective camera
		this.sgPerspectiveCamera = null;
		for( int i = 0; i < this.onscreenLookingGlass.getCameraCount(); i++ ) {
			if( this.onscreenLookingGlass.getCameraAt( 0 ) instanceof SymmetricPerspectiveCamera ) {
				this.sgPerspectiveCamera = (SymmetricPerspectiveCamera)this.onscreenLookingGlass.getCameraAt( 0 );
			}
		}
		assert this.sgPerspectiveCamera != null;
		//Add and set up the snap grid (this needs to happen before setting the camera)
		this.scene.getSGComposite().addComponent(this.snapGrid);
		this.snapGrid.setTranslationOnly(0, 0, 0, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE);
		this.snapGrid.setShowing(this.snapState.shouldShowSnapGrid());
		
		//Initialize stuff that needs a camera
		this.setCameras(this.sgPerspectiveCamera, this.sgOrthographicCamera);
		
		//Add the orthographic camera to this scene
		this.scene.getSGComposite().addComponent( this.sgOrthographicCamera.getParent() );
		//Add the orthographic markers
		addCameraMarkersToScene(this.scene);
		this.openingSceneMarker.setLocalTransformation(this.sgPerspectiveCamera.getAbsoluteTransformation());
		
		AffineMatrix4x4 sceneViewTransform = getSGCameraForCreatingMarker().getTransformation( AsSeenBy.SCENE.getSGReferenceFrame() );
		sceneViewTransform.applyTranslationAlongYAxis(12.0);
		sceneViewTransform.applyTranslationAlongZAxis(10.0);
		sceneViewTransform.applyRotationAboutXAxis(new AngleInDegrees(-40));
		if (this.sceneViewMarker.getSGTransformable().getParent() != null)
		{
			this.sceneViewMarker.getSGTransformable().setTransformation( sceneViewTransform, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE );
		}
		else
		{
			this.sceneViewMarker.setLocalTransformation(sceneViewTransform);
		}
		
		//Make the markers visible
		setCameraMarkerVisibility(true);
		this.mainCameraMarkerList.setSelectedItem(selectedCameraMarker);
		//Turn animation back on
		this.mainCameraViewTracker.setShouldAnimate(true);
		
		if (this.sidePane != null)
		{
			this.sidePane.setSceneGraphRoot();
			this.sidePane.setLookingglassRoot();
		}
		
		return rv;
	}
	
	private void updateCameraMarkerToCamera( org.alice.apis.moveandturn.PerspectiveCameraMarker cameraMarker, SymmetricPerspectiveCamera perspectiveCamera)
	{
		setViewingAngleOnMarker(cameraMarker, perspectiveCamera);
		cameraMarker.setFarClippingPlane(perspectiveCamera.farClippingPlaneDistance.getValue());
	}
	
	private void setViewingAngleOnMarker( org.alice.apis.moveandturn.PerspectiveCameraMarker cameraMarker, SymmetricPerspectiveCamera perspectiveCamera )
	{
		if (this.onscreenLookingGlass != null)
		{
			cameraMarker.setViewingAngle(this.onscreenLookingGlass.getActualHorizontalViewingAngle(perspectiveCamera), this.onscreenLookingGlass.getActualVerticalViewingAngle(perspectiveCamera));
		}
	}

	private void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field, FieldAndInstanceMapper mapper) {
		SetUpMethodGenerator.fillInAutomaticSetUpMethod( bodyStatementsProperty, isThis, field, mapper.getInstanceInJavaVMForField( field ), mapper );
	}
	
	@Override
	public void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, FieldAndInstanceMapper fieldAndInstanceMapper ) {
		//Set the camera to have the point of view of the opening scene marker
		AffineMatrix4x4 currentCameraTransformable = this.sgPerspectiveCamera.getAbsoluteTransformation();
		edu.cmu.cs.dennisc.scenegraph.Transformable cameraParent = (edu.cmu.cs.dennisc.scenegraph.Transformable)this.sgPerspectiveCamera.getParent();
		cameraParent.setTransformation(this.openingSceneMarker.getTransformation(AsSeenBy.SCENE), this.scene.getSGReferenceFrame());
		
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
		this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField, fieldAndInstanceMapper );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : this.sceneType.getDeclaredFields() ) {
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field, fieldAndInstanceMapper );
		}
		
		//Set the camera back to its original position
		cameraParent.setTransformation(currentCameraTransformable, this.scene.getSGReferenceFrame());
	}

	@Override
	public void setOmittingThisFieldAccesses( boolean isOmittingThisFieldAccesses ) {
		this.updateFieldLabels();
	}

	//	public void editPerson( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
	//		org.alice.apis.stage.Person person = this.getInstanceInJavaForField( field, org.alice.apis.stage.Person.class );
	//		if( person != null ) {
	//			org.alice.stageide.personeditor.PersonEditorInputPane inputPane = new org.alice.stageide.personeditor.PersonEditorInputPane( person );
	//			org.alice.apis.stage.Person result = inputPane.showInJDialog( this.getIDE() );
	//			edu.cmu.cs.dennisc.print.PrintUtilities.println( result );
	//		}
	//	}

	public edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		return this.onscreenLookingGlass;
	}
	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCameraForCreatingThumbnails() {
		return this.onscreenLookingGlass.getCameraAt( 0 );
	}

	public edu.cmu.cs.dennisc.scenegraph.AbstractCamera getSGCameraForCreatingMarker() {
		return this.sgPerspectiveCamera;
	}

	public edu.cmu.cs.dennisc.scenegraph.OrthographicCamera getSGOrthographicCamera() {
		return this.sgOrthographicCamera;
	}

	public edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera getSGPerspectiveCamera() {
		return this.sgPerspectiveCamera;
	}

	//TODO: TAKE THIS OUT. TEMPORARY
	public Scene getScene()
	{
		return this.scene;
	}
	
	private static final String DEFAULT_CAMERA_MARKER_NAME;
	private static final String[] COLOR_NAMES;
	private static final org.alice.apis.moveandturn.Color[] COLORS;
	
	static
	{
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( SceneViewManagerPanel.class.getPackage().getName() + ".cameraMarkers" );
		DEFAULT_CAMERA_MARKER_NAME = resourceBundle.getString("defaultMarkerName");
		
		String[] colorNames = {
				resourceBundle.getString("red"),
				resourceBundle.getString("green"),
//				resourceBundle.getString("blue"),
				resourceBundle.getString("magenta"),
				resourceBundle.getString("yellow"),
//				resourceBundle.getString("cyan"),
				resourceBundle.getString("orange"),
				resourceBundle.getString("pink"),
				resourceBundle.getString("purple"),
		};
		COLOR_NAMES = colorNames;
		
		org.alice.apis.moveandturn.Color[] colors = { 
				org.alice.apis.moveandturn.Color.RED,
				org.alice.apis.moveandturn.Color.GREEN,
//				org.alice.apis.moveandturn.Color.BLUE,
				org.alice.apis.moveandturn.Color.MAGENTA,
				org.alice.apis.moveandturn.Color.YELLOW,
//				org.alice.apis.moveandturn.Color.CYAN,
				org.alice.apis.moveandturn.Color.ORANGE,
				org.alice.apis.moveandturn.Color.PINK,
				org.alice.apis.moveandturn.Color.PURPLE
			};
		COLORS = colors;
	}
	
	
	
	private static int getColorIndexForName(String name)
	{
		for (int i=0; i<COLOR_NAMES.length; i++)
		{
			if (name.contains(COLOR_NAMES[i]))
			{
				return i;
			}
		}
		return -1;
	}
	
	private static String getIconNameForMarkerName(String markerName)
	{
		int colorIndex = getColorIndexForName(markerName);
		if (colorIndex != -1)
		{
			return "markerIcon_"+COLOR_NAMES[colorIndex]+".png"; 
		}
		else return "markerIcon_White.png";
	}
	
	public static Icon getIconForMarkerName(String markerName)
	{
		String iconName = getIconNameForMarkerName(markerName);
		if (iconName == null)
		{
			iconName = "images/markerIcon_White.png";
		}
		else
		{
			iconName = "images/"+iconName;
		}
		return new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource(iconName));
	}
	
	public static Icon getHighlightIconForMarkerName(String markerName)
	{
		String iconName = getIconNameForMarkerName(markerName);
		if (iconName == null)
		{
			iconName = "images/markerIcon_White.png";
		}
		else
		{
			iconName = "images/"+iconName;
		}
		return new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource(iconName));
	}
	
	public static org.alice.apis.moveandturn.Color getColorForMarkerName(String markerName)
	{
		int colorIndex = getColorIndexForName(markerName);
		if (colorIndex != -1)
		{
			return COLORS[colorIndex]; 
		}
		else 
		{	
			return null;
		}
	}
	
	private static String makeMarkerName(int colorIndex, int addOnNumber)
	{
		String markerName = DEFAULT_CAMERA_MARKER_NAME + "_" + COLOR_NAMES[colorIndex];
		if (addOnNumber > 0)
		{
			markerName += "_"+ Integer.toString( addOnNumber );
		}
		return markerName;
	}
	
	private String getNameForCameraMarker( TypeDeclaredInAlice ownerType ) {
		FieldNameValidator nameValidator = new FieldNameValidator( ownerType );
		int colorIndex = 0;
		int addOnNumber = 0;
		String markerName = makeMarkerName(colorIndex, addOnNumber);
		while( nameValidator.getExplanationIfOkButtonShouldBeDisabled( markerName ) != null ) {
			colorIndex++;
			if (colorIndex >= COLOR_NAMES.length)
			{
				colorIndex = 0;
				addOnNumber++;
			}
			markerName = makeMarkerName(colorIndex, addOnNumber);
		}
		return markerName;
	}
	
	private org.alice.apis.moveandturn.Color getColorForMarker() {
		int colorCounts[] = new int[COLORS.length];
		for (int i=0; i<colorCounts.length; i++) { colorCounts[i] = 0; } 
		for (FieldDeclaredInAlice markerField : this.sceneMarkerFieldList)
		{
			CameraMarker marker = this.getCameraMarkerForField(markerField);
			Color4f markerColor = marker.getMarkerColor();
			for (int i=0; i<COLORS.length; i++)
			{
				if (markerColor.equals(COLORS[i].getInternal()))
				{
					colorCounts[i]++;
				}
			}
		}
		int minIndex = 0;
		for (int i=0; i<colorCounts.length; i++)
		{
			if (colorCounts[i] < colorCounts[minIndex])
			{
				minIndex = i;
			}
		}
		return COLORS[minIndex];
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
		switchToCamera( this.sgOrthographicCamera );
		this.globalDragAdapter.makeCameraActive( this.sgOrthographicCamera );
		this.mainCameraNavigatorWidget.setToOrthographicMode();
	}

	public void switchToPerspectiveCamera() {
		switchToCamera( this.sgPerspectiveCamera );
		this.globalDragAdapter.makeCameraActive( this.sgPerspectiveCamera );
		this.mainCameraNavigatorWidget.setToPerspectiveMode();
	}
	
	public CameraMarker getActiveCameraMarker()
	{
		return this.mainCameraMarkerList.getSelectedItem();
	}
	
	public List<CameraMarker> getOrthographicCameraMarkers()
	{
		return this.orthographicCameraMarkers;
	}
	
	private void createOrthographicCameraMarkers()
	{
		this.orthographicCameraMarkers.clear();
		this.topOrthoMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		this.topOrthoMarker.setIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/topIcon.png")));
		this.topOrthoMarker.setHighlightedIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/topIcon_highlighted.png")));
		AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
		topTransform.translation.y = 10;
		topTransform.translation.z = -10;
		topTransform.orientation.up.set( 0, 0, 1 );
		topTransform.orientation.right.set( -1, 0, 0 );
		topTransform.orientation.backward.set( 0, 1, 0 );
		assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		this.topOrthoMarker.setLocalTransformation( topTransform );
		ClippedZPlane picturePlane = new ClippedZPlane();
		picturePlane.setCenter(0, 0);
		picturePlane.setHeight(16);
		this.topOrthoMarker.setPicturePlane(picturePlane);
		orthographicCameraMarkers.add(this.topOrthoMarker);

		this.sideOrthoMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		this.sideOrthoMarker.setIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/sideIcon.png")));
		this.sideOrthoMarker.setHighlightedIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/sideIcon_highlighted.png")));
		AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
		sideTransform.translation.x = 10;
		sideTransform.translation.y = 1;
		sideTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()) );
		assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		this.sideOrthoMarker.setLocalTransformation( sideTransform );
		picturePlane.setHeight(4);
		this.sideOrthoMarker.setPicturePlane(picturePlane);
		orthographicCameraMarkers.add(this.sideOrthoMarker);

		this.frontOrthoMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		this.frontOrthoMarker.setIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/frontIcon.png")));
		this.frontOrthoMarker.setHighlightedIcon(new javax.swing.ImageIcon(MoveAndTurnSceneEditor.class.getResource("images/frontIcon_highlighted.png")));
		AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
		frontTransform.translation.z = -10;
		frontTransform.translation.y = 1;
		frontTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessPositiveZAxis(), Vector3.accessPositiveYAxis()) );
		assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		this.frontOrthoMarker.setLocalTransformation( frontTransform );
		picturePlane.setHeight(4);
		this.frontOrthoMarker.setPicturePlane(picturePlane);
		orthographicCameraMarkers.add(this.frontOrthoMarker);
		
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( MoveAndTurnSceneEditor.class.getPackage().getName() + ".cameraViews" );
		this.openingSceneMarker.setName( resourceBundle.getString( "sceneCameraView" ) );
		this.sceneViewMarker.setName( resourceBundle.getString( "layoutPerspectiveView" ) );
		this.topOrthoMarker.setName( resourceBundle.getString( "topOrthographicView" ) );
		this.sideOrthoMarker.setName( resourceBundle.getString( "leftOrthographicView" ) );
		this.frontOrthoMarker.setName( resourceBundle.getString( "frontOrthographicView" ) );
	}
	
	public void addCameraMarkersToScene(Scene sceneToAddTo)
	{
		Transformable[] existingComponents = sceneToAddTo.getComponents();
		for (CameraMarker marker : this.mainCameraMarkerList)
		{
			boolean alreadyHasIt = false;
			for (Transformable t : existingComponents)
			{
				if (t == marker)
				{
					alreadyHasIt = true;
					break;
				}
			}
			if (!alreadyHasIt)
			{
				if (marker.getVehicle() != null)
				{
					marker.setVehicle(null);
				}
				sceneToAddTo.addComponent(marker);
			}
		}
	}

	public Tuple2< FieldDeclaredInAlice, org.alice.apis.moveandturn.BookmarkCameraMarker > createCameraMarkerField( TypeDeclaredInAlice ownerType ) {
		CreateCameraMarkerActionOperation.getInstance().setEnabled(false);
		String markerName = getNameForCameraMarker( ownerType );

		org.alice.apis.moveandturn.BookmarkCameraMarker cameraMarker = new org.alice.apis.moveandturn.BookmarkCameraMarker();
		cameraMarker.setName( markerName );
//		cameraMarker.setMarkerColor( getColorForMarker().getInternal() );
		cameraMarker.setShowing(true);
		org.alice.apis.moveandturn.ReferenceFrame asSeenBy = AsSeenBy.SCENE;
		cameraMarker.setLocalTransformation( getSGCameraForCreatingMarker().getTransformation( asSeenBy.getSGReferenceFrame() ) );
		updateCameraMarkerToCamera(cameraMarker, (SymmetricPerspectiveCamera)getSGCameraForCreatingMarker());

		edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.BookmarkCameraMarker.class );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraMarkerField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( markerName, org.alice.apis.moveandturn.BookmarkCameraMarker.class, initializer );
		cameraMarkerField.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		cameraMarkerField.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );
		
//		this.sceneMarkerFieldList.setSelectedItem(cameraMarkerField);
		
		return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( cameraMarkerField, cameraMarker );
	}

	public void moveActiveCameraToMarker(FieldDeclaredInAlice markerField)
	{
		CameraMarker marker = this.getCameraMarkerForField(markerField);
		if (marker != null)
		{
			org.alice.apis.moveandturn.AbstractCamera mvCamera = (org.alice.apis.moveandturn.AbstractCamera)Element.getElement(this.sgPerspectiveCamera);
			mvCamera.moveAndOrientTo(marker);
		}
	}
	
	public List< FieldDeclaredInAlice > getDeclaredFields() {
		List< FieldDeclaredInAlice > declaredFields = new LinkedList< FieldDeclaredInAlice >();
		if( org.alice.ide.IDE.getSingleton().getSceneType() != null ) {
			declaredFields.addAll( org.alice.ide.IDE.getSingleton().getSceneType().getDeclaredFields() );
		}
		return declaredFields;
	}

	protected void paintHorizonLine(Graphics graphics, LightweightOnscreenLookingGlass lookingGlass, OrthographicCamera camera)
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
				graphics.setColor(Color.BLACK);
				graphics.drawLine(0, horizonLinePixelVal, lookingGlassSize.width, horizonLinePixelVal);
			}
		}
	}
	
	public void setHandleVisibilityForObject( edu.cmu.cs.dennisc.scenegraph.Transformable object, boolean handlesVisible)
	{
		this.globalDragAdapter.setHandleShowingForObject(object, handlesVisible);
	}
	
	public boolean isObjectSelected( edu.cmu.cs.dennisc.scenegraph.Transformable object )
	{
		return this.globalDragAdapter.getSelectedObject() == object;
	}
	
	public boolean isCameraMarkerActive( CameraMarker marker )
	{
		CameraMarker activeMarker = this.mainCameraMarkerList.getSelectedItem();
		return marker == activeMarker;
	}
	
	public SnapState getSnapState()
	{
		return this.snapState;
	}
	
	public void setShowSnapGrid( boolean showSnapGrid )
	{
		this.snapGrid.setShowing(showSnapGrid);
	}
	
	public void setSnapGridSpacing( final double spacing )
	{
		snapGrid.setSpacing(spacing);
	}
	
	
	public void initialized( LookingGlassInitializeEvent e )
	{
	}

	public void cleared( LookingGlassRenderEvent e )
	{
	}
	
	public void rendered( LookingGlassRenderEvent e )
	{	
		if (this.onscreenLookingGlass.getCameraCount() > 0 && this.onscreenLookingGlass.getCameraAt(0) instanceof OrthographicCamera)
		{
			paintHorizonLine(e.getGraphics2D(), this.onscreenLookingGlass, (OrthographicCamera)this.onscreenLookingGlass.getCameraAt(0));
		}
	}
	
	public void resized( LookingGlassResizeEvent e )
	{
		updateCameraMarkers();
	}
	
	public void displayChanged( LookingGlassDisplayChangeEvent e )
	{
	}

	public void dragStarted(DragAndDropContext dragAndDropContext) {
		DragComponent dragSource = dragAndDropContext.getDragSource();
		dragSource.showDragProxy();
		Operation<?> operation = dragSource.getLeftButtonClickOperation();
		if (operation instanceof GalleryFileOperation)
		{
			((GalleryFileOperation)operation).setDesiredTransformation(null);
		}
	}

	public void dragStopped(DragAndDropContext dragAndDropContext) 
	{
		this.globalDragAdapter.dragExited(dragAndDropContext);
	}

	public void dragEntered(DragAndDropContext dragAndDropContext) 
	{
	}
	public void dragExited(DragAndDropContext dragAndDropContext, boolean isDropRecipient) 
	{
	}

	private boolean overLookingGlass = false;
	
	private boolean isDropLocationOverLookingGlass(DragAndDropContext dragAndDropContext)
	{
		java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		java.awt.Point pointInLookingGlass = javax.swing.SwingUtilities.convertPoint( eSource.getComponent(), eSource.getPoint(), this.lookingGlassPanel.getAwtComponent() );
		return this.lookingGlassPanel.getAwtComponent().contains(pointInLookingGlass);
	}
	
	public edu.cmu.cs.dennisc.croquet.DropSite dragUpdated(DragAndDropContext dragAndDropContext) {
		if (isDropLocationOverLookingGlass(dragAndDropContext))
		{
			if (!overLookingGlass)
			{
				overLookingGlass = true;
				this.globalDragAdapter.dragEntered(dragAndDropContext);
			}
			this.globalDragAdapter.dragUpdated(dragAndDropContext);
		}
		else
		{
			if (overLookingGlass)
			{
				overLookingGlass = false;
				this.globalDragAdapter.dragExited(dragAndDropContext);
			}
		}
		return null;
	}

	public edu.cmu.cs.dennisc.croquet.JComponent<?> getViewController() {
		return this;
	}

	public boolean isPotentiallyAcceptingOf(DragComponent source) {
		return source instanceof org.alice.stageide.gallerybrowser.GalleryDragComponent;
	}
	
	public Operation<?> dragDropped(DragAndDropContext dragAndDropContext) {
		DragComponent dragSource = dragAndDropContext.getDragSource();
		if (isDropLocationOverLookingGlass(dragAndDropContext))
		{
			Operation<?> operation = dragSource.getLeftButtonClickOperation();
			if (operation instanceof GalleryFileOperation)
			{
				AffineMatrix4x4 dropTargetPosition = this.globalDragAdapter.getDropTargetTransformation();
				((GalleryFileOperation)operation).setDesiredTransformation(dropTargetPosition);
			}
			return operation;
		}
		return null;
	}
	
}
