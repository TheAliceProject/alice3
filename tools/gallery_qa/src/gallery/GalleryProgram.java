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

package gallery;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.alice.ide.croquet.models.gallerybrowser.ArgumentTypeGalleryNode;
import org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode;
import org.alice.ide.croquet.models.gallerybrowser.GalleryNode;
import org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState;
import org.lgna.common.ComponentThread;
import org.lgna.croquet.State;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.story.*;
import org.lgna.story.implementation.BasicJointedModelImp;
import org.lgna.story.resources.BasicResource;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.PropResource;
import org.lgna.story.resources.QuadrupedResource;
import org.lgna.story.resources.SwimmerResource;
import org.lgna.story.resources.VehicleResource;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.FullBodyOutfitManager;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.HairManager;
import org.lgna.story.resources.sims2.IngredientManager;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.PersonResource;
import org.lgna.story.resources.sims2.SkinTone;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Dennis Cosgrove
 */
public class GalleryProgram extends Program {
	private final Camera camera = new Camera();
	private final GalleryScene scene = new GalleryScene( camera );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter modelManipulationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter();

	private final Biped ogre = new Biped( org.lgna.story.resources.biped.Ogre.BROWN_OGRE );
	private final State.ValueListener<GalleryNode> galleryListener = new State.ValueListener<GalleryNode>() {
		public void changing(State<GalleryNode> state, GalleryNode prevValue,
				GalleryNode nextValue, boolean isAdjusting) {
		}
		public void changed(State<GalleryNode> state, GalleryNode prevValue,
				GalleryNode nextValue, boolean isAdjusting) {
			JointedModel model;
			if (nextValue instanceof FieldGalleryNode) {
				FieldGalleryNode fieldGalleryNode = (FieldGalleryNode) nextValue;
				AbstractField field = fieldGalleryNode.getDeclaration();
				if (field instanceof JavaField) {
					JavaField javaField = (JavaField) field;
					Field fld = javaField.getFieldReflectionProxy().getReification();
					try {
						Object constant = fld.get( null );
						if (constant instanceof BasicResource) {
							BasicResource basicResource = (BasicResource) constant;
							if (basicResource instanceof PropResource) {
								PropResource propResource = (PropResource) basicResource;
								model = new Prop( propResource );
							}else{
								model = null;
							}
						}else if (constant instanceof BipedResource) {
							BipedResource bipedResource = (BipedResource) constant;
							model = new Biped( bipedResource );
						} else if (constant instanceof FlyerResource) {
							FlyerResource flyerResource = (FlyerResource) constant;
							model = new Flyer( flyerResource );
						} else if (constant instanceof QuadrupedResource) {
							QuadrupedResource quadrupedResource = (QuadrupedResource) constant;
							model = new Quadruped( quadrupedResource );
						} else if (constant instanceof SwimmerResource) {
							SwimmerResource swimmerResource = (SwimmerResource) constant;
							model = new Swimmer( swimmerResource );
						} else if (constant instanceof VehicleResource) {
							VehicleResource vehicleResource = (VehicleResource) constant;
							model = new Vehicle( vehicleResource );
						} else {
							model = null;
						}
					} catch (IllegalArgumentException e) {
						throw new RuntimeException( e );
					} catch (IllegalAccessException e) {
						throw new RuntimeException( e );
					}
				} else {
					model = null;
				}
			} else if( nextValue instanceof ArgumentTypeGalleryNode) {
				ArgumentTypeGalleryNode argumentTypeGalleryNode = (ArgumentTypeGalleryNode) nextValue;
				AbstractType<?,?,?> type = argumentTypeGalleryNode.getDeclaration();
				if (type instanceof JavaType) {
					JavaType javaType = (JavaType) type;
					if( javaType.isAssignableTo( PersonResource.class ) ) {
						LifeStage lifeStage = LifeStage.ADULT;
						Gender gender = Gender.getRandom();
						model = new Biped( new AdultPersonResource(
								gender, 
								BaseSkinTone.getRandom(), 
								BaseEyeColor.getRandom(), 
								HairManager.getSingleton().getRandomEnumConstant(lifeStage, gender),
								0.5,
								FullBodyOutfitManager.getSingleton().getRandomEnumConstant(lifeStage, gender)
						) );
					} else {
						model = null;
					}
				} else {
					model = null;
				}
			} else {
				model = null;
			}
			scene.setModel( model );
			animate();
		}
	};
	private void animate() {
		Model model = scene.getModel();
		if (model instanceof Biped) {
			Biped biped = (Biped) model;
			warmUp(biped);
		} else if (model instanceof Quadruped) {
			Quadruped quadruped = (Quadruped) model;
			warmUp(quadruped);
		} else if (model instanceof Flyer) {
			Flyer flyer = (Flyer) model;
			warmUp(flyer);
		}
	}
	
