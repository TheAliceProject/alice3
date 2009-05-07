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

import org.alice.apis.moveandturn.gallery.environments.Ground;
import org.alice.apis.moveandturn.gallery.environments.grounds.DirtGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.MoonSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SandyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.SeaSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SnowyGround;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;

class BogusModelManipulationModePane extends swing.PageAxisPane {
	public BogusModelManipulationModePane() {
		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		group.add( new javax.swing.JRadioButton( "rotateY", true ) );
		group.add( new javax.swing.JRadioButton( "translateXZ" ) );
		group.add( new javax.swing.JRadioButton( "rotateXYZ" ) );
		group.add( new javax.swing.JRadioButton( "scale" ) );
		group.add( new javax.swing.JRadioButton( "translateY" ) );
		this.setBorder( javax.swing.BorderFactory.createTitledBorder( "models" ) );
		for( java.util.Enumeration< javax.swing.AbstractButton > e = group.getElements(); e.hasMoreElements(); ) {
			this.add( e.nextElement() );
		}
	}
}

class BogusCameraViewPane extends swing.PageAxisPane {
	public BogusCameraViewPane() {
		javax.swing.ButtonGroup group = new javax.swing.ButtonGroup();
		group.add( new javax.swing.JRadioButton( "opening scene view", true ) );
		group.add( new javax.swing.JRadioButton( "working view" ) );
		this.setBorder( javax.swing.BorderFactory.createTitledBorder( "camera" ) );
		for( java.util.Enumeration< javax.swing.AbstractButton > e = group.getElements(); e.hasMoreElements(); ) {
			this.add( e.nextElement() );
		}
	}
}

class SidePane extends swing.PageAxisPane {
	private boolean isExpanded = false;

	public SidePane() {
//		this.add( zoot.ZLabel.acquire( "NOTE: " ) );
//		this.add( zoot.ZLabel.acquire( "This pane is just a placeholder." ) );
//		this.add( zoot.ZLabel.acquire( "It is not hooked up to anything." ) );
//		this.add( javax.swing.Box.createVerticalStrut( 16 ) );
//
//		this.add( new BogusCameraViewPane() );
//		this.add( new BogusModelManipulationModePane() );
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
	}
	public boolean isExpanded() {
		return this.isExpanded;
	}
	public void setExpanded( boolean isExpanded ) {
		this.isExpanded = isExpanded;
		//		this.revalidate();
		//		this.repaint();
		//		//this.doLayout();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends org.alice.ide.sceneeditor.AbstractInstantiatingSceneEditor {
	private Program program = this.createProgram();
	//private edu.cmu.cs.dennisc.lookingglass.util.CardPane cardPane;
	private org.alice.stageide.sceneeditor.ControlsForOverlayPane controlsForOverlayPane;
	private javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private SidePane sidePane = new SidePane();
	private org.alice.interact.GlobalDragAdapter globalDragAdapter = new org.alice.interact.GlobalDragAdapter();

	//private edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	
	public MoveAndTurnSceneEditor() {
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( this.splitPane );
		this.splitPane.setResizeWeight( 1.0 );
		this.splitPane.setDividerLocation( 1.0 );

		//		new Thread() {
		//			@Override
		//			public void run() {
		//				MoveAndTurnSceneEditor.this.program.showInAWTContainer( MoveAndTurnSceneEditor.this, new String[] {} );
		//				edu.cmu.cs.dennisc.animation.Animator animator = MoveAndTurnSceneEditor.this.program.getAnimator();
		//				while( animator == null ) {
		//					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
		//					animator = MoveAndTurnSceneEditor.this.program.getAnimator();
		//				}
		//				MoveAndTurnSceneEditor.this.globalDragAdapter.setAnimator( animator );
		//				MoveAndTurnSceneEditor.this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
		//					public void selecting( org.alice.interact.event.SelectionEvent e ) {
		//					}
		//					public void selected( org.alice.interact.event.SelectionEvent e ) {
		//						MoveAndTurnSceneEditor.this.handleSelection( e );
		//					}
		//				} );
		//			}
		//		}.start();
		new Thread() {
			@Override
			public void run() {
				MoveAndTurnSceneEditor.this.initializeProgramAndDragAdapter();
			}
		}.start();
	}
	
	private void initializeProgramAndDragAdapter() {
		this.program.setArgs( new String[] {} );
		this.program.init();
		this.program.start();
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
		while( true ) {
			onscreenLookingGlass = MoveAndTurnSceneEditor.this.program.getOnscreenLookingGlass();
			if( onscreenLookingGlass != null ) {
				break;
			} else {
				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
			}
		}

		
		//note: do not add the program... it is an applet and therefore heavyweight
		this.splitPane.setLeftComponent( this.program.getOnscreenLookingGlass().getAWTComponent() );
		
		
		this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
		edu.cmu.cs.dennisc.animation.Animator animator = this.program.getAnimator();
		while( animator == null ) {
			edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
			animator = this.program.getAnimator();
		}
		this.globalDragAdapter.setAnimator( animator );
		this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
			public void selecting( org.alice.interact.event.SelectionEvent e ) {
			}
			public void selected( org.alice.interact.event.SelectionEvent e ) {
				MoveAndTurnSceneEditor.this.handleSelection( e );
			}
		} );

