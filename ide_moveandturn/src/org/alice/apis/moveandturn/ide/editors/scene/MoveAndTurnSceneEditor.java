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

/**
 * @author Dennis Cosgrove
 */
public class MoveAndTurnSceneEditor extends edu.cmu.cs.dennisc.alice.ide.editors.scene.AbstractInstantiatingSceneEditor {
	
	private Program program = this.createProgram();
	private edu.cmu.cs.dennisc.lookingglass.util.CardPane cardPane;
	private edu.cmu.cs.dennisc.alice.ide.editors.scene.ControlsForOverlayPane controlsForOverlayPane = this.createControlsForOverlayPane();
	private edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();


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
			}
		}.start();
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
