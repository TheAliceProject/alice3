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
package org.alice.apis.moveandturn.ide.editors.scene;

import org.alice.apis.moveandturn.gallery.environments.Ground;
import org.alice.apis.moveandturn.gallery.environments.grounds.DirtGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.MoonSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SandyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.SeaSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SnowyGround;
import org.alice.interact.GlobalDragAdapter;

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends edu.cmu.cs.dennisc.alice.ide.editors.scene.AbstractInstantiatingSceneEditor {
	
	private Program program = this.createProgram();
	private edu.cmu.cs.dennisc.lookingglass.util.CardPane cardPane;
	private edu.cmu.cs.dennisc.alice.ide.editors.scene.ControlsForOverlayPane controlsForOverlayPane = this.createControlsForOverlayPane();
	private edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();

	private org.alice.interact.GlobalDragAdapter globalDragAdapter = new org.alice.interact.GlobalDragAdapter();

	public MoveAndTurnSceneEditor() {
		this.setLayout( new java.awt.BorderLayout() );
		this.program.setArgs( new String[] {} );
		this.program.init();
		this.program.start();
		new Thread() {
			@Override
			public void run() {
				edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = MoveAndTurnSceneEditor.this.program.getOnscreenLookingGlass();
				while( onscreenLookingGlass == null ) {
					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
					onscreenLookingGlass = MoveAndTurnSceneEditor.this.program.getOnscreenLookingGlass();
				}
				MoveAndTurnSceneEditor.this.cardPane = new edu.cmu.cs.dennisc.lookingglass.util.CardPane( onscreenLookingGlass );
				MoveAndTurnSceneEditor.this.add( MoveAndTurnSceneEditor.this.cardPane, java.awt.BorderLayout.CENTER );
				MoveAndTurnSceneEditor.this.cameraNavigationDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
				MoveAndTurnSceneEditor.this.globalDragAdapter.setOnscreenLookingGlass( onscreenLookingGlass );
				edu.cmu.cs.dennisc.animation.Animator animator = MoveAndTurnSceneEditor.this.program.getAnimator();
				while( animator == null ) {
					edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 50 );
					animator = MoveAndTurnSceneEditor.this.program.getAnimator();
				}
				MoveAndTurnSceneEditor.this.globalDragAdapter.setAnimator( animator );
			}
		}.start();
	}

	private static boolean isGround( org.alice.apis.moveandturn.Model model ) {
		Class[] clses = { Ground.class, GrassyGround.class, DirtGround.class, MoonSurface.class, SandyGround.class, SeaSurface.class, SnowyGround.class };
		for( Class<? extends org.alice.apis.moveandturn.PolygonalModel> cls : clses ) {
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
					sgTransformable.putBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY, model.getAxisAlignedMinimumBoundingBox());
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
	protected edu.cmu.cs.dennisc.alice.ide.editors.scene.ControlsForOverlayPane createControlsForOverlayPane() {
		return new edu.cmu.cs.dennisc.alice.ide.editors.scene.ControlsForOverlayPane();
	}
	protected Program createProgram() {
		return new Program( this );
	}
	protected Program getProgram() {
		return this.program;
	}
	public void initializeLightweightOnscreenLookingGlass( edu.cmu.cs.dennisc.lookingglass.LightweightOnscreenLookingGlass lightweightOnscreenLookingGlass ) {
		javax.swing.JPanel panel = lightweightOnscreenLookingGlass.getJPanel();
		panel.setLayout( new java.awt.BorderLayout() );
		panel.add( this.controlsForOverlayPane );
	}
	
	@Override
	public void fieldSelectionChanged( edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e ) {
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
	}
	
	
	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice sceneField ) {
		Object rv = super.createScene( sceneField );
		edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType = sceneField.getValueType();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSetUp" );
		this.getVM().invokeEntryPoint( method, rv );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)rv).getInstanceInJava();
		this.program.setScene( scene );
		this.controlsForOverlayPane.setRootField( sceneField );
		return rv;
	}
	
	public void setDragInProgress( boolean isDragInProgress ) {
		if( isDragInProgress ) {
			this.cardPane.showSnapshot();
		} else {
			this.cardPane.showLive();
		}
	}
	
	public Object createInstance( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		return this.getVM().createInstanceEntryPoint( type );
	}

	public void preserveProjectProperties() {
		this.preserveCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}
	public void restoreProjectProperties() {
		this.restoreCameraNavigationProperties( this.cameraNavigationDragAdapter );
	}
	
	private java.util.Stack< Boolean > isCameraNavigationDragAdapterEnabledStack = new java.util.Stack< Boolean >();
	protected void pushAndSetCameraNavigationDragAdapterEnabled( Boolean isCameraNavigationDragAdapterEnabled ) {
		this.isCameraNavigationDragAdapterEnabledStack.push( this.cameraNavigationDragAdapter.isEnabled() );
		this.cameraNavigationDragAdapter.setEnabled( isCameraNavigationDragAdapterEnabled );
	}
	protected void popCameraNavigationDragAdapterEnabled() {
		this.cameraNavigationDragAdapter.setEnabled( this.isCameraNavigationDragAdapterEnabledStack.pop() );
	}

	public static void main( String[] args ) {
		
		edu.cmu.cs.dennisc.alice.ide.IDE ide = new edu.cmu.cs.dennisc.alice.ide.IDE() {
			@Override
			public edu.cmu.cs.dennisc.alice.ast.Node createCopy( edu.cmu.cs.dennisc.alice.ast.Node original ) {
				return null;
			}
			@Override
			protected edu.cmu.cs.dennisc.alice.ide.Operation createExitOperation() {
				return null;
			}
			@Override
			public void createProjectFromBootstrap() {
			}
			@Override
			protected edu.cmu.cs.dennisc.alice.ide.Operation createRunOperation() {
				return null;
			}
			@Override
			protected void generateCodeForSceneSetUp() {
			}
			@Override
			public java.io.File getApplicationRootDirectory() {
				return null;
			}
			@Override
			protected edu.cmu.cs.dennisc.alice.ide.editors.code.CodeEditor getCodeEditorInFocus() {
				return null;
			}
			@Override
			protected void handleWindowClosing() {
			}
			@Override
			public void promptUserForStatement( java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) {
			}
			@Override
			public void promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, java.awt.event.MouseEvent e,
					edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
			}
			@Override
			public void promptUserForMore( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
			}
			
			@Override
			public void unsetPreviousExpression() {
			}
			@Override
			protected void preserveProjectProperties() {
			}
		};
		
		MoveAndTurnSceneEditor moveAndTurnSceneEditor = new MoveAndTurnSceneEditor();
		
		ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
		
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( moveAndTurnSceneEditor );
		frame.setSize( 1024, 768 );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );
	}
}