		//this.cameraNavigationDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
		
		
//		this.program.setArgs( new String[] {} );
//		this.program.init();
//		this.program.start();
//		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass;
//		while( true ) {
//			onscreenLookingGlass = MoveAndTurnSceneEditor.this.program.getOnscreenLookingGlass();
//			if( onscreenLookingGlass != null ) {
//				break;
//			} else {
//				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
//			}
//		}
//		this.cardPane = new edu.cmu.cs.dennisc.lookingglass.util.CardPane( onscreenLookingGlass );
//		this.splitPane.setLeftComponent( this.cardPane );
//
//		this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
//		edu.cmu.cs.dennisc.animation.Animator animator = this.program.getAnimator();
//		while( animator == null ) {
//			edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
//			animator = this.program.getAnimator();
//		}
//		this.globalDragAdapter.setAnimator( animator );
//		this.globalDragAdapter.addPropertyListener( new org.alice.interact.event.SelectionListener() {
//			public void selecting( org.alice.interact.event.SelectionEvent e ) {
//			}
//			public void selected( org.alice.interact.event.SelectionEvent e ) {
//				MoveAndTurnSceneEditor.this.handleSelection( e );
//			}
//		} );
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
		final org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instance;
		declaringType.fields.add( field );
		this.putInstanceForField( field, transformable );
		this.getIDE().setFieldSelection( field );
		final org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera = this.program.getScene().findFirstMatch( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class );
		if( camera != null && transformable instanceof org.alice.apis.moveandturn.Model ) {
			new Thread() {
				@Override
				public void run() {
					MoveAndTurnSceneEditor.this.getGoodLookAtShowInstanceAndReturnCamera( camera, (org.alice.apis.moveandturn.Model)transformable );
				}
			}.start();
		} else {
			this.program.getScene().addComponent( transformable );
		}
		getIDE().markChanged( "scene program addInstance" );
	}
	private void getGoodLookAtShowInstanceAndReturnCamera( org.alice.apis.moveandturn.SymmetricPerspectiveCamera camera, org.alice.apis.moveandturn.Model model ) {
		this.pushAndSetCameraNavigationDragAdapterEnabled( false );
		try {
			model.setOpacity( 0.0, org.alice.apis.moveandturn.Composite.RIGHT_NOW );
			org.alice.apis.moveandturn.PointOfView pov = camera.getPointOfView( this.program.getScene() );
			camera.getGoodLookAt( model, 0.5 );
			this.program.getScene().addComponent( model );
			model.setOpacity( 1.0 );
			camera.moveAndOrientTo( this.program.getScene().createOffsetStandIn( pov.getInternal() ), 0.5 );
		} finally {
			this.popCameraNavigationDragAdapterEnabled();
		}
	}
	private void handleSelection( org.alice.interact.event.SelectionEvent e ) {
		edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = e.getTransformable();
		org.alice.apis.moveandturn.Element element = org.alice.apis.moveandturn.Element.getElement( sgTransformable );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldForInstanceInJava( element );
		if( field != null ) {
			this.getIDE().setFieldSelection( field );
		}
	}

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
					edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera = this.getProgram().getOnscreenLookingGlass().getCameraAt( 0 );
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
	}

	@Override
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		super.focusedCodeChanged( e );
		this.updateSceneBasedOnScope();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = e.getNextValue();
		if( code != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = code.getDeclaringType();

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
		this.controlsForOverlayPane.focusedCodeChanged( e );
	}

	private static boolean isGround( org.alice.apis.moveandturn.Model model ) {
		Class[] clses = { Ground.class, GrassyGround.class, DirtGround.class, MoonSurface.class, SandyGround.class, SeaSurface.class, SnowyGround.class };
		for( Class< ? extends org.alice.apis.moveandturn.PolygonalModel > cls : clses ) {
			if( cls.isAssignableFrom( model.getClass() ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void putFieldForInstanceInJava( java.lang.Object instanceInJava, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super.putFieldForInstanceInJava( instanceInJava, field );
		if( instanceInJava instanceof org.alice.apis.moveandturn.Transformable ) {
			org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)instanceInJava;
			edu.cmu.cs.dennisc.scenegraph.Transformable sgTransformable = transformable.getSGTransformable();
			if( instanceInJava instanceof org.alice.apis.moveandturn.Model ) {
				org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)instanceInJava;
				if( MoveAndTurnSceneEditor.isGround( model ) ) {
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.GROUND );
				} else {
					sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
					sgTransformable.putBonusDataFor( org.alice.interact.GlobalDragAdapter.BOUNDING_BOX_KEY, model.getAxisAlignedMinimumBoundingBox() );
					//edu.cmu.cs.dennisc.print.PrintUtilities.println( sgTransformable.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY ) );
				}
			} else if( transformable instanceof org.alice.apis.moveandturn.Light ) {
				org.alice.apis.moveandturn.Light light = (org.alice.apis.moveandturn.Light)instanceInJava;
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.LIGHT );
			} else if( transformable instanceof org.alice.apis.moveandturn.AbstractCamera ) {
				org.alice.apis.moveandturn.AbstractCamera camera = (org.alice.apis.moveandturn.AbstractCamera)instanceInJava;
				sgTransformable.putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.CAMERA );
			}
		}
	}
	protected org.alice.stageide.sceneeditor.ControlsForOverlayPane createControlsForOverlayPane() {
		return new org.alice.stageide.sceneeditor.ControlsForOverlayPane( this.globalDragAdapter );
	}
	protected Program createProgram() {
		return new Program( this );
	}
	protected Program getProgram() {
		return this.program;
	}
	private org.alice.stageide.sceneeditor.ControlsForOverlayPane getControlsForOverlayPane() {
		if( this.controlsForOverlayPane != null ) {
			//pass
		} else {
			this.controlsForOverlayPane = this.createControlsForOverlayPane();
		}
		return this.controlsForOverlayPane;
	}
	public void initializeLightweightOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass lightweightOnscreenLookingGlass ) {
		javax.swing.JPanel panel = lightweightOnscreenLookingGlass.getJPanel();
		panel.setLayout( new java.awt.BorderLayout() );
		panel.add( this.getControlsForOverlayPane() );
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
		this.controlsForOverlayPane.fieldSelectionChanged( e );
	}

	private edu.cmu.cs.dennisc.scenegraph.Background cameraBackground = new edu.cmu.cs.dennisc.scenegraph.Background();

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
		public void removing( RemoveListPropertyEvent< FieldDeclaredInAlice > e ) {
			MoveAndTurnSceneEditor.this.handleRemoved( e );
		}
		public void removed( RemoveListPropertyEvent< FieldDeclaredInAlice > e ) {
		}
		public void set( SetListPropertyEvent< FieldDeclaredInAlice > e ) {
		}
		public void setting( SetListPropertyEvent< FieldDeclaredInAlice > e ) {
		};
	};

	private void handleRemoved( RemoveListPropertyEvent< FieldDeclaredInAlice > e ) {
		for( FieldDeclaredInAlice fieldDeclaredInAlice : e.getElements() ) {
			org.alice.apis.moveandturn.Transformable transformable = this.getInstanceInJavaForField( fieldDeclaredInAlice, org.alice.apis.moveandturn.Transformable.class );
			if( transformable != null ) {
				this.program.getScene().removeComponent( transformable );
			}
		}
	}

	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.program.getOnscreenLookingGlass();
		if( onscreenLookingGlass.getCameraCount() > 0 ) {
			onscreenLookingGlass.clearCameras();
			this.program.setScene( null );
		}
		if( this.sceneType != null ) {
			this.sceneType.fields.removeListPropertyListener( this.listPropertyAdapter );
		}

		Object rv = super.createScene( sceneField );
		this.sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneField.getValueType();
		if( this.sceneType != null ) {
			this.sceneType.fields.addListPropertyListener( this.listPropertyAdapter );
		}
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSetUp" );
		this.getVM().invokeEntryPoint( method, rv );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)rv).getInstanceInJava();
		this.program.setScene( scene );
		this.getControlsForOverlayPane().setRootField( sceneField );
		//this.cameraNavigationDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
		return rv;
	}
	//	def createScene( self, sceneField ):
	//		program = self.getProgram()
	//		if program:
	//			lg = program.getOnscreenLookingGlass()
	//			lg.clearCameras()
	//			program.setScene( None )
	//
	//		sceneInstance = org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.createScene( self, sceneField )
	//		if sceneInstance:
	//			self.restoreProjectProperties()
	//		return sceneInstance

	//	@Override
	//	protected void setSceneField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
	//		super.setSceneField( sceneField );
	//		this.controlsForOverlayPane.setRootField( sceneField );
	//	}

	//	public void setDragInProgress( boolean isDragInProgress ) {
	//		if( isDragInProgress ) {
	//			this.showSnapshotIfAppropriate();
	//		} else {
	//			this.showLiveIfAppropriate();
	//		}
	//	}

	//	public void showSnapshotIfAppropriate() {
	//		this.cardPane.showSnapshot();
	//	}
	//	public void showLiveIfAppropriate() {
	//		this.cardPane.showLive();
	//	}

	public Object createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return this.getVM().createInstanceEntryPoint( type );
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.Expression thisExpression = new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
		if( isThis ) {
			return thisExpression;
		} else {
			return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( thisExpression, field );
		}
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( Double d ) {
		return new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( d );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( String s ) {
		return new edu.cmu.cs.dennisc.alice.ast.StringLiteral( s );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( Enum e ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( e.getClass() );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = type.getDeclaredField( type, e.name() );
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.font.Attribute attribute ) {
		if( attribute instanceof Enum ) {
			return createExpression( (Enum)attribute );
		} else {
			throw new RuntimeException( "todo" );
		}
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Color color ) {
		edu.cmu.cs.dennisc.alice.ast.Expression rv = null;
		Class< ? > cls = org.alice.apis.moveandturn.Color.class;
		for( java.lang.reflect.Field fld : edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( cls, cls ) ) {
			if( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( fld, null ).equals( color ) ) {
				edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression = new edu.cmu.cs.dennisc.alice.ast.TypeExpression( cls );
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getField( fld );
				rv = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( typeExpression, field );
				break;
			}
		}
		if( rv != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( cls, Number.class, Number.class,
					Number.class ) );
			rv = org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( color.getRed() ), createExpression( color.getGreen() ), createExpression( color.getBlue() ) );
		}
		return rv;
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createPositionExpression( edu.cmu.cs.dennisc.math.Point3 translation ) {
		Class< ? > cls = org.alice.apis.moveandturn.Position.class;
		java.lang.reflect.Constructor< ? > cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( cls, Number.class, Number.class, Number.class );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( cnstrctr );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( translation.x ), createExpression( translation.y ), createExpression( translation.z ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createQuaternionExpression( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation ) {
		edu.cmu.cs.dennisc.math.UnitQuaternion q = new edu.cmu.cs.dennisc.math.UnitQuaternion( orientation );
		Class< ? > cls = org.alice.apis.moveandturn.Quaternion.class;
		java.lang.reflect.Constructor< ? > cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( cls, Number.class, Number.class, Number.class, Number.class );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( cnstrctr );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( q.x ), createExpression( q.y ), createExpression( q.z ), createExpression( q.w ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.PointOfView pointOfView ) {
		Class< ? > cls = org.alice.apis.moveandturn.PointOfView.class;
		java.lang.reflect.Constructor< ? > cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( cls, org.alice.apis.moveandturn.Orientation.class, org.alice.apis.moveandturn.Position.class );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( cnstrctr );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createQuaternionExpression( pointOfView.getInternal().orientation ), createPositionExpression( pointOfView.getInternal().translation ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Font font ) {
		Class< ? > cls = org.alice.apis.moveandturn.Font.class;
		java.lang.reflect.Constructor< ? > cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( cls, org.alice.apis.moveandturn.font.Attribute[].class );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( cnstrctr );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( font.getFamily() ), createExpression( font.getWeight() ), createExpression( font.getPosture() ) );
	}

	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? > parameterCls, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression,
			edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}
	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? >[] parameterClses, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression,
			edu.cmu.cs.dennisc.alice.ast.Expression... argumentExpressions ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}
	private void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		Object instance = this.getInstanceInJavaForField( field );
		if( instance instanceof org.alice.apis.moveandturn.Element ) {
			org.alice.apis.moveandturn.Element element = (org.alice.apis.moveandturn.Element)instance;
			bodyStatementsProperty.add( createStatement( edu.cmu.cs.dennisc.pattern.AbstractElement.class, "setName", String.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( element
					.getName() ) ) );
			if( instance instanceof org.alice.apis.moveandturn.Transformable ) {
				org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.AbstractTransformable.class, "setLocalPointOfView", org.alice.apis.moveandturn.PointOfView.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ),
						MoveAndTurnSceneEditor.createExpression( transformable.getLocalPointOfView() ) ) );
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Composite.class, "addComponent", org.alice.apis.moveandturn.Transformable.class, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), MoveAndTurnSceneEditor
						.createInstanceExpression( isThis, field ) ) );
				if( instance instanceof org.alice.apis.moveandturn.Model ) {
					org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)transformable;

					double widthFactor = model.getResizeWidthAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( widthFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeWidth", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, MoveAndTurnSceneEditor
								.createInstanceExpression( isThis, field ), createExpression( widthFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					double heightFactor = model.getResizeHeightAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( heightFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeHeight", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, MoveAndTurnSceneEditor
								.createInstanceExpression( isThis, field ), createExpression( heightFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					double depthFactor = model.getResizeDepthAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( depthFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeDepth", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, MoveAndTurnSceneEditor
								.createInstanceExpression( isThis, field ), createExpression( depthFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					if( model instanceof org.alice.apis.moveandturn.Text ) {
						org.alice.apis.moveandturn.Text text = (org.alice.apis.moveandturn.Text)model;
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setValue", String.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text
								.getValue() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setFont", org.alice.apis.moveandturn.Font.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
								.createExpression( text.getFont() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setLetterHeight", Number.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor.createExpression( text
								.getLetterHeight() ) ) );
					} else if( model instanceof org.alice.apis.stage.Person ) {
						org.alice.apis.stage.Person person = (org.alice.apis.stage.Person)model;
						bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setHair", org.alice.apis.stage.Hair.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
								.createExpression( (Enum)person.getHair() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setEyeColor", org.alice.apis.stage.EyeColor.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
								.createExpression( (Enum)person.getEyeColor() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setSkinTone", org.alice.apis.stage.SkinTone.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
								.createExpression( (Enum)person.getSkinTone() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setOutfit", org.alice.apis.stage.Outfit.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
								.createExpression( (Enum)person.getOutfit() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setFitnessLevel", org.alice.apis.stage.FitnessLevel.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
								.createExpression( person.getFitnessLevel() ) ) );

					}
				}
			} else if( instance instanceof org.alice.apis.moveandturn.Scene ) {
				org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Scene.class, "setAtmosphereColor", org.alice.apis.moveandturn.Color.class, MoveAndTurnSceneEditor.createInstanceExpression( isThis, field ), MoveAndTurnSceneEditor
						.createExpression( scene.getAtmosphereColor() ) ) );
			}
		}
	}

	@Override
	public void generateCodeForSetUp() {
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = this.getSceneField();
		edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSceneEditorGeneratedSetUp" );
		if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
			edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty = methodDeclaredInAlice.body.getValue().statements;
			bodyStatementsProperty.clear();
			bodyStatementsProperty.add( new edu.cmu.cs.dennisc.alice.ast.Comment( "DO NOT EDIT.  This code is automatically generated." ) );
			this.fillInAutomaticSetUpMethod( bodyStatementsProperty, true, sceneField );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : sceneType.getDeclaredFields() ) {
				this.fillInAutomaticSetUpMethod( bodyStatementsProperty, false, field );
			}
		} else {
			throw new RuntimeException();
		}
	}
	@Override
	public void preserveProjectProperties() {
		//this.preserveCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}
	@Override
	public void restoreProjectProperties() {
		//this.restoreCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}

	//private java.util.Stack< Boolean > isCameraNavigationDragAdapterEnabledStack = new java.util.Stack< Boolean >();

	protected void pushAndSetCameraNavigationDragAdapterEnabled( Boolean isCameraNavigationDragAdapterEnabled ) {
		//this.isCameraNavigationDragAdapterEnabledStack.push( this.cameraNavigationDragAdapter.isEnabled() );
		//this.cameraNavigationDragAdapter.setEnabled( isCameraNavigationDragAdapterEnabled );
	}
	protected void popCameraNavigationDragAdapterEnabled() {
		//this.cameraNavigationDragAdapter.setEnabled( this.isCameraNavigationDragAdapterEnabledStack.pop() );
	}

	public void orientToUpright( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		org.alice.apis.moveandturn.AbstractTransformable transformable = this.getInstanceInJavaForField( field, org.alice.apis.moveandturn.AbstractTransformable.class );
		if( transformable != null ) {
			transformable.orientToUpright();
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "orientToUpright", field );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "orientToUpright", this.getInstanceInJavaForField( field ) );
		}
	}
	public void placeOnTopOfGround( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		org.alice.apis.moveandturn.Model model = this.getInstanceInJavaForField( field, org.alice.apis.moveandturn.Model.class );
		if( model != null ) {
			org.alice.apis.moveandturn.Scene scene = model.getScene();
			edu.cmu.cs.dennisc.math.AxisAlignedBox bb = model.getAxisAlignedMinimumBoundingBox();
			edu.cmu.cs.dennisc.math.Point3 position = model.getPosition( scene );
			position.y = -bb.getYMinimum();
			model.moveTo( scene.createOffsetStandIn( position ) );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "placeOnTopOfGround", field );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "placeOnTopOfGround", this.getInstanceInJavaForField( field ) );
		}
	}
	@Override
	public void setRenderingEnabled( boolean isRenderingEnabled ) {
		//this.program.getOnscreenLookingGlass().setRenderingEnabled( isRenderingEnabled );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "ignoring: setRenderingEnabled", isRenderingEnabled );
	}

	//	def performOrientToUpright(self, field):
	//		instanceInJava = self.getInstanceInJavaForField( field )
	//		instanceInJava.orientToUpright()
	//		
	//	def performPlaceOnTopOfGround(self, field):
	//		instanceInJava = self.getInstanceInJavaForField( field )
	//		asSeenBy = org.alice.apis.moveandturn.AsSeenBy.SCENE
	//		bb = instanceInJava.getAxisAlignedMinimumBoundingBox()
	//		position = instanceInJava.getPosition( asSeenBy )
	//		position.y = -bb.minimum.y
	//		instanceInJava.moveTo( instanceInJava.acquireStandIn( asSeenBy, position ) )
	//	
	//	def getScene(self):
	//		return self.getSceneInstanceInJava()
	//	
	//	def getSceneType( self ):
	//		return self.getSceneField().getValueType()
	//
	//	def _isFieldNameFree( self, name ):
	//		sceneType = self.getSceneType()
	//		if sceneType:
	//			for field in sceneType.fields.iterator():
	//				if field.getName() == name:
	//					return False
	//		return True 
	//
	//	def _getAvailableFieldName( self, superClassBaseName ):
	//		name = superClassBaseName[ 0 ].lower() + superClassBaseName[ 1: ]
	//		rv = name
	//		i = 2
	//		while not self._isFieldNameFree( rv ):
	//			rv = name + `i`
	//			i += 1
	//		return rv
	//
	//	def _handleCreateInstance( self, type ):
	//		#todo
	//		t = type.getFirstTypeEncounteredDeclaredInJava()
	//		cls = t.getCls()
	//		clsName = cls.__name__
	//		thumbnailsRoot = self._api._getGalleryThumbnailsRoot()
	//		
	//		prefixList = []
	//		prefixList.append( "org.alice.apis.moveandturn.gallery" )
	//		prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery" )
	//		for prefix in prefixList:
	//			if clsName.startswith( prefix ):
	//				file = java.io.File( thumbnailsRoot, prefix + clsName[ len( prefix ): ].replace( ".", "/" ) + ".png" )
	//
	//		inputPane = ecc.dennisc.alice.ide.moveandturn.editors.gallery.CreateInstancePane( self, file, thumbnailsRoot, type )
	//		owner = alice.ide.IDE.getSingleton()
	//		instance = inputPane.showInJDialog( owner, "Create Instance", True )
	//		if instance:
	//			self.addInstance( instance )
	//
	//#	def _select(self, nextField ):
	//#		prevField = self._currSelectedField
	//#		operation = self._createSelectionOperation( nextField, prevField )
	//#		event = None
	//#		alice.ide.IDE.getSingleton().performIfAppropriate( operation, event )
	//
	//	def addASTFieldFor( self, instance ):
	//		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
	//		name = instanceInJava.getName()
	//		#programType = alice.ast.TypeDeclaredInJava.get( nameable.__class__ )
	//		type = instance.getType()
	//		astField = alice.ast.FieldDeclaredInAlice( name, type, alice.ast.InstanceCreation( type.getDeclaredConstructor( [] ), [] ) )
	//		astField.finalVolatileOrNeither.setValue( alice.ast.FieldModifierFinalVolatileOrNeither.FINAL )
	//		astField.access.setValue( alice.ast.Access.PRIVATE )
	//		self.getSceneType().fields.add( [ astField ] )
	//
	//		self.putInstanceForField( astField, instance )
	//		self.getIDE().setFieldSelection( astField )
	//#		self._select( astField )
	//
	//	def addInstance( self, instance ):
	//		self.addASTFieldFor( instance )
	//		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( instance )
	//		if isinstance( instanceInJava, apis.moveandturn.Model ):
	//			camera = self.getScene().findFirstMatch( apis.moveandturn.AbstractCamera )
	//			if camera:
	//				java.lang.Thread( ecc.dennisc.lang.ApplyRunnable( self._getGoodLookAtShowInstanceAndReturnCamera, ( camera, instanceInJava, ) ) ).start()
	//			else:
	//				self.getScene().addComponent( instanceInJava )
	//		else:
	//			self.getScene().addComponent( instanceInJava )
	//		alice.ide.IDE.getSingleton().markChanged( "scene program addInstance" )
	//

	//	def createScene( self, sceneField ):
	//		program = self.getProgram()
	//		if program:
	//			lg = program.getOnscreenLookingGlass()
	//			lg.clearCameras()
	//			program.setScene( None )
	//
	//		sceneInstance = org.alice.apis.moveandturn.ide.editors.scene.MoveAndTurnSceneEditor.createScene( self, sceneField )
	//		if sceneInstance:
	//			self.restoreProjectProperties()
	//		return sceneInstance
	//
	//	def getSceneAutomaticSetUpMethod( self ):
	//		return self.getSceneType().getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )
	//
	//	def getFilledInSceneAutomaticSetUpMethod( self, fillerInner ):
	//		rv = self.getSceneAutomaticSetUpMethod()
	//		map = {}
	//		for key in self.mapFieldToInstance.keySet():
	//			map[ key ] = self.mapFieldToInstance.get( key )
	//		fillerInner.fillInSceneAutomaticSetUpMethod( rv, self.getSceneField(), map )
	//		return rv
	//
	//	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
	//		self.getFilledInSceneAutomaticSetUpMethod( setUpMethodGenerator )
	//
	//	def _createSelectionOperation(self, nextField, prevField ):
	//		return BogusSelectionOperation( nextField, prevField )
	//
	//	def _getGoodLookAtShowInstanceAndReturnCamera( self, camera, instance ):
	//		self.pushAndSetCameraNavigationDragAdapterEnabled( False )
	//		try:
	//			instance.setOpacity( 0.0, apis.moveandturn.Composite.RIGHT_NOW )
	//			pov = camera.getPointOfView( self.getScene() )
	//			camera.getGoodLookAt( instance, 0.5 )
	//			self.getScene().addComponent( instance )
	//			instance.setOpacity( 1.0 )
	//			camera.moveAndOrientTo( self.getScene().createOffsetStandIn( pov.getInternal() ), 0.5 ) 
	//		finally:
	//			self.popCameraNavigationDragAdapterEnabled()
	//
	//	def handleDelete(self, node):
	//		instance = self.mapFieldToInstance.get( node )
	//		if instance:
	//			self.getScene().removeComponent( ecc.dennisc.alice.vm.getInstanceInJava( instance ) )

	//	public static void main( String[] args ) {
	//
	//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
	//
	//		MoveAndTurnSceneEditor moveAndTurnSceneEditor = new MoveAndTurnSceneEditor();
	//
	//		ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
	//
	//		javax.swing.JFrame frame = new javax.swing.JFrame();
	//		frame.getContentPane().add( moveAndTurnSceneEditor );
	//		frame.setSize( 1024, 768 );
	//		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
	//		frame.setVisible( true );
	//	}
}
