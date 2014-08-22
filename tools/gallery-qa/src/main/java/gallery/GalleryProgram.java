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

import org.alice.stageide.modelresource.ResourceNode;
import org.lgna.croquet.State;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.JavaField;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SFlyer;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.SModel;
import org.lgna.story.SProgram;
import org.lgna.story.SProp;
import org.lgna.story.SQuadruped;
import org.lgna.story.SSwimmer;
import org.lgna.story.STransport;
import org.lgna.story.Turn;
import org.lgna.story.TurnDirection;
import org.lgna.story.resources.BasicResource;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.PropResource;
import org.lgna.story.resources.QuadrupedResource;
import org.lgna.story.resources.SwimmerResource;
import org.lgna.story.resources.TransportResource;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.FullBodyOutfitManager;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.HairManager;
import org.lgna.story.resources.sims2.LifeStage;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Dennis Cosgrove
 */
public class GalleryProgram extends SProgram {
	private final SCamera camera = new SCamera();
	private final GalleryScene scene = new GalleryScene( camera );
	private final edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter cameraNavigationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.CameraNavigationDragAdapter();
	private final edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter modelManipulationDragAdapter = new edu.cmu.cs.dennisc.ui.lookingglass.ModelManipulationDragAdapter();

	private final SBiped ogre = new SBiped( org.lgna.story.resources.biped.OgreResource.BROWN );
	private final State.ValueListener<ResourceNode> galleryListener = new State.ValueListener<ResourceNode>() {
		public void changing( State<ResourceNode> state, ResourceNode prevValue, ResourceNode nextValue, boolean isAdjusting ) {
		}

		private SJointedModel createJointedModelFromModelResource( org.lgna.story.resources.ModelResource constant ) {
			if( constant instanceof BasicResource ) {
				BasicResource basicResource = (BasicResource)constant;
				if( basicResource instanceof PropResource ) {
					PropResource propResource = (PropResource)basicResource;
					return new SProp( propResource );
				} else {
					return null;
				}
			} else if( constant instanceof BipedResource ) {
				BipedResource bipedResource = (BipedResource)constant;
				return new SBiped( bipedResource );
			} else if( constant instanceof FlyerResource ) {
				FlyerResource flyerResource = (FlyerResource)constant;
				return new SFlyer( flyerResource );
			} else if( constant instanceof QuadrupedResource ) {
				QuadrupedResource quadrupedResource = (QuadrupedResource)constant;
				return new SQuadruped( quadrupedResource );
			} else if( constant instanceof SwimmerResource ) {
				SwimmerResource swimmerResource = (SwimmerResource)constant;
				return new SSwimmer( swimmerResource );
			} else if( constant instanceof TransportResource ) {
				TransportResource transportResource = (TransportResource)constant;
				return new STransport( transportResource );
			} else {
				return null;
			}

		}

		public void changed( State<ResourceNode> state, ResourceNode prevValue, ResourceNode nextValue, boolean isAdjusting ) {
			SJointedModel model;
			org.alice.stageide.modelresource.ResourceKey resourceKey = nextValue.getResourceKey();
			if( resourceKey.isLeaf() ) {
				if( resourceKey instanceof org.alice.stageide.modelresource.EnumConstantResourceKey ) {
					org.alice.stageide.modelresource.EnumConstantResourceKey fieldResourceNode = (org.alice.stageide.modelresource.EnumConstantResourceKey)resourceKey;
					AbstractField field = fieldResourceNode.getField();
					if( field instanceof JavaField ) {
						JavaField javaField = (JavaField)field;
						Field fld = javaField.getFieldReflectionProxy().getReification();
						try {
							Object constant = fld.get( null );
							if( constant instanceof org.lgna.story.resources.ModelResource ) {
								org.lgna.story.resources.ModelResource modelResource = (org.lgna.story.resources.ModelResource)constant;
								model = this.createJointedModelFromModelResource( modelResource );
							} else {
								model = null;
							}
						} catch( IllegalArgumentException e ) {
							throw new RuntimeException( e );
						} catch( IllegalAccessException e ) {
							throw new RuntimeException( e );
						}
					} else {
						model = null;
					}
				} else if( resourceKey instanceof org.alice.stageide.modelresource.ClassResourceKey ) {
					org.alice.stageide.modelresource.ClassResourceKey classResourceKey = (org.alice.stageide.modelresource.ClassResourceKey)resourceKey;
					org.lgna.story.resources.ModelResource constant = classResourceKey.getModelResourceCls().getEnumConstants()[ 0 ];
					model = this.createJointedModelFromModelResource( constant );
				} else if( resourceKey instanceof org.alice.stageide.modelresource.PersonResourceKey ) {
					org.alice.stageide.modelresource.PersonResourceKey personResourceKey = (org.alice.stageide.modelresource.PersonResourceKey)resourceKey;
					LifeStage lifeStage = LifeStage.ADULT;
					Gender gender = Gender.getRandom();
					model = new SBiped( new AdultPersonResource(
							gender,
							BaseSkinTone.getRandom(),
							BaseEyeColor.getRandom(),
							HairManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
							0.5,
							FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender )
							) );
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
		SModel model = scene.getModel();
		if( model instanceof SBiped ) {
			SBiped biped = (SBiped)model;
			warmUp( biped );
		} else if( model instanceof SQuadruped ) {
			SQuadruped quadruped = (SQuadruped)model;
			warmUp( quadruped );
		} else if( model instanceof SFlyer ) {
			SFlyer flyer = (SFlyer)model;
			warmUp( flyer );
		}
	}

	private void warmUp( SFlyer flyer ) {
		final SJoint rightKnee = flyer.getRightKnee();
		final SJoint rightHip = flyer.getRightHip();
		final SJoint leftKnee = flyer.getLeftKnee();
		final SJoint leftHip = flyer.getLeftHip();
		final SJoint rightShoulder = flyer.getRightWingShoulder();
		final SJoint rightElbow = flyer.getRightWingElbow();
		final SJoint leftShoulder = flyer.getLeftWingShoulder();
		final SJoint leftElbow = flyer.getLeftWingElbow();
		final SJoint head = flyer.getHead();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				head.turn( TurnDirection.FORWARD, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.BACKWARD, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.LEFT, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.RIGHT, .12, Turn.duration( 0.5 ) );
			}
		}, "head" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftShoulder.turn( TurnDirection.BACKWARD, 0.12, Turn.duration( 0.5 ) );
				leftShoulder.turn( TurnDirection.FORWARD, 0.25 );
				leftShoulder.turn( TurnDirection.BACKWARD, 0.25 );
				leftShoulder.turn( TurnDirection.FORWARD, 0.12, Turn.duration( 0.5 ) );
			}
		}, "leftArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightShoulder.turn( TurnDirection.BACKWARD, 0.12, Turn.duration( 0.5 ) );
				rightShoulder.turn( TurnDirection.FORWARD, 0.25 );
				rightShoulder.turn( TurnDirection.BACKWARD, 0.25 );
				rightShoulder.turn( TurnDirection.FORWARD, 0.12, Turn.duration( 0.5 ) );
			}
		}, "rightArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightHip.turn( TurnDirection.RIGHT, 0.15 );
				rightHip.turn( TurnDirection.LEFT, 0.15 );
				rightHip.turn( TurnDirection.BACKWARD, 0.25 );
				rightKnee.turn( TurnDirection.FORWARD, 0.25 );
				rightKnee.turn( TurnDirection.BACKWARD, 0.25 );
				rightHip.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "leftLeg" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftHip.turn( TurnDirection.LEFT, 0.15 );
				leftHip.turn( TurnDirection.RIGHT, 0.15 );
				leftHip.turn( TurnDirection.BACKWARD, 0.25 );
				leftKnee.turn( TurnDirection.FORWARD, 0.25 );
				leftKnee.turn( TurnDirection.BACKWARD, 0.25 );
				leftHip.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "rightLeg" ).start();

	}

	private void warmUp( SQuadruped quadruped ) {
		final SJoint rightHip = quadruped.getBackRightHip();
		final SJoint leftHip = quadruped.getBackLeftHip();
		final SJoint leftKnee = quadruped.getBackLeftKnee();
		final SJoint rightKnee = quadruped.getBackRightKnee();
		final SJoint rightShoulder = quadruped.getFrontRightShoulder();
		final SJoint leftShoulder = quadruped.getFrontLeftShoulder();
		final SJoint leftElbow = quadruped.getFrontLeftKnee();
		final SJoint rightElbow = quadruped.getFrontRightKnee();
		final SJoint neck = quadruped.getNeck();
		final SJoint head = quadruped.getHead();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				neck.turn( TurnDirection.FORWARD, 0.25 );
				neck.turn( TurnDirection.BACKWARD, 0.25 );
				head.turn( TurnDirection.FORWARD, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.BACKWARD, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.LEFT, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.RIGHT, .12, Turn.duration( 0.5 ) );
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

	private void warmUp( SBiped biped ) {
		final SJoint leftShoulder = biped.getLeftShoulder();
		final SJoint rightShoulder = biped.getRightShoulder();
		final SJoint rightElbow = biped.getRightElbow();
		final SJoint leftElbow = biped.getLeftElbow();
		final SJoint rightHip = biped.getRightHip();
		final SJoint leftHip = biped.getLeftHip();
		final SJoint rightKnee = biped.getRightKnee();
		final SJoint head = biped.getHead();
		final SJoint leftKnee = biped.getLeftKnee();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				head.turn( TurnDirection.FORWARD, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.BACKWARD, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.LEFT, .12, Turn.duration( 0.5 ) );
				head.turn( TurnDirection.RIGHT, .12, Turn.duration( 0.5 ) );
			}
		}, "head" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftShoulder.turn( TurnDirection.BACKWARD, 0.40 );
				leftShoulder.turn( TurnDirection.FORWARD, 0.40 );
				leftShoulder.turn( TurnDirection.RIGHT, 0.25, Turn.duration( 0.5 ) );
				leftElbow.turn( TurnDirection.RIGHT, 0.25, Turn.duration( 0.5 ) );
				leftElbow.turn( TurnDirection.BACKWARD, 0.25 );
				leftElbow.turn( TurnDirection.FORWARD, 0.25 );
				leftElbow.turn( TurnDirection.LEFT, 0.25, Turn.duration( 0.5 ) );
				leftShoulder.turn( TurnDirection.LEFT, 0.25, Turn.duration( 0.5 ) );
			}
		}, "leftArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightShoulder.turn( TurnDirection.BACKWARD, 0.40 );
				rightShoulder.turn( TurnDirection.FORWARD, 0.40 );
				rightShoulder.turn( TurnDirection.LEFT, 0.25, Turn.duration( 0.5 ) );
				rightElbow.turn( TurnDirection.LEFT, 0.25, Turn.duration( 0.5 ) );
				rightElbow.turn( TurnDirection.BACKWARD, 0.25 );
				rightElbow.turn( TurnDirection.FORWARD, 0.25 );
				rightElbow.turn( TurnDirection.RIGHT, 0.25, Turn.duration( 0.5 ) );
				rightShoulder.turn( TurnDirection.RIGHT, 0.25, Turn.duration( 0.5 ) );
			}
		}, "rightArm" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				rightHip.turn( TurnDirection.RIGHT, 0.15 );
				rightHip.turn( TurnDirection.LEFT, 0.15 );
				rightHip.turn( TurnDirection.BACKWARD, 0.25 );
				rightKnee.turn( TurnDirection.FORWARD, 0.25 );
				rightKnee.turn( TurnDirection.BACKWARD, 0.25 );
				rightHip.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "leftLeg" ).start();
		new org.lgna.common.ComponentThread( new Runnable() {
			public void run() {
				leftHip.turn( TurnDirection.LEFT, 0.15 );
				leftHip.turn( TurnDirection.RIGHT, 0.15 );
				leftHip.turn( TurnDirection.BACKWARD, 0.25 );
				leftKnee.turn( TurnDirection.FORWARD, 0.25 );
				leftKnee.turn( TurnDirection.BACKWARD, 0.25 );
				leftHip.turn( TurnDirection.FORWARD, 0.25 );
			}
		}, "rightLeg" ).start();
	}

	private void initializeTest() {
		this.setActiveScene( this.scene );
		this.modelManipulationDragAdapter.setOnscreenLookingGlass( org.lgna.story.EmployeesOnly.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.setOnscreenLookingGlass( org.lgna.story.EmployeesOnly.getImplementation( this ).getOnscreenLookingGlass() );
		this.cameraNavigationDragAdapter.requestTarget( new edu.cmu.cs.dennisc.math.Point3( 0.0, 1.0, 0.0 ) );
		this.cameraNavigationDragAdapter.requestDistance( 8.0 );

		org.alice.stageide.modelresource.ClassHierarchyBasedResourceNodeTreeState.getInstance().addValueListener( this.galleryListener );

		Logger.todo( "remove ogre" );
		this.scene.setModel( this.ogre );
	}

	public static void main( String[] args ) {
		Logger.setLevel( Level.WARNING );
		ResourceNode.ACCEPTABLE_HACK_FOR_GALLERY_QA_setLeftClickModelAlwaysNull( true );
		GalleryApplication app = new GalleryApplication();
		app.initialize( args );
		app.getFrame().setMainComposite( new gallery.croquet.GallerySplitComposite() );
		GalleryProgram program = new GalleryProgram();

		new test.ik.croquet.SceneComposite().getView().initializeInAwtContainer( program );
		program.initializeTest();

		app.getFrame().setSize( 1200, 800 );
		app.getFrame().setVisible( true );

		org.alice.stageide.modelresource.ClassHierarchyBasedResourceNodeTreeState treeState = org.alice.stageide.modelresource.ClassHierarchyBasedResourceNodeTreeState.getInstance();
		treeState.setValueTransactionlessly( treeState.getChildren( treeState.getTreeModel().getRoot() ).get( 0 ) );
	}
}
