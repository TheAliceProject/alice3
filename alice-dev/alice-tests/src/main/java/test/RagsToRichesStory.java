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
package test;

import java.awt.Component;

import org.lgna.story.Color;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.Move;
import org.lgna.story.MoveDirection;
import org.lgna.story.RollDirection;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SCone;
import org.lgna.story.SGround;
import org.lgna.story.SProgram;
import org.lgna.story.SProp;
import org.lgna.story.SScene;
import org.lgna.story.SSphere;
import org.lgna.story.SSun;
import org.lgna.story.SThing;
import org.lgna.story.TurnDirection;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitAmbulanceDriver;
import org.lgna.story.resources.sims2.FemaleAdultHairBraids;
import org.lgna.story.resources.sims2.Gender;

import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

class MyBiped extends SBiped {
	public MyBiped( BipedResource resource ) {
		super( resource );
	}
}

class MyOgre extends MyBiped {

	public MyOgre( org.lgna.story.resources.biped.OgreResource resource ) {
		super( resource );
	}

}

class MyArmoire extends SProp {
	public MyArmoire() {
		super( org.lgna.story.resources.prop.ArmoireResource.LOFT_DARK_WOOD_BLACK_TRIM );
	}

	public org.lgna.story.SJoint getLeftDoor() {
		return this.getJoint( org.lgna.story.resources.prop.ArmoireResource.LEFT_DOOR );
	}

	public org.lgna.story.SJoint getRightDoor() {
		return this.getJoint( org.lgna.story.resources.prop.ArmoireResource.RIGHT_DOOR );
	}
}

class DesertScene extends SScene {
	private final SSun sun = new SSun();
	private final SGround desert = new SGround();
	private final SSphere sphere = new SSphere();
	private final MyBiped fellowLaborer = new MyBiped( org.lgna.story.resources.biped.OgreResource.BROWN );
	private final org.lgna.story.SBillboard billboard = new org.lgna.story.SBillboard();
	private final SCamera camera;
	private final MyOgre ogre;

	public DesertScene( SCamera camera, MyOgre ogre ) {
		this.camera = camera;
		this.ogre = ogre;
	}

	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
		this.desert.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.sphere.setVehicle( this );
		this.ogre.setVehicle( this );
		this.fellowLaborer.setVehicle( this );

		this.billboard.setVehicle( this );
		this.billboard.setPaint( Color.RED );
		this.billboard.setBackPaint( Color.BLUE );

		this.ogre.move( MoveDirection.RIGHT, 1.0 );
		this.fellowLaborer.move( MoveDirection.RIGHT, 1.0 );

		this.desert.setPaint( SGround.SurfaceAppearance.SAND );
		this.sphere.setRadius( 0.1 );
		this.sphere.setPaint( Color.RED );
		this.camera.moveAndOrientToAGoodVantagePointOf( this.sphere );
	}

	private void performCustomSetup() {
	}

	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
			} else {
				this.restoreStateAndEventListeners();
			}
		} else {
			this.preserveStateAndEventListeners();
		}
	}

	public void turnBigRocksIntoLittleRocks() {
		this.ogre.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
		this.ogre.getRightElbow().turn( TurnDirection.FORWARD, 0.25 );
	}
}

class SnowScene extends SScene {
	private final SSun sun = new SSun();
	private final SGround snow = new SGround();
	private final SCone redCone = new SCone();
	private final SCone greenCone = new SCone();
	private final SCone blueCone = new SCone();
	private final MyArmoire armoire = new MyArmoire();
	private final SCamera camera;
	private final MyOgre ogre;
	private final MyBiped susan;

	public SnowScene( SCamera camera, MyOgre ogre, MyBiped susan ) {
		this.camera = camera;
		this.susan = susan;
		this.ogre = ogre;
	}

	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
		this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.redCone.setVehicle( this );
		this.greenCone.setVehicle( this );
		this.blueCone.setVehicle( this );
		//		this.armoire.setVehicle( this );
		this.camera.setVehicle( this );
		this.susan.setVehicle( this );
		this.ogre.setVehicle( this );
		//		this.susan.turn( TurnDirection.LEFT, .25 );
		this.ogre.getRightElbow().turn( TurnDirection.LEFT, .25 );
		this.ogre.move( MoveDirection.BACKWARD, 1 );
		this.ogre.move( MoveDirection.RIGHT, 1 );

		this.redCone.setPaint( Color.RED );
		this.greenCone.setPaint( Color.GREEN );
		this.blueCone.setPaint( Color.BLUE );
		this.redCone.setBaseRadius( 0.1 );
		this.greenCone.setBaseRadius( 0.1 );
		this.blueCone.setBaseRadius( 0.1 );
		this.redCone.setLength( 0.25 );
		this.greenCone.setLength( 0.25 );
		this.blueCone.setLength( 0.25 );

		this.redCone.move( MoveDirection.LEFT, 0.5 );
		this.greenCone.move( MoveDirection.LEFT, 1.0 );
		this.blueCone.move( MoveDirection.LEFT, 1.5 );

		//		this.armoire.move( MoveDirection.BACKWARD, 2.0 );