	private void warmUp(Flyer flyer) {
		final Joint rightKnee = flyer.getRightKnee();
		final Joint rightHip = flyer.getRightHip();
		final Joint leftKnee = flyer.getLeftKnee();
		final Joint leftHip = flyer.getLeftHip();
		final Joint rightShoulder = flyer.getRightShoulder();
		final Joint rightElbow = flyer.getRightElbow();
		final Joint leftShoulder = flyer.getLeftShoulder();
		final Joint leftElbow = flyer.getLeftElbow();
		final Joint head = flyer.getHead();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				head.turn( TurnDirection.FORWARD, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.BACKWARD, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.LEFT, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.RIGHT, .12, Turn.duration(0.5) );
			}
		}, "head" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftShoulder.turn(TurnDirection.BACKWARD, 0.12, Turn.duration(0.5) );
				leftShoulder.turn(TurnDirection.FORWARD, 0.25);
				leftShoulder.turn(TurnDirection.BACKWARD, 0.25);
				leftShoulder.turn(TurnDirection.FORWARD, 0.12, Turn.duration(0.5) );
			}
		}, "leftArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightShoulder.turn(TurnDirection.BACKWARD, 0.12, Turn.duration(0.5) );
				rightShoulder.turn(TurnDirection.FORWARD, 0.25);
				rightShoulder.turn(TurnDirection.BACKWARD, 0.25);
				rightShoulder.turn(TurnDirection.FORWARD, 0.12, Turn.duration(0.5) );
			}
		}, "rightArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightHip.turn(TurnDirection.RIGHT, 0.15);
				rightHip.turn(TurnDirection.LEFT, 0.15);
				rightHip.turn(TurnDirection.BACKWARD, 0.25);
				rightKnee.turn(TurnDirection.FORWARD, 0.25);
				rightKnee.turn(TurnDirection.BACKWARD, 0.25);
				rightHip.turn(TurnDirection.FORWARD, 0.25);
			}
		}, "leftLeg" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftHip.turn(TurnDirection.LEFT, 0.15);
				leftHip.turn(TurnDirection.RIGHT, 0.15);
				leftHip.turn(TurnDirection.BACKWARD, 0.25);
				leftKnee.turn(TurnDirection.FORWARD, 0.25);
				leftKnee.turn(TurnDirection.BACKWARD, 0.25);
				leftHip.turn(TurnDirection.FORWARD, 0.25);
			}
		}, "rightLeg" ).start();
		
	}

	private void warmUp(Quadruped quadruped) {
		final Joint rightHip = quadruped.getBackRightHip();
		final Joint leftHip = quadruped.getBackLeftHip();
		final Joint leftKnee = quadruped.getBackLeftKnee();
		final Joint rightKnee = quadruped.getBackRightKnee();
		final Joint rightShoulder = quadruped.getFrontRightShoulder();
		final Joint leftShoulder = quadruped.getFrontLeftShoulder();
		final Joint leftElbow = quadruped.getFrontLeftKnee();
		final Joint rightElbow = quadruped.getFrontRightKnee();
		final Joint neck = quadruped.getNeck();
		final Joint head = quadruped.getHead();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				neck.turn( TurnDirection.FORWARD, 0.25 );
				neck.turn( TurnDirection.BACKWARD, 0.25 );
				head.turn( TurnDirection.FORWARD, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.BACKWARD, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.LEFT, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.RIGHT, .12, Turn.duration(0.5) );
			}
		}, "head" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightShoulder.turn( TurnDirection.BACKWARD, 0.25 );
				rightElbow.turn( TurnDirection.FORWARD, 0.25 );
				rightElbow.turn( TurnDirection.BACKWARD, 0.25 );
				rightShoulder.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "rightFront" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftShoulder.turn( TurnDirection.BACKWARD, 0.25 );
				leftElbow.turn( TurnDirection.FORWARD, 0.25 );
				leftElbow.turn( TurnDirection.BACKWARD, 0.25 );
				leftShoulder.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "leftFront" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftHip.turn( TurnDirection.BACKWARD, 0.25 );
				leftKnee.turn( TurnDirection.FORWARD, 0.25 );
				leftKnee.turn( TurnDirection.BACKWARD, 0.25 );
				leftHip.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "rightBack" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightHip.turn( TurnDirection.BACKWARD, 0.25 );
				rightKnee.turn( TurnDirection.FORWARD, 0.25 );
				rightKnee.turn( TurnDirection.BACKWARD, 0.25 );
				rightHip.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "leftBack" ).start();
	}

	private void warmUp(Biped biped) {
		final Joint leftShoulder = biped.getLeftShoulder();
		final Joint rightShoulder = biped.getRightShoulder();
		final Joint rightElbow = biped.getRightElbow();
		final Joint leftElbow = biped.getLeftElbow();
		final Joint rightHip = biped.getRightHip();
		final Joint leftHip = biped.getLeftHip();
		final Joint rightKnee = biped.getRightKnee();
		final Joint head = biped.getHead();
		final Joint leftKnee = biped.getLeftKnee();new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				head.turn( TurnDirection.FORWARD, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.BACKWARD, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.LEFT, .12, Turn.duration(0.5) );
				head.turn( TurnDirection.RIGHT, .12, Turn.duration(0.5) );
			}
		}, "head" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftShoulder.turn(TurnDirection.BACKWARD, 0.40);
				leftShoulder.turn(TurnDirection.FORWARD, 0.40);
				leftShoulder.turn(TurnDirection.RIGHT, 0.25, Turn.duration(0.5));
				leftElbow.turn(TurnDirection.RIGHT, 0.25, Turn.duration(0.5));
				leftElbow.turn(TurnDirection.BACKWARD, 0.25);
				leftElbow.turn(TurnDirection.FORWARD, 0.25);
				leftElbow.turn(TurnDirection.LEFT, 0.25, Turn.duration(0.5));
				leftShoulder.turn(TurnDirection.LEFT, 0.25, Turn.duration(0.5));
			}
		}, "leftArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightShoulder.turn(TurnDirection.BACKWARD, 0.40);
				rightShoulder.turn(TurnDirection.FORWARD, 0.40);
				rightShoulder.turn(TurnDirection.LEFT, 0.25, Turn.duration(0.5));
				rightElbow.turn(TurnDirection.LEFT, 0.25, Turn.duration(0.5));
				rightElbow.turn(TurnDirection.BACKWARD, 0.25);
				rightElbow.turn(TurnDirection.FORWARD, 0.25);
				rightElbow.turn(TurnDirection.RIGHT, 0.25, Turn.duration(0.5));
				rightShoulder.turn(TurnDirection.RIGHT, 0.25, Turn.duration(0.5));
			}
		}, "rightArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightHip.turn(TurnDirection.RIGHT, 0.15);
				rightHip.turn(TurnDirection.LEFT, 0.15);
				rightHip.turn(TurnDirection.BACKWARD, 0.25);
				rightKnee.turn(TurnDirection.FORWARD, 0.25);
				rightKnee.turn(TurnDirection.BACKWARD, 0.25);
				rightHip.turn(TurnDirection.FORWARD, 0.25);
			}
		}, "leftLeg" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftHip.turn(TurnDirection.LEFT, 0.15);
				leftHip.turn(TurnDirection.RIGHT, 0.15);
				leftHip.turn(TurnDirection.BACKWARD, 0.25);
				leftKnee.turn(TurnDirection.FORWARD, 0.25);
				leftKnee.turn(TurnDirection.BACKWARD, 0.25);
				leftHip.turn(TurnDirection.FORWARD, 0.25);
			}
		}, "rightLeg" ).start();
	}

	private void initializeTest() {
		this.setActiveScene( this.scene );
		this.modelManipulationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.setOnscreenLookingGlass( ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );
		
		GalleryResourceTreeSelectionState.getInstance().addValueListener( this.galleryListener );
		
		Logger.todo( "remove ogre" );
		this.scene.setModel( this.ogre );
	}
	public static void main( String[] args ) {
		Logger.setLevel( Level.WARNING );
		GalleryApplication app = new GalleryApplication();
		app.initialize( args );
		app.setPerspective( new gallery.croquet.GalleryPerspective() );
		GalleryProgram program = new GalleryProgram();

		test.ik.croquet.SceneComposite.getInstance().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );
	}
}
