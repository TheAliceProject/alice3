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
	private org.alice.apis.moveandturn.Program program = new org.alice.apis.moveandturn.Program() {
		@Override
		protected boolean isLightweightOnscreenLookingGlassDesired() {
			return true;
		}
		@Override
		protected boolean isSpeedMultiplierControlPanelDesired() {
			return false;
		}
		@Override
		protected void initialize() {
		}
		@Override
		protected void run() {
		}
	};
	
	public MoveAndTurnSceneEditor() {
		program.showInAWTContainer( this, new String[] {} );
	}
	
	@Override
	protected Object createScene( edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType ) {
		Object rv = super.createScene( sceneType );
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( "performSetUp" );
		this.getVM().invokeEntryPoint( method, rv );
		
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)((edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)rv).getInstanceInJava();
		this.program.setScene( scene );
		return rv;
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
		frame.setDefaultCloseOperation( javax.swing.JFrame.DISPOSE_ON_CLOSE );
		frame.setVisible( true );
	}
}
