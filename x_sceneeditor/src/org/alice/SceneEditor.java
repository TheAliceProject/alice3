/**
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

package org.alice;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.alice.apis.moveandturn.DirectionalLight;
import org.alice.apis.moveandturn.Program;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.SymmetricPerspectiveCamera;
import org.alice.apis.moveandturn.TurnDirection;
import org.alice.apis.moveandturn.gallery.animals.Chicken;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;
import org.alice.interact.CameraNavigatorWidget;
import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.PickHint;
import org.alice.stageide.sceneeditor.viewmanager.ManipulationHandleControlPanel;
import org.alice.stageide.sceneeditor.viewmanager.SceneViewManagerPanel;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter;


/**
 * @author David Culyba
 */
public class SceneEditor extends Program {
	
	
	
	static {
		Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler() {
			public void uncaughtException( Thread thread, Throwable throwable ) {
				throwable.printStackTrace();
			}
		});
	}
	
	
	//IDE level stuff
	protected edu.cmu.cs.dennisc.alice.Project project;
	private java.util.List< org.alice.ide.event.IDEListener > ideListeners = new java.util.LinkedList< org.alice.ide.event.IDEListener >();
	
	Scene scene = new Scene();
	GrassyGround grassyGround = new GrassyGround();
	DirectionalLight sunLight = new DirectionalLight();
	SymmetricPerspectiveCamera camera = new SymmetricPerspectiveCamera();
	Chicken chicken = new Chicken();

