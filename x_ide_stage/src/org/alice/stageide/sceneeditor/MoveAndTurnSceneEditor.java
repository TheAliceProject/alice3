/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.sceneeditor;

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor {
	private static final int INSET = 8;

	private edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass onscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createLightweightOnscreenLookingGlass();
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private SidePane sidePane = new SidePane();

	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();

	private org.alice.apis.moveandturn.Scene scene;
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;
	private java.util.List< FieldTile > fieldTiles = new java.util.LinkedList< FieldTile >();

	private org.alice.interact.GlobalDragAdapter globalDragAdapter;


	public MoveAndTurnSceneEditor() {
		javax.swing.JPanel lgPanel = this.getLGPanel();
		javax.swing.SpringLayout springLayout = new javax.swing.SpringLayout();
		lgPanel.setLayout( springLayout );

		this.splitPane.setResizeWeight( 1.0 );
		this.splitPane.setDividerLocation( 1.0 );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.splitPane, java.awt.BorderLayout.CENTER );
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

	private boolean isRenderingEnabled = true;
	@Override
	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		this.isRenderingEnabled = isRenderingEnabled;
		this.onscreenLookingGlass.setRenderingEnabled( isRenderingEnabled );
	}
	@Override
	public void update( java.awt.Graphics g ) {
		if( isRenderingEnabled ) {
			super.update( g );
		} else {
//			java.awt.Dimension size = this.getSize();
//			g.setColor( java.awt.Color.GRAY );
//			g.fillRect( 0, 0, size.width, size.height );
//			String text = "rendering disabled for performance considerations";
//			g.setColor( java.awt.Color.BLACK );
//			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, size );
//			g.setColor( java.awt.Color.YELLOW );
//			g.translate( -1, -1 );
//			edu.cmu.cs.dennisc.awt.GraphicsUtilties.drawCenteredText( g, text, size );
//			g.translate( 1, 1 );
//			g.dispose();
		}
	}

	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			MoveAndTurnSceneEditor.this.animator.update();
		}
	};

	@Override
	public void addNotify() {
		this.initializeIfNecessary();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( this.automaticDisplayListener );
		this.splitPane.setLeftComponent( this.getLGPanel() );
		super.addNotify();
	}

	@Override
	public void removeNotify() {
		this.splitPane.setLeftComponent( null );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().removeAutomaticDisplayListener( this.automaticDisplayListener );
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
		super.removeNotify();
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
			
			final org.alice.interact.CameraNavigatorWidget cameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.globalDragAdapter );
			//final org.alice.interact.CameraNavigatorWidget cameraNavigatorWidget = null;

			final IsExpandedCheckBox isSceneEditorExpandedCheckBox = new IsExpandedCheckBox();
			//final javax.swing.JCheckBox isSceneEditorExpandedCheckBox = edu.cmu.cs.dennisc.zoot.ZManager.createCheckBox( this.getIDE().getIsSceneEditorExpandedOperation() );
			if( cameraNavigatorWidget != null ) {
				cameraNavigatorWidget.setExpanded( isSceneEditorExpandedCheckBox.isSelected() );
			}
			isSceneEditorExpandedCheckBox.addItemListener( new java.awt.event.ItemListener() {
				public void itemStateChanged( java.awt.event.ItemEvent e ) {
					boolean isExpanded = e.getStateChange() == java.awt.event.ItemEvent.SELECTED;
					if( cameraNavigatorWidget != null ) {
						cameraNavigatorWidget.setExpanded( isExpanded );
					}
				}
			} );

			javax.swing.JPanel lgPanel = this.getLGPanel();
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouthEast( lgPanel, isSceneEditorExpandedCheckBox, INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthEast( lgPanel, edu.cmu.cs.dennisc.zoot.ZManager.createButton( this.getIDE().getRunOperation() ), INSET );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addSouth( lgPanel, cameraNavigatorWidget, INSET );

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
	public void handleFieldCreation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, Object instance ) {
		assert instance instanceof org.alice.apis.moveandturn.Transformable;
		final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;
		//declaringType.fields.add( field );
		this.putInstanceForField( field, transformable );
		this.getIDE().setFieldSelection( field );
		final org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = this.scene.findFirstMatch( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class );
		if( camera != null && transformable instanceof org.alice.apis.moveandturn.Model ) {
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

	@Override
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		super.focusedCodeChanged( e );
		this.updateSceneBasedOnScope();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = e.getNextValue();
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

	public void updateFieldLabels() {
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
			springLayout.removeLayoutComponent( fieldTile );
			this.onscreenLookingGlass.getJPanel().remove( fieldTile );
		}
		this.fieldTiles.clear();
		if( this.rootField != null ) {
			FieldTile rootFieldTile = this.createFieldTile( this.rootField );
			//rootFieldTile.setOpaque( true );
			edu.cmu.cs.dennisc.javax.swing.SpringUtilities.addNorthWest( lgPanel, rootFieldTile, INSET );
			this.fieldTiles.add( rootFieldTile );
			java.awt.Component prev = rootFieldTile;
			if( rootField != null ) {
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
					FieldTile fieldTile = createFieldTile( field );
					//fieldTile.setOpaque( true );
					this.onscreenLookingGlass.getJPanel().add( fieldTile );
					this.fieldTiles.add( fieldTile );
					springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile, 1, javax.swing.SpringLayout.SOUTH, prev );
					springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile, 10, javax.swing.SpringLayout.WEST, rootFieldTile );
					prev = fieldTile;
				}
			}
		}
		revalidate();
		repaint();
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

	@Override
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		super.fieldSelectionChanged( e );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = e.getNextValue();
		Object instance = this.getInstanceForField( field );
		if( instance instanceof edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice ) {
			edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice instanceInAlice = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)instance;
			instance = instanceInAlice.getInstanceInJava();
		}
		if( instance instanceof org.alice.apis.moveandturn.Model ) {
			org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instance;
			this.globalDragAdapter.setSelectedObject( model.getSGTransformable() );
		}
		this.refreshFields();
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

		this.setRootField( sceneField );
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
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
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

}
