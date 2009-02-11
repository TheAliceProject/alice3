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

package org.alice.interact;
import org.alice.apis.moveandturn.DirectionalLight;
import org.alice.apis.moveandturn.Program;
import org.alice.apis.moveandturn.Scene;
import org.alice.apis.moveandturn.SymmetricPerspectiveCamera;
import org.alice.apis.moveandturn.TurnDirection;
import org.alice.apis.moveandturn.gallery.animals.Chicken;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.util.Jack;
import edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter;


/**
 * @author David Culyba
 */
public class SceneEditor extends Program {
	
	public static final String BOUNDING_BOX_KEY = "BOUNDING_BOX_KEY";
	
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
	CameraNavigationDragAdapter cameraNavigationDragAdapter = new CameraNavigationDragAdapter();

	@Override
	protected void initialize() {
		scene.addComponent(grassyGround);
		grassyGround.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.GROUND );
		scene.addComponent(chicken);
		chicken.getSGComposite().putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.MOVEABLE_OBJECTS );
		chicken.getSGComposite().putBonusDataFor( SceneEditor.BOUNDING_BOX_KEY, chicken.getAxisAlignedMinimumBoundingBox());
		
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
		globalDragAdapter.setOnscreenLookingGlass(this.getOnscreenLookingGlass());
		
		
//		cameraNavigationDragAdapter.setOnscreenLookingGlass(this
//				.getOnscreenLookingGlass());
	}

	@Override
	protected void run() {
		globalDragAdapter.setAnimator( this.getAnimator() );
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