		this.ogre.move( MoveDirection.LEFT, 2.0 );
		//		this.susan.turn( TurnDirection.LEFT, 0.25 );
		this.snow.setPaint( SGround.SurfaceAppearance.SNOW );
		this.camera.moveAndOrientToAGoodVantagePointOf( this.ogre );
	}

	private void performCustomSetup() {
	}

	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
				this.performInitializeEvents();
			} else {
				this.restoreStateAndEventListeners();
			}
		} else {
			this.preserveStateAndEventListeners();
		}
	}

	private void performInitializeEvents() {
		this.addSceneActivationListener( new SceneActivationListener() {
			@Override
			public void sceneActivated( SceneActivationEvent e ) {
			}
		} );
		SThing[] groupOne = { ogre };
		SThing[] groupTwo = { susan };
		this.addDefaultModelManipulation();
		this.addMouseClickOnScreenListener( new MouseClickOnScreenListener() {

			@Override
			public void mouseClicked( org.lgna.story.event.MouseClickOnScreenEvent event ) {
				susan.walkTo( ogre );
				ogre.touch( redCone );
			}
		} );
		this.addKeyPressListener( new KeyPressListener() {

			@Override
			public void keyPressed( KeyEvent e ) {
				susan.straightenOutJoints();
			}
		} );
		this.addCollisionStartListener( new CollisionStartListener() {

			@Override
			public void collisionStarted( StartCollisionEvent e ) {
				System.out.println( e.getModels()[ 0 ] );
				System.out.println( e.getModels()[ 1 ] );
			}
		}, groupOne, groupTwo );

		//		this.addCollisionStartListener( new CollisionStartListener() {
		//
		//			public void collisionStarted( StartCollisionEvent e ) {
		//				System.out.println( e.getModels()[ 0 ] );
		//				System.out.println( e.getModels()[ 1 ] );
		//			}
		//		}, groupOne, groupTwo );
		//		this.addKeyPressListener( new KeyPressListener() {
		//
		//			public void keyPressed( KeyEvent e ) {
		//				susan.turn( TurnDirection.RIGHT, .1, new Duration( .1 ) );
		//			}
		//		}, AddKeyPressListener.heldKeyPolicy( HeldKeyPolicy.FIRE_MULTIPLE ) );
		addObjectMoverFor( ogre );
	}

	int zero = 0;

	public void chillInSkiChalet() {
		while( 1 < zero ) {
			//			this.susan.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
			//			this.susan.getLeftKnee().turn( TurnDirection.BACKWARD, 0.25 );
			this.ogre.delay( 1 );
			//			this.armoire.setResource(org.lgna.story.resources.prop.ArmoireResource.LOFT_BLACK_TRIM);
			//			this.ogre.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
			//			this.ogre.setResource(org.lgna.story.resources.biped.AlienResource.ALIEN);
			//			this.ogre.delay(1);
			//			this.armoire.setResource(org.lgna.story.resources.prop.ArmoireResource.LOFT_BLACK_TRIM);
			this.ogre.getRightShoulder().turn( TurnDirection.LEFT, 0.25 );
			org.lgna.common.ThreadUtilities.doTogether( new Runnable() {
				@Override
				public void run() {
					SnowScene.this.redCone.move( MoveDirection.UP, 1.0 );
				}
			}, new Runnable() {
				@Override
				public void run() {
					SnowScene.this.greenCone.move( MoveDirection.UP, 1.0 );
				}
			}, new Runnable() {
				@Override
				public void run() {
					SnowScene.this.blueCone.move( MoveDirection.UP, 1.0 );
				}
			} );
			this.redCone.move( MoveDirection.DOWN, 1.0, Move.duration( 0.333 ) );
			this.greenCone.move( MoveDirection.DOWN, 1.0, Move.duration( 0.333 ) );
			this.blueCone.move( MoveDirection.DOWN, 1.0, Move.duration( 0.333 ) );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class RagsToRichesStory extends SProgram {

	private final SCamera camera = new SCamera();
	private final MyBiped susan = new MyBiped( new AdultPersonResource( Gender.FEMALE, BaseSkinTone.getRandom(),

			BaseEyeColor.getRandom(), FemaleAdultHairBraids.BLACK, 0.5, FemaleAdultFullBodyOutfitAmbulanceDriver.BLUE ) );
	private final MyOgre ogre = new MyOgre( org.lgna.story.resources.biped.OgreResource.GREEN );
	private final DesertScene desertScene = new DesertScene( camera, ogre );
	private final SnowScene snowScene = new SnowScene( camera, ogre, susan );

	public void playOutStory() {
		//		this.setActiveScene( this.desertScene );
		//		this.desertScene.turnBigRocksIntoLittleRocks();
		//		org.lgna.story.implementation.JointedModelImp<?, ?> susanImp = EmployeesOnly.getImplementation( susan );
		//		susanImp.opacity.setValue( 0.25f );
		//		susanImp.showVisualization();
		//		org.lgna.story.implementation.JointedModelImp<?, ?> ogreImp = EmployeesOnly.getImplementation( ogre );
		//		ogreImp.opacity.setValue( 0.25f );
		//		ogreImp.showVisualization();
		this.setActiveScene( this.snowScene );
		this.snowScene.chillInSkiChalet();
	}

	public static void main( final String[] args ) {
		final RagsToRichesStory ragsToRichesStory = new RagsToRichesStory();
		ragsToRichesStory.initializeInFrame( args );

		new Thread() {
			@Override
			public void run() {
				ragsToRichesStory.playOutStory();
			}
		}.start();
		Logger.todo( "remove this EPIC HACK" );
		new Thread() {
			@Override
			public void run() {
				ThreadUtilities.sleep( 250 );
				Component awtComponent = EmployeesOnly.getImplementation( ragsToRichesStory ).getOnscreenRenderTarget().getAwtComponent();
				edu.cmu.cs.dennisc.java.awt.ComponentUtilities.getRootFrame( awtComponent ).setSize( 1024, 768 );
			}
		}.start();

	}
}
