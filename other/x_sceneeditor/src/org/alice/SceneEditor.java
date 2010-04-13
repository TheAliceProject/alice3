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
import java.awt.Font;
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
import org.alice.ide.IDE;
import org.alice.ide.event.ProjectOpenEvent;
import org.alice.interact.CameraNavigatorWidget;
import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.PickHint;
import org.alice.stageide.StageIDE;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;
import org.alice.stageide.sceneeditor.viewmanager.CameraViewSelector;
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
	protected edu.cmu.cs.dennisc.alice.Project project = null;
	private java.util.List< org.alice.ide.event.IDEListener > ideListeners = new java.util.LinkedList< org.alice.ide.event.IDEListener >();
	
	private org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor sceneEditor;
	
	Scene scene = new Scene();
	GrassyGround grassyGround = new GrassyGround();
	DirectionalLight sunLight = new DirectionalLight();
	SymmetricPerspectiveCamera camera = new SymmetricPerspectiveCamera();
	Chicken chicken = new Chicken();
	

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
	
	
	protected void setProgramType( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType ) {
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice;
		if( programType != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = programType.getDeclaredFields().get( 0 );
			if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
				fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field;
			} else {
				fieldInAlice = null;
			}
		} else {
			fieldInAlice = null;
		}
		this.setRootField( fieldInAlice );
	}
	
	public void setProject(edu.cmu.cs.dennisc.alice.Project project)
	{
		IDE.getSingleton().setProject( project );
		this.viewSelector.addOrthographicMarkersToScene(this.sceneEditor.getScene());
		//this.sceneEditor.projectOpened( new ProjectOpenEvent(IDE.getSingleton(), this.project, project) );
		this.setProgramType( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)project.getProgramType() );
		this.project = project;
//		this.viewPanel.setProject( project );
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
	
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;
	
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			SceneEditor.this.refreshFields();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			SceneEditor.this.refreshFields();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			SceneEditor.this.refreshFields();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			SceneEditor.this.refreshFields();
		}
	};
	
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
	}
	public void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshFields();
	}
	
	public void refreshFields()
	{
		System.out.println("Refresh!");
		this.viewPanel.refreshFields();
		this.viewSelector.refreshFields();
	}
	
	SceneViewManagerPanel viewPanel = null;
	ManipulationHandleControlPanel handleControlPanel;
	CameraViewSelector viewSelector;
	protected java.io.File projectFile = new java.io.File( edu.cmu.cs.dennisc.alice.project.ProjectUtilities.getMyAliceDirectory("Alice3"), "SCENE_EDITOR_TEST.a3p" );
	protected java.io.File defaultFile = new java.io.File( "C:/Users/Administrator/Documents/Alice3/MyProjects/cameraTest.a3p" );

	private edu.cmu.cs.dennisc.animation.Animator animator = new edu.cmu.cs.dennisc.animation.ClockBasedAnimator();
	
	private edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener automaticDisplayListener = new edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayListener() {
		public void automaticDisplayCompleted( edu.cmu.cs.dennisc.lookingglass.event.AutomaticDisplayEvent e ) {
			SceneEditor.this.animator.update();
		}
	};
	
	protected void initializeUI()
	{
		
		this.remove( this.getOnscreenLookingGlass().getAWTComponent() );
		this.add(this.sceneEditor, BorderLayout.CENTER);
	
		edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().addAutomaticDisplayListener( this.automaticDisplayListener );
		
		this.viewPanel = new SceneViewManagerPanel(this.sceneEditor);
		this.viewSelector = new CameraViewSelector(this.sceneEditor, animator);
		this.handleControlPanel = new ManipulationHandleControlPanel();
		
		JButton saveButton = new JButton("SAVE");
		Font buttonFont = saveButton.getFont();
		System.out.println(buttonFont.getFontName());
		
		saveButton.addActionListener( new ActionListener(){

			public void actionPerformed( ActionEvent e ) {
				SceneEditor.this.saveProjectTo( SceneEditor.this.projectFile );
			}
		});
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
		this.add( this.viewSelector, java.awt.BorderLayout.SOUTH );
		this.add( saveAndLoadPanel, java.awt.BorderLayout.NORTH );
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
		this.sceneEditor = (MoveAndTurnSceneEditor)org.alice.ide.IDE.getSingleton().getSceneEditor();
		this.initializeScene();
		initializeUI();
		this.loadProjectFrom( defaultFile );
		this.viewPanel.setActive( true );
		
	}

	@Override
	protected void run() {
		this.viewSelector.setPersespectiveCamera( this.sceneEditor.getSGPerspectiveCamera());
		this.viewSelector.setOrthographicCamera( this.sceneEditor.getSGOrthographicCamera() );
	}

	
//	static {
//		Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler() {
//			public void uncaughtException( Thread t, Throwable e ) {
//				e.printStackTrace();
//			}
//		} );
//	}
	public static void main(String[] args) {
		
		System.out.println(System.getProperty("java.library.path"));
		
		org.alice.ide.IDE ide = new StageIDE();
		SceneEditor sceneEditor = new SceneEditor();
		sceneEditor.showInJFrame(args, true);
	}
}
