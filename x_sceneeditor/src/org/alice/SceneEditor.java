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
import javax.swing.JPanel;

import org.alice.apis.moveandturn.DirectionalLight;
import org.alice.apis.moveandturn.Program;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.SymmetricPerspectiveCamera;
import org.alice.apis.moveandturn.TurnDirection;
import org.alice.apis.moveandturn.gallery.animals.Chicken;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;
import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.PickHint;
import org.alice.interact.PointOfViewManager;
import org.alice.interact.PointsOfViewPanel;

import edu.cmu.cs.dennisc.alice.ast.ThisExpression;
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
	

	Scene scene = new Scene();
	GrassyGround grassyGround = new GrassyGround();
	DirectionalLight sunLight = new DirectionalLight();
	SymmetricPerspectiveCamera camera = new SymmetricPerspectiveCamera();
	Chicken chicken = new Chicken();

	org.alice.interact.GlobalDragAdapter globalDragAdapter = new org.alice.interact.GlobalDragAdapter();
	org.alice.interact.CreateASimDragAdapter simDragAdapter = new org.alice.interact.CreateASimDragAdapter();
	CameraNavigationDragAdapter cameraNavigationDragAdapter = new CameraNavigationDragAdapter();
	
	PointOfViewManager pointOfViewManager = new PointOfViewManager();

	@Override
	protected void initialize() {
		scene.addComponent(grassyGround);
		grassyGround.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.GROUND );
		scene.addComponent(chicken);
		chicken.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.MOVEABLE_OBJECTS );
		chicken.getSGComposite().putBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY, chicken.getAxisAlignedMinimumBoundingBox());
		
//		Jack jack = new Jack();
//		jack.setModest( false );
//		jack.setParent( chicken.getSGComposite() );
//		
//		Jack jack2 = new Jack();
//		jack2.setModest( false );
//		jack2.setParent( scene.getSGComposite() );
		
		scene.addComponent(sunLight);
		sunLight.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.LIGHT );
		scene.addComponent(camera);
		camera.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.CAMERA );
		grassyGround.setVehicle(scene);
		sunLight.setVehicle(scene);
		camera.setVehicle(scene);
		
		//scene.getSGComposite().addComponent( myFirstHandle );
		
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
		simDragAdapter.setOnscreenLookingGlass(this.getOnscreenLookingGlass());
		
		this.pointOfViewManager.setCamera(camera.getSGPerspectiveCamera());
		PointsOfViewPanel povPanel = new PointsOfViewPanel(this.pointOfViewManager);
		
		this.add(povPanel, java.awt.BorderLayout.SOUTH);
		
		
		simDragAdapter.setSelectedObject( chicken.getSGTransformable() );
		
		//cameraNavigationDragAdapter.setOnscreenLookingGlass(this.getOnscreenLookingGlass());
	}

	@Override
	protected void run() {
		globalDragAdapter.setAnimator( this.getAnimator() );
		this.pointOfViewManager.setOnscreenLookingGlass(globalDragAdapter.getOnscreenLookingGlass());
	}

	
	static {
		Thread.setDefaultUncaughtExceptionHandler( new Thread.UncaughtExceptionHandler() {
			public void uncaughtException( Thread t, Throwable e ) {
				e.printStackTrace();
			}
		} );
	}
	public static void main(String[] args) {
		SceneEditor sceneEditor = new SceneEditor();
		sceneEditor.showInJFrame(args, true);
	}
}
