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
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.ide.memberseditor.MembersEditor;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.OrthographicCameraMarker;
import org.alice.apis.moveandturn.PerspectiveCameraMarker;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.Transformable;
import org.alice.ide.IDE;
import org.alice.ide.name.validators.FieldNameValidator;
import org.alice.interact.SnapGrid;
import org.alice.interact.SnapState;
import org.alice.interact.AbstractDragAdapter.CameraView;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.viewmanager.CameraFieldAndMarker;
import org.alice.stageide.sceneeditor.viewmanager.LookingGlassViewSelector;
import org.alice.stageide.sceneeditor.viewmanager.SceneViewManagerPanel;
import org.alice.stageide.sceneeditor.viewmanager.SnapControlPanel;
import org.alice.stageide.sceneeditor.viewmanager.StartingCameraViewManager;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.alice.ast.AbstractType;
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
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava;
import edu.cmu.cs.dennisc.croquet.AbstractButton;
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
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor implements LookingGlassListener{
	private static final int INSET = 8;

	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createLightweightOnscreenLookingGlass();
	
	private class LookingGlassPanel extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JPanel > {
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = MoveAndTurnSceneEditor.this.onscreenLookingGlass.getJPanel();
			rv.setLayout( new javax.swing.SpringLayout() );
			return rv;
		}
	}
	private LookingGlassPanel lookingGlassPanel = new LookingGlassPanel();
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane splitPane = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane();
	private SidePane sidePane; //= new SidePane();

	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();

	private org.alice.apis.moveandturn.Scene scene;
//	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;
//	private java.util.List< FieldTile > fieldTiles = new java.util.LinkedList< FieldTile >();

	private edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera = null;
	private edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgPerspectiveCamera = null;
	private org.alice.interact.CameraNavigatorWidget mainCameraNavigatorWidget = null;
	
	private LookingGlassViewSelector mainViewSelector = null;
	private int expandedViewSelectedIndex = -1;
	
	//SNAP STATE
	protected SnapState snapState = new SnapState();
	protected SnapGrid snapGrid;

	private org.alice.interact.GlobalDragAdapter globalDragAdapter;
	private edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > codeSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
		public void changed( edu.cmu.cs.dennisc.alice.ast.AbstractCode next ) {
			MoveAndTurnSceneEditor.this.handleFocusedCodeChanged( next );
		}
	};
	private edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver<edu.cmu.cs.dennisc.alice.ast.AbstractField> fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver<edu.cmu.cs.dennisc.alice.ast.AbstractField>() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.AbstractField nextValue) {
			MoveAndTurnSceneEditor.this.handleFieldSelection( nextValue );
		}
	};
	
//	private org.alice.ide.IDE.FieldSelectionObserver fieldSelectionObserver = new org.alice.ide.IDE.FieldSelectionObserver() {
//		public void fieldSelectionChanging( edu.cmu.cs.dennisc.alice.ast.AbstractField previousField, edu.cmu.cs.dennisc.alice.ast.AbstractField nextField ) {
//		}
//		public void fieldSelectionChanged( edu.cmu.cs.dennisc.alice.ast.AbstractField previousField, edu.cmu.cs.dennisc.alice.ast.AbstractField nextField ) {
//			MoveAndTurnSceneEditor.this.handleFieldSelection( nextField );
//		}
//	};
//	
//	private javax.swing.ButtonGroup fieldTileGroup = new javax.swing.ButtonGroup();

	private FieldRadioButtons fieldRadioButtons;
	
	private List<CameraMarker> orthographicCameraMarkers = new LinkedList<CameraMarker>();

//	public MoveAndTurnSceneEditor() {
//		javax.swing.JPanel lgPanel = this.getLGPanel();
//		javax.swing.SpringLayout springLayout = new javax.swing.SpringLayout();
//		lgPanel.setLayout( springLayout );
//
//		this.sidePane = new SidePane(this);
//		
//		//		this.splitPane.setBackground( java.awt.Color.GRAY );
//		this.splitPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
//		this.splitPane.setResizeWeight( 1.0 );
//		this.splitPane.setDividerLocation( 1.0 );
//		this.setLayout( new java.awt.BorderLayout() );
//		this.addComponent( this.splitPane, java.awt.BorderLayout.CENTER );
//		
//	}
	
	public MoveAndTurnSceneEditor() {
		this.sidePane = new SidePane(this);
		this.splitPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.splitPane.setResizeWeight( 1.0 );
		this.splitPane.setDividerProportionalLocation( 1.0 );
		//this.splitPane.setDividerLocation( 1.0 );

		this.addComponent( this.splitPane, Constraint.CENTER );
		this.snapGrid = new SnapGrid();
	}

	//	private boolean isRenderingEnabled = true;
	//	@Override
	//	public void paintChildren( java.awt.Graphics g ) {
	//		if( this.isRenderingEnabled ) {
	//			super.paintChildren( g );
	//		}
	//	}
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

	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		this.initializeIfNecessary();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( this.automaticDisplayListener );

		this.splitPane.setLeftComponent( this.lookingGlassPanel );
		
		org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().addAndInvokeValueObserver( this.codeSelectionObserver );
		org.alice.ide.IDE.getSingleton().getFieldSelectionState().addAndInvokeValueObserver( this.fieldSelectionObserver );
	}

	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.splitPane.setLeftComponent( null );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().removeValueObserver( this.codeSelectionObserver );
		org.alice.ide.IDE.getSingleton().getFieldSelectionState().removeValueObserver( this.fieldSelectionObserver );
		super.handleRemovedFrom( parent );
	}

	private javax.swing.JPanel getLGPanel() {
		return this.onscreenLookingGlass.getJPanel();
	}
