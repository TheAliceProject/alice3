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

import java.util.LinkedList;
import java.util.List;

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.ReferenceFrame;
import org.alice.ide.common.SelectedFieldExpressionPane;
import org.alice.ide.memberseditor.MembersEditor;
import org.alice.ide.name.validators.FieldNameValidator;

import edu.cmu.cs.dennisc.alice.ast.AbstractField;
import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.pattern.Tuple2;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera;

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor {
	private static final int INSET = 8;

	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createLightweightOnscreenLookingGlass();
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane splitPane = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane();
	private SidePane sidePane = new SidePane();

	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();

	private org.alice.apis.moveandturn.Scene scene;
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;
	private java.util.List< FieldTile > fieldTiles = new java.util.LinkedList< FieldTile >();

	private edu.cmu.cs.dennisc.scenegraph.OrthographicCamera sgOrthographicCamera = null;
	private edu.cmu.cs.dennisc.scenegraph.SymmetricPerspectiveCamera sgPerspectiveCamera = null;
	private org.alice.interact.CameraNavigatorWidget mainCameraNavigatorWidget = null;

	private org.alice.interact.GlobalDragAdapter globalDragAdapter;
	private org.alice.ide.IDE.CodeInFocusObserver codeInFocusObserver = new org.alice.ide.IDE.CodeInFocusObserver() {
		public void focusedCodeChanging( edu.cmu.cs.dennisc.alice.ast.AbstractCode previousCode, edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode ) {
		}
		public void focusedCodeChanged( edu.cmu.cs.dennisc.alice.ast.AbstractCode previousCode, edu.cmu.cs.dennisc.alice.ast.AbstractCode nextCode ) {
			MoveAndTurnSceneEditor.this.handleFocusedCodeChanged( nextCode );
		}
	};
	private org.alice.ide.IDE.FieldSelectionObserver fieldSelectionObserver = new org.alice.ide.IDE.FieldSelectionObserver() {
		public void fieldSelectionChanging( edu.cmu.cs.dennisc.alice.ast.AbstractField previousField, edu.cmu.cs.dennisc.alice.ast.AbstractField nextField ) {
		}
		public void fieldSelectionChanged( edu.cmu.cs.dennisc.alice.ast.AbstractField previousField, edu.cmu.cs.dennisc.alice.ast.AbstractField nextField ) {
			MoveAndTurnSceneEditor.this.handleFieldSelection( nextField );
		}
	};

	public MoveAndTurnSceneEditor() {
		javax.swing.JPanel lgPanel = this.getLGPanel();
		javax.swing.SpringLayout springLayout = new javax.swing.SpringLayout();
		lgPanel.setLayout( springLayout );

		//		this.splitPane.setBackground( java.awt.Color.GRAY );
		this.splitPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.splitPane.setResizeWeight( 1.0 );
		this.splitPane.setDividerProportionalLocation( 1.0 );
		this.addComponent( this.splitPane, Constraint.CENTER );
	}
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.refreshFields();
		}
	};

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
		this.splitPane.setLeftComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( this.getLGPanel() ) );
		org.alice.ide.IDE.getSingleton().addCodeInFocusObserver( this.codeInFocusObserver );
		org.alice.ide.IDE.getSingleton().addFieldSelectionObserver( this.fieldSelectionObserver );
	}

	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.splitPane.setLeftComponent( null );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		org.alice.ide.IDE.getSingleton().removeCodeInFocusObserver( this.codeInFocusObserver );
		org.alice.ide.IDE.getSingleton().removeFieldSelectionObserver( this.fieldSelectionObserver );
		super.handleRemovedFrom( parent );
	}

	private javax.swing.JPanel getLGPanel() {
		return this.onscreenLookingGlass.getJPanel();
	}
	private javax.swing.SpringLayout getLGSpringLayout() {
		return (javax.swing.SpringLayout)this.getLGPanel().getLayout();
	}

	private void initializeIfNecessary() {
		if( this.globalDragAdapter != null ) {
			//pass
		} else {
			this.globalDragAdapter = new org.alice.interact.GlobalDragAdapter();
			this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );

			this.mainCameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter );
			//final org.alice.interact.CameraNavigatorWidget cameraNavigatorWidget = null;

			edu.cmu.cs.dennisc.croquet.BooleanStateOperation isSceneEditorExpandedOperation = this.getIDE().getIsSceneEditorExpandedOperation();
			final edu.cmu.cs.dennisc.croquet.CheckBox isSceneEditorExpandedCheckBox = isSceneEditorExpandedOperation.createCheckBox();
			isSceneEditorExpandedCheckBox.getJComponent().setUI( new IsExpandedCheckBoxUI() );
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

			javax.swing.JPanel lgPanel = this.getLGPanel();
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouthEast( lgPanel, isSceneEditorExpandedCheckBox.getJComponent(), INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthEast( lgPanel, this.getIDE().getRunOperation().createButton().getJComponent(), INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouth( lgPanel, mainCameraNavigatorWidget, INSET );

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
		if( this.sgOrthographicCamera != null ) {
			//pass
		} else {
			this.sgOrthographicCamera = new edu.cmu.cs.dennisc.scenegraph.OrthographicCamera();
			edu.cmu.cs.dennisc.scenegraph.Transformable orthographicSGTransformable = new edu.cmu.cs.dennisc.scenegraph.Transformable();
			this.sgOrthographicCamera.setParent( orthographicSGTransformable );
		}
	}
	@Override
	public void handleExpandContractChange( boolean isExpanded ) {
		this.sidePane.setExpanded( isExpanded );
		if( isExpanded ) {
			this.splitPane.setRightComponent( this.sidePane );
			this.splitPane.setDividerSize( 10 );
		} else {
			this.splitPane.setRightComponent( null );
			this.splitPane.setDividerSize( 0 );
		}
		this.updateSceneBasedOnScope();
	}

	@Override
	public void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance, boolean isAnimationDesired ) {
		assert instance instanceof org.alice.apis.moveandturn.Transformable;
		final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;
		//declaringType.fields.add( field );
		this.putInstanceForField( field, transformable );
		this.getIDE().setFieldSelection( field );
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
			this.getIDE().setFieldSelection( field );
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
				edu.cmu.cs.dennisc.alice.ast.AbstractField selectedField = this.getIDE().getFieldSelection();
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
										this.getIDE().setFieldSelection( field );
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
		for( FieldTile fieldTile : this.fieldTiles ) {
			fieldTile.updateLabel();
		}
	}

	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
	}
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getRootField() {
		return this.rootField;
	}
	public void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.getRootField() != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshFields();
	}
	private FieldTile createFieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		assert field != null;
		return new FieldTile( field );
	}
	private void refreshFields() {
		javax.swing.SpringLayout springLayout = this.getLGSpringLayout();
		java.awt.Container lgPanel = this.onscreenLookingGlass.getJPanel();
		for( FieldTile fieldTile : this.fieldTiles ) {
			springLayout.removeLayoutComponent( fieldTile.getJComponent() );
			this.onscreenLookingGlass.getJPanel().remove( fieldTile.getJComponent() );
		}
		this.fieldTiles.clear();
		if( this.rootField != null ) {
			FieldTile rootFieldTile = this.createFieldTile( this.rootField );
			//rootFieldTile.setOpaque( true );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( lgPanel, rootFieldTile.getJComponent(), INSET );
			this.fieldTiles.add( rootFieldTile );
			java.awt.Component prev = rootFieldTile.getJComponent();
			if( rootField != null ) {
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
					FieldTile fieldTile = createFieldTile( field );
					//fieldTile.setOpaque( true );
					this.onscreenLookingGlass.getJPanel().add( fieldTile.getJComponent() );
					this.fieldTiles.add( fieldTile );
					springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile.getJComponent(), 1, javax.swing.SpringLayout.SOUTH, prev );
					springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile.getJComponent(), 10, javax.swing.SpringLayout.WEST, rootFieldTile.getJComponent() );
					prev = fieldTile.getJComponent();
				}
			}
		}
		this.revalidateAndRepaint();
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

	private void handleFieldSelection( AbstractField field ) {
		Object instance = this.getInstanceForField( field );
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}
		if( instance instanceof org.alice.apis.moveandturn.Model ) {
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instance;
			this.globalDragAdapter.setSelectedObject( model.getSGTransformable() );
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

	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
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
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "TODO:" );
		this.initializeIfNecessary();
		this.scene.getSGComposite().addComponent( this.sgOrthographicCamera.getParent() );
		this.setRootField( sceneField );

		//		createInitialOrthographicCameraMarkers();
		//this.cameraNavigationDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
		return rv;
	}

	private void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		SetUpMethodGenerator.fillInAutomaticSetUpMethod( bodyStatementsProperty, isThis, field, this.getInstanceInJavaForField( field ) );
	}

	@Override
	public void generateCodeForSetUp( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
		this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneField.getValueType().getDeclaredFields() ) {
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
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

	private static final String DEFAULT_CAMERA_MARKER_NAME = "cameraMarker";

	private String getNameForCameraMarker( TypeDeclaredInAlice ownerType ) {
		FieldNameValidator nameValidator = new FieldNameValidator( ownerType );
		int count = 0;
		String markerName = DEFAULT_CAMERA_MARKER_NAME;
		while( !nameValidator.isNameValidAndAvailable( markerName ) ) {
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
	}

	public void switchToOthographicCamera() {
		switchToCamera( this.sgOrthographicCamera );
		this.mainCameraNavigatorWidget.setCamera( this.sgOrthographicCamera );
	}

	public void switchToPerspectiveCamera() {
		switchToCamera( this.sgPerspectiveCamera );
		this.mainCameraNavigatorWidget.setCamera( this.sgPerspectiveCamera );
	}

	private boolean hasFieldNamed( String fieldName ) {
		for( AbstractField field : this.sceneType.fields ) {
			if( field.getName().equals( fieldName ) ) {
				return true;
			}
		}
		return false;
	}

	private void createInitialOrthographicCameraMarkers() {
		if( !hasFieldNamed( "TOP" ) ) {
			org.alice.apis.moveandturn.OrthographicCameraMarker topMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
			topMarker.setName( "TOP" );
			AffineMatrix4x4 topTransform = AffineMatrix4x4.createIdentity();
			topTransform.translation.y = 10;
			topTransform.orientation.up.set( 0, 0, 1 );
			topTransform.orientation.right.set( 1, 0, 0 );
			topTransform.orientation.backward.set( 0, 1, 0 );
			assert topTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
			topMarker.setLocalTransformation( topTransform );

			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.OrthographicCameraMarker.class );
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice topMarkerField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( "TOP", org.alice.apis.moveandturn.OrthographicCameraMarker.class, initializer );
			topMarkerField.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
			topMarkerField.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );

			System.out.println( "Creating marker " + topMarker.getName() + ":" + topMarker.hashCode() );

			this.sceneType.fields.add( this.sceneType.fields.size(), topMarkerField );
			this.handleFieldCreation( this.sceneType, topMarkerField, topMarker, false );
		}
		if( !hasFieldNamed( "SIDE" ) ) {
			org.alice.apis.moveandturn.OrthographicCameraMarker sideMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
			sideMarker.setName( "SIDE" );
			AffineMatrix4x4 sideTransform = AffineMatrix4x4.createIdentity();
			sideTransform.translation.x = 10;
			sideTransform.orientation.setValue( new ForwardAndUpGuide( Vector3.accessNegativeXAxis(), Vector3.accessPositiveYAxis() ) );
			assert sideTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
			sideMarker.setLocalTransformation( sideTransform );

			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.OrthographicCameraMarker.class );
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sideMarkerField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( "SIDE", org.alice.apis.moveandturn.OrthographicCameraMarker.class, initializer );
			sideMarkerField.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
			sideMarkerField.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );

			System.out.println( "Creating marker " + sideMarker.getName() + ":" + sideMarker.hashCode() );

			this.sceneType.fields.add( this.sceneType.fields.size(), sideMarkerField );
			this.handleFieldCreation( this.sceneType, sideMarkerField, sideMarker, false );
		}
		if( !hasFieldNamed( "FRONT" ) ) {
			org.alice.apis.moveandturn.OrthographicCameraMarker frontMarker = new org.alice.apis.moveandturn.OrthographicCameraMarker();
			frontMarker.setName( "FRONT" );
			AffineMatrix4x4 frontTransform = AffineMatrix4x4.createIdentity();
			frontTransform.translation.z = 10;
			frontTransform.orientation.setValue( new ForwardAndUpGuide( Vector3.accessNegativeZAxis(), Vector3.accessPositiveYAxis() ) );
			assert frontTransform.orientation.isWithinReasonableEpsilonOfUnitLengthSquared();
			frontMarker.setLocalTransformation( frontTransform );

			edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.OrthographicCameraMarker.class );
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice frontMarkerField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( "FRONT", org.alice.apis.moveandturn.OrthographicCameraMarker.class, initializer );
			frontMarkerField.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
			frontMarkerField.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );

			System.out.println( "Creating marker " + frontMarker.getName() + ":" + frontMarker.hashCode() );

			this.sceneType.fields.add( this.sceneType.fields.size(), frontMarkerField );
			this.handleFieldCreation( this.sceneType, frontMarkerField, frontMarker, false );
		}
	}

	public Tuple2< FieldDeclaredInAlice, Object > createCameraMarkerField( TypeDeclaredInAlice ownerType ) {
		String markerName = getNameForCameraMarker( ownerType );

		org.alice.apis.moveandturn.PerspectiveCameraMarker cameraMarker = new org.alice.apis.moveandturn.PerspectiveCameraMarker();
		cameraMarker.setName( markerName );
		cameraMarker.setLocalTransformation( getSGCameraForCreatingMarker().getTransformation( AsSeenBy.SCENE.getSGReferenceFrame() ) );

		edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation( org.alice.apis.moveandturn.CameraMarker.class );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice cameraMarkerField = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice( markerName, org.alice.apis.moveandturn.CameraMarker.class, initializer );
		cameraMarkerField.finalVolatileOrNeither.setValue( edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL );
		cameraMarkerField.access.setValue( edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE );

		return new Tuple2< FieldDeclaredInAlice, Object >( cameraMarkerField, cameraMarker );
	}

	public List< edu.cmu.cs.dennisc.alice.ast.AbstractField > getDeclaredFields() {
		List< edu.cmu.cs.dennisc.alice.ast.AbstractField > declaredFields = new LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractField >();
		if( this.rootField != null ) {
			declaredFields.addAll( rootField.valueType.getValue().getDeclaredFields() );
		}
		return declaredFields;
	}
}