//	org.alice.interact.RuntimeDragAdapter globalDragAdapter = new org.alice.interact.RuntimeDragAdapter();
	org.alice.interact.GlobalDragAdapter globalDragAdapter = new org.alice.interact.GlobalDragAdapter();
	org.alice.interact.CreateASimDragAdapter simDragAdapter = new org.alice.interact.CreateASimDragAdapter();
	CameraNavigationDragAdapter cameraNavigationDragAdapter = new CameraNavigationDragAdapter();
	

	public void addIDEListener( org.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.add( l );
		}
	}

	public void removeIDEListener( org.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.remove( l );
		}
	}
	
	protected void fireProjectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : this.ideListeners ) {
				l.projectOpening( e );
			}
		}
	}

	protected void fireProjectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : this.ideListeners ) {
				l.projectOpened( e );
			}
		}
	}
	
	public void setProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		this.viewPanel.setProject( project );
	}
	
	public void loadProjectFrom( java.io.File file ) {
		if( file.exists() ) {
			this.setProject( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.readProject( file ) );
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( "Cannot read project from file:\n\t" );
			sb.append( file.getAbsolutePath() );
			sb.append( "\nIt does not exist." );
			javax.swing.JOptionPane.showMessageDialog( this, sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE );
		}
	}
	
	public void loadProjectFrom( String path ) {
		loadProjectFrom( new java.io.File( path ) );
	}
	
	public void saveProjectTo( java.io.File file ) {
		this.viewPanel.saveToProject();
		try
		{
			edu.cmu.cs.dennisc.alice.project.ProjectUtilities.writeProject( file, this.project );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "project saved to: ", file.getAbsolutePath() );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveProjectTo( String path ) {
		saveProjectTo( new java.io.File( path ) );
	}
	
	@Override
	protected boolean isLightweightOnscreenLookingGlassDesired() {
		return false;
	}
	
	SceneViewManagerPanel viewPanel = new SceneViewManagerPanel();
	ManipulationHandleControlPanel handleControlPanel;
	protected java.io.File projectFile = new java.io.File( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyAliceDirectory("Alice3"), "SCENE_EDITOR_TEST.a3p" );
	protected java.io.File defaultFile = new java.io.File( "C:/AliceApplicationFiles/AliceBaseApplication/application/projects/templates/GrassyProject.a3p" );
	
	protected void initializeUI()
	{
		CameraNavigatorWidget controlPanel = new CameraNavigatorWidget(globalDragAdapter);
		
		globalDragAdapter.setOnscreenLookingGlass(this.getOnscreenLookingGlass());
		
		this.viewPanel.setCamera(camera.getSGPerspectiveCamera());
		
		this.handleControlPanel = new ManipulationHandleControlPanel();
		this.handleControlPanel.setDragAdapter( this.globalDragAdapter );
		
		JButton saveButton = new JButton("SAVE");
		saveButton.addActionListener( new ActionListener(){

			public void actionPerformed( ActionEvent e ) {
				SceneEditor.this.saveProjectTo( SceneEditor.this.projectFile );
			}
		});
		
		JPanel widgetPanel = new JPanel();
		widgetPanel.setLayout(new FlowLayout());
		widgetPanel.add(controlPanel);
		//widgetPanel.add(this.viewPanel);
		
		JButton loadButton = new JButton("LOAD");
		loadButton.addActionListener( new ActionListener(){

			public void actionPerformed( ActionEvent e ) {
				SceneEditor.this.loadProjectFrom( SceneEditor.this.projectFile );
			}
		});
		javax.swing.JPanel saveAndLoadPanel = new JPanel();
		saveAndLoadPanel.setLayout( new FlowLayout());
		saveAndLoadPanel.add( saveButton );
		saveAndLoadPanel.add( loadButton );
		
		this.add( saveAndLoadPanel, java.awt.BorderLayout.NORTH );
		this.add( widgetPanel, java.awt.BorderLayout.SOUTH );
		this.add( (JPanel)(this.viewPanel), java.awt.BorderLayout.EAST );
		this.add( (JPanel)(this.handleControlPanel), BorderLayout.WEST );
	}
	
	protected void initializeScene()
	{
		scene.addComponent(grassyGround);
		grassyGround.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.GROUND );
		scene.addComponent(chicken);
		chicken.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.MOVEABLE_OBJECTS );
		chicken.getSGComposite().putBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY, chicken.getAxisAlignedMinimumBoundingBox());
		
		scene.addComponent(sunLight);
		sunLight.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.LIGHT );
		scene.addComponent(camera);
		camera.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.CAMERA );
		grassyGround.setVehicle(scene);
		sunLight.setVehicle(scene);
		camera.setVehicle(scene);
		
		edu.cmu.cs.dennisc.math.Vector3 initialPoint = new edu.cmu.cs.dennisc.math.Vector3(0.0d, 5.0d, 5.0d);
		edu.cmu.cs.dennisc.math.Vector3 initialOrigin = new edu.cmu.cs.dennisc.math.Vector3(0.0d, 0.0d, 0.0d);
		edu.cmu.cs.dennisc.math.Vector3 forward = edu.cmu.cs.dennisc.math.Vector3.createSubtraction( initialOrigin, initialPoint );
		forward.normalize();
		edu.cmu.cs.dennisc.math.Vector3 up = new Vector3(0.0d, 1.0d, 0.0d);
		edu.cmu.cs.dennisc.math.ForwardAndUpGuide forwardAndUp = new edu.cmu.cs.dennisc.math.ForwardAndUpGuide(forward, up);
		AffineMatrix4x4 cameraTransformation = camera.getLocalTransformation();
		cameraTransformation.orientation.setValue( forwardAndUp );
		cameraTransformation.translation.set( initialPoint );
		camera.setLocalTransformation( cameraTransformation );
		
		chicken.setVehicle(scene);
		this.setScene(this.scene);
		sunLight.turn(TurnDirection.FORWARD, 0.25);
	}
	
	@Override
	protected void initialize() {
		this.initializeScene();
		initializeUI();
		this.loadProjectFrom( defaultFile );
		
		this.viewPanel.setActive( true );
		
		
		
		
		
		
		globalDragAdapter.setSelectedObject( chicken.getSGTransformable() );
		
	}

	@Override
	protected void run() {
		globalDragAdapter.setAnimator( this.getAnimator() );
		this.viewPanel.setCamera(globalDragAdapter.getOnscreenLookingGlass().getCameraAt( 0 ));
	}

	
//	static {
//		Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler() {
//			public void uncaughtException( Thread t, Throwable e ) {
//				e.printStackTrace();
//			}
//		} );
//	}
	public static void main(String[] args) {
		SceneEditor sceneEditor = new SceneEditor();
		sceneEditor.showInJFrame(args, true);
	}
}