//	private javax.swing.SpringLayout getLGSpringLayout() {
//		return (javax.swing.SpringLayout)this.getLGPanel().getLayout();
//	}
//	private void refreshFields() {
//	javax.swing.JPanel lgPanel = this.lightweightLookingGlass.getAwtComponent(); 
//	javax.swing.SpringLayout springLayout = (javax.swing.SpringLayout)lgPanel.getLayout();
//	javax.swing.ButtonModel selection = this.fieldTileGroup.getSelection();
//	if( selection != null ) {
//		this.fieldTileGroup.setSelected( selection, false );
//	}
//	for( FieldTile fieldTile : this.fieldTiles ) {
//		springLayout.removeLayoutComponent( fieldTile.getAwtComponent() );
//		this.onscreenLookingGlass.getJPanel().remove( fieldTile.getAwtComponent() );
//		this.fieldTileGroup.remove( fieldTile.getAwtComponent() );
//	}
//	this.fieldTiles.clear();
//	if( this.rootField != null ) {
//		FieldTile rootFieldTile = this.createFieldTile( this.rootField );
//		//rootFieldTile.setOpaque( true );
//		edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( lgPanel, rootFieldTile.getAwtComponent(), INSET );
//		this.fieldTiles.add( rootFieldTile );
//		java.awt.Component prev = rootFieldTile.getAwtComponent();
//		if( rootField != null ) {
//			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
//				FieldTile fieldTile = createFieldTile( field );
//				//fieldTile.setOpaque( true );
//				this.onscreenLookingGlass.getJPanel().add( fieldTile.getAwtComponent() );
//				this.fieldTiles.add( fieldTile );
//				springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile.getAwtComponent(), 1, javax.swing.SpringLayout.SOUTH, prev );
//				springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile.getAwtComponent(), 10, javax.swing.SpringLayout.WEST, rootFieldTile.getAwtComponent() );
//				prev = fieldTile.getAwtComponent();
//			}
//		}
//	}

	private static class FieldRadioButtons extends edu.cmu.cs.dennisc.croquet.AbstractRadioButtons< AbstractField > {
		private static final int SUB_FIELD_LEFT_INSET = 10;
		private static final int INTRA_FIELD_PAD = 1;
		private javax.swing.SpringLayout springLayout;
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
		protected edu.cmu.cs.dennisc.croquet.AbstractButton<?> createButton(edu.cmu.cs.dennisc.alice.ast.AbstractField item) {
			return new FieldTile( item );
		}
		
		private edu.cmu.cs.dennisc.croquet.Component<?> previousComponent;
		private edu.cmu.cs.dennisc.croquet.Component<?> rootComponent;
		@Override
		protected void addPrologue(int count) {
			this.previousComponent = null;
			this.rootComponent = null;
		}
		@Override
		protected void addButton(AbstractButton<?> button) {
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
		}
	}

//	//TODO: REMOVE
//	public Component getViewSelector()
//	{
//		return this.mainViewSelector;
//	}
//	
//	//TODO: REMOVE
//	public Component getViewManager()
//	{
//		return this.viewManagerPanel;
//	}
	
	private void initializeIfNecessary() {
		if( this.globalDragAdapter != null ) {
			//pass
		} else {
			
			createOrthographicCameraMarkers();
			
			this.snapState = new SnapState();
			this.sidePane.setSnapState(this.snapState);
			this.globalDragAdapter = new org.alice.interact.GlobalDragAdapter(this);
			this.globalDragAdapter.setSnapState(this.snapState);
			this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );

			this.onscreenLookingGlass.addLookingGlassListener(this);
			
			this.mainCameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter, CameraView.MAIN);

			this.mainViewSelector = new LookingGlassViewSelector(this, animator);
			this.mainViewSelector.setExtraMarkerOptions(this.orthographicCameraMarkers);
			
			edu.cmu.cs.dennisc.croquet.BooleanStateOperation isSceneEditorExpandedOperation = this.getIDE().getIsSceneEditorExpandedOperation();
			final edu.cmu.cs.dennisc.croquet.CheckBox isSceneEditorExpandedCheckBox = isSceneEditorExpandedOperation.createCheckBox();
			isSceneEditorExpandedCheckBox.getAwtComponent().setUI( new IsExpandedCheckBoxUI() );
			final int X_PAD = 16;
			final int Y_PAD = 10;
			isSceneEditorExpandedCheckBox.setOpaque( false );
			isSceneEditorExpandedCheckBox.setFontSize( 18.0f );
			isSceneEditorExpandedCheckBox.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD, X_PAD, Y_PAD, X_PAD ) );
			
			
			if( mainCameraNavigatorWidget != null ) {
				mainCameraNavigatorWidget.setExpanded( isSceneEditorExpandedOperation.getState() );
			}
			isSceneEditorExpandedOperation.addValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanStateOperation.ValueObserver() {
				public void changing(boolean nextValue) {
				}
				public void changed(boolean nextValue) {
					if( mainCameraNavigatorWidget != null ) {
						mainCameraNavigatorWidget.setExpanded( nextValue );
					}
				}
			} );

			javax.swing.JPanel lgPanel = this.lookingGlassPanel.getAwtComponent();
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouthEast( lgPanel, isSceneEditorExpandedCheckBox.getAwtComponent(), INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthEast( lgPanel, this.getIDE().getRunOperation().createButton().getAwtComponent(), INSET );
			
			if (isSceneEditorExpandedOperation.getState())
			{
				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( lgPanel, this.mainViewSelector, Horizontal.EAST, -INSET, Vertical.NORTH, INSET + 30 );
			}

			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouth( lgPanel, mainCameraNavigatorWidget, INSET );

			this.fieldRadioButtons = new FieldRadioButtons();
			this.getIDE().getFieldSelectionState().register( this.fieldRadioButtons );
			
			
//			edu.cmu.cs.dennisc.croquet.GridPanel testPanel = edu.cmu.cs.dennisc.croquet.GridPanel.createSingleColumnGridPane();
//			
//			javax.swing.JRadioButton a = new javax.swing.JRadioButton( "scene" );
//			javax.swing.JRadioButton b = new javax.swing.JRadioButton( "ground" );
//			a.setOpaque( false );
//			b.setOpaque( false );
//			testPanel.getAwtComponent().add( a );
//			testPanel.getAwtComponent().add( b );
//			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouthWest( lgPanel, testPanel.getAwtComponent(), INSET );
			
			this.globalDragAdapter.setAnimator( animator );
			this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
				public void selecting( org.alice.interact.event.SelectionEvent e ) {
				}
				public void selected( org.alice.interact.event.SelectionEvent e ) {
					MoveAndTurnSceneEditor.this.handleSelection( e );
				}
			} );

			this.sidePane.setDragAdapter( this.globalDragAdapter );
		}
		createOrthographicCamera();
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
			this.sgOrthographicCamera.putBonusDataFor(org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.CAMERA);
		}
	}
	
	@Override
	public void handleExpandContractChange( boolean isExpanded ) {
		this.sidePane.setExpanded( isExpanded );
		javax.swing.JPanel lgPanel = this.lookingGlassPanel.getAwtComponent();
		if( mainCameraNavigatorWidget != null ) {
			mainCameraNavigatorWidget.setExpanded( isExpanded );
		}
		if( isExpanded ) {
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( lgPanel, fieldRadioButtons.getAwtComponent(), INSET );
		}
		if( mainCameraNavigatorWidget != null ) {
			mainCameraNavigatorWidget.setExpanded( isExpanded );
		}
		if( isExpanded ) {
			//Add the camera view controls into the split pane and show it
			this.splitPane.setRightComponent( this.sidePane );
			this.splitPane.setDividerSize( 10 );
			
			if (this.mainViewSelector != null)
			{
				//Add the camera view selector back
				edu.cmu.cs.dennisc.javax.swing.SpringUtilities.add( this.getLGPanel(), this.mainViewSelector, Horizontal.EAST, -INSET, Vertical.NORTH, INSET + 30 );
				
				//Switch the main view to the previously selected index
				if (this.expandedViewSelectedIndex != -1)
				{
					this.mainViewSelector.setSelectedIndex(this.expandedViewSelectedIndex);
				}
			}
			
		} else {
			lgPanel.remove( fieldRadioButtons.getAwtComponent() );
			this.splitPane.setRightComponent( null );
			this.splitPane.setDividerSize( 0 );
			
			//Take the camera view selector out
			if (this.mainViewSelector != null)
			{
				if (this.mainViewSelector.getParent() == this.getLGPanel())
				{
					this.getLGPanel().remove(this.mainViewSelector);
				}
			
				//Cache the currently selected view index
				this.expandedViewSelectedIndex = this.mainViewSelector.getSelectedIndex();
				
				//Switch the main view to the starting view
				CameraFieldAndMarker cfAm = this.sidePane.getStartingCameraViewManager().getSelectedView();
				if( cfAm != null ) {
					int startingViewIndex = this.mainViewSelector.getIndexForField(cfAm.field);
					assert startingViewIndex != -1;
					if (startingViewIndex != -1)
					{
						this.mainViewSelector.setSelectedIndex(startingViewIndex);
					}
				} else {
					PrintUtilities.println( "todo: handle cfAm == null" );
				}
			}
		}
		this.updateSceneBasedOnScope();
	}

	@Override
	public void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance, boolean isAnimationDesired ) {
		assert instance instanceof org.alice.apis.moveandturn.Transformable;
		final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;
		//declaringType.fields.add( field );
		this.putInstanceForField( field, transformable );
		this.getIDE().getFieldSelectionState().setValue( field );
		final org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = this.scene.findFirstMatch( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class );
		if( isAnimationDesired && camera != null && transformable instanceof org.alice.apis.moveandturn.Model ) {
			new Thread() {
				@Override
				public void run() {
					MoveAndTurnSceneEditor.this.getGoodLookAtShowInstanceAndReturnCamera( camera, (org.alice.apis.moveandturn.Model)transformable );
				}
			}.start();
		} else {
			this.scene.addComponent( transformable );
		}
		//getIDE().markChanged( "scene program addInstance" );
	}
	private void getGoodLookAtShowInstanceAndReturnCamera( org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera, org.alice.apis.moveandturn.Model model ) {
		model.setOpacity( 0.0, org.alice.apis.moveandturn.Composite.RIGHT_NOW );
		org.alice.apis.moveandturn.PointOfView pov = camera.getPointOfView( this.scene );
		camera.getGoodLookAt( model, 0.5 );
		this.scene.addComponent( model );
		model.setOpacity( 1.0 );
		camera.moveAndOrientTo( this.scene.createOffsetStandIn( pov.getInternal() ), 0.5 );
	}
	
	private void handleSelection( org.alice.interact.event.SelectionEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = e.getTransformable();
		org.alice.apis.moveandturn.Element element = org.alice.apis.moveandturn.Element.getElement( sgTransformable );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldForInstanceInJava( element );
		if( field != null ) {
			this.getIDE().getFieldSelectionState().setValue( field );
		}
	}

	private edu.cmu.cs.dennisc.scenegraph.Background cameraBackground = new edu.cmu.cs.dennisc.scenegraph.Background();

	protected void updateSceneBasedOnScope() {
		//		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = this.getIDE().getFocusedCode();
		edu.cmu.cs.dennisc.alice.ast.AbstractType type = this.getIDE().getTypeInScope();
		if( type != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
			if( sceneField != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
				boolean isSceneScope = type.isAssignableFrom( sceneType ) || this.sidePane.isExpanded();

				java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneType.getDeclaredFields();

				try {
					edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = this.getOnscreenLookingGlass().getCameraAt( 0 );
					if( isSceneScope ) {
						camera.background.setValue( null );
					} else {
						cameraBackground.color.setValue( edu.cmu.cs.dennisc.color.Color4f.BLACK );
						camera.background.setValue( cameraBackground );
					}
				} catch( Throwable t ) {
					//pass
				}
				//				Object sceneInstanceInJava = this.getInstanceInJavaForField( sceneField );
				//				if( sceneInstanceInJava instanceof org.alice.apis.moveandturn.Scene ) {
				//					org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)sceneInstanceInJava;
				//					if( isSceneScope ) {
				//						scene.setAtmosphereColor( new org.alice.apis.moveandturn.Color( 0.75f, 0.75f, 1.0f ), org.alice.apis.moveandturn.Scene.RIGHT_NOW );
				//					} else {
				//						scene.setAtmosphereColor( org.alice.apis.moveandturn.Color.BLACK, org.alice.apis.moveandturn.Scene.RIGHT_NOW );
				//					}
				//				}
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : fields ) {
					Object instanceInJava = this.getInstanceInJavaForField( field );
					if( instanceInJava instanceof org.alice.apis.moveandturn.Model ) {
						org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instanceInJava;
						if( isSceneScope || type.isAssignableTo( model.getClass() ) ) {
							model.setColor( org.alice.apis.moveandturn.Color.WHITE, org.alice.apis.moveandturn.Model.RIGHT_NOW );
							//							model.setOpacity( 1.0f, org.alice.apis.moveandturn.Model.RIGHT_NOW );
						} else {
							model.setColor( new org.alice.apis.moveandturn.Color( 0.25, 0.25, 0.25 ), org.alice.apis.moveandturn.Model.RIGHT_NOW );
							//							model.setOpacity( 0.125f, org.alice.apis.moveandturn.Model.RIGHT_NOW );
						}
					}
				}
			}
		}
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "force repaint" );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				if( onscreenLookingGlass != null ) {
					onscreenLookingGlass.repaint();
				}
			}
		} );
	}

	private void handleFocusedCodeChanged( edu.cmu.cs.dennisc.alice.ast.AbstractCode code ) {
		this.updateSceneBasedOnScope();
		if( code != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = code.getDeclaringType();
			if( type != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField selectedField = this.getIDE().getFieldSelectionState().getValue();
				if( selectedField != null ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractType selectedType = selectedField.getValueType();
					if( type.isAssignableFrom( selectedType ) ) {
						//pass
					} else {
						edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
						if( sceneField != null ) {
							edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
							if( type.isAssignableFrom( sceneType ) ) {
								//pass
							} else {
								for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
									if( type.isAssignableFrom( field.getValueType() ) ) {
										this.getIDE().getFieldSelectionState().setValue( field );
										break;
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
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "update field labels" );
	}
//
//	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
//		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
//	}
//	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getRootField() {
//		return this.rootField;
//	}
//	public void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
//		if( this.rootField != null ) {
//			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
//		}
//		this.rootField = rootField;
//		if( this.getRootField() != null ) {
//			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
//		}
//		this.refreshFields();
//	}
//	private FieldTile createFieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
//		assert field != null;
//		FieldTile rv = FieldTile.createInstance( field );
//		this.fieldTileGroup.add( rv.getAwtComponent() );
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( this.fieldTileGroup );
//		return rv;
//	}
//	private void refreshFields() {
//		javax.swing.JPanel lgPanel = this.lightweightLookingGlass.getAwtComponent(); 
//		javax.swing.SpringLayout springLayout = (javax.swing.SpringLayout)lgPanel.getLayout();
//		javax.swing.ButtonModel selection = this.fieldTileGroup.getSelection();
//		if( selection != null ) {
//			this.fieldTileGroup.setSelected( selection, false );
//		}
//		for( FieldTile fieldTile : this.fieldTiles ) {
//			springLayout.removeLayoutComponent( fieldTile.getAwtComponent() );
//			this.onscreenLookingGlass.getJPanel().remove( fieldTile.getAwtComponent() );
//			this.fieldTileGroup.remove( fieldTile.getAwtComponent() );
//		}
//		this.fieldTiles.clear();
//		if( this.rootField != null ) {
//			FieldTile rootFieldTile = this.createFieldTile( this.rootField );
//			//rootFieldTile.setOpaque( true );
//			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( lgPanel, rootFieldTile.getAwtComponent(), INSET );
//			this.fieldTiles.add( rootFieldTile );
//			java.awt.Component prev = rootFieldTile.getAwtComponent();
//			if( rootField != null ) {
//				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
//					FieldTile fieldTile = createFieldTile( field );
//					//fieldTile.setOpaque( true );
//					this.onscreenLookingGlass.getJPanel().add( fieldTile.getAwtComponent() );
//					this.fieldTiles.add( fieldTile );
//					springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile.getAwtComponent(), 1, javax.swing.SpringLayout.SOUTH, prev );
//					springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile.getAwtComponent(), 10, javax.swing.SpringLayout.WEST, rootFieldTile.getAwtComponent() );
//					prev = fieldTile.getAwtComponent();
//				}
//			}
//		}
//		this.revalidateAndRepaint();
//	}

	@Override
	protected void putFieldForInstanceInJava( Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super.putFieldForInstanceInJava( instanceInJava, field );
		if( instanceInJava instanceof org.alice.apis.moveandturn.Transformable ) {
			org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instanceInJava;
			transformable.realizeIfNecessary();
			org.alice.apis.moveandturn.PickHintUtilities.setPickHint( transformable );
		}
	}

	private void handleFieldSelection( AbstractField field ) {
		Object instance = this.getInstanceForField( field );
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}
		if( instance instanceof org.alice.apis.moveandturn.Model ) {
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instance;
			if (this.needToDoSomeSceneSetupStuff)
			{
				this.objectToBeSelectedWhenWeAreReady = model.getSGTransformable();
			}
			else
			{
				this.globalDragAdapter.setSelectedObject( model.getSGTransformable() );
			}
		}
		else if( instance instanceof org.alice.apis.moveandturn.Marker ) {
			org.alice.apis.moveandturn.Marker marker = (org.alice.apis.moveandturn.Marker)instance;
			if (this.needToDoSomeSceneSetupStuff)
			{
				this.objectToBeSelectedWhenWeAreReady = marker.getSGTransformable();
			}
			else
			{
				this.globalDragAdapter.setSelectedObject( marker.getSGTransformable() );
			}
		}
		this.updateFieldLabels();
	}

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = null;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleRemoved( e );
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		};
	};

	private void handleRemoved( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldDeclaredInAlice : e.getElements() ) {
			org.alice.apis.moveandturn.Transformable transformable = this.getInstanceInJavaForField( fieldDeclaredInAlice, org.alice.apis.moveandturn.Transformable.class );
			if( transformable != null ) {
				this.scene.removeComponent( transformable );
			}
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
	
	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		
		this.snapGrid.stopTrackingCameras();
		
		if( this.onscreenLookingGlass.getCameraCount() > 0 ) {
			onscreenLookingGlass.clearCameras();
			this.scene = null;
		}
		if( this.sceneType != null ) {
			this.sceneType.fields.removeListPropertyListener( this.listPropertyAdapter );
		}

		Object rv = super.createScene( sceneField );
		this.sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneField.getValueType();
		if( this.sceneType != null ) {
			this.sceneType.fields.addListPropertyListener( this.listPropertyAdapter );
		}
		
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = getIDE().getPerformEditorGeneratedSetUpMethod();
		this.getVM().invokeEntryPoint( method, rv );
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
		this.sgPerspectiveCamera = null;
		for( int i = 0; i < this.onscreenLookingGlass.getCameraCount(); i++ ) {
			if( this.onscreenLookingGlass.getCameraAt( 0 ) instanceof SymmetricPerspectiveCamera ) {
				this.sgPerspectiveCamera = (SymmetricPerspectiveCamera)this.onscreenLookingGlass.getCameraAt( 0 );
			}
		}
		assert this.sgPerspectiveCamera != null;

		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO: investigate initializeIfNecessary in MoveAndTurnSceneEditor" );
		this.initializeIfNecessary();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		
		
		this.scene.getSGComposite().addComponent( this.sgOrthographicCamera.getParent() );

		addOrthographicMarkersToScene(this.scene);
		
		this.scene.getSGComposite().addComponent(this.snapGrid);
		this.snapGrid.setTranslationOnly(0, 0, 0, edu.cmu.cs.dennisc.scenegraph.AsSeenBy.SCENE);
		this.snapGrid.addCamera(this.getSGPerspectiveCamera());
		this.snapGrid.addCamera(this.getSGOrthographicCamera());
		this.snapGrid.setCurrentCamera(this.getSGPerspectiveCamera());
		this.snapGrid.setShowing(this.snapState.shouldShowSnapGrid());
		
		upgradeSceneToStateOfTheArt();
		
		setCameraMarkerVisibility(true);
		
		//TODO: Probably want to add the orthographic markers to the scene here
		
		this.globalDragAdapter.addCameraView( CameraView.MAIN, this.sgPerspectiveCamera, this.sgOrthographicCamera );
		this.globalDragAdapter.makeCameraActive( this.sgPerspectiveCamera );
		
		doDelayedSceneSetupStuff();
		
		//sidePane.showSceneGraphView(this.scene.getSGComposite());
		
		//this.needToDoSomeSceneSetupStuff = true;
		
		return rv;
	}
	
	private boolean needToDoSomeSceneSetupStuff = false;
	private edu.cmu.cs.dennisc.scenegraph.Transformable objectToBeSelectedWhenWeAreReady = null;
	
	private void doDelayedSceneSetupStuff()
	{
		if (this.objectToBeSelectedWhenWeAreReady != null)
		{
			this.globalDragAdapter.setSelectedObject( this.objectToBeSelectedWhenWeAreReady );
			this.objectToBeSelectedWhenWeAreReady = null;
		}
		this.mainViewSelector.setPerspectiveCamera( this.getSGPerspectiveCamera());
		this.mainViewSelector.setOrthographicCamera( this.getSGOrthographicCamera() );
		
		//Find the camera.moveAndOrientTo(<starting camera point of view>) statement and set the StartingCameraViewManager to have that view selected
		AbstractField startingPointOfViewField = getStartingPointOfViewField();
		if (startingPointOfViewField != null)
		{	
			StartingCameraViewManager startingViewManager = this.sidePane.getStartingCameraViewManager();
			startingViewManager.setSelectedView(startingPointOfViewField);
			this.mainViewSelector.setSelectedView(startingPointOfViewField);
		}
	}
	
	private boolean doesMethodInvocationMatchMethodName(MethodInvocation invocation, String methodName)
	{
		if (invocation.method.getValue() instanceof MethodDeclaredInJava)
		{
			MethodDeclaredInJava javaMethod = (MethodDeclaredInJava)invocation.method.getValue();
			MethodReflectionProxy reflectionProxy = javaMethod.getMethodReflectionProxy();
			if (reflectionProxy.getName().equals(methodName))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean isCameraField(Expression field)
	{
		if (field instanceof FieldAccess)
		{
			FieldAccess fieldAccess = (FieldAccess)field;
			if (fieldAccess.field.getValue() instanceof FieldDeclaredInAlice)
			{
				FieldDeclaredInAlice aliceField = (FieldDeclaredInAlice)fieldAccess.field.getValue();
				boolean fieldIsCamera = aliceField.getName().equals("camera");
				boolean parentIsScene = aliceField.getParent().getName().equals("MyScene");
				return (fieldIsCamera && parentIsScene);
			}
		}
		return false;
	}

	private AbstractField getStartingPointOfViewField()
	{
		if (this.sceneType != null)
		{
			for (MethodDeclaredInAlice method : this.sceneType.methods.getValue())
			{
				if (method.getName().equals(IDE.GENERATED_SET_UP_METHOD_NAME))
				{
					for (Statement statement : method.body.getValue().statements.getValue())
					{
						if (statement instanceof ExpressionStatement)
						{
							ExpressionStatement expression = (ExpressionStatement)statement;
							if (expression.expression.getValue() instanceof MethodInvocation)
							{
								MethodInvocation invocation = (MethodInvocation)expression.expression.getValue();
								if (isCameraField(invocation.expression.getValue()) && 
									doesMethodInvocationMatchMethodName(invocation, "moveAndOrientTo") && 
									invocation.arguments.size() >= 1)
								{
									Argument primaryArgument = invocation.arguments.get(0);
									assert primaryArgument.expression.getValue() instanceof FieldAccess;
									FieldAccess startingPointOfViewFieldAccess = (FieldAccess)primaryArgument.expression.getValue();
									assert startingPointOfViewFieldAccess.field.getValue() instanceof FieldDeclaredInAlice;
									FieldDeclaredInAlice startingPointOfViewField = (FieldDeclaredInAlice)startingPointOfViewFieldAccess.field.getValue();
									assert startingPointOfViewField.getDesiredValueType().isAssignableTo(CameraMarker.class);
									return startingPointOfViewField;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private boolean needsDefaultCameraViewField()
	{
		//If we have any CameraMarker fields on the scene then that's good enough for us
		for( AbstractField field : this.sceneType.fields ) 
		{
			if (field.getDesiredValueType().isAssignableTo(org.alice.apis.moveandturn.CameraMarker.class))
			{
				return false;
			}
		}
		return true;
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
	
	private void upgradeSceneToStateOfTheArt()
	{
		if (needsDefaultCameraViewField())
		{
			org.alice.apis.moveandturn.PerspectiveCameraMarker defaultViewMarker = new org.alice.apis.moveandturn.PerspectiveCameraMarker();
			defaultViewMarker.setName( "defaultCameraView" );
			defaultViewMarker.setLocalTransformation( getSGCameraForCreatingMarker().getTransformation( AsSeenBy.SCENE.getSGReferenceFrame() ) );
			defaultViewMarker.setShowing(true);
			updateCameraMarkerToCamera(defaultViewMarker, (SymmetricPerspectiveCamera)getSGCameraForCreatingMarker());
			
			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation(org.alice.apis.moveandturn.PerspectiveCameraMarker.class);
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice defaultViewField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice("defaultCameraView", org.alice.apis.moveandturn.PerspectiveCameraMarker.class, initializer);
			defaultViewField.finalVolatileOrNeither.setValue(edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL);
			defaultViewField.access.setValue(edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE);
			this.sceneType.fields.add( this.sceneType.fields.size(), defaultViewField );
			this.handleFieldCreation(this.sceneType, defaultViewField, defaultViewMarker, false );
		}
	}

	private void fillInAutomaticPointOfViewAssignment(edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, edu.cmu.cs.dennisc.alice.ast.AbstractField field)
	{
		SetUpMethodGenerator.fillInAutomaticPointOfViewAssignment(bodyStatementsProperty, field, this.getPointOfViewFieldForField(field));
	}
	
	private void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field) {
		SetUpMethodGenerator.fillInAutomaticSetUpMethod( bodyStatementsProperty, isThis, field, this.getInstanceInJavaForField( field ) );
	}

	private AbstractField getPointOfViewFieldForField( AbstractField field )
	{
		if (field.getName().equals("camera"))
		{
			return this.sidePane.getStartingCameraViewManager().getSelectedView().field;
		}
		return null;
	}
	
	@Override
	public void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
		this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneField.getValueType().getDeclaredFields() ) {
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
		}
		
		//Add in statements setting up PointOfView after general field initialization
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
			this.fillInAutomaticPointOfViewAssignment( bodyStatementsProperty, field );
		}
		
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
	
	private static final String DEFAULT_CAMERA_MARKER_NAME = "cameraMarker";

	private String getNameForCameraMarker( TypeDeclaredInAlice ownerType ) {
		FieldNameValidator nameValidator = new FieldNameValidator( ownerType );
		int count = 0;
		String markerName = DEFAULT_CAMERA_MARKER_NAME;
		while( nameValidator.getExplanationIfOkButtonShouldBeDisabled( markerName ) != null ) {
			count++;
			markerName = DEFAULT_CAMERA_MARKER_NAME + "_" + Integer.toString( count );
		}
		return markerName;
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
	
	public List<CameraMarker> getOrthographicCameraMarkers()
	{
		return this.orthographicCameraMarkers;
	}
	
	private void createOrthographicCameraMarkers()
	{
		this.orthographicCameraMarkers.clear();
		org.alice.apis.moveandturn.OrthographicCameraMarker topMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		topMarker.setName( "TOP" );
		AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
		topTransform.translation.y = 10;
		topTransform.orientation.up.set( 0, 0, 1 );
		topTransform.orientation.right.set( -1, 0, 0 );
		topTransform.orientation.backward.set( 0, 1, 0 );
		assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		topMarker.setLocalTransformation( topTransform );
		ClippedZPlane picturePlane = new ClippedZPlane();
		picturePlane.setCenter(0, 0);
		picturePlane.setHeight(3);
		topMarker.setPicturePlane(picturePlane);
		orthographicCameraMarkers.add(topMarker);

		org.alice.apis.moveandturn.OrthographicCameraMarker sideMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		sideMarker.setName( "SIDE" );
		AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
		sideTransform.translation.x = 10;
		sideTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis()) );
		assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		sideMarker.setLocalTransformation( sideTransform );
		picturePlane.setHeight(4);
		sideMarker.setPicturePlane(picturePlane);
		orthographicCameraMarkers.add(sideMarker);

		org.alice.apis.moveandturn.OrthographicCameraMarker frontMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
		frontMarker.setName( "FRONT" );
		AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
		frontTransform.translation.z = -10;
		frontTransform.orientation.setValue( new ForwardAndUpGuide(Vector3.accessPositiveZAxis(), Vector3.accessPositiveYAxis()) );
		assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
		frontMarker.setLocalTransformation( frontTransform );
		picturePlane.setHeight(4);
		frontMarker.setPicturePlane(picturePlane);
		orthographicCameraMarkers.add(frontMarker);
	}
	
	public void addOrthographicMarkersToScene(Scene sceneToAddTo)
	{
		Transformable[] existingComponents = sceneToAddTo.getComponents();
		for (CameraMarker marker : orthographicCameraMarkers)
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

	public Tuple2< FieldDeclaredInAlice, Object > createCameraMarkerField( TypeDeclaredInAlice ownerType ) {
		String markerName = getNameForCameraMarker( ownerType );

		org.alice.apis.moveandturn.PerspectiveCameraMarker cameraMarker = new org.alice.apis.moveandturn.PerspectiveCameraMarker();
		cameraMarker.setName( markerName );
		cameraMarker.setShowing(true);
		cameraMarker.setLocalTransformation( getSGCameraForCreatingMarker().getTransformation( AsSeenBy.SCENE.getSGReferenceFrame() ) );
		updateCameraMarkerToCamera(cameraMarker, (SymmetricPerspectiveCamera)getSGCameraForCreatingMarker());

		edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.PerspectiveCameraMarker.class );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraMarkerField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( markerName, org.alice.apis.moveandturn.PerspectiveCameraMarker.class, initializer );
		cameraMarkerField.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		cameraMarkerField.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );

		return new Tuple2< FieldDeclaredInAlice, Object >( cameraMarkerField, cameraMarker );
	}

	public List< edu.cmu.cs.dennisc.alice.ast.AbstractField > getDeclaredFields() {
		List< edu.cmu.cs.dennisc.alice.ast.AbstractField > declaredFields = new LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractField >();
		if( org.alice.ide.IDE.getSingleton().getSceneType() != null ) {
			declaredFields.addAll( org.alice.ide.IDE.getSingleton().getSceneType().getDeclaredFields() );
		}
		return declaredFields;
	}

	protected void paintHorizonLine(LightweightOnscreenLookingGlass lookingGlass, OrthographicCamera camera)
	{
		AffineMatrix4x4 cameraTransform = camera.getAbsoluteTransformation();
		double dotProd = Vector3.calculateDotProduct(cameraTransform.orientation.up, Vector3.accessPositiveYAxis());
		if (dotProd == 1 || dotProd == -1)
		{
			Graphics g = lookingGlass.getJPanel().getGraphics();
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
				g.setColor(Color.BLACK);
				g.drawLine(0, horizonLinePixelVal, lookingGlassSize.width, horizonLinePixelVal);
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
		CameraFieldAndMarker fieldAndMarker = this.mainViewSelector.getSelectedMarker();
		if (fieldAndMarker != null)
		{
			return fieldAndMarker.marker == marker;
		}
		return false;
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
			paintHorizonLine(this.onscreenLookingGlass, (OrthographicCamera)this.onscreenLookingGlass.getCameraAt(0));
		}
	}
	
	public void resized( LookingGlassResizeEvent e )
	{
		updateCameraMarkers();
	}
	
	public void displayChanged( LookingGlassDisplayChangeEvent e )
	{
	}

}
